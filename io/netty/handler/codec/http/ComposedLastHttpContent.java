package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCounted;

final class ComposedLastHttpContent implements LastHttpContent {
   private final HttpHeaders trailingHeaders;
   private DecoderResult result;

   ComposedLastHttpContent(HttpHeaders var1) {
      this.trailingHeaders = var1;
   }

   public HttpHeaders trailingHeaders() {
      return this.trailingHeaders;
   }

   public LastHttpContent copy() {
      DefaultLastHttpContent var1 = new DefaultLastHttpContent(Unpooled.EMPTY_BUFFER);
      var1.trailingHeaders().set(this.trailingHeaders());
      return var1;
   }

   public LastHttpContent retain(int var1) {
      return this;
   }

   public LastHttpContent retain() {
      return this;
   }

   public HttpContent duplicate() {
      return this.copy();
   }

   public ByteBuf content() {
      return Unpooled.EMPTY_BUFFER;
   }

   public DecoderResult getDecoderResult() {
      return this.result;
   }

   public void setDecoderResult(DecoderResult var1) {
      this.result = var1;
   }

   public int refCnt() {
      return 1;
   }

   public boolean release() {
      return false;
   }

   public boolean release(int var1) {
      return false;
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
}
