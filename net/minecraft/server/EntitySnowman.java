package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityGolem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IMonster;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntitySnowman extends EntityGolem implements IRangedEntity {
   public EntitySnowman(World var1) {
      super(var1);
      this.setSize(0.7F, 1.9F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
      this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 10, true, false, IMonster.d));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(4.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
   }

   public void m() {
      super.m();
      if(!this.world.isClientSide) {
         int var1 = MathHelper.floor(this.locX);
         int var2 = MathHelper.floor(this.locY);
         int var3 = MathHelper.floor(this.locZ);
         if(this.U()) {
            this.damageEntity(DamageSource.DROWN, 1.0F);
         }

         if(this.world.getBiome(new BlockPosition(var1, 0, var3)).a(new BlockPosition(var1, var2, var3)) > 1.0F) {
            this.damageEntity(DamageSource.BURN, 1.0F);
         }

         for(int var4 = 0; var4 < 4; ++var4) {
            var1 = MathHelper.floor(this.locX + (double)((float)(var4 % 2 * 2 - 1) * 0.25F));
            var2 = MathHelper.floor(this.locY);
            var3 = MathHelper.floor(this.locZ + (double)((float)(var4 / 2 % 2 * 2 - 1) * 0.25F));
            BlockPosition var5 = new BlockPosition(var1, var2, var3);
            if(this.world.getType(var5).getBlock().getMaterial() == Material.AIR && this.world.getBiome(new BlockPosition(var1, 0, var3)).a(var5) < 0.8F && Blocks.SNOW_LAYER.canPlace(this.world, var5)) {
               this.world.setTypeUpdate(var5, Blocks.SNOW_LAYER.getBlockData());
            }
         }
      }

   }

   protected Item getLoot() {
      return Items.SNOWBALL;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(16);

      for(int var4 = 0; var4 < var3; ++var4) {
         this.a(Items.SNOWBALL, 1);
      }

   }

   public void a(EntityLiving var1, float var2) {
      EntitySnowball var3 = new EntitySnowball(this.world, this);
      double var4 = var1.locY + (double)var1.getHeadHeight() - 1.100000023841858D;
      double var6 = var1.locX - this.locX;
      double var8 = var4 - var3.locY;
      double var10 = var1.locZ - this.locZ;
      float var12 = MathHelper.sqrt(var6 * var6 + var10 * var10) * 0.2F;
      var3.shoot(var6, var8 + (double)var12, var10, 1.6F, 12.0F);
      this.makeSound("random.bow", 1.0F, 1.0F / (this.bc().nextFloat() * 0.4F + 0.8F));
      this.world.addEntity(var3);
   }

   public float getHeadHeight() {
      return 1.7F;
   }
}
