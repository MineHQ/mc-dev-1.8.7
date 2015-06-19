package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.ChestLock;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;

public class InventoryLargeChest implements ITileInventory {
   private String a;
   public ITileInventory left;
   public ITileInventory right;

   public InventoryLargeChest(String var1, ITileInventory var2, ITileInventory var3) {
      this.a = var1;
      if(var2 == null) {
         var2 = var3;
      }

      if(var3 == null) {
         var3 = var2;
      }

      this.left = var2;
      this.right = var3;
      if(var2.r_()) {
         var3.a(var2.i());
      } else if(var3.r_()) {
         var2.a(var3.i());
      }

   }

   public int getSize() {
      return this.left.getSize() + this.right.getSize();
   }

   public boolean a(IInventory var1) {
      return this.left == var1 || this.right == var1;
   }

   public String getName() {
      return this.left.hasCustomName()?this.left.getName():(this.right.hasCustomName()?this.right.getName():this.a);
   }

   public boolean hasCustomName() {
      return this.left.hasCustomName() || this.right.hasCustomName();
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public ItemStack getItem(int var1) {
      return var1 >= this.left.getSize()?this.right.getItem(var1 - this.left.getSize()):this.left.getItem(var1);
   }

   public ItemStack splitStack(int var1, int var2) {
      return var1 >= this.left.getSize()?this.right.splitStack(var1 - this.left.getSize(), var2):this.left.splitStack(var1, var2);
   }

   public ItemStack splitWithoutUpdate(int var1) {
      return var1 >= this.left.getSize()?this.right.splitWithoutUpdate(var1 - this.left.getSize()):this.left.splitWithoutUpdate(var1);
   }

   public void setItem(int var1, ItemStack var2) {
      if(var1 >= this.left.getSize()) {
         this.right.setItem(var1 - this.left.getSize(), var2);
      } else {
         this.left.setItem(var1, var2);
      }

   }

   public int getMaxStackSize() {
      return this.left.getMaxStackSize();
   }

   public void update() {
      this.left.update();
      this.right.update();
   }

   public boolean a(EntityHuman var1) {
      return this.left.a(var1) && this.right.a(var1);
   }

   public void startOpen(EntityHuman var1) {
      this.left.startOpen(var1);
      this.right.startOpen(var1);
   }

   public void closeContainer(EntityHuman var1) {
      this.left.closeContainer(var1);
      this.right.closeContainer(var1);
   }

   public boolean b(int var1, ItemStack var2) {
      return true;
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
      return this.left.r_() || this.right.r_();
   }

   public void a(ChestLock var1) {
      this.left.a(var1);
      this.right.a(var1);
   }

   public ChestLock i() {
      return this.left.i();
   }

   public String getContainerName() {
      return this.left.getContainerName();
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerChest(var1, this, var2);
   }

   public void l() {
      this.left.l();
      this.right.l();
   }
}
