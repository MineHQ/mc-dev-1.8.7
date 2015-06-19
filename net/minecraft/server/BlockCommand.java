package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityCommand;
import net.minecraft.server.World;

public class BlockCommand extends BlockContainer {
   public static final BlockStateBoolean TRIGGERED = BlockStateBoolean.of("triggered");

   public BlockCommand() {
      super(Material.ORE, MaterialMapColor.q);
      this.j(this.blockStateList.getBlockData().set(TRIGGERED, Boolean.valueOf(false)));
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityCommand();
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         boolean var5 = var1.isBlockIndirectlyPowered(var2);
         boolean var6 = ((Boolean)var3.get(TRIGGERED)).booleanValue();
         if(var5 && !var6) {
            var1.setTypeAndData(var2, var3.set(TRIGGERED, Boolean.valueOf(true)), 4);
            var1.a((BlockPosition)var2, (Block)this, this.a(var1));
         } else if(!var5 && var6) {
            var1.setTypeAndData(var2, var3.set(TRIGGERED, Boolean.valueOf(false)), 4);
         }
      }

   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      TileEntity var5 = var1.getTileEntity(var2);
      if(var5 instanceof TileEntityCommand) {
         ((TileEntityCommand)var5).getCommandBlock().a(var1);
         var1.updateAdjacentComparators(var2, this);
      }

   }

   public int a(World var1) {
      return 1;
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      TileEntity var9 = var1.getTileEntity(var2);
      return var9 instanceof TileEntityCommand?((TileEntityCommand)var9).getCommandBlock().a(var4):false;
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      return var3 instanceof TileEntityCommand?((TileEntityCommand)var3).getCommandBlock().j():0;
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      TileEntity var6 = var1.getTileEntity(var2);
      if(var6 instanceof TileEntityCommand) {
         CommandBlockListenerAbstract var7 = ((TileEntityCommand)var6).getCommandBlock();
         if(var5.hasName()) {
            var7.setName(var5.getName());
         }

         if(!var1.isClientSide) {
            var7.a(var1.getGameRules().getBoolean("sendCommandFeedback"));
         }

      }
   }

   public int a(Random var1) {
      return 0;
   }

   public int b() {
      return 3;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(TRIGGERED, Boolean.valueOf((var1 & 1) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;
      if(((Boolean)var1.get(TRIGGERED)).booleanValue()) {
         var2 |= 1;
      }

      return var2;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{TRIGGERED});
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(TRIGGERED, Boolean.valueOf(false));
   }
}
