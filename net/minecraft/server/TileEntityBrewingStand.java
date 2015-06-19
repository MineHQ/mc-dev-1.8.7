package net.minecraft.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.BlockBrewingStand;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerBrewingStand;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.IWorldInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemPotion;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.PotionBrewer;
import net.minecraft.server.TileEntityContainer;

public class TileEntityBrewingStand extends TileEntityContainer implements IUpdatePlayerListBox, IWorldInventory {
   private static final int[] a = new int[]{3};
   private static final int[] f = new int[]{0, 1, 2};
   private ItemStack[] items = new ItemStack[4];
   public int brewTime;
   private boolean[] i;
   private Item j;
   private String k;

   public TileEntityBrewingStand() {
   }

   public String getName() {
      return this.hasCustomName()?this.k:"container.brewing";
   }

   public boolean hasCustomName() {
      return this.k != null && this.k.length() > 0;
   }

   public void a(String var1) {
      this.k = var1;
   }

   public int getSize() {
      return this.items.length;
   }

   public void c() {
      if(this.brewTime > 0) {
         --this.brewTime;
         if(this.brewTime == 0) {
            this.o();
            this.update();
         } else if(!this.n()) {
            this.brewTime = 0;
            this.update();
         } else if(this.j != this.items[3].getItem()) {
            this.brewTime = 0;
            this.update();
         }
      } else if(this.n()) {
         this.brewTime = 400;
         this.j = this.items[3].getItem();
      }

      if(!this.world.isClientSide) {
         boolean[] var1 = this.m();
         if(!Arrays.equals(var1, this.i)) {
            this.i = var1;
            IBlockData var2 = this.world.getType(this.getPosition());
            if(!(var2.getBlock() instanceof BlockBrewingStand)) {
               return;
            }

            for(int var3 = 0; var3 < BlockBrewingStand.HAS_BOTTLE.length; ++var3) {
               var2 = var2.set(BlockBrewingStand.HAS_BOTTLE[var3], Boolean.valueOf(var1[var3]));
            }

            this.world.setTypeAndData(this.position, var2, 2);
         }
      }

   }

   private boolean n() {
      if(this.items[3] != null && this.items[3].count > 0) {
         ItemStack var1 = this.items[3];
         if(!var1.getItem().l(var1)) {
            return false;
         } else {
            boolean var2 = false;

            for(int var3 = 0; var3 < 3; ++var3) {
               if(this.items[var3] != null && this.items[var3].getItem() == Items.POTION) {
                  int var4 = this.items[var3].getData();
                  int var5 = this.c(var4, var1);
                  if(!ItemPotion.f(var4) && ItemPotion.f(var5)) {
                     var2 = true;
                     break;
                  }

                  List var6 = Items.POTION.e(var4);
                  List var7 = Items.POTION.e(var5);
                  if((var4 <= 0 || var6 != var7) && (var6 == null || !var6.equals(var7) && var7 != null) && var4 != var5) {
                     var2 = true;
                     break;
                  }
               }
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   private void o() {
      if(this.n()) {
         ItemStack var1 = this.items[3];

         for(int var2 = 0; var2 < 3; ++var2) {
            if(this.items[var2] != null && this.items[var2].getItem() == Items.POTION) {
               int var3 = this.items[var2].getData();
               int var4 = this.c(var3, var1);
               List var5 = Items.POTION.e(var3);
               List var6 = Items.POTION.e(var4);
               if((var3 <= 0 || var5 != var6) && (var5 == null || !var5.equals(var6) && var6 != null)) {
                  if(var3 != var4) {
                     this.items[var2].setData(var4);
                  }
               } else if(!ItemPotion.f(var3) && ItemPotion.f(var4)) {
                  this.items[var2].setData(var4);
               }
            }
         }

         if(var1.getItem().r()) {
            this.items[3] = new ItemStack(var1.getItem().q());
         } else {
            --this.items[3].count;
            if(this.items[3].count <= 0) {
               this.items[3] = null;
            }
         }

      }
   }

   private int c(int var1, ItemStack var2) {
      return var2 == null?var1:(var2.getItem().l(var2)?PotionBrewer.a(var1, var2.getItem().j(var2)):var1);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.getList("Items", 10);
      this.items = new ItemStack[this.getSize()];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         byte var5 = var4.getByte("Slot");
         if(var5 >= 0 && var5 < this.items.length) {
            this.items[var5] = ItemStack.createStack(var4);
         }
      }

      this.brewTime = var1.getShort("BrewTime");
      if(var1.hasKeyOfType("CustomName", 8)) {
         this.k = var1.getString("CustomName");
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setShort("BrewTime", (short)this.brewTime);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.items[var3].save(var4);
            var2.add(var4);
         }
      }

      var1.set("Items", var2);
      if(this.hasCustomName()) {
         var1.setString("CustomName", this.k);
      }

   }

   public ItemStack getItem(int var1) {
      return var1 >= 0 && var1 < this.items.length?this.items[var1]:null;
   }

   public ItemStack splitStack(int var1, int var2) {
      if(var1 >= 0 && var1 < this.items.length) {
         ItemStack var3 = this.items[var1];
         this.items[var1] = null;
         return var3;
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(var1 >= 0 && var1 < this.items.length) {
         ItemStack var2 = this.items[var1];
         this.items[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      if(var1 >= 0 && var1 < this.items.length) {
         this.items[var1] = var2;
      }

   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.position) != this?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return var1 == 3?var2.getItem().l(var2):var2.getItem() == Items.POTION || var2.getItem() == Items.GLASS_BOTTLE;
   }

   public boolean[] m() {
      boolean[] var1 = new boolean[3];

      for(int var2 = 0; var2 < 3; ++var2) {
         if(this.items[var2] != null) {
            var1[var2] = true;
         }
      }

      return var1;
   }

   public int[] getSlotsForFace(EnumDirection var1) {
      return var1 == EnumDirection.UP?a:f;
   }

   public boolean canPlaceItemThroughFace(int var1, ItemStack var2, EnumDirection var3) {
      return this.b(var1, var2);
   }

   public boolean canTakeItemThroughFace(int var1, ItemStack var2, EnumDirection var3) {
      return true;
   }

   public String getContainerName() {
      return "minecraft:brewing_stand";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerBrewingStand(var1, this);
   }

   public int getProperty(int var1) {
      switch(var1) {
      case 0:
         return this.brewTime;
      default:
         return 0;
      }
   }

   public void b(int var1, int var2) {
      switch(var1) {
      case 0:
         this.brewTime = var2;
      default:
      }
   }

   public int g() {
      return 1;
   }

   public void l() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

   }
}
