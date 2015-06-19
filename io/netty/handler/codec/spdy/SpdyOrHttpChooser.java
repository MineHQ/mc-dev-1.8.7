package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdyHttpDecoder;
import io.netty.handler.codec.spdy.SpdyHttpEncoder;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.internal.StringUtil;
import java.util.List;
import javax.net.ssl.SSLEngine;

public abstract class SpdyOrHttpChooser extends ByteToMessageDecoder {
   private final int maxSpdyContentLength;
   private final int maxHttpContentLength;

   protected SpdyOrHttpChooser(int var1, int var2) {
      this.maxSpdyContentLength = var1;
      this.maxHttpContentLength = var2;
   }

   protected SpdyOrHttpChooser.SelectedProtocol getProtocol(SSLEngine var1) {
      String[] var2 = StringUtil.split(var1.getSession().getProtocol(), ':');
      if(var2.length < 2) {
         return SpdyOrHttpChooser.SelectedProtocol.HTTP_1_1;
      } else {
         SpdyOrHttpChooser.SelectedProtocol var3 = SpdyOrHttpChooser.SelectedProtocol.protocol(var2[1]);
         return var3;
      }
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List<Object> var3) throws Exception {
      if(this.initPipeline(var1)) {
         var1.pipeline().remove((ChannelHandler)this);
      }

   }

   private boolean initPipeline(ChannelHandlerContext var1) {
      SslHandler var2 = (SslHandler)var1.pipeline().get(SslHandler.class);
      if(var2 == null) {
         throw new IllegalStateException("SslHandler is needed for SPDY");
      } else {
         SpdyOrHttpChooser.SelectedProtocol var3 = this.getProtocol(var2.engine());
         switch(SpdyOrHttpChooser.SyntheticClass_1.$SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol[var3.ordinal()]) {
         case 1:
            return false;
         case 2:
            this.addSpdyHandlers(var1, SpdyVersion.SPDY_3_1);
            break;
         case 3:
         case 4:
            this.addHttpHandlers(var1);
            break;
         default:
            throw new IllegalStateException("Unknown SelectedProtocol");
         }

         return true;
      }
   }

   protected void addSpdyHandlers(ChannelHandlerContext var1, SpdyVersion var2) {
      ChannelPipeline var3 = var1.pipeline();
      var3.addLast((String)"spdyFrameCodec", (ChannelHandler)(new SpdyFrameCodec(var2)));
      var3.addLast((String)"spdySessionHandler", (ChannelHandler)(new SpdySessionHandler(var2, true)));
      var3.addLast((String)"spdyHttpEncoder", (ChannelHandler)(new SpdyHttpEncoder(var2)));
      var3.addLast((String)"spdyHttpDecoder", (ChannelHandler)(new SpdyHttpDecoder(var2, this.maxSpdyContentLength)));
      var3.addLast((String)"spdyStreamIdHandler", (ChannelHandler)(new SpdyHttpResponseStreamIdHandler()));
      var3.addLast((String)"httpRequestHandler", (ChannelHandler)this.createHttpRequestHandlerForSpdy());
   }

   protected void addHttpHandlers(ChannelHandlerContext var1) {
      ChannelPipeline var2 = var1.pipeline();
      var2.addLast((String)"httpRequestDecoder", (ChannelHandler)(new HttpRequestDecoder()));
      var2.addLast((String)"httpResponseEncoder", (ChannelHandler)(new HttpResponseEncoder()));
      var2.addLast((String)"httpChunkAggregator", (ChannelHandler)(new HttpObjectAggregator(this.maxHttpContentLength)));
      var2.addLast((String)"httpRequestHandler", (ChannelHandler)this.createHttpRequestHandlerForHttp());
   }

   protected abstract ChannelInboundHandler createHttpRequestHandlerForHttp();

   protected ChannelInboundHandler createHttpRequestHandlerForSpdy() {
      return this.createHttpRequestHandlerForHttp();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol = new int[SpdyOrHttpChooser.SelectedProtocol.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol[SpdyOrHttpChooser.SelectedProtocol.UNKNOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol[SpdyOrHttpChooser.SelectedProtocol.SPDY_3_1.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol[SpdyOrHttpChooser.SelectedProtocol.HTTP_1_0.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$codec$spdy$SpdyOrHttpChooser$SelectedProtocol[SpdyOrHttpChooser.SelectedProtocol.HTTP_1_1.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum SelectedProtocol {
      SPDY_3_1("spdy/3.1"),
      HTTP_1_1("http/1.1"),
      HTTP_1_0("http/1.0"),
      UNKNOWN("Unknown");

      private final String name;

      private SelectedProtocol(String var3) {
         this.name = var3;
      }

      public String protocolName() {
         return this.name;
      }

      public static SpdyOrHttpChooser.SelectedProtocol protocol(String var0) {
         SpdyOrHttpChooser.SelectedProtocol[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            SpdyOrHttpChooser.SelectedProtocol var4 = var1[var3];
            if(var4.protocolName().equals(var0)) {
               return var4;
            }
         }

         return UNKNOWN;
      }
   }
}
