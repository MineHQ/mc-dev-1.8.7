package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumColor;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;
import net.minecraft.server.StatisticList;
import net.minecraft.server.WeightedRandomEnchant;
import net.minecraft.server.World;

public class ContainerEnchantTable extends Container {
   public IInventory enchantSlots = new InventorySubcontainer("Enchant", true, 2) {
      public int getMaxStackSize() {
         return 64;
      }

      public void update() {
         super.update();
         ContainerEnchantTable.this.a((IInventory)this);
      }
   };
   private World world;
   private BlockPosition position;
   private Random k = new Random();
   public int f;
   public int[] costs = new int[3];
   public int[] h = new int[]{-1, -1, -1};

   public ContainerEnchantTable(PlayerInventory var1, World var2, BlockPosition var3) {
      this.world = var2;
      this.position = var3;
      this.f = var1.player.cj();
      this.a((Slot)(new Slot(this.enchantSlots, 0, 15, 47) {
         public boolean isAllowed(ItemStack var1) {
            return true;
         }

         public int getMaxStackSize() {
            return 1;
         }
      }));
      this.a((Slot)(new Slot(this.enchantSlots, 1, 35, 47) {
         public boolean isAllowed(ItemStack var1) {
            return var1.getItem() == Items.DYE && EnumColor.fromInvColorIndex(var1.getData()) == EnumColor.BLUE;
         }
      }));

      int var4;
      for(var4 = 0; var4 < 3; ++var4) {
         for(int var5 = 0; var5 < 9; ++var5) {
            this.a((Slot)(new Slot(var1, var5 + var4 * 9 + 9, 8 + var5 * 18, 84 + var4 * 18)));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.a((Slot)(new Slot(var1, var4, 8 + var4 * 18, 142)));
      }

   }

   public void addSlotListener(ICrafting var1) {
      super.addSlotListener(var1);
      var1.setContainerData(this, 0, this.costs[0]);
      var1.setContainerData(this, 1, this.costs[1]);
      var1.setContainerData(this, 2, this.costs[2]);
      var1.setContainerData(this, 3, this.f & -16);
      var1.setContainerData(this, 4, this.h[0]);
      var1.setContainerData(this, 5, this.h[1]);
      var1.setContainerData(this, 6, this.h[2]);
   }

   public void b() {
      super.b();

      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ICrafting var2 = (ICrafting)this.listeners.get(var1);
         var2.setContainerData(this, 0, this.costs[0]);
         var2.setContainerData(this, 1, this.costs[1]);
         var2.setContainerData(this, 2, this.costs[2]);
         var2.setContainerData(this, 3, this.f & -16);
         var2.setContainerData(this, 4, this.h[0]);
         var2.setContainerData(this, 5, this.h[1]);
         var2.setContainerData(this, 6, this.h[2]);
      }

   }

   public void a(IInventory var1) {
      if(var1 == this.enchantSlots) {
         ItemStack var2 = var1.getItem(0);
         int var3;
         if(var2 != null && var2.v()) {
            if(!this.world.isClientSide) {
               var3 = 0;

               int var4;
               for(var4 = -1; var4 <= 1; ++var4) {
                  for(int var5 = -1; var5 <= 1; ++var5) {
                     if((var4 != 0 || var5 != 0) && this.world.isEmpty(this.position.a(var5, 0, var4)) && this.world.isEmpty(this.position.a(var5, 1, var4))) {
                        if(this.world.getType(this.position.a(var5 * 2, 0, var4 * 2)).getBlock() == Blocks.BOOKSHELF) {
                           ++var3;
                        }

                        if(this.world.getType(this.position.a(var5 * 2, 1, var4 * 2)).getBlock() == Blocks.BOOKSHELF) {
                           ++var3;
                        }

                        if(var5 != 0 && var4 != 0) {
                           if(this.world.getType(this.position.a(var5 * 2, 0, var4)).getBlock() == Blocks.BOOKSHELF) {
                              ++var3;
                           }

                           if(this.world.getType(this.position.a(var5 * 2, 1, var4)).getBlock() == Blocks.BOOKSHELF) {
                              ++var3;
                           }

                           if(this.world.getType(this.position.a(var5, 0, var4 * 2)).getBlock() == Blocks.BOOKSHELF) {
                              ++var3;
                           }

                           if(this.world.getType(this.position.a(var5, 1, var4 * 2)).getBlock() == Blocks.BOOKSHELF) {
                              ++var3;
                           }
                        }
                     }
                  }
               }

               this.k.setSeed((long)this.f);

               for(var4 = 0; var4 < 3; ++var4) {
                  this.costs[var4] = EnchantmentManager.a(this.k, var4, var3, var2);
                  this.h[var4] = -1;
                  if(this.costs[var4] < var4 + 1) {
                     this.costs[var4] = 0;
                  }
               }

               for(var4 = 0; var4 < 3; ++var4) {
                  if(this.costs[var4] > 0) {
                     List var7 = this.a(var2, var4, this.costs[var4]);
                     if(var7 != null && !var7.isEmpty()) {
                        WeightedRandomEnchant var6 = (WeightedRandomEnchant)var7.get(this.k.nextInt(var7.size()));
                        this.h[var4] = var6.enchantment.id | var6.level << 8;
                     }
                  }
               }

               this.b();
            }
         } else {
            for(var3 = 0; var3 < 3; ++var3) {
               this.costs[var3] = 0;
               this.h[var3] = -1;
            }
         }
      }

   }

