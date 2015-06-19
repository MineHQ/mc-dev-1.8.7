package com.sun.jna;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WeakIdentityHashMap;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Memory extends Pointer {
   private static final Map buffers;
   protected long size;

   public static void purge() {
      buffers.size();
   }

   public Memory(long var1) {
      this.size = var1;
      if(var1 <= 0L) {
         throw new IllegalArgumentException("Allocation size must be greater than zero");
      } else {
         this.peer = malloc(var1);
         if(this.peer == 0L) {
            throw new OutOfMemoryError("Cannot allocate " + var1 + " bytes");
         }
      }
   }

   protected Memory() {
   }

   public Pointer share(long var1) {
      return this.share(var1, this.getSize() - var1);
   }

   public Pointer share(long var1, long var3) {
      if(var1 == 0L && var3 == this.getSize()) {
         return this;
      } else {
         this.boundsCheck(var1, var3);
         return new Memory.SharedMemory(var1);
      }
   }

   public Memory align(int var1) {
      if(var1 <= 0) {
         throw new IllegalArgumentException("Byte boundary must be positive: " + var1);
      } else {
         for(int var2 = 0; var2 < 32; ++var2) {
            if(var1 == 1 << var2) {
               long var3 = ~((long)var1 - 1L);
               if((this.peer & var3) != this.peer) {
                  long var5 = this.peer + (long)var1 - 1L & var3;
                  long var7 = this.peer + this.size - var5;
                  if(var7 <= 0L) {
                     throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
                  }

                  return (Memory)this.share(var5 - this.peer, var7);
               }

               return this;
            }
         }

         throw new IllegalArgumentException("Byte boundary must be a power of two");
      }
   }

   protected void finalize() {
      this.dispose();
   }

   protected synchronized void dispose() {
      free(this.peer);
      this.peer = 0L;
   }

   public void clear() {
      this.clear(this.size);
   }

   /** @deprecated */
   public boolean isValid() {
      return this.valid();
   }

   public boolean valid() {
      return this.peer != 0L;
   }

   public long size() {
      return this.size;
   }

   /** @deprecated */
   public long getSize() {
      return this.size();
   }

   protected void boundsCheck(long var1, long var3) {
      if(var1 < 0L) {
         throw new IndexOutOfBoundsException("Invalid offset: " + var1);
      } else if(var1 + var3 > this.size) {
         String var5 = "Bounds exceeds available space : size=" + this.size + ", offset=" + (var1 + var3);
         throw new IndexOutOfBoundsException(var5);
      }
   }

   public void read(long var1, byte[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 1L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, short[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 2L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, char[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 2L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, int[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 4L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, long[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 8L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, float[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 4L);
      super.read(var1, var3, var4, var5);
   }

   public void read(long var1, double[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 8L);
      super.read(var1, var3, var4, var5);
   }

   public void write(long var1, byte[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 1L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, short[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 2L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, char[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 2L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, int[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 4L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, long[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 8L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, float[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 4L);
      super.write(var1, var3, var4, var5);
   }

   public void write(long var1, double[] var3, int var4, int var5) {
      this.boundsCheck(var1, (long)var5 * 8L);
      super.write(var1, var3, var4, var5);
   }

   public byte getByte(long var1) {
      this.boundsCheck(var1, 1L);
      return super.getByte(var1);
   }

   public char getChar(long var1) {
      this.boundsCheck(var1, 1L);
      return super.getChar(var1);
   }

   public short getShort(long var1) {
      this.boundsCheck(var1, 2L);
      return super.getShort(var1);
   }

   public int getInt(long var1) {
      this.boundsCheck(var1, 4L);
      return super.getInt(var1);
   }

   public long getLong(long var1) {
      this.boundsCheck(var1, 8L);
      return super.getLong(var1);
   }

   public float getFloat(long var1) {
      this.boundsCheck(var1, 4L);
      return super.getFloat(var1);
   }

   public double getDouble(long var1) {
      this.boundsCheck(var1, 8L);
      return super.getDouble(var1);
   }

   public Pointer getPointer(long var1) {
      this.boundsCheck(var1, (long)Pointer.SIZE);
      return super.getPointer(var1);
   }

   public ByteBuffer getByteBuffer(long var1, long var3) {
      this.boundsCheck(var1, var3);
      ByteBuffer var5 = super.getByteBuffer(var1, var3);
      buffers.put(var5, this);
      return var5;
   }

   public String getString(long var1, boolean var3) {
      this.boundsCheck(var1, 0L);
      return super.getString(var1, var3);
   }

   public void setByte(long var1, byte var3) {
      this.boundsCheck(var1, 1L);
      super.setByte(var1, var3);
   }

   public void setChar(long var1, char var3) {
      this.boundsCheck(var1, (long)Native.WCHAR_SIZE);
      super.setChar(var1, var3);
   }

   public void setShort(long var1, short var3) {
      this.boundsCheck(var1, 2L);
      super.setShort(var1, var3);
   }

   public void setInt(long var1, int var3) {
      this.boundsCheck(var1, 4L);
      super.setInt(var1, var3);
   }

   public void setLong(long var1, long var3) {
      this.boundsCheck(var1, 8L);
      super.setLong(var1, var3);
   }

   public void setFloat(long var1, float var3) {
      this.boundsCheck(var1, 4L);
      super.setFloat(var1, var3);
   }

   public void setDouble(long var1, double var3) {
      this.boundsCheck(var1, 8L);
      super.setDouble(var1, var3);
   }

   public void setPointer(long var1, Pointer var3) {
      this.boundsCheck(var1, (long)Pointer.SIZE);
      super.setPointer(var1, var3);
   }

   public void setString(long var1, String var3, boolean var4) {
      if(var4) {
         this.boundsCheck(var1, ((long)var3.length() + 1L) * (long)Native.WCHAR_SIZE);
      } else {
         this.boundsCheck(var1, (long)var3.getBytes().length + 1L);
      }

      super.setString(var1, var3, var4);
   }

   public String toString() {
      return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
   }

   protected static void free(long var0) {
      Native.free(var0);
   }

   protected static long malloc(long var0) {
      return Native.malloc(var0);
   }

   static {
      buffers = Collections.synchronizedMap((Map)(Platform.HAS_BUFFERS?new WeakIdentityHashMap():new HashMap()));
   }

   private class SharedMemory extends Memory {
      public SharedMemory(long var2) {
         this.size = Memory.this.size - var2;
         this.peer = Memory.this.peer + var2;
      }

      protected void finalize() {
      }

      protected void boundsCheck(long var1, long var3) {
         Memory.this.boundsCheck(this.peer - Memory.this.peer + var1, var3);
      }

      public String toString() {
         return super.toString() + " (shared from " + Memory.this.toString() + ")";
      }
   }
}
