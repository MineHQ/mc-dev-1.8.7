package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.ScheduledFutureTask;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GlobalEventExecutor extends AbstractEventExecutor {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
   private static final long SCHEDULE_PURGE_INTERVAL;
   public static final GlobalEventExecutor INSTANCE;
   final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue();
   final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue();
   final ScheduledFutureTask<Void> purgeTask;
   private final ThreadFactory threadFactory;
   private final GlobalEventExecutor.TaskRunner taskRunner;
   private final AtomicBoolean started;
   volatile Thread thread;
   private final Future<?> terminationFuture;

   private GlobalEventExecutor() {
      this.purgeTask = new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(new GlobalEventExecutor.PurgeTask(null), (Object)null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL);
      this.threadFactory = new DefaultThreadFactory(this.getClass());
      this.taskRunner = new GlobalEventExecutor.TaskRunner();
      this.started = new AtomicBoolean();
      this.terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
      this.delayedTaskQueue.add(this.purgeTask);
   }

   public EventExecutorGroup parent() {
      return null;
   }

   Runnable takeTask() {
      BlockingQueue var1 = this.taskQueue;

      Runnable var5;
      do {
         ScheduledFutureTask var2 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
         if(var2 == null) {
            Runnable var9 = null;

            try {
               var9 = (Runnable)var1.take();
            } catch (InterruptedException var7) {
               ;
            }

            return var9;
         }

         long var3 = var2.delayNanos();
         if(var3 > 0L) {
            try {
               var5 = (Runnable)var1.poll(var3, TimeUnit.NANOSECONDS);
            } catch (InterruptedException var8) {
               return null;
            }
         } else {
            var5 = (Runnable)var1.poll();
         }

         if(var5 == null) {
            this.fetchFromDelayedQueue();
            var5 = (Runnable)var1.poll();
         }
      } while(var5 == null);

      return var5;
   }

   private void fetchFromDelayedQueue() {
      long var1 = 0L;

      while(true) {
         ScheduledFutureTask var3 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
         if(var3 == null) {
            break;
         }

         if(var1 == 0L) {
            var1 = ScheduledFutureTask.nanoTime();
         }

         if(var3.deadlineNanos() > var1) {
            break;
         }

         this.delayedTaskQueue.remove();
         this.taskQueue.add(var3);
      }

   }

   public int pendingTasks() {
      return this.taskQueue.size();
   }

   private void addTask(Runnable var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         this.taskQueue.add(var1);
      }
   }

   public boolean inEventLoop(Thread var1) {
      return var1 == this.thread;
   }

   public Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5) {
      return this.terminationFuture();
   }

   public Future<?> terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      throw new UnsupportedOperationException();
   }

   public boolean isShuttingDown() {
      return false;
   }

   public boolean isShutdown() {
      return false;
   }

   public boolean isTerminated() {
      return false;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) {
      return false;
   }

   public boolean awaitInactivity(long var1, TimeUnit var3) throws InterruptedException {
      if(var3 == null) {
         throw new NullPointerException("unit");
      } else {
         Thread var4 = this.thread;
         if(var4 == null) {
            throw new IllegalStateException("thread was not started");
         } else {
            var4.join(var3.toMillis(var1));
            return !var4.isAlive();
         }
      }
   }

   public void execute(Runnable var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         this.addTask(var1);
         if(!this.inEventLoop()) {
            this.startThread();
         }

      }
   }

   public ScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4) {
      if(var1 == null) {
         throw new NullPointerException("command");
      } else if(var4 == null) {
         throw new NullPointerException("unit");
      } else if(var2 < 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[]{Long.valueOf(var2)}));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, var1, (Object)null, ScheduledFutureTask.deadlineNanos(var4.toNanos(var2))));
      }
   }

   public <V> ScheduledFuture<V> schedule(Callable<V> var1, long var2, TimeUnit var4) {
      if(var1 == null) {
         throw new NullPointerException("callable");
      } else if(var4 == null) {
         throw new NullPointerException("unit");
      } else if(var2 < 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: >= 0)", new Object[]{Long.valueOf(var2)}));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, var1, ScheduledFutureTask.deadlineNanos(var4.toNanos(var2))));
      }
   }

   public ScheduledFuture<?> scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      if(var1 == null) {
         throw new NullPointerException("command");
      } else if(var6 == null) {
         throw new NullPointerException("unit");
      } else if(var2 < 0L) {
         throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[]{Long.valueOf(var2)}));
      } else if(var4 <= 0L) {
         throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[]{Long.valueOf(var4)}));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(var1, (Object)null), ScheduledFutureTask.deadlineNanos(var6.toNanos(var2)), var6.toNanos(var4)));
      }
   }

   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      if(var1 == null) {
         throw new NullPointerException("command");
      } else if(var6 == null) {
         throw new NullPointerException("unit");
      } else if(var2 < 0L) {
         throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[]{Long.valueOf(var2)}));
      } else if(var4 <= 0L) {
         throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[]{Long.valueOf(var4)}));
      } else {
         return this.schedule(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(var1, (Object)null), ScheduledFutureTask.deadlineNanos(var6.toNanos(var2)), -var6.toNanos(var4)));
      }
   }

   private <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         if(this.inEventLoop()) {
            this.delayedTaskQueue.add(var1);
         } else {
            this.execute(new Runnable() {
               public void run() {
                  GlobalEventExecutor.this.delayedTaskQueue.add(var1);
               }
            });
         }

         return var1;
      }
   }

   private void startThread() {
      if(this.started.compareAndSet(false, true)) {
         Thread var1 = this.threadFactory.newThread(this.taskRunner);
         var1.start();
         this.thread = var1;
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.scheduleAtFixedRate(var1, var2, var4, var6);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public java.util.concurrent.ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.schedule(var1, var2, var4);
   }

   static {
      SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
      INSTANCE = new GlobalEventExecutor();
   }

   private final class PurgeTask implements Runnable {
      private PurgeTask() {
      }

      public void run() {
         Iterator var1 = GlobalEventExecutor.this.delayedTaskQueue.iterator();

         while(var1.hasNext()) {
            ScheduledFutureTask var2 = (ScheduledFutureTask)var1.next();
            if(var2.isCancelled()) {
               var1.remove();
            }
         }

      }

      // $FF: synthetic method
      PurgeTask(Object var2) {
         this();
      }
   }

   final class TaskRunner implements Runnable {
      TaskRunner() {
      }

      public void run() {
         while(true) {
            Runnable var1 = GlobalEventExecutor.this.takeTask();
            if(var1 != null) {
               try {
                  var1.run();
               } catch (Throwable var3) {
                  GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", var3);
               }

               if(var1 != GlobalEventExecutor.this.purgeTask) {
                  continue;
               }
            }

            if(GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1) {
               boolean var2 = GlobalEventExecutor.this.started.compareAndSet(true, false);

               assert var2;

               if(GlobalEventExecutor.this.taskQueue.isEmpty() && GlobalEventExecutor.this.delayedTaskQueue.size() == 1 || !GlobalEventExecutor.this.started.compareAndSet(false, true)) {
                  return;
               }
            }
         }
      }
   }
}
