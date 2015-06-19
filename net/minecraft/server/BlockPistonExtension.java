package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockPistonExtension extends Block {
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
   public static final BlockStateEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockStateEnum.of("type", BlockPistonExtension.EnumPistonType.class);
   public static final BlockStateBoolean SHORT = BlockStateBoolean.of("short");

   public BlockPistonExtension() {
      super(Material.PISTON);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).set(SHORT, Boolean.valueOf(false)));
      this.a(i);
      this.c(0.5F);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(var4.abilities.canInstantlyBuild) {
         EnumDirection var5 = (EnumDirection)var3.get(FACING);
         if(var5 != null) {
            BlockPosition var6 = var2.shift(var5.opposite());
            Block var7 = var1.getType(var6).getBlock();
            if(var7 == Blocks.PISTON || var7 == Blocks.STICKY_PISTON) {
               var1.setAir(var6);
            }
         }
      }

      super.a(var1, var2, var3, var4);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      super.remove(var1, var2, var3);
      EnumDirection var4 = ((EnumDirection)var3.get(FACING)).opposite();
      var2 = var2.shift(var4);
      IBlockData var5 = var1.getType(var2);
      if((var5.getBlock() == Blocks.PISTON || var5.getBlock() == Blocks.STICKY_PISTON) && ((Boolean)var5.get(BlockPiston.EXTENDED)).booleanValue()) {
         var5.getBlock().b(var1, var2, var5, 0);
         var1.setAir(var2);
      }

   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return false;
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      return false;
   }

   public int a(Random var1) {
      return 0;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.d(var3);
      super.a(var1, var2, var3, var4, var5, var6);
      this.e(var3);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   private void e(IBlockData var1) {
      float var2 = 0.25F;
      float var3 = 0.375F;
      float var4 = 0.625F;
      float var5 = 0.25F;
      float var6 = 0.75F;
      switch(BlockPistonExtension.SyntheticClass_1.a[((EnumDirection)var1.get(FACING)).ordinal()]) {
      case 1:
         this.a(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
         break;
      case 2:
         this.a(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
         break;
      case 3:
         this.a(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
         break;
      case 4:
         this.a(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
         break;
      case 5:
         this.a(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
         break;
      case 6:
         this.a(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
      }

   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.d(var1.getType(var2));
   }

   public void d(IBlockData var1) {
      float var2 = 0.25F;
      EnumDirection var3 = (EnumDirection)var1.get(FACING);
      if(var3 != null) {
         switch(BlockPistonExtension.SyntheticClass_1.a[var3.ordinal()]) {
         case 1:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
            break;
         case 2:
            this.a(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 3:
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
            break;
         case 4:
            this.a(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
            break;
         case 5:
            this.a(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
            break;
         case 6:
            this.a(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         }

      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      EnumDirection var5 = (EnumDirection)var3.get(FACING);
      BlockPosition var6 = var2.shift(var5.opposite());
      IBlockData var7 = var1.getType(var6);
      if(var7.getBlock() != Blocks.PISTON && var7.getBlock() != Blocks.STICKY_PISTON) {
         var1.setAir(var2);
      } else {
         var7.getBlock().doPhysics(var1, var6, var7, var4);
      }

   }

   public static EnumDirection b(int var0) {
      int var1 = var0 & 7;
      return var1 > 5?null:EnumDirection.fromType1(var1);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, b(var1)).set(TYPE, (var1 & 8) > 0?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT);
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(var1.get(TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, TYPE, SHORT});
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

   public static enum EnumPistonType implements INamable {
      DEFAULT("normal"),
      STICKY("sticky");

      private final String c;

      private EnumPistonType(String var3) {
         this.c = var3;
      }

      public String toString() {
         return this.c;
      }

      public String getName() {
         return this.c;
      }
   }
}
