package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.Village;
import net.minecraft.server.VillageDoor;

public class PathfinderGoalRestrictOpenDoor extends PathfinderGoal {
   private EntityCreature a;
   private VillageDoor b;

   public PathfinderGoalRestrictOpenDoor(EntityCreature var1) {
      this.a = var1;
      if(!(var1.getNavigation() instanceof Navigation)) {
         throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
      }
   }

   public boolean a() {
      if(this.a.world.w()) {
         return false;
      } else {
         BlockPosition var1 = new BlockPosition(this.a);
         Village var2 = this.a.world.ae().getClosestVillage(var1, 16);
         if(var2 == null) {
            return false;
         } else {
            this.b = var2.b(var1);
            return this.b == null?false:(double)this.b.b(var1) < 2.25D;
         }
      }
   }

   public boolean b() {
      return this.a.world.w()?false:!this.b.i() && this.b.c(new BlockPosition(this.a));
   }

   public void c() {
      ((Navigation)this.a.getNavigation()).b(false);
      ((Navigation)this.a.getNavigation()).c(false);
   }

   public void d() {
      ((Navigation)this.a.getNavigation()).b(true);
      ((Navigation)this.a.getNavigation()).c(true);
      this.b = null;
   }

   public void e() {
      this.b.b();
   }
}
