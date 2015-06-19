package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagIntArray extends NBTBase {
   private int[] data;

   NBTTagIntArray() {
   }

   public NBTTagIntArray(int[] var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeInt(this.data.length);

      for(int var2 = 0; var2 < this.data.length; ++var2) {
         var1.writeInt(this.data[var2]);
      }

   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(192L);
      int var4 = var1.readInt();
      var3.a((long)(32 * var4));
      this.data = new int[var4];

      for(int var5 = 0; var5 < var4; ++var5) {
         this.data[var5] = var1.readInt();
      }

   }

   public byte getTypeId() {
      return (byte)11;
   }

   public String toString() {
      String var1 = "[";
      int[] var2 = this.data;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int var5 = var2[var4];
         var1 = var1 + var5 + ",";
      }

      return var1 + "]";
   }

   public NBTBase clone() {
      int[] var1 = new int[this.data.length];
      System.arraycopy(this.data, 0, var1, 0, this.data.length);
      return new NBTTagIntArray(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1)?Arrays.equals(this.data, ((NBTTagIntArray)var1).data):false;
   }

   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.data);
   }

   public int[] c() {
      return this.data;
   }
}
