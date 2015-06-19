package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObjectEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class HttpResponseEncoder extends HttpObjectEncoder<HttpResponse> {
   private static final byte[] CRLF = new byte[]{(byte)13, (byte)10};

   public HttpResponseEncoder() {
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return super.acceptOutboundMessage(var1) && !(var1 instanceof HttpRequest);
   }

   protected void encodeInitialLine(ByteBuf var1, HttpResponse var2) throws Exception {
      var2.getProtocolVersion().encode(var1);
      var1.writeByte(32);
      var2.getStatus().encode(var1);
      var1.writeBytes(CRLF);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpResponse)var2);
   }
}
