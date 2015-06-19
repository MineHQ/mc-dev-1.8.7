package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.server.NBTReadLimiter;
import net.minecraft.server.NBTTagByte;
import net.minecraft.server.NBTTagByteArray;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagEnd;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagInt;
import net.minecraft.server.NBTTagIntArray;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagLong;
import net.minecraft.server.NBTTagShort;
import net.minecraft.server.NBTTagString;

public abstract class NBTBase {
   public static final String[] a = new String[]{"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"};

   abstract void write(DataOutput var1) throws IOException;

   abstract void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException;

   public abstract String toString();

   public abstract byte getTypeId();

   protected NBTBase() {
   }

   protected static NBTBase createTag(byte var0) {
      switch(var0) {
      case 0:
         return new NBTTagEnd();
      case 1:
         return new NBTTagByte();
      case 2:
         return new NBTTagShort();
      case 3:
         return new NBTTagInt();
      case 4:
         return new NBTTagLong();
      case 5:
         return new NBTTagFloat();
      case 6:
         return new NBTTagDouble();
      case 7:
         return new NBTTagByteArray();
      case 8:
         return new NBTTagString();
      case 9:
         return new NBTTagList();
      case 10:
         return new NBTTagCompound();
      case 11:
         return new NBTTagIntArray();
      default:
         return null;
      }
   }

   public abstract NBTBase clone();

   public boolean isEmpty() {
      return false;
   }

   public boolean equals(Object var1) {
      if(!(var1 instanceof NBTBase)) {
         return false;
      } else {
         NBTBase var2 = (NBTBase)var1;
         return this.getTypeId() == var2.getTypeId();
      }
   }

   public int hashCode() {
      return this.getTypeId();
   }

   protected String a_() {
      return this.toString();
   }

   public abstract static class NBTNumber extends NBTBase {
      protected NBTNumber() {
      }

      public abstract long c();

      public abstract int d();

      public abstract short e();

      public abstract byte f();

      public abstract double g();

      public abstract float h();
   }
}
