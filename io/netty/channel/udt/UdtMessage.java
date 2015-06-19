package io.netty.channel.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.util.ReferenceCounted;

public final class UdtMessage extends DefaultByteBufHolder {
   public UdtMessage(ByteBuf var1) {
      super(var1);
   }

   public UdtMessage copy() {
      return new UdtMessage(this.content().copy());
   }

   public UdtMessage duplicate() {
      return new UdtMessage(this.content().duplicate());
   }

   public UdtMessage retain() {
      super.retain();
      return this;
   }

   public UdtMessage retain(int var1) {
      super.retain(var1);
      return this;
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
