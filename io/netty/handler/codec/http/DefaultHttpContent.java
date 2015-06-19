package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.DefaultHttpObject;
import io.netty.handler.codec.http.HttpContent;
import io.netty.util.ReferenceCounted;
import io.netty.util.internal.StringUtil;

public class DefaultHttpContent extends DefaultHttpObject implements HttpContent {
   private final ByteBuf content;

   public DefaultHttpContent(ByteBuf var1) {
      if(var1 == null) {
         throw new NullPointerException("content");
      } else {
         this.content = var1;
      }
   }

   public ByteBuf content() {
      return this.content;
   }

   public HttpContent copy() {
      return new DefaultHttpContent(this.content.copy());
   }

   public HttpContent duplicate() {
      return new DefaultHttpContent(this.content.duplicate());
   }

   public int refCnt() {
      return this.content.refCnt();
   }

   public HttpContent retain() {
      this.content.retain();
      return this;
   }

   public HttpContent retain(int var1) {
      this.content.retain(var1);
      return this;
   }

   public boolean release() {
      return this.content.release();
   }

   public boolean release(int var1) {
      return this.content.release(var1);
   }

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "(data: " + this.content() + ", decoderResult: " + this.getDecoderResult() + ')';
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
