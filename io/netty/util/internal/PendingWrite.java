package io.netty.util.internal;

import io.netty.util.Recycler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

public final class PendingWrite {
   private static final Recycler<PendingWrite> RECYCLER = new Recycler() {
      protected PendingWrite newObject(Recycler.Handle var1) {
         return new PendingWrite(var1, null);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object newObject(Recycler.Handle var1) {
         return this.newObject(var1);
      }
   };
   private final Recycler.Handle handle;
   private Object msg;
   private Promise<Void> promise;

   public static PendingWrite newInstance(Object var0, Promise<Void> var1) {
      PendingWrite var2 = (PendingWrite)RECYCLER.get();
      var2.msg = var0;
      var2.promise = var1;
      return var2;
   }

   private PendingWrite(Recycler.Handle var1) {
      this.handle = var1;
   }

   public boolean recycle() {
      this.msg = null;
      this.promise = null;
      return RECYCLER.recycle(this, this.handle);
   }

   public boolean failAndRecycle(Throwable var1) {
      ReferenceCountUtil.release(this.msg);
      if(this.promise != null) {
         this.promise.setFailure(var1);
      }

      return this.recycle();
   }

   public boolean successAndRecycle() {
      if(this.promise != null) {
         this.promise.setSuccess((Object)null);
      }

      return this.recycle();
   }

   public Object msg() {
      return this.msg;
   }

   public Promise<Void> promise() {
      return this.promise;
   }

   public Promise<Void> recycleAndGet() {
      Promise var1 = this.promise;
      this.recycle();
      return var1;
   }

   // $FF: synthetic method
   PendingWrite(Recycler.Handle var1, Object var2) {
      this(var1);
   }
}
