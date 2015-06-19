package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Container;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.DispenseBehaviorItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IDispenseBehavior;
import net.minecraft.server.IPosition;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.InventoryUtils;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.Position;
import net.minecraft.server.RegistryDefault;
import net.minecraft.server.SourceBlock;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityDropper;
import net.minecraft.server.World;

public class BlockDispenser extends BlockContainer {
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
   public static final BlockStateBoolean TRIGGERED = BlockStateBoolean.of("triggered");
   public static final RegistryDefault<Item, IDispenseBehavior> N = new RegistryDefault(new DispenseBehaviorItem());
   protected Random O = new Random();

   protected BlockDispenser() {
      super(Material.STONE);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(TRIGGERED, Boolean.valueOf(false)));
      this.a((CreativeModeTab)CreativeModeTab.d);
   }

   public int a(World var1) {
      return 4;
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      super.onPlace(var1, var2, var3);
      this.e(var1, var2, var3);
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         EnumDirection var4 = (EnumDirection)var3.get(FACING);
         boolean var5 = var1.getType(var2.north()).getBlock().o();
         boolean var6 = var1.getType(var2.south()).getBlock().o();
         if(var4 == EnumDirection.NORTH && var5 && !var6) {
            var4 = EnumDirection.SOUTH;
         } else if(var4 == EnumDirection.SOUTH && var6 && !var5) {
            var4 = EnumDirection.NORTH;
         } else {
            boolean var7 = var1.getType(var2.west()).getBlock().o();
            boolean var8 = var1.getType(var2.east()).getBlock().o();
            if(var4 == EnumDirection.WEST && var7 && !var8) {
               var4 = EnumDirection.EAST;
            } else if(var4 == EnumDirection.EAST && var8 && !var7) {
               var4 = EnumDirection.WEST;
            }
         }

         var1.setTypeAndData(var2, var3.set(FACING, var4).set(TRIGGERED, Boolean.valueOf(false)), 2);
      }
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityDispenser) {
            var4.openContainer((TileEntityDispenser)var9);
            if(var9 instanceof TileEntityDropper) {
               var4.b(StatisticList.O);
            } else {
               var4.b(StatisticList.Q);
            }
         }

         return true;
      }
   }

   public void dispense(World var1, BlockPosition var2) {
      SourceBlock var3 = new SourceBlock(var1, var2);
      TileEntityDispenser var4 = (TileEntityDispenser)var3.getTileEntity();
      if(var4 != null) {
         int var5 = var4.m();
         if(var5 < 0) {
            var1.triggerEffect(1001, var2, 0);
         } else {
            ItemStack var6 = var4.getItem(var5);
            IDispenseBehavior var7 = this.a(var6);
            if(var7 != IDispenseBehavior.a) {
               ItemStack var8 = var7.a(var3, var6);
               var4.setItem(var5, var8.count <= 0?null:var8);
            }

         }
      }
   }

   protected IDispenseBehavior a(ItemStack var1) {
      return (IDispenseBehavior)N.get(var1 == null?null:var1.getItem());
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      boolean var5 = var1.isBlockIndirectlyPowered(var2) || var1.isBlockIndirectlyPowered(var2.up());
      boolean var6 = ((Boolean)var3.get(TRIGGERED)).booleanValue();
      if(var5 && !var6) {
         var1.a((BlockPosition)var2, (Block)this, this.a(var1));
         var1.setTypeAndData(var2, var3.set(TRIGGERED, Boolean.valueOf(true)), 4);
      } else if(!var5 && var6) {
         var1.setTypeAndData(var2, var3.set(TRIGGERED, Boolean.valueOf(false)), 4);
      }

   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         this.dispense(var1, var2);
      }

   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityDispenser();
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, BlockPiston.a(var1, var2, var8)).set(TRIGGERED, Boolean.valueOf(false));
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      var1.setTypeAndData(var2, var3.set(FACING, BlockPiston.a(var1, var2, var4)), 2);
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityDispenser) {
            ((TileEntityDispenser)var6).a(var5.getName());
         }
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      TileEntity var4 = var1.getTileEntity(var2);
      if(var4 instanceof TileEntityDispenser) {
         InventoryUtils.dropInventory(var1, var2, (TileEntityDispenser)var4);
         var1.updateAdjacentComparators(var2, this);
      }

      super.remove(var1, var2, var3);
   }

   public static IPosition a(ISourceBlock var0) {
      EnumDirection var1 = b(var0.f());
      double var2 = var0.getX() + 0.7D * (double)var1.getAdjacentX();
      double var4 = var0.getY() + 0.7D * (double)var1.getAdjacentY();
      double var6 = var0.getZ() + 0.7D * (double)var1.getAdjacentZ();
      return new Position(var2, var4, var6);
   }

   public static EnumDirection b(int var0) {
      return EnumDirection.fromType1(var0 & 7);
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return Container.a(var1.getTileEntity(var2));
   }

   public int b() {
      return 3;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, b(var1)).set(TRIGGERED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(((Boolean)var1.get(TRIGGERED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, TRIGGERED});
   }
}
