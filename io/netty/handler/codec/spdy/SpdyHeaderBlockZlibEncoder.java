package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.spdy.SpdyCodecUtil;
import io.netty.handler.codec.spdy.SpdyHeaderBlockRawEncoder;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyVersion;
import java.util.zip.Deflater;

class SpdyHeaderBlockZlibEncoder extends SpdyHeaderBlockRawEncoder {
   private final Deflater compressor;
   private boolean finished;

   SpdyHeaderBlockZlibEncoder(SpdyVersion var1, int var2) {
      super(var1);
      if(var2 >= 0 && var2 <= 9) {
         this.compressor = new Deflater(var2);
         this.compressor.setDictionary(SpdyCodecUtil.SPDY_DICT);
      } else {
         throw new IllegalArgumentException("compressionLevel: " + var2 + " (expected: 0-9)");
      }
   }

   private int setInput(ByteBuf var1) {
      int var2 = var1.readableBytes();
      if(var1.hasArray()) {
         this.compressor.setInput(var1.array(), var1.arrayOffset() + var1.readerIndex(), var2);
      } else {
         byte[] var3 = new byte[var2];
         var1.getBytes(var1.readerIndex(), var3);
         this.compressor.setInput(var3, 0, var3.length);
      }

      return var2;
   }

   private void encode(ByteBuf var1) {
      while(this.compressInto(var1)) {
         var1.ensureWritable(var1.capacity() << 1);
      }

   }

   private boolean compressInto(ByteBuf var1) {
      byte[] var2 = var1.array();
      int var3 = var1.arrayOffset() + var1.writerIndex();
      int var4 = var1.writableBytes();
      int var5 = this.compressor.deflate(var2, var3, var4, 2);
      var1.writerIndex(var1.writerIndex() + var5);
      return var5 == var4;
   }

   public ByteBuf encode(SpdyHeadersFrame var1) throws Exception {
      if(var1 == null) {
         throw new IllegalArgumentException("frame");
      } else if(this.finished) {
         return Unpooled.EMPTY_BUFFER;
      } else {
         ByteBuf var2 = super.encode(var1);
         if(var2.readableBytes() == 0) {
            return Unpooled.EMPTY_BUFFER;
         } else {
            ByteBuf var3 = var2.alloc().heapBuffer(var2.readableBytes());
            int var4 = this.setInput(var2);
            this.encode(var3);
            var2.skipBytes(var4);
            return var3;
         }
      }
   }

   public void end() {
      if(!this.finished) {
         this.finished = true;
         this.compressor.end();
         super.end();
      }
   }
}
