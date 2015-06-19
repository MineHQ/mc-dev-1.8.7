package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.Entity;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.ReportedException;
import net.minecraft.server.Vector3f;
import org.apache.commons.lang3.ObjectUtils;

public class DataWatcher {
   private final Entity a;
   private boolean b = true;
   private static final Map<Class<?>, Integer> c = Maps.newHashMap();
   private final Map<Integer, DataWatcher.WatchableObject> d = Maps.newHashMap();
   private boolean e;
   private ReadWriteLock f = new ReentrantReadWriteLock();

   public DataWatcher(Entity var1) {
      this.a = var1;
   }

   public <T> void a(int var1, T var2) {
      Integer var3 = (Integer)c.get(var2.getClass());
      if(var3 == null) {
         throw new IllegalArgumentException("Unknown data type: " + var2.getClass());
      } else if(var1 > 31) {
         throw new IllegalArgumentException("Data value id is too big with " + var1 + "! (Max is " + 31 + ")");
      } else if(this.d.containsKey(Integer.valueOf(var1))) {
         throw new IllegalArgumentException("Duplicate id value for " + var1 + "!");
      } else {
         DataWatcher.WatchableObject var4 = new DataWatcher.WatchableObject(var3.intValue(), var1, var2);
         this.f.writeLock().lock();
         this.d.put(Integer.valueOf(var1), var4);
         this.f.writeLock().unlock();
         this.b = false;
      }
   }

   public void add(int var1, int var2) {
      DataWatcher.WatchableObject var3 = new DataWatcher.WatchableObject(var2, var1, (Object)null);
      this.f.writeLock().lock();
      this.d.put(Integer.valueOf(var1), var3);
      this.f.writeLock().unlock();
      this.b = false;
   }

   public byte getByte(int var1) {
      return ((Byte)this.j(var1).b()).byteValue();
   }

   public short getShort(int var1) {
      return ((Short)this.j(var1).b()).shortValue();
   }

   public int getInt(int var1) {
      return ((Integer)this.j(var1).b()).intValue();
   }

   public float getFloat(int var1) {
      return ((Float)this.j(var1).b()).floatValue();
   }

   public String getString(int var1) {
      return (String)this.j(var1).b();
   }

   public ItemStack getItemStack(int var1) {
      return (ItemStack)this.j(var1).b();
   }

   private DataWatcher.WatchableObject j(int var1) {
      this.f.readLock().lock();

      DataWatcher.WatchableObject var2;
      try {
         var2 = (DataWatcher.WatchableObject)this.d.get(Integer.valueOf(var1));
      } catch (Throwable var6) {
         CrashReport var4 = CrashReport.a(var6, "Getting synched entity data");
         CrashReportSystemDetails var5 = var4.a("Synched entity data");
         var5.a((String)"Data ID", (Object)Integer.valueOf(var1));
         throw new ReportedException(var4);
      }

      this.f.readLock().unlock();
      return var2;
   }

   public Vector3f h(int var1) {
      return (Vector3f)this.j(var1).b();
   }

   public <T> void watch(int var1, T var2) {
      DataWatcher.WatchableObject var3 = this.j(var1);
      if(ObjectUtils.notEqual(var2, var3.b())) {
         var3.a(var2);
         this.a.i(var1);
         var3.a(true);
         this.e = true;
      }

   }

   public void update(int var1) {
      this.j(var1).d = true;
      this.e = true;
   }

   public boolean a() {
      return this.e;
   }

