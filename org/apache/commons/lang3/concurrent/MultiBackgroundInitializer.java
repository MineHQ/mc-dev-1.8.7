package org.apache.commons.lang3.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;

public class MultiBackgroundInitializer extends BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults> {
   private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap();

   public MultiBackgroundInitializer() {
   }

   public MultiBackgroundInitializer(ExecutorService var1) {
      super(var1);
   }

   public void addInitializer(String var1, BackgroundInitializer<?> var2) {
      if(var1 == null) {
         throw new IllegalArgumentException("Name of child initializer must not be null!");
      } else if(var2 == null) {
         throw new IllegalArgumentException("Child initializer must not be null!");
      } else {
         synchronized(this) {
            if(this.isStarted()) {
               throw new IllegalStateException("addInitializer() must not be called after start()!");
            } else {
               this.childInitializers.put(var1, var2);
            }
         }
      }
   }

   protected int getTaskCount() {
      int var1 = 1;

      BackgroundInitializer var3;
      for(Iterator var2 = this.childInitializers.values().iterator(); var2.hasNext(); var1 += var3.getTaskCount()) {
         var3 = (BackgroundInitializer)var2.next();
      }

      return var1;
   }

   protected MultiBackgroundInitializer.MultiBackgroundInitializerResults initialize() throws Exception {
      HashMap var1;
      synchronized(this) {
         var1 = new HashMap(this.childInitializers);
      }

      ExecutorService var2 = this.getActiveExecutor();

      BackgroundInitializer var4;
      for(Iterator var3 = var1.values().iterator(); var3.hasNext(); var4.start()) {
         var4 = (BackgroundInitializer)var3.next();
         if(var4.getExternalExecutor() == null) {
            var4.setExternalExecutor(var2);
         }
      }

      HashMap var10 = new HashMap();
      HashMap var11 = new HashMap();
      Iterator var5 = var1.entrySet().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();

         try {
            var10.put(var6.getKey(), ((BackgroundInitializer)var6.getValue()).get());
         } catch (ConcurrentException var8) {
            var11.put(var6.getKey(), var8);
         }
      }

      return new MultiBackgroundInitializer.MultiBackgroundInitializerResults(var1, var10, var11);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object initialize() throws Exception {
      return this.initialize();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   public static class MultiBackgroundInitializerResults {
      private final Map<String, BackgroundInitializer<?>> initializers;
      private final Map<String, Object> resultObjects;
      private final Map<String, ConcurrentException> exceptions;

      private MultiBackgroundInitializerResults(Map<String, BackgroundInitializer<?>> var1, Map<String, Object> var2, Map<String, ConcurrentException> var3) {
         this.initializers = var1;
         this.resultObjects = var2;
         this.exceptions = var3;
      }

      public BackgroundInitializer<?> getInitializer(String var1) {
         return this.checkName(var1);
      }

      public Object getResultObject(String var1) {
         this.checkName(var1);
         return this.resultObjects.get(var1);
      }

      public boolean isException(String var1) {
         this.checkName(var1);
         return this.exceptions.containsKey(var1);
      }

      public ConcurrentException getException(String var1) {
         this.checkName(var1);
         return (ConcurrentException)this.exceptions.get(var1);
      }

      public Set<String> initializerNames() {
         return Collections.unmodifiableSet(this.initializers.keySet());
      }

      public boolean isSuccessful() {
         return this.exceptions.isEmpty();
      }

      private BackgroundInitializer<?> checkName(String var1) {
         BackgroundInitializer var2 = (BackgroundInitializer)this.initializers.get(var1);
         if(var2 == null) {
            throw new NoSuchElementException("No child initializer with name " + var1);
         } else {
            return var2;
         }
      }

      // $FF: synthetic method
      MultiBackgroundInitializerResults(Map var1, Map var2, Map var3, MultiBackgroundInitializer.SyntheticClass_1 var4) {
         this(var1, var2, var3);
      }
   }
}
