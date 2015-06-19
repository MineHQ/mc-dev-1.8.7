package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerDispenser;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.TileEntityContainer;

public class TileEntityDispenser extends TileEntityContainer implements IInventory {
   private static final Random f = new Random();
   private ItemStack[] items = new ItemStack[9];
   protected String a;

   public TileEntityDispenser() {
   }

   public int getSize() {
      return 9;
   }

   public ItemStack getItem(int var1) {
      return this.items[var1];
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.items[var1] != null) {
         ItemStack var3;
         if(this.items[var1].count <= var2) {
            var3 = this.items[var1];
            this.items[var1] = null;
            this.update();
            return var3;
         } else {
            var3 = this.items[var1].a(var2);
            if(this.items[var1].count == 0) {
               this.items[var1] = null;
            }

            this.update();
            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(this.items[var1] != null) {
         ItemStack var2 = this.items[var1];
         this.items[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public int m() {
      int var1 = -1;
      int var2 = 1;

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null && f.nextInt(var2++) == 0) {
            var1 = var3;
         }
      }

      return var1;
   }

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      this.update();
   }

   public int addItem(ItemStack var1) {
      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] == null || this.items[var2].getItem() == null) {
            this.setItem(var2, var1);
            return var2;
         }
      }

      return -1;
   }

   public String getName() {
      return this.hasCustomName()?this.a:"container.dispenser";
   }

   public void a(String var1) {
      this.a = var1;
   }

   public boolean hasCustomName() {
      return this.a != null;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.getList("Items", 10);
      this.items = new ItemStack[this.getSize()];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         int var5 = var4.getByte("Slot") & 255;
         if(var5 >= 0 && var5 < this.items.length) {
            this.items[var5] = ItemStack.createStack(var4);
         }
      }

      if(var1.hasKeyOfType("CustomName", 8)) {
         this.a = var1.getString("CustomName");
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
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
         var1.setString("CustomName", this.a);
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
      return true;
   }

   public String getContainerName() {
      return "minecraft:dispenser";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerDispenser(var1, this);
   }

   public int getProperty(int var1) {
      return 0;
   }

   public void b(int var1, int var2) {
   }

   public int g() {
      return 0;
   }

   public void l() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

   }
}
