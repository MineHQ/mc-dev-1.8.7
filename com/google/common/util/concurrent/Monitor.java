package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
   private final boolean fair;
   private final ReentrantLock lock;
   @GuardedBy("lock")
   private Monitor.Guard activeGuards;

   public Monitor() {
      this(false);
   }

   public Monitor(boolean var1) {
      this.activeGuards = null;
      this.fair = var1;
      this.lock = new ReentrantLock(var1);
   }

   public void enter() {
      this.lock.lock();
   }

   public void enterInterruptibly() throws InterruptedException {
      this.lock.lockInterruptibly();
   }

   public boolean enter(long var1, TimeUnit var3) {
      long var4 = var3.toNanos(var1);
      ReentrantLock var6 = this.lock;
      if(!this.fair && var6.tryLock()) {
         return true;
      } else {
         long var7 = System.nanoTime() + var4;
         boolean var9 = Thread.interrupted();

         try {
            while(true) {
               try {
                  boolean var10 = var6.tryLock(var4, TimeUnit.NANOSECONDS);
                  return var10;
               } catch (InterruptedException var14) {
                  var9 = true;
                  var4 = var7 - System.nanoTime();
               }
            }
         } finally {
            if(var9) {
               Thread.currentThread().interrupt();
            }

         }
      }
   }

   public boolean enterInterruptibly(long var1, TimeUnit var3) throws InterruptedException {
      return this.lock.tryLock(var1, var3);
   }

   public boolean tryEnter() {
      return this.lock.tryLock();
   }

   public void enterWhen(Monitor.Guard var1) throws InterruptedException {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         boolean var3 = var2.isHeldByCurrentThread();
         var2.lockInterruptibly();
         boolean var4 = false;

         try {
            if(!var1.isSatisfied()) {
               this.await(var1, var3);
            }

            var4 = true;
         } finally {
            if(!var4) {
               this.leave();
            }

         }

      }
   }

   public void enterWhenUninterruptibly(Monitor.Guard var1) {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         boolean var3 = var2.isHeldByCurrentThread();
         var2.lock();
         boolean var4 = false;

         try {
            if(!var1.isSatisfied()) {
               this.awaitUninterruptibly(var1, var3);
            }

            var4 = true;
         } finally {
            if(!var4) {
               this.leave();
            }

         }

      }
   }

   public boolean enterWhen(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      long var5 = var4.toNanos(var2);
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var7 = this.lock;
         boolean var8 = var7.isHeldByCurrentThread();
         if(this.fair || !var7.tryLock()) {
            long var9 = System.nanoTime() + var5;
            if(!var7.tryLock(var2, var4)) {
               return false;
            }

            var5 = var9 - System.nanoTime();
         }

         boolean var26 = false;
         boolean var10 = true;

         boolean var11;
         try {
            var26 = var1.isSatisfied() || this.awaitNanos(var1, var5, var8);
            var10 = false;
            var11 = var26;
         } finally {
            if(!var26) {
               try {
                  if(var10 && !var8) {
                     this.signalNextWaiter();
                  }
               } finally {
                  var7.unlock();
               }
            }

         }

         return var11;
      }
   }

   public boolean enterWhenUninterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) {
      long var5 = var4.toNanos(var2);
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var7 = this.lock;
         long var8 = System.nanoTime() + var5;
         boolean var10 = var7.isHeldByCurrentThread();
         boolean var11 = Thread.interrupted();

         try {
            boolean var12;
            InterruptedException var13;
            if(this.fair || !var7.tryLock()) {
               var12 = false;

               do {
                  try {
                     var12 = var7.tryLock(var5, TimeUnit.NANOSECONDS);
                     if(!var12) {
                        boolean var28 = false;
                        return var28;
                     }
                  } catch (InterruptedException var24) {
                     var13 = var24;
                     var11 = true;
                  }

                  var5 = var8 - System.nanoTime();
               } while(!var12);
            }

            var12 = false;

            try {
               while(true) {
                  try {
                     var13 = var12 = var1.isSatisfied() || this.awaitNanos(var1, var5, var10);
                     return (boolean)var13;
                  } catch (InterruptedException var25) {
                     var11 = true;
                     var10 = false;
                     var5 = var8 - System.nanoTime();
                  }
               }
            } finally {
               if(!var12) {
                  var7.unlock();
               }

            }
         } finally {
            if(var11) {
               Thread.currentThread().interrupt();
            }

         }
      }
   }

   public boolean enterIf(Monitor.Guard var1) {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         var2.lock();
         boolean var3 = false;

         boolean var4;
         try {
            var4 = var3 = var1.isSatisfied();
         } finally {
            if(!var3) {
               var2.unlock();
            }

         }

         return var4;
      }
   }

   public boolean enterIfInterruptibly(Monitor.Guard var1) throws InterruptedException {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         var2.lockInterruptibly();
         boolean var3 = false;

         boolean var4;
         try {
            var4 = var3 = var1.isSatisfied();
         } finally {
            if(!var3) {
               var2.unlock();
            }

         }

         return var4;
      }
   }

   public boolean enterIf(Monitor.Guard var1, long var2, TimeUnit var4) {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else if(!this.enter(var2, var4)) {
         return false;
      } else {
         boolean var5 = false;

         boolean var6;
         try {
            var6 = var5 = var1.isSatisfied();
         } finally {
            if(!var5) {
               this.lock.unlock();
            }

         }

         return var6;
      }
   }

   public boolean enterIfInterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var5 = this.lock;
         if(!var5.tryLock(var2, var4)) {
            return false;
         } else {
            boolean var6 = false;

            boolean var7;
            try {
               var7 = var6 = var1.isSatisfied();
            } finally {
               if(!var6) {
                  var5.unlock();
               }

            }

            return var7;
         }
      }
   }

   public boolean tryEnterIf(Monitor.Guard var1) {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         ReentrantLock var2 = this.lock;
         if(!var2.tryLock()) {
            return false;
         } else {
            boolean var3 = false;

            boolean var4;
            try {
               var4 = var3 = var1.isSatisfied();
            } finally {
               if(!var3) {
                  var2.unlock();
               }

            }

            return var4;
         }
      }
   }

   public void waitFor(Monitor.Guard var1) throws InterruptedException {
      if(!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         if(!var1.isSatisfied()) {
            this.await(var1, true);
         }

      }
   }

   public void waitForUninterruptibly(Monitor.Guard var1) {
      if(!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         if(!var1.isSatisfied()) {
            this.awaitUninterruptibly(var1, true);
         }

      }
   }

   public boolean waitFor(Monitor.Guard var1, long var2, TimeUnit var4) throws InterruptedException {
      long var5 = var4.toNanos(var2);
      if(!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else {
         return var1.isSatisfied() || this.awaitNanos(var1, var5, true);
      }
   }

   public boolean waitForUninterruptibly(Monitor.Guard var1, long var2, TimeUnit var4) {
      long var5 = var4.toNanos(var2);
      if(!(var1.monitor == this & this.lock.isHeldByCurrentThread())) {
         throw new IllegalMonitorStateException();
      } else if(var1.isSatisfied()) {
         return true;
      } else {
         boolean var7 = true;
         long var8 = System.nanoTime() + var5;
         boolean var10 = Thread.interrupted();

         try {
            while(true) {
               try {
                  boolean var11 = this.awaitNanos(var1, var5, var7);
                  return var11;
               } catch (InterruptedException var16) {
                  var10 = true;
                  if(!var1.isSatisfied()) {
                     var7 = false;
                     var5 = var8 - System.nanoTime();
                  } else {
                     boolean var12 = true;
                     return var12;
                  }
               }
            }
         } finally {
            if(var10) {
               Thread.currentThread().interrupt();
            }

         }
      }
   }

   public void leave() {
      ReentrantLock var1 = this.lock;

      try {
         if(var1.getHoldCount() == 1) {
            this.signalNextWaiter();
         }
      } finally {
         var1.unlock();
      }

   }

   public boolean isFair() {
      return this.fair;
   }

   public boolean isOccupied() {
      return this.lock.isLocked();
   }

   public boolean isOccupiedByCurrentThread() {
      return this.lock.isHeldByCurrentThread();
   }

   public int getOccupiedDepth() {
      return this.lock.getHoldCount();
   }

   public int getQueueLength() {
      return this.lock.getQueueLength();
   }

   public boolean hasQueuedThreads() {
      return this.lock.hasQueuedThreads();
   }

   public boolean hasQueuedThread(Thread var1) {
      return this.lock.hasQueuedThread(var1);
   }

   public boolean hasWaiters(Monitor.Guard var1) {
      return this.getWaitQueueLength(var1) > 0;
   }

   public int getWaitQueueLength(Monitor.Guard var1) {
      if(var1.monitor != this) {
         throw new IllegalMonitorStateException();
      } else {
         this.lock.lock();

         int var2;
         try {
            var2 = var1.waiterCount;
         } finally {
            this.lock.unlock();
         }

         return var2;
      }
   }

   @GuardedBy("lock")
   private void signalNextWaiter() {
      for(Monitor.Guard var1 = this.activeGuards; var1 != null; var1 = var1.next) {
         if(this.isSatisfied(var1)) {
            var1.condition.signal();
            break;
         }
      }

   }

   @GuardedBy("lock")
   private boolean isSatisfied(Monitor.Guard var1) {
      try {
         return var1.isSatisfied();
      } catch (Throwable var3) {
         this.signalAllWaiters();
         throw Throwables.propagate(var3);
      }
   }

   @GuardedBy("lock")
   private void signalAllWaiters() {
      for(Monitor.Guard var1 = this.activeGuards; var1 != null; var1 = var1.next) {
         var1.condition.signalAll();
      }

   }

   @GuardedBy("lock")
   private void beginWaitingFor(Monitor.Guard var1) {
      int var2 = var1.waiterCount++;
      if(var2 == 0) {
         var1.next = this.activeGuards;
         this.activeGuards = var1;
      }

   }

   @GuardedBy("lock")
   private void endWaitingFor(Monitor.Guard var1) {
      int var2 = --var1.waiterCount;
      if(var2 == 0) {
         Monitor.Guard var3 = this.activeGuards;

         Monitor.Guard var4;
         for(var4 = null; var3 != var1; var3 = var3.next) {
            var4 = var3;
         }

         if(var4 == null) {
            this.activeGuards = var3.next;
         } else {
            var4.next = var3.next;
         }

         var3.next = null;
      }

   }

   @GuardedBy("lock")
   private void await(Monitor.Guard var1, boolean var2) throws InterruptedException {
      if(var2) {
         this.signalNextWaiter();
      }

      this.beginWaitingFor(var1);

      try {
         do {
            var1.condition.await();
         } while(!var1.isSatisfied());
      } finally {
         this.endWaitingFor(var1);
      }

   }

   @GuardedBy("lock")
   private void awaitUninterruptibly(Monitor.Guard var1, boolean var2) {
      if(var2) {
         this.signalNextWaiter();
      }

      this.beginWaitingFor(var1);

      try {
         do {
            var1.condition.awaitUninterruptibly();
         } while(!var1.isSatisfied());
      } finally {
         this.endWaitingFor(var1);
      }

   }

   @GuardedBy("lock")
   private boolean awaitNanos(Monitor.Guard var1, long var2, boolean var4) throws InterruptedException {
      if(var4) {
         this.signalNextWaiter();
      }

      this.beginWaitingFor(var1);

      boolean var5;
      try {
         while(var2 >= 0L) {
            var2 = var1.condition.awaitNanos(var2);
            if(var1.isSatisfied()) {
               var5 = true;
               return var5;
            }
         }

         var5 = false;
      } finally {
         this.endWaitingFor(var1);
      }

      return var5;
   }

   @Beta
   public abstract static class Guard {
      final Monitor monitor;
      final Condition condition;
      @GuardedBy("monitor.lock")
      int waiterCount = 0;
      @GuardedBy("monitor.lock")
      Monitor.Guard next;

      protected Guard(Monitor var1) {
         this.monitor = (Monitor)Preconditions.checkNotNull(var1, "monitor");
         this.condition = var1.lock.newCondition();
      }

      public abstract boolean isSatisfied();
   }
}
