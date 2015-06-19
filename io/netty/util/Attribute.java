package io.netty.util;

import io.netty.util.AttributeKey;

public interface Attribute<T> {
   AttributeKey<T> key();

   T get();

   void set(T var1);

   T getAndSet(T var1);

   T setIfAbsent(T var1);

   T getAndRemove();

   boolean compareAndSet(T var1, T var2);

   void remove();
}
