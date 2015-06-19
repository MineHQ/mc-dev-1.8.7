package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.spdy.SpdyHttpHeaders;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.util.ReferenceCountUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SpdyHttpResponseStreamIdHandler extends MessageToMessageCodec<Object, HttpMessage> {
   private static final Integer NO_ID = Integer.valueOf(-1);
   private final Queue<Integer> ids = new LinkedList();

   public SpdyHttpResponseStreamIdHandler() {
   }

   public boolean acceptInboundMessage(Object var1) throws Exception {
      return var1 instanceof HttpMessage || var1 instanceof SpdyRstStreamFrame;
   }

   protected void encode(ChannelHandlerContext var1, HttpMessage var2, List<Object> var3) throws Exception {
      Integer var4 = (Integer)this.ids.poll();
      if(var4 != null && var4.intValue() != NO_ID.intValue() && !var2.headers().contains("X-SPDY-Stream-ID")) {
         SpdyHttpHeaders.setStreamId(var2, var4.intValue());
      }

      var3.add(ReferenceCountUtil.retain(var2));
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List<Object> var3) throws Exception {
      if(var2 instanceof HttpMessage) {
         boolean var4 = ((HttpMessage)var2).headers().contains("X-SPDY-Stream-ID");
         if(!var4) {
            this.ids.add(NO_ID);
         } else {
            this.ids.add(Integer.valueOf(SpdyHttpHeaders.getStreamId((HttpMessage)var2)));
         }
      } else if(var2 instanceof SpdyRstStreamFrame) {
         this.ids.remove(Integer.valueOf(((SpdyRstStreamFrame)var2).streamId()));
      }

      var3.add(ReferenceCountUtil.retain(var2));
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (HttpMessage)var2, var3);
   }
}
