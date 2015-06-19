package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksInitRequest;
import io.netty.handler.codec.socks.SocksProtocolVersion;
import io.netty.handler.codec.socks.SocksRequest;
import java.util.ArrayList;
import java.util.List;

public class SocksInitRequestDecoder extends ReplayingDecoder<SocksInitRequestDecoder.State> {
   private static final String name = "SOCKS_INIT_REQUEST_DECODER";
   private final List<SocksAuthScheme> authSchemes = new ArrayList();
   private SocksProtocolVersion version;
   private byte authSchemeNum;
   private SocksRequest msg;

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_INIT_REQUEST_DECODER";
   }

   public SocksInitRequestDecoder() {
      super(SocksInitRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      switch(SocksInitRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksInitRequestDecoder$State[((SocksInitRequestDecoder.State)this.state()).ordinal()]) {
      case 1:
         this.version = SocksProtocolVersion.valueOf(var2.readByte());
         if(this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksInitRequestDecoder.State.READ_AUTH_SCHEMES);
      case 2:
         this.authSchemes.clear();
         this.authSchemeNum = var2.readByte();

         for(int var4 = 0; var4 < this.authSchemeNum; ++var4) {
            this.authSchemes.add(SocksAuthScheme.valueOf(var2.readByte()));
         }

         this.msg = new SocksInitRequest(this.authSchemes);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksInitRequestDecoder$State = new int[SocksInitRequestDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksInitRequestDecoder$State[SocksInitRequestDecoder.State.CHECK_PROTOCOL_VERSION.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksInitRequestDecoder$State[SocksInitRequestDecoder.State.READ_AUTH_SCHEMES.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_AUTH_SCHEMES;

      private State() {
      }
   }
}
