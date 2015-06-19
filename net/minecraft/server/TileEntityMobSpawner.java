package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.MobSpawnerAbstract;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class TileEntityMobSpawner extends TileEntity implements IUpdatePlayerListBox {
   private final MobSpawnerAbstract a = new MobSpawnerAbstract() {
      public void a(int var1) {
         TileEntityMobSpawner.this.world.playBlockAction(TileEntityMobSpawner.this.position, Blocks.MOB_SPAWNER, var1, 0);
      }

      public World a() {
         return TileEntityMobSpawner.this.world;
      }

      public BlockPosition b() {
         return TileEntityMobSpawner.this.position;
      }

      public void a(MobSpawnerAbstract.a var1) {
         super.a(var1);
         if(this.a() != null) {
            this.a().notify(TileEntityMobSpawner.this.position);
         }

      }
   };

   public TileEntityMobSpawner() {
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a.a(var1);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      this.a.b(var1);
   }

   public void c() {
      this.a.c();
   }

   public Packet getUpdatePacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.b(var1);
      var1.remove("SpawnPotentials");
      return new PacketPlayOutTileEntityData(this.position, 1, var1);
   }

   public boolean c(int var1, int var2) {
      return this.a.b(var1)?true:super.c(var1, var2);
   }

   public boolean F() {
      return true;
   }

   public MobSpawnerAbstract getSpawner() {
      return this.a;
   }
}
