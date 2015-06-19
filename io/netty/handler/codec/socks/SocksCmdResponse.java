package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.socks.SocksAddressType;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.handler.codec.socks.SocksResponse;
import io.netty.handler.codec.socks.SocksResponseType;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import java.net.IDN;

public final class SocksCmdResponse extends SocksResponse {
   private final SocksCmdStatus cmdStatus;
   private final SocksAddressType addressType;
   private final String host;
   private final int port;
   private static final byte[] DOMAIN_ZEROED = new byte[]{(byte)0};
   private static final byte[] IPv4_HOSTNAME_ZEROED = new byte[]{(byte)0, (byte)0, (byte)0, (byte)0};
   private static final byte[] IPv6_HOSTNAME_ZEROED = new byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0};

   public SocksCmdResponse(SocksCmdStatus var1, SocksAddressType var2) {
      this(var1, var2, (String)null, 0);
   }

   public SocksCmdResponse(SocksCmdStatus var1, SocksAddressType var2, String var3, int var4) {
      super(SocksResponseType.CMD);
      if(var1 == null) {
         throw new NullPointerException("cmdStatus");
      } else if(var2 == null) {
         throw new NullPointerException("addressType");
      } else {
         if(var3 != null) {
            switch(SocksCmdResponse.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksAddressType[var2.ordinal()]) {
            case 1:
               if(!NetUtil.isValidIpV4Address(var3)) {
                  throw new IllegalArgumentException(var3 + " is not a valid IPv4 address");
               }
               break;
            case 2:
               if(IDN.toASCII(var3).length() > 255) {
                  throw new IllegalArgumentException(var3 + " IDN: " + IDN.toASCII(var3) + " exceeds 255 char limit");
               }
               break;
            case 3:
               if(!NetUtil.isValidIpV6Address(var3)) {
                  throw new IllegalArgumentException(var3 + " is not a valid IPv6 address");
               }
            case 4:
            }

            var3 = IDN.toASCII(var3);
         }

         if(var4 >= 0 && var4 <= '\uffff') {
            this.cmdStatus = var1;
            this.addressType = var2;
            this.host = var3;
            this.port = var4;
         } else {
            throw new IllegalArgumentException(var4 + " is not in bounds 0 <= x <= 65535");
         }
      }
   }

   public SocksCmdStatus cmdStatus() {
      return this.cmdStatus;
   }

   public SocksAddressType addressType() {
      return this.addressType;
   }

   public String host() {
      return this.host != null?IDN.toUnicode(this.host):null;
   }

   public int port() {
      return this.port;
   }

   public void encodeAsByteBuf(ByteBuf var1) {
      var1.writeByte(this.protocolVersion().byteValue());
      var1.writeByte(this.cmdStatus.byteValue());
      var1.writeByte(0);
      var1.writeByte(this.addressType.byteValue());
      byte[] var2;
      switch(SocksCmdResponse.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$socks$SocksAddressType[this.addressType.ordinal()]) {
      case 1:
         var2 = this.host == null?IPv4_HOSTNAME_ZEROED:NetUtil.createByteArrayFromIpAddressString(this.host);
         var1.writeBytes(var2);
         var1.writeShort(this.port);
         break;
      case 2:
         var2 = this.host == null?DOMAIN_ZEROED:this.host.getBytes(CharsetUtil.US_ASCII);
         var1.writeByte(var2.length);
         var1.writeBytes(var2);
         var1.writeShort(this.port);
         break;
      case 3:
         var2 = this.host == null?IPv6_HOSTNAME_ZEROED:NetUtil.createByteArrayFromIpAddressString(this.host);
         var1.writeBytes(var2);
         var1.writeShort(this.port);
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$socks$SocksAddressType = new int[SocksAddressType.values().length];

      static {
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
}
