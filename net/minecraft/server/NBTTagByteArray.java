package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagByteArray extends NBTBase {
   private byte[] data;

   NBTTagByteArray() {
   }

   public NBTTagByteArray(byte[] var1) {
      this.data = var1;
   }

   void write(DataOutput var1) throws IOException {
      var1.writeInt(this.data.length);
      var1.write(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(192L);
      int var4 = var1.readInt();
      var3.a((long)(8 * var4));
      this.data = new byte[var4];
      var1.readFully(this.data);
   }

   public byte getTypeId() {
      return (byte)7;
   }

   public String toString() {
      return "[" + this.data.length + " bytes]";
   }

   public NBTBase clone() {
      byte[] var1 = new byte[this.data.length];
      System.arraycopy(this.data, 0, var1, 0, this.data.length);
      return new NBTTagByteArray(var1);
   }

   public boolean equals(Object var1) {
      return super.equals(var1)?Arrays.equals(this.data, ((NBTTagByteArray)var1).data):false;
   }

   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.data);
   }

   public byte[] c() {
      return this.data;
   }
}
