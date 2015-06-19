package net.minecraft.server;

import com.google.gson.JsonObject;

public class JsonListEntry<T> {
   private final T a;

   public JsonListEntry(T var1) {
      this.a = var1;
   }

   protected JsonListEntry(T var1, JsonObject var2) {
      this.a = var1;
   }

   public T getKey() {
      return this.a;
   }

   boolean hasExpired() {
      return false;
   }

   protected void a(JsonObject var1) {
   }
}
