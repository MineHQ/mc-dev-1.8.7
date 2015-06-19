package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Vec3D;

public class MovingObjectPosition {
   private BlockPosition e;
   public MovingObjectPosition.EnumMovingObjectType type;
   public EnumDirection direction;
   public Vec3D pos;
   public Entity entity;

   public MovingObjectPosition(Vec3D var1, EnumDirection var2, BlockPosition var3) {
      this(MovingObjectPosition.EnumMovingObjectType.BLOCK, var1, var2, var3);
   }

   public MovingObjectPosition(Vec3D var1, EnumDirection var2) {
      this(MovingObjectPosition.EnumMovingObjectType.BLOCK, var1, var2, BlockPosition.ZERO);
   }

   public MovingObjectPosition(Entity var1) {
      this(var1, new Vec3D(var1.locX, var1.locY, var1.locZ));
   }

   public MovingObjectPosition(MovingObjectPosition.EnumMovingObjectType var1, Vec3D var2, EnumDirection var3, BlockPosition var4) {
      this.type = var1;
      this.e = var4;
      this.direction = var3;
      this.pos = new Vec3D(var2.a, var2.b, var2.c);
   }

   public MovingObjectPosition(Entity var1, Vec3D var2) {
      this.type = MovingObjectPosition.EnumMovingObjectType.ENTITY;
      this.entity = var1;
      this.pos = var2;
   }

   public BlockPosition a() {
      return this.e;
   }

   public String toString() {
      return "HitResult{type=" + this.type + ", blockpos=" + this.e + ", f=" + this.direction + ", pos=" + this.pos + ", entity=" + this.entity + '}';
   }

   public static enum EnumMovingObjectType {
      MISS,
      BLOCK,
      ENTITY;

      private EnumMovingObjectType() {
      }
   }
}
