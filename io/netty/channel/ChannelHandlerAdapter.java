package io.netty.channel;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.InternalThreadLocalMap;
import java.util.Map;

public abstract class ChannelHandlerAdapter implements ChannelHandler {
   boolean added;

   public ChannelHandlerAdapter() {
   }

   public boolean isSharable() {
      Class var1 = this.getClass();
      Map var2 = InternalThreadLocalMap.get().handlerSharableCache();
      Boolean var3 = (Boolean)var2.get(var1);
      if(var3 == null) {
         var3 = Boolean.valueOf(var1.isAnnotationPresent(ChannelHandler.Sharable.class));
         var2.put(var1, var3);
      }

      return var3.booleanValue();
   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      var1.fireExceptionCaught(var2);
   }
}
