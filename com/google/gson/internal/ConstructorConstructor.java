package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public final class ConstructorConstructor {
   private final Map<Type, InstanceCreator<?>> instanceCreators;

   public ConstructorConstructor(Map<Type, InstanceCreator<?>> var1) {
      this.instanceCreators = var1;
   }

   public <T> ObjectConstructor<T> get(TypeToken<T> var1) {
      final Type var2 = var1.getType();
      Class var3 = var1.getRawType();
      final InstanceCreator var4 = (InstanceCreator)this.instanceCreators.get(var2);
      if(var4 != null) {
         return new ObjectConstructor() {
            public T construct() {
               return var4.createInstance(var2);
            }
         };
      } else {
         final InstanceCreator var5 = (InstanceCreator)this.instanceCreators.get(var3);
         if(var5 != null) {
            return new ObjectConstructor() {
               public T construct() {
                  return var5.createInstance(var2);
               }
            };
         } else {
            ObjectConstructor var6 = this.newDefaultConstructor(var3);
            if(var6 != null) {
               return var6;
            } else {
               ObjectConstructor var7 = this.newDefaultImplementationConstructor(var2, var3);
               return var7 != null?var7:this.newUnsafeAllocator(var2, var3);
            }
         }
      }
   }

   private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> var1) {
      try {
         final Constructor var2 = var1.getDeclaredConstructor(new Class[0]);
         if(!var2.isAccessible()) {
            var2.setAccessible(true);
         }

         return new ObjectConstructor() {
            public T construct() {
               try {
                  Object var1 = null;
                  return var2.newInstance((Object[])var1);
               } catch (InstantiationException var2x) {
                  throw new RuntimeException("Failed to invoke " + var2 + " with no args", var2x);
               } catch (InvocationTargetException var3) {
                  throw new RuntimeException("Failed to invoke " + var2 + " with no args", var3.getTargetException());
               } catch (IllegalAccessException var4) {
                  throw new AssertionError(var4);
               }
            }
         };
      } catch (NoSuchMethodException var3) {
         return null;
      }
   }

   private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type var1, Class<? super T> var2) {
      return Collection.class.isAssignableFrom(var2)?(SortedSet.class.isAssignableFrom(var2)?new ObjectConstructor() {
         public T construct() {
            return new TreeSet();
         }
      }:(EnumSet.class.isAssignableFrom(var2)?new ObjectConstructor() {
         public T construct() {
            if(var1 instanceof ParameterizedType) {
               Type var1x = ((ParameterizedType)var1).getActualTypeArguments()[0];
               if(var1x instanceof Class) {
                  return EnumSet.noneOf((Class)var1x);
               } else {
                  throw new JsonIOException("Invalid EnumSet type: " + var1.toString());
               }
            } else {
               throw new JsonIOException("Invalid EnumSet type: " + var1.toString());
            }
         }
      }:(Set.class.isAssignableFrom(var2)?new ObjectConstructor() {
         public T construct() {
            return new LinkedHashSet();
         }
      }:(Queue.class.isAssignableFrom(var2)?new ObjectConstructor() {
         public T construct() {
            return new LinkedList();
         }
      }:new ObjectConstructor() {
         public T construct() {
            return new ArrayList();
         }
      })))):(Map.class.isAssignableFrom(var2)?(SortedMap.class.isAssignableFrom(var2)?new ObjectConstructor() {
         public T construct() {
            return new TreeMap();
         }
      }:(var1 instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)var1).getActualTypeArguments()[0]).getRawType())?new ObjectConstructor() {
         public T construct() {
            return new LinkedHashMap();
         }
      }:new ObjectConstructor() {
         public T construct() {
            return new LinkedTreeMap();
         }
      })):null);
   }

   private <T> ObjectConstructor<T> newUnsafeAllocator(final Type var1, final Class<? super T> var2) {
      return new ObjectConstructor() {
         private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

         public T construct() {
            try {
               Object var1x = this.unsafeAllocator.newInstance(var2);
               return var1x;
            } catch (Exception var2x) {
               throw new RuntimeException("Unable to invoke no-args constructor for " + var1 + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", var2x);
            }
         }
      };
   }

   public String toString() {
      return this.instanceCreators.toString();
   }
}
