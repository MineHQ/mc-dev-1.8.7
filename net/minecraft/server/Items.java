package net.minecraft.server;

import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemArmorStand;
import net.minecraft.server.ItemBow;
import net.minecraft.server.ItemEnchantedBook;
import net.minecraft.server.ItemFishingRod;
import net.minecraft.server.ItemMapEmpty;
import net.minecraft.server.ItemPotion;
import net.minecraft.server.ItemShears;
import net.minecraft.server.ItemWorldMap;
import net.minecraft.server.MinecraftKey;

public class Items {
   public static final Item IRON_SHOVEL;
   public static final Item IRON_PICKAXE;
   public static final Item IRON_AXE;
   public static final Item FLINT_AND_STEEL;
   public static final Item APPLE;
   public static final ItemBow BOW;
   public static final Item ARROW;
   public static final Item COAL;
   public static final Item DIAMOND;
   public static final Item IRON_INGOT;
   public static final Item GOLD_INGOT;
   public static final Item IRON_SWORD;
   public static final Item WOODEN_SWORD;
   public static final Item WOODEN_SHOVEL;
   public static final Item WOODEN_PICKAXE;
   public static final Item WOODEN_AXE;
   public static final Item STONE_SWORD;
   public static final Item STONE_SHOVEL;
   public static final Item STONE_PICKAXE;
   public static final Item STONE_AXE;
   public static final Item DIAMOND_SWORD;
   public static final Item DIAMOND_SHOVEL;
   public static final Item DIAMOND_PICKAXE;
   public static final Item DIAMOND_AXE;
   public static final Item STICK;
   public static final Item BOWL;
   public static final Item MUSHROOM_STEW;
   public static final Item GOLDEN_SWORD;
   public static final Item GOLDEN_SHOVEL;
   public static final Item GOLDEN_PICKAXE;
   public static final Item GOLDEN_AXE;
   public static final Item STRING;
   public static final Item FEATHER;
   public static final Item GUNPOWDER;
   public static final Item WOODEN_HOE;
   public static final Item STONE_HOE;
   public static final Item IRON_HOE;
   public static final Item DIAMOND_HOE;
   public static final Item GOLDEN_HOE;
   public static final Item WHEAT_SEEDS;
   public static final Item WHEAT;
   public static final Item BREAD;
   public static final ItemArmor LEATHER_HELMET;
   public static final ItemArmor LEATHER_CHESTPLATE;
   public static final ItemArmor LEATHER_LEGGINGS;
   public static final ItemArmor LEATHER_BOOTS;
   public static final ItemArmor CHAINMAIL_HELMET;
   public static final ItemArmor CHAINMAIL_CHESTPLATE;
   public static final ItemArmor CHAINMAIL_LEGGINGS;
   public static final ItemArmor CHAINMAIL_BOOTS;
   public static final ItemArmor IRON_HELMET;
   public static final ItemArmor IRON_CHESTPLATE;
   public static final ItemArmor IRON_LEGGINGS;
   public static final ItemArmor IRON_BOOTS;
   public static final ItemArmor DIAMOND_HELMET;
   public static final ItemArmor DIAMOND_CHESTPLATE;
   public static final ItemArmor DIAMOND_LEGGINGS;
   public static final ItemArmor DIAMOND_BOOTS;
   public static final ItemArmor GOLDEN_HELMET;
   public static final ItemArmor GOLDEN_CHESTPLATE;
   public static final ItemArmor GOLDEN_LEGGINGS;
   public static final ItemArmor GOLDEN_BOOTS;
   public static final Item FLINT;
   public static final Item PORKCHOP;
   public static final Item COOKED_PORKCHOP;
   public static final Item PAINTING;
   public static final Item GOLDEN_APPLE;
   public static final Item SIGN;
   public static final Item WOODEN_DOOR;
   public static final Item SPRUCE_DOOR;
   public static final Item BIRCH_DOOR;
   public static final Item JUNGLE_DOOR;
   public static final Item ACACIA_DOOR;
   public static final Item DARK_OAK_DOOR;
   public static final Item BUCKET;
   public static final Item WATER_BUCKET;
   public static final Item LAVA_BUCKET;
   public static final Item MINECART;
   public static final Item SADDLE;
   public static final Item IRON_DOOR;
   public static final Item REDSTONE;
   public static final Item SNOWBALL;
   public static final Item BOAT;
   public static final Item LEATHER;
   public static final Item MILK_BUCKET;
   public static final Item BRICK;
   public static final Item CLAY_BALL;
   public static final Item REEDS;
   public static final Item PAPER;
   public static final Item BOOK;
   public static final Item SLIME;
   public static final Item CHEST_MINECART;
   public static final Item FURNACE_MINECART;
   public static final Item EGG;
   public static final Item COMPASS;
   public static final ItemFishingRod FISHING_ROD;
   public static final Item CLOCK;
   public static final Item GLOWSTONE_DUST;
   public static final Item FISH;
   public static final Item COOKED_FISH;
   public static final Item DYE;
   public static final Item BONE;
   public static final Item SUGAR;
   public static final Item CAKE;
   public static final Item BED;
   public static final Item REPEATER;
   public static final Item COOKIE;
   public static final ItemWorldMap FILLED_MAP;
   public static final ItemShears SHEARS;
   public static final Item MELON;
   public static final Item PUMPKIN_SEEDS;
   public static final Item MELON_SEEDS;
   public static final Item BEEF;
   public static final Item COOKED_BEEF;
   public static final Item CHICKEN;
   public static final Item COOKED_CHICKEN;
   public static final Item MUTTON;
   public static final Item COOKED_MUTTON;
   public static final Item RABBIT;
   public static final Item COOKED_RABBIT;
   public static final Item RABBIT_STEW;
   public static final Item RABBIT_FOOT;
   public static final Item RABBIT_HIDE;
   public static final Item ROTTEN_FLESH;
   public static final Item ENDER_PEARL;
   public static final Item BLAZE_ROD;
   public static final Item GHAST_TEAR;
   public static final Item GOLD_NUGGET;
   public static final Item NETHER_WART;
   public static final ItemPotion POTION;
   public static final Item GLASS_BOTTLE;
   public static final Item SPIDER_EYE;
   public static final Item FERMENTED_SPIDER_EYE;
   public static final Item BLAZE_POWDER;
   public static final Item MAGMA_CREAM;
   public static final Item BREWING_STAND;
   public static final Item CAULDRON;
   public static final Item ENDER_EYE;
   public static final Item SPECKLED_MELON;
   public static final Item SPAWN_EGG;
   public static final Item EXPERIENCE_BOTTLE;
   public static final Item FIRE_CHARGE;
   public static final Item WRITABLE_BOOK;
   public static final Item WRITTEN_BOOK;
   public static final Item EMERALD;
   public static final Item ITEM_FRAME;
   public static final Item FLOWER_POT;
   public static final Item CARROT;
   public static final Item POTATO;
   public static final Item BAKED_POTATO;
   public static final Item POISONOUS_POTATO;
   public static final ItemMapEmpty MAP;
   public static final Item GOLDEN_CARROT;
   public static final Item SKULL;
   public static final Item CARROT_ON_A_STICK;
   public static final Item NETHER_STAR;
   public static final Item PUMPKIN_PIE;
   public static final Item FIREWORKS;
   public static final Item FIREWORK_CHARGE;
   public static final ItemEnchantedBook ENCHANTED_BOOK;
   public static final Item COMPARATOR;
   public static final Item NETHERBRICK;
   public static final Item QUARTZ;
   public static final Item TNT_MINECART;
   public static final Item HOPPER_MINECART;
   public static final ItemArmorStand ARMOR_STAND;
   public static final Item IRON_HORSE_ARMOR;
   public static final Item GOLDEN_HORSE_ARMOR;
   public static final Item DIAMOND_HORSE_ARMOR;
   public static final Item LEAD;
   public static final Item NAME_TAG;
   public static final Item COMMAND_BLOCK_MINECART;
   public static final Item RECORD_13;
   public static final Item RECORD_CAT;
   public static final Item RECORD_BLOCKS;
   public static final Item RECORD_CHIRP;
   public static final Item RECORD_FAR;
   public static final Item RECORD_MALL;
   public static final Item RECORD_MELLOHI;
   public static final Item RECORD_STAL;
   public static final Item RECORD_STRAD;
   public static final Item RECORD_WARD;
   public static final Item RECORD_11;
   public static final Item RECORD_WAIT;
   public static final Item PRISMARINE_SHARD;
   public static final Item PRISMARINE_CRYSTALS;
   public static final Item BANNER;

