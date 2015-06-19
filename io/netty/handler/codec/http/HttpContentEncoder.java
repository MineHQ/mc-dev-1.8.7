package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.ComposedLastHttpContent;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public abstract class HttpContentEncoder extends MessageToMessageCodec<HttpRequest, HttpObject> {
   private final Queue<String> acceptEncodingQueue = new ArrayDeque();
   private String acceptEncoding;
   private EmbeddedChannel encoder;
   private HttpContentEncoder.State state;

   public HttpContentEncoder() {
      this.state = HttpContentEncoder.State.AWAIT_HEADERS;
   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof HttpContent || var1 instanceof HttpResponse;
   }

   protected void decode(ChannelHandlerContext var1, HttpRequest var2, List<Object> var3) throws Exception {
      String var4 = var2.headers().get("Accept-Encoding");
      if(var4 == null) {
         var4 = "identity";
      }

      this.acceptEncodingQueue.add(var4);
      var3.add(ReferenceCountUtil.retain(var2));
   }

   protected void encode(ChannelHandlerContext var1, HttpObject var2, List<Object> var3) throws Exception {
      boolean var4 = var2 instanceof HttpResponse && var2 instanceof LastHttpContent;
      switch(HttpContentEncoder.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$http$HttpContentEncoder$State[this.state.ordinal()]) {
      case 1:
         ensureHeaders(var2);

         assert this.encoder == null;

         HttpResponse var5 = (HttpResponse)var2;
         if(var5.getStatus().code() == 100) {
            if(var4) {
               var3.add(ReferenceCountUtil.retain(var5));
            } else {
               var3.add(var5);
               this.state = HttpContentEncoder.State.PASS_THROUGH;
            }
            break;
         } else {
            this.acceptEncoding = (String)this.acceptEncodingQueue.poll();
            if(this.acceptEncoding == null) {
               throw new IllegalStateException("cannot send more responses than requests");
            }

            if(var4 && !((ByteBufHolder)var5).content().isReadable()) {
               var3.add(ReferenceCountUtil.retain(var5));
               break;
            } else {
               HttpContentEncoder.Result var6 = this.beginEncode(var5, this.acceptEncoding);
               if(var6 == null) {
                  if(var4) {
                     var3.add(ReferenceCountUtil.retain(var5));
                  } else {
                     var3.add(var5);
                     this.state = HttpContentEncoder.State.PASS_THROUGH;
                  }
                  break;
               } else {
                  this.encoder = var6.contentEncoder();
                  var5.headers().set((String)"Content-Encoding", (Object)var6.targetContentEncoding());
                  var5.headers().remove("Content-Length");
                  var5.headers().set((String)"Transfer-Encoding", (Object)"chunked");
                  if(var4) {
                     DefaultHttpResponse var7 = new DefaultHttpResponse(var5.getProtocolVersion(), var5.getStatus());
                     var7.headers().set(var5.headers());
                     var3.add(var7);
                  } else {
                     var3.add(var5);
                     this.state = HttpContentEncoder.State.AWAIT_CONTENT;
                     if(!(var2 instanceof HttpContent)) {
                        break;
                     }
                  }
               }
            }
         }
      case 2:
         ensureContent(var2);
         if(this.encodeContent((HttpContent)var2, var3)) {
            this.state = HttpContentEncoder.State.AWAIT_HEADERS;
         }
         break;
      case 3:
         ensureContent(var2);
         var3.add(ReferenceCountUtil.retain(var2));
         if(var2 instanceof LastHttpContent) {
            this.state = HttpContentEncoder.State.AWAIT_HEADERS;
         }
      }

   }

   private static void ensureHeaders(HttpObject var0) {
      if(!(var0 instanceof HttpResponse)) {
         throw new IllegalStateException("unexpected message type: " + var0.getClass().getName() + " (expected: " + HttpResponse.class.getSimpleName() + ')');
      }
   }

   private static void ensureContent(HttpObject var0) {
      if(!(var0 instanceof HttpContent)) {
         throw new IllegalStateException("unexpected message type: " + var0.getClass().getName() + " (expected: " + HttpContent.class.getSimpleName() + ')');
      }
   }

   private boolean encodeContent(HttpContent var1, List<Object> var2) {
      ByteBuf var3 = var1.content();
      this.encode(var3, var2);
      if(var1 instanceof LastHttpContent) {
         this.finishEncode(var2);
         LastHttpContent var4 = (LastHttpContent)var1;
         HttpHeaders var5 = var4.trailingHeaders();
         if(var5.isEmpty()) {
            var2.add(LastHttpContent.EMPTY_LAST_CONTENT);
         } else {
            var2.add(new ComposedLastHttpContent(var5));
         }

         return true;
      } else {
         return false;
      }
   }

   protected abstract HttpContentEncoder.Result beginEncode(HttpResponse var1, String var2) throws Exception;

   public void handlerRemoved(ChannelHandlerContext var1) throws Exception {
      this.cleanup();
      super.handlerRemoved(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      this.cleanup();
      super.channelInactive(var1);
   }

   private void cleanup() {
      if(this.encoder != null) {
         if(this.encoder.finish()) {
            while(true) {
               ByteBuf var1 = (ByteBuf)this.encoder.readOutbound();
               if(var1 == null) {
                  break;
               }

               var1.release();
            }
         }

         this.encoder = null;
      }

   }

   private void encode(ByteBuf var1, List<Object> var2) {
      this.encoder.writeOutbound(new Object[]{var1.retain()});
      this.fetchEncoderOutput(var2);
   }

   private void finishEncode(List<Object> var1) {
      if(this.encoder.finish()) {
         this.fetchEncoderOutput(var1);
      }

      this.encoder = null;
   }

   private void fetchEncoderOutput(List<Object> var1) {
      while(true) {
         ByteBuf var2 = (ByteBuf)this.encoder.readOutbound();
         if(var2 == null) {
            return;
         }

         if(!var2.isReadable()) {
            var2.release();
         } else {
            var1.add(new DefaultHttpContent(var2));
         }
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (HttpRequest)var2, var3);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (HttpObject)var2, var3);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$http$HttpContentEncoder$State = new int[HttpContentEncoder.State.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$http$HttpContentEncoder$State[HttpContentEncoder.State.AWAIT_HEADERS.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpContentEncoder$State[HttpContentEncoder.State.AWAIT_CONTENT.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$http$HttpContentEncoder$State[HttpContentEncoder.State.PASS_THROUGH.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static final class Result {
      private final String targetContentEncoding;
      private final EmbeddedChannel contentEncoder;

      public Result(String var1, EmbeddedChannel var2) {
         if(var1 == null) {
            throw new NullPointerException("targetContentEncoding");
         } else if(var2 == null) {
            throw new NullPointerException("contentEncoder");
         } else {
            this.targetContentEncoding = var1;
            this.contentEncoder = var2;
         }
      }

      public String targetContentEncoding() {
         return this.targetContentEncoding;
      }

      public EmbeddedChannel contentEncoder() {
         return this.contentEncoder;
      }
   }

   private static enum State {
      PASS_THROUGH,
      AWAIT_HEADERS,
      AWAIT_CONTENT;

      private State() {
      }
   }
}
