package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Block;
import net.minecraft.server.BlockLeaves;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockWood;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockLeaves1 extends BlockLeaves {
   public static final BlockStateEnum<BlockWood.EnumLogVariant> VARIANT = BlockStateEnum.a("variant", BlockWood.EnumLogVariant.class, new Predicate() {
      public boolean a(BlockWood.EnumLogVariant var1) {
         return var1.a() < 4;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((BlockWood.EnumLogVariant)var1);
      }
   });

   public BlockLeaves1() {
      this.j(this.blockStateList.getBlockData().set(VARIANT, BlockWood.EnumLogVariant.OAK).set(CHECK_DECAY, Boolean.valueOf(true)).set(DECAYABLE, Boolean.valueOf(true)));
   }

   protected void a(World var1, BlockPosition var2, IBlockData var3, int var4) {
      if(var3.get(VARIANT) == BlockWood.EnumLogVariant.OAK && var1.random.nextInt(var4) == 0) {
         a(var1, var2, new ItemStack(Items.APPLE, 1, 0));
      }

   }

   protected int d(IBlockData var1) {
      return var1.get(VARIANT) == BlockWood.EnumLogVariant.JUNGLE?40:super.d(var1);
   }

   protected ItemStack i(IBlockData var1) {
      return new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a());
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(VARIANT, this.b(var1)).set(DECAYABLE, Boolean.valueOf((var1 & 4) == 0)).set(CHECK_DECAY, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
      if(!((Boolean)var1.get(DECAYABLE)).booleanValue()) {
         var3 |= 4;
      }

      if(((Boolean)var1.get(CHECK_DECAY)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   public BlockWood.EnumLogVariant b(int var1) {
      return BlockWood.EnumLogVariant.a((var1 & 3) % 4);
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{VARIANT, CHECK_DECAY, DECAYABLE});
   }

   public int getDropData(IBlockData var1) {
      return ((BlockWood.EnumLogVariant)var1.get(VARIANT)).a();
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      if(!var1.isClientSide && var2.bZ() != null && var2.bZ().getItem() == Items.SHEARS) {
         var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
         a(var1, var3, new ItemStack(Item.getItemOf(this), 1, ((BlockWood.EnumLogVariant)var4.get(VARIANT)).a()));
      } else {
         super.a(var1, var2, var3, var4, var5);
      }
   }
}
