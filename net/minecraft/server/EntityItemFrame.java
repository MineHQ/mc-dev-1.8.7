package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWorldMap;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;
import net.minecraft.server.WorldMap;

public class EntityItemFrame extends EntityHanging {
   private float c = 1.0F;

   public EntityItemFrame(World var1) {
      super(var1);
   }

   public EntityItemFrame(World var1, BlockPosition var2, EnumDirection var3) {
      super(var1, var2);
      this.setDirection(var3);
   }

   protected void h() {
      this.getDataWatcher().add(8, 5);
      this.getDataWatcher().a(9, Byte.valueOf((byte)0));
   }

   public float ao() {
      return 0.0F;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else if(!var1.isExplosion() && this.getItem() != null) {
         if(!this.world.isClientSide) {
            this.a(var1.getEntity(), false);
            this.setItem((ItemStack)null);
         }

         return true;
      } else {
         return super.damageEntity(var1, var2);
      }
   }

   public int l() {
      return 12;
   }

   public int m() {
      return 12;
   }

   public void b(Entity var1) {
      this.a(var1, true);
   }

   public void a(Entity var1, boolean var2) {
      if(this.world.getGameRules().getBoolean("doEntityDrops")) {
         ItemStack var3 = this.getItem();
         if(var1 instanceof EntityHuman) {
            EntityHuman var4 = (EntityHuman)var1;
            if(var4.abilities.canInstantlyBuild) {
               this.b(var3);
               return;
            }
         }

         if(var2) {
            this.a(new ItemStack(Items.ITEM_FRAME), 0.0F);
         }

         if(var3 != null && this.random.nextFloat() < this.c) {
            var3 = var3.cloneItemStack();
            this.b(var3);
            this.a(var3, 0.0F);
         }

      }
   }

   private void b(ItemStack var1) {
      if(var1 != null) {
         if(var1.getItem() == Items.FILLED_MAP) {
            WorldMap var2 = ((ItemWorldMap)var1.getItem()).getSavedMap(var1, this.world);
            var2.decorations.remove("frame-" + this.getId());
         }

         var1.a((EntityItemFrame)null);
      }
   }

   public ItemStack getItem() {
      return this.getDataWatcher().getItemStack(8);
   }

   public void setItem(ItemStack var1) {
      this.setItem(var1, true);
   }

   private void setItem(ItemStack var1, boolean var2) {
      if(var1 != null) {
         var1 = var1.cloneItemStack();
         var1.count = 1;
         var1.a(this);
      }

      this.getDataWatcher().watch(8, var1);
      this.getDataWatcher().update(8);
      if(var2 && this.blockPosition != null) {
         this.world.updateAdjacentComparators(this.blockPosition, Blocks.AIR);
      }

   }

   public int getRotation() {
      return this.getDataWatcher().getByte(9);
   }

   public void setRotation(int var1) {
      this.setRotation(var1, true);
   }

   private void setRotation(int var1, boolean var2) {
      this.getDataWatcher().watch(9, Byte.valueOf((byte)(var1 % 8)));
      if(var2 && this.blockPosition != null) {
         this.world.updateAdjacentComparators(this.blockPosition, Blocks.AIR);
      }

   }

   public void b(NBTTagCompound var1) {
      if(this.getItem() != null) {
         var1.set("Item", this.getItem().save(new NBTTagCompound()));
         var1.setByte("ItemRotation", (byte)this.getRotation());
         var1.setFloat("ItemDropChance", this.c);
      }

      super.b(var1);
   }

   public void a(NBTTagCompound var1) {
      NBTTagCompound var2 = var1.getCompound("Item");
      if(var2 != null && !var2.isEmpty()) {
         this.setItem(ItemStack.createStack(var2), false);
         this.setRotation(var1.getByte("ItemRotation"), false);
         if(var1.hasKeyOfType("ItemDropChance", 99)) {
            this.c = var1.getFloat("ItemDropChance");
         }

         if(var1.hasKey("Direction")) {
            this.setRotation(this.getRotation() * 2, false);
         }
      }

      super.a(var1);
   }

   public boolean e(EntityHuman var1) {
      if(this.getItem() == null) {
         ItemStack var2 = var1.bA();
         if(var2 != null && !this.world.isClientSide) {
            this.setItem(var2);
            if(!var1.abilities.canInstantlyBuild && --var2.count <= 0) {
               var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
            }
         }
      } else if(!this.world.isClientSide) {
         this.setRotation(this.getRotation() + 1);
      }

      return true;
   }

   public int q() {
      return this.getItem() == null?0:this.getRotation() % 8 + 1;
   }
}
