package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.BlockLogAbstract;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockWood;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MaterialMapColor;

public class BlockLog1 extends BlockLogAbstract {
   public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.a("variant", BlockWood.EnumLogVariant.class, new Predicate() {
      public boolean a(BlockWood.EnumLogVariant var1) {
         return var1.a() < 4;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((BlockWood.EnumLogVariant)var1);
      }
   });

   public BlockLog1() {
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.OAK).set(AXIS, BlockLogAbstract.EnumLogRotation.Y));
   }

   public MaterialMapColor g(IBlockData var1) {
      BlockWood.EnumLogVariant var2 = (BlockWood.EnumLogVariant)var1.get(VARIANT);
      switch(BlockLog1.SyntheticClass_1.b[((BlockLogAbstract.EnumLogRotation)var1.get(AXIS)).ordinal()]) {
      case 1:
      case 2:
      case 3:
      default:
         switch(BlockLog1.SyntheticClass_1.a[var2.ordinal()]) {
         case 1:
         default:
            return BlockWood.EnumLogVariant.SPRUCE.c();
         case 2:
            return BlockWood.EnumLogVariant.DARK_OAK.c();
         case 3:
            return MaterialMapColor.p;
         case 4:
            return BlockWood.EnumLogVariant.SPRUCE.c();
         }
      case 4:
         return var2.c();
      }
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.a((var1 & 3) % 4));
      switch(var1 & 12) {
      case 0:
         var2 = var2.set(AXIS, BlockLogAbstract.EnumLogRotation.Y);
         break;
      case 4:
         var2 = var2.set(AXIS, BlockLogAbstract.EnumLogRotation.X);
         break;
      case 8:
         var2 = var2.set(AXIS, BlockLogAbstract.EnumLogRotation.Z);
         break;
      default:
         var2 = var2.set(AXIS, BlockLogAbstract.EnumLogRotation.NONE);
      }

      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
      switch(BlockLog1.SyntheticClass_1.b[((BlockLogAbstract.EnumLogRotation)var1.get(AXIS)).ordinal()]) {
      case 1:
         var3 |= 4;
         break;
      case 2:
         var3 |= 8;
         break;
      case 3:
         var3 |= 12;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT, AXIS});
   }

   protected ItemStack i(IBlockData var1) {
      return new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a());
   }

   public int getDropData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b = new int[BlockLogAbstract.EnumLogRotation.values().length];

      static {
         try {
            b[BlockLogAbstract.EnumLogRotation.X.ordinal()] = 1;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[BlockLogAbstract.EnumLogRotation.Z.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            b[BlockLogAbstract.EnumLogRotation.NONE.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            b[BlockLogAbstract.EnumLogRotation.Y.ordinal()] = 4;
         } catch (NoSuchFieldError var5) {
            ;
         }

         a = new int[BlockWood.EnumLogVariant.values().length];

         try {
            a[BlockWood.EnumLogVariant.OAK.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.SPRUCE.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.BIRCH.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.JUNGLE.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
