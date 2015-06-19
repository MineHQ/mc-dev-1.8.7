package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.PathfinderGoalLookAtPlayer;

public class PathfinderGoalLookAtTradingPlayer extends PathfinderGoalLookAtPlayer {
   private final EntityVillager e;

   public PathfinderGoalLookAtTradingPlayer(EntityVillager var1) {
      super(var1, EntityHuman.class, 8.0F);
      this.e = var1;
   }

   public boolean a() {
      if(this.e.co()) {
         this.b = this.e.v_();
         return true;
      } else {
         return false;
      }
   }
}
