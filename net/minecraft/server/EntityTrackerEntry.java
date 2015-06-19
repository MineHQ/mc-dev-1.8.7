package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.AttributeMapServer;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityEnderPearl;
import net.minecraft.server.EntityEnderSignal;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.EntityLeash;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityPainting;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EntityThrownExpBottle;
import net.minecraft.server.EntityWitherSkull;
import net.minecraft.server.IAnimal;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWorldMap;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffect;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutAttachEntity;
import net.minecraft.server.PacketPlayOutBed;
import net.minecraft.server.PacketPlayOutEntity;
import net.minecraft.server.PacketPlayOutEntityEffect;
import net.minecraft.server.PacketPlayOutEntityEquipment;
import net.minecraft.server.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.PacketPlayOutEntityMetadata;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import net.minecraft.server.PacketPlayOutEntityVelocity;
import net.minecraft.server.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.PacketPlayOutSpawnEntity;
import net.minecraft.server.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.PacketPlayOutSpawnEntityPainting;
import net.minecraft.server.PacketPlayOutUpdateAttributes;
import net.minecraft.server.PacketPlayOutUpdateEntityNBT;
import net.minecraft.server.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry {
   private static final Logger p = LogManager.getLogger();
   public Entity tracker;
   public int b;
   public int c;
   public int xLoc;
   public int yLoc;
   public int zLoc;
   public int yRot;
   public int xRot;
   public int i;
   public double j;
   public double k;
   public double l;
   public int m;
   private double q;
   private double r;
   private double s;
   private boolean isMoving;
   private boolean u;
   private int v;
   private Entity w;
   private boolean x;
   private boolean y;
   public boolean n;
   public Set<EntityPlayer> trackedPlayers = Sets.newHashSet();

   public EntityTrackerEntry(Entity var1, int var2, int var3, boolean var4) {
      this.tracker = var1;
      this.b = var2;
      this.c = var3;
      this.u = var4;
      this.xLoc = MathHelper.floor(var1.locX * 32.0D);
      this.yLoc = MathHelper.floor(var1.locY * 32.0D);
      this.zLoc = MathHelper.floor(var1.locZ * 32.0D);
      this.yRot = MathHelper.d(var1.yaw * 256.0F / 360.0F);
      this.xRot = MathHelper.d(var1.pitch * 256.0F / 360.0F);
      this.i = MathHelper.d(var1.getHeadRotation() * 256.0F / 360.0F);
      this.y = var1.onGround;
   }

   public boolean equals(Object var1) {
      return var1 instanceof EntityTrackerEntry?((EntityTrackerEntry)var1).tracker.getId() == this.tracker.getId():false;
   }

   public int hashCode() {
      return this.tracker.getId();
   }

   public void track(List<EntityHuman> var1) {
      this.n = false;
      if(!this.isMoving || this.tracker.e(this.q, this.r, this.s) > 16.0D) {
         this.q = this.tracker.locX;
         this.r = this.tracker.locY;
         this.s = this.tracker.locZ;
         this.isMoving = true;
         this.n = true;
         this.scanPlayers(var1);
      }

      if(this.w != this.tracker.vehicle || this.tracker.vehicle != null && this.m % 60 == 0) {
         this.w = this.tracker.vehicle;
         this.broadcast(new PacketPlayOutAttachEntity(0, this.tracker, this.tracker.vehicle));
      }

      if(this.tracker instanceof EntityItemFrame && this.m % 10 == 0) {
         EntityItemFrame var2 = (EntityItemFrame)this.tracker;
         ItemStack var3 = var2.getItem();
         if(var3 != null && var3.getItem() instanceof ItemWorldMap) {
            WorldMap var4 = Items.FILLED_MAP.getSavedMap(var3, this.tracker.world);
            Iterator var5 = var1.iterator();

            while(var5.hasNext()) {
               EntityHuman var6 = (EntityHuman)var5.next();
               EntityPlayer var7 = (EntityPlayer)var6;
               var4.a(var7, var3);
               Packet var8 = Items.FILLED_MAP.c(var3, this.tracker.world, var7);
               if(var8 != null) {
                  var7.playerConnection.sendPacket(var8);
               }
            }
         }

         this.b();
      }

      if(this.m % this.c == 0 || this.tracker.ai || this.tracker.getDataWatcher().a()) {
         int var23;
         int var24;
         if(this.tracker.vehicle == null) {
            ++this.v;
            var23 = MathHelper.floor(this.tracker.locX * 32.0D);
            var24 = MathHelper.floor(this.tracker.locY * 32.0D);
            int var26 = MathHelper.floor(this.tracker.locZ * 32.0D);
            int var27 = MathHelper.d(this.tracker.yaw * 256.0F / 360.0F);
            int var28 = MathHelper.d(this.tracker.pitch * 256.0F / 360.0F);
            int var29 = var23 - this.xLoc;
            int var30 = var24 - this.yLoc;
            int var9 = var26 - this.zLoc;
            Object var10 = null;
            boolean var11 = Math.abs(var29) >= 4 || Math.abs(var30) >= 4 || Math.abs(var9) >= 4 || this.m % 60 == 0;
            boolean var12 = Math.abs(var27 - this.yRot) >= 4 || Math.abs(var28 - this.xRot) >= 4;
            if(this.m > 0 || this.tracker instanceof EntityArrow) {
               if(var29 >= -128 && var29 < 128 && var30 >= -128 && var30 < 128 && var9 >= -128 && var9 < 128 && this.v <= 400 && !this.x && this.y == this.tracker.onGround) {
                  if((!var11 || !var12) && !(this.tracker instanceof EntityArrow)) {
                     if(var11) {
                        var10 = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(this.tracker.getId(), (byte)var29, (byte)var30, (byte)var9, this.tracker.onGround);
                     } else if(var12) {
                        var10 = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.tracker.getId(), (byte)var27, (byte)var28, this.tracker.onGround);
                     }
                  } else {
                     var10 = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.tracker.getId(), (byte)var29, (byte)var30, (byte)var9, (byte)var27, (byte)var28, this.tracker.onGround);
                  }
               } else {
                  this.y = this.tracker.onGround;
                  this.v = 0;
                  var10 = new PacketPlayOutEntityTeleport(this.tracker.getId(), var23, var24, var26, (byte)var27, (byte)var28, this.tracker.onGround);
               }
            }

            if(this.u) {
               double var13 = this.tracker.motX - this.j;
               double var15 = this.tracker.motY - this.k;
               double var17 = this.tracker.motZ - this.l;
               double var19 = 0.02D;
               double var21 = var13 * var13 + var15 * var15 + var17 * var17;
               if(var21 > var19 * var19 || var21 > 0.0D && this.tracker.motX == 0.0D && this.tracker.motY == 0.0D && this.tracker.motZ == 0.0D) {
                  this.j = this.tracker.motX;
                  this.k = this.tracker.motY;
                  this.l = this.tracker.motZ;
                  this.broadcast(new PacketPlayOutEntityVelocity(this.tracker.getId(), this.j, this.k, this.l));
               }
            }

            if(var10 != null) {
               this.broadcast((Packet)var10);
            }

            this.b();
            if(var11) {
               this.xLoc = var23;
               this.yLoc = var24;
               this.zLoc = var26;
            }

            if(var12) {
               this.yRot = var27;
               this.xRot = var28;
            }

            this.x = false;
         } else {
            var23 = MathHelper.d(this.tracker.yaw * 256.0F / 360.0F);
            var24 = MathHelper.d(this.tracker.pitch * 256.0F / 360.0F);
            boolean var25 = Math.abs(var23 - this.yRot) >= 4 || Math.abs(var24 - this.xRot) >= 4;
            if(var25) {
               this.broadcast(new PacketPlayOutEntity.PacketPlayOutEntityLook(this.tracker.getId(), (byte)var23, (byte)var24, this.tracker.onGround));
               this.yRot = var23;
               this.xRot = var24;
            }

            this.xLoc = MathHelper.floor(this.tracker.locX * 32.0D);
            this.yLoc = MathHelper.floor(this.tracker.locY * 32.0D);
            this.zLoc = MathHelper.floor(this.tracker.locZ * 32.0D);
            this.b();
            this.x = true;
         }

         var23 = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
         if(Math.abs(var23 - this.i) >= 4) {
            this.broadcast(new PacketPlayOutEntityHeadRotation(this.tracker, (byte)var23));
            this.i = var23;
         }

         this.tracker.ai = false;
      }

      ++this.m;
      if(this.tracker.velocityChanged) {
         this.broadcastIncludingSelf(new PacketPlayOutEntityVelocity(this.tracker));
         this.tracker.velocityChanged = false;
      }

   }

   private void b() {
      DataWatcher var1 = this.tracker.getDataWatcher();
      if(var1.a()) {
         this.broadcastIncludingSelf(new PacketPlayOutEntityMetadata(this.tracker.getId(), var1, false));
      }

      if(this.tracker instanceof EntityLiving) {
         AttributeMapServer var2 = (AttributeMapServer)((EntityLiving)this.tracker).getAttributeMap();
         Set var3 = var2.getAttributes();
         if(!var3.isEmpty()) {
            this.broadcastIncludingSelf(new PacketPlayOutUpdateAttributes(this.tracker.getId(), var3));
         }

         var3.clear();
      }

   }

   public void broadcast(Packet var1) {
      Iterator var2 = this.trackedPlayers.iterator();

      while(var2.hasNext()) {
         EntityPlayer var3 = (EntityPlayer)var2.next();
         var3.playerConnection.sendPacket(var1);
      }

   }

   public void broadcastIncludingSelf(Packet var1) {
      this.broadcast(var1);
      if(this.tracker instanceof EntityPlayer) {
         ((EntityPlayer)this.tracker).playerConnection.sendPacket(var1);
      }

   }

   public void a() {
      Iterator var1 = this.trackedPlayers.iterator();

      while(var1.hasNext()) {
         EntityPlayer var2 = (EntityPlayer)var1.next();
         var2.d(this.tracker);
      }

   }

   public void a(EntityPlayer var1) {
      if(this.trackedPlayers.contains(var1)) {
         var1.d(this.tracker);
         this.trackedPlayers.remove(var1);
      }

   }

   public void updatePlayer(EntityPlayer var1) {
      if(var1 != this.tracker) {
         if(this.c(var1)) {
            if(!this.trackedPlayers.contains(var1) && (this.e(var1) || this.tracker.attachedToPlayer)) {
               this.trackedPlayers.add(var1);
               Packet var2 = this.c();
               var1.playerConnection.sendPacket(var2);
               if(!this.tracker.getDataWatcher().d()) {
                  var1.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(this.tracker.getId(), this.tracker.getDataWatcher(), true));
               }

               NBTTagCompound var3 = this.tracker.getNBTTag();
               if(var3 != null) {
                  var1.playerConnection.sendPacket(new PacketPlayOutUpdateEntityNBT(this.tracker.getId(), var3));
               }

               if(this.tracker instanceof EntityLiving) {
                  AttributeMapServer var4 = (AttributeMapServer)((EntityLiving)this.tracker).getAttributeMap();
                  Collection var5 = var4.c();
                  if(!var5.isEmpty()) {
                     var1.playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(this.tracker.getId(), var5));
                  }
               }

               this.j = this.tracker.motX;
               this.k = this.tracker.motY;
               this.l = this.tracker.motZ;
               if(this.u && !(var2 instanceof PacketPlayOutSpawnEntityLiving)) {
                  var1.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(this.tracker.getId(), this.tracker.motX, this.tracker.motY, this.tracker.motZ));
               }

               if(this.tracker.vehicle != null) {
                  var1.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this.tracker, this.tracker.vehicle));
               }

               if(this.tracker instanceof EntityInsentient && ((EntityInsentient)this.tracker).getLeashHolder() != null) {
                  var1.playerConnection.sendPacket(new PacketPlayOutAttachEntity(1, this.tracker, ((EntityInsentient)this.tracker).getLeashHolder()));
               }

               if(this.tracker instanceof EntityLiving) {
                  for(int var7 = 0; var7 < 5; ++var7) {
                     ItemStack var9 = ((EntityLiving)this.tracker).getEquipment(var7);
                     if(var9 != null) {
                        var1.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(this.tracker.getId(), var7, var9));
                     }
                  }
               }

               if(this.tracker instanceof EntityHuman) {
                  EntityHuman var8 = (EntityHuman)this.tracker;
                  if(var8.isSleeping()) {
                     var1.playerConnection.sendPacket(new PacketPlayOutBed(var8, new BlockPosition(this.tracker)));
                  }
               }

               if(this.tracker instanceof EntityLiving) {
                  EntityLiving var10 = (EntityLiving)this.tracker;
                  Iterator var11 = var10.getEffects().iterator();

                  while(var11.hasNext()) {
                     MobEffect var6 = (MobEffect)var11.next();
                     var1.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.tracker.getId(), var6));
                  }
               }
            }
         } else if(this.trackedPlayers.contains(var1)) {
            this.trackedPlayers.remove(var1);
            var1.d(this.tracker);
         }

      }
   }

   public boolean c(EntityPlayer var1) {
      double var2 = var1.locX - (double)(this.xLoc / 32);
      double var4 = var1.locZ - (double)(this.zLoc / 32);
      return var2 >= (double)(-this.b) && var2 <= (double)this.b && var4 >= (double)(-this.b) && var4 <= (double)this.b && this.tracker.a(var1);
   }

   private boolean e(EntityPlayer var1) {
      return var1.u().getPlayerChunkMap().a(var1, this.tracker.ae, this.tracker.ag);
   }

   public void scanPlayers(List<EntityHuman> var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         this.updatePlayer((EntityPlayer)var1.get(var2));
      }

   }

   private Packet c() {
      if(this.tracker.dead) {
         p.warn("Fetching addPacket for removed entity");
      }

      if(this.tracker instanceof EntityItem) {
         return new PacketPlayOutSpawnEntity(this.tracker, 2, 1);
      } else if(this.tracker instanceof EntityPlayer) {
         return new PacketPlayOutNamedEntitySpawn((EntityHuman)this.tracker);
      } else if(this.tracker instanceof EntityMinecartAbstract) {
         EntityMinecartAbstract var9 = (EntityMinecartAbstract)this.tracker;
         return new PacketPlayOutSpawnEntity(this.tracker, 10, var9.s().a());
      } else if(this.tracker instanceof EntityBoat) {
         return new PacketPlayOutSpawnEntity(this.tracker, 1);
      } else if(this.tracker instanceof IAnimal) {
         this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
         return new PacketPlayOutSpawnEntityLiving((EntityLiving)this.tracker);
      } else if(this.tracker instanceof EntityFishingHook) {
         EntityHuman var8 = ((EntityFishingHook)this.tracker).owner;
         return new PacketPlayOutSpawnEntity(this.tracker, 90, var8 != null?var8.getId():this.tracker.getId());
      } else if(this.tracker instanceof EntityArrow) {
         Entity var7 = ((EntityArrow)this.tracker).shooter;
         return new PacketPlayOutSpawnEntity(this.tracker, 60, var7 != null?var7.getId():this.tracker.getId());
      } else if(this.tracker instanceof EntitySnowball) {
         return new PacketPlayOutSpawnEntity(this.tracker, 61);
      } else if(this.tracker instanceof EntityPotion) {
         return new PacketPlayOutSpawnEntity(this.tracker, 73, ((EntityPotion)this.tracker).getPotionValue());
      } else if(this.tracker instanceof EntityThrownExpBottle) {
         return new PacketPlayOutSpawnEntity(this.tracker, 75);
      } else if(this.tracker instanceof EntityEnderPearl) {
         return new PacketPlayOutSpawnEntity(this.tracker, 65);
      } else if(this.tracker instanceof EntityEnderSignal) {
         return new PacketPlayOutSpawnEntity(this.tracker, 72);
      } else if(this.tracker instanceof EntityFireworks) {
         return new PacketPlayOutSpawnEntity(this.tracker, 76);
      } else {
         PacketPlayOutSpawnEntity var2;
         if(this.tracker instanceof EntityFireball) {
            EntityFireball var6 = (EntityFireball)this.tracker;
            var2 = null;
            byte var10 = 63;
            if(this.tracker instanceof EntitySmallFireball) {
               var10 = 64;
            } else if(this.tracker instanceof EntityWitherSkull) {
               var10 = 66;
            }

            if(var6.shooter != null) {
               var2 = new PacketPlayOutSpawnEntity(this.tracker, var10, ((EntityFireball)this.tracker).shooter.getId());
            } else {
               var2 = new PacketPlayOutSpawnEntity(this.tracker, var10, 0);
            }

            var2.d((int)(var6.dirX * 8000.0D));
            var2.e((int)(var6.dirY * 8000.0D));
            var2.f((int)(var6.dirZ * 8000.0D));
            return var2;
         } else if(this.tracker instanceof EntityEgg) {
            return new PacketPlayOutSpawnEntity(this.tracker, 62);
         } else if(this.tracker instanceof EntityTNTPrimed) {
            return new PacketPlayOutSpawnEntity(this.tracker, 50);
         } else if(this.tracker instanceof EntityEnderCrystal) {
            return new PacketPlayOutSpawnEntity(this.tracker, 51);
         } else if(this.tracker instanceof EntityFallingBlock) {
            EntityFallingBlock var5 = (EntityFallingBlock)this.tracker;
            return new PacketPlayOutSpawnEntity(this.tracker, 70, Block.getCombinedId(var5.getBlock()));
         } else if(this.tracker instanceof EntityArmorStand) {
            return new PacketPlayOutSpawnEntity(this.tracker, 78);
         } else if(this.tracker instanceof EntityPainting) {
            return new PacketPlayOutSpawnEntityPainting((EntityPainting)this.tracker);
         } else {
            BlockPosition var3;
            if(this.tracker instanceof EntityItemFrame) {
               EntityItemFrame var4 = (EntityItemFrame)this.tracker;
               var2 = new PacketPlayOutSpawnEntity(this.tracker, 71, var4.direction.b());
               var3 = var4.getBlockPosition();
               var2.a(MathHelper.d((float)(var3.getX() * 32)));
               var2.b(MathHelper.d((float)(var3.getY() * 32)));
               var2.c(MathHelper.d((float)(var3.getZ() * 32)));
               return var2;
            } else if(this.tracker instanceof EntityLeash) {
               EntityLeash var1 = (EntityLeash)this.tracker;
               var2 = new PacketPlayOutSpawnEntity(this.tracker, 77);
               var3 = var1.getBlockPosition();
               var2.a(MathHelper.d((float)(var3.getX() * 32)));
               var2.b(MathHelper.d((float)(var3.getY() * 32)));
               var2.c(MathHelper.d((float)(var3.getZ() * 32)));
               return var2;
            } else if(this.tracker instanceof EntityExperienceOrb) {
               return new PacketPlayOutSpawnEntityExperienceOrb((EntityExperienceOrb)this.tracker);
            } else {
               throw new IllegalArgumentException("Don\'t know how to add " + this.tracker.getClass() + "!");
            }
         }
      }
   }

   public void clear(EntityPlayer var1) {
      if(this.trackedPlayers.contains(var1)) {
         this.trackedPlayers.remove(var1);
         var1.d(this.tracker);
      }

   }
}
