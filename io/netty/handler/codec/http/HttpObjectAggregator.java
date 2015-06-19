package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCounted;
import io.netty.util.concurrent.Future;
import java.util.List;

public class HttpObjectAggregator extends MessageToMessageDecoder<HttpObject> {
   public static final int DEFAULT_MAX_COMPOSITEBUFFER_COMPONENTS = 1024;
   private static final FullHttpResponse CONTINUE;
   private final int maxContentLength;
   private HttpObjectAggregator.AggregatedFullHttpMessage currentMessage;
   private boolean tooLongFrameFound;
   private int maxCumulationBufferComponents = 1024;
   private ChannelHandlerContext ctx;

   public HttpObjectAggregator(int var1) {
      if(var1 <= 0) {
         throw new IllegalArgumentException("maxContentLength must be a positive integer: " + var1);
      } else {
         this.maxContentLength = var1;
      }
   }

   public final int getMaxCumulationBufferComponents() {
      return this.maxCumulationBufferComponents;
   }

   public final void setMaxCumulationBufferComponents(int var1) {
      if(var1 < 2) {
         throw new IllegalArgumentException("maxCumulationBufferComponents: " + var1 + " (expected: >= 2)");
      } else if(this.ctx == null) {
         this.maxCumulationBufferComponents = var1;
      } else {
         throw new IllegalStateException("decoder properties cannot be changed once the decoder is added to a pipeline.");
      }
   }

   protected void decode(final ChannelHandlerContext var1, HttpObject var2, List<Object> var3) throws Exception {
      HttpObjectAggregator.AggregatedFullHttpMessage var4 = this.currentMessage;
      if(var2 instanceof HttpMessage) {
         this.tooLongFrameFound = false;

         assert var4 == null;

         HttpMessage var5 = (HttpMessage)var2;
         if(HttpHeaders.is100ContinueExpected(var5)) {
            var1.writeAndFlush(CONTINUE).addListener(new ChannelFutureListener() {
               public void operationComplete(ChannelFuture var1x) throws Exception {
                  if(!var1x.isSuccess()) {
                     var1.fireExceptionCaught(var1x.cause());
                  }

               }

               // $FF: synthetic method
               // $FF: bridge method
               public void operationComplete(Future var1x) throws Exception {
                  this.operationComplete((ChannelFuture)var1x);
               }
            });
         }

         if(!var5.getDecoderResult().isSuccess()) {
            HttpHeaders.removeTransferEncodingChunked(var5);
            var3.add(toFullMessage(var5));
            this.currentMessage = null;
            return;
         }

         Object var9;
         if(var2 instanceof HttpRequest) {
            HttpRequest var6 = (HttpRequest)var2;
            this.currentMessage = (HttpObjectAggregator.AggregatedFullHttpMessage)(var9 = new HttpObjectAggregator.AggregatedFullHttpRequest(var6, var1.alloc().compositeBuffer(this.maxCumulationBufferComponents), (HttpHeaders)null, null));
         } else {
            if(!(var2 instanceof HttpResponse)) {
               throw new Error();
            }

            HttpResponse var11 = (HttpResponse)var2;
            this.currentMessage = (HttpObjectAggregator.AggregatedFullHttpMessage)(var9 = new HttpObjectAggregator.AggregatedFullHttpResponse(var11, Unpooled.compositeBuffer(this.maxCumulationBufferComponents), (HttpHeaders)null, null));
         }

         HttpHeaders.removeTransferEncodingChunked((HttpMessage)var9);
      } else {
         if(!(var2 instanceof HttpContent)) {
            throw new Error();
         }

         if(this.tooLongFrameFound) {
            if(var2 instanceof LastHttpContent) {
               this.currentMessage = null;
            }

            return;
         }

         assert var4 != null;

         HttpContent var10 = (HttpContent)var2;
         CompositeByteBuf var12 = (CompositeByteBuf)var4.content();
         if(var12.readableBytes() > this.maxContentLength - var10.content().readableBytes()) {
            this.tooLongFrameFound = true;
            var4.release();
            this.currentMessage = null;
            throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
         }

         if(var10.content().isReadable()) {
            var10.retain();
            var12.addComponent(var10.content());
            var12.writerIndex(var12.writerIndex() + var10.content().readableBytes());
         }

         boolean var7;
         if(!var10.getDecoderResult().isSuccess()) {
            var4.setDecoderResult(DecoderResult.failure(var10.getDecoderResult().cause()));
            var7 = true;
         } else {
            var7 = var10 instanceof LastHttpContent;
         }

         if(var7) {
            this.currentMessage = null;
            if(var10 instanceof LastHttpContent) {
               LastHttpContent var8 = (LastHttpContent)var10;
               var4.setTrailingHeaders(var8.trailingHeaders());
            } else {
               var4.setTrailingHeaders(new DefaultHttpHeaders());
            }

            var4.headers().set((String)"Content-Length", (Object)String.valueOf(var12.readableBytes()));
            var3.add(var4);
         }
      }

   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      super.channelInactive(var1);
      if(this.currentMessage != null) {
         this.currentMessage.release();
         this.currentMessage = null;
      }

   }

