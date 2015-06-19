package net.minecraft.server;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileEntityComparator extends TileEntity {
   private int a;

   public TileEntityComparator() {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("OutputSignal", this.a);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = var1.getInt("OutputSignal");
   }

   public int b() {
      return this.a;
   }

   public void a(int var1) {
      this.a = var1;
   }
}
