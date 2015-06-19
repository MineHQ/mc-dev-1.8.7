package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntityEnderChest;

public class InventoryEnderChest extends InventorySubcontainer {
   private TileEntityEnderChest a;

   public InventoryEnderChest() {
      super("container.enderchest", false, 27);
   }

   public void a(TileEntityEnderChest var1) {
      this.a = var1;
   }

   public void a(NBTTagList var1) {
      int var2;
      for(var2 = 0; var2 < this.getSize(); ++var2) {
         this.setItem(var2, (ItemStack)null);
      }

      for(var2 = 0; var2 < var1.size(); ++var2) {
         NBTTagCompound var3 = var1.get(var2);
         int var4 = var3.getByte("Slot") & 255;
         if(var4 >= 0 && var4 < this.getSize()) {
            this.setItem(var4, ItemStack.createStack(var3));
         }
      }

   }

   public NBTTagList h() {
      NBTTagList var1 = new NBTTagList();

      for(int var2 = 0; var2 < this.getSize(); ++var2) {
         ItemStack var3 = this.getItem(var2);
         if(var3 != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var2);
            var3.save(var4);
            var1.add(var4);
         }
      }

      return var1;
   }

   public boolean a(EntityHuman var1) {
      return this.a != null && !this.a.a(var1)?false:super.a(var1);
   }

   public void startOpen(EntityHuman var1) {
      if(this.a != null) {
         this.a.b();
      }

      super.startOpen(var1);
   }

   public void closeContainer(EntityHuman var1) {
      if(this.a != null) {
         this.a.d();
      }

      super.closeContainer(var1);
      this.a = null;
   }
}
