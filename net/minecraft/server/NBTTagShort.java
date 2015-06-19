package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagShort extends NBTBase.NBTNumber {
   private short data;

   public NBTTagShort() {
   }

   public NBTTagShort(short var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeShort(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(80L);
      this.data = var1.readShort();
   }

   public byte getTypeId() {
      return (byte)2;
   }

   public String toString() {
      return "" + this.data + "s";
   }

   public NBTBase clone() {
      return new NBTTagShort(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagShort var2 = (NBTTagShort)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return super.hashCode() ^ this.data;
   }

   public long c() {
      return (long)this.data;
   }

   public int d() {
      return this.data;
   }

   public short e() {
      return this.data;
   }

   public byte f() {
      return (byte)(this.data & 255);
   }

   public double g() {
      return (double)this.data;
   }

   public float h() {
      return (float)this.data;
   }
}
