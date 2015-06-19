package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class HttpObjectEncoder<H extends HttpMessage> extends MessageToMessageEncoder<Object> {
   private static final byte[] CRLF = new byte[]{(byte)13, (byte)10};
   private static final byte[] ZERO_CRLF = new byte[]{(byte)48, (byte)13, (byte)10};
   private static final byte[] ZERO_CRLF_CRLF = new byte[]{(byte)48, (byte)13, (byte)10, (byte)13, (byte)10};
   private static final ByteBuf CRLF_BUF;
   private static final ByteBuf ZERO_CRLF_CRLF_BUF;
   private static final int ST_INIT = 0;
   private static final int ST_CONTENT_NON_CHUNK = 1;
   private static final int ST_CONTENT_CHUNK = 2;
   private int state = 0;

   public HttpObjectEncoder() {
   }

   protected void encode(ChannelHandlerContext var1, Object var2, List<Object> var3) throws Exception {
      ByteBuf var4 = null;
      if(var2 instanceof HttpMessage) {
         if(this.state != 0) {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var2));
         }

         HttpMessage var5 = (HttpMessage)var2;
         var4 = var1.alloc().buffer();
         this.encodeInitialLine(var4, var5);
         HttpHeaders.encode(var5.headers(), var4);
         var4.writeBytes(CRLF);
         this.state = HttpHeaders.isTransferEncodingChunked(var5)?2:1;
      }

      if(!(var2 instanceof HttpContent) && !(var2 instanceof ByteBuf) && !(var2 instanceof FileRegion)) {
         if(var4 != null) {
            var3.add(var4);
         }
      } else {
         if(this.state == 0) {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var2));
         }

         long var7 = contentLength(var2);
         if(this.state == 1) {
            if(var7 > 0L) {
               if(var4 != null && (long)var4.writableBytes() >= var7 && var2 instanceof HttpContent) {
                  var4.writeBytes(((HttpContent)var2).content());
                  var3.add(var4);
               } else {
                  if(var4 != null) {
                     var3.add(var4);
                  }

                  var3.add(encodeAndRetain(var2));
               }
            } else if(var4 != null) {
               var3.add(var4);
            } else {
               var3.add(Unpooled.EMPTY_BUFFER);
            }

            if(var2 instanceof LastHttpContent) {
               this.state = 0;
            }
         } else {
            if(this.state != 2) {
               throw new Error();
            }

            if(var4 != null) {
               var3.add(var4);
            }

            this.encodeChunkedContent(var1, var2, var7, var3);
         }
      }

   }

   private void encodeChunkedContent(ChannelHandlerContext var1, Object var2, long var3, List<Object> var5) {
      ByteBuf var7;
      if(var3 > 0L) {
         byte[] var6 = Long.toHexString(var3).getBytes(CharsetUtil.US_ASCII);
         var7 = var1.alloc().buffer(var6.length + 2);
         var7.writeBytes(var6);
         var7.writeBytes(CRLF);
         var5.add(var7);
         var5.add(encodeAndRetain(var2));
         var5.add(CRLF_BUF.duplicate());
      }

      if(var2 instanceof LastHttpContent) {
         HttpHeaders var8 = ((LastHttpContent)var2).trailingHeaders();
         if(var8.isEmpty()) {
            var5.add(ZERO_CRLF_CRLF_BUF.duplicate());
         } else {
            var7 = var1.alloc().buffer();
            var7.writeBytes(ZERO_CRLF);
            HttpHeaders.encode(var8, var7);
            var7.writeBytes(CRLF);
            var5.add(var7);
         }

         this.state = 0;
      } else if(var3 == 0L) {
         var5.add(Unpooled.EMPTY_BUFFER);
      }

   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof HttpObject || var1 instanceof ByteBuf || var1 instanceof FileRegion;
   }

   private static Object encodeAndRetain(Object var0) {
      if(var0 instanceof ByteBuf) {
         return ((ByteBuf)var0).retain();
      } else if(var0 instanceof HttpContent) {
         return ((HttpContent)var0).content().retain();
      } else if(var0 instanceof FileRegion) {
         return ((FileRegion)var0).retain();
      } else {
         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var0));
      }
   }

   private static long contentLength(Object var0) {
      if(var0 instanceof HttpContent) {
         return (long)((HttpContent)var0).content().readableBytes();
      } else if(var0 instanceof ByteBuf) {
         return (long)((ByteBuf)var0).readableBytes();
      } else if(var0 instanceof FileRegion) {
         return ((FileRegion)var0).count();
      } else {
         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var0));
      }
   }

   /** @deprecated */
   @Deprecated
   protected static void encodeAscii(String var0, ByteBuf var1) {
      HttpHeaders.encodeAscii0(var0, var1);
   }

   protected abstract void encodeInitialLine(ByteBuf var1, H var2) throws Exception;

   static {
      CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(CRLF.length).writeBytes(CRLF));
      ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length).writeBytes(ZERO_CRLF_CRLF));
   }
}
