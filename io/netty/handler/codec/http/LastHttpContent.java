package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.ReferenceCounted;

public interface LastHttpContent extends HttpContent {
   LastHttpContent EMPTY_LAST_CONTENT = new LastHttpContent() {
      public ByteBuf content() {
         return Unpooled.EMPTY_BUFFER;
      }

      public LastHttpContent copy() {
         return EMPTY_LAST_CONTENT;
      }

      public LastHttpContent duplicate() {
         return this;
      }

      public HttpHeaders trailingHeaders() {
         return HttpHeaders.EMPTY_HEADERS;
      }

      public DecoderResult getDecoderResult() {
         return DecoderResult.SUCCESS;
      }

      public void setDecoderResult(DecoderResult var1) {
         throw new UnsupportedOperationException("read only");
      }

      public int refCnt() {
         return 1;
      }

      public LastHttpContent retain() {
         return this;
      }

      public LastHttpContent retain(int var1) {
         return this;
      }

      public boolean release() {
         return false;
      }

      public boolean release(int var1) {
         return false;
      }

      public String toString() {
         return "EmptyLastHttpContent";
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
   };

   HttpHeaders trailingHeaders();

   LastHttpContent copy();

   LastHttpContent retain(int var1);

   LastHttpContent retain();
}