   public void handlerAdded(ChannelHandlerContext var1) throws Exception {
      this.ctx = var1;
   }

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      super.handlerRemoved(var1);
      if(this.currentMessage != null) {
         this.currentMessage.release();
         this.currentMessage = null;
      }

   }

   private static FullHttpMessage toFullMessage(HttpMessage var0) {
      if(var0 instanceof FullHttpMessage) {
         return ((FullHttpMessage)var0).retain();
      } else {
         Object var1;
         if(var0 instanceof HttpRequest) {
            var1 = new HttpObjectAggregator.AggregatedFullHttpRequest((HttpRequest)var0, Unpooled.EMPTY_BUFFER, new DefaultHttpHeaders(), null);
         } else {
            if(!(var0 instanceof HttpResponse)) {
               throw new IllegalStateException();
            }

            var1 = new HttpObjectAggregator.AggregatedFullHttpResponse((HttpResponse)var0, Unpooled.EMPTY_BUFFER, new DefaultHttpHeaders(), null);
         }

         return (FullHttpMessage)var1;
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (HttpObject)var2, var3);
   }

   static {
      CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
   }

   private static final class AggregatedFullHttpResponse extends HttpObjectAggregator.AggregatedFullHttpMessage implements FullHttpResponse {
      private AggregatedFullHttpResponse(HttpResponse var1, ByteBuf var2, HttpHeaders var3) {
         super(var1, var2, var3, null);
      }

      public FullHttpResponse copy() {
         DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().copy());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpResponse duplicate() {
         DefaultFullHttpResponse var1 = new DefaultFullHttpResponse(this.getProtocolVersion(), this.getStatus(), this.content().duplicate());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpResponse setStatus(HttpResponseStatus var1) {
         ((HttpResponse)this.message).setStatus(var1);
         return this;
      }

      public HttpResponseStatus getStatus() {
         return ((HttpResponse)this.message).getStatus();
      }

      public FullHttpResponse setProtocolVersion(HttpVersion var1) {
         super.setProtocolVersion(var1);
         return this;
      }

      public FullHttpResponse retain(int var1) {
         super.retain(var1);
         return this;
      }

      public FullHttpResponse retain() {
         super.retain();
         return this;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpResponse setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpResponse setStatus(HttpResponseStatus var1) {
         return this.setStatus(var1);
      }

      // $FF: synthetic method
      AggregatedFullHttpResponse(HttpResponse var1, ByteBuf var2, HttpHeaders var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static final class AggregatedFullHttpRequest extends HttpObjectAggregator.AggregatedFullHttpMessage implements FullHttpRequest {
      private AggregatedFullHttpRequest(HttpRequest var1, ByteBuf var2, HttpHeaders var3) {
         super(var1, var2, var3, null);
      }

      public FullHttpRequest copy() {
         DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().copy());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpRequest duplicate() {
         DefaultFullHttpRequest var1 = new DefaultFullHttpRequest(this.getProtocolVersion(), this.getMethod(), this.getUri(), this.content().duplicate());
         var1.headers().set(this.headers());
         var1.trailingHeaders().set(this.trailingHeaders());
         return var1;
      }

      public FullHttpRequest retain(int var1) {
         super.retain(var1);
         return this;
      }

      public FullHttpRequest retain() {
         super.retain();
         return this;
      }

      public FullHttpRequest setMethod(HttpMethod var1) {
         ((HttpRequest)this.message).setMethod(var1);
         return this;
      }

      public FullHttpRequest setUri(String var1) {
         ((HttpRequest)this.message).setUri(var1);
         return this;
      }

      public HttpMethod getMethod() {
         return ((HttpRequest)this.message).getMethod();
      }

      public String getUri() {
         return ((HttpRequest)this.message).getUri();
      }

      public FullHttpRequest setProtocolVersion(HttpVersion var1) {
         super.setProtocolVersion(var1);
         return this;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public FullHttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpRequest setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpRequest setUri(String var1) {
         return this.setUri(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpRequest setMethod(HttpMethod var1) {
         return this.setMethod(var1);
      }

      // $FF: synthetic method
      AggregatedFullHttpRequest(HttpRequest var1, ByteBuf var2, HttpHeaders var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private abstract static class AggregatedFullHttpMessage extends DefaultByteBufHolder implements FullHttpMessage {
      protected final HttpMessage message;
      private HttpHeaders trailingHeaders;

      private AggregatedFullHttpMessage(HttpMessage var1, ByteBuf var2, HttpHeaders var3) {
         super(var2);
         this.message = var1;
         this.trailingHeaders = var3;
      }

      public HttpHeaders trailingHeaders() {
         return this.trailingHeaders;
      }

      public void setTrailingHeaders(HttpHeaders var1) {
         this.trailingHeaders = var1;
      }

      public HttpVersion getProtocolVersion() {
         return this.message.getProtocolVersion();
      }

      public FullHttpMessage setProtocolVersion(HttpVersion var1) {
         this.message.setProtocolVersion(var1);
         return this;
      }

      public HttpHeaders headers() {
         return this.message.headers();
      }

      public DecoderResult getDecoderResult() {
         return this.message.getDecoderResult();
      }

      public void setDecoderResult(DecoderResult var1) {
         this.message.setDecoderResult(var1);
      }

      public FullHttpMessage retain(int var1) {
         super.retain(var1);
         return this;
      }

      public FullHttpMessage retain() {
         super.retain();
         return this;
      }

      public abstract FullHttpMessage copy();

      public abstract FullHttpMessage duplicate();

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ByteBufHolder copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public ReferenceCounted retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpMessage setProtocolVersion(HttpVersion var1) {
         return this.setProtocolVersion(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public LastHttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain(int var1) {
         return this.retain(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent retain() {
         return this.retain();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent duplicate() {
         return this.duplicate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public HttpContent copy() {
         return this.copy();
      }

      // $FF: synthetic method
      AggregatedFullHttpMessage(HttpMessage var1, ByteBuf var2, HttpHeaders var3, Object var4) {
         this(var1, var2, var3);
      }
   }
}
