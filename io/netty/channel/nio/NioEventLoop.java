package io.netty.channel.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopException;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.nio.AbstractNioChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.nio.NioTask;
import io.netty.channel.nio.SelectedSelectionKeySet;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class NioEventLoop extends SingleThreadEventLoop {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
   private static final int CLEANUP_INTERVAL = 256;
   private static final boolean DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
   private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
   private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
   Selector selector;
   private SelectedSelectionKeySet selectedKeys;
   private final SelectorProvider provider;
   private final AtomicBoolean wakenUp = new AtomicBoolean();
   private volatile int ioRatio = 50;
   private int cancelledKeys;
   private boolean needsToSelectAgain;

   NioEventLoop(NioEventLoopGroup var1, ThreadFactory var2, SelectorProvider var3) {
      super(var1, var2, false);
      if(var3 == null) {
         throw new NullPointerException("selectorProvider");
      } else {
         this.provider = var3;
         this.selector = this.openSelector();
      }
   }

   private Selector openSelector() {
      AbstractSelector var1;
      try {
         var1 = this.provider.openSelector();
      } catch (IOException var7) {
         throw new ChannelException("failed to open a new selector", var7);
      }

      if(DISABLE_KEYSET_OPTIMIZATION) {
         return var1;
      } else {
         try {
            SelectedSelectionKeySet var2 = new SelectedSelectionKeySet();
            Class var3 = Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
            if(!var3.isAssignableFrom(var1.getClass())) {
               return var1;
            }

            Field var4 = var3.getDeclaredField("selectedKeys");
            Field var5 = var3.getDeclaredField("publicSelectedKeys");
            var4.setAccessible(true);
            var5.setAccessible(true);
            var4.set(var1, var2);
            var5.set(var1, var2);
            this.selectedKeys = var2;
            logger.trace("Instrumented an optimized java.util.Set into: {}", (Object)var1);
         } catch (Throwable var6) {
            this.selectedKeys = null;
            logger.trace("Failed to instrument an optimized java.util.Set into: {}", var1, var6);
         }

         return var1;
      }
   }

   protected Queue<Runnable> newTaskQueue() {
      return PlatformDependent.newMpscQueue();
   }

   public void register(SelectableChannel var1, int var2, NioTask<?> var3) {
      if(var1 == null) {
         throw new NullPointerException("ch");
      } else if(var2 == 0) {
         throw new IllegalArgumentException("interestOps must be non-zero.");
      } else if((var2 & ~var1.validOps()) != 0) {
         throw new IllegalArgumentException("invalid interestOps: " + var2 + "(validOps: " + var1.validOps() + ')');
      } else if(var3 == null) {
         throw new NullPointerException("task");
      } else if(this.isShutdown()) {
         throw new IllegalStateException("event loop shut down");
      } else {
         try {
            var1.register(this.selector, var2, var3);
         } catch (Exception var5) {
            throw new EventLoopException("failed to register a channel", var5);
         }
      }
   }

   public int getIoRatio() {
      return this.ioRatio;
   }

   public void setIoRatio(int var1) {
      if(var1 > 0 && var1 <= 100) {
         this.ioRatio = var1;
      } else {
         throw new IllegalArgumentException("ioRatio: " + var1 + " (expected: 0 < ioRatio <= 100)");
      }
   }

   public void rebuildSelector() {
      if(!this.inEventLoop()) {
         this.execute(new Runnable() {
            public void run() {
               NioEventLoop.this.rebuildSelector();
            }
         });
      } else {
         Selector var1 = this.selector;
         if(var1 != null) {
            Selector var2;
            try {
               var2 = this.openSelector();
            } catch (Exception var9) {
               logger.warn("Failed to create a new Selector.", (Throwable)var9);
               return;
            }

            int var3 = 0;

            label69:
            while(true) {
               try {
                  Iterator var4 = var1.keys().iterator();

                  while(true) {
                     if(!var4.hasNext()) {
                        break label69;
                     }

                     SelectionKey var5 = (SelectionKey)var4.next();
                     Object var6 = var5.attachment();

                     try {
                        if(var5.isValid() && var5.channel().keyFor(var2) == null) {
                           int var7 = var5.interestOps();
                           var5.cancel();
                           SelectionKey var14 = var5.channel().register(var2, var7, var6);
                           if(var6 instanceof AbstractNioChannel) {
                              ((AbstractNioChannel)var6).selectionKey = var14;
                           }

                           ++var3;
                        }
                     } catch (Exception var11) {
                        logger.warn("Failed to re-register a Channel to the new Selector.", (Throwable)var11);
                        if(var6 instanceof AbstractNioChannel) {
                           AbstractNioChannel var13 = (AbstractNioChannel)var6;
                           var13.unsafe().close(var13.unsafe().voidPromise());
                        } else {
                           NioTask var8 = (NioTask)var6;
                           invokeChannelUnregistered(var8, var5, var11);
                        }
                     }
                  }
               } catch (ConcurrentModificationException var12) {
                  ;
               }
            }

            this.selector = var2;

            try {
               var1.close();
            } catch (Throwable var10) {
               if(logger.isWarnEnabled()) {
                  logger.warn("Failed to close the old Selector.", var10);
               }
            }

            logger.info("Migrated " + var3 + " channel(s) to the new Selector.");
         }
      }
   }

   protected void run() {
      while(true) {
         boolean var1 = this.wakenUp.getAndSet(false);

         try {
            if(this.hasTasks()) {
               this.selectNow();
            } else {
               this.select(var1);
               if(this.wakenUp.get()) {
                  this.selector.wakeup();
               }
            }

            this.cancelledKeys = 0;
            this.needsToSelectAgain = false;
            int var2 = this.ioRatio;
            if(var2 == 100) {
               this.processSelectedKeys();
               this.runAllTasks();
            } else {
               long var3 = System.nanoTime();
               this.processSelectedKeys();
               long var5 = System.nanoTime() - var3;
               this.runAllTasks(var5 * (long)(100 - var2) / (long)var2);
            }

            if(this.isShuttingDown()) {
               this.closeAll();
               if(this.confirmShutdown()) {
                  return;
               }
            }
         } catch (Throwable var8) {
            logger.warn("Unexpected exception in the selector loop.", var8);

            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var7) {
               ;
            }
         }
      }
   }

   private void processSelectedKeys() {
      if(this.selectedKeys != null) {
         this.processSelectedKeysOptimized(this.selectedKeys.flip());
      } else {
         this.processSelectedKeysPlain(this.selector.selectedKeys());
      }

   }

   protected void cleanup() {
      try {
         this.selector.close();
      } catch (IOException var2) {
         logger.warn("Failed to close a selector.", (Throwable)var2);
      }

   }

   void cancel(SelectionKey var1) {
      var1.cancel();
      ++this.cancelledKeys;
      if(this.cancelledKeys >= 256) {
         this.cancelledKeys = 0;
         this.needsToSelectAgain = true;
      }

   }

   protected Runnable pollTask() {
      Runnable var1 = super.pollTask();
      if(this.needsToSelectAgain) {
         this.selectAgain();
      }

      return var1;
   }

   private void processSelectedKeysPlain(Set<SelectionKey> var1) {
      if(!var1.isEmpty()) {
         Iterator var2 = var1.iterator();

         while(true) {
            SelectionKey var3 = (SelectionKey)var2.next();
            Object var4 = var3.attachment();
            var2.remove();
            if(var4 instanceof AbstractNioChannel) {
               processSelectedKey(var3, (AbstractNioChannel)var4);
            } else {
               NioTask var5 = (NioTask)var4;
               processSelectedKey(var3, var5);
            }

            if(!var2.hasNext()) {
               break;
            }

            if(this.needsToSelectAgain) {
               this.selectAgain();
               var1 = this.selector.selectedKeys();
               if(var1.isEmpty()) {
                  break;
               }

               var2 = var1.iterator();
            }
         }

      }
   }

   private void processSelectedKeysOptimized(SelectionKey[] var1) {
      int var2 = 0;

      while(true) {
         SelectionKey var3 = var1[var2];
         if(var3 == null) {
            return;
         }

         var1[var2] = null;
         Object var4 = var3.attachment();
         if(var4 instanceof AbstractNioChannel) {
            processSelectedKey(var3, (AbstractNioChannel)var4);
         } else {
            NioTask var5 = (NioTask)var4;
            processSelectedKey(var3, var5);
         }

         if(this.needsToSelectAgain) {
            while(var1[var2] != null) {
               var1[var2] = null;
               ++var2;
            }

            this.selectAgain();
            var1 = this.selectedKeys.flip();
            var2 = -1;
         }

         ++var2;
      }
   }

   private static void processSelectedKey(SelectionKey var0, AbstractNioChannel var1) {
      AbstractNioChannel.NioUnsafe var2 = var1.unsafe();
      if(!var0.isValid()) {
         var2.close(var2.voidPromise());
      } else {
         try {
            int var3 = var0.readyOps();
            if((var3 & 17) != 0 || var3 == 0) {
               var2.read();
               if(!var1.isOpen()) {
                  return;
               }
            }

            if((var3 & 4) != 0) {
               var1.unsafe().forceFlush();
            }

            if((var3 & 8) != 0) {
               int var4 = var0.interestOps();
               var4 &= -9;
               var0.interestOps(var4);
               var2.finishConnect();
            }
         } catch (CancelledKeyException var5) {
            var2.close(var2.voidPromise());
         }

      }
   }

   private static void processSelectedKey(SelectionKey var0, NioTask<SelectableChannel> var1) {
      byte var2 = 0;
      boolean var7 = false;

      label91: {
         try {
            var7 = true;
            var1.channelReady(var0.channel(), var0);
            var2 = 1;
            var7 = false;
            break label91;
         } catch (Exception var8) {
            var0.cancel();
            invokeChannelUnregistered(var1, var0, var8);
            var2 = 2;
            var7 = false;
         } finally {
            if(var7) {
               switch(var2) {
               case 0:
                  var0.cancel();
                  invokeChannelUnregistered(var1, var0, (Throwable)null);
                  break;
               case 1:
                  if(!var0.isValid()) {
                     invokeChannelUnregistered(var1, var0, (Throwable)null);
                  }
               }

            }
         }

         switch(var2) {
         case 0:
            var0.cancel();
            invokeChannelUnregistered(var1, var0, (Throwable)null);
            return;
         case 1:
            if(!var0.isValid()) {
               invokeChannelUnregistered(var1, var0, (Throwable)null);
            }

            return;
         default:
            return;
         }
      }

      switch(var2) {
      case 0:
         var0.cancel();
         invokeChannelUnregistered(var1, var0, (Throwable)null);
         break;
      case 1:
         if(!var0.isValid()) {
            invokeChannelUnregistered(var1, var0, (Throwable)null);
         }
      }

   }

   private void closeAll() {
      this.selectAgain();
      Set var1 = this.selector.keys();
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         SelectionKey var4 = (SelectionKey)var3.next();
         Object var5 = var4.attachment();
         if(var5 instanceof AbstractNioChannel) {
            var2.add((AbstractNioChannel)var5);
         } else {
            var4.cancel();
            NioTask var6 = (NioTask)var5;
            invokeChannelUnregistered(var6, var4, (Throwable)null);
         }
      }

      var3 = var2.iterator();

      while(var3.hasNext()) {
         AbstractNioChannel var7 = (AbstractNioChannel)var3.next();
         var7.unsafe().close(var7.unsafe().voidPromise());
      }

   }

   private static void invokeChannelUnregistered(NioTask<SelectableChannel> var0, SelectionKey var1, Throwable var2) {
      try {
         var0.channelUnregistered(var1.channel(), var2);
      } catch (Exception var4) {
         logger.warn("Unexpected exception while running NioTask.channelUnregistered()", (Throwable)var4);
      }

   }

   protected void wakeup(boolean var1) {
      if(!var1 && this.wakenUp.compareAndSet(false, true)) {
         this.selector.wakeup();
      }

   }

   void selectNow() throws IOException {
      try {
         this.selector.selectNow();
      } finally {
         if(this.wakenUp.get()) {
            this.selector.wakeup();
         }

      }

   }

   private void select(boolean var1) throws IOException {
      Selector var2 = this.selector;

      try {
         int var3 = 0;
         long var4 = System.nanoTime();
         long var6 = var4 + this.delayNanos(var4);

         while(true) {
            long var8 = (var6 - var4 + 500000L) / 1000000L;
            if(var8 <= 0L) {
               if(var3 == 0) {
                  var2.selectNow();
                  var3 = 1;
               }
               break;
            }

            int var10 = var2.select(var8);
            ++var3;
            if(var10 != 0 || var1 || this.wakenUp.get() || this.hasTasks() || this.hasScheduledTasks()) {
               break;
            }

            if(Thread.interrupted()) {
               if(logger.isDebugEnabled()) {
                  logger.debug("Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop.");
               }

               var3 = 1;
               break;
            }

            long var11 = System.nanoTime();
            if(var11 - TimeUnit.MILLISECONDS.toNanos(var8) >= var4) {
               var3 = 1;
            } else if(SELECTOR_AUTO_REBUILD_THRESHOLD > 0 && var3 >= SELECTOR_AUTO_REBUILD_THRESHOLD) {
               logger.warn("Selector.select() returned prematurely {} times in a row; rebuilding selector.", (Object)Integer.valueOf(var3));
               this.rebuildSelector();
               var2 = this.selector;
               var2.selectNow();
               var3 = 1;
               break;
            }

            var4 = var11;
         }

         if(var3 > 3 && logger.isDebugEnabled()) {
            logger.debug("Selector.select() returned prematurely {} times in a row.", (Object)Integer.valueOf(var3 - 1));
         }
      } catch (CancelledKeyException var13) {
         if(logger.isDebugEnabled()) {
            logger.debug(CancelledKeyException.class.getSimpleName() + " raised by a Selector - JDK bug?", (Throwable)var13);
         }
      }

   }

   private void selectAgain() {
      this.needsToSelectAgain = false;

      try {
         this.selector.selectNow();
      } catch (Throwable var2) {
         logger.warn("Failed to update SelectionKeys.", var2);
      }

   }

   static {
      String var0 = "sun.nio.ch.bugLevel";

      try {
         String var1 = SystemPropertyUtil.get(var0);
         if(var1 == null) {
            System.setProperty(var0, "");
         }
      } catch (SecurityException var2) {
         if(logger.isDebugEnabled()) {
            logger.debug("Unable to get/set System Property: {}", var0, var2);
         }
      }

      int var3 = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
      if(var3 < 3) {
         var3 = 0;
      }

      SELECTOR_AUTO_REBUILD_THRESHOLD = var3;
      if(logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.noKeySetOptimization: {}", (Object)Boolean.valueOf(DISABLE_KEYSET_OPTIMIZATION));
         logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", (Object)Integer.valueOf(SELECTOR_AUTO_REBUILD_THRESHOLD));
      }

   }
}
