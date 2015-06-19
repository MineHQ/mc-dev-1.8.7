package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockTripwireHook;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockTripwire extends Block {
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
   public static final BlockStateBoolean SUSPENDED = BlockStateBoolean.of("suspended");
   public static final BlockStateBoolean ATTACHED = BlockStateBoolean.of("attached");
   public static final BlockStateBoolean DISARMED = BlockStateBoolean.of("disarmed");
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");

   public BlockTripwire() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)).set(SUSPENDED, Boolean.valueOf(false)).set(ATTACHED, Boolean.valueOf(false)).set(DISARMED, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)));
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
      this.a(true);
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(NORTH, Boolean.valueOf(c(var2, var3, var1, EnumDirection.NORTH))).set(EAST, Boolean.valueOf(c(var2, var3, var1, EnumDirection.EAST))).set(SOUTH, Boolean.valueOf(c(var2, var3, var1, EnumDirection.SOUTH))).set(WEST, Boolean.valueOf(c(var2, var3, var1, EnumDirection.WEST)));
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.STRING;
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      boolean var5 = ((Boolean)var3.get(SUSPENDED)).booleanValue();
      boolean var6 = !World.a((IBlockAccess)var1, (BlockPosition)var2.down());
      if(var5 != var6) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      boolean var4 = ((Boolean)var3.get(ATTACHED)).booleanValue();
      boolean var5 = ((Boolean)var3.get(SUSPENDED)).booleanValue();
      if(!var5) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
      } else if(!var4) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
      } else {
         this.a(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
      }

   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      var3 = var3.set(SUSPENDED, Boolean.valueOf(!World.a((IBlockAccess)var1, (BlockPosition)var2.down())));
      var1.setTypeAndData(var2, var3, 3);
      this.e(var1, var2, var3);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      this.e(var1, var2, var3.set(POWERED, Boolean.valueOf(true)));
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(!var1.isClientSide) {
         if(var4.bZ() != null && var4.bZ().getItem() == Items.SHEARS) {
            var1.setTypeAndData(var2, var3.set(DISARMED, Boolean.valueOf(true)), 4);
         }

      }
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      EnumDirection[] var4 = new EnumDirection[]{EnumDirection.SOUTH, EnumDirection.WEST};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EnumDirection var7 = var4[var6];

         for(int var8 = 1; var8 < 42; ++var8) {
            BlockPosition var9 = var2.shift(var7, var8);
            IBlockData var10 = var1.getType(var9);
            if(var10.getBlock() == Blocks.TRIPWIRE_HOOK) {
               if(var10.get(BlockTripwireHook.FACING) == var7.opposite()) {
                  Blocks.TRIPWIRE_HOOK.a(var1, var9, var10, false, true, var8, var3);
               }
               break;
            }

            if(var10.getBlock() != Blocks.TRIPWIRE) {
               break;
            }
         }
      }

   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      if(!var1.isClientSide) {
         if(!((Boolean)var3.get(POWERED)).booleanValue()) {
            this.e(var1, var2);
         }
      }
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Random var4) {
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         if(((Boolean)var1.getType(var2).get(POWERED)).booleanValue()) {
            this.e(var1, var2);
         }
      }
   }

   private void e(World var1, BlockPosition var2) {
      IBlockData var3 = var1.getType(var2);
      boolean var4 = ((Boolean)var3.get(POWERED)).booleanValue();
      boolean var5 = false;
      List var6 = var1.getEntities((Entity)null, new AxisAlignedBB((double)var2.getX() + this.minX, (double)var2.getY() + this.minY, (double)var2.getZ() + this.minZ, (double)var2.getX() + this.maxX, (double)var2.getY() + this.maxY, (double)var2.getZ() + this.maxZ));
      if(!var6.isEmpty()) {
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Entity var8 = (Entity)var7.next();
            if(!var8.aI()) {
               var5 = true;
               break;
            }
         }
      }

      if(var5 != var4) {
         var3 = var3.set(POWERED, Boolean.valueOf(var5));
         var1.setTypeAndData(var2, var3, 3);
         this.e(var1, var2, var3);
      }

      if(var5) {
         var1.a((BlockPosition)var2, (Block)this, this.a(var1));
      }

   }

   public static boolean c(IBlockAccess var0, BlockPosition var1, IBlockData var2, EnumDirection var3) {
      BlockPosition var4 = var1.shift(var3);
      IBlockData var5 = var0.getType(var4);
      Block var6 = var5.getBlock();
      if(var6 == Blocks.TRIPWIRE_HOOK) {
         EnumDirection var9 = var3.opposite();
         return var5.get(BlockTripwireHook.FACING) == var9;
      } else if(var6 == Blocks.TRIPWIRE) {
         boolean var7 = ((Boolean)var2.get(SUSPENDED)).booleanValue();
         boolean var8 = ((Boolean)var5.get(SUSPENDED)).booleanValue();
         return var7 == var8;
      } else {
         return false;
      }
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(POWERED, Boolean.valueOf((var1 & 1) > 0)).set(SUSPENDED, Boolean.valueOf((var1 & 2) > 0)).set(ATTACHED, Boolean.valueOf((var1 & 4) > 0)).set(DISARMED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      int var2 = 0;
      if(((Boolean)var1.get(POWERED)).booleanValue()) {
         var2 |= 1;
      }

      if(((Boolean)var1.get(SUSPENDED)).booleanValue()) {
         var2 |= 2;
      }

      if(((Boolean)var1.get(ATTACHED)).booleanValue()) {
         var2 |= 4;
      }

      if(((Boolean)var1.get(DISARMED)).booleanValue()) {
         var2 |= 8;
      }

      return var2;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{POWERED, SUSPENDED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH});
   }
}
