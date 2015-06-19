package com.google.gson;

import com.google.gson.JsonElement;

public final class JsonNull extends JsonElement {
   public static final JsonNull INSTANCE = new JsonNull();

   /** @deprecated */
   @Deprecated
   public JsonNull() {
   }

   JsonNull deepCopy() {
      return INSTANCE;
   }

   public int hashCode() {
      return JsonNull.class.hashCode();
   }

   public boolean equals(Object var1) {
      return this == var1 || var1 instanceof JsonNull;
   }

   // $FF: synthetic method
   // $FF: bridge method
   JsonElement deepCopy() {
      return this.deepCopy();
   }
}
