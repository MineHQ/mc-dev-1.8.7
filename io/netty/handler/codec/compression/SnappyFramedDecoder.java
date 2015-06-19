package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.Snappy;
import java.util.Arrays;
import java.util.List;

public class SnappyFramedDecoder extends ByteToMessageDecoder {
   private static final byte[] SNAPPY = new byte[]{(byte)115, (byte)78, (byte)97, (byte)80, (byte)112, (byte)89};
   private static final int MAX_UNCOMPRESSED_DATA_SIZE = 65540;
   private final Snappy snappy;
   private final boolean validateChecksums;
   private boolean started;
   private boolean corrupted;

   public SnappyFramedDecoder() {
      this(false);
   }

   public SnappyFramedDecoder(boolean var1) {
      this.snappy = new Snappy();
      this.validateChecksums = var1;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if(this.corrupted) {
         var2.skipBytes(var2.readableBytes());
      } else {
         try {
            int var4 = var2.readerIndex();
            int var5 = var2.readableBytes();
            if(var5 >= 4) {
               short var6 = var2.getUnsignedByte(var4);
               SnappyFramedDecoder.ChunkType var7 = mapChunkType((byte)var6);
               int var8 = ByteBufUtil.swapMedium(var2.getUnsignedMedium(var4 + 1));
               int var10;
               switch(SnappyFramedDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[var7.ordinal()]) {
               case 1:
                  if(var8 != SNAPPY.length) {
                     throw new DecompressionException("Unexpected length of stream identifier: " + var8);
                  }

                  if(var5 >= 4 + SNAPPY.length) {
                     byte[] var9 = new byte[var8];
                     var2.skipBytes(4).readBytes(var9);
                     if(!Arrays.equals(var9, SNAPPY)) {
                        throw new DecompressionException("Unexpected stream identifier contents. Mismatched snappy protocol version?");
                     }

                     this.started = true;
                  }
                  break;
               case 2:
                  if(!this.started) {
                     throw new DecompressionException("Received RESERVED_SKIPPABLE tag before STREAM_IDENTIFIER");
                  }

                  if(var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4 + var8);
                  break;
               case 3:
                  throw new DecompressionException("Found reserved unskippable chunk type: 0x" + Integer.toHexString(var6));
               case 4:
                  if(!this.started) {
                     throw new DecompressionException("Received UNCOMPRESSED_DATA tag before STREAM_IDENTIFIER");
                  }

                  if(var8 > 65540) {
                     throw new DecompressionException("Received UNCOMPRESSED_DATA larger than 65540 bytes");
                  }

                  if(var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4);
                  if(this.validateChecksums) {
                     var10 = ByteBufUtil.swapInt(var2.readInt());
                     Snappy.validateChecksum(var10, var2, var2.readerIndex(), var8 - 4);
                  } else {
                     var2.skipBytes(4);
                  }

                  var3.add(var2.readSlice(var8 - 4).retain());
                  break;
               case 5:
                  if(!this.started) {
                     throw new DecompressionException("Received COMPRESSED_DATA tag before STREAM_IDENTIFIER");
                  }

                  if(var5 < 4 + var8) {
                     return;
                  }

                  var2.skipBytes(4);
                  var10 = ByteBufUtil.swapInt(var2.readInt());
                  ByteBuf var11 = var1.alloc().buffer(0);
                  if(this.validateChecksums) {
                     int var12 = var2.writerIndex();

                     try {
                        var2.writerIndex(var2.readerIndex() + var8 - 4);
                        this.snappy.decode(var2, var11);
                     } finally {
                        var2.writerIndex(var12);
                     }

                     Snappy.validateChecksum(var10, var11, 0, var11.writerIndex());
                  } else {
                     this.snappy.decode(var2.readSlice(var8 - 4), var11);
                  }

                  var3.add(var11);
                  this.snappy.reset();
               }

            }
         } catch (Exception var17) {
            this.corrupted = true;
            throw var17;
         }
      }
   }

   private static SnappyFramedDecoder.ChunkType mapChunkType(byte var0) {
      return var0 == 0?SnappyFramedDecoder.ChunkType.COMPRESSED_DATA:(var0 == 1?SnappyFramedDecoder.ChunkType.UNCOMPRESSED_DATA:(var0 == -1?SnappyFramedDecoder.ChunkType.STREAM_IDENTIFIER:((var0 & 128) == 128?SnappyFramedDecoder.ChunkType.RESERVED_SKIPPABLE:SnappyFramedDecoder.ChunkType.RESERVED_UNSKIPPABLE)));
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType = new int[SnappyFramedDecoder.ChunkType.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[SnappyFramedDecoder.ChunkType.STREAM_IDENTIFIER.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[SnappyFramedDecoder.ChunkType.RESERVED_SKIPPABLE.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[SnappyFramedDecoder.ChunkType.RESERVED_UNSKIPPABLE.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[SnappyFramedDecoder.ChunkType.UNCOMPRESSED_DATA.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$SnappyFramedDecoder$ChunkType[SnappyFramedDecoder.ChunkType.COMPRESSED_DATA.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private static enum ChunkType {
      STREAM_IDENTIFIER,
      COMPRESSED_DATA,
      UNCOMPRESSED_DATA,
      RESERVED_UNSKIPPABLE,
      RESERVED_SKIPPABLE;

      private ChunkType() {
      }
   }
}
