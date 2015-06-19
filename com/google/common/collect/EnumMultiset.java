package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Serialization;
import com.google.common.collect.WellBehavedMap;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Set;

@GwtCompatible(
   emulated = true
)
public final class EnumMultiset<E extends Enum<E>> extends AbstractMapBasedMultiset<E> {
   private transient Class<E> type;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> var0) {
      return new EnumMultiset(var0);
   }

   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> var0) {
      Iterator var1 = var0.iterator();
      Preconditions.checkArgument(var1.hasNext(), "EnumMultiset constructor passed empty Iterable");
      EnumMultiset var2 = new EnumMultiset(((Enum)var1.next()).getDeclaringClass());
      Iterables.addAll(var2, var0);
      return var2;
   }

   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> var0, Class<E> var1) {
      EnumMultiset var2 = create(var1);
      Iterables.addAll(var2, var0);
      return var2;
   }

   private EnumMultiset(Class<E> var1) {
      super(WellBehavedMap.wrap(new EnumMap(var1)));
      this.type = var1;
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.type);
      Serialization.writeMultiset(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      Class var2 = (Class)var1.readObject();
      this.type = var2;
      this.setBackingMap(WellBehavedMap.wrap(new EnumMap(this.type)));
      Serialization.populateMultiset(this, var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int remove(Object var1, int var2) {
      return super.remove(var1, var2);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int count(Object var1) {
      return super.count(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Iterator iterator() {
      return super.iterator();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int size() {
      return super.size();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void clear() {
      super.clear();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set entrySet() {
      return super.entrySet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String toString() {
      return super.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Set elementSet() {
      return super.elementSet();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean retainAll(Collection var1) {
      return super.retainAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean removeAll(Collection var1) {
      return super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean addAll(Collection var1) {
      return super.addAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean remove(Object var1) {
      return super.remove(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean contains(Object var1) {
      return super.contains(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
   }
}
