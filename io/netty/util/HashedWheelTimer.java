package io.netty.util;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class HashedWheelTimer implements Timer {
   static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
   private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = new ResourceLeakDetector(HashedWheelTimer.class, 1, (long)(Runtime.getRuntime().availableProcessors() * 4));
   private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER;
   private final ResourceLeak leak;
   private final HashedWheelTimer.Worker worker;
   private final Thread workerThread;
   public static final int WORKER_STATE_INIT = 0;
   public static final int WORKER_STATE_STARTED = 1;
   public static final int WORKER_STATE_SHUTDOWN = 2;
   private volatile int workerState;
   private final long tickDuration;
   private final HashedWheelTimer.HashedWheelBucket[] wheel;
   private final int mask;
   private final CountDownLatch startTimeInitialized;
   private final Queue<HashedWheelTimer.HashedWheelTimeout> timeouts;
   private final Queue<Runnable> cancelledTimeouts;
   private volatile long startTime;

   public HashedWheelTimer() {
      this(Executors.defaultThreadFactory());
   }

   public HashedWheelTimer(long var1, TimeUnit var3) {
      this(Executors.defaultThreadFactory(), var1, var3);
   }

   public HashedWheelTimer(long var1, TimeUnit var3, int var4) {
      this(Executors.defaultThreadFactory(), var1, var3, var4);
   }

   public HashedWheelTimer(ThreadFactory var1) {
      this(var1, 100L, TimeUnit.MILLISECONDS);
   }

   public HashedWheelTimer(ThreadFactory var1, long var2, TimeUnit var4) {
      this(var1, var2, var4, 512);
   }

   public HashedWheelTimer(ThreadFactory var1, long var2, TimeUnit var4, int var5) {
      this.worker = new HashedWheelTimer.Worker();
      this.workerState = 0;
      this.startTimeInitialized = new CountDownLatch(1);
      this.timeouts = PlatformDependent.newMpscQueue();
      this.cancelledTimeouts = PlatformDependent.newMpscQueue();
      if(var1 == null) {
         throw new NullPointerException("threadFactory");
      } else if(var4 == null) {
         throw new NullPointerException("unit");
      } else if(var2 <= 0L) {
         throw new IllegalArgumentException("tickDuration must be greater than 0: " + var2);
      } else if(var5 <= 0) {
         throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + var5);
      } else {
         this.wheel = createWheel(var5);
         this.mask = this.wheel.length - 1;
         this.tickDuration = var4.toNanos(var2);
         if(this.tickDuration >= Long.MAX_VALUE / (long)this.wheel.length) {
            throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", new Object[]{Long.valueOf(var2), Long.valueOf(Long.MAX_VALUE / (long)this.wheel.length)}));
         } else {
            this.workerThread = var1.newThread(this.worker);
            this.leak = leakDetector.open(this);
         }
      }
   }

   private static HashedWheelTimer.HashedWheelBucket[] createWheel(int var0) {
      if(var0 <= 0) {
         throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + var0);
      } else if(var0 > 1073741824) {
         throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + var0);
      } else {
         var0 = normalizeTicksPerWheel(var0);
         HashedWheelTimer.HashedWheelBucket[] var1 = new HashedWheelTimer.HashedWheelBucket[var0];

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] = new HashedWheelTimer.HashedWheelBucket();
         }

         return var1;
      }
   }

   private static int normalizeTicksPerWheel(int var0) {
      int var1;
      for(var1 = 1; var1 < var0; var1 <<= 1) {
         ;
      }

      return var1;
   }

   public void start() {
      switch(WORKER_STATE_UPDATER.get(this)) {
      case 0:
         if(WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
            this.workerThread.start();
         }
      case 1:
         break;
      case 2:
         throw new IllegalStateException("cannot be started once stopped");
      default:
         throw new Error("Invalid WorkerState");
      }

      while(this.startTime == 0L) {
         try {
            this.startTimeInitialized.await();
         } catch (InterruptedException var2) {
            ;
         }
      }

   }

   public Set<Timeout> stop() {
      if(Thread.currentThread() == this.workerThread) {
         throw new IllegalStateException(HashedWheelTimer.class.getSimpleName() + ".stop() cannot be called from " + TimerTask.class.getSimpleName());
      } else if(!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
         WORKER_STATE_UPDATER.set(this, 2);
         if(this.leak != null) {
            this.leak.close();
         }

         return Collections.emptySet();
      } else {
         boolean var1 = false;

         while(this.workerThread.isAlive()) {
            this.workerThread.interrupt();

            try {
               this.workerThread.join(100L);
            } catch (InterruptedException var3) {
               var1 = true;
            }
         }

         if(var1) {
            Thread.currentThread().interrupt();
         }

         if(this.leak != null) {
            this.leak.close();
         }

         return this.worker.unprocessedTimeouts();
      }
   }

   public Timeout newTimeout(TimerTask var1, long var2, TimeUnit var4) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else if(var4 == null) {
         throw new NullPointerException("unit");
      } else {
         this.start();
         long var5 = System.nanoTime() + var4.toNanos(var2) - this.startTime;
         HashedWheelTimer.HashedWheelTimeout var7 = new HashedWheelTimer.HashedWheelTimeout(this, var1, var5);
         this.timeouts.add(var7);
         return var7;
      }
   }

   static {
      AtomicIntegerFieldUpdater var0 = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimer.class, "workerState");
      if(var0 == null) {
         var0 = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
      }

      WORKER_STATE_UPDATER = var0;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class HashedWheelBucket {
      private HashedWheelTimer.HashedWheelTimeout head;
      private HashedWheelTimer.HashedWheelTimeout tail;

      private HashedWheelBucket() {
      }

      public void addTimeout(HashedWheelTimer.HashedWheelTimeout var1) {
         assert var1.bucket == null;

         var1.bucket = this;
         if(this.head == null) {
            this.head = this.tail = var1;
         } else {
            this.tail.next = var1;
            var1.prev = this.tail;
            this.tail = var1;
         }

      }

      public void expireTimeouts(long var1) {
         HashedWheelTimer.HashedWheelTimeout var5;
         for(HashedWheelTimer.HashedWheelTimeout var3 = this.head; var3 != null; var3 = var5) {
            boolean var4 = false;
            if(var3.remainingRounds <= 0L) {
               if(var3.deadline > var1) {
                  throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", new Object[]{Long.valueOf(var3.deadline), Long.valueOf(var1)}));
               }

               var3.expire();
               var4 = true;
            } else if(var3.isCancelled()) {
               var4 = true;
            } else {
               --var3.remainingRounds;
            }

            var5 = var3.next;
            if(var4) {
               this.remove(var3);
            }
         }

      }

      public void remove(HashedWheelTimer.HashedWheelTimeout var1) {
         HashedWheelTimer.HashedWheelTimeout var2 = var1.next;
         if(var1.prev != null) {
            var1.prev.next = var2;
         }

         if(var1.next != null) {
            var1.next.prev = var1.prev;
         }

         if(var1 == this.head) {
            if(var1 == this.tail) {
               this.tail = null;
               this.head = null;
            } else {
               this.head = var2;
            }
         } else if(var1 == this.tail) {
            this.tail = var1.prev;
         }

         var1.prev = null;
         var1.next = null;
         var1.bucket = null;
      }

      public void clearTimeouts(Set<Timeout> var1) {
         while(true) {
            HashedWheelTimer.HashedWheelTimeout var2 = this.pollTimeout();
            if(var2 == null) {
               return;
            }

            if(!var2.isExpired() && !var2.isCancelled()) {
               var1.add(var2);
            }
         }
      }

      private HashedWheelTimer.HashedWheelTimeout pollTimeout() {
         HashedWheelTimer.HashedWheelTimeout var1 = this.head;
         if(var1 == null) {
            return null;
         } else {
            HashedWheelTimer.HashedWheelTimeout var2 = var1.next;
            if(var2 == null) {
               this.tail = this.head = null;
            } else {
               this.head = var2;
               var2.prev = null;
            }

            var1.next = null;
            var1.prev = null;
            var1.bucket = null;
            return var1;
         }
      }

      // $FF: synthetic method
      HashedWheelBucket(HashedWheelTimer.SyntheticClass_1 var1) {
         this();
      }
   }

   private static final class HashedWheelTimeout extends MpscLinkedQueueNode<Timeout> implements Timeout {
      private static final int ST_INIT = 0;
      private static final int ST_CANCELLED = 1;
      private static final int ST_EXPIRED = 2;
      private static final AtomicIntegerFieldUpdater<HashedWheelTimer.HashedWheelTimeout> STATE_UPDATER;
      private final HashedWheelTimer timer;
      private final TimerTask task;
      private final long deadline;
      private volatile int state = 0;
      long remainingRounds;
      HashedWheelTimer.HashedWheelTimeout next;
      HashedWheelTimer.HashedWheelTimeout prev;
      HashedWheelTimer.HashedWheelBucket bucket;

      HashedWheelTimeout(HashedWheelTimer var1, TimerTask var2, long var3) {
         this.timer = var1;
         this.task = var2;
         this.deadline = var3;
      }

      public Timer timer() {
         return this.timer;
      }

      public TimerTask task() {
         return this.task;
      }

      public boolean cancel() {
         if(!this.compareAndSetState(0, 1)) {
            return false;
         } else {
            this.timer.cancelledTimeouts.add(new Runnable() {
               public void run() {
                  HashedWheelTimer.HashedWheelBucket var1 = HashedWheelTimeout.this.bucket;
                  if(var1 != null) {
                     var1.remove(HashedWheelTimeout.this);
                  }

               }
            });
            return true;
         }
      }

      public boolean compareAndSetState(int var1, int var2) {
         return STATE_UPDATER.compareAndSet(this, var1, var2);
      }

      public int state() {
         return this.state;
      }

      public boolean isCancelled() {
         return this.state() == 1;
      }

      public boolean isExpired() {
         return this.state() == 2;
      }

      public HashedWheelTimer.HashedWheelTimeout value() {
         return this;
      }

      public void expire() {
         if(this.compareAndSetState(0, 2)) {
            try {
               this.task.run(this);
            } catch (Throwable var2) {
               if(HashedWheelTimer.logger.isWarnEnabled()) {
                  HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', var2);
               }
            }

         }
      }

      public String toString() {
         long var1 = System.nanoTime();
         long var3 = this.deadline - var1 + this.timer.startTime;
         StringBuilder var5 = new StringBuilder(192);
         var5.append(StringUtil.simpleClassName((Object)this));
         var5.append('(');
         var5.append("deadline: ");
         if(var3 > 0L) {
            var5.append(var3);
            var5.append(" ns later");
         } else if(var3 < 0L) {
            var5.append(-var3);
            var5.append(" ns ago");
         } else {
            var5.append("now");
         }

         if(this.isCancelled()) {
            var5.append(", cancelled");
         }

         var5.append(", task: ");
         var5.append(this.task());
         return var5.append(')').toString();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object value() {
         return this.value();
      }

      static {
         AtomicIntegerFieldUpdater var0 = PlatformDependent.newAtomicIntegerFieldUpdater(HashedWheelTimer.HashedWheelTimeout.class, "state");
         if(var0 == null) {
            var0 = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.HashedWheelTimeout.class, "state");
         }

         STATE_UPDATER = var0;
      }
   }

   private final class Worker implements Runnable {
      private final Set<Timeout> unprocessedTimeouts;
      private long tick;

      private Worker() {
         this.unprocessedTimeouts = new HashSet();
      }

      public void run() {
         HashedWheelTimer.this.startTime = System.nanoTime();
         if(HashedWheelTimer.this.startTime == 0L) {
            HashedWheelTimer.this.startTime = 1L;
         }

         HashedWheelTimer.this.startTimeInitialized.countDown();

         int var3;
         HashedWheelTimer.HashedWheelBucket var4;
         do {
            long var1 = this.waitForNextTick();
            if(var1 > 0L) {
               var3 = (int)(this.tick & (long)HashedWheelTimer.this.mask);
               this.processCancelledTasks();
               var4 = HashedWheelTimer.this.wheel[var3];
               this.transferTimeoutsToBuckets();
               var4.expireTimeouts(var1);
               ++this.tick;
            }
         } while(HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);

         HashedWheelTimer.HashedWheelBucket[] var5 = HashedWheelTimer.this.wheel;
         int var2 = var5.length;

         for(var3 = 0; var3 < var2; ++var3) {
            var4 = var5[var3];
            var4.clearTimeouts(this.unprocessedTimeouts);
         }

         while(true) {
            HashedWheelTimer.HashedWheelTimeout var6 = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
            if(var6 == null) {
               this.processCancelledTasks();
               return;
            }

            if(!var6.isCancelled()) {
               this.unprocessedTimeouts.add(var6);
            }
         }
      }

      private void transferTimeoutsToBuckets() {
         for(int var1 = 0; var1 < 100000; ++var1) {
            HashedWheelTimer.HashedWheelTimeout var2 = (HashedWheelTimer.HashedWheelTimeout)HashedWheelTimer.this.timeouts.poll();
            if(var2 == null) {
               break;
            }

            if(var2.state() != 1) {
               long var3 = var2.deadline / HashedWheelTimer.this.tickDuration;
               var2.remainingRounds = (var3 - this.tick) / (long)HashedWheelTimer.this.wheel.length;
               long var5 = Math.max(var3, this.tick);
               int var7 = (int)(var5 & (long)HashedWheelTimer.this.mask);
               HashedWheelTimer.HashedWheelBucket var8 = HashedWheelTimer.this.wheel[var7];
               var8.addTimeout(var2);
            }
         }

      }

      private void processCancelledTasks() {
         while(true) {
            Runnable var1 = (Runnable)HashedWheelTimer.this.cancelledTimeouts.poll();
            if(var1 == null) {
               return;
            }

            try {
               var1.run();
            } catch (Throwable var3) {
               if(HashedWheelTimer.logger.isWarnEnabled()) {
                  HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", var3);
               }
            }
         }
      }

      private long waitForNextTick() {
         long var1 = HashedWheelTimer.this.tickDuration * (this.tick + 1L);

         while(true) {
            long var3 = System.nanoTime() - HashedWheelTimer.this.startTime;
            long var5 = (var1 - var3 + 999999L) / 1000000L;
            if(var5 <= 0L) {
               if(var3 == Long.MIN_VALUE) {
                  return -9223372036854775807L;
               }

               return var3;
            }

            if(PlatformDependent.isWindows()) {
               var5 = var5 / 10L * 10L;
            }

            try {
               Thread.sleep(var5);
            } catch (InterruptedException var8) {
               if(HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
                  return Long.MIN_VALUE;
               }
            }
         }
      }

      public Set<Timeout> unprocessedTimeouts() {
         return Collections.unmodifiableSet(this.unprocessedTimeouts);
      }

      // $FF: synthetic method
      Worker(HashedWheelTimer.SyntheticClass_1 var2) {
         this();
      }
   }
}
