package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.ScheduledFutureTask;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class SingleThreadEventExecutor extends AbstractEventExecutor {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
   private static final int ST_NOT_STARTED = 1;
   private static final int ST_STARTED = 2;
   private static final int ST_SHUTTING_DOWN = 3;
   private static final int ST_SHUTDOWN = 4;
   private static final int ST_TERMINATED = 5;
   private static final Runnable WAKEUP_TASK = new Runnable() {
      public void run() {
      }
   };
   private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER;
   private final EventExecutorGroup parent;
   private final Queue<Runnable> taskQueue;
   final Queue<ScheduledFutureTask<?>> delayedTaskQueue = new PriorityQueue();
   private final Thread thread;
   private final Semaphore threadLock = new Semaphore(0);
   private final Set<Runnable> shutdownHooks = new LinkedHashSet();
   private final boolean addTaskWakesUp;
   private long lastExecutionTime;
   private volatile int state = 1;
   private volatile long gracefulShutdownQuietPeriod;
   private volatile long gracefulShutdownTimeout;
   private long gracefulShutdownStartTime;
   private final Promise<?> terminationFuture;
   private static final long SCHEDULE_PURGE_INTERVAL;

   protected SingleThreadEventExecutor(EventExecutorGroup var1, ThreadFactory var2, boolean var3) {
      this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
      if(var2 == null) {
         throw new NullPointerException("threadFactory");
      } else {
         this.parent = var1;
         this.addTaskWakesUp = var3;
         this.thread = var2.newThread(new Runnable() {
            public void run() {
               boolean var1 = false;
               SingleThreadEventExecutor.this.updateLastExecutionTime();
               boolean var112 = false;

               int var2;
               label1642: {
                  try {
                     var112 = true;
                     SingleThreadEventExecutor.this.run();
                     var1 = true;
                     var112 = false;
                     break label1642;
                  } catch (Throwable var119) {
                     SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", var119);
                     var112 = false;
                  } finally {
                     if(var112) {
                        int var10;
                        do {
                           var10 = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                        } while(var10 < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, var10, 3));

                        if(var1 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                           SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                        }

                        try {
                           while(!SingleThreadEventExecutor.this.confirmShutdown()) {
                              ;
                           }
                        } finally {
                           try {
                              SingleThreadEventExecutor.this.cleanup();
                           } finally {
                              SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                              SingleThreadEventExecutor.this.threadLock.release();
                              if(!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                                 SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                              }

                              SingleThreadEventExecutor.this.terminationFuture.setSuccess((Object)null);
                           }
                        }

                     }
                  }

                  do {
                     var2 = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
                  } while(var2 < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, var2, 3));

                  if(var1 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                     SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
                  }

                  try {
                     while(!SingleThreadEventExecutor.this.confirmShutdown()) {
                        ;
                     }

                     return;
                  } finally {
                     try {
                        SingleThreadEventExecutor.this.cleanup();
                     } finally {
                        SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                        SingleThreadEventExecutor.this.threadLock.release();
                        if(!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                           SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                        }

                        SingleThreadEventExecutor.this.terminationFuture.setSuccess((Object)null);
                     }
                  }
               }

               do {
                  var2 = SingleThreadEventExecutor.STATE_UPDATER.get(SingleThreadEventExecutor.this);
               } while(var2 < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, var2, 3));

               if(var1 && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
                  SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class.getSimpleName() + ".confirmShutdown() must be called " + "before run() implementation terminates.");
               }

               try {
                  while(!SingleThreadEventExecutor.this.confirmShutdown()) {
                     ;
                  }
               } finally {
                  try {
                     SingleThreadEventExecutor.this.cleanup();
                  } finally {
                     SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
                     SingleThreadEventExecutor.this.threadLock.release();
                     if(!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
                        SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this.taskQueue.size() + ')');
                     }

                     SingleThreadEventExecutor.this.terminationFuture.setSuccess((Object)null);
                  }
               }

            }
         });
         this.taskQueue = this.newTaskQueue();
      }
   }

   protected Queue<Runnable> newTaskQueue() {
      return new LinkedBlockingQueue();
   }

   public EventExecutorGroup parent() {
      return this.parent;
   }

   protected void interruptThread() {
      this.thread.interrupt();
   }

   protected Runnable pollTask() {
      assert this.inEventLoop();

      Runnable var1;
      do {
         var1 = (Runnable)this.taskQueue.poll();
      } while(var1 == WAKEUP_TASK);

      return var1;
   }

   protected Runnable takeTask() {
      assert this.inEventLoop();

      if(!(this.taskQueue instanceof BlockingQueue)) {
         throw new UnsupportedOperationException();
      } else {
         BlockingQueue var1 = (BlockingQueue)this.taskQueue;

         Runnable var5;
         do {
            ScheduledFutureTask var2 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
            if(var2 == null) {
               Runnable var9 = null;

               try {
                  var9 = (Runnable)var1.take();
                  if(var9 == WAKEUP_TASK) {
                     var9 = null;
                  }
               } catch (InterruptedException var7) {
                  ;
               }

               return var9;
            }

            long var3 = var2.delayNanos();
            var5 = null;
            if(var3 > 0L) {
               try {
                  var5 = (Runnable)var1.poll(var3, TimeUnit.NANOSECONDS);
               } catch (InterruptedException var8) {
                  return null;
               }
            }

            if(var5 == null) {
               this.fetchFromDelayedQueue();
               var5 = (Runnable)var1.poll();
            }
         } while(var5 == null);

         return var5;
      }
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

   protected Runnable peekTask() {
      assert this.inEventLoop();

      return (Runnable)this.taskQueue.peek();
   }

   protected boolean hasTasks() {
      assert this.inEventLoop();

      return !this.taskQueue.isEmpty();
   }

   protected boolean hasScheduledTasks() {
      assert this.inEventLoop();

      ScheduledFutureTask var1 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
      return var1 != null && var1.deadlineNanos() <= ScheduledFutureTask.nanoTime();
   }

   public final int pendingTasks() {
      return this.taskQueue.size();
   }

   protected void addTask(Runnable var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         if(this.isShutdown()) {
            reject();
         }

         this.taskQueue.add(var1);
      }
   }

   protected boolean removeTask(Runnable var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         return this.taskQueue.remove(var1);
      }
   }

   protected boolean runAllTasks() {
      this.fetchFromDelayedQueue();
      Runnable var1 = this.pollTask();
      if(var1 == null) {
         return false;
      } else {
         do {
            try {
               var1.run();
            } catch (Throwable var3) {
               logger.warn("A task raised an exception.", var3);
            }

            var1 = this.pollTask();
         } while(var1 != null);

         this.lastExecutionTime = ScheduledFutureTask.nanoTime();
         return true;
      }
   }

   protected boolean runAllTasks(long var1) {
      this.fetchFromDelayedQueue();
      Runnable var3 = this.pollTask();
      if(var3 == null) {
         return false;
      } else {
         long var4 = ScheduledFutureTask.nanoTime() + var1;
         long var6 = 0L;

         long var8;
         while(true) {
            try {
               var3.run();
            } catch (Throwable var11) {
               logger.warn("A task raised an exception.", var11);
            }

            ++var6;
            if((var6 & 63L) == 0L) {
               var8 = ScheduledFutureTask.nanoTime();
               if(var8 >= var4) {
                  break;
               }
            }

            var3 = this.pollTask();
            if(var3 == null) {
               var8 = ScheduledFutureTask.nanoTime();
               break;
            }
         }

         this.lastExecutionTime = var8;
         return true;
      }
   }

   protected long delayNanos(long var1) {
      ScheduledFutureTask var3 = (ScheduledFutureTask)this.delayedTaskQueue.peek();
      return var3 == null?SCHEDULE_PURGE_INTERVAL:var3.delayNanos(var1);
   }

   protected void updateLastExecutionTime() {
      this.lastExecutionTime = ScheduledFutureTask.nanoTime();
   }

   protected abstract void run();

   protected void cleanup() {
   }

   protected void wakeup(boolean var1) {
      if(!var1 || STATE_UPDATER.get(this) == 3) {
         this.taskQueue.add(WAKEUP_TASK);
      }

   }

   public boolean inEventLoop(Thread var1) {
      return var1 == this.thread;
   }

   public void addShutdownHook(final Runnable var1) {
      if(this.inEventLoop()) {
         this.shutdownHooks.add(var1);
      } else {
         this.execute(new Runnable() {
            public void run() {
               SingleThreadEventExecutor.this.shutdownHooks.add(var1);
            }
         });
      }

   }

   public void removeShutdownHook(final Runnable var1) {
      if(this.inEventLoop()) {
         this.shutdownHooks.remove(var1);
      } else {
         this.execute(new Runnable() {
            public void run() {
               SingleThreadEventExecutor.this.shutdownHooks.remove(var1);
            }
         });
      }

   }

   private boolean runShutdownHooks() {
      boolean var1 = false;

      while(!this.shutdownHooks.isEmpty()) {
         ArrayList var2 = new ArrayList(this.shutdownHooks);
         this.shutdownHooks.clear();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Runnable var4 = (Runnable)var3.next();

            try {
               var4.run();
            } catch (Throwable var9) {
               logger.warn("Shutdown hook raised an exception.", var9);
            } finally {
               var1 = true;
            }
         }
      }

      if(var1) {
         this.lastExecutionTime = ScheduledFutureTask.nanoTime();
      }

      return var1;
   }

   public Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5) {
      if(var1 < 0L) {
         throw new IllegalArgumentException("quietPeriod: " + var1 + " (expected >= 0)");
      } else if(var3 < var1) {
         throw new IllegalArgumentException("timeout: " + var3 + " (expected >= quietPeriod (" + var1 + "))");
      } else if(var5 == null) {
         throw new NullPointerException("unit");
      } else if(this.isShuttingDown()) {
         return this.terminationFuture();
      } else {
         boolean var6 = this.inEventLoop();

         boolean var7;
         int var8;
         int var9;
         do {
            if(this.isShuttingDown()) {
               return this.terminationFuture();
            }

            var7 = true;
            var8 = STATE_UPDATER.get(this);
            if(var6) {
               var9 = 3;
            } else {
               switch(var8) {
               case 1:
               case 2:
                  var9 = 3;
                  break;
               default:
                  var9 = var8;
                  var7 = false;
               }
            }
         } while(!STATE_UPDATER.compareAndSet(this, var8, var9));

         this.gracefulShutdownQuietPeriod = var5.toNanos(var1);
         this.gracefulShutdownTimeout = var5.toNanos(var3);
         if(var8 == 1) {
            this.thread.start();
         }

         if(var7) {
            this.wakeup(var6);
         }

         return this.terminationFuture();
      }
   }

   public Future<?> terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      if(!this.isShutdown()) {
         boolean var1 = this.inEventLoop();

         boolean var2;
         int var3;
         int var4;
         do {
            if(this.isShuttingDown()) {
               return;
            }

            var2 = true;
            var3 = STATE_UPDATER.get(this);
            if(var1) {
               var4 = 4;
            } else {
               switch(var3) {
               case 1:
               case 2:
               case 3:
                  var4 = 4;
                  break;
               default:
                  var4 = var3;
                  var2 = false;
               }
            }
         } while(!STATE_UPDATER.compareAndSet(this, var3, var4));

         if(var3 == 1) {
            this.thread.start();
         }

         if(var2) {
            this.wakeup(var1);
         }

      }
   }

   public boolean isShuttingDown() {
      return STATE_UPDATER.get(this) >= 3;
   }

   public boolean isShutdown() {
      return STATE_UPDATER.get(this) >= 4;
   }

   public boolean isTerminated() {
      return STATE_UPDATER.get(this) == 5;
   }

   protected boolean confirmShutdown() {
      if(!this.isShuttingDown()) {
         return false;
      } else if(!this.inEventLoop()) {
         throw new IllegalStateException("must be invoked from an event loop");
      } else {
         this.cancelDelayedTasks();
         if(this.gracefulShutdownStartTime == 0L) {
            this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
         }

         if(!this.runAllTasks() && !this.runShutdownHooks()) {
            long var1 = ScheduledFutureTask.nanoTime();
            if(!this.isShutdown() && var1 - this.gracefulShutdownStartTime <= this.gracefulShutdownTimeout) {
               if(var1 - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod) {
                  this.wakeup(true);

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var4) {
                     ;
                  }

                  return false;
               } else {
                  return true;
               }
            } else {
               return true;
            }
         } else if(this.isShutdown()) {
            return true;
         } else {
            this.wakeup(true);
            return false;
         }
      }
   }

   private void cancelDelayedTasks() {
      if(!this.delayedTaskQueue.isEmpty()) {
         ScheduledFutureTask[] var1 = (ScheduledFutureTask[])this.delayedTaskQueue.toArray(new ScheduledFutureTask[this.delayedTaskQueue.size()]);
         ScheduledFutureTask[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ScheduledFutureTask var5 = var2[var4];
            var5.cancel(false);
         }

         this.delayedTaskQueue.clear();
      }
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      if(var3 == null) {
         throw new NullPointerException("unit");
      } else if(this.inEventLoop()) {
         throw new IllegalStateException("cannot await termination of the current thread");
      } else {
         if(this.threadLock.tryAcquire(var1, var3)) {
            this.threadLock.release();
         }

         return this.isTerminated();
      }
   }

   public void execute(Runnable var1) {
      if(var1 == null) {
         throw new NullPointerException("task");
      } else {
         boolean var2 = this.inEventLoop();
         if(var2) {
            this.addTask(var1);
         } else {
            this.startThread();
            this.addTask(var1);
            if(this.isShutdown() && this.removeTask(var1)) {
               reject();
            }
         }

         if(!this.addTaskWakesUp && this.wakesUpForTask(var1)) {
            this.wakeup(var2);
         }

      }
   }

   protected boolean wakesUpForTask(Runnable var1) {
      return true;
   }

   protected static void reject() {
      throw new RejectedExecutionException("event executor terminated");
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
                  SingleThreadEventExecutor.this.delayedTaskQueue.add(var1);
               }
            });
         }

         return var1;
      }
   }

   private void startThread() {
      if(STATE_UPDATER.get(this) == 1 && STATE_UPDATER.compareAndSet(this, 1, 2)) {
         this.delayedTaskQueue.add(new ScheduledFutureTask(this, this.delayedTaskQueue, Executors.callable(new SingleThreadEventExecutor.PurgeTask(null), (Object)null), ScheduledFutureTask.deadlineNanos(SCHEDULE_PURGE_INTERVAL), -SCHEDULE_PURGE_INTERVAL));
         this.thread.start();
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
      AtomicIntegerFieldUpdater var0 = PlatformDependent.newAtomicIntegerFieldUpdater(SingleThreadEventExecutor.class, "state");
      if(var0 == null) {
         var0 = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
      }

      STATE_UPDATER = var0;
      SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
   }

   private final class PurgeTask implements Runnable {
      private PurgeTask() {
      }

      public void run() {
         Iterator var1 = SingleThreadEventExecutor.this.delayedTaskQueue.iterator();

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
}
