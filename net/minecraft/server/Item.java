package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCobbleWall;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockMonsterEggs;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockPrismarine;
import net.minecraft.server.BlockRedSandstone;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockSandStone;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.BlockStone;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityPainting;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumItemRarity;
import net.minecraft.server.ItemAnvil;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemArmorStand;
import net.minecraft.server.ItemAxe;
import net.minecraft.server.ItemBanner;
import net.minecraft.server.ItemBed;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemBoat;
import net.minecraft.server.ItemBook;
import net.minecraft.server.ItemBookAndQuill;
import net.minecraft.server.ItemBow;
import net.minecraft.server.ItemBucket;
import net.minecraft.server.ItemCarrotStick;
import net.minecraft.server.ItemCloth;
import net.minecraft.server.ItemCoal;
import net.minecraft.server.ItemDoor;
import net.minecraft.server.ItemDye;
import net.minecraft.server.ItemEgg;
import net.minecraft.server.ItemEnchantedBook;
import net.minecraft.server.ItemEnderEye;
import net.minecraft.server.ItemEnderPearl;
import net.minecraft.server.ItemExpBottle;
import net.minecraft.server.ItemFireball;
import net.minecraft.server.ItemFireworks;
import net.minecraft.server.ItemFireworksCharge;
import net.minecraft.server.ItemFish;
import net.minecraft.server.ItemFishingRod;
import net.minecraft.server.ItemFlintAndSteel;
import net.minecraft.server.ItemFood;
import net.minecraft.server.ItemGlassBottle;
import net.minecraft.server.ItemGoldenApple;
import net.minecraft.server.ItemHanging;
import net.minecraft.server.ItemHoe;
import net.minecraft.server.ItemLeash;
import net.minecraft.server.ItemLeaves;
import net.minecraft.server.ItemMapEmpty;
import net.minecraft.server.ItemMilkBucket;
import net.minecraft.server.ItemMinecart;
import net.minecraft.server.ItemMonsterEgg;
import net.minecraft.server.ItemMultiTexture;
import net.minecraft.server.ItemNameTag;
import net.minecraft.server.ItemNetherStar;
import net.minecraft.server.ItemPickaxe;
import net.minecraft.server.ItemPiston;
import net.minecraft.server.ItemPotion;
import net.minecraft.server.ItemRecord;
import net.minecraft.server.ItemRedstone;
import net.minecraft.server.ItemReed;
import net.minecraft.server.ItemSaddle;
import net.minecraft.server.ItemSeedFood;
import net.minecraft.server.ItemSeeds;
import net.minecraft.server.ItemShears;
import net.minecraft.server.ItemSign;
import net.minecraft.server.ItemSkull;
import net.minecraft.server.ItemSnow;
import net.minecraft.server.ItemSnowball;
import net.minecraft.server.ItemSoup;
import net.minecraft.server.ItemSpade;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemStep;
import net.minecraft.server.ItemSword;
import net.minecraft.server.ItemTallPlant;
import net.minecraft.server.ItemWaterLily;
import net.minecraft.server.ItemWithAuxData;
import net.minecraft.server.ItemWorldMap;
import net.minecraft.server.ItemWrittenBook;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PotionBrewer;
import net.minecraft.server.RegistryMaterials;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class Item {
   public static final RegistryMaterials<MinecraftKey, Item> REGISTRY = new RegistryMaterials();
   private static final Map<Block, Item> a = Maps.newHashMap();
   protected static final UUID f = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
   private CreativeModeTab b;
   protected static Random g = new Random();
   protected int maxStackSize = 64;
   private int durability;
   protected boolean i;
   protected boolean j;
   private Item craftingResult;
   private String k;
   private String name;

   public Item() {
   }

   public static int getId(Item var0) {
      return var0 == null?0:REGISTRY.b(var0);
   }

   public static Item getById(int var0) {
      return (Item)REGISTRY.a(var0);
   }

   public static Item getItemOf(Block var0) {
      return (Item)a.get(var0);
   }

   public static Item d(String var0) {
      Item var1 = (Item)REGISTRY.get(new MinecraftKey(var0));
      if(var1 == null) {
         try {
            return getById(Integer.parseInt(var0));
         } catch (NumberFormatException var3) {
            ;
         }
      }

      return var1;
   }

   public boolean a(NBTTagCompound var1) {
      return false;
   }

   public Item c(int var1) {
      this.maxStackSize = var1;
      return this;
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      return false;
   }

   public float getDestroySpeed(ItemStack var1, Block var2) {
      return 1.0F;
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      return var1;
   }

   public ItemStack b(ItemStack var1, World var2, EntityHuman var3) {
      return var1;
   }

   public int getMaxStackSize() {
      return this.maxStackSize;
   }

   public int filterData(int var1) {
      return 0;
   }

   public boolean k() {
      return this.j;
   }

   protected Item a(boolean var1) {
      this.j = var1;
      return this;
   }

   public int getMaxDurability() {
      return this.durability;
   }

   protected Item setMaxDurability(int var1) {
      this.durability = var1;
      return this;
   }

   public boolean usesDurability() {
      return this.durability > 0 && !this.j;
   }

   public boolean a(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      return false;
   }

   public boolean a(ItemStack var1, World var2, Block var3, BlockPosition var4, EntityLiving var5) {
      return false;
   }

   public boolean canDestroySpecialBlock(Block var1) {
      return false;
   }

   public boolean a(ItemStack var1, EntityHuman var2, EntityLiving var3) {
      return false;
   }

   public Item n() {
      this.i = true;
      return this;
   }

   public Item c(String var1) {
      this.name = var1;
      return this;
   }

   public String k(ItemStack var1) {
      String var2 = this.e_(var1);
      return var2 == null?"":LocaleI18n.get(var2);
   }

   public String getName() {
      return "item." + this.name;
   }

   public String e_(ItemStack var1) {
      return "item." + this.name;
   }

   public Item c(Item var1) {
      this.craftingResult = var1;
      return this;
   }

   public boolean p() {
      return true;
   }

   public Item q() {
      return this.craftingResult;
   }

   public boolean r() {
      return this.craftingResult != null;
   }

   public void a(ItemStack var1, World var2, Entity var3, int var4, boolean var5) {
   }

   public void d(ItemStack var1, World var2, EntityHuman var3) {
   }

   public boolean f() {
      return false;
   }

   public EnumAnimation e(ItemStack var1) {
      return EnumAnimation.NONE;
   }

   public int d(ItemStack var1) {
      return 0;
   }

   public void a(ItemStack var1, World var2, EntityHuman var3, int var4) {
   }

   protected Item e(String var1) {
      this.k = var1;
      return this;
   }

   public String j(ItemStack var1) {
      return this.k;
   }

   public boolean l(ItemStack var1) {
      return this.j(var1) != null;
   }

   public String a(ItemStack var1) {
      return ("" + LocaleI18n.get(this.k(var1) + ".name")).trim();
   }

   public EnumItemRarity g(ItemStack var1) {
      return var1.hasEnchantments()?EnumItemRarity.RARE:EnumItemRarity.COMMON;
   }

   public boolean f_(ItemStack var1) {
      return this.getMaxStackSize() == 1 && this.usesDurability();
   }

   protected MovingObjectPosition a(World var1, EntityHuman var2, boolean var3) {
      float var4 = var2.pitch;
      float var5 = var2.yaw;
      double var6 = var2.locX;
      double var8 = var2.locY + (double)var2.getHeadHeight();
      double var10 = var2.locZ;
      Vec3D var12 = new Vec3D(var6, var8, var10);
      float var13 = MathHelper.cos(-var5 * 0.017453292F - 3.1415927F);
      float var14 = MathHelper.sin(-var5 * 0.017453292F - 3.1415927F);
      float var15 = -MathHelper.cos(-var4 * 0.017453292F);
      float var16 = MathHelper.sin(-var4 * 0.017453292F);
      float var17 = var14 * var15;
      float var19 = var13 * var15;
      double var20 = 5.0D;
      Vec3D var22 = var12.add((double)var17 * var20, (double)var16 * var20, (double)var19 * var20);
      return var1.rayTrace(var12, var22, var3, !var3, false);
   }

   public int b() {
      return 0;
   }

   public Item a(CreativeModeTab var1) {
      this.b = var1;
      return this;
   }

   public boolean s() {
      return false;
   }

   public boolean a(ItemStack var1, ItemStack var2) {
      return false;
   }

   public Multimap<String, AttributeModifier> i() {
      return HashMultimap.create();
   }

   public static void t() {
      a((Block)Blocks.STONE, (Item)(new ItemMultiTexture(Blocks.STONE, Blocks.STONE, new Function() {
         public String a(ItemStack var1) {
            return BlockStone.EnumStoneVariant.a(var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("stone"));
      a((Block)Blocks.GRASS, (Item)(new ItemWithAuxData(Blocks.GRASS, false)));
      a((Block)Blocks.DIRT, (Item)(new ItemMultiTexture(Blocks.DIRT, Blocks.DIRT, new Function() {
         public String a(ItemStack var1) {
            return BlockDirt.EnumDirtVariant.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("dirt"));
      c(Blocks.COBBLESTONE);
      a((Block)Blocks.PLANKS, (Item)(new ItemMultiTexture(Blocks.PLANKS, Blocks.PLANKS, new Function() {
         public String a(ItemStack var1) {
            return BlockWood.EnumLogVariant.a(var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("wood"));
      a((Block)Blocks.SAPLING, (Item)(new ItemMultiTexture(Blocks.SAPLING, Blocks.SAPLING, new Function() {
         public String a(ItemStack var1) {
            return BlockWood.EnumLogVariant.a(var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("sapling"));
      c(Blocks.BEDROCK);
      a((Block)Blocks.SAND, (Item)(new ItemMultiTexture(Blocks.SAND, Blocks.SAND, new Function() {
         public String a(ItemStack var1) {
            return BlockSand.EnumSandVariant.a(var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("sand"));
      c(Blocks.GRAVEL);
      c(Blocks.GOLD_ORE);
      c(Blocks.IRON_ORE);
      c(Blocks.COAL_ORE);
      a((Block)Blocks.LOG, (Item)(new ItemMultiTexture(Blocks.LOG, Blocks.LOG, new Function() {
         public String a(ItemStack var1) {
            return BlockWood.EnumLogVariant.a(var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("log"));
      a((Block)Blocks.LOG2, (Item)(new ItemMultiTexture(Blocks.LOG2, Blocks.LOG2, new Function() {
         public String a(ItemStack var1) {
            return BlockWood.EnumLogVariant.a(var1.getData() + 4).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("log"));
      a((Block)Blocks.LEAVES, (Item)(new ItemLeaves(Blocks.LEAVES)).b("leaves"));
      a((Block)Blocks.LEAVES2, (Item)(new ItemLeaves(Blocks.LEAVES2)).b("leaves"));
      a((Block)Blocks.SPONGE, (Item)(new ItemMultiTexture(Blocks.SPONGE, Blocks.SPONGE, new Function() {
         public String a(ItemStack var1) {
            return (var1.getData() & 1) == 1?"wet":"dry";
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("sponge"));
      c(Blocks.GLASS);
      c(Blocks.LAPIS_ORE);
      c(Blocks.LAPIS_BLOCK);
      c(Blocks.DISPENSER);
      a((Block)Blocks.SANDSTONE, (Item)(new ItemMultiTexture(Blocks.SANDSTONE, Blocks.SANDSTONE, new Function() {
         public String a(ItemStack var1) {
            return BlockSandStone.EnumSandstoneVariant.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("sandStone"));
      c(Blocks.NOTEBLOCK);
      c(Blocks.GOLDEN_RAIL);
      c(Blocks.DETECTOR_RAIL);
      a((Block)Blocks.STICKY_PISTON, (Item)(new ItemPiston(Blocks.STICKY_PISTON)));
      c(Blocks.WEB);
      a((Block)Blocks.TALLGRASS, (Item)(new ItemWithAuxData(Blocks.TALLGRASS, true)).a(new String[]{"shrub", "grass", "fern"}));
      c((Block)Blocks.DEADBUSH);
      a((Block)Blocks.PISTON, (Item)(new ItemPiston(Blocks.PISTON)));
      a((Block)Blocks.WOOL, (Item)(new ItemCloth(Blocks.WOOL)).b("cloth"));
      a((Block)Blocks.YELLOW_FLOWER, (Item)(new ItemMultiTexture(Blocks.YELLOW_FLOWER, Blocks.YELLOW_FLOWER, new Function() {
         public String a(ItemStack var1) {
            return BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.YELLOW, var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("flower"));
      a((Block)Blocks.RED_FLOWER, (Item)(new ItemMultiTexture(Blocks.RED_FLOWER, Blocks.RED_FLOWER, new Function() {
         public String a(ItemStack var1) {
            return BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.RED, var1.getData()).d();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("rose"));
      c((Block)Blocks.BROWN_MUSHROOM);
      c((Block)Blocks.RED_MUSHROOM);
      c(Blocks.GOLD_BLOCK);
      c(Blocks.IRON_BLOCK);
      a((Block)Blocks.STONE_SLAB, (Item)(new ItemStep(Blocks.STONE_SLAB, Blocks.STONE_SLAB, Blocks.DOUBLE_STONE_SLAB)).b("stoneSlab"));
      c(Blocks.BRICK_BLOCK);
      c(Blocks.TNT);
      c(Blocks.BOOKSHELF);
      c(Blocks.MOSSY_COBBLESTONE);
      c(Blocks.OBSIDIAN);
      c(Blocks.TORCH);
      c(Blocks.MOB_SPAWNER);
      c(Blocks.OAK_STAIRS);
      c((Block)Blocks.CHEST);
      c(Blocks.DIAMOND_ORE);
      c(Blocks.DIAMOND_BLOCK);
      c(Blocks.CRAFTING_TABLE);
      c(Blocks.FARMLAND);
      c(Blocks.FURNACE);
      c(Blocks.LIT_FURNACE);
      c(Blocks.LADDER);
      c(Blocks.RAIL);
      c(Blocks.STONE_STAIRS);
      c(Blocks.LEVER);
      c(Blocks.STONE_PRESSURE_PLATE);
      c(Blocks.WOODEN_PRESSURE_PLATE);
      c(Blocks.REDSTONE_ORE);
      c(Blocks.REDSTONE_TORCH);
      c(Blocks.STONE_BUTTON);
      a((Block)Blocks.SNOW_LAYER, (Item)(new ItemSnow(Blocks.SNOW_LAYER)));
      c(Blocks.ICE);
      c(Blocks.SNOW);
      c((Block)Blocks.CACTUS);
      c(Blocks.CLAY);
      c(Blocks.JUKEBOX);
      c(Blocks.FENCE);
      c(Blocks.SPRUCE_FENCE);
      c(Blocks.BIRCH_FENCE);
      c(Blocks.JUNGLE_FENCE);
      c(Blocks.DARK_OAK_FENCE);
      c(Blocks.ACACIA_FENCE);
      c(Blocks.PUMPKIN);
      c(Blocks.NETHERRACK);
      c(Blocks.SOUL_SAND);
      c(Blocks.GLOWSTONE);
      c(Blocks.LIT_PUMPKIN);
      c(Blocks.TRAPDOOR);
      a((Block)Blocks.MONSTER_EGG, (Item)(new ItemMultiTexture(Blocks.MONSTER_EGG, Blocks.MONSTER_EGG, new Function() {
         public String a(ItemStack var1) {
            return BlockMonsterEggs.EnumMonsterEggVarient.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("monsterStoneEgg"));
      a((Block)Blocks.STONEBRICK, (Item)(new ItemMultiTexture(Blocks.STONEBRICK, Blocks.STONEBRICK, new Function() {
         public String a(ItemStack var1) {
            return BlockSmoothBrick.EnumStonebrickType.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("stonebricksmooth"));
      c(Blocks.BROWN_MUSHROOM_BLOCK);
      c(Blocks.RED_MUSHROOM_BLOCK);
      c(Blocks.IRON_BARS);
      c(Blocks.GLASS_PANE);
      c(Blocks.MELON_BLOCK);
      a((Block)Blocks.VINE, (Item)(new ItemWithAuxData(Blocks.VINE, false)));
      c(Blocks.FENCE_GATE);
      c(Blocks.SPRUCE_FENCE_GATE);
      c(Blocks.BIRCH_FENCE_GATE);
      c(Blocks.JUNGLE_FENCE_GATE);
      c(Blocks.DARK_OAK_FENCE_GATE);
      c(Blocks.ACACIA_FENCE_GATE);
      c(Blocks.BRICK_STAIRS);
      c(Blocks.STONE_BRICK_STAIRS);
      c((Block)Blocks.MYCELIUM);
      a((Block)Blocks.WATERLILY, (Item)(new ItemWaterLily(Blocks.WATERLILY)));
      c(Blocks.NETHER_BRICK);
      c(Blocks.NETHER_BRICK_FENCE);
      c(Blocks.NETHER_BRICK_STAIRS);
      c(Blocks.ENCHANTING_TABLE);
      c(Blocks.END_PORTAL_FRAME);
      c(Blocks.END_STONE);
      c(Blocks.DRAGON_EGG);
      c(Blocks.REDSTONE_LAMP);
      a((Block)Blocks.WOODEN_SLAB, (Item)(new ItemStep(Blocks.WOODEN_SLAB, Blocks.WOODEN_SLAB, Blocks.DOUBLE_WOODEN_SLAB)).b("woodSlab"));
      c(Blocks.SANDSTONE_STAIRS);
      c(Blocks.EMERALD_ORE);
      c(Blocks.ENDER_CHEST);
      c((Block)Blocks.TRIPWIRE_HOOK);
      c(Blocks.EMERALD_BLOCK);
      c(Blocks.SPRUCE_STAIRS);
      c(Blocks.BIRCH_STAIRS);
      c(Blocks.JUNGLE_STAIRS);
      c(Blocks.COMMAND_BLOCK);
      c((Block)Blocks.BEACON);
      a((Block)Blocks.COBBLESTONE_WALL, (Item)(new ItemMultiTexture(Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE_WALL, new Function() {
         public String a(ItemStack var1) {
            return BlockCobbleWall.EnumCobbleVariant.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("cobbleWall"));
      c(Blocks.WOODEN_BUTTON);
      a((Block)Blocks.ANVIL, (Item)(new ItemAnvil(Blocks.ANVIL)).b("anvil"));
      c(Blocks.TRAPPED_CHEST);
      c(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
      c(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
      c((Block)Blocks.DAYLIGHT_DETECTOR);
      c(Blocks.REDSTONE_BLOCK);
      c(Blocks.QUARTZ_ORE);
      c((Block)Blocks.HOPPER);
      a((Block)Blocks.QUARTZ_BLOCK, (Item)(new ItemMultiTexture(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, new String[]{"default", "chiseled", "lines"})).b("quartzBlock"));
      c(Blocks.QUARTZ_STAIRS);
      c(Blocks.ACTIVATOR_RAIL);
      c(Blocks.DROPPER);
      a((Block)Blocks.STAINED_HARDENED_CLAY, (Item)(new ItemCloth(Blocks.STAINED_HARDENED_CLAY)).b("clayHardenedStained"));
      c(Blocks.BARRIER);
      c(Blocks.IRON_TRAPDOOR);
      c(Blocks.HAY_BLOCK);
      a((Block)Blocks.CARPET, (Item)(new ItemCloth(Blocks.CARPET)).b("woolCarpet"));
      c(Blocks.HARDENED_CLAY);
      c(Blocks.COAL_BLOCK);
      c(Blocks.PACKED_ICE);
      c(Blocks.ACACIA_STAIRS);
      c(Blocks.DARK_OAK_STAIRS);
      c(Blocks.SLIME);
      a((Block)Blocks.DOUBLE_PLANT, (Item)(new ItemTallPlant(Blocks.DOUBLE_PLANT, Blocks.DOUBLE_PLANT, new Function() {
         public String a(ItemStack var1) {
            return BlockTallPlant.EnumTallFlowerVariants.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("doublePlant"));
      a((Block)Blocks.STAINED_GLASS, (Item)(new ItemCloth(Blocks.STAINED_GLASS)).b("stainedGlass"));
      a((Block)Blocks.STAINED_GLASS_PANE, (Item)(new ItemCloth(Blocks.STAINED_GLASS_PANE)).b("stainedGlassPane"));
      a((Block)Blocks.PRISMARINE, (Item)(new ItemMultiTexture(Blocks.PRISMARINE, Blocks.PRISMARINE, new Function() {
         public String a(ItemStack var1) {
            return BlockPrismarine.EnumPrismarineVariant.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("prismarine"));
      c(Blocks.SEA_LANTERN);
      a((Block)Blocks.RED_SANDSTONE, (Item)(new ItemMultiTexture(Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE, new Function() {
         public String a(ItemStack var1) {
            return BlockRedSandstone.EnumRedSandstoneVariant.a(var1.getData()).c();
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      })).b("redSandStone"));
      c(Blocks.RED_SANDSTONE_STAIRS);
      a((Block)Blocks.STONE_SLAB2, (Item)(new ItemStep(Blocks.STONE_SLAB2, Blocks.STONE_SLAB2, Blocks.DOUBLE_STONE_SLAB2)).b("stoneSlab2"));
      a(256, (String)"iron_shovel", (new ItemSpade(Item.EnumToolMaterial.IRON)).c("shovelIron"));
      a(257, (String)"iron_pickaxe", (new ItemPickaxe(Item.EnumToolMaterial.IRON)).c("pickaxeIron"));
      a(258, (String)"iron_axe", (new ItemAxe(Item.EnumToolMaterial.IRON)).c("hatchetIron"));
      a(259, (String)"flint_and_steel", (new ItemFlintAndSteel()).c("flintAndSteel"));
      a(260, (String)"apple", (new ItemFood(4, 0.3F, false)).c("apple"));
      a(261, (String)"bow", (new ItemBow()).c("bow"));
      a(262, (String)"arrow", (new Item()).c("arrow").a(CreativeModeTab.j));
      a(263, (String)"coal", (new ItemCoal()).c("coal"));
      a(264, (String)"diamond", (new Item()).c("diamond").a(CreativeModeTab.l));
      a(265, (String)"iron_ingot", (new Item()).c("ingotIron").a(CreativeModeTab.l));
      a(266, (String)"gold_ingot", (new Item()).c("ingotGold").a(CreativeModeTab.l));
      a(267, (String)"iron_sword", (new ItemSword(Item.EnumToolMaterial.IRON)).c("swordIron"));
      a(268, (String)"wooden_sword", (new ItemSword(Item.EnumToolMaterial.WOOD)).c("swordWood"));
      a(269, (String)"wooden_shovel", (new ItemSpade(Item.EnumToolMaterial.WOOD)).c("shovelWood"));
      a(270, (String)"wooden_pickaxe", (new ItemPickaxe(Item.EnumToolMaterial.WOOD)).c("pickaxeWood"));
      a(271, (String)"wooden_axe", (new ItemAxe(Item.EnumToolMaterial.WOOD)).c("hatchetWood"));
      a(272, (String)"stone_sword", (new ItemSword(Item.EnumToolMaterial.STONE)).c("swordStone"));
      a(273, (String)"stone_shovel", (new ItemSpade(Item.EnumToolMaterial.STONE)).c("shovelStone"));
      a(274, (String)"stone_pickaxe", (new ItemPickaxe(Item.EnumToolMaterial.STONE)).c("pickaxeStone"));
      a(275, (String)"stone_axe", (new ItemAxe(Item.EnumToolMaterial.STONE)).c("hatchetStone"));
      a(276, (String)"diamond_sword", (new ItemSword(Item.EnumToolMaterial.EMERALD)).c("swordDiamond"));
      a(277, (String)"diamond_shovel", (new ItemSpade(Item.EnumToolMaterial.EMERALD)).c("shovelDiamond"));
      a(278, (String)"diamond_pickaxe", (new ItemPickaxe(Item.EnumToolMaterial.EMERALD)).c("pickaxeDiamond"));
      a(279, (String)"diamond_axe", (new ItemAxe(Item.EnumToolMaterial.EMERALD)).c("hatchetDiamond"));
      a(280, (String)"stick", (new Item()).n().c("stick").a(CreativeModeTab.l));
      a(281, (String)"bowl", (new Item()).c("bowl").a(CreativeModeTab.l));
      a(282, (String)"mushroom_stew", (new ItemSoup(6)).c("mushroomStew"));
      a(283, (String)"golden_sword", (new ItemSword(Item.EnumToolMaterial.GOLD)).c("swordGold"));
      a(284, (String)"golden_shovel", (new ItemSpade(Item.EnumToolMaterial.GOLD)).c("shovelGold"));
      a(285, (String)"golden_pickaxe", (new ItemPickaxe(Item.EnumToolMaterial.GOLD)).c("pickaxeGold"));
      a(286, (String)"golden_axe", (new ItemAxe(Item.EnumToolMaterial.GOLD)).c("hatchetGold"));
      a(287, (String)"string", (new ItemReed(Blocks.TRIPWIRE)).c("string").a(CreativeModeTab.l));
      a(288, (String)"feather", (new Item()).c("feather").a(CreativeModeTab.l));
      a(289, (String)"gunpowder", (new Item()).c("sulphur").e(PotionBrewer.k).a(CreativeModeTab.l));
      a(290, (String)"wooden_hoe", (new ItemHoe(Item.EnumToolMaterial.WOOD)).c("hoeWood"));
      a(291, (String)"stone_hoe", (new ItemHoe(Item.EnumToolMaterial.STONE)).c("hoeStone"));
      a(292, (String)"iron_hoe", (new ItemHoe(Item.EnumToolMaterial.IRON)).c("hoeIron"));
      a(293, (String)"diamond_hoe", (new ItemHoe(Item.EnumToolMaterial.EMERALD)).c("hoeDiamond"));
      a(294, (String)"golden_hoe", (new ItemHoe(Item.EnumToolMaterial.GOLD)).c("hoeGold"));
      a(295, (String)"wheat_seeds", (new ItemSeeds(Blocks.WHEAT, Blocks.FARMLAND)).c("seeds"));
      a(296, (String)"wheat", (new Item()).c("wheat").a(CreativeModeTab.l));
      a(297, (String)"bread", (new ItemFood(5, 0.6F, false)).c("bread"));
      a(298, (String)"leather_helmet", (new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 0)).c("helmetCloth"));
      a(299, (String)"leather_chestplate", (new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 1)).c("chestplateCloth"));
      a(300, (String)"leather_leggings", (new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 2)).c("leggingsCloth"));
      a(301, (String)"leather_boots", (new ItemArmor(ItemArmor.EnumArmorMaterial.LEATHER, 0, 3)).c("bootsCloth"));
      a(302, (String)"chainmail_helmet", (new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 0)).c("helmetChain"));
      a(303, (String)"chainmail_chestplate", (new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 1)).c("chestplateChain"));
      a(304, (String)"chainmail_leggings", (new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 2)).c("leggingsChain"));
      a(305, (String)"chainmail_boots", (new ItemArmor(ItemArmor.EnumArmorMaterial.CHAIN, 1, 3)).c("bootsChain"));
      a(306, (String)"iron_helmet", (new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 0)).c("helmetIron"));
      a(307, (String)"iron_chestplate", (new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 1)).c("chestplateIron"));
      a(308, (String)"iron_leggings", (new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 2)).c("leggingsIron"));
      a(309, (String)"iron_boots", (new ItemArmor(ItemArmor.EnumArmorMaterial.IRON, 2, 3)).c("bootsIron"));
      a(310, (String)"diamond_helmet", (new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 0)).c("helmetDiamond"));
      a(311, (String)"diamond_chestplate", (new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 1)).c("chestplateDiamond"));
      a(312, (String)"diamond_leggings", (new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 2)).c("leggingsDiamond"));
      a(313, (String)"diamond_boots", (new ItemArmor(ItemArmor.EnumArmorMaterial.DIAMOND, 3, 3)).c("bootsDiamond"));
      a(314, (String)"golden_helmet", (new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 0)).c("helmetGold"));
      a(315, (String)"golden_chestplate", (new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 1)).c("chestplateGold"));
      a(316, (String)"golden_leggings", (new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 2)).c("leggingsGold"));
      a(317, (String)"golden_boots", (new ItemArmor(ItemArmor.EnumArmorMaterial.GOLD, 4, 3)).c("bootsGold"));
      a(318, (String)"flint", (new Item()).c("flint").a(CreativeModeTab.l));
      a(319, (String)"porkchop", (new ItemFood(3, 0.3F, true)).c("porkchopRaw"));
      a(320, (String)"cooked_porkchop", (new ItemFood(8, 0.8F, true)).c("porkchopCooked"));
      a(321, (String)"painting", (new ItemHanging(EntityPainting.class)).c("painting"));
      a(322, (String)"golden_apple", (new ItemGoldenApple(4, 1.2F, false)).h().a(MobEffectList.REGENERATION.id, 5, 1, 1.0F).c("appleGold"));
      a(323, (String)"sign", (new ItemSign()).c("sign"));
      a(324, (String)"wooden_door", (new ItemDoor(Blocks.WOODEN_DOOR)).c("doorOak"));
      Item var0 = (new ItemBucket(Blocks.AIR)).c("bucket").c(16);
      a(325, (String)"bucket", var0);
      a(326, (String)"water_bucket", (new ItemBucket(Blocks.FLOWING_WATER)).c("bucketWater").c(var0));
      a(327, (String)"lava_bucket", (new ItemBucket(Blocks.FLOWING_LAVA)).c("bucketLava").c(var0));
      a(328, (String)"minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.RIDEABLE)).c("minecart"));
      a(329, (String)"saddle", (new ItemSaddle()).c("saddle"));
      a(330, (String)"iron_door", (new ItemDoor(Blocks.IRON_DOOR)).c("doorIron"));
      a(331, (String)"redstone", (new ItemRedstone()).c("redstone").e(PotionBrewer.i));
      a(332, (String)"snowball", (new ItemSnowball()).c("snowball"));
      a(333, (String)"boat", (new ItemBoat()).c("boat"));
      a(334, (String)"leather", (new Item()).c("leather").a(CreativeModeTab.l));
      a(335, (String)"milk_bucket", (new ItemMilkBucket()).c("milk").c(var0));
      a(336, (String)"brick", (new Item()).c("brick").a(CreativeModeTab.l));
      a(337, (String)"clay_ball", (new Item()).c("clay").a(CreativeModeTab.l));
      a(338, (String)"reeds", (new ItemReed(Blocks.REEDS)).c("reeds").a(CreativeModeTab.l));
      a(339, (String)"paper", (new Item()).c("paper").a(CreativeModeTab.f));
      a(340, (String)"book", (new ItemBook()).c("book").a(CreativeModeTab.f));
      a(341, (String)"slime_ball", (new Item()).c("slimeball").a(CreativeModeTab.f));
      a(342, (String)"chest_minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.CHEST)).c("minecartChest"));
      a(343, (String)"furnace_minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.FURNACE)).c("minecartFurnace"));
      a(344, (String)"egg", (new ItemEgg()).c("egg"));
      a(345, (String)"compass", (new Item()).c("compass").a(CreativeModeTab.i));
      a(346, (String)"fishing_rod", (new ItemFishingRod()).c("fishingRod"));
      a(347, (String)"clock", (new Item()).c("clock").a(CreativeModeTab.i));
      a(348, (String)"glowstone_dust", (new Item()).c("yellowDust").e(PotionBrewer.j).a(CreativeModeTab.l));
      a(349, (String)"fish", (new ItemFish(false)).c("fish").a(true));
      a(350, (String)"cooked_fish", (new ItemFish(true)).c("fish").a(true));
      a(351, (String)"dye", (new ItemDye()).c("dyePowder"));
      a(352, (String)"bone", (new Item()).c("bone").n().a(CreativeModeTab.f));
      a(353, (String)"sugar", (new Item()).c("sugar").e(PotionBrewer.b).a(CreativeModeTab.l));
      a(354, (String)"cake", (new ItemReed(Blocks.CAKE)).c(1).c("cake").a(CreativeModeTab.h));
      a(355, (String)"bed", (new ItemBed()).c(1).c("bed"));
      a(356, (String)"repeater", (new ItemReed(Blocks.UNPOWERED_REPEATER)).c("diode").a(CreativeModeTab.d));
      a(357, (String)"cookie", (new ItemFood(2, 0.1F, false)).c("cookie"));
      a(358, (String)"filled_map", (new ItemWorldMap()).c("map"));
      a(359, (String)"shears", (new ItemShears()).c("shears"));
      a(360, (String)"melon", (new ItemFood(2, 0.3F, false)).c("melon"));
      a(361, (String)"pumpkin_seeds", (new ItemSeeds(Blocks.PUMPKIN_STEM, Blocks.FARMLAND)).c("seeds_pumpkin"));
      a(362, (String)"melon_seeds", (new ItemSeeds(Blocks.MELON_STEM, Blocks.FARMLAND)).c("seeds_melon"));
      a(363, (String)"beef", (new ItemFood(3, 0.3F, true)).c("beefRaw"));
      a(364, (String)"cooked_beef", (new ItemFood(8, 0.8F, true)).c("beefCooked"));
      a(365, (String)"chicken", (new ItemFood(2, 0.3F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.3F).c("chickenRaw"));
      a(366, (String)"cooked_chicken", (new ItemFood(6, 0.6F, true)).c("chickenCooked"));
      a(367, (String)"rotten_flesh", (new ItemFood(4, 0.1F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.8F).c("rottenFlesh"));
      a(368, (String)"ender_pearl", (new ItemEnderPearl()).c("enderPearl"));
      a(369, (String)"blaze_rod", (new Item()).c("blazeRod").a(CreativeModeTab.l).n());
      a(370, (String)"ghast_tear", (new Item()).c("ghastTear").e(PotionBrewer.c).a(CreativeModeTab.k));
      a(371, (String)"gold_nugget", (new Item()).c("goldNugget").a(CreativeModeTab.l));
      a(372, (String)"nether_wart", (new ItemSeeds(Blocks.NETHER_WART, Blocks.SOUL_SAND)).c("netherStalkSeeds").e("+4"));
      a(373, (String)"potion", (new ItemPotion()).c("potion"));
      a(374, (String)"glass_bottle", (new ItemGlassBottle()).c("glassBottle"));
      a(375, (String)"spider_eye", (new ItemFood(2, 0.8F, false)).a(MobEffectList.POISON.id, 5, 0, 1.0F).c("spiderEye").e(PotionBrewer.d));
      a(376, (String)"fermented_spider_eye", (new Item()).c("fermentedSpiderEye").e(PotionBrewer.e).a(CreativeModeTab.k));
      a(377, (String)"blaze_powder", (new Item()).c("blazePowder").e(PotionBrewer.g).a(CreativeModeTab.k));
      a(378, (String)"magma_cream", (new Item()).c("magmaCream").e(PotionBrewer.h).a(CreativeModeTab.k));
      a(379, (String)"brewing_stand", (new ItemReed(Blocks.BREWING_STAND)).c("brewingStand").a(CreativeModeTab.k));
      a(380, (String)"cauldron", (new ItemReed(Blocks.cauldron)).c("cauldron").a(CreativeModeTab.k));
      a(381, (String)"ender_eye", (new ItemEnderEye()).c("eyeOfEnder"));
      a(382, (String)"speckled_melon", (new Item()).c("speckledMelon").e(PotionBrewer.f).a(CreativeModeTab.k));
      a(383, (String)"spawn_egg", (new ItemMonsterEgg()).c("monsterPlacer"));
      a(384, (String)"experience_bottle", (new ItemExpBottle()).c("expBottle"));
      a(385, (String)"fire_charge", (new ItemFireball()).c("fireball"));
      a(386, (String)"writable_book", (new ItemBookAndQuill()).c("writingBook").a(CreativeModeTab.f));
      a(387, (String)"written_book", (new ItemWrittenBook()).c("writtenBook").c(16));
      a(388, (String)"emerald", (new Item()).c("emerald").a(CreativeModeTab.l));
      a(389, (String)"item_frame", (new ItemHanging(EntityItemFrame.class)).c("frame"));
      a(390, (String)"flower_pot", (new ItemReed(Blocks.FLOWER_POT)).c("flowerPot").a(CreativeModeTab.c));
      a(391, (String)"carrot", (new ItemSeedFood(3, 0.6F, Blocks.CARROTS, Blocks.FARMLAND)).c("carrots"));
      a(392, (String)"potato", (new ItemSeedFood(1, 0.3F, Blocks.POTATOES, Blocks.FARMLAND)).c("potato"));
      a(393, (String)"baked_potato", (new ItemFood(5, 0.6F, false)).c("potatoBaked"));
      a(394, (String)"poisonous_potato", (new ItemFood(2, 0.3F, false)).a(MobEffectList.POISON.id, 5, 0, 0.6F).c("potatoPoisonous"));
      a(395, (String)"map", (new ItemMapEmpty()).c("emptyMap"));
      a(396, (String)"golden_carrot", (new ItemFood(6, 1.2F, false)).c("carrotGolden").e(PotionBrewer.l).a(CreativeModeTab.k));
      a(397, (String)"skull", (new ItemSkull()).c("skull"));
      a(398, (String)"carrot_on_a_stick", (new ItemCarrotStick()).c("carrotOnAStick"));
      a(399, (String)"nether_star", (new ItemNetherStar()).c("netherStar").a(CreativeModeTab.l));
      a(400, (String)"pumpkin_pie", (new ItemFood(8, 0.3F, false)).c("pumpkinPie").a(CreativeModeTab.h));
      a(401, (String)"fireworks", (new ItemFireworks()).c("fireworks"));
      a(402, (String)"firework_charge", (new ItemFireworksCharge()).c("fireworksCharge").a(CreativeModeTab.f));
      a(403, (String)"enchanted_book", (new ItemEnchantedBook()).c(1).c("enchantedBook"));
      a(404, (String)"comparator", (new ItemReed(Blocks.UNPOWERED_COMPARATOR)).c("comparator").a(CreativeModeTab.d));
      a(405, (String)"netherbrick", (new Item()).c("netherbrick").a(CreativeModeTab.l));
      a(406, (String)"quartz", (new Item()).c("netherquartz").a(CreativeModeTab.l));
      a(407, (String)"tnt_minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.TNT)).c("minecartTnt"));
      a(408, (String)"hopper_minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.HOPPER)).c("minecartHopper"));
      a(409, (String)"prismarine_shard", (new Item()).c("prismarineShard").a(CreativeModeTab.l));
      a(410, (String)"prismarine_crystals", (new Item()).c("prismarineCrystals").a(CreativeModeTab.l));
      a(411, (String)"rabbit", (new ItemFood(3, 0.3F, true)).c("rabbitRaw"));
      a(412, (String)"cooked_rabbit", (new ItemFood(5, 0.6F, true)).c("rabbitCooked"));
      a(413, (String)"rabbit_stew", (new ItemSoup(10)).c("rabbitStew"));
      a(414, (String)"rabbit_foot", (new Item()).c("rabbitFoot").e(PotionBrewer.n).a(CreativeModeTab.k));
      a(415, (String)"rabbit_hide", (new Item()).c("rabbitHide").a(CreativeModeTab.l));
      a(416, (String)"armor_stand", (new ItemArmorStand()).c("armorStand").c(16));
      a(417, (String)"iron_horse_armor", (new Item()).c("horsearmormetal").c(1).a(CreativeModeTab.f));
      a(418, (String)"golden_horse_armor", (new Item()).c("horsearmorgold").c(1).a(CreativeModeTab.f));
      a(419, (String)"diamond_horse_armor", (new Item()).c("horsearmordiamond").c(1).a(CreativeModeTab.f));
      a(420, (String)"lead", (new ItemLeash()).c("leash"));
      a(421, (String)"name_tag", (new ItemNameTag()).c("nameTag"));
      a(422, (String)"command_block_minecart", (new ItemMinecart(EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK)).c("minecartCommandBlock").a((CreativeModeTab)null));
      a(423, (String)"mutton", (new ItemFood(2, 0.3F, true)).c("muttonRaw"));
      a(424, (String)"cooked_mutton", (new ItemFood(6, 0.8F, true)).c("muttonCooked"));
      a(425, (String)"banner", (new ItemBanner()).b("banner"));
      a(427, (String)"spruce_door", (new ItemDoor(Blocks.SPRUCE_DOOR)).c("doorSpruce"));
      a(428, (String)"birch_door", (new ItemDoor(Blocks.BIRCH_DOOR)).c("doorBirch"));
      a(429, (String)"jungle_door", (new ItemDoor(Blocks.JUNGLE_DOOR)).c("doorJungle"));
      a(430, (String)"acacia_door", (new ItemDoor(Blocks.ACACIA_DOOR)).c("doorAcacia"));
      a(431, (String)"dark_oak_door", (new ItemDoor(Blocks.DARK_OAK_DOOR)).c("doorDarkOak"));
      a(2256, (String)"record_13", (new ItemRecord("13")).c("record"));
      a(2257, (String)"record_cat", (new ItemRecord("cat")).c("record"));
      a(2258, (String)"record_blocks", (new ItemRecord("blocks")).c("record"));
      a(2259, (String)"record_chirp", (new ItemRecord("chirp")).c("record"));
      a(2260, (String)"record_far", (new ItemRecord("far")).c("record"));
      a(2261, (String)"record_mall", (new ItemRecord("mall")).c("record"));
      a(2262, (String)"record_mellohi", (new ItemRecord("mellohi")).c("record"));
      a(2263, (String)"record_stal", (new ItemRecord("stal")).c("record"));
      a(2264, (String)"record_strad", (new ItemRecord("strad")).c("record"));
      a(2265, (String)"record_ward", (new ItemRecord("ward")).c("record"));
      a(2266, (String)"record_11", (new ItemRecord("11")).c("record"));
      a(2267, (String)"record_wait", (new ItemRecord("wait")).c("record"));
   }

   private static void c(Block var0) {
      a((Block)var0, (Item)(new ItemBlock(var0)));
   }

   protected static void a(Block var0, Item var1) {
      a(Block.getId(var0), (MinecraftKey)Block.REGISTRY.c(var0), var1);
      a.put(var0, var1);
   }

   private static void a(int var0, String var1, Item var2) {
      a(var0, new MinecraftKey(var1), var2);
   }

   private static void a(int var0, MinecraftKey var1, Item var2) {
      REGISTRY.a(var0, var1, var2);
   }

   public static enum EnumToolMaterial {
      WOOD(0, 59, 2.0F, 0.0F, 15),
      STONE(1, 131, 4.0F, 1.0F, 5),
      IRON(2, 250, 6.0F, 2.0F, 14),
      EMERALD(3, 1561, 8.0F, 3.0F, 10),
      GOLD(0, 32, 12.0F, 0.0F, 22);

      private final int f;
      private final int g;
      private final float h;
      private final float i;
      private final int j;

      private EnumToolMaterial(int var3, int var4, float var5, float var6, int var7) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
         this.i = var6;
         this.j = var7;
      }

      public int a() {
         return this.g;
      }

      public float b() {
         return this.h;
      }

      public float c() {
         return this.i;
      }

      public int d() {
         return this.f;
      }

      public int e() {
         return this.j;
      }

      public Item f() {
         return this == WOOD?Item.getItemOf(Blocks.PLANKS):(this == STONE?Item.getItemOf(Blocks.COBBLESTONE):(this == GOLD?Items.GOLD_INGOT:(this == IRON?Items.IRON_INGOT:(this == EMERALD?Items.DIAMOND:null))));
      }
   }
}
