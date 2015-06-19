package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class ItemBlock extends Item {
   protected final Block a;

   public ItemBlock(Block var1) {
      this.a = var1;
   }

   public ItemBlock b(String var1) {
      super.c(var1);
      return this;
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      IBlockData var9 = var3.getType(var4);
      Block var10 = var9.getBlock();
      if(!var10.a(var3, var4)) {
         var4 = var4.shift(var5);
      }

      if(var1.count == 0) {
         return false;
      } else if(!var2.a(var4, var5, var1)) {
         return false;
      } else if(var3.a(this.a, var4, false, var5, (Entity)null, var1)) {
         int var11 = this.filterData(var1.getData());
         IBlockData var12 = this.a.getPlacedState(var3, var4, var5, var6, var7, var8, var11, var2);
         if(var3.setTypeAndData(var4, var12, 3)) {
            var12 = var3.getType(var4);
            if(var12.getBlock() == this.a) {
               a(var3, var2, var4, var1);
               this.a.postPlace(var3, var4, var12, var2, var1);
            }

            var3.makeSound((double)((float)var4.getX() + 0.5F), (double)((float)var4.getY() + 0.5F), (double)((float)var4.getZ() + 0.5F), this.a.stepSound.getPlaceSound(), (this.a.stepSound.getVolume1() + 1.0F) / 2.0F, this.a.stepSound.getVolume2() * 0.8F);
            --var1.count;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean a(World var0, EntityHuman var1, BlockPosition var2, ItemStack var3) {
      MinecraftServer var4 = MinecraftServer.getServer();
      if(var4 == null) {
         return false;
      } else {
         if(var3.hasTag() && var3.getTag().hasKeyOfType("BlockEntityTag", 10)) {
            TileEntity var5 = var0.getTileEntity(var2);
            if(var5 != null) {
               if(!var0.isClientSide && var5.F() && !var4.getPlayerList().isOp(var1.getProfile())) {
                  return false;
               }

               NBTTagCompound var6 = new NBTTagCompound();
               NBTTagCompound var7 = (NBTTagCompound)var6.clone();
               var5.b(var6);
               NBTTagCompound var8 = (NBTTagCompound)var3.getTag().get("BlockEntityTag");
               var6.a(var8);
               var6.setInt("x", var2.getX());
               var6.setInt("y", var2.getY());
               var6.setInt("z", var2.getZ());
               if(!var6.equals(var7)) {
                  var5.a(var6);
                  var5.update();
                  return true;
               }
            }
         }

         return false;
      }
   }

   public String e_(ItemStack var1) {
      return this.a.a();
   }

   public String getName() {
      return this.a.a();
   }

   public Block d() {
      return this.a;
   }

   // $FF: synthetic method
   public Item c(String var1) {
      return this.b(var1);
   }
}
