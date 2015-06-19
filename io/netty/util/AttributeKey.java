package io.netty.util;

import io.netty.util.UniqueName;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class AttributeKey<T> extends UniqueName {
   private static final ConcurrentMap<String, Boolean> names = PlatformDependent.newConcurrentHashMap();

   public static <T> AttributeKey<T> valueOf(String var0) {
      return new AttributeKey(var0);
   }

   /** @deprecated */
   @Deprecated
   public AttributeKey(String var1) {
      super(names, var1, new Object[0]);
   }
}
