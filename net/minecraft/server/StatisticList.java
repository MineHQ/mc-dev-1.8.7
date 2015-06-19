package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.AchievementList;
import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CounterStatistic;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.CraftingStatistic;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.IRecipe;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.RecipesFurnace;
import net.minecraft.server.Statistic;

public class StatisticList {
   protected static Map<String, Statistic> a = Maps.newHashMap();
   public static List<Statistic> stats = Lists.newArrayList();
   public static List<Statistic> c = Lists.newArrayList();
   public static List<CraftingStatistic> d = Lists.newArrayList();
   public static List<CraftingStatistic> e = Lists.newArrayList();
   public static Statistic f = (new CounterStatistic("stat.leaveGame", new ChatMessage("stat.leaveGame", new Object[0]))).i().h();
   public static Statistic g;
   public static Statistic h;
   public static Statistic i;
   public static Statistic j;
   public static Statistic k;
   public static Statistic l;
   public static Statistic m;
   public static Statistic n;
   public static Statistic o;
   public static Statistic p;
   public static Statistic q;
   public static Statistic r;
   public static Statistic s;
   public static Statistic t;
   public static Statistic u;
   public static Statistic v;
   public static Statistic w;
   public static Statistic x;
   public static Statistic y;
   public static Statistic z;
   public static Statistic A;
   public static Statistic B;
   public static Statistic C;
   public static Statistic D;
   public static Statistic E;
   public static Statistic F;
   public static Statistic G;
   public static Statistic H;
   public static Statistic I;
   public static Statistic J;
   public static Statistic K;
   public static Statistic L;
   public static Statistic M;
   public static Statistic N;
   public static Statistic O;
   public static Statistic P;
   public static Statistic Q;
   public static Statistic R;
   public static Statistic S;
   public static Statistic T;
   public static Statistic U;
   public static Statistic V;
   public static Statistic W;
   public static Statistic X;
   public static Statistic Y;
   public static Statistic Z;
   public static Statistic aa;
   public static final Statistic[] MINE_BLOCK_COUNT;
   public static final Statistic[] CRAFT_BLOCK_COUNT;
   public static final Statistic[] USE_ITEM_COUNT;
   public static final Statistic[] BREAK_ITEM_COUNT;

   public static void a() {
      c();
      d();
      e();
      b();
      AchievementList.a();
      EntityTypes.a();
   }

   private static void b() {
      HashSet var0 = Sets.newHashSet();
      Iterator var1 = CraftingManager.getInstance().getRecipes().iterator();

      while(var1.hasNext()) {
         IRecipe var2 = (IRecipe)var1.next();
         if(var2.b() != null) {
            var0.add(var2.b().getItem());
         }
      }

      var1 = RecipesFurnace.getInstance().getRecipes().values().iterator();

      while(var1.hasNext()) {
         ItemStack var5 = (ItemStack)var1.next();
         var0.add(var5.getItem());
      }

      var1 = var0.iterator();

      while(var1.hasNext()) {
         Item var6 = (Item)var1.next();
         if(var6 != null) {
            int var3 = Item.getId(var6);
            String var4 = a(var6);
            if(var4 != null) {
               CRAFT_BLOCK_COUNT[var3] = (new CraftingStatistic("stat.craftItem.", var4, new ChatMessage("stat.craftItem", new Object[]{(new ItemStack(var6)).C()}), var6)).h();
            }
         }
      }

      a(CRAFT_BLOCK_COUNT);
   }

   private static void c() {
      Iterator var0 = Block.REGISTRY.iterator();

      while(var0.hasNext()) {
         Block var1 = (Block)var0.next();
         Item var2 = Item.getItemOf(var1);
         if(var2 != null) {
            int var3 = Block.getId(var1);
            String var4 = a(var2);
            if(var4 != null && var1.J()) {
               MINE_BLOCK_COUNT[var3] = (new CraftingStatistic("stat.mineBlock.", var4, new ChatMessage("stat.mineBlock", new Object[]{(new ItemStack(var1)).C()}), var2)).h();
               e.add((CraftingStatistic)MINE_BLOCK_COUNT[var3]);
            }
         }
      }

      a(MINE_BLOCK_COUNT);
   }

   private static void d() {
      Iterator var0 = Item.REGISTRY.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if(var1 != null) {
            int var2 = Item.getId(var1);
            String var3 = a(var1);
            if(var3 != null) {
               USE_ITEM_COUNT[var2] = (new CraftingStatistic("stat.useItem.", var3, new ChatMessage("stat.useItem", new Object[]{(new ItemStack(var1)).C()}), var1)).h();
               if(!(var1 instanceof ItemBlock)) {
                  d.add((CraftingStatistic)USE_ITEM_COUNT[var2]);
               }
            }
         }
      }

