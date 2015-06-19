package net.minecraft.server;

import net.minecraft.server.EntityAmbient;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityWaterAnimal;
import net.minecraft.server.IAnimal;
import net.minecraft.server.IMonster;
import net.minecraft.server.Material;

public enum EnumCreatureType {
   MONSTER,
   CREATURE,
   AMBIENT,
   WATER_CREATURE;

   private final Class<? extends IAnimal> e;
   private final int f;
   private final Material g;
   private final boolean h;
   private final boolean i;

   private EnumCreatureType(Class<? extends IAnimal> var3, int var4, Material var5, boolean var6, boolean var7) {
      this.e = var3;
      this.f = var4;
      this.g = var5;
      this.h = var6;
      this.i = var7;
   }

   public Class<? extends IAnimal> a() {
      return this.e;
   }

   public int b() {
      return this.f;
   }

   public boolean d() {
      return this.h;
   }

   public boolean e() {
      return this.i;
   }

   static {
      MONSTER = new EnumCreatureType("MONSTER", 0, IMonster.class, 70, Material.AIR, false, false);
      CREATURE = new EnumCreatureType("CREATURE", 1, EntityAnimal.class, 10, Material.AIR, true, true);
      AMBIENT = new EnumCreatureType("AMBIENT", 2, EntityAmbient.class, 15, Material.AIR, true, false);
      WATER_CREATURE = new EnumCreatureType("WATER_CREATURE", 3, EntityWaterAnimal.class, 5, Material.WATER, true, false);
   }
}
