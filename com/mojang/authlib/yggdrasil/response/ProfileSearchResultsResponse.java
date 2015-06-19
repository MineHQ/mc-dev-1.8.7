package com.mojang.authlib.yggdrasil.response;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.response.Response;
import java.lang.reflect.Type;

public class ProfileSearchResultsResponse extends Response {
   private GameProfile[] profiles;

   public ProfileSearchResultsResponse() {
   }

   public GameProfile[] getProfiles() {
      return this.profiles;
   }

   public static class Serializer implements JsonDeserializer<ProfileSearchResultsResponse> {
      public Serializer() {
      }

      public ProfileSearchResultsResponse deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         ProfileSearchResultsResponse var4 = new ProfileSearchResultsResponse();
         if(var1 instanceof JsonObject) {
            JsonObject var5 = (JsonObject)var1;
            if(var5.has("error")) {
               var4.setError(var5.getAsJsonPrimitive("error").getAsString());
            }

            if(var5.has("errorMessage")) {
               var4.setError(var5.getAsJsonPrimitive("errorMessage").getAsString());
            }

            if(var5.has("cause")) {
               var4.setError(var5.getAsJsonPrimitive("cause").getAsString());
            }
         } else {
            var4.profiles = (GameProfile[])var3.deserialize(var1, GameProfile[].class);
         }

         return var4;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }
   }
}
