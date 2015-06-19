package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdType;
import io.netty.handler.codec.socks.SocksCommonUtils;
import io.netty.handler.codec.socks.SocksProtocolVersion;
import io.netty.handler.codec.socks.SocksRequest;
import io.netty.util.CharsetUtil;
import java.util.List;

public class SocksCmdRequestDecoder extends ReplayingDecoder<SocksCmdRequestDecoder.State> {
   private static final String name = "SOCKS_CMD_REQUEST_DECODER";
   private SocksProtocolVersion version;
   private int fieldLength;
   private SocksCmdType cmdType;
   private SocksAddressType addressType;
   private byte reserved;
   private String host;
   private int port;
   private SocksRequest msg;

   /** @deprecated */
   @Deprecated
   public static String getName() {
      return "SOCKS_CMD_REQUEST_DECODER";
   }

   public SocksCmdRequestDecoder() {
      super(SocksCmdRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      switch(SocksCmdRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksCmdRequestDecoder$State[((SocksCmdRequestDecoder.State)this.state()).ordinal()]) {
      case 1:
         this.version = SocksProtocolVersion.valueOf(var2.readByte());
         if(this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksCmdRequestDecoder.State.READ_CMD_HEADER);
      case 2:
         this.cmdType = SocksCmdType.valueOf(var2.readByte());
         this.reserved = var2.readByte();
         this.addressType = SocksAddressType.valueOf(var2.readByte());
         this.checkpoint(SocksCmdRequestDecoder.State.READ_CMD_ADDRESS);
      case 3:
         switch(SocksCmdRequestDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksAddressType[this.addressType.ordinal()]) {
         case 1:
            this.host = SocksCommonUtils.intToIp(var2.readInt());
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
            break;
         case 2:
            this.fieldLength = var2.readByte();
            this.host = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
            break;
         case 3:
            this.host = SocksCommonUtils.ipv6toStr(var2.readBytes(16).array());
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
         case 4:
         }
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksAddressType;
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksCmdRequestDecoder$State = new int[SocksCmdRequestDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksCmdRequestDecoder$State[SocksCmdRequestDecoder.State.CHECK_PROTOCOL_VERSION.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksCmdRequestDecoder$State[SocksCmdRequestDecoder.State.READ_CMD_HEADER.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksCmdRequestDecoder$State[SocksCmdRequestDecoder.State.READ_CMD_ADDRESS.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         $SwitchMap$io$netty$handler$codec$socks$SocksAddressType = new int[SocksAddressType.values().length];

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAddressType[SocksAddressType.IPv4.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAddressType[SocksAddressType.DOMAIN.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAddressType[SocksAddressType.IPv6.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$socks$SocksAddressType[SocksAddressType.UNKNOWN.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_CMD_HEADER,
      READ_CMD_ADDRESS;

      private State() {
      }
   }
}
