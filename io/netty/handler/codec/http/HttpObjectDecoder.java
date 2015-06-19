package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.internal.AppendableCharSequence;
import java.util.List;

public abstract class HttpObjectDecoder extends ReplayingDecoder<HttpObjectDecoder.State> {
   private final int maxInitialLineLength;
   private final int maxHeaderSize;
   private final int maxChunkSize;
   private final boolean chunkedSupported;
   protected final boolean validateHeaders;
   private final AppendableCharSequence seq;
   private final HttpObjectDecoder.HeaderParser headerParser;
   private final HttpObjectDecoder.LineParser lineParser;
   private HttpMessage message;
   private long chunkSize;
   private int headerSize;
   private long contentLength;

   protected HttpObjectDecoder() {
      this(4096, 8192, 8192, true);
   }

   protected HttpObjectDecoder(int var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, true);
   }

   protected HttpObjectDecoder(int var1, int var2, int var3, boolean var4, boolean var5) {
      super(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
      this.seq = new AppendableCharSequence(128);
      this.headerParser = new HttpObjectDecoder.HeaderParser(this.seq);
      this.lineParser = new HttpObjectDecoder.LineParser(this.seq);
      this.contentLength = Long.MIN_VALUE;
      if(var1 <= 0) {
         throw new IllegalArgumentException("maxInitialLineLength must be a positive integer: " + var1);
      } else if(var2 <= 0) {
         throw new IllegalArgumentException("maxHeaderSize must be a positive integer: " + var2);
      } else if(var3 <= 0) {
         throw new IllegalArgumentException("maxChunkSize must be a positive integer: " + var3);
      } else {
         this.maxInitialLineLength = var1;
         this.maxHeaderSize = var2;
         this.maxChunkSize = var3;
         this.chunkedSupported = var4;
         this.validateHeaders = var5;
      }
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      int var4;
      int var5;
      switch(HttpObjectDecoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[((HttpObjectDecoder.State)this.state()).ordinal()]) {
      case 1:
         try {
            skipControlCharacters(var2);
            this.checkpoint(HttpObjectDecoder.State.READ_INITIAL);
         } finally {
            this.checkpoint();
         }
      case 2:
         try {
            String[] var21 = splitInitialLine(this.lineParser.parse(var2));
            if(var21.length < 3) {
               this.checkpoint(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
               return;
            }

            this.message = this.createMessage(var21);
            this.checkpoint(HttpObjectDecoder.State.READ_HEADER);
         } catch (Exception var14) {
            var3.add(this.invalidMessage(var14));
            return;
         }
      case 3:
         try {
            HttpObjectDecoder.State var22 = this.readHeaders(var2);
            this.checkpoint(var22);
            if(var22 == HttpObjectDecoder.State.READ_CHUNK_SIZE) {
               if(!this.chunkedSupported) {
                  throw new IllegalArgumentException("Chunked messages not supported");
               }

               var3.add(this.message);
               return;
            }

            if(var22 == HttpObjectDecoder.State.SKIP_CONTROL_CHARS) {
               var3.add(this.message);
               var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
               this.reset();
               return;
            }

            long var24 = this.contentLength();
            if(var24 == 0L || var24 == -1L && this.isDecodingRequest()) {
               var3.add(this.message);
               var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
               this.reset();
               return;
            }

            assert var22 == HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT || var22 == HttpObjectDecoder.State.READ_VARIABLE_LENGTH_CONTENT;

            var3.add(this.message);
            if(var22 == HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT) {
               this.chunkSize = var24;
            }

            return;
         } catch (Exception var16) {
            var3.add(this.invalidMessage(var16));
            return;
         }
      case 4:
         var4 = Math.min(this.actualReadableBytes(), this.maxChunkSize);
         if(var4 > 0) {
            ByteBuf var23 = ByteBufUtil.readBytes(var1.alloc(), var2, var4);
            if(var2.isReadable()) {
               var3.add(new DefaultHttpContent(var23));
            } else {
               var3.add(new DefaultLastHttpContent(var23, this.validateHeaders));
               this.reset();
            }
         } else if(!var2.isReadable()) {
            var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
            this.reset();
         }

         return;
      case 5:
         var4 = this.actualReadableBytes();
         if(var4 == 0) {
            return;
         }

         var5 = Math.min(var4, this.maxChunkSize);
         if((long)var5 > this.chunkSize) {
            var5 = (int)this.chunkSize;
         }

         ByteBuf var6 = ByteBufUtil.readBytes(var1.alloc(), var2, var5);
         this.chunkSize -= (long)var5;
         if(this.chunkSize == 0L) {
            var3.add(new DefaultLastHttpContent(var6, this.validateHeaders));
            this.reset();
         } else {
            var3.add(new DefaultHttpContent(var6));
         }

         return;
      case 6:
         try {
            AppendableCharSequence var18 = this.lineParser.parse(var2);
            var5 = getChunkSize(var18.toString());
            this.chunkSize = (long)var5;
            if(var5 == 0) {
               this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_FOOTER);
               return;
            }

            this.checkpoint(HttpObjectDecoder.State.READ_CHUNKED_CONTENT);
         } catch (Exception var13) {
            var3.add(this.invalidChunk(var13));
            return;
         }
      case 7:
         assert this.chunkSize <= 2147483647L;

         var4 = Math.min((int)this.chunkSize, this.maxChunkSize);
         DefaultHttpContent var20 = new DefaultHttpContent(ByteBufUtil.readBytes(var1.alloc(), var2, var4));
         this.chunkSize -= (long)var4;
         var3.add(var20);
         if(this.chunkSize != 0L) {
            return;
         }

         this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_DELIMITER);
      case 8:
         do {
            while(true) {
               byte var19 = var2.readByte();
               if(var19 == 13) {
                  break;
               }

               if(var19 == 10) {
                  this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_SIZE);
                  return;
               }

               this.checkpoint();
            }
         } while(var2.readByte() != 10);

         this.checkpoint(HttpObjectDecoder.State.READ_CHUNK_SIZE);
         return;
      case 9:
         try {
            LastHttpContent var17 = this.readTrailingHeaders(var2);
            var3.add(var17);
            this.reset();
            return;
         } catch (Exception var12) {
            var3.add(this.invalidChunk(var12));
            return;
         }
      case 10:
         var2.skipBytes(this.actualReadableBytes());
         break;
      case 11:
         var4 = this.actualReadableBytes();
         if(var4 > 0) {
            var3.add(var2.readBytes(this.actualReadableBytes()));
         }
      }

   }

   protected void decodeLast(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      this.decode(var1, var2, var3);
      if(this.message != null) {
         boolean var4;
         if(this.isDecodingRequest()) {
            var4 = true;
         } else {
            var4 = this.contentLength() > 0L;
         }

         this.reset();
         if(!var4) {
            var3.add(LastHttpContent.EMPTY_LAST_CONTENT);
         }
      }

   }

   protected boolean isContentAlwaysEmpty(HttpMessage var1) {
      if(var1 instanceof HttpResponse) {
         HttpResponse var2 = (HttpResponse)var1;
         int var3 = var2.getStatus().code();
         if(var3 >= 100 && var3 < 200) {
            return var3 != 101 || var2.headers().contains("Sec-WebSocket-Accept");
         }

         switch(var3) {
         case 204:
         case 205:
         case 304:
            return true;
         }
      }

      return false;
   }

   private void reset() {
      HttpMessage var1 = this.message;
      this.message = null;
      this.contentLength = Long.MIN_VALUE;
      if(!this.isDecodingRequest()) {
         HttpResponse var2 = (HttpResponse)var1;
         if(var2 != null && var2.getStatus().code() == 101) {
            this.checkpoint(HttpObjectDecoder.State.UPGRADED);
            return;
         }
      }

      this.checkpoint(HttpObjectDecoder.State.SKIP_CONTROL_CHARS);
   }

   private HttpMessage invalidMessage(Exception var1) {
      this.checkpoint(HttpObjectDecoder.State.BAD_MESSAGE);
      if(this.message != null) {
         this.message.setDecoderResult(DecoderResult.failure(var1));
      } else {
         this.message = this.createInvalidMessage();
         this.message.setDecoderResult(DecoderResult.failure(var1));
      }

      HttpMessage var2 = this.message;
      this.message = null;
      return var2;
   }

   private HttpContent invalidChunk(Exception var1) {
      this.checkpoint(HttpObjectDecoder.State.BAD_MESSAGE);
      DefaultLastHttpContent var2 = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
      var2.setDecoderResult(DecoderResult.failure(var1));
      this.message = null;
      return var2;
   }

   private static void skipControlCharacters(ByteBuf var0) {
      char var1;
      do {
         var1 = (char)var0.readUnsignedByte();
      } while(Character.isISOControl(var1) || Character.isWhitespace(var1));

      var0.readerIndex(var0.readerIndex() - 1);
   }

   private HttpObjectDecoder.State readHeaders(ByteBuf var1) {
      this.headerSize = 0;
      HttpMessage var2 = this.message;
      HttpHeaders var3 = var2.headers();
      AppendableCharSequence var4 = this.headerParser.parse(var1);
      String var5 = null;
      String var6 = null;
      if(var4.length() > 0) {
         var3.clear();

         do {
            char var7 = var4.charAt(0);
            if(var5 == null || var7 != 32 && var7 != 9) {
               if(var5 != null) {
                  var3.add((String)var5, (Object)var6);
               }

               String[] var8 = splitHeader(var4);
               var5 = var8[0];
               var6 = var8[1];
            } else {
               var6 = var6 + ' ' + var4.toString().trim();
            }

            var4 = this.headerParser.parse(var1);
         } while(var4.length() > 0);

         if(var5 != null) {
            var3.add((String)var5, (Object)var6);
         }
      }

      HttpObjectDecoder.State var9;
      if(this.isContentAlwaysEmpty(var2)) {
         HttpHeaders.removeTransferEncodingChunked(var2);
         var9 = HttpObjectDecoder.State.SKIP_CONTROL_CHARS;
      } else if(HttpHeaders.isTransferEncodingChunked(var2)) {
         var9 = HttpObjectDecoder.State.READ_CHUNK_SIZE;
      } else if(this.contentLength() >= 0L) {
         var9 = HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT;
      } else {
         var9 = HttpObjectDecoder.State.READ_VARIABLE_LENGTH_CONTENT;
      }

      return var9;
   }

   private long contentLength() {
      if(this.contentLength == Long.MIN_VALUE) {
         this.contentLength = HttpHeaders.getContentLength(this.message, -1L);
      }

      return this.contentLength;
   }

   private LastHttpContent readTrailingHeaders(ByteBuf var1) {
      this.headerSize = 0;
      AppendableCharSequence var2 = this.headerParser.parse(var1);
      String var3 = null;
      if(var2.length() <= 0) {
         return LastHttpContent.EMPTY_LAST_CONTENT;
      } else {
         DefaultLastHttpContent var4 = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER, this.validateHeaders);

         do {
            char var5 = var2.charAt(0);
            if(var3 != null && (var5 == 32 || var5 == 9)) {
               List var9 = var4.trailingHeaders().getAll(var3);
               if(!var9.isEmpty()) {
                  int var10 = var9.size() - 1;
                  String var8 = (String)var9.get(var10) + var2.toString().trim();
                  var9.set(var10, var8);
               }
            } else {
               String[] var6 = splitHeader(var2);
               String var7 = var6[0];
               if(!HttpHeaders.equalsIgnoreCase(var7, "Content-Length") && !HttpHeaders.equalsIgnoreCase(var7, "Transfer-Encoding") && !HttpHeaders.equalsIgnoreCase(var7, "Trailer")) {
                  var4.trailingHeaders().add((String)var7, (Object)var6[1]);
               }

               var3 = var7;
            }

            var2 = this.headerParser.parse(var1);
         } while(var2.length() > 0);

         return var4;
      }
   }

   protected abstract boolean isDecodingRequest();

   protected abstract HttpMessage createMessage(String[] var1) throws Exception;

   protected abstract HttpMessage createInvalidMessage();

   private static int getChunkSize(String var0) {
      var0 = var0.trim();

      for(int var1 = 0; var1 < var0.length(); ++var1) {
         char var2 = var0.charAt(var1);
         if(var2 == 59 || Character.isWhitespace(var2) || Character.isISOControl(var2)) {
            var0 = var0.substring(0, var1);
            break;
         }
      }

      return Integer.parseInt(var0, 16);
   }

   private static String[] splitInitialLine(AppendableCharSequence var0) {
      int var1 = findNonWhitespace(var0, 0);
      int var2 = findWhitespace(var0, var1);
      int var3 = findNonWhitespace(var0, var2);
      int var4 = findWhitespace(var0, var3);
      int var5 = findNonWhitespace(var0, var4);
      int var6 = findEndOfString(var0);
      return new String[]{var0.substring(var1, var2), var0.substring(var3, var4), var5 < var6?var0.substring(var5, var6):""};
   }

   private static String[] splitHeader(AppendableCharSequence var0) {
      int var1 = var0.length();
      int var2 = findNonWhitespace(var0, 0);

      int var3;
      for(var3 = var2; var3 < var1; ++var3) {
         char var7 = var0.charAt(var3);
         if(var7 == 58 || Character.isWhitespace(var7)) {
            break;
         }
      }

      int var4;
      for(var4 = var3; var4 < var1; ++var4) {
         if(var0.charAt(var4) == 58) {
            ++var4;
            break;
         }
      }

      int var5 = findNonWhitespace(var0, var4);
      if(var5 == var1) {
         return new String[]{var0.substring(var2, var3), ""};
      } else {
         int var6 = findEndOfString(var0);
         return new String[]{var0.substring(var2, var3), var0.substring(var5, var6)};
      }
   }

   private static int findNonWhitespace(CharSequence var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && Character.isWhitespace(var0.charAt(var2)); ++var2) {
         ;
      }

      return var2;
   }

   private static int findWhitespace(CharSequence var0, int var1) {
      int var2;
      for(var2 = var1; var2 < var0.length() && !Character.isWhitespace(var0.charAt(var2)); ++var2) {
         ;
      }

      return var2;
   }

   private static int findEndOfString(CharSequence var0) {
      int var1;
      for(var1 = var0.length(); var1 > 0 && Character.isWhitespace(var0.charAt(var1 - 1)); --var1) {
         ;
      }

      return var1;
   }

   // $FF: synthetic method
   static int access$008(HttpObjectDecoder var0) {
      return var0.headerSize++;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State = new int[HttpObjectDecoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.SKIP_CONTROL_CHARS.ordinal()] = 1;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_INITIAL.ordinal()] = 2;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_HEADER.ordinal()] = 3;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_VARIABLE_LENGTH_CONTENT.ordinal()] = 4;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_FIXED_LENGTH_CONTENT.ordinal()] = 5;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_CHUNK_SIZE.ordinal()] = 6;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_CHUNKED_CONTENT.ordinal()] = 7;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_CHUNK_DELIMITER.ordinal()] = 8;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.READ_CHUNK_FOOTER.ordinal()] = 9;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.BAD_MESSAGE.ordinal()] = 10;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpObjectDecoder$State[HttpObjectDecoder.State.UPGRADED.ordinal()] = 11;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private final class LineParser implements ByteBufProcessor {
      private final AppendableCharSequence seq;
      private int size;

      LineParser(AppendableCharSequence var2) {
         this.seq = var2;
      }

      public AppendableCharSequence parse(ByteBuf var1) {
         this.seq.reset();
         this.size = 0;
         int var2 = var1.forEachByte(this);
         var1.readerIndex(var2 + 1);
         return this.seq;
      }

      public boolean process(byte var1) throws Exception {
         char var2 = (char)var1;
         if(var2 == 13) {
            return true;
         } else if(var2 == 10) {
            return false;
         } else if(this.size >= HttpObjectDecoder.this.maxInitialLineLength) {
            throw new TooLongFrameException("An HTTP line is larger than " + HttpObjectDecoder.this.maxInitialLineLength + " bytes.");
         } else {
            ++this.size;
            this.seq.append(var2);
            return true;
         }
      }
   }

   private final class HeaderParser implements ByteBufProcessor {
      private final AppendableCharSequence seq;

      HeaderParser(AppendableCharSequence var2) {
         this.seq = var2;
      }

      public AppendableCharSequence parse(ByteBuf var1) {
         this.seq.reset();
         HttpObjectDecoder.this.headerSize = 0;
         int var2 = var1.forEachByte(this);
         var1.readerIndex(var2 + 1);
         return this.seq;
      }

      public boolean process(byte var1) throws Exception {
         char var2 = (char)var1;
         HttpObjectDecoder.access$008(HttpObjectDecoder.this);
         if(var2 == 13) {
            return true;
         } else if(var2 == 10) {
            return false;
         } else if(HttpObjectDecoder.this.headerSize >= HttpObjectDecoder.this.maxHeaderSize) {
            throw new TooLongFrameException("HTTP header is larger than " + HttpObjectDecoder.this.maxHeaderSize + " bytes.");
         } else {
            this.seq.append(var2);
            return true;
         }
      }
   }

   static enum State {
      SKIP_CONTROL_CHARS,
      READ_INITIAL,
      READ_HEADER,
      READ_VARIABLE_LENGTH_CONTENT,
      READ_FIXED_LENGTH_CONTENT,
      READ_CHUNK_SIZE,
      READ_CHUNKED_CONTENT,
      READ_CHUNK_DELIMITER,
      READ_CHUNK_FOOTER,
      BAD_MESSAGE,
      UPGRADED;

      private State() {
      }
   }
}
