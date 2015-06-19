package io.netty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.TypeParameterMatcher;

public abstract class SimpleChannelInboundHandler<I> extends ChannelInboundHandlerAdapter {
   private final TypeParameterMatcher matcher;
   private final boolean autoRelease;

   protected SimpleChannelInboundHandler() {
      this(true);
   }

   protected SimpleChannelInboundHandler(boolean var1) {
      this.matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
      this.autoRelease = var1;
   }

   protected SimpleChannelInboundHandler(Class<? extends I> var1) {
      this(var1, true);
   }

   protected SimpleChannelInboundHandler(Class<? extends I> var1, boolean var2) {
      this.matcher = TypeParameterMatcher.get(var1);
      this.autoRelease = var2;
   }

   public boolean acceptInboundMessage(Object var1) throws Exception {
      return this.matcher.match(var1);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      boolean var3 = true;

      try {
         if(this.acceptInboundMessage(var2)) {
            this.channelRead0(var1, var2);
         } else {
            var3 = false;
            var1.fireChannelRead(var2);
         }
      } finally {
         if(this.autoRelease && var3) {
            ReferenceCountUtil.release(var2);
         }

      }

   }

   protected abstract void channelRead0(ChannelHandlerContext var1, I var2) throws Exception;
}
