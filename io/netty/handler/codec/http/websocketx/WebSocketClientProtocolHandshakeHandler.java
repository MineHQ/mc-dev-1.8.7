package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.util.concurrent.Future;

class WebSocketClientProtocolHandshakeHandler extends ChannelInboundHandlerAdapter {
   private final WebSocketClientHandshaker handshaker;

   WebSocketClientProtocolHandshakeHandler(WebSocketClientHandshaker var1) {
      this.handshaker = var1;
   }

   public void channelActive(final ChannelHandlerContext var1) throws Exception {
      super.channelActive(var1);
      this.handshaker.handshake(var1.channel()).addListener(new ChannelFutureListener() {
         public void operationComplete(ChannelFuture var1x) throws Exception {
            if(!var1x.isSuccess()) {
               var1.fireExceptionCaught(var1x.cause());
            } else {
               var1.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_ISSUED);
            }

         }

         // $FF: synthetic method
         // $FF: bridge method
         public void operationComplete(Future var1x) throws Exception {
            this.operationComplete((ChannelFuture)var1x);
         }
      });
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      if(!(var2 instanceof FullHttpResponse)) {
         var1.fireChannelRead(var2);
      } else if(!this.handshaker.isHandshakeComplete()) {
         this.handshaker.finishHandshake(var1.channel(), (FullHttpResponse)var2);
         var1.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
         var1.pipeline().remove((ChannelHandler)this);
      } else {
         throw new IllegalStateException("WebSocketClientHandshaker should have been non finished yet");
      }
   }
}
