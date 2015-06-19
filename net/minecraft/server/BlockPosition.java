package net.minecraft.server;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Vec3D;

public class BlockPosition extends BaseBlockPosition {
   public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);
   private static final int c = 1 + MathHelper.c(MathHelper.b(30000000));
   private static final int d;
   private static final int e;
   private static final int f;
   private static final int g;
   private static final long h;
   private static final long i;
   private static final long j;

   public BlockPosition(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public BlockPosition(double var1, double var3, double var5) {
      super(var1, var3, var5);
   }

   public BlockPosition(Entity var1) {
      this(var1.locX, var1.locY, var1.locZ);
   }

   public BlockPosition(Vec3D var1) {
      this(var1.a, var1.b, var1.c);
   }

   public BlockPosition(BaseBlockPosition var1) {
      this(var1.getX(), var1.getY(), var1.getZ());
   }

   public BlockPosition a(double var1, double var3, double var5) {
      return var1 == 0.0D && var3 == 0.0D && var5 == 0.0D?this:new BlockPosition((double)this.getX() + var1, (double)this.getY() + var3, (double)this.getZ() + var5);
   }

   public BlockPosition a(int var1, int var2, int var3) {
      return var1 == 0 && var2 == 0 && var3 == 0?this:new BlockPosition(this.getX() + var1, this.getY() + var2, this.getZ() + var3);
   }

   public BlockPosition a(BaseBlockPosition var1) {
      return var1.getX() == 0 && var1.getY() == 0 && var1.getZ() == 0?this:new BlockPosition(this.getX() + var1.getX(), this.getY() + var1.getY(), this.getZ() + var1.getZ());
   }

   public BlockPosition b(BaseBlockPosition var1) {
      return var1.getX() == 0 && var1.getY() == 0 && var1.getZ() == 0?this:new BlockPosition(this.getX() - var1.getX(), this.getY() - var1.getY(), this.getZ() - var1.getZ());
   }

   public BlockPosition up() {
      return this.up(1);
   }

   public BlockPosition up(int var1) {
      return this.shift(EnumDirection.UP, var1);
   }

   public BlockPosition down() {
      return this.down(1);
   }

   public BlockPosition down(int var1) {
      return this.shift(EnumDirection.DOWN, var1);
   }

   public BlockPosition north() {
      return this.north(1);
   }

   public BlockPosition north(int var1) {
      return this.shift(EnumDirection.NORTH, var1);
   }

   public BlockPosition south() {
      return this.south(1);
   }

   public BlockPosition south(int var1) {
      return this.shift(EnumDirection.SOUTH, var1);
   }

   public BlockPosition west() {
      return this.west(1);
   }

   public BlockPosition west(int var1) {
      return this.shift(EnumDirection.WEST, var1);
   }

   public BlockPosition east() {
      return this.east(1);
   }

   public BlockPosition east(int var1) {
      return this.shift(EnumDirection.EAST, var1);
   }

   public BlockPosition shift(EnumDirection var1) {
      return this.shift(var1, 1);
   }

   public BlockPosition shift(EnumDirection var1, int var2) {
      return var2 == 0?this:new BlockPosition(this.getX() + var1.getAdjacentX() * var2, this.getY() + var1.getAdjacentY() * var2, this.getZ() + var1.getAdjacentZ() * var2);
   }

   public BlockPosition c(BaseBlockPosition var1) {
      return new BlockPosition(this.getY() * var1.getZ() - this.getZ() * var1.getY(), this.getZ() * var1.getX() - this.getX() * var1.getZ(), this.getX() * var1.getY() - this.getY() * var1.getX());
   }

   public long asLong() {
      return ((long)this.getX() & h) << g | ((long)this.getY() & i) << f | ((long)this.getZ() & j) << 0;
   }

   public static BlockPosition fromLong(long var0) {
      int var2 = (int)(var0 << 64 - g - c >> 64 - c);
      int var3 = (int)(var0 << 64 - f - e >> 64 - e);
      int var4 = (int)(var0 << 64 - d >> 64 - d);
      return new BlockPosition(var2, var3, var4);
   }

   public static Iterable<BlockPosition> a(BlockPosition var0, BlockPosition var1) {
      final BlockPosition var2 = new BlockPosition(Math.min(var0.getX(), var1.getX()), Math.min(var0.getY(), var1.getY()), Math.min(var0.getZ(), var1.getZ()));
      final BlockPosition var3 = new BlockPosition(Math.max(var0.getX(), var1.getX()), Math.max(var0.getY(), var1.getY()), Math.max(var0.getZ(), var1.getZ()));
      return new Iterable() {
         public Iterator<BlockPosition> iterator() {
            return new AbstractIterator() {
               private BlockPosition b = null;

               protected BlockPosition a() {
                  if(this.b == null) {
                     this.b = var2;
                     return this.b;
                  } else if(this.b.equals(var3)) {
                     return (BlockPosition)this.endOfData();
                  } else {
                     int var1 = this.b.getX();
                     int var2x = this.b.getY();
                     int var3x = this.b.getZ();
                     if(var1 < var3.getX()) {
                        ++var1;
                     } else if(var2x < var3.getY()) {
                        var1 = var2.getX();
                        ++var2x;
                     } else if(var3x < var3.getZ()) {
                        var1 = var2.getX();
                        var2x = var2.getY();
                        ++var3x;
                     }

                     this.b = new BlockPosition(var1, var2x, var3x);
                     return this.b;
                  }
               }

               // $FF: synthetic method
               protected Object computeNext() {
                  return this.a();
               }
            };
         }
      };
   }

   public static Iterable<BlockPosition.MutableBlockPosition> b(BlockPosition var0, BlockPosition var1) {
      final BlockPosition var2 = new BlockPosition(Math.min(var0.getX(), var1.getX()), Math.min(var0.getY(), var1.getY()), Math.min(var0.getZ(), var1.getZ()));
      final BlockPosition var3 = new BlockPosition(Math.max(var0.getX(), var1.getX()), Math.max(var0.getY(), var1.getY()), Math.max(var0.getZ(), var1.getZ()));
      return new Iterable() {
         public Iterator<BlockPosition.MutableBlockPosition> iterator() {
            return new AbstractIterator() {
               private BlockPosition.MutableBlockPosition b = null;

               protected BlockPosition.MutableBlockPosition a() {
                  if(this.b == null) {
                     this.b = new BlockPosition.MutableBlockPosition(var2.getX(), var2.getY(), var2.getZ());
                     return this.b;
                  } else if(this.b.equals(var3)) {
                     return (BlockPosition.MutableBlockPosition)this.endOfData();
                  } else {
                     int var1 = this.b.getX();
                     int var2x = this.b.getY();
                     int var3x = this.b.getZ();
                     if(var1 < var3.getX()) {
                        ++var1;
                     } else if(var2x < var3.getY()) {
                        var1 = var2.getX();
                        ++var2x;
                     } else if(var3x < var3.getZ()) {
                        var1 = var2.getX();
                        var2x = var2.getY();
                        ++var3x;
                     }

                     this.b.c = var1;
                     this.b.d = var2x;
                     this.b.e = var3x;
                     return this.b;
                  }
               }

               // $FF: synthetic method
               protected Object computeNext() {
                  return this.a();
               }
            };
         }
      };
   }

   // $FF: synthetic method
   public BaseBlockPosition d(BaseBlockPosition var1) {
      return this.c(var1);
   }

   static {
      d = c;
      e = 64 - c - d;
      f = 0 + d;
      g = f + e;
      h = (1L << c) - 1L;
      i = (1L << e) - 1L;
      j = (1L << d) - 1L;
   }

   public static final class MutableBlockPosition extends BlockPosition {
      private int c;
      private int d;
      private int e;

      public MutableBlockPosition() {
         this(0, 0, 0);
      }

      public MutableBlockPosition(int var1, int var2, int var3) {
         super(0, 0, 0);
         this.c = var1;
         this.d = var2;
         this.e = var3;
      }

      public int getX() {
         return this.c;
      }

      public int getY() {
         return this.d;
      }

      public int getZ() {
         return this.e;
      }

      public BlockPosition.MutableBlockPosition c(int var1, int var2, int var3) {
         this.c = var1;
         this.d = var2;
         this.e = var3;
         return this;
      }

      // $FF: synthetic method
      public BaseBlockPosition d(BaseBlockPosition var1) {
         return super.c(var1);
      }
   }
}
