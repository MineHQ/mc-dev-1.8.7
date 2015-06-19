package net.minecraft.server;

import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockDoubleStepAbstract;
import net.minecraft.server.BlockDoubleStoneStepAbstract;
import net.minecraft.server.BlockPrismarine;
import net.minecraft.server.BlockQuartz;
import net.minecraft.server.BlockRedSandstone;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockSandStone;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.BlockStone;
import net.minecraft.server.Blocks;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EnumColor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;

public class RecipesCrafting {
   public RecipesCrafting() {
   }

   public void a(CraftingManager var1) {
      var1.registerShapedRecipe(new ItemStack(Blocks.CHEST), new Object[]{"###", "# #", "###", Character.valueOf('#'), Blocks.PLANKS});
      var1.registerShapedRecipe(new ItemStack(Blocks.TRAPPED_CHEST), new Object[]{"#-", Character.valueOf('#'), Blocks.CHEST, Character.valueOf('-'), Blocks.TRIPWIRE_HOOK});
      var1.registerShapedRecipe(new ItemStack(Blocks.ENDER_CHEST), new Object[]{"###", "#E#", "###", Character.valueOf('#'), Blocks.OBSIDIAN, Character.valueOf('E'), Items.ENDER_EYE});
      var1.registerShapedRecipe(new ItemStack(Blocks.FURNACE), new Object[]{"###", "# #", "###", Character.valueOf('#'), Blocks.COBBLESTONE});
      var1.registerShapedRecipe(new ItemStack(Blocks.CRAFTING_TABLE), new Object[]{"##", "##", Character.valueOf('#'), Blocks.PLANKS});
      var1.registerShapedRecipe(new ItemStack(Blocks.SANDSTONE), new Object[]{"##", "##", Character.valueOf('#'), new ItemStack(Blocks.SAND, 1, BlockSand.EnumSandVariant.SAND.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.RED_SANDSTONE), new Object[]{"##", "##", Character.valueOf('#'), new ItemStack(Blocks.SAND, 1, BlockSand.EnumSandVariant.RED_SAND.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.SANDSTONE, 4, BlockSandStone.EnumSandstoneVariant.SMOOTH.a()), new Object[]{"##", "##", Character.valueOf('#'), new ItemStack(Blocks.SANDSTONE, 1, BlockSandStone.EnumSandstoneVariant.DEFAULT.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.RED_SANDSTONE, 4, BlockRedSandstone.EnumRedSandstoneVariant.SMOOTH.a()), new Object[]{"##", "##", Character.valueOf('#'), new ItemStack(Blocks.RED_SANDSTONE, 1, BlockRedSandstone.EnumRedSandstoneVariant.DEFAULT.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.SANDSTONE, 1, BlockSandStone.EnumSandstoneVariant.CHISELED.a()), new Object[]{"#", "#", Character.valueOf('#'), new ItemStack(Blocks.STONE_SLAB, 1, BlockDoubleStepAbstract.EnumStoneSlabVariant.SAND.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.RED_SANDSTONE, 1, BlockRedSandstone.EnumRedSandstoneVariant.CHISELED.a()), new Object[]{"#", "#", Character.valueOf('#'), new ItemStack(Blocks.STONE_SLAB2, 1, BlockDoubleStoneStepAbstract.EnumStoneSlab2Variant.RED_SANDSTONE.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.QUARTZ_BLOCK, 1, BlockQuartz.EnumQuartzVariant.CHISELED.a()), new Object[]{"#", "#", Character.valueOf('#'), new ItemStack(Blocks.STONE_SLAB, 1, BlockDoubleStepAbstract.EnumStoneSlabVariant.QUARTZ.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.QUARTZ_BLOCK, 2, BlockQuartz.EnumQuartzVariant.LINES_Y.a()), new Object[]{"#", "#", Character.valueOf('#'), new ItemStack(Blocks.QUARTZ_BLOCK, 1, BlockQuartz.EnumQuartzVariant.DEFAULT.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONEBRICK, 4), new Object[]{"##", "##", Character.valueOf('#'), new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.STONE.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.P), new Object[]{"#", "#", Character.valueOf('#'), new ItemStack(Blocks.STONE_SLAB, 1, BlockDoubleStepAbstract.EnumStoneSlabVariant.SMOOTHBRICK.a())});
      var1.registerShapelessRecipe(new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.N), new Object[]{Blocks.STONEBRICK, Blocks.VINE});
      var1.registerShapelessRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE, 1), new Object[]{Blocks.COBBLESTONE, Blocks.VINE});
      var1.registerShapedRecipe(new ItemStack(Blocks.IRON_BARS, 16), new Object[]{"###", "###", Character.valueOf('#'), Items.IRON_INGOT});
      var1.registerShapedRecipe(new ItemStack(Blocks.GLASS_PANE, 16), new Object[]{"###", "###", Character.valueOf('#'), Blocks.GLASS});
      var1.registerShapedRecipe(new ItemStack(Blocks.REDSTONE_LAMP, 1), new Object[]{" R ", "RGR", " R ", Character.valueOf('R'), Items.REDSTONE, Character.valueOf('G'), Blocks.GLOWSTONE});
      var1.registerShapedRecipe(new ItemStack(Blocks.BEACON, 1), new Object[]{"GGG", "GSG", "OOO", Character.valueOf('G'), Blocks.GLASS, Character.valueOf('S'), Items.NETHER_STAR, Character.valueOf('O'), Blocks.OBSIDIAN});
      var1.registerShapedRecipe(new ItemStack(Blocks.NETHER_BRICK, 1), new Object[]{"NN", "NN", Character.valueOf('N'), Items.NETHERBRICK});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONE, 2, BlockStone.EnumStoneVariant.DIORITE.a()), new Object[]{"CQ", "QC", Character.valueOf('C'), Blocks.COBBLESTONE, Character.valueOf('Q'), Items.QUARTZ});
      var1.registerShapelessRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.GRANITE.a()), new Object[]{new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.DIORITE.a()), Items.QUARTZ});
      var1.registerShapelessRecipe(new ItemStack(Blocks.STONE, 2, BlockStone.EnumStoneVariant.ANDESITE.a()), new Object[]{new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.DIORITE.a()), Blocks.COBBLESTONE});
      var1.registerShapedRecipe(new ItemStack(Blocks.DIRT, 4, BlockDirt.EnumDirtVariant.COARSE_DIRT.a()), new Object[]{"DG", "GD", Character.valueOf('D'), new ItemStack(Blocks.DIRT, 1, BlockDirt.EnumDirtVariant.DIRT.a()), Character.valueOf('G'), Blocks.GRAVEL});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONE, 4, BlockStone.EnumStoneVariant.DIORITE_SMOOTH.a()), new Object[]{"SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.DIORITE.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONE, 4, BlockStone.EnumStoneVariant.GRANITE_SMOOTH.a()), new Object[]{"SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.GRANITE.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.STONE, 4, BlockStone.EnumStoneVariant.ANDESITE_SMOOTH.a()), new Object[]{"SS", "SS", Character.valueOf('S'), new ItemStack(Blocks.STONE, 1, BlockStone.EnumStoneVariant.ANDESITE.a())});
      var1.registerShapedRecipe(new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.b), new Object[]{"SS", "SS", Character.valueOf('S'), Items.PRISMARINE_SHARD});
      var1.registerShapedRecipe(new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.N), new Object[]{"SSS", "SSS", "SSS", Character.valueOf('S'), Items.PRISMARINE_SHARD});
      var1.registerShapedRecipe(new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.O), new Object[]{"SSS", "SIS", "SSS", Character.valueOf('S'), Items.PRISMARINE_SHARD, Character.valueOf('I'), new ItemStack(Items.DYE, 1, EnumColor.BLACK.getInvColorIndex())});
      var1.registerShapedRecipe(new ItemStack(Blocks.SEA_LANTERN, 1, 0), new Object[]{"SCS", "CCC", "SCS", Character.valueOf('S'), Items.PRISMARINE_SHARD, Character.valueOf('C'), Items.PRISMARINE_CRYSTALS});
   }
}
