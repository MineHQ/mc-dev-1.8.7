package net.minecraft.server;

import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.Blocks;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EnumColor;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;

public class RecipesDyes {
   public RecipesDyes() {
   }

   public void a(CraftingManager var1) {
      int var2;
      for(var2 = 0; var2 < 16; ++var2) {
         var1.registerShapelessRecipe(new ItemStack(Blocks.WOOL, 1, var2), new Object[]{new ItemStack(Items.DYE, 1, 15 - var2), new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 0)});
         var1.registerShapedRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 8, 15 - var2), new Object[]{"###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.HARDENED_CLAY), Character.valueOf('X'), new ItemStack(Items.DYE, 1, var2)});
         var1.registerShapedRecipe(new ItemStack(Blocks.STAINED_GLASS, 8, 15 - var2), new Object[]{"###", "#X#", "###", Character.valueOf('#'), new ItemStack(Blocks.GLASS), Character.valueOf('X'), new ItemStack(Items.DYE, 1, var2)});
         var1.registerShapedRecipe(new ItemStack(Blocks.STAINED_GLASS_PANE, 16, var2), new Object[]{"###", "###", Character.valueOf('#'), new ItemStack(Blocks.STAINED_GLASS, 1, var2)});
      }

      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.YELLOW.getInvColorIndex()), new Object[]{new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockFlowers.EnumFlowerVarient.DANDELION.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.POPPY.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.WHITE.getInvColorIndex()), new Object[]{Items.BONE});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PINK.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.ORANGE.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.YELLOW.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.LIME.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.GREEN.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.GRAY.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLACK.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.SILVER.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.GRAY.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.SILVER.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLACK.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.LIGHT_BLUE.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.CYAN.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.GREEN.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PURPLE.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.MAGENTA.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.PURPLE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 3, EnumColor.MAGENTA.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 4, EnumColor.MAGENTA.getInvColorIndex()), new Object[]{new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new ItemStack(Items.DYE, 1, EnumColor.WHITE.getInvColorIndex())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.LIGHT_BLUE.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.BLUE_ORCHID.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.MAGENTA.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.ALLIUM.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.HOUSTONIA.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.RED.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.RED_TULIP.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.ORANGE.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.ORANGE_TULIP.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.WHITE_TULIP.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.PINK.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.PINK_TULIP.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 1, EnumColor.SILVER.getInvColorIndex()), new Object[]{new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.OXEYE_DAISY.b())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.YELLOW.getInvColorIndex()), new Object[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.SUNFLOWER.a())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.MAGENTA.getInvColorIndex()), new Object[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.SYRINGA.a())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.RED.getInvColorIndex()), new Object[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.ROSE.a())});
      var1.registerShapelessRecipe(new ItemStack(Items.DYE, 2, EnumColor.PINK.getInvColorIndex()), new Object[]{new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockTallPlant.EnumTallFlowerVariants.PAEONIA.a())});

      for(var2 = 0; var2 < 16; ++var2) {
         var1.registerShapedRecipe(new ItemStack(Blocks.CARPET, 3, var2), new Object[]{"##", Character.valueOf('#'), new ItemStack(Blocks.WOOL, 1, var2)});
      }

   }
}
