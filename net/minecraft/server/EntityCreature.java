package net.minecraft.server;

import java.util.UUID;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.World;

public abstract class EntityCreature extends EntityInsentient {
   public static final UUID bk = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
   public static final AttributeModifier bl;
   private BlockPosition a;
   private float b;
   private PathfinderGoal c;
   private boolean bm;

   public EntityCreature(World var1) {
      super(var1);
      this.a = BlockPosition.ZERO;
      this.b = -1.0F;
      this.c = new PathfinderGoalMoveTowardsRestriction(this, 1.0D);
   }

   public float a(BlockPosition var1) {
      return 0.0F;
   }

   public boolean bR() {
      return super.bR() && this.a(new BlockPosition(this.locX, this.getBoundingBox().b, this.locZ)) >= 0.0F;
   }

   public boolean cf() {
      return !this.navigation.m();
   }

   public boolean cg() {
      return this.e(new BlockPosition(this));
   }

   public boolean e(BlockPosition var1) {
      return this.b == -1.0F?true:this.a.i(var1) < (double)(this.b * this.b);
   }

   public void a(BlockPosition var1, int var2) {
      this.a = var1;
      this.b = (float)var2;
   }

   public BlockPosition ch() {
      return this.a;
   }

   public float ci() {
      return this.b;
   }

   public void cj() {
      this.b = -1.0F;
   }

   public boolean ck() {
      return this.b != -1.0F;
   }

   protected void ca() {
      super.ca();
      if(this.cc() && this.getLeashHolder() != null && this.getLeashHolder().world == this.world) {
         Entity var1 = this.getLeashHolder();
         this.a(new BlockPosition((int)var1.locX, (int)var1.locY, (int)var1.locZ), 5);
         float var2 = this.g(var1);
         if(this instanceof EntityTameableAnimal && ((EntityTameableAnimal)this).isSitting()) {
            if(var2 > 10.0F) {
               this.unleash(true, true);
            }

            return;
         }

         if(!this.bm) {
            this.goalSelector.a(2, this.c);
            if(this.getNavigation() instanceof Navigation) {
               ((Navigation)this.getNavigation()).a(false);
            }

            this.bm = true;
         }

         this.o(var2);
         if(var2 > 4.0F) {
            this.getNavigation().a(var1, 1.0D);
         }

         if(var2 > 6.0F) {
            double var3 = (var1.locX - this.locX) / (double)var2;
            double var5 = (var1.locY - this.locY) / (double)var2;
            double var7 = (var1.locZ - this.locZ) / (double)var2;
            this.motX += var3 * Math.abs(var3) * 0.4D;
            this.motY += var5 * Math.abs(var5) * 0.4D;
            this.motZ += var7 * Math.abs(var7) * 0.4D;
         }

         if(var2 > 10.0F) {
            this.unleash(true, true);
         }
      } else if(!this.cc() && this.bm) {
         this.bm = false;
         this.goalSelector.a(this.c);
         if(this.getNavigation() instanceof Navigation) {
            ((Navigation)this.getNavigation()).a(true);
         }

         this.cj();
      }

   }

   protected void o(float var1) {
   }

   static {
      bl = (new AttributeModifier(bk, "Fleeing speed bonus", 2.0D, 2)).a(false);
   }
}
