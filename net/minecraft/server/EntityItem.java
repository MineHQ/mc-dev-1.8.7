package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Statistic;
import net.minecraft.server.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityItem extends Entity {
   private static final Logger b = LogManager.getLogger();
   private int age;
   public int pickupDelay;
   private int e;
   private String f;
   private String g;
   public float a;

   public EntityItem(World var1, double var2, double var4, double var6) {
      super(var1);
      this.e = 5;
      this.a = (float)(Math.random() * 3.141592653589793D * 2.0D);
      this.setSize(0.25F, 0.25F);
      this.setPosition(var2, var4, var6);
      this.yaw = (float)(Math.random() * 360.0D);
      this.motX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
      this.motY = 0.20000000298023224D;
      this.motZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
   }

   public EntityItem(World var1, double var2, double var4, double var6, ItemStack var8) {
      this(var1, var2, var4, var6);
      this.setItemStack(var8);
   }

   protected boolean s_() {
      return false;
   }

   public EntityItem(World var1) {
      super(var1);
      this.e = 5;
      this.a = (float)(Math.random() * 3.141592653589793D * 2.0D);
      this.setSize(0.25F, 0.25F);
      this.setItemStack(new ItemStack(Blocks.AIR, 0));
   }

   protected void h() {
      this.getDataWatcher().add(10, 5);
   }

   public void t_() {
      if(this.getItemStack() == null) {
         this.die();
      } else {
         super.t_();
         if(this.pickupDelay > 0 && this.pickupDelay != 32767) {
            --this.pickupDelay;
         }

         this.lastX = this.locX;
         this.lastY = this.locY;
         this.lastZ = this.locZ;
         this.motY -= 0.03999999910593033D;
         this.noclip = this.j(this.locX, (this.getBoundingBox().b + this.getBoundingBox().e) / 2.0D, this.locZ);
         this.move(this.motX, this.motY, this.motZ);
         boolean var1 = (int)this.lastX != (int)this.locX || (int)this.lastY != (int)this.locY || (int)this.lastZ != (int)this.locZ;
         if(var1 || this.ticksLived % 25 == 0) {
            if(this.world.getType(new BlockPosition(this)).getBlock().getMaterial() == Material.LAVA) {
               this.motY = 0.20000000298023224D;
               this.motX = (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
               this.motZ = (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
               this.makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
            }

            if(!this.world.isClientSide) {
               this.w();
            }
         }

         float var2 = 0.98F;
         if(this.onGround) {
            var2 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.98F;
         }

         this.motX *= (double)var2;
         this.motY *= 0.9800000190734863D;
         this.motZ *= (double)var2;
         if(this.onGround) {
            this.motY *= -0.5D;
         }

         if(this.age != -32768) {
            ++this.age;
         }

         this.W();
         if(!this.world.isClientSide && this.age >= 6000) {
            this.die();
         }

      }
   }

   private void w() {
      Iterator var1 = this.world.a(EntityItem.class, this.getBoundingBox().grow(0.5D, 0.0D, 0.5D)).iterator();

      while(var1.hasNext()) {
         EntityItem var2 = (EntityItem)var1.next();
         this.a(var2);
      }

   }

   private boolean a(EntityItem var1) {
      if(var1 == this) {
         return false;
      } else if(var1.isAlive() && this.isAlive()) {
         ItemStack var2 = this.getItemStack();
         ItemStack var3 = var1.getItemStack();
         if(this.pickupDelay != 32767 && var1.pickupDelay != 32767) {
            if(this.age != -32768 && var1.age != -32768) {
               if(var3.getItem() != var2.getItem()) {
                  return false;
               } else if(var3.hasTag() ^ var2.hasTag()) {
                  return false;
               } else if(var3.hasTag() && !var3.getTag().equals(var2.getTag())) {
                  return false;
               } else if(var3.getItem() == null) {
                  return false;
               } else if(var3.getItem().k() && var3.getData() != var2.getData()) {
                  return false;
               } else if(var3.count < var2.count) {
                  return var1.a(this);
               } else if(var3.count + var2.count > var3.getMaxStackSize()) {
                  return false;
               } else {
                  var3.count += var2.count;
                  var1.pickupDelay = Math.max(var1.pickupDelay, this.pickupDelay);
                  var1.age = Math.min(var1.age, this.age);
                  var1.setItemStack(var3);
                  this.die();
                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void j() {
      this.age = 4800;
   }

   public boolean W() {
      if(this.world.a((AxisAlignedBB)this.getBoundingBox(), (Material)Material.WATER, (Entity)this)) {
         if(!this.inWater && !this.justCreated) {
            this.X();
         }

         this.inWater = true;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   protected void burn(int var1) {
      this.damageEntity(DamageSource.FIRE, (float)var1);
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(this.getItemStack() != null && this.getItemStack().getItem() == Items.NETHER_STAR && var1.isExplosion()) {
         return false;
      } else {
         this.ac();
         this.e = (int)((float)this.e - var2);
         if(this.e <= 0) {
            this.die();
         }

         return false;
      }
   }

   public void b(NBTTagCompound var1) {
      var1.setShort("Health", (short)((byte)this.e));
      var1.setShort("Age", (short)this.age);
      var1.setShort("PickupDelay", (short)this.pickupDelay);
      if(this.n() != null) {
         var1.setString("Thrower", this.f);
      }

      if(this.m() != null) {
         var1.setString("Owner", this.g);
      }

      if(this.getItemStack() != null) {
         var1.set("Item", this.getItemStack().save(new NBTTagCompound()));
      }

   }

   public void a(NBTTagCompound var1) {
      this.e = var1.getShort("Health") & 255;
      this.age = var1.getShort("Age");
      if(var1.hasKey("PickupDelay")) {
         this.pickupDelay = var1.getShort("PickupDelay");
      }

      if(var1.hasKey("Owner")) {
         this.g = var1.getString("Owner");
      }

      if(var1.hasKey("Thrower")) {
         this.f = var1.getString("Thrower");
      }

      NBTTagCompound var2 = var1.getCompound("Item");
      this.setItemStack(ItemStack.createStack(var2));
      if(this.getItemStack() == null) {
         this.die();
      }

   }

   public void d(EntityHuman var1) {
      if(!this.world.isClientSide) {
         ItemStack var2 = this.getItemStack();
         int var3 = var2.count;
         if(this.pickupDelay == 0 && (this.g == null || 6000 - this.age <= 200 || this.g.equals(var1.getName())) && var1.inventory.pickup(var2)) {
            if(var2.getItem() == Item.getItemOf(Blocks.LOG)) {
               var1.b((Statistic)AchievementList.g);
            }

            if(var2.getItem() == Item.getItemOf(Blocks.LOG2)) {
               var1.b((Statistic)AchievementList.g);
            }

            if(var2.getItem() == Items.LEATHER) {
               var1.b((Statistic)AchievementList.t);
            }

            if(var2.getItem() == Items.DIAMOND) {
               var1.b((Statistic)AchievementList.w);
            }

            if(var2.getItem() == Items.BLAZE_ROD) {
               var1.b((Statistic)AchievementList.A);
            }

            if(var2.getItem() == Items.DIAMOND && this.n() != null) {
               EntityHuman var4 = this.world.a(this.n());
               if(var4 != null && var4 != var1) {
                  var4.b((Statistic)AchievementList.x);
               }
            }

            if(!this.R()) {
               this.world.makeSound(var1, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            var1.receive(this, var3);
            if(var2.count <= 0) {
               this.die();
            }
         }

      }
   }

   public String getName() {
      return this.hasCustomName()?this.getCustomName():LocaleI18n.get("item." + this.getItemStack().a());
   }

   public boolean aD() {
      return false;
   }

   public void c(int var1) {
      super.c(var1);
      if(!this.world.isClientSide) {
         this.w();
      }

   }

   public ItemStack getItemStack() {
      ItemStack var1 = this.getDataWatcher().getItemStack(10);
      if(var1 == null) {
         if(this.world != null) {
            b.error("Item entity " + this.getId() + " has no item?!");
         }

         return new ItemStack(Blocks.STONE);
      } else {
         return var1;
      }
   }

   public void setItemStack(ItemStack var1) {
      this.getDataWatcher().watch(10, var1);
      this.getDataWatcher().update(10);
   }

   public String m() {
      return this.g;
   }

   public void b(String var1) {
      this.g = var1;
   }

   public String n() {
      return this.f;
   }

   public void c(String var1) {
      this.f = var1;
   }

   public void p() {
      this.pickupDelay = 10;
   }

   public void q() {
      this.pickupDelay = 0;
   }

   public void r() {
      this.pickupDelay = 32767;
   }

   public void a(int var1) {
      this.pickupDelay = var1;
   }

   public boolean s() {
      return this.pickupDelay > 0;
   }

   public void u() {
      this.age = -6000;
   }

   public void v() {
      this.r();
      this.age = 5999;
   }
}
