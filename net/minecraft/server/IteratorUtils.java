package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IteratorUtils {
   public static <T> Iterable<T[]> a(Class<T> var0, Iterable<? extends Iterable<? extends T>> var1) {
      return new IteratorUtils.ClassIterable(var0, (Iterable[])b(Iterable.class, var1));
   }

   public static <T> Iterable<List<T>> a(Iterable<? extends Iterable<? extends T>> var0) {
      return b(a(Object.class, var0));
   }

   private static <T> Iterable<List<T>> b(Iterable<Object[]> var0) {
      return Iterables.transform(var0, new IteratorUtils.ArrayToList());
   }

   private static <T> T[] b(Class<? super T> var0, Iterable<? extends T> var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var2.add(var4);
      }

      return (Object[])var2.toArray(b(var0, var2.size()));
   }

   private static <T> T[] b(Class<? super T> var0, int var1) {
      return (Object[])((Object[])Array.newInstance(var0, var1));
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   static class ClassIterable<T> implements Iterable<T[]> {
      private final Class<T> a;
      private final Iterable<? extends T>[] b;

      private ClassIterable(Class<T> var1, Iterable<? extends T>[] var2) {
         this.a = var1;
         this.b = var2;
      }

      public Iterator<T[]> iterator() {
         return (Iterator)(this.b.length <= 0?Collections.singletonList((Object[])IteratorUtils.b(this.a, 0)).iterator():new IteratorUtils.ClassIterable.ClassIterable$ClassIterator(this.a, this.b));
      }

      // $FF: synthetic method
      ClassIterable(Class var1, Iterable[] var2, IteratorUtils.SyntheticClass_1 var3) {
         this(var1, var2);
      }

      static class ClassIterable$ClassIterator<T> extends UnmodifiableIterator<T[]> {
         private int a;
         private final Iterable<? extends T>[] b;
         private final Iterator<? extends T>[] c;
         private final T[] d;

         private ClassIterable$ClassIterator(Class<T> var1, Iterable<? extends T>[] var2) {
            this.a = -2;
            this.b = var2;
            this.c = (Iterator[])IteratorUtils.b(Iterator.class, this.b.length);

            for(int var3 = 0; var3 < this.b.length; ++var3) {
               this.c[var3] = var2[var3].iterator();
            }

            this.d = IteratorUtils.b(var1, this.c.length);
         }

         private void b() {
            this.a = -1;
            Arrays.fill(this.c, (Object)null);
            Arrays.fill(this.d, (Object)null);
         }

         public boolean hasNext() {
            if(this.a == -2) {
               this.a = 0;
               Iterator[] var5 = this.c;
               int var2 = var5.length;

               for(int var3 = 0; var3 < var2; ++var3) {
                  Iterator var4 = var5[var3];
                  if(!var4.hasNext()) {
                     this.b();
                     break;
                  }
               }

               return true;
            } else {
               if(this.a >= this.c.length) {
                  for(this.a = this.c.length - 1; this.a >= 0; --this.a) {
                     Iterator var1 = this.c[this.a];
                     if(var1.hasNext()) {
                        break;
                     }

                     if(this.a == 0) {
                        this.b();
                        break;
                     }

                     var1 = this.b[this.a].iterator();
                     this.c[this.a] = var1;
                     if(!var1.hasNext()) {
                        this.b();
                        break;
                     }
                  }
               }

               return this.a >= 0;
            }
         }

         public T[] a() {
            if(!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               while(this.a < this.c.length) {
                  this.d[this.a] = this.c[this.a].next();
                  ++this.a;
               }

               return (Object[])this.d.clone();
            }
         }

         // $FF: synthetic method
         public Object next() {
            return this.a();
         }

         // $FF: synthetic method
         ClassIterable$ClassIterator(Class var1, Iterable[] var2, IteratorUtils.SyntheticClass_1 var3) {
            this(var1, var2);
         }
      }
   }

   static class ArrayToList<T> implements Function<Object[], List<T>> {
      private ArrayToList() {
      }

      public List<T> a(Object[] var1) {
         return Arrays.asList((Object[])var1);
      }

      // $FF: synthetic method
      public Object apply(Object var1) {
         return this.a((Object[])var1);
      }

      // $FF: synthetic method
      ArrayToList(IteratorUtils.SyntheticClass_1 var1) {
         this();
      }
   }
}
