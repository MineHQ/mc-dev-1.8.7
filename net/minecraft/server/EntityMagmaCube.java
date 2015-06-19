package net.minecraft.server;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Entity;
import net.minecraft.server.EntitySlime;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.World;

public class EntityMagmaCube extends EntitySlime {
   public EntityMagmaCube(World var1) {
      super(var1);
      this.fireProof = true;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
   }

   public boolean bR() {
      return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
   }

   public boolean canSpawn() {
      return this.world.a((AxisAlignedBB)this.getBoundingBox(), (Entity)this) && this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox());
   }

   public int br() {
      return this.getSize() * 3;
   }

   public float c(float var1) {
      return 1.0F;
   }

   protected EnumParticle n() {
      return EnumParticle.FLAME;
   }

   protected EntitySlime cf() {
      return new EntityMagmaCube(this.world);
   }

   protected Item getLoot() {
      return Items.MAGMA_CREAM;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      Item var3 = this.getLoot();
      if(var3 != null && this.getSize() > 1) {
         int var4 = this.random.nextInt(4) - 2;
         if(var2 > 0) {
            var4 += this.random.nextInt(var2 + 1);
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            this.a(var3, 1);
         }
      }

   }

   public boolean isBurning() {
      return false;
   }

   protected int cg() {
      return super.cg() * 4;
   }

   protected void ch() {
      this.a *= 0.9F;
   }

   protected void bF() {
      this.motY = (double)(0.42F + (float)this.getSize() * 0.1F);
      this.ai = true;
   }

   protected void bH() {
      this.motY = (double)(0.22F + (float)this.getSize() * 0.05F);
      this.ai = true;
   }

   public void e(float var1, float var2) {
   }

   protected boolean ci() {
      return true;
   }

   protected int cj() {
      return super.cj() + 2;
   }

   protected String ck() {
      return this.getSize() > 1?"mob.magmacube.big":"mob.magmacube.small";
   }

   protected boolean cl() {
      return true;
   }
}
