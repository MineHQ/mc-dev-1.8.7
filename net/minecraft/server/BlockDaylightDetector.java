package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityLightDetector;
import net.minecraft.server.World;

public class BlockDaylightDetector extends BlockContainer {
   public static final BlockStateInteger POWER = BlockStateInteger.of("power", 0, 15);
   private final boolean b;

   public BlockDaylightDetector(boolean var1) {
      super(Material.WOOD);
      this.b = var1;
      this.j(this.blockStateList.getBlockData().set(POWER, Integer.valueOf(0)));
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
      this.a(CreativeModeTab.d);
      this.c(0.2F);
      this.a(f);
      this.c("daylightDetector");
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return ((Integer)var3.get(POWER)).intValue();
   }

   public void f(World var1, BlockPosition var2) {
      if(!var1.worldProvider.o()) {
         IBlockData var3 = var1.getType(var2);
         int var4 = var1.b(EnumSkyBlock.SKY, var2) - var1.ab();
         float var5 = var1.d(1.0F);
         float var6 = var5 < 3.1415927F?0.0F:6.2831855F;
         var5 += (var6 - var5) * 0.2F;
         var4 = Math.round((float)var4 * MathHelper.cos(var5));
         var4 = MathHelper.clamp(var4, 0, 15);
         if(this.b) {
            var4 = 15 - var4;
         }

         if(((Integer)var3.get(POWER)).intValue() != var4) {
            var1.setTypeAndData(var2, var3.set(POWER, Integer.valueOf(var4)), 3);
         }

      }
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var4.cn()) {
         if(var1.isClientSide) {
            return true;
         } else {
            if(this.b) {
               var1.setTypeAndData(var2, Blocks.DAYLIGHT_DETECTOR.getBlockData().set(POWER, var3.get(POWER)), 4);
               Blocks.DAYLIGHT_DETECTOR.f(var1, var2);
            } else {
               var1.setTypeAndData(var2, Blocks.DAYLIGHT_DETECTOR_INVERTED.getBlockData().set(POWER, var3.get(POWER)), 4);
               Blocks.DAYLIGHT_DETECTOR_INVERTED.f(var1, var2);
            }

            return true;
         }
      } else {
         return super.interact(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.DAYLIGHT_DETECTOR);
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public int b() {
      return 3;
   }

   public boolean isPowerSource() {
      return true;
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityLightDetector();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(POWER, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(POWER)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{POWER});
   }
}
