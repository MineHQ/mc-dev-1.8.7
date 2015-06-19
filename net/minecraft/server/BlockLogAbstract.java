package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.BlockLeaves;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockRotatable;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.INamable;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public abstract class BlockLogAbstract extends BlockRotatable {
   public static final BlockStateEnum<BlockLogAbstract.EnumLogRotation> AXIS = BlockStateEnum.of("axis", BlockLogAbstract.EnumLogRotation.class);

   public BlockLogAbstract() {
      super(Material.WOOD);
      this.a(CreativeModeTab.b);
      this.c(2.0F);
      this.a(f);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      byte var4 = 4;
      int var5 = var4 + 1;
      if(var1.areChunksLoadedBetween(var2.a(-var5, -var5, -var5), var2.a(var5, var5, var5))) {
         Iterator var6 = BlockPosition.a(var2.a(-var4, -var4, -var4), var2.a(var4, var4, var4)).iterator();

         while(var6.hasNext()) {
            BlockPosition var7 = (BlockPosition)var6.next();
            IBlockData var8 = var1.getType(var7);
            if(var8.getBlock().getMaterial() == Material.LEAVES && !((Boolean)var8.get(BlockLeaves.CHECK_DECAY)).booleanValue()) {
               var1.setTypeAndData(var7, var8.set(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
            }
         }

      }
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return super.getPlacedState(var1, var2, var3, var4, var5, var6, var7, var8).set(AXIS, BlockLogAbstract.EnumLogRotation.a(var3.k()));
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.EnumAxis.values().length];

      static {
         try {
            a[EnumDirection.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumLogRotation implements INamable {
      X("x"),
      Y("y"),
      Z("z"),
      NONE("none");

      private final String e;

      private EnumLogRotation(String var3) {
         this.e = var3;
      }

      public String toString() {
         return this.e;
      }

      public static BlockLogAbstract.EnumLogRotation a(EnumDirection.EnumAxis var0) {
         switch(BlockLogAbstract.SyntheticClass_1.a[var0.ordinal()]) {
         case 1:
            return X;
         case 2:
            return Y;
         case 3:
            return Z;
         default:
            return NONE;
         }
      }

      public String getName() {
         return this.e;
      }
   }
}
