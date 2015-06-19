package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.IAnimal;
import net.minecraft.server.World;

public abstract class EntityAmbient extends EntityInsentient implements IAnimal {
   public EntityAmbient(World var1) {
      super(var1);
   }

   public boolean cb() {
      return false;
   }

   protected boolean a(EntityHuman var1) {
      return false;
   }
}
