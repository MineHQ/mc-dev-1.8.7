package net.minecraft.server;

import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;

public class Vector3f {
   protected final float x;
   protected final float y;
   protected final float z;

   public Vector3f(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector3f(NBTTagList var1) {
      this.x = var1.e(0);
      this.y = var1.e(1);
      this.z = var1.e(2);
   }

   public NBTTagList a() {
      NBTTagList var1 = new NBTTagList();
      var1.add(new NBTTagFloat(this.x));
      var1.add(new NBTTagFloat(this.y));
      var1.add(new NBTTagFloat(this.z));
      return var1;
   }

   public boolean equals(Object var1) {
      if(!(var1 instanceof Vector3f)) {
         return false;
      } else {
         Vector3f var2 = (Vector3f)var1;
         return this.x == var2.x && this.y == var2.y && this.z == var2.z;
      }
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getZ() {
      return this.z;
   }
}
