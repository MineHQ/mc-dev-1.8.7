package com.google.common.eventbus;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventSubscriber;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberFindingStrategy;
import com.google.common.eventbus.SynchronizedEventSubscriber;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

class AnnotatedSubscriberFinder implements SubscriberFindingStrategy {
   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader() {
      public ImmutableList<Method> load(Class<?> var1) throws Exception {
         return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object load(Object var1) throws Exception {
         return this.load((Class)var1);
      }
   });

   AnnotatedSubscriberFinder() {
   }

   public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object var1) {
      HashMultimap var2 = HashMultimap.create();
      Class var3 = var1.getClass();
      Iterator var4 = getAnnotatedMethods(var3).iterator();

      while(var4.hasNext()) {
         Method var5 = (Method)var4.next();
         Class[] var6 = var5.getParameterTypes();
         Class var7 = var6[0];
         EventSubscriber var8 = makeSubscriber(var1, var5);
         var2.put(var7, var8);
      }

      return var2;
   }

   private static ImmutableList<Method> getAnnotatedMethods(Class<?> var0) {
      try {
         return (ImmutableList)subscriberMethodsCache.getUnchecked(var0);
      } catch (UncheckedExecutionException var2) {
         throw Throwables.propagate(var2.getCause());
      }
   }

   private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> var0) {
      Set var1 = TypeToken.of(var0).getTypes().rawTypes();
      HashMap var2 = Maps.newHashMap();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         Method[] var5 = var4.getMethods();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Method var8 = var5[var7];
            if(var8.isAnnotationPresent(Subscribe.class)) {
               Class[] var9 = var8.getParameterTypes();
               if(var9.length != 1) {
                  throw new IllegalArgumentException("Method " + var8 + " has @Subscribe annotation, but requires " + var9.length + " arguments.  Event subscriber methods must require a single argument.");
               }

               AnnotatedSubscriberFinder.MethodIdentifier var10 = new AnnotatedSubscriberFinder.MethodIdentifier(var8);
               if(!var2.containsKey(var10)) {
                  var2.put(var10, var8);
               }
            }
         }
      }

      return ImmutableList.copyOf(var2.values());
   }

   private static EventSubscriber makeSubscriber(Object var0, Method var1) {
      Object var2;
      if(methodIsDeclaredThreadSafe(var1)) {
         var2 = new EventSubscriber(var0, var1);
      } else {
         var2 = new SynchronizedEventSubscriber(var0, var1);
      }

      return (EventSubscriber)var2;
   }

   private static boolean methodIsDeclaredThreadSafe(Method var0) {
      return var0.getAnnotation(AllowConcurrentEvents.class) != null;
   }

   private static final class MethodIdentifier {
      private final String name;
      private final List<Class<?>> parameterTypes;

      MethodIdentifier(Method var1) {
         this.name = var1.getName();
         this.parameterTypes = Arrays.asList(var1.getParameterTypes());
      }

      public int hashCode() {
         return Objects.hashCode(new Object[]{this.name, this.parameterTypes});
      }

      public boolean equals(@Nullable Object var1) {
         if(!(var1 instanceof AnnotatedSubscriberFinder.MethodIdentifier)) {
            return false;
         } else {
            AnnotatedSubscriberFinder.MethodIdentifier var2 = (AnnotatedSubscriberFinder.MethodIdentifier)var1;
            return this.name.equals(var2.name) && this.parameterTypes.equals(var2.parameterTypes);
         }
      }
   }
}
