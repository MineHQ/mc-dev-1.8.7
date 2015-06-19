package io.netty.handler.codec.compression;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.JZlib.WrapperType;
import io.netty.handler.codec.compression.CompressionException;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.ZlibWrapper;

final class ZlibUtil {
   static void fail(Inflater var0, String var1, int var2) {
      throw inflaterException(var0, var1, var2);
   }

   static void fail(Deflater var0, String var1, int var2) {
      throw deflaterException(var0, var1, var2);
   }

   static DecompressionException inflaterException(Inflater var0, String var1, int var2) {
      return new DecompressionException(var1 + " (" + var2 + ')' + (var0.msg != null?": " + var0.msg:""));
   }

   static CompressionException deflaterException(Deflater var0, String var1, int var2) {
      return new CompressionException(var1 + " (" + var2 + ')' + (var0.msg != null?": " + var0.msg:""));
   }

   static WrapperType convertWrapperType(ZlibWrapper var0) {
      WrapperType var1;
      switch(ZlibUtil.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[var0.ordinal()]) {
      case 1:
         var1 = JZlib.W_NONE;
         break;
      case 2:
         var1 = JZlib.W_ZLIB;
         break;
      case 3:
         var1 = JZlib.W_GZIP;
         break;
      case 4:
         var1 = JZlib.W_ANY;
         break;
      default:
         throw new Error();
      }

      return var1;
   }

   static int wrapperOverhead(ZlibWrapper var0) {
      byte var1;
      switch(ZlibUtil.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[var0.ordinal()]) {
      case 1:
         var1 = 0;
         break;
      case 2:
      case 4:
         var1 = 2;
         break;
      case 3:
         var1 = 10;
         break;
      default:
         throw new Error();
      }

      return var1;
   }

   private ZlibUtil() {
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper = new int[ZlibWrapper.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.ZLIB.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.GZIP.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.ZLIB_OR_NONE.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
