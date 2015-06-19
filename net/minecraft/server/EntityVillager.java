package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWitch;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.EnumColor;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IMerchant;
import net.minecraft.server.IMonster;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MerchantRecipe;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NPC;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalAvoidTarget;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalInteract;
import net.minecraft.server.PathfinderGoalInteractVillagers;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalLookAtTradingPlayer;
import net.minecraft.server.PathfinderGoalMakeLove;
import net.minecraft.server.PathfinderGoalMoveIndoors;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalOpenDoor;
import net.minecraft.server.PathfinderGoalPlay;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRestrictOpenDoor;
import net.minecraft.server.PathfinderGoalTakeFlower;
import net.minecraft.server.PathfinderGoalTradeWithPlayer;
import net.minecraft.server.PathfinderGoalVillagerFarm;
import net.minecraft.server.StatisticList;
import net.minecraft.server.Tuple;
import net.minecraft.server.Village;
import net.minecraft.server.WeightedRandomEnchant;
import net.minecraft.server.World;

public class EntityVillager extends EntityAgeable implements IMerchant, NPC {
   private int profession;
   private boolean bo;
   private boolean bp;
   Village village;
   private EntityHuman tradingPlayer;
   private MerchantRecipeList br;
   private int bs;
   private boolean bt;
   private boolean bu;
   private int riches;
   private String bw;
   private int bx;
   private int by;
   private boolean bz;
   private boolean bA;
   public InventorySubcontainer inventory;
   private static final EntityVillager.IMerchantRecipeOption[][][][] bC;

   public EntityVillager(World var1) {
      this(var1, 0);
   }

   public EntityVillager(World var1, int var2) {
      super(var1);
      this.inventory = new InventorySubcontainer("Items", false, 8);
      this.setProfession(var2);
      this.setSize(0.6F, 1.8F);
      ((Navigation)this.getNavigation()).b(true);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalAvoidTarget(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
      this.goalSelector.a(1, new PathfinderGoalTradeWithPlayer(this));
      this.goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(this));
      this.goalSelector.a(2, new PathfinderGoalMoveIndoors(this));
      this.goalSelector.a(3, new PathfinderGoalRestrictOpenDoor(this));
      this.goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
      this.goalSelector.a(6, new PathfinderGoalMakeLove(this));
      this.goalSelector.a(7, new PathfinderGoalTakeFlower(this));
      this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
      this.goalSelector.a(9, new PathfinderGoalInteractVillagers(this));
      this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
      this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
      this.j(true);
   }

   private void cv() {
      if(!this.bA) {
         this.bA = true;
         if(this.isBaby()) {
            this.goalSelector.a(8, new PathfinderGoalPlay(this, 0.32D));
         } else if(this.getProfession() == 0) {
            this.goalSelector.a(6, new PathfinderGoalVillagerFarm(this, 0.6D));
         }

      }
   }

   protected void n() {
      if(this.getProfession() == 0) {
         this.goalSelector.a(8, new PathfinderGoalVillagerFarm(this, 0.6D));
      }

      super.n();
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
   }

