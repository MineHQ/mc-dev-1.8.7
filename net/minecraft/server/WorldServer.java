package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockActionData;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkProviderServer;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTracker;
import net.minecraft.server.EntityWaterAnimal;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.Explosion;
import net.minecraft.server.IAsyncTaskHandler;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IDataManager;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NPC;
import net.minecraft.server.NextTickListEntry;
import net.minecraft.server.PacketPlayOutBlockAction;
import net.minecraft.server.PacketPlayOutEntityStatus;
import net.minecraft.server.PacketPlayOutExplosion;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.PacketPlayOutWorldParticles;
import net.minecraft.server.PersistentCollection;
import net.minecraft.server.PersistentScoreboard;
import net.minecraft.server.PersistentVillage;
import net.minecraft.server.PlayerChunkMap;
import net.minecraft.server.PortalTravelAgent;
import net.minecraft.server.ReportedException;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.SpawnerCreature;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePieceTreasure;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.VillageSiege;
import net.minecraft.server.WeightedRandom;
import net.minecraft.server.World;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldGenBonusChest;
import net.minecraft.server.WorldProvider;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer extends World implements IAsyncTaskHandler {
   private static final Logger a = LogManager.getLogger();
   private final MinecraftServer server;
   public EntityTracker tracker;
   private final PlayerChunkMap manager;
   private final Set<NextTickListEntry> L = Sets.newHashSet();
   private final TreeSet<NextTickListEntry> M = new TreeSet();
   private final Map<UUID, Entity> entitiesByUUID = Maps.newHashMap();
   public ChunkProviderServer chunkProviderServer;
   public boolean savingDisabled;
   private boolean O;
   private int emptyTime;
   private final PortalTravelAgent Q;
   private final SpawnerCreature R = new SpawnerCreature();
   protected final VillageSiege siegeManager = new VillageSiege(this);
   private WorldServer.BlockActionDataList[] S = new WorldServer.BlockActionDataList[]{new WorldServer.BlockActionDataList(null), new WorldServer.BlockActionDataList(null)};
   private int T;
   private static final List<StructurePieceTreasure> U;
   private List<NextTickListEntry> V = Lists.newArrayList();

   public WorldServer(MinecraftServer var1, IDataManager var2, WorldData var3, int var4, MethodProfiler var5) {
      super(var2, var3, WorldProvider.byDimension(var4), var5, false);
      this.server = var1;
      this.tracker = new EntityTracker(this);
      this.manager = new PlayerChunkMap(this);
      this.worldProvider.a(this);
      this.chunkProvider = this.k();
      this.Q = new PortalTravelAgent(this);
      this.B();
      this.C();
      this.getWorldBorder().a(var1.aI());
   }

   public World b() {
      this.worldMaps = new PersistentCollection(this.dataManager);
      String var1 = PersistentVillage.a(this.worldProvider);
      PersistentVillage var2 = (PersistentVillage)this.worldMaps.get(PersistentVillage.class, var1);
      if(var2 == null) {
         this.villages = new PersistentVillage(this);
         this.worldMaps.a(var1, this.villages);
      } else {
         this.villages = var2;
         this.villages.a((World)this);
      }

      this.scoreboard = new ScoreboardServer(this.server);
      PersistentScoreboard var3 = (PersistentScoreboard)this.worldMaps.get(PersistentScoreboard.class, "scoreboard");
      if(var3 == null) {
         var3 = new PersistentScoreboard();
         this.worldMaps.a("scoreboard", var3);
      }

      var3.a(this.scoreboard);
      ((ScoreboardServer)this.scoreboard).a(var3);
      this.getWorldBorder().setCenter(this.worldData.C(), this.worldData.D());
      this.getWorldBorder().setDamageAmount(this.worldData.I());
      this.getWorldBorder().setDamageBuffer(this.worldData.H());
      this.getWorldBorder().setWarningDistance(this.worldData.J());
      this.getWorldBorder().setWarningTime(this.worldData.K());
      if(this.worldData.F() > 0L) {
         this.getWorldBorder().transitionSizeBetween(this.worldData.E(), this.worldData.G(), this.worldData.F());
      } else {
         this.getWorldBorder().setSize(this.worldData.E());
      }

      return this;
   }

   public void doTick() {
      super.doTick();
      if(this.getWorldData().isHardcore() && this.getDifficulty() != EnumDifficulty.HARD) {
         this.getWorldData().setDifficulty(EnumDifficulty.HARD);
      }

      this.worldProvider.m().b();
      if(this.everyoneDeeplySleeping()) {
         if(this.getGameRules().getBoolean("doDaylightCycle")) {
            long var1 = this.worldData.getDayTime() + 24000L;
            this.worldData.setDayTime(var1 - var1 % 24000L);
         }

         this.e();
      }

      this.methodProfiler.a("mobSpawner");
      if(this.getGameRules().getBoolean("doMobSpawning") && this.worldData.getType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.R.a(this, this.allowMonsters, this.allowAnimals, this.worldData.getTime() % 400L == 0L);
      }

      this.methodProfiler.c("chunkSource");
      this.chunkProvider.unloadChunks();
      int var3 = this.a(1.0F);
      if(var3 != this.ab()) {
         this.c(var3);
      }

      this.worldData.setTime(this.worldData.getTime() + 1L);
      if(this.getGameRules().getBoolean("doDaylightCycle")) {
         this.worldData.setDayTime(this.worldData.getDayTime() + 1L);
      }

      this.methodProfiler.c("tickPending");
      this.a(false);
      this.methodProfiler.c("tickBlocks");
      this.h();
      this.methodProfiler.c("chunkMap");
      this.manager.flush();
      this.methodProfiler.c("village");
      this.villages.tick();
      this.siegeManager.a();
      this.methodProfiler.c("portalForcer");
      this.Q.a(this.getTime());
      this.methodProfiler.b();
      this.ak();
   }

   public BiomeBase.BiomeMeta a(EnumCreatureType var1, BlockPosition var2) {
      List var3 = this.N().getMobsFor(var1, var2);
      return var3 != null && !var3.isEmpty()?(BiomeBase.BiomeMeta)WeightedRandom.a(this.random, var3):null;
   }

   public boolean a(EnumCreatureType var1, BiomeBase.BiomeMeta var2, BlockPosition var3) {
      List var4 = this.N().getMobsFor(var1, var3);
      return var4 != null && !var4.isEmpty()?var4.contains(var2):false;
   }

   public void everyoneSleeping() {
      this.O = false;
      if(!this.players.isEmpty()) {
         int var1 = 0;
         int var2 = 0;
         Iterator var3 = this.players.iterator();

         while(var3.hasNext()) {
            EntityHuman var4 = (EntityHuman)var3.next();
            if(var4.isSpectator()) {
               ++var1;
            } else if(var4.isSleeping()) {
               ++var2;
            }
         }

         this.O = var2 > 0 && var2 >= this.players.size() - var1;
      }

   }

   protected void e() {
      this.O = false;
      Iterator var1 = this.players.iterator();

      while(var1.hasNext()) {
         EntityHuman var2 = (EntityHuman)var1.next();
         if(var2.isSleeping()) {
            var2.a(false, false, true);
         }
      }

      this.ag();
   }

   private void ag() {
      this.worldData.setWeatherDuration(0);
      this.worldData.setStorm(false);
      this.worldData.setThunderDuration(0);
      this.worldData.setThundering(false);
   }

   public boolean everyoneDeeplySleeping() {
      if(this.O && !this.isClientSide) {
         Iterator var1 = this.players.iterator();

         EntityHuman var2;
         do {
            if(!var1.hasNext()) {
               return true;
            }

            var2 = (EntityHuman)var1.next();
         } while(!var2.isSpectator() && var2.isDeeplySleeping());

         return false;
      } else {
         return false;
      }
   }

   protected void h() {
      super.h();
      if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         Iterator var20 = this.chunkTickList.iterator();

         while(var20.hasNext()) {
            ChunkCoordIntPair var21 = (ChunkCoordIntPair)var20.next();
            this.getChunkAt(var21.x, var21.z).b(false);
         }

      } else {
         int var1 = 0;
         int var2 = 0;

         for(Iterator var3 = this.chunkTickList.iterator(); var3.hasNext(); this.methodProfiler.b()) {
            ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
            int var5 = var4.x * 16;
            int var6 = var4.z * 16;
            this.methodProfiler.a("getChunk");
            Chunk var7 = this.getChunkAt(var4.x, var4.z);
            this.a(var5, var6, var7);
            this.methodProfiler.c("tickChunk");
            var7.b(false);
            this.methodProfiler.c("thunder");
            int var8;
            BlockPosition var9;
            if(this.random.nextInt(100000) == 0 && this.S() && this.R()) {
               this.m = this.m * 3 + 1013904223;
               var8 = this.m >> 2;
               var9 = this.a(new BlockPosition(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));
               if(this.isRainingAt(var9)) {
                  this.strikeLightning(new EntityLightning(this, (double)var9.getX(), (double)var9.getY(), (double)var9.getZ()));
               }
            }

            this.methodProfiler.c("iceandsnow");
            if(this.random.nextInt(16) == 0) {
               this.m = this.m * 3 + 1013904223;
               var8 = this.m >> 2;
               var9 = this.q(new BlockPosition(var5 + (var8 & 15), 0, var6 + (var8 >> 8 & 15)));
               BlockPosition var10 = var9.down();
               if(this.w(var10)) {
                  this.setTypeUpdate(var10, Blocks.ICE.getBlockData());
               }

               if(this.S() && this.f(var9, true)) {
                  this.setTypeUpdate(var9, Blocks.SNOW_LAYER.getBlockData());
               }

               if(this.S() && this.getBiome(var10).e()) {
                  this.getType(var10).getBlock().k(this, var10);
               }
            }

            this.methodProfiler.c("tickBlocks");
            var8 = this.getGameRules().c("randomTickSpeed");
            if(var8 > 0) {
               ChunkSection[] var22 = var7.getSections();
               int var23 = var22.length;

               for(int var11 = 0; var11 < var23; ++var11) {
                  ChunkSection var12 = var22[var11];
                  if(var12 != null && var12.shouldTick()) {
                     for(int var13 = 0; var13 < var8; ++var13) {
                        this.m = this.m * 3 + 1013904223;
                        int var14 = this.m >> 2;
                        int var15 = var14 & 15;
                        int var16 = var14 >> 8 & 15;
                        int var17 = var14 >> 16 & 15;
                        ++var2;
                        IBlockData var18 = var12.getType(var15, var17, var16);
                        Block var19 = var18.getBlock();
                        if(var19.isTicking()) {
                           ++var1;
                           var19.a((World)this, new BlockPosition(var15 + var5, var17 + var12.getYPosition(), var16 + var6), (IBlockData)var18, (Random)this.random);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   protected BlockPosition a(BlockPosition var1) {
      BlockPosition var2 = this.q(var1);
      AxisAlignedBB var3 = (new AxisAlignedBB(var2, new BlockPosition(var2.getX(), this.getHeight(), var2.getZ()))).grow(3.0D, 3.0D, 3.0D);
      List var4 = this.a(EntityLiving.class, var3, new Predicate() {
         public boolean a(EntityLiving var1) {
            return var1 != null && var1.isAlive() && WorldServer.this.i(var1.getChunkCoordinates());
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((EntityLiving)var1);
         }
      });
      return !var4.isEmpty()?((EntityLiving)var4.get(this.random.nextInt(var4.size()))).getChunkCoordinates():var2;
   }

   public boolean a(BlockPosition var1, Block var2) {
      NextTickListEntry var3 = new NextTickListEntry(var1, var2);
      return this.V.contains(var3);
   }

   public void a(BlockPosition var1, Block var2, int var3) {
      this.a(var1, var2, var3, 0);
   }

   public void a(BlockPosition var1, Block var2, int var3, int var4) {
      NextTickListEntry var5 = new NextTickListEntry(var1, var2);
      byte var6 = 0;
      if(this.e && var2.getMaterial() != Material.AIR) {
         if(var2.N()) {
            var6 = 8;
            if(this.areChunksLoadedBetween(var5.a.a(-var6, -var6, -var6), var5.a.a(var6, var6, var6))) {
               IBlockData var7 = this.getType(var5.a);
               if(var7.getBlock().getMaterial() != Material.AIR && var7.getBlock() == var5.a()) {
                  var7.getBlock().b((World)this, var5.a, var7, (Random)this.random);
               }
            }

            return;
         }

         var3 = 1;
      }

      if(this.areChunksLoadedBetween(var1.a(-var6, -var6, -var6), var1.a(var6, var6, var6))) {
         if(var2.getMaterial() != Material.AIR) {
            var5.a((long)var3 + this.worldData.getTime());
            var5.a(var4);
         }

         if(!this.L.contains(var5)) {
            this.L.add(var5);
            this.M.add(var5);
         }
      }

   }

   public void b(BlockPosition var1, Block var2, int var3, int var4) {
      NextTickListEntry var5 = new NextTickListEntry(var1, var2);
      var5.a(var4);
      if(var2.getMaterial() != Material.AIR) {
         var5.a((long)var3 + this.worldData.getTime());
      }

      if(!this.L.contains(var5)) {
         this.L.add(var5);
         this.M.add(var5);
      }

   }

   public void tickEntities() {
      if(this.players.isEmpty()) {
         if(this.emptyTime++ >= 1200) {
            return;
         }
      } else {
         this.j();
      }

      super.tickEntities();
   }

   public void j() {
      this.emptyTime = 0;
   }

   public boolean a(boolean var1) {
      if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         return false;
      } else {
         int var2 = this.M.size();
         if(var2 != this.L.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
         } else {
            if(var2 > 1000) {
               var2 = 1000;
            }

            this.methodProfiler.a("cleaning");

            NextTickListEntry var4;
            for(int var3 = 0; var3 < var2; ++var3) {
               var4 = (NextTickListEntry)this.M.first();
               if(!var1 && var4.b > this.worldData.getTime()) {
                  break;
               }

               this.M.remove(var4);
               this.L.remove(var4);
               this.V.add(var4);
            }

            this.methodProfiler.b();
            this.methodProfiler.a("ticking");
            Iterator var11 = this.V.iterator();

            while(var11.hasNext()) {
               var4 = (NextTickListEntry)var11.next();
               var11.remove();
               byte var5 = 0;
               if(this.areChunksLoadedBetween(var4.a.a(-var5, -var5, -var5), var4.a.a(var5, var5, var5))) {
                  IBlockData var6 = this.getType(var4.a);
                  if(var6.getBlock().getMaterial() != Material.AIR && Block.a(var6.getBlock(), var4.a())) {
                     try {
                        var6.getBlock().b((World)this, var4.a, var6, (Random)this.random);
                     } catch (Throwable var10) {
                        CrashReport var8 = CrashReport.a(var10, "Exception while ticking a block");
                        CrashReportSystemDetails var9 = var8.a("Block being ticked");
                        CrashReportSystemDetails.a(var9, var4.a, var6);
                        throw new ReportedException(var8);
                     }
                  }
               } else {
                  this.a(var4.a, var4.a(), 0);
               }
            }

            this.methodProfiler.b();
            this.V.clear();
            return !this.M.isEmpty();
         }
      }
   }

   public List<NextTickListEntry> a(Chunk var1, boolean var2) {
      ChunkCoordIntPair var3 = var1.j();
      int var4 = (var3.x << 4) - 2;
      int var5 = var4 + 16 + 2;
      int var6 = (var3.z << 4) - 2;
      int var7 = var6 + 16 + 2;
      return this.a(new StructureBoundingBox(var4, 0, var6, var5, 256, var7), var2);
   }

   public List<NextTickListEntry> a(StructureBoundingBox var1, boolean var2) {
      ArrayList var3 = null;

      for(int var4 = 0; var4 < 2; ++var4) {
         Iterator var5;
         if(var4 == 0) {
            var5 = this.M.iterator();
         } else {
            var5 = this.V.iterator();
         }

         while(var5.hasNext()) {
            NextTickListEntry var6 = (NextTickListEntry)var5.next();
            BlockPosition var7 = var6.a;
            if(var7.getX() >= var1.a && var7.getX() < var1.d && var7.getZ() >= var1.c && var7.getZ() < var1.f) {
               if(var2) {
                  this.L.remove(var6);
                  var5.remove();
               }

               if(var3 == null) {
                  var3 = Lists.newArrayList();
               }

               var3.add(var6);
            }
         }
      }

      return var3;
   }

   public void entityJoinedWorld(Entity var1, boolean var2) {
      if(!this.getSpawnAnimals() && (var1 instanceof EntityAnimal || var1 instanceof EntityWaterAnimal)) {
         var1.die();
      }

      if(!this.getSpawnNPCs() && var1 instanceof NPC) {
         var1.die();
      }

      super.entityJoinedWorld(var1, var2);
   }

   private boolean getSpawnNPCs() {
      return this.server.getSpawnNPCs();
   }

   private boolean getSpawnAnimals() {
      return this.server.getSpawnAnimals();
   }

   protected IChunkProvider k() {
      IChunkLoader var1 = this.dataManager.createChunkLoader(this.worldProvider);
      this.chunkProviderServer = new ChunkProviderServer(this, var1, this.worldProvider.getChunkProvider());
      return this.chunkProviderServer;
   }

   public List<TileEntity> getTileEntities(int var1, int var2, int var3, int var4, int var5, int var6) {
      ArrayList var7 = Lists.newArrayList();

      for(int var8 = 0; var8 < this.h.size(); ++var8) {
         TileEntity var9 = (TileEntity)this.h.get(var8);
         BlockPosition var10 = var9.getPosition();
         if(var10.getX() >= var1 && var10.getY() >= var2 && var10.getZ() >= var3 && var10.getX() < var4 && var10.getY() < var5 && var10.getZ() < var6) {
            var7.add(var9);
         }
      }

      return var7;
   }

   public boolean a(EntityHuman var1, BlockPosition var2) {
      return !this.server.a(this, var2, var1) && this.getWorldBorder().a(var2);
   }

   public void a(WorldSettings var1) {
      if(!this.worldData.w()) {
         try {
            this.b(var1);
            if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
               this.aj();
            }

            super.a(var1);
         } catch (Throwable var6) {
            CrashReport var3 = CrashReport.a(var6, "Exception initializing level");

            try {
               this.a((CrashReport)var3);
            } catch (Throwable var5) {
               ;
            }

            throw new ReportedException(var3);
         }

         this.worldData.d(true);
      }

   }

   private void aj() {
      this.worldData.f(false);
      this.worldData.c(true);
      this.worldData.setStorm(false);
      this.worldData.setThundering(false);
      this.worldData.i(1000000000);
      this.worldData.setDayTime(6000L);
      this.worldData.setGameType(WorldSettings.EnumGamemode.SPECTATOR);
      this.worldData.g(false);
      this.worldData.setDifficulty(EnumDifficulty.PEACEFUL);
      this.worldData.e(true);
      this.getGameRules().set("doDaylightCycle", "false");
   }

   private void b(WorldSettings var1) {
      if(!this.worldProvider.e()) {
         this.worldData.setSpawn(BlockPosition.ZERO.up(this.worldProvider.getSeaLevel()));
      } else if(this.worldData.getType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         this.worldData.setSpawn(BlockPosition.ZERO.up());
      } else {
         this.isLoading = true;
         WorldChunkManager var2 = this.worldProvider.m();
         List var3 = var2.a();
         Random var4 = new Random(this.getSeed());
         BlockPosition var5 = var2.a(0, 0, 256, var3, var4);
         int var6 = 0;
         int var7 = this.worldProvider.getSeaLevel();
         int var8 = 0;
         if(var5 != null) {
            var6 = var5.getX();
            var8 = var5.getZ();
         } else {
            a.warn("Unable to find spawn biome");
         }

         int var9 = 0;

         while(!this.worldProvider.canSpawn(var6, var8)) {
            var6 += var4.nextInt(64) - var4.nextInt(64);
            var8 += var4.nextInt(64) - var4.nextInt(64);
            ++var9;
            if(var9 == 1000) {
               break;
            }
         }

         this.worldData.setSpawn(new BlockPosition(var6, var7, var8));
         this.isLoading = false;
         if(var1.c()) {
            this.l();
         }

      }
   }

   protected void l() {
      WorldGenBonusChest var1 = new WorldGenBonusChest(U, 10);

      for(int var2 = 0; var2 < 10; ++var2) {
         int var3 = this.worldData.c() + this.random.nextInt(6) - this.random.nextInt(6);
         int var4 = this.worldData.e() + this.random.nextInt(6) - this.random.nextInt(6);
         BlockPosition var5 = this.r(new BlockPosition(var3, 0, var4)).up();
         if(var1.generate(this, this.random, var5)) {
            break;
         }
      }

   }

   public BlockPosition getDimensionSpawn() {
      return this.worldProvider.h();
   }

   public void save(boolean var1, IProgressUpdate var2) throws ExceptionWorldConflict {
      if(this.chunkProvider.canSave()) {
         if(var2 != null) {
            var2.a("Saving level");
         }

         this.a();
         if(var2 != null) {
            var2.c("Saving chunks");
         }

         this.chunkProvider.saveChunks(var1, var2);
         ArrayList var3 = Lists.newArrayList((Iterable)this.chunkProviderServer.a());
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            Chunk var5 = (Chunk)var4.next();
            if(var5 != null && !this.manager.a(var5.locX, var5.locZ)) {
               this.chunkProviderServer.queueUnload(var5.locX, var5.locZ);
            }
         }

      }
   }

   public void flushSave() {
      if(this.chunkProvider.canSave()) {
         this.chunkProvider.c();
      }
   }

   protected void a() throws ExceptionWorldConflict {
      this.checkSession();
      this.worldData.a(this.getWorldBorder().getSize());
      this.worldData.d(this.getWorldBorder().getCenterX());
      this.worldData.c(this.getWorldBorder().getCenterZ());
      this.worldData.e(this.getWorldBorder().getDamageBuffer());
      this.worldData.f(this.getWorldBorder().getDamageAmount());
      this.worldData.j(this.getWorldBorder().getWarningDistance());
      this.worldData.k(this.getWorldBorder().getWarningTime());
      this.worldData.b(this.getWorldBorder().j());
      this.worldData.e(this.getWorldBorder().i());
      this.dataManager.saveWorldData(this.worldData, this.server.getPlayerList().t());
      this.worldMaps.a();
   }

   protected void a(Entity var1) {
      super.a(var1);
      this.entitiesById.a(var1.getId(), var1);
      this.entitiesByUUID.put(var1.getUniqueID(), var1);
      Entity[] var2 = var1.aB();
      if(var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.entitiesById.a(var2[var3].getId(), var2[var3]);
         }
      }

   }

   protected void b(Entity var1) {
      super.b(var1);
      this.entitiesById.d(var1.getId());
      this.entitiesByUUID.remove(var1.getUniqueID());
      Entity[] var2 = var1.aB();
      if(var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.entitiesById.d(var2[var3].getId());
         }
      }

   }

   public boolean strikeLightning(Entity var1) {
      if(super.strikeLightning(var1)) {
         this.server.getPlayerList().sendPacketNearby(var1.locX, var1.locY, var1.locZ, 512.0D, this.worldProvider.getDimension(), new PacketPlayOutSpawnEntityWeather(var1));
         return true;
      } else {
         return false;
      }
   }

   public void broadcastEntityEffect(Entity var1, byte var2) {
      this.getTracker().sendPacketToEntity(var1, new PacketPlayOutEntityStatus(var1, var2));
   }

   public Explosion createExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9, boolean var10) {
      Explosion var11 = new Explosion(this, var1, var2, var4, var6, var8, var9, var10);
      var11.a();
      var11.a(false);
      if(!var10) {
         var11.clearBlocks();
      }

      Iterator var12 = this.players.iterator();

      while(var12.hasNext()) {
         EntityHuman var13 = (EntityHuman)var12.next();
         if(var13.e(var2, var4, var6) < 4096.0D) {
            ((EntityPlayer)var13).playerConnection.sendPacket(new PacketPlayOutExplosion(var2, var4, var6, var8, var11.getBlocks(), (Vec3D)var11.b().get(var13)));
         }
      }

      return var11;
   }

   public void playBlockAction(BlockPosition var1, Block var2, int var3, int var4) {
      BlockActionData var5 = new BlockActionData(var1, var2, var3, var4);
      Iterator var6 = this.S[this.T].iterator();

      BlockActionData var7;
      do {
         if(!var6.hasNext()) {
            this.S[this.T].add(var5);
            return;
         }

         var7 = (BlockActionData)var6.next();
      } while(!var7.equals(var5));

   }

   private void ak() {
      while(!this.S[this.T].isEmpty()) {
         int var1 = this.T;
         this.T ^= 1;
         Iterator var2 = this.S[var1].iterator();

         while(var2.hasNext()) {
            BlockActionData var3 = (BlockActionData)var2.next();
            if(this.a(var3)) {
               this.server.getPlayerList().sendPacketNearby((double)var3.a().getX(), (double)var3.a().getY(), (double)var3.a().getZ(), 64.0D, this.worldProvider.getDimension(), new PacketPlayOutBlockAction(var3.a(), var3.d(), var3.b(), var3.c()));
            }
         }

         this.S[var1].clear();
      }

   }

   private boolean a(BlockActionData var1) {
      IBlockData var2 = this.getType(var1.a());
      return var2.getBlock() == var1.d()?var2.getBlock().a(this, var1.a(), var2, var1.b(), var1.c()):false;
   }

   public void saveLevel() {
      this.dataManager.a();
   }

   protected void p() {
      boolean var1 = this.S();
      super.p();
      if(this.o != this.p) {
         this.server.getPlayerList().a(new PacketPlayOutGameStateChange(7, this.p), this.worldProvider.getDimension());
      }

      if(this.q != this.r) {
         this.server.getPlayerList().a(new PacketPlayOutGameStateChange(8, this.r), this.worldProvider.getDimension());
      }

      if(var1 != this.S()) {
         if(var1) {
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(2, 0.0F));
         } else {
            this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(1, 0.0F));
         }

         this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(7, this.p));
         this.server.getPlayerList().sendAll(new PacketPlayOutGameStateChange(8, this.r));
      }

   }

   protected int q() {
      return this.server.getPlayerList().s();
   }

   public MinecraftServer getMinecraftServer() {
      return this.server;
   }

   public EntityTracker getTracker() {
      return this.tracker;
   }

   public PlayerChunkMap getPlayerChunkMap() {
      return this.manager;
   }

   public PortalTravelAgent getTravelAgent() {
      return this.Q;
   }

   public void a(EnumParticle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, int... var17) {
      this.a(var1, false, var2, var4, var6, var8, var9, var11, var13, var15, var17);
   }

   public void a(EnumParticle var1, boolean var2, double var3, double var5, double var7, int var9, double var10, double var12, double var14, double var16, int... var18) {
      PacketPlayOutWorldParticles var19 = new PacketPlayOutWorldParticles(var1, var2, (float)var3, (float)var5, (float)var7, (float)var10, (float)var12, (float)var14, (float)var16, var9, var18);

      for(int var20 = 0; var20 < this.players.size(); ++var20) {
         EntityPlayer var21 = (EntityPlayer)this.players.get(var20);
         BlockPosition var22 = var21.getChunkCoordinates();
         double var23 = var22.c(var3, var5, var7);
         if(var23 <= 256.0D || var2 && var23 <= 65536.0D) {
            var21.playerConnection.sendPacket(var19);
         }
      }

   }

   public Entity getEntity(UUID var1) {
      return (Entity)this.entitiesByUUID.get(var1);
   }

   public ListenableFuture<Object> postToMainThread(Runnable var1) {
      return this.server.postToMainThread(var1);
   }

   public boolean isMainThread() {
      return this.server.isMainThread();
   }

   static {
      U = Lists.newArrayList((Object[])(new StructurePieceTreasure[]{new StructurePieceTreasure(Items.STICK, 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.PLANKS), 0, 1, 3, 10), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG), 0, 1, 3, 10), new StructurePieceTreasure(Items.STONE_AXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOODEN_AXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.STONE_PICKAXE, 0, 1, 1, 3), new StructurePieceTreasure(Items.WOODEN_PICKAXE, 0, 1, 1, 5), new StructurePieceTreasure(Items.APPLE, 0, 2, 3, 5), new StructurePieceTreasure(Items.BREAD, 0, 2, 3, 3), new StructurePieceTreasure(Item.getItemOf(Blocks.LOG2), 0, 1, 3, 10)}));
   }

   static class BlockActionDataList extends ArrayList<BlockActionData> {
      private BlockActionDataList() {
      }

      // $FF: synthetic method
      BlockActionDataList(Object var1) {
         this();
      }
   }
}
