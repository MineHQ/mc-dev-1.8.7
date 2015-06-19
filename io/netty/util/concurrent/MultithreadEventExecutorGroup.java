package io.netty.util.concurrent;

import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MultithreadEventExecutorGroup extends AbstractEventExecutorGroup {
   private final EventExecutor[] children;
   private final AtomicInteger childIndex = new AtomicInteger();
   private final AtomicInteger terminatedChildren = new AtomicInteger();
   private final Promise<?> terminationFuture;
   private final MultithreadEventExecutorGroup.EventExecutorChooser chooser;

   protected MultithreadEventExecutorGroup(int var1, ThreadFactory var2, Object... var3) {
      this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
      if(var1 <= 0) {
         throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", new Object[]{Integer.valueOf(var1)}));
      } else {
         if(var2 == null) {
            var2 = this.newDefaultThreadFactory();
         }

         this.children = new SingleThreadEventExecutor[var1];
         if(isPowerOfTwo(this.children.length)) {
            this.chooser = new MultithreadEventExecutorGroup.PowerOfTwoEventExecutorChooser(null);
         } else {
            this.chooser = new MultithreadEventExecutorGroup.GenericEventExecutorChooser(null);
         }

         int var6;
         for(int var4 = 0; var4 < var1; ++var4) {
            boolean var5 = false;
            boolean var17 = false;

            try {
               var17 = true;
               this.children[var4] = this.newChild(var2, var3);
               var5 = true;
               var17 = false;
            } catch (Exception var18) {
               throw new IllegalStateException("failed to create a child event loop", var18);
            } finally {
               if(var17) {
                  if(!var5) {
                     int var10;
                     for(var10 = 0; var10 < var4; ++var10) {
                        this.children[var10].shutdownGracefully();
                     }

                     for(var10 = 0; var10 < var4; ++var10) {
                        EventExecutor var11 = this.children[var10];

                        try {
                           while(!var11.isTerminated()) {
                              var11.awaitTermination(2147483647L, TimeUnit.SECONDS);
                           }
                        } catch (InterruptedException var19) {
                           Thread.currentThread().interrupt();
                           break;
                        }
                     }
                  }

               }
            }

            if(!var5) {
               for(var6 = 0; var6 < var4; ++var6) {
                  this.children[var6].shutdownGracefully();
               }

               for(var6 = 0; var6 < var4; ++var6) {
                  EventExecutor var7 = this.children[var6];

                  try {
                     while(!var7.isTerminated()) {
                        var7.awaitTermination(2147483647L, TimeUnit.SECONDS);
                     }
                  } catch (InterruptedException var21) {
                     Thread.currentThread().interrupt();
                     break;
                  }
               }
            }
         }

         FutureListener var22 = new FutureListener() {
            public void operationComplete(Future<Object> var1) throws Exception {
               if(MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
                  MultithreadEventExecutorGroup.this.terminationFuture.setSuccess((Object)null);
               }

            }
         };
         EventExecutor[] var23 = this.children;
         var6 = var23.length;

         for(int var24 = 0; var24 < var6; ++var24) {
            EventExecutor var8 = var23[var24];
            var8.terminationFuture().addListener(var22);
         }

      }
   }

   protected ThreadFactory newDefaultThreadFactory() {
      return new DefaultThreadFactory(this.getClass());
   }

   public EventExecutor next() {
      return this.chooser.next();
   }

   public Iterator<EventExecutor> iterator() {
      return this.children().iterator();
   }

   public final int executorCount() {
      return this.children.length;
   }

   protected Set<EventExecutor> children() {
      Set var1 = Collections.newSetFromMap(new LinkedHashMap());
      Collections.addAll(var1, this.children);
      return var1;
   }

   protected abstract EventExecutor newChild(ThreadFactory var1, Object... var2) throws Exception;

   public Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5) {
      EventExecutor[] var6 = this.children;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EventExecutor var9 = var6[var8];
         var9.shutdownGracefully(var1, var3, var5);
      }

      return this.terminationFuture();
   }

   public Future<?> terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         var4.shutdown();
      }

   }

   public boolean isShuttingDown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if(!var4.isShuttingDown()) {
            return false;
         }
      }

      return true;
   }

   public boolean isShutdown() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if(!var4.isShutdown()) {
            return false;
         }
      }

      return true;
   }

   public boolean isTerminated() {
      EventExecutor[] var1 = this.children;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EventExecutor var4 = var1[var3];
         if(!var4.isTerminated()) {
            return false;
         }
      }

      return true;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      long var4 = System.nanoTime() + var3.toNanos(var1);
      EventExecutor[] var6 = this.children;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EventExecutor var9 = var6[var8];

         long var10;
         do {
            var10 = var4 - System.nanoTime();
            if(var10 <= 0L) {
               return this.isTerminated();
            }
         } while(!var9.awaitTermination(var10, TimeUnit.NANOSECONDS));
      }

      return this.isTerminated();
   }

   private static boolean isPowerOfTwo(int var0) {
      return (var0 & -var0) == var0;
   }

   private final class GenericEventExecutorChooser implements MultithreadEventExecutorGroup.EventExecutorChooser {
      private GenericEventExecutorChooser() {
      }

      public EventExecutor next() {
         return MultithreadEventExecutorGroup.this.children[Math.abs(MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() % MultithreadEventExecutorGroup.this.children.length)];
      }

      // $FF: synthetic method
      GenericEventExecutorChooser(Object var2) {
         this();
      }
   }

   private final class PowerOfTwoEventExecutorChooser implements MultithreadEventExecutorGroup.EventExecutorChooser {
      private PowerOfTwoEventExecutorChooser() {
      }

      public EventExecutor next() {
         return MultithreadEventExecutorGroup.this.children[MultithreadEventExecutorGroup.this.childIndex.getAndIncrement() & MultithreadEventExecutorGroup.this.children.length - 1];
      }

      // $FF: synthetic method
      PowerOfTwoEventExecutorChooser(Object var2) {
         this();
      }
   }

   private interface EventExecutorChooser {
      EventExecutor next();
   }
}
