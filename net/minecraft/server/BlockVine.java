package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockVine extends Block {
   public static final BlockStateBoolean UP = BlockStateBoolean.of("up");
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
   public static final BlockStateBoolean[] Q;

   public BlockVine() {
      super(Material.REPLACEABLE_PLANT);
      this.j(this.blockStateList.getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
      this.a(true);
      this.a((CreativeModeTab)CreativeModeTab.c);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(UP, Boolean.valueOf(var2.getType(var3.up()).getBlock().u()));
   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean a(World var1, BlockPosition var2) {
      return true;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      float var3 = 0.0625F;
      float var4 = 1.0F;
      float var5 = 1.0F;
      float var6 = 1.0F;
      float var7 = 0.0F;
      float var8 = 0.0F;
      float var9 = 0.0F;
      boolean var10 = false;
      if(((Boolean)var1.getType(var2).get(WEST)).booleanValue()) {
         var7 = Math.max(var7, 0.0625F);
         var4 = 0.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
         var10 = true;
      }

      if(((Boolean)var1.getType(var2).get(EAST)).booleanValue()) {
         var4 = Math.min(var4, 0.9375F);
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
         var10 = true;
      }

      if(((Boolean)var1.getType(var2).get(NORTH)).booleanValue()) {
         var9 = Math.max(var9, 0.0625F);
         var6 = 0.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var10 = true;
      }

      if(((Boolean)var1.getType(var2).get(SOUTH)).booleanValue()) {
         var6 = Math.min(var6, 0.9375F);
         var9 = 1.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var5 = 0.0F;
         var8 = 1.0F;
         var10 = true;
      }

      if(!var10 && this.c(var1.getType(var2.up()).getBlock())) {
         var5 = Math.min(var5, 0.9375F);
         var8 = 1.0F;
         var4 = 0.0F;
         var7 = 1.0F;
         var6 = 0.0F;
         var9 = 1.0F;
      }

      this.a(var4, var5, var6, var7, var8, var9);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      switch(BlockVine.SyntheticClass_1.a[var3.ordinal()]) {
      case 1:
         return this.c(var1.getType(var2.up()).getBlock());
      case 2:
      case 3:
      case 4:
      case 5:
         return this.c(var1.getType(var2.shift(var3.opposite())).getBlock());
      default:
         return false;
      }
   }

   private boolean c(Block var1) {
      return var1.d() && var1.material.isSolid();
   }

   private boolean e(World var1, BlockPosition var2, IBlockData var3) {
      IBlockData var4 = var3;
      Iterator var5 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

      while(true) {
         BlockStateBoolean var7;
         IBlockData var8;
         do {
            EnumDirection var6;
            do {
               do {
                  if(!var5.hasNext()) {
                     if(d(var3) == 0) {
                        return false;
                     }

                     if(var4 != var3) {
                        var1.setTypeAndData(var2, var3, 2);
                     }

                     return true;
                  }

                  var6 = (EnumDirection)var5.next();
                  var7 = a(var6);
               } while(!((Boolean)var3.get(var7)).booleanValue());
            } while(this.c(var1.getType(var2.shift(var6)).getBlock()));

            var8 = var1.getType(var2.up());
         } while(var8.getBlock() == this && ((Boolean)var8.get(var7)).booleanValue());

         var3 = var3.set(var7, Boolean.valueOf(false));
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide && !this.e(var1, var2, var3)) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         if(var1.random.nextInt(4) == 0) {
            byte var5 = 4;
            int var6 = 5;
            boolean var7 = false;

            label191:
            for(int var8 = -var5; var8 <= var5; ++var8) {
               for(int var9 = -var5; var9 <= var5; ++var9) {
                  for(int var10 = -1; var10 <= 1; ++var10) {
                     if(var1.getType(var2.a(var8, var10, var9)).getBlock() == this) {
                        --var6;
                        if(var6 <= 0) {
                           var7 = true;
                           break label191;
                        }
                     }
                  }
               }
            }

            EnumDirection var18 = EnumDirection.a(var4);
            BlockPosition var19 = var2.up();
            EnumDirection var24;
            if(var18 == EnumDirection.UP && var2.getY() < 255 && var1.isEmpty(var19)) {
               if(!var7) {
                  IBlockData var21 = var3;
                  Iterator var23 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                  while(true) {
                     do {
                        if(!var23.hasNext()) {
                           if(((Boolean)var21.get(NORTH)).booleanValue() || ((Boolean)var21.get(EAST)).booleanValue() || ((Boolean)var21.get(SOUTH)).booleanValue() || ((Boolean)var21.get(WEST)).booleanValue()) {
                              var1.setTypeAndData(var19, var21, 2);
                           }

                           return;
                        }

                        var24 = (EnumDirection)var23.next();
                     } while(!var4.nextBoolean() && this.c(var1.getType(var19.shift(var24)).getBlock()));

                     var21 = var21.set(a(var24), Boolean.valueOf(false));
                  }
               }
            } else {
               BlockPosition var20;
               if(var18.k().c() && !((Boolean)var3.get(a(var18))).booleanValue()) {
                  if(!var7) {
                     var20 = var2.shift(var18);
                     Block var22 = var1.getType(var20).getBlock();
                     if(var22.material == Material.AIR) {
                        var24 = var18.e();
                        EnumDirection var25 = var18.f();
                        boolean var26 = ((Boolean)var3.get(a(var24))).booleanValue();
                        boolean var27 = ((Boolean)var3.get(a(var25))).booleanValue();
                        BlockPosition var28 = var20.shift(var24);
                        BlockPosition var17 = var20.shift(var25);
                        if(var26 && this.c(var1.getType(var28).getBlock())) {
                           var1.setTypeAndData(var20, this.getBlockData().set(a(var24), Boolean.valueOf(true)), 2);
                        } else if(var27 && this.c(var1.getType(var17).getBlock())) {
                           var1.setTypeAndData(var20, this.getBlockData().set(a(var25), Boolean.valueOf(true)), 2);
                        } else if(var26 && var1.isEmpty(var28) && this.c(var1.getType(var2.shift(var24)).getBlock())) {
                           var1.setTypeAndData(var28, this.getBlockData().set(a(var18.opposite()), Boolean.valueOf(true)), 2);
                        } else if(var27 && var1.isEmpty(var17) && this.c(var1.getType(var2.shift(var25)).getBlock())) {
                           var1.setTypeAndData(var17, this.getBlockData().set(a(var18.opposite()), Boolean.valueOf(true)), 2);
                        } else if(this.c(var1.getType(var20.up()).getBlock())) {
                           var1.setTypeAndData(var20, this.getBlockData(), 2);
                        }
                     } else if(var22.material.k() && var22.d()) {
                        var1.setTypeAndData(var2, var3.set(a(var18), Boolean.valueOf(true)), 2);
                     }

                  }
               } else {
                  if(var2.getY() > 1) {
                     var20 = var2.down();
                     IBlockData var11 = var1.getType(var20);
                     Block var12 = var11.getBlock();
                     IBlockData var13;
                     Iterator var14;
                     EnumDirection var15;
                     if(var12.material == Material.AIR) {
                        var13 = var3;
                        var14 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                        while(var14.hasNext()) {
                           var15 = (EnumDirection)var14.next();
                           if(var4.nextBoolean()) {
                              var13 = var13.set(a(var15), Boolean.valueOf(false));
                           }
                        }

                        if(((Boolean)var13.get(NORTH)).booleanValue() || ((Boolean)var13.get(EAST)).booleanValue() || ((Boolean)var13.get(SOUTH)).booleanValue() || ((Boolean)var13.get(WEST)).booleanValue()) {
                           var1.setTypeAndData(var20, var13, 2);
                        }
                     } else if(var12 == this) {
                        var13 = var11;
                        var14 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                        while(var14.hasNext()) {
                           var15 = (EnumDirection)var14.next();
                           BlockStateBoolean var16 = a(var15);
                           if(var4.nextBoolean() && ((Boolean)var3.get(var16)).booleanValue()) {
                              var13 = var13.set(var16, Boolean.valueOf(true));
                           }
                        }

                        if(((Boolean)var13.get(NORTH)).booleanValue() || ((Boolean)var13.get(EAST)).booleanValue() || ((Boolean)var13.get(SOUTH)).booleanValue() || ((Boolean)var13.get(WEST)).booleanValue()) {
                           var1.setTypeAndData(var20, var13, 2);
                        }
                     }
                  }

               }
            }
         }
      }
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      IBlockData var9 = this.getBlockData().set(UP, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false));
      return var3.k().c()?var9.set(a(var3.opposite()), Boolean.valueOf(true)):var9;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public int a(Random var1) {
      return 0;
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(!var1.isClientSide && var2.bZ() != null && var2.bZ().getItem() == Items.SHEARS) {
         var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
         a(var1, var3, new ItemStack(Blocks.VINE, 1, 0));
      } else {
         super.a(var1, var2, var3, var4, var5);
      }

   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(SOUTH, Boolean.valueOf((var1 & 1) > 0)).set(WEST, Boolean.valueOf((var1 & 2) > 0)).set(NORTH, Boolean.valueOf((var1 & 4) > 0)).set(EAST, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;
      if(((Boolean)var1.get(SOUTH)).booleanValue()) {
         var2 |= 1;
      }

      if(((Boolean)var1.get(WEST)).booleanValue()) {
         var2 |= 2;
      }

      if(((Boolean)var1.get(NORTH)).booleanValue()) {
         var2 |= 4;
      }

      if(((Boolean)var1.get(EAST)).booleanValue()) {
         var2 |= 8;
      }

      return var2;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{UP, NORTH, EAST, SOUTH, WEST});
   }

   public static BlockStateBoolean a(EnumDirection var0) {
      switch(BlockVine.SyntheticClass_1.a[var0.ordinal()]) {
      case 1:
         return UP;
      case 2:
         return NORTH;
      case 3:
         return SOUTH;
      case 4:
         return EAST;
      case 5:
         return WEST;
      default:
         throw new IllegalArgumentException(var0 + " is an invalid choice");
      }
   }

   public static int d(IBlockData var0) {
      int var1 = 0;
      BlockStateBoolean[] var2 = Q;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         BlockStateBoolean var5 = var2[var4];
         if(((Boolean)var0.get(var5)).booleanValue()) {
            ++var1;
         }
      }

      return var1;
   }

   static {
      Q = new BlockStateBoolean[]{UP, NORTH, SOUTH, WEST, EAST};
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.UP.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[EnumDirection.NORTH.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
