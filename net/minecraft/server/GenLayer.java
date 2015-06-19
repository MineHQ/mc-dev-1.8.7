package net.minecraft.server;

import java.util.concurrent.Callable;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.CustomWorldSettingsFinal;
import net.minecraft.server.GenLayerBiome;
import net.minecraft.server.GenLayerCleaner;
import net.minecraft.server.GenLayerDeepOcean;
import net.minecraft.server.GenLayerDesert;
import net.minecraft.server.GenLayerIcePlains;
import net.minecraft.server.GenLayerIsland;
import net.minecraft.server.GenLayerMushroomIsland;
import net.minecraft.server.GenLayerMushroomShore;
import net.minecraft.server.GenLayerPlains;
import net.minecraft.server.GenLayerRegionHills;
import net.minecraft.server.GenLayerRiver;
import net.minecraft.server.GenLayerRiverMix;
import net.minecraft.server.GenLayerSmooth;
import net.minecraft.server.GenLayerSpecial;
import net.minecraft.server.GenLayerTopSoil;
import net.minecraft.server.GenLayerZoom;
import net.minecraft.server.GenLayerZoomFuzzy;
import net.minecraft.server.GenLayerZoomVoronoi;
import net.minecraft.server.LayerIsland;
import net.minecraft.server.ReportedException;
import net.minecraft.server.WorldType;

public abstract class GenLayer {
   private long c;
   protected GenLayer a;
   private long d;
   protected long b;

   public static GenLayer[] a(long var0, WorldType var2, String var3) {
      LayerIsland var4 = new LayerIsland(1L);
      GenLayerZoomFuzzy var13 = new GenLayerZoomFuzzy(2000L, var4);
      GenLayerIsland var14 = new GenLayerIsland(1L, var13);
      GenLayerZoom var15 = new GenLayerZoom(2001L, var14);
      var14 = new GenLayerIsland(2L, var15);
      var14 = new GenLayerIsland(50L, var14);
      var14 = new GenLayerIsland(70L, var14);
      GenLayerIcePlains var16 = new GenLayerIcePlains(2L, var14);
      GenLayerTopSoil var17 = new GenLayerTopSoil(2L, var16);
      var14 = new GenLayerIsland(3L, var17);
      GenLayerSpecial var18 = new GenLayerSpecial(2L, var14, GenLayerSpecial.EnumGenLayerSpecial.COOL_WARM);
      var18 = new GenLayerSpecial(2L, var18, GenLayerSpecial.EnumGenLayerSpecial.HEAT_ICE);
      var18 = new GenLayerSpecial(3L, var18, GenLayerSpecial.EnumGenLayerSpecial.SPECIAL);
      var15 = new GenLayerZoom(2002L, var18);
      var15 = new GenLayerZoom(2003L, var15);
      var14 = new GenLayerIsland(4L, var15);
      GenLayerMushroomIsland var20 = new GenLayerMushroomIsland(5L, var14);
      GenLayerDeepOcean var23 = new GenLayerDeepOcean(4L, var20);
      GenLayer var26 = GenLayerZoom.b(1000L, var23, 0);
      CustomWorldSettingsFinal var5 = null;
      int var6 = 4;
      int var7 = var6;
      if(var2 == WorldType.CUSTOMIZED && var3.length() > 0) {
         var5 = CustomWorldSettingsFinal.CustomWorldSettings.a(var3).b();
         var6 = var5.G;
         var7 = var5.H;
      }

      if(var2 == WorldType.LARGE_BIOMES) {
         var6 = 6;
      }

      GenLayer var8 = GenLayerZoom.b(1000L, var26, 0);
      GenLayerCleaner var19 = new GenLayerCleaner(100L, var8);
      GenLayerBiome var9 = new GenLayerBiome(200L, var26, var2, var3);
      GenLayer var21 = GenLayerZoom.b(1000L, var9, 2);
      GenLayerDesert var24 = new GenLayerDesert(1000L, var21);
      GenLayer var10 = GenLayerZoom.b(1000L, var19, 2);
      GenLayerRegionHills var27 = new GenLayerRegionHills(1000L, var24, var10);
      var8 = GenLayerZoom.b(1000L, var19, 2);
      var8 = GenLayerZoom.b(1000L, var8, var7);
      GenLayerRiver var22 = new GenLayerRiver(1L, var8);
      GenLayerSmooth var25 = new GenLayerSmooth(1000L, var22);
      Object var28 = new GenLayerPlains(1001L, var27);

      for(int var11 = 0; var11 < var6; ++var11) {
         var28 = new GenLayerZoom((long)(1000 + var11), (GenLayer)var28);
         if(var11 == 0) {
            var28 = new GenLayerIsland(3L, (GenLayer)var28);
         }

         if(var11 == 1 || var6 == 1) {
            var28 = new GenLayerMushroomShore(1000L, (GenLayer)var28);
         }
      }

      GenLayerSmooth var29 = new GenLayerSmooth(1000L, (GenLayer)var28);
      GenLayerRiverMix var30 = new GenLayerRiverMix(100L, var29, var25);
      GenLayerZoomVoronoi var12 = new GenLayerZoomVoronoi(10L, var30);
      var30.a(var0);
      var12.a(var0);
      return new GenLayer[]{var30, var12, var30};
   }

