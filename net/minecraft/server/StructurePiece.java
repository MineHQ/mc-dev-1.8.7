package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemDoor;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenFactory;

public abstract class StructurePiece {
   protected StructureBoundingBox l;
   protected EnumDirection m;
   protected int n;

   public StructurePiece() {
   }

   protected StructurePiece(int var1) {
      this.n = var1;
   }

   public NBTTagCompound b() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setString("id", WorldGenFactory.a(this));
      var1.set("BB", this.l.g());
      var1.setInt("O", this.m == null?-1:this.m.b());
      var1.setInt("GD", this.n);
      this.a(var1);
      return var1;
   }

   protected abstract void a(NBTTagCompound var1);

   public void a(World var1, NBTTagCompound var2) {
      if(var2.hasKey("BB")) {
         this.l = new StructureBoundingBox(var2.getIntArray("BB"));
      }

      int var3 = var2.getInt("O");
      this.m = var3 == -1?null:EnumDirection.fromType2(var3);
      this.n = var2.getInt("GD");
      this.b(var2);
   }

   protected abstract void b(NBTTagCompound var1);

   public void a(StructurePiece var1, List<StructurePiece> var2, Random var3) {
   }

   public abstract boolean a(World var1, Random var2, StructureBoundingBox var3);

   public StructureBoundingBox c() {
      return this.l;
   }

   public int d() {
      return this.n;
   }

   public static StructurePiece a(List<StructurePiece> var0, StructureBoundingBox var1) {
      Iterator var2 = var0.iterator();

      StructurePiece var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (StructurePiece)var2.next();
      } while(var3.c() == null || !var3.c().a(var1));

      return var3;
   }

   public BlockPosition a() {
      return new BlockPosition(this.l.f());
   }

   protected boolean a(World var1, StructureBoundingBox var2) {
      int var3 = Math.max(this.l.a - 1, var2.a);
      int var4 = Math.max(this.l.b - 1, var2.b);
      int var5 = Math.max(this.l.c - 1, var2.c);
      int var6 = Math.min(this.l.d + 1, var2.d);
      int var7 = Math.min(this.l.e + 1, var2.e);
      int var8 = Math.min(this.l.f + 1, var2.f);
      BlockPosition.MutableBlockPosition var9 = new BlockPosition.MutableBlockPosition();

      int var10;
      int var11;
      for(var10 = var3; var10 <= var6; ++var10) {
         for(var11 = var5; var11 <= var8; ++var11) {
            if(var1.getType(var9.c(var10, var4, var11)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if(var1.getType(var9.c(var10, var7, var11)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for(var10 = var3; var10 <= var6; ++var10) {
         for(var11 = var4; var11 <= var7; ++var11) {
            if(var1.getType(var9.c(var10, var11, var5)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if(var1.getType(var9.c(var10, var11, var8)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      for(var10 = var5; var10 <= var8; ++var10) {
         for(var11 = var4; var11 <= var7; ++var11) {
            if(var1.getType(var9.c(var3, var11, var10)).getBlock().getMaterial().isLiquid()) {
               return true;
            }

            if(var1.getType(var9.c(var6, var11, var10)).getBlock().getMaterial().isLiquid()) {
               return true;
            }
         }
      }

      return false;
   }

   protected int a(int var1, int var2) {
      if(this.m == null) {
         return var1;
      } else {
         switch(StructurePiece.SyntheticClass_1.a[this.m.ordinal()]) {
         case 1:
         case 2:
            return this.l.a + var1;
         case 3:
            return this.l.d - var2;
         case 4:
            return this.l.a + var2;
         default:
            return var1;
         }
      }
   }

   protected int d(int var1) {
      return this.m == null?var1:var1 + this.l.b;
   }

   protected int b(int var1, int var2) {
      if(this.m == null) {
         return var2;
      } else {
         switch(StructurePiece.SyntheticClass_1.a[this.m.ordinal()]) {
         case 1:
            return this.l.f - var2;
         case 2:
            return this.l.c + var2;
         case 3:
         case 4:
            return this.l.c + var1;
         default:
            return var2;
         }
      }
   }

   protected int a(Block var1, int var2) {
      if(var1 == Blocks.RAIL) {
         if(this.m == EnumDirection.WEST || this.m == EnumDirection.EAST) {
            if(var2 == 1) {
               return 0;
            }

            return 1;
         }
      } else if(var1 instanceof BlockDoor) {
         if(this.m == EnumDirection.SOUTH) {
            if(var2 == 0) {
               return 2;
            }

            if(var2 == 2) {
               return 0;
            }
         } else {
            if(this.m == EnumDirection.WEST) {
               return var2 + 1 & 3;
            }

            if(this.m == EnumDirection.EAST) {
               return var2 + 3 & 3;
            }
         }
      } else if(var1 != Blocks.STONE_STAIRS && var1 != Blocks.OAK_STAIRS && var1 != Blocks.NETHER_BRICK_STAIRS && var1 != Blocks.STONE_BRICK_STAIRS && var1 != Blocks.SANDSTONE_STAIRS) {
         if(var1 == Blocks.LADDER) {
            if(this.m == EnumDirection.SOUTH) {
               if(var2 == EnumDirection.NORTH.a()) {
                  return EnumDirection.SOUTH.a();
               }

               if(var2 == EnumDirection.SOUTH.a()) {
                  return EnumDirection.NORTH.a();
               }
            } else if(this.m == EnumDirection.WEST) {
               if(var2 == EnumDirection.NORTH.a()) {
                  return EnumDirection.WEST.a();
               }

               if(var2 == EnumDirection.SOUTH.a()) {
                  return EnumDirection.EAST.a();
               }

               if(var2 == EnumDirection.WEST.a()) {
                  return EnumDirection.NORTH.a();
               }

               if(var2 == EnumDirection.EAST.a()) {
                  return EnumDirection.SOUTH.a();
               }
            } else if(this.m == EnumDirection.EAST) {
               if(var2 == EnumDirection.NORTH.a()) {
                  return EnumDirection.EAST.a();
               }

               if(var2 == EnumDirection.SOUTH.a()) {
                  return EnumDirection.WEST.a();
               }

               if(var2 == EnumDirection.WEST.a()) {
                  return EnumDirection.NORTH.a();
               }

               if(var2 == EnumDirection.EAST.a()) {
                  return EnumDirection.SOUTH.a();
               }
            }
         } else if(var1 == Blocks.STONE_BUTTON) {
            if(this.m == EnumDirection.SOUTH) {
               if(var2 == 3) {
                  return 4;
               }

               if(var2 == 4) {
                  return 3;
               }
            } else if(this.m == EnumDirection.WEST) {
               if(var2 == 3) {
                  return 1;
               }

               if(var2 == 4) {
                  return 2;
               }

               if(var2 == 2) {
                  return 3;
               }

               if(var2 == 1) {
                  return 4;
               }
            } else if(this.m == EnumDirection.EAST) {
               if(var2 == 3) {
                  return 2;
               }

               if(var2 == 4) {
                  return 1;
               }

               if(var2 == 2) {
                  return 3;
               }

               if(var2 == 1) {
                  return 4;
               }
            }
         } else if(var1 != Blocks.TRIPWIRE_HOOK && !(var1 instanceof BlockDirectional)) {
            if(var1 == Blocks.PISTON || var1 == Blocks.STICKY_PISTON || var1 == Blocks.LEVER || var1 == Blocks.DISPENSER) {
               if(this.m == EnumDirection.SOUTH) {
                  if(var2 == EnumDirection.NORTH.a() || var2 == EnumDirection.SOUTH.a()) {
                     return EnumDirection.fromType1(var2).opposite().a();
                  }
               } else if(this.m == EnumDirection.WEST) {
                  if(var2 == EnumDirection.NORTH.a()) {
                     return EnumDirection.WEST.a();
                  }

                  if(var2 == EnumDirection.SOUTH.a()) {
                     return EnumDirection.EAST.a();
                  }

                  if(var2 == EnumDirection.WEST.a()) {
                     return EnumDirection.NORTH.a();
                  }

                  if(var2 == EnumDirection.EAST.a()) {
                     return EnumDirection.SOUTH.a();
                  }
               } else if(this.m == EnumDirection.EAST) {
                  if(var2 == EnumDirection.NORTH.a()) {
                     return EnumDirection.EAST.a();
                  }

                  if(var2 == EnumDirection.SOUTH.a()) {
                     return EnumDirection.WEST.a();
                  }

                  if(var2 == EnumDirection.WEST.a()) {
                     return EnumDirection.NORTH.a();
                  }

                  if(var2 == EnumDirection.EAST.a()) {
                     return EnumDirection.SOUTH.a();
                  }
               }
            }
         } else {
            EnumDirection var3 = EnumDirection.fromType2(var2);
            if(this.m == EnumDirection.SOUTH) {
               if(var3 == EnumDirection.SOUTH || var3 == EnumDirection.NORTH) {
                  return var3.opposite().b();
               }
            } else if(this.m == EnumDirection.WEST) {
               if(var3 == EnumDirection.NORTH) {
                  return EnumDirection.WEST.b();
               }

               if(var3 == EnumDirection.SOUTH) {
                  return EnumDirection.EAST.b();
               }

               if(var3 == EnumDirection.WEST) {
                  return EnumDirection.NORTH.b();
               }

               if(var3 == EnumDirection.EAST) {
                  return EnumDirection.SOUTH.b();
               }
            } else if(this.m == EnumDirection.EAST) {
               if(var3 == EnumDirection.NORTH) {
                  return EnumDirection.EAST.b();
               }

               if(var3 == EnumDirection.SOUTH) {
                  return EnumDirection.WEST.b();
               }

               if(var3 == EnumDirection.WEST) {
                  return EnumDirection.NORTH.b();
               }

               if(var3 == EnumDirection.EAST) {
                  return EnumDirection.SOUTH.b();
               }
            }
         }
      } else if(this.m == EnumDirection.SOUTH) {
         if(var2 == 2) {
            return 3;
         }

         if(var2 == 3) {
            return 2;
         }
      } else if(this.m == EnumDirection.WEST) {
         if(var2 == 0) {
            return 2;
         }

         if(var2 == 1) {
            return 3;
         }

         if(var2 == 2) {
            return 0;
         }

         if(var2 == 3) {
            return 1;
         }
      } else if(this.m == EnumDirection.EAST) {
         if(var2 == 0) {
            return 2;
         }

         if(var2 == 1) {
            return 3;
         }

         if(var2 == 2) {
            return 1;
         }

         if(var2 == 3) {
            return 0;
         }
      }

      return var2;
   }

   protected void a(World var1, IBlockData var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      BlockPosition var7 = new BlockPosition(this.a(var3, var5), this.d(var4), this.b(var3, var5));
      if(var6.b((BaseBlockPosition)var7)) {
         var1.setTypeAndData(var7, var2, 2);
      }
   }

   protected IBlockData a(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      int var6 = this.a(var2, var4);
      int var7 = this.d(var3);
      int var8 = this.b(var2, var4);
      BlockPosition var9 = new BlockPosition(var6, var7, var8);
      return !var5.b((BaseBlockPosition)var9)?Blocks.AIR.getBlockData():var1.getType(var9);
   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      for(int var9 = var4; var9 <= var7; ++var9) {
         for(int var10 = var3; var10 <= var6; ++var10) {
            for(int var11 = var5; var11 <= var8; ++var11) {
               this.a(var1, Blocks.AIR.getBlockData(), var10, var9, var11, var2);
            }
         }
      }

   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockData var9, IBlockData var10, boolean var11) {
      for(int var12 = var4; var12 <= var7; ++var12) {
         for(int var13 = var3; var13 <= var6; ++var13) {
            for(int var14 = var5; var14 <= var8; ++var14) {
               if(!var11 || this.a(var1, var13, var12, var14, var2).getBlock().getMaterial() != Material.AIR) {
                  if(var12 != var4 && var12 != var7 && var13 != var3 && var13 != var6 && var14 != var5 && var14 != var8) {
                     this.a(var1, var10, var13, var12, var14, var2);
                  } else {
                     this.a(var1, var9, var13, var12, var14, var2);
                  }
               }
            }
         }
      }

   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, Random var10, StructurePiece.StructurePieceBlockSelector var11) {
      for(int var12 = var4; var12 <= var7; ++var12) {
         for(int var13 = var3; var13 <= var6; ++var13) {
            for(int var14 = var5; var14 <= var8; ++var14) {
               if(!var9 || this.a(var1, var13, var12, var14, var2).getBlock().getMaterial() != Material.AIR) {
                  var11.a(var10, var13, var12, var14, var12 == var4 || var12 == var7 || var13 == var3 || var13 == var6 || var14 == var5 || var14 == var8);
                  this.a(var1, var11.a(), var13, var12, var14, var2);
               }
            }
         }
      }

   }

   protected void a(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7, int var8, int var9, int var10, IBlockData var11, IBlockData var12, boolean var13) {
      for(int var14 = var6; var14 <= var9; ++var14) {
         for(int var15 = var5; var15 <= var8; ++var15) {
            for(int var16 = var7; var16 <= var10; ++var16) {
               if(var3.nextFloat() <= var4 && (!var13 || this.a(var1, var15, var14, var16, var2).getBlock().getMaterial() != Material.AIR)) {
                  if(var14 != var6 && var14 != var9 && var15 != var5 && var15 != var8 && var16 != var7 && var16 != var10) {
                     this.a(var1, var12, var15, var14, var16, var2);
                  } else {
                     this.a(var1, var11, var15, var14, var16, var2);
                  }
               }
            }
         }
      }

   }

   protected void a(World var1, StructureBoundingBox var2, Random var3, float var4, int var5, int var6, int var7, IBlockData var8) {
      if(var3.nextFloat() < var4) {
         this.a(var1, var8, var5, var6, var7, var2);
      }

   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockData var9, boolean var10) {
      float var11 = (float)(var6 - var3 + 1);
      float var12 = (float)(var7 - var4 + 1);
      float var13 = (float)(var8 - var5 + 1);
      float var14 = (float)var3 + var11 / 2.0F;
      float var15 = (float)var5 + var13 / 2.0F;

      for(int var16 = var4; var16 <= var7; ++var16) {
         float var17 = (float)(var16 - var4) / var12;

         for(int var18 = var3; var18 <= var6; ++var18) {
            float var19 = ((float)var18 - var14) / (var11 * 0.5F);

            for(int var20 = var5; var20 <= var8; ++var20) {
               float var21 = ((float)var20 - var15) / (var13 * 0.5F);
               if(!var10 || this.a(var1, var18, var16, var20, var2).getBlock().getMaterial() != Material.AIR) {
                  float var22 = var19 * var19 + var17 * var17 + var21 * var21;
                  if(var22 <= 1.05F) {
                     this.a(var1, var9, var18, var16, var20, var2);
                  }
               }
            }
         }
      }

   }

   protected void b(World var1, int var2, int var3, int var4, StructureBoundingBox var5) {
      BlockPosition var6 = new BlockPosition(this.a(var2, var4), this.d(var3), this.b(var2, var4));
      if(var5.b((BaseBlockPosition)var6)) {
         while(!var1.isEmpty(var6) && var6.getY() < 255) {
            var1.setTypeAndData(var6, Blocks.AIR.getBlockData(), 2);
            var6 = var6.up();
         }

      }
   }

   protected void b(World var1, IBlockData var2, int var3, int var4, int var5, StructureBoundingBox var6) {
      int var7 = this.a(var3, var5);
      int var8 = this.d(var4);
      int var9 = this.b(var3, var5);
      if(var6.b((BaseBlockPosition)(new BlockPosition(var7, var8, var9)))) {
         while((var1.isEmpty(new BlockPosition(var7, var8, var9)) || var1.getType(new BlockPosition(var7, var8, var9)).getBlock().getMaterial().isLiquid()) && var8 > 1) {
            var1.setTypeAndData(new BlockPosition(var7, var8, var9), var2, 2);
            --var8;
         }

      }
   }

   protected boolean a(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, List<StructurePieceTreasure> var7, int var8) {
      BlockPosition var9 = new BlockPosition(this.a(var4, var6), this.d(var5), this.b(var4, var6));
      if(var2.b((BaseBlockPosition)var9) && var1.getType(var9).getBlock() != Blocks.CHEST) {
         IBlockData var10 = Blocks.CHEST.getBlockData();
         var1.setTypeAndData(var9, Blocks.CHEST.f(var1, var9, var10), 2);
         TileEntity var11 = var1.getTileEntity(var9);
         if(var11 instanceof TileEntityChest) {
            StructurePieceTreasure.a(var3, var7, (IInventory)((TileEntityChest)var11), var8);
         }

         return true;
      } else {
         return false;
      }
   }

   protected boolean a(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, int var7, List<StructurePieceTreasure> var8, int var9) {
      BlockPosition var10 = new BlockPosition(this.a(var4, var6), this.d(var5), this.b(var4, var6));
      if(var2.b((BaseBlockPosition)var10) && var1.getType(var10).getBlock() != Blocks.DISPENSER) {
         var1.setTypeAndData(var10, Blocks.DISPENSER.fromLegacyData(this.a(Blocks.DISPENSER, var7)), 2);
         TileEntity var11 = var1.getTileEntity(var10);
         if(var11 instanceof TileEntityDispenser) {
            StructurePieceTreasure.a(var3, var8, (TileEntityDispenser)var11, var9);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void a(World var1, StructureBoundingBox var2, Random var3, int var4, int var5, int var6, EnumDirection var7) {
      BlockPosition var8 = new BlockPosition(this.a(var4, var6), this.d(var5), this.b(var4, var6));
      if(var2.b((BaseBlockPosition)var8)) {
         ItemDoor.a(var1, var8, var7.f(), Blocks.WOODEN_DOOR);
      }

   }

   public void a(int var1, int var2, int var3) {
      this.l.a(var1, var2, var3);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public abstract static class StructurePieceBlockSelector {
      protected IBlockData a;

      protected StructurePieceBlockSelector() {
         this.a = Blocks.AIR.getBlockData();
      }

      public abstract void a(Random var1, int var2, int var3, int var4, boolean var5);

      public IBlockData a() {
         return this.a;
      }
   }
}
