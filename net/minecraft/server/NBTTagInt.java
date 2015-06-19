package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagInt extends NBTBase.NBTNumber {
   private int data;

   NBTTagInt() {
   }

   public NBTTagInt(int var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeInt(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(96L);
      this.data = var1.readInt();
   }

   public byte getTypeId() {
      return (byte)3;
   }

   public String toString() {
      return "" + this.data;
   }

   public NBTBase clone() {
      return new NBTTagInt(this.data);
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagInt var2 = (NBTTagInt)var1;
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
      return (short)(this.data & '\uffff');
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
