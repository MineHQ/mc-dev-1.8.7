package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Synchronized;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public final class Queues {
   private Queues() {
   }

   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int var0) {
      return new ArrayBlockingQueue(var0);
   }

   public static <E> ArrayDeque<E> newArrayDeque() {
      return new ArrayDeque();
   }

   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new ArrayDeque(Collections2.cast(var0));
      } else {
         ArrayDeque var1 = new ArrayDeque();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
      return new ConcurrentLinkedQueue();
   }

   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new ConcurrentLinkedQueue(Collections2.cast(var0));
      } else {
         ConcurrentLinkedQueue var1 = new ConcurrentLinkedQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
      return new LinkedBlockingDeque();
   }

   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int var0) {
      return new LinkedBlockingDeque(var0);
   }

   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new LinkedBlockingDeque(Collections2.cast(var0));
      } else {
         LinkedBlockingDeque var1 = new LinkedBlockingDeque();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
      return new LinkedBlockingQueue();
   }

   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int var0) {
      return new LinkedBlockingQueue(var0);
   }

   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new LinkedBlockingQueue(Collections2.cast(var0));
      } else {
         LinkedBlockingQueue var1 = new LinkedBlockingQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
      return new PriorityBlockingQueue();
   }

   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new PriorityBlockingQueue(Collections2.cast(var0));
      } else {
         PriorityBlockingQueue var1 = new PriorityBlockingQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
      return new PriorityQueue();
   }

   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> var0) {
      if(var0 instanceof Collection) {
         return new PriorityQueue(Collections2.cast(var0));
      } else {
         PriorityQueue var1 = new PriorityQueue();
         Iterables.addAll(var1, var0);
         return var1;
      }
   }

   public static <E> SynchronousQueue<E> newSynchronousQueue() {
      return new SynchronousQueue();
   }

   @Beta
   public static <E> int drain(BlockingQueue<E> var0, Collection<? super E> var1, int var2, long var3, TimeUnit var5) throws InterruptedException {
      Preconditions.checkNotNull(var1);
      long var6 = System.nanoTime() + var5.toNanos(var3);
      int var8 = 0;

      while(var8 < var2) {
         var8 += var0.drainTo(var1, var2 - var8);
         if(var8 < var2) {
            Object var9 = var0.poll(var6 - System.nanoTime(), TimeUnit.NANOSECONDS);
            if(var9 == null) {
               break;
            }

            var1.add(var9);
            ++var8;
         }
      }

      return var8;
   }

   @Beta
   public static <E> int drainUninterruptibly(BlockingQueue<E> var0, Collection<? super E> var1, int var2, long var3, TimeUnit var5) {
      Preconditions.checkNotNull(var1);
      long var6 = System.nanoTime() + var5.toNanos(var3);
      int var8 = 0;
      boolean var9 = false;

      try {
         while(var8 < var2) {
            var8 += var0.drainTo(var1, var2 - var8);
            if(var8 < var2) {
               Object var10;
               while(true) {
                  try {
                     var10 = var0.poll(var6 - System.nanoTime(), TimeUnit.NANOSECONDS);
                     break;
                  } catch (InterruptedException var15) {
                     var9 = true;
                  }
               }

               if(var10 == null) {
                  break;
               }

               var1.add(var10);
               ++var8;
            }
         }
      } finally {
         if(var9) {
            Thread.currentThread().interrupt();
         }

      }

      return var8;
   }

   @Beta
   public static <E> Queue<E> synchronizedQueue(Queue<E> var0) {
      return Synchronized.queue(var0, (Object)null);
   }

   @Beta
   public static <E> Deque<E> synchronizedDeque(Deque<E> var0) {
      return Synchronized.deque(var0, (Object)null);
   }
}
