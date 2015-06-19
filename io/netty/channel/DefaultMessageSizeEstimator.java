package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.FileRegion;
import io.netty.channel.MessageSizeEstimator;

public final class DefaultMessageSizeEstimator implements MessageSizeEstimator {
   public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(0);
   private final MessageSizeEstimator.Handle handle;

   public DefaultMessageSizeEstimator(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("unknownSize: " + var1 + " (expected: >= 0)");
      } else {
         this.handle = new DefaultMessageSizeEstimator.HandleImpl(var1);
      }
   }

   public MessageSizeEstimator.Handle newHandle() {
      return this.handle;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class HandleImpl implements MessageSizeEstimator.Handle {
      private final int unknownSize;

      private HandleImpl(int var1) {
         this.unknownSize = var1;
      }

      public int size(Object var1) {
         return var1 instanceof ByteBuf?((ByteBuf)var1).readableBytes():(var1 instanceof ByteBufHolder?((ByteBufHolder)var1).content().readableBytes():(var1 instanceof FileRegion?0:this.unknownSize));
      }

      // $FF: synthetic method
      HandleImpl(int var1, DefaultMessageSizeEstimator.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
