package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Navigation;
import net.minecraft.server.NavigationAbstract;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class PathfinderGoalFollowOwner extends PathfinderGoal {
   private EntityTameableAnimal d;
   private EntityLiving e;
   World a;
   private double f;
   private NavigationAbstract g;
   private int h;
   float b;
   float c;
   private boolean i;

   public PathfinderGoalFollowOwner(EntityTameableAnimal var1, double var2, float var4, float var5) {
      this.d = var1;
      this.a = var1.world;
      this.f = var2;
      this.g = var1.getNavigation();
      this.c = var4;
      this.b = var5;
      this.a(3);
      if(!(var1.getNavigation() instanceof Navigation)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
      }
   }

   public boolean a() {
      EntityLiving var1 = this.d.getOwner();
      if(var1 == null) {
         return false;
      } else if(var1 instanceof EntityHuman && ((EntityHuman)var1).isSpectator()) {
         return false;
      } else if(this.d.isSitting()) {
         return false;
      } else if(this.d.h(var1) < (double)(this.c * this.c)) {
         return false;
      } else {
         this.e = var1;
         return true;
      }
   }

   public boolean b() {
      return !this.g.m() && this.d.h(this.e) > (double)(this.b * this.b) && !this.d.isSitting();
   }

   public void c() {
      this.h = 0;
      this.i = ((Navigation)this.d.getNavigation()).e();
      ((Navigation)this.d.getNavigation()).a(false);
   }

   public void d() {
      this.e = null;
      this.g.n();
      ((Navigation)this.d.getNavigation()).a(true);
   }

   private boolean a(BlockPosition var1) {
      IBlockData var2 = this.a.getType(var1);
      Block var3 = var2.getBlock();
      return var3 == Blocks.AIR?true:!var3.d();
   }

   public void e() {
      this.d.getControllerLook().a(this.e, 10.0F, (float)this.d.bQ());
      if(!this.d.isSitting()) {
         if(--this.h <= 0) {
            this.h = 10;
            if(!this.g.a((Entity)this.e, this.f)) {
               if(!this.d.cc()) {
                  if(this.d.h(this.e) >= 144.0D) {
                     int var1 = MathHelper.floor(this.e.locX) - 2;
                     int var2 = MathHelper.floor(this.e.locZ) - 2;
                     int var3 = MathHelper.floor(this.e.getBoundingBox().b);

                     for(int var4 = 0; var4 <= 4; ++var4) {
                        for(int var5 = 0; var5 <= 4; ++var5) {
                           if((var4 < 1 || var5 < 1 || var4 > 3 || var5 > 3) && World.a((IBlockAccess)this.a, (BlockPosition)(new BlockPosition(var1 + var4, var3 - 1, var2 + var5))) && this.a(new BlockPosition(var1 + var4, var3, var2 + var5)) && this.a(new BlockPosition(var1 + var4, var3 + 1, var2 + var5))) {
                              this.d.setPositionRotation((double)((float)(var1 + var4) + 0.5F), (double)var3, (double)((float)(var2 + var5) + 0.5F), this.d.yaw, this.d.pitch);
                              this.g.n();
                              return;
                           }
                        }
                     }

                  }
               }
            }
         }
      }
   }
}
