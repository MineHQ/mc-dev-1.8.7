package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemDoor extends Item {
   private Block a;

   public ItemDoor(Block var1) {
      this.a = var1;
      this.a(CreativeModeTab.d);
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var5 != EnumDirection.UP) {
         return false;
      } else {
         IBlockData var9 = var3.getType(var4);
         Block var10 = var9.getBlock();
         if(!var10.a(var3, var4)) {
            var4 = var4.shift(var5);
         }

         if(!var2.a(var4, var5, var1)) {
            return false;
         } else if(!this.a.canPlace(var3, var4)) {
            return false;
         } else {
            a(var3, var4, EnumDirection.fromAngle((double)var2.yaw), this.a);
            --var1.count;
            return true;
         }
      }
   }

   public static void a(World var0, BlockPosition var1, EnumDirection var2, Block var3) {
      BlockPosition var4 = var1.shift(var2.e());
      BlockPosition var5 = var1.shift(var2.f());
      int var6 = (var0.getType(var5).getBlock().isOccluding()?1:0) + (var0.getType(var5.up()).getBlock().isOccluding()?1:0);
      int var7 = (var0.getType(var4).getBlock().isOccluding()?1:0) + (var0.getType(var4.up()).getBlock().isOccluding()?1:0);
      boolean var8 = var0.getType(var5).getBlock() == var3 || var0.getType(var5.up()).getBlock() == var3;
      boolean var9 = var0.getType(var4).getBlock() == var3 || var0.getType(var4.up()).getBlock() == var3;
      boolean var10 = false;
      if(var8 && !var9 || var7 > var6) {
         var10 = true;
      }

      BlockPosition var11 = var1.up();
      IBlockData var12 = var3.getBlockData().set(BlockDoor.FACING, var2).set(BlockDoor.HINGE, var10?BlockDoor.EnumDoorHinge.RIGHT:BlockDoor.EnumDoorHinge.LEFT);
      var0.setTypeAndData(var1, var12.set(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
      var0.setTypeAndData(var11, var12.set(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
      var0.applyPhysics(var1, var3);
      var0.applyPhysics(var11, var3);
   }
}
