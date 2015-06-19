package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.UtilColor;
import net.minecraft.server.WeightedRandom;
import net.minecraft.server.World;

public abstract class MobSpawnerAbstract {
   public int spawnDelay = 20;
   private String mobName = "Pig";
   private final List<MobSpawnerAbstract.a> mobs = Lists.newArrayList();
   private MobSpawnerAbstract.a spawnData;
   private double e;
   private double f;
   private int minSpawnDelay = 200;
   private int maxSpawnDelay = 800;
   private int spawnCount = 4;
   private Entity j;
   private int maxNearbyEntities = 6;
   private int requiredPlayerRange = 16;
   private int spawnRange = 4;

   public MobSpawnerAbstract() {
   }

   public String getMobName() {
      if(this.i() == null) {
         if(this.mobName != null && this.mobName.equals("Minecart")) {
            this.mobName = "MinecartRideable";
         }

         return this.mobName;
      } else {
         return this.i().d;
      }
   }

   public void setMobName(String var1) {
      this.mobName = var1;
   }

   private boolean g() {
      BlockPosition var1 = this.b();
      return this.a().isPlayerNearby((double)var1.getX() + 0.5D, (double)var1.getY() + 0.5D, (double)var1.getZ() + 0.5D, (double)this.requiredPlayerRange);
   }

   public void c() {
      if(this.g()) {
         BlockPosition var1 = this.b();
         double var6;
         if(this.a().isClientSide) {
            double var13 = (double)((float)var1.getX() + this.a().random.nextFloat());
            double var14 = (double)((float)var1.getY() + this.a().random.nextFloat());
            var6 = (double)((float)var1.getZ() + this.a().random.nextFloat());
            this.a().addParticle(EnumParticle.SMOKE_NORMAL, var13, var14, var6, 0.0D, 0.0D, 0.0D, new int[0]);
            this.a().addParticle(EnumParticle.FLAME, var13, var14, var6, 0.0D, 0.0D, 0.0D, new int[0]);
            if(this.spawnDelay > 0) {
               --this.spawnDelay;
            }

            this.f = this.e;
            this.e = (this.e + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
         } else {
            if(this.spawnDelay == -1) {
               this.h();
            }

            if(this.spawnDelay > 0) {
               --this.spawnDelay;
               return;
            }

            boolean var2 = false;
            int var3 = 0;

            while(true) {
               if(var3 >= this.spawnCount) {
                  if(var2) {
                     this.h();
                  }
                  break;
               }

               Entity var4 = EntityTypes.createEntityByName(this.getMobName(), this.a());
               if(var4 == null) {
                  return;
               }

               int var5 = this.a().a(var4.getClass(), (new AxisAlignedBB((double)var1.getX(), (double)var1.getY(), (double)var1.getZ(), (double)(var1.getX() + 1), (double)(var1.getY() + 1), (double)(var1.getZ() + 1))).grow((double)this.spawnRange, (double)this.spawnRange, (double)this.spawnRange)).size();
               if(var5 >= this.maxNearbyEntities) {
                  this.h();
                  return;
               }

               var6 = (double)var1.getX() + (this.a().random.nextDouble() - this.a().random.nextDouble()) * (double)this.spawnRange + 0.5D;
               double var8 = (double)(var1.getY() + this.a().random.nextInt(3) - 1);
               double var10 = (double)var1.getZ() + (this.a().random.nextDouble() - this.a().random.nextDouble()) * (double)this.spawnRange + 0.5D;
               EntityInsentient var12 = var4 instanceof EntityInsentient?(EntityInsentient)var4:null;
               var4.setPositionRotation(var6, var8, var10, this.a().random.nextFloat() * 360.0F, 0.0F);
               if(var12 == null || var12.bR() && var12.canSpawn()) {
                  this.a(var4, true);
                  this.a().triggerEffect(2004, var1, 0);
                  if(var12 != null) {
                     var12.y();
                  }

                  var2 = true;
               }

               ++var3;
            }
         }

      }
   }

   private Entity a(Entity var1, boolean var2) {
      if(this.i() != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         var1.d(var3);
         Iterator var4 = this.i().c.c().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            NBTBase var6 = this.i().c.get(var5);
            var3.set(var5, var6.clone());
         }

         var1.f(var3);
         if(var1.world != null && var2) {
            var1.world.addEntity(var1);
         }

         NBTTagCompound var12;
         for(Entity var11 = var1; var3.hasKeyOfType("Riding", 10); var3 = var12) {
            var12 = var3.getCompound("Riding");
            Entity var13 = EntityTypes.createEntityByName(var12.getString("id"), var1.world);
            if(var13 != null) {
               NBTTagCompound var7 = new NBTTagCompound();
               var13.d(var7);
               Iterator var8 = var12.c().iterator();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  NBTBase var10 = var12.get(var9);
                  var7.set(var9, var10.clone());
               }

               var13.f(var7);
               var13.setPositionRotation(var11.locX, var11.locY, var11.locZ, var11.yaw, var11.pitch);
               if(var1.world != null && var2) {
                  var1.world.addEntity(var13);
               }

               var11.mount(var13);
            }

            var11 = var13;
         }
      } else if(var1 instanceof EntityLiving && var1.world != null && var2) {
         if(var1 instanceof EntityInsentient) {
            ((EntityInsentient)var1).prepare(var1.world.E(new BlockPosition(var1)), (GroupDataEntity)null);
         }

         var1.world.addEntity(var1);
      }

