package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.ChunkSnapshot;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.PersistentStructure;
import net.minecraft.server.ReportedException;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.StructureStart;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenBase;
import net.minecraft.server.WorldGenFactory;

public abstract class StructureGenerator extends WorldGenBase {
   private PersistentStructure d;
   protected Map<Long, StructureStart> e = Maps.newHashMap();

   public StructureGenerator() {
   }

   public abstract String a();

   protected final void a(World var1, final int var2, final int var3, int var4, int var5, ChunkSnapshot var6) {
      this.a(var1);
      if(!this.e.containsKey(Long.valueOf(ChunkCoordIntPair.a(var2, var3)))) {
         this.b.nextInt();

         try {
            if(this.a(var2, var3)) {
               StructureStart var7 = this.b(var2, var3);
               this.e.put(Long.valueOf(ChunkCoordIntPair.a(var2, var3)), var7);
               this.a(var2, var3, var7);
            }

         } catch (Throwable var10) {
            CrashReport var8 = CrashReport.a(var10, "Exception preparing structure feature");
            CrashReportSystemDetails var9 = var8.a("Feature being prepared");
            var9.a("Is feature chunk", new Callable() {
               public String a() throws Exception {
                  return StructureGenerator.this.a(var2, var3)?"True":"False";
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            var9.a((String)"Chunk location", (Object)String.format("%d,%d", new Object[]{Integer.valueOf(var2), Integer.valueOf(var3)}));
            var9.a("Chunk pos hash", new Callable() {
               public String a() throws Exception {
                  return String.valueOf(ChunkCoordIntPair.a(var2, var3));
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            var9.a("Structure type", new Callable() {
               public String a() throws Exception {
                  return StructureGenerator.this.getClass().getCanonicalName();
               }

               // $FF: synthetic method
               public Object call() throws Exception {
                  return this.a();
               }
            });
            throw new ReportedException(var8);
         }
      }
   }

   public boolean a(World var1, Random var2, ChunkCoordIntPair var3) {
      this.a(var1);
      int var4 = (var3.x << 4) + 8;
      int var5 = (var3.z << 4) + 8;
      boolean var6 = false;
      Iterator var7 = this.e.values().iterator();

      while(var7.hasNext()) {
         StructureStart var8 = (StructureStart)var7.next();
         if(var8.d() && var8.a(var3) && var8.a().a(var4, var5, var4 + 15, var5 + 15)) {
            var8.a(var1, var2, new StructureBoundingBox(var4, var5, var4 + 15, var5 + 15));
            var8.b(var3);
            var6 = true;
            this.a(var8.e(), var8.f(), var8);
         }
      }

      return var6;
   }

   public boolean b(BlockPosition var1) {
      this.a(this.c);
      return this.c(var1) != null;
   }

   protected StructureStart c(BlockPosition var1) {
      Iterator var2 = this.e.values().iterator();

      while(true) {
         StructureStart var3;
         do {
            do {
               if(!var2.hasNext()) {
                  return null;
               }

               var3 = (StructureStart)var2.next();
            } while(!var3.d());
         } while(!var3.a().b((BaseBlockPosition)var1));

         Iterator var4 = var3.b().iterator();

         while(var4.hasNext()) {
            StructurePiece var5 = (StructurePiece)var4.next();
            if(var5.c().b((BaseBlockPosition)var1)) {
               return var3;
            }
         }
      }
   }

   public boolean a(World var1, BlockPosition var2) {
      this.a(var1);
      Iterator var3 = this.e.values().iterator();

      StructureStart var4;
      do {
         if(!var3.hasNext()) {
            return false;
         }

         var4 = (StructureStart)var3.next();
      } while(!var4.d() || !var4.a().b((BaseBlockPosition)var2));

      return true;
   }

   public BlockPosition getNearestGeneratedFeature(World var1, BlockPosition var2) {
      this.c = var1;
      this.a(var1);
      this.b.setSeed(var1.getSeed());
      long var3 = this.b.nextLong();
      long var5 = this.b.nextLong();
      long var7 = (long)(var2.getX() >> 4) * var3;
      long var9 = (long)(var2.getZ() >> 4) * var5;
      this.b.setSeed(var7 ^ var9 ^ var1.getSeed());
      this.a(var1, var2.getX() >> 4, var2.getZ() >> 4, 0, 0, (ChunkSnapshot)null);
      double var11 = Double.MAX_VALUE;
      BlockPosition var13 = null;
      Iterator var14 = this.e.values().iterator();

      BlockPosition var17;
      double var18;
      while(var14.hasNext()) {
         StructureStart var15 = (StructureStart)var14.next();
         if(var15.d()) {
            StructurePiece var16 = (StructurePiece)var15.b().get(0);
            var17 = var16.a();
            var18 = var17.i(var2);
            if(var18 < var11) {
               var11 = var18;
               var13 = var17;
            }
         }
      }

      if(var13 != null) {
         return var13;
      } else {
         List var20 = this.z_();
         if(var20 != null) {
            BlockPosition var21 = null;
            Iterator var22 = var20.iterator();

            while(var22.hasNext()) {
               var17 = (BlockPosition)var22.next();
               var18 = var17.i(var2);
               if(var18 < var11) {
                  var11 = var18;
                  var21 = var17;
               }
            }

            return var21;
         } else {
            return null;
         }
      }
   }

   protected List<BlockPosition> z_() {
      return null;
   }

   private void a(World var1) {
      if(this.d == null) {
         this.d = (PersistentStructure)var1.a(PersistentStructure.class, this.a());
         if(this.d == null) {
            this.d = new PersistentStructure(this.a());
            var1.a((String)this.a(), (PersistentBase)this.d);
         } else {
            NBTTagCompound var2 = this.d.a();
            Iterator var3 = var2.c().iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               NBTBase var5 = var2.get(var4);
               if(var5.getTypeId() == 10) {
                  NBTTagCompound var6 = (NBTTagCompound)var5;
                  if(var6.hasKey("ChunkX") && var6.hasKey("ChunkZ")) {
                     int var7 = var6.getInt("ChunkX");
                     int var8 = var6.getInt("ChunkZ");
                     StructureStart var9 = WorldGenFactory.a(var6, var1);
                     if(var9 != null) {
                        this.e.put(Long.valueOf(ChunkCoordIntPair.a(var7, var8)), var9);
                     }
                  }
               }
            }
         }
      }

   }

   private void a(int var1, int var2, StructureStart var3) {
      this.d.a(var3.a(var1, var2), var1, var2);
      this.d.c();
   }

   protected abstract boolean a(int var1, int var2);

   protected abstract StructureStart b(int var1, int var2);
}
