package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IRangedEntity;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.PathfinderGoalArrowAttack;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.World;

public class EntityWitch extends EntityMonster implements IRangedEntity {
   private static final UUID a = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
   private static final AttributeModifier b;
   private static final Item[] c;
   private int bm;

   public EntityWitch(World var1) {
      super(var1);
      this.setSize(0.6F, 1.95F);
      this.goalSelector.a(1, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 60, 10.0F));
      this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
   }

   protected void h() {
      super.h();
      this.getDataWatcher().a(21, Byte.valueOf((byte)0));
   }

   protected String z() {
      return null;
   }

   protected String bo() {
      return null;
   }

   protected String bp() {
      return null;
   }

   public void a(boolean var1) {
      this.getDataWatcher().watch(21, Byte.valueOf((byte)(var1?1:0)));
   }

   public boolean n() {
      return this.getDataWatcher().getByte(21) == 1;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(26.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
   }

   public void m() {
      if(!this.world.isClientSide) {
         if(this.n()) {
            if(this.bm-- <= 0) {
               this.a(false);
               ItemStack var6 = this.bA();
               this.setEquipment(0, (ItemStack)null);
               if(var6 != null && var6.getItem() == Items.POTION) {
                  List var5 = Items.POTION.h(var6);
                  if(var5 != null) {
                     Iterator var3 = var5.iterator();

                     while(var3.hasNext()) {
                        MobEffect var4 = (MobEffect)var3.next();
                        this.addEffect(new MobEffect(var4));
                     }
                  }
               }

               this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).c(b);
            }
         } else {
            short var1 = -1;
            if(this.random.nextFloat() < 0.15F && this.a(Material.WATER) && !this.hasEffect(MobEffectList.WATER_BREATHING)) {
               var1 = 8237;
            } else if(this.random.nextFloat() < 0.15F && this.isBurning() && !this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
               var1 = 16307;
            } else if(this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
               var1 = 16341;
            } else if(this.random.nextFloat() < 0.25F && this.getGoalTarget() != null && !this.hasEffect(MobEffectList.FASTER_MOVEMENT) && this.getGoalTarget().h(this) > 121.0D) {
               var1 = 16274;
            } else if(this.random.nextFloat() < 0.25F && this.getGoalTarget() != null && !this.hasEffect(MobEffectList.FASTER_MOVEMENT) && this.getGoalTarget().h(this) > 121.0D) {
               var1 = 16274;
            }

            if(var1 > -1) {
               this.setEquipment(0, new ItemStack(Items.POTION, 1, var1));
               this.bm = this.bA().l();
               this.a(true);
               AttributeInstance var2 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
               var2.c(b);
               var2.b(b);
            }
         }

         if(this.random.nextFloat() < 7.5E-4F) {
            this.world.broadcastEntityEffect(this, (byte)15);
         }
      }

      super.m();
   }

   protected float applyMagicModifier(DamageSource var1, float var2) {
      var2 = super.applyMagicModifier(var1, var2);
      if(var1.getEntity() == this) {
         var2 = 0.0F;
      }

      if(var1.isMagic()) {
         var2 = (float)((double)var2 * 0.15D);
      }

      return var2;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      int var3 = this.random.nextInt(3) + 1;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = this.random.nextInt(3);
         Item var6 = c[this.random.nextInt(c.length)];
         if(var2 > 0) {
            var5 += this.random.nextInt(var2 + 1);
         }

         for(int var7 = 0; var7 < var5; ++var7) {
            this.a(var6, 1);
         }
      }

   }

   public void a(EntityLiving var1, float var2) {
      if(!this.n()) {
         EntityPotion var3 = new EntityPotion(this.world, this, 32732);
         double var4 = var1.locY + (double)var1.getHeadHeight() - 1.100000023841858D;
         var3.pitch -= -20.0F;
         double var6 = var1.locX + var1.motX - this.locX;
         double var8 = var4 - this.locY;
         double var10 = var1.locZ + var1.motZ - this.locZ;
         float var12 = MathHelper.sqrt(var6 * var6 + var10 * var10);
         if(var12 >= 8.0F && !var1.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
            var3.setPotionValue(32698);
         } else if(var1.getHealth() >= 8.0F && !var1.hasEffect(MobEffectList.POISON)) {
            var3.setPotionValue(32660);
         } else if(var12 <= 3.0F && !var1.hasEffect(MobEffectList.WEAKNESS) && this.random.nextFloat() < 0.25F) {
            var3.setPotionValue(32696);
         }

         var3.shoot(var6, var8 + (double)(var12 * 0.2F), var10, 0.75F, 8.0F);
         this.world.addEntity(var3);
      }
   }

   public float getHeadHeight() {
      return 1.62F;
   }

   static {
      b = (new AttributeModifier(a, "Drinking speed penalty", -0.25D, 0)).a(false);
      c = new Item[]{Items.GLOWSTONE_DUST, Items.SUGAR, Items.REDSTONE, Items.SPIDER_EYE, Items.GLASS_BOTTLE, Items.GUNPOWDER, Items.STICK, Items.STICK};
   }
}
