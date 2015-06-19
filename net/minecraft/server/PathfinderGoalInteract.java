package net.minecraft.server;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.PathfinderGoalLookAtPlayer;

public class PathfinderGoalInteract extends PathfinderGoalLookAtPlayer {
   public PathfinderGoalInteract(EntityInsentient var1, Class<? extends Entity> var2, float var3, float var4) {
      super(var1, var2, var3, var4);
      this.a(3);
   }
}
