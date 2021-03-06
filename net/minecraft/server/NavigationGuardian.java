package net.minecraft.server;

import net.minecraft.server.EntityInsentient;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NavigationAbstract;
import net.minecraft.server.Pathfinder;
import net.minecraft.server.PathfinderWater;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class NavigationGuardian extends NavigationAbstract {
   public NavigationGuardian(EntityInsentient var1, World var2) {
      super(var1, var2);
   }

   protected Pathfinder a() {
      return new Pathfinder(new PathfinderWater());
   }

   protected boolean b() {
      return this.o();
   }

   protected Vec3D c() {
      return new Vec3D(this.b.locX, this.b.locY + (double)this.b.length * 0.5D, this.b.locZ);
   }

   protected void l() {
      Vec3D var1 = this.c();
      float var2 = this.b.width * this.b.width;
      byte var3 = 6;
      if(var1.distanceSquared(this.d.a(this.b, this.d.e())) < (double)var2) {
         this.d.a();
      }

      for(int var4 = Math.min(this.d.e() + var3, this.d.d() - 1); var4 > this.d.e(); --var4) {
         Vec3D var5 = this.d.a(this.b, var4);
         if(var5.distanceSquared(var1) <= 36.0D && this.a(var1, var5, 0, 0, 0)) {
            this.d.c(var4);
            break;
         }
      }

      this.a(var1);
   }

   protected void d() {
      super.d();
   }

   protected boolean a(Vec3D var1, Vec3D var2, int var3, int var4, int var5) {
      MovingObjectPosition var6 = this.c.rayTrace(var1, new Vec3D(var2.a, var2.b + (double)this.b.length * 0.5D, var2.c), false, true, false);
      return var6 == null || var6.type == MovingObjectPosition.EnumMovingObjectType.MISS;
   }
}