   public static void a(List<DataWatcher.WatchableObject> var0, PacketDataSerializer var1) throws IOException {
      if(var0 != null) {
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject)var2.next();
            a(var1, var3);
         }
      }

      var1.writeByte(127);
   }

   public List<DataWatcher.WatchableObject> b() {
      ArrayList var1 = null;
      if(this.e) {
         this.f.readLock().lock();
         Iterator var2 = this.d.values().iterator();

         while(var2.hasNext()) {
            DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject)var2.next();
            if(var3.d()) {
               var3.a(false);
               if(var1 == null) {
                  var1 = Lists.newArrayList();
               }

               var1.add(var3);
            }
         }

         this.f.readLock().unlock();
      }

      this.e = false;
      return var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.f.readLock().lock();
      Iterator var2 = this.d.values().iterator();

      while(var2.hasNext()) {
         DataWatcher.WatchableObject var3 = (DataWatcher.WatchableObject)var2.next();
         a(var1, var3);
      }

      this.f.readLock().unlock();
      var1.writeByte(127);
   }

   public List<DataWatcher.WatchableObject> c() {
      ArrayList var1 = null;
      this.f.readLock().lock();

      DataWatcher.WatchableObject var3;
      for(Iterator var2 = this.d.values().iterator(); var2.hasNext(); var1.add(var3)) {
         var3 = (DataWatcher.WatchableObject)var2.next();
         if(var1 == null) {
            var1 = Lists.newArrayList();
         }
      }

      this.f.readLock().unlock();
      return var1;
   }

   private static void a(PacketDataSerializer var0, DataWatcher.WatchableObject var1) throws IOException {
      int var2 = (var1.c() << 5 | var1.a() & 31) & 255;
      var0.writeByte(var2);
      switch(var1.c()) {
      case 0:
         var0.writeByte(((Byte)var1.b()).byteValue());
         break;
      case 1:
         var0.writeShort(((Short)var1.b()).shortValue());
         break;
      case 2:
         var0.writeInt(((Integer)var1.b()).intValue());
         break;
      case 3:
         var0.writeFloat(((Float)var1.b()).floatValue());
         break;
      case 4:
         var0.a((String)var1.b());
         break;
      case 5:
         ItemStack var3 = (ItemStack)var1.b();
         var0.a(var3);
         break;
      case 6:
         BlockPosition var4 = (BlockPosition)var1.b();
         var0.writeInt(var4.getX());
         var0.writeInt(var4.getY());
         var0.writeInt(var4.getZ());
         break;
      case 7:
         Vector3f var5 = (Vector3f)var1.b();
         var0.writeFloat(var5.getX());
         var0.writeFloat(var5.getY());
         var0.writeFloat(var5.getZ());
      }

   }

   public static List<DataWatcher.WatchableObject> b(PacketDataSerializer var0) throws IOException {
      ArrayList var1 = null;

      for(byte var2 = var0.readByte(); var2 != 127; var2 = var0.readByte()) {
         if(var1 == null) {
            var1 = Lists.newArrayList();
         }

         int var3 = (var2 & 224) >> 5;
         int var4 = var2 & 31;
         DataWatcher.WatchableObject var5 = null;
         switch(var3) {
         case 0:
            var5 = new DataWatcher.WatchableObject(var3, var4, Byte.valueOf(var0.readByte()));
            break;
         case 1:
            var5 = new DataWatcher.WatchableObject(var3, var4, Short.valueOf(var0.readShort()));
            break;
         case 2:
            var5 = new DataWatcher.WatchableObject(var3, var4, Integer.valueOf(var0.readInt()));
            break;
         case 3:
            var5 = new DataWatcher.WatchableObject(var3, var4, Float.valueOf(var0.readFloat()));
            break;
         case 4:
            var5 = new DataWatcher.WatchableObject(var3, var4, var0.c(32767));
            break;
         case 5:
            var5 = new DataWatcher.WatchableObject(var3, var4, var0.i());
            break;
         case 6:
            int var6 = var0.readInt();
            int var7 = var0.readInt();
            int var8 = var0.readInt();
            var5 = new DataWatcher.WatchableObject(var3, var4, new BlockPosition(var6, var7, var8));
            break;
         case 7:
            float var9 = var0.readFloat();
            float var10 = var0.readFloat();
            float var11 = var0.readFloat();
            var5 = new DataWatcher.WatchableObject(var3, var4, new Vector3f(var9, var10, var11));
         }

         var1.add(var5);
      }

      return var1;
   }

   public boolean d() {
      return this.b;
   }

   public void e() {
      this.e = false;
   }

   static {
      c.put(Byte.class, Integer.valueOf(0));
      c.put(Short.class, Integer.valueOf(1));
      c.put(Integer.class, Integer.valueOf(2));
      c.put(Float.class, Integer.valueOf(3));
      c.put(String.class, Integer.valueOf(4));
      c.put(ItemStack.class, Integer.valueOf(5));
      c.put(BlockPosition.class, Integer.valueOf(6));
      c.put(Vector3f.class, Integer.valueOf(7));
   }

   public static class WatchableObject {
      private final int a;
      private final int b;
      private Object c;
      private boolean d;

      public WatchableObject(int var1, int var2, Object var3) {
         this.b = var2;
         this.c = var3;
         this.a = var1;
         this.d = true;
      }

      public int a() {
         return this.b;
      }

      public void a(Object var1) {
         this.c = var1;
      }

      public Object b() {
         return this.c;
      }

      public int c() {
         return this.a;
      }

      public boolean d() {
         return this.d;
      }

      public void a(boolean var1) {
         this.d = var1;
      }
   }
}
