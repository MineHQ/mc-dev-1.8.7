package io.netty.util.internal;

import io.netty.util.internal.MpscLinkedQueueNode;

public abstract class OneTimeTask extends MpscLinkedQueueNode<Runnable> implements Runnable {
   public OneTimeTask() {
   }

   public Runnable value() {
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object value() {
      return this.value();
   }
}
