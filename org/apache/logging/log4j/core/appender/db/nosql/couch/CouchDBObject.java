package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class CouchDBObject implements NoSQLObject<Map<String, Object>> {
   private final Map<String, Object> map = new HashMap();

   public CouchDBObject() {
   }

   public void set(String var1, Object var2) {
      this.map.put(var1, var2);
   }

   public void set(String var1, NoSQLObject<Map<String, Object>> var2) {
      this.map.put(var1, var2.unwrap());
   }

   public void set(String var1, Object[] var2) {
      this.map.put(var1, Arrays.asList(var2));
   }

   public void set(String var1, NoSQLObject<Map<String, Object>>[] var2) {
      ArrayList var3 = new ArrayList();
      NoSQLObject[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         NoSQLObject var7 = var4[var6];
         var3.add(var7.unwrap());
      }

      this.map.put(var1, var3);
   }

   public Map<String, Object> unwrap() {
      return this.map;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object unwrap() {
      return this.unwrap();
   }
}
