package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockSnow extends Block {
   public static final BlockStateInteger LAYERS = BlockStateInteger.of("layers", 1, 8);

   protected BlockSnow() {
      super(Material.PACKED_ICE);
      this.j(this.blockStateList.getBlockData().set(LAYERS, Integer.valueOf(1)));
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      this.a(true);
      this.a(CreativeModeTab.c);
      this.j();
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return ((Integer)var1.getType(var2).get(LAYERS)).intValue() < 5;
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      int var4 = ((Integer)var3.get(LAYERS)).intValue() - 1;
      float var5 = 0.125F;
      return new AxisAlignedBB((double)var2.getX() + this.minX, (double)var2.getY() + this.minY, (double)var2.getZ() + this.minZ, (double)var2.getX() + this.maxX, (double)((float)var2.getY() + (float)var4 * var5), (double)var2.getZ() + this.maxZ);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void j() {
      this.b(0);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      this.b(((Integer)var3.get(LAYERS)).intValue());
   }

   protected void b(int var1) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, (float)var1 / 8.0F, 1.0F);
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2.down());
      Block var4 = var3.getBlock();
      return var4 != Blocks.ICE && var4 != Blocks.PACKED_ICE?(var4.getMaterial() == Material.LEAVES?true:(var4 == this && ((Integer)var3.get(LAYERS)).intValue() >= 7?true:var4.c() && var4.material.isSolid())):false;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      this.e(var1, var2, var3);
   }

   private boolean e(World var1, BlockPosition var2, IBlockData var3) {
      if(!this.canPlace(var1, var2)) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
         return false;
      } else {
         return true;
      }
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      a(var1, var3, new ItemStack(Items.SNOWBALL, ((Integer)var4.get(LAYERS)).intValue() + 1, 0));
      var1.setAir(var3);
      var2.b(StatisticList.MINE_BLOCK_COUNT[Block.getId(this)]);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.SNOWBALL;
   }

   public int a(Random var1) {
      return 0;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var1.b(EnumSkyBlock.BLOCK, var2) > 11) {
         this.b(var1, var2, var1.getType(var2), 0);
         var1.setAir(var2);
      }

   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(LAYERS, Integer.valueOf((var1 & 7) + 1));
   }

   public boolean a(World var1, BlockPosition var2) {
      return ((Integer)var1.getType(var2).get(LAYERS)).intValue() == 1;
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(LAYERS)).intValue() - 1;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{LAYERS});
   }
}
