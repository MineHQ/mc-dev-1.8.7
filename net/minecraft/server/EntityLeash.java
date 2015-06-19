package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityLeash extends EntityHanging {
   public EntityLeash(World var1) {
      super(var1);
   }

   public EntityLeash(World var1, BlockPosition var2) {
      super(var1, var2);
      this.setPosition((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D);
      float var3 = 0.125F;
      float var4 = 0.1875F;
      float var5 = 0.25F;
      this.a(new AxisAlignedBB(this.locX - 0.1875D, this.locY - 0.25D + 0.125D, this.locZ - 0.1875D, this.locX + 0.1875D, this.locY + 0.25D + 0.125D, this.locZ + 0.1875D));
   }

   protected void h() {
      super.h();
   }

   public void setDirection(EnumDirection var1) {
   }

   public int l() {
      return 9;
   }

   public int m() {
      return 9;
   }

   public float getHeadHeight() {
      return -0.0625F;
   }

   public void b(Entity var1) {
   }

   public boolean d(NBTTagCompound var1) {
      return false;
   }

   public void b(NBTTagCompound var1) {
   }

   public void a(NBTTagCompound var1) {
   }

   public boolean e(EntityHuman var1) {
      ItemStack var2 = var1.bA();
      boolean var3 = false;
      double var4;
      List var6;
      Iterator var7;
      EntityInsentient var8;
      if(var2 != null && var2.getItem() == Items.LEAD && !this.world.isClientSide) {
         var4 = 7.0D;
         var6 = this.world.a(EntityInsentient.class, new AxisAlignedBB(this.locX - var4, this.locY - var4, this.locZ - var4, this.locX + var4, this.locY + var4, this.locZ + var4));
         var7 = var6.iterator();

         while(var7.hasNext()) {
            var8 = (EntityInsentient)var7.next();
            if(var8.cc() && var8.getLeashHolder() == var1) {
               var8.setLeashHolder(this, true);
               var3 = true;
            }
         }
      }

      if(!this.world.isClientSide && !var3) {
         this.die();
         if(var1.abilities.canInstantlyBuild) {
            var4 = 7.0D;
            var6 = this.world.a(EntityInsentient.class, new AxisAlignedBB(this.locX - var4, this.locY - var4, this.locZ - var4, this.locX + var4, this.locY + var4, this.locZ + var4));
            var7 = var6.iterator();

            while(var7.hasNext()) {
               var8 = (EntityInsentient)var7.next();
               if(var8.cc() && var8.getLeashHolder() == this) {
                  var8.unleash(true, false);
               }
            }
         }
      }

      return true;
   }

   public boolean survives() {
      return this.world.getType(this.blockPosition).getBlock() instanceof BlockFence;
   }

   public static EntityLeash a(World var0, BlockPosition var1) {
      EntityLeash var2 = new EntityLeash(var0, var1);
      var2.attachedToPlayer = true;
      var0.addEntity(var2);
      return var2;
   }

   public static EntityLeash b(World var0, BlockPosition var1) {
      int var2 = var1.getX();
      int var3 = var1.getY();
      int var4 = var1.getZ();
      List var5 = var0.a(EntityLeash.class, new AxisAlignedBB((double)var2 - 1.0D, (double)var3 - 1.0D, (double)var4 - 1.0D, (double)var2 + 1.0D, (double)var3 + 1.0D, (double)var4 + 1.0D));
      Iterator var6 = var5.iterator();

      EntityLeash var7;
      do {
         if(!var6.hasNext()) {
            return null;
         }

         var7 = (EntityLeash)var6.next();
      } while(!var7.getBlockPosition().equals(var1));

      return var7;
   }
}
