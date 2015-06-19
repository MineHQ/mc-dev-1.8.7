package com.google.common.collect;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MapConstraint;
import com.google.common.collect.MapConstraints;
import com.google.common.primitives.Primitives;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MutableClassToInstanceMap<B> extends MapConstraints.ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {
   private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint() {
      public void checkKeyValue(Class<?> var1, Object var2) {
         MutableClassToInstanceMap.cast(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void checkKeyValue(Object var1, Object var2) {
         this.checkKeyValue((Class)var1, var2);
      }
   };
   private static final long serialVersionUID = 0L;

   public static <B> MutableClassToInstanceMap<B> create() {
      return new MutableClassToInstanceMap(new HashMap());
   }

   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> var0) {
      return new MutableClassToInstanceMap(var0);
   }

   private MutableClassToInstanceMap(Map<Class<? extends B>, B> var1) {
      super(var1, VALUE_CAN_BE_CAST_TO_KEY);
   }

   public <T extends B> T putInstance(Class<T> var1, T var2) {
      return cast(var1, this.put(var1, var2));
   }

   public <T extends B> T getInstance(Class<T> var1) {
      return cast(var1, this.get(var1));
   }

   private static <B, T extends B> T cast(Class<T> var0, B var1) {
      return Primitives.wrap(var0).cast(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void putAll(Map var1) {
      super.putAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return super.entrySet();
   }
}
