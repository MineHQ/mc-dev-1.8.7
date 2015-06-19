package net.minecraft.server;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.IJsonStatistic;

public class AchievementSet extends ForwardingSet<String> implements IJsonStatistic {
   private final Set<String> a = Sets.newHashSet();

   public AchievementSet() {
   }

   public void a(JsonElement var1) {
      if(var1.isJsonArray()) {
         Iterator var2 = var1.getAsJsonArray().iterator();

         while(var2.hasNext()) {
            JsonElement var3 = (JsonElement)var2.next();
            this.add(var3.getAsString());
         }
      }

   }

   public JsonElement a() {
      JsonArray var1 = new JsonArray();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.add(new JsonPrimitive(var3));
      }

      return var1;
   }

   protected Set<String> delegate() {
      return this.a;
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
