package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.epoll.Native;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.PlatformDependent;

final class IovArray implements ChannelOutboundBuffer.MessageProcessor {
   private static final int ADDRESS_SIZE = PlatformDependent.addressSize();
   private static final int IOV_SIZE;
   private static final int CAPACITY;
   private static final FastThreadLocal<IovArray> ARRAY;
   private final long memoryAddress;
   private int count;
   private long size;

   private IovArray() {
      this.memoryAddress = PlatformDependent.allocateMemory((long)CAPACITY);
   }

   private boolean add(ByteBuf var1) {
      if(this.count == Native.IOV_MAX) {
         return false;
      } else {
         int var2 = var1.readableBytes();
         if(var2 == 0) {
            return true;
         } else {
            long var3 = var1.memoryAddress();
            int var5 = var1.readerIndex();
            long var6 = this.memoryAddress(this.count++);
            long var8 = var6 + (long)ADDRESS_SIZE;
            if(ADDRESS_SIZE == 8) {
               PlatformDependent.putLong(var6, var3 + (long)var5);
               PlatformDependent.putLong(var8, (long)var2);
            } else {
               assert ADDRESS_SIZE == 4;

               PlatformDependent.putInt(var6, (int)var3 + var5);
               PlatformDependent.putInt(var8, var2);
            }

            this.size += (long)var2;
            return true;
         }
      }
   }

   long processWritten(int var1, long var2) {
      long var4 = this.memoryAddress(var1);
      long var6 = var4 + (long)ADDRESS_SIZE;
      long var8;
      if(ADDRESS_SIZE == 8) {
         var8 = PlatformDependent.getLong(var6);
         if(var8 > var2) {
            long var12 = PlatformDependent.getLong(var4);
            PlatformDependent.putLong(var4, var12 + var2);
            PlatformDependent.putLong(var6, var8 - var2);
            return -1L;
         } else {
            return var8;
         }
      } else {
         assert ADDRESS_SIZE == 4;

         var8 = (long)PlatformDependent.getInt(var6);
         if(var8 > var2) {
            int var10 = PlatformDependent.getInt(var4);
            PlatformDependent.putInt(var4, (int)((long)var10 + var2));
            PlatformDependent.putInt(var6, (int)(var8 - var2));
            return -1L;
         } else {
            return var8;
         }
      }
   }

   int count() {
      return this.count;
   }

   long size() {
      return this.size;
   }

   long memoryAddress(int var1) {
      return this.memoryAddress + (long)(IOV_SIZE * var1);
   }

   public boolean processMessage(Object var1) throws Exception {
      return var1 instanceof ByteBuf && this.add((ByteBuf)var1);
   }

   static IovArray get(ChannelOutboundBuffer var0) throws Exception {
      IovArray var1 = (IovArray)ARRAY.get();
      var1.size = 0L;
      var1.count = 0;
      var0.forEachFlushedMessage(var1);
      return var1;
   }

   // $FF: synthetic method
   IovArray(Object var1) {
      this();
   }

   static {
      IOV_SIZE = 2 * ADDRESS_SIZE;
      CAPACITY = Native.IOV_MAX * IOV_SIZE;
      ARRAY = new FastThreadLocal() {
         protected IovArray initialValue() throws Exception {
            return new IovArray(null);
         }

         protected void onRemoval(IovArray var1) throws Exception {
            PlatformDependent.freeMemory(var1.memoryAddress);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected void onRemoval(Object var1) throws Exception {
            this.onRemoval((IovArray)var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object initialValue() throws Exception {
            return this.initialValue();
         }
      };
   }
}
