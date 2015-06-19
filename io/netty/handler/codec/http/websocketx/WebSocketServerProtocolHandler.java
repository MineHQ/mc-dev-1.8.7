package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandshakeHandler;
import io.netty.util.AttributeKey;
import java.util.List;

public class WebSocketServerProtocolHandler extends WebSocketProtocolHandler {
   private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class.getName() + ".HANDSHAKER");
   private final String websocketPath;
   private final String subprotocols;
   private final boolean allowExtensions;
   private final int maxFramePayloadLength;

   public WebSocketServerProtocolHandler(String var1) {
      this(var1, (String)null, false);
   }

   public WebSocketServerProtocolHandler(String var1, String var2) {
      this(var1, var2, false);
   }

   public WebSocketServerProtocolHandler(String var1, String var2, boolean var3) {
      this(var1, var2, var3, 65536);
   }

   public WebSocketServerProtocolHandler(String var1, String var2, boolean var3, int var4) {
      this.websocketPath = var1;
      this.subprotocols = var2;
      this.allowExtensions = var3;
      this.maxFramePayloadLength = var4;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      ChannelPipeline var2 = var1.pipeline();
      if(var2.get(WebSocketServerProtocolHandshakeHandler.class) == null) {
         var1.pipeline().addBefore(var1.name(), WebSocketServerProtocolHandshakeHandler.class.getName(), new WebSocketServerProtocolHandshakeHandler(this.websocketPath, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength));
      }

   }

   protected void decode(ChannelHandlerContext var1, WebSocketFrame var2, List<Object> var3) throws Exception {
      if(var2 instanceof CloseWebSocketFrame) {
         WebSocketServerHandshaker var4 = getHandshaker(var1);
         if(var4 != null) {
            var2.retain();
            var4.close(var1.channel(), (CloseWebSocketFrame)var2);
         } else {
            var1.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
         }

      } else {
         super.decode(var1, var2, var3);
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if(var2 instanceof WebSocketHandshakeException) {
         DefaultFullHttpResponse var3 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(var2.getMessage().getBytes()));
         var1.channel().writeAndFlush(var3).addListener(ChannelFutureListener.CLOSE);
      } else {
         var1.close();
      }

   }

   static WebSocketServerHandshaker getHandshaker(ChannelHandlerContext var0) {
      return (WebSocketServerHandshaker)var0.attr(HANDSHAKER_ATTR_KEY).get();
   }

   static void setHandshaker(ChannelHandlerContext var0, WebSocketServerHandshaker var1) {
      var0.attr(HANDSHAKER_ATTR_KEY).set(var1);
   }

   static ChannelHandler forbiddenHttpRequestResponder() {
      return new ChannelInboundHandlerAdapter() {
         public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
            if(var2 instanceof FullHttpRequest) {
               ((FullHttpRequest)var2).release();
               DefaultFullHttpResponse var3 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
               var1.channel().writeAndFlush(var3);
            } else {
               var1.fireChannelRead(var2);
            }

         }
      };
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (WebSocketFrame)var2, var3);
   }

   public static enum ServerHandshakeStateEvent {
      HANDSHAKE_COMPLETE;

      private ServerHandshakeStateEvent() {
      }
   }
}
