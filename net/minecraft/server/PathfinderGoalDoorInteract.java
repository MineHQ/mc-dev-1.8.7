package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.Material;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;
import net.minecraft.server.PathfinderGoal;

public abstract class PathfinderGoalDoorInteract extends PathfinderGoal {
   protected EntityInsentient a;
   protected BlockPosition b;
   protected BlockDoor c;
   boolean d;
   float e;
   float f;

   public PathfinderGoalDoorInteract(EntityInsentient var1) {
      this.b = BlockPosition.ZERO;
      this.a = var1;
      if(!(var1.getNavigation() instanceof Navigation)) {
         throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
      }
   }

   public boolean a() {
      if(!this.a.positionChanged) {
         return false;
      } else {
         Navigation var1 = (Navigation)this.a.getNavigation();
         PathEntity var2 = var1.j();
         if(var2 != null && !var2.b() && var1.g()) {
            for(int var3 = 0; var3 < Math.min(var2.e() + 2, var2.d()); ++var3) {
               PathPoint var4 = var2.a(var3);
               this.b = new BlockPosition(var4.a, var4.b + 1, var4.c);
               if(this.a.e((double)this.b.getX(), this.a.locY, (double)this.b.getZ()) <= 2.25D) {
                  this.c = this.a(this.b);
                  if(this.c != null) {
                     return true;
                  }
               }
            }

            this.b = (new BlockPosition(this.a)).up();
            this.c = this.a(this.b);
            return this.c != null;
         } else {
            return false;
         }
      }
   }

   public boolean b() {
      return !this.d;
   }

   public void c() {
      this.d = false;
      this.e = (float)((double)((float)this.b.getX() + 0.5F) - this.a.locX);
      this.f = (float)((double)((float)this.b.getZ() + 0.5F) - this.a.locZ);
   }

   public void e() {
      float var1 = (float)((double)((float)this.b.getX() + 0.5F) - this.a.locX);
      float var2 = (float)((double)((float)this.b.getZ() + 0.5F) - this.a.locZ);
      float var3 = this.e * var1 + this.f * var2;
      if(var3 < 0.0F) {
         this.d = true;
      }

   }

   private BlockDoor a(BlockPosition var1) {
      Block var2 = this.a.world.getType(var1).getBlock();
      return var2 instanceof BlockDoor && var2.getMaterial() == Material.WOOD?(BlockDoor)var2:null;
   }
}
