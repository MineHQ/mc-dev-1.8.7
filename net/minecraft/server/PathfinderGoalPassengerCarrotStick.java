package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStairs;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.PathfinderNormal;

public class PathfinderGoalPassengerCarrotStick extends PathfinderGoal {
   private final EntityInsentient a;
   private final float b;
   private float c;
   private boolean d;
   private int e;
   private int f;

   public PathfinderGoalPassengerCarrotStick(EntityInsentient var1, float var2) {
      this.a = var1;
      this.b = var2;
      this.a(7);
   }

   public void c() {
      this.c = 0.0F;
   }

   public void d() {
      this.d = false;
      this.c = 0.0F;
   }

   public boolean a() {
      return this.a.isAlive() && this.a.passenger != null && this.a.passenger instanceof EntityHuman && (this.d || this.a.bW());
   }

   public void e() {
      EntityHuman var1 = (EntityHuman)this.a.passenger;
      EntityCreature var2 = (EntityCreature)this.a;
      float var3 = MathHelper.g(var1.yaw - this.a.yaw) * 0.5F;
      if(var3 > 5.0F) {
         var3 = 5.0F;
      }

      if(var3 < -5.0F) {
         var3 = -5.0F;
      }

      this.a.yaw = MathHelper.g(this.a.yaw + var3);
      if(this.c < this.b) {
         this.c += (this.b - this.c) * 0.01F;
      }

      if(this.c > this.b) {
         this.c = this.b;
      }

      int var4 = MathHelper.floor(this.a.locX);
      int var5 = MathHelper.floor(this.a.locY);
      int var6 = MathHelper.floor(this.a.locZ);
      float var7 = this.c;
      if(this.d) {
         if(this.e++ > this.f) {
            this.d = false;
         }

         var7 += var7 * 1.15F * MathHelper.sin((float)this.e / (float)this.f * 3.1415927F);
      }

      float var8 = 0.91F;
      if(this.a.onGround) {
         var8 = this.a.world.getType(new BlockPosition(MathHelper.d((float)var4), MathHelper.d((float)var5) - 1, MathHelper.d((float)var6))).getBlock().frictionFactor * 0.91F;
      }

      float var9 = 0.16277136F / (var8 * var8 * var8);
      float var10 = MathHelper.sin(var2.yaw * 3.1415927F / 180.0F);
      float var11 = MathHelper.cos(var2.yaw * 3.1415927F / 180.0F);
      float var12 = var2.bI() * var9;
      float var13 = Math.max(var7, 1.0F);
      var13 = var12 / var13;
      float var14 = var7 * var13;
      float var15 = -(var14 * var10);
      float var16 = var14 * var11;
      if(MathHelper.e(var15) > MathHelper.e(var16)) {
         if(var15 < 0.0F) {
            var15 -= this.a.width / 2.0F;
         }

         if(var15 > 0.0F) {
            var15 += this.a.width / 2.0F;
         }

         var16 = 0.0F;
      } else {
         var15 = 0.0F;
         if(var16 < 0.0F) {
            var16 -= this.a.width / 2.0F;
         }

         if(var16 > 0.0F) {
            var16 += this.a.width / 2.0F;
         }
      }

      int var17 = MathHelper.floor(this.a.locX + (double)var15);
      int var18 = MathHelper.floor(this.a.locZ + (double)var16);
      int var19 = MathHelper.d(this.a.width + 1.0F);
      int var20 = MathHelper.d(this.a.length + var1.length + 1.0F);
      int var21 = MathHelper.d(this.a.width + 1.0F);
      if(var4 != var17 || var6 != var18) {
         Block var22 = this.a.world.getType(new BlockPosition(var4, var5, var6)).getBlock();
         boolean var23 = !this.a(var22) && (var22.getMaterial() != Material.AIR || !this.a(this.a.world.getType(new BlockPosition(var4, var5 - 1, var6)).getBlock()));
         if(var23 && 0 == PathfinderNormal.a(this.a.world, this.a, var17, var5, var18, var19, var20, var21, false, false, true) && 1 == PathfinderNormal.a(this.a.world, this.a, var4, var5 + 1, var6, var19, var20, var21, false, false, true) && 1 == PathfinderNormal.a(this.a.world, this.a, var17, var5 + 1, var18, var19, var20, var21, false, false, true)) {
            var2.getControllerJump().a();
         }
      }

      if(!var1.abilities.canInstantlyBuild && this.c >= this.b * 0.5F && this.a.bc().nextFloat() < 0.006F && !this.d) {
         ItemStack var24 = var1.bA();
         if(var24 != null && var24.getItem() == Items.CARROT_ON_A_STICK) {
            var24.damage(1, var1);
            if(var24.count == 0) {
               ItemStack var25 = new ItemStack(Items.FISHING_ROD);
               var25.setTag(var24.getTag());
               var1.inventory.items[var1.inventory.itemInHandIndex] = var25;
            }
         }
      }

      this.a.g(0.0F, var7);
   }

   private boolean a(Block var1) {
      return var1 instanceof BlockStairs || var1 instanceof BlockStepAbstract;
   }

   public boolean f() {
      return this.d;
   }

   public void g() {
      this.d = true;
      this.e = 0;
      this.f = this.a.bc().nextInt(841) + 140;
   }

   public boolean h() {
      return !this.f() && this.c > this.b * 0.3F;
   }
}
