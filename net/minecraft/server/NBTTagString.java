package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagString extends NBTBase {
   private String data;

   public NBTTagString() {
      this.data = "";
   }

   public NBTTagString(String var1) {
      this.data = var1;
      if(var1 == null) {
         throw new IllegalArgumentException("Empty string not allowed");
      }
   }

   void write(DataOutput var1) throws IOException {
      var1.writeUTF(this.data);
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(288L);
      this.data = var1.readUTF();
      var3.a((long)(16 * this.data.length()));
   }

   public byte getTypeId() {
      return (byte)8;
   }

   public String toString() {
      return "\"" + this.data.replace("\"", "\\\"") + "\"";
   }

   public NBTBase clone() {
      return new NBTTagString(this.data);
   }

   public boolean isEmpty() {
      return this.data.isEmpty();
   }

   public boolean equals(Object var1) {
      if(!super.equals(var1)) {
         return false;
      } else {
         NBTTagString var2 = (NBTTagString)var1;
         return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
      }
   }

   public int hashCode() {
      return super.hashCode() ^ this.data.hashCode();
   }

   public String a_() {
      return this.data;
   }
}
