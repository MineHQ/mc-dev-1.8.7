package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public final class ImmutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
   private final ImmutableMap<Class<? extends B>, B> delegate;

   public static <B> ImmutableClassToInstanceMap.Builder<B> builder() {
      return new ImmutableClassToInstanceMap.Builder();
   }

   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> var0) {
      if(var0 instanceof ImmutableClassToInstanceMap) {
         ImmutableClassToInstanceMap var1 = (ImmutableClassToInstanceMap)var0;
         return var1;
      } else {
         return (new ImmutableClassToInstanceMap.Builder()).putAll(var0).build();
      }
   }

   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> var1) {
      this.delegate = var1;
   }

   protected Map<Class<? extends B>, B> delegate() {
      return this.delegate;
   }

   @Nullable
   public <T extends B> T getInstance(Class<T> var1) {
      return this.delegate.get(Preconditions.checkNotNull(var1));
   }

   /** @deprecated */
   @Deprecated
   public <T extends B> T putInstance(Class<T> var1, T var2) {
      throw new UnsupportedOperationException();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   ImmutableClassToInstanceMap(ImmutableMap var1, ImmutableClassToInstanceMap.SyntheticClass_1 var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   public static final class Builder<B> {
      private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();

      public Builder() {
      }

      public <T extends B> ImmutableClassToInstanceMap.Builder<B> put(Class<T> var1, T var2) {
         this.mapBuilder.put(var1, var2);
         return this;
      }

      public <T extends B> ImmutableClassToInstanceMap.Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            Class var4 = (Class)var3.getKey();
            Object var5 = var3.getValue();
            this.mapBuilder.put(var4, cast(var4, var5));
         }

         return this;
      }

      private static <B, T extends B> T cast(Class<T> var0, B var1) {
         return Primitives.wrap(var0).cast(var1);
      }

      public ImmutableClassToInstanceMap<B> build() {
         return new ImmutableClassToInstanceMap(this.mapBuilder.build());
      }
   }
}
