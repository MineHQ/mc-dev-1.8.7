package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;

public final class SocksInitResponse extends SocksResponse {
   private final SocksAuthScheme authScheme;

   public SocksInitResponse(SocksAuthScheme var1) {
      super(SocksResponseType.INIT);
      if(var1 == null) {
         throw new NullPointerException("authScheme");
      } else {
         this.authScheme = var1;
      }
   }

   public SocksAuthScheme authScheme() {
      return this.authScheme;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(this.protocolVersion().byteValue());
      var1.writeByte(this.authScheme.byteValue());
   }
}
