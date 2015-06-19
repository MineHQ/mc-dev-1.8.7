package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityPotion extends EntityProjectile {
   public ItemStack item;

   public EntityPotion(World var1) {
      super(var1);
   }

   public EntityPotion(World var1, EntityLiving var2, int var3) {
      this(var1, var2, new ItemStack(Items.POTION, 1, var3));
   }

   public EntityPotion(World var1, EntityLiving var2, ItemStack var3) {
      super(var1, var2);
      this.item = var3;
   }

   public EntityPotion(World var1, double var2, double var4, double var6, ItemStack var8) {
      super(var1, var2, var4, var6);
      this.item = var8;
   }

   protected float m() {
      return 0.05F;
   }

   protected float j() {
      return 0.5F;
   }

   protected float l() {
      return -20.0F;
   }

   public void setPotionValue(int var1) {
      if(this.item == null) {
         this.item = new ItemStack(Items.POTION, 1, 0);
      }

      this.item.setData(var1);
   }

   public int getPotionValue() {
      if(this.item == null) {
         this.item = new ItemStack(Items.POTION, 1, 0);
      }

      return this.item.getData();
   }

   protected void a(MovingObjectPosition var1) {
      if(!this.world.isClientSide) {
         List var2 = Items.POTION.h(this.item);
         if(var2 != null && !var2.isEmpty()) {
            AxisAlignedBB var3 = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List var4 = this.world.a(EntityLiving.class, var3);
            if(!var4.isEmpty()) {
               Iterator var5 = var4.iterator();

               label45:
               while(true) {
                  EntityLiving var6;
                  double var7;
                  do {
                     if(!var5.hasNext()) {
                        break label45;
                     }

                     var6 = (EntityLiving)var5.next();
                     var7 = this.h(var6);
                  } while(var7 >= 16.0D);

                  double var9 = 1.0D - Math.sqrt(var7) / 4.0D;
                  if(var6 == var1.entity) {
                     var9 = 1.0D;
                  }

                  Iterator var11 = var2.iterator();

                  while(var11.hasNext()) {
                     MobEffect var12 = (MobEffect)var11.next();
                     int var13 = var12.getEffectId();
                     if(MobEffectList.byId[var13].isInstant()) {
                        MobEffectList.byId[var13].applyInstantEffect(this, this.getShooter(), var6, var12.getAmplifier(), var9);
                     } else {
                        int var14 = (int)(var9 * (double)var12.getDuration() + 0.5D);
                        if(var14 > 20) {
                           var6.addEffect(new MobEffect(var13, var14, var12.getAmplifier()));
                        }
                     }
                  }
               }
            }
         }

         this.world.triggerEffect(2002, new BlockPosition(this), this.getPotionValue());
         this.die();
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("Potion", 10)) {
         this.item = ItemStack.createStack(var1.getCompound("Potion"));
      } else {
         this.setPotionValue(var1.getInt("potionValue"));
      }

      if(this.item == null) {
         this.die();
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.item != null) {
         var1.set("Potion", this.item.save(new NBTTagCompound()));
      }

   }
}
