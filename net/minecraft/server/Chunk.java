package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkProviderDebug;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.ChunkSnapshot;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.Entity;
import net.minecraft.server.EntitySlice;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IContainer;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.ReportedException;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
   private static final Logger c = LogManager.getLogger();
   private final ChunkSection[] sections;
   private final byte[] e;
   private final int[] f;
   private final boolean[] g;
   private boolean h;
   public final World world;
   public final int[] heightMap;
   public final int locX;
   public final int locZ;
   private boolean k;
   public final Map<BlockPosition, TileEntity> tileEntities;
   public final EntitySlice<Entity>[] entitySlices;
   private boolean done;
   private boolean lit;
   private boolean p;
   private boolean q;
   private boolean r;
   private long lastSaved;
   private int t;
   private long u;
   private int v;
   private ConcurrentLinkedQueue<BlockPosition> w;

   public Chunk(World var1, int var2, int var3) {
      this.sections = new ChunkSection[16];
      this.e = new byte[256];
      this.f = new int[256];
      this.g = new boolean[256];
      this.tileEntities = Maps.newHashMap();
      this.v = 4096;
      this.w = Queues.newConcurrentLinkedQueue();
      this.entitySlices = (EntitySlice[])(new EntitySlice[16]);
      this.world = var1;
      this.locX = var2;
      this.locZ = var3;
      this.heightMap = new int[256];

      for(int var4 = 0; var4 < this.entitySlices.length; ++var4) {
         this.entitySlices[var4] = new EntitySlice(Entity.class);
      }

      Arrays.fill(this.f, -999);
      Arrays.fill(this.e, (byte)-1);
   }

   public Chunk(World var1, ChunkSnapshot var2, int var3, int var4) {
      this(var1, var3, var4);
      short var5 = 256;
      boolean var6 = !var1.worldProvider.o();

      for(int var7 = 0; var7 < 16; ++var7) {
         for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < var5; ++var9) {
               int var10 = var7 * var5 * 16 | var8 * var5 | var9;
               IBlockData var11 = var2.a(var10);
               if(var11.getBlock().getMaterial() != Material.AIR) {
                  int var12 = var9 >> 4;
                  if(this.sections[var12] == null) {
                     this.sections[var12] = new ChunkSection(var12 << 4, var6);
                  }

                  this.sections[var12].setType(var7, var9 & 15, var8, var11);
               }
            }
         }
      }

   }

   public boolean a(int var1, int var2) {
      return var1 == this.locX && var2 == this.locZ;
   }

   public int f(BlockPosition var1) {
      return this.b(var1.getX() & 15, var1.getZ() & 15);
   }

   public int b(int var1, int var2) {
      return this.heightMap[var2 << 4 | var1];
   }

   public int g() {
      for(int var1 = this.sections.length - 1; var1 >= 0; --var1) {
         if(this.sections[var1] != null) {
            return this.sections[var1].getYPosition();
         }
      }

      return 0;
   }

   public ChunkSection[] getSections() {
      return this.sections;
   }

   public void initLighting() {
      int var1 = this.g();
      this.t = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < 16; ++var2) {
         for(int var3 = 0; var3 < 16; ++var3) {
            this.f[var2 + (var3 << 4)] = -999;

            int var4;
            for(var4 = var1 + 16; var4 > 0; --var4) {
               if(this.e(var2, var4 - 1, var3) != 0) {
                  this.heightMap[var3 << 4 | var2] = var4;
                  if(var4 < this.t) {
                     this.t = var4;
                  }
                  break;
               }
            }

            if(!this.world.worldProvider.o()) {
               var4 = 15;
               int var5 = var1 + 16 - 1;

               do {
                  int var6 = this.e(var2, var5, var3);
                  if(var6 == 0 && var4 != 15) {
                     var6 = 1;
                  }

                  var4 -= var6;
                  if(var4 > 0) {
                     ChunkSection var7 = this.sections[var5 >> 4];
                     if(var7 != null) {
                        var7.a(var2, var5 & 15, var3, var4);
                        this.world.n(new BlockPosition((this.locX << 4) + var2, var5, (this.locZ << 4) + var3));
                     }
                  }

                  --var5;
               } while(var5 > 0 && var4 > 0);
            }
         }
      }

      this.q = true;
   }

   private void d(int var1, int var2) {
      this.g[var1 + var2 * 16] = true;
      this.k = true;
   }

   private void h(boolean var1) {
      this.world.methodProfiler.a("recheckGaps");
      if(this.world.areChunksLoaded(new BlockPosition(this.locX * 16 + 8, 0, this.locZ * 16 + 8), 16)) {
         for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
               if(this.g[var2 + var3 * 16]) {
                  this.g[var2 + var3 * 16] = false;
                  int var4 = this.b(var2, var3);
                  int var5 = this.locX * 16 + var2;
                  int var6 = this.locZ * 16 + var3;
                  int var7 = Integer.MAX_VALUE;

                  Iterator var8;
                  EnumDirection var9;
                  for(var8 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator(); var8.hasNext(); var7 = Math.min(var7, this.world.b(var5 + var9.getAdjacentX(), var6 + var9.getAdjacentZ()))) {
                     var9 = (EnumDirection)var8.next();
                  }

                  this.c(var5, var6, var7);
                  var8 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                  while(var8.hasNext()) {
                     var9 = (EnumDirection)var8.next();
                     this.c(var5 + var9.getAdjacentX(), var6 + var9.getAdjacentZ(), var4);
                  }

                  if(var1) {
                     this.world.methodProfiler.b();
                     return;
                  }
               }
            }
         }

         this.k = false;
      }

      this.world.methodProfiler.b();
   }

   private void c(int var1, int var2, int var3) {
      int var4 = this.world.getHighestBlockYAt(new BlockPosition(var1, 0, var2)).getY();
      if(var4 > var3) {
         this.a(var1, var2, var3, var4 + 1);
      } else if(var4 < var3) {
         this.a(var1, var2, var4, var3 + 1);
      }

   }

   private void a(int var1, int var2, int var3, int var4) {
      if(var4 > var3 && this.world.areChunksLoaded(new BlockPosition(var1, 0, var2), 16)) {
         for(int var5 = var3; var5 < var4; ++var5) {
            this.world.c(EnumSkyBlock.SKY, new BlockPosition(var1, var5, var2));
         }

         this.q = true;
      }

   }

   private void d(int var1, int var2, int var3) {
      int var4 = this.heightMap[var3 << 4 | var1] & 255;
      int var5 = var4;
      if(var2 > var4) {
         var5 = var2;
      }

      while(var5 > 0 && this.e(var1, var5 - 1, var3) == 0) {
         --var5;
      }

      if(var5 != var4) {
         this.world.a(var1 + this.locX * 16, var3 + this.locZ * 16, var5, var4);
         this.heightMap[var3 << 4 | var1] = var5;
         int var6 = this.locX * 16 + var1;
         int var7 = this.locZ * 16 + var3;
         int var8;
         int var13;
         if(!this.world.worldProvider.o()) {
            ChunkSection var9;
            if(var5 < var4) {
               for(var8 = var5; var8 < var4; ++var8) {
                  var9 = this.sections[var8 >> 4];
                  if(var9 != null) {
                     var9.a(var1, var8 & 15, var3, 15);
                     this.world.n(new BlockPosition((this.locX << 4) + var1, var8, (this.locZ << 4) + var3));
                  }
               }
            } else {
               for(var8 = var4; var8 < var5; ++var8) {
                  var9 = this.sections[var8 >> 4];
                  if(var9 != null) {
                     var9.a(var1, var8 & 15, var3, 0);
                     this.world.n(new BlockPosition((this.locX << 4) + var1, var8, (this.locZ << 4) + var3));
                  }
               }
            }

            var8 = 15;

            while(var5 > 0 && var8 > 0) {
               --var5;
               var13 = this.e(var1, var5, var3);
               if(var13 == 0) {
                  var13 = 1;
               }

               var8 -= var13;
               if(var8 < 0) {
                  var8 = 0;
               }

               ChunkSection var10 = this.sections[var5 >> 4];
               if(var10 != null) {
                  var10.a(var1, var5 & 15, var3, var8);
               }
            }
         }

         var8 = this.heightMap[var3 << 4 | var1];
         var13 = var4;
         int var14 = var8;
         if(var8 < var4) {
            var13 = var8;
            var14 = var4;
         }

         if(var8 < this.t) {
            this.t = var8;
         }

         if(!this.world.worldProvider.o()) {
            Iterator var11 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            while(var11.hasNext()) {
               EnumDirection var12 = (EnumDirection)var11.next();
               this.a(var6 + var12.getAdjacentX(), var7 + var12.getAdjacentZ(), var13, var14);
            }

            this.a(var6, var7, var13, var14);
         }

         this.q = true;
      }
   }

   public int b(BlockPosition var1) {
      return this.getType(var1).p();
   }

   private int e(int var1, int var2, int var3) {
      return this.getType(var1, var2, var3).p();
   }

   private Block getType(int var1, int var2, int var3) {
      Block var4 = Blocks.AIR;
      if(var2 >= 0 && var2 >> 4 < this.sections.length) {
         ChunkSection var5 = this.sections[var2 >> 4];
         if(var5 != null) {
            try {
               var4 = var5.b(var1, var2 & 15, var3);
            } catch (Throwable var8) {
               CrashReport var7 = CrashReport.a(var8, "Getting block");
               throw new ReportedException(var7);
            }
         }
      }

      return var4;
   }

   public Block getTypeAbs(final int var1, final int var2, final int var3) {
      try {
         return this.getType(var1 & 15, var2, var3 & 15);
      } catch (ReportedException var6) {
         CrashReportSystemDetails var5 = var6.a().a("Block being got");
         var5.a("Location", new Callable() {
            public String a() throws Exception {
               return CrashReportSystemDetails.a(new BlockPosition(Chunk.this.locX * 16 + var1, var2, Chunk.this.locZ * 16 + var3));
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
         throw var6;
      }
   }

   public Block getType(final BlockPosition var1) {
      try {
         return this.getType(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
      } catch (ReportedException var4) {
         CrashReportSystemDetails var3 = var4.a().a("Block being got");
         var3.a("Location", new Callable() {
            public String a() throws Exception {
               return CrashReportSystemDetails.a(var1);
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
         throw var4;
      }
   }

   public IBlockData getBlockData(final BlockPosition var1) {
      if(this.world.G() == WorldType.DEBUG_ALL_BLOCK_STATES) {
         IBlockData var7 = null;
         if(var1.getY() == 60) {
            var7 = Blocks.BARRIER.getBlockData();
         }

         if(var1.getY() == 70) {
            var7 = ChunkProviderDebug.b(var1.getX(), var1.getZ());
         }

         return var7 == null?Blocks.AIR.getBlockData():var7;
      } else {
         try {
            if(var1.getY() >= 0 && var1.getY() >> 4 < this.sections.length) {
               ChunkSection var2 = this.sections[var1.getY() >> 4];
               if(var2 != null) {
                  int var8 = var1.getX() & 15;
                  int var9 = var1.getY() & 15;
                  int var5 = var1.getZ() & 15;
                  return var2.getType(var8, var9, var5);
               }
            }

            return Blocks.AIR.getBlockData();
         } catch (Throwable var6) {
            CrashReport var3 = CrashReport.a(var6, "Getting block state");
            CrashReportSystemDetails var4 = var3.a("Block being got");
            var4.a("Location", new Callable() {
               public String a() throws Exception {
                  return CrashReportSystemDetails.a(var1);
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            throw new ReportedException(var3);
         }
      }
   }

   private int g(int var1, int var2, int var3) {
      if(var2 >> 4 >= this.sections.length) {
         return 0;
      } else {
         ChunkSection var4 = this.sections[var2 >> 4];
         return var4 != null?var4.c(var1, var2 & 15, var3):0;
      }
   }

   public int c(BlockPosition var1) {
      return this.g(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
   }

   public IBlockData a(BlockPosition var1, IBlockData var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getY();
      int var5 = var1.getZ() & 15;
      int var6 = var5 << 4 | var3;
      if(var4 >= this.f[var6] - 1) {
         this.f[var6] = -999;
      }

      int var7 = this.heightMap[var6];
      IBlockData var8 = this.getBlockData(var1);
      if(var8 == var2) {
         return null;
      } else {
         Block var9 = var2.getBlock();
         Block var10 = var8.getBlock();
         ChunkSection var11 = this.sections[var4 >> 4];
         boolean var12 = false;
         if(var11 == null) {
            if(var9 == Blocks.AIR) {
               return null;
            }

            var11 = this.sections[var4 >> 4] = new ChunkSection(var4 >> 4 << 4, !this.world.worldProvider.o());
            var12 = var4 >= var7;
         }

         var11.setType(var3, var4 & 15, var5, var2);
         if(var10 != var9) {
            if(!this.world.isClientSide) {
               var10.remove(this.world, var1, var8);
            } else if(var10 instanceof IContainer) {
               this.world.t(var1);
            }
         }

         if(var11.b(var3, var4 & 15, var5) != var9) {
            return null;
         } else {
            if(var12) {
               this.initLighting();
            } else {
               int var13 = var9.p();
               int var14 = var10.p();
               if(var13 > 0) {
                  if(var4 >= var7) {
                     this.d(var3, var4 + 1, var5);
                  }
               } else if(var4 == var7 - 1) {
                  this.d(var3, var4, var5);
               }

               if(var13 != var14 && (var13 < var14 || this.getBrightness(EnumSkyBlock.SKY, var1) > 0 || this.getBrightness(EnumSkyBlock.BLOCK, var1) > 0)) {
                  this.d(var3, var5);
               }
            }

            TileEntity var15;
            if(var10 instanceof IContainer) {
               var15 = this.a(var1, Chunk.EnumTileEntityState.CHECK);
               if(var15 != null) {
                  var15.E();
               }
            }

            if(!this.world.isClientSide && var10 != var9) {
               var9.onPlace(this.world, var1, var2);
            }

            if(var9 instanceof IContainer) {
               var15 = this.a(var1, Chunk.EnumTileEntityState.CHECK);
               if(var15 == null) {
                  var15 = ((IContainer)var9).a(this.world, var9.toLegacyData(var2));
                  this.world.setTileEntity(var1, var15);
               }

               if(var15 != null) {
                  var15.E();
               }
            }

            this.q = true;
            return var8;
         }
      }
   }

   public int getBrightness(EnumSkyBlock var1, BlockPosition var2) {
      int var3 = var2.getX() & 15;
      int var4 = var2.getY();
      int var5 = var2.getZ() & 15;
      ChunkSection var6 = this.sections[var4 >> 4];
      return var6 == null?(this.d(var2)?var1.c:0):(var1 == EnumSkyBlock.SKY?(this.world.worldProvider.o()?0:var6.d(var3, var4 & 15, var5)):(var1 == EnumSkyBlock.BLOCK?var6.e(var3, var4 & 15, var5):var1.c));
   }

   public void a(EnumSkyBlock var1, BlockPosition var2, int var3) {
      int var4 = var2.getX() & 15;
      int var5 = var2.getY();
      int var6 = var2.getZ() & 15;
      ChunkSection var7 = this.sections[var5 >> 4];
      if(var7 == null) {
         var7 = this.sections[var5 >> 4] = new ChunkSection(var5 >> 4 << 4, !this.world.worldProvider.o());
         this.initLighting();
      }

      this.q = true;
      if(var1 == EnumSkyBlock.SKY) {
         if(!this.world.worldProvider.o()) {
            var7.a(var4, var5 & 15, var6, var3);
         }
      } else if(var1 == EnumSkyBlock.BLOCK) {
         var7.b(var4, var5 & 15, var6, var3);
      }

   }

   public int a(BlockPosition var1, int var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getY();
      int var5 = var1.getZ() & 15;
      ChunkSection var6 = this.sections[var4 >> 4];
      if(var6 == null) {
         return !this.world.worldProvider.o() && var2 < EnumSkyBlock.SKY.c?EnumSkyBlock.SKY.c - var2:0;
      } else {
         int var7 = this.world.worldProvider.o()?0:var6.d(var3, var4 & 15, var5);
         var7 -= var2;
         int var8 = var6.e(var3, var4 & 15, var5);
         if(var8 > var7) {
            var7 = var8;
         }

         return var7;
      }
   }

   public void a(Entity var1) {
      this.r = true;
      int var2 = MathHelper.floor(var1.locX / 16.0D);
      int var3 = MathHelper.floor(var1.locZ / 16.0D);
      if(var2 != this.locX || var3 != this.locZ) {
         c.warn("Wrong location! (" + var2 + ", " + var3 + ") should be (" + this.locX + ", " + this.locZ + "), " + var1, new Object[]{var1});
         var1.die();
      }

      int var4 = MathHelper.floor(var1.locY / 16.0D);
      if(var4 < 0) {
         var4 = 0;
      }

      if(var4 >= this.entitySlices.length) {
         var4 = this.entitySlices.length - 1;
      }

      var1.ad = true;
      var1.ae = this.locX;
      var1.af = var4;
      var1.ag = this.locZ;
      this.entitySlices[var4].add(var1);
   }

   public void b(Entity var1) {
      this.a(var1, var1.af);
   }

   public void a(Entity var1, int var2) {
      if(var2 < 0) {
         var2 = 0;
      }

      if(var2 >= this.entitySlices.length) {
         var2 = this.entitySlices.length - 1;
      }

      this.entitySlices[var2].remove(var1);
   }

   public boolean d(BlockPosition var1) {
      int var2 = var1.getX() & 15;
      int var3 = var1.getY();
      int var4 = var1.getZ() & 15;
      return var3 >= this.heightMap[var4 << 4 | var2];
   }

   private TileEntity i(BlockPosition var1) {
      Block var2 = this.getType(var1);
      return !var2.isTileEntity()?null:((IContainer)var2).a(this.world, this.c(var1));
   }

   public TileEntity a(BlockPosition var1, Chunk.EnumTileEntityState var2) {
      TileEntity var3 = (TileEntity)this.tileEntities.get(var1);
      if(var3 == null) {
         if(var2 == Chunk.EnumTileEntityState.IMMEDIATE) {
            var3 = this.i(var1);
            this.world.setTileEntity(var1, var3);
         } else if(var2 == Chunk.EnumTileEntityState.QUEUED) {
            this.w.add(var1);
         }
      } else if(var3.x()) {
         this.tileEntities.remove(var1);
         return null;
      }

      return var3;
   }

   public void a(TileEntity var1) {
      this.a(var1.getPosition(), var1);
      if(this.h) {
         this.world.a(var1);
      }

   }

   public void a(BlockPosition var1, TileEntity var2) {
      var2.a(this.world);
      var2.a(var1);
      if(this.getType(var1) instanceof IContainer) {
         if(this.tileEntities.containsKey(var1)) {
            ((TileEntity)this.tileEntities.get(var1)).y();
         }

         var2.D();
         this.tileEntities.put(var1, var2);
      }
   }

   public void e(BlockPosition var1) {
      if(this.h) {
         TileEntity var2 = (TileEntity)this.tileEntities.remove(var1);
         if(var2 != null) {
            var2.y();
         }
      }

   }

   public void addEntities() {
      this.h = true;
      this.world.a(this.tileEntities.values());

      for(int var1 = 0; var1 < this.entitySlices.length; ++var1) {
         Iterator var2 = this.entitySlices[var1].iterator();

         while(var2.hasNext()) {
            Entity var3 = (Entity)var2.next();
            var3.ah();
         }

         this.world.b((Collection)this.entitySlices[var1]);
      }

   }

   public void removeEntities() {
      this.h = false;
      Iterator var1 = this.tileEntities.values().iterator();

      while(var1.hasNext()) {
         TileEntity var2 = (TileEntity)var1.next();
         this.world.b(var2);
      }

      for(int var3 = 0; var3 < this.entitySlices.length; ++var3) {
         this.world.c((Collection)this.entitySlices[var3]);
      }

   }

   public void e() {
      this.q = true;
   }

   public void a(Entity var1, AxisAlignedBB var2, List<Entity> var3, Predicate<? super Entity> var4) {
      int var5 = MathHelper.floor((var2.b - 2.0D) / 16.0D);
      int var6 = MathHelper.floor((var2.e + 2.0D) / 16.0D);
      var5 = MathHelper.clamp(var5, 0, this.entitySlices.length - 1);
      var6 = MathHelper.clamp(var6, 0, this.entitySlices.length - 1);

      label68:
      for(int var7 = var5; var7 <= var6; ++var7) {
         if(!this.entitySlices[var7].isEmpty()) {
            Iterator var8 = this.entitySlices[var7].iterator();

            while(true) {
               Entity var9;
               Entity[] var10;
               do {
                  do {
                     do {
                        if(!var8.hasNext()) {
                           continue label68;
                        }

                        var9 = (Entity)var8.next();
                     } while(!var9.getBoundingBox().b(var2));
                  } while(var9 == var1);

                  if(var4 == null || var4.apply(var9)) {
                     var3.add(var9);
                  }

                  var10 = var9.aB();
               } while(var10 == null);

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  var9 = var10[var11];
                  if(var9 != var1 && var9.getBoundingBox().b(var2) && (var4 == null || var4.apply(var9))) {
                     var3.add(var9);
                  }
               }
            }
         }
      }

   }

   public <T extends Entity> void a(Class<? extends T> var1, AxisAlignedBB var2, List<T> var3, Predicate<? super T> var4) {
      int var5 = MathHelper.floor((var2.b - 2.0D) / 16.0D);
      int var6 = MathHelper.floor((var2.e + 2.0D) / 16.0D);
      var5 = MathHelper.clamp(var5, 0, this.entitySlices.length - 1);
      var6 = MathHelper.clamp(var6, 0, this.entitySlices.length - 1);

      label33:
      for(int var7 = var5; var7 <= var6; ++var7) {
         Iterator var8 = this.entitySlices[var7].c(var1).iterator();

         while(true) {
            Entity var9;
            do {
               do {
                  if(!var8.hasNext()) {
                     continue label33;
                  }

                  var9 = (Entity)var8.next();
               } while(!var9.getBoundingBox().b(var2));
            } while(var4 != null && !var4.apply(var9));

            var3.add(var9);
         }
      }

   }

   public boolean a(boolean var1) {
      if(var1) {
         if(this.r && this.world.getTime() != this.lastSaved || this.q) {
            return true;
         }
      } else if(this.r && this.world.getTime() >= this.lastSaved + 600L) {
         return true;
      }

      return this.q;
   }

   public Random a(long var1) {
      return new Random(this.world.getSeed() + (long)(this.locX * this.locX * 4987142) + (long)(this.locX * 5947611) + (long)(this.locZ * this.locZ) * 4392871L + (long)(this.locZ * 389711) ^ var1);
   }

   public boolean isEmpty() {
      return false;
   }

   public void loadNearby(IChunkProvider var1, IChunkProvider var2, int var3, int var4) {
      boolean var5 = var1.isChunkLoaded(var3, var4 - 1);
      boolean var6 = var1.isChunkLoaded(var3 + 1, var4);
      boolean var7 = var1.isChunkLoaded(var3, var4 + 1);
      boolean var8 = var1.isChunkLoaded(var3 - 1, var4);
      boolean var9 = var1.isChunkLoaded(var3 - 1, var4 - 1);
      boolean var10 = var1.isChunkLoaded(var3 + 1, var4 + 1);
      boolean var11 = var1.isChunkLoaded(var3 - 1, var4 + 1);
      boolean var12 = var1.isChunkLoaded(var3 + 1, var4 - 1);
      if(var6 && var7 && var10) {
         if(!this.done) {
            var1.getChunkAt(var2, var3, var4);
         } else {
            var1.a(var2, this, var3, var4);
         }
      }

      Chunk var13;
      if(var8 && var7 && var11) {
         var13 = var1.getOrCreateChunk(var3 - 1, var4);
         if(!var13.done) {
            var1.getChunkAt(var2, var3 - 1, var4);
         } else {
            var1.a(var2, var13, var3 - 1, var4);
         }
      }

      if(var5 && var6 && var12) {
         var13 = var1.getOrCreateChunk(var3, var4 - 1);
         if(!var13.done) {
            var1.getChunkAt(var2, var3, var4 - 1);
         } else {
            var1.a(var2, var13, var3, var4 - 1);
         }
      }

      if(var9 && var5 && var8) {
         var13 = var1.getOrCreateChunk(var3 - 1, var4 - 1);
         if(!var13.done) {
            var1.getChunkAt(var2, var3 - 1, var4 - 1);
         } else {
            var1.a(var2, var13, var3 - 1, var4 - 1);
         }
      }

   }

   public BlockPosition h(BlockPosition var1) {
      int var2 = var1.getX() & 15;
      int var3 = var1.getZ() & 15;
      int var4 = var2 | var3 << 4;
      BlockPosition var5 = new BlockPosition(var1.getX(), this.f[var4], var1.getZ());
      if(var5.getY() == -999) {
         int var6 = this.g() + 15;
         var5 = new BlockPosition(var1.getX(), var6, var1.getZ());
         int var7 = -1;

         while(true) {
            while(var5.getY() > 0 && var7 == -1) {
               Block var8 = this.getType(var5);
               Material var9 = var8.getMaterial();
               if(!var9.isSolid() && !var9.isLiquid()) {
                  var5 = var5.down();
               } else {
                  var7 = var5.getY() + 1;
               }
            }

            this.f[var4] = var7;
            break;
         }
      }

      return new BlockPosition(var1.getX(), this.f[var4], var1.getZ());
   }

   public void b(boolean var1) {
      if(this.k && !this.world.worldProvider.o() && !var1) {
         this.h(this.world.isClientSide);
      }

      this.p = true;
      if(!this.lit && this.done) {
         this.n();
      }

      while(!this.w.isEmpty()) {
         BlockPosition var2 = (BlockPosition)this.w.poll();
         if(this.a(var2, Chunk.EnumTileEntityState.CHECK) == null && this.getType(var2).isTileEntity()) {
            TileEntity var3 = this.i(var2);
            this.world.setTileEntity(var2, var3);
            this.world.b(var2, var2);
         }
      }

   }

   public boolean isReady() {
      return this.p && this.done && this.lit;
   }

   public ChunkCoordIntPair j() {
      return new ChunkCoordIntPair(this.locX, this.locZ);
   }

   public boolean c(int var1, int var2) {
      if(var1 < 0) {
         var1 = 0;
      }

      if(var2 >= 256) {
         var2 = 255;
      }

      for(int var3 = var1; var3 <= var2; var3 += 16) {
         ChunkSection var4 = this.sections[var3 >> 4];
         if(var4 != null && !var4.a()) {
            return false;
         }
      }

      return true;
   }

   public void a(ChunkSection[] var1) {
      if(this.sections.length != var1.length) {
         c.warn("Could not set level chunk sections, array length is " + var1.length + " instead of " + this.sections.length);
      } else {
         for(int var2 = 0; var2 < this.sections.length; ++var2) {
            this.sections[var2] = var1[var2];
         }

      }
   }

   public BiomeBase getBiome(BlockPosition var1, WorldChunkManager var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getZ() & 15;
      int var5 = this.e[var4 << 4 | var3] & 255;
      BiomeBase var6;
      if(var5 == 255) {
         var6 = var2.getBiome(var1, BiomeBase.PLAINS);
         var5 = var6.id;
         this.e[var4 << 4 | var3] = (byte)(var5 & 255);
      }

      var6 = BiomeBase.getBiome(var5);
      return var6 == null?BiomeBase.PLAINS:var6;
   }

   public byte[] getBiomeIndex() {
      return this.e;
   }

   public void a(byte[] var1) {
      if(this.e.length != var1.length) {
         c.warn("Could not set level chunk biomes, array length is " + var1.length + " instead of " + this.e.length);
      } else {
         for(int var2 = 0; var2 < this.e.length; ++var2) {
            this.e[var2] = var1[var2];
         }

      }
   }

   public void l() {
      this.v = 0;
   }

   public void m() {
      BlockPosition var1 = new BlockPosition(this.locX << 4, 0, this.locZ << 4);

      for(int var2 = 0; var2 < 8; ++var2) {
         if(this.v >= 4096) {
            return;
         }

         int var3 = this.v % 16;
         int var4 = this.v / 16 % 16;
         int var5 = this.v / 256;
         ++this.v;

         for(int var6 = 0; var6 < 16; ++var6) {
            BlockPosition var7 = var1.a(var4, (var3 << 4) + var6, var5);
            boolean var8 = var6 == 0 || var6 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15;
            if(this.sections[var3] == null && var8 || this.sections[var3] != null && this.sections[var3].b(var4, var6, var5).getMaterial() == Material.AIR) {
               EnumDirection[] var9 = EnumDirection.values();
               int var10 = var9.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  EnumDirection var12 = var9[var11];
                  BlockPosition var13 = var7.shift(var12);
                  if(this.world.getType(var13).getBlock().r() > 0) {
                     this.world.x(var13);
                  }
               }

               this.world.x(var7);
            }
         }
      }

   }

   public void n() {
      this.done = true;
      this.lit = true;
      BlockPosition var1 = new BlockPosition(this.locX << 4, 0, this.locZ << 4);
      if(!this.world.worldProvider.o()) {
         if(this.world.areChunksLoadedBetween(var1.a(-1, 0, -1), var1.a(16, this.world.F(), 16))) {
            label44:
            for(int var2 = 0; var2 < 16; ++var2) {
               for(int var3 = 0; var3 < 16; ++var3) {
                  if(!this.e(var2, var3)) {
                     this.lit = false;
                     break label44;
                  }
               }
            }

            if(this.lit) {
               Iterator var5 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

               while(var5.hasNext()) {
                  EnumDirection var6 = (EnumDirection)var5.next();
                  int var4 = var6.c() == EnumDirection.EnumAxisDirection.POSITIVE?16:1;
                  this.world.getChunkAtWorldCoords(var1.shift(var6, var4)).a(var6.opposite());
               }

               this.y();
            }
         } else {
            this.lit = false;
         }
      }

   }

   private void y() {
      for(int var1 = 0; var1 < this.g.length; ++var1) {
         this.g[var1] = true;
      }

      this.h(false);
   }

   private void a(EnumDirection var1) {
      if(this.done) {
         int var2;
         if(var1 == EnumDirection.EAST) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.e(15, var2);
            }
         } else if(var1 == EnumDirection.WEST) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.e(0, var2);
            }
         } else if(var1 == EnumDirection.SOUTH) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.e(var2, 15);
            }
         } else if(var1 == EnumDirection.NORTH) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.e(var2, 0);
            }
         }

      }
   }

   private boolean e(int var1, int var2) {
      int var3 = this.g();
      boolean var4 = false;
      boolean var5 = false;
      BlockPosition.MutableBlockPosition var6 = new BlockPosition.MutableBlockPosition((this.locX << 4) + var1, 0, (this.locZ << 4) + var2);

      int var7;
      for(var7 = var3 + 16 - 1; var7 > this.world.F() || var7 > 0 && !var5; --var7) {
         var6.c(var6.getX(), var7, var6.getZ());
         int var8 = this.b((BlockPosition)var6);
         if(var8 == 255 && var6.getY() < this.world.F()) {
            var5 = true;
         }

         if(!var4 && var8 > 0) {
            var4 = true;
         } else if(var4 && var8 == 0 && !this.world.x(var6)) {
            return false;
         }
      }

      for(var7 = var6.getY(); var7 > 0; --var7) {
         var6.c(var6.getX(), var7, var6.getZ());
         if(this.getType(var6).r() > 0) {
            this.world.x(var6);
         }
      }

      return true;
   }

   public boolean o() {
      return this.h;
   }

   public World getWorld() {
      return this.world;
   }

   public int[] q() {
      return this.heightMap;
   }

   public void a(int[] var1) {
      if(this.heightMap.length != var1.length) {
         c.warn("Could not set level chunk heightmap, array length is " + var1.length + " instead of " + this.heightMap.length);
      } else {
         for(int var2 = 0; var2 < this.heightMap.length; ++var2) {
            this.heightMap[var2] = var1[var2];
         }

      }
   }

   public Map<BlockPosition, TileEntity> getTileEntities() {
      return this.tileEntities;
   }

   public EntitySlice<Entity>[] getEntitySlices() {
      return this.entitySlices;
   }

   public boolean isDone() {
      return this.done;
   }

   public void d(boolean var1) {
      this.done = var1;
   }

   public boolean u() {
      return this.lit;
   }

   public void e(boolean var1) {
      this.lit = var1;
   }

   public void f(boolean var1) {
      this.q = var1;
   }

   public void g(boolean var1) {
      this.r = var1;
   }

   public void setLastSaved(long var1) {
      this.lastSaved = var1;
   }

   public int v() {
      return this.t;
   }

   public long w() {
      return this.u;
   }

   public void c(long var1) {
      this.u = var1;
   }

   public static enum EnumTileEntityState {
      IMMEDIATE,
      QUEUED,
      CHECK;

      private EnumTileEntityState() {
      }
   }
}
