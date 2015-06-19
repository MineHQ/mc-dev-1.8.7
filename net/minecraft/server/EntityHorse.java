package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeRanged;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IAttribute;
import net.minecraft.server.IInventoryListener;
import net.minecraft.server.InventoryHorseChest;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NameReferencingFileConverter;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTame;
import net.minecraft.server.World;

public class EntityHorse extends EntityAnimal implements IInventoryListener {
   private static final Predicate<Entity> bs = new Predicate() {
      public boolean a(Entity var1) {
         return var1 instanceof EntityHorse && ((EntityHorse)var1).cA();
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((Entity)var1);
      }
   };
   public static final IAttribute attributeJumpStrength = (new AttributeRanged((IAttribute)null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).a("Jump Strength").a(true);
   private static final String[] bu = new String[]{null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png"};
   private static final String[] bv = new String[]{"", "meo", "goo", "dio"};
   private static final int[] bw = new int[]{0, 5, 7, 11};
   private static final String[] bx = new String[]{"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
   private static final String[] by = new String[]{"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
   private static final String[] bz = new String[]{null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
   private static final String[] bA = new String[]{"", "wo_", "wmo", "wdo", "bdo"};
   private int bB;
   private int bC;
   private int bD;
   public int bm;
   public int bo;
   protected boolean bp;
   public InventoryHorseChest inventoryChest;
   private boolean bF;
   protected int bq;
   protected float br;
   private boolean bG;
   private float bH;
   private float bI;
   private float bJ;
   private float bK;
   private float bL;
   private float bM;
   private int bN;
   private String bO;
   private String[] bP = new String[3];
   private boolean bQ = false;

   public EntityHorse(World var1) {
      super(var1);
      this.setSize(1.4F, 1.6F);
      this.fireProof = false;
      this.setHasChest(false);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D));
      this.goalSelector.a(1, new PathfinderGoalTame(this, 1.2D));
      this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D));
      this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.7D));
      this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.loadChest();
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Integer.valueOf(0));
      this.datawatcher.a(19, Byte.valueOf((byte)0));
      this.datawatcher.a(20, Integer.valueOf(0));
      this.datawatcher.a(21, String.valueOf(""));
      this.datawatcher.a(22, Integer.valueOf(0));
   }

   public void setType(int var1) {
      this.datawatcher.watch(19, Byte.valueOf((byte)var1));
      this.dc();
   }

   public int getType() {
      return this.datawatcher.getByte(19);
   }

   public void setVariant(int var1) {
      this.datawatcher.watch(20, Integer.valueOf(var1));
      this.dc();
   }

   public int getVariant() {
      return this.datawatcher.getInt(20);
   }

   public String getName() {
      if(this.hasCustomName()) {
         return this.getCustomName();
      } else {
         int var1 = this.getType();
         switch(var1) {
         case 0:
         default:
            return LocaleI18n.get("entity.horse.name");
         case 1:
            return LocaleI18n.get("entity.donkey.name");
         case 2:
            return LocaleI18n.get("entity.mule.name");
         case 3:
            return LocaleI18n.get("entity.zombiehorse.name");
         case 4:
            return LocaleI18n.get("entity.skeletonhorse.name");
         }
      }
   }

   private boolean w(int var1) {
      return (this.datawatcher.getInt(16) & var1) != 0;
   }

   private void c(int var1, boolean var2) {
      int var3 = this.datawatcher.getInt(16);
      if(var2) {
         this.datawatcher.watch(16, Integer.valueOf(var3 | var1));
      } else {
         this.datawatcher.watch(16, Integer.valueOf(var3 & ~var1));
      }

   }

   public boolean cn() {
      return !this.isBaby();
   }

   public boolean isTame() {
      return this.w(2);
   }

   public boolean cp() {
      return this.cn();
   }

   public String getOwnerUUID() {
      return this.datawatcher.getString(21);
   }

   public void setOwnerUUID(String var1) {
      this.datawatcher.watch(21, var1);
   }

   public float cu() {
      return 0.5F;
   }

   public void a(boolean var1) {
      if(var1) {
         this.a(this.cu());
      } else {
         this.a(1.0F);
      }

   }

   public boolean cv() {
      return this.bp;
   }

   public void setTame(boolean var1) {
      this.c(2, var1);
   }

   public void m(boolean var1) {
      this.bp = var1;
   }

   public boolean cb() {
      return !this.cR() && super.cb();
   }

   protected void o(float var1) {
      if(var1 > 6.0F && this.cy()) {
         this.r(false);
      }

   }

   public boolean hasChest() {
      return this.w(8);
   }

   public int cx() {
      return this.datawatcher.getInt(22);
   }

   private int f(ItemStack var1) {
      if(var1 == null) {
         return 0;
      } else {
         Item var2 = var1.getItem();
         return var2 == Items.IRON_HORSE_ARMOR?1:(var2 == Items.GOLDEN_HORSE_ARMOR?2:(var2 == Items.DIAMOND_HORSE_ARMOR?3:0));
      }
   }

   public boolean cy() {
      return this.w(32);
   }

   public boolean cz() {
      return this.w(64);
   }

   public boolean cA() {
      return this.w(16);
   }

   public boolean cB() {
      return this.bF;
   }

   public void e(ItemStack var1) {
      this.datawatcher.watch(22, Integer.valueOf(this.f(var1)));
      this.dc();
   }

   public void n(boolean var1) {
      this.c(16, var1);
   }

   public void setHasChest(boolean var1) {
      this.c(8, var1);
   }

   public void p(boolean var1) {
      this.bF = var1;
   }

   public void q(boolean var1) {
      this.c(4, var1);
   }

   public int getTemper() {
      return this.bq;
   }

   public void setTemper(int var1) {
      this.bq = var1;
   }

   public int u(int var1) {
      int var2 = MathHelper.clamp(this.getTemper() + var1, 0, this.getMaxDomestication());
      this.setTemper(var2);
      return var2;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      Entity var3 = var1.getEntity();
      return this.passenger != null && this.passenger.equals(var3)?false:super.damageEntity(var1, var2);
   }

   public int br() {
      return bw[this.cx()];
   }

   public boolean ae() {
      return this.passenger == null;
   }

   public boolean cD() {
      int var1 = MathHelper.floor(this.locX);
      int var2 = MathHelper.floor(this.locZ);
      this.world.getBiome(new BlockPosition(var1, 0, var2));
      return true;
   }

   public void cE() {
      if(!this.world.isClientSide && this.hasChest()) {
         this.a(Item.getItemOf(Blocks.CHEST), 1);
         this.setHasChest(false);
      }
   }

   private void cY() {
      this.df();
      if(!this.R()) {
         this.world.makeSound(this, "eating", 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
      }

   }

   public void e(float var1, float var2) {
      if(var1 > 1.0F) {
         this.makeSound("mob.horse.land", 0.4F, 1.0F);
      }

      int var3 = MathHelper.f((var1 * 0.5F - 3.0F) * var2);
      if(var3 > 0) {
         this.damageEntity(DamageSource.FALL, (float)var3);
         if(this.passenger != null) {
            this.passenger.damageEntity(DamageSource.FALL, (float)var3);
         }

         Block var4 = this.world.getType(new BlockPosition(this.locX, this.locY - 0.2D - (double)this.lastYaw, this.locZ)).getBlock();
         if(var4.getMaterial() != Material.AIR && !this.R()) {
            Block.StepSound var5 = var4.stepSound;
            this.world.makeSound(this, var5.getStepSound(), var5.getVolume1() * 0.5F, var5.getVolume2() * 0.75F);
         }

      }
   }

   private int cZ() {
      int var1 = this.getType();
      return !this.hasChest() || var1 != 1 && var1 != 2?2:17;
   }

   public void loadChest() {
      InventoryHorseChest var1 = this.inventoryChest;
      this.inventoryChest = new InventoryHorseChest("HorseChest", this.cZ());
      this.inventoryChest.a(this.getName());
      if(var1 != null) {
         var1.b(this);
         int var2 = Math.min(var1.getSize(), this.inventoryChest.getSize());

         for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack var4 = var1.getItem(var3);
            if(var4 != null) {
               this.inventoryChest.setItem(var3, var4.cloneItemStack());
            }
         }
      }

      this.inventoryChest.a(this);
      this.db();
   }

   private void db() {
      if(!this.world.isClientSide) {
         this.q(this.inventoryChest.getItem(0) != null);
         if(this.cO()) {
            this.e(this.inventoryChest.getItem(1));
         }
      }

   }

   public void a(InventorySubcontainer var1) {
      int var2 = this.cx();
      boolean var3 = this.cG();
      this.db();
      if(this.ticksLived > 20) {
         if(var2 == 0 && var2 != this.cx()) {
            this.makeSound("mob.horse.armor", 0.5F, 1.0F);
         } else if(var2 != this.cx()) {
            this.makeSound("mob.horse.armor", 0.5F, 1.0F);
         }

         if(!var3 && this.cG()) {
            this.makeSound("mob.horse.leather", 0.5F, 1.0F);
         }
      }

   }

   public boolean bR() {
      this.cD();
      return super.bR();
   }

   protected EntityHorse a(Entity var1, double var2) {
      double var4 = Double.MAX_VALUE;
      Entity var6 = null;
      List var7 = this.world.a(var1, var1.getBoundingBox().a(var2, var2, var2), bs);
      Iterator var8 = var7.iterator();

      while(var8.hasNext()) {
         Entity var9 = (Entity)var8.next();
         double var10 = var9.e(var1.locX, var1.locY, var1.locZ);
         if(var10 < var4) {
            var6 = var9;
            var4 = var10;
         }
      }

      return (EntityHorse)var6;
   }

   public double getJumpStrength() {
      return this.getAttributeInstance(attributeJumpStrength).getValue();
   }

   protected String bp() {
      this.df();
      int var1 = this.getType();
      return var1 == 3?"mob.horse.zombie.death":(var1 == 4?"mob.horse.skeleton.death":(var1 != 1 && var1 != 2?"mob.horse.death":"mob.horse.donkey.death"));
   }

   protected Item getLoot() {
      boolean var1 = this.random.nextInt(4) == 0;
      int var2 = this.getType();
      return var2 == 4?Items.BONE:(var2 == 3?(var1?null:Items.ROTTEN_FLESH):Items.LEATHER);
   }

   protected String bo() {
      this.df();
      if(this.random.nextInt(3) == 0) {
         this.dh();
      }

      int var1 = this.getType();
      return var1 == 3?"mob.horse.zombie.hit":(var1 == 4?"mob.horse.skeleton.hit":(var1 != 1 && var1 != 2?"mob.horse.hit":"mob.horse.donkey.hit"));
   }

   public boolean cG() {
      return this.w(4);
   }

   protected String z() {
      this.df();
      if(this.random.nextInt(10) == 0 && !this.bD()) {
         this.dh();
      }

      int var1 = this.getType();
      return var1 == 3?"mob.horse.zombie.idle":(var1 == 4?"mob.horse.skeleton.idle":(var1 != 1 && var1 != 2?"mob.horse.idle":"mob.horse.donkey.idle"));
   }

   protected String cH() {
      this.df();
      this.dh();
      int var1 = this.getType();
      return var1 != 3 && var1 != 4?(var1 != 1 && var1 != 2?"mob.horse.angry":"mob.horse.donkey.angry"):null;
   }

   protected void a(BlockPosition var1, Block var2) {
      Block.StepSound var3 = var2.stepSound;
      if(this.world.getType(var1.up()).getBlock() == Blocks.SNOW_LAYER) {
         var3 = Blocks.SNOW_LAYER.stepSound;
      }

      if(!var2.getMaterial().isLiquid()) {
         int var4 = this.getType();
         if(this.passenger != null && var4 != 1 && var4 != 2) {
            ++this.bN;
            if(this.bN > 5 && this.bN % 3 == 0) {
               this.makeSound("mob.horse.gallop", var3.getVolume1() * 0.15F, var3.getVolume2());
               if(var4 == 0 && this.random.nextInt(10) == 0) {
                  this.makeSound("mob.horse.breathe", var3.getVolume1() * 0.6F, var3.getVolume2());
               }
            } else if(this.bN <= 5) {
               this.makeSound("mob.horse.wood", var3.getVolume1() * 0.15F, var3.getVolume2());
            }
         } else if(var3 == Block.f) {
            this.makeSound("mob.horse.wood", var3.getVolume1() * 0.15F, var3.getVolume2());
         } else {
            this.makeSound("mob.horse.soft", var3.getVolume1() * 0.15F, var3.getVolume2());
         }
      }

   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeMap().b(attributeJumpStrength);
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(53.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.22499999403953552D);
   }

   public int bV() {
      return 6;
   }

   public int getMaxDomestication() {
      return 100;
   }

   protected float bB() {
      return 0.8F;
   }

   public int w() {
      return 400;
   }

   private void dc() {
      this.bO = null;
   }

   public void g(EntityHuman var1) {
      if(!this.world.isClientSide && (this.passenger == null || this.passenger == var1) && this.isTame()) {
         this.inventoryChest.a(this.getName());
         var1.openHorseInventory(this, this.inventoryChest);
      }

   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.SPAWN_EGG) {
         return super.a(var1);
      } else if(!this.isTame() && this.cR()) {
         return false;
      } else if(this.isTame() && this.cn() && var1.isSneaking()) {
         this.g(var1);
         return true;
      } else if(this.cp() && this.passenger != null) {
         return super.a(var1);
      } else {
         if(var2 != null) {
            boolean var3 = false;
            if(this.cO()) {
               byte var4 = -1;
               if(var2.getItem() == Items.IRON_HORSE_ARMOR) {
                  var4 = 1;
               } else if(var2.getItem() == Items.GOLDEN_HORSE_ARMOR) {
                  var4 = 2;
               } else if(var2.getItem() == Items.DIAMOND_HORSE_ARMOR) {
                  var4 = 3;
               }

               if(var4 >= 0) {
                  if(!this.isTame()) {
                     this.cW();
                     return true;
                  }

                  this.g(var1);
                  return true;
               }
            }

            if(!var3 && !this.cR()) {
               float var7 = 0.0F;
               short var5 = 0;
               byte var6 = 0;
               if(var2.getItem() == Items.WHEAT) {
                  var7 = 2.0F;
                  var5 = 20;
                  var6 = 3;
               } else if(var2.getItem() == Items.SUGAR) {
                  var7 = 1.0F;
                  var5 = 30;
                  var6 = 3;
               } else if(Block.asBlock(var2.getItem()) == Blocks.HAY_BLOCK) {
                  var7 = 20.0F;
                  var5 = 180;
               } else if(var2.getItem() == Items.APPLE) {
                  var7 = 3.0F;
                  var5 = 60;
                  var6 = 3;
               } else if(var2.getItem() == Items.GOLDEN_CARROT) {
                  var7 = 4.0F;
                  var5 = 60;
                  var6 = 5;
                  if(this.isTame() && this.getAge() == 0) {
                     var3 = true;
                     this.c(var1);
                  }
               } else if(var2.getItem() == Items.GOLDEN_APPLE) {
                  var7 = 10.0F;
                  var5 = 240;
                  var6 = 10;
                  if(this.isTame() && this.getAge() == 0) {
                     var3 = true;
                     this.c(var1);
                  }
               }

               if(this.getHealth() < this.getMaxHealth() && var7 > 0.0F) {
                  this.heal(var7);
                  var3 = true;
               }

               if(!this.cn() && var5 > 0) {
                  this.setAge(var5);
                  var3 = true;
               }

               if(var6 > 0 && (var3 || !this.isTame()) && var6 < this.getMaxDomestication()) {
                  var3 = true;
                  this.u(var6);
               }

               if(var3) {
                  this.cY();
               }
            }

            if(!this.isTame() && !var3) {
               if(var2 != null && var2.a((EntityHuman)var1, (EntityLiving)this)) {
                  return true;
               }

               this.cW();
               return true;
            }

            if(!var3 && this.cP() && !this.hasChest() && var2.getItem() == Item.getItemOf(Blocks.CHEST)) {
               this.setHasChest(true);
               this.makeSound("mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
               var3 = true;
               this.loadChest();
            }

            if(!var3 && this.cp() && !this.cG() && var2.getItem() == Items.SADDLE) {
               this.g(var1);
               return true;
            }

            if(var3) {
               if(!var1.abilities.canInstantlyBuild && --var2.count == 0) {
                  var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
               }

               return true;
            }
         }

         if(this.cp() && this.passenger == null) {
            if(var2 != null && var2.a((EntityHuman)var1, (EntityLiving)this)) {
               return true;
            } else {
               this.i(var1);
               return true;
            }
         } else {
            return super.a(var1);
         }
      }
   }

   private void i(EntityHuman var1) {
      var1.yaw = this.yaw;
      var1.pitch = this.pitch;
      this.r(false);
      this.s(false);
      if(!this.world.isClientSide) {
         var1.mount(this);
      }

   }

   public boolean cO() {
      return this.getType() == 0;
   }

   public boolean cP() {
      int var1 = this.getType();
      return var1 == 2 || var1 == 1;
   }

   protected boolean bD() {
      return this.passenger != null && this.cG()?true:this.cy() || this.cz();
   }

   public boolean cR() {
      int var1 = this.getType();
      return var1 == 3 || var1 == 4;
   }

   public boolean cS() {
      return this.cR() || this.getType() == 2;
   }

   public boolean d(ItemStack var1) {
      return false;
   }

   private void de() {
      this.bm = 1;
   }

   public void die(DamageSource var1) {
      super.die(var1);
      if(!this.world.isClientSide) {
         this.dropChest();
      }

   }

   public void m() {
      if(this.random.nextInt(200) == 0) {
         this.de();
      }

      super.m();
      if(!this.world.isClientSide) {
         if(this.random.nextInt(900) == 0 && this.deathTicks == 0) {
            this.heal(1.0F);
         }

         if(!this.cy() && this.passenger == null && this.random.nextInt(300) == 0 && this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.locY) - 1, MathHelper.floor(this.locZ))).getBlock() == Blocks.GRASS) {
            this.r(true);
         }

         if(this.cy() && ++this.bB > 50) {
            this.bB = 0;
            this.r(false);
         }

         if(this.cA() && !this.cn() && !this.cy()) {
            EntityHorse var1 = this.a(this, 16.0D);
            if(var1 != null && this.h(var1) > 4.0D) {
               this.navigation.a((Entity)var1);
            }
         }
      }

   }

   public void t_() {
      super.t_();
      if(this.world.isClientSide && this.datawatcher.a()) {
         this.datawatcher.e();
         this.dc();
      }

      if(this.bC > 0 && ++this.bC > 30) {
         this.bC = 0;
         this.c(128, false);
      }

      if(!this.world.isClientSide && this.bD > 0 && ++this.bD > 20) {
         this.bD = 0;
         this.s(false);
      }

      if(this.bm > 0 && ++this.bm > 8) {
         this.bm = 0;
      }

      if(this.bo > 0) {
         ++this.bo;
         if(this.bo > 300) {
            this.bo = 0;
         }
      }

      this.bI = this.bH;
      if(this.cy()) {
         this.bH += (1.0F - this.bH) * 0.4F + 0.05F;
         if(this.bH > 1.0F) {
            this.bH = 1.0F;
         }
      } else {
         this.bH += (0.0F - this.bH) * 0.4F - 0.05F;
         if(this.bH < 0.0F) {
            this.bH = 0.0F;
         }
      }

      this.bK = this.bJ;
      if(this.cz()) {
         this.bI = this.bH = 0.0F;
         this.bJ += (1.0F - this.bJ) * 0.4F + 0.05F;
         if(this.bJ > 1.0F) {
            this.bJ = 1.0F;
         }
      } else {
         this.bG = false;
         this.bJ += (0.8F * this.bJ * this.bJ * this.bJ - this.bJ) * 0.6F - 0.05F;
         if(this.bJ < 0.0F) {
            this.bJ = 0.0F;
         }
      }

      this.bM = this.bL;
      if(this.w(128)) {
         this.bL += (1.0F - this.bL) * 0.7F + 0.05F;
         if(this.bL > 1.0F) {
            this.bL = 1.0F;
         }
      } else {
         this.bL += (0.0F - this.bL) * 0.7F - 0.05F;
         if(this.bL < 0.0F) {
            this.bL = 0.0F;
         }
      }

   }

   private void df() {
      if(!this.world.isClientSide) {
         this.bC = 1;
         this.c(128, true);
      }

   }

   private boolean dg() {
      return this.passenger == null && this.vehicle == null && this.isTame() && this.cn() && !this.cS() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
   }

   public void f(boolean var1) {
      this.c(32, var1);
   }

   public void r(boolean var1) {
      this.f(var1);
   }

   public void s(boolean var1) {
      if(var1) {
         this.r(false);
      }

      this.c(64, var1);
   }

   private void dh() {
      if(!this.world.isClientSide) {
         this.bD = 1;
         this.s(true);
      }

   }

   public void cW() {
      this.dh();
      String var1 = this.cH();
      if(var1 != null) {
         this.makeSound(var1, this.bB(), this.bC());
      }

   }

   public void dropChest() {
      this.a((Entity)this, (InventoryHorseChest)this.inventoryChest);
      this.cE();
   }

   private void a(Entity var1, InventoryHorseChest var2) {
      if(var2 != null && !this.world.isClientSide) {
         for(int var3 = 0; var3 < var2.getSize(); ++var3) {
            ItemStack var4 = var2.getItem(var3);
            if(var4 != null) {
               this.a(var4, 0.0F);
            }
         }

      }
   }

   public boolean h(EntityHuman var1) {
      this.setOwnerUUID(var1.getUniqueID().toString());
      this.setTame(true);
      return true;
   }

   public void g(float var1, float var2) {
      if(this.passenger != null && this.passenger instanceof EntityLiving && this.cG()) {
         this.lastYaw = this.yaw = this.passenger.yaw;
         this.pitch = this.passenger.pitch * 0.5F;
         this.setYawPitch(this.yaw, this.pitch);
         this.aK = this.aI = this.yaw;
         var1 = ((EntityLiving)this.passenger).aZ * 0.5F;
         var2 = ((EntityLiving)this.passenger).ba;
         if(var2 <= 0.0F) {
            var2 *= 0.25F;
            this.bN = 0;
         }

         if(this.onGround && this.br == 0.0F && this.cz() && !this.bG) {
            var1 = 0.0F;
            var2 = 0.0F;
         }

         if(this.br > 0.0F && !this.cv() && this.onGround) {
            this.motY = this.getJumpStrength() * (double)this.br;
            if(this.hasEffect(MobEffectList.JUMP)) {
               this.motY += (double)((float)(this.getEffect(MobEffectList.JUMP).getAmplifier() + 1) * 0.1F);
            }

            this.m(true);
            this.ai = true;
            if(var2 > 0.0F) {
               float var3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
               float var4 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
               this.motX += (double)(-0.4F * var3 * this.br);
               this.motZ += (double)(0.4F * var4 * this.br);
               this.makeSound("mob.horse.jump", 0.4F, 1.0F);
            }

            this.br = 0.0F;
         }

         this.S = 1.0F;
         this.aM = this.bI() * 0.1F;
         if(!this.world.isClientSide) {
            this.k((float)this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            super.g(var1, var2);
         }

         if(this.onGround) {
            this.br = 0.0F;
            this.m(false);
         }

         this.aA = this.aB;
         double var8 = this.locX - this.lastX;
         double var5 = this.locZ - this.lastZ;
         float var7 = MathHelper.sqrt(var8 * var8 + var5 * var5) * 4.0F;
         if(var7 > 1.0F) {
            var7 = 1.0F;
         }

         this.aB += (var7 - this.aB) * 0.4F;
         this.aC += this.aB;
      } else {
         this.S = 0.5F;
         this.aM = 0.02F;
         super.g(var1, var2);
      }
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("EatingHaystack", this.cy());
      var1.setBoolean("ChestedHorse", this.hasChest());
      var1.setBoolean("HasReproduced", this.cB());
      var1.setBoolean("Bred", this.cA());
      var1.setInt("Type", this.getType());
      var1.setInt("Variant", this.getVariant());
      var1.setInt("Temper", this.getTemper());
      var1.setBoolean("Tame", this.isTame());
      var1.setString("OwnerUUID", this.getOwnerUUID());
      if(this.hasChest()) {
         NBTTagList var2 = new NBTTagList();

         for(int var3 = 2; var3 < this.inventoryChest.getSize(); ++var3) {
            ItemStack var4 = this.inventoryChest.getItem(var3);
            if(var4 != null) {
               NBTTagCompound var5 = new NBTTagCompound();
               var5.setByte("Slot", (byte)var3);
               var4.save(var5);
               var2.add(var5);
            }
         }

         var1.set("Items", var2);
      }

      if(this.inventoryChest.getItem(1) != null) {
         var1.set("ArmorItem", this.inventoryChest.getItem(1).save(new NBTTagCompound()));
      }

      if(this.inventoryChest.getItem(0) != null) {
         var1.set("SaddleItem", this.inventoryChest.getItem(0).save(new NBTTagCompound()));
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.r(var1.getBoolean("EatingHaystack"));
      this.n(var1.getBoolean("Bred"));
      this.setHasChest(var1.getBoolean("ChestedHorse"));
      this.p(var1.getBoolean("HasReproduced"));
      this.setType(var1.getInt("Type"));
      this.setVariant(var1.getInt("Variant"));
      this.setTemper(var1.getInt("Temper"));
      this.setTame(var1.getBoolean("Tame"));
      String var2 = "";
      if(var1.hasKeyOfType("OwnerUUID", 8)) {
         var2 = var1.getString("OwnerUUID");
      } else {
         String var3 = var1.getString("Owner");
         var2 = NameReferencingFileConverter.a(var3);
      }

      if(var2.length() > 0) {
         this.setOwnerUUID(var2);
      }

      AttributeInstance var8 = this.getAttributeMap().a("Speed");
      if(var8 != null) {
         this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(var8.b() * 0.25D);
      }

      if(this.hasChest()) {
         NBTTagList var4 = var1.getList("Items", 10);
         this.loadChest();

         for(int var5 = 0; var5 < var4.size(); ++var5) {
            NBTTagCompound var6 = var4.get(var5);
            int var7 = var6.getByte("Slot") & 255;
            if(var7 >= 2 && var7 < this.inventoryChest.getSize()) {
               this.inventoryChest.setItem(var7, ItemStack.createStack(var6));
            }
         }
      }

      ItemStack var9;
      if(var1.hasKeyOfType("ArmorItem", 10)) {
         var9 = ItemStack.createStack(var1.getCompound("ArmorItem"));
         if(var9 != null && a(var9.getItem())) {
            this.inventoryChest.setItem(1, var9);
         }
      }

      if(var1.hasKeyOfType("SaddleItem", 10)) {
         var9 = ItemStack.createStack(var1.getCompound("SaddleItem"));
         if(var9 != null && var9.getItem() == Items.SADDLE) {
            this.inventoryChest.setItem(0, var9);
         }
      } else if(var1.getBoolean("Saddle")) {
         this.inventoryChest.setItem(0, new ItemStack(Items.SADDLE));
      }

      this.db();
   }

   public boolean mate(EntityAnimal var1) {
      if(var1 == this) {
         return false;
      } else if(var1.getClass() != this.getClass()) {
         return false;
      } else {
         EntityHorse var2 = (EntityHorse)var1;
         if(this.dg() && var2.dg()) {
            int var3 = this.getType();
            int var4 = var2.getType();
            return var3 == var4 || var3 == 0 && var4 == 1 || var3 == 1 && var4 == 0;
         } else {
            return false;
         }
      }
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      EntityHorse var2 = (EntityHorse)var1;
      EntityHorse var3 = new EntityHorse(this.world);
      int var4 = this.getType();
      int var5 = var2.getType();
      int var6 = 0;
      if(var4 == var5) {
         var6 = var4;
      } else if(var4 == 0 && var5 == 1 || var4 == 1 && var5 == 0) {
         var6 = 2;
      }

      if(var6 == 0) {
         int var8 = this.random.nextInt(9);
         int var7;
         if(var8 < 4) {
            var7 = this.getVariant() & 255;
         } else if(var8 < 8) {
            var7 = var2.getVariant() & 255;
         } else {
            var7 = this.random.nextInt(7);
         }

         int var9 = this.random.nextInt(5);
         if(var9 < 2) {
            var7 |= this.getVariant() & '\uff00';
         } else if(var9 < 4) {
            var7 |= var2.getVariant() & '\uff00';
         } else {
            var7 |= this.random.nextInt(5) << 8 & '\uff00';
         }

         var3.setVariant(var7);
      }

      var3.setType(var6);
      double var14 = this.getAttributeInstance(GenericAttributes.maxHealth).b() + var1.getAttributeInstance(GenericAttributes.maxHealth).b() + (double)this.di();
      var3.getAttributeInstance(GenericAttributes.maxHealth).setValue(var14 / 3.0D);
      double var13 = this.getAttributeInstance(attributeJumpStrength).b() + var1.getAttributeInstance(attributeJumpStrength).b() + this.dj();
      var3.getAttributeInstance(attributeJumpStrength).setValue(var13 / 3.0D);
      double var11 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b() + var1.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).b() + this.dk();
      var3.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(var11 / 3.0D);
      return var3;
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      Object var7 = super.prepare(var1, var2);
      boolean var3 = false;
      int var4 = 0;
      int var8;
      if(var7 instanceof EntityHorse.GroupDataHorse) {
         var8 = ((EntityHorse.GroupDataHorse)var7).a;
         var4 = ((EntityHorse.GroupDataHorse)var7).b & 255 | this.random.nextInt(5) << 8;
      } else {
         if(this.random.nextInt(10) == 0) {
            var8 = 1;
         } else {
            int var5 = this.random.nextInt(7);
            int var6 = this.random.nextInt(5);
            var8 = 0;
            var4 = var5 | var6 << 8;
         }

         var7 = new EntityHorse.GroupDataHorse(var8, var4);
      }

      this.setType(var8);
      this.setVariant(var4);
      if(this.random.nextInt(5) == 0) {
         this.setAgeRaw(-24000);
      }

      if(var8 != 4 && var8 != 3) {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue((double)this.di());
         if(var8 == 0) {
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.dk());
         } else {
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.17499999701976776D);
         }
      } else {
         this.getAttributeInstance(GenericAttributes.maxHealth).setValue(15.0D);
         this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.20000000298023224D);
      }

      if(var8 != 2 && var8 != 1) {
         this.getAttributeInstance(attributeJumpStrength).setValue(this.dj());
      } else {
         this.getAttributeInstance(attributeJumpStrength).setValue(0.5D);
      }

      this.setHealth(this.getMaxHealth());
      return (GroupDataEntity)var7;
   }

   public void v(int var1) {
      if(this.cG()) {
         if(var1 < 0) {
            var1 = 0;
         } else {
            this.bG = true;
            this.dh();
         }

         if(var1 >= 90) {
            this.br = 1.0F;
         } else {
            this.br = 0.4F + 0.4F * (float)var1 / 90.0F;
         }
      }

   }

   public void al() {
      super.al();
      if(this.bK > 0.0F) {
         float var1 = MathHelper.sin(this.aI * 3.1415927F / 180.0F);
         float var2 = MathHelper.cos(this.aI * 3.1415927F / 180.0F);
         float var3 = 0.7F * this.bK;
         float var4 = 0.15F * this.bK;
         this.passenger.setPosition(this.locX + (double)(var3 * var1), this.locY + this.an() + this.passenger.am() + (double)var4, this.locZ - (double)(var3 * var2));
         if(this.passenger instanceof EntityLiving) {
            ((EntityLiving)this.passenger).aI = this.aI;
         }
      }

   }

   private float di() {
      return 15.0F + (float)this.random.nextInt(8) + (float)this.random.nextInt(9);
   }

   private double dj() {
      return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
   }

   private double dk() {
      return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
   }

   public static boolean a(Item var0) {
      return var0 == Items.IRON_HORSE_ARMOR || var0 == Items.GOLDEN_HORSE_ARMOR || var0 == Items.DIAMOND_HORSE_ARMOR;
   }

   public boolean k_() {
      return false;
   }

   public float getHeadHeight() {
      return this.length;
   }

   public boolean d(int var1, ItemStack var2) {
      if(var1 == 499 && this.cP()) {
         if(var2 == null && this.hasChest()) {
            this.setHasChest(false);
            this.loadChest();
            return true;
         }

         if(var2 != null && var2.getItem() == Item.getItemOf(Blocks.CHEST) && !this.hasChest()) {
            this.setHasChest(true);
            this.loadChest();
            return true;
         }
      }

      int var3 = var1 - 400;
      if(var3 >= 0 && var3 < 2 && var3 < this.inventoryChest.getSize()) {
         if(var3 == 0 && var2 != null && var2.getItem() != Items.SADDLE) {
            return false;
         } else if(var3 != 1 || (var2 == null || a(var2.getItem())) && this.cO()) {
            this.inventoryChest.setItem(var3, var2);
            this.db();
            return true;
         } else {
            return false;
         }
      } else {
         int var4 = var1 - 500 + 2;
         if(var4 >= 2 && var4 < this.inventoryChest.getSize()) {
            this.inventoryChest.setItem(var4, var2);
            return true;
         } else {
            return false;
         }
      }
   }

   public static class GroupDataHorse implements GroupDataEntity {
      public int a;
      public int b;

      public GroupDataHorse(int var1, int var2) {
         this.a = var1;
         this.b = var2;
      }
   }
}
