package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAuthResponse;
import io.netty.handler.codec.socks.SocksAuthStatus;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksSubnegotiationVersion;
import java.util.List;

public class SocksAuthResponseDecoder extends ReplayingDecoder<SocksAuthResponseDecoder.State> {
   private static final String name = "SOCKS_AUTH_RESPONSE_DECODER";
   private SocksSubnegotiationVersion version;
   private SocksAuthStatus authStatus;
   private SocksResponse msg;

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_AUTH_RESPONSE_DECODER";
   }

   public SocksAuthResponseDecoder() {
      super(SocksAuthResponseDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      switch(SocksAuthResponseDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksAuthResponseDecoder$State[((SocksAuthResponseDecoder.State)this.state()).ordinal()]) {
      case 1:
         this.version = SocksSubnegotiationVersion.valueOf(var2.readByte());
         if(this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
            break;
         }

         this.checkpoint(SocksAuthResponseDecoder.State.READ_AUTH_RESPONSE);
      case 2:
         this.authStatus = SocksAuthStatus.valueOf(var2.readByte());
         this.msg = new SocksAuthResponse(this.authStatus);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksAuthResponseDecoder$State = new int[SocksAuthResponseDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAuthResponseDecoder$State[SocksAuthResponseDecoder.State.CHECK_PROTOCOL_VERSION.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAuthResponseDecoder$State[SocksAuthResponseDecoder.State.READ_AUTH_RESPONSE.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_AUTH_RESPONSE;

      private State() {
      }
   }
}
