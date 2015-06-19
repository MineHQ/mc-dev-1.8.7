package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;

abstract class WebSocketProtocolHandler extends MessageToMessageDecoder<WebSocketFrame> {
   WebSocketProtocolHandler() {
   }

   protected void decode(ChannelHandlerContext var1, WebSocketFrame var2, List<Object> var3) throws Exception {
      if(var2 instanceof PingWebSocketFrame) {
         var2.content().retain();
         var1.channel().writeAndFlush(new PongWebSocketFrame(var2.content()));
      } else if(!(var2 instanceof PongWebSocketFrame)) {
         var3.add(var2.retain());
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      var1.close();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (WebSocketFrame)var2, var3);
   }
}
