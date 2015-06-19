package net.minecraft.server;

import net.minecraft.server.Blocks;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EnumColor;
import net.minecraft.server.IRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntityBanner;
import net.minecraft.server.World;

public class RecipesBanner {
   public RecipesBanner() {
   }

   void a(CraftingManager var1) {
      EnumColor[] var2 = EnumColor.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumColor var5 = var2[var4];
         var1.registerShapedRecipe(new ItemStack(Items.BANNER, 1, var5.getInvColorIndex()), new Object[]{"###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.WOOL, 1, var5.getColorIndex()), Character.valueOf('|'), Items.STICK});
      }

      var1.a(new RecipesBanner.DuplicateRecipe());
      var1.a(new RecipesBanner.AddRecipe());
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   static class AddRecipe implements IRecipe {
      private AddRecipe() {
      }

      public boolean a(InventoryCrafting var1, World var2) {
         boolean var3 = false;

         for(int var4 = 0; var4 < var1.getSize(); ++var4) {
            ItemStack var5 = var1.getItem(var4);
            if(var5 != null && var5.getItem() == Items.BANNER) {
               if(var3) {
                  return false;
               }

               if(TileEntityBanner.c(var5) >= 6) {
                  return false;
               }

               var3 = true;
            }
         }

         if(!var3) {
            return false;
         } else {
            return this.c(var1) != null;
         }
      }

      public ItemStack a(InventoryCrafting var1) {
         ItemStack var2 = null;

         for(int var3 = 0; var3 < var1.getSize(); ++var3) {
            ItemStack var4 = var1.getItem(var3);
            if(var4 != null && var4.getItem() == Items.BANNER) {
               var2 = var4.cloneItemStack();
               var2.count = 1;
               break;
            }
         }

         TileEntityBanner.EnumBannerPatternType var8 = this.c(var1);
         if(var8 != null) {
            int var9 = 0;

            ItemStack var6;
            for(int var5 = 0; var5 < var1.getSize(); ++var5) {
               var6 = var1.getItem(var5);
               if(var6 != null && var6.getItem() == Items.DYE) {
                  var9 = var6.getData();
                  break;
               }
            }

            NBTTagCompound var10 = var2.a("BlockEntityTag", true);
            var6 = null;
            NBTTagList var11;
            if(var10.hasKeyOfType("Patterns", 9)) {
               var11 = var10.getList("Patterns", 10);
            } else {
               var11 = new NBTTagList();
               var10.set("Patterns", var11);
            }

            NBTTagCompound var7 = new NBTTagCompound();
            var7.setString("Pattern", var8.b());
            var7.setInt("Color", var9);
            var11.add(var7);
         }

         return var2;
      }

      public int a() {
         return 10;
      }

      public ItemStack b() {
         return null;
      }

      public ItemStack[] b(InventoryCrafting var1) {
         ItemStack[] var2 = new ItemStack[var1.getSize()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ItemStack var4 = var1.getItem(var3);
            if(var4 != null && var4.getItem().r()) {
               var2[var3] = new ItemStack(var4.getItem().q());
            }
         }

         return var2;
      }

      private TileEntityBanner.EnumBannerPatternType c(InventoryCrafting var1) {
         TileEntityBanner.EnumBannerPatternType[] var2 = TileEntityBanner.EnumBannerPatternType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            TileEntityBanner.EnumBannerPatternType var5 = var2[var4];
            if(var5.d()) {
               boolean var6 = true;
               int var9;
               if(var5.e()) {
                  boolean var7 = false;
                  boolean var8 = false;

                  for(var9 = 0; var9 < var1.getSize() && var6; ++var9) {
                     ItemStack var10 = var1.getItem(var9);
                     if(var10 != null && var10.getItem() != Items.BANNER) {
                        if(var10.getItem() == Items.DYE) {
                           if(var8) {
                              var6 = false;
                              break;
                           }

                           var8 = true;
                        } else {
                           if(var7 || !var10.doMaterialsMatch(var5.f())) {
                              var6 = false;
                              break;
                           }

                           var7 = true;
                        }
                     }
                  }

                  if(!var7) {
                     var6 = false;
                  }
               } else if(var1.getSize() == var5.c().length * var5.c()[0].length()) {
                  int var12 = -1;

                  for(int var13 = 0; var13 < var1.getSize() && var6; ++var13) {
                     var9 = var13 / 3;
                     int var14 = var13 % 3;
                     ItemStack var11 = var1.getItem(var13);
                     if(var11 != null && var11.getItem() != Items.BANNER) {
                        if(var11.getItem() != Items.DYE) {
                           var6 = false;
                           break;
                        }

                        if(var12 != -1 && var12 != var11.getData()) {
                           var6 = false;
                           break;
                        }

                        if(var5.c()[var9].charAt(var14) == 32) {
                           var6 = false;
                           break;
                        }

                        var12 = var11.getData();
                     } else if(var5.c()[var9].charAt(var14) != 32) {
                        var6 = false;
                        break;
                     }
                  }
               } else {
                  var6 = false;
               }

               if(var6) {
                  return var5;
               }
            }
         }

         return null;
      }

      // $FF: synthetic method
      AddRecipe(RecipesBanner.SyntheticClass_1 var1) {
         this();
      }
   }

   static class DuplicateRecipe implements IRecipe {
      private DuplicateRecipe() {
      }

      public boolean a(InventoryCrafting var1, World var2) {
         ItemStack var3 = null;
         ItemStack var4 = null;

         for(int var5 = 0; var5 < var1.getSize(); ++var5) {
            ItemStack var6 = var1.getItem(var5);
            if(var6 != null) {
               if(var6.getItem() != Items.BANNER) {
                  return false;
               }

               if(var3 != null && var4 != null) {
                  return false;
               }

               int var7 = TileEntityBanner.b(var6);
               boolean var8 = TileEntityBanner.c(var6) > 0;
               if(var3 != null) {
                  if(var8) {
                     return false;
                  }

                  if(var7 != TileEntityBanner.b(var3)) {
                     return false;
                  }

                  var4 = var6;
               } else if(var4 != null) {
                  if(!var8) {
                     return false;
                  }

                  if(var7 != TileEntityBanner.b(var4)) {
                     return false;
                  }

                  var3 = var6;
               } else if(var8) {
                  var3 = var6;
               } else {
                  var4 = var6;
               }
            }
         }

         return var3 != null && var4 != null;
      }

      public ItemStack a(InventoryCrafting var1) {
         for(int var2 = 0; var2 < var1.getSize(); ++var2) {
            ItemStack var3 = var1.getItem(var2);
            if(var3 != null && TileEntityBanner.c(var3) > 0) {
               ItemStack var4 = var3.cloneItemStack();
               var4.count = 1;
               return var4;
            }
         }

         return null;
      }

      public int a() {
         return 2;
      }

      public ItemStack b() {
         return null;
      }

      public ItemStack[] b(InventoryCrafting var1) {
         ItemStack[] var2 = new ItemStack[var1.getSize()];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            ItemStack var4 = var1.getItem(var3);
            if(var4 != null) {
               if(var4.getItem().r()) {
                  var2[var3] = new ItemStack(var4.getItem().q());
               } else if(var4.hasTag() && TileEntityBanner.c(var4) > 0) {
                  var2[var3] = var4.cloneItemStack();
                  var2[var3].count = 1;
               }
            }
         }

         return var2;
      }

      // $FF: synthetic method
      DuplicateRecipe(RecipesBanner.SyntheticClass_1 var1) {
         this();
      }
   }
}
