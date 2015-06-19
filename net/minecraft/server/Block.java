package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockAir;
import net.minecraft.server.BlockAnvil;
import net.minecraft.server.BlockBanner;
import net.minecraft.server.BlockBarrier;
import net.minecraft.server.BlockBeacon;
import net.minecraft.server.BlockBed;
import net.minecraft.server.BlockBloodStone;
import net.minecraft.server.BlockBookshelf;
import net.minecraft.server.BlockBrewingStand;
import net.minecraft.server.BlockCactus;
import net.minecraft.server.BlockCake;
import net.minecraft.server.BlockCarpet;
import net.minecraft.server.BlockCarrots;
import net.minecraft.server.BlockCauldron;
import net.minecraft.server.BlockChest;
import net.minecraft.server.BlockClay;
import net.minecraft.server.BlockCloth;
import net.minecraft.server.BlockCobbleWall;
import net.minecraft.server.BlockCocoa;
import net.minecraft.server.BlockCommand;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockDaylightDetector;
import net.minecraft.server.BlockDeadBush;
import net.minecraft.server.BlockDirt;
import net.minecraft.server.BlockDispenser;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.BlockDoubleStep;
import net.minecraft.server.BlockDoubleStoneStep2;
import net.minecraft.server.BlockDoubleWoodStep;
import net.minecraft.server.BlockDragonEgg;
import net.minecraft.server.BlockDropper;
import net.minecraft.server.BlockEnchantmentTable;
import net.minecraft.server.BlockEnderChest;
import net.minecraft.server.BlockEnderPortal;
import net.minecraft.server.BlockEnderPortalFrame;
import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockFenceGate;
import net.minecraft.server.BlockFire;
import net.minecraft.server.BlockFloorSign;
import net.minecraft.server.BlockFlowerPot;
import net.minecraft.server.BlockFlowing;
import net.minecraft.server.BlockFurnace;
import net.minecraft.server.BlockGlass;
import net.minecraft.server.BlockGrass;
import net.minecraft.server.BlockGravel;
import net.minecraft.server.BlockHardenedClay;
import net.minecraft.server.BlockHay;
import net.minecraft.server.BlockHopper;
import net.minecraft.server.BlockHugeMushroom;
import net.minecraft.server.BlockIce;
import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.BlockLadder;
import net.minecraft.server.BlockLeaves1;
import net.minecraft.server.BlockLeaves2;
import net.minecraft.server.BlockLever;
import net.minecraft.server.BlockLightStone;
import net.minecraft.server.BlockLog1;
import net.minecraft.server.BlockLog2;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockMelon;
import net.minecraft.server.BlockMinecartDetector;
import net.minecraft.server.BlockMinecartTrack;
import net.minecraft.server.BlockMobSpawner;
import net.minecraft.server.BlockMonsterEggs;
import net.minecraft.server.BlockMushroom;
import net.minecraft.server.BlockMycel;
import net.minecraft.server.BlockNetherWart;
import net.minecraft.server.BlockNetherbrick;
import net.minecraft.server.BlockNote;
import net.minecraft.server.BlockObsidian;
import net.minecraft.server.BlockOre;
import net.minecraft.server.BlockPackedIce;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPistonExtension;
import net.minecraft.server.BlockPistonMoving;
import net.minecraft.server.BlockPortal;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockPotatoes;
import net.minecraft.server.BlockPowered;
import net.minecraft.server.BlockPoweredRail;
import net.minecraft.server.BlockPressurePlateBinary;
import net.minecraft.server.BlockPressurePlateWeighted;
import net.minecraft.server.BlockPrismarine;
import net.minecraft.server.BlockPumpkin;
import net.minecraft.server.BlockQuartz;
import net.minecraft.server.BlockRedFlowers;
import net.minecraft.server.BlockRedSandstone;
import net.minecraft.server.BlockRedstoneComparator;
import net.minecraft.server.BlockRedstoneLamp;
import net.minecraft.server.BlockRedstoneOre;
import net.minecraft.server.BlockRedstoneTorch;
import net.minecraft.server.BlockRedstoneWire;
import net.minecraft.server.BlockReed;
import net.minecraft.server.BlockRepeater;
import net.minecraft.server.BlockSand;
import net.minecraft.server.BlockSandStone;
import net.minecraft.server.BlockSapling;
import net.minecraft.server.BlockSeaLantern;
import net.minecraft.server.BlockSkull;
import net.minecraft.server.BlockSlime;
import net.minecraft.server.BlockSlowSand;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.BlockSnow;
import net.minecraft.server.BlockSnowBlock;
import net.minecraft.server.BlockSoil;
import net.minecraft.server.BlockSponge;
import net.minecraft.server.BlockStainedGlass;
import net.minecraft.server.BlockStainedGlassPane;
import net.minecraft.server.BlockStairs;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStationary;
import net.minecraft.server.BlockStem;
import net.minecraft.server.BlockStep;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.BlockStone;
import net.minecraft.server.BlockStoneButton;
import net.minecraft.server.BlockStoneStep2;
import net.minecraft.server.BlockTNT;
import net.minecraft.server.BlockTallPlant;
import net.minecraft.server.BlockThin;
import net.minecraft.server.BlockTorch;
import net.minecraft.server.BlockTrapdoor;
import net.minecraft.server.BlockTripwire;
import net.minecraft.server.BlockTripwireHook;
import net.minecraft.server.BlockVine;
import net.minecraft.server.BlockWallSign;
import net.minecraft.server.BlockWaterLily;
import net.minecraft.server.BlockWeb;
import net.minecraft.server.BlockWood;
import net.minecraft.server.BlockWoodButton;
import net.minecraft.server.BlockWoodStep;
import net.minecraft.server.BlockWorkbench;
import net.minecraft.server.BlockYellowFlowers;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.RegistryBlocks;
import net.minecraft.server.RegistryID;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class Block {
   private static final MinecraftKey a = new MinecraftKey("air");
   public static final RegistryBlocks<MinecraftKey, Block> REGISTRY;
   public static final RegistryID<IBlockData> d;
   private CreativeModeTab creativeTab;
   public static final Block.StepSound e;
   public static final Block.StepSound f;
   public static final Block.StepSound g;
   public static final Block.StepSound h;
   public static final Block.StepSound i;
   public static final Block.StepSound j;
   public static final Block.StepSound k;
   public static final Block.StepSound l;
   public static final Block.StepSound m;
   public static final Block.StepSound n;
   public static final Block.StepSound o;
   public static final Block.StepSound p;
   public static final Block.StepSound q;
   protected boolean r;
   protected int s;
   protected boolean t;
   protected int u;
   protected boolean v;
   protected float strength;
   protected float durability;
   protected boolean y;
   protected boolean z;
   protected boolean isTileEntity;
   protected double minX;
   protected double minY;
   protected double minZ;
   protected double maxX;
   protected double maxY;
   protected double maxZ;
   public Block.StepSound stepSound;
   public float I;
   protected final Material material;
   protected final MaterialMapColor K;
   public float frictionFactor;
   protected final BlockStateList blockStateList;
   private IBlockData blockData;
   private String name;

   public static int getId(Block var0) {
      return REGISTRY.b(var0);
   }

   public static int getCombinedId(IBlockData var0) {
      Block var1 = var0.getBlock();
      return getId(var1) + (var1.toLegacyData(var0) << 12);
   }

   public static Block getById(int var0) {
      return (Block)REGISTRY.a(var0);
   }

   public static IBlockData getByCombinedId(int var0) {
      int var1 = var0 & 4095;
      int var2 = var0 >> 12 & 15;
      return getById(var1).fromLegacyData(var2);
   }

   public static Block asBlock(Item var0) {
      return var0 instanceof ItemBlock?((ItemBlock)var0).d():null;
   }

   public static Block getByName(String var0) {
      MinecraftKey var1 = new MinecraftKey(var0);
      if(REGISTRY.d(var1)) {
         return (Block)REGISTRY.get(var1);
      } else {
         try {
            return (Block)REGISTRY.a(Integer.parseInt(var0));
         } catch (NumberFormatException var3) {
            return null;
         }
      }
   }

   public boolean o() {
      return this.r;
   }

   public int p() {
      return this.s;
   }

   public int r() {
      return this.u;
   }

   public boolean s() {
      return this.v;
   }

   public Material getMaterial() {
      return this.material;
   }

   public MaterialMapColor g(IBlockData var1) {
      return this.K;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData();
   }

   public int toLegacyData(IBlockData var1) {
      if(var1 != null && !var1.a().isEmpty()) {
         throw new IllegalArgumentException("Don\'t know how to convert " + var1 + " back into data...");
      } else {
         return 0;
      }
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1;
   }

   public Block(Material var1, MaterialMapColor var2) {
      this.y = true;
      this.stepSound = e;
      this.I = 1.0F;
      this.frictionFactor = 0.6F;
      this.material = var1;
      this.K = var2;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      this.r = this.c();
      this.s = this.c()?255:0;
      this.t = !var1.blocksLight();
      this.blockStateList = this.getStateList();
      this.j(this.blockStateList.getBlockData());
   }

   protected Block(Material var1) {
      this(var1, var1.r());
   }

   protected Block a(Block.StepSound var1) {
      this.stepSound = var1;
      return this;
   }

   protected Block e(int var1) {
      this.s = var1;
      return this;
   }

   protected Block a(float var1) {
      this.u = (int)(15.0F * var1);
      return this;
   }

   protected Block b(float var1) {
      this.durability = var1 * 3.0F;
      return this;
   }

   public boolean u() {
      return this.material.isSolid() && this.d();
   }

   public boolean isOccluding() {
      return this.material.k() && this.d() && !this.isPowerSource();
   }

   public boolean w() {
      return this.material.isSolid() && this.d();
   }

   public boolean d() {
      return true;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return !this.material.isSolid();
   }

   public int b() {
      return 3;
   }

   public boolean a(World var1, BlockPosition var2) {
      return false;
   }

   protected Block c(float var1) {
      this.strength = var1;
      if(this.durability < var1 * 5.0F) {
         this.durability = var1 * 5.0F;
      }

      return this;
   }

   protected Block x() {
      this.c(-1.0F);
      return this;
   }

   public float g(World var1, BlockPosition var2) {
      return this.strength;
   }

   protected Block a(boolean var1) {
      this.z = var1;
      return this;
   }

   public boolean isTicking() {
      return this.z;
   }

   public boolean isTileEntity() {
      return this.isTileEntity;
   }

   protected final void a(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.minX = (double)var1;
      this.minY = (double)var2;
      this.minZ = (double)var3;
      this.maxX = (double)var4;
      this.maxY = (double)var5;
      this.maxZ = (double)var6;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2, EnumDirection var3) {
      return var1.getType(var2).getBlock().getMaterial().isBuildable();
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      AxisAlignedBB var7 = this.a(var1, var2, var3);
      if(var7 != null && var4.b(var7)) {
         var5.add(var7);
      }

   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return new AxisAlignedBB((double)var2.getX() + this.minX, (double)var2.getY() + this.minY, (double)var2.getZ() + this.minZ, (double)var2.getX() + this.maxX, (double)var2.getY() + this.maxY, (double)var2.getZ() + this.maxZ);
   }

   public boolean c() {
      return true;
   }

   public boolean a(IBlockData var1, boolean var2) {
      return this.A();
   }

   public boolean A() {
      return true;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      this.b(var1, var2, var3, var4);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
   }

   public void postBreak(World var1, BlockPosition var2, IBlockData var3) {
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
   }

   public int a(World var1) {
      return 10;
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
   }

   public int a(Random var1) {
      return 1;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(this);
   }

   public float getDamage(EntityHuman var1, World var2, BlockPosition var3) {
      float var4 = this.g(var2, var3);
      return var4 < 0.0F?0.0F:(!var1.b(this)?var1.a(this) / var4 / 100.0F:var1.a(this) / var4 / 30.0F);
   }

   public final void b(World var1, BlockPosition var2, IBlockData var3, int var4) {
      this.dropNaturally(var1, var2, var3, 1.0F, var4);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(!var1.isClientSide) {
         int var6 = this.getDropCount(var5, var1.random);

         for(int var7 = 0; var7 < var6; ++var7) {
            if(var1.random.nextFloat() <= var4) {
               Item var8 = this.getDropType(var3, var1.random, var5);
               if(var8 != null) {
                  a(var1, var2, new ItemStack(var8, 1, this.getDropData(var3)));
               }
            }
         }

      }
   }

   public static void a(World var0, BlockPosition var1, ItemStack var2) {
      if(!var0.isClientSide && var0.getGameRules().getBoolean("doTileDrops")) {
         float var3 = 0.5F;
         double var4 = (double)(var0.random.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
         double var6 = (double)(var0.random.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
         double var8 = (double)(var0.random.nextFloat() * var3) + (double)(1.0F - var3) * 0.5D;
         EntityItem var10 = new EntityItem(var0, (double)var1.getX() + var4, (double)var1.getY() + var6, (double)var1.getZ() + var8, var2);
         var10.p();
         var0.addEntity(var10);
      }
   }

   protected void dropExperience(World var1, BlockPosition var2, int var3) {
      if(!var1.isClientSide) {
         while(var3 > 0) {
            int var4 = EntityExperienceOrb.getOrbValue(var3);
            var3 -= var4;
            var1.addEntity(new EntityExperienceOrb(var1, (double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, var4));
         }
      }

   }

   public int getDropData(IBlockData var1) {
      return 0;
   }

   public float a(Entity var1) {
      return this.durability / 5.0F;
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      this.updateShape(var1, var2);
      var3 = var3.add((double)(-var2.getX()), (double)(-var2.getY()), (double)(-var2.getZ()));
      var4 = var4.add((double)(-var2.getX()), (double)(-var2.getY()), (double)(-var2.getZ()));
      Vec3D var5 = var3.a(var4, this.minX);
      Vec3D var6 = var3.a(var4, this.maxX);
      Vec3D var7 = var3.b(var4, this.minY);
      Vec3D var8 = var3.b(var4, this.maxY);
      Vec3D var9 = var3.c(var4, this.minZ);
      Vec3D var10 = var3.c(var4, this.maxZ);
      if(!this.a(var5)) {
         var5 = null;
      }

      if(!this.a(var6)) {
         var6 = null;
      }

      if(!this.b(var7)) {
         var7 = null;
      }

      if(!this.b(var8)) {
         var8 = null;
      }

      if(!this.c(var9)) {
         var9 = null;
      }

      if(!this.c(var10)) {
         var10 = null;
      }

      Vec3D var11 = null;
      if(var5 != null && (var11 == null || var3.distanceSquared(var5) < var3.distanceSquared(var11))) {
         var11 = var5;
      }

      if(var6 != null && (var11 == null || var3.distanceSquared(var6) < var3.distanceSquared(var11))) {
         var11 = var6;
      }

      if(var7 != null && (var11 == null || var3.distanceSquared(var7) < var3.distanceSquared(var11))) {
         var11 = var7;
      }

      if(var8 != null && (var11 == null || var3.distanceSquared(var8) < var3.distanceSquared(var11))) {
         var11 = var8;
      }

      if(var9 != null && (var11 == null || var3.distanceSquared(var9) < var3.distanceSquared(var11))) {
         var11 = var9;
      }

      if(var10 != null && (var11 == null || var3.distanceSquared(var10) < var3.distanceSquared(var11))) {
         var11 = var10;
      }

      if(var11 == null) {
         return null;
      } else {
         EnumDirection var12 = null;
         if(var11 == var5) {
            var12 = EnumDirection.WEST;
         }

         if(var11 == var6) {
            var12 = EnumDirection.EAST;
         }

         if(var11 == var7) {
            var12 = EnumDirection.DOWN;
         }

         if(var11 == var8) {
            var12 = EnumDirection.UP;
         }

         if(var11 == var9) {
            var12 = EnumDirection.NORTH;
         }

         if(var11 == var10) {
            var12 = EnumDirection.SOUTH;
         }

         return new MovingObjectPosition(var11.add((double)var2.getX(), (double)var2.getY(), (double)var2.getZ()), var12, var2);
      }
   }

   private boolean a(Vec3D var1) {
      return var1 == null?false:var1.b >= this.minY && var1.b <= this.maxY && var1.c >= this.minZ && var1.c <= this.maxZ;
   }

   private boolean b(Vec3D var1) {
      return var1 == null?false:var1.a >= this.minX && var1.a <= this.maxX && var1.c >= this.minZ && var1.c <= this.maxZ;
   }

   private boolean c(Vec3D var1) {
      return var1 == null?false:var1.a >= this.minX && var1.a <= this.maxX && var1.b >= this.minY && var1.b <= this.maxY;
   }

   public void wasExploded(World var1, BlockPosition var2, Explosion var3) {
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3, ItemStack var4) {
      return this.canPlace(var1, var2, var3);
   }

   public boolean canPlace(World var1, BlockPosition var2, EnumDirection var3) {
      return this.canPlace(var1, var2);
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return var1.getType(var2).getBlock().material.isReplaceable();
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      return false;
   }

   public void a(World var1, BlockPosition var2, Entity var3) {
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.fromLegacyData(var7);
   }

   public void attack(World var1, BlockPosition var2, EntityHuman var3) {
   }

   public Vec3D a(World var1, BlockPosition var2, Entity var3, Vec3D var4) {
      return var4;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
   }

   public final double B() {
      return this.minX;
   }

   public final double C() {
      return this.maxX;
   }

   public final double D() {
      return this.minY;
   }

   public final double E() {
      return this.maxY;
   }

   public final double F() {
      return this.minZ;
   }

   public final double G() {
      return this.maxZ;
   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return 0;
   }

   public boolean isPowerSource() {
      return false;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
   }

   public int b(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return 0;
   }

   public void j() {
   }

   public void a(World var1, EntityHuman var2, BlockPosition var3, IBlockData var4, TileEntity var5) {
      var2.b(StatisticList.MINE_BLOCK_COUNT[getId(this)]);
      var2.applyExhaustion(0.025F);
      if(this.I() && EnchantmentManager.hasSilkTouchEnchantment(var2)) {
         ItemStack var7 = this.i(var4);
         if(var7 != null) {
            a(var1, var3, var7);
         }
      } else {
         int var6 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(var2);
         this.b(var1, var3, var4, var6);
      }

   }

   protected boolean I() {
      return this.d() && !this.isTileEntity;
   }

   protected ItemStack i(IBlockData var1) {
      int var2 = 0;
      Item var3 = Item.getItemOf(this);
      if(var3 != null && var3.k()) {
         var2 = this.toLegacyData(var1);
      }

      return new ItemStack(var3, 1, var2);
   }

   public int getDropCount(int var1, Random var2) {
      return this.a(var2);
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
   }

   public boolean g() {
      return !this.material.isBuildable() && !this.material.isLiquid();
   }

   public Block c(String var1) {
      this.name = var1;
      return this;
   }

   public String getName() {
      return LocaleI18n.get(this.a() + ".name");
   }

   public String a() {
      return "tile." + this.name;
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, int var4, int var5) {
      return false;
   }

   public boolean J() {
      return this.y;
   }

   protected Block K() {
      this.y = false;
      return this;
   }

   public int k() {
      return this.material.getPushReaction();
   }

   public void a(World var1, BlockPosition var2, Entity var3, float var4) {
      var3.e(var4, 1.0F);
   }

   public void a(World var1, Entity var2) {
      var2.motY = 0.0D;
   }

   public int getDropData(World var1, BlockPosition var2) {
      return this.getDropData(var1.getType(var2));
   }

   public Block a(CreativeModeTab var1) {
      this.creativeTab = var1;
      return this;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
   }

   public void k(World var1, BlockPosition var2) {
   }

   public boolean N() {
      return true;
   }

   public boolean a(Explosion var1) {
      return true;
   }

   public boolean b(Block var1) {
      return this == var1;
   }

   public static boolean a(Block var0, Block var1) {
      return var0 != null && var1 != null?(var0 == var1?true:var0.b(var1)):false;
   }

   public boolean isComplexRedstone() {
      return false;
   }

   public int l(World var1, BlockPosition var2) {
      return 0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[0]);
   }

   public BlockStateList P() {
      return this.blockStateList;
   }

   protected final void j(IBlockData var1) {
      this.blockData = var1;
   }

   public final IBlockData getBlockData() {
      return this.blockData;
   }

   public String toString() {
      return "Block{" + REGISTRY.c(this) + "}";
   }

   public static void S() {
      a(0, (MinecraftKey)a, (new BlockAir()).c("air"));
      a(1, (String)"stone", (new BlockStone()).c(1.5F).b(10.0F).a(i).c("stone"));
      a(2, (String)"grass", (new BlockGrass()).c(0.6F).a(h).c("grass"));
      a(3, (String)"dirt", (new BlockDirt()).c(0.5F).a(g).c("dirt"));
      Block var0 = (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("stonebrick").a(CreativeModeTab.b);
      a(4, (String)"cobblestone", var0);
      Block var1 = (new BlockWood()).c(2.0F).b(5.0F).a(f).c("wood");
      a(5, (String)"planks", var1);
      a(6, (String)"sapling", (new BlockSapling()).c(0.0F).a(h).c("sapling"));
      a(7, (String)"bedrock", (new Block(Material.STONE)).x().b(6000000.0F).a(i).c("bedrock").K().a(CreativeModeTab.b));
      a(8, (String)"flowing_water", (new BlockFlowing(Material.WATER)).c(100.0F).e(3).c("water").K());
      a(9, (String)"water", (new BlockStationary(Material.WATER)).c(100.0F).e(3).c("water").K());
      a(10, (String)"flowing_lava", (new BlockFlowing(Material.LAVA)).c(100.0F).a(1.0F).c("lava").K());
      a(11, (String)"lava", (new BlockStationary(Material.LAVA)).c(100.0F).a(1.0F).c("lava").K());
      a(12, (String)"sand", (new BlockSand()).c(0.5F).a(m).c("sand"));
      a(13, (String)"gravel", (new BlockGravel()).c(0.6F).a(g).c("gravel"));
      a(14, (String)"gold_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreGold"));
      a(15, (String)"iron_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreIron"));
      a(16, (String)"coal_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreCoal"));
      a(17, (String)"log", (new BlockLog1()).c("log"));
      a(18, (String)"leaves", (new BlockLeaves1()).c("leaves"));
      a(19, (String)"sponge", (new BlockSponge()).c(0.6F).a(h).c("sponge"));
      a(20, (String)"glass", (new BlockGlass(Material.SHATTERABLE, false)).c(0.3F).a(k).c("glass"));
      a(21, (String)"lapis_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreLapis"));
      a(22, (String)"lapis_block", (new Block(Material.ORE, MaterialMapColor.H)).c(3.0F).b(5.0F).a(i).c("blockLapis").a(CreativeModeTab.b));
      a(23, (String)"dispenser", (new BlockDispenser()).c(3.5F).a(i).c("dispenser"));
      Block var2 = (new BlockSandStone()).a(i).c(0.8F).c("sandStone");
      a(24, (String)"sandstone", var2);
      a(25, (String)"noteblock", (new BlockNote()).c(0.8F).c("musicBlock"));
      a(26, (String)"bed", (new BlockBed()).a(f).c(0.2F).c("bed").K());
      a(27, (String)"golden_rail", (new BlockPoweredRail()).c(0.7F).a(j).c("goldenRail"));
      a(28, (String)"detector_rail", (new BlockMinecartDetector()).c(0.7F).a(j).c("detectorRail"));
      a(29, (String)"sticky_piston", (new BlockPiston(true)).c("pistonStickyBase"));
      a(30, (String)"web", (new BlockWeb()).e(1).c(4.0F).c("web"));
      a(31, (String)"tallgrass", (new BlockLongGrass()).c(0.0F).a(h).c("tallgrass"));
      a(32, (String)"deadbush", (new BlockDeadBush()).c(0.0F).a(h).c("deadbush"));
      a(33, (String)"piston", (new BlockPiston(false)).c("pistonBase"));
      a(34, (String)"piston_head", (new BlockPistonExtension()).c("pistonBase"));
      a(35, (String)"wool", (new BlockCloth(Material.CLOTH)).c(0.8F).a(l).c("cloth"));
      a(36, (String)"piston_extension", new BlockPistonMoving());
      a(37, (String)"yellow_flower", (new BlockYellowFlowers()).c(0.0F).a(h).c("flower1"));
      a(38, (String)"red_flower", (new BlockRedFlowers()).c(0.0F).a(h).c("flower2"));
      Block var3 = (new BlockMushroom()).c(0.0F).a(h).a(0.125F).c("mushroom");
      a(39, (String)"brown_mushroom", var3);
      Block var4 = (new BlockMushroom()).c(0.0F).a(h).c("mushroom");
      a(40, (String)"red_mushroom", var4);
      a(41, (String)"gold_block", (new Block(Material.ORE, MaterialMapColor.F)).c(3.0F).b(10.0F).a(j).c("blockGold").a(CreativeModeTab.b));
      a(42, (String)"iron_block", (new Block(Material.ORE, MaterialMapColor.h)).c(5.0F).b(10.0F).a(j).c("blockIron").a(CreativeModeTab.b));
      a(43, (String)"double_stone_slab", (new BlockDoubleStep()).c(2.0F).b(10.0F).a(i).c("stoneSlab"));
      a(44, (String)"stone_slab", (new BlockStep()).c(2.0F).b(10.0F).a(i).c("stoneSlab"));
      Block var5 = (new Block(Material.STONE, MaterialMapColor.D)).c(2.0F).b(10.0F).a(i).c("brick").a(CreativeModeTab.b);
      a(45, (String)"brick_block", var5);
      a(46, (String)"tnt", (new BlockTNT()).c(0.0F).a(h).c("tnt"));
      a(47, (String)"bookshelf", (new BlockBookshelf()).c(1.5F).a(f).c("bookshelf"));
      a(48, (String)"mossy_cobblestone", (new Block(Material.STONE)).c(2.0F).b(10.0F).a(i).c("stoneMoss").a(CreativeModeTab.b));
      a(49, (String)"obsidian", (new BlockObsidian()).c(50.0F).b(2000.0F).a(i).c("obsidian"));
      a(50, (String)"torch", (new BlockTorch()).c(0.0F).a(0.9375F).a(f).c("torch"));
      a(51, (String)"fire", (new BlockFire()).c(0.0F).a(1.0F).a(l).c("fire").K());
      a(52, (String)"mob_spawner", (new BlockMobSpawner()).c(5.0F).a(j).c("mobSpawner").K());
      a(53, (String)"oak_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.OAK))).c("stairsWood"));
      a(54, (String)"chest", (new BlockChest(0)).c(2.5F).a(f).c("chest"));
      a(55, (String)"redstone_wire", (new BlockRedstoneWire()).c(0.0F).a(e).c("redstoneDust").K());
      a(56, (String)"diamond_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreDiamond"));
      a(57, (String)"diamond_block", (new Block(Material.ORE, MaterialMapColor.G)).c(5.0F).b(10.0F).a(j).c("blockDiamond").a(CreativeModeTab.b));
      a(58, (String)"crafting_table", (new BlockWorkbench()).c(2.5F).a(f).c("workbench"));
      a(59, (String)"wheat", (new BlockCrops()).c("crops"));
      Block var6 = (new BlockSoil()).c(0.6F).a(g).c("farmland");
      a(60, (String)"farmland", var6);
      a(61, (String)"furnace", (new BlockFurnace(false)).c(3.5F).a(i).c("furnace").a(CreativeModeTab.c));
      a(62, (String)"lit_furnace", (new BlockFurnace(true)).c(3.5F).a(i).a(0.875F).c("furnace"));
      a(63, (String)"standing_sign", (new BlockFloorSign()).c(1.0F).a(f).c("sign").K());
      a(64, (String)"wooden_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorOak").K());
      a(65, (String)"ladder", (new BlockLadder()).c(0.4F).a(o).c("ladder"));
      a(66, (String)"rail", (new BlockMinecartTrack()).c(0.7F).a(j).c("rail"));
      a(67, (String)"stone_stairs", (new BlockStairs(var0.getBlockData())).c("stairsStone"));
      a(68, (String)"wall_sign", (new BlockWallSign()).c(1.0F).a(f).c("sign").K());
      a(69, (String)"lever", (new BlockLever()).c(0.5F).a(f).c("lever"));
      a(70, (String)"stone_pressure_plate", (new BlockPressurePlateBinary(Material.STONE, BlockPressurePlateBinary.EnumMobType.MOBS)).c(0.5F).a(i).c("pressurePlateStone"));
      a(71, (String)"iron_door", (new BlockDoor(Material.ORE)).c(5.0F).a(j).c("doorIron").K());
      a(72, (String)"wooden_pressure_plate", (new BlockPressurePlateBinary(Material.WOOD, BlockPressurePlateBinary.EnumMobType.EVERYTHING)).c(0.5F).a(f).c("pressurePlateWood"));
      a(73, (String)"redstone_ore", (new BlockRedstoneOre(false)).c(3.0F).b(5.0F).a(i).c("oreRedstone").a(CreativeModeTab.b));
      a(74, (String)"lit_redstone_ore", (new BlockRedstoneOre(true)).a(0.625F).c(3.0F).b(5.0F).a(i).c("oreRedstone"));
      a(75, (String)"unlit_redstone_torch", (new BlockRedstoneTorch(false)).c(0.0F).a(f).c("notGate"));
      a(76, (String)"redstone_torch", (new BlockRedstoneTorch(true)).c(0.0F).a(0.5F).a(f).c("notGate").a(CreativeModeTab.d));
      a(77, (String)"stone_button", (new BlockStoneButton()).c(0.5F).a(i).c("button"));
      a(78, (String)"snow_layer", (new BlockSnow()).c(0.1F).a(n).c("snow").e(0));
      a(79, (String)"ice", (new BlockIce()).c(0.5F).e(3).a(k).c("ice"));
      a(80, (String)"snow", (new BlockSnowBlock()).c(0.2F).a(n).c("snow"));
      a(81, (String)"cactus", (new BlockCactus()).c(0.4F).a(l).c("cactus"));
      a(82, (String)"clay", (new BlockClay()).c(0.6F).a(g).c("clay"));
      a(83, (String)"reeds", (new BlockReed()).c(0.0F).a(h).c("reeds").K());
      a(84, (String)"jukebox", (new BlockJukeBox()).c(2.0F).b(10.0F).a(i).c("jukebox"));
      a(85, (String)"fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.OAK.c())).c(2.0F).b(5.0F).a(f).c("fence"));
      Block var7 = (new BlockPumpkin()).c(1.0F).a(f).c("pumpkin");
      a(86, (String)"pumpkin", var7);
      a(87, (String)"netherrack", (new BlockBloodStone()).c(0.4F).a(i).c("hellrock"));
      a(88, (String)"soul_sand", (new BlockSlowSand()).c(0.5F).a(m).c("hellsand"));
      a(89, (String)"glowstone", (new BlockLightStone(Material.SHATTERABLE)).c(0.3F).a(k).a(1.0F).c("lightgem"));
      a(90, (String)"portal", (new BlockPortal()).c(-1.0F).a(k).a(0.75F).c("portal"));
      a(91, (String)"lit_pumpkin", (new BlockPumpkin()).c(1.0F).a(f).a(1.0F).c("litpumpkin"));
      a(92, (String)"cake", (new BlockCake()).c(0.5F).a(l).c("cake").K());
      a(93, (String)"unpowered_repeater", (new BlockRepeater(false)).c(0.0F).a(f).c("diode").K());
      a(94, (String)"powered_repeater", (new BlockRepeater(true)).c(0.0F).a(f).c("diode").K());
      a(95, (String)"stained_glass", (new BlockStainedGlass(Material.SHATTERABLE)).c(0.3F).a(k).c("stainedGlass"));
      a(96, (String)"trapdoor", (new BlockTrapdoor(Material.WOOD)).c(3.0F).a(f).c("trapdoor").K());
      a(97, (String)"monster_egg", (new BlockMonsterEggs()).c(0.75F).c("monsterStoneEgg"));
      Block var8 = (new BlockSmoothBrick()).c(1.5F).b(10.0F).a(i).c("stonebricksmooth");
      a(98, (String)"stonebrick", var8);
      a(99, (String)"brown_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MaterialMapColor.l, var3)).c(0.2F).a(f).c("mushroom"));
      a(100, (String)"red_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MaterialMapColor.D, var4)).c(0.2F).a(f).c("mushroom"));
      a(101, (String)"iron_bars", (new BlockThin(Material.ORE, true)).c(5.0F).b(10.0F).a(j).c("fenceIron"));
      a(102, (String)"glass_pane", (new BlockThin(Material.SHATTERABLE, false)).c(0.3F).a(k).c("thinGlass"));
      Block var9 = (new BlockMelon()).c(1.0F).a(f).c("melon");
      a(103, (String)"melon_block", var9);
      a(104, (String)"pumpkin_stem", (new BlockStem(var7)).c(0.0F).a(f).c("pumpkinStem"));
      a(105, (String)"melon_stem", (new BlockStem(var9)).c(0.0F).a(f).c("pumpkinStem"));
      a(106, (String)"vine", (new BlockVine()).c(0.2F).a(h).c("vine"));
      a(107, (String)"fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.OAK)).c(2.0F).b(5.0F).a(f).c("fenceGate"));
      a(108, (String)"brick_stairs", (new BlockStairs(var5.getBlockData())).c("stairsBrick"));
      a(109, (String)"stone_brick_stairs", (new BlockStairs(var8.getBlockData().set(BlockSmoothBrick.VARIANT, BlockSmoothBrick.EnumStonebrickType.DEFAULT))).c("stairsStoneBrickSmooth"));
      a(110, (String)"mycelium", (new BlockMycel()).c(0.6F).a(h).c("mycel"));
      a(111, (String)"waterlily", (new BlockWaterLily()).c(0.0F).a(h).c("waterlily"));
      Block var10 = (new BlockNetherbrick()).c(2.0F).b(10.0F).a(i).c("netherBrick").a(CreativeModeTab.b);
      a(112, (String)"nether_brick", var10);
      a(113, (String)"nether_brick_fence", (new BlockFence(Material.STONE, MaterialMapColor.K)).c(2.0F).b(10.0F).a(i).c("netherFence"));
      a(114, (String)"nether_brick_stairs", (new BlockStairs(var10.getBlockData())).c("stairsNetherBrick"));
      a(115, (String)"nether_wart", (new BlockNetherWart()).c("netherStalk"));
      a(116, (String)"enchanting_table", (new BlockEnchantmentTable()).c(5.0F).b(2000.0F).c("enchantmentTable"));
      a(117, (String)"brewing_stand", (new BlockBrewingStand()).c(0.5F).a(0.125F).c("brewingStand"));
      a(118, (String)"cauldron", (new BlockCauldron()).c(2.0F).c("cauldron"));
      a(119, (String)"end_portal", (new BlockEnderPortal(Material.PORTAL)).c(-1.0F).b(6000000.0F));
      a(120, (String)"end_portal_frame", (new BlockEnderPortalFrame()).a(k).a(0.125F).c(-1.0F).c("endPortalFrame").b(6000000.0F).a(CreativeModeTab.c));
      a(121, (String)"end_stone", (new Block(Material.STONE, MaterialMapColor.d)).c(3.0F).b(15.0F).a(i).c("whiteStone").a(CreativeModeTab.b));
      a(122, (String)"dragon_egg", (new BlockDragonEgg()).c(3.0F).b(15.0F).a(i).a(0.125F).c("dragonEgg"));
      a(123, (String)"redstone_lamp", (new BlockRedstoneLamp(false)).c(0.3F).a(k).c("redstoneLight").a(CreativeModeTab.d));
      a(124, (String)"lit_redstone_lamp", (new BlockRedstoneLamp(true)).c(0.3F).a(k).c("redstoneLight"));
      a(125, (String)"double_wooden_slab", (new BlockDoubleWoodStep()).c(2.0F).b(5.0F).a(f).c("woodSlab"));
      a(126, (String)"wooden_slab", (new BlockWoodStep()).c(2.0F).b(5.0F).a(f).c("woodSlab"));
      a(127, (String)"cocoa", (new BlockCocoa()).c(0.2F).b(5.0F).a(f).c("cocoa"));
      a(128, (String)"sandstone_stairs", (new BlockStairs(var2.getBlockData().set(BlockSandStone.TYPE, BlockSandStone.EnumSandstoneVariant.SMOOTH))).c("stairsSandStone"));
      a(129, (String)"emerald_ore", (new BlockOre()).c(3.0F).b(5.0F).a(i).c("oreEmerald"));
      a(130, (String)"ender_chest", (new BlockEnderChest()).c(22.5F).b(1000.0F).a(i).c("enderChest").a(0.5F));
      a(131, (String)"tripwire_hook", (new BlockTripwireHook()).c("tripWireSource"));
      a(132, (String)"tripwire", (new BlockTripwire()).c("tripWire"));
      a(133, (String)"emerald_block", (new Block(Material.ORE, MaterialMapColor.I)).c(5.0F).b(10.0F).a(j).c("blockEmerald").a(CreativeModeTab.b));
      a(134, (String)"spruce_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.SPRUCE))).c("stairsWoodSpruce"));
      a(135, (String)"birch_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.BIRCH))).c("stairsWoodBirch"));
      a(136, (String)"jungle_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.JUNGLE))).c("stairsWoodJungle"));
      a(137, (String)"command_block", (new BlockCommand()).x().b(6000000.0F).c("commandBlock"));
      a(138, (String)"beacon", (new BlockBeacon()).c("beacon").a(1.0F));
      a(139, (String)"cobblestone_wall", (new BlockCobbleWall(var0)).c("cobbleWall"));
      a(140, (String)"flower_pot", (new BlockFlowerPot()).c(0.0F).a(e).c("flowerPot"));
      a(141, (String)"carrots", (new BlockCarrots()).c("carrots"));
      a(142, (String)"potatoes", (new BlockPotatoes()).c("potatoes"));
      a(143, (String)"wooden_button", (new BlockWoodButton()).c(0.5F).a(f).c("button"));
      a(144, (String)"skull", (new BlockSkull()).c(1.0F).a(i).c("skull"));
      a(145, (String)"anvil", (new BlockAnvil()).c(5.0F).a(p).b(2000.0F).c("anvil"));
      a(146, (String)"trapped_chest", (new BlockChest(1)).c(2.5F).a(f).c("chestTrap"));
      a(147, (String)"light_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.ORE, 15, MaterialMapColor.F)).c(0.5F).a(f).c("weightedPlate_light"));
      a(148, (String)"heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.ORE, 150)).c(0.5F).a(f).c("weightedPlate_heavy"));
      a(149, (String)"unpowered_comparator", (new BlockRedstoneComparator(false)).c(0.0F).a(f).c("comparator").K());
      a(150, (String)"powered_comparator", (new BlockRedstoneComparator(true)).c(0.0F).a(0.625F).a(f).c("comparator").K());
      a(151, (String)"daylight_detector", new BlockDaylightDetector(false));
      a(152, (String)"redstone_block", (new BlockPowered(Material.ORE, MaterialMapColor.f)).c(5.0F).b(10.0F).a(j).c("blockRedstone").a(CreativeModeTab.d));
      a(153, (String)"quartz_ore", (new BlockOre(MaterialMapColor.K)).c(3.0F).b(5.0F).a(i).c("netherquartz"));
      a(154, (String)"hopper", (new BlockHopper()).c(3.0F).b(8.0F).a(j).c("hopper"));
      Block var11 = (new BlockQuartz()).a(i).c(0.8F).c("quartzBlock");
      a(155, (String)"quartz_block", var11);
      a(156, (String)"quartz_stairs", (new BlockStairs(var11.getBlockData().set(BlockQuartz.VARIANT, BlockQuartz.EnumQuartzVariant.DEFAULT))).c("stairsQuartz"));
      a(157, (String)"activator_rail", (new BlockPoweredRail()).c(0.7F).a(j).c("activatorRail"));
      a(158, (String)"dropper", (new BlockDropper()).c(3.5F).a(i).c("dropper"));
      a(159, (String)"stained_hardened_clay", (new BlockCloth(Material.STONE)).c(1.25F).b(7.0F).a(i).c("clayHardenedStained"));
      a(160, (String)"stained_glass_pane", (new BlockStainedGlassPane()).c(0.3F).a(k).c("thinStainedGlass"));
      a(161, (String)"leaves2", (new BlockLeaves2()).c("leaves"));
      a(162, (String)"log2", (new BlockLog2()).c("log"));
      a(163, (String)"acacia_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.ACACIA))).c("stairsWoodAcacia"));
      a(164, (String)"dark_oak_stairs", (new BlockStairs(var1.getBlockData().set(BlockWood.VARIANT, BlockWood.EnumLogVariant.DARK_OAK))).c("stairsWoodDarkOak"));
      a(165, (String)"slime", (new BlockSlime()).c("slime").a(q));
      a(166, (String)"barrier", (new BlockBarrier()).c("barrier"));
      a(167, (String)"iron_trapdoor", (new BlockTrapdoor(Material.ORE)).c(5.0F).a(j).c("ironTrapdoor").K());
      a(168, (String)"prismarine", (new BlockPrismarine()).c(1.5F).b(10.0F).a(i).c("prismarine"));
      a(169, (String)"sea_lantern", (new BlockSeaLantern(Material.SHATTERABLE)).c(0.3F).a(k).a(1.0F).c("seaLantern"));
      a(170, (String)"hay_block", (new BlockHay()).c(0.5F).a(h).c("hayBlock").a(CreativeModeTab.b));
      a(171, (String)"carpet", (new BlockCarpet()).c(0.1F).a(l).c("woolCarpet").e(0));
      a(172, (String)"hardened_clay", (new BlockHardenedClay()).c(1.25F).b(7.0F).a(i).c("clayHardened"));
      a(173, (String)"coal_block", (new Block(Material.STONE, MaterialMapColor.E)).c(5.0F).b(10.0F).a(i).c("blockCoal").a(CreativeModeTab.b));
      a(174, (String)"packed_ice", (new BlockPackedIce()).c(0.5F).a(k).c("icePacked"));
      a(175, (String)"double_plant", new BlockTallPlant());
      a(176, (String)"standing_banner", (new BlockBanner.BlockStandingBanner()).c(1.0F).a(f).c("banner").K());
      a(177, (String)"wall_banner", (new BlockBanner.BlockWallBanner()).c(1.0F).a(f).c("banner").K());
      a(178, (String)"daylight_detector_inverted", new BlockDaylightDetector(true));
      Block var12 = (new BlockRedSandstone()).a(i).c(0.8F).c("redSandStone");
      a(179, (String)"red_sandstone", var12);
      a(180, (String)"red_sandstone_stairs", (new BlockStairs(var12.getBlockData().set(BlockRedSandstone.TYPE, BlockRedSandstone.EnumRedSandstoneVariant.SMOOTH))).c("stairsRedSandStone"));
      a(181, (String)"double_stone_slab2", (new BlockDoubleStoneStep2()).c(2.0F).b(10.0F).a(i).c("stoneSlab2"));
      a(182, (String)"stone_slab2", (new BlockStoneStep2()).c(2.0F).b(10.0F).a(i).c("stoneSlab2"));
      a(183, (String)"spruce_fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.SPRUCE)).c(2.0F).b(5.0F).a(f).c("spruceFenceGate"));
      a(184, (String)"birch_fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.BIRCH)).c(2.0F).b(5.0F).a(f).c("birchFenceGate"));
      a(185, (String)"jungle_fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.JUNGLE)).c(2.0F).b(5.0F).a(f).c("jungleFenceGate"));
      a(186, (String)"dark_oak_fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.DARK_OAK)).c(2.0F).b(5.0F).a(f).c("darkOakFenceGate"));
      a(187, (String)"acacia_fence_gate", (new BlockFenceGate(BlockWood.EnumLogVariant.ACACIA)).c(2.0F).b(5.0F).a(f).c("acaciaFenceGate"));
      a(188, (String)"spruce_fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.SPRUCE.c())).c(2.0F).b(5.0F).a(f).c("spruceFence"));
      a(189, (String)"birch_fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.BIRCH.c())).c(2.0F).b(5.0F).a(f).c("birchFence"));
      a(190, (String)"jungle_fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.JUNGLE.c())).c(2.0F).b(5.0F).a(f).c("jungleFence"));
      a(191, (String)"dark_oak_fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.DARK_OAK.c())).c(2.0F).b(5.0F).a(f).c("darkOakFence"));
      a(192, (String)"acacia_fence", (new BlockFence(Material.WOOD, BlockWood.EnumLogVariant.ACACIA.c())).c(2.0F).b(5.0F).a(f).c("acaciaFence"));
      a(193, (String)"spruce_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorSpruce").K());
      a(194, (String)"birch_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorBirch").K());
      a(195, (String)"jungle_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorJungle").K());
      a(196, (String)"acacia_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorAcacia").K());
      a(197, (String)"dark_oak_door", (new BlockDoor(Material.WOOD)).c(3.0F).a(f).c("doorDarkOak").K());
      REGISTRY.a();
      Iterator var13 = REGISTRY.iterator();

      while(true) {
         Block var14;
         while(var13.hasNext()) {
            var14 = (Block)var13.next();
            if(var14.material == Material.AIR) {
               var14.v = false;
            } else {
               boolean var15 = false;
               boolean var16 = var14 instanceof BlockStairs;
               boolean var17 = var14 instanceof BlockStepAbstract;
               boolean var18 = var14 == var6;
               boolean var19 = var14.t;
               boolean var20 = var14.s == 0;
               if(var16 || var17 || var18 || var19 || var20) {
                  var15 = true;
               }

               var14.v = var15;
            }
         }

         var13 = REGISTRY.iterator();

         while(var13.hasNext()) {
            var14 = (Block)var13.next();
            Iterator var21 = var14.P().a().iterator();

            while(var21.hasNext()) {
               IBlockData var22 = (IBlockData)var21.next();
               int var23 = REGISTRY.b(var14) << 4 | var14.toLegacyData(var22);
               d.a(var22, var23);
            }
         }

         return;
      }
   }

   private static void a(int var0, MinecraftKey var1, Block var2) {
      REGISTRY.a(var0, var1, var2);
   }

   private static void a(int var0, String var1, Block var2) {
      a(var0, new MinecraftKey(var1), var2);
   }

   static {
      REGISTRY = new RegistryBlocks(a);
      d = new RegistryID();
      e = new Block.StepSound("stone", 1.0F, 1.0F);
      f = new Block.StepSound("wood", 1.0F, 1.0F);
      g = new Block.StepSound("gravel", 1.0F, 1.0F);
      h = new Block.StepSound("grass", 1.0F, 1.0F);
      i = new Block.StepSound("stone", 1.0F, 1.0F);
      j = new Block.StepSound("stone", 1.0F, 1.5F);
      k = new Block.StepSound("stone", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "dig.glass";
         }

         public String getPlaceSound() {
            return "step.stone";
         }
      };
      l = new Block.StepSound("cloth", 1.0F, 1.0F);
      m = new Block.StepSound("sand", 1.0F, 1.0F);
      n = new Block.StepSound("snow", 1.0F, 1.0F);
      o = new Block.StepSound("ladder", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "dig.wood";
         }
      };
      p = new Block.StepSound("anvil", 0.3F, 1.0F) {
         public String getBreakSound() {
            return "dig.stone";
         }

         public String getPlaceSound() {
            return "random.anvil_land";
         }
      };
      q = new Block.StepSound("slime", 1.0F, 1.0F) {
         public String getBreakSound() {
            return "mob.slime.big";
         }

         public String getPlaceSound() {
            return "mob.slime.big";
         }

         public String getStepSound() {
            return "mob.slime.small";
         }
      };
   }

   public static class StepSound {
      public final String a;
      public final float b;
      public final float c;

      public StepSound(String var1, float var2, float var3) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
      }

      public float getVolume1() {
         return this.b;
      }

      public float getVolume2() {
         return this.c;
      }

      public String getBreakSound() {
         return "dig." + this.a;
      }

      public String getStepSound() {
         return "step." + this.a;
      }

      public String getPlaceSound() {
         return this.getBreakSound();
      }
   }
}
