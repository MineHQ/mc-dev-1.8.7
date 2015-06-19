package io.netty.handler.codec.serialization;

import io.netty.handler.codec.serialization.ClassResolver;
import java.util.Map;

class CachingClassResolver implements ClassResolver {
   private final Map<String, Class<?>> classCache;
   private final ClassResolver delegate;

   CachingClassResolver(ClassResolver var1, Map<String, Class<?>> var2) {
      this.delegate = var1;
      this.classCache = var2;
   }

   public Class<?> resolve(String var1) throws ClassNotFoundException {
      Class var2 = (Class)this.classCache.get(var1);
      if(var2 != null) {
         return var2;
      } else {
         var2 = this.delegate.resolve(var1);
         this.classCache.put(var1, var2);
         return var2;
      }
   }
}
