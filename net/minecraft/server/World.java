package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockHopper;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockSnow;
import net.minecraft.server.BlockStairs;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.Blocks;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.Explosion;
import net.minecraft.server.GameRules;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IDataManager;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.IWorldAccess;
import net.minecraft.server.IntHashMap;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NextTickListEntry;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.PersistentCollection;
import net.minecraft.server.PersistentVillage;
import net.minecraft.server.ReportedException;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldProvider;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public abstract class World implements IBlockAccess {
   private int a = 63;
   protected boolean e;
   public final List<Entity> entityList = Lists.newArrayList();
   protected final List<Entity> g = Lists.newArrayList();
   public final List<TileEntity> h = Lists.newArrayList();
   public final List<TileEntity> tileEntityList = Lists.newArrayList();
   private final List<TileEntity> b = Lists.newArrayList();
   private final List<TileEntity> c = Lists.newArrayList();
   public final List<EntityHuman> players = Lists.newArrayList();
   public final List<Entity> k = Lists.newArrayList();
   protected final IntHashMap<Entity> entitiesById = new IntHashMap();
   private long d = 16777215L;
   private int I;
   protected int m = (new Random()).nextInt();
   protected final int n = 1013904223;
   protected float o;
   protected float p;
   protected float q;
   protected float r;
   private int J;
   public final Random random = new Random();
   public final WorldProvider worldProvider;
   protected List<IWorldAccess> u = Lists.newArrayList();
   protected IChunkProvider chunkProvider;
   protected final IDataManager dataManager;
   protected WorldData worldData;
   protected boolean isLoading;
   protected PersistentCollection worldMaps;
   protected PersistentVillage villages;
   public final MethodProfiler methodProfiler;
   private final Calendar K = Calendar.getInstance();
   protected Scoreboard scoreboard = new Scoreboard();
   public final boolean isClientSide;
   protected Set<ChunkCoordIntPair> chunkTickList = Sets.newHashSet();
   private int L;
   protected boolean allowMonsters;
   protected boolean allowAnimals;
   private boolean M;
   private final WorldBorder N;
   int[] H;

   protected World(IDataManager var1, WorldData var2, WorldProvider var3, MethodProfiler var4, boolean var5) {
      this.L = this.random.nextInt(12000);
      this.allowMonsters = true;
      this.allowAnimals = true;
      this.H = new int['\u8000'];
      this.dataManager = var1;
      this.methodProfiler = var4;
      this.worldData = var2;
      this.worldProvider = var3;
      this.isClientSide = var5;
      this.N = var3.getWorldBorder();
   }

   public World b() {
      return this;
   }

   public BiomeBase getBiome(final BlockPosition var1) {
      if(this.isLoaded(var1)) {
         Chunk var2 = this.getChunkAtWorldCoords(var1);

         try {
            return var2.getBiome(var1, this.worldProvider.m());
         } catch (Throwable var6) {
            CrashReport var4 = CrashReport.a(var6, "Getting biome");
            CrashReportSystemDetails var5 = var4.a("Coordinates of biome request");
            var5.a("Location", new Callable() {
               public String a() throws Exception {
                  return CrashReportSystemDetails.a(var1);
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            throw new ReportedException(var4);
         }
      } else {
         return this.worldProvider.m().getBiome(var1, BiomeBase.PLAINS);
      }
   }

   public WorldChunkManager getWorldChunkManager() {
      return this.worldProvider.m();
   }

   protected abstract IChunkProvider k();

   public void a(WorldSettings var1) {
      this.worldData.d(true);
   }

   public Block c(BlockPosition var1) {
      BlockPosition var2;
      for(var2 = new BlockPosition(var1.getX(), this.F(), var1.getZ()); !this.isEmpty(var2.up()); var2 = var2.up()) {
         ;
      }

      return this.getType(var2).getBlock();
   }

   private boolean isValidLocation(BlockPosition var1) {
      return var1.getX() >= -30000000 && var1.getZ() >= -30000000 && var1.getX() < 30000000 && var1.getZ() < 30000000 && var1.getY() >= 0 && var1.getY() < 256;
   }

   public boolean isEmpty(BlockPosition var1) {
      return this.getType(var1).getBlock().getMaterial() == Material.AIR;
   }

   public boolean isLoaded(BlockPosition var1) {
      return this.a(var1, true);
   }

   public boolean a(BlockPosition var1, boolean var2) {
      return !this.isValidLocation(var1)?false:this.isChunkLoaded(var1.getX() >> 4, var1.getZ() >> 4, var2);
   }

   public boolean areChunksLoaded(BlockPosition var1, int var2) {
      return this.areChunksLoaded(var1, var2, true);
   }

   public boolean areChunksLoaded(BlockPosition var1, int var2, boolean var3) {
      return this.isAreaLoaded(var1.getX() - var2, var1.getY() - var2, var1.getZ() - var2, var1.getX() + var2, var1.getY() + var2, var1.getZ() + var2, var3);
   }

   public boolean areChunksLoadedBetween(BlockPosition var1, BlockPosition var2) {
      return this.areChunksLoadedBetween(var1, var2, true);
   }

   public boolean areChunksLoadedBetween(BlockPosition var1, BlockPosition var2, boolean var3) {
      return this.isAreaLoaded(var1.getX(), var1.getY(), var1.getZ(), var2.getX(), var2.getY(), var2.getZ(), var3);
   }

   public boolean a(StructureBoundingBox var1) {
      return this.b(var1, true);
   }

   public boolean b(StructureBoundingBox var1, boolean var2) {
      return this.isAreaLoaded(var1.a, var1.b, var1.c, var1.d, var1.e, var1.f, var2);
   }

   private boolean isAreaLoaded(int var1, int var2, int var3, int var4, int var5, int var6, boolean var7) {
      if(var5 >= 0 && var2 < 256) {
         var1 >>= 4;
         var3 >>= 4;
         var4 >>= 4;
         var6 >>= 4;

         for(int var8 = var1; var8 <= var4; ++var8) {
            for(int var9 = var3; var9 <= var6; ++var9) {
               if(!this.isChunkLoaded(var8, var9, var7)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected boolean isChunkLoaded(int var1, int var2, boolean var3) {
      return this.chunkProvider.isChunkLoaded(var1, var2) && (var3 || !this.chunkProvider.getOrCreateChunk(var1, var2).isEmpty());
   }

   public Chunk getChunkAtWorldCoords(BlockPosition var1) {
      return this.getChunkAt(var1.getX() >> 4, var1.getZ() >> 4);
   }

   public Chunk getChunkAt(int var1, int var2) {
      return this.chunkProvider.getOrCreateChunk(var1, var2);
   }

   public boolean setTypeAndData(BlockPosition var1, IBlockData var2, int var3) {
      if(!this.isValidLocation(var1)) {
         return false;
      } else if(!this.isClientSide && this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         return false;
      } else {
         Chunk var4 = this.getChunkAtWorldCoords(var1);
         Block var5 = var2.getBlock();
         IBlockData var6 = var4.a(var1, var2);
         if(var6 == null) {
            return false;
         } else {
            Block var7 = var6.getBlock();
            if(var5.p() != var7.p() || var5.r() != var7.r()) {
               this.methodProfiler.a("checkLight");
               this.x(var1);
               this.methodProfiler.b();
            }

            if((var3 & 2) != 0 && (!this.isClientSide || (var3 & 4) == 0) && var4.isReady()) {
               this.notify(var1);
            }

            if(!this.isClientSide && (var3 & 1) != 0) {
               this.update(var1, var6.getBlock());
               if(var5.isComplexRedstone()) {
                  this.updateAdjacentComparators(var1, var5);
               }
            }

            return true;
         }
      }
   }

   public boolean setAir(BlockPosition var1) {
      return this.setTypeAndData(var1, Blocks.AIR.getBlockData(), 3);
   }

   public boolean setAir(BlockPosition var1, boolean var2) {
      IBlockData var3 = this.getType(var1);
      Block var4 = var3.getBlock();
      if(var4.getMaterial() == Material.AIR) {
         return false;
      } else {
         this.triggerEffect(2001, var1, Block.getCombinedId(var3));
         if(var2) {
            var4.b(this, var1, var3, 0);
         }

         return this.setTypeAndData(var1, Blocks.AIR.getBlockData(), 3);
      }
   }

   public boolean setTypeUpdate(BlockPosition var1, IBlockData var2) {
      return this.setTypeAndData(var1, var2, 3);
   }

   public void notify(BlockPosition var1) {
      for(int var2 = 0; var2 < this.u.size(); ++var2) {
         ((IWorldAccess)this.u.get(var2)).a(var1);
      }

   }

   public void update(BlockPosition var1, Block var2) {
      if(this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.applyPhysics(var1, var2);
      }

   }

   public void a(int var1, int var2, int var3, int var4) {
      int var5;
      if(var3 > var4) {
         var5 = var4;
         var4 = var3;
         var3 = var5;
      }

      if(!this.worldProvider.o()) {
         for(var5 = var3; var5 <= var4; ++var5) {
            this.c(EnumSkyBlock.SKY, new BlockPosition(var1, var5, var2));
         }
      }

      this.b(var1, var3, var2, var1, var4, var2);
   }

   public void b(BlockPosition var1, BlockPosition var2) {
      this.b(var1.getX(), var1.getY(), var1.getZ(), var2.getX(), var2.getY(), var2.getZ());
   }

   public void b(int var1, int var2, int var3, int var4, int var5, int var6) {
      for(int var7 = 0; var7 < this.u.size(); ++var7) {
         ((IWorldAccess)this.u.get(var7)).a(var1, var2, var3, var4, var5, var6);
      }

   }

   public void applyPhysics(BlockPosition var1, Block var2) {
      this.d(var1.west(), var2);
      this.d(var1.east(), var2);
      this.d(var1.down(), var2);
      this.d(var1.up(), var2);
      this.d(var1.north(), var2);
      this.d(var1.south(), var2);
   }

   public void a(BlockPosition var1, Block var2, EnumDirection var3) {
      if(var3 != EnumDirection.WEST) {
         this.d(var1.west(), var2);
      }

      if(var3 != EnumDirection.EAST) {
         this.d(var1.east(), var2);
      }

      if(var3 != EnumDirection.DOWN) {
         this.d(var1.down(), var2);
      }

      if(var3 != EnumDirection.UP) {
         this.d(var1.up(), var2);
      }

      if(var3 != EnumDirection.NORTH) {
         this.d(var1.north(), var2);
      }

      if(var3 != EnumDirection.SOUTH) {
         this.d(var1.south(), var2);
      }

   }

   public void d(BlockPosition var1, final Block var2) {
      if(!this.isClientSide) {
         IBlockData var3 = this.getType(var1);

         try {
            var3.getBlock().doPhysics(this, var1, var3, var2);
         } catch (Throwable var7) {
            CrashReport var5 = CrashReport.a(var7, "Exception while updating neighbours");
            CrashReportSystemDetails var6 = var5.a("Block being updated");
            var6.a("Source block type", new Callable() {
               public String a() throws Exception {
                  try {
                     return String.format("ID #%d (%s // %s)", new Object[]{Integer.valueOf(Block.getId(var2)), var2.a(), var2.getClass().getCanonicalName()});
                  } catch (Throwable var2x) {
                     return "ID #" + Block.getId(var2);
                  }
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            CrashReportSystemDetails.a(var6, var1, var3);
            throw new ReportedException(var5);
         }
      }
   }

   public boolean a(BlockPosition var1, Block var2) {
      return false;
   }

   public boolean i(BlockPosition var1) {
      return this.getChunkAtWorldCoords(var1).d(var1);
   }

   public boolean j(BlockPosition var1) {
      if(var1.getY() >= this.F()) {
         return this.i(var1);
      } else {
         BlockPosition var2 = new BlockPosition(var1.getX(), this.F(), var1.getZ());
         if(!this.i(var2)) {
            return false;
         } else {
            for(var2 = var2.down(); var2.getY() > var1.getY(); var2 = var2.down()) {
               Block var3 = this.getType(var2).getBlock();
               if(var3.p() > 0 && !var3.getMaterial().isLiquid()) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int k(BlockPosition var1) {
      if(var1.getY() < 0) {
         return 0;
      } else {
         if(var1.getY() >= 256) {
            var1 = new BlockPosition(var1.getX(), 255, var1.getZ());
         }

         return this.getChunkAtWorldCoords(var1).a((BlockPosition)var1, 0);
      }
   }

   public int getLightLevel(BlockPosition var1) {
      return this.c(var1, true);
   }

   public int c(BlockPosition var1, boolean var2) {
      if(var1.getX() >= -30000000 && var1.getZ() >= -30000000 && var1.getX() < 30000000 && var1.getZ() < 30000000) {
         if(var2 && this.getType(var1).getBlock().s()) {
            int var8 = this.c(var1.up(), false);
            int var4 = this.c(var1.east(), false);
            int var5 = this.c(var1.west(), false);
            int var6 = this.c(var1.south(), false);
            int var7 = this.c(var1.north(), false);
            if(var4 > var8) {
               var8 = var4;
            }

            if(var5 > var8) {
               var8 = var5;
            }

            if(var6 > var8) {
               var8 = var6;
            }

            if(var7 > var8) {
               var8 = var7;
            }

            return var8;
         } else if(var1.getY() < 0) {
            return 0;
         } else {
            if(var1.getY() >= 256) {
               var1 = new BlockPosition(var1.getX(), 255, var1.getZ());
            }

            Chunk var3 = this.getChunkAtWorldCoords(var1);
            return var3.a(var1, this.I);
         }
      } else {
         return 15;
      }
   }

   public BlockPosition getHighestBlockYAt(BlockPosition var1) {
      int var2;
      if(var1.getX() >= -30000000 && var1.getZ() >= -30000000 && var1.getX() < 30000000 && var1.getZ() < 30000000) {
         if(this.isChunkLoaded(var1.getX() >> 4, var1.getZ() >> 4, true)) {
            var2 = this.getChunkAt(var1.getX() >> 4, var1.getZ() >> 4).b(var1.getX() & 15, var1.getZ() & 15);
         } else {
            var2 = 0;
         }
      } else {
         var2 = this.F() + 1;
      }

      return new BlockPosition(var1.getX(), var2, var1.getZ());
   }

   public int b(int var1, int var2) {
      if(var1 >= -30000000 && var2 >= -30000000 && var1 < 30000000 && var2 < 30000000) {
         if(!this.isChunkLoaded(var1 >> 4, var2 >> 4, true)) {
            return 0;
         } else {
            Chunk var3 = this.getChunkAt(var1 >> 4, var2 >> 4);
            return var3.v();
         }
      } else {
         return this.F() + 1;
      }
   }

   public int b(EnumSkyBlock var1, BlockPosition var2) {
      if(var2.getY() < 0) {
         var2 = new BlockPosition(var2.getX(), 0, var2.getZ());
      }

      if(!this.isValidLocation(var2)) {
         return var1.c;
      } else if(!this.isLoaded(var2)) {
         return var1.c;
      } else {
         Chunk var3 = this.getChunkAtWorldCoords(var2);
         return var3.getBrightness(var1, var2);
      }
   }

   public void a(EnumSkyBlock var1, BlockPosition var2, int var3) {
      if(this.isValidLocation(var2)) {
         if(this.isLoaded(var2)) {
            Chunk var4 = this.getChunkAtWorldCoords(var2);
            var4.a(var1, var2, var3);
            this.n(var2);
         }
      }
   }

   public void n(BlockPosition var1) {
      for(int var2 = 0; var2 < this.u.size(); ++var2) {
         ((IWorldAccess)this.u.get(var2)).b(var1);
      }

   }

   public float o(BlockPosition var1) {
      return this.worldProvider.p()[this.getLightLevel(var1)];
   }

   public IBlockData getType(BlockPosition var1) {
      if(!this.isValidLocation(var1)) {
         return Blocks.AIR.getBlockData();
      } else {
         Chunk var2 = this.getChunkAtWorldCoords(var1);
         return var2.getBlockData(var1);
      }
   }

   public boolean w() {
      return this.I < 4;
   }

   public MovingObjectPosition rayTrace(Vec3D var1, Vec3D var2) {
      return this.rayTrace(var1, var2, false, false, false);
   }

   public MovingObjectPosition rayTrace(Vec3D var1, Vec3D var2, boolean var3) {
      return this.rayTrace(var1, var2, var3, false, false);
   }

   public MovingObjectPosition rayTrace(Vec3D var1, Vec3D var2, boolean var3, boolean var4, boolean var5) {
      if(!Double.isNaN(var1.a) && !Double.isNaN(var1.b) && !Double.isNaN(var1.c)) {
         if(!Double.isNaN(var2.a) && !Double.isNaN(var2.b) && !Double.isNaN(var2.c)) {
            int var6 = MathHelper.floor(var2.a);
            int var7 = MathHelper.floor(var2.b);
            int var8 = MathHelper.floor(var2.c);
            int var9 = MathHelper.floor(var1.a);
            int var10 = MathHelper.floor(var1.b);
            int var11 = MathHelper.floor(var1.c);
            BlockPosition var12 = new BlockPosition(var9, var10, var11);
            IBlockData var13 = this.getType(var12);
            Block var14 = var13.getBlock();
            if((!var4 || var14.a(this, var12, var13) != null) && var14.a(var13, var3)) {
               MovingObjectPosition var15 = var14.a(this, var12, var1, var2);
               if(var15 != null) {
                  return var15;
               }
            }

            MovingObjectPosition var40 = null;
            int var41 = 200;

            while(var41-- >= 0) {
               if(Double.isNaN(var1.a) || Double.isNaN(var1.b) || Double.isNaN(var1.c)) {
                  return null;
               }

               if(var9 == var6 && var10 == var7 && var11 == var8) {
                  return var5?var40:null;
               }

               boolean var42 = true;
               boolean var16 = true;
               boolean var17 = true;
               double var18 = 999.0D;
               double var20 = 999.0D;
               double var22 = 999.0D;
               if(var6 > var9) {
                  var18 = (double)var9 + 1.0D;
               } else if(var6 < var9) {
                  var18 = (double)var9 + 0.0D;
               } else {
                  var42 = false;
               }

               if(var7 > var10) {
                  var20 = (double)var10 + 1.0D;
               } else if(var7 < var10) {
                  var20 = (double)var10 + 0.0D;
               } else {
                  var16 = false;
               }

               if(var8 > var11) {
                  var22 = (double)var11 + 1.0D;
               } else if(var8 < var11) {
                  var22 = (double)var11 + 0.0D;
               } else {
                  var17 = false;
               }

               double var24 = 999.0D;
               double var26 = 999.0D;
               double var28 = 999.0D;
               double var30 = var2.a - var1.a;
               double var32 = var2.b - var1.b;
               double var34 = var2.c - var1.c;
               if(var42) {
                  var24 = (var18 - var1.a) / var30;
               }

               if(var16) {
                  var26 = (var20 - var1.b) / var32;
               }

               if(var17) {
                  var28 = (var22 - var1.c) / var34;
               }

               if(var24 == -0.0D) {
                  var24 = -1.0E-4D;
               }

               if(var26 == -0.0D) {
                  var26 = -1.0E-4D;
               }

               if(var28 == -0.0D) {
                  var28 = -1.0E-4D;
               }

               EnumDirection var36;
               if(var24 < var26 && var24 < var28) {
                  var36 = var6 > var9?EnumDirection.WEST:EnumDirection.EAST;
                  var1 = new Vec3D(var18, var1.b + var32 * var24, var1.c + var34 * var24);
               } else if(var26 < var28) {
                  var36 = var7 > var10?EnumDirection.DOWN:EnumDirection.UP;
                  var1 = new Vec3D(var1.a + var30 * var26, var20, var1.c + var34 * var26);
               } else {
                  var36 = var8 > var11?EnumDirection.NORTH:EnumDirection.SOUTH;
                  var1 = new Vec3D(var1.a + var30 * var28, var1.b + var32 * var28, var22);
               }

               var9 = MathHelper.floor(var1.a) - (var36 == EnumDirection.EAST?1:0);
               var10 = MathHelper.floor(var1.b) - (var36 == EnumDirection.UP?1:0);
               var11 = MathHelper.floor(var1.c) - (var36 == EnumDirection.SOUTH?1:0);
               var12 = new BlockPosition(var9, var10, var11);
               IBlockData var37 = this.getType(var12);
               Block var38 = var37.getBlock();
               if(!var4 || var38.a(this, var12, var37) != null) {
                  if(var38.a(var37, var3)) {
                     MovingObjectPosition var39 = var38.a(this, var12, var1, var2);
                     if(var39 != null) {
                        return var39;
                     }
                  } else {
                     var40 = new MovingObjectPosition(MovingObjectPosition.EnumMovingObjectType.MISS, var1, var36, var12);
                  }
               }
            }

            return var5?var40:null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void makeSound(Entity var1, String var2, float var3, float var4) {
      for(int var5 = 0; var5 < this.u.size(); ++var5) {
         ((IWorldAccess)this.u.get(var5)).a(var2, var1.locX, var1.locY, var1.locZ, var3, var4);
      }

   }

   public void a(EntityHuman var1, String var2, float var3, float var4) {
      for(int var5 = 0; var5 < this.u.size(); ++var5) {
         ((IWorldAccess)this.u.get(var5)).a(var1, var2, var1.locX, var1.locY, var1.locZ, var3, var4);
      }

   }

   public void makeSound(double var1, double var3, double var5, String var7, float var8, float var9) {
      for(int var10 = 0; var10 < this.u.size(); ++var10) {
         ((IWorldAccess)this.u.get(var10)).a(var7, var1, var3, var5, var8, var9);
      }

   }

   public void a(double var1, double var3, double var5, String var7, float var8, float var9, boolean var10) {
   }

   public void a(BlockPosition var1, String var2) {
      for(int var3 = 0; var3 < this.u.size(); ++var3) {
         ((IWorldAccess)this.u.get(var3)).a(var2, var1);
      }

   }

   public void addParticle(EnumParticle var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      this.a(var1.c(), var1.e(), var2, var4, var6, var8, var10, var12, var14);
   }

   private void a(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      for(int var16 = 0; var16 < this.u.size(); ++var16) {
         ((IWorldAccess)this.u.get(var16)).a(var1, var2, var3, var5, var7, var9, var11, var13, var15);
      }

   }

   public boolean strikeLightning(Entity var1) {
      this.k.add(var1);
      return true;
   }

   public boolean addEntity(Entity var1) {
      int var2 = MathHelper.floor(var1.locX / 16.0D);
      int var3 = MathHelper.floor(var1.locZ / 16.0D);
      boolean var4 = var1.attachedToPlayer;
      if(var1 instanceof EntityHuman) {
         var4 = true;
      }

      if(!var4 && !this.isChunkLoaded(var2, var3, true)) {
         return false;
      } else {
         if(var1 instanceof EntityHuman) {
            EntityHuman var5 = (EntityHuman)var1;
            this.players.add(var5);
            this.everyoneSleeping();
         }

         this.getChunkAt(var2, var3).a(var1);
         this.entityList.add(var1);
         this.a(var1);
         return true;
      }
   }

   protected void a(Entity var1) {
      for(int var2 = 0; var2 < this.u.size(); ++var2) {
         ((IWorldAccess)this.u.get(var2)).a(var1);
      }

   }

   protected void b(Entity var1) {
      for(int var2 = 0; var2 < this.u.size(); ++var2) {
         ((IWorldAccess)this.u.get(var2)).b(var1);
      }

   }

   public void kill(Entity var1) {
      if(var1.passenger != null) {
         var1.passenger.mount((Entity)null);
      }

      if(var1.vehicle != null) {
         var1.mount((Entity)null);
      }

      var1.die();
      if(var1 instanceof EntityHuman) {
         this.players.remove(var1);
         this.everyoneSleeping();
         this.b(var1);
      }

   }

   public void removeEntity(Entity var1) {
      var1.die();
      if(var1 instanceof EntityHuman) {
         this.players.remove(var1);
         this.everyoneSleeping();
      }

      int var2 = var1.ae;
      int var3 = var1.ag;
      if(var1.ad && this.isChunkLoaded(var2, var3, true)) {
         this.getChunkAt(var2, var3).b(var1);
      }

      this.entityList.remove(var1);
      this.b(var1);
   }

   public void addIWorldAccess(IWorldAccess var1) {
      this.u.add(var1);
   }

   public List<AxisAlignedBB> getCubes(Entity var1, AxisAlignedBB var2) {
      ArrayList var3 = Lists.newArrayList();
      int var4 = MathHelper.floor(var2.a);
      int var5 = MathHelper.floor(var2.d + 1.0D);
      int var6 = MathHelper.floor(var2.b);
      int var7 = MathHelper.floor(var2.e + 1.0D);
      int var8 = MathHelper.floor(var2.c);
      int var9 = MathHelper.floor(var2.f + 1.0D);
      WorldBorder var10 = this.getWorldBorder();
      boolean var11 = var1.aT();
      boolean var12 = this.a(var10, var1);
      IBlockData var13 = Blocks.STONE.getBlockData();
      BlockPosition.MutableBlockPosition var14 = new BlockPosition.MutableBlockPosition();

      for(int var15 = var4; var15 < var5; ++var15) {
         for(int var16 = var8; var16 < var9; ++var16) {
            if(this.isLoaded(var14.c(var15, 64, var16))) {
               for(int var17 = var6 - 1; var17 < var7; ++var17) {
                  var14.c(var15, var17, var16);
                  if(var11 && var12) {
                     var1.h(false);
                  } else if(!var11 && !var12) {
                     var1.h(true);
                  }

                  IBlockData var18 = var13;
                  if(var10.a((BlockPosition)var14) || !var12) {
                     var18 = this.getType(var14);
                  }

                  var18.getBlock().a(this, var14, var18, var2, var3, var1);
               }
            }
         }
      }

      double var20 = 0.25D;
      List var21 = this.getEntities(var1, var2.grow(var20, var20, var20));

      for(int var22 = 0; var22 < var21.size(); ++var22) {
         if(var1.passenger != var21 && var1.vehicle != var21) {
            AxisAlignedBB var19 = ((Entity)var21.get(var22)).S();
            if(var19 != null && var19.b(var2)) {
               var3.add(var19);
            }

            var19 = var1.j((Entity)var21.get(var22));
            if(var19 != null && var19.b(var2)) {
               var3.add(var19);
            }
         }
      }

      return var3;
   }

   public boolean a(WorldBorder var1, Entity var2) {
      double var3 = var1.b();
      double var5 = var1.c();
      double var7 = var1.d();
      double var9 = var1.e();
      if(var2.aT()) {
         ++var3;
         ++var5;
         --var7;
         --var9;
      } else {
         --var3;
         --var5;
         ++var7;
         ++var9;
      }

      return var2.locX > var3 && var2.locX < var7 && var2.locZ > var5 && var2.locZ < var9;
   }

   public List<AxisAlignedBB> a(AxisAlignedBB var1) {
      ArrayList var2 = Lists.newArrayList();
      int var3 = MathHelper.floor(var1.a);
      int var4 = MathHelper.floor(var1.d + 1.0D);
      int var5 = MathHelper.floor(var1.b);
      int var6 = MathHelper.floor(var1.e + 1.0D);
      int var7 = MathHelper.floor(var1.c);
      int var8 = MathHelper.floor(var1.f + 1.0D);
      BlockPosition.MutableBlockPosition var9 = new BlockPosition.MutableBlockPosition();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var7; var11 < var8; ++var11) {
            if(this.isLoaded(var9.c(var10, 64, var11))) {
               for(int var12 = var5 - 1; var12 < var6; ++var12) {
                  var9.c(var10, var12, var11);
                  IBlockData var13;
                  if(var10 >= -30000000 && var10 < 30000000 && var11 >= -30000000 && var11 < 30000000) {
                     var13 = this.getType(var9);
                  } else {
                     var13 = Blocks.BEDROCK.getBlockData();
                  }

                  var13.getBlock().a(this, var9, var13, var1, var2, (Entity)null);
               }
            }
         }
      }

      return var2;
   }

   public int a(float var1) {
      float var2 = this.c(var1);
      float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F);
      var3 = MathHelper.a(var3, 0.0F, 1.0F);
      var3 = 1.0F - var3;
      var3 = (float)((double)var3 * (1.0D - (double)(this.j(var1) * 5.0F) / 16.0D));
      var3 = (float)((double)var3 * (1.0D - (double)(this.h(var1) * 5.0F) / 16.0D));
      var3 = 1.0F - var3;
      return (int)(var3 * 11.0F);
   }

   public float c(float var1) {
      return this.worldProvider.a(this.worldData.getDayTime(), var1);
   }

   public float y() {
      return WorldProvider.a[this.worldProvider.a(this.worldData.getDayTime())];
   }

   public float d(float var1) {
      float var2 = this.c(var1);
      return var2 * 3.1415927F * 2.0F;
   }

   public BlockPosition q(BlockPosition var1) {
      return this.getChunkAtWorldCoords(var1).h(var1);
   }

   public BlockPosition r(BlockPosition var1) {
      Chunk var2 = this.getChunkAtWorldCoords(var1);

      BlockPosition var3;
      BlockPosition var4;
      for(var3 = new BlockPosition(var1.getX(), var2.g() + 16, var1.getZ()); var3.getY() >= 0; var3 = var4) {
         var4 = var3.down();
         Material var5 = var2.getType(var4).getMaterial();
         if(var5.isSolid() && var5 != Material.LEAVES) {
            break;
         }
      }

      return var3;
   }

   public void a(BlockPosition var1, Block var2, int var3) {
   }

   public void a(BlockPosition var1, Block var2, int var3, int var4) {
   }

   public void b(BlockPosition var1, Block var2, int var3, int var4) {
   }

   public void tickEntities() {
      this.methodProfiler.a("entities");
      this.methodProfiler.a("global");

      int var1;
      Entity var2;
      CrashReport var4;
      CrashReportSystemDetails var5;
      for(var1 = 0; var1 < this.k.size(); ++var1) {
         var2 = (Entity)this.k.get(var1);

         try {
            ++var2.ticksLived;
            var2.t_();
         } catch (Throwable var9) {
            var4 = CrashReport.a(var9, "Ticking entity");
            var5 = var4.a("Entity being ticked");
            if(var2 == null) {
               var5.a((String)"Entity", (Object)"~~NULL~~");
            } else {
               var2.appendEntityCrashDetails(var5);
            }

            throw new ReportedException(var4);
         }

         if(var2.dead) {
            this.k.remove(var1--);
         }
      }

      this.methodProfiler.c("remove");
      this.entityList.removeAll(this.g);

      int var3;
      int var14;
      for(var1 = 0; var1 < this.g.size(); ++var1) {
         var2 = (Entity)this.g.get(var1);
         var3 = var2.ae;
         var14 = var2.ag;
         if(var2.ad && this.isChunkLoaded(var3, var14, true)) {
            this.getChunkAt(var3, var14).b(var2);
         }
      }

      for(var1 = 0; var1 < this.g.size(); ++var1) {
         this.b((Entity)this.g.get(var1));
      }

      this.g.clear();
      this.methodProfiler.c("regular");

      for(var1 = 0; var1 < this.entityList.size(); ++var1) {
         var2 = (Entity)this.entityList.get(var1);
         if(var2.vehicle != null) {
            if(!var2.vehicle.dead && var2.vehicle.passenger == var2) {
               continue;
            }

            var2.vehicle.passenger = null;
            var2.vehicle = null;
         }

         this.methodProfiler.a("tick");
         if(!var2.dead) {
            try {
               this.g(var2);
            } catch (Throwable var8) {
               var4 = CrashReport.a(var8, "Ticking entity");
               var5 = var4.a("Entity being ticked");
               var2.appendEntityCrashDetails(var5);
               throw new ReportedException(var4);
            }
         }

         this.methodProfiler.b();
         this.methodProfiler.a("remove");
         if(var2.dead) {
            var3 = var2.ae;
            var14 = var2.ag;
            if(var2.ad && this.isChunkLoaded(var3, var14, true)) {
               this.getChunkAt(var3, var14).b(var2);
            }

            this.entityList.remove(var1--);
            this.b(var2);
         }

         this.methodProfiler.b();
      }

      this.methodProfiler.c("blockEntities");
      this.M = true;
      Iterator var15 = this.tileEntityList.iterator();

      while(var15.hasNext()) {
         TileEntity var10 = (TileEntity)var15.next();
         if(!var10.x() && var10.t()) {
            BlockPosition var12 = var10.getPosition();
            if(this.isLoaded(var12) && this.N.a(var12)) {
               try {
                  ((IUpdatePlayerListBox)var10).c();
               } catch (Throwable var7) {
                  CrashReport var16 = CrashReport.a(var7, "Ticking block entity");
                  CrashReportSystemDetails var6 = var16.a("Block entity being ticked");
                  var10.a(var6);
                  throw new ReportedException(var16);
               }
            }
         }

         if(var10.x()) {
            var15.remove();
            this.h.remove(var10);
            if(this.isLoaded(var10.getPosition())) {
               this.getChunkAtWorldCoords(var10.getPosition()).e(var10.getPosition());
            }
         }
      }

      this.M = false;
      if(!this.c.isEmpty()) {
         this.tileEntityList.removeAll(this.c);
         this.h.removeAll(this.c);
         this.c.clear();
      }

      this.methodProfiler.c("pendingBlockEntities");
      if(!this.b.isEmpty()) {
         for(int var11 = 0; var11 < this.b.size(); ++var11) {
            TileEntity var13 = (TileEntity)this.b.get(var11);
            if(!var13.x()) {
               if(!this.h.contains(var13)) {
                  this.a(var13);
               }

               if(this.isLoaded(var13.getPosition())) {
                  this.getChunkAtWorldCoords(var13.getPosition()).a(var13.getPosition(), var13);
               }

               this.notify(var13.getPosition());
            }
         }

         this.b.clear();
      }

      this.methodProfiler.b();
      this.methodProfiler.b();
   }

   public boolean a(TileEntity var1) {
      boolean var2 = this.h.add(var1);
      if(var2 && var1 instanceof IUpdatePlayerListBox) {
         this.tileEntityList.add(var1);
      }

      return var2;
   }

   public void a(Collection<TileEntity> var1) {
      if(this.M) {
         this.b.addAll(var1);
      } else {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            TileEntity var3 = (TileEntity)var2.next();
            this.h.add(var3);
            if(var3 instanceof IUpdatePlayerListBox) {
               this.tileEntityList.add(var3);
            }
         }
      }

   }

   public void g(Entity var1) {
      this.entityJoinedWorld(var1, true);
   }

   public void entityJoinedWorld(Entity var1, boolean var2) {
      int var3 = MathHelper.floor(var1.locX);
      int var4 = MathHelper.floor(var1.locZ);
      byte var5 = 32;
      if(!var2 || this.isAreaLoaded(var3 - var5, 0, var4 - var5, var3 + var5, 0, var4 + var5, true)) {
         var1.P = var1.locX;
         var1.Q = var1.locY;
         var1.R = var1.locZ;
         var1.lastYaw = var1.yaw;
         var1.lastPitch = var1.pitch;
         if(var2 && var1.ad) {
            ++var1.ticksLived;
            if(var1.vehicle != null) {
               var1.ak();
            } else {
               var1.t_();
            }
         }

         this.methodProfiler.a("chunkCheck");
         if(Double.isNaN(var1.locX) || Double.isInfinite(var1.locX)) {
            var1.locX = var1.P;
         }

         if(Double.isNaN(var1.locY) || Double.isInfinite(var1.locY)) {
            var1.locY = var1.Q;
         }

         if(Double.isNaN(var1.locZ) || Double.isInfinite(var1.locZ)) {
            var1.locZ = var1.R;
         }

         if(Double.isNaN((double)var1.pitch) || Double.isInfinite((double)var1.pitch)) {
            var1.pitch = var1.lastPitch;
         }

         if(Double.isNaN((double)var1.yaw) || Double.isInfinite((double)var1.yaw)) {
            var1.yaw = var1.lastYaw;
         }

         int var6 = MathHelper.floor(var1.locX / 16.0D);
         int var7 = MathHelper.floor(var1.locY / 16.0D);
         int var8 = MathHelper.floor(var1.locZ / 16.0D);
         if(!var1.ad || var1.ae != var6 || var1.af != var7 || var1.ag != var8) {
            if(var1.ad && this.isChunkLoaded(var1.ae, var1.ag, true)) {
               this.getChunkAt(var1.ae, var1.ag).a(var1, var1.af);
            }

            if(this.isChunkLoaded(var6, var8, true)) {
               var1.ad = true;
               this.getChunkAt(var6, var8).a(var1);
            } else {
               var1.ad = false;
            }
         }

         this.methodProfiler.b();
         if(var2 && var1.ad && var1.passenger != null) {
            if(!var1.passenger.dead && var1.passenger.vehicle == var1) {
               this.g(var1.passenger);
            } else {
               var1.passenger.vehicle = null;
               var1.passenger = null;
            }
         }

      }
   }

   public boolean b(AxisAlignedBB var1) {
      return this.a((AxisAlignedBB)var1, (Entity)null);
   }

   public boolean a(AxisAlignedBB var1, Entity var2) {
      List var3 = this.getEntities((Entity)null, var1);

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         Entity var5 = (Entity)var3.get(var4);
         if(!var5.dead && var5.k && var5 != var2 && (var2 == null || var2.vehicle != var5 && var2.passenger != var5)) {
            return false;
         }
      }

      return true;
   }

   public boolean c(AxisAlignedBB var1) {
      int var2 = MathHelper.floor(var1.a);
      int var3 = MathHelper.floor(var1.d);
      int var4 = MathHelper.floor(var1.b);
      int var5 = MathHelper.floor(var1.e);
      int var6 = MathHelper.floor(var1.c);
      int var7 = MathHelper.floor(var1.f);
      BlockPosition.MutableBlockPosition var8 = new BlockPosition.MutableBlockPosition();

      for(int var9 = var2; var9 <= var3; ++var9) {
         for(int var10 = var4; var10 <= var5; ++var10) {
            for(int var11 = var6; var11 <= var7; ++var11) {
               Block var12 = this.getType(var8.c(var9, var10, var11)).getBlock();
               if(var12.getMaterial() != Material.AIR) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean containsLiquid(AxisAlignedBB var1) {
      int var2 = MathHelper.floor(var1.a);
      int var3 = MathHelper.floor(var1.d);
      int var4 = MathHelper.floor(var1.b);
      int var5 = MathHelper.floor(var1.e);
      int var6 = MathHelper.floor(var1.c);
      int var7 = MathHelper.floor(var1.f);
      BlockPosition.MutableBlockPosition var8 = new BlockPosition.MutableBlockPosition();

      for(int var9 = var2; var9 <= var3; ++var9) {
         for(int var10 = var4; var10 <= var5; ++var10) {
            for(int var11 = var6; var11 <= var7; ++var11) {
               Block var12 = this.getType(var8.c(var9, var10, var11)).getBlock();
               if(var12.getMaterial().isLiquid()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean e(AxisAlignedBB var1) {
      int var2 = MathHelper.floor(var1.a);
      int var3 = MathHelper.floor(var1.d + 1.0D);
      int var4 = MathHelper.floor(var1.b);
      int var5 = MathHelper.floor(var1.e + 1.0D);
      int var6 = MathHelper.floor(var1.c);
      int var7 = MathHelper.floor(var1.f + 1.0D);
      if(this.isAreaLoaded(var2, var4, var6, var3, var5, var7, true)) {
         BlockPosition.MutableBlockPosition var8 = new BlockPosition.MutableBlockPosition();

         for(int var9 = var2; var9 < var3; ++var9) {
            for(int var10 = var4; var10 < var5; ++var10) {
               for(int var11 = var6; var11 < var7; ++var11) {
                  Block var12 = this.getType(var8.c(var9, var10, var11)).getBlock();
                  if(var12 == Blocks.FIRE || var12 == Blocks.FLOWING_LAVA || var12 == Blocks.LAVA) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean a(AxisAlignedBB var1, Material var2, Entity var3) {
      int var4 = MathHelper.floor(var1.a);
      int var5 = MathHelper.floor(var1.d + 1.0D);
      int var6 = MathHelper.floor(var1.b);
      int var7 = MathHelper.floor(var1.e + 1.0D);
      int var8 = MathHelper.floor(var1.c);
      int var9 = MathHelper.floor(var1.f + 1.0D);
      if(!this.isAreaLoaded(var4, var6, var8, var5, var7, var9, true)) {
         return false;
      } else {
         boolean var10 = false;
         Vec3D var11 = new Vec3D(0.0D, 0.0D, 0.0D);
         BlockPosition.MutableBlockPosition var12 = new BlockPosition.MutableBlockPosition();

         for(int var13 = var4; var13 < var5; ++var13) {
            for(int var14 = var6; var14 < var7; ++var14) {
               for(int var15 = var8; var15 < var9; ++var15) {
                  var12.c(var13, var14, var15);
                  IBlockData var16 = this.getType(var12);
                  Block var17 = var16.getBlock();
                  if(var17.getMaterial() == var2) {
                     double var18 = (double)((float)(var14 + 1) - BlockFluids.b(((Integer)var16.get(BlockFluids.LEVEL)).intValue()));
                     if((double)var7 >= var18) {
                        var10 = true;
                        var11 = var17.a((World)this, var12, (Entity)var3, (Vec3D)var11);
                     }
                  }
               }
            }
         }

         if(var11.b() > 0.0D && var3.aL()) {
            var11 = var11.a();
            double var20 = 0.014D;
            var3.motX += var11.a * var20;
            var3.motY += var11.b * var20;
            var3.motZ += var11.c * var20;
         }

         return var10;
      }
   }

   public boolean a(AxisAlignedBB var1, Material var2) {
      int var3 = MathHelper.floor(var1.a);
      int var4 = MathHelper.floor(var1.d + 1.0D);
      int var5 = MathHelper.floor(var1.b);
      int var6 = MathHelper.floor(var1.e + 1.0D);
      int var7 = MathHelper.floor(var1.c);
      int var8 = MathHelper.floor(var1.f + 1.0D);
      BlockPosition.MutableBlockPosition var9 = new BlockPosition.MutableBlockPosition();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var5; var11 < var6; ++var11) {
            for(int var12 = var7; var12 < var8; ++var12) {
               if(this.getType(var9.c(var10, var11, var12)).getBlock().getMaterial() == var2) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean b(AxisAlignedBB var1, Material var2) {
      int var3 = MathHelper.floor(var1.a);
      int var4 = MathHelper.floor(var1.d + 1.0D);
      int var5 = MathHelper.floor(var1.b);
      int var6 = MathHelper.floor(var1.e + 1.0D);
      int var7 = MathHelper.floor(var1.c);
      int var8 = MathHelper.floor(var1.f + 1.0D);
      BlockPosition.MutableBlockPosition var9 = new BlockPosition.MutableBlockPosition();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var5; var11 < var6; ++var11) {
            for(int var12 = var7; var12 < var8; ++var12) {
               IBlockData var13 = this.getType(var9.c(var10, var11, var12));
               Block var14 = var13.getBlock();
               if(var14.getMaterial() == var2) {
                  int var15 = ((Integer)var13.get(BlockFluids.LEVEL)).intValue();
                  double var16 = (double)(var11 + 1);
                  if(var15 < 8) {
                     var16 = (double)(var11 + 1) - (double)var15 / 8.0D;
                  }

                  if(var16 >= var1.b) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public Explosion explode(Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
      return this.createExplosion(var1, var2, var4, var6, var8, false, var9);
   }

   public Explosion createExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9, boolean var10) {
      Explosion var11 = new Explosion(this, var1, var2, var4, var6, var8, var9, var10);
      var11.a();
      var11.a(true);
      return var11;
   }

   public float a(Vec3D var1, AxisAlignedBB var2) {
      double var3 = 1.0D / ((var2.d - var2.a) * 2.0D + 1.0D);
      double var5 = 1.0D / ((var2.e - var2.b) * 2.0D + 1.0D);
      double var7 = 1.0D / ((var2.f - var2.c) * 2.0D + 1.0D);
      double var9 = (1.0D - Math.floor(1.0D / var3) * var3) / 2.0D;
      double var11 = (1.0D - Math.floor(1.0D / var7) * var7) / 2.0D;
      if(var3 >= 0.0D && var5 >= 0.0D && var7 >= 0.0D) {
         int var13 = 0;
         int var14 = 0;

         for(float var15 = 0.0F; var15 <= 1.0F; var15 = (float)((double)var15 + var3)) {
            for(float var16 = 0.0F; var16 <= 1.0F; var16 = (float)((double)var16 + var5)) {
               for(float var17 = 0.0F; var17 <= 1.0F; var17 = (float)((double)var17 + var7)) {
                  double var18 = var2.a + (var2.d - var2.a) * (double)var15;
                  double var20 = var2.b + (var2.e - var2.b) * (double)var16;
                  double var22 = var2.c + (var2.f - var2.c) * (double)var17;
                  if(this.rayTrace(new Vec3D(var18 + var9, var20, var22 + var11), var1) == null) {
                     ++var13;
                  }

                  ++var14;
               }
            }
         }

         return (float)var13 / (float)var14;
      } else {
         return 0.0F;
      }
   }

   public boolean douseFire(EntityHuman var1, BlockPosition var2, EnumDirection var3) {
      var2 = var2.shift(var3);
      if(this.getType(var2).getBlock() == Blocks.FIRE) {
         this.a(var1, 1004, var2, 0);
         this.setAir(var2);
         return true;
      } else {
         return false;
      }
   }

   public TileEntity getTileEntity(BlockPosition var1) {
      if(!this.isValidLocation(var1)) {
         return null;
      } else {
         TileEntity var2 = null;
         int var3;
         TileEntity var4;
         if(this.M) {
            for(var3 = 0; var3 < this.b.size(); ++var3) {
               var4 = (TileEntity)this.b.get(var3);
               if(!var4.x() && var4.getPosition().equals(var1)) {
                  var2 = var4;
                  break;
               }
            }
         }

         if(var2 == null) {
            var2 = this.getChunkAtWorldCoords(var1).a(var1, Chunk.EnumTileEntityState.IMMEDIATE);
         }

         if(var2 == null) {
            for(var3 = 0; var3 < this.b.size(); ++var3) {
               var4 = (TileEntity)this.b.get(var3);
               if(!var4.x() && var4.getPosition().equals(var1)) {
                  var2 = var4;
                  break;
               }
            }
         }

         return var2;
      }
   }

   public void setTileEntity(BlockPosition var1, TileEntity var2) {
      if(var2 != null && !var2.x()) {
         if(this.M) {
            var2.a(var1);
            Iterator var3 = this.b.iterator();

            while(var3.hasNext()) {
               TileEntity var4 = (TileEntity)var3.next();
               if(var4.getPosition().equals(var1)) {
                  var4.y();
                  var3.remove();
               }
            }

            this.b.add(var2);
         } else {
            this.a(var2);
            this.getChunkAtWorldCoords(var1).a(var1, var2);
         }
      }

   }

   public void t(BlockPosition var1) {
      TileEntity var2 = this.getTileEntity(var1);
      if(var2 != null && this.M) {
         var2.y();
         this.b.remove(var2);
      } else {
         if(var2 != null) {
            this.b.remove(var2);
            this.h.remove(var2);
            this.tileEntityList.remove(var2);
         }

         this.getChunkAtWorldCoords(var1).e(var1);
      }

   }

   public void b(TileEntity var1) {
      this.c.add(var1);
   }

   public boolean u(BlockPosition var1) {
      IBlockData var2 = this.getType(var1);
      AxisAlignedBB var3 = var2.getBlock().a(this, var1, var2);
      return var3 != null && var3.a() >= 1.0D;
   }

   public static boolean a(IBlockAccess var0, BlockPosition var1) {
      IBlockData var2 = var0.getType(var1);
      Block var3 = var2.getBlock();
      return var3.getMaterial().k() && var3.d()?true:(var3 instanceof BlockStairs?var2.get(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP:(var3 instanceof BlockStepAbstract?var2.get(BlockStepAbstract.HALF) == BlockStepAbstract.EnumSlabHalf.TOP:(var3 instanceof BlockHopper?true:(var3 instanceof BlockSnow?((Integer)var2.get(BlockSnow.LAYERS)).intValue() == 7:false))));
   }

   public boolean d(BlockPosition var1, boolean var2) {
      if(!this.isValidLocation(var1)) {
         return var2;
      } else {
         Chunk var3 = this.chunkProvider.getChunkAt(var1);
         if(var3.isEmpty()) {
            return var2;
         } else {
            Block var4 = this.getType(var1).getBlock();
            return var4.getMaterial().k() && var4.d();
         }
      }
   }

   public void B() {
      int var1 = this.a(1.0F);
      if(var1 != this.I) {
         this.I = var1;
      }

   }

   public void setSpawnFlags(boolean var1, boolean var2) {
      this.allowMonsters = var1;
      this.allowAnimals = var2;
   }

   public void doTick() {
      this.p();
   }

   protected void C() {
      if(this.worldData.hasStorm()) {
         this.p = 1.0F;
         if(this.worldData.isThundering()) {
            this.r = 1.0F;
         }
      }

   }

   protected void p() {
      if(!this.worldProvider.o()) {
         if(!this.isClientSide) {
            int var1 = this.worldData.A();
            if(var1 > 0) {
               --var1;
               this.worldData.i(var1);
               this.worldData.setThunderDuration(this.worldData.isThundering()?1:2);
               this.worldData.setWeatherDuration(this.worldData.hasStorm()?1:2);
            }

            int var2 = this.worldData.getThunderDuration();
            if(var2 <= 0) {
               if(this.worldData.isThundering()) {
                  this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
               } else {
                  this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
               }
            } else {
               --var2;
               this.worldData.setThunderDuration(var2);
               if(var2 <= 0) {
                  this.worldData.setThundering(!this.worldData.isThundering());
               }
            }

            this.q = this.r;
            if(this.worldData.isThundering()) {
               this.r = (float)((double)this.r + 0.01D);
            } else {
               this.r = (float)((double)this.r - 0.01D);
            }

            this.r = MathHelper.a(this.r, 0.0F, 1.0F);
            int var3 = this.worldData.getWeatherDuration();
            if(var3 <= 0) {
               if(this.worldData.hasStorm()) {
                  this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
               } else {
                  this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
               }
            } else {
               --var3;
               this.worldData.setWeatherDuration(var3);
               if(var3 <= 0) {
                  this.worldData.setStorm(!this.worldData.hasStorm());
               }
            }

            this.o = this.p;
            if(this.worldData.hasStorm()) {
               this.p = (float)((double)this.p + 0.01D);
            } else {
               this.p = (float)((double)this.p - 0.01D);
            }

            this.p = MathHelper.a(this.p, 0.0F, 1.0F);
         }
      }
   }

   protected void D() {
      this.chunkTickList.clear();
      this.methodProfiler.a("buildList");

      int var1;
      EntityHuman var2;
      int var3;
      int var4;
      int var5;
      for(var1 = 0; var1 < this.players.size(); ++var1) {
         var2 = (EntityHuman)this.players.get(var1);
         var3 = MathHelper.floor(var2.locX / 16.0D);
         var4 = MathHelper.floor(var2.locZ / 16.0D);
         var5 = this.q();

         for(int var6 = -var5; var6 <= var5; ++var6) {
            for(int var7 = -var5; var7 <= var5; ++var7) {
               this.chunkTickList.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
            }
         }
      }

      this.methodProfiler.b();
      if(this.L > 0) {
         --this.L;
      }

      this.methodProfiler.a("playerCheckLight");
      if(!this.players.isEmpty()) {
         var1 = this.random.nextInt(this.players.size());
         var2 = (EntityHuman)this.players.get(var1);
         var3 = MathHelper.floor(var2.locX) + this.random.nextInt(11) - 5;
         var4 = MathHelper.floor(var2.locY) + this.random.nextInt(11) - 5;
         var5 = MathHelper.floor(var2.locZ) + this.random.nextInt(11) - 5;
         this.x(new BlockPosition(var3, var4, var5));
      }

      this.methodProfiler.b();
   }

   protected abstract int q();

   protected void a(int var1, int var2, Chunk var3) {
      this.methodProfiler.c("moodSound");
      if(this.L == 0 && !this.isClientSide) {
         this.m = this.m * 3 + 1013904223;
         int var4 = this.m >> 2;
         int var5 = var4 & 15;
         int var6 = var4 >> 8 & 15;
         int var7 = var4 >> 16 & 255;
         BlockPosition var8 = new BlockPosition(var5, var7, var6);
         Block var9 = var3.getType(var8);
         var5 += var1;
         var6 += var2;
         if(var9.getMaterial() == Material.AIR && this.k(var8) <= this.random.nextInt(8) && this.b(EnumSkyBlock.SKY, var8) <= 0) {
            EntityHuman var10 = this.findNearbyPlayer((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, 8.0D);
            if(var10 != null && var10.e((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D) > 4.0D) {
               this.makeSound((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.random.nextFloat() * 0.2F);
               this.L = this.random.nextInt(12000) + 6000;
            }
         }
      }

      this.methodProfiler.c("checkLight");
      var3.m();
   }

   protected void h() {
      this.D();
   }

   public void a(Block var1, BlockPosition var2, Random var3) {
      this.e = true;
      var1.b(this, var2, this.getType(var2), var3);
      this.e = false;
   }

   public boolean v(BlockPosition var1) {
      return this.e(var1, false);
   }

   public boolean w(BlockPosition var1) {
      return this.e(var1, true);
   }

   public boolean e(BlockPosition var1, boolean var2) {
      BiomeBase var3 = this.getBiome(var1);
      float var4 = var3.a(var1);
      if(var4 > 0.15F) {
         return false;
      } else {
         if(var1.getY() >= 0 && var1.getY() < 256 && this.b(EnumSkyBlock.BLOCK, var1) < 10) {
            IBlockData var5 = this.getType(var1);
            Block var6 = var5.getBlock();
            if((var6 == Blocks.WATER || var6 == Blocks.FLOWING_WATER) && ((Integer)var5.get(BlockFluids.LEVEL)).intValue() == 0) {
               if(!var2) {
                  return true;
               }

               boolean var7 = this.F(var1.west()) && this.F(var1.east()) && this.F(var1.north()) && this.F(var1.south());
               if(!var7) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean F(BlockPosition var1) {
      return this.getType(var1).getBlock().getMaterial() == Material.WATER;
   }

   public boolean f(BlockPosition var1, boolean var2) {
      BiomeBase var3 = this.getBiome(var1);
      float var4 = var3.a(var1);
      if(var4 > 0.15F) {
         return false;
      } else if(!var2) {
         return true;
      } else {
         if(var1.getY() >= 0 && var1.getY() < 256 && this.b(EnumSkyBlock.BLOCK, var1) < 10) {
            Block var5 = this.getType(var1).getBlock();
            if(var5.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlace(this, var1)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean x(BlockPosition var1) {
      boolean var2 = false;
      if(!this.worldProvider.o()) {
         var2 |= this.c(EnumSkyBlock.SKY, var1);
      }

      var2 |= this.c(EnumSkyBlock.BLOCK, var1);
      return var2;
   }

   private int a(BlockPosition var1, EnumSkyBlock var2) {
      if(var2 == EnumSkyBlock.SKY && this.i(var1)) {
         return 15;
      } else {
         Block var3 = this.getType(var1).getBlock();
         int var4 = var2 == EnumSkyBlock.SKY?0:var3.r();
         int var5 = var3.p();
         if(var5 >= 15 && var3.r() > 0) {
            var5 = 1;
         }

         if(var5 < 1) {
            var5 = 1;
         }

         if(var5 >= 15) {
            return 0;
         } else if(var4 >= 14) {
            return var4;
         } else {
            EnumDirection[] var6 = EnumDirection.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               EnumDirection var9 = var6[var8];
               BlockPosition var10 = var1.shift(var9);
               int var11 = this.b(var2, var10) - var5;
               if(var11 > var4) {
                  var4 = var11;
               }

               if(var4 >= 14) {
                  return var4;
               }
            }

            return var4;
         }
      }
   }

   public boolean c(EnumSkyBlock var1, BlockPosition var2) {
      if(!this.areChunksLoaded(var2, 17, false)) {
         return false;
      } else {
         int var3 = 0;
         int var4 = 0;
         this.methodProfiler.a("getBrightness");
         int var5 = this.b(var1, var2);
         int var6 = this.a(var2, var1);
         int var7 = var2.getX();
         int var8 = var2.getY();
         int var9 = var2.getZ();
         int var10;
         int var11;
         int var12;
         int var13;
         int var16;
         int var17;
         int var18;
         int var19;
         if(var6 > var5) {
            this.H[var4++] = 133152;
         } else if(var6 < var5) {
            this.H[var4++] = 133152 | var5 << 18;

            label90:
            while(true) {
               int var14;
               do {
                  do {
                     BlockPosition var15;
                     do {
                        if(var3 >= var4) {
                           var3 = 0;
                           break label90;
                        }

                        var10 = this.H[var3++];
                        var11 = (var10 & 63) - 32 + var7;
                        var12 = (var10 >> 6 & 63) - 32 + var8;
                        var13 = (var10 >> 12 & 63) - 32 + var9;
                        var14 = var10 >> 18 & 15;
                        var15 = new BlockPosition(var11, var12, var13);
                        var16 = this.b(var1, var15);
                     } while(var16 != var14);

                     this.a((EnumSkyBlock)var1, (BlockPosition)var15, 0);
                  } while(var14 <= 0);

                  var17 = MathHelper.a(var11 - var7);
                  var18 = MathHelper.a(var12 - var8);
                  var19 = MathHelper.a(var13 - var9);
               } while(var17 + var18 + var19 >= 17);

               BlockPosition.MutableBlockPosition var20 = new BlockPosition.MutableBlockPosition();
               EnumDirection[] var21 = EnumDirection.values();
               int var22 = var21.length;

               for(int var23 = 0; var23 < var22; ++var23) {
                  EnumDirection var24 = var21[var23];
                  int var25 = var11 + var24.getAdjacentX();
                  int var26 = var12 + var24.getAdjacentY();
                  int var27 = var13 + var24.getAdjacentZ();
                  var20.c(var25, var26, var27);
                  int var28 = Math.max(1, this.getType(var20).getBlock().p());
                  var16 = this.b((EnumSkyBlock)var1, (BlockPosition)var20);
                  if(var16 == var14 - var28 && var4 < this.H.length) {
                     this.H[var4++] = var25 - var7 + 32 | var26 - var8 + 32 << 6 | var27 - var9 + 32 << 12 | var14 - var28 << 18;
                  }
               }
            }
         }

         this.methodProfiler.b();
         this.methodProfiler.a("checkedPosition < toCheckCount");

         while(var3 < var4) {
            var10 = this.H[var3++];
            var11 = (var10 & 63) - 32 + var7;
            var12 = (var10 >> 6 & 63) - 32 + var8;
            var13 = (var10 >> 12 & 63) - 32 + var9;
            BlockPosition var29 = new BlockPosition(var11, var12, var13);
            int var30 = this.b(var1, var29);
            var16 = this.a(var29, var1);
            if(var16 != var30) {
               this.a(var1, var29, var16);
               if(var16 > var30) {
                  var17 = Math.abs(var11 - var7);
                  var18 = Math.abs(var12 - var8);
                  var19 = Math.abs(var13 - var9);
                  boolean var31 = var4 < this.H.length - 6;
                  if(var17 + var18 + var19 < 17 && var31) {
                     if(this.b(var1, var29.west()) < var16) {
                        this.H[var4++] = var11 - 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if(this.b(var1, var29.east()) < var16) {
                        this.H[var4++] = var11 + 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if(this.b(var1, var29.down()) < var16) {
                        this.H[var4++] = var11 - var7 + 32 + (var12 - 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if(this.b(var1, var29.up()) < var16) {
                        this.H[var4++] = var11 - var7 + 32 + (var12 + 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if(this.b(var1, var29.north()) < var16) {
                        this.H[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - 1 - var9 + 32 << 12);
                     }

                     if(this.b(var1, var29.south()) < var16) {
                        this.H[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 + 1 - var9 + 32 << 12);
                     }
                  }
               }
            }
         }

         this.methodProfiler.b();
         return true;
      }
   }

   public boolean a(boolean var1) {
      return false;
   }

   public List<NextTickListEntry> a(Chunk var1, boolean var2) {
      return null;
   }

   public List<NextTickListEntry> a(StructureBoundingBox var1, boolean var2) {
      return null;
   }

   public List<Entity> getEntities(Entity var1, AxisAlignedBB var2) {
      return this.a(var1, var2, IEntitySelector.d);
   }

   public List<Entity> a(Entity var1, AxisAlignedBB var2, Predicate<? super Entity> var3) {
      ArrayList var4 = Lists.newArrayList();
      int var5 = MathHelper.floor((var2.a - 2.0D) / 16.0D);
      int var6 = MathHelper.floor((var2.d + 2.0D) / 16.0D);
      int var7 = MathHelper.floor((var2.c - 2.0D) / 16.0D);
      int var8 = MathHelper.floor((var2.f + 2.0D) / 16.0D);

      for(int var9 = var5; var9 <= var6; ++var9) {
         for(int var10 = var7; var10 <= var8; ++var10) {
            if(this.isChunkLoaded(var9, var10, true)) {
               this.getChunkAt(var9, var10).a((Entity)var1, var2, var4, var3);
            }
         }
      }

      return var4;
   }

   public <T extends Entity> List<T> a(Class<? extends T> var1, Predicate<? super T> var2) {
      ArrayList var3 = Lists.newArrayList();
      Iterator var4 = this.entityList.iterator();

      while(var4.hasNext()) {
         Entity var5 = (Entity)var4.next();
         if(var1.isAssignableFrom(var5.getClass()) && var2.apply(var5)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public <T extends Entity> List<T> b(Class<? extends T> var1, Predicate<? super T> var2) {
      ArrayList var3 = Lists.newArrayList();
      Iterator var4 = this.players.iterator();

      while(var4.hasNext()) {
         Entity var5 = (Entity)var4.next();
         if(var1.isAssignableFrom(var5.getClass()) && var2.apply(var5)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public <T extends Entity> List<T> a(Class<? extends T> var1, AxisAlignedBB var2) {
      return this.a(var1, var2, IEntitySelector.d);
   }

   public <T extends Entity> List<T> a(Class<? extends T> var1, AxisAlignedBB var2, Predicate<? super T> var3) {
      int var4 = MathHelper.floor((var2.a - 2.0D) / 16.0D);
      int var5 = MathHelper.floor((var2.d + 2.0D) / 16.0D);
      int var6 = MathHelper.floor((var2.c - 2.0D) / 16.0D);
      int var7 = MathHelper.floor((var2.f + 2.0D) / 16.0D);
      ArrayList var8 = Lists.newArrayList();

      for(int var9 = var4; var9 <= var5; ++var9) {
         for(int var10 = var6; var10 <= var7; ++var10) {
            if(this.isChunkLoaded(var9, var10, true)) {
               this.getChunkAt(var9, var10).a((Class)var1, var2, var8, var3);
            }
         }
      }

      return var8;
   }

   public <T extends Entity> T a(Class<? extends T> var1, AxisAlignedBB var2, T var3) {
      List var4 = this.a(var1, var2);
      Entity var5 = null;
      double var6 = Double.MAX_VALUE;

      for(int var8 = 0; var8 < var4.size(); ++var8) {
         Entity var9 = (Entity)var4.get(var8);
         if(var9 != var3 && IEntitySelector.d.apply(var9)) {
            double var10 = var3.h(var9);
            if(var10 <= var6) {
               var5 = var9;
               var6 = var10;
            }
         }
      }

      return var5;
   }

   public Entity a(int var1) {
      return (Entity)this.entitiesById.get(var1);
   }

   public void b(BlockPosition var1, TileEntity var2) {
      if(this.isLoaded(var1)) {
         this.getChunkAtWorldCoords(var1).e();
      }

   }

   public int a(Class<?> var1) {
      int var2 = 0;
      Iterator var3 = this.entityList.iterator();

      while(true) {
         Entity var4;
         do {
            if(!var3.hasNext()) {
               return var2;
            }

            var4 = (Entity)var3.next();
         } while(var4 instanceof EntityInsentient && ((EntityInsentient)var4).isPersistent());

         if(var1.isAssignableFrom(var4.getClass())) {
            ++var2;
         }
      }
   }

   public void b(Collection<Entity> var1) {
      this.entityList.addAll(var1);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Entity var3 = (Entity)var2.next();
         this.a(var3);
      }

   }

   public void c(Collection<Entity> var1) {
      this.g.addAll(var1);
   }

   public boolean a(Block var1, BlockPosition var2, boolean var3, EnumDirection var4, Entity var5, ItemStack var6) {
      Block var7 = this.getType(var2).getBlock();
      AxisAlignedBB var8 = var3?null:var1.a(this, var2, var1.getBlockData());
      return var8 != null && !this.a(var8, var5)?false:(var7.getMaterial() == Material.ORIENTABLE && var1 == Blocks.ANVIL?true:var7.getMaterial().isReplaceable() && var1.canPlace(this, var2, var4, var6));
   }

   public int F() {
      return this.a;
   }

   public void b(int var1) {
      this.a = var1;
   }

   public int getBlockPower(BlockPosition var1, EnumDirection var2) {
      IBlockData var3 = this.getType(var1);
      return var3.getBlock().b((IBlockAccess)this, var1, var3, (EnumDirection)var2);
   }

   public WorldType G() {
      return this.worldData.getType();
   }

   public int getBlockPower(BlockPosition var1) {
      byte var2 = 0;
      int var3 = Math.max(var2, this.getBlockPower(var1.down(), EnumDirection.DOWN));
      if(var3 >= 15) {
         return var3;
      } else {
         var3 = Math.max(var3, this.getBlockPower(var1.up(), EnumDirection.UP));
         if(var3 >= 15) {
            return var3;
         } else {
            var3 = Math.max(var3, this.getBlockPower(var1.north(), EnumDirection.NORTH));
            if(var3 >= 15) {
               return var3;
            } else {
               var3 = Math.max(var3, this.getBlockPower(var1.south(), EnumDirection.SOUTH));
               if(var3 >= 15) {
                  return var3;
               } else {
                  var3 = Math.max(var3, this.getBlockPower(var1.west(), EnumDirection.WEST));
                  if(var3 >= 15) {
                     return var3;
                  } else {
                     var3 = Math.max(var3, this.getBlockPower(var1.east(), EnumDirection.EAST));
                     return var3 >= 15?var3:var3;
                  }
               }
            }
         }
      }
   }

   public boolean isBlockFacePowered(BlockPosition var1, EnumDirection var2) {
      return this.getBlockFacePower(var1, var2) > 0;
   }

   public int getBlockFacePower(BlockPosition var1, EnumDirection var2) {
      IBlockData var3 = this.getType(var1);
      Block var4 = var3.getBlock();
      return var4.isOccluding()?this.getBlockPower(var1):var4.a((IBlockAccess)this, var1, (IBlockData)var3, (EnumDirection)var2);
   }

   public boolean isBlockIndirectlyPowered(BlockPosition var1) {
      return this.getBlockFacePower(var1.down(), EnumDirection.DOWN) > 0?true:(this.getBlockFacePower(var1.up(), EnumDirection.UP) > 0?true:(this.getBlockFacePower(var1.north(), EnumDirection.NORTH) > 0?true:(this.getBlockFacePower(var1.south(), EnumDirection.SOUTH) > 0?true:(this.getBlockFacePower(var1.west(), EnumDirection.WEST) > 0?true:this.getBlockFacePower(var1.east(), EnumDirection.EAST) > 0))));
   }

   public int A(BlockPosition var1) {
      int var2 = 0;
      EnumDirection[] var3 = EnumDirection.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumDirection var6 = var3[var5];
         int var7 = this.getBlockFacePower(var1.shift(var6), var6);
         if(var7 >= 15) {
            return 15;
         }

         if(var7 > var2) {
            var2 = var7;
         }
      }

      return var2;
   }

   public EntityHuman findNearbyPlayer(Entity var1, double var2) {
      return this.findNearbyPlayer(var1.locX, var1.locY, var1.locZ, var2);
   }

   public EntityHuman findNearbyPlayer(double var1, double var3, double var5, double var7) {
      double var9 = -1.0D;
      EntityHuman var11 = null;

      for(int var12 = 0; var12 < this.players.size(); ++var12) {
         EntityHuman var13 = (EntityHuman)this.players.get(var12);
         if(IEntitySelector.d.apply(var13)) {
            double var14 = var13.e(var1, var3, var5);
            if((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
               var9 = var14;
               var11 = var13;
            }
         }
      }

      return var11;
   }

   public boolean isPlayerNearby(double var1, double var3, double var5, double var7) {
      for(int var9 = 0; var9 < this.players.size(); ++var9) {
         EntityHuman var10 = (EntityHuman)this.players.get(var9);
         if(IEntitySelector.d.apply(var10)) {
            double var11 = var10.e(var1, var3, var5);
            if(var7 < 0.0D || var11 < var7 * var7) {
               return true;
            }
         }
      }

      return false;
   }

   public EntityHuman a(String var1) {
      for(int var2 = 0; var2 < this.players.size(); ++var2) {
         EntityHuman var3 = (EntityHuman)this.players.get(var2);
         if(var1.equals(var3.getName())) {
            return var3;
         }
      }

      return null;
   }

   public EntityHuman b(UUID var1) {
      for(int var2 = 0; var2 < this.players.size(); ++var2) {
         EntityHuman var3 = (EntityHuman)this.players.get(var2);
         if(var1.equals(var3.getUniqueID())) {
            return var3;
         }
      }

      return null;
   }

   public void checkSession() throws ExceptionWorldConflict {
      this.dataManager.checkSession();
   }

   public long getSeed() {
      return this.worldData.getSeed();
   }

   public long getTime() {
      return this.worldData.getTime();
   }

   public long getDayTime() {
      return this.worldData.getDayTime();
   }

   public void setDayTime(long var1) {
      this.worldData.setDayTime(var1);
   }

   public BlockPosition getSpawn() {
      BlockPosition var1 = new BlockPosition(this.worldData.c(), this.worldData.d(), this.worldData.e());
      if(!this.getWorldBorder().a(var1)) {
         var1 = this.getHighestBlockYAt(new BlockPosition(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
      }

      return var1;
   }

   public void B(BlockPosition var1) {
      this.worldData.setSpawn(var1);
   }

   public boolean a(EntityHuman var1, BlockPosition var2) {
      return true;
   }

   public void broadcastEntityEffect(Entity var1, byte var2) {
   }

   public IChunkProvider N() {
      return this.chunkProvider;
   }

   public void playBlockAction(BlockPosition var1, Block var2, int var3, int var4) {
      var2.a(this, var1, this.getType(var1), var3, var4);
   }

   public IDataManager getDataManager() {
      return this.dataManager;
   }

   public WorldData getWorldData() {
      return this.worldData;
   }

   public GameRules getGameRules() {
      return this.worldData.x();
   }

   public void everyoneSleeping() {
   }

   public float h(float var1) {
      return (this.q + (this.r - this.q) * var1) * this.j(var1);
   }

   public float j(float var1) {
      return this.o + (this.p - this.o) * var1;
   }

   public boolean R() {
      return (double)this.h(1.0F) > 0.9D;
   }

   public boolean S() {
      return (double)this.j(1.0F) > 0.2D;
   }

   public boolean isRainingAt(BlockPosition var1) {
      if(!this.S()) {
         return false;
      } else if(!this.i(var1)) {
         return false;
      } else if(this.q(var1).getY() > var1.getY()) {
         return false;
      } else {
         BiomeBase var2 = this.getBiome(var1);
         return var2.d()?false:(this.f(var1, false)?false:var2.e());
      }
   }

   public boolean D(BlockPosition var1) {
      BiomeBase var2 = this.getBiome(var1);
      return var2.f();
   }

   public PersistentCollection T() {
      return this.worldMaps;
   }

   public void a(String var1, PersistentBase var2) {
      this.worldMaps.a(var1, var2);
   }

   public PersistentBase a(Class<? extends PersistentBase> var1, String var2) {
      return this.worldMaps.get(var1, var2);
   }

   public int b(String var1) {
      return this.worldMaps.a(var1);
   }

   public void a(int var1, BlockPosition var2, int var3) {
      for(int var4 = 0; var4 < this.u.size(); ++var4) {
         ((IWorldAccess)this.u.get(var4)).a(var1, var2, var3);
      }

   }

   public void triggerEffect(int var1, BlockPosition var2, int var3) {
      this.a((EntityHuman)null, var1, var2, var3);
   }

   public void a(EntityHuman var1, int var2, BlockPosition var3, int var4) {
      try {
         for(int var5 = 0; var5 < this.u.size(); ++var5) {
            ((IWorldAccess)this.u.get(var5)).a(var1, var2, var3, var4);
         }

      } catch (Throwable var8) {
         CrashReport var6 = CrashReport.a(var8, "Playing level event");
         CrashReportSystemDetails var7 = var6.a("Level event being played");
         var7.a((String)"Block coordinates", (Object)CrashReportSystemDetails.a(var3));
         var7.a((String)"Event source", (Object)var1);
         var7.a((String)"Event type", (Object)Integer.valueOf(var2));
         var7.a((String)"Event data", (Object)Integer.valueOf(var4));
         throw new ReportedException(var6);
      }
   }

   public int getHeight() {
      return 256;
   }

   public int V() {
      return this.worldProvider.o()?128:256;
   }

   public Random a(int var1, int var2, int var3) {
      long var4 = (long)var1 * 341873128712L + (long)var2 * 132897987541L + this.getWorldData().getSeed() + (long)var3;
      this.random.setSeed(var4);
      return this.random;
   }

   public BlockPosition a(String var1, BlockPosition var2) {
      return this.N().findNearestMapFeature(this, var1, var2);
   }

   public CrashReportSystemDetails a(CrashReport var1) {
      CrashReportSystemDetails var2 = var1.a("Affected level", 1);
      var2.a((String)"Level name", (Object)(this.worldData == null?"????":this.worldData.getName()));
      var2.a("All players", new Callable() {
         public String a() {
            return World.this.players.size() + " total; " + World.this.players.toString();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var2.a("Chunk stats", new Callable() {
         public String a() {
            return World.this.chunkProvider.getName();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });

      try {
         this.worldData.a(var2);
      } catch (Throwable var4) {
         var2.a("Level Data Unobtainable", var4);
      }

      return var2;
   }

   public void c(int var1, BlockPosition var2, int var3) {
      for(int var4 = 0; var4 < this.u.size(); ++var4) {
         IWorldAccess var5 = (IWorldAccess)this.u.get(var4);
         var5.b(var1, var2, var3);
      }

   }

   public Calendar Y() {
      if(this.getTime() % 600L == 0L) {
         this.K.setTimeInMillis(MinecraftServer.az());
      }

      return this.K;
   }

   public Scoreboard getScoreboard() {
      return this.scoreboard;
   }

   public void updateAdjacentComparators(BlockPosition var1, Block var2) {
      Iterator var3 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

      while(var3.hasNext()) {
         EnumDirection var4 = (EnumDirection)var3.next();
         BlockPosition var5 = var1.shift(var4);
         if(this.isLoaded(var5)) {
            IBlockData var6 = this.getType(var5);
            if(Blocks.UNPOWERED_COMPARATOR.e(var6.getBlock())) {
               var6.getBlock().doPhysics(this, var5, var6, var2);
            } else if(var6.getBlock().isOccluding()) {
               var5 = var5.shift(var4);
               var6 = this.getType(var5);
               if(Blocks.UNPOWERED_COMPARATOR.e(var6.getBlock())) {
                  var6.getBlock().doPhysics(this, var5, var6, var2);
               }
            }
         }
      }

   }

   public DifficultyDamageScaler E(BlockPosition var1) {
      long var2 = 0L;
      float var4 = 0.0F;
      if(this.isLoaded(var1)) {
         var4 = this.y();
         var2 = this.getChunkAtWorldCoords(var1).w();
      }

      return new DifficultyDamageScaler(this.getDifficulty(), this.getDayTime(), var2, var4);
   }

   public EnumDifficulty getDifficulty() {
      return this.getWorldData().getDifficulty();
   }

   public int ab() {
      return this.I;
   }

   public void c(int var1) {
      this.I = var1;
   }

   public void d(int var1) {
      this.J = var1;
   }

   public boolean ad() {
      return this.isLoading;
   }

   public PersistentVillage ae() {
      return this.villages;
   }

   public WorldBorder getWorldBorder() {
      return this.N;
   }

   public boolean c(int var1, int var2) {
      BlockPosition var3 = this.getSpawn();
      int var4 = var1 * 16 + 8 - var3.getX();
      int var5 = var2 * 16 + 8 - var3.getZ();
      short var6 = 128;
      return var4 >= -var6 && var4 <= var6 && var5 >= -var6 && var5 <= var6;
   }
}
