package io.netty.channel.oio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.oio.AbstractOioChannel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOioMessageChannel extends AbstractOioChannel {
   private final List<Object> readBuf = new ArrayList();

   protected AbstractOioMessageChannel(Channel var1) {
      super(var1);
   }

   protected void doRead() {
      ChannelConfig var1 = this.config();
      ChannelPipeline var2 = this.pipeline();
      boolean var3 = false;
      int var4 = var1.getMaxMessagesPerRead();
      Throwable var5 = null;
      int var6 = 0;

      try {
         do {
            var6 = this.doReadMessages(this.readBuf);
            if(var6 == 0) {
               break;
            }

            if(var6 < 0) {
               var3 = true;
               break;
            }
         } while(this.readBuf.size() < var4 && var1.isAutoRead());
      } catch (Throwable var9) {
         var5 = var9;
      }

      int var7 = this.readBuf.size();

      for(int var8 = 0; var8 < var7; ++var8) {
         var2.fireChannelRead(this.readBuf.get(var8));
      }

      this.readBuf.clear();
      var2.fireChannelReadComplete();
      if(var5 != null) {
         if(var5 instanceof IOException) {
            var3 = true;
         }

         this.pipeline().fireExceptionCaught(var5);
      }

      if(var3) {
         if(this.isOpen()) {
            this.unsafe().close(this.unsafe().voidPromise());
         }
      } else if(var6 == 0 && this.isActive()) {
         this.read();
      }

   }

   protected abstract int doReadMessages(List<Object> var1) throws Exception;
}
