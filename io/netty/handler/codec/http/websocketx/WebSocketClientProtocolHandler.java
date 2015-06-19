package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandshakeHandler;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import java.net.URI;
import java.util.List;

public class WebSocketClientProtocolHandler extends WebSocketProtocolHandler {
   private final WebSocketClientHandshaker handshaker;
   private final boolean handleCloseFrames;

   public WebSocketClientProtocolHandler(URI var1, WebSocketVersion var2, String var3, boolean var4, HttpHeaders var5, int var6, boolean var7) {
      this(WebSocketClientHandshakerFactory.newHandshaker(var1, var2, var3, var4, var5, var6), var7);
   }

   public WebSocketClientProtocolHandler(URI var1, WebSocketVersion var2, String var3, boolean var4, HttpHeaders var5, int var6) {
      this(var1, var2, var3, var4, var5, var6, true);
   }

   public WebSocketClientProtocolHandler(WebSocketClientHandshaker var1, boolean var2) {
      this.handshaker = var1;
      this.handleCloseFrames = var2;
   }

   public WebSocketClientProtocolHandler(WebSocketClientHandshaker var1) {
      this(var1, true);
   }

   protected void decode(ChannelHandlerContext var1, WebSocketFrame var2, List<Object> var3) throws Exception {
      if(this.handleCloseFrames && var2 instanceof CloseWebSocketFrame) {
         var1.close();
      } else {
         super.decode(var1, var2, var3);
      }
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      ChannelPipeline var2 = var1.pipeline();
      if(var2.get(WebSocketClientProtocolHandshakeHandler.class) == null) {
         var1.pipeline().addBefore(var1.name(), WebSocketClientProtocolHandshakeHandler.class.getName(), new WebSocketClientProtocolHandshakeHandler(this.handshaker));
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      super.exceptionCaught(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (WebSocketFrame)var2, var3);
   }

   public static enum ClientHandshakeStateEvent {
      HANDSHAKE_ISSUED,
      HANDSHAKE_COMPLETE;

      private ClientHandshakeStateEvent() {
      }
   }
}
