package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
   private transient Map<K, V> delegate;
   transient AbstractBiMap<V, K> inverse;
   private transient Set<K> keySet;
   private transient Set<V> valueSet;
   private transient Set<Entry<K, V>> entrySet;
   @GwtIncompatible("Not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   AbstractBiMap(Map<K, V> var1, Map<V, K> var2) {
      this.setDelegates(var1, var2);
   }

   private AbstractBiMap(Map<K, V> var1, AbstractBiMap<V, K> var2) {
      this.delegate = var1;
      this.inverse = var2;
   }

   protected Map<K, V> delegate() {
      return this.delegate;
   }

   K checkKey(@Nullable K var1) {
      return var1;
   }

   V checkValue(@Nullable V var1) {
      return var1;
   }

   void setDelegates(Map<K, V> var1, Map<V, K> var2) {
      Preconditions.checkState(this.delegate == null);
      Preconditions.checkState(this.inverse == null);
      Preconditions.checkArgument(var1.isEmpty());
      Preconditions.checkArgument(var2.isEmpty());
      Preconditions.checkArgument(var1 != var2);
      this.delegate = var1;
      this.inverse = new AbstractBiMap.Inverse(var2, this);
   }

   void setInverse(AbstractBiMap<V, K> var1) {
      this.inverse = var1;
   }

   public boolean containsValue(@Nullable Object var1) {
      return this.inverse.containsKey(var1);
   }

   public V put(@Nullable K var1, @Nullable V var2) {
      return this.putInBothMaps(var1, var2, false);
   }

   public V forcePut(@Nullable K var1, @Nullable V var2) {
      return this.putInBothMaps(var1, var2, true);
   }

   private V putInBothMaps(@Nullable K var1, @Nullable V var2, boolean var3) {
      this.checkKey(var1);
      this.checkValue(var2);
      boolean var4 = this.containsKey(var1);
      if(var4 && Objects.equal(var2, this.get(var1))) {
         return var2;
      } else {
         if(var3) {
            this.inverse().remove(var2);
         } else {
            Preconditions.checkArgument(!this.containsValue(var2), "value already present: %s", new Object[]{var2});
         }

         Object var5 = this.delegate.put(var1, var2);
         this.updateInverseMap(var1, var4, var5, var2);
         return var5;
      }
   }

   private void updateInverseMap(K var1, boolean var2, V var3, V var4) {
      if(var2) {
         this.removeFromInverseMap(var3);
      }

      this.inverse.delegate.put(var4, var1);
   }

   public V remove(@Nullable Object var1) {
      return this.containsKey(var1)?this.removeFromBothMaps(var1):null;
   }

   private V removeFromBothMaps(Object var1) {
      Object var2 = this.delegate.remove(var1);
      this.removeFromInverseMap(var2);
      return var2;
   }

   private void removeFromInverseMap(V var1) {
      this.inverse.delegate.remove(var1);
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put(var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      this.delegate.clear();
      this.inverse.delegate.clear();
   }

   public BiMap<V, K> inverse() {
      return this.inverse;
   }

   public Set<K> keySet() {
      Set var1 = this.keySet;
      return var1 == null?(this.keySet = new AbstractBiMap.KeySet()):var1;
   }

   public Set<V> values() {
      Set var1 = this.valueSet;
      return var1 == null?(this.valueSet = new AbstractBiMap.ValueSet()):var1;
   }

   public Set<Entry<K, V>> entrySet() {
      Set var1 = this.entrySet;
      return var1 == null?(this.entrySet = new AbstractBiMap.EntrySet()):var1;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Collection values() {
      return this.values();
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected Object delegate() {
      return this.delegate();
   }

   // $FF: synthetic method
   AbstractBiMap(Map var1, AbstractBiMap var2, AbstractBiMap.SyntheticClass_1 var3) {
      this(var1, var2);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class Inverse<K, V> extends AbstractBiMap<K, V> {
      @GwtIncompatible("Not needed in emulated source.")
      private static final long serialVersionUID = 0L;

      private Inverse(Map<K, V> var1, AbstractBiMap<V, K> var2) {
         super(var1, var2, (AbstractBiMap.SyntheticClass_1)null);
      }

      K checkKey(K var1) {
         return this.inverse.checkValue(var1);
      }

      V checkValue(V var1) {
         return this.inverse.checkKey(var1);
      }

      @GwtIncompatible("java.io.ObjectOuputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
         var1.writeObject(this.inverse());
      }

      @GwtIncompatible("java.io.ObjectInputStream")
      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.setInverse((AbstractBiMap)var1.readObject());
      }

      @GwtIncompatible("Not needed in the emulated source.")
      Object readResolve() {
         return this.inverse().inverse();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection values() {
         return super.values();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return super.delegate();
      }

      // $FF: synthetic method
      Inverse(Map var1, AbstractBiMap var2, AbstractBiMap.SyntheticClass_1 var3) {
         this(var1, var2);
      }
   }

   private class EntrySet extends ForwardingSet<Entry<K, V>> {
      final Set<Entry<K, V>> esDelegate;

      private EntrySet() {
         this.esDelegate = AbstractBiMap.this.delegate.entrySet();
      }

      protected Set<Entry<K, V>> delegate() {
         return this.esDelegate;
      }

      public void clear() {
         AbstractBiMap.this.clear();
      }

      public boolean remove(Object var1) {
         if(!this.esDelegate.contains(var1)) {
            return false;
         } else {
            Entry var2 = (Entry)var1;
            AbstractBiMap.this.inverse.delegate.remove(var2.getValue());
            this.esDelegate.remove(var2);
            return true;
         }
      }

      public Iterator<Entry<K, V>> iterator() {
         final Iterator var1 = this.esDelegate.iterator();
         return new Iterator() {
            Entry<K, V> entry;

            public boolean hasNext() {
               return var1.hasNext();
            }

            public Entry<K, V> next() {
               this.entry = (Entry)var1.next();
               final Entry var1x = this.entry;
               return new ForwardingMapEntry() {
                  protected Entry<K, V> delegate() {
                     return var1x;
                  }

                  public V setValue(V var1xx) {
                     Preconditions.checkState(EntrySet.this.contains(this), "entry no longer in map");
                     if(Objects.equal(var1xx, this.getValue())) {
                        return var1xx;
                     } else {
                        Preconditions.checkArgument(!AbstractBiMap.this.containsValue(var1xx), "value already present: %s", new Object[]{var1xx});
                        Object var2 = var1x.setValue(var1xx);
                        Preconditions.checkState(Objects.equal(var1xx, AbstractBiMap.this.get(this.getKey())), "entry no longer in map");
                        AbstractBiMap.this.updateInverseMap(this.getKey(), true, var2, var1xx);
                        return var2;
                     }
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  protected Object delegate() {
                     return this.delegate();
                  }
               };
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.entry != null);
               Object var1x = this.entry.getValue();
               var1.remove();
               AbstractBiMap.this.removeFromInverseMap(var1x);
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object next() {
               return this.next();
            }
         };
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.standardToArray(var1);
      }

      public boolean contains(Object var1) {
         return Maps.containsEntryImpl(this.delegate(), var1);
      }

      public boolean containsAll(Collection<?> var1) {
         return this.standardContainsAll(var1);
      }

      public boolean removeAll(Collection<?> var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection<?> var1) {
         return this.standardRetainAll(var1);
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

      // $FF: synthetic method
      EntrySet(AbstractBiMap.SyntheticClass_1 var2) {
         this();
      }
   }

   private class ValueSet extends ForwardingSet<V> {
      final Set<V> valuesDelegate;

      private ValueSet() {
         this.valuesDelegate = AbstractBiMap.this.inverse.keySet();
      }

      protected Set<V> delegate() {
         return this.valuesDelegate;
      }

      public Iterator<V> iterator() {
         return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] var1) {
         return this.standardToArray(var1);
      }

      public String toString() {
         return this.standardToString();
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

      // $FF: synthetic method
      ValueSet(AbstractBiMap.SyntheticClass_1 var2) {
         this();
      }
   }

   private class KeySet extends ForwardingSet<K> {
      private KeySet() {
      }

      protected Set<K> delegate() {
         return AbstractBiMap.this.delegate.keySet();
      }

      public void clear() {
         AbstractBiMap.this.clear();
      }

      public boolean remove(Object var1) {
         if(!this.contains(var1)) {
            return false;
         } else {
            AbstractBiMap.this.removeFromBothMaps(var1);
            return true;
         }
      }

      public boolean removeAll(Collection<?> var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection<?> var1) {
         return this.standardRetainAll(var1);
      }

      public Iterator<K> iterator() {
         return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
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

      // $FF: synthetic method
      KeySet(AbstractBiMap.SyntheticClass_1 var2) {
         this();
      }
   }
}
