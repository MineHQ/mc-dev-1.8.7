package io.netty.channel.local;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannelRegistry;
import io.netty.channel.local.LocalServerChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.InternalThreadLocalMap;
import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public class LocalChannel extends AbstractChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private static final int MAX_READER_STACK_DEPTH = 8;
   private final ChannelConfig config = new DefaultChannelConfig(this);
   private final Queue<Object> inboundBuffer = new ArrayDeque();
   private final Runnable readTask = new Runnable() {
      public void run() {
         ChannelPipeline var1 = LocalChannel.this.pipeline();

         while(true) {
            Object var2 = LocalChannel.this.inboundBuffer.poll();
            if(var2 == null) {
               var1.fireChannelReadComplete();
               return;
            }

            var1.fireChannelRead(var2);
         }
      }
   };
   private final Runnable shutdownHook = new Runnable() {
      public void run() {
         LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
      }
   };
   private volatile int state;
   private volatile LocalChannel peer;
   private volatile LocalAddress localAddress;
   private volatile LocalAddress remoteAddress;
   private volatile ChannelPromise connectPromise;
   private volatile boolean readInProgress;
   private volatile boolean registerInProgress;

   public LocalChannel() {
      super((Channel)null);
   }

   LocalChannel(LocalServerChannel var1, LocalChannel var2) {
      super(var1);
      this.peer = var2;
      this.localAddress = var1.localAddress();
      this.remoteAddress = var2.localAddress();
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public ChannelConfig config() {
      return this.config;
   }

   public LocalServerChannel parent() {
      return (LocalServerChannel)super.parent();
   }

   public LocalAddress localAddress() {
      return (LocalAddress)super.localAddress();
   }

   public LocalAddress remoteAddress() {
      return (LocalAddress)super.remoteAddress();
   }

   public boolean isOpen() {
      return this.state < 3;
   }

   public boolean isActive() {
      return this.state == 2;
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new LocalChannel.LocalUnsafe(null);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof SingleThreadEventLoop;
   }

   protected SocketAddress localAddress0() {
      return this.localAddress;
   }

   protected SocketAddress remoteAddress0() {
      return this.remoteAddress;
   }

   protected void doRegister() throws Exception {
      if(this.peer != null && this.parent() != null) {
         final LocalChannel var1 = this.peer;
         this.registerInProgress = true;
         this.state = 2;
         var1.remoteAddress = this.parent().localAddress();
         var1.state = 2;
         var1.eventLoop().execute(new Runnable() {
            public void run() {
               LocalChannel.this.registerInProgress = false;
               var1.pipeline().fireChannelActive();
               var1.connectPromise.setSuccess();
            }
         });
      }

      ((SingleThreadEventExecutor)this.eventLoop()).addShutdownHook(this.shutdownHook);
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.localAddress = LocalChannelRegistry.register(this, this.localAddress, var1);
      this.state = 1;
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      if(this.state <= 2) {
         if(this.localAddress != null) {
            if(this.parent() == null) {
               LocalChannelRegistry.unregister(this.localAddress);
            }

            this.localAddress = null;
         }

         this.state = 3;
      }

      final LocalChannel var1 = this.peer;
      if(var1 != null && var1.isActive()) {
         EventLoop var2 = var1.eventLoop();
         if(var2.inEventLoop() && !this.registerInProgress) {
            var1.unsafe().close(this.unsafe().voidPromise());
         } else {
            var1.eventLoop().execute(new Runnable() {
               public void run() {
                  var1.unsafe().close(LocalChannel.this.unsafe().voidPromise());
               }
            });
         }

         this.peer = null;
      }

   }

   protected void doDeregister() throws Exception {
      ((SingleThreadEventExecutor)this.eventLoop()).removeShutdownHook(this.shutdownHook);
   }

   protected void doBeginRead() throws Exception {
      if(!this.readInProgress) {
         ChannelPipeline var1 = this.pipeline();
         Queue var2 = this.inboundBuffer;
         if(var2.isEmpty()) {
            this.readInProgress = true;
         } else {
            InternalThreadLocalMap var3 = InternalThreadLocalMap.get();
            Integer var4 = Integer.valueOf(var3.localChannelReaderStackDepth());
            if(var4.intValue() < 8) {
               var3.setLocalChannelReaderStackDepth(var4.intValue() + 1);

               try {
                  while(true) {
                     Object var5 = var2.poll();
                     if(var5 == null) {
                        var1.fireChannelReadComplete();
                        break;
                     }

                     var1.fireChannelRead(var5);
                  }
               } finally {
                  var3.setLocalChannelReaderStackDepth(var4.intValue());
               }
            } else {
               this.eventLoop().execute(this.readTask);
            }

         }
      }
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      if(this.state < 2) {
         throw new NotYetConnectedException();
      } else if(this.state > 2) {
         throw new ClosedChannelException();
      } else {
         final LocalChannel var2 = this.peer;
         final ChannelPipeline var3 = var2.pipeline();
         EventLoop var4 = var2.eventLoop();
         if(var4 != this.eventLoop()) {
            final Object[] var7 = new Object[var1.size()];

            for(int var6 = 0; var6 < var7.length; ++var6) {
               var7[var6] = ReferenceCountUtil.retain(var1.current());
               var1.remove();
            }

            var4.execute(new Runnable() {
               public void run() {
                  Collections.addAll(var2.inboundBuffer, var7);
                  LocalChannel.finishPeerRead(var2, var3);
               }
            });
         } else {
            while(true) {
               Object var5 = var1.current();
               if(var5 == null) {
                  finishPeerRead(var2, var3);
                  break;
               }

               var2.inboundBuffer.add(var5);
               ReferenceCountUtil.retain(var5);
               var1.remove();
            }
         }

      }
   }

   private static void finishPeerRead(LocalChannel var0, ChannelPipeline var1) {
      if(var0.readInProgress) {
         var0.readInProgress = false;

         while(true) {
            Object var2 = var0.inboundBuffer.poll();
            if(var2 == null) {
               var1.fireChannelReadComplete();
               break;
            }

            var1.fireChannelRead(var2);
         }
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketAddress remoteAddress() {
      return this.remoteAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SocketAddress localAddress() {
      return this.localAddress();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Channel parent() {
      return this.parent();
   }

   private class LocalUnsafe extends AbstractChannel.AbstractUnsafe {
      private LocalUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if(var3.setUncancellable() && this.ensureOpen(var3)) {
            if(LocalChannel.this.state == 2) {
               AlreadyConnectedException var7 = new AlreadyConnectedException();
               this.safeSetFailure(var3, var7);
               LocalChannel.this.pipeline().fireExceptionCaught(var7);
            } else if(LocalChannel.this.connectPromise != null) {
               throw new ConnectionPendingException();
            } else {
               LocalChannel.this.connectPromise = var3;
               if(LocalChannel.this.state != 1 && var2 == null) {
                  var2 = new LocalAddress(LocalChannel.this);
               }

               if(var2 != null) {
                  try {
                     LocalChannel.this.doBind((SocketAddress)var2);
                  } catch (Throwable var6) {
                     this.safeSetFailure(var3, var6);
                     this.close(this.voidPromise());
                     return;
                  }
               }

               Channel var4 = LocalChannelRegistry.get(var1);
               if(!(var4 instanceof LocalServerChannel)) {
                  ChannelException var8 = new ChannelException("connection refused");
                  this.safeSetFailure(var3, var8);
                  this.close(this.voidPromise());
               } else {
                  LocalServerChannel var5 = (LocalServerChannel)var4;
                  LocalChannel.this.peer = var5.serve(LocalChannel.this);
               }
            }
         }
      }

      // $FF: synthetic method
      LocalUnsafe(Object var2) {
         this();
      }
   }
}
