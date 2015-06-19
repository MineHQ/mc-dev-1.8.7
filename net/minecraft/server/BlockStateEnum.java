package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.BlockState;
import net.minecraft.server.INamable;

public class BlockStateEnum<T extends Enum<T> & INamable> extends BlockState<T> {
   private final ImmutableSet<T> a;
   private final Map<String, T> b = Maps.newHashMap();

   protected BlockStateEnum(String var1, Class<T> var2, Collection<T> var3) {
      super(var1, var2);
      this.a = ImmutableSet.copyOf(var3);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Enum var5 = (Enum)var4.next();
         String var6 = ((INamable)var5).getName();
         if(this.b.containsKey(var6)) {
            throw new IllegalArgumentException("Multiple values have the same name \'" + var6 + "\'");
         }

         this.b.put(var6, var5);
      }

   }

   public Collection<T> c() {
      return this.a;
   }

   public String a(T var1) {
      return ((INamable)var1).getName();
   }

   public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String var0, Class<T> var1) {
      return a(var0, var1, Predicates.alwaysTrue());
   }

   public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String var0, Class<T> var1, Predicate<T> var2) {
      return a(var0, var1, Collections2.filter(Lists.newArrayList(var1.getEnumConstants()), var2));
   }

   public static <T extends Enum<T> & INamable> BlockStateEnum<T> of(String var0, Class<T> var1, T... var2) {
      return a(var0, var1, (Collection)Lists.newArrayList((Object[])var2));
   }

   public static <T extends Enum<T> & INamable> BlockStateEnum<T> a(String var0, Class<T> var1, Collection<T> var2) {
      return new BlockStateEnum(var0, var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String a(Comparable var1) {
      return this.a((Enum)var1);
   }
}
