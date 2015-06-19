package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.util.concurrent.Executor;

@Beta
public final class RemovalListeners {
   private RemovalListeners() {
   }

   public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> var0, final Executor var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new RemovalListener() {
         public void onRemoval(final RemovalNotification<K, V> var1x) {
            var1.execute(new Runnable() {
               public void run() {
                  var0.onRemoval(var1x);
               }
            });
         }
      };
   }
}
