package net.minecraft.server;

import net.minecraft.server.NBTTagCompound;

public class ChestLock {
   public static final ChestLock a = new ChestLock("");
   private final String b;

   public ChestLock(String var1) {
      this.b = var1;
   }

   public boolean a() {
      return this.b == null || this.b.isEmpty();
   }

   public String b() {
      return this.b;
   }

   public void a(NBTTagCompound var1) {
      var1.setString("Lock", this.b);
   }

   public static ChestLock b(NBTTagCompound var0) {
      if(var0.hasKeyOfType("Lock", 8)) {
         String var1 = var0.getString("Lock");
         return new ChestLock(var1);
      } else {
         return a;
      }
   }
}
