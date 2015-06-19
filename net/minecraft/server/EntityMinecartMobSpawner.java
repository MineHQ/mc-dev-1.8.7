package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.IBlockData;
import net.minecraft.server.MobSpawnerAbstract;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityMinecartMobSpawner extends EntityMinecartAbstract {
   private final MobSpawnerAbstract a = new MobSpawnerAbstract() {
      public void a(int var1) {
         EntityMinecartMobSpawner.this.world.broadcastEntityEffect(EntityMinecartMobSpawner.this, (byte)var1);
      }

      public World a() {
         return EntityMinecartMobSpawner.this.world;
      }

      public BlockPosition b() {
         return new BlockPosition(EntityMinecartMobSpawner.this);
      }
   };

   public EntityMinecartMobSpawner(World var1) {
      super(var1);
   }

   public EntityMinecartMobSpawner(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public EntityMinecartAbstract.EnumMinecartType s() {
      return EntityMinecartAbstract.EnumMinecartType.SPAWNER;
   }

   public IBlockData u() {
      return Blocks.MOB_SPAWNER.getBlockData();
   }

   protected void a(NBTTagCompound var1) {
      super.a(var1);
      this.a.a(var1);
   }

   protected void b(NBTTagCompound var1) {
      super.b(var1);
      this.a.b(var1);
   }

   public void t_() {
      super.t_();
      this.a.c();
   }
}