      return var1;
   }

   private void h() {
      if(this.maxSpawnDelay <= this.minSpawnDelay) {
         this.spawnDelay = this.minSpawnDelay;
      } else {
         int var10003 = this.maxSpawnDelay - this.minSpawnDelay;
         this.spawnDelay = this.minSpawnDelay + this.a().random.nextInt(var10003);
      }

      if(this.mobs.size() > 0) {
         this.a((MobSpawnerAbstract.a)WeightedRandom.a(this.a().random, this.mobs));
      }

      this.a(1);
   }

   public void a(NBTTagCompound var1) {
      this.mobName = var1.getString("EntityId");
      this.spawnDelay = var1.getShort("Delay");
      this.mobs.clear();
      if(var1.hasKeyOfType("SpawnPotentials", 9)) {
         NBTTagList var2 = var1.getList("SpawnPotentials", 10);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            this.mobs.add(new MobSpawnerAbstract.a(var2.get(var3)));
         }
      }

      if(var1.hasKeyOfType("SpawnData", 10)) {
         this.a(new MobSpawnerAbstract.a(var1.getCompound("SpawnData"), this.mobName));
      } else {
         this.a((MobSpawnerAbstract.a)null);
      }

      if(var1.hasKeyOfType("MinSpawnDelay", 99)) {
         this.minSpawnDelay = var1.getShort("MinSpawnDelay");
         this.maxSpawnDelay = var1.getShort("MaxSpawnDelay");
         this.spawnCount = var1.getShort("SpawnCount");
      }

      if(var1.hasKeyOfType("MaxNearbyEntities", 99)) {
         this.maxNearbyEntities = var1.getShort("MaxNearbyEntities");
         this.requiredPlayerRange = var1.getShort("RequiredPlayerRange");
      }

      if(var1.hasKeyOfType("SpawnRange", 99)) {
         this.spawnRange = var1.getShort("SpawnRange");
      }

      if(this.a() != null) {
         this.j = null;
      }

   }

   public void b(NBTTagCompound var1) {
      String var2 = this.getMobName();
      if(!UtilColor.b(var2)) {
         var1.setString("EntityId", var2);
         var1.setShort("Delay", (short)this.spawnDelay);
         var1.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
         var1.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
         var1.setShort("SpawnCount", (short)this.spawnCount);
         var1.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
         var1.setShort("RequiredPlayerRange", (short)this.requiredPlayerRange);
         var1.setShort("SpawnRange", (short)this.spawnRange);
         if(this.i() != null) {
            var1.set("SpawnData", this.i().c.clone());
         }

         if(this.i() != null || this.mobs.size() > 0) {
            NBTTagList var3 = new NBTTagList();
            if(this.mobs.size() > 0) {
               Iterator var4 = this.mobs.iterator();

               while(var4.hasNext()) {
                  MobSpawnerAbstract.a var5 = (MobSpawnerAbstract.a)var4.next();
                  var3.add(var5.a());
               }
            } else {
               var3.add(this.i().a());
            }

            var1.set("SpawnPotentials", var3);
         }

      }
   }

   public boolean b(int var1) {
      if(var1 == 1 && this.a().isClientSide) {
         this.spawnDelay = this.minSpawnDelay;
         return true;
      } else {
         return false;
      }
   }

   private MobSpawnerAbstract.a i() {
      return this.spawnData;
   }

   public void a(MobSpawnerAbstract.a var1) {
      this.spawnData = var1;
   }

   public abstract void a(int var1);

   public abstract World a();

   public abstract BlockPosition b();

   public class a extends WeightedRandom.WeightedRandomChoice {
      private final NBTTagCompound c;
      private final String d;

      public a(NBTTagCompound var2) {
         this();
      }

      public a(NBTTagCompound var2, String var3) {
         this();
      }

      private a(NBTTagCompound var2, String var3, int var4) {
         super(var4);
         if(var3.equals("Minecart")) {
            if(var2 != null) {
               var3 = EntityMinecartAbstract.EnumMinecartType.a(var2.getInt("Type")).b();
            } else {
               var3 = "MinecartRideable";
            }
         }

         this.c = var2;
         this.d = var3;
      }

      public NBTTagCompound a() {
         NBTTagCompound var1 = new NBTTagCompound();
         var1.set("Properties", this.c);
         var1.setString("Type", this.d);
         var1.setInt("Weight", this.a);
         return var1;
      }
   }
}
