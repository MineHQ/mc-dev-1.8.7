package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingQueue;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

@Beta
@GwtIncompatible("java.util.ArrayDeque")
public final class EvictingQueue<E> extends ForwardingQueue<E> implements Serializable {
   private final Queue<E> delegate;
   @VisibleForTesting
   final int maxSize;
   private static final long serialVersionUID = 0L;

   private EvictingQueue(int var1) {
      Preconditions.checkArgument(var1 >= 0, "maxSize (%s) must >= 0", new Object[]{Integer.valueOf(var1)});
      this.delegate = new ArrayDeque(var1);
      this.maxSize = var1;
   }

   public static <E> EvictingQueue<E> create(int var0) {
      return new EvictingQueue(var0);
   }

   public int remainingCapacity() {
      return this.maxSize - this.size();
   }

   protected Queue<E> delegate() {
      return this.delegate;
   }

   public boolean offer(E var1) {
      return this.add(var1);
   }

   public boolean add(E var1) {
      Preconditions.checkNotNull(var1);
      if(this.maxSize == 0) {
         return true;
      } else {
         if(this.size() == this.maxSize) {
            this.delegate.remove();
         }

         this.delegate.add(var1);
         return true;
      }
   }

   public boolean addAll(Collection<? extends E> var1) {
      return this.standardAddAll(var1);
   }

   public boolean contains(Object var1) {
      return this.delegate().contains(Preconditions.checkNotNull(var1));
   }

   public boolean remove(Object var1) {
      return this.delegate().remove(Preconditions.checkNotNull(var1));
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Collection delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }
}
