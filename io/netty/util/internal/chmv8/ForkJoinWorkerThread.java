package io.netty.util.internal.chmv8;

import io.netty.util.internal.chmv8.ForkJoinPool;

public class ForkJoinWorkerThread extends Thread {
   final ForkJoinPool pool;
   final ForkJoinPool.WorkQueue workQueue;

   protected ForkJoinWorkerThread(ForkJoinPool var1) {
      super("aForkJoinWorkerThread");
      this.pool = var1;
      this.workQueue = var1.registerWorker(this);
   }

   public ForkJoinPool getPool() {
      return this.pool;
   }

   public int getPoolIndex() {
      return this.workQueue.poolIndex >>> 1;
   }

   protected void onStart() {
   }

   protected void onTermination(Throwable var1) {
   }

   public void run() {
      Throwable var1 = null;

      try {
         this.onStart();
         this.pool.runWorker(this.workQueue);
      } catch (Throwable var40) {
         var1 = var40;
      } finally {
         try {
            this.onTermination(var1);
         } catch (Throwable var41) {
            if(var1 == null) {
               var1 = var41;
            }
         } finally {
            this.pool.deregisterWorker(this, var1);
         }

      }

   }
}
