package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityGuardian;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructureGenerator;
import net.minecraft.server.StructureStart;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenMonumentPieces;

public class WorldGenMonument extends StructureGenerator {
   private int f;
   private int g;
   public static final List<BiomeBase> d;
   private static final List<BiomeBase.BiomeMeta> h;

   public WorldGenMonument() {
      this.f = 32;
      this.g = 5;
   }

   public WorldGenMonument(Map<String, String> var1) {
      this();
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if(((String)var3.getKey()).equals("spacing")) {
            this.f = MathHelper.a((String)var3.getValue(), this.f, 1);
         } else if(((String)var3.getKey()).equals("separation")) {
            this.g = MathHelper.a((String)var3.getValue(), this.g, 1);
         }
      }

   }

   public String a() {
      return "Monument";
   }

   protected boolean a(int var1, int var2) {
      int var3 = var1;
      int var4 = var2;
      if(var1 < 0) {
         var1 -= this.f - 1;
      }

      if(var2 < 0) {
         var2 -= this.f - 1;
      }

      int var5 = var1 / this.f;
      int var6 = var2 / this.f;
      Random var7 = this.c.a(var5, var6, 10387313);
      var5 *= this.f;
      var6 *= this.f;
      var5 += (var7.nextInt(this.f - this.g) + var7.nextInt(this.f - this.g)) / 2;
      var6 += (var7.nextInt(this.f - this.g) + var7.nextInt(this.f - this.g)) / 2;
      if(var3 == var5 && var4 == var6) {
         if(this.c.getWorldChunkManager().getBiome(new BlockPosition(var3 * 16 + 8, 64, var4 * 16 + 8), (BiomeBase)null) != BiomeBase.DEEP_OCEAN) {
            return false;
         }

         boolean var8 = this.c.getWorldChunkManager().a(var3 * 16 + 8, var4 * 16 + 8, 29, d);
         if(var8) {
            return true;
         }
      }

      return false;
   }

   protected StructureStart b(int var1, int var2) {
      return new WorldGenMonument.WorldGenMonumentStart(this.c, this.b, var1, var2);
   }

   public List<BiomeBase.BiomeMeta> b() {
      return h;
   }

   static {
      d = Arrays.asList(new BiomeBase[]{BiomeBase.OCEAN, BiomeBase.DEEP_OCEAN, BiomeBase.RIVER, BiomeBase.FROZEN_OCEAN, BiomeBase.FROZEN_RIVER});
      h = Lists.newArrayList();
      h.add(new BiomeBase.BiomeMeta(EntityGuardian.class, 1, 2, 4));
   }

   public static class WorldGenMonumentStart extends StructureStart {
      private Set<ChunkCoordIntPair> c = Sets.newHashSet();
      private boolean d;

      public WorldGenMonumentStart() {
      }

      public WorldGenMonumentStart(World var1, Random var2, int var3, int var4) {
         super(var3, var4);
         this.b(var1, var2, var3, var4);
      }

      private void b(World var1, Random var2, int var3, int var4) {
         var2.setSeed(var1.getSeed());
         long var5 = var2.nextLong();
         long var7 = var2.nextLong();
         long var9 = (long)var3 * var5;
         long var11 = (long)var4 * var7;
         var2.setSeed(var9 ^ var11 ^ var1.getSeed());
         int var13 = var3 * 16 + 8 - 29;
         int var14 = var4 * 16 + 8 - 29;
         EnumDirection var15 = EnumDirection.EnumDirectionLimit.HORIZONTAL.a(var2);
         this.a.add(new WorldGenMonumentPieces.WorldGenMonumentPiece1(var2, var13, var14, var15));
         this.c();
         this.d = true;
      }

      public void a(World var1, Random var2, StructureBoundingBox var3) {
         if(!this.d) {
            this.a.clear();
            this.b(var1, var2, this.e(), this.f());
         }

         super.a(var1, var2, var3);
      }

      public boolean a(ChunkCoordIntPair var1) {
         return this.c.contains(var1)?false:super.a(var1);
      }

      public void b(ChunkCoordIntPair var1) {
         super.b(var1);
         this.c.add(var1);
      }

      public void a(NBTTagCompound var1) {
         super.a(var1);
         NBTTagList var2 = new NBTTagList();
         Iterator var3 = this.c.iterator();

         while(var3.hasNext()) {
            ChunkCoordIntPair var4 = (ChunkCoordIntPair)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setInt("X", var4.x);
            var5.setInt("Z", var4.z);
            var2.add(var5);
         }

         var1.set("Processed", var2);
      }

      public void b(NBTTagCompound var1) {
         super.b(var1);
         if(var1.hasKeyOfType("Processed", 9)) {
            NBTTagList var2 = var1.getList("Processed", 10);

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               NBTTagCompound var4 = var2.get(var3);
               this.c.add(new ChunkCoordIntPair(var4.getInt("X"), var4.getInt("Z")));
            }
         }

      }
   }
}
