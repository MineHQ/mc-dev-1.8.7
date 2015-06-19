package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagDouble extends NBTBase.NBTNumber {
   private double data;

   NBTTagDouble() {
   }

   public NBTTagDouble(double var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeDouble(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(128L);
      this.data = var1.readDouble();
   }

   public byte getTypeId() {
      return (byte)6;
   }

   public String toString() {
      return "" + this.data + "d";
   }

   public NBTBase clone() {
      return new NBTTagDouble(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagDouble var2 = (NBTTagDouble)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.data);
      return super.hashCode() ^ (int)(var1 ^ var1 >>> 32);
   }

   public long c() {
      return (long)Math.floor(this.data);
   }

   public int d() {
      return MathHelper.floor(this.data);
   }

   public short e() {
      return (short)(MathHelper.floor(this.data) & '\uffff');
   }

   public byte f() {
      return (byte)(MathHelper.floor(this.data) & 255);
   }

   public double g() {
      return this.data;
   }

   public float h() {
      return (float)this.data;
   }
}
