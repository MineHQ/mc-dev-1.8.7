package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.CompleteChannelFuture;
import io.netty.util.concurrent.EventExecutor;

final class SucceededChannelFuture extends CompleteChannelFuture {
   SucceededChannelFuture(Channel var1, EventExecutor var2) {
      super(var1, var2);
   }

   public Throwable cause() {
      return null;
   }

   public boolean isSuccess() {
      return true;
   }
}
