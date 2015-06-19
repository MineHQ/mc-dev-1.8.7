package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class PathfinderGoalBeg extends PathfinderGoal {
   private EntityWolf a;
   private EntityHuman b;
   private World c;
   private float d;
   private int e;

   public PathfinderGoalBeg(EntityWolf var1, float var2) {
      this.a = var1;
      this.c = var1.world;
      this.d = var2;
      this.a(2);
   }

   public boolean a() {
      this.b = this.c.findNearbyPlayer(this.a, (double)this.d);
      return this.b == null?false:this.a(this.b);
   }

   public boolean b() {
      return !this.b.isAlive()?false:(this.a.h(this.b) > (double)(this.d * this.d)?false:this.e > 0 && this.a(this.b));
   }

   public void c() {
      this.a.p(true);
      this.e = 40 + this.a.bc().nextInt(40);
   }

   public void d() {
      this.a.p(false);
      this.b = null;
   }

   public void e() {
      this.a.getControllerLook().a(this.b.locX, this.b.locY + (double)this.b.getHeadHeight(), this.b.locZ, 10.0F, (float)this.a.bQ());
      --this.e;
   }

   private boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      return var2 == null?false:(!this.a.isTamed() && var2.getItem() == Items.BONE?true:this.a.d(var2));
   }
}
