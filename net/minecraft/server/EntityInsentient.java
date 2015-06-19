package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ControllerJump;
import net.minecraft.server.ControllerLook;
import net.minecraft.server.ControllerMove;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAIBodyControl;
import net.minecraft.server.EntityGhast;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLeash;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySenses;
import net.minecraft.server.EntityTameableAnimal;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemBow;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemSword;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Navigation;
import net.minecraft.server.NavigationAbstract;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutAttachEntity;
import net.minecraft.server.PathfinderGoalSelector;
import net.minecraft.server.Statistic;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public abstract class EntityInsentient extends EntityLiving {
   public int a_;
   protected int b_;
   private ControllerLook lookController;
   protected ControllerMove moveController;
   protected ControllerJump g;
   private EntityAIBodyControl b;
   protected NavigationAbstract navigation;
   public final PathfinderGoalSelector goalSelector;
   public final PathfinderGoalSelector targetSelector;
   private EntityLiving goalTarget;
   private EntitySenses bk;
   private ItemStack[] equipment = new ItemStack[5];
   public float[] dropChances = new float[5];
   public boolean canPickUpLoot;
   public boolean persistent;
   private boolean bo;
   private Entity bp;
   private NBTTagCompound bq;

   public EntityInsentient(World var1) {
      super(var1);
      this.goalSelector = new PathfinderGoalSelector(var1 != null && var1.methodProfiler != null?var1.methodProfiler:null);
      this.targetSelector = new PathfinderGoalSelector(var1 != null && var1.methodProfiler != null?var1.methodProfiler:null);
      this.lookController = new ControllerLook(this);
      this.moveController = new ControllerMove(this);
      this.g = new ControllerJump(this);
      this.b = new EntityAIBodyControl(this);
      this.navigation = this.b(var1);
      this.bk = new EntitySenses(this);

      for(int var2 = 0; var2 < this.dropChances.length; ++var2) {
         this.dropChances[var2] = 0.085F;
      }

   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeMap().b(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
   }

   protected NavigationAbstract b(World var1) {
      return new Navigation(this, var1);
   }

   public ControllerLook getControllerLook() {
      return this.lookController;
   }

   public ControllerMove getControllerMove() {
      return this.moveController;
   }

   public ControllerJump getControllerJump() {
      return this.g;
   }

   public NavigationAbstract getNavigation() {
      return this.navigation;
   }

   public EntitySenses getEntitySenses() {
      return this.bk;
   }

   public EntityLiving getGoalTarget() {
      return this.goalTarget;
   }

   public void setGoalTarget(EntityLiving var1) {
      this.goalTarget = var1;
   }

   public boolean a(Class<? extends EntityLiving> var1) {
      return var1 != EntityGhast.class;
   }

   public void v() {
   }

   protected void h() {
      super.h();
      this.datawatcher.a(15, Byte.valueOf((byte)0));
   }

   public int w() {
      return 80;
   }

   public void x() {
      String var1 = this.z();
      if(var1 != null) {
         this.makeSound(var1, this.bB(), this.bC());
      }

   }

   public void K() {
      super.K();
      this.world.methodProfiler.a("mobBaseTick");
      if(this.isAlive() && this.random.nextInt(1000) < this.a_++) {
         this.a_ = -this.w();
         this.x();
      }

      this.world.methodProfiler.b();
   }

   protected int getExpValue(EntityHuman var1) {
      if(this.b_ > 0) {
         int var2 = this.b_;
         ItemStack[] var3 = this.getEquipment();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if(var3[var4] != null && this.dropChances[var4] <= 1.0F) {
               var2 += 1 + this.random.nextInt(3);
            }
         }

         return var2;
      } else {
         return this.b_;
      }
   }

   public void y() {
      if(this.world.isClientSide) {
         for(int var1 = 0; var1 < 20; ++var1) {
            double var2 = this.random.nextGaussian() * 0.02D;
            double var4 = this.random.nextGaussian() * 0.02D;
            double var6 = this.random.nextGaussian() * 0.02D;
            double var8 = 10.0D;
            this.world.addParticle(EnumParticle.EXPLOSION_NORMAL, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width - var2 * var8, this.locY + (double)(this.random.nextFloat() * this.length) - var4 * var8, this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width - var6 * var8, var2, var4, var6, new int[0]);
         }
      } else {
         this.world.broadcastEntityEffect(this, (byte)20);
      }

   }

   public void t_() {
      super.t_();
      if(!this.world.isClientSide) {
         this.ca();
      }

   }

   protected float h(float var1, float var2) {
      this.b.a();
      return var2;
   }

   protected String z() {
      return null;
   }

   protected Item getLoot() {
      return null;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      Item var3 = this.getLoot();
      if(var3 != null) {
         int var4 = this.random.nextInt(3);
         if(var2 > 0) {
            var4 += this.random.nextInt(var2 + 1);
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            this.a(var3, 1);
         }
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("CanPickUpLoot", this.bY());
      var1.setBoolean("PersistenceRequired", this.persistent);
      NBTTagList var2 = new NBTTagList();

      NBTTagCompound var4;
      for(int var3 = 0; var3 < this.equipment.length; ++var3) {
         var4 = new NBTTagCompound();
         if(this.equipment[var3] != null) {
            this.equipment[var3].save(var4);
         }

         var2.add(var4);
      }

      var1.set("Equipment", var2);
      NBTTagList var6 = new NBTTagList();

      for(int var7 = 0; var7 < this.dropChances.length; ++var7) {
         var6.add(new NBTTagFloat(this.dropChances[var7]));
      }

      var1.set("DropChances", var6);
      var1.setBoolean("Leashed", this.bo);
      if(this.bp != null) {
         var4 = new NBTTagCompound();
         if(this.bp instanceof EntityLiving) {
            var4.setLong("UUIDMost", this.bp.getUniqueID().getMostSignificantBits());
            var4.setLong("UUIDLeast", this.bp.getUniqueID().getLeastSignificantBits());
         } else if(this.bp instanceof EntityHanging) {
            BlockPosition var5 = ((EntityHanging)this.bp).getBlockPosition();
            var4.setInt("X", var5.getX());
            var4.setInt("Y", var5.getY());
            var4.setInt("Z", var5.getZ());
         }

         var1.set("Leash", var4);
      }

      if(this.ce()) {
         var1.setBoolean("NoAI", this.ce());
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("CanPickUpLoot", 1)) {
         this.j(var1.getBoolean("CanPickUpLoot"));
      }

      this.persistent = var1.getBoolean("PersistenceRequired");
      NBTTagList var2;
      int var3;
      if(var1.hasKeyOfType("Equipment", 9)) {
         var2 = var1.getList("Equipment", 10);

         for(var3 = 0; var3 < this.equipment.length; ++var3) {
            this.equipment[var3] = ItemStack.createStack(var2.get(var3));
         }
      }

      if(var1.hasKeyOfType("DropChances", 9)) {
         var2 = var1.getList("DropChances", 5);

         for(var3 = 0; var3 < var2.size(); ++var3) {
            this.dropChances[var3] = var2.e(var3);
         }
      }

      this.bo = var1.getBoolean("Leashed");
      if(this.bo && var1.hasKeyOfType("Leash", 10)) {
         this.bq = var1.getCompound("Leash");
      }

      this.k(var1.getBoolean("NoAI"));
   }

   public void n(float var1) {
      this.ba = var1;
   }

   public void k(float var1) {
      super.k(var1);
      this.n(var1);
   }

   public void m() {
      super.m();
      this.world.methodProfiler.a("looting");
      if(!this.world.isClientSide && this.bY() && !this.aP && this.world.getGameRules().getBoolean("mobGriefing")) {
         List var1 = this.world.a(EntityItem.class, this.getBoundingBox().grow(1.0D, 0.0D, 1.0D));
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            EntityItem var3 = (EntityItem)var2.next();
            if(!var3.dead && var3.getItemStack() != null && !var3.s()) {
               this.a(var3);
            }
         }
      }

      this.world.methodProfiler.b();
   }

   protected void a(EntityItem var1) {
      ItemStack var2 = var1.getItemStack();
      int var3 = c(var2);
      if(var3 > -1) {
         boolean var4 = true;
         ItemStack var5 = this.getEquipment(var3);
         if(var5 != null) {
            if(var3 == 0) {
               if(var2.getItem() instanceof ItemSword && !(var5.getItem() instanceof ItemSword)) {
                  var4 = true;
               } else if(var2.getItem() instanceof ItemSword && var5.getItem() instanceof ItemSword) {
                  ItemSword var8 = (ItemSword)var2.getItem();
                  ItemSword var10 = (ItemSword)var5.getItem();
                  if(var8.g() != var10.g()) {
                     var4 = var8.g() > var10.g();
                  } else {
                     var4 = var2.getData() > var5.getData() || var2.hasTag() && !var5.hasTag();
                  }
               } else if(var2.getItem() instanceof ItemBow && var5.getItem() instanceof ItemBow) {
                  var4 = var2.hasTag() && !var5.hasTag();
               } else {
                  var4 = false;
               }
            } else if(var2.getItem() instanceof ItemArmor && !(var5.getItem() instanceof ItemArmor)) {
               var4 = true;
            } else if(var2.getItem() instanceof ItemArmor && var5.getItem() instanceof ItemArmor) {
               ItemArmor var6 = (ItemArmor)var2.getItem();
               ItemArmor var7 = (ItemArmor)var5.getItem();
               if(var6.c != var7.c) {
                  var4 = var6.c > var7.c;
               } else {
                  var4 = var2.getData() > var5.getData() || var2.hasTag() && !var5.hasTag();
               }
            } else {
               var4 = false;
            }
         }

         if(var4 && this.a(var2)) {
            if(var5 != null && this.random.nextFloat() - 0.1F < this.dropChances[var3]) {
               this.a(var5, 0.0F);
            }

            if(var2.getItem() == Items.DIAMOND && var1.n() != null) {
               EntityHuman var9 = this.world.a(var1.n());
               if(var9 != null) {
                  var9.b((Statistic)AchievementList.x);
               }
            }

            this.setEquipment(var3, var2);
            this.dropChances[var3] = 2.0F;
            this.persistent = true;
            this.receive(var1, 1);
            var1.die();
         }
      }

   }

   protected boolean a(ItemStack var1) {
      return true;
   }

   protected boolean isTypeNotPersistent() {
      return true;
   }

   protected void D() {
      if(this.persistent) {
         this.ticksFarFromPlayer = 0;
      } else {
         EntityHuman var1 = this.world.findNearbyPlayer(this, -1.0D);
         if(var1 != null) {
            double var2 = var1.locX - this.locX;
            double var4 = var1.locY - this.locY;
            double var6 = var1.locZ - this.locZ;
            double var8 = var2 * var2 + var4 * var4 + var6 * var6;
            if(this.isTypeNotPersistent() && var8 > 16384.0D) {
               this.die();
            }

            if(this.ticksFarFromPlayer > 600 && this.random.nextInt(800) == 0 && var8 > 1024.0D && this.isTypeNotPersistent()) {
               this.die();
            } else if(var8 < 1024.0D) {
               this.ticksFarFromPlayer = 0;
            }
         }

      }
   }

   protected final void doTick() {
      ++this.ticksFarFromPlayer;
      this.world.methodProfiler.a("checkDespawn");
      this.D();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("sensing");
      this.bk.a();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("targetSelector");
      this.targetSelector.a();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("goalSelector");
      this.goalSelector.a();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("navigation");
      this.navigation.k();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("mob tick");
      this.E();
      this.world.methodProfiler.b();
      this.world.methodProfiler.a("controls");
      this.world.methodProfiler.a("move");
      this.moveController.c();
      this.world.methodProfiler.c("look");
      this.lookController.a();
      this.world.methodProfiler.c("jump");
      this.g.b();
      this.world.methodProfiler.b();
      this.world.methodProfiler.b();
   }

   protected void E() {
   }

   public int bQ() {
      return 40;
   }

   public void a(Entity var1, float var2, float var3) {
      double var4 = var1.locX - this.locX;
      double var8 = var1.locZ - this.locZ;
      double var6;
      if(var1 instanceof EntityLiving) {
         EntityLiving var10 = (EntityLiving)var1;
         var6 = var10.locY + (double)var10.getHeadHeight() - (this.locY + (double)this.getHeadHeight());
      } else {
         var6 = (var1.getBoundingBox().b + var1.getBoundingBox().e) / 2.0D - (this.locY + (double)this.getHeadHeight());
      }

      double var14 = (double)MathHelper.sqrt(var4 * var4 + var8 * var8);
      float var12 = (float)(MathHelper.b(var8, var4) * 180.0D / 3.1415927410125732D) - 90.0F;
      float var13 = (float)(-(MathHelper.b(var6, var14) * 180.0D / 3.1415927410125732D));
      this.pitch = this.b(this.pitch, var13, var3);
      this.yaw = this.b(this.yaw, var12, var2);
   }

   private float b(float var1, float var2, float var3) {
      float var4 = MathHelper.g(var2 - var1);
      if(var4 > var3) {
         var4 = var3;
      }

      if(var4 < -var3) {
         var4 = -var3;
      }

      return var1 + var4;
   }

   public boolean bR() {
      return true;
   }

   public boolean canSpawn() {
      return this.world.a((AxisAlignedBB)this.getBoundingBox(), (Entity)this) && this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox());
   }

   public int bV() {
      return 4;
   }

   public int aE() {
      if(this.getGoalTarget() == null) {
         return 3;
      } else {
         int var1 = (int)(this.getHealth() - this.getMaxHealth() * 0.33F);
         var1 -= (3 - this.world.getDifficulty().a()) * 4;
         if(var1 < 0) {
            var1 = 0;
         }

         return var1 + 3;
      }
   }

   public ItemStack bA() {
      return this.equipment[0];
   }

   public ItemStack getEquipment(int var1) {
      return this.equipment[var1];
   }

   public ItemStack q(int var1) {
      return this.equipment[var1 + 1];
   }

   public void setEquipment(int var1, ItemStack var2) {
      this.equipment[var1] = var2;
   }

   public ItemStack[] getEquipment() {
      return this.equipment;
   }

   protected void dropEquipment(boolean var1, int var2) {
      for(int var3 = 0; var3 < this.getEquipment().length; ++var3) {
         ItemStack var4 = this.getEquipment(var3);
         boolean var5 = this.dropChances[var3] > 1.0F;
         if(var4 != null && (var1 || var5) && this.random.nextFloat() - (float)var2 * 0.01F < this.dropChances[var3]) {
            if(!var5 && var4.e()) {
               int var6 = Math.max(var4.j() - 25, 1);
               int var7 = var4.j() - this.random.nextInt(this.random.nextInt(var6) + 1);
               if(var7 > var6) {
                  var7 = var6;
               }

               if(var7 < 1) {
                  var7 = 1;
               }

               var4.setData(var7);
            }

            this.a(var4, 0.0F);
         }
      }

   }

   protected void a(DifficultyDamageScaler var1) {
      if(this.random.nextFloat() < 0.15F * var1.c()) {
         int var2 = this.random.nextInt(2);
         float var3 = this.world.getDifficulty() == EnumDifficulty.HARD?0.1F:0.25F;
         if(this.random.nextFloat() < 0.095F) {
            ++var2;
         }

         if(this.random.nextFloat() < 0.095F) {
            ++var2;
         }

         if(this.random.nextFloat() < 0.095F) {
            ++var2;
         }

         for(int var4 = 3; var4 >= 0; --var4) {
            ItemStack var5 = this.q(var4);
            if(var4 < 3 && this.random.nextFloat() < var3) {
               break;
            }

            if(var5 == null) {
               Item var6 = a(var4 + 1, var2);
               if(var6 != null) {
                  this.setEquipment(var4 + 1, new ItemStack(var6));
               }
            }
         }
      }

   }

   public static int c(ItemStack var0) {
      if(var0.getItem() != Item.getItemOf(Blocks.PUMPKIN) && var0.getItem() != Items.SKULL) {
         if(var0.getItem() instanceof ItemArmor) {
            switch(((ItemArmor)var0.getItem()).b) {
            case 0:
               return 4;
            case 1:
               return 3;
            case 2:
               return 2;
            case 3:
               return 1;
            }
         }

         return 0;
      } else {
         return 4;
      }
   }

   public static Item a(int var0, int var1) {
      switch(var0) {
      case 4:
         if(var1 == 0) {
            return Items.LEATHER_HELMET;
         } else if(var1 == 1) {
            return Items.GOLDEN_HELMET;
         } else if(var1 == 2) {
            return Items.CHAINMAIL_HELMET;
         } else if(var1 == 3) {
            return Items.IRON_HELMET;
         } else if(var1 == 4) {
            return Items.DIAMOND_HELMET;
         }
      case 3:
         if(var1 == 0) {
            return Items.LEATHER_CHESTPLATE;
         } else if(var1 == 1) {
            return Items.GOLDEN_CHESTPLATE;
         } else if(var1 == 2) {
            return Items.CHAINMAIL_CHESTPLATE;
         } else if(var1 == 3) {
            return Items.IRON_CHESTPLATE;
         } else if(var1 == 4) {
            return Items.DIAMOND_CHESTPLATE;
         }
      case 2:
         if(var1 == 0) {
            return Items.LEATHER_LEGGINGS;
         } else if(var1 == 1) {
            return Items.GOLDEN_LEGGINGS;
         } else if(var1 == 2) {
            return Items.CHAINMAIL_LEGGINGS;
         } else if(var1 == 3) {
            return Items.IRON_LEGGINGS;
         } else if(var1 == 4) {
            return Items.DIAMOND_LEGGINGS;
         }
      case 1:
         if(var1 == 0) {
            return Items.LEATHER_BOOTS;
         } else if(var1 == 1) {
            return Items.GOLDEN_BOOTS;
         } else if(var1 == 2) {
            return Items.CHAINMAIL_BOOTS;
         } else if(var1 == 3) {
            return Items.IRON_BOOTS;
         } else if(var1 == 4) {
            return Items.DIAMOND_BOOTS;
         }
      default:
         return null;
      }
   }

   protected void b(DifficultyDamageScaler var1) {
      float var2 = var1.c();
      if(this.bA() != null && this.random.nextFloat() < 0.25F * var2) {
         EnchantmentManager.a(this.random, this.bA(), (int)(5.0F + var2 * (float)this.random.nextInt(18)));
      }

      for(int var3 = 0; var3 < 4; ++var3) {
         ItemStack var4 = this.q(var3);
         if(var4 != null && this.random.nextFloat() < 0.5F * var2) {
            EnchantmentManager.a(this.random, var4, (int)(5.0F + var2 * (float)this.random.nextInt(18)));
         }
      }

   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).b(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, 1));
      return var2;
   }

   public boolean bW() {
      return false;
   }

   public void bX() {
      this.persistent = true;
   }

   public void a(int var1, float var2) {
      this.dropChances[var1] = var2;
   }

   public boolean bY() {
      return this.canPickUpLoot;
   }

   public void j(boolean var1) {
      this.canPickUpLoot = var1;
   }

   public boolean isPersistent() {
      return this.persistent;
   }

   public final boolean e(EntityHuman var1) {
      if(this.cc() && this.getLeashHolder() == var1) {
         this.unleash(true, !var1.abilities.canInstantlyBuild);
         return true;
      } else {
         ItemStack var2 = var1.inventory.getItemInHand();
         if(var2 != null && var2.getItem() == Items.LEAD && this.cb()) {
            if(!(this instanceof EntityTameableAnimal) || !((EntityTameableAnimal)this).isTamed()) {
               this.setLeashHolder(var1, true);
               --var2.count;
               return true;
            }

            if(((EntityTameableAnimal)this).e(var1)) {
               this.setLeashHolder(var1, true);
               --var2.count;
               return true;
            }
         }

         return this.a(var1)?true:super.e(var1);
      }
   }

   protected boolean a(EntityHuman var1) {
      return false;
   }

   protected void ca() {
      if(this.bq != null) {
         this.n();
      }

      if(this.bo) {
         if(!this.isAlive()) {
            this.unleash(true, true);
         }

         if(this.bp == null || this.bp.dead) {
            this.unleash(true, true);
         }
      }
   }

   public void unleash(boolean var1, boolean var2) {
      if(this.bo) {
         this.bo = false;
         this.bp = null;
         if(!this.world.isClientSide && var2) {
            this.a(Items.LEAD, 1);
         }

         if(!this.world.isClientSide && var1 && this.world instanceof WorldServer) {
            ((WorldServer)this.world).getTracker().a((Entity)this, (Packet)(new PacketPlayOutAttachEntity(1, this, (Entity)null)));
         }
      }

   }

   public boolean cb() {
      return !this.cc() && !(this instanceof IMonster);
   }

   public boolean cc() {
      return this.bo;
   }

   public Entity getLeashHolder() {
      return this.bp;
   }

   public void setLeashHolder(Entity var1, boolean var2) {
      this.bo = true;
      this.bp = var1;
      if(!this.world.isClientSide && var2 && this.world instanceof WorldServer) {
         ((WorldServer)this.world).getTracker().a((Entity)this, (Packet)(new PacketPlayOutAttachEntity(1, this, this.bp)));
      }

   }

   private void n() {
      if(this.bo && this.bq != null) {
         if(this.bq.hasKeyOfType("UUIDMost", 4) && this.bq.hasKeyOfType("UUIDLeast", 4)) {
            UUID var5 = new UUID(this.bq.getLong("UUIDMost"), this.bq.getLong("UUIDLeast"));
            List var6 = this.world.a(EntityLiving.class, this.getBoundingBox().grow(10.0D, 10.0D, 10.0D));
            Iterator var3 = var6.iterator();

            while(var3.hasNext()) {
               EntityLiving var4 = (EntityLiving)var3.next();
               if(var4.getUniqueID().equals(var5)) {
                  this.bp = var4;
                  break;
               }
            }
         } else if(this.bq.hasKeyOfType("X", 99) && this.bq.hasKeyOfType("Y", 99) && this.bq.hasKeyOfType("Z", 99)) {
            BlockPosition var1 = new BlockPosition(this.bq.getInt("X"), this.bq.getInt("Y"), this.bq.getInt("Z"));
            EntityLeash var2 = EntityLeash.b(this.world, var1);
            if(var2 == null) {
               var2 = EntityLeash.a(this.world, var1);
            }

            this.bp = var2;
         } else {
            this.unleash(false, true);
         }
      }

      this.bq = null;
   }

   public boolean d(int var1, ItemStack var2) {
      int var3;
      if(var1 == 99) {
         var3 = 0;
      } else {
         var3 = var1 - 100 + 1;
         if(var3 < 0 || var3 >= this.equipment.length) {
            return false;
         }
      }

      if(var2 != null && c(var2) != var3 && (var3 != 4 || !(var2.getItem() instanceof ItemBlock))) {
         return false;
      } else {
         this.setEquipment(var3, var2);
         return true;
      }
   }

   public boolean bM() {
      return super.bM() && !this.ce();
   }

   public void k(boolean var1) {
      this.datawatcher.watch(15, Byte.valueOf((byte)(var1?1:0)));
   }

   public boolean ce() {
      return this.datawatcher.getByte(15) != 0;
   }

   public static enum EnumEntityPositionType {
      ON_GROUND,
      IN_AIR,
      IN_WATER;

      private EnumEntityPositionType() {
      }
   }
}
