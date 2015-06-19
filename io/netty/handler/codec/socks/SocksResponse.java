package io.netty.handler.codec.socks;

import io.netty.handler.codec.socks.SocksMessage;
import io.netty.handler.codec.socks.SocksMessageType;
import io.netty.handler.codec.socks.SocksResponseType;

public abstract class SocksResponse extends SocksMessage {
   private final SocksResponseType responseType;

   protected SocksResponse(SocksResponseType var1) {
      super(SocksMessageType.RESPONSE);
      if(var1 == null) {
         throw new NullPointerException("responseType");
      } else {
         this.responseType = var1;
      }
   }

   public SocksResponseType responseType() {
      return this.responseType;
   }
}
