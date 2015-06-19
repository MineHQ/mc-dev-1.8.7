package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockChest;
import net.minecraft.server.BlockHopper;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerHopper;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.IHopper;
import net.minecraft.server.IInventory;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.IWorldInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityContainer;
import net.minecraft.server.World;

public class TileEntityHopper extends TileEntityContainer implements IHopper, IUpdatePlayerListBox {
   private ItemStack[] items = new ItemStack[5];
   private String f;
   private int g = -1;

   public TileEntityHopper() {
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.getList("Items", 10);
      this.items = new ItemStack[this.getSize()];
      if(var1.hasKeyOfType("CustomName", 8)) {
         this.f = var1.getString("CustomName");
      }

      this.g = var1.getInt("TransferCooldown");

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         byte var5 = var4.getByte("Slot");
         if(var5 >= 0 && var5 < this.items.length) {
            this.items[var5] = ItemStack.createStack(var4);
         }
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
      var1.setInt("TransferCooldown", this.g);
      if(this.hasCustomName()) {
         var1.setString("CustomName", this.f);
      }

   }

   public void update() {
      super.update();
   }

   public int getSize() {
      return this.items.length;
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

   public String getName() {
      return this.hasCustomName()?this.f:"container.hopper";
   }

   public boolean hasCustomName() {
      return this.f != null && this.f.length() > 0;
   }

   public void a(String var1) {
      this.f = var1;
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

   public void c() {
      if(this.world != null && !this.world.isClientSide) {
         --this.g;
         if(!this.n()) {
            this.d(0);
            this.m();
         }

      }
   }

   public boolean m() {
      if(this.world != null && !this.world.isClientSide) {
         if(!this.n() && BlockHopper.f(this.u())) {
            boolean var1 = false;
            if(!this.p()) {
               var1 = this.r();
            }

            if(!this.q()) {
               var1 = a((IHopper)this) || var1;
            }

            if(var1) {
               this.d(8);
               this.update();
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean p() {
      ItemStack[] var1 = this.items;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemStack var4 = var1[var3];
         if(var4 != null) {
            return false;
         }
      }

      return true;
   }

   private boolean q() {
      ItemStack[] var1 = this.items;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemStack var4 = var1[var3];
         if(var4 == null || var4.count != var4.getMaxStackSize()) {
            return false;
         }
      }

      return true;
   }

   private boolean r() {
      IInventory var1 = this.H();
      if(var1 == null) {
         return false;
      } else {
         EnumDirection var2 = BlockHopper.b(this.u()).opposite();
         if(this.a(var1, var2)) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.getSize(); ++var3) {
               if(this.getItem(var3) != null) {
                  ItemStack var4 = this.getItem(var3).cloneItemStack();
                  ItemStack var5 = addItem(var1, this.splitStack(var3, 1), var2);
                  if(var5 == null || var5.count == 0) {
                     var1.update();
                     return true;
                  }

                  this.setItem(var3, var4);
               }
            }

            return false;
         }
      }
   }

   private boolean a(IInventory var1, EnumDirection var2) {
      if(var1 instanceof IWorldInventory) {
         IWorldInventory var7 = (IWorldInventory)var1;
         int[] var8 = var7.getSlotsForFace(var2);

         for(int var9 = 0; var9 < var8.length; ++var9) {
            ItemStack var6 = var7.getItem(var8[var9]);
            if(var6 == null || var6.count != var6.getMaxStackSize()) {
               return false;
            }
         }
      } else {
         int var3 = var1.getSize();

         for(int var4 = 0; var4 < var3; ++var4) {
            ItemStack var5 = var1.getItem(var4);
            if(var5 == null || var5.count != var5.getMaxStackSize()) {
               return false;
            }
         }
      }

      return true;
   }

   private static boolean b(IInventory var0, EnumDirection var1) {
      if(var0 instanceof IWorldInventory) {
         IWorldInventory var2 = (IWorldInventory)var0;
         int[] var3 = var2.getSlotsForFace(var1);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if(var2.getItem(var3[var4]) != null) {
               return false;
            }
         }
      } else {
         int var5 = var0.getSize();

         for(int var6 = 0; var6 < var5; ++var6) {
            if(var0.getItem(var6) != null) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean a(IHopper var0) {
      IInventory var1 = b(var0);
      if(var1 != null) {
         EnumDirection var2 = EnumDirection.DOWN;
         if(b(var1, var2)) {
            return false;
         }

         if(var1 instanceof IWorldInventory) {
            IWorldInventory var3 = (IWorldInventory)var1;
            int[] var4 = var3.getSlotsForFace(var2);

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if(a(var0, var1, var4[var5], var2)) {
                  return true;
               }
            }
         } else {
            int var7 = var1.getSize();

            for(int var9 = 0; var9 < var7; ++var9) {
               if(a(var0, var1, var9, var2)) {
                  return true;
               }
            }
         }
      } else {
         Iterator var6 = a(var0.getWorld(), var0.A(), var0.B() + 1.0D, var0.C()).iterator();

         while(var6.hasNext()) {
            EntityItem var8 = (EntityItem)var6.next();
            if(a((IInventory)var0, (EntityItem)var8)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean a(IHopper var0, IInventory var1, int var2, EnumDirection var3) {
      ItemStack var4 = var1.getItem(var2);
      if(var4 != null && b(var1, var4, var2, var3)) {
         ItemStack var5 = var4.cloneItemStack();
         ItemStack var6 = addItem(var0, var1.splitStack(var2, 1), (EnumDirection)null);
         if(var6 == null || var6.count == 0) {
            var1.update();
            return true;
         }

         var1.setItem(var2, var5);
      }

      return false;
   }

   public static boolean a(IInventory var0, EntityItem var1) {
      boolean var2 = false;
      if(var1 == null) {
         return false;
      } else {
         ItemStack var3 = var1.getItemStack().cloneItemStack();
         ItemStack var4 = addItem(var0, var3, (EnumDirection)null);
         if(var4 != null && var4.count != 0) {
            var1.setItemStack(var4);
         } else {
            var2 = true;
            var1.die();
         }

         return var2;
      }
   }

   public static ItemStack addItem(IInventory var0, ItemStack var1, EnumDirection var2) {
      if(var0 instanceof IWorldInventory && var2 != null) {
         IWorldInventory var6 = (IWorldInventory)var0;
         int[] var7 = var6.getSlotsForFace(var2);

         for(int var5 = 0; var5 < var7.length && var1 != null && var1.count > 0; ++var5) {
            var1 = c(var0, var1, var7[var5], var2);
         }
      } else {
         int var3 = var0.getSize();

         for(int var4 = 0; var4 < var3 && var1 != null && var1.count > 0; ++var4) {
            var1 = c(var0, var1, var4, var2);
         }
      }

      if(var1 != null && var1.count == 0) {
         var1 = null;
      }

      return var1;
   }

   private static boolean a(IInventory var0, ItemStack var1, int var2, EnumDirection var3) {
      return !var0.b(var2, var1)?false:!(var0 instanceof IWorldInventory) || ((IWorldInventory)var0).canPlaceItemThroughFace(var2, var1, var3);
   }

   private static boolean b(IInventory var0, ItemStack var1, int var2, EnumDirection var3) {
      return !(var0 instanceof IWorldInventory) || ((IWorldInventory)var0).canTakeItemThroughFace(var2, var1, var3);
   }

   private static ItemStack c(IInventory var0, ItemStack var1, int var2, EnumDirection var3) {
      ItemStack var4 = var0.getItem(var2);
      if(a(var0, var1, var2, var3)) {
         boolean var5 = false;
         if(var4 == null) {
            var0.setItem(var2, var1);
            var1 = null;
            var5 = true;
         } else if(a(var4, var1)) {
            int var6 = var1.getMaxStackSize() - var4.count;
            int var7 = Math.min(var1.count, var6);
            var1.count -= var7;
            var4.count += var7;
            var5 = var7 > 0;
         }

         if(var5) {
            if(var0 instanceof TileEntityHopper) {
               TileEntityHopper var8 = (TileEntityHopper)var0;
               if(var8.o()) {
                  var8.d(8);
               }

               var0.update();
            }

            var0.update();
         }
      }

      return var1;
   }

   private IInventory H() {
      EnumDirection var1 = BlockHopper.b(this.u());
      return b(this.getWorld(), (double)(this.position.getX() + var1.getAdjacentX()), (double)(this.position.getY() + var1.getAdjacentY()), (double)(this.position.getZ() + var1.getAdjacentZ()));
   }

   public static IInventory b(IHopper var0) {
      return b(var0.getWorld(), var0.A(), var0.B() + 1.0D, var0.C());
   }

   public static List<EntityItem> a(World var0, double var1, double var3, double var5) {
      return var0.a(EntityItem.class, new AxisAlignedBB(var1 - 0.5D, var3 - 0.5D, var5 - 0.5D, var1 + 0.5D, var3 + 0.5D, var5 + 0.5D), IEntitySelector.a);
   }

   public static IInventory b(World var0, double var1, double var3, double var5) {
      Object var7 = null;
      int var8 = MathHelper.floor(var1);
      int var9 = MathHelper.floor(var3);
      int var10 = MathHelper.floor(var5);
      BlockPosition var11 = new BlockPosition(var8, var9, var10);
      Block var12 = var0.getType(var11).getBlock();
      if(var12.isTileEntity()) {
         TileEntity var13 = var0.getTileEntity(var11);
         if(var13 instanceof IInventory) {
            var7 = (IInventory)var13;
            if(var7 instanceof TileEntityChest && var12 instanceof BlockChest) {
               var7 = ((BlockChest)var12).f(var0, var11);
            }
         }
      }

      if(var7 == null) {
         List var14 = var0.a((Entity)null, (AxisAlignedBB)(new AxisAlignedBB(var1 - 0.5D, var3 - 0.5D, var5 - 0.5D, var1 + 0.5D, var3 + 0.5D, var5 + 0.5D)), (Predicate)IEntitySelector.c);
         if(var14.size() > 0) {
            var7 = (IInventory)var14.get(var0.random.nextInt(var14.size()));
         }
      }

      return (IInventory)var7;
   }

   private static boolean a(ItemStack var0, ItemStack var1) {
      return var0.getItem() != var1.getItem()?false:(var0.getData() != var1.getData()?false:(var0.count > var0.getMaxStackSize()?false:ItemStack.equals(var0, var1)));
   }

   public double A() {
      return (double)this.position.getX() + 0.5D;
   }

   public double B() {
      return (double)this.position.getY() + 0.5D;
   }

   public double C() {
      return (double)this.position.getZ() + 0.5D;
   }

   public void d(int var1) {
      this.g = var1;
   }

   public boolean n() {
      return this.g > 0;
   }

   public boolean o() {
      return this.g <= 1;
   }

   public String getContainerName() {
      return "minecraft:hopper";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerHopper(var1, this, var2);
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
