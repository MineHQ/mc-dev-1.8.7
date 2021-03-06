package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCounted;

public class BinaryWebSocketFrame extends WebSocketFrame {
   public BinaryWebSocketFrame() {
      super(Unpooled.buffer(0));
   }

   public BinaryWebSocketFrame(ByteBuf var1) {
      super(var1);
   }

   public BinaryWebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var1, var2, var3);
   }

   public BinaryWebSocketFrame copy() {
      return new BinaryWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
   }

   public BinaryWebSocketFrame duplicate() {
      return new BinaryWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
   }

   public BinaryWebSocketFrame retain() {
      super.retain();
      return this;
   }

   public BinaryWebSocketFrame retain(int var1) {
      super.retain(var1);
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public WebSocketFrame retain(int var1) {
      return this.retain(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public WebSocketFrame retain() {
      return this.retain();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public WebSocketFrame duplicate() {
      return this.duplicate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public WebSocketFrame copy() {
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
