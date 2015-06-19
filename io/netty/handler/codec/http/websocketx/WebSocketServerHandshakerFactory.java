package io.netty.handler.codec.http.websocketx;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker00;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker07;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker08;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker13;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

public class WebSocketServerHandshakerFactory {
   private final String webSocketURL;
   private final String subprotocols;
   private final boolean allowExtensions;
   private final int maxFramePayloadLength;

   public WebSocketServerHandshakerFactory(String var1, String var2, boolean var3) {
      this(var1, var2, var3, 65536);
   }

   public WebSocketServerHandshakerFactory(String var1, String var2, boolean var3, int var4) {
      this.webSocketURL = var1;
      this.subprotocols = var2;
      this.allowExtensions = var3;
      this.maxFramePayloadLength = var4;
   }

   public WebSocketServerHandshaker newHandshaker(HttpRequest var1) {
      String var2 = var1.headers().get("Sec-WebSocket-Version");
      return (WebSocketServerHandshaker)(var2 != null?(var2.equals(WebSocketVersion.V13.toHttpHeaderValue())?new WebSocketServerHandshaker13(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength):(var2.equals(WebSocketVersion.V08.toHttpHeaderValue())?new WebSocketServerHandshaker08(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength):(var2.equals(WebSocketVersion.V07.toHttpHeaderValue())?new WebSocketServerHandshaker07(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength):null))):new WebSocketServerHandshaker00(this.webSocketURL, this.subprotocols, this.maxFramePayloadLength));
   }

   /** @deprecated */
   @Deprecated
   public static void sendUnsupportedWebSocketVersionResponse(Channel var0) {
      sendUnsupportedVersionResponse(var0);
   }

   public static ChannelFuture sendUnsupportedVersionResponse(Channel var0) {
      return sendUnsupportedVersionResponse(var0, var0.newPromise());
   }

   public static ChannelFuture sendUnsupportedVersionResponse(Channel var0, ChannelPromise var1) {
      DefaultHttpResponse var2 = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UPGRADE_REQUIRED);
      var2.headers().set((String)"Sec-WebSocket-Version", (Object)WebSocketVersion.V13.toHttpHeaderValue());
      return var0.write(var2, var1);
   }
}
