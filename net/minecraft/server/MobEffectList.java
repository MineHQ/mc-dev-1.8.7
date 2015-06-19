package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeMapBase;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IAttribute;
import net.minecraft.server.InstantMobEffect;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MobEffectAbsorption;
import net.minecraft.server.MobEffectAttackDamage;
import net.minecraft.server.MobEffectHealthBoost;

public class MobEffectList {
   public static final MobEffectList[] byId = new MobEffectList[32];
   private static final Map<MinecraftKey, MobEffectList> I = Maps.newHashMap();
   public static final MobEffectList b = null;
   public static final MobEffectList FASTER_MOVEMENT;
   public static final MobEffectList SLOWER_MOVEMENT;
   public static final MobEffectList FASTER_DIG;
   public static final MobEffectList SLOWER_DIG;
   public static final MobEffectList INCREASE_DAMAGE;
   public static final MobEffectList HEAL;
   public static final MobEffectList HARM;
   public static final MobEffectList JUMP;
   public static final MobEffectList CONFUSION;
   public static final MobEffectList REGENERATION;
   public static final MobEffectList RESISTANCE;
   public static final MobEffectList FIRE_RESISTANCE;
   public static final MobEffectList WATER_BREATHING;
   public static final MobEffectList INVISIBILITY;
   public static final MobEffectList BLINDNESS;
   public static final MobEffectList NIGHT_VISION;
   public static final MobEffectList HUNGER;
   public static final MobEffectList WEAKNESS;
   public static final MobEffectList POISON;
   public static final MobEffectList WITHER;
   public static final MobEffectList HEALTH_BOOST;
   public static final MobEffectList ABSORBTION;
   public static final MobEffectList SATURATION;
   public static final MobEffectList z;
   public static final MobEffectList A;
   public static final MobEffectList B;
   public static final MobEffectList C;
   public static final MobEffectList D;
   public static final MobEffectList E;
   public static final MobEffectList F;
   public static final MobEffectList G;
   public final int id;
   private final Map<IAttribute, AttributeModifier> J = Maps.newHashMap();
   private final boolean K;
   private final int L;
   private String M = "";
   private int N = -1;
   private double O;
   private boolean P;

   protected MobEffectList(int var1, MinecraftKey var2, boolean var3, int var4) {
      this.id = var1;
      byId[var1] = this;
      I.put(var2, this);
      this.K = var3;
      if(var3) {
         this.O = 0.5D;
      } else {
         this.O = 1.0D;
      }

      this.L = var4;
   }

   public static MobEffectList b(String var0) {
      return (MobEffectList)I.get(new MinecraftKey(var0));
   }

   public static Set<MinecraftKey> c() {
      return I.keySet();
   }

   protected MobEffectList b(int var1, int var2) {
      this.N = var1 + var2 * 8;
      return this;
   }

   public int getId() {
      return this.id;
   }

   public void tick(EntityLiving var1, int var2) {
      if(this.id == REGENERATION.id) {
         if(var1.getHealth() < var1.getMaxHealth()) {
            var1.heal(1.0F);
         }
      } else if(this.id == POISON.id) {
         if(var1.getHealth() > 1.0F) {
            var1.damageEntity(DamageSource.MAGIC, 1.0F);
         }
      } else if(this.id == WITHER.id) {
         var1.damageEntity(DamageSource.WITHER, 1.0F);
      } else if(this.id == HUNGER.id && var1 instanceof EntityHuman) {
         ((EntityHuman)var1).applyExhaustion(0.025F * (float)(var2 + 1));
      } else if(this.id == SATURATION.id && var1 instanceof EntityHuman) {
         if(!var1.world.isClientSide) {
            ((EntityHuman)var1).getFoodData().eat(var2 + 1, 1.0F);
         }
      } else if((this.id != HEAL.id || var1.bm()) && (this.id != HARM.id || !var1.bm())) {
         if(this.id == HARM.id && !var1.bm() || this.id == HEAL.id && var1.bm()) {
            var1.damageEntity(DamageSource.MAGIC, (float)(6 << var2));
         }
      } else {
         var1.heal((float)Math.max(4 << var2, 0));
      }

   }

   public void applyInstantEffect(Entity var1, Entity var2, EntityLiving var3, int var4, double var5) {
      int var7;
      if((this.id != HEAL.id || var3.bm()) && (this.id != HARM.id || !var3.bm())) {
         if(this.id == HARM.id && !var3.bm() || this.id == HEAL.id && var3.bm()) {
            var7 = (int)(var5 * (double)(6 << var4) + 0.5D);
            if(var1 == null) {
               var3.damageEntity(DamageSource.MAGIC, (float)var7);
            } else {
               var3.damageEntity(DamageSource.b(var1, var2), (float)var7);
            }
         }
      } else {
         var7 = (int)(var5 * (double)(4 << var4) + 0.5D);
         var3.heal((float)var7);
      }

   }

   public boolean isInstant() {
      return false;
   }

   public boolean a(int var1, int var2) {
      int var3;
      if(this.id == REGENERATION.id) {
         var3 = 50 >> var2;
         return var3 > 0?var1 % var3 == 0:true;
      } else if(this.id == POISON.id) {
         var3 = 25 >> var2;
         return var3 > 0?var1 % var3 == 0:true;
      } else if(this.id == WITHER.id) {
         var3 = 40 >> var2;
         return var3 > 0?var1 % var3 == 0:true;
      } else {
         return this.id == HUNGER.id;
      }
   }

