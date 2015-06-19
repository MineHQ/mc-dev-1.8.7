package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

@ChannelHandler.Sharable
public abstract class ChannelInitializer<C extends Channel> extends ChannelInboundHandlerAdapter {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChannelInitializer.class);

   public ChannelInitializer() {
   }

   protected abstract void initChannel(C var1) throws Exception;

   public final void channelRegistered(ChannelHandlerContext var1) throws Exception {
      ChannelPipeline var2 = var1.pipeline();
      boolean var3 = false;

      try {
         this.initChannel(var1.channel());
         var2.remove((ChannelHandler)this);
         var1.fireChannelRegistered();
         var3 = true;
      } catch (Throwable var8) {
         logger.warn("Failed to initialize a channel. Closing: " + var1.channel(), var8);
      } finally {
         if(var2.context((ChannelHandler)this) != null) {
            var2.remove((ChannelHandler)this);
         }

         if(!var3) {
            var1.close();
         }

      }

   }
}
