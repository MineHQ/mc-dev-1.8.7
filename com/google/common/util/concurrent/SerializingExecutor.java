package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

final class SerializingExecutor implements Executor {
   private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
   private final Executor executor;
   @GuardedBy("internalLock")
   private final Queue<Runnable> waitQueue = new ArrayDeque();
   @GuardedBy("internalLock")
   private boolean isThreadScheduled = false;
   private final SerializingExecutor.TaskRunner taskRunner = new SerializingExecutor.TaskRunner(null);
   private final Object internalLock = new Object() {
      public String toString() {
         return "SerializingExecutor lock: " + super.toString();
      }
   };

   public SerializingExecutor(Executor var1) {
      Preconditions.checkNotNull(var1, "\'executor\' must not be null.");
      this.executor = var1;
   }

   public void execute(Runnable var1) {
      Preconditions.checkNotNull(var1, "\'r\' must not be null.");
      boolean var2 = false;
      Object var3 = this.internalLock;
      synchronized(this.internalLock) {
         this.waitQueue.add(var1);
         if(!this.isThreadScheduled) {
            this.isThreadScheduled = true;
            var2 = true;
         }
      }

      if(var2) {
         boolean var18 = true;
         boolean var13 = false;

         try {
            var13 = true;
            this.executor.execute(this.taskRunner);
            var18 = false;
            var13 = false;
         } finally {
            if(var13) {
               if(var18) {
                  Object var7 = this.internalLock;
                  synchronized(this.internalLock) {
                     this.isThreadScheduled = false;
                  }
               }

            }
         }

         if(var18) {
            Object var4 = this.internalLock;
            synchronized(this.internalLock) {
               this.isThreadScheduled = false;
            }
         }
      }

   }

   private class TaskRunner implements Runnable {
      private TaskRunner() {
      }

      public void run() {
         boolean var1 = true;

         while(true) {
            boolean var14 = false;

            try {
               var14 = true;
               Preconditions.checkState(SerializingExecutor.this.isThreadScheduled);
               Runnable var2;
               synchronized(SerializingExecutor.this.internalLock) {
                  var2 = (Runnable)SerializingExecutor.this.waitQueue.poll();
                  if(var2 == null) {
                     SerializingExecutor.this.isThreadScheduled = false;
                     var1 = false;
                     var14 = false;
                     break;
                  }
               }

               try {
                  var2.run();
               } catch (RuntimeException var17) {
                  SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + var2, var17);
               }
            } finally {
               if(var14) {
                  if(var1) {
                     synchronized(SerializingExecutor.this.internalLock) {
                        SerializingExecutor.this.isThreadScheduled = false;
                     }
                  }

               }
            }
         }

         if(var1) {
            synchronized(SerializingExecutor.this.internalLock) {
               SerializingExecutor.this.isThreadScheduled = false;
            }
         }

      }

      // $FF: synthetic method
      TaskRunner(Object var2) {
         this();
      }
   }
}
