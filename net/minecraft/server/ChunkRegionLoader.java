package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.FileIOThread;
import net.minecraft.server.IAsyncChunkSaver;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NextTickListEntry;
import net.minecraft.server.NibbleArray;
import net.minecraft.server.RegionFileCache;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkRegionLoader implements IChunkLoader, IAsyncChunkSaver {
   private static final Logger a = LogManager.getLogger();
   private Map<ChunkCoordIntPair, NBTTagCompound> b = new ConcurrentHashMap();
   private Set<ChunkCoordIntPair> c = Collections.newSetFromMap(new ConcurrentHashMap());
   private final File d;

   public ChunkRegionLoader(File var1) {
      this.d = var1;
   }

   public Chunk a(World var1, int var2, int var3) throws IOException {
      ChunkCoordIntPair var4 = new ChunkCoordIntPair(var2, var3);
      NBTTagCompound var5 = (NBTTagCompound)this.b.get(var4);
      if(var5 == null) {
         DataInputStream var6 = RegionFileCache.c(this.d, var2, var3);
         if(var6 == null) {
            return null;
         }

         var5 = NBTCompressedStreamTools.a(var6);
      }

      return this.a(var1, var2, var3, var5);
   }

   protected Chunk a(World var1, int var2, int var3, NBTTagCompound var4) {
      if(!var4.hasKeyOfType("Level", 10)) {
         a.error("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
         return null;
      } else {
         NBTTagCompound var5 = var4.getCompound("Level");
         if(!var5.hasKeyOfType("Sections", 9)) {
            a.error("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
            return null;
         } else {
            Chunk var6 = this.a(var1, var5);
            if(!var6.a(var2, var3)) {
               a.error("Chunk file at " + var2 + "," + var3 + " is in the wrong location; relocating. (Expected " + var2 + ", " + var3 + ", got " + var6.locX + ", " + var6.locZ + ")");
               var5.setInt("xPos", var2);
               var5.setInt("zPos", var3);
               var6 = this.a(var1, var5);
            }

            return var6;
         }
      }
   }

   public void a(World var1, Chunk var2) throws IOException, ExceptionWorldConflict {
      var1.checkSession();

      try {
         NBTTagCompound var3 = new NBTTagCompound();
         NBTTagCompound var4 = new NBTTagCompound();
         var3.set("Level", var4);
         this.a(var2, var1, var4);
         this.a(var2.j(), var3);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   protected void a(ChunkCoordIntPair var1, NBTTagCompound var2) {
      if(!this.c.contains(var1)) {
         this.b.put(var1, var2);
      }

      FileIOThread.a().a(this);
   }

   public boolean c() {
      if(this.b.isEmpty()) {
         return false;
      } else {
         ChunkCoordIntPair var1 = (ChunkCoordIntPair)this.b.keySet().iterator().next();

         boolean var3;
         try {
            this.c.add(var1);
            NBTTagCompound var2 = (NBTTagCompound)this.b.remove(var1);
            if(var2 != null) {
               try {
                  this.b(var1, var2);
               } catch (Exception var7) {
                  var7.printStackTrace();
               }
            }

            var3 = true;
         } finally {
            this.c.remove(var1);
         }

         return var3;
      }
   }

   private void b(ChunkCoordIntPair var1, NBTTagCompound var2) throws IOException {
      DataOutputStream var3 = RegionFileCache.d(this.d, var1.x, var1.z);
      NBTCompressedStreamTools.a((NBTTagCompound)var2, (DataOutput)var3);
      var3.close();
   }

   public void b(World var1, Chunk var2) throws IOException {
   }

   public void a() {
   }

   public void b() {
      while(this.c()) {
         ;
      }

   }

   private void a(Chunk var1, World var2, NBTTagCompound var3) {
      var3.setByte("V", (byte)1);
      var3.setInt("xPos", var1.locX);
      var3.setInt("zPos", var1.locZ);
      var3.setLong("LastUpdate", var2.getTime());
      var3.setIntArray("HeightMap", var1.q());
      var3.setBoolean("TerrainPopulated", var1.isDone());
      var3.setBoolean("LightPopulated", var1.u());
      var3.setLong("InhabitedTime", var1.w());
      ChunkSection[] var4 = var1.getSections();
      NBTTagList var5 = new NBTTagList();
      boolean var6 = !var2.worldProvider.o();
      ChunkSection[] var7 = var4;
      int var8 = var4.length;

      NBTTagCompound var11;
      for(int var9 = 0; var9 < var8; ++var9) {
         ChunkSection var10 = var7[var9];
         if(var10 != null) {
            var11 = new NBTTagCompound();
            var11.setByte("Y", (byte)(var10.getYPosition() >> 4 & 255));
            byte[] var12 = new byte[var10.getIdArray().length];
            NibbleArray var13 = new NibbleArray();
            NibbleArray var14 = null;

            for(int var15 = 0; var15 < var10.getIdArray().length; ++var15) {
               char var16 = var10.getIdArray()[var15];
               int var17 = var15 & 15;
               int var18 = var15 >> 8 & 15;
               int var19 = var15 >> 4 & 15;
               if(var16 >> 12 != 0) {
                  if(var14 == null) {
                     var14 = new NibbleArray();
                  }

                  var14.a(var17, var18, var19, var16 >> 12);
               }

               var12[var15] = (byte)(var16 >> 4 & 255);
               var13.a(var17, var18, var19, var16 & 15);
            }

            var11.setByteArray("Blocks", var12);
            var11.setByteArray("Data", var13.a());
            if(var14 != null) {
               var11.setByteArray("Add", var14.a());
            }

            var11.setByteArray("BlockLight", var10.getEmittedLightArray().a());
            if(var6) {
               var11.setByteArray("SkyLight", var10.getSkyLightArray().a());
            } else {
               var11.setByteArray("SkyLight", new byte[var10.getEmittedLightArray().a().length]);
            }

            var5.add(var11);
         }
      }

      var3.set("Sections", var5);
      var3.setByteArray("Biomes", var1.getBiomeIndex());
      var1.g(false);
      NBTTagList var20 = new NBTTagList();

      Iterator var22;
      for(var8 = 0; var8 < var1.getEntitySlices().length; ++var8) {
         var22 = var1.getEntitySlices()[var8].iterator();

         while(var22.hasNext()) {
            Entity var24 = (Entity)var22.next();
            var11 = new NBTTagCompound();
            if(var24.d(var11)) {
               var1.g(true);
               var20.add(var11);
            }
         }
      }

      var3.set("Entities", var20);
      NBTTagList var21 = new NBTTagList();
      var22 = var1.getTileEntities().values().iterator();

      while(var22.hasNext()) {
         TileEntity var25 = (TileEntity)var22.next();
         var11 = new NBTTagCompound();
         var25.b(var11);
         var21.add(var11);
      }

      var3.set("TileEntities", var21);
      List var23 = var2.a(var1, false);
      if(var23 != null) {
         long var26 = var2.getTime();
         NBTTagList var27 = new NBTTagList();
         Iterator var28 = var23.iterator();

         while(var28.hasNext()) {
            NextTickListEntry var29 = (NextTickListEntry)var28.next();
            NBTTagCompound var30 = new NBTTagCompound();
            MinecraftKey var31 = (MinecraftKey)Block.REGISTRY.c(var29.a());
            var30.setString("i", var31 == null?"":var31.toString());
            var30.setInt("x", var29.a.getX());
            var30.setInt("y", var29.a.getY());
            var30.setInt("z", var29.a.getZ());
            var30.setInt("t", (int)(var29.b - var26));
            var30.setInt("p", var29.c);
            var27.add(var30);
         }

         var3.set("TileTicks", var27);
      }

   }

   private Chunk a(World var1, NBTTagCompound var2) {
      int var3 = var2.getInt("xPos");
      int var4 = var2.getInt("zPos");
      Chunk var5 = new Chunk(var1, var3, var4);
      var5.a(var2.getIntArray("HeightMap"));
      var5.d(var2.getBoolean("TerrainPopulated"));
      var5.e(var2.getBoolean("LightPopulated"));
      var5.c(var2.getLong("InhabitedTime"));
      NBTTagList var6 = var2.getList("Sections", 10);
      byte var7 = 16;
      ChunkSection[] var8 = new ChunkSection[var7];
      boolean var9 = !var1.worldProvider.o();

      for(int var10 = 0; var10 < var6.size(); ++var10) {
         NBTTagCompound var11 = var6.get(var10);
         byte var12 = var11.getByte("Y");
         ChunkSection var13 = new ChunkSection(var12 << 4, var9);
         byte[] var14 = var11.getByteArray("Blocks");
         NibbleArray var15 = new NibbleArray(var11.getByteArray("Data"));
         NibbleArray var16 = var11.hasKeyOfType("Add", 7)?new NibbleArray(var11.getByteArray("Add")):null;
         char[] var17 = new char[var14.length];

         for(int var18 = 0; var18 < var17.length; ++var18) {
            int var19 = var18 & 15;
            int var20 = var18 >> 8 & 15;
            int var21 = var18 >> 4 & 15;
            int var22 = var16 != null?var16.a(var19, var20, var21):0;
            var17[var18] = (char)(var22 << 12 | (var14[var18] & 255) << 4 | var15.a(var19, var20, var21));
         }

         var13.a(var17);
         var13.a(new NibbleArray(var11.getByteArray("BlockLight")));
         if(var9) {
            var13.b(new NibbleArray(var11.getByteArray("SkyLight")));
         }

         var13.recalcBlockCounts();
         var8[var12] = var13;
      }

      var5.a(var8);
      if(var2.hasKeyOfType("Biomes", 7)) {
         var5.a(var2.getByteArray("Biomes"));
      }

      NBTTagList var23 = var2.getList("Entities", 10);
      if(var23 != null) {
         for(int var24 = 0; var24 < var23.size(); ++var24) {
            NBTTagCompound var26 = var23.get(var24);
            Entity var28 = EntityTypes.a(var26, var1);
            var5.g(true);
            if(var28 != null) {
               var5.a(var28);
               Entity var32 = var28;

               for(NBTTagCompound var34 = var26; var34.hasKeyOfType("Riding", 10); var34 = var34.getCompound("Riding")) {
                  Entity var37 = EntityTypes.a(var34.getCompound("Riding"), var1);
                  if(var37 != null) {
                     var5.a(var37);
                     var32.mount(var37);
                  }

                  var32 = var37;
               }
            }
         }
      }

      NBTTagList var25 = var2.getList("TileEntities", 10);
      if(var25 != null) {
         for(int var27 = 0; var27 < var25.size(); ++var27) {
            NBTTagCompound var30 = var25.get(var27);
            TileEntity var33 = TileEntity.c(var30);
            if(var33 != null) {
               var5.a(var33);
            }
         }
      }

      if(var2.hasKeyOfType("TileTicks", 9)) {
         NBTTagList var29 = var2.getList("TileTicks", 10);
         if(var29 != null) {
            for(int var31 = 0; var31 < var29.size(); ++var31) {
               NBTTagCompound var35 = var29.get(var31);
               Block var36;
               if(var35.hasKeyOfType("i", 8)) {
                  var36 = Block.getByName(var35.getString("i"));
               } else {
                  var36 = Block.getById(var35.getInt("i"));
               }

               var1.b(new BlockPosition(var35.getInt("x"), var35.getInt("y"), var35.getInt("z")), var36, var35.getInt("t"), var35.getInt("p"));
            }
         }
      }

      return var5;
   }
}
