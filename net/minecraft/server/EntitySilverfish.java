package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockMonsterEggs;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntitySilverfish extends EntityMonster {
   private EntitySilverfish.PathfinderGoalSilverfishWakeOthers a;

   public EntitySilverfish(World var1) {
      super(var1);
      this.setSize(0.4F, 0.3F);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(3, this.a = new EntitySilverfish.PathfinderGoalSilverfishWakeOthers(this));
      this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
      this.goalSelector.a(5, new EntitySilverfish.PathfinderGoalSilverfishHideInBlock(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
   }

   public double am() {
      return 0.2D;
   }

   public float getHeadHeight() {
      return 0.1F;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(1.0D);
   }

   protected boolean s_() {
      return false;
   }

   protected String z() {
      return "mob.silverfish.say";
   }

   protected String bo() {
      return "mob.silverfish.hit";
   }

   protected String bp() {
      return "mob.silverfish.kill";
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         if(var1 instanceof EntityDamageSource || var1 == DamageSource.MAGIC) {
            this.a.f();
         }

         return super.damageEntity(var1, var2);
      }
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.silverfish.step", 0.15F, 1.0F);
   }

   protected Item getLoot() {
      return null;
   }

   public void t_() {
      this.aI = this.yaw;
      super.t_();
   }

   public float a(BlockPosition var1) {
      return this.world.getType(var1.down()).getBlock() == Blocks.STONE?10.0F:super.a(var1);
   }

   protected boolean n_() {
      return true;
   }

   public boolean bR() {
      if(super.bR()) {
         EntityHuman var1 = this.world.findNearbyPlayer(this, 5.0D);
         return var1 == null;
      } else {
         return false;
      }
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.ARTHROPOD;
   }

   static class PathfinderGoalSilverfishHideInBlock extends PathfinderGoalRandomStroll {
      private final EntitySilverfish silverfish;
      private EnumDirection b;
      private boolean c;

      public PathfinderGoalSilverfishHideInBlock(EntitySilverfish var1) {
         super(var1, 1.0D, 10);
         this.silverfish = var1;
         this.a(1);
      }

      public boolean a() {
         if(this.silverfish.getGoalTarget() != null) {
            return false;
         } else if(!this.silverfish.getNavigation().m()) {
            return false;
         } else {
            Random var1 = this.silverfish.bc();
            if(var1.nextInt(10) == 0) {
               this.b = EnumDirection.a(var1);
               BlockPosition var2 = (new BlockPosition(this.silverfish.locX, this.silverfish.locY + 0.5D, this.silverfish.locZ)).shift(this.b);
               IBlockData var3 = this.silverfish.world.getType(var2);
               if(BlockMonsterEggs.d(var3)) {
                  this.c = true;
                  return true;
               }
            }

            this.c = false;
            return super.a();
         }
      }

      public boolean b() {
         return this.c?false:super.b();
      }

      public void c() {
         if(!this.c) {
            super.c();
         } else {
            World var1 = this.silverfish.world;
            BlockPosition var2 = (new BlockPosition(this.silverfish.locX, this.silverfish.locY + 0.5D, this.silverfish.locZ)).shift(this.b);
            IBlockData var3 = var1.getType(var2);
            if(BlockMonsterEggs.d(var3)) {
               var1.setTypeAndData(var2, Blocks.MONSTER_EGG.getBlockData().set(BlockMonsterEggs.VARIANT, BlockMonsterEggs.EnumMonsterEggVarient.a(var3)), 3);
               this.silverfish.y();
               this.silverfish.die();
            }

         }
      }
   }

   static class PathfinderGoalSilverfishWakeOthers extends PathfinderGoal {
      private EntitySilverfish silverfish;
      private int b;

      public PathfinderGoalSilverfishWakeOthers(EntitySilverfish var1) {
         this.silverfish = var1;
      }

      public void f() {
         if(this.b == 0) {
            this.b = 20;
         }

      }

      public boolean a() {
         return this.b > 0;
      }

      public void e() {
         --this.b;
         if(this.b <= 0) {
            World var1 = this.silverfish.world;
            Random var2 = this.silverfish.bc();
            BlockPosition var3 = new BlockPosition(this.silverfish);

            for(int var4 = 0; var4 <= 5 && var4 >= -5; var4 = var4 <= 0?1 - var4:0 - var4) {
               for(int var5 = 0; var5 <= 10 && var5 >= -10; var5 = var5 <= 0?1 - var5:0 - var5) {
                  for(int var6 = 0; var6 <= 10 && var6 >= -10; var6 = var6 <= 0?1 - var6:0 - var6) {
                     BlockPosition var7 = var3.a(var5, var4, var6);
                     IBlockData var8 = var1.getType(var7);
                     if(var8.getBlock() == Blocks.MONSTER_EGG) {
                        if(var1.getGameRules().getBoolean("mobGriefing")) {
                           var1.setAir(var7, true);
                        } else {
                           var1.setTypeAndData(var7, ((BlockMonsterEggs.EnumMonsterEggVarient)var8.get(BlockMonsterEggs.VARIANT)).d(), 3);
                        }

                        if(var2.nextBoolean()) {
                           return;
                        }
                     }
                  }
               }
            }
         }

      }
   }
}
