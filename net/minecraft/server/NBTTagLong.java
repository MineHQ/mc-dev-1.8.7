package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagLong extends NBTBase.NBTNumber {
   private long data;

   NBTTagLong() {
   }

   public NBTTagLong(long var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeLong(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(128L);
      this.data = var1.readLong();
   }

   public byte getTypeId() {
      return (byte)4;
   }

   public String toString() {
      return "" + this.data + "L";
   }

   public NBTBase clone() {
      return new NBTTagLong(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagLong var2 = (NBTTagLong)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
   }

   public long c() {
      return this.data;
   }

   public int d() {
      return (int)(this.data & -1L);
   }

   public short e() {
      return (short)((int)(this.data & 65535L));
   }

   public byte f() {
      return (byte)((int)(this.data & 255L));
   }

   public double g() {
      return (double)this.data;
   }

   public float h() {
      return (float)this.data;
   }
}
