package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.ForwardingFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractScheduledService implements Service {
   private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
   private final AbstractService delegate = new AbstractService() {
      private volatile Future<?> runningTask;
      private volatile ScheduledExecutorService executorService;
      private final ReentrantLock lock = new ReentrantLock();
      private final Runnable task = new Runnable() {
         public void run() {
            lock.lock();

            try {
               AbstractScheduledService.this.runOneIteration();
            } catch (Throwable var8) {
               try {
                  AbstractScheduledService.this.shutDown();
               } catch (Exception var7) {
                  AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", var7);
               }

               notifyFailed(var8);
               throw Throwables.propagate(var8);
            } finally {
               lock.unlock();
            }

         }
      };

      protected final void doStart() {
         this.executorService = MoreExecutors.renamingDecorator(AbstractScheduledService.this.executor(), new Supplier() {
            public String get() {
               return AbstractScheduledService.this.serviceName() + " " + state();
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object get() {
               return this.get();
            }
         });
         this.executorService.execute(new Runnable() {
            public void run() {
               lock.lock();

               try {
                  AbstractScheduledService.this.startUp();
                  runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, executorService, task);
                  notifyStarted();
               } catch (Throwable var5) {
                  notifyFailed(var5);
                  throw Throwables.propagate(var5);
               } finally {
                  lock.unlock();
               }

            }
         });
      }

      protected final void doStop() {
         this.runningTask.cancel(false);
         this.executorService.execute(new Runnable() {
            public void run() {
               try {
                  lock.lock();

                  try {
                     if(state() != Service.State.STOPPING) {
                        return;
                     }

                     AbstractScheduledService.this.shutDown();
                  } finally {
                     lock.unlock();
                  }

                  notifyStopped();
               } catch (Throwable var5) {
                  notifyFailed(var5);
                  throw Throwables.propagate(var5);
               }
            }
         });
      }
   };

   protected AbstractScheduledService() {
   }

   protected abstract void runOneIteration() throws Exception;

   protected void startUp() throws Exception {
   }

   protected void shutDown() throws Exception {
   }

   protected abstract AbstractScheduledService.Scheduler scheduler();

   protected ScheduledExecutorService executor() {
      final ScheduledExecutorService var1 = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
         public Thread newThread(Runnable var1) {
            return MoreExecutors.newThread(AbstractScheduledService.this.serviceName(), var1);
         }
      });
      this.addListener(new Service.Listener() {
         public void terminated(Service.State var1x) {
            var1.shutdown();
         }

         public void failed(Service.State var1x, Throwable var2) {
            var1.shutdown();
         }
      }, MoreExecutors.sameThreadExecutor());
      return var1;
   }

   protected String serviceName() {
      return this.getClass().getSimpleName();
   }

   public String toString() {
      return this.serviceName() + " [" + this.state() + "]";
   }

   public final boolean isRunning() {
      return this.delegate.isRunning();
   }

   public final Service.State state() {
      return this.delegate.state();
   }

   public final void addListener(Service.Listener var1, Executor var2) {
      this.delegate.addListener(var1, var2);
   }

   public final Throwable failureCause() {
      return this.delegate.failureCause();
   }

   public final Service startAsync() {
      this.delegate.startAsync();
      return this;
   }

   public final Service stopAsync() {
      this.delegate.stopAsync();
      return this;
   }

   public final void awaitRunning() {
      this.delegate.awaitRunning();
   }

   public final void awaitRunning(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitRunning(var1, var3);
   }

   public final void awaitTerminated() {
      this.delegate.awaitTerminated();
   }

   public final void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitTerminated(var1, var3);
   }

   @Beta
   public abstract static class CustomScheduler extends AbstractScheduledService.Scheduler {
      public CustomScheduler() {
         super(null);
      }

      final Future<?> schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3) {
         AbstractScheduledService.CustomScheduler.CustomScheduler$ReschedulableCallable var4 = new AbstractScheduledService.CustomScheduler.CustomScheduler$ReschedulableCallable(var1, var2, var3);
         var4.reschedule();
         return var4;
      }

      protected abstract AbstractScheduledService.CustomScheduler.CustomScheduler$Schedule getNextSchedule() throws Exception;

      @Beta
      protected static final class CustomScheduler$Schedule {
         private final long delay;
         private final TimeUnit unit;

         public CustomScheduler$Schedule(long var1, TimeUnit var3) {
            this.delay = var1;
            this.unit = (TimeUnit)Preconditions.checkNotNull(var3);
         }
      }

      private class CustomScheduler$ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
         private final Runnable wrappedRunnable;
         private final ScheduledExecutorService executor;
         private final AbstractService service;
         private final ReentrantLock lock = new ReentrantLock();
         @GuardedBy("lock")
         private Future<Void> currentFuture;

         CustomScheduler$ReschedulableCallable(AbstractService var2, ScheduledExecutorService var3, Runnable var4) {
            this.wrappedRunnable = var4;
            this.executor = var3;
            this.service = var2;
         }

         public Void call() throws Exception {
            this.wrappedRunnable.run();
            this.reschedule();
            return null;
         }

         public void reschedule() {
            this.lock.lock();

            try {
               if(this.currentFuture == null || !this.currentFuture.isCancelled()) {
                  AbstractScheduledService.CustomScheduler.CustomScheduler$Schedule var1 = CustomScheduler.this.getNextSchedule();
                  this.currentFuture = this.executor.schedule(this, var1.delay, var1.unit);
               }
            } catch (Throwable var5) {
               this.service.notifyFailed(var5);
            } finally {
               this.lock.unlock();
            }

         }

         public boolean cancel(boolean var1) {
            this.lock.lock();

            boolean var2;
            try {
               var2 = this.currentFuture.cancel(var1);
            } finally {
               this.lock.unlock();
            }

            return var2;
         }

         protected Future<Void> delegate() {
            throw new UnsupportedOperationException("Only cancel is supported by this future");
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object call() throws Exception {
            return this.call();
         }
      }
   }

   public abstract static class Scheduler {
      public static AbstractScheduledService.Scheduler newFixedDelaySchedule(final long var0, final long var2, final TimeUnit var4) {
         return new AbstractScheduledService.Scheduler(null) {
            public Future<?> schedule(AbstractService var1, ScheduledExecutorService var2x, Runnable var3) {
               return var2x.scheduleWithFixedDelay(var3, var0, var2, var4);
            }
         };
      }

      public static AbstractScheduledService.Scheduler newFixedRateSchedule(final long var0, final long var2, final TimeUnit var4) {
         return new AbstractScheduledService.Scheduler(null) {
            public Future<?> schedule(AbstractService var1, ScheduledExecutorService var2x, Runnable var3) {
               return var2x.scheduleAtFixedRate(var3, var0, var2, var4);
            }
         };
      }

      abstract Future<?> schedule(AbstractService var1, ScheduledExecutorService var2, Runnable var3);

      private Scheduler() {
      }

      // $FF: synthetic method
      Scheduler(Object var1) {
         this();
      }
   }
}
