package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockChest;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IInventory;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.InventoryLargeChest;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityContainer;

public class TileEntityChest extends TileEntityContainer implements IUpdatePlayerListBox, IInventory {
   private ItemStack[] items = new ItemStack[27];
   public boolean a;
   public TileEntityChest f;
   public TileEntityChest g;
   public TileEntityChest h;
   public TileEntityChest i;
   public float j;
   public float k;
   public int l;
   private int n;
   private int o = -1;
   private String p;

   public TileEntityChest() {
   }

   public int getSize() {
      return 27;
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

   public void setItem(int var1, ItemStack var2) {
      this.items[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      this.update();
   }

   public String getName() {
      return this.hasCustomName()?this.p:"container.chest";
   }

   public boolean hasCustomName() {
      return this.p != null && this.p.length() > 0;
   }

   public void a(String var1) {
      this.p = var1;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.getList("Items", 10);
      this.items = new ItemStack[this.getSize()];
      if(var1.hasKeyOfType("CustomName", 8)) {
         this.p = var1.getString("CustomName");
      }

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         int var5 = var4.getByte("Slot") & 255;
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
      if(this.hasCustomName()) {
         var1.setString("CustomName", this.p);
      }

   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.position) != this?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public void E() {
      super.E();
      this.a = false;
   }

   private void a(TileEntityChest var1, EnumDirection var2) {
      if(var1.x()) {
         this.a = false;
      } else if(this.a) {
         switch(TileEntityChest.SyntheticClass_1.a[var2.ordinal()]) {
         case 1:
            if(this.f != var1) {
               this.a = false;
            }
            break;
         case 2:
            if(this.i != var1) {
               this.a = false;
            }
            break;
         case 3:
            if(this.g != var1) {
               this.a = false;
            }
            break;
         case 4:
            if(this.h != var1) {
               this.a = false;
            }
         }
      }

   }

   public void m() {
      if(!this.a) {
         this.a = true;
         this.h = this.a(EnumDirection.WEST);
         this.g = this.a(EnumDirection.EAST);
         this.f = this.a(EnumDirection.NORTH);
         this.i = this.a(EnumDirection.SOUTH);
      }
   }

   protected TileEntityChest a(EnumDirection var1) {
      BlockPosition var2 = this.position.shift(var1);
      if(this.b(var2)) {
         TileEntity var3 = this.world.getTileEntity(var2);
         if(var3 instanceof TileEntityChest) {
            TileEntityChest var4 = (TileEntityChest)var3;
            var4.a(this, var1.opposite());
            return var4;
         }
      }

      return null;
   }

   private boolean b(BlockPosition var1) {
      if(this.world == null) {
         return false;
      } else {
         Block var2 = this.world.getType(var1).getBlock();
         return var2 instanceof BlockChest && ((BlockChest)var2).b == this.n();
      }
   }

   public void c() {
      this.m();
      int var1 = this.position.getX();
      int var2 = this.position.getY();
      int var3 = this.position.getZ();
      ++this.n;
      float var4;
      if(!this.world.isClientSide && this.l != 0 && (this.n + var1 + var2 + var3) % 200 == 0) {
         this.l = 0;
         var4 = 5.0F;
         List var5 = this.world.a(EntityHuman.class, new AxisAlignedBB((double)((float)var1 - var4), (double)((float)var2 - var4), (double)((float)var3 - var4), (double)((float)(var1 + 1) + var4), (double)((float)(var2 + 1) + var4), (double)((float)(var3 + 1) + var4)));
         Iterator var6 = var5.iterator();

         label93:
         while(true) {
            IInventory var8;
            do {
               EntityHuman var7;
               do {
                  if(!var6.hasNext()) {
                     break label93;
                  }

                  var7 = (EntityHuman)var6.next();
               } while(!(var7.activeContainer instanceof ContainerChest));

               var8 = ((ContainerChest)var7.activeContainer).e();
            } while(var8 != this && (!(var8 instanceof InventoryLargeChest) || !((InventoryLargeChest)var8).a((IInventory)this)));

            ++this.l;
         }
      }

      this.k = this.j;
      var4 = 0.1F;
      double var14;
      if(this.l > 0 && this.j == 0.0F && this.f == null && this.h == null) {
         double var11 = (double)var1 + 0.5D;
         var14 = (double)var3 + 0.5D;
         if(this.i != null) {
            var14 += 0.5D;
         }

         if(this.g != null) {
            var11 += 0.5D;
         }

         this.world.makeSound(var11, (double)var2 + 0.5D, var14, "random.chestopen", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }

      if(this.l == 0 && this.j > 0.0F || this.l > 0 && this.j < 1.0F) {
         float var12 = this.j;
         if(this.l > 0) {
            this.j += var4;
         } else {
            this.j -= var4;
         }

         if(this.j > 1.0F) {
            this.j = 1.0F;
         }

         float var13 = 0.5F;
         if(this.j < var13 && var12 >= var13 && this.f == null && this.h == null) {
            var14 = (double)var1 + 0.5D;
            double var9 = (double)var3 + 0.5D;
            if(this.i != null) {
               var9 += 0.5D;
            }

            if(this.g != null) {
               var14 += 0.5D;
            }

            this.world.makeSound(var14, (double)var2 + 0.5D, var9, "random.chestclosed", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
         }

         if(this.j < 0.0F) {
            this.j = 0.0F;
         }
      }

   }

   public boolean c(int var1, int var2) {
      if(var1 == 1) {
         this.l = var2;
         return true;
      } else {
         return super.c(var1, var2);
      }
   }

   public void startOpen(EntityHuman var1) {
      if(!var1.isSpectator()) {
         if(this.l < 0) {
            this.l = 0;
         }

         ++this.l;
         this.world.playBlockAction(this.position, this.w(), 1, this.l);
         this.world.applyPhysics(this.position, this.w());
         this.world.applyPhysics(this.position.down(), this.w());
      }

   }

   public void closeContainer(EntityHuman var1) {
      if(!var1.isSpectator() && this.w() instanceof BlockChest) {
         --this.l;
         this.world.playBlockAction(this.position, this.w(), 1, this.l);
         this.world.applyPhysics(this.position, this.w());
         this.world.applyPhysics(this.position.down(), this.w());
      }

   }

   public boolean b(int var1, ItemStack var2) {
      return true;
   }

   public void y() {
      super.y();
      this.E();
      this.m();
   }

   public int n() {
      if(this.o == -1) {
         if(this.world == null || !(this.w() instanceof BlockChest)) {
            return 0;
         }

         this.o = ((BlockChest)this.w()).b;
      }

      return this.o;
   }

   public String getContainerName() {
      return "minecraft:chest";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerChest(var1, this, var2);
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

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
