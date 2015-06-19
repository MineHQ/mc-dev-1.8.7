package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.EntityBlaze;
import net.minecraft.server.EntityMagmaCube;
import net.minecraft.server.EntityPigZombie;
import net.minecraft.server.EntitySkeleton;
import net.minecraft.server.StructureGenerator;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.StructureStart;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenNetherPieces;

public class WorldGenNether extends StructureGenerator {
   private List<BiomeBase.BiomeMeta> d = Lists.newArrayList();

   public WorldGenNether() {
      this.d.add(new BiomeBase.BiomeMeta(EntityBlaze.class, 10, 2, 3));
      this.d.add(new BiomeBase.BiomeMeta(EntityPigZombie.class, 5, 4, 4));
      this.d.add(new BiomeBase.BiomeMeta(EntitySkeleton.class, 10, 4, 4));
      this.d.add(new BiomeBase.BiomeMeta(EntityMagmaCube.class, 3, 4, 4));
   }

   public String a() {
      return "Fortress";
   }

   public List<BiomeBase.BiomeMeta> b() {
      return this.d;
   }

   protected boolean a(int var1, int var2) {
      int var3 = var1 >> 4;
      int var4 = var2 >> 4;
      this.b.setSeed((long)(var3 ^ var4 << 4) ^ this.c.getSeed());
      this.b.nextInt();
      return this.b.nextInt(3) != 0?false:(var1 != (var3 << 4) + 4 + this.b.nextInt(8)?false:var2 == (var4 << 4) + 4 + this.b.nextInt(8));
   }

   protected StructureStart b(int var1, int var2) {
      return new WorldGenNether.WorldGenNetherStart(this.c, this.b, var1, var2);
   }

   public static class WorldGenNetherStart extends StructureStart {
      public WorldGenNetherStart() {
      }

      public WorldGenNetherStart(World var1, Random var2, int var3, int var4) {
         super(var3, var4);
         WorldGenNetherPieces.WorldGenNetherPiece15 var5 = new WorldGenNetherPieces.WorldGenNetherPiece15(var2, (var3 << 4) + 2, (var4 << 4) + 2);
         this.a.add(var5);
         var5.a(var5, this.a, var2);
         List var6 = var5.e;

         while(!var6.isEmpty()) {
            int var7 = var2.nextInt(var6.size());
            StructurePiece var8 = (StructurePiece)var6.remove(var7);
            var8.a((StructurePiece)var5, (List)this.a, (Random)var2);
         }

         this.c();
         this.a(var1, var2, 48, 70);
      }
   }
}
