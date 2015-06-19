package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.cache.RemovalNotification;

@Beta
@GwtCompatible
public interface RemovalListener<K, V> {
   void onRemoval(RemovalNotification<K, V> var1);
}
