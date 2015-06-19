package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockThin extends Block {
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
   private final boolean a;

   protected BlockThin(Material var1, boolean var2) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
      this.a = var2;
      this.a(CreativeModeTab.c);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(NORTH, Boolean.valueOf(this.c(var2.getType(var3.north()).getBlock()))).set(SOUTH, Boolean.valueOf(this.c(var2.getType(var3.south()).getBlock()))).set(WEST, Boolean.valueOf(this.c(var2.getType(var3.west()).getBlock()))).set(EAST, Boolean.valueOf(this.c(var2.getType(var3.east()).getBlock())));
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return !this.a?null:super.getDropType(var1, var2, var3);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      boolean var7 = this.c(var1.getType(var2.north()).getBlock());
      boolean var8 = this.c(var1.getType(var2.south()).getBlock());
      boolean var9 = this.c(var1.getType(var2.west()).getBlock());
      boolean var10 = this.c(var1.getType(var2.east()).getBlock());
      if((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
         if(var9) {
            this.a(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
            super.a(var1, var2, var3, var4, var5, var6);
         } else if(var10) {
            this.a(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.a(var1, var2, var3, var4, var5, var6);
         }
      } else {
         this.a(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

      if((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
         if(var7) {
            this.a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
            super.a(var1, var2, var3, var4, var5, var6);
         } else if(var8) {
            this.a(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
            super.a(var1, var2, var3, var4, var5, var6);
         }
      } else {
         this.a(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
         super.a(var1, var2, var3, var4, var5, var6);
      }

   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      float var3 = 0.4375F;
      float var4 = 0.5625F;
      float var5 = 0.4375F;
      float var6 = 0.5625F;
      boolean var7 = this.c(var1.getType(var2.north()).getBlock());
      boolean var8 = this.c(var1.getType(var2.south()).getBlock());
      boolean var9 = this.c(var1.getType(var2.west()).getBlock());
      boolean var10 = this.c(var1.getType(var2.east()).getBlock());
      if((!var9 || !var10) && (var9 || var10 || var7 || var8)) {
         if(var9) {
            var3 = 0.0F;
         } else if(var10) {
            var4 = 1.0F;
         }
      } else {
         var3 = 0.0F;
         var4 = 1.0F;
      }

      if((!var7 || !var8) && (var9 || var10 || var7 || var8)) {
         if(var7) {
            var5 = 0.0F;
         } else if(var8) {
            var6 = 1.0F;
         }
      } else {
         var5 = 0.0F;
         var6 = 1.0F;
      }

      this.a(var3, 0.0F, var5, var4, 1.0F, var6);
   }

   public final boolean c(Block var1) {
      return var1.o() || var1 == this || var1 == Blocks.GLASS || var1 == Blocks.STAINED_GLASS || var1 == Blocks.STAINED_GLASS_PANE || var1 instanceof BlockThin;
   }

   protected boolean I() {
      return true;
   }

   public int toLegacyData(IBlockData var1) {
      return 0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{NORTH, EAST, WEST, SOUTH});
   }
}
