package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.compression.DecompressionException;
import io.netty.handler.codec.compression.ZlibDecoder;
import io.netty.handler.codec.compression.ZlibWrapper;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class JdkZlibDecoder extends ZlibDecoder {
   private static final int FHCRC = 2;
   private static final int FEXTRA = 4;
   private static final int FNAME = 8;
   private static final int FCOMMENT = 16;
   private static final int FRESERVED = 224;
   private Inflater inflater;
   private final byte[] dictionary;
   private final CRC32 crc;
   private JdkZlibDecoder.GzipState gzipState;
   private int flags;
   private int xlen;
   private volatile boolean finished;
   private boolean decideZlibOrNone;

   public JdkZlibDecoder() {
      this(ZlibWrapper.ZLIB, (byte[])null);
   }

   public JdkZlibDecoder(byte[] var1) {
      this(ZlibWrapper.ZLIB, var1);
   }

   public JdkZlibDecoder(ZlibWrapper var1) {
      this(var1, (byte[])null);
   }

   private JdkZlibDecoder(ZlibWrapper var1, byte[] var2) {
      this.gzipState = JdkZlibDecoder.GzipState.HEADER_START;
      this.flags = -1;
      this.xlen = -1;
      if(var1 == null) {
         throw new NullPointerException("wrapper");
      } else {
         switch(JdkZlibDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[var1.ordinal()]) {
         case 1:
            this.inflater = new Inflater(true);
            this.crc = new CRC32();
            break;
         case 2:
            this.inflater = new Inflater(true);
            this.crc = null;
            break;
         case 3:
            this.inflater = new Inflater();
            this.crc = null;
            break;
         case 4:
            this.decideZlibOrNone = true;
            this.crc = null;
            break;
         default:
            throw new IllegalArgumentException("Only GZIP or ZLIB is supported, but you used " + var1);
         }

         this.dictionary = var2;
      }
   }

   public boolean isClosed() {
      return this.finished;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if(this.finished) {
         var2.skipBytes(var2.readableBytes());
      } else if(var2.isReadable()) {
         if(this.decideZlibOrNone) {
            if(var2.readableBytes() < 2) {
               return;
            }

            boolean var4 = !looksLikeZlib(var2.getShort(0));
            this.inflater = new Inflater(var4);
            this.decideZlibOrNone = false;
         }

         if(this.crc != null) {
            switch(JdkZlibDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[this.gzipState.ordinal()]) {
            case 1:
               if(this.readGZIPFooter(var2)) {
                  this.finished = true;
               }

               return;
            default:
               if(this.gzipState != JdkZlibDecoder.GzipState.HEADER_END && !this.readGZIPHeader(var2)) {
                  return;
               }
            }
         }

         int var18 = var2.readableBytes();
         if(var2.hasArray()) {
            this.inflater.setInput(var2.array(), var2.arrayOffset() + var2.readerIndex(), var2.readableBytes());
         } else {
            byte[] var5 = new byte[var2.readableBytes()];
            var2.getBytes(var2.readerIndex(), var5);
            this.inflater.setInput(var5);
         }

         int var19 = this.inflater.getRemaining() << 1;
         ByteBuf var6 = var1.alloc().heapBuffer(var19);

         try {
            boolean var7 = false;
            byte[] var8 = var6.array();

            while(true) {
               if(!this.inflater.needsInput()) {
                  int var9 = var6.writerIndex();
                  int var10 = var6.arrayOffset() + var9;
                  int var11 = var6.writableBytes();
                  if(var11 == 0) {
                     var3.add(var6);
                     var6 = var1.alloc().heapBuffer(var19);
                     var8 = var6.array();
                     continue;
                  }

                  int var12 = this.inflater.inflate(var8, var10, var11);
                  if(var12 > 0) {
                     var6.writerIndex(var9 + var12);
                     if(this.crc != null) {
                        this.crc.update(var8, var10, var12);
                     }
                  } else if(this.inflater.needsDictionary()) {
                     if(this.dictionary == null) {
                        throw new DecompressionException("decompression failure, unable to set dictionary as non was specified");
                     }

                     this.inflater.setDictionary(this.dictionary);
                  }

                  if(!this.inflater.finished()) {
                     continue;
                  }

                  if(this.crc == null) {
                     this.finished = true;
                  } else {
                     var7 = true;
                  }
               }

               var2.skipBytes(var18 - this.inflater.getRemaining());
               if(!var7) {
                  break;
               }

               this.gzipState = JdkZlibDecoder.GzipState.FOOTER_START;
               if(this.readGZIPFooter(var2)) {
                  this.finished = true;
               }
               break;
            }
         } catch (DataFormatException var16) {
            throw new DecompressionException("decompression failure", var16);
         } finally {
            if(var6.isReadable()) {
               var3.add(var6);
            } else {
               var6.release();
            }

         }

      }
   }

   protected void handlerRemoved0(ChannelHandlerContext var1) throws Exception {
      super.handlerRemoved0(var1);
      if(this.inflater != null) {
         this.inflater.end();
      }

   }

   private boolean readGZIPHeader(ByteBuf var1) {
      short var5;
      switch(JdkZlibDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[this.gzipState.ordinal()]) {
      case 2:
         if(var1.readableBytes() < 10) {
            return false;
         }

         byte var2 = var1.readByte();
         byte var3 = var1.readByte();
         if(var2 != 31) {
            throw new DecompressionException("Input is not in the GZIP format");
         }

         this.crc.update(var2);
         this.crc.update(var3);
         short var4 = var1.readUnsignedByte();
         if(var4 != 8) {
            throw new DecompressionException("Unsupported compression method " + var4 + " in the GZIP header");
         }

         this.crc.update(var4);
         this.flags = var1.readUnsignedByte();
         this.crc.update(this.flags);
         if((this.flags & 224) != 0) {
            throw new DecompressionException("Reserved flags are set in the GZIP header");
         }

         this.crc.update(var1.readByte());
         this.crc.update(var1.readByte());
         this.crc.update(var1.readByte());
         this.crc.update(var1.readByte());
         this.crc.update(var1.readUnsignedByte());
         this.crc.update(var1.readUnsignedByte());
         this.gzipState = JdkZlibDecoder.GzipState.FLG_READ;
      case 3:
         if((this.flags & 4) != 0) {
            if(var1.readableBytes() < 2) {
               return false;
            }

            var5 = var1.readUnsignedByte();
            short var6 = var1.readUnsignedByte();
            this.crc.update(var5);
            this.crc.update(var6);
            this.xlen |= var5 << 8 | var6;
         }

         this.gzipState = JdkZlibDecoder.GzipState.XLEN_READ;
      case 4:
         if(this.xlen != -1) {
            if(var1.readableBytes() < this.xlen) {
               return false;
            }

            byte[] var7 = new byte[this.xlen];
            var1.readBytes(var7);
            this.crc.update(var7);
         }

         this.gzipState = JdkZlibDecoder.GzipState.SKIP_FNAME;
      case 5:
         if((this.flags & 8) != 0) {
            if(!var1.isReadable()) {
               return false;
            }

            do {
               var5 = var1.readUnsignedByte();
               this.crc.update(var5);
            } while(var5 != 0 && var1.isReadable());
         }

         this.gzipState = JdkZlibDecoder.GzipState.SKIP_COMMENT;
      case 6:
         if((this.flags & 16) != 0) {
            if(!var1.isReadable()) {
               return false;
            }

            do {
               var5 = var1.readUnsignedByte();
               this.crc.update(var5);
            } while(var5 != 0 && var1.isReadable());
         }

         this.gzipState = JdkZlibDecoder.GzipState.PROCESS_FHCRC;
      case 7:
         break;
      case 8:
         return true;
      default:
         throw new IllegalStateException();
      }

      if((this.flags & 2) != 0) {
         if(var1.readableBytes() < 4) {
            return false;
         }

         this.verifyCrc(var1);
      }

      this.crc.reset();
      this.gzipState = JdkZlibDecoder.GzipState.HEADER_END;
      return true;
   }

   private boolean readGZIPFooter(ByteBuf var1) {
      if(var1.readableBytes() < 8) {
         return false;
      } else {
         this.verifyCrc(var1);
         int var2 = 0;

         int var3;
         for(var3 = 0; var3 < 4; ++var3) {
            var2 |= var1.readUnsignedByte() << var3 * 8;
         }

         var3 = this.inflater.getTotalOut();
         if(var2 != var3) {
            throw new DecompressionException("Number of bytes mismatch. Expected: " + var2 + ", Got: " + var3);
         } else {
            return true;
         }
      }
   }

   private void verifyCrc(ByteBuf var1) {
      long var2 = 0L;

      for(int var4 = 0; var4 < 4; ++var4) {
         var2 |= (long)var1.readUnsignedByte() << var4 * 8;
      }

      long var6 = this.crc.getValue();
      if(var2 != var6) {
         throw new DecompressionException("CRC value missmatch. Expected: " + var2 + ", Got: " + var6);
      }
   }

   private static boolean looksLikeZlib(short var0) {
      return (var0 & 30720) == 30720 && var0 % 31 == 0;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper;
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState = new int[JdkZlibDecoder.GzipState.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.FOOTER_START.ordinal()] = 1;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.HEADER_START.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.FLG_READ.ordinal()] = 3;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.XLEN_READ.ordinal()] = 4;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.SKIP_FNAME.ordinal()] = 5;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.SKIP_COMMENT.ordinal()] = 6;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.PROCESS_FHCRC.ordinal()] = 7;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$JdkZlibDecoder$GzipState[JdkZlibDecoder.GzipState.HEADER_END.ordinal()] = 8;
         } catch (NoSuchFieldError var5) {
            ;
         }

         $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper = new int[ZlibWrapper.values().length];

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.GZIP.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.NONE.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$compression$ZlibWrapper[ZlibWrapper.ZLIB.ordinal()] = 3;
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

   private static enum GzipState {
      HEADER_START,
      HEADER_END,
      FLG_READ,
      XLEN_READ,
      SKIP_FNAME,
      SKIP_COMMENT,
      PROCESS_FHCRC,
      FOOTER_START;

      private GzipState() {
      }
   }
}
