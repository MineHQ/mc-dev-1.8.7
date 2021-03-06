package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EnumColor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;

public class RecipeIngots {
   private Object[][] a;

   public RecipeIngots() {
      this.a = new Object[][]{{Blocks.GOLD_BLOCK, new ItemStack(Items.GOLD_INGOT, 9)}, {Blocks.IRON_BLOCK, new ItemStack(Items.IRON_INGOT, 9)}, {Blocks.DIAMOND_BLOCK, new ItemStack(Items.DIAMOND, 9)}, {Blocks.EMERALD_BLOCK, new ItemStack(Items.EMERALD, 9)}, {Blocks.LAPIS_BLOCK, new ItemStack(Items.DYE, 9, EnumColor.BLUE.getInvColorIndex())}, {Blocks.REDSTONE_BLOCK, new ItemStack(Items.REDSTONE, 9)}, {Blocks.COAL_BLOCK, new ItemStack(Items.COAL, 9, 0)}, {Blocks.HAY_BLOCK, new ItemStack(Items.WHEAT, 9)}, {Blocks.SLIME, new ItemStack(Items.SLIME, 9)}};
   }

   public void a(CraftingManager var1) {
      for(int var2 = 0; var2 < this.a.length; ++var2) {
         Block var3 = (Block)this.a[var2][0];
         ItemStack var4 = (ItemStack)this.a[var2][1];
         var1.registerShapedRecipe(new ItemStack(var3), new Object[]{"###", "###", "###", Character.valueOf('#'), var4});
         var1.registerShapedRecipe(var4, new Object[]{"#", Character.valueOf('#'), var3});
      }

      var1.registerShapedRecipe(new ItemStack(Items.GOLD_INGOT), new Object[]{"###", "###", "###", Character.valueOf('#'), Items.GOLD_NUGGET});
      var1.registerShapedRecipe(new ItemStack(Items.GOLD_NUGGET, 9), new Object[]{"#", Character.valueOf('#'), Items.GOLD_INGOT});
   }
}
