package net.minecraft.server;

import net.minecraft.server.BlockDispenser;
import net.minecraft.server.BlockMinecartTrackAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.DispenseBehaviorItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IDispenseBehavior;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class ItemMinecart extends Item {
   private static final IDispenseBehavior a = new DispenseBehaviorItem() {
      private final DispenseBehaviorItem b = new DispenseBehaviorItem();

      public ItemStack b(ISourceBlock var1, ItemStack var2) {
         EnumDirection var3 = BlockDispenser.b(var1.f());
         World var4 = var1.i();
         double var5 = var1.getX() + (double)var3.getAdjacentX() * 1.125D;
         double var7 = Math.floor(var1.getY()) + (double)var3.getAdjacentY();
         double var9 = var1.getZ() + (double)var3.getAdjacentZ() * 1.125D;
         BlockPosition var11 = var1.getBlockPosition().shift(var3);
         IBlockData var12 = var4.getType(var11);
         BlockMinecartTrackAbstract.EnumTrackPosition var13 = var12.getBlock() instanceof BlockMinecartTrackAbstract?(BlockMinecartTrackAbstract.EnumTrackPosition)var12.get(((BlockMinecartTrackAbstract)var12.getBlock()).n()):BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
         double var14;
         if(BlockMinecartTrackAbstract.d(var12)) {
            if(var13.c()) {
               var14 = 0.6D;
            } else {
               var14 = 0.1D;
            }
         } else {
            if(var12.getBlock().getMaterial() != Material.AIR || !BlockMinecartTrackAbstract.d(var4.getType(var11.down()))) {
               return this.b.a(var1, var2);
            }

            IBlockData var16 = var4.getType(var11.down());
            BlockMinecartTrackAbstract.EnumTrackPosition var17 = var16.getBlock() instanceof BlockMinecartTrackAbstract?(BlockMinecartTrackAbstract.EnumTrackPosition)var16.get(((BlockMinecartTrackAbstract)var16.getBlock()).n()):BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
            if(var3 != EnumDirection.DOWN && var17.c()) {
               var14 = -0.4D;
            } else {
               var14 = -0.9D;
            }
         }

         EntityMinecartAbstract var18 = EntityMinecartAbstract.a(var4, var5, var7 + var14, var9, ((ItemMinecart)var2.getItem()).b);
         if(var2.hasName()) {
            var18.setCustomName(var2.getName());
         }

         var4.addEntity(var18);
         var2.a(1);
         return var2;
      }

      protected void a(ISourceBlock var1) {
         var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
      }
   };
   private final EntityMinecartAbstract.EnumMinecartType b;

   public ItemMinecart(EntityMinecartAbstract.EnumMinecartType var1) {
      this.maxStackSize = 1;
      this.b = var1;
      this.a(CreativeModeTab.e);
      BlockDispenser.N.a(this, a);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      IBlockData var9 = var3.getType(var4);
      if(BlockMinecartTrackAbstract.d(var9)) {
         if(!var3.isClientSide) {
            BlockMinecartTrackAbstract.EnumTrackPosition var10 = var9.getBlock() instanceof BlockMinecartTrackAbstract?(BlockMinecartTrackAbstract.EnumTrackPosition)var9.get(((BlockMinecartTrackAbstract)var9.getBlock()).n()):BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH;
            double var11 = 0.0D;
            if(var10.c()) {
               var11 = 0.5D;
            }

            EntityMinecartAbstract var13 = EntityMinecartAbstract.a(var3, (double)var4.getX() + 0.5D, (double)var4.getY() + 0.0625D + var11, (double)var4.getZ() + 0.5D, this.b);
            if(var1.hasName()) {
               var13.setCustomName(var1.getName());
            }

            var3.addEntity(var13);
         }

         --var1.count;
         return true;
      } else {
         return false;
      }
   }
}
