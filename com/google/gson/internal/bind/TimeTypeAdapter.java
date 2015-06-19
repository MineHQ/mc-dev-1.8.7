package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2) {
         return var2.getRawType() == Time.class?new TimeTypeAdapter():null;
      }
   };
   private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

   public TimeTypeAdapter() {
   }

   public synchronized Time read(JsonReader var1) throws IOException {
      if(var1.peek() == JsonToken.NULL) {
         var1.nextNull();
         return null;
      } else {
         try {
            Date var2 = this.format.parse(var1.nextString());
            return new Time(var2.getTime());
         } catch (ParseException var3) {
            throw new JsonSyntaxException(var3);
         }
      }
   }

   public synchronized void write(JsonWriter var1, Time var2) throws IOException {
      var1.value(var2 == null?null:this.format.format(var2));
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object read(JsonReader var1) throws IOException {
      return this.read(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void write(JsonWriter var1, Object var2) throws IOException {
      this.write(var1, (Time)var2);
   }
}
