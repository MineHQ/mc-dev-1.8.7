package io.netty.util.internal;

import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.MpscLinkedQueuePad0;
import io.netty.util.internal.PlatformDependent;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueHeadRef<E> extends MpscLinkedQueuePad0<E> implements Serializable {
   private static final long serialVersionUID = 8467054865577874285L;
   private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;
   private transient volatile MpscLinkedQueueNode<E> headRef;

   MpscLinkedQueueHeadRef() {
   }

   protected final MpscLinkedQueueNode<E> headRef() {
      return this.headRef;
   }

   protected final void setHeadRef(MpscLinkedQueueNode<E> var1) {
      this.headRef = var1;
   }

   protected final void lazySetHeadRef(MpscLinkedQueueNode<E> var1) {
      UPDATER.lazySet(this, var1);
   }

   static {
      AtomicReferenceFieldUpdater var0 = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
      if(var0 == null) {
         var0 = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
      }

      UPDATER = var0;
   }
}