      a(USE_ITEM_COUNT);
   }

   private static void e() {
      Iterator var0 = Item.REGISTRY.iterator();

      while(var0.hasNext()) {
         Item var1 = (Item)var0.next();
         if(var1 != null) {
            int var2 = Item.getId(var1);
            String var3 = a(var1);
            if(var3 != null && var1.usesDurability()) {
               BREAK_ITEM_COUNT[var2] = (new CraftingStatistic("stat.breakItem.", var3, new ChatMessage("stat.breakItem", new Object[]{(new ItemStack(var1)).C()}), var1)).h();
            }
         }
      }

      a(BREAK_ITEM_COUNT);
   }

   private static String a(Item var0) {
      MinecraftKey var1 = (MinecraftKey)Item.REGISTRY.c(var0);
      return var1 != null?var1.toString().replace(':', '.'):null;
   }

   private static void a(Statistic[] var0) {
      a(var0, Blocks.WATER, Blocks.FLOWING_WATER);
      a(var0, Blocks.LAVA, Blocks.FLOWING_LAVA);
      a(var0, Blocks.LIT_PUMPKIN, Blocks.PUMPKIN);
      a(var0, Blocks.LIT_FURNACE, Blocks.FURNACE);
      a(var0, Blocks.LIT_REDSTONE_ORE, Blocks.REDSTONE_ORE);
      a(var0, Blocks.POWERED_REPEATER, Blocks.UNPOWERED_REPEATER);
      a(var0, Blocks.POWERED_COMPARATOR, Blocks.UNPOWERED_COMPARATOR);
      a(var0, Blocks.REDSTONE_TORCH, Blocks.UNLIT_REDSTONE_TORCH);
      a(var0, Blocks.LIT_REDSTONE_LAMP, Blocks.REDSTONE_LAMP);
      a(var0, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB);
      a(var0, Blocks.DOUBLE_WOODEN_SLAB, Blocks.WOODEN_SLAB);
      a(var0, Blocks.DOUBLE_STONE_SLAB2, Blocks.STONE_SLAB2);
      a(var0, Blocks.GRASS, Blocks.DIRT);
      a(var0, Blocks.FARMLAND, Blocks.DIRT);
   }

   private static void a(Statistic[] var0, Block var1, Block var2) {
      int var3 = Block.getId(var1);
      int var4 = Block.getId(var2);
      if(var0[var3] != null && var0[var4] == null) {
         var0[var4] = var0[var3];
      } else {
         stats.remove(var0[var3]);
         e.remove(var0[var3]);
         c.remove(var0[var3]);
         var0[var3] = var0[var4];
      }
   }

   public static Statistic a(EntityTypes.MonsterEggInfo var0) {
      String var1 = EntityTypes.b(var0.a);
      return var1 == null?null:(new Statistic("stat.killEntity." + var1, new ChatMessage("stat.entityKill", new Object[]{new ChatMessage("entity." + var1 + ".name", new Object[0])}))).h();
   }

   public static Statistic b(EntityTypes.MonsterEggInfo var0) {
      String var1 = EntityTypes.b(var0.a);
      return var1 == null?null:(new Statistic("stat.entityKilledBy." + var1, new ChatMessage("stat.entityKilledBy", new Object[]{new ChatMessage("entity." + var1 + ".name", new Object[0])}))).h();
   }

   public static Statistic getStatistic(String var0) {
      return (Statistic)a.get(var0);
   }

   static {
      g = (new CounterStatistic("stat.playOneMinute", new ChatMessage("stat.playOneMinute", new Object[0]), Statistic.h)).i().h();
      h = (new CounterStatistic("stat.timeSinceDeath", new ChatMessage("stat.timeSinceDeath", new Object[0]), Statistic.h)).i().h();
      i = (new CounterStatistic("stat.walkOneCm", new ChatMessage("stat.walkOneCm", new Object[0]), Statistic.i)).i().h();
      j = (new CounterStatistic("stat.crouchOneCm", new ChatMessage("stat.crouchOneCm", new Object[0]), Statistic.i)).i().h();
      k = (new CounterStatistic("stat.sprintOneCm", new ChatMessage("stat.sprintOneCm", new Object[0]), Statistic.i)).i().h();
      l = (new CounterStatistic("stat.swimOneCm", new ChatMessage("stat.swimOneCm", new Object[0]), Statistic.i)).i().h();
      m = (new CounterStatistic("stat.fallOneCm", new ChatMessage("stat.fallOneCm", new Object[0]), Statistic.i)).i().h();
      n = (new CounterStatistic("stat.climbOneCm", new ChatMessage("stat.climbOneCm", new Object[0]), Statistic.i)).i().h();
      o = (new CounterStatistic("stat.flyOneCm", new ChatMessage("stat.flyOneCm", new Object[0]), Statistic.i)).i().h();
      p = (new CounterStatistic("stat.diveOneCm", new ChatMessage("stat.diveOneCm", new Object[0]), Statistic.i)).i().h();
      q = (new CounterStatistic("stat.minecartOneCm", new ChatMessage("stat.minecartOneCm", new Object[0]), Statistic.i)).i().h();
      r = (new CounterStatistic("stat.boatOneCm", new ChatMessage("stat.boatOneCm", new Object[0]), Statistic.i)).i().h();
      s = (new CounterStatistic("stat.pigOneCm", new ChatMessage("stat.pigOneCm", new Object[0]), Statistic.i)).i().h();
      t = (new CounterStatistic("stat.horseOneCm", new ChatMessage("stat.horseOneCm", new Object[0]), Statistic.i)).i().h();
      u = (new CounterStatistic("stat.jump", new ChatMessage("stat.jump", new Object[0]))).i().h();
      v = (new CounterStatistic("stat.drop", new ChatMessage("stat.drop", new Object[0]))).i().h();
      w = (new CounterStatistic("stat.damageDealt", new ChatMessage("stat.damageDealt", new Object[0]), Statistic.j)).h();
      x = (new CounterStatistic("stat.damageTaken", new ChatMessage("stat.damageTaken", new Object[0]), Statistic.j)).h();
      y = (new CounterStatistic("stat.deaths", new ChatMessage("stat.deaths", new Object[0]))).h();
      z = (new CounterStatistic("stat.mobKills", new ChatMessage("stat.mobKills", new Object[0]))).h();
      A = (new CounterStatistic("stat.animalsBred", new ChatMessage("stat.animalsBred", new Object[0]))).h();
      B = (new CounterStatistic("stat.playerKills", new ChatMessage("stat.playerKills", new Object[0]))).h();
      C = (new CounterStatistic("stat.fishCaught", new ChatMessage("stat.fishCaught", new Object[0]))).h();
      D = (new CounterStatistic("stat.junkFished", new ChatMessage("stat.junkFished", new Object[0]))).h();
      E = (new CounterStatistic("stat.treasureFished", new ChatMessage("stat.treasureFished", new Object[0]))).h();
      F = (new CounterStatistic("stat.talkedToVillager", new ChatMessage("stat.talkedToVillager", new Object[0]))).h();
      G = (new CounterStatistic("stat.tradedWithVillager", new ChatMessage("stat.tradedWithVillager", new Object[0]))).h();
      H = (new CounterStatistic("stat.cakeSlicesEaten", new ChatMessage("stat.cakeSlicesEaten", new Object[0]))).h();
      I = (new CounterStatistic("stat.cauldronFilled", new ChatMessage("stat.cauldronFilled", new Object[0]))).h();
      J = (new CounterStatistic("stat.cauldronUsed", new ChatMessage("stat.cauldronUsed", new Object[0]))).h();
      K = (new CounterStatistic("stat.armorCleaned", new ChatMessage("stat.armorCleaned", new Object[0]))).h();
      L = (new CounterStatistic("stat.bannerCleaned", new ChatMessage("stat.bannerCleaned", new Object[0]))).h();
      M = (new CounterStatistic("stat.brewingstandInteraction", new ChatMessage("stat.brewingstandInteraction", new Object[0]))).h();
      N = (new CounterStatistic("stat.beaconInteraction", new ChatMessage("stat.beaconInteraction", new Object[0]))).h();
      O = (new CounterStatistic("stat.dropperInspected", new ChatMessage("stat.dropperInspected", new Object[0]))).h();
      P = (new CounterStatistic("stat.hopperInspected", new ChatMessage("stat.hopperInspected", new Object[0]))).h();
      Q = (new CounterStatistic("stat.dispenserInspected", new ChatMessage("stat.dispenserInspected", new Object[0]))).h();
      R = (new CounterStatistic("stat.noteblockPlayed", new ChatMessage("stat.noteblockPlayed", new Object[0]))).h();
      S = (new CounterStatistic("stat.noteblockTuned", new ChatMessage("stat.noteblockTuned", new Object[0]))).h();
      T = (new CounterStatistic("stat.flowerPotted", new ChatMessage("stat.flowerPotted", new Object[0]))).h();
      U = (new CounterStatistic("stat.trappedChestTriggered", new ChatMessage("stat.trappedChestTriggered", new Object[0]))).h();
      V = (new CounterStatistic("stat.enderchestOpened", new ChatMessage("stat.enderchestOpened", new Object[0]))).h();
      W = (new CounterStatistic("stat.itemEnchanted", new ChatMessage("stat.itemEnchanted", new Object[0]))).h();
      X = (new CounterStatistic("stat.recordPlayed", new ChatMessage("stat.recordPlayed", new Object[0]))).h();
      Y = (new CounterStatistic("stat.furnaceInteraction", new ChatMessage("stat.furnaceInteraction", new Object[0]))).h();
      Z = (new CounterStatistic("stat.craftingTableInteraction", new ChatMessage("stat.workbenchInteraction", new Object[0]))).h();
      aa = (new CounterStatistic("stat.chestOpened", new ChatMessage("stat.chestOpened", new Object[0]))).h();
      MINE_BLOCK_COUNT = new Statistic[4096];
      CRAFT_BLOCK_COUNT = new Statistic[32000];
      USE_ITEM_COUNT = new Statistic[32000];
      BREAK_ITEM_COUNT = new Statistic[32000];
   }
}
