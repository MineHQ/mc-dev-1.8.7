package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockBed;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatClickable;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChestLock;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerPlayer;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityFishingHook;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPig;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.FoodMetaData;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IComplex;
import net.minecraft.server.IInventory;
import net.minecraft.server.IMerchant;
import net.minecraft.server.IMonster;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.ITileEntityContainer;
import net.minecraft.server.InventoryEnderChest;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PacketPlayOutEntityVelocity;
import net.minecraft.server.PlayerAbilities;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.ScoreboardTeam;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldSettings;

public abstract class EntityHuman extends EntityLiving {
   public PlayerInventory inventory = new PlayerInventory(this);
   private InventoryEnderChest enderChest = new InventoryEnderChest();
   public Container defaultContainer;
   public Container activeContainer;
   protected FoodMetaData foodData = new FoodMetaData();
   protected int bm;
   public float bn;
   public float bo;
   public int bp;
   public double bq;
   public double br;
   public double bs;
   public double bt;
   public double bu;
   public double bv;
   public boolean sleeping;
   public BlockPosition bx;
   public int sleepTicks;
   public float by;
   public float bz;
   private BlockPosition c;
   private boolean d;
   private BlockPosition e;
   public PlayerAbilities abilities = new PlayerAbilities();
   public int expLevel;
   public int expTotal;
   public float exp;
   private int f;
   private ItemStack g;
   private int h;
   protected float bE = 0.1F;
   protected float bF = 0.02F;
   private int i;
   private final GameProfile bH;
   private boolean bI = false;
   public EntityFishingHook hookedFish;

