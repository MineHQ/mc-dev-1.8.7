package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.TileEntity;

public abstract class Container {
   public List<ItemStack> b = Lists.newArrayList();
   public List<Slot> c = Lists.newArrayList();
   public int windowId;
   private int dragType = -1;
   private int g;
   private final Set<Slot> h = Sets.newHashSet();
   protected List<ICrafting> listeners = Lists.newArrayList();
   private Set<EntityHuman> i = Sets.newHashSet();

   public Container() {
   }

   protected Slot a(Slot var1) {
      var1.rawSlotIndex = this.c.size();
      this.c.add(var1);
      this.b.add((Object)null);
      return var1;
   }

   public void addSlotListener(ICrafting var1) {
      if(this.listeners.contains(var1)) {
         throw new IllegalArgumentException("Listener already listening");
      } else {
         this.listeners.add(var1);
         var1.a(this, this.a());
         this.b();
      }
   }

   public List<ItemStack> a() {
      ArrayList var1 = Lists.newArrayList();

      for(int var2 = 0; var2 < this.c.size(); ++var2) {
         var1.add(((Slot)this.c.get(var2)).getItem());
      }

      return var1;
   }

   public void b() {
      for(int var1 = 0; var1 < this.c.size(); ++var1) {
         ItemStack var2 = ((Slot)this.c.get(var1)).getItem();
         ItemStack var3 = (ItemStack)this.b.get(var1);
         if(!ItemStack.matches(var3, var2)) {
            var3 = var2 == null?null:var2.cloneItemStack();
            this.b.set(var1, var3);

            for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
               ((ICrafting)this.listeners.get(var4)).a(this, var1, var3);
            }
         }
      }

   }

   public boolean a(EntityHuman var1, int var2) {
      return false;
   }

   public Slot getSlot(IInventory var1, int var2) {
      for(int var3 = 0; var3 < this.c.size(); ++var3) {
         Slot var4 = (Slot)this.c.get(var3);
         if(var4.a(var1, var2)) {
            return var4;
         }
      }

      return null;
   }

   public Slot getSlot(int var1) {
      return (Slot)this.c.get(var1);
   }

   public ItemStack b(EntityHuman var1, int var2) {
      Slot var3 = (Slot)this.c.get(var2);
      return var3 != null?var3.getItem():null;
   }

   public ItemStack clickItem(int var1, int var2, int var3, EntityHuman var4) {
      ItemStack var5 = null;
      PlayerInventory var6 = var4.inventory;
      int var9;
      ItemStack var17;
      if(var3 == 5) {
         int var7 = this.g;
         this.g = c(var2);
         if((var7 != 1 || this.g != 2) && var7 != this.g) {
            this.d();
         } else if(var6.getCarried() == null) {
            this.d();
         } else if(this.g == 0) {
            this.dragType = b(var2);
            if(a(this.dragType, var4)) {
               this.g = 1;
               this.h.clear();
            } else {
               this.d();
            }
         } else if(this.g == 1) {
            Slot var8 = (Slot)this.c.get(var1);
            if(var8 != null && a(var8, var6.getCarried(), true) && var8.isAllowed(var6.getCarried()) && var6.getCarried().count > this.h.size() && this.b(var8)) {
               this.h.add(var8);
            }
         } else if(this.g == 2) {
            if(!this.h.isEmpty()) {
               var17 = var6.getCarried().cloneItemStack();
               var9 = var6.getCarried().count;
               Iterator var10 = this.h.iterator();

               while(var10.hasNext()) {
                  Slot var11 = (Slot)var10.next();
                  if(var11 != null && a(var11, var6.getCarried(), true) && var11.isAllowed(var6.getCarried()) && var6.getCarried().count >= this.h.size() && this.b(var11)) {
                     ItemStack var12 = var17.cloneItemStack();
                     int var13 = var11.hasItem()?var11.getItem().count:0;
                     a(this.h, this.dragType, var12, var13);
                     if(var12.count > var12.getMaxStackSize()) {
                        var12.count = var12.getMaxStackSize();
                     }

                     if(var12.count > var11.getMaxStackSize(var12)) {
                        var12.count = var11.getMaxStackSize(var12);
                     }

                     var9 -= var12.count - var13;
                     var11.set(var12);
                  }
               }

               var17.count = var9;
               if(var17.count <= 0) {
                  var17 = null;
               }

               var6.setCarried(var17);
            }

            this.d();
         } else {
            this.d();
         }
      } else if(this.g != 0) {
         this.d();
      } else {
         Slot var16;
         int var19;
         ItemStack var23;
         if((var3 == 0 || var3 == 1) && (var2 == 0 || var2 == 1)) {
            if(var1 == -999) {
               if(var6.getCarried() != null) {
                  if(var2 == 0) {
                     var4.drop(var6.getCarried(), true);
                     var6.setCarried((ItemStack)null);
                  }

                  if(var2 == 1) {
                     var4.drop(var6.getCarried().a(1), true);
                     if(var6.getCarried().count == 0) {
                        var6.setCarried((ItemStack)null);
                     }
                  }
               }
            } else if(var3 == 1) {
               if(var1 < 0) {
                  return null;
               }

               var16 = (Slot)this.c.get(var1);
               if(var16 != null && var16.isAllowed(var4)) {
                  var17 = this.b(var4, var1);
                  if(var17 != null) {
                     Item var20 = var17.getItem();
                     var5 = var17.cloneItemStack();
                     if(var16.getItem() != null && var16.getItem().getItem() == var20) {
                        this.a(var1, var2, true, var4);
                     }
                  }
               }
            } else {
               if(var1 < 0) {
                  return null;
               }

               var16 = (Slot)this.c.get(var1);
               if(var16 != null) {
                  var17 = var16.getItem();
                  ItemStack var21 = var6.getCarried();
                  if(var17 != null) {
                     var5 = var17.cloneItemStack();
                  }

                  if(var17 == null) {
                     if(var21 != null && var16.isAllowed(var21)) {
                        var19 = var2 == 0?var21.count:1;
                        if(var19 > var16.getMaxStackSize(var21)) {
                           var19 = var16.getMaxStackSize(var21);
                        }

                        if(var21.count >= var19) {
                           var16.set(var21.a(var19));
                        }

                        if(var21.count == 0) {
                           var6.setCarried((ItemStack)null);
                        }
                     }
                  } else if(var16.isAllowed(var4)) {
                     if(var21 == null) {
                        var19 = var2 == 0?var17.count:(var17.count + 1) / 2;
                        var23 = var16.a(var19);
                        var6.setCarried(var23);
                        if(var17.count == 0) {
                           var16.set((ItemStack)null);
                        }

                        var16.a(var4, var6.getCarried());
                     } else if(var16.isAllowed(var21)) {
                        if(var17.getItem() == var21.getItem() && var17.getData() == var21.getData() && ItemStack.equals(var17, var21)) {
                           var19 = var2 == 0?var21.count:1;
                           if(var19 > var16.getMaxStackSize(var21) - var17.count) {
                              var19 = var16.getMaxStackSize(var21) - var17.count;
                           }

                           if(var19 > var21.getMaxStackSize() - var17.count) {
                              var19 = var21.getMaxStackSize() - var17.count;
                           }

                           var21.a(var19);
                           if(var21.count == 0) {
                              var6.setCarried((ItemStack)null);
                           }

                           var17.count += var19;
                        } else if(var21.count <= var16.getMaxStackSize(var21)) {
                           var16.set(var21);
                           var6.setCarried(var17);
                        }
                     } else if(var17.getItem() == var21.getItem() && var21.getMaxStackSize() > 1 && (!var17.usesData() || var17.getData() == var21.getData()) && ItemStack.equals(var17, var21)) {
                        var19 = var17.count;
                        if(var19 > 0 && var19 + var21.count <= var21.getMaxStackSize()) {
                           var21.count += var19;
                           var17 = var16.a(var19);
                           if(var17.count == 0) {
                              var16.set((ItemStack)null);
                           }

                           var16.a(var4, var6.getCarried());
                        }
                     }
                  }

                  var16.f();
               }
            }
         } else if(var3 == 2 && var2 >= 0 && var2 < 9) {
            var16 = (Slot)this.c.get(var1);
            if(var16.isAllowed(var4)) {
               var17 = var6.getItem(var2);
               boolean var18 = var17 == null || var16.inventory == var6 && var16.isAllowed(var17);
               var19 = -1;
               if(!var18) {
                  var19 = var6.getFirstEmptySlotIndex();
                  var18 |= var19 > -1;
               }

               if(var16.hasItem() && var18) {
                  var23 = var16.getItem();
                  var6.setItem(var2, var23.cloneItemStack());
                  if((var16.inventory != var6 || !var16.isAllowed(var17)) && var17 != null) {
                     if(var19 > -1) {
                        var6.pickup(var17);
                        var16.a(var23.count);
                        var16.set((ItemStack)null);
                        var16.a(var4, var23);
                     }
                  } else {
                     var16.a(var23.count);
                     var16.set(var17);
                     var16.a(var4, var23);
                  }
               } else if(!var16.hasItem() && var17 != null && var16.isAllowed(var17)) {
                  var6.setItem(var2, (ItemStack)null);
                  var16.set(var17);
               }
            }
         } else if(var3 == 3 && var4.abilities.canInstantlyBuild && var6.getCarried() == null && var1 >= 0) {
            var16 = (Slot)this.c.get(var1);
            if(var16 != null && var16.hasItem()) {
               var17 = var16.getItem().cloneItemStack();
               var17.count = var17.getMaxStackSize();
               var6.setCarried(var17);
            }
         } else if(var3 == 4 && var6.getCarried() == null && var1 >= 0) {
            var16 = (Slot)this.c.get(var1);
            if(var16 != null && var16.hasItem() && var16.isAllowed(var4)) {
               var17 = var16.a(var2 == 0?1:var16.getItem().count);
               var16.a(var4, var17);
               var4.drop(var17, true);
            }
         } else if(var3 == 6 && var1 >= 0) {
            var16 = (Slot)this.c.get(var1);
            var17 = var6.getCarried();
            if(var17 != null && (var16 == null || !var16.hasItem() || !var16.isAllowed(var4))) {
               var9 = var2 == 0?0:this.c.size() - 1;
               var19 = var2 == 0?1:-1;

               for(int var22 = 0; var22 < 2; ++var22) {
                  for(int var24 = var9; var24 >= 0 && var24 < this.c.size() && var17.count < var17.getMaxStackSize(); var24 += var19) {
                     Slot var25 = (Slot)this.c.get(var24);
                     if(var25.hasItem() && a(var25, var17, true) && var25.isAllowed(var4) && this.a(var17, var25) && (var22 != 0 || var25.getItem().count != var25.getItem().getMaxStackSize())) {
                        int var14 = Math.min(var17.getMaxStackSize() - var17.count, var25.getItem().count);
                        ItemStack var15 = var25.a(var14);
                        var17.count += var14;
                        if(var15.count <= 0) {
                           var25.set((ItemStack)null);
                        }

                        var25.a(var4, var15);
                     }
                  }
               }
            }

            this.b();
         }
      }

      return var5;
   }

   public boolean a(ItemStack var1, Slot var2) {
      return true;
   }

   protected void a(int var1, int var2, boolean var3, EntityHuman var4) {
      this.clickItem(var1, var2, 1, var4);
   }

   public void b(EntityHuman var1) {
      PlayerInventory var2 = var1.inventory;
      if(var2.getCarried() != null) {
         var1.drop(var2.getCarried(), false);
         var2.setCarried((ItemStack)null);
      }

   }

   public void a(IInventory var1) {
      this.b();
   }

   public void setItem(int var1, ItemStack var2) {
      this.getSlot(var1).set(var2);
   }

   public boolean c(EntityHuman var1) {
      return !this.i.contains(var1);
   }

   public void a(EntityHuman var1, boolean var2) {
      if(var2) {
         this.i.remove(var1);
      } else {
         this.i.add(var1);
      }

   }

   public abstract boolean a(EntityHuman var1);

   protected boolean a(ItemStack var1, int var2, int var3, boolean var4) {
      boolean var5 = false;
      int var6 = var2;
      if(var4) {
         var6 = var3 - 1;
      }

      Slot var7;
      ItemStack var8;
      if(var1.isStackable()) {
         while(var1.count > 0 && (!var4 && var6 < var3 || var4 && var6 >= var2)) {
            var7 = (Slot)this.c.get(var6);
            var8 = var7.getItem();
            if(var8 != null && var8.getItem() == var1.getItem() && (!var1.usesData() || var1.getData() == var8.getData()) && ItemStack.equals(var1, var8)) {
               int var9 = var8.count + var1.count;
               if(var9 <= var1.getMaxStackSize()) {
                  var1.count = 0;
                  var8.count = var9;
                  var7.f();
                  var5 = true;
               } else if(var8.count < var1.getMaxStackSize()) {
                  var1.count -= var1.getMaxStackSize() - var8.count;
                  var8.count = var1.getMaxStackSize();
                  var7.f();
                  var5 = true;
               }
            }

            if(var4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if(var1.count > 0) {
         if(var4) {
            var6 = var3 - 1;
         } else {
            var6 = var2;
         }

         while(!var4 && var6 < var3 || var4 && var6 >= var2) {
            var7 = (Slot)this.c.get(var6);
            var8 = var7.getItem();
            if(var8 == null) {
               var7.set(var1.cloneItemStack());
               var7.f();
               var1.count = 0;
               var5 = true;
               break;
            }

            if(var4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }

   public static int b(int var0) {
      return var0 >> 2 & 3;
   }

   public static int c(int var0) {
      return var0 & 3;
   }

   public static boolean a(int var0, EntityHuman var1) {
      return var0 == 0?true:(var0 == 1?true:var0 == 2 && var1.abilities.canInstantlyBuild);
   }

   protected void d() {
      this.g = 0;
      this.h.clear();
   }

   public static boolean a(Slot var0, ItemStack var1, boolean var2) {
      boolean var3 = var0 == null || !var0.hasItem();
      if(var0 != null && var0.hasItem() && var1 != null && var1.doMaterialsMatch(var0.getItem()) && ItemStack.equals(var0.getItem(), var1)) {
         var3 |= var0.getItem().count + (var2?0:var1.count) <= var1.getMaxStackSize();
      }

      return var3;
   }

   public static void a(Set<Slot> var0, int var1, ItemStack var2, int var3) {
      switch(var1) {
      case 0:
         var2.count = MathHelper.d((float)var2.count / (float)var0.size());
         break;
      case 1:
         var2.count = 1;
         break;
      case 2:
         var2.count = var2.getItem().getMaxStackSize();
      }

      var2.count += var3;
   }

   public boolean b(Slot var1) {
      return true;
   }

   public static int a(TileEntity var0) {
      return var0 instanceof IInventory?b((IInventory)var0):0;
   }

   public static int b(IInventory var0) {
      if(var0 == null) {
         return 0;
      } else {
         int var1 = 0;
         float var2 = 0.0F;

         for(int var3 = 0; var3 < var0.getSize(); ++var3) {
            ItemStack var4 = var0.getItem(var3);
            if(var4 != null) {
               var2 += (float)var4.count / (float)Math.min(var0.getMaxStackSize(), var4.getMaxStackSize());
               ++var1;
            }
         }

         var2 /= (float)var0.getSize();
         return MathHelper.d(var2 * 14.0F) + (var1 > 0?1:0);
      }
   }
}
