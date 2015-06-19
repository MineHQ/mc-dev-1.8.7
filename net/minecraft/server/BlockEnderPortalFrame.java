package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;

public class BlockEnderPortalFrame extends Block {
   public static final BlockStateDirection FACING;
   public static final BlockStateBoolean EYE;

   public BlockEnderPortalFrame() {
      super(Material.STONE, MaterialMapColor.C);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(EYE, Boolean.valueOf(false)));
   }

   public boolean c() {
      return false;
   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      if(((Boolean)var1.getType(var2).get(EYE)).booleanValue()) {
         this.a(0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      this.j();
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return null;
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection().opposite()).set(EYE, Boolean.valueOf(false));
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return ((Boolean)var1.getType(var2).get(EYE)).booleanValue()?15:0;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(EYE, Boolean.valueOf((var1 & 4) != 0)).set(FACING, EnumDirection.fromType2(var1 & 3));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).b();
      if(((Boolean)var1.get(EYE)).booleanValue()) {
         var3 |= 4;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, EYE});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      EYE = BlockStateBoolean.of("eye");
   }
}