   public MobEffectList c(String var1) {
      this.M = var1;
      return this;
   }

   public String a() {
      return this.M;
   }

   protected MobEffectList a(double var1) {
      this.O = var1;
      return this;
   }

   public double getDurationModifier() {
      return this.O;
   }

   public boolean j() {
      return this.P;
   }

   public int k() {
      return this.L;
   }

   public MobEffectList a(IAttribute var1, String var2, double var3, int var5) {
      AttributeModifier var6 = new AttributeModifier(UUID.fromString(var2), this.a(), var3, var5);
      this.J.put(var1, var6);
      return this;
   }

   public void a(EntityLiving var1, AttributeMapBase var2, int var3) {
      Iterator var4 = this.J.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         AttributeInstance var6 = var2.a((IAttribute)var5.getKey());
         if(var6 != null) {
            var6.c((AttributeModifier)var5.getValue());
         }
      }

   }

   public void b(EntityLiving var1, AttributeMapBase var2, int var3) {
      Iterator var4 = this.J.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         AttributeInstance var6 = var2.a((IAttribute)var5.getKey());
         if(var6 != null) {
            AttributeModifier var7 = (AttributeModifier)var5.getValue();
            var6.c(var7);
            var6.b(new AttributeModifier(var7.a(), this.a() + " " + var3, this.a(var3, var7), var7.c()));
         }
      }

   }

   public double a(int var1, AttributeModifier var2) {
      return var2.d() * (double)(var1 + 1);
   }

   static {
      FASTER_MOVEMENT = (new MobEffectList(1, new MinecraftKey("speed"), false, 8171462)).c("potion.moveSpeed").b(0, 0).a(GenericAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2);
      SLOWER_MOVEMENT = (new MobEffectList(2, new MinecraftKey("slowness"), true, 5926017)).c("potion.moveSlowdown").b(1, 0).a(GenericAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2);
      FASTER_DIG = (new MobEffectList(3, new MinecraftKey("haste"), false, 14270531)).c("potion.digSpeed").b(2, 0).a(1.5D);
      SLOWER_DIG = (new MobEffectList(4, new MinecraftKey("mining_fatigue"), true, 4866583)).c("potion.digSlowDown").b(3, 0);
      INCREASE_DAMAGE = (new MobEffectAttackDamage(5, new MinecraftKey("strength"), false, 9643043)).c("potion.damageBoost").b(4, 0).a(GenericAttributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 2.5D, 2);
      HEAL = (new InstantMobEffect(6, new MinecraftKey("instant_health"), false, 16262179)).c("potion.heal");
      HARM = (new InstantMobEffect(7, new MinecraftKey("instant_damage"), true, 4393481)).c("potion.harm");
      JUMP = (new MobEffectList(8, new MinecraftKey("jump_boost"), false, 2293580)).c("potion.jump").b(2, 1);
      CONFUSION = (new MobEffectList(9, new MinecraftKey("nausea"), true, 5578058)).c("potion.confusion").b(3, 1).a(0.25D);
      REGENERATION = (new MobEffectList(10, new MinecraftKey("regeneration"), false, 13458603)).c("potion.regeneration").b(7, 0).a(0.25D);
      RESISTANCE = (new MobEffectList(11, new MinecraftKey("resistance"), false, 10044730)).c("potion.resistance").b(6, 1);
      FIRE_RESISTANCE = (new MobEffectList(12, new MinecraftKey("fire_resistance"), false, 14981690)).c("potion.fireResistance").b(7, 1);
      WATER_BREATHING = (new MobEffectList(13, new MinecraftKey("water_breathing"), false, 3035801)).c("potion.waterBreathing").b(0, 2);
      INVISIBILITY = (new MobEffectList(14, new MinecraftKey("invisibility"), false, 8356754)).c("potion.invisibility").b(0, 1);
      BLINDNESS = (new MobEffectList(15, new MinecraftKey("blindness"), true, 2039587)).c("potion.blindness").b(5, 1).a(0.25D);
      NIGHT_VISION = (new MobEffectList(16, new MinecraftKey("night_vision"), false, 2039713)).c("potion.nightVision").b(4, 1);
      HUNGER = (new MobEffectList(17, new MinecraftKey("hunger"), true, 5797459)).c("potion.hunger").b(1, 1);
      WEAKNESS = (new MobEffectAttackDamage(18, new MinecraftKey("weakness"), true, 4738376)).c("potion.weakness").b(5, 0).a(GenericAttributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);
      POISON = (new MobEffectList(19, new MinecraftKey("poison"), true, 5149489)).c("potion.poison").b(6, 0).a(0.25D);
      WITHER = (new MobEffectList(20, new MinecraftKey("wither"), true, 3484199)).c("potion.wither").b(1, 2).a(0.25D);
      HEALTH_BOOST = (new MobEffectHealthBoost(21, new MinecraftKey("health_boost"), false, 16284963)).c("potion.healthBoost").b(2, 2).a(GenericAttributes.maxHealth, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0);
      ABSORBTION = (new MobEffectAbsorption(22, new MinecraftKey("absorption"), false, 2445989)).c("potion.absorption").b(2, 2);
      SATURATION = (new InstantMobEffect(23, new MinecraftKey("saturation"), false, 16262179)).c("potion.saturation");
      z = null;
      A = null;
      B = null;
      C = null;
      D = null;
      E = null;
      F = null;
      G = null;
   }
}