   private static Item get(String var0) {
      return (Item)Item.REGISTRY.get(new MinecraftKey(var0));
   }

   static {
      if(!DispenserRegistry.a()) {
         throw new RuntimeException("Accessed Items before Bootstrap!");
      } else {
         IRON_SHOVEL = get("iron_shovel");
         IRON_PICKAXE = get("iron_pickaxe");
         IRON_AXE = get("iron_axe");
         FLINT_AND_STEEL = get("flint_and_steel");
         APPLE = get("apple");
         BOW = (ItemBow)get("bow");
         ARROW = get("arrow");
         COAL = get("coal");
         DIAMOND = get("diamond");
         IRON_INGOT = get("iron_ingot");
         GOLD_INGOT = get("gold_ingot");
         IRON_SWORD = get("iron_sword");
         WOODEN_SWORD = get("wooden_sword");
         WOODEN_SHOVEL = get("wooden_shovel");
         WOODEN_PICKAXE = get("wooden_pickaxe");
         WOODEN_AXE = get("wooden_axe");
         STONE_SWORD = get("stone_sword");
         STONE_SHOVEL = get("stone_shovel");
         STONE_PICKAXE = get("stone_pickaxe");
         STONE_AXE = get("stone_axe");
         DIAMOND_SWORD = get("diamond_sword");
         DIAMOND_SHOVEL = get("diamond_shovel");
         DIAMOND_PICKAXE = get("diamond_pickaxe");
         DIAMOND_AXE = get("diamond_axe");
         STICK = get("stick");
         BOWL = get("bowl");
         MUSHROOM_STEW = get("mushroom_stew");
         GOLDEN_SWORD = get("golden_sword");
         GOLDEN_SHOVEL = get("golden_shovel");
         GOLDEN_PICKAXE = get("golden_pickaxe");
         GOLDEN_AXE = get("golden_axe");
         STRING = get("string");
         FEATHER = get("feather");
         GUNPOWDER = get("gunpowder");
         WOODEN_HOE = get("wooden_hoe");
         STONE_HOE = get("stone_hoe");
         IRON_HOE = get("iron_hoe");
         DIAMOND_HOE = get("diamond_hoe");
         GOLDEN_HOE = get("golden_hoe");
         WHEAT_SEEDS = get("wheat_seeds");
         WHEAT = get("wheat");
         BREAD = get("bread");
         LEATHER_HELMET = (ItemArmor)get("leather_helmet");
         LEATHER_CHESTPLATE = (ItemArmor)get("leather_chestplate");
         LEATHER_LEGGINGS = (ItemArmor)get("leather_leggings");
         LEATHER_BOOTS = (ItemArmor)get("leather_boots");
         CHAINMAIL_HELMET = (ItemArmor)get("chainmail_helmet");
         CHAINMAIL_CHESTPLATE = (ItemArmor)get("chainmail_chestplate");
         CHAINMAIL_LEGGINGS = (ItemArmor)get("chainmail_leggings");
         CHAINMAIL_BOOTS = (ItemArmor)get("chainmail_boots");
         IRON_HELMET = (ItemArmor)get("iron_helmet");
         IRON_CHESTPLATE = (ItemArmor)get("iron_chestplate");
         IRON_LEGGINGS = (ItemArmor)get("iron_leggings");
         IRON_BOOTS = (ItemArmor)get("iron_boots");
         DIAMOND_HELMET = (ItemArmor)get("diamond_helmet");
         DIAMOND_CHESTPLATE = (ItemArmor)get("diamond_chestplate");
         DIAMOND_LEGGINGS = (ItemArmor)get("diamond_leggings");
         DIAMOND_BOOTS = (ItemArmor)get("diamond_boots");
         GOLDEN_HELMET = (ItemArmor)get("golden_helmet");
         GOLDEN_CHESTPLATE = (ItemArmor)get("golden_chestplate");
         GOLDEN_LEGGINGS = (ItemArmor)get("golden_leggings");
         GOLDEN_BOOTS = (ItemArmor)get("golden_boots");
         FLINT = get("flint");
         PORKCHOP = get("porkchop");
         COOKED_PORKCHOP = get("cooked_porkchop");
         PAINTING = get("painting");
         GOLDEN_APPLE = get("golden_apple");
         SIGN = get("sign");
         WOODEN_DOOR = get("wooden_door");
         SPRUCE_DOOR = get("spruce_door");
         BIRCH_DOOR = get("birch_door");
         JUNGLE_DOOR = get("jungle_door");
         ACACIA_DOOR = get("acacia_door");
         DARK_OAK_DOOR = get("dark_oak_door");
         BUCKET = get("bucket");
         WATER_BUCKET = get("water_bucket");
         LAVA_BUCKET = get("lava_bucket");
         MINECART = get("minecart");
         SADDLE = get("saddle");
         IRON_DOOR = get("iron_door");
         REDSTONE = get("redstone");
         SNOWBALL = get("snowball");
         BOAT = get("boat");
         LEATHER = get("leather");
         MILK_BUCKET = get("milk_bucket");
         BRICK = get("brick");
         CLAY_BALL = get("clay_ball");
         REEDS = get("reeds");
         PAPER = get("paper");
         BOOK = get("book");
         SLIME = get("slime_ball");
         CHEST_MINECART = get("chest_minecart");
         FURNACE_MINECART = get("furnace_minecart");
         EGG = get("egg");
         COMPASS = get("compass");
         FISHING_ROD = (ItemFishingRod)get("fishing_rod");
         CLOCK = get("clock");
         GLOWSTONE_DUST = get("glowstone_dust");
         FISH = get("fish");
         COOKED_FISH = get("cooked_fish");
         DYE = get("dye");
         BONE = get("bone");
         SUGAR = get("sugar");
         CAKE = get("cake");
         BED = get("bed");
         REPEATER = get("repeater");
         COOKIE = get("cookie");
         FILLED_MAP = (ItemWorldMap)get("filled_map");
         SHEARS = (ItemShears)get("shears");
         MELON = get("melon");
         PUMPKIN_SEEDS = get("pumpkin_seeds");
         MELON_SEEDS = get("melon_seeds");
         BEEF = get("beef");
         COOKED_BEEF = get("cooked_beef");
         CHICKEN = get("chicken");
         COOKED_CHICKEN = get("cooked_chicken");
         MUTTON = get("mutton");
         COOKED_MUTTON = get("cooked_mutton");
         RABBIT = get("rabbit");
         COOKED_RABBIT = get("cooked_rabbit");
         RABBIT_STEW = get("rabbit_stew");
         RABBIT_FOOT = get("rabbit_foot");
         RABBIT_HIDE = get("rabbit_hide");
         ROTTEN_FLESH = get("rotten_flesh");
         ENDER_PEARL = get("ender_pearl");
         BLAZE_ROD = get("blaze_rod");
         GHAST_TEAR = get("ghast_tear");
         GOLD_NUGGET = get("gold_nugget");
         NETHER_WART = get("nether_wart");
         POTION = (ItemPotion)get("potion");
         GLASS_BOTTLE = get("glass_bottle");
         SPIDER_EYE = get("spider_eye");
         FERMENTED_SPIDER_EYE = get("fermented_spider_eye");
         BLAZE_POWDER = get("blaze_powder");
         MAGMA_CREAM = get("magma_cream");
         BREWING_STAND = get("brewing_stand");
         CAULDRON = get("cauldron");
         ENDER_EYE = get("ender_eye");
         SPECKLED_MELON = get("speckled_melon");
         SPAWN_EGG = get("spawn_egg");
         EXPERIENCE_BOTTLE = get("experience_bottle");
         FIRE_CHARGE = get("fire_charge");
         WRITABLE_BOOK = get("writable_book");
         WRITTEN_BOOK = get("written_book");
         EMERALD = get("emerald");
         ITEM_FRAME = get("item_frame");
         FLOWER_POT = get("flower_pot");
         CARROT = get("carrot");
         POTATO = get("potato");
         BAKED_POTATO = get("baked_potato");
         POISONOUS_POTATO = get("poisonous_potato");
         MAP = (ItemMapEmpty)get("map");
         GOLDEN_CARROT = get("golden_carrot");
         SKULL = get("skull");
         CARROT_ON_A_STICK = get("carrot_on_a_stick");
         NETHER_STAR = get("nether_star");
         PUMPKIN_PIE = get("pumpkin_pie");
         FIREWORKS = get("fireworks");
         FIREWORK_CHARGE = get("firework_charge");
         ENCHANTED_BOOK = (ItemEnchantedBook)get("enchanted_book");
         COMPARATOR = get("comparator");
         NETHERBRICK = get("netherbrick");
         QUARTZ = get("quartz");
         TNT_MINECART = get("tnt_minecart");
         HOPPER_MINECART = get("hopper_minecart");
         ARMOR_STAND = (ItemArmorStand)get("armor_stand");
         IRON_HORSE_ARMOR = get("iron_horse_armor");
         GOLDEN_HORSE_ARMOR = get("golden_horse_armor");
         DIAMOND_HORSE_ARMOR = get("diamond_horse_armor");
         LEAD = get("lead");
         NAME_TAG = get("name_tag");
         COMMAND_BLOCK_MINECART = get("command_block_minecart");
         RECORD_13 = get("record_13");
         RECORD_CAT = get("record_cat");
         RECORD_BLOCKS = get("record_blocks");
         RECORD_CHIRP = get("record_chirp");
         RECORD_FAR = get("record_far");
         RECORD_MALL = get("record_mall");
         RECORD_MELLOHI = get("record_mellohi");
         RECORD_STAL = get("record_stal");
         RECORD_STRAD = get("record_strad");
         RECORD_WARD = get("record_ward");
         RECORD_11 = get("record_11");
         RECORD_WAIT = get("record_wait");
         PRISMARINE_SHARD = get("prismarine_shard");
         PRISMARINE_CRYSTALS = get("prismarine_crystals");
         BANNER = get("banner");
      }
   }
}
