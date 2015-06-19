package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPistonExtension;
import net.minecraft.server.BlockPistonMoving;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IContainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PistonExtendsChecker;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityPiston;
import net.minecraft.server.World;

public class BlockPiston extends Block {
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
   public static final BlockStateBoolean EXTENDED = BlockStateBoolean.of("extended");
   private final boolean N;

   public BlockPiston(boolean var1) {
      super(Material.PISTON);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(EXTENDED, Boolean.valueOf(false)));
      this.N = var1;
      this.a(i);
      this.c(0.5F);
      this.a(CreativeModeTab.d);
   }

   public boolean c() {
      return false;
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      var1.setTypeAndData(var2, var3.set(FACING, a(var1, var2, var4)), 2);
      if(!var1.isClientSide) {
         this.e(var1, var2, var3);
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         this.e(var1, var2, var3);
      }

   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide && var1.getTileEntity(var2) == null) {
         this.e(var1, var2, var3);
      }

   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, a(var1, var2, var8)).set(EXTENDED, Boolean.valueOf(false));
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      EnumDirection var4 = (EnumDirection)var3.get(FACING);
      boolean var5 = this.a(var1, var2, var4);
      if(var5 && !((Boolean)var3.get(EXTENDED)).booleanValue()) {
         if((new PistonExtendsChecker(var1, var2, var4, true)).a()) {
            var1.playBlockAction(var2, this, 0, var4.a());
         }
      } else if(!var5 && ((Boolean)var3.get(EXTENDED)).booleanValue()) {
         var1.setTypeAndData(var2, var3.set(EXTENDED, Boolean.valueOf(false)), 2);
         var1.playBlockAction(var2, this, 1, var4.a());
      }

   }

   private boolean a(World var1, BlockPosition var2, EnumDirection var3) {
      EnumDirection[] var4 = EnumDirection.values();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         EnumDirection var7 = var4[var6];
         if(var7 != var3 && var1.isBlockFacePowered(var2.shift(var7), var7)) {
            return true;
         }
      }

      if(var1.isBlockFacePowered(var2, EnumDirection.DOWN)) {
         return true;
      } else {
         BlockPosition var9 = var2.up();
         EnumDirection[] var10 = EnumDirection.values();
         var6 = var10.length;

         for(int var11 = 0; var11 < var6; ++var11) {
            EnumDirection var8 = var10[var11];
            if(var8 != EnumDirection.DOWN && var1.isBlockFacePowered(var9.shift(var8), var8)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, int var4, int var5) {
      EnumDirection var6 = (EnumDirection)var3.get(FACING);
      if(!var1.isClientSide) {
         boolean var7 = this.a(var1, var2, var6);
         if(var7 && var4 == 1) {
            var1.setTypeAndData(var2, var3.set(EXTENDED, Boolean.valueOf(true)), 2);
            return false;
         }

         if(!var7 && var4 == 0) {
            return false;
         }
      }

      if(var4 == 0) {
         if(!this.a(var1, var2, var6, true)) {
            return false;
         }

         var1.setTypeAndData(var2, var3.set(EXTENDED, Boolean.valueOf(true)), 2);
         var1.makeSound((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "tile.piston.out", 0.5F, var1.random.nextFloat() * 0.25F + 0.6F);
      } else if(var4 == 1) {
         TileEntity var13 = var1.getTileEntity(var2.shift(var6));
         if(var13 instanceof TileEntityPiston) {
            ((TileEntityPiston)var13).h();
         }

         var1.setTypeAndData(var2, Blocks.PISTON_EXTENSION.getBlockData().set(BlockPistonMoving.FACING, var6).set(BlockPistonMoving.TYPE, this.N?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT), 3);
         var1.setTileEntity(var2, BlockPistonMoving.a(this.fromLegacyData(var5), var6, false, true));
         if(this.N) {
            BlockPosition var8 = var2.a(var6.getAdjacentX() * 2, var6.getAdjacentY() * 2, var6.getAdjacentZ() * 2);
            Block var9 = var1.getType(var8).getBlock();
            boolean var10 = false;
            if(var9 == Blocks.PISTON_EXTENSION) {
               TileEntity var11 = var1.getTileEntity(var8);
               if(var11 instanceof TileEntityPiston) {
                  TileEntityPiston var12 = (TileEntityPiston)var11;
                  if(var12.e() == var6 && var12.d()) {
                     var12.h();
                     var10 = true;
                  }
               }
            }

            if(!var10 && var9.getMaterial() != Material.AIR && a(var9, var1, var8, var6.opposite(), false) && (var9.k() == 0 || var9 == Blocks.PISTON || var9 == Blocks.STICKY_PISTON)) {
               this.a(var1, var2, var6, false);
            }
         } else {
            var1.setAir(var2.shift(var6));
         }

         var1.makeSound((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "tile.piston.in", 0.5F, var1.random.nextFloat() * 0.15F + 0.6F);
      }

      return true;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      if(var3.getBlock() == this && ((Boolean)var3.get(EXTENDED)).booleanValue()) {
         float var4 = 0.25F;
         EnumDirection var5 = (EnumDirection)var3.get(FACING);
         if(var5 != null) {
            switch(BlockPiston.SyntheticClass_1.a[var5.ordinal()]) {
            case 1:
               this.a(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 2:
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
               break;
            case 3:
               this.a(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
               break;
            case 4:
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
               break;
            case 5:
               this.a(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
               break;
            case 6:
               this.a(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
         }
      } else {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3);
   }

   public boolean d() {
      return false;
   }

   public static EnumDirection b(int var0) {
      int var1 = var0 & 7;
      return var1 > 5?null:EnumDirection.fromType1(var1);
   }

   public static EnumDirection a(World var0, BlockPosition var1, EntityLiving var2) {
      if(MathHelper.e((float)var2.locX - (float)var1.getX()) < 2.0F && MathHelper.e((float)var2.locZ - (float)var1.getZ()) < 2.0F) {
         double var3 = var2.locY + (double)var2.getHeadHeight();
         if(var3 - (double)var1.getY() > 2.0D) {
            return EnumDirection.UP;
         }

         if((double)var1.getY() - var3 > 0.0D) {
            return EnumDirection.DOWN;
         }
      }

      return var2.getDirection().opposite();
   }

   public static boolean a(Block var0, World var1, BlockPosition var2, EnumDirection var3, boolean var4) {
      if(var0 == Blocks.OBSIDIAN) {
         return false;
      } else if(!var1.getWorldBorder().a(var2)) {
         return false;
      } else if(var2.getY() < 0 || var3 == EnumDirection.DOWN && var2.getY() == 0) {
         return false;
      } else if(var2.getY() <= var1.getHeight() - 1 && (var3 != EnumDirection.UP || var2.getY() != var1.getHeight() - 1)) {
         if(var0 != Blocks.PISTON && var0 != Blocks.STICKY_PISTON) {
            if(var0.g(var1, var2) == -1.0F) {
               return false;
            }

            if(var0.k() == 2) {
               return false;
            }

            if(var0.k() == 1) {
               if(!var4) {
                  return false;
               }

               return true;
            }
         } else if(((Boolean)var1.getType(var2).get(EXTENDED)).booleanValue()) {
            return false;
         }

         return !(var0 instanceof IContainer);
      } else {
         return false;
      }
   }

   private boolean a(World var1, BlockPosition var2, EnumDirection var3, boolean var4) {
      if(!var4) {
         var1.setAir(var2.shift(var3));
      }

      PistonExtendsChecker var5 = new PistonExtendsChecker(var1, var2, var3, var4);
      List var6 = var5.getMovedBlocks();
      List var7 = var5.getBrokenBlocks();
      if(!var5.a()) {
         return false;
      } else {
         int var8 = var6.size() + var7.size();
         Block[] var9 = new Block[var8];
         EnumDirection var10 = var4?var3:var3.opposite();

         int var11;
         BlockPosition var12;
         for(var11 = var7.size() - 1; var11 >= 0; --var11) {
            var12 = (BlockPosition)var7.get(var11);
            Block var13 = var1.getType(var12).getBlock();
            var13.b(var1, var12, var1.getType(var12), 0);
            var1.setAir(var12);
            --var8;
            var9[var8] = var13;
         }

         IBlockData var18;
         for(var11 = var6.size() - 1; var11 >= 0; --var11) {
            var12 = (BlockPosition)var6.get(var11);
            var18 = var1.getType(var12);
            Block var14 = var18.getBlock();
            var14.toLegacyData(var18);
            var1.setAir(var12);
            var12 = var12.shift(var10);
            var1.setTypeAndData(var12, Blocks.PISTON_EXTENSION.getBlockData().set(FACING, var3), 4);
            var1.setTileEntity(var12, BlockPistonMoving.a(var18, var3, var4, false));
            --var8;
            var9[var8] = var14;
         }

         BlockPosition var16 = var2.shift(var3);
         if(var4) {
            BlockPistonExtension.EnumPistonType var17 = this.N?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT;
            var18 = Blocks.PISTON_HEAD.getBlockData().set(BlockPistonExtension.FACING, var3).set(BlockPistonExtension.TYPE, var17);
            IBlockData var20 = Blocks.PISTON_EXTENSION.getBlockData().set(BlockPistonMoving.FACING, var3).set(BlockPistonMoving.TYPE, this.N?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT);
            var1.setTypeAndData(var16, var20, 4);
            var1.setTileEntity(var16, BlockPistonMoving.a(var18, var3, true, false));
         }

         int var19;
         for(var19 = var7.size() - 1; var19 >= 0; --var19) {
            var1.applyPhysics((BlockPosition)var7.get(var19), var9[var8++]);
         }

         for(var19 = var6.size() - 1; var19 >= 0; --var19) {
            var1.applyPhysics((BlockPosition)var6.get(var19), var9[var8++]);
         }

         if(var4) {
            var1.applyPhysics(var16, Blocks.PISTON_HEAD);
            var1.applyPhysics(var2, this);
         }

         return true;
      }
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, b(var1)).set(EXTENDED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(((Boolean)var1.get(EXTENDED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, EXTENDED});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[EnumDirection.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[EnumDirection.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
