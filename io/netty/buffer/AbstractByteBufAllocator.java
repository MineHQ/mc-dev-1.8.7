package io.netty.buffer;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.AdvancedLeakAwareByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.SimpleLeakAwareByteBuf;
import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

public abstract class AbstractByteBufAllocator implements ByteBufAllocator {
   private static final int DEFAULT_INITIAL_CAPACITY = 256;
   private static final int DEFAULT_MAX_COMPONENTS = 16;
   private final boolean directByDefault;
   private final ByteBuf emptyBuf;

   protected static ByteBuf toLeakAwareBuffer(ByteBuf var0) {
      ResourceLeak var1;
      switch(AbstractByteBufAllocator.SyntheticClass_1.$SwitchMap$io$netty$util$ResourceLeakDetector$Level[ResourceLeakDetector.getLevel().ordinal()]) {
      case 1:
         var1 = AbstractByteBuf.leakDetector.open(var0);
         if(var1 != null) {
            var0 = new SimpleLeakAwareByteBuf((ByteBuf)var0, var1);
         }
         break;
      case 2:
      case 3:
         var1 = AbstractByteBuf.leakDetector.open(var0);
         if(var1 != null) {
            var0 = new AdvancedLeakAwareByteBuf((ByteBuf)var0, var1);
         }
      }

      return (ByteBuf)var0;
   }

   protected AbstractByteBufAllocator() {
      this(false);
   }

   protected AbstractByteBufAllocator(boolean var1) {
      this.directByDefault = var1 && PlatformDependent.hasUnsafe();
      this.emptyBuf = new EmptyByteBuf(this);
   }

   public ByteBuf buffer() {
      return this.directByDefault?this.directBuffer():this.heapBuffer();
   }

   public ByteBuf buffer(int var1) {
      return this.directByDefault?this.directBuffer(var1):this.heapBuffer(var1);
   }

   public ByteBuf buffer(int var1, int var2) {
      return this.directByDefault?this.directBuffer(var1, var2):this.heapBuffer(var1, var2);
   }

   public ByteBuf ioBuffer() {
      return PlatformDependent.hasUnsafe()?this.directBuffer(256):this.heapBuffer(256);
   }

   public ByteBuf ioBuffer(int var1) {
      return PlatformDependent.hasUnsafe()?this.directBuffer(var1):this.heapBuffer(var1);
   }

   public ByteBuf ioBuffer(int var1, int var2) {
      return PlatformDependent.hasUnsafe()?this.directBuffer(var1, var2):this.heapBuffer(var1, var2);
   }

   public ByteBuf heapBuffer() {
      return this.heapBuffer(256, Integer.MAX_VALUE);
   }

   public ByteBuf heapBuffer(int var1) {
      return this.heapBuffer(var1, Integer.MAX_VALUE);
   }

   public ByteBuf heapBuffer(int var1, int var2) {
      if(var1 == 0 && var2 == 0) {
         return this.emptyBuf;
      } else {
         validate(var1, var2);
         return this.newHeapBuffer(var1, var2);
      }
   }

   public ByteBuf directBuffer() {
      return this.directBuffer(256, Integer.MAX_VALUE);
   }

   public ByteBuf directBuffer(int var1) {
      return this.directBuffer(var1, Integer.MAX_VALUE);
   }

   public ByteBuf directBuffer(int var1, int var2) {
      if(var1 == 0 && var2 == 0) {
         return this.emptyBuf;
      } else {
         validate(var1, var2);
         return this.newDirectBuffer(var1, var2);
      }
   }

   public CompositeByteBuf compositeBuffer() {
      return this.directByDefault?this.compositeDirectBuffer():this.compositeHeapBuffer();
   }

   public CompositeByteBuf compositeBuffer(int var1) {
      return this.directByDefault?this.compositeDirectBuffer(var1):this.compositeHeapBuffer(var1);
   }

   public CompositeByteBuf compositeHeapBuffer() {
      return this.compositeHeapBuffer(16);
   }

   public CompositeByteBuf compositeHeapBuffer(int var1) {
      return new CompositeByteBuf(this, false, var1);
   }

   public CompositeByteBuf compositeDirectBuffer() {
      return this.compositeDirectBuffer(16);
   }

   public CompositeByteBuf compositeDirectBuffer(int var1) {
      return new CompositeByteBuf(this, true, var1);
   }

   private static void validate(int var0, int var1) {
      if(var0 < 0) {
         throw new IllegalArgumentException("initialCapacity: " + var0 + " (expectd: 0+)");
      } else if(var0 > var1) {
         throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[]{Integer.valueOf(var0), Integer.valueOf(var1)}));
      }
   }

   protected abstract ByteBuf newHeapBuffer(int var1, int var2);

   protected abstract ByteBuf newDirectBuffer(int var1, int var2);

   public String toString() {
      return StringUtil.simpleClassName((Object)this) + "(directByDefault: " + this.directByDefault + ')';
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$util$ResourceLeakDetector$Level = new int[ResourceLeakDetector.Level.values().length];

      static {
         try {
            $SwitchMap$io$netty$util$ResourceLeakDetector$Level[ResourceLeakDetector.Level.SIMPLE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$ResourceLeakDetector$Level[ResourceLeakDetector.Level.ADVANCED.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$util$ResourceLeakDetector$Level[ResourceLeakDetector.Level.PARANOID.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
