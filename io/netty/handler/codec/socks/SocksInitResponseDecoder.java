package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksInitResponse;
import io.netty.handler.codec.socks.SocksProtocolVersion;
import io.netty.handler.codec.socks.SocksResponse;
import java.util.List;

public class SocksInitResponseDecoder extends ReplayingDecoder<SocksInitResponseDecoder.State> {
   private static final String name = "SOCKS_INIT_RESPONSE_DECODER";
   private SocksProtocolVersion version;
   private SocksAuthScheme authScheme;
   private SocksResponse msg;

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_INIT_RESPONSE_DECODER";
   }

   public SocksInitResponseDecoder() {
      super(SocksInitResponseDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      switch(SocksInitResponseDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksInitResponseDecoder$State[((SocksInitResponseDecoder.State)this.state()).ordinal()]) {
      case 1:
         this.version = SocksProtocolVersion.valueOf(var2.readByte());
         if(this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksInitResponseDecoder.State.READ_PREFFERED_AUTH_TYPE);
      case 2:
         this.authScheme = SocksAuthScheme.valueOf(var2.readByte());
         this.msg = new SocksInitResponse(this.authScheme);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksInitResponseDecoder$State = new int[SocksInitResponseDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksInitResponseDecoder$State[SocksInitResponseDecoder.State.CHECK_PROTOCOL_VERSION.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksInitResponseDecoder$State[SocksInitResponseDecoder.State.READ_PREFFERED_AUTH_TYPE.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_PREFFERED_AUTH_TYPE;

      private State() {
      }
   }
}
