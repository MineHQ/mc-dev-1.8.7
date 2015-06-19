package net.minecraft.server;

import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public abstract class EntityAgeable extends EntityCreature {
   protected int a;
   protected int b;
   protected int c;
   private float bm = -1.0F;
   private float bn;

   public EntityAgeable(World var1) {
      super(var1);
   }

   public abstract EntityAgeable createChild(EntityAgeable var1);

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.SPAWN_EGG) {
         if(!this.world.isClientSide) {
            Class var3 = EntityTypes.a(var2.getData());
            if(var3 != null && this.getClass() == var3) {
               EntityAgeable var4 = this.createChild(this);
               if(var4 != null) {
                  var4.setAgeRaw(-24000);
                  var4.setPositionRotation(this.locX, this.locY, this.locZ, 0.0F, 0.0F);
                  this.world.addEntity(var4);
                  if(var2.hasName()) {
                     var4.setCustomName(var2.getName());
                  }

                  if(!var1.abilities.canInstantlyBuild) {
                     --var2.count;
                     if(var2.count <= 0) {
                        var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
                     }
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected void h() {
      super.h();
      this.datawatcher.a(12, Byte.valueOf((byte)0));
   }

   public int getAge() {
      return this.world.isClientSide?this.datawatcher.getByte(12):this.a;
   }

   public void setAge(int var1, boolean var2) {
      int var3 = this.getAge();
      int var4 = var3;
      var3 += var1 * 20;
      if(var3 > 0) {
         var3 = 0;
         if(var4 < 0) {
            this.n();
         }
      }

      int var5 = var3 - var4;
      this.setAgeRaw(var3);
      if(var2) {
         this.b += var5;
         if(this.c == 0) {
            this.c = 40;
         }
      }

      if(this.getAge() == 0) {
         this.setAgeRaw(this.b);
      }

   }

   public void setAge(int var1) {
      this.setAge(var1, false);
   }

   public void setAgeRaw(int var1) {
      this.datawatcher.watch(12, Byte.valueOf((byte)MathHelper.clamp(var1, -1, 1)));
      this.a = var1;
      this.a(this.isBaby());
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Age", this.getAge());
      var1.setInt("ForcedAge", this.b);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setAgeRaw(var1.getInt("Age"));
      this.b = var1.getInt("ForcedAge");
   }

   public void m() {
      super.m();
      if(this.world.isClientSide) {
         if(this.c > 0) {
            if(this.c % 4 == 0) {
               this.world.addParticle(EnumParticle.VILLAGER_HAPPY, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }

            --this.c;
         }

         this.a(this.isBaby());
      } else {
         int var1 = this.getAge();
         if(var1 < 0) {
            ++var1;
            this.setAgeRaw(var1);
            if(var1 == 0) {
               this.n();
            }
         } else if(var1 > 0) {
            --var1;
            this.setAgeRaw(var1);
         }
      }

   }

   protected void n() {
   }

   public boolean isBaby() {
      return this.getAge() < 0;
   }

   public void a(boolean var1) {
      this.a(var1?0.5F:1.0F);
   }

   protected final void setSize(float var1, float var2) {
      boolean var3 = this.bm > 0.0F;
      this.bm = var1;
      this.bn = var2;
      if(!var3) {
         this.a(1.0F);
      }

   }

   protected final void a(float var1) {
      super.setSize(this.bm * var1, this.bn * var1);
   }
}
