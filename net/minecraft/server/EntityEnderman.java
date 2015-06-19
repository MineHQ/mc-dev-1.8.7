package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityDamageSourceIndirect;
import net.minecraft.server.EntityEndermite;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityEnderman extends EntityMonster {
   private static final UUID a = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
   private static final AttributeModifier b;
   private static final Set<Block> c;
   private boolean bm;

   public EntityEnderman(World var1) {
      super(var1);
      this.setSize(0.6F, 2.9F);
      this.S = 1.0F;
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.goalSelector.a(10, new EntityEnderman.PathfinderGoalEndermanPlaceBlock(this));
      this.goalSelector.a(11, new EntityEnderman.PathfinderGoalEndermanPickupBlock(this));
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
      this.targetSelector.a(2, new EntityEnderman.PathfinderGoalPlayerWhoLookedAtTarget(this));
      this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate() {
         public boolean a(EntityEndermite var1) {
            return var1.n();
         }

         // $FF: synthetic method
         public boolean apply(Object var1) {
            return this.a((EntityEndermite)var1);
         }
      }));
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(40.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
      this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(7.0D);
      this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(64.0D);
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, new Short((short)0));
      this.datawatcher.a(17, new Byte((byte)0));
      this.datawatcher.a(18, new Byte((byte)0));
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      IBlockData var2 = this.getCarried();
      var1.setShort("carried", (short)Block.getId(var2.getBlock()));
      var1.setShort("carriedData", (short)var2.getBlock().toLegacyData(var2));
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      IBlockData var2;
      if(var1.hasKeyOfType("carried", 8)) {
         var2 = Block.getByName(var1.getString("carried")).fromLegacyData(var1.getShort("carriedData") & '\uffff');
      } else {
         var2 = Block.getById(var1.getShort("carried")).fromLegacyData(var1.getShort("carriedData") & '\uffff');
      }

      this.setCarried(var2);
   }

   private boolean c(EntityHuman var1) {
      ItemStack var2 = var1.inventory.armor[3];
      if(var2 != null && var2.getItem() == Item.getItemOf(Blocks.PUMPKIN)) {
         return false;
      } else {
         Vec3D var3 = var1.d(1.0F).a();
         Vec3D var4 = new Vec3D(this.locX - var1.locX, this.getBoundingBox().b + (double)(this.length / 2.0F) - (var1.locY + (double)var1.getHeadHeight()), this.locZ - var1.locZ);
         double var5 = var4.b();
         var4 = var4.a();
         double var7 = var3.b(var4);
         return var7 > 1.0D - 0.025D / var5?var1.hasLineOfSight(this):false;
      }
   }

   public float getHeadHeight() {
      return 2.55F;
   }

   public void m() {
      if(this.world.isClientSide) {
         for(int var1 = 0; var1 < 2; ++var1) {
            this.world.addParticle(EnumParticle.PORTAL, this.locX + (this.random.nextDouble() - 0.5D) * (double)this.width, this.locY + this.random.nextDouble() * (double)this.length - 0.25D, this.locZ + (this.random.nextDouble() - 0.5D) * (double)this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D, new int[0]);
         }
      }

      this.aY = false;
      super.m();
   }

   protected void E() {
      if(this.U()) {
         this.damageEntity(DamageSource.DROWN, 1.0F);
      }

      if(this.co() && !this.bm && this.random.nextInt(100) == 0) {
         this.a(false);
      }

      if(this.world.w()) {
         float var1 = this.c(1.0F);
         if(var1 > 0.5F && this.world.i(new BlockPosition(this)) && this.random.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
            this.setGoalTarget((EntityLiving)null);
            this.a(false);
            this.bm = false;
            this.n();
         }
      }

      super.E();
   }

   protected boolean n() {
      double var1 = this.locX + (this.random.nextDouble() - 0.5D) * 64.0D;
      double var3 = this.locY + (double)(this.random.nextInt(64) - 32);
      double var5 = this.locZ + (this.random.nextDouble() - 0.5D) * 64.0D;
      return this.k(var1, var3, var5);
   }

   protected boolean b(Entity var1) {
      Vec3D var2 = new Vec3D(this.locX - var1.locX, this.getBoundingBox().b + (double)(this.length / 2.0F) - var1.locY + (double)var1.getHeadHeight(), this.locZ - var1.locZ);
      var2 = var2.a();
      double var3 = 16.0D;
      double var5 = this.locX + (this.random.nextDouble() - 0.5D) * 8.0D - var2.a * var3;
      double var7 = this.locY + (double)(this.random.nextInt(16) - 8) - var2.b * var3;
      double var9 = this.locZ + (this.random.nextDouble() - 0.5D) * 8.0D - var2.c * var3;
      return this.k(var5, var7, var9);
   }

   protected boolean k(double var1, double var3, double var5) {
      double var7 = this.locX;
      double var9 = this.locY;
      double var11 = this.locZ;
      this.locX = var1;
      this.locY = var3;
      this.locZ = var5;
      boolean var13 = false;
      BlockPosition var14 = new BlockPosition(this.locX, this.locY, this.locZ);
      if(this.world.isLoaded(var14)) {
         boolean var15 = false;

         while(!var15 && var14.getY() > 0) {
            BlockPosition var16 = var14.down();
            Block var17 = this.world.getType(var16).getBlock();
            if(var17.getMaterial().isSolid()) {
               var15 = true;
            } else {
               --this.locY;
               var14 = var16;
            }
         }

         if(var15) {
            super.enderTeleportTo(this.locX, this.locY, this.locZ);
            if(this.world.getCubes(this, this.getBoundingBox()).isEmpty() && !this.world.containsLiquid(this.getBoundingBox())) {
               var13 = true;
            }
         }
      }

      if(!var13) {
         this.setPosition(var7, var9, var11);
         return false;
      } else {
         short var28 = 128;

         for(int var29 = 0; var29 < var28; ++var29) {
            double var30 = (double)var29 / ((double)var28 - 1.0D);
            float var19 = (this.random.nextFloat() - 0.5F) * 0.2F;
            float var20 = (this.random.nextFloat() - 0.5F) * 0.2F;
            float var21 = (this.random.nextFloat() - 0.5F) * 0.2F;
            double var22 = var7 + (this.locX - var7) * var30 + (this.random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
            double var24 = var9 + (this.locY - var9) * var30 + this.random.nextDouble() * (double)this.length;
            double var26 = var11 + (this.locZ - var11) * var30 + (this.random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
            this.world.addParticle(EnumParticle.PORTAL, var22, var24, var26, (double)var19, (double)var20, (double)var21, new int[0]);
         }

         this.world.makeSound(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
         this.makeSound("mob.endermen.portal", 1.0F, 1.0F);
         return true;
      }
   }

   protected String z() {
      return this.co()?"mob.endermen.scream":"mob.endermen.idle";
   }

   protected String bo() {
      return "mob.endermen.hit";
   }

   protected String bp() {
      return "mob.endermen.death";
   }

   protected Item getLoot() {
      return Items.ENDER_PEARL;
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      Item var3 = this.getLoot();
      if(var3 != null) {
         int var4 = this.random.nextInt(2 + var2);

         for(int var5 = 0; var5 < var4; ++var5) {
            this.a(var3, 1);
         }
      }

   }

   public void setCarried(IBlockData var1) {
      this.datawatcher.watch(16, Short.valueOf((short)(Block.getCombinedId(var1) & '\uffff')));
   }

   public IBlockData getCarried() {
      return Block.getByCombinedId(this.datawatcher.getShort(16) & '\uffff');
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         if(var1.getEntity() == null || !(var1.getEntity() instanceof EntityEndermite)) {
            if(!this.world.isClientSide) {
               this.a(true);
            }

            if(var1 instanceof EntityDamageSource && var1.getEntity() instanceof EntityHuman) {
               if(var1.getEntity() instanceof EntityPlayer && ((EntityPlayer)var1.getEntity()).playerInteractManager.isCreative()) {
                  this.a(false);
               } else {
                  this.bm = true;
               }
            }

            if(var1 instanceof EntityDamageSourceIndirect) {
               this.bm = false;

               for(int var4 = 0; var4 < 64; ++var4) {
                  if(this.n()) {
                     return true;
                  }
               }

               return false;
            }
         }

         boolean var3 = super.damageEntity(var1, var2);
         if(var1.ignoresArmor() && this.random.nextInt(10) != 0) {
            this.n();
         }

         return var3;
      }
   }

   public boolean co() {
      return this.datawatcher.getByte(18) > 0;
   }

   public void a(boolean var1) {
      this.datawatcher.watch(18, Byte.valueOf((byte)(var1?1:0)));
   }

   static {
      b = (new AttributeModifier(a, "Attacking speed boost", 0.15000000596046448D, 0)).a(false);
      c = Sets.newIdentityHashSet();
      c.add(Blocks.GRASS);
      c.add(Blocks.DIRT);
      c.add(Blocks.SAND);
      c.add(Blocks.GRAVEL);
      c.add(Blocks.YELLOW_FLOWER);
      c.add(Blocks.RED_FLOWER);
      c.add(Blocks.BROWN_MUSHROOM);
      c.add(Blocks.RED_MUSHROOM);
      c.add(Blocks.TNT);
      c.add(Blocks.CACTUS);
      c.add(Blocks.CLAY);
      c.add(Blocks.PUMPKIN);
      c.add(Blocks.MELON_BLOCK);
      c.add(Blocks.MYCELIUM);
   }

   static class PathfinderGoalEndermanPickupBlock extends PathfinderGoal {
      private EntityEnderman enderman;

      public PathfinderGoalEndermanPickupBlock(EntityEnderman var1) {
         this.enderman = var1;
      }

      public boolean a() {
         return !this.enderman.world.getGameRules().getBoolean("mobGriefing")?false:(this.enderman.getCarried().getBlock().getMaterial() != Material.AIR?false:this.enderman.bc().nextInt(20) == 0);
      }

      public void e() {
         Random var1 = this.enderman.bc();
         World var2 = this.enderman.world;
         int var3 = MathHelper.floor(this.enderman.locX - 2.0D + var1.nextDouble() * 4.0D);
         int var4 = MathHelper.floor(this.enderman.locY + var1.nextDouble() * 3.0D);
         int var5 = MathHelper.floor(this.enderman.locZ - 2.0D + var1.nextDouble() * 4.0D);
         BlockPosition var6 = new BlockPosition(var3, var4, var5);
         IBlockData var7 = var2.getType(var6);
         Block var8 = var7.getBlock();
         if(EntityEnderman.c.contains(var8)) {
            this.enderman.setCarried(var7);
            var2.setTypeUpdate(var6, Blocks.AIR.getBlockData());
         }

      }
   }

   static class PathfinderGoalEndermanPlaceBlock extends PathfinderGoal {
      private EntityEnderman a;

      public PathfinderGoalEndermanPlaceBlock(EntityEnderman var1) {
         this.a = var1;
      }

      public boolean a() {
         return !this.a.world.getGameRules().getBoolean("mobGriefing")?false:(this.a.getCarried().getBlock().getMaterial() == Material.AIR?false:this.a.bc().nextInt(2000) == 0);
      }

      public void e() {
         Random var1 = this.a.bc();
         World var2 = this.a.world;
         int var3 = MathHelper.floor(this.a.locX - 1.0D + var1.nextDouble() * 2.0D);
         int var4 = MathHelper.floor(this.a.locY + var1.nextDouble() * 2.0D);
         int var5 = MathHelper.floor(this.a.locZ - 1.0D + var1.nextDouble() * 2.0D);
         BlockPosition var6 = new BlockPosition(var3, var4, var5);
         Block var7 = var2.getType(var6).getBlock();
         Block var8 = var2.getType(var6.down()).getBlock();
         if(this.a(var2, var6, this.a.getCarried().getBlock(), var7, var8)) {
            var2.setTypeAndData(var6, this.a.getCarried(), 3);
            this.a.setCarried(Blocks.AIR.getBlockData());
         }

      }

      private boolean a(World var1, BlockPosition var2, Block var3, Block var4, Block var5) {
         return !var3.canPlace(var1, var2)?false:(var4.getMaterial() != Material.AIR?false:(var5.getMaterial() == Material.AIR?false:var5.d()));
      }
   }

   static class PathfinderGoalPlayerWhoLookedAtTarget extends PathfinderGoalNearestAttackableTarget {
      private EntityHuman g;
      private int h;
      private int i;
      private EntityEnderman j;

      public PathfinderGoalPlayerWhoLookedAtTarget(EntityEnderman var1) {
         super(var1, EntityHuman.class, true);
         this.j = var1;
      }

      public boolean a() {
         double var1 = this.f();
         List var3 = this.e.world.a(EntityHuman.class, this.e.getBoundingBox().grow(var1, 4.0D, var1), this.c);
         Collections.sort(var3, this.b);
         if(var3.isEmpty()) {
            return false;
         } else {
            this.g = (EntityHuman)var3.get(0);
            return true;
         }
      }

      public void c() {
         this.h = 5;
         this.i = 0;
      }

      public void d() {
         this.g = null;
         this.j.a(false);
         AttributeInstance var1 = this.j.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
         var1.c(EntityEnderman.b);
         super.d();
      }

      public boolean b() {
         if(this.g != null) {
            if(!this.j.c(this.g)) {
               return false;
            } else {
               this.j.bm = true;
               this.j.a(this.g, 10.0F, 10.0F);
               return true;
            }
         } else {
            return super.b();
         }
      }

      public void e() {
         if(this.g != null) {
            if(--this.h <= 0) {
               this.d = this.g;
               this.g = null;
               super.c();
               this.j.makeSound("mob.endermen.stare", 1.0F, 1.0F);
               this.j.a(true);
               AttributeInstance var1 = this.j.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
               var1.b(EntityEnderman.b);
            }
         } else {
            if(this.d != null) {
               if(this.d instanceof EntityHuman && this.j.c((EntityHuman)this.d)) {
                  if(this.d.h(this.j) < 16.0D) {
                     this.j.n();
                  }

                  this.i = 0;
               } else if(this.d.h(this.j) > 256.0D && this.i++ >= 30 && this.j.b((Entity)this.d)) {
                  this.i = 0;
               }
            }

            super.e();
         }

      }
   }
}