   public GenLayer(long var1) {
      this.b = var1;
      this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
      this.b += var1;
      this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
      this.b += var1;
      this.b *= this.b * 6364136223846793005L + 1442695040888963407L;
      this.b += var1;
   }

   public void a(long var1) {
      this.c = var1;
      if(this.a != null) {
         this.a.a(var1);
      }

      this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
      this.c += this.b;
      this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
      this.c += this.b;
      this.c *= this.c * 6364136223846793005L + 1442695040888963407L;
      this.c += this.b;
   }

   public void a(long var1, long var3) {
      this.d = this.c;
      this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
      this.d += var1;
      this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
      this.d += var3;
      this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
      this.d += var1;
      this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
      this.d += var3;
   }

   protected int a(int var1) {
      int var2 = (int)((this.d >> 24) % (long)var1);
      if(var2 < 0) {
         var2 += var1;
      }

      this.d *= this.d * 6364136223846793005L + 1442695040888963407L;
      this.d += this.c;
      return var2;
   }

   public abstract int[] a(int var1, int var2, int var3, int var4);

   protected static boolean a(int var0, int var1) {
      if(var0 == var1) {
         return true;
      } else if(var0 != BiomeBase.MESA_PLATEAU_F.id && var0 != BiomeBase.MESA_PLATEAU.id) {
         final BiomeBase var2 = BiomeBase.getBiome(var0);
         final BiomeBase var3 = BiomeBase.getBiome(var1);

         try {
            return var2 != null && var3 != null?var2.a(var3):false;
         } catch (Throwable var7) {
            CrashReport var5 = CrashReport.a(var7, "Comparing biomes");
            CrashReportSystemDetails var6 = var5.a("Biomes being compared");
            var6.a((String)"Biome A ID", (Object)Integer.valueOf(var0));
            var6.a((String)"Biome B ID", (Object)Integer.valueOf(var1));
            var6.a("Biome A", new Callable() {
               public String a() throws Exception {
                  return String.valueOf(var2);
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            var6.a("Biome B", new Callable() {
               public String a() throws Exception {
                  return String.valueOf(var3);
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            throw new ReportedException(var5);
         }
      } else {
         return var1 == BiomeBase.MESA_PLATEAU_F.id || var1 == BiomeBase.MESA_PLATEAU.id;
      }
   }

   protected static boolean b(int var0) {
      return var0 == BiomeBase.OCEAN.id || var0 == BiomeBase.DEEP_OCEAN.id || var0 == BiomeBase.FROZEN_OCEAN.id;
   }

   protected int a(int... var1) {
      return var1[this.a(var1.length)];
   }

   protected int b(int var1, int var2, int var3, int var4) {
      return var2 == var3 && var3 == var4?var2:(var1 == var2 && var1 == var3?var1:(var1 == var2 && var1 == var4?var1:(var1 == var3 && var1 == var4?var1:(var1 == var2 && var3 != var4?var1:(var1 == var3 && var2 != var4?var1:(var1 == var4 && var2 != var3?var1:(var2 == var3 && var1 != var4?var2:(var2 == var4 && var1 != var3?var2:(var3 == var4 && var1 != var2?var3:this.a(new int[]{var1, var2, var3, var4}))))))))));
   }
}
