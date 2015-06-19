package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;

public class NBTTagEnd extends NBTBase {
   NBTTagEnd() {
   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(64L);
   }

   void write(DataOutput var1) throws IOException {
   }

   public byte getTypeId() {
      return (byte)0;
   }

   public String toString() {
      return "END";
   }

   public NBTBase clone() {
      return new NBTTagEnd();
   }
}