   public boolean a(EntityHuman var1, int var2) {
      ItemStack var3 = this.enchantSlots.getItem(0);
      ItemStack var4 = this.enchantSlots.getItem(1);
      int var5 = var2 + 1;
      if((var4 == null || var4.count < var5) && !var1.abilities.canInstantlyBuild) {
         return false;
      } else if(this.costs[var2] <= 0 || var3 == null || (var1.expLevel < var5 || var1.expLevel < this.costs[var2]) && !var1.abilities.canInstantlyBuild) {
         return false;
      } else {
         if(!this.world.isClientSide) {
            List var6 = this.a(var3, var2, this.costs[var2]);
            boolean var7 = var3.getItem() == Items.BOOK;
            if(var6 != null) {
               var1.b(var5);
               if(var7) {
                  var3.setItem(Items.ENCHANTED_BOOK);
               }

               for(int var8 = 0; var8 < var6.size(); ++var8) {
                  WeightedRandomEnchant var9 = (WeightedRandomEnchant)var6.get(var8);
                  if(var7) {
                     Items.ENCHANTED_BOOK.a(var3, var9);
                  } else {
                     var3.addEnchantment(var9.enchantment, var9.level);
                  }
               }

               if(!var1.abilities.canInstantlyBuild) {
                  var4.count -= var5;
                  if(var4.count <= 0) {
                     this.enchantSlots.setItem(1, (ItemStack)null);
                  }
               }

               var1.b(StatisticList.W);
               this.enchantSlots.update();
               this.f = var1.cj();
               this.a(this.enchantSlots);
            }
         }

         return true;
      }
   }

   private List<WeightedRandomEnchant> a(ItemStack var1, int var2, int var3) {
      this.k.setSeed((long)(this.f + var2));
      List var4 = EnchantmentManager.b(this.k, var1, var3);
      if(var1.getItem() == Items.BOOK && var4 != null && var4.size() > 1) {
         var4.remove(this.k.nextInt(var4.size()));
      }

      return var4;
   }

   public void b(EntityHuman var1) {
      super.b(var1);
      if(!this.world.isClientSide) {
         for(int var2 = 0; var2 < this.enchantSlots.getSize(); ++var2) {
            ItemStack var3 = this.enchantSlots.splitWithoutUpdate(var2);
            if(var3 != null) {
               var1.drop(var3, false);
            }
         }

      }
   }

   public boolean a(EntityHuman var1) {
      return this.world.getType(this.position).getBlock() != Blocks.ENCHANTING_TABLE?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public ItemStack b(EntityHuman var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.c.get(var2);
      if(var4 != null && var4.hasItem()) {
         ItemStack var5 = var4.getItem();
         var3 = var5.cloneItemStack();
         if(var2 == 0) {
            if(!this.a(var5, 2, 38, true)) {
               return null;
            }
         } else if(var2 == 1) {
            if(!this.a(var5, 2, 38, true)) {
               return null;
            }
         } else if(var5.getItem() == Items.DYE && EnumColor.fromInvColorIndex(var5.getData()) == EnumColor.BLUE) {
            if(!this.a(var5, 1, 2, true)) {
               return null;
            }
         } else {
            if(((Slot)this.c.get(0)).hasItem() || !((Slot)this.c.get(0)).isAllowed(var5)) {
               return null;
            }

            if(var5.hasTag() && var5.count == 1) {
               ((Slot)this.c.get(0)).set(var5.cloneItemStack());
               var5.count = 0;
            } else if(var5.count >= 1) {
               ((Slot)this.c.get(0)).set(new ItemStack(var5.getItem(), 1, var5.getData()));
               --var5.count;
            }
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
}
