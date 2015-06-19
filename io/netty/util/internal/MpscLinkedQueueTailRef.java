package io.netty.util.internal;

import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.MpscLinkedQueuePad1;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef<E> extends MpscLinkedQueuePad1<E> {
   private static final long serialVersionUID = 8717072462993327429L;
   private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;
   private transient volatile MpscLinkedQueueNode<E> tailRef;

   MpscLinkedQueueTailRef() {
   }

   protected final MpscLinkedQueueNode<E> tailRef() {
      return this.tailRef;
   }

   protected final void setTailRef(MpscLinkedQueueNode<E> var1) {
      this.tailRef = var1;
   }

   protected final MpscLinkedQueueNode<E> getAndSetTailRef(MpscLinkedQueueNode<E> var1) {
      return (MpscLinkedQueueNode)UPDATER.getAndSet(this, var1);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
      if(var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
      }

      UPDATER = var0;
   }
}
