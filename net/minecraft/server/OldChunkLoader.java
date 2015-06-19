package net.minecraft.server;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NibbleArray;
import net.minecraft.server.OldNibbleArray;
import net.minecraft.server.WorldChunkManager;

public class OldChunkLoader {
   public static OldChunkLoader.OldChunk a(NBTTagCompound var0) {
      int var1 = var0.getInt("xPos");
      int var2 = var0.getInt("zPos");
      OldChunkLoader.OldChunk var3 = new OldChunkLoader.OldChunk(var1, var2);
      var3.g = var0.getByteArray("Blocks");
      var3.f = new OldNibbleArray(var0.getByteArray("Data"), 7);
      var3.e = new OldNibbleArray(var0.getByteArray("SkyLight"), 7);
      var3.d = new OldNibbleArray(var0.getByteArray("BlockLight"), 7);
      var3.c = var0.getByteArray("HeightMap");
      var3.b = var0.getBoolean("TerrainPopulated");
      var3.h = var0.getList("Entities", 10);
      var3.i = var0.getList("TileEntities", 10);
      var3.j = var0.getList("TileTicks", 10);

      try {
         var3.a = var0.getLong("LastUpdate");
      } catch (ClassCastException var5) {
         var3.a = (long)var0.getInt("LastUpdate");
      }

      return var3;
   }

   public static void a(OldChunkLoader.OldChunk var0, NBTTagCompound var1, WorldChunkManager var2) {
      var1.setInt("xPos", var0.k);
      var1.setInt("zPos", var0.l);
      var1.setLong("LastUpdate", var0.a);
      int[] var3 = new int[var0.c.length];

      for(int var4 = 0; var4 < var0.c.length; ++var4) {
         var3[var4] = var0.c[var4];
      }

      var1.setIntArray("HeightMap", var3);
      var1.setBoolean("TerrainPopulated", var0.b);
      NBTTagList var16 = new NBTTagList();

      int var7;
      int var8;
      for(int var5 = 0; var5 < 8; ++var5) {
         boolean var6 = true;

         for(var7 = 0; var7 < 16 && var6; ++var7) {
            for(var8 = 0; var8 < 16 && var6; ++var8) {
               for(int var9 = 0; var9 < 16; ++var9) {
                  int var10 = var7 << 11 | var9 << 7 | var8 + (var5 << 4);
                  byte var11 = var0.g[var10];
                  if(var11 != 0) {
                     var6 = false;
                     break;
                  }
               }
            }
         }

         if(!var6) {
            byte[] var19 = new byte[4096];
            NibbleArray var20 = new NibbleArray();
            NibbleArray var21 = new NibbleArray();
            NibbleArray var22 = new NibbleArray();

            for(int var23 = 0; var23 < 16; ++var23) {
               for(int var12 = 0; var12 < 16; ++var12) {
                  for(int var13 = 0; var13 < 16; ++var13) {
                     int var14 = var23 << 11 | var13 << 7 | var12 + (var5 << 4);
                     byte var15 = var0.g[var14];
                     var19[var12 << 8 | var13 << 4 | var23] = (byte)(var15 & 255);
                     var20.a(var23, var12, var13, var0.f.a(var23, var12 + (var5 << 4), var13));
                     var21.a(var23, var12, var13, var0.e.a(var23, var12 + (var5 << 4), var13));
                     var22.a(var23, var12, var13, var0.d.a(var23, var12 + (var5 << 4), var13));
                  }
               }
            }

            NBTTagCompound var24 = new NBTTagCompound();
            var24.setByte("Y", (byte)(var5 & 255));
            var24.setByteArray("Blocks", var19);
            var24.setByteArray("Data", var20.a());
            var24.setByteArray("SkyLight", var21.a());
            var24.setByteArray("BlockLight", var22.a());
            var16.add(var24);
         }
      }

      var1.set("Sections", var16);
      byte[] var17 = new byte[256];
      BlockPosition.MutableBlockPosition var18 = new BlockPosition.MutableBlockPosition();

      for(var7 = 0; var7 < 16; ++var7) {
         for(var8 = 0; var8 < 16; ++var8) {
            var18.c(var0.k << 4 | var7, 0, var0.l << 4 | var8);
            var17[var8 << 4 | var7] = (byte)(var2.getBiome(var18, BiomeBase.ad).id & 255);
         }
      }

      var1.setByteArray("Biomes", var17);
      var1.set("Entities", var0.h);
      var1.set("TileEntities", var0.i);
      if(var0.j != null) {
         var1.set("TileTicks", var0.j);
      }

   }

   public static class OldChunk {
      public long a;
      public boolean b;
      public byte[] c;
      public OldNibbleArray d;
      public OldNibbleArray e;
      public OldNibbleArray f;
      public byte[] g;
      public NBTTagList h;
      public NBTTagList i;
      public NBTTagList j;
      public final int k;
      public final int l;

      public OldChunk(int var1, int var2) {
         this.k = var1;
         this.l = var2;
      }
   }
}
