package io.netty.handler.codec.rtsp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.rtsp.RtspObjectEncoder;
import io.netty.util.CharsetUtil;

public class RtspRequestEncoder extends RtspObjectEncoder<HttpRequest> {
   private static final byte[] CRLF = new byte[]{(byte)13, (byte)10};

   public RtspRequestEncoder() {
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof FullHttpRequest;
   }

   protected void encodeInitialLine(ByteBuf var1, HttpRequest var2) throws Exception {
      HttpHeaders.encodeAscii(var2.getMethod().toString(), var1);
      var1.writeByte(32);
      var1.writeBytes(var2.getUri().getBytes(CharsetUtil.UTF_8));
      var1.writeByte(32);
      encodeAscii(var2.getProtocolVersion().toString(), var1);
      var1.writeBytes(CRLF);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception {
      this.encodeInitialLine(var1, (HttpRequest)var2);
   }
}
