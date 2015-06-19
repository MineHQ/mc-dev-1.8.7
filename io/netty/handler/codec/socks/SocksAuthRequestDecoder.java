package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAuthRequest;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.handler.codec.socks.SocksSubnegotiationVersion;
import io.netty.util.CharsetUtil;
import java.util.List;

public class SocksAuthRequestDecoder extends ReplayingDecoder<SocksAuthRequestDecoder.State> {
   private static final String name = "SOCKS_AUTH_REQUEST_DECODER";
   private SocksSubnegotiationVersion version;
   private int fieldLength;
   private String username;
   private String password;
   private SocksRequest msg;

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_AUTH_REQUEST_DECODER";
   }

   public SocksAuthRequestDecoder() {
      super(SocksAuthRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      switch(SocksAuthRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksAuthRequestDecoder$State[((SocksAuthRequestDecoder.State)this.state()).ordinal()]) {
      case 1:
         this.version = SocksSubnegotiationVersion.valueOf(var2.readByte());
         if(this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
            break;
         }

         this.checkpoint(SocksAuthRequestDecoder.State.READ_USERNAME);
      case 2:
         this.fieldLength = var2.readByte();
         this.username = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
         this.checkpoint(SocksAuthRequestDecoder.State.READ_PASSWORD);
      case 3:
         this.fieldLength = var2.readByte();
         this.password = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
         this.msg = new SocksAuthRequest(this.username, this.password);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksAuthRequestDecoder$State = new int[SocksAuthRequestDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAuthRequestDecoder$State[SocksAuthRequestDecoder.State.CHECK_PROTOCOL_VERSION.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAuthRequestDecoder$State[SocksAuthRequestDecoder.State.READ_USERNAME.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAuthRequestDecoder$State[SocksAuthRequestDecoder.State.READ_PASSWORD.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_USERNAME,
      READ_PASSWORD;

      private State() {
      }
   }
}
