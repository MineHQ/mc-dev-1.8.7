package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.IComplex;
import net.minecraft.server.NBTTagCompound;

public class EntityComplexPart extends Entity {
   public final IComplex owner;
   public final String b;

   public EntityComplexPart(IComplex var1, String var2, float var3, float var4) {
      super(var1.a());
      this.setSize(var3, var4);
      this.owner = var1;
      this.b = var2;
   }

   protected void h() {
   }

   protected void a(NBTTagCompound var1) {
   }

   protected void b(NBTTagCompound var1) {
   }

   public boolean ad() {
      return true;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      return this.isInvulnerable(var1)?false:this.owner.a(this, var1, var2);
   }

   public boolean k(Entity var1) {
      return this == var1 || this.owner == var1;
   }
}
