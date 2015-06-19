package net.minecraft.server;

import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Vec3D;
import net.minecraft.server.Vector3f;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public class EntityArmorStand extends EntityLiving {
   private static final Vector3f a = new Vector3f(0.0F, 0.0F, 0.0F);
   private static final Vector3f b = new Vector3f(0.0F, 0.0F, 0.0F);
   private static final Vector3f c = new Vector3f(-10.0F, 0.0F, -10.0F);
   private static final Vector3f d = new Vector3f(-15.0F, 0.0F, 10.0F);
   private static final Vector3f e = new Vector3f(-1.0F, 0.0F, -1.0F);
   private static final Vector3f f = new Vector3f(1.0F, 0.0F, 1.0F);
   private final ItemStack[] items;
   private boolean h;
   private long i;
   private int bi;
   private boolean bj;
   public Vector3f headPose;
   public Vector3f bodyPose;
   public Vector3f leftArmPose;
   public Vector3f rightArmPose;
   public Vector3f leftLegPose;
   public Vector3f rightLegPose;

   public EntityArmorStand(World var1) {
      super(var1);
      this.items = new ItemStack[5];
      this.headPose = a;
      this.bodyPose = b;
      this.leftArmPose = c;
      this.rightArmPose = d;
      this.leftLegPose = e;
      this.rightLegPose = f;
      this.b(true);
      this.noclip = this.hasGravity();
      this.setSize(0.5F, 1.975F);
   }

   public EntityArmorStand(World var1, double var2, double var4, double var6) {
      this(var1);
      this.setPosition(var2, var4, var6);
   }

   public boolean bM() {
      return super.bM() && !this.hasGravity();
   }

   protected void h() {
      super.h();
      this.datawatcher.a(10, Byte.valueOf((byte)0));
      this.datawatcher.a(11, a);
      this.datawatcher.a(12, b);
      this.datawatcher.a(13, c);
      this.datawatcher.a(14, d);
      this.datawatcher.a(15, e);
      this.datawatcher.a(16, f);
   }

   public ItemStack bA() {
      return this.items[0];
   }

   public ItemStack getEquipment(int var1) {
      return this.items[var1];
   }

   public void setEquipment(int var1, ItemStack var2) {
      this.items[var1] = var2;
   }

   public ItemStack[] getEquipment() {
      return this.items;
   }

   public boolean d(int var1, ItemStack var2) {
      int var3;
      if(var1 == 99) {
         var3 = 0;
      } else {
         var3 = var1 - 100 + 1;
         if(var3 < 0 || var3 >= this.items.length) {
            return false;
         }
      }

      if(var2 != null && EntityInsentient.c(var2) != var3 && (var3 != 4 || !(var2.getItem() instanceof ItemBlock))) {
         return false;
      } else {
         this.setEquipment(var3, var2);
         return true;
      }
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         NBTTagCompound var4 = new NBTTagCompound();
         if(this.items[var3] != null) {
            this.items[var3].save(var4);
         }

         var2.add(var4);
      }

      var1.set("Equipment", var2);
      if(this.getCustomNameVisible() && (this.getCustomName() == null || this.getCustomName().length() == 0)) {
         var1.setBoolean("CustomNameVisible", this.getCustomNameVisible());
      }

      var1.setBoolean("Invisible", this.isInvisible());
      var1.setBoolean("Small", this.isSmall());
      var1.setBoolean("ShowArms", this.hasArms());
      var1.setInt("DisabledSlots", this.bi);
      var1.setBoolean("NoGravity", this.hasGravity());
      var1.setBoolean("NoBasePlate", this.hasBasePlate());
      if(this.s()) {
         var1.setBoolean("Marker", this.s());
      }

      var1.set("Pose", this.z());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("Equipment", 9)) {
         NBTTagList var2 = var1.getList("Equipment", 10);

         for(int var3 = 0; var3 < this.items.length; ++var3) {
            this.items[var3] = ItemStack.createStack(var2.get(var3));
         }
      }

      this.setInvisible(var1.getBoolean("Invisible"));
      this.setSmall(var1.getBoolean("Small"));
      this.setArms(var1.getBoolean("ShowArms"));
      this.bi = var1.getInt("DisabledSlots");
      this.setGravity(var1.getBoolean("NoGravity"));
      this.setBasePlate(var1.getBoolean("NoBasePlate"));
      this.n(var1.getBoolean("Marker"));
      this.bj = !this.s();
      this.noclip = this.hasGravity();
      NBTTagCompound var4 = var1.getCompound("Pose");
      this.h(var4);
   }

   private void h(NBTTagCompound var1) {
      NBTTagList var2 = var1.getList("Head", 5);
      if(var2.size() > 0) {
         this.setHeadPose(new Vector3f(var2));
      } else {
         this.setHeadPose(a);
      }

      NBTTagList var3 = var1.getList("Body", 5);
      if(var3.size() > 0) {
         this.setBodyPose(new Vector3f(var3));
      } else {
         this.setBodyPose(b);
      }

      NBTTagList var4 = var1.getList("LeftArm", 5);
      if(var4.size() > 0) {
         this.setLeftArmPose(new Vector3f(var4));
      } else {
         this.setLeftArmPose(c);
      }

      NBTTagList var5 = var1.getList("RightArm", 5);
      if(var5.size() > 0) {
         this.setRightArmPose(new Vector3f(var5));
      } else {
         this.setRightArmPose(d);
      }

      NBTTagList var6 = var1.getList("LeftLeg", 5);
      if(var6.size() > 0) {
         this.setLeftLegPose(new Vector3f(var6));
      } else {
         this.setLeftLegPose(e);
      }

      NBTTagList var7 = var1.getList("RightLeg", 5);
      if(var7.size() > 0) {
         this.setRightLegPose(new Vector3f(var7));
      } else {
         this.setRightLegPose(f);
      }

   }

   private NBTTagCompound z() {
      NBTTagCompound var1 = new NBTTagCompound();
      if(!a.equals(this.headPose)) {
         var1.set("Head", this.headPose.a());
      }

      if(!b.equals(this.bodyPose)) {
         var1.set("Body", this.bodyPose.a());
      }

      if(!c.equals(this.leftArmPose)) {
         var1.set("LeftArm", this.leftArmPose.a());
      }

      if(!d.equals(this.rightArmPose)) {
         var1.set("RightArm", this.rightArmPose.a());
      }

      if(!e.equals(this.leftLegPose)) {
         var1.set("LeftLeg", this.leftLegPose.a());
      }

      if(!f.equals(this.rightLegPose)) {
         var1.set("RightLeg", this.rightLegPose.a());
      }

      return var1;
   }

   public boolean ae() {
      return false;
   }

   protected void s(Entity var1) {
   }

   protected void bL() {
      List var1 = this.world.getEntities(this, this.getBoundingBox());
      if(var1 != null && !var1.isEmpty()) {
         for(int var2 = 0; var2 < var1.size(); ++var2) {
            Entity var3 = (Entity)var1.get(var2);
            if(var3 instanceof EntityMinecartAbstract && ((EntityMinecartAbstract)var3).s() == EntityMinecartAbstract.EnumMinecartType.RIDEABLE && this.h(var3) <= 0.2D) {
               var3.collide(this);
            }
         }
      }

   }

   public boolean a(EntityHuman var1, Vec3D var2) {
      if(this.s()) {
         return false;
      } else if(!this.world.isClientSide && !var1.isSpectator()) {
         byte var3 = 0;
         ItemStack var4 = var1.bZ();
         boolean var5 = var4 != null;
         if(var5 && var4.getItem() instanceof ItemArmor) {
            ItemArmor var6 = (ItemArmor)var4.getItem();
            if(var6.b == 3) {
               var3 = 1;
            } else if(var6.b == 2) {
               var3 = 2;
            } else if(var6.b == 1) {
               var3 = 3;
            } else if(var6.b == 0) {
               var3 = 4;
            }
         }

         if(var5 && (var4.getItem() == Items.SKULL || var4.getItem() == Item.getItemOf(Blocks.PUMPKIN))) {
            var3 = 4;
         }

         double var19 = 0.1D;
         double var8 = 0.9D;
         double var10 = 0.4D;
         double var12 = 1.6D;
         byte var14 = 0;
         boolean var15 = this.isSmall();
         double var16 = var15?var2.b * 2.0D:var2.b;
         if(var16 >= 0.1D && var16 < 0.1D + (var15?0.8D:0.45D) && this.items[1] != null) {
            var14 = 1;
         } else if(var16 >= 0.9D + (var15?0.3D:0.0D) && var16 < 0.9D + (var15?1.0D:0.7D) && this.items[3] != null) {
            var14 = 3;
         } else if(var16 >= 0.4D && var16 < 0.4D + (var15?1.0D:0.8D) && this.items[2] != null) {
            var14 = 2;
         } else if(var16 >= 1.6D && this.items[4] != null) {
            var14 = 4;
         }

         boolean var18 = this.items[var14] != null;
         if((this.bi & 1 << var14) != 0 || (this.bi & 1 << var3) != 0) {
            var14 = var3;
            if((this.bi & 1 << var3) != 0) {
               if((this.bi & 1) != 0) {
                  return true;
               }

               var14 = 0;
            }
         }

         if(var5 && var3 == 0 && !this.hasArms()) {
            return true;
         } else {
            if(var5) {
               this.a(var1, var3);
            } else if(var18) {
               this.a(var1, var14);
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private void a(EntityHuman var1, int var2) {
      ItemStack var3 = this.items[var2];
      if(var3 == null || (this.bi & 1 << var2 + 8) == 0) {
         if(var3 != null || (this.bi & 1 << var2 + 16) == 0) {
            int var4 = var1.inventory.itemInHandIndex;
            ItemStack var5 = var1.inventory.getItem(var4);
            ItemStack var6;
            if(var1.abilities.canInstantlyBuild && (var3 == null || var3.getItem() == Item.getItemOf(Blocks.AIR)) && var5 != null) {
               var6 = var5.cloneItemStack();
               var6.count = 1;
               this.setEquipment(var2, var6);
            } else if(var5 != null && var5.count > 1) {
               if(var3 == null) {
                  var6 = var5.cloneItemStack();
                  var6.count = 1;
                  this.setEquipment(var2, var6);
                  --var5.count;
               }
            } else {
               this.setEquipment(var2, var5);
               var1.inventory.setItem(var4, var3);
            }
         }
      }
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.world.isClientSide) {
         return false;
      } else if(DamageSource.OUT_OF_WORLD.equals(var1)) {
         this.die();
         return false;
      } else if(!this.isInvulnerable(var1) && !this.h && !this.s()) {
         if(var1.isExplosion()) {
            this.D();
            this.die();
            return false;
         } else if(DamageSource.FIRE.equals(var1)) {
            if(!this.isBurning()) {
               this.setOnFire(5);
            } else {
               this.a(0.15F);
            }

            return false;
         } else if(DamageSource.BURN.equals(var1) && this.getHealth() > 0.5F) {
            this.a(4.0F);
            return false;
         } else {
            boolean var3 = "arrow".equals(var1.p());
            boolean var4 = "player".equals(var1.p());
            if(!var4 && !var3) {
               return false;
            } else {
               if(var1.i() instanceof EntityArrow) {
                  var1.i().die();
               }

               if(var1.getEntity() instanceof EntityHuman && !((EntityHuman)var1.getEntity()).abilities.mayBuild) {
                  return false;
               } else if(var1.u()) {
                  this.A();
                  this.die();
                  return false;
               } else {
                  long var5 = this.world.getTime();
                  if(var5 - this.i > 5L && !var3) {
                     this.i = var5;
                  } else {
                     this.C();
                     this.A();
                     this.die();
                  }

                  return false;
               }
            }
         }
      } else {
         return false;
      }
   }

   private void A() {
      if(this.world instanceof WorldServer) {
         ((WorldServer)this.world).a(EnumParticle.BLOCK_DUST, this.locX, this.locY + (double)this.length / 1.5D, this.locZ, 10, (double)(this.width / 4.0F), (double)(this.length / 4.0F), (double)(this.width / 4.0F), 0.05D, new int[]{Block.getCombinedId(Blocks.PLANKS.getBlockData())});
      }

   }

   private void a(float var1) {
      float var2 = this.getHealth();
      var2 -= var1;
      if(var2 <= 0.5F) {
         this.D();
         this.die();
      } else {
         this.setHealth(var2);
      }

   }

   private void C() {
      Block.a(this.world, new BlockPosition(this), new ItemStack(Items.ARMOR_STAND));
      this.D();
   }

   private void D() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         if(this.items[var1] != null && this.items[var1].count > 0) {
            if(this.items[var1] != null) {
               Block.a(this.world, (new BlockPosition(this)).up(), this.items[var1]);
            }

            this.items[var1] = null;
         }
      }

   }

   protected float h(float var1, float var2) {
      this.aJ = this.lastYaw;
      this.aI = this.yaw;
      return 0.0F;
   }

   public float getHeadHeight() {
      return this.isBaby()?this.length * 0.5F:this.length * 0.9F;
   }

   public void g(float var1, float var2) {
      if(!this.hasGravity()) {
         super.g(var1, var2);
      }
   }

   public void t_() {
      super.t_();
      Vector3f var1 = this.datawatcher.h(11);
      if(!this.headPose.equals(var1)) {
         this.setHeadPose(var1);
      }

      Vector3f var2 = this.datawatcher.h(12);
      if(!this.bodyPose.equals(var2)) {
         this.setBodyPose(var2);
      }

      Vector3f var3 = this.datawatcher.h(13);
      if(!this.leftArmPose.equals(var3)) {
         this.setLeftArmPose(var3);
      }

      Vector3f var4 = this.datawatcher.h(14);
      if(!this.rightArmPose.equals(var4)) {
         this.setRightArmPose(var4);
      }

      Vector3f var5 = this.datawatcher.h(15);
      if(!this.leftLegPose.equals(var5)) {
         this.setLeftLegPose(var5);
      }

      Vector3f var6 = this.datawatcher.h(16);
      if(!this.rightLegPose.equals(var6)) {
         this.setRightLegPose(var6);
      }

      boolean var7 = this.s();
      if(!this.bj && var7) {
         this.a(false);
      } else {
         if(!this.bj || var7) {
            return;
         }

         this.a(true);
      }

      this.bj = var7;
   }

   private void a(boolean var1) {
      double var2 = this.locX;
      double var4 = this.locY;
      double var6 = this.locZ;
      if(var1) {
         this.setSize(0.5F, 1.975F);
      } else {
         this.setSize(0.0F, 0.0F);
      }

      this.setPosition(var2, var4, var6);
   }

   protected void B() {
      this.setInvisible(this.h);
   }

   public void setInvisible(boolean var1) {
      this.h = var1;
      super.setInvisible(var1);
   }

   public boolean isBaby() {
      return this.isSmall();
   }

   public void G() {
      this.die();
   }

   public boolean aW() {
      return this.isInvisible();
   }

   public void setSmall(boolean var1) {
      byte var2 = this.datawatcher.getByte(10);
      if(var1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 &= -2;
      }

      this.datawatcher.watch(10, Byte.valueOf(var2));
   }

   public boolean isSmall() {
      return (this.datawatcher.getByte(10) & 1) != 0;
   }

   public void setGravity(boolean var1) {
      byte var2 = this.datawatcher.getByte(10);
      if(var1) {
         var2 = (byte)(var2 | 2);
      } else {
         var2 &= -3;
      }

      this.datawatcher.watch(10, Byte.valueOf(var2));
   }

   public boolean hasGravity() {
      return (this.datawatcher.getByte(10) & 2) != 0;
   }

   public void setArms(boolean var1) {
      byte var2 = this.datawatcher.getByte(10);
      if(var1) {
         var2 = (byte)(var2 | 4);
      } else {
         var2 &= -5;
      }

      this.datawatcher.watch(10, Byte.valueOf(var2));
   }

   public boolean hasArms() {
      return (this.datawatcher.getByte(10) & 4) != 0;
   }

   public void setBasePlate(boolean var1) {
      byte var2 = this.datawatcher.getByte(10);
      if(var1) {
         var2 = (byte)(var2 | 8);
      } else {
         var2 &= -9;
      }

      this.datawatcher.watch(10, Byte.valueOf(var2));
   }

   public boolean hasBasePlate() {
      return (this.datawatcher.getByte(10) & 8) != 0;
   }

   private void n(boolean var1) {
      byte var2 = this.datawatcher.getByte(10);
      if(var1) {
         var2 = (byte)(var2 | 16);
      } else {
         var2 &= -17;
      }

      this.datawatcher.watch(10, Byte.valueOf(var2));
   }

   public boolean s() {
      return (this.datawatcher.getByte(10) & 16) != 0;
   }

   public void setHeadPose(Vector3f var1) {
      this.headPose = var1;
      this.datawatcher.watch(11, var1);
   }

   public void setBodyPose(Vector3f var1) {
      this.bodyPose = var1;
      this.datawatcher.watch(12, var1);
   }

   public void setLeftArmPose(Vector3f var1) {
      this.leftArmPose = var1;
      this.datawatcher.watch(13, var1);
   }

   public void setRightArmPose(Vector3f var1) {
      this.rightArmPose = var1;
      this.datawatcher.watch(14, var1);
   }

   public void setLeftLegPose(Vector3f var1) {
      this.leftLegPose = var1;
      this.datawatcher.watch(15, var1);
   }

   public void setRightLegPose(Vector3f var1) {
      this.rightLegPose = var1;
      this.datawatcher.watch(16, var1);
   }

   public Vector3f t() {
      return this.headPose;
   }

   public Vector3f u() {
      return this.bodyPose;
   }

   public boolean ad() {
      return super.ad() && !this.s();
   }
}