   protected void E() {
      if(--this.profession <= 0) {
         BlockPosition var1 = new BlockPosition(this);
         this.world.ae().a(var1);
         this.profession = 70 + this.random.nextInt(50);
         this.village = this.world.ae().getClosestVillage(var1, 32);
         if(this.village == null) {
            this.cj();
         } else {
            BlockPosition var2 = this.village.a();
            this.a(var2, (int)((float)this.village.b() * 1.0F));
            if(this.bz) {
               this.bz = false;
               this.village.b(5);
            }
         }
      }

      if(!this.co() && this.bs > 0) {
         --this.bs;
         if(this.bs <= 0) {
            if(this.bt) {
               Iterator var3 = this.br.iterator();

               while(var3.hasNext()) {
                  MerchantRecipe var4 = (MerchantRecipe)var3.next();
                  if(var4.h()) {
                     var4.a(this.random.nextInt(6) + this.random.nextInt(6) + 2);
                  }
               }

               this.cw();
               this.bt = false;
               if(this.village != null && this.bw != null) {
                  this.world.broadcastEntityEffect(this, (byte)14);
                  this.village.a(this.bw, 1);
               }
            }

            this.addEffect(new MobEffect(MobEffectList.REGENERATION.id, 200, 0));
         }
      }

      super.E();
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      boolean var3 = var2 != null && var2.getItem() == Items.SPAWN_EGG;
      if(!var3 && this.isAlive() && !this.co() && !this.isBaby()) {
         if(!this.world.isClientSide && (this.br == null || this.br.size() > 0)) {
            this.a_(var1);
            var1.openTrade(this);
         }

         var1.b(StatisticList.F);
         return true;
      } else {
         return super.a(var1);
      }
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, Integer.valueOf(0));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Profession", this.getProfession());
      var1.setInt("Riches", this.riches);
      var1.setInt("Career", this.bx);
      var1.setInt("CareerLevel", this.by);
      var1.setBoolean("Willing", this.bu);
      if(this.br != null) {
         var1.set("Offers", this.br.a());
      }

      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.inventory.getSize(); ++var3) {
         ItemStack var4 = this.inventory.getItem(var3);
         if(var4 != null) {
            var2.add(var4.save(new NBTTagCompound()));
         }
      }

      var1.set("Inventory", var2);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setProfession(var1.getInt("Profession"));
      this.riches = var1.getInt("Riches");
      this.bx = var1.getInt("Career");
      this.by = var1.getInt("CareerLevel");
      this.bu = var1.getBoolean("Willing");
      if(var1.hasKeyOfType("Offers", 10)) {
         NBTTagCompound var2 = var1.getCompound("Offers");
         this.br = new MerchantRecipeList(var2);
      }

      NBTTagList var5 = var1.getList("Inventory", 10);

      for(int var3 = 0; var3 < var5.size(); ++var3) {
         ItemStack var4 = ItemStack.createStack(var5.get(var3));
         if(var4 != null) {
            this.inventory.a(var4);
         }
      }

      this.j(true);
      this.cv();
   }

   protected boolean isTypeNotPersistent() {
      return false;
   }

   protected String z() {
      return this.co()?"mob.villager.haggle":"mob.villager.idle";
   }

   protected String bo() {
      return "mob.villager.hit";
   }

   protected String bp() {
      return "mob.villager.death";
   }

   public void setProfession(int var1) {
      this.datawatcher.watch(16, Integer.valueOf(var1));
   }

   public int getProfession() {
      return Math.max(this.datawatcher.getInt(16) % 5, 0);
   }

   public boolean cm() {
      return this.bo;
   }

   public void l(boolean var1) {
      this.bo = var1;
   }

   public void m(boolean var1) {
      this.bp = var1;
   }

   public boolean cn() {
      return this.bp;
   }

   public void b(EntityLiving var1) {
      super.b(var1);
      if(this.village != null && var1 != null) {
         this.village.a(var1);
         if(var1 instanceof EntityHuman) {
            byte var2 = -1;
            if(this.isBaby()) {
               var2 = -3;
            }

            this.village.a(var1.getName(), var2);
            if(this.isAlive()) {
               this.world.broadcastEntityEffect(this, (byte)13);
            }
         }
      }

   }

   public void die(DamageSource var1) {
      if(this.village != null) {
         Entity var2 = var1.getEntity();
         if(var2 != null) {
            if(var2 instanceof EntityHuman) {
               this.village.a(var2.getName(), -2);
            } else if(var2 instanceof IMonster) {
               this.village.h();
            }
         } else {
            EntityHuman var3 = this.world.findNearbyPlayer(this, 16.0D);
            if(var3 != null) {
               this.village.h();
            }
         }
      }

      super.die(var1);
   }

   public void a_(EntityHuman var1) {
      this.tradingPlayer = var1;
   }

   public EntityHuman v_() {
      return this.tradingPlayer;
   }

   public boolean co() {
      return this.tradingPlayer != null;
   }

   public boolean n(boolean var1) {
      if(!this.bu && var1 && this.cr()) {
         boolean var2 = false;

         for(int var3 = 0; var3 < this.inventory.getSize(); ++var3) {
            ItemStack var4 = this.inventory.getItem(var3);
            if(var4 != null) {
               if(var4.getItem() == Items.BREAD && var4.count >= 3) {
                  var2 = true;
                  this.inventory.splitStack(var3, 3);
               } else if((var4.getItem() == Items.POTATO || var4.getItem() == Items.CARROT) && var4.count >= 12) {
                  var2 = true;
                  this.inventory.splitStack(var3, 12);
               }
            }

            if(var2) {
               this.world.broadcastEntityEffect(this, (byte)18);
               this.bu = true;
               break;
            }
         }
      }

      return this.bu;
   }

   public void o(boolean var1) {
      this.bu = var1;
   }

   public void a(MerchantRecipe var1) {
      var1.g();
      this.a_ = -this.w();
      this.makeSound("mob.villager.yes", this.bB(), this.bC());
      int var2 = 3 + this.random.nextInt(4);
      if(var1.e() == 1 || this.random.nextInt(5) == 0) {
         this.bs = 40;
         this.bt = true;
         this.bu = true;
         if(this.tradingPlayer != null) {
            this.bw = this.tradingPlayer.getName();
         } else {
            this.bw = null;
         }

         var2 += 5;
      }

      if(var1.getBuyItem1().getItem() == Items.EMERALD) {
         this.riches += var1.getBuyItem1().count;
      }

      if(var1.j()) {
         this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY + 0.5D, this.locZ, var2));
      }

   }

   public void a_(ItemStack var1) {
      if(!this.world.isClientSide && this.a_ > -this.w() + 20) {
         this.a_ = -this.w();
         if(var1 != null) {
            this.makeSound("mob.villager.yes", this.bB(), this.bC());
         } else {
            this.makeSound("mob.villager.no", this.bB(), this.bC());
         }
      }

   }

   public MerchantRecipeList getOffers(EntityHuman var1) {
      if(this.br == null) {
         this.cw();
      }

      return this.br;
   }

   private void cw() {
      EntityVillager.IMerchantRecipeOption[][][] var1 = bC[this.getProfession()];
      if(this.bx != 0 && this.by != 0) {
         ++this.by;
      } else {
         this.bx = this.random.nextInt(var1.length) + 1;
         this.by = 1;
      }

      if(this.br == null) {
         this.br = new MerchantRecipeList();
      }

      int var2 = this.bx - 1;
      int var3 = this.by - 1;
      EntityVillager.IMerchantRecipeOption[][] var4 = var1[var2];
      if(var3 >= 0 && var3 < var4.length) {
         EntityVillager.IMerchantRecipeOption[] var5 = var4[var3];
         EntityVillager.IMerchantRecipeOption[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            EntityVillager.IMerchantRecipeOption var9 = var6[var8];
            var9.a(this.br, this.random);
         }
      }

   }

   public IChatBaseComponent getScoreboardDisplayName() {
      String var1 = this.getCustomName();
      if(var1 != null && var1.length() > 0) {
         ChatComponentText var4 = new ChatComponentText(var1);
         var4.getChatModifier().setChatHoverable(this.aQ());
         var4.getChatModifier().setInsertion(this.getUniqueID().toString());
         return var4;
      } else {
         if(this.br == null) {
            this.cw();
         }

         String var2 = null;
         switch(this.getProfession()) {
         case 0:
            if(this.bx == 1) {
               var2 = "farmer";
            } else if(this.bx == 2) {
               var2 = "fisherman";
            } else if(this.bx == 3) {
               var2 = "shepherd";
            } else if(this.bx == 4) {
               var2 = "fletcher";
            }
            break;
         case 1:
            var2 = "librarian";
            break;
         case 2:
            var2 = "cleric";
            break;
         case 3:
            if(this.bx == 1) {
               var2 = "armor";
            } else if(this.bx == 2) {
               var2 = "weapon";
            } else if(this.bx == 3) {
               var2 = "tool";
            }
            break;
         case 4:
            if(this.bx == 1) {
               var2 = "butcher";
            } else if(this.bx == 2) {
               var2 = "leather";
            }
         }

         if(var2 != null) {
            ChatMessage var3 = new ChatMessage("entity.Villager." + var2, new Object[0]);
            var3.getChatModifier().setChatHoverable(this.aQ());
            var3.getChatModifier().setInsertion(this.getUniqueID().toString());
            return var3;
         } else {
            return super.getScoreboardDisplayName();
         }
      }
   }

   public float getHeadHeight() {
      float var1 = 1.62F;
      if(this.isBaby()) {
         var1 = (float)((double)var1 - 0.81D);
      }

      return var1;
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      var2 = super.prepare(var1, var2);
      this.setProfession(this.world.random.nextInt(5));
      this.cv();
      return var2;
   }

   public void cp() {
      this.bz = true;
   }

   public EntityVillager b(EntityAgeable var1) {
      EntityVillager var2 = new EntityVillager(this.world);
      var2.prepare(this.world.E(new BlockPosition(var2)), (GroupDataEntity)null);
      return var2;
   }

   public boolean cb() {
      return false;
   }

   public void onLightningStrike(EntityLightning var1) {
      if(!this.world.isClientSide && !this.dead) {
         EntityWitch var2 = new EntityWitch(this.world);
         var2.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
         var2.prepare(this.world.E(new BlockPosition(var2)), (GroupDataEntity)null);
         var2.k(this.ce());
         if(this.hasCustomName()) {
            var2.setCustomName(this.getCustomName());
            var2.setCustomNameVisible(this.getCustomNameVisible());
         }

         this.world.addEntity(var2);
         this.die();
      }
   }

   public InventorySubcontainer cq() {
      return this.inventory;
   }

   protected void a(EntityItem var1) {
      ItemStack var2 = var1.getItemStack();
      Item var3 = var2.getItem();
      if(this.a(var3)) {
         ItemStack var4 = this.inventory.a(var2);
         if(var4 == null) {
            var1.die();
         } else {
            var2.count = var4.count;
         }
      }

   }

   private boolean a(Item var1) {
      return var1 == Items.BREAD || var1 == Items.POTATO || var1 == Items.CARROT || var1 == Items.WHEAT || var1 == Items.WHEAT_SEEDS;
   }

   public boolean cr() {
      return this.s(1);
   }

   public boolean cs() {
      return this.s(2);
   }

   public boolean ct() {
      boolean var1 = this.getProfession() == 0;
      return var1?!this.s(5):!this.s(1);
   }

   private boolean s(int var1) {
      boolean var2 = this.getProfession() == 0;

      for(int var3 = 0; var3 < this.inventory.getSize(); ++var3) {
         ItemStack var4 = this.inventory.getItem(var3);
         if(var4 != null) {
            if(var4.getItem() == Items.BREAD && var4.count >= 3 * var1 || var4.getItem() == Items.POTATO && var4.count >= 12 * var1 || var4.getItem() == Items.CARROT && var4.count >= 12 * var1) {
               return true;
            }

            if(var2 && var4.getItem() == Items.WHEAT && var4.count >= 9 * var1) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean cu() {
      for(int var1 = 0; var1 < this.inventory.getSize(); ++var1) {
         ItemStack var2 = this.inventory.getItem(var1);
         if(var2 != null && (var2.getItem() == Items.WHEAT_SEEDS || var2.getItem() == Items.POTATO || var2.getItem() == Items.CARROT)) {
            return true;
         }
      }

      return false;
   }

   public boolean d(int var1, ItemStack var2) {
      if(super.d(var1, var2)) {
         return true;
      } else {
         int var3 = var1 - 300;
         if(var3 >= 0 && var3 < this.inventory.getSize()) {
            this.inventory.setItem(var3, var2);
            return true;
         } else {
            return false;
         }
      }
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }

   static {
      bC = new EntityVillager.IMerchantRecipeOption[][][][]{{{{new EntityVillager.MerchantRecipeOptionBuy(Items.WHEAT, new EntityVillager.MerchantOptionRandomRange(18, 22)), new EntityVillager.MerchantRecipeOptionBuy(Items.POTATO, new EntityVillager.MerchantOptionRandomRange(15, 19)), new EntityVillager.MerchantRecipeOptionBuy(Items.CARROT, new EntityVillager.MerchantOptionRandomRange(15, 19)), new EntityVillager.MerchantRecipeOptionSell(Items.BREAD, new EntityVillager.MerchantOptionRandomRange(-4, -2))}, {new EntityVillager.MerchantRecipeOptionBuy(Item.getItemOf(Blocks.PUMPKIN), new EntityVillager.MerchantOptionRandomRange(8, 13)), new EntityVillager.MerchantRecipeOptionSell(Items.PUMPKIN_PIE, new EntityVillager.MerchantOptionRandomRange(-3, -2))}, {new EntityVillager.MerchantRecipeOptionBuy(Item.getItemOf(Blocks.MELON_BLOCK), new EntityVillager.MerchantOptionRandomRange(7, 12)), new EntityVillager.MerchantRecipeOptionSell(Items.APPLE, new EntityVillager.MerchantOptionRandomRange(-5, -7))}, {new EntityVillager.MerchantRecipeOptionSell(Items.COOKIE, new EntityVillager.MerchantOptionRandomRange(-6, -10)), new EntityVillager.MerchantRecipeOptionSell(Items.CAKE, new EntityVillager.MerchantOptionRandomRange(1, 1))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Items.STRING, new EntityVillager.MerchantOptionRandomRange(15, 20)), new EntityVillager.MerchantRecipeOptionBuy(Items.COAL, new EntityVillager.MerchantOptionRandomRange(16, 24)), new EntityVillager.MerchantRecipeOptionProcess(Items.FISH, new EntityVillager.MerchantOptionRandomRange(6, 6), Items.COOKED_FISH, new EntityVillager.MerchantOptionRandomRange(6, 6))}, {new EntityVillager.MerchantRecipeOptionEnchant(Items.FISHING_ROD, new EntityVillager.MerchantOptionRandomRange(7, 8))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Item.getItemOf(Blocks.WOOL), new EntityVillager.MerchantOptionRandomRange(16, 22)), new EntityVillager.MerchantRecipeOptionSell(Items.SHEARS, new EntityVillager.MerchantOptionRandomRange(3, 4))}, {new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 0), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 1), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 2), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 3), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 4), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 5), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 6), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 7), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 8), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 9), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 10), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 11), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 12), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 13), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 14), new EntityVillager.MerchantOptionRandomRange(1, 2)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, 15), new EntityVillager.MerchantOptionRandomRange(1, 2))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Items.STRING, new EntityVillager.MerchantOptionRandomRange(15, 20)), new EntityVillager.MerchantRecipeOptionSell(Items.ARROW, new EntityVillager.MerchantOptionRandomRange(-12, -8))}, {new EntityVillager.MerchantRecipeOptionSell(Items.BOW, new EntityVillager.MerchantOptionRandomRange(2, 3)), new EntityVillager.MerchantRecipeOptionProcess(Item.getItemOf(Blocks.GRAVEL), new EntityVillager.MerchantOptionRandomRange(10, 10), Items.FLINT, new EntityVillager.MerchantOptionRandomRange(6, 10))}}}, {{{new EntityVillager.MerchantRecipeOptionBuy(Items.PAPER, new EntityVillager.MerchantOptionRandomRange(24, 36)), new EntityVillager.MerchantRecipeOptionBook()}, {new EntityVillager.MerchantRecipeOptionBuy(Items.BOOK, new EntityVillager.MerchantOptionRandomRange(8, 10)), new EntityVillager.MerchantRecipeOptionSell(Items.COMPASS, new EntityVillager.MerchantOptionRandomRange(10, 12)), new EntityVillager.MerchantRecipeOptionSell(Item.getItemOf(Blocks.BOOKSHELF), new EntityVillager.MerchantOptionRandomRange(3, 4))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.WRITTEN_BOOK, new EntityVillager.MerchantOptionRandomRange(2, 2)), new EntityVillager.MerchantRecipeOptionSell(Items.CLOCK, new EntityVillager.MerchantOptionRandomRange(10, 12)), new EntityVillager.MerchantRecipeOptionSell(Item.getItemOf(Blocks.GLASS), new EntityVillager.MerchantOptionRandomRange(-5, -3))}, {new EntityVillager.MerchantRecipeOptionBook()}, {new EntityVillager.MerchantRecipeOptionBook()}, {new EntityVillager.MerchantRecipeOptionSell(Items.NAME_TAG, new EntityVillager.MerchantOptionRandomRange(20, 22))}}}, {{{new EntityVillager.MerchantRecipeOptionBuy(Items.ROTTEN_FLESH, new EntityVillager.MerchantOptionRandomRange(36, 40)), new EntityVillager.MerchantRecipeOptionBuy(Items.GOLD_INGOT, new EntityVillager.MerchantOptionRandomRange(8, 10))}, {new EntityVillager.MerchantRecipeOptionSell(Items.REDSTONE, new EntityVillager.MerchantOptionRandomRange(-4, -1)), new EntityVillager.MerchantRecipeOptionSell(new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), new EntityVillager.MerchantOptionRandomRange(-2, -1))}, {new EntityVillager.MerchantRecipeOptionSell(Items.ENDER_EYE, new EntityVillager.MerchantOptionRandomRange(7, 11)), new EntityVillager.MerchantRecipeOptionSell(Item.getItemOf(Blocks.GLOWSTONE), new EntityVillager.MerchantOptionRandomRange(-3, -1))}, {new EntityVillager.MerchantRecipeOptionSell(Items.EXPERIENCE_BOTTLE, new EntityVillager.MerchantOptionRandomRange(3, 11))}}}, {{{new EntityVillager.MerchantRecipeOptionBuy(Items.COAL, new EntityVillager.MerchantOptionRandomRange(16, 24)), new EntityVillager.MerchantRecipeOptionSell(Items.IRON_HELMET, new EntityVillager.MerchantOptionRandomRange(4, 6))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.IRON_INGOT, new EntityVillager.MerchantOptionRandomRange(7, 9)), new EntityVillager.MerchantRecipeOptionSell(Items.IRON_CHESTPLATE, new EntityVillager.MerchantOptionRandomRange(10, 14))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.DIAMOND, new EntityVillager.MerchantOptionRandomRange(3, 4)), new EntityVillager.MerchantRecipeOptionEnchant(Items.DIAMOND_CHESTPLATE, new EntityVillager.MerchantOptionRandomRange(16, 19))}, {new EntityVillager.MerchantRecipeOptionSell(Items.CHAINMAIL_BOOTS, new EntityVillager.MerchantOptionRandomRange(5, 7)), new EntityVillager.MerchantRecipeOptionSell(Items.CHAINMAIL_LEGGINGS, new EntityVillager.MerchantOptionRandomRange(9, 11)), new EntityVillager.MerchantRecipeOptionSell(Items.CHAINMAIL_HELMET, new EntityVillager.MerchantOptionRandomRange(5, 7)), new EntityVillager.MerchantRecipeOptionSell(Items.CHAINMAIL_CHESTPLATE, new EntityVillager.MerchantOptionRandomRange(11, 15))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Items.COAL, new EntityVillager.MerchantOptionRandomRange(16, 24)), new EntityVillager.MerchantRecipeOptionSell(Items.IRON_AXE, new EntityVillager.MerchantOptionRandomRange(6, 8))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.IRON_INGOT, new EntityVillager.MerchantOptionRandomRange(7, 9)), new EntityVillager.MerchantRecipeOptionEnchant(Items.IRON_SWORD, new EntityVillager.MerchantOptionRandomRange(9, 10))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.DIAMOND, new EntityVillager.MerchantOptionRandomRange(3, 4)), new EntityVillager.MerchantRecipeOptionEnchant(Items.DIAMOND_SWORD, new EntityVillager.MerchantOptionRandomRange(12, 15)), new EntityVillager.MerchantRecipeOptionEnchant(Items.DIAMOND_AXE, new EntityVillager.MerchantOptionRandomRange(9, 12))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Items.COAL, new EntityVillager.MerchantOptionRandomRange(16, 24)), new EntityVillager.MerchantRecipeOptionEnchant(Items.IRON_SHOVEL, new EntityVillager.MerchantOptionRandomRange(5, 7))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.IRON_INGOT, new EntityVillager.MerchantOptionRandomRange(7, 9)), new EntityVillager.MerchantRecipeOptionEnchant(Items.IRON_PICKAXE, new EntityVillager.MerchantOptionRandomRange(9, 11))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.DIAMOND, new EntityVillager.MerchantOptionRandomRange(3, 4)), new EntityVillager.MerchantRecipeOptionEnchant(Items.DIAMOND_PICKAXE, new EntityVillager.MerchantOptionRandomRange(12, 15))}}}, {{{new EntityVillager.MerchantRecipeOptionBuy(Items.PORKCHOP, new EntityVillager.MerchantOptionRandomRange(14, 18)), new EntityVillager.MerchantRecipeOptionBuy(Items.CHICKEN, new EntityVillager.MerchantOptionRandomRange(14, 18))}, {new EntityVillager.MerchantRecipeOptionBuy(Items.COAL, new EntityVillager.MerchantOptionRandomRange(16, 24)), new EntityVillager.MerchantRecipeOptionSell(Items.COOKED_PORKCHOP, new EntityVillager.MerchantOptionRandomRange(-7, -5)), new EntityVillager.MerchantRecipeOptionSell(Items.COOKED_CHICKEN, new EntityVillager.MerchantOptionRandomRange(-8, -6))}}, {{new EntityVillager.MerchantRecipeOptionBuy(Items.LEATHER, new EntityVillager.MerchantOptionRandomRange(9, 12)), new EntityVillager.MerchantRecipeOptionSell(Items.LEATHER_LEGGINGS, new EntityVillager.MerchantOptionRandomRange(2, 4))}, {new EntityVillager.MerchantRecipeOptionEnchant(Items.LEATHER_CHESTPLATE, new EntityVillager.MerchantOptionRandomRange(7, 12))}, {new EntityVillager.MerchantRecipeOptionSell(Items.SADDLE, new EntityVillager.MerchantOptionRandomRange(8, 10))}}}};
   }

   static class MerchantRecipeOptionProcess implements EntityVillager.IMerchantRecipeOption {
      public ItemStack a;
      public EntityVillager.MerchantOptionRandomRange b;
      public ItemStack c;
      public EntityVillager.MerchantOptionRandomRange d;

      public MerchantRecipeOptionProcess(Item var1, EntityVillager.MerchantOptionRandomRange var2, Item var3, EntityVillager.MerchantOptionRandomRange var4) {
         this.a = new ItemStack(var1);
         this.b = var2;
         this.c = new ItemStack(var3);
         this.d = var4;
      }

      public void a(MerchantRecipeList var1, Random var2) {
         int var3 = 1;
         if(this.b != null) {
            var3 = this.b.a(var2);
         }

         int var4 = 1;
         if(this.d != null) {
            var4 = this.d.a(var2);
         }

         var1.add(new MerchantRecipe(new ItemStack(this.a.getItem(), var3, this.a.getData()), new ItemStack(Items.EMERALD), new ItemStack(this.c.getItem(), var4, this.c.getData())));
      }
   }

   static class MerchantRecipeOptionBook implements EntityVillager.IMerchantRecipeOption {
      public MerchantRecipeOptionBook() {
      }

      public void a(MerchantRecipeList var1, Random var2) {
         Enchantment var3 = Enchantment.b[var2.nextInt(Enchantment.b.length)];
         int var4 = MathHelper.nextInt(var2, var3.getStartLevel(), var3.getMaxLevel());
         ItemStack var5 = Items.ENCHANTED_BOOK.a(new WeightedRandomEnchant(var3, var4));
         int var6 = 2 + var2.nextInt(5 + var4 * 10) + 3 * var4;
         if(var6 > 64) {
            var6 = 64;
         }

         var1.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, var6), var5));
      }
   }

   static class MerchantRecipeOptionEnchant implements EntityVillager.IMerchantRecipeOption {
      public ItemStack a;
      public EntityVillager.MerchantOptionRandomRange b;

      public MerchantRecipeOptionEnchant(Item var1, EntityVillager.MerchantOptionRandomRange var2) {
         this.a = new ItemStack(var1);
         this.b = var2;
      }

      public void a(MerchantRecipeList var1, Random var2) {
         int var3 = 1;
         if(this.b != null) {
            var3 = this.b.a(var2);
         }

         ItemStack var4 = new ItemStack(Items.EMERALD, var3, 0);
         ItemStack var5 = new ItemStack(this.a.getItem(), 1, this.a.getData());
         var5 = EnchantmentManager.a(var2, var5, 5 + var2.nextInt(15));
         var1.add(new MerchantRecipe(var4, var5));
      }
   }

   static class MerchantRecipeOptionSell implements EntityVillager.IMerchantRecipeOption {
      public ItemStack a;
      public EntityVillager.MerchantOptionRandomRange b;

      public MerchantRecipeOptionSell(Item var1, EntityVillager.MerchantOptionRandomRange var2) {
         this.a = new ItemStack(var1);
         this.b = var2;
      }

      public MerchantRecipeOptionSell(ItemStack var1, EntityVillager.MerchantOptionRandomRange var2) {
         this.a = var1;
         this.b = var2;
      }

      public void a(MerchantRecipeList var1, Random var2) {
         int var3 = 1;
         if(this.b != null) {
            var3 = this.b.a(var2);
         }

         ItemStack var4;
         ItemStack var5;
         if(var3 < 0) {
            var4 = new ItemStack(Items.EMERALD, 1, 0);
            var5 = new ItemStack(this.a.getItem(), -var3, this.a.getData());
         } else {
            var4 = new ItemStack(Items.EMERALD, var3, 0);
            var5 = new ItemStack(this.a.getItem(), 1, this.a.getData());
         }

         var1.add(new MerchantRecipe(var4, var5));
      }
   }

   static class MerchantRecipeOptionBuy implements EntityVillager.IMerchantRecipeOption {
      public Item a;
      public EntityVillager.MerchantOptionRandomRange b;

      public MerchantRecipeOptionBuy(Item var1, EntityVillager.MerchantOptionRandomRange var2) {
         this.a = var1;
         this.b = var2;
      }

      public void a(MerchantRecipeList var1, Random var2) {
         int var3 = 1;
         if(this.b != null) {
            var3 = this.b.a(var2);
         }

         var1.add(new MerchantRecipe(new ItemStack(this.a, var3, 0), Items.EMERALD));
      }
   }

   interface IMerchantRecipeOption {
      void a(MerchantRecipeList var1, Random var2);
   }

   static class MerchantOptionRandomRange extends Tuple<Integer, Integer> {
      public MerchantOptionRandomRange(int var1, int var2) {
         super(Integer.valueOf(var1), Integer.valueOf(var2));
      }

      public int a(Random var1) {
         return ((Integer)this.a()).intValue() >= ((Integer)this.b()).intValue()?((Integer)this.a()).intValue():((Integer)this.a()).intValue() + var1.nextInt(((Integer)this.b()).intValue() - ((Integer)this.a()).intValue() + 1);
      }
   }
}
