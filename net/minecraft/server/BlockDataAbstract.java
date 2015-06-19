package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.server.Block;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;

public abstract class BlockDataAbstract implements IBlockData {
   private static final Joiner a = Joiner.on(',');
   private static final Function<Entry<IBlockState, Comparable>, String> b = new Function() {
      public String a(Entry<IBlockState, Comparable> var1) {
         if(var1 == null) {
            return "<NULL>";
         } else {
            IBlockState var2 = (IBlockState)var1.getKey();
            return var2.a() + "=" + var2.a((Comparable)var1.getValue());
         }
      }

      // $FF: synthetic method
      public Object apply(Object var1) {
         return this.a((Entry)var1);
      }
   };

   public BlockDataAbstract() {
   }

   public <T extends Comparable<T>> IBlockData a(IBlockState<T> var1) {
      return this.set(var1, (Comparable)a(var1.c(), this.get(var1)));
   }

   protected static <T> T a(Collection<T> var0, T var1) {
      Iterator var2 = var0.iterator();

      do {
         if(!var2.hasNext()) {
            return var2.next();
         }
      } while(!var2.next().equals(var1));

      if(var2.hasNext()) {
         return var2.next();
      } else {
         return var0.iterator().next();
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(Block.REGISTRY.c(this.getBlock()));
      if(!this.b().isEmpty()) {
         var1.append("[");
         a.appendTo(var1, Iterables.transform(this.b().entrySet(), b));
         var1.append("]");
      }

      return var1.toString();
   }
}
