package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode<T> {
   private static final AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> nextUpdater;
   private volatile MpscLinkedQueueNode<T> next;

   public MpscLinkedQueueNode() {
   }

   final MpscLinkedQueueNode<T> next() {
      return this.next;
   }

   final void setNext(MpscLinkedQueueNode<T> var1) {
      nextUpdater.lazySet(this, var1);
   }

   public abstract T value();

   protected T clearMaybe() {
      return this.value();
   }

   void unlink() {
      this.setNext((MpscLinkedQueueNode)null);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
      if(var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next");
      }

      nextUpdater = var0;
   }
}
