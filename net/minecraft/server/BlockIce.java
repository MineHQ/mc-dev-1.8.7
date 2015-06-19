package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockHalfTransparent;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockIce extends BlockHalfTransparent {
   public BlockIce() {
      super(Material.ICE, false);
      this.frictionFactor = 0.98F;
      this.a(true);
      this.a(CreativeModeTab.b);
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
      var2.applyExhaustion(0.025F);
      if(this.I() && EnchantmentManager.hasSilkTouchEnchantment(var2)) {
         ItemStack var8 = this.i(var4);
         if(var8 != null) {
            a(var1, var3, var8);
         }
      } else {
         if(var1.worldProvider.n()) {
            var1.setAir(var3);
            return;
         }

         int var6 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(var2);
         this.b(var1, var3, var4, var6);
         Material var7 = var1.getType(var3.down()).getBlock().getMaterial();
         if(var7.isSolid() || var7.isLiquid()) {
            var1.setTypeUpdate(var3, Blocks.FLOWING_WATER.getBlockData());
         }
      }

   }

   public int a(Random var1) {
      return 0;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var1.b(EnumSkyBlock.BLOCK, var2) > 11 - this.p()) {
         if(var1.worldProvider.n()) {
            var1.setAir(var2);
         } else {
            this.b(var1, var2, var1.getType(var2), 0);
            var1.setTypeUpdate(var2, Blocks.WATER.getBlockData());
         }
      }
   }

   public int k() {
      return 0;
   }
}
