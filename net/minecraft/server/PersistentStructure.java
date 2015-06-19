package net.minecraft.server;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PersistentBase;

public class PersistentStructure extends PersistentBase {
   private NBTTagCompound b = new NBTTagCompound();

   public PersistentStructure(String var1) {
      super(var1);
   }

   public void a(NBTTagCompound var1) {
      this.b = var1.getCompound("Features");
   }

   public void b(NBTTagCompound var1) {
      var1.set("Features", this.b);
   }

   public void a(NBTTagCompound var1, int var2, int var3) {
      this.b.set(b(var2, var3), var1);
   }

   public static String b(int var0, int var1) {
      return "[" + var0 + "," + var1 + "]";
   }

   public NBTTagCompound a() {
      return this.b;
   }
}
