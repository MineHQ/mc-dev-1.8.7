package net.minecraft.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagEnd;
import net.minecraft.server.ReportedException;

public class NBTCompressedStreamTools {
   public static NBTTagCompound a(InputStream var0) throws IOException {
      DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(var0)));

      NBTTagCompound var2;
      try {
         var2 = a((DataInput)var1, (NBTReadLimiter)NBTReadLimiter.a);
      } finally {
         var1.close();
      }

      return var2;
   }

   public static void a(NBTTagCompound var0, OutputStream var1) throws IOException {
      DataOutputStream var2 = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(var1)));

      try {
         a((NBTTagCompound)var0, (DataOutput)var2);
      } finally {
         var2.close();
      }

   }

   public static NBTTagCompound a(DataInputStream var0) throws IOException {
      return a((DataInput)var0, (NBTReadLimiter)NBTReadLimiter.a);
   }

   public static NBTTagCompound a(DataInput var0, NBTReadLimiter var1) throws IOException {
      NBTBase var2 = a(var0, 0, var1);
      if(var2 instanceof NBTTagCompound) {
         return (NBTTagCompound)var2;
      } else {
         throw new IOException("Root tag must be a named compound tag");
      }
   }

   public static void a(NBTTagCompound var0, DataOutput var1) throws IOException {
      a((NBTBase)var0, (DataOutput)var1);
   }

   private static void a(NBTBase var0, DataOutput var1) throws IOException {
      var1.writeByte(var0.getTypeId());
      if(var0.getTypeId() != 0) {
         var1.writeUTF("");
         var0.write(var1);
      }
   }

   private static NBTBase a(DataInput var0, int var1, NBTReadLimiter var2) throws IOException {
      byte var3 = var0.readByte();
      if(var3 == 0) {
         return new NBTTagEnd();
      } else {
         var0.readUTF();
         NBTBase var4 = NBTBase.createTag(var3);

         try {
            var4.load(var0, var1, var2);
            return var4;
         } catch (IOException var8) {
            CrashReport var6 = CrashReport.a(var8, "Loading NBT data");
            CrashReportSystemDetails var7 = var6.a("NBT Tag");
            var7.a((String)"Tag name", (Object)"[UNNAMED TAG]");
            var7.a((String)"Tag type", (Object)Byte.valueOf(var3));
            throw new ReportedException(var6);
         }
      }
   }
}
