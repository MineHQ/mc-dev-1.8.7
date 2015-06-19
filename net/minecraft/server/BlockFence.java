package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFenceGate;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.ItemLeash;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockFence extends Block {
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");

   public BlockFence(Material var1) {
      this(var1, var1.r());
   }

   public BlockFence(Material var1, MaterialMapColor var2) {
      super(var1, var2);
      this.j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
      this.a(CreativeModeTab.c);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      boolean var7 = this.e(var1, var2.north());
      boolean var8 = this.e(var1, var2.south());
      boolean var9 = this.e(var1, var2.west());
      boolean var10 = this.e(var1, var2.east());
      float var11 = 0.375F;
      float var12 = 0.625F;
      float var13 = 0.375F;
      float var14 = 0.625F;
      if(var7) {
         var13 = 0.0F;
      }

      if(var8) {
         var14 = 1.0F;
      }

      if(var7 || var8) {
         this.a(var11, 0.0F, var13, var12, 1.5F, var14);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      var13 = 0.375F;
      var14 = 0.625F;
      if(var9) {
         var11 = 0.0F;
      }

      if(var10) {
         var12 = 1.0F;
      }

      if(var9 || var10 || !var7 && !var8) {
         this.a(var11, 0.0F, var13, var12, 1.5F, var14);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if(var7) {
         var13 = 0.0F;
      }

      if(var8) {
         var14 = 1.0F;
      }

      this.a(var11, 0.0F, var13, var12, 1.0F, var14);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      boolean var3 = this.e(var1, var2.north());
      boolean var4 = this.e(var1, var2.south());
      boolean var5 = this.e(var1, var2.west());
      boolean var6 = this.e(var1, var2.east());
      float var7 = 0.375F;
      float var8 = 0.625F;
      float var9 = 0.375F;
      float var10 = 0.625F;
      if(var3) {
         var9 = 0.0F;
      }

      if(var4) {
         var10 = 1.0F;
      }

      if(var5) {
         var7 = 0.0F;
      }

      if(var6) {
         var8 = 1.0F;
      }

      this.a(var7, 0.0F, var9, var8, 1.0F, var10);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return false;
   }

   public boolean e(IBlockAccess var1, BlockPosition var2) {
      Block var3 = var1.getType(var2).getBlock();
      return var3 == Blocks.BARRIER?false:((!(var3 instanceof BlockFence) || var3.material != this.material) && !(var3 instanceof BlockFenceGate)?(var3.material.k() && var3.d()?var3.material != Material.PUMPKIN:false):true);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      return var1.isClientSide?true:ItemLeash.a(var4, var1, var2);
   }

   public int toLegacyData(IBlockData var1) {
      return 0;
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(NORTH, Boolean.valueOf(this.e(var2, var3.north()))).set(EAST, Boolean.valueOf(this.e(var2, var3.east()))).set(SOUTH, Boolean.valueOf(this.e(var2, var3.south()))).set(WEST, Boolean.valueOf(this.e(var2, var3.west())));
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{NORTH, EAST, WEST, SOUTH});
   }
}
