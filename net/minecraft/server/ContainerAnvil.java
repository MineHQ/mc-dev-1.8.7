package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.BlockAnvil;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventoryCraftResult;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.World;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerAnvil extends Container {
   private static final Logger f = LogManager.getLogger();
   private IInventory g = new InventoryCraftResult();
   private IInventory h = new InventorySubcontainer("Repair", true, 2) {
      public void update() {
         super.update();
         ContainerAnvil.this.a((IInventory)this);
      }
   };
   private World i;
   private BlockPosition j;
   public int a;
   private int k;
   private String l;
   private final EntityHuman m;

   public ContainerAnvil(PlayerInventory var1, final World var2, final BlockPosition var3, EntityHuman var4) {
      this.j = var3;
      this.i = var2;
      this.m = var4;
      this.a((Slot)(new Slot(this.h, 0, 27, 47)));
      this.a((Slot)(new Slot(this.h, 1, 76, 47)));
      this.a((Slot)(new Slot(this.g, 2, 134, 47) {
         public boolean isAllowed(ItemStack var1) {
            return false;
         }

         public boolean isAllowed(EntityHuman var1) {
            return (var1.abilities.canInstantlyBuild || var1.expLevel >= ContainerAnvil.this.a) && ContainerAnvil.this.a > 0 && this.hasItem();
         }

         public void a(EntityHuman var1, ItemStack var2x) {
            if(!var1.abilities.canInstantlyBuild) {
               var1.levelDown(-ContainerAnvil.this.a);
            }

            ContainerAnvil.this.h.setItem(0, (ItemStack)null);
            if(ContainerAnvil.this.k > 0) {
               ItemStack var3x = ContainerAnvil.this.h.getItem(1);
               if(var3x != null && var3x.count > ContainerAnvil.this.k) {
                  var3x.count -= ContainerAnvil.this.k;
                  ContainerAnvil.this.h.setItem(1, var3x);
               } else {
                  ContainerAnvil.this.h.setItem(1, (ItemStack)null);
               }
            } else {
               ContainerAnvil.this.h.setItem(1, (ItemStack)null);
            }

            ContainerAnvil.this.a = 0;
            IBlockData var5 = var2.getType(var3);
            if(!var1.abilities.canInstantlyBuild && !var2.isClientSide && var5.getBlock() == Blocks.ANVIL && var1.bc().nextFloat() < 0.12F) {
               int var4 = ((Integer)var5.get(BlockAnvil.DAMAGE)).intValue();
               ++var4;
               if(var4 > 2) {
                  var2.setAir(var3);
                  var2.triggerEffect(1020, var3, 0);
               } else {
                  var2.setTypeAndData(var3, var5.set(BlockAnvil.DAMAGE, Integer.valueOf(var4)), 2);
                  var2.triggerEffect(1021, var3, 0);
               }
            } else if(!var2.isClientSide) {
               var2.triggerEffect(1021, var3, 0);
            }

         }
      }));

      int var5;
      for(var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 9; ++var6) {
            this.a((Slot)(new Slot(var1, var6 + var5 * 9 + 9, 8 + var6 * 18, 84 + var5 * 18)));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.a((Slot)(new Slot(var1, var5, 8 + var5 * 18, 142)));
      }

   }

   public void a(IInventory var1) {
      super.a(var1);
      if(var1 == this.h) {
         this.e();
      }

   }

   public void e() {
      boolean var1 = false;
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = true;
      boolean var5 = true;
      boolean var6 = true;
      boolean var7 = true;
      ItemStack var8 = this.h.getItem(0);
      this.a = 1;
      int var9 = 0;
      byte var10 = 0;
      byte var11 = 0;
      if(var8 == null) {
         this.g.setItem(0, (ItemStack)null);
         this.a = 0;
      } else {
         ItemStack var12 = var8.cloneItemStack();
         ItemStack var13 = this.h.getItem(1);
         Map var14 = EnchantmentManager.a(var12);
         boolean var15 = false;
         int var25 = var10 + var8.getRepairCost() + (var13 == null?0:var13.getRepairCost());
         this.k = 0;
         int var16;
         if(var13 != null) {
            var15 = var13.getItem() == Items.ENCHANTED_BOOK && Items.ENCHANTED_BOOK.h(var13).size() > 0;
            int var17;
            int var18;
            if(var12.e() && var12.getItem().a(var8, var13)) {
               var16 = Math.min(var12.h(), var12.j() / 4);
               if(var16 <= 0) {
                  this.g.setItem(0, (ItemStack)null);
                  this.a = 0;
                  return;
               }

               for(var17 = 0; var16 > 0 && var17 < var13.count; ++var17) {
                  var18 = var12.h() - var16;
                  var12.setData(var18);
                  ++var9;
                  var16 = Math.min(var12.h(), var12.j() / 4);
               }

               this.k = var17;
            } else {
               if(!var15 && (var12.getItem() != var13.getItem() || !var12.e())) {
                  this.g.setItem(0, (ItemStack)null);
                  this.a = 0;
                  return;
               }

               int var20;
               if(var12.e() && !var15) {
                  var16 = var8.j() - var8.h();
                  var17 = var13.j() - var13.h();
                  var18 = var17 + var12.j() * 12 / 100;
                  int var19 = var16 + var18;
                  var20 = var12.j() - var19;
                  if(var20 < 0) {
                     var20 = 0;
                  }

                  if(var20 < var12.getData()) {
                     var12.setData(var20);
                     var9 += 2;
                  }
               }

               Map var26 = EnchantmentManager.a(var13);
               Iterator var27 = var26.keySet().iterator();

               label144:
               while(true) {
                  Enchantment var28;
                  do {
                     if(!var27.hasNext()) {
                        break label144;
                     }

                     var18 = ((Integer)var27.next()).intValue();
                     var28 = Enchantment.getById(var18);
                  } while(var28 == null);

                  var20 = var14.containsKey(Integer.valueOf(var18))?((Integer)var14.get(Integer.valueOf(var18))).intValue():0;
                  int var21 = ((Integer)var26.get(Integer.valueOf(var18))).intValue();
                  int var10000;
                  if(var20 == var21) {
                     ++var21;
                     var10000 = var21;
                  } else {
                     var10000 = Math.max(var21, var20);
                  }

                  var21 = var10000;
                  boolean var22 = var28.canEnchant(var8);
                  if(this.m.abilities.canInstantlyBuild || var8.getItem() == Items.ENCHANTED_BOOK) {
                     var22 = true;
                  }

                  Iterator var23 = var14.keySet().iterator();

                  while(var23.hasNext()) {
                     int var24 = ((Integer)var23.next()).intValue();
                     if(var24 != var18 && !var28.a(Enchantment.getById(var24))) {
                        var22 = false;
                        ++var9;
                     }
                  }

                  if(var22) {
                     if(var21 > var28.getMaxLevel()) {
                        var21 = var28.getMaxLevel();
                     }

                     var14.put(Integer.valueOf(var18), Integer.valueOf(var21));
                     int var29 = 0;
                     switch(var28.getRandomWeight()) {
                     case 1:
                        var29 = 8;
                        break;
                     case 2:
                        var29 = 4;
                     case 3:
                     case 4:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     default:
                        break;
                     case 5:
                        var29 = 2;
                        break;
                     case 10:
                        var29 = 1;
                     }

                     if(var15) {
                        var29 = Math.max(1, var29 / 2);
                     }

                     var9 += var29 * var21;
                  }
               }
            }
         }

         if(StringUtils.isBlank(this.l)) {
            if(var8.hasName()) {
               var11 = 1;
               var9 += var11;
               var12.r();
            }
         } else if(!this.l.equals(var8.getName())) {
            var11 = 1;
            var9 += var11;
            var12.c(this.l);
         }

         this.a = var25 + var9;
         if(var9 <= 0) {
            var12 = null;
         }

         if(var11 == var9 && var11 > 0 && this.a >= 40) {
            this.a = 39;
         }

         if(this.a >= 40 && !this.m.abilities.canInstantlyBuild) {
            var12 = null;
         }

         if(var12 != null) {
            var16 = var12.getRepairCost();
            if(var13 != null && var16 < var13.getRepairCost()) {
               var16 = var13.getRepairCost();
            }

            var16 = var16 * 2 + 1;
            var12.setRepairCost(var16);
            EnchantmentManager.a(var14, var12);
         }

         this.g.setItem(0, var12);
         this.b();
      }
   }

   public void addSlotListener(ICrafting var1) {
      super.addSlotListener(var1);
      var1.setContainerData(this, 0, this.a);
   }

   public void b(EntityHuman var1) {
      super.b(var1);
      if(!this.i.isClientSide) {
         for(int var2 = 0; var2 < this.h.getSize(); ++var2) {
            ItemStack var3 = this.h.splitWithoutUpdate(var2);
            if(var3 != null) {
               var1.drop(var3, false);
            }
         }

      }
   }

   public boolean a(EntityHuman var1) {
      return this.i.getType(this.j).getBlock() != Blocks.ANVIL?false:var1.e((double)this.j.getX() + 0.5D, (double)this.j.getY() + 0.5D, (double)this.j.getZ() + 0.5D) <= 64.0D;
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if(var2 == 2) {
            if(!this.a(var5, 3, 39, true)) {
               return null;
            }

            var4.a(var5, var3);
         } else if(var2 != 0 && var2 != 1) {
            if(var2 >= 3 && var2 < 39 && !this.a(var5, 0, 2, false)) {
               return null;
            }
         } else if(!this.a(var5, 3, 39, false)) {
            return null;
         }

         if(var5.count == 0) {
            var4.set((ItemStack)null);
         } else {
            var4.f();
         }

         if(var5.count == var3.count) {
            return null;
         }

         var4.a(var1, var5);
      }

      return var3;
   }

   public void a(String var1) {
      this.l = var1;
      if(this.getSlot(2).hasItem()) {
         ItemStack var2 = this.getSlot(2).getItem();
         if(StringUtils.isBlank(var1)) {
            var2.r();
         } else {
            var2.c(this.l);
         }
      }

      this.e();
   }
}
