package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockMinecartTrackAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Container;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityMinecartCommandBlock;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.IInventory;
import net.minecraft.server.World;

public class BlockMinecartDetector extends BlockMinecartTrackAbstract {
   public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.a("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class, new Predicate() {
      public boolean a(BlockMinecartTrackAbstract.EnumTrackPosition var1) {
         return var1 != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST && var1 != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((BlockMinecartTrackAbstract.EnumTrackPosition)var1);
      }
   });
   public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");

   public BlockMinecartDetector() {
      super(true);
      this.j(this.blockStateList.getBlockData().set(POWERED, Boolean.valueOf(false)).set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH));
      this.a(true);
   }

   public int a(World var1) {
      return 20;
   }

   public boolean isPowerSource() {
      return true;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      if(!var1.isClientSide) {
         if(!((Boolean)var3.get(POWERED)).booleanValue()) {
            this.e(var1, var2, var3);
         }
      }
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Random var4) {
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide && ((Boolean)var3.get(POWERED)).booleanValue()) {
         this.e(var1, var2, var3);
      }
   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return ((Boolean)var3.get(POWERED)).booleanValue()?15:0;
   }

   public int b(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return !((Boolean)var3.get(POWERED)).booleanValue()?0:(var4 == EnumDirection.UP?15:0);
   }

   private void e(World var1, BlockPosition var2, IBlockData var3) {
      boolean var4 = ((Boolean)var3.get(POWERED)).booleanValue();
      boolean var5 = false;
      List var6 = this.a(var1, var2, EntityMinecartAbstract.class, new Predicate[0]);
      if(!var6.isEmpty()) {
         var5 = true;
      }

      if(var5 && !var4) {
         var1.setTypeAndData(var2, var3.set(POWERED, Boolean.valueOf(true)), 3);
         var1.applyPhysics(var2, this);
         var1.applyPhysics(var2.down(), this);
         var1.b(var2, var2);
      }

      if(!var5 && var4) {
         var1.setTypeAndData(var2, var3.set(POWERED, Boolean.valueOf(false)), 3);
         var1.applyPhysics(var2, this);
         var1.applyPhysics(var2.down(), this);
         var1.b(var2, var2);
      }

      if(var5) {
         var1.a((BlockPosition)var2, (Block)this, this.a(var1));
      }

      var1.updateAdjacentComparators(var2, this);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      super.onPlace(var1, var2, var3);
      this.e(var1, var2, var3);
   }

   public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> n() {
      return SHAPE;
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      if(((Boolean)var1.getType(var2).get(POWERED)).booleanValue()) {
         List var3 = this.a(var1, var2, EntityMinecartCommandBlock.class, new Predicate[0]);
         if(!var3.isEmpty()) {
            return ((EntityMinecartCommandBlock)var3.get(0)).getCommandBlock().j();
         }

         List var4 = this.a(var1, var2, EntityMinecartAbstract.class, new Predicate[]{IEntitySelector.c});
         if(!var4.isEmpty()) {
            return Container.b((IInventory)var4.get(0));
         }
      }

      return 0;
   }

   protected <T extends EntityMinecartAbstract> List<T> a(World var1, BlockPosition var2, Class<T> var3, Predicate... var4) {
      AxisAlignedBB var5 = this.a(var2);
      return var4.length != 1?var1.a(var3, var5):var1.a(var3, var5, var4[0]);
   }

   private AxisAlignedBB a(BlockPosition var1) {
      float var2 = 0.2F;
      return new AxisAlignedBB((double)((float)var1.getX() + 0.2F), (double)var1.getY(), (double)((float)var1.getZ() + 0.2F), (double)((float)(var1.getX() + 1) - 0.2F), (double)((float)(var1.getY() + 1) - 0.2F), (double)((float)(var1.getZ() + 1) - 0.2F));
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(var1 & 7)).set(POWERED, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockMinecartTrackAbstract.EnumTrackPosition)var1.get(SHAPE)).a();
      if(((Boolean)var1.get(POWERED)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{SHAPE, POWERED});
   }
}
