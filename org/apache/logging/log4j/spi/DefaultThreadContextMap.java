package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.spi.ThreadContextMap;

public class DefaultThreadContextMap implements ThreadContextMap {
   private final boolean useMap;
   private final ThreadLocal<Map<String, String>> localMap = new InheritableThreadLocal() {
      protected Map<String, String> childValue(Map<String, String> var1) {
         return var1 != null && DefaultThreadContextMap.this.useMap?Collections.unmodifiableMap(new HashMap(var1)):null;
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object childValue(Object var1) {
         return this.childValue((Map)var1);
      }
   };

   public DefaultThreadContextMap(boolean var1) {
      this.useMap = var1;
   }

   public void put(String var1, String var2) {
      if(this.useMap) {
         Map var3 = (Map)this.localMap.get();
         HashMap var4 = var3 == null?new HashMap():new HashMap(var3);
         var4.put(var1, var2);
         this.localMap.set(Collections.unmodifiableMap(var4));
      }
   }

   public String get(String var1) {
      Map var2 = (Map)this.localMap.get();
      return var2 == null?null:(String)var2.get(var1);
   }

   public void remove(String var1) {
      Map var2 = (Map)this.localMap.get();
      if(var2 != null) {
         HashMap var3 = new HashMap(var2);
         var3.remove(var1);
         this.localMap.set(Collections.unmodifiableMap(var3));
      }

   }

   public void clear() {
      this.localMap.remove();
   }

   public boolean containsKey(String var1) {
      Map var2 = (Map)this.localMap.get();
      return var2 != null && var2.containsKey(var1);
   }

   public Map<String, String> getCopy() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null?new HashMap():new HashMap(var1);
   }

   public Map<String, String> getImmutableMapOrNull() {
      return (Map)this.localMap.get();
   }

   public boolean isEmpty() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null || var1.size() == 0;
   }

   public String toString() {
      Map var1 = (Map)this.localMap.get();
      return var1 == null?"{}":var1.toString();
   }
}
