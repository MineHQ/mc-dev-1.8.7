package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.CompactObjectInputStream;

public class ObjectDecoder extends LengthFieldBasedFrameDecoder {
   private final ClassResolver classResolver;

   public ObjectDecoder(ClassResolver var1) {
      this(1048576, var1);
   }

   public ObjectDecoder(int var1, ClassResolver var2) {
      super(var1, 0, 4, 0, 4);
      this.classResolver = var2;
   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      ByteBuf var3 = (ByteBuf)super.decode(var1, var2);
      if(var3 == null) {
         return null;
      } else {
         CompactObjectInputStream var4 = new CompactObjectInputStream(new ByteBufInputStream(var3), this.classResolver);
         Object var5 = var4.readObject();
         var4.close();
         return var5;
      }
   }

   protected ByteBuf extractFrame(ChannelHandlerContext var1, ByteBuf var2, int var3, int var4) {
      return var2.slice(var3, var4);
   }
}
