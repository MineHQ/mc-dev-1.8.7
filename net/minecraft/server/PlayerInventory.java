package net.minecraft.server;

import java.util.concurrent.Callable;
import net.minecraft.server.Block;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.ReportedException;

public class PlayerInventory implements IInventory {
   public ItemStack[] items = new ItemStack[36];
   public ItemStack[] armor = new ItemStack[4];
   public int itemInHandIndex;
   public EntityHuman player;
   private ItemStack f;
   public boolean e;

   public PlayerInventory(EntityHuman var1) {
      this.player = var1;
   }

   public ItemStack getItemInHand() {
      return this.itemInHandIndex < 9 && this.itemInHandIndex >= 0?this.items[this.itemInHandIndex]:null;
   }

   public static int getHotbarSize() {
      return 9;
   }

   private int c(Item var1) {
      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] != null && this.items[var2].getItem() == var1) {
            return var2;
         }
      }

      return -1;
   }

   private int firstPartial(ItemStack var1) {
      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] != null && this.items[var2].getItem() == var1.getItem() && this.items[var2].isStackable() && this.items[var2].count < this.items[var2].getMaxStackSize() && this.items[var2].count < this.getMaxStackSize() && (!this.items[var2].usesData() || this.items[var2].getData() == var1.getData()) && ItemStack.equals(this.items[var2], var1)) {
            return var2;
         }
      }

      return -1;
   }

   public int getFirstEmptySlotIndex() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         if(this.items[var1] == null) {
            return var1;
         }
      }

      return -1;
   }

   public int a(Item var1, int var2, int var3, NBTTagCompound var4) {
      int var5 = 0;

      int var6;
      ItemStack var7;
      int var8;
      for(var6 = 0; var6 < this.items.length; ++var6) {
         var7 = this.items[var6];
         if(var7 != null && (var1 == null || var7.getItem() == var1) && (var2 <= -1 || var7.getData() == var2) && (var4 == null || GameProfileSerializer.a(var4, var7.getTag(), true))) {
            var8 = var3 <= 0?var7.count:Math.min(var3 - var5, var7.count);
            var5 += var8;
            if(var3 != 0) {
               this.items[var6].count -= var8;
               if(this.items[var6].count == 0) {
                  this.items[var6] = null;
               }

               if(var3 > 0 && var5 >= var3) {
                  return var5;
               }
            }
         }
      }

      for(var6 = 0; var6 < this.armor.length; ++var6) {
         var7 = this.armor[var6];
         if(var7 != null && (var1 == null || var7.getItem() == var1) && (var2 <= -1 || var7.getData() == var2) && (var4 == null || GameProfileSerializer.a(var4, var7.getTag(), false))) {
            var8 = var3 <= 0?var7.count:Math.min(var3 - var5, var7.count);
            var5 += var8;
            if(var3 != 0) {
               this.armor[var6].count -= var8;
               if(this.armor[var6].count == 0) {
                  this.armor[var6] = null;
               }

               if(var3 > 0 && var5 >= var3) {
                  return var5;
               }
            }
         }
      }

      if(this.f != null) {
         if(var1 != null && this.f.getItem() != var1) {
            return var5;
         }

         if(var2 > -1 && this.f.getData() != var2) {
            return var5;
         }

         if(var4 != null && !GameProfileSerializer.a(var4, this.f.getTag(), false)) {
            return var5;
         }

         var6 = var3 <= 0?this.f.count:Math.min(var3 - var5, this.f.count);
         var5 += var6;
         if(var3 != 0) {
            this.f.count -= var6;
            if(this.f.count == 0) {
               this.f = null;
            }

            if(var3 > 0 && var5 >= var3) {
               return var5;
            }
         }
      }

      return var5;
   }

   private int e(ItemStack var1) {
      Item var2 = var1.getItem();
      int var3 = var1.count;
      int var4 = this.firstPartial(var1);
      if(var4 < 0) {
         var4 = this.getFirstEmptySlotIndex();
      }

      if(var4 < 0) {
         return var3;
      } else {
         if(this.items[var4] == null) {
            this.items[var4] = new ItemStack(var2, 0, var1.getData());
            if(var1.hasTag()) {
               this.items[var4].setTag((NBTTagCompound)var1.getTag().clone());
            }
         }

         int var5 = var3;
         if(var3 > this.items[var4].getMaxStackSize() - this.items[var4].count) {
            var5 = this.items[var4].getMaxStackSize() - this.items[var4].count;
         }

         if(var5 > this.getMaxStackSize() - this.items[var4].count) {
            var5 = this.getMaxStackSize() - this.items[var4].count;
         }

         if(var5 == 0) {
            return var3;
         } else {
            var3 -= var5;
            this.items[var4].count += var5;
            this.items[var4].c = 5;
            return var3;
         }
      }
   }

   public void k() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         if(this.items[var1] != null) {
            this.items[var1].a(this.player.world, this.player, var1, this.itemInHandIndex == var1);
         }
      }

   }

   public boolean a(Item var1) {
      int var2 = this.c(var1);
      if(var2 < 0) {
         return false;
      } else {
         if(--this.items[var2].count <= 0) {
            this.items[var2] = null;
         }

         return true;
      }
   }

   public boolean b(Item var1) {
      int var2 = this.c(var1);
      return var2 >= 0;
   }

   public boolean pickup(final ItemStack var1) {
      if(var1 != null && var1.count != 0 && var1.getItem() != null) {
         try {
            int var2;
            if(var1.g()) {
               var2 = this.getFirstEmptySlotIndex();
               if(var2 >= 0) {
                  this.items[var2] = ItemStack.b(var1);
                  this.items[var2].c = 5;
                  var1.count = 0;
                  return true;
               } else if(this.player.abilities.canInstantlyBuild) {
                  var1.count = 0;
                  return true;
               } else {
                  return false;
               }
            } else {
               do {
                  var2 = var1.count;
                  var1.count = this.e(var1);
               } while(var1.count > 0 && var1.count < var2);

               if(var1.count == var2 && this.player.abilities.canInstantlyBuild) {
                  var1.count = 0;
                  return true;
               } else {
                  return var1.count < var2;
               }
            }
         } catch (Throwable var5) {
            CrashReport var3 = CrashReport.a(var5, "Adding item to inventory");
            CrashReportSystemDetails var4 = var3.a("Item being added");
            var4.a((String)"Item ID", (Object)Integer.valueOf(Item.getId(var1.getItem())));
            var4.a((String)"Item data", (Object)Integer.valueOf(var1.getData()));
            var4.a("Item name", new Callable() {
               public String a() throws Exception {
                  return var1.getName();
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            throw new ReportedException(var3);
         }
      } else {
         return false;
      }
   }

   public ItemStack splitStack(int var1, int var2) {
      ItemStack[] var3 = this.items;
      if(var1 >= this.items.length) {
         var3 = this.armor;
         var1 -= this.items.length;
      }

      if(var3[var1] != null) {
         ItemStack var4;
         if(var3[var1].count <= var2) {
            var4 = var3[var1];
            var3[var1] = null;
            return var4;
         } else {
            var4 = var3[var1].a(var2);
            if(var3[var1].count == 0) {
               var3[var1] = null;
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      ItemStack[] var2 = this.items;
      if(var1 >= this.items.length) {
         var2 = this.armor;
         var1 -= this.items.length;
      }

      if(var2[var1] != null) {
         ItemStack var3 = var2[var1];
         var2[var1] = null;
         return var3;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      ItemStack[] var3 = this.items;
      if(var1 >= var3.length) {
         var1 -= var3.length;
         var3 = this.armor;
      }

      var3[var1] = var2;
   }

   public float a(Block var1) {
      float var2 = 1.0F;
      if(this.items[this.itemInHandIndex] != null) {
         var2 *= this.items[this.itemInHandIndex].a(var1);
      }

      return var2;
   }

   public NBTTagList a(NBTTagList var1) {
      int var2;
      NBTTagCompound var3;
      for(var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] != null) {
            var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)var2);
            this.items[var2].save(var3);
            var1.add(var3);
         }
      }

      for(var2 = 0; var2 < this.armor.length; ++var2) {
         if(this.armor[var2] != null) {
            var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)(var2 + 100));
            this.armor[var2].save(var3);
            var1.add(var3);
         }
      }

      return var1;
   }

   public void b(NBTTagList var1) {
      this.items = new ItemStack[36];
      this.armor = new ItemStack[4];

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         NBTTagCompound var3 = var1.get(var2);
         int var4 = var3.getByte("Slot") & 255;
         ItemStack var5 = ItemStack.createStack(var3);
         if(var5 != null) {
            if(var4 >= 0 && var4 < this.items.length) {
               this.items[var4] = var5;
            }

            if(var4 >= 100 && var4 < this.armor.length + 100) {
               this.armor[var4 - 100] = var5;
            }
         }
      }

   }

   public int getSize() {
      return this.items.length + 4;
   }

   public ItemStack getItem(int var1) {
      ItemStack[] var2 = this.items;
      if(var1 >= var2.length) {
         var1 -= var2.length;
         var2 = this.armor;
      }

      return var2[var1];
   }

   public String getName() {
      return "container.inventory";
   }

   public boolean hasCustomName() {
      return false;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean b(Block var1) {
      if(var1.getMaterial().isAlwaysDestroyable()) {
         return true;
      } else {
         ItemStack var2 = this.getItem(this.itemInHandIndex);
         return var2 != null?var2.b(var1):false;
      }
   }

   public ItemStack e(int var1) {
      return this.armor[var1];
   }

   public int m() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.armor.length; ++var2) {
         if(this.armor[var2] != null && this.armor[var2].getItem() instanceof ItemArmor) {
            int var3 = ((ItemArmor)this.armor[var2].getItem()).c;
            var1 += var3;
         }
      }

      return var1;
   }

   public void a(float var1) {
      var1 /= 4.0F;
      if(var1 < 1.0F) {
         var1 = 1.0F;
      }

      for(int var2 = 0; var2 < this.armor.length; ++var2) {
         if(this.armor[var2] != null && this.armor[var2].getItem() instanceof ItemArmor) {
            this.armor[var2].damage((int)var1, this.player);
            if(this.armor[var2].count == 0) {
               this.armor[var2] = null;
            }
         }
      }

   }

   public void n() {
      int var1;
      for(var1 = 0; var1 < this.items.length; ++var1) {
         if(this.items[var1] != null) {
            this.player.a(this.items[var1], true, false);
            this.items[var1] = null;
         }
      }

      for(var1 = 0; var1 < this.armor.length; ++var1) {
         if(this.armor[var1] != null) {
            this.player.a(this.armor[var1], true, false);
            this.armor[var1] = null;
         }
      }

   }

   public void update() {
      this.e = true;
   }

   public void setCarried(ItemStack var1) {
      this.f = var1;
   }

   public ItemStack getCarried() {
      return this.f;
   }

   public boolean a(EntityHuman var1) {
      return this.player.dead?false:var1.h(this.player) <= 64.0D;
   }

   public boolean c(ItemStack var1) {
      int var2;
      for(var2 = 0; var2 < this.armor.length; ++var2) {
         if(this.armor[var2] != null && this.armor[var2].doMaterialsMatch(var1)) {
            return true;
         }
      }

      for(var2 = 0; var2 < this.items.length; ++var2) {
         if(this.items[var2] != null && this.items[var2].doMaterialsMatch(var1)) {
            return true;
         }
      }

      return false;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return true;
   }

   public void b(PlayerInventory var1) {
      int var2;
      for(var2 = 0; var2 < this.items.length; ++var2) {
         this.items[var2] = ItemStack.b(var1.items[var2]);
      }

      for(var2 = 0; var2 < this.armor.length; ++var2) {
         this.armor[var2] = ItemStack.b(var1.armor[var2]);
      }

      this.itemInHandIndex = var1.itemInHandIndex;
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
      int var1;
      for(var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

      for(var1 = 0; var1 < this.armor.length; ++var1) {
         this.armor[var1] = null;
      }

   }
}
