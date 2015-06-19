package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public abstract class BlockWoodenStep extends BlockStepAbstract {
   public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.of("variant", BlockWood.EnumLogVariant.class);

   public BlockWoodenStep() {
      super(Material.WOOD);
      IBlockData var1 = this.blockStateList.getBlockData();
      if(!this.l()) {
         var1 = var1.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
      }

      this.j(var1.set(VARIANT, BlockWood.EnumLogVariant.OAK));
      this.a(CreativeModeTab.b);
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).c();
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.WOODEN_SLAB);
   }

   public String b(int var1) {
      return super.a() + "." + BlockWood.EnumLogVariant.a(var1).d();
   }

   public IBlockState<?> n() {
      return VARIANT;
   }

   public Object a(ItemStack var1) {
      return BlockWood.EnumLogVariant.a(var1.getData() & 7);
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.a(var1 & 7));
      if(!this.l()) {
         var2 = var2.set(HALF, (var1 & 8) == 0?BlockStepAbstract.EnumSlabHalf.BOTTOM:BlockStepAbstract.EnumSlabHalf.TOP);
      }

      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
      if(!this.l() && var1.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return this.l()?new BlockStateList(this, new IBlockState[]{VARIANT}):new BlockStateList(this, new IBlockState[]{HALF, VARIANT});
   }

   public int getDropData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
   }
}
