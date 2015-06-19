package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockBeacon;
import net.minecraft.server.BlockCactus;
import net.minecraft.server.BlockCauldron;
import net.minecraft.server.BlockChest;
import net.minecraft.server.BlockDaylightDetector;
import net.minecraft.server.BlockDeadBush;
import net.minecraft.server.BlockFire;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockFlowing;
import net.minecraft.server.BlockGrass;
import net.minecraft.server.BlockHopper;
import net.minecraft.server.BlockLeaves;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockMycel;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPistonExtension;
import net.minecraft.server.BlockPistonMoving;
import net.minecraft.server.BlockPlant;
import net.minecraft.server.BlockPortal;
import net.minecraft.server.BlockRedstoneComparator;
import net.minecraft.server.BlockRedstoneWire;
import net.minecraft.server.BlockReed;
import net.minecraft.server.BlockRepeater;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockSkull;
import net.minecraft.server.BlockStainedGlass;
import net.minecraft.server.BlockStainedGlassPane;
import net.minecraft.server.BlockStationary;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.BlockTripwireHook;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.MinecraftKey;

public class Blocks {
   public static final Block AIR;
   public static final Block STONE;
   public static final BlockGrass GRASS;
   public static final Block DIRT;
   public static final Block COBBLESTONE;
   public static final Block PLANKS;
   public static final Block SAPLING;
   public static final Block BEDROCK;
   public static final BlockFlowing FLOWING_WATER;
   public static final BlockStationary WATER;
   public static final BlockFlowing FLOWING_LAVA;
   public static final BlockStationary LAVA;
   public static final BlockSand SAND;
   public static final Block GRAVEL;
   public static final Block GOLD_ORE;
   public static final Block IRON_ORE;
   public static final Block COAL_ORE;
   public static final Block LOG;
   public static final Block LOG2;
   public static final BlockLeaves LEAVES;
   public static final BlockLeaves LEAVES2;
   public static final Block SPONGE;
   public static final Block GLASS;
   public static final Block LAPIS_ORE;
   public static final Block LAPIS_BLOCK;
   public static final Block DISPENSER;
   public static final Block SANDSTONE;
   public static final Block NOTEBLOCK;
   public static final Block BED;
   public static final Block GOLDEN_RAIL;
   public static final Block DETECTOR_RAIL;
   public static final BlockPiston STICKY_PISTON;
   public static final Block WEB;
   public static final BlockLongGrass TALLGRASS;
   public static final BlockDeadBush DEADBUSH;
   public static final BlockPiston PISTON;
   public static final BlockPistonExtension PISTON_HEAD;
   public static final Block WOOL;
   public static final BlockPistonMoving PISTON_EXTENSION;
   public static final BlockFlowers YELLOW_FLOWER;
   public static final BlockFlowers RED_FLOWER;
   public static final BlockPlant BROWN_MUSHROOM;
   public static final BlockPlant RED_MUSHROOM;
   public static final Block GOLD_BLOCK;
   public static final Block IRON_BLOCK;
   public static final BlockStepAbstract DOUBLE_STONE_SLAB;
   public static final BlockStepAbstract STONE_SLAB;
   public static final Block BRICK_BLOCK;
   public static final Block TNT;
   public static final Block BOOKSHELF;
   public static final Block MOSSY_COBBLESTONE;
   public static final Block OBSIDIAN;
   public static final Block TORCH;
   public static final BlockFire FIRE;
   public static final Block MOB_SPAWNER;
   public static final Block OAK_STAIRS;
   public static final BlockChest CHEST;
   public static final BlockRedstoneWire REDSTONE_WIRE;
   public static final Block DIAMOND_ORE;
   public static final Block DIAMOND_BLOCK;
   public static final Block CRAFTING_TABLE;
   public static final Block WHEAT;
   public static final Block FARMLAND;
   public static final Block FURNACE;
   public static final Block LIT_FURNACE;
   public static final Block STANDING_SIGN;
   public static final Block WOODEN_DOOR;
   public static final Block SPRUCE_DOOR;
   public static final Block BIRCH_DOOR;
   public static final Block JUNGLE_DOOR;
   public static final Block ACACIA_DOOR;
   public static final Block DARK_OAK_DOOR;
   public static final Block LADDER;
   public static final Block RAIL;
   public static final Block STONE_STAIRS;
   public static final Block WALL_SIGN;
   public static final Block LEVER;
   public static final Block STONE_PRESSURE_PLATE;
   public static final Block IRON_DOOR;
   public static final Block WOODEN_PRESSURE_PLATE;
   public static final Block REDSTONE_ORE;
   public static final Block LIT_REDSTONE_ORE;
   public static final Block UNLIT_REDSTONE_TORCH;
   public static final Block REDSTONE_TORCH;
   public static final Block STONE_BUTTON;
   public static final Block SNOW_LAYER;
   public static final Block ICE;
   public static final Block SNOW;
   public static final BlockCactus CACTUS;
   public static final Block CLAY;
   public static final BlockReed REEDS;
   public static final Block JUKEBOX;
   public static final Block FENCE;
   public static final Block SPRUCE_FENCE;
   public static final Block BIRCH_FENCE;
   public static final Block JUNGLE_FENCE;
   public static final Block DARK_OAK_FENCE;
   public static final Block ACACIA_FENCE;
   public static final Block PUMPKIN;
   public static final Block NETHERRACK;
   public static final Block SOUL_SAND;
   public static final Block GLOWSTONE;
   public static final BlockPortal PORTAL;
   public static final Block LIT_PUMPKIN;
   public static final Block CAKE;
   public static final BlockRepeater UNPOWERED_REPEATER;
   public static final BlockRepeater POWERED_REPEATER;
   public static final Block TRAPDOOR;
   public static final Block MONSTER_EGG;
   public static final Block STONEBRICK;
   public static final Block BROWN_MUSHROOM_BLOCK;
   public static final Block RED_MUSHROOM_BLOCK;
   public static final Block IRON_BARS;
   public static final Block GLASS_PANE;
   public static final Block MELON_BLOCK;
   public static final Block PUMPKIN_STEM;
   public static final Block MELON_STEM;
   public static final Block VINE;
   public static final Block FENCE_GATE;
   public static final Block SPRUCE_FENCE_GATE;
   public static final Block BIRCH_FENCE_GATE;
   public static final Block JUNGLE_FENCE_GATE;
   public static final Block DARK_OAK_FENCE_GATE;
   public static final Block ACACIA_FENCE_GATE;
   public static final Block BRICK_STAIRS;
   public static final Block STONE_BRICK_STAIRS;
   public static final BlockMycel MYCELIUM;
   public static final Block WATERLILY;
   public static final Block NETHER_BRICK;
   public static final Block NETHER_BRICK_FENCE;
   public static final Block NETHER_BRICK_STAIRS;
   public static final Block NETHER_WART;
   public static final Block ENCHANTING_TABLE;
   public static final Block BREWING_STAND;
   public static final BlockCauldron cauldron;
   public static final Block END_PORTAL;
   public static final Block END_PORTAL_FRAME;
   public static final Block END_STONE;
   public static final Block DRAGON_EGG;
   public static final Block REDSTONE_LAMP;
   public static final Block LIT_REDSTONE_LAMP;
   public static final BlockStepAbstract DOUBLE_WOODEN_SLAB;
   public static final BlockStepAbstract WOODEN_SLAB;
   public static final Block COCOA;
   public static final Block SANDSTONE_STAIRS;
   public static final Block EMERALD_ORE;
   public static final Block ENDER_CHEST;
   public static final BlockTripwireHook TRIPWIRE_HOOK;
   public static final Block TRIPWIRE;
   public static final Block EMERALD_BLOCK;
   public static final Block SPRUCE_STAIRS;
   public static final Block BIRCH_STAIRS;
   public static final Block JUNGLE_STAIRS;
   public static final Block COMMAND_BLOCK;
   public static final BlockBeacon BEACON;
   public static final Block COBBLESTONE_WALL;
   public static final Block FLOWER_POT;
   public static final Block CARROTS;
   public static final Block POTATOES;
   public static final Block WOODEN_BUTTON;
   public static final BlockSkull SKULL;
   public static final Block ANVIL;
   public static final Block TRAPPED_CHEST;
   public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE;
   public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE;
   public static final BlockRedstoneComparator UNPOWERED_COMPARATOR;
   public static final BlockRedstoneComparator POWERED_COMPARATOR;
   public static final BlockDaylightDetector DAYLIGHT_DETECTOR;
   public static final BlockDaylightDetector DAYLIGHT_DETECTOR_INVERTED;
   public static final Block REDSTONE_BLOCK;
   public static final Block QUARTZ_ORE;
   public static final BlockHopper HOPPER;
   public static final Block QUARTZ_BLOCK;
   public static final Block QUARTZ_STAIRS;
   public static final Block ACTIVATOR_RAIL;
   public static final Block DROPPER;
   public static final Block STAINED_HARDENED_CLAY;
   public static final Block BARRIER;
   public static final Block IRON_TRAPDOOR;
   public static final Block HAY_BLOCK;
   public static final Block CARPET;
   public static final Block HARDENED_CLAY;
   public static final Block COAL_BLOCK;
   public static final Block PACKED_ICE;
   public static final Block ACACIA_STAIRS;
   public static final Block DARK_OAK_STAIRS;
   public static final Block SLIME;
   public static final BlockTallPlant DOUBLE_PLANT;
   public static final BlockStainedGlass STAINED_GLASS;
   public static final BlockStainedGlassPane STAINED_GLASS_PANE;
   public static final Block PRISMARINE;
   public static final Block SEA_LANTERN;
   public static final Block STANDING_BANNER;
   public static final Block WALL_BANNER;
   public static final Block RED_SANDSTONE;
   public static final Block RED_SANDSTONE_STAIRS;
   public static final BlockStepAbstract DOUBLE_STONE_SLAB2;
   public static final BlockStepAbstract STONE_SLAB2;

