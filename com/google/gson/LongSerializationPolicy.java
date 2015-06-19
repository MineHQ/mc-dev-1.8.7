package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public enum LongSerializationPolicy {
   DEFAULT {
      public JsonElement serialize(Long var1) {
         return new JsonPrimitive(var1);
      }
   },
   STRING {
      public JsonElement serialize(Long var1) {
         return new JsonPrimitive(String.valueOf(var1));
      }
   };

   private LongSerializationPolicy() {
   }

   public abstract JsonElement serialize(Long var1);

   // $FF: synthetic method
   LongSerializationPolicy(Object var3) {
      this();
   }
}
