package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeCapture;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeResolver;
import com.google.common.reflect.TypeVisitor;
import com.google.common.reflect.Types;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
   private final Type runtimeType;
   private transient TypeResolver typeResolver;

   protected TypeToken() {
      this.runtimeType = this.capture();
      Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", new Object[]{this.runtimeType});
   }

   protected TypeToken(Class<?> var1) {
      Type var2 = super.capture();
      if(var2 instanceof Class) {
         this.runtimeType = var2;
      } else {
         this.runtimeType = of(var1).resolveType(var2).runtimeType;
      }

   }

   private TypeToken(Type var1) {
      this.runtimeType = (Type)Preconditions.checkNotNull(var1);
   }

   public static <T> TypeToken<T> of(Class<T> var0) {
      return new TypeToken.SimpleTypeToken(var0);
   }

   public static TypeToken<?> of(Type var0) {
      return new TypeToken.SimpleTypeToken(var0);
   }

   public final Class<? super T> getRawType() {
      Class var1 = getRawType(this.runtimeType);
      return var1;
   }

   private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
      ImmutableSet var1 = getRawTypes(this.runtimeType);
      return var1;
   }

   public final Type getType() {
      return this.runtimeType;
   }

   public final <X> TypeToken<T> where(TypeParameter<X> var1, TypeToken<X> var2) {
      TypeResolver var3 = (new TypeResolver()).where(ImmutableMap.of(new TypeResolver.TypeVariableKey(var1.typeVariable), var2.runtimeType));
      return new TypeToken.SimpleTypeToken(var3.resolveType(this.runtimeType));
   }

   public final <X> TypeToken<T> where(TypeParameter<X> var1, Class<X> var2) {
      return this.where(var1, of(var2));
   }

   public final TypeToken<?> resolveType(Type var1) {
      Preconditions.checkNotNull(var1);
      TypeResolver var2 = this.typeResolver;
      if(var2 == null) {
         var2 = this.typeResolver = TypeResolver.accordingTo(this.runtimeType);
      }

      return of(var2.resolveType(var1));
   }

   private Type[] resolveInPlace(Type[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = this.resolveType(var1[var2]).getType();
      }

      return var1;
   }

   private TypeToken<?> resolveSupertype(Type var1) {
      TypeToken var2 = this.resolveType(var1);
      var2.typeResolver = this.typeResolver;
      return var2;
   }

   @Nullable
   final TypeToken<? super T> getGenericSuperclass() {
      if(this.runtimeType instanceof TypeVariable) {
         return this.boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
      } else if(this.runtimeType instanceof WildcardType) {
         return this.boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
      } else {
         Type var1 = this.getRawType().getGenericSuperclass();
         if(var1 == null) {
            return null;
         } else {
            TypeToken var2 = this.resolveSupertype(var1);
            return var2;
         }
      }
   }

   @Nullable
   private TypeToken<? super T> boundAsSuperclass(Type var1) {
      TypeToken var2 = of(var1);
      return var2.getRawType().isInterface()?null:var2;
   }

   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
      if(this.runtimeType instanceof TypeVariable) {
         return this.boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
      } else if(this.runtimeType instanceof WildcardType) {
         return this.boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
      } else {
         ImmutableList.Builder var1 = ImmutableList.builder();
         Type[] var2 = this.getRawType().getGenericInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Type var5 = var2[var4];
            TypeToken var6 = this.resolveSupertype(var5);
            var1.add((Object)var6);
         }

         return var1.build();
      }
   }

   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] var1) {
      ImmutableList.Builder var2 = ImmutableList.builder();
      Type[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type var6 = var3[var5];
         TypeToken var7 = of(var6);
         if(var7.getRawType().isInterface()) {
            var2.add((Object)var7);
         }
      }

      return var2.build();
   }

   public final TypeToken<T>.TypeSet getTypes() {
      return new TypeToken.TypeSet();
   }

   public final TypeToken<? super T> getSupertype(Class<? super T> var1) {
      Preconditions.checkArgument(var1.isAssignableFrom(this.getRawType()), "%s is not a super class of %s", new Object[]{var1, this});
      if(this.runtimeType instanceof TypeVariable) {
         return this.getSupertypeFromUpperBounds(var1, ((TypeVariable)this.runtimeType).getBounds());
      } else if(this.runtimeType instanceof WildcardType) {
         return this.getSupertypeFromUpperBounds(var1, ((WildcardType)this.runtimeType).getUpperBounds());
      } else if(var1.isArray()) {
         return this.getArraySupertype(var1);
      } else {
         TypeToken var2 = this.resolveSupertype(toGenericType(var1).runtimeType);
         return var2;
      }
   }

   public final TypeToken<? extends T> getSubtype(Class<?> var1) {
      Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", new Object[]{this});
      if(this.runtimeType instanceof WildcardType) {
         return this.getSubtypeFromLowerBounds(var1, ((WildcardType)this.runtimeType).getLowerBounds());
      } else {
         Preconditions.checkArgument(this.getRawType().isAssignableFrom(var1), "%s isn\'t a subclass of %s", new Object[]{var1, this});
         if(this.isArray()) {
            return this.getArraySubtype(var1);
         } else {
            TypeToken var2 = of(this.resolveTypeArgsForSubclass(var1));
            return var2;
         }
      }
   }

   public final boolean isAssignableFrom(TypeToken<?> var1) {
      return this.isAssignableFrom(var1.runtimeType);
   }

   public final boolean isAssignableFrom(Type var1) {
      return isAssignable((Type)Preconditions.checkNotNull(var1), this.runtimeType);
   }

   public final boolean isArray() {
      return this.getComponentType() != null;
   }

   public final boolean isPrimitive() {
      return this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive();
   }

   public final TypeToken<T> wrap() {
      if(this.isPrimitive()) {
         Class var1 = (Class)this.runtimeType;
         return of(Primitives.wrap(var1));
      } else {
         return this;
      }
   }

   private boolean isWrapper() {
      return Primitives.allWrapperTypes().contains(this.runtimeType);
   }

   public final TypeToken<T> unwrap() {
      if(this.isWrapper()) {
         Class var1 = (Class)this.runtimeType;
         return of(Primitives.unwrap(var1));
      } else {
         return this;
      }
   }

   @Nullable
   public final TypeToken<?> getComponentType() {
      Type var1 = Types.getComponentType(this.runtimeType);
      return var1 == null?null:of(var1);
   }

   public final Invokable<T, Object> method(final Method var1) {
      Preconditions.checkArgument(of(var1.getDeclaringClass()).isAssignableFrom(this), "%s not declared by %s", new Object[]{var1, this});
      return new Invokable.MethodInvokable(var1) {
         Type getGenericReturnType() {
            return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
         }

         Type[] getGenericParameterTypes() {
            return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
         }

         Type[] getGenericExceptionTypes() {
            return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
         }

         public TypeToken<T> getOwnerType() {
            return TypeToken.this;
         }

         public String toString() {
            return this.getOwnerType() + "." + super.toString();
         }
      };
   }

   public final Invokable<T, T> constructor(final Constructor<?> var1) {
      Preconditions.checkArgument(var1.getDeclaringClass() == this.getRawType(), "%s not declared by %s", new Object[]{var1, this.getRawType()});
      return new Invokable.ConstructorInvokable(var1) {
         Type getGenericReturnType() {
            return TypeToken.this.resolveType(super.getGenericReturnType()).getType();
         }

         Type[] getGenericParameterTypes() {
            return TypeToken.this.resolveInPlace(super.getGenericParameterTypes());
         }

         Type[] getGenericExceptionTypes() {
            return TypeToken.this.resolveInPlace(super.getGenericExceptionTypes());
         }

         public TypeToken<T> getOwnerType() {
            return TypeToken.this;
         }

         public String toString() {
            return this.getOwnerType() + "(" + Joiner.on(", ").join((Object[])this.getGenericParameterTypes()) + ")";
         }
      };
   }

   public boolean equals(@Nullable Object var1) {
      if(var1 instanceof TypeToken) {
         TypeToken var2 = (TypeToken)var1;
         return this.runtimeType.equals(var2.runtimeType);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.runtimeType.hashCode();
   }

   public String toString() {
      return Types.toString(this.runtimeType);
   }

   protected Object writeReplace() {
      return of((new TypeResolver()).resolveType(this.runtimeType));
   }

   final TypeToken<T> rejectTypeVariables() {
      (new TypeVisitor() {
         void visitTypeVariable(TypeVariable<?> var1) {
            throw new IllegalArgumentException(TypeToken.this.runtimeType + "contains a type variable and is not safe for the operation");
         }

         void visitWildcardType(WildcardType var1) {
            this.visit(var1.getLowerBounds());
            this.visit(var1.getUpperBounds());
         }

         void visitParameterizedType(ParameterizedType var1) {
            this.visit(var1.getActualTypeArguments());
            this.visit(new Type[]{var1.getOwnerType()});
         }

         void visitGenericArrayType(GenericArrayType var1) {
            this.visit(new Type[]{var1.getGenericComponentType()});
         }
      }).visit(new Type[]{this.runtimeType});
      return this;
   }

   private static boolean isAssignable(Type var0, Type var1) {
      return var1.equals(var0)?true:(var1 instanceof WildcardType?isAssignableToWildcardType(var0, (WildcardType)var1):(var0 instanceof TypeVariable?isAssignableFromAny(((TypeVariable)var0).getBounds(), var1):(var0 instanceof WildcardType?isAssignableFromAny(((WildcardType)var0).getUpperBounds(), var1):(var0 instanceof GenericArrayType?isAssignableFromGenericArrayType((GenericArrayType)var0, var1):(var1 instanceof Class?isAssignableToClass(var0, (Class)var1):(var1 instanceof ParameterizedType?isAssignableToParameterizedType(var0, (ParameterizedType)var1):(var1 instanceof GenericArrayType?isAssignableToGenericArrayType(var0, (GenericArrayType)var1):false)))))));
   }

   private static boolean isAssignableFromAny(Type[] var0, Type var1) {
      Type[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Type var5 = var2[var4];
         if(isAssignable(var5, var1)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isAssignableToClass(Type var0, Class<?> var1) {
      return var1.isAssignableFrom(getRawType(var0));
   }

   private static boolean isAssignableToWildcardType(Type var0, WildcardType var1) {
      return isAssignable(var0, supertypeBound(var1)) && isAssignableBySubtypeBound(var0, var1);
   }

   private static boolean isAssignableBySubtypeBound(Type var0, WildcardType var1) {
      Type var2 = subtypeBound(var1);
      if(var2 == null) {
         return true;
      } else {
         Type var3 = subtypeBound(var0);
         return var3 == null?false:isAssignable(var2, var3);
      }
   }

   private static boolean isAssignableToParameterizedType(Type var0, ParameterizedType var1) {
      Class var2 = getRawType(var1);
      if(!var2.isAssignableFrom(getRawType(var0))) {
         return false;
      } else {
         TypeVariable[] var3 = var2.getTypeParameters();
         Type[] var4 = var1.getActualTypeArguments();
         TypeToken var5 = of(var0);

         for(int var6 = 0; var6 < var3.length; ++var6) {
            Type var7 = var5.resolveType(var3[var6]).runtimeType;
            if(!matchTypeArgument(var7, var4[var6])) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean isAssignableToGenericArrayType(Type var0, GenericArrayType var1) {
      if(var0 instanceof Class) {
         Class var3 = (Class)var0;
         return !var3.isArray()?false:isAssignable(var3.getComponentType(), var1.getGenericComponentType());
      } else if(var0 instanceof GenericArrayType) {
         GenericArrayType var2 = (GenericArrayType)var0;
         return isAssignable(var2.getGenericComponentType(), var1.getGenericComponentType());
      } else {
         return false;
      }
   }

   private static boolean isAssignableFromGenericArrayType(GenericArrayType var0, Type var1) {
      if(var1 instanceof Class) {
         Class var3 = (Class)var1;
         return !var3.isArray()?var3 == Object.class:isAssignable(var0.getGenericComponentType(), var3.getComponentType());
      } else if(var1 instanceof GenericArrayType) {
         GenericArrayType var2 = (GenericArrayType)var1;
         return isAssignable(var0.getGenericComponentType(), var2.getGenericComponentType());
      } else {
         return false;
      }
   }

   private static boolean matchTypeArgument(Type var0, Type var1) {
      return var0.equals(var1)?true:(var1 instanceof WildcardType?isAssignableToWildcardType(var0, (WildcardType)var1):false);
   }

   private static Type supertypeBound(Type var0) {
      return var0 instanceof WildcardType?supertypeBound((WildcardType)var0):var0;
   }

   private static Type supertypeBound(WildcardType var0) {
      Type[] var1 = var0.getUpperBounds();
      if(var1.length == 1) {
         return supertypeBound(var1[0]);
      } else if(var1.length == 0) {
         return Object.class;
      } else {
         throw new AssertionError("There should be at most one upper bound for wildcard type: " + var0);
      }
   }

   @Nullable
   private static Type subtypeBound(Type var0) {
      return var0 instanceof WildcardType?subtypeBound((WildcardType)var0):var0;
   }

   @Nullable
   private static Type subtypeBound(WildcardType var0) {
      Type[] var1 = var0.getLowerBounds();
      if(var1.length == 1) {
         return subtypeBound(var1[0]);
      } else if(var1.length == 0) {
         return null;
      } else {
         throw new AssertionError("Wildcard should have at most one lower bound: " + var0);
      }
   }

   @VisibleForTesting
   static Class<?> getRawType(Type var0) {
      return (Class)getRawTypes(var0).iterator().next();
   }

   @VisibleForTesting
   static ImmutableSet<Class<?>> getRawTypes(Type var0) {
      Preconditions.checkNotNull(var0);
      final ImmutableSet.Builder var1 = ImmutableSet.builder();
      (new TypeVisitor() {
         void visitTypeVariable(TypeVariable<?> var1x) {
            this.visit(var1x.getBounds());
         }

         void visitWildcardType(WildcardType var1x) {
            this.visit(var1x.getUpperBounds());
         }

         void visitParameterizedType(ParameterizedType var1x) {
            var1.add((Object)((Class)var1x.getRawType()));
         }

         void visitClass(Class<?> var1x) {
            var1.add((Object)var1x);
         }

         void visitGenericArrayType(GenericArrayType var1x) {
            var1.add((Object)Types.getArrayClass(TypeToken.getRawType(var1x.getGenericComponentType())));
         }
      }).visit(new Type[]{var0});
      return var1.build();
   }

   @VisibleForTesting
   static <T> TypeToken<? extends T> toGenericType(Class<T> var0) {
      TypeToken var2;
      if(var0.isArray()) {
         Type var3 = Types.newArrayType(toGenericType(var0.getComponentType()).runtimeType);
         var2 = of(var3);
         return var2;
      } else {
         TypeVariable[] var1 = var0.getTypeParameters();
         if(var1.length > 0) {
            var2 = of((Type)Types.newParameterizedType(var0, var1));
            return var2;
         } else {
            return of(var0);
         }
      }
   }

   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> var1, Type[] var2) {
      Type[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type var6 = var3[var5];
         TypeToken var7 = of(var6);
         if(of(var1).isAssignableFrom(var7)) {
            TypeToken var8 = var7.getSupertype(var1);
            return var8;
         }
      }

      throw new IllegalArgumentException(var1 + " isn\'t a super type of " + this);
   }

   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> var1, Type[] var2) {
      int var4 = var2.length;
      byte var5 = 0;
      if(var5 < var4) {
         Type var6 = var2[var5];
         TypeToken var7 = of(var6);
         return var7.getSubtype(var1);
      } else {
         throw new IllegalArgumentException(var1 + " isn\'t a subclass of " + this);
      }
   }

   private TypeToken<? super T> getArraySupertype(Class<? super T> var1) {
      TypeToken var2 = (TypeToken)Preconditions.checkNotNull(this.getComponentType(), "%s isn\'t a super type of %s", new Object[]{var1, this});
      TypeToken var3 = var2.getSupertype(var1.getComponentType());
      TypeToken var4 = of(newArrayClassOrGenericArrayType(var3.runtimeType));
      return var4;
   }

   private TypeToken<? extends T> getArraySubtype(Class<?> var1) {
      TypeToken var2 = this.getComponentType().getSubtype(var1.getComponentType());
      TypeToken var3 = of(newArrayClassOrGenericArrayType(var2.runtimeType));
      return var3;
   }

   private Type resolveTypeArgsForSubclass(Class<?> var1) {
      if(this.runtimeType instanceof Class) {
         return var1;
      } else {
         TypeToken var2 = toGenericType(var1);
         Type var3 = var2.getSupertype(this.getRawType()).runtimeType;
         return (new TypeResolver()).where(var3, this.runtimeType).resolveType(var2.runtimeType);
      }
   }

   private static Type newArrayClassOrGenericArrayType(Type var0) {
      return Types.JavaVersion.JAVA7.newArrayType(var0);
   }

   // $FF: synthetic method
   TypeToken(Type var1, Object var2) {
      this(var1);
   }

   private abstract static class TypeCollector<K> {
      static final TypeToken.TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeToken.TypeCollector(null) {
         Class<?> getRawType(TypeToken<?> var1) {
            return var1.getRawType();
         }

         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> var1) {
            return var1.getGenericInterfaces();
         }

         @Nullable
         TypeToken<?> getSuperclass(TypeToken<?> var1) {
            return var1.getGenericSuperclass();
         }

         // $FF: synthetic method
         // $FF: bridge method
         Object getSuperclass(Object var1) {
            return this.getSuperclass((TypeToken)var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         Iterable getInterfaces(Object var1) {
            return this.getInterfaces((TypeToken)var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         Class getRawType(Object var1) {
            return this.getRawType((TypeToken)var1);
         }
      };
      static final TypeToken.TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeToken.TypeCollector(null) {
         Class<?> getRawType(Class<?> var1) {
            return var1;
         }

         Iterable<? extends Class<?>> getInterfaces(Class<?> var1) {
            return Arrays.asList(var1.getInterfaces());
         }

         @Nullable
         Class<?> getSuperclass(Class<?> var1) {
            return var1.getSuperclass();
         }

         // $FF: synthetic method
         // $FF: bridge method
         Object getSuperclass(Object var1) {
            return this.getSuperclass((Class)var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         Iterable getInterfaces(Object var1) {
            return this.getInterfaces((Class)var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         Class getRawType(Object var1) {
            return this.getRawType((Class)var1);
         }
      };

      private TypeCollector() {
      }

      final TypeToken.TypeCollector<K> classesOnly() {
         return new TypeToken.TypeCollector.TypeCollector$ForwardingTypeCollector(this) {
            Iterable<? extends K> getInterfaces(K var1) {
               return ImmutableSet.of();
            }

            ImmutableList<K> collectTypes(Iterable<? extends K> var1) {
               ImmutableList.Builder var2 = ImmutableList.builder();
               Iterator var3 = var1.iterator();

               while(var3.hasNext()) {
                  Object var4 = var3.next();
                  if(!this.getRawType(var4).isInterface()) {
                     var2.add(var4);
                  }
               }

               return super.collectTypes(var2.build());
            }
         };
      }

      final ImmutableList<K> collectTypes(K var1) {
         return this.collectTypes((Iterable)ImmutableList.of(var1));
      }

      ImmutableList<K> collectTypes(Iterable<? extends K> var1) {
         HashMap var2 = Maps.newHashMap();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            this.collectTypes(var4, var2);
         }

         return sortKeysByValue(var2, Ordering.natural().reverse());
      }

      private int collectTypes(K var1, Map<? super K, Integer> var2) {
         Integer var3 = (Integer)var2.get(this);
         if(var3 != null) {
            return var3.intValue();
         } else {
            int var4 = this.getRawType(var1).isInterface()?1:0;

            Object var6;
            for(Iterator var5 = this.getInterfaces(var1).iterator(); var5.hasNext(); var4 = Math.max(var4, this.collectTypes(var6, var2))) {
               var6 = var5.next();
            }

            Object var7 = this.getSuperclass(var1);
            if(var7 != null) {
               var4 = Math.max(var4, this.collectTypes(var7, var2));
            }

            var2.put(var1, Integer.valueOf(var4 + 1));
            return var4 + 1;
         }
      }

      private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> var0, final Comparator<? super V> var1) {
         Ordering var2 = new Ordering() {
            public int compare(K var1x, K var2) {
               return var1.compare(var0.get(var1x), var0.get(var2));
            }
         };
         return var2.immutableSortedCopy(var0.keySet());
      }

      abstract Class<?> getRawType(K var1);

      abstract Iterable<? extends K> getInterfaces(K var1);

      @Nullable
      abstract K getSuperclass(K var1);

      // $FF: synthetic method
      TypeCollector(Object var1) {
         this();
      }

      private static class TypeCollector$ForwardingTypeCollector<K> extends TypeToken.TypeCollector<K> {
         private final TypeToken.TypeCollector<K> delegate;

         TypeCollector$ForwardingTypeCollector(TypeToken.TypeCollector<K> var1) {
            super(null);
            this.delegate = var1;
         }

         Class<?> getRawType(K var1) {
            return this.delegate.getRawType(var1);
         }

         Iterable<? extends K> getInterfaces(K var1) {
            return this.delegate.getInterfaces(var1);
         }

         K getSuperclass(K var1) {
            return this.delegate.getSuperclass(var1);
         }
      }
   }

   private static final class SimpleTypeToken<T> extends TypeToken<T> {
      private static final long serialVersionUID = 0L;

      SimpleTypeToken(Type var1) {
         super(var1, null);
      }
   }

   private static enum TypeFilter implements Predicate<TypeToken<?>> {
      IGNORE_TYPE_VARIABLE_OR_WILDCARD {
         public boolean apply(TypeToken<?> var1) {
            return !(var1.runtimeType instanceof TypeVariable) && !(var1.runtimeType instanceof WildcardType);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public boolean apply(Object var1) {
            return this.apply((TypeToken)var1);
         }
      },
      INTERFACE_ONLY {
         public boolean apply(TypeToken<?> var1) {
            return var1.getRawType().isInterface();
         }

         // $FF: synthetic method
         // $FF: bridge method
         public boolean apply(Object var1) {
            return this.apply((TypeToken)var1);
         }
      };

      private TypeFilter() {
      }

      // $FF: synthetic method
      TypeFilter(Object var3) {
         this();
      }
   }

   private final class ClassSet extends TypeToken<T>.TypeSet {
      private transient ImmutableSet<TypeToken<? super T>> classes;
      private static final long serialVersionUID = 0L;

      private ClassSet() {
         super();
      }

      protected Set<TypeToken<? super T>> delegate() {
         ImmutableSet var1 = this.classes;
         if(var1 == null) {
            ImmutableList var2 = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes((Object)TypeToken.this);
            return this.classes = FluentIterable.from((Iterable)var2).filter((Predicate)TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
         } else {
            return var1;
         }
      }

      public TypeToken<T>.TypeSet classes() {
         return this;
      }

      public Set<Class<? super T>> rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable)TypeToken.this.getImmediateRawTypes());
         return ImmutableSet.copyOf((Collection)var1);
      }

      public TypeToken<T>.TypeSet interfaces() {
         throw new UnsupportedOperationException("classes().interfaces() not supported.");
      }

      private Object readResolve() {
         return TypeToken.this.getTypes().classes();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Collection delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      ClassSet(Object var2) {
         this();
      }
   }

   private final class InterfaceSet extends TypeToken<T>.TypeSet {
      private final transient TypeToken<T>.TypeSet allTypes;
      private transient ImmutableSet<TypeToken<? super T>> interfaces;
      private static final long serialVersionUID = 0L;

      InterfaceSet(TypeToken<T>.TypeSet var1) {
         super();
         this.allTypes = var2;
      }

      protected Set<TypeToken<? super T>> delegate() {
         ImmutableSet var1 = this.interfaces;
         return var1 == null?(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter((Predicate)TypeToken.TypeFilter.INTERFACE_ONLY).toSet()):var1;
      }

      public TypeToken<T>.TypeSet interfaces() {
         return this;
      }

      public Set<Class<? super T>> rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable)TypeToken.this.getImmediateRawTypes());
         return FluentIterable.from((Iterable)var1).filter(new Predicate() {
            public boolean apply(Class<?> var1) {
               return var1.isInterface();
            }

            // $FF: synthetic method
            // $FF: bridge method
            public boolean apply(Object var1) {
               return this.apply((Class)var1);
            }
         }).toSet();
      }

      public TypeToken<T>.TypeSet classes() {
         throw new UnsupportedOperationException("interfaces().classes() not supported.");
      }

      private Object readResolve() {
         return TypeToken.this.getTypes().interfaces();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Collection delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
      private transient ImmutableSet<TypeToken<? super T>> types;
      private static final long serialVersionUID = 0L;

      TypeSet() {
      }

      public TypeToken<T>.TypeSet interfaces() {
         return TypeToken.this.new InterfaceSet(this);
      }

      public TypeToken<T>.TypeSet classes() {
         return TypeToken.this.new ClassSet(null);
      }

      protected Set<TypeToken<? super T>> delegate() {
         ImmutableSet var1 = this.types;
         if(var1 == null) {
            ImmutableList var2 = TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes((Object)TypeToken.this);
            return this.types = FluentIterable.from((Iterable)var2).filter((Predicate)TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet();
         } else {
            return var1;
         }
      }

      public Set<Class<? super T>> rawTypes() {
         ImmutableList var1 = TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable)TypeToken.this.getImmediateRawTypes());
         return ImmutableSet.copyOf((Collection)var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Collection delegate() {
         return this.delegate();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }
}
