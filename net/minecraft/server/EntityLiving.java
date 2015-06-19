package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeMapBase;
import net.minecraft.server.AttributeMapServer;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CombatTracker;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTracker;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IAttribute;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagShort;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutAnimation;
import net.minecraft.server.PacketPlayOutCollect;
import net.minecraft.server.PacketPlayOutEntityEquipment;
import net.minecraft.server.PotionBrewer;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public abstract class EntityLiving extends Entity {
   private static final UUID a = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
   private static final AttributeModifier b;
   private AttributeMapBase c;
   public final CombatTracker combatTracker = new CombatTracker(this);
   public final Map<Integer, MobEffect> effects = Maps.newHashMap();
   private final ItemStack[] h = new ItemStack[5];
   public boolean ar;
   public int as;
   public int at;
   public int hurtTicks;
   public int av;
   public float aw;
   public int deathTicks;
   public float ay;
   public float az;
   public float aA;
   public float aB;
   public float aC;
   public int maxNoDamageTicks = 20;
   public float aE;
   public float aF;
   public float aG;
   public float aH;
   public float aI;
   public float aJ;
   public float aK;
   public float aL;
   public float aM = 0.02F;
   public EntityHuman killer;
   protected int lastDamageByPlayerTime;
   protected boolean aP;
   protected int ticksFarFromPlayer;
   protected float aR;
   protected float aS;
   protected float aT;
   protected float aU;
   protected float aV;
   protected int aW;
   public float lastDamage;
   protected boolean aY;
   public float aZ;
   public float ba;
   protected float bb;
   protected int bc;
   protected double bd;
   protected double be;
   protected double bf;
   protected double bg;
   protected double bh;
   public boolean updateEffects = true;
   public EntityLiving lastDamager;
   public int hurtTimestamp;
   private EntityLiving bk;
   private int bl;
   private float bm;
   private int bn;
   private float bo;

   public void G() {
      this.damageEntity(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
   }

   public EntityLiving(World var1) {
      super(var1);
      this.initAttributes();
      this.setHealth(this.getMaxHealth());
      this.k = true;
      this.aH = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
      this.setPosition(this.locX, this.locY, this.locZ);
      this.aG = (float)Math.random() * 12398.0F;
      this.yaw = (float)(Math.random() * 3.1415927410125732D * 2.0D);
      this.aK = this.yaw;
      this.S = 0.6F;
   }

   protected void h() {
      this.datawatcher.a(7, Integer.valueOf(0));
      this.datawatcher.a(8, Byte.valueOf((byte)0));
      this.datawatcher.a(9, Byte.valueOf((byte)0));
      this.datawatcher.a(6, Float.valueOf(1.0F));
   }

   protected void initAttributes() {
      this.getAttributeMap().b(GenericAttributes.maxHealth);
      this.getAttributeMap().b(GenericAttributes.c);
      this.getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
   }

   protected void a(double var1, boolean var3, Block var4, BlockPosition var5) {
      if(!this.V()) {
         this.W();
      }

      if(!this.world.isClientSide && this.fallDistance > 3.0F && var3) {
         IBlockData var6 = this.world.getType(var5);
         Block var7 = var6.getBlock();
         float var8 = (float)MathHelper.f(this.fallDistance - 3.0F);
         if(var7.getMaterial() != Material.AIR) {
            double var9 = (double)Math.min(0.2F + var8 / 15.0F, 10.0F);
            if(var9 > 2.5D) {
               var9 = 2.5D;
            }

            int var11 = (int)(150.0D * var9);
            ((WorldServer)this.world).a(EnumParticle.BLOCK_DUST, this.locX, this.locY, this.locZ, var11, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[]{Block.getCombinedId(var6)});
         }
      }

      super.a(var1, var3, var4, var5);
   }

   public boolean aY() {
      return false;
   }

   public void K() {
      this.ay = this.az;
      super.K();
      this.world.methodProfiler.a("livingEntityBaseTick");
      boolean var1 = this instanceof EntityHuman;
      if(this.isAlive()) {
         if(this.inBlock()) {
            this.damageEntity(DamageSource.STUCK, 1.0F);
         } else if(var1 && !this.world.getWorldBorder().a(this.getBoundingBox())) {
            double var2 = this.world.getWorldBorder().a((Entity)this) + this.world.getWorldBorder().getDamageBuffer();
            if(var2 < 0.0D) {
               this.damageEntity(DamageSource.STUCK, (float)Math.max(1, MathHelper.floor(-var2 * this.world.getWorldBorder().getDamageAmount())));
            }
         }
      }

      if(this.isFireProof() || this.world.isClientSide) {
         this.extinguish();
      }

      boolean var7 = var1 && ((EntityHuman)this).abilities.isInvulnerable;
      if(this.isAlive()) {
         if(this.a((Material)Material.WATER)) {
            if(!this.aY() && !this.hasEffect(MobEffectList.WATER_BREATHING.id) && !var7) {
               this.setAirTicks(this.j(this.getAirTicks()));
               if(this.getAirTicks() == -20) {
                  this.setAirTicks(0);

                  for(int var3 = 0; var3 < 8; ++var3) {
                     float var4 = this.random.nextFloat() - this.random.nextFloat();
                     float var5 = this.random.nextFloat() - this.random.nextFloat();
                     float var6 = this.random.nextFloat() - this.random.nextFloat();
                     this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + (double)var4, this.locY + (double)var5, this.locZ + (double)var6, this.motX, this.motY, this.motZ, new int[0]);
                  }

                  this.damageEntity(DamageSource.DROWN, 2.0F);
               }
            }

            if(!this.world.isClientSide && this.au() && this.vehicle instanceof EntityLiving) {
               this.mount((Entity)null);
            }
         } else {
            this.setAirTicks(300);
         }
      }

      if(this.isAlive() && this.U()) {
         this.extinguish();
      }

      this.aE = this.aF;
      if(this.hurtTicks > 0) {
         --this.hurtTicks;
      }

      if(this.noDamageTicks > 0 && !(this instanceof EntityPlayer)) {
         --this.noDamageTicks;
      }

      if(this.getHealth() <= 0.0F) {
         this.aZ();
      }

      if(this.lastDamageByPlayerTime > 0) {
         --this.lastDamageByPlayerTime;
      } else {
         this.killer = null;
      }

      if(this.bk != null && !this.bk.isAlive()) {
         this.bk = null;
      }

      if(this.lastDamager != null) {
         if(!this.lastDamager.isAlive()) {
            this.b((EntityLiving)null);
         } else if(this.ticksLived - this.hurtTimestamp > 100) {
            this.b((EntityLiving)null);
         }
      }

      this.bi();
      this.aU = this.aT;
      this.aJ = this.aI;
      this.aL = this.aK;
      this.lastYaw = this.yaw;
      this.lastPitch = this.pitch;
      this.world.methodProfiler.b();
   }

   public boolean isBaby() {
      return false;
   }

   protected void aZ() {
      ++this.deathTicks;
      if(this.deathTicks == 20) {
         int var1;
         if(!this.world.isClientSide && (this.lastDamageByPlayerTime > 0 || this.alwaysGivesExp()) && this.ba() && this.world.getGameRules().getBoolean("doMobLoot")) {
            var1 = this.getExpValue(this.killer);

            while(var1 > 0) {
               int var2 = EntityExperienceOrb.getOrbValue(var1);
               var1 -= var2;
               this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, var2));
            }
         }

         this.die();

         for(var1 = 0; var1 < 20; ++var1) {
            double var8 = this.random.nextGaussian() * 0.02D;
            double var4 = this.random.nextGaussian() * 0.02D;
            double var6 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(EnumParticle.EXPLOSION_NORMAL, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var8, var4, var6, new int[0]);
         }
      }

   }

   protected boolean ba() {
      return !this.isBaby();
   }

   protected int j(int var1) {
      int var2 = EnchantmentManager.getOxygenEnchantmentLevel(this);
      return var2 > 0 && this.random.nextInt(var2 + 1) > 0?var1:var1 - 1;
   }

   protected int getExpValue(EntityHuman var1) {
      return 0;
   }

   protected boolean alwaysGivesExp() {
      return false;
   }

   public Random bc() {
      return this.random;
   }

   public EntityLiving getLastDamager() {
      return this.lastDamager;
   }

   public int be() {
      return this.hurtTimestamp;
   }

   public void b(EntityLiving var1) {
      this.lastDamager = var1;
      this.hurtTimestamp = this.ticksLived;
   }

   public EntityLiving bf() {
      return this.bk;
   }

   public int bg() {
      return this.bl;
   }

   public void p(Entity var1) {
      if(var1 instanceof EntityLiving) {
         this.bk = (EntityLiving)var1;
      } else {
         this.bk = null;
      }

      this.bl = this.ticksLived;
   }

   public int bh() {
      return this.ticksFarFromPlayer;
   }

   public void b(NBTTagCompound var1) {
      var1.setFloat("HealF", this.getHealth());
      var1.setShort("Health", (short)((int)Math.ceil((double)this.getHealth())));
      var1.setShort("HurtTime", (short)this.hurtTicks);
      var1.setInt("HurtByTimestamp", this.hurtTimestamp);
      var1.setShort("DeathTime", (short)this.deathTicks);
      var1.setFloat("AbsorptionAmount", this.getAbsorptionHearts());
      ItemStack[] var2 = this.getEquipment();
      int var3 = var2.length;

      int var4;
      ItemStack var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if(var5 != null) {
            this.c.a(var5.B());
         }
      }

      var1.set("Attributes", GenericAttributes.a(this.getAttributeMap()));
      var2 = this.getEquipment();
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         if(var5 != null) {
            this.c.b(var5.B());
         }
      }

      if(!this.effects.isEmpty()) {
         NBTTagList var6 = new NBTTagList();
         Iterator var7 = this.effects.values().iterator();

         while(var7.hasNext()) {
            MobEffect var8 = (MobEffect)var7.next();
            var6.add(var8.a(new NBTTagCompound()));
         }

         var1.set("ActiveEffects", var6);
      }

   }

   public void a(NBTTagCompound var1) {
      this.setAbsorptionHearts(var1.getFloat("AbsorptionAmount"));
      if(var1.hasKeyOfType("Attributes", 9) && this.world != null && !this.world.isClientSide) {
         GenericAttributes.a(this.getAttributeMap(), var1.getList("Attributes", 10));
      }

      if(var1.hasKeyOfType("ActiveEffects", 9)) {
         NBTTagList var2 = var1.getList("ActiveEffects", 10);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            NBTTagCompound var4 = var2.get(var3);
            MobEffect var5 = MobEffect.b(var4);
            if(var5 != null) {
               this.effects.put(Integer.valueOf(var5.getEffectId()), var5);
            }
         }
      }

      if(var1.hasKeyOfType("HealF", 99)) {
         this.setHealth(var1.getFloat("HealF"));
      } else {
         NBTBase var6 = var1.get("Health");
         if(var6 == null) {
            this.setHealth(this.getMaxHealth());
         } else if(var6.getTypeId() == 5) {
            this.setHealth(((NBTTagFloat)var6).h());
         } else if(var6.getTypeId() == 2) {
            this.setHealth((float)((NBTTagShort)var6).e());
         }
      }

      this.hurtTicks = var1.getShort("HurtTime");
      this.deathTicks = var1.getShort("DeathTime");
      this.hurtTimestamp = var1.getInt("HurtByTimestamp");
   }

   protected void bi() {
      Iterator var1 = this.effects.keySet().iterator();

      while(var1.hasNext()) {
         Integer var2 = (Integer)var1.next();
         MobEffect var3 = (MobEffect)this.effects.get(var2);
         if(!var3.tick(this)) {
            if(!this.world.isClientSide) {
               var1.remove();
               this.b(var3);
            }
         } else if(var3.getDuration() % 600 == 0) {
            this.a(var3, false);
         }
      }

      if(this.updateEffects) {
         if(!this.world.isClientSide) {
            this.B();
         }

         this.updateEffects = false;
      }

      int var11 = this.datawatcher.getInt(7);
      boolean var12 = this.datawatcher.getByte(8) > 0;
      if(var11 > 0) {
         boolean var4 = false;
         if(!this.isInvisible()) {
            var4 = this.random.nextBoolean();
         } else {
            var4 = this.random.nextInt(15) == 0;
         }

         if(var12) {
            var4 &= this.random.nextInt(5) == 0;
         }

         if(var4 && var11 > 0) {
            double var5 = (double)(var11 >> 16 & 255) / 255.0D;
            double var7 = (double)(var11 >> 8 & 255) / 255.0D;
            double var9 = (double)(var11 >> 0 & 255) / 255.0D;
            this.world.addParticle(var12?EnumParticle.SPELL_MOB_AMBIENT:EnumParticle.SPELL_MOB, this.locX + (this.random.nextDouble() - 0.5D) * (double)this.width, this.locY + this.random.nextDouble() * (double)this.length, this.locZ + (this.random.nextDouble() - 0.5D) * (double)this.width, var5, var7, var9, new int[0]);
         }
      }

   }

   protected void B() {
      if(this.effects.isEmpty()) {
         this.bj();
         this.setInvisible(false);
      } else {
         int var1 = PotionBrewer.a(this.effects.values());
         this.datawatcher.watch(8, Byte.valueOf((byte)(PotionBrewer.b(this.effects.values())?1:0)));
         this.datawatcher.watch(7, Integer.valueOf(var1));
         this.setInvisible(this.hasEffect(MobEffectList.INVISIBILITY.id));
      }

   }

   protected void bj() {
      this.datawatcher.watch(8, Byte.valueOf((byte)0));
      this.datawatcher.watch(7, Integer.valueOf(0));
   }

   public void removeAllEffects() {
      Iterator var1 = this.effects.keySet().iterator();

      while(var1.hasNext()) {
         Integer var2 = (Integer)var1.next();
         MobEffect var3 = (MobEffect)this.effects.get(var2);
         if(!this.world.isClientSide) {
            var1.remove();
            this.b(var3);
         }
      }

   }

   public Collection<MobEffect> getEffects() {
      return this.effects.values();
   }

   public boolean hasEffect(int var1) {
      return this.effects.containsKey(Integer.valueOf(var1));
   }

   public boolean hasEffect(MobEffectList var1) {
      return this.effects.containsKey(Integer.valueOf(var1.id));
   }

   public MobEffect getEffect(MobEffectList var1) {
      return (MobEffect)this.effects.get(Integer.valueOf(var1.id));
   }

   public void addEffect(MobEffect var1) {
      if(this.d(var1)) {
         if(this.effects.containsKey(Integer.valueOf(var1.getEffectId()))) {
            ((MobEffect)this.effects.get(Integer.valueOf(var1.getEffectId()))).a(var1);
            this.a((MobEffect)this.effects.get(Integer.valueOf(var1.getEffectId())), true);
         } else {
            this.effects.put(Integer.valueOf(var1.getEffectId()), var1);
            this.a(var1);
         }

      }
   }

   public boolean d(MobEffect var1) {
      if(this.getMonsterType() == EnumMonsterType.UNDEAD) {
         int var2 = var1.getEffectId();
         if(var2 == MobEffectList.REGENERATION.id || var2 == MobEffectList.POISON.id) {
            return false;
         }
      }

      return true;
   }

   public boolean bm() {
      return this.getMonsterType() == EnumMonsterType.UNDEAD;
   }

   public void removeEffect(int var1) {
      MobEffect var2 = (MobEffect)this.effects.remove(Integer.valueOf(var1));
      if(var2 != null) {
         this.b(var2);
      }

   }

   protected void a(MobEffect var1) {
      this.updateEffects = true;
      if(!this.world.isClientSide) {
         MobEffectList.byId[var1.getEffectId()].b(this, this.getAttributeMap(), var1.getAmplifier());
      }

   }

   protected void a(MobEffect var1, boolean var2) {
      this.updateEffects = true;
      if(var2 && !this.world.isClientSide) {
         MobEffectList.byId[var1.getEffectId()].a(this, this.getAttributeMap(), var1.getAmplifier());
         MobEffectList.byId[var1.getEffectId()].b(this, this.getAttributeMap(), var1.getAmplifier());
      }

   }

   protected void b(MobEffect var1) {
      this.updateEffects = true;
      if(!this.world.isClientSide) {
         MobEffectList.byId[var1.getEffectId()].a(this, this.getAttributeMap(), var1.getAmplifier());
      }

   }

   public void heal(float var1) {
      float var2 = this.getHealth();
      if(var2 > 0.0F) {
         this.setHealth(var2 + var1);
      }

   }

   public final float getHealth() {
      return this.datawatcher.getFloat(6);
   }

   public void setHealth(float var1) {
      this.datawatcher.watch(6, Float.valueOf(MathHelper.a(var1, 0.0F, this.getMaxHealth())));
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(this.world.isClientSide) {
         return false;
      } else {
         this.ticksFarFromPlayer = 0;
         if(this.getHealth() <= 0.0F) {
            return false;
         } else if(var1.o() && this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
            return false;
         } else {
            if((var1 == DamageSource.ANVIL || var1 == DamageSource.FALLING_BLOCK) && this.getEquipment(4) != null) {
               this.getEquipment(4).damage((int)(var2 * 4.0F + this.random.nextFloat() * var2 * 2.0F), this);
               var2 *= 0.75F;
            }

            this.aB = 1.5F;
            boolean var3 = true;
            if((float)this.noDamageTicks > (float)this.maxNoDamageTicks / 2.0F) {
               if(var2 <= this.lastDamage) {
                  return false;
               }

               this.d(var1, var2 - this.lastDamage);
               this.lastDamage = var2;
               var3 = false;
            } else {
               this.lastDamage = var2;
               this.noDamageTicks = this.maxNoDamageTicks;
               this.d(var1, var2);
               this.hurtTicks = this.av = 10;
            }

            this.aw = 0.0F;
            Entity var4 = var1.getEntity();
            if(var4 != null) {
               if(var4 instanceof EntityLiving) {
                  this.b((EntityLiving)var4);
               }

               if(var4 instanceof EntityHuman) {
                  this.lastDamageByPlayerTime = 100;
                  this.killer = (EntityHuman)var4;
               } else if(var4 instanceof EntityWolf) {
                  EntityWolf var5 = (EntityWolf)var4;
                  if(var5.isTamed()) {
                     this.lastDamageByPlayerTime = 100;
                     this.killer = null;
                  }
               }
            }

            if(var3) {
               this.world.broadcastEntityEffect(this, (byte)2);
               if(var1 != DamageSource.DROWN) {
                  this.ac();
               }

               if(var4 != null) {
                  double var9 = var4.locX - this.locX;

                  double var7;
                  for(var7 = var4.locZ - this.locZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math.random() - Math.random()) * 0.01D) {
                     var9 = (Math.random() - Math.random()) * 0.01D;
                  }

                  this.aw = (float)(MathHelper.b(var7, var9) * 180.0D / 3.1415927410125732D - (double)this.yaw);
                  this.a(var4, var2, var9, var7);
               } else {
                  this.aw = (float)((int)(Math.random() * 2.0D) * 180);
               }
            }

            String var10;
            if(this.getHealth() <= 0.0F) {
               var10 = this.bp();
               if(var3 && var10 != null) {
                  this.makeSound(var10, this.bB(), this.bC());
               }

               this.die(var1);
            } else {
               var10 = this.bo();
               if(var3 && var10 != null) {
                  this.makeSound(var10, this.bB(), this.bC());
               }
            }

            return true;
         }
      }
   }

   public void b(ItemStack var1) {
      this.makeSound("random.break", 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);

      for(int var2 = 0; var2 < 5; ++var2) {
         Vec3D var3 = new Vec3D(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
         var3 = var3.a(-this.pitch * 3.1415927F / 180.0F);
         var3 = var3.b(-this.yaw * 3.1415927F / 180.0F);
         double var4 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
         Vec3D var6 = new Vec3D(((double)this.random.nextFloat() - 0.5D) * 0.3D, var4, 0.6D);
         var6 = var6.a(-this.pitch * 3.1415927F / 180.0F);
         var6 = var6.b(-this.yaw * 3.1415927F / 180.0F);
         var6 = var6.add(this.locX, this.locY + (double)this.getHeadHeight(), this.locZ);
         this.world.addParticle(EnumParticle.ITEM_CRACK, var6.a, var6.b, var6.c, var3.a, var3.b + 0.05D, var3.c, new int[]{Item.getId(var1.getItem())});
      }

   }

   public void die(DamageSource var1) {
      Entity var2 = var1.getEntity();
      EntityLiving var3 = this.bt();
      if(this.aW >= 0 && var3 != null) {
         var3.b(this, this.aW);
      }

      if(var2 != null) {
         var2.a(this);
      }

      this.aP = true;
      this.bs().g();
      if(!this.world.isClientSide) {
         int var4 = 0;
         if(var2 instanceof EntityHuman) {
            var4 = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving)var2);
         }

         if(this.ba() && this.world.getGameRules().getBoolean("doMobLoot")) {
            this.dropDeathLoot(this.lastDamageByPlayerTime > 0, var4);
            this.dropEquipment(this.lastDamageByPlayerTime > 0, var4);
            if(this.lastDamageByPlayerTime > 0 && this.random.nextFloat() < 0.025F + (float)var4 * 0.01F) {
               this.getRareDrop();
            }
         }
      }

      this.world.broadcastEntityEffect(this, (byte)3);
   }

   protected void dropEquipment(boolean var1, int var2) {
   }

   public void a(Entity var1, float var2, double var3, double var5) {
      if(this.random.nextDouble() >= this.getAttributeInstance(GenericAttributes.c).getValue()) {
         this.ai = true;
         float var7 = MathHelper.sqrt(var3 * var3 + var5 * var5);
         float var8 = 0.4F;
         this.motX /= 2.0D;
         this.motY /= 2.0D;
         this.motZ /= 2.0D;
         this.motX -= var3 / (double)var7 * (double)var8;
         this.motY += (double)var8;
         this.motZ -= var5 / (double)var7 * (double)var8;
         if(this.motY > 0.4000000059604645D) {
            this.motY = 0.4000000059604645D;
         }

      }
   }

   protected String bo() {
      return "game.neutral.hurt";
   }

   protected String bp() {
      return "game.neutral.die";
   }

   protected void getRareDrop() {
   }

   protected void dropDeathLoot(boolean var1, int var2) {
   }

   public boolean k_() {
      int var1 = MathHelper.floor(this.locX);
      int var2 = MathHelper.floor(this.getBoundingBox().b);
      int var3 = MathHelper.floor(this.locZ);
      Block var4 = this.world.getType(new BlockPosition(var1, var2, var3)).getBlock();
      return (var4 == Blocks.LADDER || var4 == Blocks.VINE) && (!(this instanceof EntityHuman) || !((EntityHuman)this).isSpectator());
   }

   public boolean isAlive() {
      return !this.dead && this.getHealth() > 0.0F;
   }

   public void e(float var1, float var2) {
      super.e(var1, var2);
      MobEffect var3 = this.getEffect(MobEffectList.JUMP);
      float var4 = var3 != null?(float)(var3.getAmplifier() + 1):0.0F;
      int var5 = MathHelper.f((var1 - 3.0F - var4) * var2);
      if(var5 > 0) {
         this.makeSound(this.n(var5), 1.0F, 1.0F);
         this.damageEntity(DamageSource.FALL, (float)var5);
         int var6 = MathHelper.floor(this.locX);
         int var7 = MathHelper.floor(this.locY - 0.20000000298023224D);
         int var8 = MathHelper.floor(this.locZ);
         Block var9 = this.world.getType(new BlockPosition(var6, var7, var8)).getBlock();
         if(var9.getMaterial() != Material.AIR) {
            Block.StepSound var10 = var9.stepSound;
            this.makeSound(var10.getStepSound(), var10.getVolume1() * 0.5F, var10.getVolume2() * 0.75F);
         }
      }

   }

   protected String n(int var1) {
      return var1 > 4?"game.neutral.hurt.fall.big":"game.neutral.hurt.fall.small";
   }

   public int br() {
      int var1 = 0;
      ItemStack[] var2 = this.getEquipment();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         if(var5 != null && var5.getItem() instanceof ItemArmor) {
            int var6 = ((ItemArmor)var5.getItem()).c;
            var1 += var6;
         }
      }

      return var1;
   }

   protected void damageArmor(float var1) {
   }

   protected float applyArmorModifier(DamageSource var1, float var2) {
      if(!var1.ignoresArmor()) {
         int var3 = 25 - this.br();
         float var4 = var2 * (float)var3;
         this.damageArmor(var2);
         var2 = var4 / 25.0F;
      }

      return var2;
   }

   protected float applyMagicModifier(DamageSource var1, float var2) {
      if(var1.isStarvation()) {
         return var2;
      } else {
         int var3;
         int var4;
         float var5;
         if(this.hasEffect(MobEffectList.RESISTANCE) && var1 != DamageSource.OUT_OF_WORLD) {
            var3 = (this.getEffect(MobEffectList.RESISTANCE).getAmplifier() + 1) * 5;
            var4 = 25 - var3;
            var5 = var2 * (float)var4;
            var2 = var5 / 25.0F;
         }

         if(var2 <= 0.0F) {
            return 0.0F;
         } else {
            var3 = EnchantmentManager.a(this.getEquipment(), var1);
            if(var3 > 20) {
               var3 = 20;
            }

            if(var3 > 0 && var3 <= 20) {
               var4 = 25 - var3;
               var5 = var2 * (float)var4;
               var2 = var5 / 25.0F;
            }

            return var2;
         }
      }
   }

   protected void d(DamageSource var1, float var2) {
      if(!this.isInvulnerable(var1)) {
         var2 = this.applyArmorModifier(var1, var2);
         var2 = this.applyMagicModifier(var1, var2);
         float var3 = var2;
         var2 = Math.max(var2 - this.getAbsorptionHearts(), 0.0F);
         this.setAbsorptionHearts(this.getAbsorptionHearts() - (var3 - var2));
         if(var2 != 0.0F) {
            float var4 = this.getHealth();
            this.setHealth(var4 - var2);
            this.bs().a(var1, var4, var2);
            this.setAbsorptionHearts(this.getAbsorptionHearts() - var2);
         }
      }
   }

   public CombatTracker bs() {
      return this.combatTracker;
   }

   public EntityLiving bt() {
      return (EntityLiving)(this.combatTracker.c() != null?this.combatTracker.c():(this.killer != null?this.killer:(this.lastDamager != null?this.lastDamager:null)));
   }

   public final float getMaxHealth() {
      return (float)this.getAttributeInstance(GenericAttributes.maxHealth).getValue();
   }

   public final int bv() {
      return this.datawatcher.getByte(9);
   }

   public final void o(int var1) {
      this.datawatcher.watch(9, Byte.valueOf((byte)var1));
   }

   private int n() {
      return this.hasEffect(MobEffectList.FASTER_DIG)?6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1:(this.hasEffect(MobEffectList.SLOWER_DIG)?6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2:6);
   }

   public void bw() {
      if(!this.ar || this.as >= this.n() / 2 || this.as < 0) {
         this.as = -1;
         this.ar = true;
         if(this.world instanceof WorldServer) {
            ((WorldServer)this.world).getTracker().a((Entity)this, (Packet)(new PacketPlayOutAnimation(this, 0)));
         }
      }

   }

   protected void O() {
      this.damageEntity(DamageSource.OUT_OF_WORLD, 4.0F);
   }

   protected void bx() {
      int var1 = this.n();
      if(this.ar) {
         ++this.as;
         if(this.as >= var1) {
            this.as = 0;
            this.ar = false;
         }
      } else {
         this.as = 0;
      }

      this.az = (float)this.as / (float)var1;
   }

   public AttributeInstance getAttributeInstance(IAttribute var1) {
      return this.getAttributeMap().a(var1);
   }

   public AttributeMapBase getAttributeMap() {
      if(this.c == null) {
         this.c = new AttributeMapServer();
      }

      return this.c;
   }

   public EnumMonsterType getMonsterType() {
      return EnumMonsterType.UNDEFINED;
   }

   public abstract ItemStack bA();

   public abstract ItemStack getEquipment(int var1);

   public abstract void setEquipment(int var1, ItemStack var2);

   public void setSprinting(boolean var1) {
      super.setSprinting(var1);
      AttributeInstance var2 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
      if(var2.a(a) != null) {
         var2.c(b);
      }

      if(var1) {
         var2.b(b);
      }

   }

   public abstract ItemStack[] getEquipment();

   protected float bB() {
      return 1.0F;
   }

   protected float bC() {
      return this.isBaby()?(this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F:(this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
   }

   protected boolean bD() {
      return this.getHealth() <= 0.0F;
   }

   public void q(Entity var1) {
      double var3 = var1.locX;
      double var5 = var1.getBoundingBox().b + (double)var1.length;
      double var7 = var1.locZ;
      byte var9 = 1;

      for(int var10 = -var9; var10 <= var9; ++var10) {
         for(int var11 = -var9; var11 < var9; ++var11) {
            if(var10 != 0 || var11 != 0) {
               int var12 = (int)(this.locX + (double)var10);
               int var13 = (int)(this.locZ + (double)var11);
               AxisAlignedBB var2 = this.getBoundingBox().c((double)var10, 1.0D, (double)var11);
               if(this.world.a(var2).isEmpty()) {
                  if(World.a((IBlockAccess)this.world, (BlockPosition)(new BlockPosition(var12, (int)this.locY, var13)))) {
                     this.enderTeleportTo(this.locX + (double)var10, this.locY + 1.0D, this.locZ + (double)var11);
                     return;
                  }

                  if(World.a((IBlockAccess)this.world, (BlockPosition)(new BlockPosition(var12, (int)this.locY - 1, var13))) || this.world.getType(new BlockPosition(var12, (int)this.locY - 1, var13)).getBlock().getMaterial() == Material.WATER) {
                     var3 = this.locX + (double)var10;
                     var5 = this.locY + 1.0D;
                     var7 = this.locZ + (double)var11;
                  }
               }
            }
         }
      }

      this.enderTeleportTo(var3, var5, var7);
   }

   protected float bE() {
      return 0.42F;
   }

   protected void bF() {
      this.motY = (double)this.bE();
      if(this.hasEffect(MobEffectList.JUMP)) {
         this.motY += (double)((float)(this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
      }

      if(this.isSprinting()) {
         float var1 = this.yaw * 0.017453292F;
         this.motX -= (double)(MathHelper.sin(var1) * 0.2F);
         this.motZ += (double)(MathHelper.cos(var1) * 0.2F);
      }

      this.ai = true;
   }

   protected void bG() {
      this.motY += 0.03999999910593033D;
   }

   protected void bH() {
      this.motY += 0.03999999910593033D;
   }

   public void g(float var1, float var2) {
      double var8;
      float var10;
      if(this.bM()) {
         float var5;
         float var6;
         if(this.V() && (!(this instanceof EntityHuman) || !((EntityHuman)this).abilities.isFlying)) {
            var8 = this.locY;
            var5 = 0.8F;
            var6 = 0.02F;
            var10 = (float)EnchantmentManager.b(this);
            if(var10 > 3.0F) {
               var10 = 3.0F;
            }

            if(!this.onGround) {
               var10 *= 0.5F;
            }

            if(var10 > 0.0F) {
               var5 += (0.54600006F - var5) * var10 / 3.0F;
               var6 += (this.bI() * 1.0F - var6) * var10 / 3.0F;
            }

            this.a(var1, var2, var6);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= (double)var5;
            this.motY *= 0.800000011920929D;
            this.motZ *= (double)var5;
            this.motY -= 0.02D;
            if(this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + var8, this.motZ)) {
               this.motY = 0.30000001192092896D;
            }
         } else if(this.ab() && (!(this instanceof EntityHuman) || !((EntityHuman)this).abilities.isFlying)) {
            var8 = this.locY;
            this.a(var1, var2, 0.02F);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
            this.motY -= 0.02D;
            if(this.positionChanged && this.c(this.motX, this.motY + 0.6000000238418579D - this.locY + var8, this.motZ)) {
               this.motY = 0.30000001192092896D;
            }
         } else {
            float var3 = 0.91F;
            if(this.onGround) {
               var3 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
            }

            float var4 = 0.16277136F / (var3 * var3 * var3);
            if(this.onGround) {
               var5 = this.bI() * var4;
            } else {
               var5 = this.aM;
            }

            this.a(var1, var2, var5);
            var3 = 0.91F;
            if(this.onGround) {
               var3 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
            }

            if(this.k_()) {
               var6 = 0.15F;
               this.motX = MathHelper.a(this.motX, (double)(-var6), (double)var6);
               this.motZ = MathHelper.a(this.motZ, (double)(-var6), (double)var6);
               this.fallDistance = 0.0F;
               if(this.motY < -0.15D) {
                  this.motY = -0.15D;
               }

               boolean var7 = this.isSneaking() && this instanceof EntityHuman;
               if(var7 && this.motY < 0.0D) {
                  this.motY = 0.0D;
               }
            }

            this.move(this.motX, this.motY, this.motZ);
            if(this.positionChanged && this.k_()) {
               this.motY = 0.2D;
            }

            if(!this.world.isClientSide || this.world.isLoaded(new BlockPosition((int)this.locX, 0, (int)this.locZ)) && this.world.getChunkAtWorldCoords(new BlockPosition((int)this.locX, 0, (int)this.locZ)).o()) {
               this.motY -= 0.08D;
            } else if(this.locY > 0.0D) {
               this.motY = -0.1D;
            } else {
               this.motY = 0.0D;
            }

            this.motY *= 0.9800000190734863D;
            this.motX *= (double)var3;
            this.motZ *= (double)var3;
         }
      }

      this.aA = this.aB;
      var8 = this.locX - this.lastX;
      double var9 = this.locZ - this.lastZ;
      var10 = MathHelper.sqrt(var8 * var8 + var9 * var9) * 4.0F;
      if(var10 > 1.0F) {
         var10 = 1.0F;
      }

      this.aB += (var10 - this.aB) * 0.4F;
      this.aC += this.aB;
   }

   public float bI() {
      return this.bm;
   }

   public void k(float var1) {
      this.bm = var1;
   }

   public boolean r(Entity var1) {
      this.p(var1);
      return false;
   }

   public boolean isSleeping() {
      return false;
   }

   public void t_() {
      super.t_();
      if(!this.world.isClientSide) {
         int var1 = this.bv();
         if(var1 > 0) {
            if(this.at <= 0) {
               this.at = 20 * (30 - var1);
            }

            --this.at;
            if(this.at <= 0) {
               this.o(var1 - 1);
            }
         }

         for(int var2 = 0; var2 < 5; ++var2) {
            ItemStack var3 = this.h[var2];
            ItemStack var4 = this.getEquipment(var2);
            if(!ItemStack.matches(var4, var3)) {
               ((WorldServer)this.world).getTracker().a((Entity)this, (Packet)(new PacketPlayOutEntityEquipment(this.getId(), var2, var4)));
               if(var3 != null) {
                  this.c.a(var3.B());
               }

               if(var4 != null) {
                  this.c.b(var4.B());
               }

               this.h[var2] = var4 == null?null:var4.cloneItemStack();
            }
         }

         if(this.ticksLived % 20 == 0) {
            this.bs().g();
         }
      }

      this.m();
      double var9 = this.locX - this.lastX;
      double var10 = this.locZ - this.lastZ;
      float var5 = (float)(var9 * var9 + var10 * var10);
      float var6 = this.aI;
      float var7 = 0.0F;
      this.aR = this.aS;
      float var8 = 0.0F;
      if(var5 > 0.0025000002F) {
         var8 = 1.0F;
         var7 = (float)Math.sqrt((double)var5) * 3.0F;
         var6 = (float)MathHelper.b(var10, var9) * 180.0F / 3.1415927F - 90.0F;
      }

      if(this.az > 0.0F) {
         var6 = this.yaw;
      }

      if(!this.onGround) {
         var8 = 0.0F;
      }

      this.aS += (var8 - this.aS) * 0.3F;
      this.world.methodProfiler.a("headTurn");
      var7 = this.h(var6, var7);
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("rangeChecks");

      while(this.yaw - this.lastYaw < -180.0F) {
         this.lastYaw -= 360.0F;
      }

      while(this.yaw - this.lastYaw >= 180.0F) {
         this.lastYaw += 360.0F;
      }

      while(this.aI - this.aJ < -180.0F) {
         this.aJ -= 360.0F;
      }

      while(this.aI - this.aJ >= 180.0F) {
         this.aJ += 360.0F;
      }

      while(this.pitch - this.lastPitch < -180.0F) {
         this.lastPitch -= 360.0F;
      }

      while(this.pitch - this.lastPitch >= 180.0F) {
         this.lastPitch += 360.0F;
      }

      while(this.aK - this.aL < -180.0F) {
         this.aL -= 360.0F;
      }

      while(this.aK - this.aL >= 180.0F) {
         this.aL += 360.0F;
      }

      this.world.methodProfiler.b();
      this.aT += var7;
   }

   protected float h(float var1, float var2) {
      float var3 = MathHelper.g(var1 - this.aI);
      this.aI += var3 * 0.3F;
      float var4 = MathHelper.g(this.yaw - this.aI);
      boolean var5 = var4 < -90.0F || var4 >= 90.0F;
      if(var4 < -75.0F) {
         var4 = -75.0F;
      }

      if(var4 >= 75.0F) {
         var4 = 75.0F;
      }

      this.aI = this.yaw - var4;
      if(var4 * var4 > 2500.0F) {
         this.aI += var4 * 0.2F;
      }

      if(var5) {
         var2 *= -1.0F;
      }

      return var2;
   }

   public void m() {
      if(this.bn > 0) {
         --this.bn;
      }

      if(this.bc > 0) {
         double var1 = this.locX + (this.bd - this.locX) / (double)this.bc;
         double var3 = this.locY + (this.be - this.locY) / (double)this.bc;
         double var5 = this.locZ + (this.bf - this.locZ) / (double)this.bc;
         double var7 = MathHelper.g(this.bg - (double)this.yaw);
         this.yaw = (float)((double)this.yaw + var7 / (double)this.bc);
         this.pitch = (float)((double)this.pitch + (this.bh - (double)this.pitch) / (double)this.bc);
         --this.bc;
         this.setPosition(var1, var3, var5);
         this.setYawPitch(this.yaw, this.pitch);
      } else if(!this.bM()) {
         this.motX *= 0.98D;
         this.motY *= 0.98D;
         this.motZ *= 0.98D;
      }

      if(Math.abs(this.motX) < 0.005D) {
         this.motX = 0.0D;
      }

      if(Math.abs(this.motY) < 0.005D) {
         this.motY = 0.0D;
      }

      if(Math.abs(this.motZ) < 0.005D) {
         this.motZ = 0.0D;
      }

      this.world.methodProfiler.a("ai");
      if(this.bD()) {
         this.aY = false;
         this.aZ = 0.0F;
         this.ba = 0.0F;
         this.bb = 0.0F;
      } else if(this.bM()) {
         this.world.methodProfiler.a("newAi");
         this.doTick();
         this.world.methodProfiler.b();
      }

      this.world.methodProfiler.b();
      this.world.methodProfiler.a("jump");
      if(this.aY) {
         if(this.V()) {
            this.bG();
         } else if(this.ab()) {
            this.bH();
         } else if(this.onGround && this.bn == 0) {
            this.bF();
            this.bn = 10;
         }
      } else {
         this.bn = 0;
      }

      this.world.methodProfiler.b();
      this.world.methodProfiler.a("travel");
      this.aZ *= 0.98F;
      this.ba *= 0.98F;
      this.bb *= 0.9F;
      this.g(this.aZ, this.ba);
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("push");
      if(!this.world.isClientSide) {
         this.bL();
      }

      this.world.methodProfiler.b();
   }

   protected void doTick() {
   }

   protected void bL() {
      List var1 = this.world.a((Entity)this, (AxisAlignedBB)this.getBoundingBox().grow(0.20000000298023224D, 0.0D, 0.20000000298023224D), (Predicate)Predicates.and(IEntitySelector.d, new Predicate() {
         public boolean a(Entity var1) {
            return var1.ae();
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((Entity)var1);
         }
      }));
      if(!var1.isEmpty()) {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Entity var3 = (Entity)var1.get(var2);
            this.s(var3);
         }
      }

   }

   protected void s(Entity var1) {
      var1.collide(this);
   }

   public void mount(Entity var1) {
      if(this.vehicle != null && var1 == null) {
         if(!this.world.isClientSide) {
            this.q(this.vehicle);
         }

         if(this.vehicle != null) {
            this.vehicle.passenger = null;
         }

         this.vehicle = null;
      } else {
         super.mount(var1);
      }
   }

   public void ak() {
      super.ak();
      this.aR = this.aS;
      this.aS = 0.0F;
      this.fallDistance = 0.0F;
   }

   public void i(boolean var1) {
      this.aY = var1;
   }

   public void receive(Entity var1, int var2) {
      if(!var1.dead && !this.world.isClientSide) {
         EntityTracker var3 = ((WorldServer)this.world).getTracker();
         if(var1 instanceof EntityItem) {
            var3.a((Entity)var1, (Packet)(new PacketPlayOutCollect(var1.getId(), this.getId())));
         }

         if(var1 instanceof EntityArrow) {
            var3.a((Entity)var1, (Packet)(new PacketPlayOutCollect(var1.getId(), this.getId())));
         }

         if(var1 instanceof EntityExperienceOrb) {
            var3.a((Entity)var1, (Packet)(new PacketPlayOutCollect(var1.getId(), this.getId())));
         }
      }

   }

   public boolean hasLineOfSight(Entity var1) {
      return this.world.rayTrace(new Vec3D(this.locX, this.locY + (double)this.getHeadHeight(), this.locZ), new Vec3D(var1.locX, var1.locY + (double)var1.getHeadHeight(), var1.locZ)) == null;
   }

   public Vec3D ap() {
      return this.d(1.0F);
   }

   public Vec3D d(float var1) {
      if(var1 == 1.0F) {
         return this.f(this.pitch, this.aK);
      } else {
         float var2 = this.lastPitch + (this.pitch - this.lastPitch) * var1;
         float var3 = this.aL + (this.aK - this.aL) * var1;
         return this.f(var2, var3);
      }
   }

   public boolean bM() {
      return !this.world.isClientSide;
   }

   public boolean ad() {
      return !this.dead;
   }

   public boolean ae() {
      return !this.dead;
   }

   protected void ac() {
      this.velocityChanged = this.random.nextDouble() >= this.getAttributeInstance(GenericAttributes.c).getValue();
   }

   public float getHeadRotation() {
      return this.aK;
   }

   public void f(float var1) {
      this.aK = var1;
   }

   public void g(float var1) {
      this.aI = var1;
   }

   public float getAbsorptionHearts() {
      return this.bo;
   }

   public void setAbsorptionHearts(float var1) {
      if(var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.bo = var1;
   }

   public ScoreboardTeamBase getScoreboardTeam() {
      return this.world.getScoreboard().getPlayerTeam(this.getUniqueID().toString());
   }

   public boolean c(EntityLiving var1) {
      return this.a(var1.getScoreboardTeam());
   }

   public boolean a(ScoreboardTeamBase var1) {
      return this.getScoreboardTeam() != null?this.getScoreboardTeam().isAlly(var1):false;
   }

   public void enterCombat() {
   }

   public void exitCombat() {
   }

   protected void bP() {
      this.updateEffects = true;
   }

   static {
      b = (new AttributeModifier(a, "Sprinting speed boost", 0.30000001192092896D, 2)).a(false);
   }
}
