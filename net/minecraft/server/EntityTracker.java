package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.server.Chunk;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArmorStand;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityBat;
import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityEnderPearl;
import net.minecraft.server.EntityEnderSignal;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntitySquid;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EntityThrownExpBottle;
import net.minecraft.server.EntityTrackerEntry;
import net.minecraft.server.EntityWither;
import net.minecraft.server.IAnimal;
import net.minecraft.server.IntHashMap;
import net.minecraft.server.Packet;
import net.minecraft.server.ReportedException;
import net.minecraft.server.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker {
   private static final Logger a = LogManager.getLogger();
   private final WorldServer world;
   private Set<EntityTrackerEntry> c = Sets.newHashSet();
   public IntHashMap<EntityTrackerEntry> trackedEntities = new IntHashMap();
   private int e;

   public EntityTracker(WorldServer var1) {
      this.world = var1;
      this.e = var1.getMinecraftServer().getPlayerList().d();
   }

   public void track(Entity var1) {
      if(var1 instanceof EntityPlayer) {
         this.addEntity(var1, 512, 2);
         EntityPlayer var2 = (EntityPlayer)var1;
         Iterator var3 = this.c.iterator();

         while(var3.hasNext()) {
            EntityTrackerEntry var4 = (EntityTrackerEntry)var3.next();
            if(var4.tracker != var2) {
               var4.updatePlayer(var2);
            }
         }
      } else if(var1 instanceof EntityFishingHook) {
         this.addEntity(var1, 64, 5, true);
      } else if(var1 instanceof EntityArrow) {
         this.addEntity(var1, 64, 20, false);
      } else if(var1 instanceof EntitySmallFireball) {
         this.addEntity(var1, 64, 10, false);
      } else if(var1 instanceof EntityFireball) {
         this.addEntity(var1, 64, 10, false);
      } else if(var1 instanceof EntitySnowball) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityEnderPearl) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityEnderSignal) {
         this.addEntity(var1, 64, 4, true);
      } else if(var1 instanceof EntityEgg) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityPotion) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityThrownExpBottle) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityFireworks) {
         this.addEntity(var1, 64, 10, true);
      } else if(var1 instanceof EntityItem) {
         this.addEntity(var1, 64, 20, true);
      } else if(var1 instanceof EntityMinecartAbstract) {
         this.addEntity(var1, 80, 3, true);
      } else if(var1 instanceof EntityBoat) {
         this.addEntity(var1, 80, 3, true);
      } else if(var1 instanceof EntitySquid) {
         this.addEntity(var1, 64, 3, true);
      } else if(var1 instanceof EntityWither) {
         this.addEntity(var1, 80, 3, false);
      } else if(var1 instanceof EntityBat) {
         this.addEntity(var1, 80, 3, false);
      } else if(var1 instanceof EntityEnderDragon) {
         this.addEntity(var1, 160, 3, true);
      } else if(var1 instanceof IAnimal) {
         this.addEntity(var1, 80, 3, true);
      } else if(var1 instanceof EntityTNTPrimed) {
         this.addEntity(var1, 160, 10, true);
      } else if(var1 instanceof EntityFallingBlock) {
         this.addEntity(var1, 160, 20, true);
      } else if(var1 instanceof EntityHanging) {
         this.addEntity(var1, 160, Integer.MAX_VALUE, false);
      } else if(var1 instanceof EntityArmorStand) {
         this.addEntity(var1, 160, 3, true);
      } else if(var1 instanceof EntityExperienceOrb) {
         this.addEntity(var1, 160, 20, true);
      } else if(var1 instanceof EntityEnderCrystal) {
         this.addEntity(var1, 256, Integer.MAX_VALUE, false);
      }

   }

   public void addEntity(Entity var1, int var2, int var3) {
      this.addEntity(var1, var2, var3, false);
   }

   public void addEntity(Entity var1, int var2, final int var3, boolean var4) {
      if(var2 > this.e) {
         var2 = this.e;
      }

      try {
         if(this.trackedEntities.b(var1.getId())) {
            throw new IllegalStateException("Entity is already tracked!");
         }

         EntityTrackerEntry var5 = new EntityTrackerEntry(var1, var2, var3, var4);
         this.c.add(var5);
         this.trackedEntities.a(var1.getId(), var5);
         var5.scanPlayers(this.world.players);
      } catch (Throwable var11) {
         CrashReport var6 = CrashReport.a(var11, "Adding entity to track");
         CrashReportSystemDetails var7 = var6.a("Entity To Track");
         var7.a((String)"Tracking range", (Object)(var2 + " blocks"));
         var7.a("Update interval", new Callable() {
            public String a() throws Exception {
               String var1 = "Once per " + var3 + " ticks";
               if(var3 == Integer.MAX_VALUE) {
                  var1 = "Maximum (" + var1 + ")";
               }

               return var1;
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
         var1.appendEntityCrashDetails(var7);
         CrashReportSystemDetails var8 = var6.a("Entity That Is Already Tracked");
         ((EntityTrackerEntry)this.trackedEntities.get(var1.getId())).tracker.appendEntityCrashDetails(var8);

         try {
            throw new ReportedException(var6);
         } catch (ReportedException var10) {
            a.error((String)"\"Silently\" catching entity tracking error.", (Throwable)var10);
         }
      }

   }

   public void untrackEntity(Entity var1) {
      if(var1 instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)var1;
         Iterator var3 = this.c.iterator();

         while(var3.hasNext()) {
            EntityTrackerEntry var4 = (EntityTrackerEntry)var3.next();
            var4.a(var2);
         }
      }

      EntityTrackerEntry var5 = (EntityTrackerEntry)this.trackedEntities.d(var1.getId());
      if(var5 != null) {
         this.c.remove(var5);
         var5.a();
      }

   }

   public void updatePlayers() {
      ArrayList var1 = Lists.newArrayList();
      Iterator var2 = this.c.iterator();

      while(var2.hasNext()) {
         EntityTrackerEntry var3 = (EntityTrackerEntry)var2.next();
         var3.track(this.world.players);
         if(var3.n && var3.tracker instanceof EntityPlayer) {
            var1.add((EntityPlayer)var3.tracker);
         }
      }

      for(int var6 = 0; var6 < var1.size(); ++var6) {
         EntityPlayer var7 = (EntityPlayer)var1.get(var6);
         Iterator var4 = this.c.iterator();

         while(var4.hasNext()) {
            EntityTrackerEntry var5 = (EntityTrackerEntry)var4.next();
            if(var5.tracker != var7) {
               var5.updatePlayer(var7);
            }
         }
      }

   }

   public void a(EntityPlayer var1) {
      Iterator var2 = this.c.iterator();

      while(var2.hasNext()) {
         EntityTrackerEntry var3 = (EntityTrackerEntry)var2.next();
         if(var3.tracker == var1) {
            var3.scanPlayers(this.world.players);
         } else {
            var3.updatePlayer(var1);
         }
      }

   }

   public void a(Entity var1, Packet var2) {
      EntityTrackerEntry var3 = (EntityTrackerEntry)this.trackedEntities.get(var1.getId());
      if(var3 != null) {
         var3.broadcast(var2);
      }

   }

   public void sendPacketToEntity(Entity var1, Packet var2) {
      EntityTrackerEntry var3 = (EntityTrackerEntry)this.trackedEntities.get(var1.getId());
      if(var3 != null) {
         var3.broadcastIncludingSelf(var2);
      }

   }

   public void untrackPlayer(EntityPlayer var1) {
      Iterator var2 = this.c.iterator();

      while(var2.hasNext()) {
         EntityTrackerEntry var3 = (EntityTrackerEntry)var2.next();
         var3.clear(var1);
      }

   }

   public void a(EntityPlayer var1, Chunk var2) {
      Iterator var3 = this.c.iterator();

      while(var3.hasNext()) {
         EntityTrackerEntry var4 = (EntityTrackerEntry)var3.next();
         if(var4.tracker != var1 && var4.tracker.ae == var2.locX && var4.tracker.ag == var2.locZ) {
            var4.updatePlayer(var1);
         }
      }

   }
}
