package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Constraint;
import com.google.common.collect.Constraints;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapConstraint;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.SortedSetMultimap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class MapConstraints {
   private MapConstraints() {
   }

   public static MapConstraint<Object, Object> notNull() {
      return MapConstraints.NotNullMapConstraint.INSTANCE;
   }

   public static <K, V> Map<K, V> constrainedMap(Map<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedMap(var0, var1);
   }

   public static <K, V> Multimap<K, V> constrainedMultimap(Multimap<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedMultimap(var0, var1);
   }

   public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedListMultimap(var0, var1);
   }

   public static <K, V> SetMultimap<K, V> constrainedSetMultimap(SetMultimap<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedSetMultimap(var0, var1);
   }

   public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(SortedSetMultimap<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedSortedSetMultimap(var0, var1);
   }

   private static <K, V> Entry<K, V> constrainedEntry(final Entry<K, V> var0, final MapConstraint<? super K, ? super V> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new ForwardingMapEntry() {
         protected Entry<K, V> delegate() {
            return var0;
         }

         public V setValue(V var1x) {
            var1.checkKeyValue(this.getKey(), var1x);
            return var0.setValue(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static <K, V> Entry<K, Collection<V>> constrainedAsMapEntry(final Entry<K, Collection<V>> var0, final MapConstraint<? super K, ? super V> var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new ForwardingMapEntry() {
         protected Entry<K, Collection<V>> delegate() {
            return var0;
         }

         public Collection<V> getValue() {
            return Constraints.constrainedTypePreservingCollection((Collection)var0.getValue(), new Constraint() {
               public V checkElement(V var1x) {
                  var1.checkKeyValue(getKey(), var1x);
                  return var1x;
               }
            });
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object getValue() {
            return this.getValue();
         }

         // $FF: synthetic method
         // $FF: bridge method
         protected Object delegate() {
            return this.delegate();
         }
      };
   }

   private static <K, V> Set<Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Entry<K, Collection<V>>> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedAsMapEntries(var0, var1);
   }

   private static <K, V> Collection<Entry<K, V>> constrainedEntries(Collection<Entry<K, V>> var0, MapConstraint<? super K, ? super V> var1) {
      return (Collection)(var0 instanceof Set?constrainedEntrySet((Set)var0, var1):new MapConstraints.ConstrainedEntries(var0, var1));
   }

   private static <K, V> Set<Entry<K, V>> constrainedEntrySet(Set<Entry<K, V>> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedEntrySet(var0, var1);
   }

   public static <K, V> BiMap<K, V> constrainedBiMap(BiMap<K, V> var0, MapConstraint<? super K, ? super V> var1) {
      return new MapConstraints.ConstrainedBiMap(var0, (BiMap)null, var1);
   }

   private static <K, V> Collection<V> checkValues(K var0, Iterable<? extends V> var1, MapConstraint<? super K, ? super V> var2) {
      ArrayList var3 = Lists.newArrayList(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var2.checkKeyValue(var0, var5);
      }

      return var3;
   }

   private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> var0, MapConstraint<? super K, ? super V> var1) {
      LinkedHashMap var2 = new LinkedHashMap(var0);
      Iterator var3 = var2.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         var1.checkKeyValue(var4.getKey(), var4.getValue());
      }

      return var2;
   }

   private static class ConstrainedSortedSetMultimap<K, V> extends MapConstraints.ConstrainedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
      ConstrainedSortedSetMultimap(SortedSetMultimap<K, V> var1, MapConstraint<? super K, ? super V> var2) {
         super(var1, var2);
      }

      public SortedSet<V> get(K var1) {
         return (SortedSet)super.get(var1);
      }

      public SortedSet<V> removeAll(Object var1) {
         return (SortedSet)super.removeAll(var1);
      }

      public SortedSet<V> replaceValues(K var1, Iterable<? extends V> var2) {
         return (SortedSet)super.replaceValues(var1, var2);
      }

      public Comparator<? super V> valueComparator() {
         return ((SortedSetMultimap)this.delegate()).valueComparator();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set removeAll(Object var1) {
         return this.removeAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Set get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }
   }

   private static class ConstrainedSetMultimap<K, V> extends MapConstraints.ConstrainedMultimap<K, V> implements SetMultimap<K, V> {
      ConstrainedSetMultimap(SetMultimap<K, V> var1, MapConstraint<? super K, ? super V> var2) {
         super(var1, var2);
      }

      public Set<V> get(K var1) {
         return (Set)super.get(var1);
      }

      public Set<Entry<K, V>> entries() {
         return (Set)super.entries();
      }

      public Set<V> removeAll(Object var1) {
         return (Set)super.removeAll(var1);
      }

      public Set<V> replaceValues(K var1, Iterable<? extends V> var2) {
         return (Set)super.replaceValues(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection entries() {
         return this.entries();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }
   }

   private static class ConstrainedListMultimap<K, V> extends MapConstraints.ConstrainedMultimap<K, V> implements ListMultimap<K, V> {
      ConstrainedListMultimap(ListMultimap<K, V> var1, MapConstraint<? super K, ? super V> var2) {
         super(var1, var2);
      }

      public List<V> get(K var1) {
         return (List)super.get(var1);
      }

      public List<V> removeAll(Object var1) {
         return (List)super.removeAll(var1);
      }

      public List<V> replaceValues(K var1, Iterable<? extends V> var2) {
         return (List)super.replaceValues(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }
   }

   static class ConstrainedAsMapEntries<K, V> extends ForwardingSet<Entry<K, Collection<V>>> {
      private final MapConstraint<? super K, ? super V> constraint;
      private final Set<Entry<K, Collection<V>>> entries;

      ConstrainedAsMapEntries(Set<Entry<K, Collection<V>>> var1, MapConstraint<? super K, ? super V> var2) {
         this.entries = var1;
         this.constraint = var2;
      }

      protected Set<Entry<K, Collection<V>>> delegate() {
         return this.entries;
      }

      public Iterator<Entry<K, Collection<V>>> iterator() {
         final Iterator var1 = this.entries.iterator();
         return new ForwardingIterator() {
            public Entry<K, Collection<V>> next() {
               return MapConstraints.constrainedAsMapEntry((Entry)var1.next(), ConstrainedAsMapEntries.this.constraint);
            }

            protected Iterator<Entry<K, Collection<V>>> delegate() {
               return var1;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object next() {
               return this.next();
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object delegate() {
               return this.delegate();
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

      public boolean equals(@Nullable Object var1) {
         return this.standardEquals(var1);
      }

      public int hashCode() {
         return this.standardHashCode();
      }

      public boolean remove(Object var1) {
         return Maps.removeEntryImpl(this.delegate(), var1);
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
   }

   static class ConstrainedEntrySet<K, V> extends MapConstraints.ConstrainedEntries<K, V> implements Set<Entry<K, V>> {
      ConstrainedEntrySet(Set<Entry<K, V>> var1, MapConstraint<? super K, ? super V> var2) {
         super(var1, var2);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   private static class ConstrainedEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
      final MapConstraint<? super K, ? super V> constraint;
      final Collection<Entry<K, V>> entries;

      ConstrainedEntries(Collection<Entry<K, V>> var1, MapConstraint<? super K, ? super V> var2) {
         this.entries = var1;
         this.constraint = var2;
      }

      protected Collection<Entry<K, V>> delegate() {
         return this.entries;
      }

      public Iterator<Entry<K, V>> iterator() {
         final Iterator var1 = this.entries.iterator();
         return new ForwardingIterator() {
            public Entry<K, V> next() {
               return MapConstraints.constrainedEntry((Entry)var1.next(), ConstrainedEntries.this.constraint);
            }

            protected Iterator<Entry<K, V>> delegate() {
               return var1;
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object next() {
               return this.next();
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object delegate() {
               return this.delegate();
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

      public boolean remove(Object var1) {
         return Maps.removeEntryImpl(this.delegate(), var1);
      }

      public boolean removeAll(Collection<?> var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection<?> var1) {
         return this.standardRetainAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class ConstrainedAsMapValues<K, V> extends ForwardingCollection<Collection<V>> {
      final Collection<Collection<V>> delegate;
      final Set<Entry<K, Collection<V>>> entrySet;

      ConstrainedAsMapValues(Collection<Collection<V>> var1, Set<Entry<K, Collection<V>>> var2) {
         this.delegate = var1;
         this.entrySet = var2;
      }

      protected Collection<Collection<V>> delegate() {
         return this.delegate;
      }

      public Iterator<Collection<V>> iterator() {
         final Iterator var1 = this.entrySet.iterator();
         return new Iterator() {
            public boolean hasNext() {
               return var1.hasNext();
            }

            public Collection<V> next() {
               return (Collection)((Entry)var1.next()).getValue();
            }

            public void remove() {
               var1.remove();
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
         return this.standardContains(var1);
      }

      public boolean containsAll(Collection<?> var1) {
         return this.standardContainsAll(var1);
      }

      public boolean remove(Object var1) {
         return this.standardRemove(var1);
      }

      public boolean removeAll(Collection<?> var1) {
         return this.standardRemoveAll(var1);
      }

      public boolean retainAll(Collection<?> var1) {
         return this.standardRetainAll(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class ConstrainedMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
      final MapConstraint<? super K, ? super V> constraint;
      final Multimap<K, V> delegate;
      transient Collection<Entry<K, V>> entries;
      transient Map<K, Collection<V>> asMap;

      public ConstrainedMultimap(Multimap<K, V> var1, MapConstraint<? super K, ? super V> var2) {
         this.delegate = (Multimap)Preconditions.checkNotNull(var1);
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var2);
      }

      protected Multimap<K, V> delegate() {
         return this.delegate;
      }

      public Map<K, Collection<V>> asMap() {
         Object var1 = this.asMap;
         if(var1 == null) {
            final Map var2 = this.delegate.asMap();
            this.asMap = (Map)(var1 = new ForwardingMap() {
               Set<Entry<K, Collection<V>>> entrySet;
               Collection<Collection<V>> values;

               protected Map<K, Collection<V>> delegate() {
                  return var2;
               }

               public Set<Entry<K, Collection<V>>> entrySet() {
                  Set var1 = this.entrySet;
                  if(var1 == null) {
                     this.entrySet = var1 = MapConstraints.constrainedAsMapEntries(var2.entrySet(), ConstrainedMultimap.this.constraint);
                  }

                  return var1;
               }

               public Collection<V> get(Object var1) {
                  try {
                     Collection var2x = ConstrainedMultimap.this.get(var1);
                     return var2x.isEmpty()?null:var2x;
                  } catch (ClassCastException var3) {
                     return null;
                  }
               }

               public Collection<Collection<V>> values() {
                  Object var1 = this.values;
                  if(var1 == null) {
                     this.values = (Collection)(var1 = new MapConstraints.ConstrainedAsMapValues(this.delegate().values(), this.entrySet()));
                  }

                  return (Collection)var1;
               }

               public boolean containsValue(Object var1) {
                  return this.values().contains(var1);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public Object get(Object var1) {
                  return this.get(var1);
               }

               // $FF: synthetic method
               // $FF: bridge method
               protected Object delegate() {
                  return this.delegate();
               }
            });
         }

         return (Map)var1;
      }

      public Collection<Entry<K, V>> entries() {
         Collection var1 = this.entries;
         if(var1 == null) {
            this.entries = var1 = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
         }

         return var1;
      }

      public Collection<V> get(final K var1) {
         return Constraints.constrainedTypePreservingCollection(this.delegate.get(var1), new Constraint() {
            public V checkElement(V var1x) {
               ConstrainedMultimap.this.constraint.checkKeyValue(var1, var1x);
               return var1x;
            }
         });
      }

      public boolean put(K var1, V var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate.put(var1, var2);
      }

      public boolean putAll(K var1, Iterable<? extends V> var2) {
         return this.delegate.putAll(var1, MapConstraints.checkValues(var1, var2, this.constraint));
      }

      public boolean putAll(Multimap<? extends K, ? extends V> var1) {
         boolean var2 = false;

         Entry var4;
         for(Iterator var3 = var1.entries().iterator(); var3.hasNext(); var2 |= this.put(var4.getKey(), var4.getValue())) {
            var4 = (Entry)var3.next();
         }

         return var2;
      }

      public Collection<V> replaceValues(K var1, Iterable<? extends V> var2) {
         return this.delegate.replaceValues(var1, MapConstraints.checkValues(var1, var2, this.constraint));
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static class InverseConstraint<K, V> implements MapConstraint<K, V> {
      final MapConstraint<? super V, ? super K> constraint;

      public InverseConstraint(MapConstraint<? super V, ? super K> var1) {
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var1);
      }

      public void checkKeyValue(K var1, V var2) {
         this.constraint.checkKeyValue(var2, var1);
      }
   }

   private static class ConstrainedBiMap<K, V> extends MapConstraints.ConstrainedMap<K, V> implements BiMap<K, V> {
      volatile BiMap<V, K> inverse;

      ConstrainedBiMap(BiMap<K, V> var1, @Nullable BiMap<V, K> var2, MapConstraint<? super K, ? super V> var3) {
         super(var1, var3);
         this.inverse = var2;
      }

      protected BiMap<K, V> delegate() {
         return (BiMap)super.delegate();
      }

      public V forcePut(K var1, V var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate().forcePut(var1, var2);
      }

      public BiMap<V, K> inverse() {
         if(this.inverse == null) {
            this.inverse = new MapConstraints.ConstrainedBiMap(this.delegate().inverse(), this, new MapConstraints.InverseConstraint(this.constraint));
         }

         return this.inverse;
      }

      public Set<V> values() {
         return this.delegate().values();
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Map delegate() {
         return this.delegate();
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
   }

   static class ConstrainedMap<K, V> extends ForwardingMap<K, V> {
      private final Map<K, V> delegate;
      final MapConstraint<? super K, ? super V> constraint;
      private transient Set<Entry<K, V>> entrySet;

      ConstrainedMap(Map<K, V> var1, MapConstraint<? super K, ? super V> var2) {
         this.delegate = (Map)Preconditions.checkNotNull(var1);
         this.constraint = (MapConstraint)Preconditions.checkNotNull(var2);
      }

      protected Map<K, V> delegate() {
         return this.delegate;
      }

      public Set<Entry<K, V>> entrySet() {
         Set var1 = this.entrySet;
         if(var1 == null) {
            this.entrySet = var1 = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
         }

         return var1;
      }

      public V put(K var1, V var2) {
         this.constraint.checkKeyValue(var1, var2);
         return this.delegate.put(var1, var2);
      }

      public void putAll(Map<? extends K, ? extends V> var1) {
         this.delegate.putAll(MapConstraints.checkMap(var1, this.constraint));
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }

   private static enum NotNullMapConstraint implements MapConstraint<Object, Object> {
      INSTANCE;

      private NotNullMapConstraint() {
      }

      public void checkKeyValue(Object var1, Object var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkNotNull(var2);
      }

      public String toString() {
         return "Not null";
      }
   }
}
