package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagByte extends NBTBase.NBTNumber {
   private byte data;

   NBTTagByte() {
   }

   public NBTTagByte(byte var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeByte(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(72L);
      this.data = var1.readByte();
   }

   public byte getTypeId() {
      return (byte)1;
   }

   public String toString() {
      return "" + this.data + "b";
   }

   public NBTBase clone() {
      return new NBTTagByte(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagByte var2 = (NBTTagByte)var1;
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
      return (short)this.data;
   }

   public byte f() {
      return this.data;
   }

   public double g() {
      return (double)this.data;
   }

   public float h() {
      return (float)this.data;
   }
}
