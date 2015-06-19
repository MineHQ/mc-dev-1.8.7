package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;

@Beta
@GwtCompatible
public abstract class Escaper {
   private final Function<String, String> asFunction = new Function() {
      public String apply(String var1) {
         return Escaper.this.escape(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object apply(Object var1) {
         return this.apply((String)var1);
      }
   };

   protected Escaper() {
   }

   public abstract String escape(String var1);

   public final Function<String, String> asFunction() {
      return this.asFunction;
   }
}
