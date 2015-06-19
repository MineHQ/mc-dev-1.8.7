package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToInstanceMap;
import com.google.common.reflect.TypeToken;
import java.util.Map;

@Beta
public final class ImmutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {
   private final ImmutableMap<TypeToken<? extends B>, B> delegate;

   public static <B> ImmutableTypeToInstanceMap<B> of() {
      return new ImmutableTypeToInstanceMap(ImmutableMap.of());
   }

   public static <B> ImmutableTypeToInstanceMap.Builder<B> builder() {
      return new ImmutableTypeToInstanceMap.Builder();
   }

   private ImmutableTypeToInstanceMap(ImmutableMap<TypeToken<? extends B>, B> var1) {
      this.delegate = var1;
   }

   public <T extends B> T getInstance(TypeToken<T> var1) {
      return this.trustedGet(var1.rejectTypeVariables());
   }

   public <T extends B> T putInstance(TypeToken<T> var1, T var2) {
      throw new UnsupportedOperationException();
   }

   public <T extends B> T getInstance(Class<T> var1) {
      return this.trustedGet(TypeToken.of(var1));
   }

   public <T extends B> T putInstance(Class<T> var1, T var2) {
      throw new UnsupportedOperationException();
   }

   protected Map<TypeToken<? extends B>, B> delegate() {
      return this.delegate;
   }

   private <T extends B> T trustedGet(TypeToken<T> var1) {
      return this.delegate.get(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   ImmutableTypeToInstanceMap(ImmutableMap var1, ImmutableTypeToInstanceMap.SyntheticClass_1 var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   @Beta
   public static final class Builder<B> {
      private final ImmutableMap.Builder<TypeToken<? extends B>, B> mapBuilder;

      private Builder() {
         this.mapBuilder = ImmutableMap.builder();
      }

      public <T extends B> ImmutableTypeToInstanceMap.Builder<B> put(Class<T> var1, T var2) {
         this.mapBuilder.put(TypeToken.of(var1), var2);
         return this;
      }

      public <T extends B> ImmutableTypeToInstanceMap.Builder<B> put(TypeToken<T> var1, T var2) {
         this.mapBuilder.put(var1.rejectTypeVariables(), var2);
         return this;
      }

      public ImmutableTypeToInstanceMap<B> build() {
         return new ImmutableTypeToInstanceMap(this.mapBuilder.build());
      }

      // $FF: synthetic method
      Builder(ImmutableTypeToInstanceMap.SyntheticClass_1 var1) {
         this();
      }
   }
}
