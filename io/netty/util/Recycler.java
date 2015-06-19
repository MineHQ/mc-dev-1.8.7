package io.netty.util;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Recycler<T> {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Recycler.class);
   private static final AtomicInteger ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
   private static final int OWN_THREAD_ID;
   private static final int DEFAULT_MAX_CAPACITY;
   private static final int INITIAL_CAPACITY;
   private final int maxCapacity;
   private final FastThreadLocal<Recycler.Stack<T>> threadLocal;
   private static final FastThreadLocal<Map<Recycler.Stack<?>, Recycler.WeakOrderQueue>> DELAYED_RECYCLED;

   protected Recycler() {
      this(DEFAULT_MAX_CAPACITY);
   }

   protected Recycler(int var1) {
      this.threadLocal = new FastThreadLocal() {
         protected Recycler.Stack<T> initialValue() {
            return new Recycler.Stack(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacity);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object initialValue() throws Exception {
            return this.initialValue();
         }
      };
      this.maxCapacity = Math.max(0, var1);
   }

   public final T get() {
      Recycler.Stack var1 = (Recycler.Stack)this.threadLocal.get();
      Recycler.DefaultHandle var2 = var1.pop();
      if(var2 == null) {
         var2 = var1.newHandle();
         var2.value = this.newObject(var2);
      }

      return var2.value;
   }

   public final boolean recycle(T var1, Recycler.Handle var2) {
      Recycler.DefaultHandle var3 = (Recycler.DefaultHandle)var2;
      if(var3.stack.parent != this) {
         return false;
      } else if(var1 != var3.value) {
         throw new IllegalArgumentException("o does not belong to handle");
      } else {
         var3.recycle();
         return true;
      }
   }

   protected abstract T newObject(Recycler.Handle var1);

   static {
      OWN_THREAD_ID = ID_GENERATOR.getAndIncrement();
      int var0 = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity.default", 0);
      if(var0 <= 0) {
         var0 = 262144;
      }

      DEFAULT_MAX_CAPACITY = var0;
      if(logger.isDebugEnabled()) {
         logger.debug("-Dio.netty.recycler.maxCapacity.default: {}", (Object)Integer.valueOf(DEFAULT_MAX_CAPACITY));
      }

      INITIAL_CAPACITY = Math.min(DEFAULT_MAX_CAPACITY, 256);
      DELAYED_RECYCLED = new FastThreadLocal() {
         protected Map<Recycler.Stack<?>, Recycler.WeakOrderQueue> initialValue() {
            return new WeakHashMap();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object initialValue() throws Exception {
            return this.initialValue();
         }
      };
   }

   static final class Stack<T> {
      final Recycler<T> parent;
      final Thread thread;
      private Recycler.DefaultHandle[] elements;
      private final int maxCapacity;
      private int size;
      private volatile Recycler.WeakOrderQueue head;
      private Recycler.WeakOrderQueue cursor;
      private Recycler.WeakOrderQueue prev;

      Stack(Recycler<T> var1, Thread var2, int var3) {
         this.parent = var1;
         this.thread = var2;
         this.maxCapacity = var3;
         this.elements = new Recycler.DefaultHandle[Recycler.INITIAL_CAPACITY];
      }

      Recycler.DefaultHandle pop() {
         int var1 = this.size;
         if(var1 == 0) {
            if(!this.scavenge()) {
               return null;
            }

            var1 = this.size;
         }

         --var1;
         Recycler.DefaultHandle var2 = this.elements[var1];
         if(var2.lastRecycledId != var2.recycleId) {
            throw new IllegalStateException("recycled multiple times");
         } else {
            var2.recycleId = 0;
            var2.lastRecycledId = 0;
            this.size = var1;
            return var2;
         }
      }

      boolean scavenge() {
         if(this.scavengeSome()) {
            return true;
         } else {
            this.prev = null;
            this.cursor = this.head;
            return false;
         }
      }

      boolean scavengeSome() {
         boolean var1 = false;
         Recycler.WeakOrderQueue var2 = this.cursor;

         Recycler.WeakOrderQueue var3;
         Recycler.WeakOrderQueue var4;
         for(var3 = this.prev; var2 != null; var2 = var4) {
            if(var2.transfer(this)) {
               var1 = true;
               break;
            }

            var4 = var2.next;
            if(var2.owner.get() == null) {
               if(var2.hasFinalData()) {
                  while(var2.transfer(this)) {
                     ;
                  }
               }

               if(var3 != null) {
                  var3.next = var4;
               }
            } else {
               var3 = var2;
            }
         }

         this.prev = var3;
         this.cursor = var2;
         return var1;
      }

      void push(Recycler.DefaultHandle var1) {
         if((var1.recycleId | var1.lastRecycledId) != 0) {
            throw new IllegalStateException("recycled already");
         } else {
            var1.recycleId = var1.lastRecycledId = Recycler.OWN_THREAD_ID;
            int var2 = this.size;
            if(var2 == this.elements.length) {
               if(var2 == this.maxCapacity) {
                  return;
               }

               this.elements = (Recycler.DefaultHandle[])Arrays.copyOf(this.elements, var2 << 1);
            }

            this.elements[var2] = var1;
            this.size = var2 + 1;
         }
      }

      Recycler.DefaultHandle newHandle() {
         return new Recycler.DefaultHandle(this);
      }
   }

   private static final class WeakOrderQueue {
      private static final int LINK_CAPACITY = 16;
      private Recycler.WeakOrderQueue.WeakOrderQueue$Link head;
      private Recycler.WeakOrderQueue.WeakOrderQueue$Link tail;
      private Recycler.WeakOrderQueue next;
      private final WeakReference<Thread> owner;
      private final int id;

      WeakOrderQueue(Recycler.Stack<?> var1, Thread var2) {
         this.id = Recycler.ID_GENERATOR.getAndIncrement();
         this.head = this.tail = new Recycler.WeakOrderQueue.WeakOrderQueue$Link(null);
         this.owner = new WeakReference(var2);
         synchronized(var1) {
            this.next = var1.head;
            var1.head = this;
         }
      }

      void add(Recycler.DefaultHandle var1) {
         var1.lastRecycledId = this.id;
         Recycler.WeakOrderQueue.WeakOrderQueue$Link var2 = this.tail;
         int var3;
         if((var3 = var2.get()) == 16) {
            this.tail = var2 = var2.next = new Recycler.WeakOrderQueue.WeakOrderQueue$Link(null);
            var3 = var2.get();
         }

         var2.elements[var3] = var1;
         var1.stack = null;
         var2.lazySet(var3 + 1);
      }

      boolean hasFinalData() {
         return this.tail.readIndex != this.tail.get();
      }

      boolean transfer(Recycler.Stack<?> var1) {
         Recycler.WeakOrderQueue.WeakOrderQueue$Link var2 = this.head;
         if(var2 == null) {
            return false;
         } else {
            if(var2.readIndex == 16) {
               if(var2.next == null) {
                  return false;
               }

               this.head = var2 = var2.next;
            }

            int var3 = var2.readIndex;
            int var4 = var2.get();
            if(var3 == var4) {
               return false;
            } else {
               int var5 = var4 - var3;
               if(var1.size + var5 > var1.elements.length) {
                  var1.elements = (Recycler.DefaultHandle[])Arrays.copyOf(var1.elements, (var1.size + var5) * 2);
               }

               Recycler.DefaultHandle[] var6 = var2.elements;
               Recycler.DefaultHandle[] var7 = var1.elements;

               int var8;
               for(var8 = var1.size; var3 < var4; var6[var3++] = null) {
                  Recycler.DefaultHandle var9 = var6[var3];
                  if(var9.recycleId == 0) {
                     var9.recycleId = var9.lastRecycledId;
                  } else if(var9.recycleId != var9.lastRecycledId) {
                     throw new IllegalStateException("recycled already");
                  }

                  var9.stack = var1;
                  var7[var8++] = var9;
               }

               var1.size = var8;
               if(var4 == 16 && var2.next != null) {
                  this.head = var2.next;
               }

               var2.readIndex = var4;
               return true;
            }
         }
      }

      private static final class WeakOrderQueue$Link extends AtomicInteger {
         private final Recycler.DefaultHandle[] elements;
         private int readIndex;
         private Recycler.WeakOrderQueue.WeakOrderQueue$Link next;

         private WeakOrderQueue$Link() {
            this.elements = new Recycler.DefaultHandle[16];
         }

         // $FF: synthetic method
         WeakOrderQueue$Link(Object var1) {
            this();
         }
      }
   }

   static final class DefaultHandle implements Recycler.Handle {
      private int lastRecycledId;
      private int recycleId;
      private Recycler.Stack<?> stack;
      private Object value;

      DefaultHandle(Recycler.Stack<?> var1) {
         this.stack = var1;
      }

      public void recycle() {
         Thread var1 = Thread.currentThread();
         if(var1 == this.stack.thread) {
            this.stack.push(this);
         } else {
            Map var2 = (Map)Recycler.DELAYED_RECYCLED.get();
            Recycler.WeakOrderQueue var3 = (Recycler.WeakOrderQueue)var2.get(this.stack);
            if(var3 == null) {
               var2.put(this.stack, var3 = new Recycler.WeakOrderQueue(this.stack, var1));
            }

            var3.add(this);
         }
      }
   }

   public interface Handle {
   }
}
