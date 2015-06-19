package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.spdy.SpdyHeaderBlockJZlibEncoder;
import io.netty.handler.codec.spdy.SpdyHeaderBlockZlibEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.util.internal.PlatformDependent;

abstract class SpdyHeaderBlockEncoder {
   SpdyHeaderBlockEncoder() {
   }

   static SpdyHeaderBlockEncoder newInstance(SpdyVersion var0, int var1, int var2, int var3) {
      return (SpdyHeaderBlockEncoder)(PlatformDependent.javaVersion() >= 7?new SpdyHeaderBlockZlibEncoder(var0, var1):new SpdyHeaderBlockJZlibEncoder(var0, var1, var2, var3));
   }

   abstract ByteBuf encode(SpdyHeadersFrame var1) throws Exception;

   abstract void end();
}
