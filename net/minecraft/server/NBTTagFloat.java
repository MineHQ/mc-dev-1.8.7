package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagFloat extends NBTBase.NBTNumber {
   private float data;

   NBTTagFloat() {
   }

   public NBTTagFloat(float var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeFloat(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(96L);
      this.data = var1.readFloat();
   }

   public byte getTypeId() {
      return (byte)5;
   }

   public String toString() {
      return "" + this.data + "f";
   }

   public NBTBase clone() {
      return new NBTTagFloat(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagFloat var2 = (NBTTagFloat)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return super.hashCode() ^ Float.floatToIntBits(this.data);
   }

   public long c() {
      return (long)this.data;
   }

   public int d() {
      return MathHelper.d(this.data);
   }

   public short e() {
      return (short)(MathHelper.d(this.data) & '\uffff');
   }

   public byte f() {
      return (byte)(MathHelper.d(this.data) & 255);
   }

   public double g() {
      return (double)this.data;
   }

   public float h() {
      return this.data;
   }
}