   private static Block get(String var0) {
      return (Block)Block.REGISTRY.get(new MinecraftKey(var0));
   }

   static {
      if(!DispenserRegistry.a()) {
         throw new RuntimeException("Accessed Blocks before Bootstrap!");
      } else {
         AIR = get("air");
         STONE = get("stone");
         GRASS = (BlockGrass)get("grass");
         DIRT = get("dirt");
         COBBLESTONE = get("cobblestone");
         PLANKS = get("planks");
         SAPLING = get("sapling");
         BEDROCK = get("bedrock");
         FLOWING_WATER = (BlockFlowing)get("flowing_water");
         WATER = (BlockStationary)get("water");
         FLOWING_LAVA = (BlockFlowing)get("flowing_lava");
         LAVA = (BlockStationary)get("lava");
         SAND = (BlockSand)get("sand");
         GRAVEL = get("gravel");
         GOLD_ORE = get("gold_ore");
         IRON_ORE = get("iron_ore");
         COAL_ORE = get("coal_ore");
         LOG = get("log");
         LOG2 = get("log2");
         LEAVES = (BlockLeaves)get("leaves");
         LEAVES2 = (BlockLeaves)get("leaves2");
         SPONGE = get("sponge");
         GLASS = get("glass");
         LAPIS_ORE = get("lapis_ore");
         LAPIS_BLOCK = get("lapis_block");
         DISPENSER = get("dispenser");
         SANDSTONE = get("sandstone");
         NOTEBLOCK = get("noteblock");
         BED = get("bed");
         GOLDEN_RAIL = get("golden_rail");
         DETECTOR_RAIL = get("detector_rail");
         STICKY_PISTON = (BlockPiston)get("sticky_piston");
         WEB = get("web");
         TALLGRASS = (BlockLongGrass)get("tallgrass");
         DEADBUSH = (BlockDeadBush)get("deadbush");
         PISTON = (BlockPiston)get("piston");
         PISTON_HEAD = (BlockPistonExtension)get("piston_head");
         WOOL = get("wool");
         PISTON_EXTENSION = (BlockPistonMoving)get("piston_extension");
         YELLOW_FLOWER = (BlockFlowers)get("yellow_flower");
         RED_FLOWER = (BlockFlowers)get("red_flower");
         BROWN_MUSHROOM = (BlockPlant)get("brown_mushroom");
         RED_MUSHROOM = (BlockPlant)get("red_mushroom");
         GOLD_BLOCK = get("gold_block");
         IRON_BLOCK = get("iron_block");
         DOUBLE_STONE_SLAB = (BlockStepAbstract)get("double_stone_slab");
         STONE_SLAB = (BlockStepAbstract)get("stone_slab");
         BRICK_BLOCK = get("brick_block");
         TNT = get("tnt");
         BOOKSHELF = get("bookshelf");
         MOSSY_COBBLESTONE = get("mossy_cobblestone");
         OBSIDIAN = get("obsidian");
         TORCH = get("torch");
         FIRE = (BlockFire)get("fire");
         MOB_SPAWNER = get("mob_spawner");
         OAK_STAIRS = get("oak_stairs");
         CHEST = (BlockChest)get("chest");
         REDSTONE_WIRE = (BlockRedstoneWire)get("redstone_wire");
         DIAMOND_ORE = get("diamond_ore");
         DIAMOND_BLOCK = get("diamond_block");
         CRAFTING_TABLE = get("crafting_table");
         WHEAT = get("wheat");
         FARMLAND = get("farmland");
         FURNACE = get("furnace");
         LIT_FURNACE = get("lit_furnace");
         STANDING_SIGN = get("standing_sign");
         WOODEN_DOOR = get("wooden_door");
         SPRUCE_DOOR = get("spruce_door");
         BIRCH_DOOR = get("birch_door");
         JUNGLE_DOOR = get("jungle_door");
         ACACIA_DOOR = get("acacia_door");
         DARK_OAK_DOOR = get("dark_oak_door");
         LADDER = get("ladder");
         RAIL = get("rail");
         STONE_STAIRS = get("stone_stairs");
         WALL_SIGN = get("wall_sign");
         LEVER = get("lever");
         STONE_PRESSURE_PLATE = get("stone_pressure_plate");
         IRON_DOOR = get("iron_door");
         WOODEN_PRESSURE_PLATE = get("wooden_pressure_plate");
         REDSTONE_ORE = get("redstone_ore");
         LIT_REDSTONE_ORE = get("lit_redstone_ore");
         UNLIT_REDSTONE_TORCH = get("unlit_redstone_torch");
         REDSTONE_TORCH = get("redstone_torch");
         STONE_BUTTON = get("stone_button");
         SNOW_LAYER = get("snow_layer");
         ICE = get("ice");
         SNOW = get("snow");
         CACTUS = (BlockCactus)get("cactus");
         CLAY = get("clay");
         REEDS = (BlockReed)get("reeds");
         JUKEBOX = get("jukebox");
         FENCE = get("fence");
         SPRUCE_FENCE = get("spruce_fence");
         BIRCH_FENCE = get("birch_fence");
         JUNGLE_FENCE = get("jungle_fence");
         DARK_OAK_FENCE = get("dark_oak_fence");
         ACACIA_FENCE = get("acacia_fence");
         PUMPKIN = get("pumpkin");
         NETHERRACK = get("netherrack");
         SOUL_SAND = get("soul_sand");
         GLOWSTONE = get("glowstone");
         PORTAL = (BlockPortal)get("portal");
         LIT_PUMPKIN = get("lit_pumpkin");
         CAKE = get("cake");
         UNPOWERED_REPEATER = (BlockRepeater)get("unpowered_repeater");
         POWERED_REPEATER = (BlockRepeater)get("powered_repeater");
         TRAPDOOR = get("trapdoor");
         MONSTER_EGG = get("monster_egg");
         STONEBRICK = get("stonebrick");
         BROWN_MUSHROOM_BLOCK = get("brown_mushroom_block");
         RED_MUSHROOM_BLOCK = get("red_mushroom_block");
         IRON_BARS = get("iron_bars");
         GLASS_PANE = get("glass_pane");
         MELON_BLOCK = get("melon_block");
         PUMPKIN_STEM = get("pumpkin_stem");
         MELON_STEM = get("melon_stem");
         VINE = get("vine");
         FENCE_GATE = get("fence_gate");
         SPRUCE_FENCE_GATE = get("spruce_fence_gate");
         BIRCH_FENCE_GATE = get("birch_fence_gate");
         JUNGLE_FENCE_GATE = get("jungle_fence_gate");
         DARK_OAK_FENCE_GATE = get("dark_oak_fence_gate");
         ACACIA_FENCE_GATE = get("acacia_fence_gate");
         BRICK_STAIRS = get("brick_stairs");
         STONE_BRICK_STAIRS = get("stone_brick_stairs");
         MYCELIUM = (BlockMycel)get("mycelium");
         WATERLILY = get("waterlily");
         NETHER_BRICK = get("nether_brick");
         NETHER_BRICK_FENCE = get("nether_brick_fence");
         NETHER_BRICK_STAIRS = get("nether_brick_stairs");
         NETHER_WART = get("nether_wart");
         ENCHANTING_TABLE = get("enchanting_table");
         BREWING_STAND = get("brewing_stand");
         cauldron = (BlockCauldron)get("cauldron");
         END_PORTAL = get("end_portal");
         END_PORTAL_FRAME = get("end_portal_frame");
         END_STONE = get("end_stone");
         DRAGON_EGG = get("dragon_egg");
         REDSTONE_LAMP = get("redstone_lamp");
         LIT_REDSTONE_LAMP = get("lit_redstone_lamp");
         DOUBLE_WOODEN_SLAB = (BlockStepAbstract)get("double_wooden_slab");
         WOODEN_SLAB = (BlockStepAbstract)get("wooden_slab");
         COCOA = get("cocoa");
         SANDSTONE_STAIRS = get("sandstone_stairs");
         EMERALD_ORE = get("emerald_ore");
         ENDER_CHEST = get("ender_chest");
         TRIPWIRE_HOOK = (BlockTripwireHook)get("tripwire_hook");
         TRIPWIRE = get("tripwire");
         EMERALD_BLOCK = get("emerald_block");
         SPRUCE_STAIRS = get("spruce_stairs");
         BIRCH_STAIRS = get("birch_stairs");
         JUNGLE_STAIRS = get("jungle_stairs");
         COMMAND_BLOCK = get("command_block");
         BEACON = (BlockBeacon)get("beacon");
         COBBLESTONE_WALL = get("cobblestone_wall");
         FLOWER_POT = get("flower_pot");
         CARROTS = get("carrots");
         POTATOES = get("potatoes");
         WOODEN_BUTTON = get("wooden_button");
         SKULL = (BlockSkull)get("skull");
         ANVIL = get("anvil");
         TRAPPED_CHEST = get("trapped_chest");
         LIGHT_WEIGHTED_PRESSURE_PLATE = get("light_weighted_pressure_plate");
         HEAVY_WEIGHTED_PRESSURE_PLATE = get("heavy_weighted_pressure_plate");
         UNPOWERED_COMPARATOR = (BlockRedstoneComparator)get("unpowered_comparator");
         POWERED_COMPARATOR = (BlockRedstoneComparator)get("powered_comparator");
         DAYLIGHT_DETECTOR = (BlockDaylightDetector)get("daylight_detector");
         DAYLIGHT_DETECTOR_INVERTED = (BlockDaylightDetector)get("daylight_detector_inverted");
         REDSTONE_BLOCK = get("redstone_block");
         QUARTZ_ORE = get("quartz_ore");
         HOPPER = (BlockHopper)get("hopper");
         QUARTZ_BLOCK = get("quartz_block");
         QUARTZ_STAIRS = get("quartz_stairs");
         ACTIVATOR_RAIL = get("activator_rail");
         DROPPER = get("dropper");
         STAINED_HARDENED_CLAY = get("stained_hardened_clay");
         BARRIER = get("barrier");
         IRON_TRAPDOOR = get("iron_trapdoor");
         HAY_BLOCK = get("hay_block");
         CARPET = get("carpet");
         HARDENED_CLAY = get("hardened_clay");
         COAL_BLOCK = get("coal_block");
         PACKED_ICE = get("packed_ice");
         ACACIA_STAIRS = get("acacia_stairs");
         DARK_OAK_STAIRS = get("dark_oak_stairs");
         SLIME = get("slime");
         DOUBLE_PLANT = (BlockTallPlant)get("double_plant");
         STAINED_GLASS = (BlockStainedGlass)get("stained_glass");
         STAINED_GLASS_PANE = (BlockStainedGlassPane)get("stained_glass_pane");
         PRISMARINE = get("prismarine");
         SEA_LANTERN = get("sea_lantern");
         STANDING_BANNER = get("standing_banner");
         WALL_BANNER = get("wall_banner");
         RED_SANDSTONE = get("red_sandstone");
         RED_SANDSTONE_STAIRS = get("red_sandstone_stairs");
         DOUBLE_STONE_SLAB2 = (BlockStepAbstract)get("double_stone_slab2");
         STONE_SLAB2 = (BlockStepAbstract)get("stone_slab2");
      }
   }
}
