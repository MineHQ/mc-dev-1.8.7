package io.netty.handler.stream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class ChunkedNioStream implements ChunkedInput<ByteBuf> {
   private final ReadableByteChannel in;
   private final int chunkSize;
   private long offset;
   private final ByteBuffer byteBuffer;

   public ChunkedNioStream(ReadableByteChannel var1) {
      this(var1, 8192);
   }

   public ChunkedNioStream(ReadableByteChannel var1, int var2) {
      if(var1 == null) {
         throw new NullPointerException("in");
      } else if(var2 <= 0) {
         throw new IllegalArgumentException("chunkSize: " + var2 + " (expected: a positive integer)");
      } else {
         this.in = var1;
         this.offset = 0L;
         this.chunkSize = var2;
         this.byteBuffer = ByteBuffer.allocate(var2);
      }
   }

   public long transferredBytes() {
      return this.offset;
   }

   public boolean isEndOfInput() throws Exception {
      if(this.byteBuffer.position() > 0) {
         return false;
      } else if(this.in.isOpen()) {
         int var1 = this.in.read(this.byteBuffer);
         if(var1 < 0) {
            return true;
         } else {
            this.offset += (long)var1;
            return false;
         }
      } else {
         return true;
      }
   }

   public void close() throws Exception {
      this.in.close();
   }

   public ByteBuf readChunk(ChannelHandlerContext var1) throws Exception {
      if(this.isEndOfInput()) {
         return null;
      } else {
         int var2 = this.byteBuffer.position();

         do {
            int var3 = this.in.read(this.byteBuffer);
            if(var3 < 0) {
               break;
            }

            var2 += var3;
            this.offset += (long)var3;
         } while(var2 != this.chunkSize);

         this.byteBuffer.flip();
         boolean var9 = true;
         ByteBuf var4 = var1.alloc().buffer(this.byteBuffer.remaining());

         ByteBuf var5;
         try {
            var4.writeBytes(this.byteBuffer);
            this.byteBuffer.clear();
            var9 = false;
            var5 = var4;
         } finally {
            if(var9) {
               var4.release();
            }

         }

         return var5;
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object readChunk(ChannelHandlerContext var1) throws Exception {
      return this.readChunk(var1);
   }
}
