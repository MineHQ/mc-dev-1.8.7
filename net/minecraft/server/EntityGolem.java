package net.minecraft.server;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.IAnimal;
import net.minecraft.server.World;

public abstract class EntityGolem extends EntityCreature implements IAnimal {
   public EntityGolem(World var1) {
      super(var1);
   }

   public void e(float var1, float var2) {
   }

   protected String z() {
      return "none";
   }

   protected String bo() {
      return "none";
   }

   protected String bp() {
      return "none";
   }

   public int w() {
      return 120;
   }

   protected boolean isTypeNotPersistent() {
      return false;
   }
}
