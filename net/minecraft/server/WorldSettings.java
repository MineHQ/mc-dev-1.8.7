package net.minecraft.server;

import net.minecraft.server.PlayerAbilities;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldType;

public final class WorldSettings {
   private final long a;
   private final WorldSettings.EnumGamemode b;
   private final boolean c;
   private final boolean d;
   private final WorldType e;
   private boolean f;
   private boolean g;
   private String h;

   public WorldSettings(long var1, WorldSettings.EnumGamemode var3, boolean var4, boolean var5, WorldType var6) {
      this.h = "";
      this.a = var1;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
   }

   public WorldSettings(WorldData var1) {
      this(var1.getSeed(), var1.getGameType(), var1.shouldGenerateMapFeatures(), var1.isHardcore(), var1.getType());
   }

   public WorldSettings a() {
      this.g = true;
      return this;
   }

   public WorldSettings setGeneratorSettings(String var1) {
      this.h = var1;
      return this;
   }

   public boolean c() {
      return this.g;
   }

   public long d() {
      return this.a;
   }

   public WorldSettings.EnumGamemode e() {
      return this.b;
   }

   public boolean f() {
      return this.d;
   }

   public boolean g() {
      return this.c;
   }

   public WorldType h() {
      return this.e;
   }

   public boolean i() {
      return this.f;
   }

   public static WorldSettings.EnumGamemode a(int var0) {
      return WorldSettings.EnumGamemode.getById(var0);
   }

   public String j() {
      return this.h;
   }

   public static enum EnumGamemode {
      NOT_SET(-1, ""),
      SURVIVAL(0, "survival"),
      CREATIVE(1, "creative"),
      ADVENTURE(2, "adventure"),
      SPECTATOR(3, "spectator");

      int f;
      String g;

      private EnumGamemode(int var3, String var4) {
         this.f = var3;
         this.g = var4;
      }

      public int getId() {
         return this.f;
      }

      public String b() {
         return this.g;
      }

      public void a(PlayerAbilities var1) {
         if(this == CREATIVE) {
            var1.canFly = true;
            var1.canInstantlyBuild = true;
            var1.isInvulnerable = true;
         } else if(this == SPECTATOR) {
            var1.canFly = true;
            var1.canInstantlyBuild = false;
            var1.isInvulnerable = true;
            var1.isFlying = true;
         } else {
            var1.canFly = false;
            var1.canInstantlyBuild = false;
            var1.isInvulnerable = false;
            var1.isFlying = false;
         }

         var1.mayBuild = !this.c();
      }

      public boolean c() {
         return this == ADVENTURE || this == SPECTATOR;
      }

      public boolean d() {
         return this == CREATIVE;
      }

      public boolean e() {
         return this == SURVIVAL || this == ADVENTURE;
      }

      public static WorldSettings.EnumGamemode getById(int var0) {
         WorldSettings.EnumGamemode[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            WorldSettings.EnumGamemode var4 = var1[var3];
            if(var4.f == var0) {
               return var4;
            }
         }

         return SURVIVAL;
      }
   }
}
