package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDiodeAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging extends Entity {
   private int c;
   public BlockPosition blockPosition;
   public EnumDirection direction;

   public EntityHanging(World var1) {
      super(var1);
      this.setSize(0.5F, 0.5F);
   }

   public EntityHanging(World var1, BlockPosition var2) {
      this(var1);
      this.blockPosition = var2;
   }

   protected void h() {
   }

   public void setDirection(EnumDirection var1) {
      Validate.notNull(var1);
      Validate.isTrue(var1.k().c());
      this.direction = var1;
      this.lastYaw = this.yaw = (float)(this.direction.b() * 90);
      this.updateBoundingBox();
   }

   private void updateBoundingBox() {
      if(this.direction != null) {
         double var1 = (double)this.blockPosition.getX() + 0.5D;
         double var3 = (double)this.blockPosition.getY() + 0.5D;
         double var5 = (double)this.blockPosition.getZ() + 0.5D;
         double var7 = 0.46875D;
         double var9 = this.a(this.l());
         double var11 = this.a(this.m());
         var1 -= (double)this.direction.getAdjacentX() * 0.46875D;
         var5 -= (double)this.direction.getAdjacentZ() * 0.46875D;
         var3 += var11;
         EnumDirection var13 = this.direction.f();
         var1 += var9 * (double)var13.getAdjacentX();
         var5 += var9 * (double)var13.getAdjacentZ();
         this.locX = var1;
         this.locY = var3;
         this.locZ = var5;
         double var14 = (double)this.l();
         double var16 = (double)this.m();
         double var18 = (double)this.l();
         if(this.direction.k() == EnumDirection.EnumAxis.Z) {
            var18 = 1.0D;
         } else {
            var14 = 1.0D;
         }

         var14 /= 32.0D;
         var16 /= 32.0D;
         var18 /= 32.0D;
         this.a(new AxisAlignedBB(var1 - var14, var3 - var16, var5 - var18, var1 + var14, var3 + var16, var5 + var18));
      }
   }

   private double a(int var1) {
      return var1 % 32 == 0?0.5D:0.0D;
   }

   public void t_() {
      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      if(this.c++ == 100 && !this.world.isClientSide) {
         this.c = 0;
         if(!this.dead && !this.survives()) {
            this.die();
            this.b((Entity)null);
         }
      }

   }

   public boolean survives() {
      if(!this.world.getCubes(this, this.getBoundingBox()).isEmpty()) {
         return false;
      } else {
         int var1 = Math.max(1, this.l() / 16);
         int var2 = Math.max(1, this.m() / 16);
         BlockPosition var3 = this.blockPosition.shift(this.direction.opposite());
         EnumDirection var4 = this.direction.f();

         for(int var5 = 0; var5 < var1; ++var5) {
            for(int var6 = 0; var6 < var2; ++var6) {
               BlockPosition var7 = var3.shift(var4, var5).up(var6);
               Block var8 = this.world.getType(var7).getBlock();
               if(!var8.getMaterial().isBuildable() && !BlockDiodeAbstract.d(var8)) {
                  return false;
               }
            }
         }

         List var9 = this.world.getEntities(this, this.getBoundingBox());
         Iterator var10 = var9.iterator();

         Entity var11;
         do {
            if(!var10.hasNext()) {
               return true;
            }

            var11 = (Entity)var10.next();
         } while(!(var11 instanceof EntityHanging));

         return false;
      }
   }

   public boolean ad() {
      return true;
   }

   public boolean l(Entity var1) {
      return var1 instanceof EntityHuman?this.damageEntity(DamageSource.playerAttack((EntityHuman)var1), 0.0F):false;
   }

   public EnumDirection getDirection() {
      return this.direction;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         if(!this.dead && !this.world.isClientSide) {
            this.die();
            this.ac();
            this.b(var1.getEntity());
         }

         return true;
      }
   }

   public void move(double var1, double var3, double var5) {
      if(!this.world.isClientSide && !this.dead && var1 * var1 + var3 * var3 + var5 * var5 > 0.0D) {
         this.die();
         this.b((Entity)null);
      }

   }

   public void g(double var1, double var3, double var5) {
      if(!this.world.isClientSide && !this.dead && var1 * var1 + var3 * var3 + var5 * var5 > 0.0D) {
         this.die();
         this.b((Entity)null);
      }

   }

   public void b(NBTTagCompound var1) {
      var1.setByte("Facing", (byte)this.direction.b());
      var1.setInt("TileX", this.getBlockPosition().getX());
      var1.setInt("TileY", this.getBlockPosition().getY());
      var1.setInt("TileZ", this.getBlockPosition().getZ());
   }

   public void a(NBTTagCompound var1) {
      this.blockPosition = new BlockPosition(var1.getInt("TileX"), var1.getInt("TileY"), var1.getInt("TileZ"));
      EnumDirection var2;
      if(var1.hasKeyOfType("Direction", 99)) {
         var2 = EnumDirection.fromType2(var1.getByte("Direction"));
         this.blockPosition = this.blockPosition.shift(var2);
      } else if(var1.hasKeyOfType("Facing", 99)) {
         var2 = EnumDirection.fromType2(var1.getByte("Facing"));
      } else {
         var2 = EnumDirection.fromType2(var1.getByte("Dir"));
      }

      this.setDirection(var2);
   }

   public abstract int l();

   public abstract int m();

   public abstract void b(Entity var1);

   protected boolean af() {
      return false;
   }

   public void setPosition(double var1, double var3, double var5) {
      this.locX = var1;
      this.locY = var3;
      this.locZ = var5;
      BlockPosition var7 = this.blockPosition;
      this.blockPosition = new BlockPosition(var1, var3, var5);
      if(!this.blockPosition.equals(var7)) {
         this.updateBoundingBox();
         this.ai = true;
      }

   }

   public BlockPosition getBlockPosition() {
      return this.blockPosition;
   }
}
