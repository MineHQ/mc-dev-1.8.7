package net.minecraft.server;

import net.minecraft.server.ChestLock;
import net.minecraft.server.Container;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.IInventory;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.InventoryUtils;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.World;

public abstract class EntityMinecartContainer extends EntityMinecartAbstract implements ITileInventory {
   private ItemStack[] items = new ItemStack[36];
   private boolean b = true;

   public EntityMinecartContainer(World var1) {
      super(var1);
   }

   public EntityMinecartContainer(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public void a(DamageSource var1) {
      super.a(var1);
      if(this.world.getGameRules().getBoolean("doEntityDrops")) {
         InventoryUtils.dropEntity(this.world, this, this);
      }

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
            return var3;
         } else {
            var3 = this.items[var1].a(var2);
            if(this.items[var1].count == 0) {
               this.items[var1] = null;
            }

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

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

   }

   public void update() {
   }

   public boolean a(EntityHuman var1) {
      return this.dead?false:var1.h(this) <= 64.0D;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return true;
   }

   public String getName() {
      return this.hasCustomName()?this.getCustomName():"container.minecart";
   }

   public int getMaxStackSize() {
      return 64;
   }

   public void c(int var1) {
      this.b = false;
      super.c(var1);
   }

   public void die() {
      if(this.b) {
         InventoryUtils.dropEntity(this.world, this, this);
      }

      super.die();
   }

   protected void b(NBTTagCompound var1) {
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
   }

   protected void a(NBTTagCompound var1) {
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

   }

   public boolean e(EntityHuman var1) {
      if(!this.world.isClientSide) {
         var1.openContainer(this);
      }

      return true;
   }

   protected void o() {
      int var1 = 15 - Container.b((IInventory)this);
      float var2 = 0.98F + (float)var1 * 0.001F;
      this.motX *= (double)var2;
      this.motY *= 0.0D;
      this.motZ *= (double)var2;
   }

   public int getProperty(int var1) {
      return 0;
   }

   public void b(int var1, int var2) {
   }

   public int g() {
      return 0;
   }

   public boolean r_() {
      return false;
   }

   public void a(ChestLock var1) {
   }

   public ChestLock i() {
      return ChestLock.a;
   }

   public void l() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

   }
}
