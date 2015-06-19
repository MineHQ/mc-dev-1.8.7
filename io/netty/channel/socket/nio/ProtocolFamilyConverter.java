package io.netty.channel.socket.nio;

import io.netty.channel.socket.InternetProtocolFamily;
import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;

final class ProtocolFamilyConverter {
   private ProtocolFamilyConverter() {
   }

   public static ProtocolFamily convert(InternetProtocolFamily var0) {
      switch(ProtocolFamilyConverter.SyntheticClass_1.$SwitchMap$io$netty$channel$socket$InternetProtocolFamily[var0.ordinal()]) {
      case 1:
         return StandardProtocolFamily.INET;
      case 2:
         return StandardProtocolFamily.INET6;
      default:
         throw new IllegalArgumentException();
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$channel$socket$InternetProtocolFamily = new int[InternetProtocolFamily.values().length];

      static {
         try {
            $SwitchMap$io$netty$channel$socket$InternetProtocolFamily[InternetProtocolFamily.IPv4.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$channel$socket$InternetProtocolFamily[InternetProtocolFamily.IPv6.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
