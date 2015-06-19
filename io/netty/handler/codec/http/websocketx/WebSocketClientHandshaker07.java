package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.WebSocket07FrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocket07FrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrameEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketUtil;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.URI;

public class WebSocketClientHandshaker07 extends WebSocketClientHandshaker {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker07.class);
   public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
   private String expectedChallengeResponseString;
   private final boolean allowExtensions;

   public WebSocketClientHandshaker07(URI var1, WebSocketVersion var2, String var3, boolean var4, HttpHeaders var5, int var6) {
      super(var1, var2, var3, var5, var6);
      this.allowExtensions = var4;
   }

   protected FullHttpRequest newHandshakeRequest() {
      URI var1 = this.uri();
      String var2 = var1.getPath();
      if(var1.getQuery() != null && !var1.getQuery().isEmpty()) {
         var2 = var1.getPath() + '?' + var1.getQuery();
      }

      if(var2 == null || var2.isEmpty()) {
         var2 = "/";
      }

      byte[] var3 = WebSocketUtil.randomBytes(16);
      String var4 = WebSocketUtil.base64(var3);
      String var5 = var4 + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      byte[] var6 = WebSocketUtil.sha1(var5.getBytes(CharsetUtil.US_ASCII));
      this.expectedChallengeResponseString = WebSocketUtil.base64(var6);
      if(logger.isDebugEnabled()) {
         logger.debug("WebSocket version 07 client handshake key: {}, expected response: {}", var4, this.expectedChallengeResponseString);
      }

      DefaultFullHttpRequest var7 = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, var2);
      HttpHeaders var8 = var7.headers();
      var8.add((String)"Upgrade", (Object)"WebSocket".toLowerCase()).add((String)"Connection", (Object)"Upgrade").add((String)"Sec-WebSocket-Key", (Object)var4).add((String)"Host", (Object)var1.getHost());
      int var9 = var1.getPort();
      String var10 = "http://" + var1.getHost();
      if(var9 != 80 && var9 != 443) {
         var10 = var10 + ':' + var9;
      }

      var8.add((String)"Sec-WebSocket-Origin", (Object)var10);
      String var11 = this.expectedSubprotocol();
      if(var11 != null && !var11.isEmpty()) {
         var8.add((String)"Sec-WebSocket-Protocol", (Object)var11);
      }

      var8.add((String)"Sec-WebSocket-Version", (Object)"7");
      if(this.customHeaders != null) {
         var8.add(this.customHeaders);
      }

      return var7;
   }

   protected void verify(FullHttpResponse var1) {
      HttpResponseStatus var2 = HttpResponseStatus.SWITCHING_PROTOCOLS;
      HttpHeaders var3 = var1.headers();
      if(!var1.getStatus().equals(var2)) {
         throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + var1.getStatus());
      } else {
         String var4 = var3.get("Upgrade");
         if(!"WebSocket".equalsIgnoreCase(var4)) {
            throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + var4);
         } else {
            String var5 = var3.get("Connection");
            if(!"Upgrade".equalsIgnoreCase(var5)) {
               throw new WebSocketHandshakeException("Invalid handshake response connection: " + var5);
            } else {
               String var6 = var3.get("Sec-WebSocket-Accept");
               if(var6 == null || !var6.equals(this.expectedChallengeResponseString)) {
                  throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", new Object[]{var6, this.expectedChallengeResponseString}));
               }
            }
         }
      }
   }

   protected WebSocketFrameDecoder newWebsocketDecoder() {
      return new WebSocket07FrameDecoder(false, this.allowExtensions, this.maxFramePayloadLength());
   }

   protected WebSocketFrameEncoder newWebSocketEncoder() {
      return new WebSocket07FrameEncoder(true);
   }
}
