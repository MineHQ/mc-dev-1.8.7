package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;

public class HttpRequestEncoder extends HttpObjectEncoder<HttpRequest> {
   private static final char SLASH = '/';
   private static final char QUESTION_MARK = '?';
   private static final byte[] CRLF = new byte[]{(byte)13, (byte)10};

   public HttpRequestEncoder() {
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return super.acceptOutboundMessage(var1) && !(var1 instanceof HttpResponse);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpRequest var2) throws Exception {
      var2.getMethod().encode(var1);
      var1.writeByte(32);
      String var3 = var2.getUri();
      if(var3.length() == 0) {
         var3 = var3 + '/';
      } else {
         int var4 = var3.indexOf("://");
         if(var4 != -1 && var3.charAt(0) != 47) {
            int var5 = var4 + 3;
            int var6 = var3.indexOf(63, var5);
            if(var6 == -1) {
               if(var3.lastIndexOf(47) <= var5) {
                  var3 = var3 + '/';
               }
            } else if(var3.lastIndexOf(47, var6) <= var5) {
               int var7 = var3.length();
               StringBuilder var8 = new StringBuilder(var7 + 1);
               var8.append(var3, 0, var6);
               var8.append('/');
               var8.append(var3, var6, var7);
               var3 = var8.toString();
            }
         }
      }

      var1.writeBytes(var3.getBytes(CharsetUtil.UTF_8));
      var1.writeByte(32);
      var2.getProtocolVersion().encode(var1);
      var1.writeBytes(CRLF);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpRequest)var2);
   }
}
