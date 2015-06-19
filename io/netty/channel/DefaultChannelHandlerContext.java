package io.netty.channel;

import io.netty.channel.AbstractChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {
   private final ChannelHandler handler;

   DefaultChannelHandlerContext(DefaultChannelPipeline var1, EventExecutorGroup var2, String var3, ChannelHandler var4) {
      super(var1, var2, var3, isInbound(var4), isOutbound(var4));
      if(var4 == null) {
         throw new NullPointerException("handler");
      } else {
         this.handler = var4;
      }
   }

   public ChannelHandler handler() {
      return this.handler;
   }

   private static boolean isInbound(ChannelHandler var0) {
      return var0 instanceof ChannelInboundHandler;
   }

   private static boolean isOutbound(ChannelHandler var0) {
      return var0 instanceof ChannelOutboundHandler;
   }
}
