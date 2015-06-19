package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.LastHttpContent;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public final class HttpClientCodec extends CombinedChannelDuplexHandler<HttpResponseDecoder, HttpRequestEncoder> {
   private final Queue<HttpMethod> queue;
   private boolean done;
   private final AtomicLong requestResponseCounter;
   private final boolean failOnMissingResponse;

   public HttpClientCodec() {
      this(4096, 8192, 8192, false);
   }

   public void setSingleDecode(boolean var1) {
      ((HttpResponseDecoder)this.inboundHandler()).setSingleDecode(var1);
   }

   public boolean isSingleDecode() {
      return ((HttpResponseDecoder)this.inboundHandler()).isSingleDecode();
   }

   public HttpClientCodec(int var1, int var2, int var3) {
      this(var1, var2, var3, false);
   }

   public HttpClientCodec(int var1, int var2, int var3, boolean var4) {
      this(var1, var2, var3, var4, true);
   }

   public HttpClientCodec(int var1, int var2, int var3, boolean var4, boolean var5) {
      this.queue = new ArrayDeque();
      this.requestResponseCounter = new AtomicLong();
      this.init(new HttpClientCodec.Decoder(var1, var2, var3, var5), new HttpClientCodec.Encoder());
      this.failOnMissingResponse = var4;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class Decoder extends HttpResponseDecoder {
      Decoder(int var2, int var3, int var4, boolean var5) {
         super(var2, var3, var4, var5);
      }

      protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
         int var4;
         if(HttpClientCodec.this.done) {
            var4 = this.actualReadableBytes();
            if(var4 == 0) {
               return;
            }

            var3.add(var2.readBytes(var4));
         } else {
            var4 = var3.size();
            super.decode(var1, var2, var3);
            if(HttpClientCodec.this.failOnMissingResponse) {
               int var5 = var3.size();

               for(int var6 = var4; var6 < var5; ++var6) {
                  this.decrement(var3.get(var6));
               }
            }
         }

      }

      private void decrement(Object var1) {
         if(var1 != null) {
            if(var1 instanceof LastHttpContent) {
               HttpClientCodec.this.requestResponseCounter.decrementAndGet();
            }

         }
      }

      protected boolean isContentAlwaysEmpty(HttpMessage var1) {
         int var2 = ((HttpResponse)var1).getStatus().code();
         if(var2 == 100) {
            return true;
         } else {
            HttpMethod var3 = (HttpMethod)HttpClientCodec.this.queue.poll();
            char var4 = var3.name().charAt(0);
            switch(var4) {
            case 'C':
               if(var2 == 200 && HttpMethod.CONNECT.equals(var3)) {
                  HttpClientCodec.this.done = true;
                  HttpClientCodec.this.queue.clear();
                  return true;
               }
               break;
            case 'H':
               if(HttpMethod.HEAD.equals(var3)) {
                  return true;
               }
            }

            return super.isContentAlwaysEmpty(var1);
         }
      }

      public void channelInactive(ChannelHandlerContext var1) throws Exception {
         super.channelInactive(var1);
         if(HttpClientCodec.this.failOnMissingResponse) {
            long var2 = HttpClientCodec.this.requestResponseCounter.get();
            if(var2 > 0L) {
               var1.fireExceptionCaught(new PrematureChannelClosureException("channel gone inactive with " + var2 + " missing response(s)"));
            }
         }

      }
   }

   private final class Encoder extends HttpRequestEncoder {
      private Encoder() {
      }

      protected void encode(ChannelHandlerContext var1, Object var2, List<Object> var3) throws Exception {
         if(var2 instanceof HttpRequest && !HttpClientCodec.this.done) {
            HttpClientCodec.this.queue.offer(((HttpRequest)var2).getMethod());
         }

         super.encode(var1, var2, var3);
         if(HttpClientCodec.this.failOnMissingResponse && var2 instanceof LastHttpContent) {
            HttpClientCodec.this.requestResponseCounter.incrementAndGet();
         }

      }

      // $FF: synthetic method
      Encoder(HttpClientCodec.SyntheticClass_1 var2) {
         this();
      }
   }
}
