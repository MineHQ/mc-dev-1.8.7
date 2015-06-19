package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCounted;

public class ContinuationWebSocketFrame extends WebSocketFrame {
   public ContinuationWebSocketFrame() {
      this(Unpooled.buffer(0));
   }

   public ContinuationWebSocketFrame(ByteBuf var1) {
      super(var1);
   }

   public ContinuationWebSocketFrame(boolean var1, int var2, ByteBuf var3) {
      super(var1, var2, var3);
   }

   public ContinuationWebSocketFrame(boolean var1, int var2, String var3) {
      this(var1, var2, fromText(var3));
   }

   public String text() {
      return this.content().toString(CharsetUtil.UTF_8);
   }

   private static ByteBuf fromText(String var0) {
      return var0 != null && !var0.isEmpty()?Unpooled.copiedBuffer((CharSequence)var0, CharsetUtil.UTF_8):Unpooled.EMPTY_BUFFER;
   }

   public ContinuationWebSocketFrame copy() {
      return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().copy());
   }

   public ContinuationWebSocketFrame duplicate() {
      return new ContinuationWebSocketFrame(this.isFinalFragment(), this.rsv(), this.content().duplicate());
   }

   public ContinuationWebSocketFrame retain() {
      super.retain();
      return this;
   }

   public ContinuationWebSocketFrame retain(int var1) {
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
