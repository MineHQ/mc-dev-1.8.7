package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.EmptyChunk;
import net.minecraft.server.EnumCreatureType;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.LongHashMap;
import net.minecraft.server.ReportedException;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider {
   private static final Logger b = LogManager.getLogger();
   private Set<Long> unloadQueue = Collections.newSetFromMap(new ConcurrentHashMap());
   public Chunk emptyChunk;
   public IChunkProvider chunkProvider;
   private IChunkLoader chunkLoader;
   public boolean forceChunkLoad = true;
   private LongHashMap<Chunk> chunks = new LongHashMap();
   private List<Chunk> chunkList = Lists.newArrayList();
   public WorldServer world;

   public ChunkProviderServer(WorldServer var1, IChunkLoader var2, IChunkProvider var3) {
      this.emptyChunk = new EmptyChunk(var1, 0, 0);
      this.world = var1;
      this.chunkLoader = var2;
      this.chunkProvider = var3;
   }

   public boolean isChunkLoaded(int var1, int var2) {
      return this.chunks.contains(ChunkCoordIntPair.a(var1, var2));
   }

   public List<Chunk> a() {
      return this.chunkList;
   }

   public void queueUnload(int var1, int var2) {
      if(this.world.worldProvider.e()) {
         if(!this.world.c(var1, var2)) {
            this.unloadQueue.add(Long.valueOf(ChunkCoordIntPair.a(var1, var2)));
         }
      } else {
         this.unloadQueue.add(Long.valueOf(ChunkCoordIntPair.a(var1, var2)));
      }

   }

   public void b() {
      Iterator var1 = this.chunkList.iterator();

      while(var1.hasNext()) {
         Chunk var2 = (Chunk)var1.next();
         this.queueUnload(var2.locX, var2.locZ);
      }

   }

   public Chunk getChunkAt(int var1, int var2) {
      long var3 = ChunkCoordIntPair.a(var1, var2);
      this.unloadQueue.remove(Long.valueOf(var3));
      Chunk var5 = (Chunk)this.chunks.getEntry(var3);
      if(var5 == null) {
         var5 = this.loadChunk(var1, var2);
         if(var5 == null) {
            if(this.chunkProvider == null) {
               var5 = this.emptyChunk;
            } else {
               try {
                  var5 = this.chunkProvider.getOrCreateChunk(var1, var2);
               } catch (Throwable var9) {
                  CrashReport var7 = CrashReport.a(var9, "Exception generating new chunk");
                  CrashReportSystemDetails var8 = var7.a("Chunk to be generated");
                  var8.a((String)"Location", (Object)String.format("%d,%d", new Object[]{Integer.valueOf(var1), Integer.valueOf(var2)}));
                  var8.a((String)"Position hash", (Object)Long.valueOf(var3));
                  var8.a((String)"Generator", (Object)this.chunkProvider.getName());
                  throw new ReportedException(var7);
               }
            }
         }

         this.chunks.put(var3, var5);
         this.chunkList.add(var5);
         var5.addEntities();
         var5.loadNearby(this, this, var1, var2);
      }

      return var5;
   }

   public Chunk getOrCreateChunk(int var1, int var2) {
      Chunk var3 = (Chunk)this.chunks.getEntry(ChunkCoordIntPair.a(var1, var2));
      return var3 == null?(!this.world.ad() && !this.forceChunkLoad?this.emptyChunk:this.getChunkAt(var1, var2)):var3;
   }

   public Chunk loadChunk(int var1, int var2) {
      if(this.chunkLoader == null) {
         return null;
      } else {
         try {
            Chunk var3 = this.chunkLoader.a(this.world, var1, var2);
            if(var3 != null) {
               var3.setLastSaved(this.world.getTime());
               if(this.chunkProvider != null) {
                  this.chunkProvider.recreateStructures(var3, var1, var2);
               }
            }

            return var3;
         } catch (Exception var4) {
            b.error((String)"Couldn\'t load chunk", (Throwable)var4);
            return null;
         }
      }
   }

   public void saveChunkNOP(Chunk var1) {
      if(this.chunkLoader != null) {
         try {
            this.chunkLoader.b(this.world, var1);
         } catch (Exception var3) {
            b.error((String)"Couldn\'t save entities", (Throwable)var3);
         }

      }
   }

   public void saveChunk(Chunk var1) {
      if(this.chunkLoader != null) {
         try {
            var1.setLastSaved(this.world.getTime());
            this.chunkLoader.a(this.world, var1);
         } catch (IOException var3) {
            b.error((String)"Couldn\'t save chunk", (Throwable)var3);
         } catch (ExceptionWorldConflict var4) {
            b.error((String)"Couldn\'t save chunk; already in use by another instance of Minecraft?", (Throwable)var4);
         }

      }
   }

   public void getChunkAt(IChunkProvider var1, int var2, int var3) {
      Chunk var4 = this.getOrCreateChunk(var2, var3);
      if(!var4.isDone()) {
         var4.n();
         if(this.chunkProvider != null) {
            this.chunkProvider.getChunkAt(var1, var2, var3);
            var4.e();
         }
      }

   }

   public boolean a(IChunkProvider var1, Chunk var2, int var3, int var4) {
      if(this.chunkProvider != null && this.chunkProvider.a(var1, var2, var3, var4)) {
         Chunk var5 = this.getOrCreateChunk(var3, var4);
         var5.e();
         return true;
      } else {
         return false;
      }
   }

   public boolean saveChunks(boolean var1, IProgressUpdate var2) {
      int var3 = 0;
      ArrayList var4 = Lists.newArrayList((Iterable)this.chunkList);

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         Chunk var6 = (Chunk)var4.get(var5);
         if(var1) {
            this.saveChunkNOP(var6);
         }

         if(var6.a(var1)) {
            this.saveChunk(var6);
            var6.f(false);
            ++var3;
            if(var3 == 24 && !var1) {
               return false;
            }
         }
      }

      return true;
   }

   public void c() {
      if(this.chunkLoader != null) {
         this.chunkLoader.b();
      }

   }

   public boolean unloadChunks() {
      if(!this.world.savingDisabled) {
         for(int var1 = 0; var1 < 100; ++var1) {
            if(!this.unloadQueue.isEmpty()) {
               Long var2 = (Long)this.unloadQueue.iterator().next();
               Chunk var3 = (Chunk)this.chunks.getEntry(var2.longValue());
               if(var3 != null) {
                  var3.removeEntities();
                  this.saveChunk(var3);
                  this.saveChunkNOP(var3);
                  this.chunks.remove(var2.longValue());
                  this.chunkList.remove(var3);
               }

               this.unloadQueue.remove(var2);
            }
         }

         if(this.chunkLoader != null) {
            this.chunkLoader.a();
         }
      }

      return this.chunkProvider.unloadChunks();
   }

   public boolean canSave() {
      return !this.world.savingDisabled;
   }

   public String getName() {
      return "ServerChunkCache: " + this.chunks.count() + " Drop: " + this.unloadQueue.size();
   }

   public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType var1, BlockPosition var2) {
      return this.chunkProvider.getMobsFor(var1, var2);
   }

   public BlockPosition findNearestMapFeature(World var1, String var2, BlockPosition var3) {
      return this.chunkProvider.findNearestMapFeature(var1, var2, var3);
   }

   public int getLoadedChunks() {
      return this.chunks.count();
   }

   public void recreateStructures(Chunk var1, int var2, int var3) {
   }

   public Chunk getChunkAt(BlockPosition var1) {
      return this.getOrCreateChunk(var1.getX() >> 4, var1.getZ() >> 4);
   }
}
