package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.World;

public class EntityGiantZombie extends EntityMonster {
   public EntityGiantZombie(World var1) {
      super(var1);
      this.setSize(this.width * 6.0F, this.length * 6.0F);
   }

   public float getHeadHeight() {
      return 10.440001F;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(50.0D);
   }

   public float a(BlockPosition var1) {
      return this.world.o(var1) - 0.5F;
   }
}
