package net.minecraft.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom {
   public static int a(Collection<? extends WeightedRandom.WeightedRandomChoice> var0) {
      int var1 = 0;

      WeightedRandom.WeightedRandomChoice var3;
      for(Iterator var2 = var0.iterator(); var2.hasNext(); var1 += var3.a) {
         var3 = (WeightedRandom.WeightedRandomChoice)var2.next();
      }

      return var1;
   }

   public static <T extends WeightedRandom.WeightedRandomChoice> T a(Random var0, Collection<T> var1, int var2) {
      if(var2 <= 0) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.nextInt(var2);
         return a(var1, var3);
      }
   }

   public static <T extends WeightedRandom.WeightedRandomChoice> T a(Collection<T> var0, int var1) {
      Iterator var2 = var0.iterator();

      WeightedRandom.WeightedRandomChoice var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (WeightedRandom.WeightedRandomChoice)var2.next();
         var1 -= var3.a;
      } while(var1 >= 0);

      return var3;
   }

   public static <T extends WeightedRandom.WeightedRandomChoice> T a(Random var0, Collection<T> var1) {
      return a(var0, var1, a(var1));
   }

   public static class WeightedRandomChoice {
      protected int a;

      public WeightedRandomChoice(int var1) {
         this.a = var1;
      }
   }
}
