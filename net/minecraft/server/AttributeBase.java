package net.minecraft.server;

import net.minecraft.server.IAttribute;

public abstract class AttributeBase implements IAttribute {
   private final IAttribute a;
   private final String b;
   private final double c;
   private boolean d;

   protected AttributeBase(IAttribute var1, String var2, double var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      if(var2 == null) {
         throw new IllegalArgumentException("Name cannot be null!");
      }
   }

   public String getName() {
      return this.b;
   }

   public double b() {
      return this.c;
   }

   public boolean c() {
      return this.d;
   }

   public AttributeBase a(boolean var1) {
      this.d = var1;
      return this;
   }

   public IAttribute d() {
      return this.a;
   }

   public int hashCode() {
      return this.b.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof IAttribute && this.b.equals(((IAttribute)var1).getName());
   }
}
