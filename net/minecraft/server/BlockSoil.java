package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStem;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockSoil extends Block {
   public static final BlockStateInteger MOISTURE = BlockStateInteger.of("moisture", 0, 7);

   protected BlockSoil() {
      super(Material.EARTH);
      this.j(this.blockStateList.getBlockData().set(MOISTURE, Integer.valueOf(0)));
      this.a(true);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
      this.e(255);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return new AxisAlignedBB((double)var2.getX(), (double)var2.getY(), (double)var2.getZ(), (double)(var2.getX() + 1), (double)(var2.getY() + 1), (double)(var2.getZ() + 1));
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      int var5 = ((Integer)var3.get(MOISTURE)).intValue();
      if(!this.f(var1, var2) && !var1.isRainingAt(var2.up())) {
         if(var5 > 0) {
            var1.setTypeAndData(var2, var3.set(MOISTURE, Integer.valueOf(var5 - 1)), 2);
         } else if(!this.e(var1, var2)) {
            var1.setTypeUpdate(var2, Blocks.DIRT.getBlockData());
         }
      } else if(var5 < 7) {
         var1.setTypeAndData(var2, var3.set(MOISTURE, Integer.valueOf(7)), 2);
      }

   }

   public void a(World var1, BlockPosition var2, Entity var3, float var4) {
      if(var3 instanceof EntityLiving) {
         if(!var1.isClientSide && var1.random.nextFloat() < var4 - 0.5F) {
            if(!(var3 instanceof EntityHuman) && !var1.getGameRules().getBoolean("mobGriefing")) {
               return;
            }

            var1.setTypeUpdate(var2, Blocks.DIRT.getBlockData());
         }

         super.a(var1, var2, var3, var4);
      }
   }

   private boolean e(World var1, BlockPosition var2) {
      Block var3 = var1.getType(var2.up()).getBlock();
      return var3 instanceof BlockCrops || var3 instanceof BlockStem;
   }

   private boolean f(World var1, BlockPosition var2) {
      Iterator var3 = BlockPosition.b(var2.a(-4, 0, -4), var2.a(4, 1, 4)).iterator();

      BlockPosition.MutableBlockPosition var4;
      do {
         if(!var3.hasNext()) {
            return false;
         }

         var4 = (BlockPosition.MutableBlockPosition)var3.next();
      } while(var1.getType(var4).getBlock().getMaterial() != Material.WATER);

      return true;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      super.doPhysics(var1, var2, var3, var4);
      if(var1.getType(var2.up()).getBlock().getMaterial().isBuildable()) {
         var1.setTypeUpdate(var2, Blocks.DIRT.getBlockData());
      }

   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Blocks.DIRT.getDropType(Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT), var2, var3);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(MOISTURE, Integer.valueOf(var1 & 7));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(MOISTURE)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{MOISTURE});
   }
}
