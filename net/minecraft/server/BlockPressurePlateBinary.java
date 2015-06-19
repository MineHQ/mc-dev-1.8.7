package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockPressurePlateAbstract;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockPressurePlateBinary extends BlockPressurePlateAbstract {
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
   private final BlockPressurePlateBinary.EnumMobType b;

   protected BlockPressurePlateBinary(Material var1, BlockPressurePlateBinary.EnumMobType var2) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)));
      this.b = var2;
   }

   protected int e(IBlockData var1) {
      return ((Boolean)var1.get(POWERED)).booleanValue()?15:0;
   }

   protected IBlockData a(IBlockData var1, int var2) {
      return var1.set(POWERED, Boolean.valueOf(var2 > 0));
   }

   protected int f(World var1, BlockPosition var2) {
      AxisAlignedBB var3 = this.a(var2);
      List var4;
      switch(BlockPressurePlateBinary.SyntheticClass_1.a[this.b.ordinal()]) {
      case 1:
         var4 = var1.getEntities((Entity)null, var3);
         break;
      case 2:
         var4 = var1.a(EntityLiving.class, var3);
         break;
      default:
         return 0;
      }

      if(!var4.isEmpty()) {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Entity var6 = (Entity)var5.next();
            if(!var6.aI()) {
               return 15;
            }
         }
      }

      return 0;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(POWERED, Boolean.valueOf(var1 == 1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Boolean)var1.get(POWERED)).booleanValue()?1:0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{POWERED});
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[BlockPressurePlateBinary.EnumMobType.values().length];

      static {
         try {
            a[BlockPressurePlateBinary.EnumMobType.EVERYTHING.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockPressurePlateBinary.EnumMobType.MOBS.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumMobType {
      EVERYTHING,
      MOBS;

      private EnumMobType() {
      }
   }
}
