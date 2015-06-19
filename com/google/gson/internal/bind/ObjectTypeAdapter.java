package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2) {
         return var2.getRawType() == Object.class?new ObjectTypeAdapter(var1, null):null;
      }
   };
   private final Gson gson;

   private ObjectTypeAdapter(Gson var1) {
      this.gson = var1;
   }

   public Object read(JsonReader var1) throws IOException {
      JsonToken var2 = var1.peek();
      switch(ObjectTypeAdapter.SyntheticClass_1.$SwitchMap$com$google$gson$stream$JsonToken[var2.ordinal()]) {
      case 1:
         ArrayList var3 = new ArrayList();
         var1.beginArray();

         while(var1.hasNext()) {
            var3.add(this.read(var1));
         }

         var1.endArray();
         return var3;
      case 2:
         LinkedTreeMap var4 = new LinkedTreeMap();
         var1.beginObject();

         while(var1.hasNext()) {
            var4.put(var1.nextName(), this.read(var1));
         }

         var1.endObject();
         return var4;
      case 3:
         return var1.nextString();
      case 4:
         return Double.valueOf(var1.nextDouble());
      case 5:
         return Boolean.valueOf(var1.nextBoolean());
      case 6:
         var1.nextNull();
         return null;
      default:
         throw new IllegalStateException();
      }
   }

   public void write(JsonWriter var1, Object var2) throws IOException {
      if(var2 == null) {
         var1.nullValue();
      } else {
         TypeAdapter var3 = this.gson.getAdapter(var2.getClass());
         if(var3 instanceof ObjectTypeAdapter) {
            var1.beginObject();
            var1.endObject();
         } else {
            var3.write(var1, var2);
         }
      }
   }

   // $FF: synthetic method
   ObjectTypeAdapter(Gson var1, Object var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$com$google$gson$stream$JsonToken = new int[JsonToken.values().length];

      static {
         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_ARRAY.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BEGIN_OBJECT.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.STRING.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NUMBER.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.BOOLEAN.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$google$gson$stream$JsonToken[JsonToken.NULL.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
