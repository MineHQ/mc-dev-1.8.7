package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityGolem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalDefendVillage;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalMoveTowardsTarget;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalOfferFlower;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.Village;
import net.minecraft.server.World;

public class EntityIronGolem extends EntityGolem {
   private int b;
   Village a;
   private int c;
   private int bm;

   public EntityIronGolem(World var1) {
      super(var1);
      this.setSize(1.4F, 2.9F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
      this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
      this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, true));
      this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
      this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
      this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.6D));
      this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
      this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(3, new EntityIronGolem.PathfinderGoalNearestGolemTarget(this, EntityInsentient.class, 10, false, true, IMonster.e));
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)0));
   }

   protected void E() {
      if(--this.b <= 0) {
         this.b = 70 + this.random.nextInt(50);
         this.a = this.world.ae().getClosestVillage(new BlockPosition(this), 32);
         if(this.a == null) {
            this.cj();
         } else {
            BlockPosition var1 = this.a.a();
            this.a(var1, (int)((float)this.a.b() * 0.6F));
         }
      }

      super.E();
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   protected int j(int var1) {
      return var1;
   }

   protected void s(Entity var1) {
      if(var1 instanceof IMonster && !(var1 instanceof EntityCreeper) && this.bc().nextInt(20) == 0) {
         this.setGoalTarget((EntityLiving)var1);
      }

      super.s(var1);
   }

   public void m() {
      super.m();
      if(this.c > 0) {
         --this.c;
      }

      if(this.bm > 0) {
         --this.bm;
      }

      if(this.motX * this.motX + this.motZ * this.motZ > 2.500000277905201E-7D && this.random.nextInt(5) == 0) {
         int var1 = MathHelper.floor(this.locX);
         int var2 = MathHelper.floor(this.locY - 0.20000000298023224D);
         int var3 = MathHelper.floor(this.locZ);
         IBlockData var4 = this.world.getType(new BlockPosition(var1, var2, var3));
         Block var5 = var4.getBlock();
         if(var5.getMaterial() != Material.AIR) {
            this.world.addParticle(EnumParticle.BLOCK_CRACK, this.locX + ((double)this.random.nextFloat() - 0.5D) * (double)this.width, this.getBoundingBox().b + 0.1D, this.locZ + ((double)this.random.nextFloat() - 0.5D) * (double)this.width, 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D, new int[]{Block.getCombinedId(var4)});
         }
      }

   }

   public boolean a(Class<? extends EntityLiving> var1) {
      return this.isPlayerCreated() && EntityHuman.class.isAssignableFrom(var1)?false:(var1 == EntityCreeper.class?false:super.a(var1));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("PlayerCreated", this.isPlayerCreated());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setPlayerCreated(var1.getBoolean("PlayerCreated"));
   }

   public boolean r(Entity var1) {
      this.c = 10;
      this.world.broadcastEntityEffect(this, (byte)4);
      boolean var2 = var1.damageEntity(DamageSource.mobAttack(this), (float)(7 + this.random.nextInt(15)));
      if(var2) {
         var1.motY += 0.4000000059604645D;
         this.a(this, var1);
      }

      this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
      return var2;
   }

   public Village n() {
      return this.a;
   }

   public void a(boolean var1) {
      this.bm = var1?400:0;
      this.world.broadcastEntityEffect(this, (byte)11);
   }

   protected String bo() {
      return "mob.irongolem.hit";
   }

   protected String bp() {
      return "mob.irongolem.death";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(3);

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         this.a(Item.getItemOf(Blocks.RED_FLOWER), 1, (float)BlockFlowers.EnumFlowerVarient.POPPY.b());
      }

      var4 = 3 + this.random.nextInt(3);

      for(int var5 = 0; var5 < var4; ++var5) {
         this.a(Items.IRON_INGOT, 1);
      }

   }

   public int cm() {
      return this.bm;
   }

   public boolean isPlayerCreated() {
      return (this.datawatcher.getByte(16) & 1) != 0;
   }

   public void setPlayerCreated(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 1)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -2)));
      }

   }

   public void die(DamageSource var1) {
      if(!this.isPlayerCreated() && this.killer != null && this.a != null) {
         this.a.a(this.killer.getName(), -5);
      }

      super.die(var1);
   }

   static class PathfinderGoalNearestGolemTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {
      public PathfinderGoalNearestGolemTarget(final EntityCreature var1, Class<T> var2, int var3, boolean var4, boolean var5, final Predicate<? super T> var6) {
         super(var1, var2, var3, var4, var5, var6);
         this.c = new Predicate() {
            public boolean a(T var1x) {
               if(var6 != null && !var6.apply(var1x)) {
                  return false;
               } else if(var1x instanceof EntityCreeper) {
                  return false;
               } else {
                  if(var1x instanceof EntityHuman) {
                     double var2 = PathfinderGoalNearestGolemTarget.this.f();
                     if(var1x.isSneaking()) {
                        var2 *= 0.800000011920929D;
                     }

                     if(var1x.isInvisible()) {
                        float var4 = ((EntityHuman)var1x).bY();
                        if(var4 < 0.1F) {
                           var4 = 0.1F;
                        }

                        var2 *= (double)(0.7F * var4);
                     }

                     if((double)var1x.g(var1) > var2) {
                        return false;
                     }
                  }

                  return PathfinderGoalNearestGolemTarget.this.a(var1x, false);
               }
            }

            // $FF: synthetic method
            public boolean apply(Object var1x) {
               return this.a((EntityLiving)var1x);
            }
         };
      }
   }
}
