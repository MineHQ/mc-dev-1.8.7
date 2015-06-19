package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.Achievement;
import net.minecraft.server.AchievementSet;
import net.minecraft.server.Blocks;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;

public class AchievementList {
   public static int a;
   public static int b;
   public static int c;
   public static int d;
   public static List<Achievement> e = Lists.newArrayList();
   public static Achievement f;
   public static Achievement g;
   public static Achievement h;
   public static Achievement i;
   public static Achievement j;
   public static Achievement k;
   public static Achievement l;
   public static Achievement m;
   public static Achievement n;
   public static Achievement o;
   public static Achievement p;
   public static Achievement q;
   public static Achievement r;
   public static Achievement s;
   public static Achievement t;
   public static Achievement u;
   public static Achievement v;
   public static Achievement w;
   public static Achievement x;
   public static Achievement y;
   public static Achievement z;
   public static Achievement A;
   public static Achievement B;
   public static Achievement C;
   public static Achievement D;
   public static Achievement E;
   public static Achievement F;
   public static Achievement G;
   public static Achievement H;
   public static Achievement I;
   public static Achievement J;
   public static Achievement K;
   public static Achievement L;
   public static Achievement M;

   public static void a() {
   }

   static {
      f = (new Achievement("achievement.openInventory", "openInventory", 0, 0, Items.BOOK, (Achievement)null)).a().c();
      g = (new Achievement("achievement.mineWood", "mineWood", 2, 1, Blocks.LOG, f)).c();
      h = (new Achievement("achievement.buildWorkBench", "buildWorkBench", 4, -1, Blocks.CRAFTING_TABLE, g)).c();
      i = (new Achievement("achievement.buildPickaxe", "buildPickaxe", 4, 2, Items.WOODEN_PICKAXE, h)).c();
      j = (new Achievement("achievement.buildFurnace", "buildFurnace", 3, 4, Blocks.FURNACE, i)).c();
      k = (new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.IRON_INGOT, j)).c();
      l = (new Achievement("achievement.buildHoe", "buildHoe", 2, -3, Items.WOODEN_HOE, h)).c();
      m = (new Achievement("achievement.makeBread", "makeBread", -1, -3, Items.BREAD, l)).c();
      n = (new Achievement("achievement.bakeCake", "bakeCake", 0, -5, Items.CAKE, l)).c();
      o = (new Achievement("achievement.buildBetterPickaxe", "buildBetterPickaxe", 6, 2, Items.STONE_PICKAXE, i)).c();
      p = (new Achievement("achievement.cookFish", "cookFish", 2, 6, Items.COOKED_FISH, j)).c();
      q = (new Achievement("achievement.onARail", "onARail", 2, 3, Blocks.RAIL, k)).b().c();
      r = (new Achievement("achievement.buildSword", "buildSword", 6, -1, Items.WOODEN_SWORD, h)).c();
      s = (new Achievement("achievement.killEnemy", "killEnemy", 8, -1, Items.BONE, r)).c();
      t = (new Achievement("achievement.killCow", "killCow", 7, -3, Items.LEATHER, r)).c();
      u = (new Achievement("achievement.flyPig", "flyPig", 9, -3, Items.SADDLE, t)).b().c();
      v = (new Achievement("achievement.snipeSkeleton", "snipeSkeleton", 7, 0, Items.BOW, s)).b().c();
      w = (new Achievement("achievement.diamonds", "diamonds", -1, 5, Blocks.DIAMOND_ORE, k)).c();
      x = (new Achievement("achievement.diamondsToYou", "diamondsToYou", -1, 2, Items.DIAMOND, w)).c();
      y = (new Achievement("achievement.portal", "portal", -1, 7, Blocks.OBSIDIAN, w)).c();
      z = (new Achievement("achievement.ghast", "ghast", -4, 8, Items.GHAST_TEAR, y)).b().c();
      A = (new Achievement("achievement.blazeRod", "blazeRod", 0, 9, Items.BLAZE_ROD, y)).c();
      B = (new Achievement("achievement.potion", "potion", 2, 8, Items.POTION, A)).c();
      C = (new Achievement("achievement.theEnd", "theEnd", 3, 10, Items.ENDER_EYE, A)).b().c();
      D = (new Achievement("achievement.theEnd2", "theEnd2", 4, 13, Blocks.DRAGON_EGG, C)).b().c();
      E = (new Achievement("achievement.enchantments", "enchantments", -4, 4, Blocks.ENCHANTING_TABLE, w)).c();
      F = (new Achievement("achievement.overkill", "overkill", -4, 1, Items.DIAMOND_SWORD, E)).b().c();
      G = (new Achievement("achievement.bookcase", "bookcase", -3, 6, Blocks.BOOKSHELF, E)).c();
      H = (new Achievement("achievement.breedCow", "breedCow", 7, -5, Items.WHEAT, t)).c();
      I = (new Achievement("achievement.spawnWither", "spawnWither", 7, 12, new ItemStack(Items.SKULL, 1, 1), D)).c();
      J = (new Achievement("achievement.killWither", "killWither", 7, 10, Items.NETHER_STAR, I)).c();
      K = (new Achievement("achievement.fullBeacon", "fullBeacon", 7, 8, Blocks.BEACON, J)).b().c();
      L = (new Achievement("achievement.exploreAllBiomes", "exploreAllBiomes", 4, 8, Items.DIAMOND_BOOTS, C)).a(AchievementSet.class).b().c();
      M = (new Achievement("achievement.overpowered", "overpowered", 6, 4, new ItemStack(Items.GOLDEN_APPLE, 1, 1), o)).b().c();
   }
}
