package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenerCallQueue;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
   private static final ListenerCallQueue.Callback<Service.Listener> STARTING_CALLBACK = new ListenerCallQueue.Callback("starting()") {
      void call(Service.Listener var1) {
         var1.starting();
      }

      // $FF: synthetic method
      // $FF: bridge method
      void call(Object var1) {
         this.call((Service.Listener)var1);
      }
   };
   private static final ListenerCallQueue.Callback<Service.Listener> RUNNING_CALLBACK = new ListenerCallQueue.Callback("running()") {
      void call(Service.Listener var1) {
         var1.running();
      }

      // $FF: synthetic method
      // $FF: bridge method
      void call(Object var1) {
         this.call((Service.Listener)var1);
      }
   };
   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_STARTING_CALLBACK;
   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_RUNNING_CALLBACK;
   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_NEW_CALLBACK;
   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_RUNNING_CALLBACK;
   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_STOPPING_CALLBACK;
   private final Monitor monitor = new Monitor();
   private final Monitor.Guard isStartable;
   private final Monitor.Guard isStoppable;
   private final Monitor.Guard hasReachedRunning;
   private final Monitor.Guard isStopped;
   @GuardedBy("monitor")
   private final List<ListenerCallQueue<Service.Listener>> listeners;
   @GuardedBy("monitor")
   private volatile AbstractService.StateSnapshot snapshot;

   private static ListenerCallQueue.Callback<Service.Listener> terminatedCallback(final Service.State var0) {
      return new ListenerCallQueue.Callback("terminated({from = " + var0 + "})") {
         void call(Service.Listener var1) {
            var1.terminated(var0);
         }

         // $FF: synthetic method
         // $FF: bridge method
         void call(Object var1) {
            this.call((Service.Listener)var1);
         }
      };
   }

   private static ListenerCallQueue.Callback<Service.Listener> stoppingCallback(final Service.State var0) {
      return new ListenerCallQueue.Callback("stopping({from = " + var0 + "})") {
         void call(Service.Listener var1) {
            var1.stopping(var0);
         }

         // $FF: synthetic method
         // $FF: bridge method
         void call(Object var1) {
            this.call((Service.Listener)var1);
         }
      };
   }

   protected AbstractService() {
      this.isStartable = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state() == Service.State.NEW;
         }
      };
      this.isStoppable = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0;
         }
      };
      this.hasReachedRunning = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0;
         }
      };
      this.isStopped = new Monitor.Guard(this.monitor) {
         public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
         }
      };
      this.listeners = Collections.synchronizedList(new ArrayList());
      this.snapshot = new AbstractService.StateSnapshot(Service.State.NEW);
   }

   protected abstract void doStart();

   protected abstract void doStop();

   public final Service startAsync() {
      if(this.monitor.enterIf(this.isStartable)) {
         try {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING);
            this.starting();
            this.doStart();
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.executeListeners();
         }

         return this;
      } else {
         throw new IllegalStateException("Service " + this + " has already been started");
      }
   }

   public final Service stopAsync() {
      if(this.monitor.enterIf(this.isStoppable)) {
         try {
            Service.State var1 = this.state();
            switch(AbstractService.SyntheticClass_1.$SwitchMap$com$google$common$util$concurrent$Service$State[var1.ordinal()]) {
            case 1:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
               this.terminated(Service.State.NEW);
               break;
            case 2:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING, true, (Throwable)null);
               this.stopping(Service.State.STARTING);
               break;
            case 3:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
               this.stopping(Service.State.RUNNING);
               this.doStop();
               break;
            case 4:
            case 5:
            case 6:
               throw new AssertionError("isStoppable is incorrectly implemented, saw: " + var1);
            default:
               throw new AssertionError("Unexpected state: " + var1);
            }
         } catch (Throwable var5) {
            this.notifyFailed(var5);
         } finally {
            this.monitor.leave();
            this.executeListeners();
         }
      }

      return this;
   }

   public final void awaitRunning() {
      this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);

      try {
         this.checkCurrentState(Service.State.RUNNING);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitRunning(long var1, TimeUnit var3) throws TimeoutException {
      if(this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, var1, var3)) {
         try {
            this.checkCurrentState(Service.State.RUNNING);
         } finally {
            this.monitor.leave();
         }

      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state. " + "Current state: " + this.state());
      }
   }

   public final void awaitTerminated() {
      this.monitor.enterWhenUninterruptibly(this.isStopped);

      try {
         this.checkCurrentState(Service.State.TERMINATED);
      } finally {
         this.monitor.leave();
      }

   }

   public final void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException {
      if(this.monitor.enterWhenUninterruptibly(this.isStopped, var1, var3)) {
         try {
            this.checkCurrentState(Service.State.TERMINATED);
         } finally {
            this.monitor.leave();
         }

      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. " + "Current state: " + this.state());
      }
   }

   @GuardedBy("monitor")
   private void checkCurrentState(Service.State var1) {
      Service.State var2 = this.state();
      if(var2 != var1) {
         if(var2 == Service.State.FAILED) {
            throw new IllegalStateException("Expected the service to be " + var1 + ", but the service has FAILED", this.failureCause());
         } else {
            throw new IllegalStateException("Expected the service to be " + var1 + ", but was " + var2);
         }
      }
   }

   protected final void notifyStarted() {
      this.monitor.enter();

      try {
         if(this.snapshot.state != Service.State.STARTING) {
            IllegalStateException var1 = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
            this.notifyFailed(var1);
            throw var1;
         }

         if(this.snapshot.shutdownWhenStartupFinishes) {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
            this.doStop();
         } else {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.RUNNING);
            this.running();
         }
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   protected final void notifyStopped() {
      this.monitor.enter();

      try {
         Service.State var1 = this.snapshot.state;
         if(var1 != Service.State.STOPPING && var1 != Service.State.RUNNING) {
            IllegalStateException var2 = new IllegalStateException("Cannot notifyStopped() when the service is " + var1);
            this.notifyFailed(var2);
            throw var2;
         }

         this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
         this.terminated(var1);
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   protected final void notifyFailed(Throwable var1) {
      Preconditions.checkNotNull(var1);
      this.monitor.enter();

      try {
         Service.State var2 = this.state();
         switch(AbstractService.SyntheticClass_1.$SwitchMap$com$google$common$util$concurrent$Service$State[var2.ordinal()]) {
         case 1:
         case 5:
            throw new IllegalStateException("Failed while in state:" + var2, var1);
         case 2:
         case 3:
         case 4:
            this.snapshot = new AbstractService.StateSnapshot(Service.State.FAILED, false, var1);
            this.failed(var2, var1);
         case 6:
            break;
         default:
            throw new AssertionError("Unexpected state: " + var2);
         }
      } finally {
         this.monitor.leave();
         this.executeListeners();
      }

   }

   public final boolean isRunning() {
      return this.state() == Service.State.RUNNING;
   }

   public final Service.State state() {
      return this.snapshot.externalState();
   }

   public final Throwable failureCause() {
      return this.snapshot.failureCause();
   }

   public final void addListener(Service.Listener var1, Executor var2) {
      Preconditions.checkNotNull(var1, "listener");
      Preconditions.checkNotNull(var2, "executor");
      this.monitor.enter();

      try {
         if(!this.state().isTerminal()) {
            this.listeners.add(new ListenerCallQueue(var1, var2));
         }
      } finally {
         this.monitor.leave();
      }

   }

   public String toString() {
      return this.getClass().getSimpleName() + " [" + this.state() + "]";
   }

   private void executeListeners() {
      if(!this.monitor.isOccupiedByCurrentThread()) {
         for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
            ((ListenerCallQueue)this.listeners.get(var1)).execute();
         }
      }

   }

   @GuardedBy("monitor")
   private void starting() {
      STARTING_CALLBACK.enqueueOn(this.listeners);
   }

   @GuardedBy("monitor")
   private void running() {
      RUNNING_CALLBACK.enqueueOn(this.listeners);
   }

   @GuardedBy("monitor")
   private void stopping(Service.State var1) {
      if(var1 == Service.State.STARTING) {
         STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
      } else {
         if(var1 != Service.State.RUNNING) {
            throw new AssertionError();
         }

         STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
      }

   }

   @GuardedBy("monitor")
   private void terminated(Service.State var1) {
      switch(AbstractService.SyntheticClass_1.$SwitchMap$com$google$common$util$concurrent$Service$State[var1.ordinal()]) {
      case 1:
         TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
         break;
      case 2:
      case 5:
      case 6:
      default:
         throw new AssertionError();
      case 3:
         TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
         break;
      case 4:
         TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
      }

   }

   @GuardedBy("monitor")
   private void failed(final Service.State var1, final Throwable var2) {
      (new ListenerCallQueue.Callback("failed({from = " + var1 + ", cause = " + var2 + "})") {
         void call(Service.Listener var1x) {
            var1x.failed(var1, var2);
         }

         // $FF: synthetic method
         // $FF: bridge method
         void call(Object var1x) {
            this.call((Service.Listener)var1x);
         }
      }).enqueueOn(this.listeners);
   }

   static {
      STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(Service.State.STARTING);
      STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(Service.State.RUNNING);
      TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(Service.State.NEW);
      TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(Service.State.RUNNING);
      TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(Service.State.STOPPING);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$com$google$common$util$concurrent$Service$State = new int[Service.State.values().length];

      static {
         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.NEW.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.STARTING.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.RUNNING.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.STOPPING.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.TERMINATED.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$google$common$util$concurrent$Service$State[Service.State.FAILED.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   @Immutable
   private static final class StateSnapshot {
      final Service.State state;
      final boolean shutdownWhenStartupFinishes;
      @Nullable
      final Throwable failure;

      StateSnapshot(Service.State var1) {
         this(var1, false, (Throwable)null);
      }

      StateSnapshot(Service.State var1, boolean var2, @Nullable Throwable var3) {
         Preconditions.checkArgument(!var2 || var1 == Service.State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", new Object[]{var1});
         Preconditions.checkArgument(!(var3 != null ^ var1 == Service.State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", new Object[]{var1, var3});
         this.state = var1;
         this.shutdownWhenStartupFinishes = var2;
         this.failure = var3;
      }

      Service.State externalState() {
         return this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING?Service.State.STOPPING:this.state;
      }

      Throwable failureCause() {
         Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", new Object[]{this.state});
         return this.failure;
      }
   }
}