   public EntityHuman(World var1, GameProfile var2) {
      super(var1);
      this.uniqueID = a(var2);
      this.bH = var2;
      this.defaultContainer = new ContainerPlayer(this.inventory, !var1.isClientSide, this);
      this.activeContainer = this.defaultContainer;
      BlockPosition var3 = var1.getSpawn();
      this.setPositionRotation((double)var3.getX() + 0.5D, (double)(var3.getY() + 1), (double)var3.getZ() + 0.5D, 0.0F, 0.0F);
      this.aV = 180.0F;
      this.maxFireTicks = 20;
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE).setValue(1.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.10000000149011612D);
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Byte.valueOf((byte)0));
      this.datawatcher.a(17, Float.valueOf(0.0F));
      this.datawatcher.a(18, Integer.valueOf(0));
      this.datawatcher.a(10, Byte.valueOf((byte)0));
   }

   public boolean bS() {
      return this.g != null;
   }

   public void bU() {
      if(this.g != null) {
         this.g.b(this.world, this, this.h);
      }

      this.bV();
   }

   public void bV() {
      this.g = null;
      this.h = 0;
      if(!this.world.isClientSide) {
         this.f(false);
      }

   }

   public boolean isBlocking() {
      return this.bS() && this.g.getItem().e(this.g) == EnumAnimation.BLOCK;
   }

   public void t_() {
      this.noclip = this.isSpectator();
      if(this.isSpectator()) {
         this.onGround = false;
      }

      if(this.g != null) {
         ItemStack var1 = this.inventory.getItemInHand();
         if(var1 == this.g) {
            if(this.h <= 25 && this.h % 4 == 0) {
               this.b((ItemStack)var1, 5);
            }

            if(--this.h == 0 && !this.world.isClientSide) {
               this.s();
            }
         } else {
            this.bV();
         }
      }

      if(this.bp > 0) {
         --this.bp;
      }

      if(this.isSleeping()) {
         ++this.sleepTicks;
         if(this.sleepTicks > 100) {
            this.sleepTicks = 100;
         }

         if(!this.world.isClientSide) {
            if(!this.p()) {
               this.a(true, true, false);
            } else if(this.world.w()) {
               this.a(false, true, true);
            }
         }
      } else if(this.sleepTicks > 0) {
         ++this.sleepTicks;
         if(this.sleepTicks >= 110) {
            this.sleepTicks = 0;
         }
      }

      super.t_();
      if(!this.world.isClientSide && this.activeContainer != null && !this.activeContainer.a(this)) {
         this.closeInventory();
         this.activeContainer = this.defaultContainer;
      }

      if(this.isBurning() && this.abilities.isInvulnerable) {
         this.extinguish();
      }

      this.bq = this.bt;
      this.br = this.bu;
      this.bs = this.bv;
      double var14 = this.locX - this.bt;
      double var3 = this.locY - this.bu;
      double var5 = this.locZ - this.bv;
      double var7 = 10.0D;
      if(var14 > var7) {
         this.bq = this.bt = this.locX;
      }

      if(var5 > var7) {
         this.bs = this.bv = this.locZ;
      }

      if(var3 > var7) {
         this.br = this.bu = this.locY;
      }

      if(var14 < -var7) {
         this.bq = this.bt = this.locX;
      }

      if(var5 < -var7) {
         this.bs = this.bv = this.locZ;
      }

      if(var3 < -var7) {
         this.br = this.bu = this.locY;
      }

      this.bt += var14 * 0.25D;
      this.bv += var5 * 0.25D;
      this.bu += var3 * 0.25D;
      if(this.vehicle == null) {
         this.e = null;
      }

      if(!this.world.isClientSide) {
         this.foodData.a(this);
         this.b(StatisticList.g);
         if(this.isAlive()) {
            this.b(StatisticList.h);
         }
      }

      int var9 = 29999999;
      double var10 = MathHelper.a(this.locX, -2.9999999E7D, 2.9999999E7D);
      double var12 = MathHelper.a(this.locZ, -2.9999999E7D, 2.9999999E7D);
      if(var10 != this.locX || var12 != this.locZ) {
         this.setPosition(var10, this.locY, var12);
      }

   }

   public int L() {
      return this.abilities.isInvulnerable?0:80;
   }

   protected String P() {
      return "game.player.swim";
   }

   protected String aa() {
      return "game.player.swim.splash";
   }

   public int aq() {
      return 10;
   }

   public void makeSound(String var1, float var2, float var3) {
      this.world.a(this, var1, var2, var3);
   }

   protected void b(ItemStack var1, int var2) {
      if(var1.m() == EnumAnimation.DRINK) {
         this.makeSound("random.drink", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
      }

      if(var1.m() == EnumAnimation.EAT) {
         for(int var3 = 0; var3 < var2; ++var3) {
            Vec3D var4 = new Vec3D(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            var4 = var4.a(-this.pitch * 3.1415927F / 180.0F);
            var4 = var4.b(-this.yaw * 3.1415927F / 180.0F);
            double var5 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
            Vec3D var7 = new Vec3D(((double)this.random.nextFloat() - 0.5D) * 0.3D, var5, 0.6D);
            var7 = var7.a(-this.pitch * 3.1415927F / 180.0F);
            var7 = var7.b(-this.yaw * 3.1415927F / 180.0F);
            var7 = var7.add(this.locX, this.locY + (double)this.getHeadHeight(), this.locZ);
            if(var1.usesData()) {
               this.world.addParticle(EnumParticle.ITEM_CRACK, var7.a, var7.b, var7.c, var4.a, var4.b + 0.05D, var4.c, new int[]{Item.getId(var1.getItem()), var1.getData()});
            } else {
               this.world.addParticle(EnumParticle.ITEM_CRACK, var7.a, var7.b, var7.c, var4.a, var4.b + 0.05D, var4.c, new int[]{Item.getId(var1.getItem())});
            }
         }

         this.makeSound("random.eat", 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
      }

   }

   protected void s() {
      if(this.g != null) {
         this.b((ItemStack)this.g, 16);
         int var1 = this.g.count;
         ItemStack var2 = this.g.b(this.world, this);
         if(var2 != this.g || var2 != null && var2.count != var1) {
            this.inventory.items[this.inventory.itemInHandIndex] = var2;
            if(var2.count == 0) {
               this.inventory.items[this.inventory.itemInHandIndex] = null;
            }
         }

         this.bV();
      }

   }

   protected boolean bD() {
      return this.getHealth() <= 0.0F || this.isSleeping();
   }

   public void closeInventory() {
      this.activeContainer = this.defaultContainer;
   }

   public void ak() {
      if(!this.world.isClientSide && this.isSneaking()) {
         this.mount((Entity)null);
         this.setSneaking(false);
      } else {
         double var1 = this.locX;
         double var3 = this.locY;
         double var5 = this.locZ;
         float var7 = this.yaw;
         float var8 = this.pitch;
         super.ak();
         this.bn = this.bo;
         this.bo = 0.0F;
         this.l(this.locX - var1, this.locY - var3, this.locZ - var5);
         if(this.vehicle instanceof EntityPig) {
            this.pitch = var8;
            this.yaw = var7;
            this.aI = ((EntityPig)this.vehicle).aI;
         }

      }
   }

   protected void doTick() {
      super.doTick();
      this.bx();
      this.aK = this.yaw;
   }

   public void m() {
      if(this.bm > 0) {
         --this.bm;
      }

      if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration")) {
         if(this.getHealth() < this.getMaxHealth() && this.ticksLived % 20 == 0) {
            this.heal(1.0F);
         }

         if(this.foodData.c() && this.ticksLived % 10 == 0) {
            this.foodData.a(this.foodData.getFoodLevel() + 1);
         }
      }

      this.inventory.k();
      this.bn = this.bo;
      super.m();
      AttributeInstance var1 = this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
      if(!this.world.isClientSide) {
         var1.setValue((double)this.abilities.b());
      }

      this.aM = this.bF;
      if(this.isSprinting()) {
         this.aM = (float)((double)this.aM + (double)this.bF * 0.3D);
      }

      this.k((float)var1.getValue());
      float var2 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
      float var3 = (float)(Math.atan(-this.motY * 0.20000000298023224D) * 15.0D);
      if(var2 > 0.1F) {
         var2 = 0.1F;
      }

      if(!this.onGround || this.getHealth() <= 0.0F) {
         var2 = 0.0F;
      }

      if(this.onGround || this.getHealth() <= 0.0F) {
         var3 = 0.0F;
      }

      this.bo += (var2 - this.bo) * 0.4F;
      this.aF += (var3 - this.aF) * 0.8F;
      if(this.getHealth() > 0.0F && !this.isSpectator()) {
         AxisAlignedBB var4 = null;
         if(this.vehicle != null && !this.vehicle.dead) {
            var4 = this.getBoundingBox().a(this.vehicle.getBoundingBox()).grow(1.0D, 0.0D, 1.0D);
         } else {
            var4 = this.getBoundingBox().grow(1.0D, 0.5D, 1.0D);
         }

         List var5 = this.world.getEntities(this, var4);

         for(int var6 = 0; var6 < var5.size(); ++var6) {
            Entity var7 = (Entity)var5.get(var6);
            if(!var7.dead) {
               this.d(var7);
            }
         }
      }

   }

   private void d(Entity var1) {
      var1.d(this);
   }

   public int getScore() {
      return this.datawatcher.getInt(18);
   }

   public void setScore(int var1) {
      this.datawatcher.watch(18, Integer.valueOf(var1));
   }

   public void addScore(int var1) {
      int var2 = this.getScore();
      this.datawatcher.watch(18, Integer.valueOf(var2 + var1));
   }

   public void die(DamageSource var1) {
      super.die(var1);
      this.setSize(0.2F, 0.2F);
      this.setPosition(this.locX, this.locY, this.locZ);
      this.motY = 0.10000000149011612D;
      if(this.getName().equals("Notch")) {
         this.a(new ItemStack(Items.APPLE, 1), true, false);
      }

      if(!this.world.getGameRules().getBoolean("keepInventory")) {
         this.inventory.n();
      }

      if(var1 != null) {
         this.motX = (double)(-MathHelper.cos((this.aw + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
         this.motZ = (double)(-MathHelper.sin((this.aw + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
      } else {
         this.motX = this.motZ = 0.0D;
      }

      this.b(StatisticList.y);
      this.a(StatisticList.h);
   }

   protected String bo() {
      return "game.player.hurt";
   }

   protected String bp() {
      return "game.player.die";
   }

   public void b(Entity var1, int var2) {
      this.addScore(var2);
      Collection var3 = this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.f);
      if(var1 instanceof EntityHuman) {
         this.b(StatisticList.B);
         var3.addAll(this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.e));
         var3.addAll(this.e(var1));
      } else {
         this.b(StatisticList.z);
      }

      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         ScoreboardObjective var5 = (ScoreboardObjective)var4.next();
         ScoreboardScore var6 = this.getScoreboard().getPlayerScoreForObjective(this.getName(), var5);
         var6.incrementScore();
      }

   }

   private Collection<ScoreboardObjective> e(Entity var1) {
      ScoreboardTeam var2 = this.getScoreboard().getPlayerTeam(this.getName());
      if(var2 != null) {
         int var3 = var2.l().b();
         if(var3 >= 0 && var3 < IScoreboardCriteria.i.length) {
            Iterator var4 = this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.i[var3]).iterator();

            while(var4.hasNext()) {
               ScoreboardObjective var5 = (ScoreboardObjective)var4.next();
               ScoreboardScore var6 = this.getScoreboard().getPlayerScoreForObjective(var1.getName(), var5);
               var6.incrementScore();
            }
         }
      }

      ScoreboardTeam var7 = this.getScoreboard().getPlayerTeam(var1.getName());
      if(var7 != null) {
         int var8 = var7.l().b();
         if(var8 >= 0 && var8 < IScoreboardCriteria.h.length) {
            return this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.h[var8]);
         }
      }

      return Lists.newArrayList();
   }

   public EntityItem a(boolean var1) {
      return this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, var1 && this.inventory.getItemInHand() != null?this.inventory.getItemInHand().count:1), false, true);
   }

   public EntityItem drop(ItemStack var1, boolean var2) {
      return this.a(var1, false, false);
   }

   public EntityItem a(ItemStack var1, boolean var2, boolean var3) {
      if(var1 == null) {
         return null;
      } else if(var1.count == 0) {
         return null;
      } else {
         double var4 = this.locY - 0.30000001192092896D + (double)this.getHeadHeight();
         EntityItem var6 = new EntityItem(this.world, this.locX, var4, this.locZ, var1);
         var6.a(40);
         if(var3) {
            var6.c(this.getName());
         }

         float var7;
         float var8;
         if(var2) {
            var7 = this.random.nextFloat() * 0.5F;
            var8 = this.random.nextFloat() * 3.1415927F * 2.0F;
            var6.motX = (double)(-MathHelper.sin(var8) * var7);
            var6.motZ = (double)(MathHelper.cos(var8) * var7);
            var6.motY = 0.20000000298023224D;
         } else {
            var7 = 0.3F;
            var6.motX = (double)(-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var7);
            var6.motZ = (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var7);
            var6.motY = (double)(-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * var7 + 0.1F);
            var8 = this.random.nextFloat() * 3.1415927F * 2.0F;
            var7 = 0.02F * this.random.nextFloat();
            var6.motX += Math.cos((double)var8) * (double)var7;
            var6.motY += (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
            var6.motZ += Math.sin((double)var8) * (double)var7;
         }

         this.a(var6);
         if(var3) {
            this.b(StatisticList.v);
         }

         return var6;
      }
   }

   protected void a(EntityItem var1) {
      this.world.addEntity(var1);
   }

   public float a(Block var1) {
      float var2 = this.inventory.a(var1);
      if(var2 > 1.0F) {
         int var3 = EnchantmentManager.getDigSpeedEnchantmentLevel(this);
         ItemStack var4 = this.inventory.getItemInHand();
         if(var3 > 0 && var4 != null) {
            var2 += (float)(var3 * var3 + 1);
         }
      }

      if(this.hasEffect(MobEffectList.FASTER_DIG)) {
         var2 *= 1.0F + (float)(this.getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F;
      }

      if(this.hasEffect(MobEffectList.SLOWER_DIG)) {
         float var5 = 1.0F;
         switch(this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) {
         case 0:
            var5 = 0.3F;
            break;
         case 1:
            var5 = 0.09F;
            break;
         case 2:
            var5 = 0.0027F;
            break;
         case 3:
         default:
            var5 = 8.1E-4F;
         }

         var2 *= var5;
      }

      if(this.a((Material)Material.WATER) && !EnchantmentManager.j(this)) {
         var2 /= 5.0F;
      }

      if(!this.onGround) {
         var2 /= 5.0F;
      }

      return var2;
   }

   public boolean b(Block var1) {
      return this.inventory.b(var1);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.uniqueID = a(this.bH);
      NBTTagList var2 = var1.getList("Inventory", 10);
      this.inventory.b(var2);
      this.inventory.itemInHandIndex = var1.getInt("SelectedItemSlot");
      this.sleeping = var1.getBoolean("Sleeping");
      this.sleepTicks = var1.getShort("SleepTimer");
      this.exp = var1.getFloat("XpP");
      this.expLevel = var1.getInt("XpLevel");
      this.expTotal = var1.getInt("XpTotal");
      this.f = var1.getInt("XpSeed");
      if(this.f == 0) {
         this.f = this.random.nextInt();
      }

      this.setScore(var1.getInt("Score"));
      if(this.sleeping) {
         this.bx = new BlockPosition(this);
         this.a(true, true, false);
      }

      if(var1.hasKeyOfType("SpawnX", 99) && var1.hasKeyOfType("SpawnY", 99) && var1.hasKeyOfType("SpawnZ", 99)) {
         this.c = new BlockPosition(var1.getInt("SpawnX"), var1.getInt("SpawnY"), var1.getInt("SpawnZ"));
         this.d = var1.getBoolean("SpawnForced");
      }

      this.foodData.a(var1);
      this.abilities.b(var1);
      if(var1.hasKeyOfType("EnderItems", 9)) {
         NBTTagList var3 = var1.getList("EnderItems", 10);
         this.enderChest.a(var3);
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.set("Inventory", this.inventory.a(new NBTTagList()));
      var1.setInt("SelectedItemSlot", this.inventory.itemInHandIndex);
      var1.setBoolean("Sleeping", this.sleeping);
      var1.setShort("SleepTimer", (short)this.sleepTicks);
      var1.setFloat("XpP", this.exp);
      var1.setInt("XpLevel", this.expLevel);
      var1.setInt("XpTotal", this.expTotal);
      var1.setInt("XpSeed", this.f);
      var1.setInt("Score", this.getScore());
      if(this.c != null) {
         var1.setInt("SpawnX", this.c.getX());
         var1.setInt("SpawnY", this.c.getY());
         var1.setInt("SpawnZ", this.c.getZ());
         var1.setBoolean("SpawnForced", this.d);
      }

      this.foodData.b(var1);
      this.abilities.a(var1);
      var1.set("EnderItems", this.enderChest.h());
      ItemStack var2 = this.inventory.getItemInHand();
      if(var2 != null && var2.getItem() != null) {
         var1.set("SelectedItem", var2.save(new NBTTagCompound()));
      }

   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(this.abilities.isInvulnerable && !var1.ignoresInvulnerability()) {
         return false;
      } else {
         this.ticksFarFromPlayer = 0;
         if(this.getHealth() <= 0.0F) {
            return false;
         } else {
            if(this.isSleeping() && !this.world.isClientSide) {
               this.a(true, true, false);
            }

            if(var1.r()) {
               if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                  var2 = 0.0F;
               }

               if(this.world.getDifficulty() == EnumDifficulty.EASY) {
                  var2 = var2 / 2.0F + 1.0F;
               }

               if(this.world.getDifficulty() == EnumDifficulty.HARD) {
                  var2 = var2 * 3.0F / 2.0F;
               }
            }

            if(var2 == 0.0F) {
               return false;
            } else {
               Entity var3 = var1.getEntity();
               if(var3 instanceof EntityArrow && ((EntityArrow)var3).shooter != null) {
                  var3 = ((EntityArrow)var3).shooter;
               }

               return super.damageEntity(var1, var2);
            }
         }
      }
   }

   public boolean a(EntityHuman var1) {
      ScoreboardTeamBase var2 = this.getScoreboardTeam();
      ScoreboardTeamBase var3 = var1.getScoreboardTeam();
      return var2 == null?true:(!var2.isAlly(var3)?true:var2.allowFriendlyFire());
   }

   protected void damageArmor(float var1) {
      this.inventory.a(var1);
   }

   public int br() {
      return this.inventory.m();
   }

   public float bY() {
      int var1 = 0;
      ItemStack[] var2 = this.inventory.armor;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ItemStack var5 = var2[var4];
         if(var5 != null) {
            ++var1;
         }
      }

      return (float)var1 / (float)this.inventory.armor.length;
   }

   protected void d(DamageSource var1, float var2) {
      if(!this.isInvulnerable(var1)) {
         if(!var1.ignoresArmor() && this.isBlocking() && var2 > 0.0F) {
            var2 = (1.0F + var2) * 0.5F;
         }

         var2 = this.applyArmorModifier(var1, var2);
         var2 = this.applyMagicModifier(var1, var2);
         float var3 = var2;
         var2 = Math.max(var2 - this.getAbsorptionHearts(), 0.0F);
         this.setAbsorptionHearts(this.getAbsorptionHearts() - (var3 - var2));
         if(var2 != 0.0F) {
            this.applyExhaustion(var1.getExhaustionCost());
            float var4 = this.getHealth();
            this.setHealth(this.getHealth() - var2);
            this.bs().a(var1, var4, var2);
            if(var2 < 3.4028235E37F) {
               this.a(StatisticList.x, Math.round(var2 * 10.0F));
            }

         }
      }
   }

   public void openSign(TileEntitySign var1) {
   }

   public void a(CommandBlockListenerAbstract var1) {
   }

   public void openTrade(IMerchant var1) {
   }

   public void openContainer(IInventory var1) {
   }

   public void openHorseInventory(EntityHorse var1, IInventory var2) {
   }

   public void openTileEntity(ITileEntityContainer var1) {
   }

   public void openBook(ItemStack var1) {
   }

   public boolean u(Entity var1) {
      if(this.isSpectator()) {
         if(var1 instanceof IInventory) {
            this.openContainer((IInventory)var1);
         }

         return false;
      } else {
         ItemStack var2 = this.bZ();
         ItemStack var3 = var2 != null?var2.cloneItemStack():null;
         if(!var1.e(this)) {
            if(var2 != null && var1 instanceof EntityLiving) {
               if(this.abilities.canInstantlyBuild) {
                  var2 = var3;
               }

               if(var2.a(this, (EntityLiving)var1)) {
                  if(var2.count <= 0 && !this.abilities.canInstantlyBuild) {
                     this.ca();
                  }

                  return true;
               }
            }

            return false;
         } else {
            if(var2 != null && var2 == this.bZ()) {
               if(var2.count <= 0 && !this.abilities.canInstantlyBuild) {
                  this.ca();
               } else if(var2.count < var3.count && this.abilities.canInstantlyBuild) {
                  var2.count = var3.count;
               }
            }

            return true;
         }
      }
   }

   public ItemStack bZ() {
      return this.inventory.getItemInHand();
   }

   public void ca() {
      this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack)null);
   }

   public double am() {
      return -0.35D;
   }

   public void attack(Entity var1) {
      if(var1.aD()) {
         if(!var1.l(this)) {
            float var2 = (float)this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
            byte var3 = 0;
            float var4 = 0.0F;
            if(var1 instanceof EntityLiving) {
               var4 = EnchantmentManager.a(this.bA(), ((EntityLiving)var1).getMonsterType());
            } else {
               var4 = EnchantmentManager.a(this.bA(), EnumMonsterType.UNDEFINED);
            }

            int var18 = var3 + EnchantmentManager.a((EntityLiving)this);
            if(this.isSprinting()) {
               ++var18;
            }

            if(var2 > 0.0F || var4 > 0.0F) {
               boolean var5 = this.fallDistance > 0.0F && !this.onGround && !this.k_() && !this.V() && !this.hasEffect(MobEffectList.BLINDNESS) && this.vehicle == null && var1 instanceof EntityLiving;
               if(var5 && var2 > 0.0F) {
                  var2 *= 1.5F;
               }

               var2 += var4;
               boolean var6 = false;
               int var7 = EnchantmentManager.getFireAspectEnchantmentLevel(this);
               if(var1 instanceof EntityLiving && var7 > 0 && !var1.isBurning()) {
                  var6 = true;
                  var1.setOnFire(1);
               }

               double var8 = var1.motX;
               double var10 = var1.motY;
               double var12 = var1.motZ;
               boolean var14 = var1.damageEntity(DamageSource.playerAttack(this), var2);
               if(var14) {
                  if(var18 > 0) {
                     var1.g((double)(-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float)var18 * 0.5F), 0.1D, (double)(MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float)var18 * 0.5F));
                     this.motX *= 0.6D;
                     this.motZ *= 0.6D;
                     this.setSprinting(false);
                  }

                  if(var1 instanceof EntityPlayer && var1.velocityChanged) {
                     ((EntityPlayer)var1).playerConnection.sendPacket(new PacketPlayOutEntityVelocity(var1));
                     var1.velocityChanged = false;
                     var1.motX = var8;
                     var1.motY = var10;
                     var1.motZ = var12;
                  }

                  if(var5) {
                     this.b(var1);
                  }

                  if(var4 > 0.0F) {
                     this.c(var1);
                  }

                  if(var2 >= 18.0F) {
                     this.b((Statistic)AchievementList.F);
                  }

                  this.p(var1);
                  if(var1 instanceof EntityLiving) {
                     EnchantmentManager.a((EntityLiving)((EntityLiving)var1), (Entity)this);
                  }

                  EnchantmentManager.b(this, var1);
                  ItemStack var15 = this.bZ();
                  Object var16 = var1;
                  if(var1 instanceof EntityComplexPart) {
                     IComplex var17 = ((EntityComplexPart)var1).owner;
                     if(var17 instanceof EntityLiving) {
                        var16 = (EntityLiving)var17;
                     }
                  }

                  if(var15 != null && var16 instanceof EntityLiving) {
                     var15.a((EntityLiving)var16, this);
                     if(var15.count <= 0) {
                        this.ca();
                     }
                  }

                  if(var1 instanceof EntityLiving) {
                     this.a(StatisticList.w, Math.round(var2 * 10.0F));
                     if(var7 > 0) {
                        var1.setOnFire(var7 * 4);
                     }
                  }

                  this.applyExhaustion(0.3F);
               } else if(var6) {
                  var1.extinguish();
               }
            }

         }
      }
   }

   public void b(Entity var1) {
   }

   public void c(Entity var1) {
   }

   public void die() {
      super.die();
      this.defaultContainer.b(this);
      if(this.activeContainer != null) {
         this.activeContainer.b(this);
      }

   }

   public boolean inBlock() {
      return !this.sleeping && super.inBlock();
   }

   public GameProfile getProfile() {
      return this.bH;
   }

   public EntityHuman.EnumBedResult a(BlockPosition var1) {
      if(!this.world.isClientSide) {
         if(this.isSleeping() || !this.isAlive()) {
            return EntityHuman.EnumBedResult.OTHER_PROBLEM;
         }

         if(!this.world.worldProvider.d()) {
            return EntityHuman.EnumBedResult.NOT_POSSIBLE_HERE;
         }

         if(this.world.w()) {
            return EntityHuman.EnumBedResult.NOT_POSSIBLE_NOW;
         }

         if(Math.abs(this.locX - (double)var1.getX()) > 3.0D || Math.abs(this.locY - (double)var1.getY()) > 2.0D || Math.abs(this.locZ - (double)var1.getZ()) > 3.0D) {
            return EntityHuman.EnumBedResult.TOO_FAR_AWAY;
         }

         double var2 = 8.0D;
         double var4 = 5.0D;
         List var6 = this.world.a(EntityMonster.class, new AxisAlignedBB((double)var1.getX() - var2, (double)var1.getY() - var4, (double)var1.getZ() - var2, (double)var1.getX() + var2, (double)var1.getY() + var4, (double)var1.getZ() + var2));
         if(!var6.isEmpty()) {
            return EntityHuman.EnumBedResult.NOT_SAFE;
         }
      }

      if(this.au()) {
         this.mount((Entity)null);
      }

      this.setSize(0.2F, 0.2F);
      if(this.world.isLoaded(var1)) {
         EnumDirection var7 = (EnumDirection)this.world.getType(var1).get(BlockDirectional.FACING);
         float var3 = 0.5F;
         float var8 = 0.5F;
         switch(EntityHuman.SyntheticClass_1.a[var7.ordinal()]) {
         case 1:
            var8 = 0.9F;
            break;
         case 2:
            var8 = 0.1F;
            break;
         case 3:
            var3 = 0.1F;
            break;
         case 4:
            var3 = 0.9F;
         }

         this.a(var7);
         this.setPosition((double)((float)var1.getX() + var3), (double)((float)var1.getY() + 0.6875F), (double)((float)var1.getZ() + var8));
      } else {
         this.setPosition((double)((float)var1.getX() + 0.5F), (double)((float)var1.getY() + 0.6875F), (double)((float)var1.getZ() + 0.5F));
      }

      this.sleeping = true;
      this.sleepTicks = 0;
      this.bx = var1;
      this.motX = this.motZ = this.motY = 0.0D;
      if(!this.world.isClientSide) {
         this.world.everyoneSleeping();
      }

      return EntityHuman.EnumBedResult.OK;
   }

   private void a(EnumDirection var1) {
      this.by = 0.0F;
      this.bz = 0.0F;
      switch(EntityHuman.SyntheticClass_1.a[var1.ordinal()]) {
      case 1:
         this.bz = -1.8F;
         break;
      case 2:
         this.bz = 1.8F;
         break;
      case 3:
         this.by = 1.8F;
         break;
      case 4:
         this.by = -1.8F;
      }

   }

   public void a(boolean var1, boolean var2, boolean var3) {
      this.setSize(0.6F, 1.8F);
      IBlockData var4 = this.world.getType(this.bx);
      if(this.bx != null && var4.getBlock() == Blocks.BED) {
         this.world.setTypeAndData(this.bx, var4.set(BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
         BlockPosition var5 = BlockBed.a(this.world, this.bx, 0);
         if(var5 == null) {
            var5 = this.bx.up();
         }

         this.setPosition((double)((float)var5.getX() + 0.5F), (double)((float)var5.getY() + 0.1F), (double)((float)var5.getZ() + 0.5F));
      }

      this.sleeping = false;
      if(!this.world.isClientSide && var2) {
         this.world.everyoneSleeping();
      }

      this.sleepTicks = var1?0:100;
      if(var3) {
         this.setRespawnPosition(this.bx, false);
      }

   }

   private boolean p() {
      return this.world.getType(this.bx).getBlock() == Blocks.BED;
   }

   public static BlockPosition getBed(World var0, BlockPosition var1, boolean var2) {
      Block var3 = var0.getType(var1).getBlock();
      if(var3 != Blocks.BED) {
         if(!var2) {
            return null;
         } else {
            boolean var4 = var3.g();
            boolean var5 = var0.getType(var1.up()).getBlock().g();
            return var4 && var5?var1:null;
         }
      } else {
         return BlockBed.a(var0, var1, 0);
      }
   }

   public boolean isSleeping() {
      return this.sleeping;
   }

   public boolean isDeeplySleeping() {
      return this.sleeping && this.sleepTicks >= 100;
   }

   public void b(IChatBaseComponent var1) {
   }

   public BlockPosition getBed() {
      return this.c;
   }

   public boolean isRespawnForced() {
      return this.d;
   }

   public void setRespawnPosition(BlockPosition var1, boolean var2) {
      if(var1 != null) {
         this.c = var1;
         this.d = var2;
      } else {
         this.c = null;
         this.d = false;
      }

   }

   public void b(Statistic var1) {
      this.a((Statistic)var1, 1);
   }

   public void a(Statistic var1, int var2) {
   }

   public void a(Statistic var1) {
   }

   public void bF() {
      super.bF();
      this.b(StatisticList.u);
      if(this.isSprinting()) {
         this.applyExhaustion(0.8F);
      } else {
         this.applyExhaustion(0.2F);
      }

   }

   public void g(float var1, float var2) {
      double var3 = this.locX;
      double var5 = this.locY;
      double var7 = this.locZ;
      if(this.abilities.isFlying && this.vehicle == null) {
         double var9 = this.motY;
         float var11 = this.aM;
         this.aM = this.abilities.a() * (float)(this.isSprinting()?2:1);
         super.g(var1, var2);
         this.motY = var9 * 0.6D;
         this.aM = var11;
      } else {
         super.g(var1, var2);
      }

      this.checkMovement(this.locX - var3, this.locY - var5, this.locZ - var7);
   }

   public float bI() {
      return (float)this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
   }

   public void checkMovement(double var1, double var3, double var5) {
      if(this.vehicle == null) {
         int var7;
         if(this.a((Material)Material.WATER)) {
            var7 = Math.round(MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
            if(var7 > 0) {
               this.a(StatisticList.p, var7);
               this.applyExhaustion(0.015F * (float)var7 * 0.01F);
            }
         } else if(this.V()) {
            var7 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
            if(var7 > 0) {
               this.a(StatisticList.l, var7);
               this.applyExhaustion(0.015F * (float)var7 * 0.01F);
            }
         } else if(this.k_()) {
            if(var3 > 0.0D) {
               this.a(StatisticList.n, (int)Math.round(var3 * 100.0D));
            }
         } else if(this.onGround) {
            var7 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
            if(var7 > 0) {
               this.a(StatisticList.i, var7);
               if(this.isSprinting()) {
                  this.a(StatisticList.k, var7);
                  this.applyExhaustion(0.099999994F * (float)var7 * 0.01F);
               } else {
                  if(this.isSneaking()) {
                     this.a(StatisticList.j, var7);
                  }

                  this.applyExhaustion(0.01F * (float)var7 * 0.01F);
               }
            }
         } else {
            var7 = Math.round(MathHelper.sqrt(var1 * var1 + var5 * var5) * 100.0F);
            if(var7 > 25) {
               this.a(StatisticList.o, var7);
            }
         }

      }
   }

   private void l(double var1, double var3, double var5) {
      if(this.vehicle != null) {
         int var7 = Math.round(MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5) * 100.0F);
         if(var7 > 0) {
            if(this.vehicle instanceof EntityMinecartAbstract) {
               this.a(StatisticList.q, var7);
               if(this.e == null) {
                  this.e = new BlockPosition(this);
               } else if(this.e.c((double)MathHelper.floor(this.locX), (double)MathHelper.floor(this.locY), (double)MathHelper.floor(this.locZ)) >= 1000000.0D) {
                  this.b((Statistic)AchievementList.q);
               }
            } else if(this.vehicle instanceof EntityBoat) {
               this.a(StatisticList.r, var7);
            } else if(this.vehicle instanceof EntityPig) {
               this.a(StatisticList.s, var7);
            } else if(this.vehicle instanceof EntityHorse) {
               this.a(StatisticList.t, var7);
            }
         }
      }

   }

   public void e(float var1, float var2) {
      if(!this.abilities.canFly) {
         if(var1 >= 2.0F) {
            this.a(StatisticList.m, (int)Math.round((double)var1 * 100.0D));
         }

         super.e(var1, var2);
      }
   }

   protected void X() {
      if(!this.isSpectator()) {
         super.X();
      }

   }

   protected String n(int var1) {
      return var1 > 4?"game.player.hurt.fall.big":"game.player.hurt.fall.small";
   }

   public void a(EntityLiving var1) {
      if(var1 instanceof IMonster) {
         this.b((Statistic)AchievementList.s);
      }

      EntityTypes.MonsterEggInfo var2 = (EntityTypes.MonsterEggInfo)EntityTypes.eggInfo.get(Integer.valueOf(EntityTypes.a(var1)));
      if(var2 != null) {
         this.b(var2.killEntityStatistic);
      }

   }

   public void aA() {
      if(!this.abilities.isFlying) {
         super.aA();
      }

   }

   public ItemStack q(int var1) {
      return this.inventory.e(var1);
   }

   public void giveExp(int var1) {
      this.addScore(var1);
      int var2 = Integer.MAX_VALUE - this.expTotal;
      if(var1 > var2) {
         var1 = var2;
      }

      this.exp += (float)var1 / (float)this.getExpToLevel();

      for(this.expTotal += var1; this.exp >= 1.0F; this.exp /= (float)this.getExpToLevel()) {
         this.exp = (this.exp - 1.0F) * (float)this.getExpToLevel();
         this.levelDown(1);
      }

   }

   public int cj() {
      return this.f;
   }

   public void b(int var1) {
      this.expLevel -= var1;
      if(this.expLevel < 0) {
         this.expLevel = 0;
         this.exp = 0.0F;
         this.expTotal = 0;
      }

      this.f = this.random.nextInt();
   }

   public void levelDown(int var1) {
      this.expLevel += var1;
      if(this.expLevel < 0) {
         this.expLevel = 0;
         this.exp = 0.0F;
         this.expTotal = 0;
      }

      if(var1 > 0 && this.expLevel % 5 == 0 && (float)this.i < (float)this.ticksLived - 100.0F) {
         float var2 = this.expLevel > 30?1.0F:(float)this.expLevel / 30.0F;
         this.world.makeSound(this, "random.levelup", var2 * 0.75F, 1.0F);
         this.i = this.ticksLived;
      }

   }

   public int getExpToLevel() {
      return this.expLevel >= 30?112 + (this.expLevel - 30) * 9:(this.expLevel >= 15?37 + (this.expLevel - 15) * 5:7 + this.expLevel * 2);
   }

   public void applyExhaustion(float var1) {
      if(!this.abilities.isInvulnerable) {
         if(!this.world.isClientSide) {
            this.foodData.a(var1);
         }

      }
   }

   public FoodMetaData getFoodData() {
      return this.foodData;
   }

   public boolean j(boolean var1) {
      return (var1 || this.foodData.c()) && !this.abilities.isInvulnerable;
   }

   public boolean cm() {
      return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
   }

   public void a(ItemStack var1, int var2) {
      if(var1 != this.g) {
         this.g = var1;
         this.h = var2;
         if(!this.world.isClientSide) {
            this.f(true);
         }

      }
   }

   public boolean cn() {
      return this.abilities.mayBuild;
   }

   public boolean a(BlockPosition var1, EnumDirection var2, ItemStack var3) {
      if(this.abilities.mayBuild) {
         return true;
      } else if(var3 == null) {
         return false;
      } else {
         BlockPosition var4 = var1.shift(var2.opposite());
         Block var5 = this.world.getType(var4).getBlock();
         return var3.d(var5) || var3.x();
      }
   }

   protected int getExpValue(EntityHuman var1) {
      if(this.world.getGameRules().getBoolean("keepInventory")) {
         return 0;
      } else {
         int var2 = this.expLevel * 7;
         return var2 > 100?100:var2;
      }
   }

   protected boolean alwaysGivesExp() {
      return true;
   }

   public void copyTo(EntityHuman var1, boolean var2) {
      if(var2) {
         this.inventory.b(var1.inventory);
         this.setHealth(var1.getHealth());
         this.foodData = var1.foodData;
         this.expLevel = var1.expLevel;
         this.expTotal = var1.expTotal;
         this.exp = var1.exp;
         this.setScore(var1.getScore());
         this.an = var1.an;
         this.ao = var1.ao;
         this.ap = var1.ap;
      } else if(this.world.getGameRules().getBoolean("keepInventory")) {
         this.inventory.b(var1.inventory);
         this.expLevel = var1.expLevel;
         this.expTotal = var1.expTotal;
         this.exp = var1.exp;
         this.setScore(var1.getScore());
      }

      this.f = var1.f;
      this.enderChest = var1.enderChest;
      this.getDataWatcher().watch(10, Byte.valueOf(var1.getDataWatcher().getByte(10)));
   }

   protected boolean s_() {
      return !this.abilities.isFlying;
   }

   public void updateAbilities() {
   }

   public void a(WorldSettings.EnumGamemode var1) {
   }

   public String getName() {
      return this.bH.getName();
   }

   public InventoryEnderChest getEnderChest() {
      return this.enderChest;
   }

   public ItemStack getEquipment(int var1) {
      return var1 == 0?this.inventory.getItemInHand():this.inventory.armor[var1 - 1];
   }

   public ItemStack bA() {
      return this.inventory.getItemInHand();
   }

   public void setEquipment(int var1, ItemStack var2) {
      this.inventory.armor[var1] = var2;
   }

   public abstract boolean isSpectator();

   public ItemStack[] getEquipment() {
      return this.inventory.armor;
   }

   public boolean aL() {
      return !this.abilities.isFlying;
   }

   public Scoreboard getScoreboard() {
      return this.world.getScoreboard();
   }

   public ScoreboardTeamBase getScoreboardTeam() {
      return this.getScoreboard().getPlayerTeam(this.getName());
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      ChatComponentText var1 = new ChatComponentText(ScoreboardTeam.getPlayerDisplayName(this.getScoreboardTeam(), this.getName()));
      var1.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
      var1.getChatModifier().setChatHoverable(this.aQ());
      var1.getChatModifier().setInsertion(this.getName());
      return var1;
   }

   public float getHeadHeight() {
      float var1 = 1.62F;
      if(this.isSleeping()) {
         var1 = 0.2F;
      }

      if(this.isSneaking()) {
         var1 -= 0.08F;
      }

      return var1;
   }

   public void setAbsorptionHearts(float var1) {
      if(var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.getDataWatcher().watch(17, Float.valueOf(var1));
   }

   public float getAbsorptionHearts() {
      return this.getDataWatcher().getFloat(17);
   }

   public static UUID a(GameProfile var0) {
      UUID var1 = var0.getId();
      if(var1 == null) {
         var1 = b(var0.getName());
      }

      return var1;
   }

   public static UUID b(String var0) {
      return UUID.nameUUIDFromBytes(("OfflinePlayer:" + var0).getBytes(Charsets.UTF_8));
   }

   public boolean a(ChestLock var1) {
      if(var1.a()) {
         return true;
      } else {
         ItemStack var2 = this.bZ();
         return var2 != null && var2.hasName()?var2.getName().equals(var1.b()):false;
      }
   }

   public boolean getSendCommandFeedback() {
      return MinecraftServer.getServer().worldServer[0].getGameRules().getBoolean("sendCommandFeedback");
   }

   public boolean d(int var1, ItemStack var2) {
      if(var1 >= 0 && var1 < this.inventory.items.length) {
         this.inventory.setItem(var1, var2);
         return true;
      } else {
         int var3 = var1 - 100;
         int var4;
         if(var3 >= 0 && var3 < this.inventory.armor.length) {
            var4 = var3 + 1;
            if(var2 != null && var2.getItem() != null) {
               if(var2.getItem() instanceof ItemArmor) {
                  if(EntityInsentient.c(var2) != var4) {
                     return false;
                  }
               } else if(var4 != 4 || var2.getItem() != Items.SKULL && !(var2.getItem() instanceof ItemBlock)) {
                  return false;
               }
            }

            this.inventory.setItem(var3 + this.inventory.items.length, var2);
            return true;
         } else {
            var4 = var1 - 200;
            if(var4 >= 0 && var4 < this.enderChest.getSize()) {
               this.enderChest.setItem(var4, var2);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.SOUTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.NORTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumBedResult {
      OK,
      NOT_POSSIBLE_HERE,
      NOT_POSSIBLE_NOW,
      TOO_FAR_AWAY,
      OTHER_PROBLEM,
      NOT_SAFE;

      private EnumBedResult() {
      }
   }

   public static enum EnumChatVisibility {
      FULL(0, "options.chat.visibility.full"),
      SYSTEM(1, "options.chat.visibility.system"),
      HIDDEN(2, "options.chat.visibility.hidden");

      private static final EntityHuman.EnumChatVisibility[] d;
      private final int e;
      private final String f;

      private EnumChatVisibility(int var3, String var4) {
         this.e = var3;
         this.f = var4;
      }

      public int a() {
         return this.e;
      }

      public static EntityHuman.EnumChatVisibility a(int var0) {
         return d[var0 % d.length];
      }

      static {
         d = new EntityHuman.EnumChatVisibility[values().length];
         EntityHuman.EnumChatVisibility[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            EntityHuman.EnumChatVisibility var3 = var0[var2];
            d[var3.e] = var3;
         }

      }
   }
}
