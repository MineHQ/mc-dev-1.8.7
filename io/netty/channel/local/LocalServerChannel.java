package io.netty.channel.local;

import io.netty.channel.AbstractServerChannel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalChannelRegistry;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;

public class LocalServerChannel extends AbstractServerChannel {
   private final ChannelConfig config = new DefaultChannelConfig(this);
   private final Queue<Object> inboundBuffer = new ArrayDeque();
   private final Runnable shutdownHook = new Runnable() {
      public void run() {
         LocalServerChannel.this.unsafe().close(LocalServerChannel.this.unsafe().voidPromise());
      }
   };
   private volatile int state;
   private volatile LocalAddress localAddress;
   private volatile boolean acceptInProgress;

   public LocalServerChannel() {
   }

   public ChannelConfig config() {
      return this.config;
   }

   public LocalAddress localAddress() {
      return (LocalAddress)super.localAddress();
   }

   public LocalAddress remoteAddress() {
      return (LocalAddress)super.remoteAddress();
   }

   public boolean isOpen() {
      return this.state < 2;
   }

   public boolean isActive() {
      return this.state == 1;
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof SingleThreadEventLoop;
   }

   protected SocketAddress localAddress0() {
      return this.localAddress;
   }

   protected void doRegister() throws Exception {
      ((SingleThreadEventExecutor)this.eventLoop()).addShutdownHook(this.shutdownHook);
   }

   protected void doBind(SocketAddress var1) throws Exception {
      this.localAddress = LocalChannelRegistry.register(this, this.localAddress, var1);
      this.state = 1;
   }

   protected void doClose() throws Exception {
      if(this.state <= 1) {
         if(this.localAddress != null) {
            LocalChannelRegistry.unregister(this.localAddress);
            this.localAddress = null;
         }

         this.state = 2;
      }

   }

   protected void doDeregister() throws Exception {
      ((SingleThreadEventExecutor)this.eventLoop()).removeShutdownHook(this.shutdownHook);
   }

   protected void doBeginRead() throws Exception {
      if(!this.acceptInProgress) {
         Queue var1 = this.inboundBuffer;
         if(var1.isEmpty()) {
            this.acceptInProgress = true;
         } else {
            ChannelPipeline var2 = this.pipeline();

            while(true) {
               Object var3 = var1.poll();
               if(var3 == null) {
                  var2.fireChannelReadComplete();
                  return;
               }

               var2.fireChannelRead(var3);
            }
         }
      }
   }

   LocalChannel serve(LocalChannel var1) {
      final LocalChannel var2 = new LocalChannel(this, var1);
      if(this.eventLoop().inEventLoop()) {
         this.serve0(var2);
      } else {
         this.eventLoop().execute(new Runnable() {
            public void run() {
               LocalServerChannel.this.serve0(var2);
            }
         });
      }

      return var2;
   }

   private void serve0(LocalChannel var1) {
      this.inboundBuffer.add(var1);
      if(this.acceptInProgress) {
         this.acceptInProgress = false;
         ChannelPipeline var2 = this.pipeline();

         while(true) {
            Object var3 = this.inboundBuffer.poll();
            if(var3 == null) {
               var2.fireChannelReadComplete();
               break;
            }

            var2.fireChannelRead(var3);
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
}
