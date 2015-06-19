package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.AbstractNavigableMap;
import com.google.common.collect.AbstractRangeSet;
import com.google.common.collect.BoundType;
import com.google.common.collect.Cut;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("uses NavigableMap")
public class TreeRangeSet<C extends Comparable<?>> extends AbstractRangeSet<C> {
   @VisibleForTesting
   final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
   private transient Set<Range<C>> asRanges;
   private transient RangeSet<C> complement;

   public static <C extends Comparable<?>> TreeRangeSet<C> create() {
      return new TreeRangeSet(new TreeMap());
   }

   public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> var0) {
      TreeRangeSet var1 = create();
      var1.addAll(var0);
      return var1;
   }

   private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> var1) {
      this.rangesByLowerBound = var1;
   }

   public Set<Range<C>> asRanges() {
      Set var1 = this.asRanges;
      return var1 == null?(this.asRanges = new TreeRangeSet.AsRanges()):var1;
   }

   @Nullable
   public Range<C> rangeContaining(C var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(Cut.belowValue(var1));
      return var2 != null && ((Range)var2.getValue()).contains(var1)?(Range)var2.getValue():null;
   }

   public boolean encloses(Range<C> var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(var1.lowerBound);
      return var2 != null && ((Range)var2.getValue()).encloses(var1);
   }

   @Nullable
   private Range<C> rangeEnclosing(Range<C> var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(var1.lowerBound);
      return var2 != null && ((Range)var2.getValue()).encloses(var1)?(Range)var2.getValue():null;
   }

   public Range<C> span() {
      Entry var1 = this.rangesByLowerBound.firstEntry();
      Entry var2 = this.rangesByLowerBound.lastEntry();
      if(var1 == null) {
         throw new NoSuchElementException();
      } else {
         return Range.create(((Range)var1.getValue()).lowerBound, ((Range)var2.getValue()).upperBound);
      }
   }

   public void add(Range<C> var1) {
      Preconditions.checkNotNull(var1);
      if(!var1.isEmpty()) {
         Cut var2 = var1.lowerBound;
         Cut var3 = var1.upperBound;
         Entry var4 = this.rangesByLowerBound.lowerEntry(var2);
         if(var4 != null) {
            Range var5 = (Range)var4.getValue();
            if(var5.upperBound.compareTo(var2) >= 0) {
               if(var5.upperBound.compareTo(var3) >= 0) {
                  var3 = var5.upperBound;
               }

               var2 = var5.lowerBound;
            }
         }

         Entry var7 = this.rangesByLowerBound.floorEntry(var3);
         if(var7 != null) {
            Range var6 = (Range)var7.getValue();
            if(var6.upperBound.compareTo(var3) >= 0) {
               var3 = var6.upperBound;
            }
         }

         this.rangesByLowerBound.subMap(var2, var3).clear();
         this.replaceRangeWithSameLowerBound(Range.create(var2, var3));
      }
   }

   public void remove(Range<C> var1) {
      Preconditions.checkNotNull(var1);
      if(!var1.isEmpty()) {
         Entry var2 = this.rangesByLowerBound.lowerEntry(var1.lowerBound);
         if(var2 != null) {
            Range var3 = (Range)var2.getValue();
            if(var3.upperBound.compareTo(var1.lowerBound) >= 0) {
               if(var1.hasUpperBound() && var3.upperBound.compareTo(var1.upperBound) >= 0) {
                  this.replaceRangeWithSameLowerBound(Range.create(var1.upperBound, var3.upperBound));
               }

               this.replaceRangeWithSameLowerBound(Range.create(var3.lowerBound, var1.lowerBound));
            }
         }

         Entry var5 = this.rangesByLowerBound.floorEntry(var1.upperBound);
         if(var5 != null) {
            Range var4 = (Range)var5.getValue();
            if(var1.hasUpperBound() && var4.upperBound.compareTo(var1.upperBound) >= 0) {
               this.replaceRangeWithSameLowerBound(Range.create(var1.upperBound, var4.upperBound));
            }
         }

         this.rangesByLowerBound.subMap(var1.lowerBound, var1.upperBound).clear();
      }
   }

   private void replaceRangeWithSameLowerBound(Range<C> var1) {
      if(var1.isEmpty()) {
         this.rangesByLowerBound.remove(var1.lowerBound);
      } else {
         this.rangesByLowerBound.put(var1.lowerBound, var1);
      }

   }

   public RangeSet<C> complement() {
      RangeSet var1 = this.complement;
      return var1 == null?(this.complement = new TreeRangeSet.Complement()):var1;
   }

   public RangeSet<C> subRangeSet(Range<C> var1) {
      return (RangeSet)(var1.equals(Range.all())?this:new TreeRangeSet.SubRangeSet(var1));
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void removeAll(RangeSet var1) {
      super.removeAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void addAll(RangeSet var1) {
      super.addAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean enclosesAll(RangeSet var1) {
      return super.enclosesAll(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void clear() {
      super.clear();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean isEmpty() {
      return super.isEmpty();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean contains(Comparable var1) {
      return super.contains(var1);
   }

   // $FF: synthetic method
   TreeRangeSet(NavigableMap var1, TreeRangeSet.SyntheticClass_1 var2) {
      this(var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class SubRangeSet extends TreeRangeSet<C> {
      private final Range<C> restriction;

      SubRangeSet(Range<C> var1) {
         super(new TreeRangeSet.SubRangeSetRangesByLowerBound(Range.all(), var2, TreeRangeSet.this.rangesByLowerBound), (TreeRangeSet.SyntheticClass_1)null);
         this.restriction = var2;
      }

      public boolean encloses(Range<C> var1) {
         if(!this.restriction.isEmpty() && this.restriction.encloses(var1)) {
            Range var2 = TreeRangeSet.this.rangeEnclosing(var1);
            return var2 != null && !var2.intersection(this.restriction).isEmpty();
         } else {
            return false;
         }
      }

      @Nullable
      public Range<C> rangeContaining(C var1) {
         if(!this.restriction.contains(var1)) {
            return null;
         } else {
            Range var2 = TreeRangeSet.this.rangeContaining(var1);
            return var2 == null?null:var2.intersection(this.restriction);
         }
      }

      public void add(Range<C> var1) {
         Preconditions.checkArgument(this.restriction.encloses(var1), "Cannot add range %s to subRangeSet(%s)", new Object[]{var1, this.restriction});
         super.add(var1);
      }

      public void remove(Range<C> var1) {
         if(var1.isConnected(this.restriction)) {
            TreeRangeSet.this.remove(var1.intersection(this.restriction));
         }

      }

      public boolean contains(C var1) {
         return this.restriction.contains(var1) && TreeRangeSet.this.contains(var1);
      }

      public void clear() {
         TreeRangeSet.this.remove(this.restriction);
      }

      public RangeSet<C> subRangeSet(Range<C> var1) {
         return (RangeSet)(var1.encloses(this.restriction)?this:(var1.isConnected(this.restriction)?new TreeRangeSet.SubRangeSet(this.restriction.intersection(var1)):ImmutableRangeSet.of()));
      }
   }

   private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
      private final Range<Cut<C>> lowerBoundWindow;
      private final Range<C> restriction;
      private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
      private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;

      private SubRangeSetRangesByLowerBound(Range<Cut<C>> var1, Range<C> var2, NavigableMap<Cut<C>, Range<C>> var3) {
         this.lowerBoundWindow = (Range)Preconditions.checkNotNull(var1);
         this.restriction = (Range)Preconditions.checkNotNull(var2);
         this.rangesByLowerBound = (NavigableMap)Preconditions.checkNotNull(var3);
         this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound(var3);
      }

      private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> var1) {
         return (NavigableMap)(!var1.isConnected(this.lowerBoundWindow)?ImmutableSortedMap.of():new TreeRangeSet.SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(var1), this.restriction, this.rangesByLowerBound));
      }

      public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> var1, boolean var2, Cut<C> var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator<? super Cut<C>> comparator() {
         return Ordering.natural();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      @Nullable
      public Range<C> get(@Nullable Object var1) {
         if(var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               if(!this.lowerBoundWindow.contains(var2) || var2.compareTo(this.restriction.lowerBound) < 0 || var2.compareTo(this.restriction.upperBound) >= 0) {
                  return null;
               }

               Range var3;
               if(var2.equals(this.restriction.lowerBound)) {
                  var3 = (Range)Maps.valueOrNull(this.rangesByLowerBound.floorEntry(var2));
                  if(var3 != null && var3.upperBound.compareTo(this.restriction.lowerBound) > 0) {
                     return var3.intersection(this.restriction);
                  }
               } else {
                  var3 = (Range)this.rangesByLowerBound.get(var2);
                  if(var3 != null) {
                     return var3.intersection(this.restriction);
                  }
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
         if(this.restriction.isEmpty()) {
            return Iterators.emptyIterator();
         } else if(this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound)) {
            return Iterators.emptyIterator();
         } else {
            final Iterator var1;
            if(this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
               var1 = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
            } else {
               var1 = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values().iterator();
            }

            final Cut var2 = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new AbstractIterator() {
               protected Entry<Cut<C>, Range<C>> computeNext() {
                  if(!var1.hasNext()) {
                     return (Entry)this.endOfData();
                  } else {
                     Range var1x = (Range)var1.next();
                     if(var2.isLessThan(var1x.lowerBound)) {
                        return (Entry)this.endOfData();
                     } else {
                        var1x = var1x.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                        return Maps.immutableEntry(var1x.lowerBound, var1x);
                     }
                  }
               }

               // $FF: synthetic method
               // $FF: bridge method
               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      }

      Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
         if(this.restriction.isEmpty()) {
            return Iterators.emptyIterator();
         } else {
            Cut var1 = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            final Iterator var2 = this.rangesByLowerBound.headMap(var1.endpoint(), var1.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
            return new AbstractIterator() {
               protected Entry<Cut<C>, Range<C>> computeNext() {
                  if(!var2.hasNext()) {
                     return (Entry)this.endOfData();
                  } else {
                     Range var1 = (Range)var2.next();
                     if(SubRangeSetRangesByLowerBound.this.restriction.lowerBound.compareTo(var1.upperBound) >= 0) {
                        return (Entry)this.endOfData();
                     } else {
                        var1 = var1.intersection(SubRangeSetRangesByLowerBound.this.restriction);
                        return SubRangeSetRangesByLowerBound.this.lowerBoundWindow.contains(var1.lowerBound)?Maps.immutableEntry(var1.lowerBound, var1):(Entry)this.endOfData();
                     }
                  }
               }

               // $FF: synthetic method
               // $FF: bridge method
               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      }

      public int size() {
         return Iterators.size(this.entryIterator());
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }

      // $FF: synthetic method
      SubRangeSetRangesByLowerBound(Range var1, Range var2, NavigableMap var3, TreeRangeSet.SyntheticClass_1 var4) {
         this(var1, var2, var3);
      }
   }

   private final class Complement extends TreeRangeSet<C> {
      Complement() {
         super(new TreeRangeSet.ComplementRangesByLowerBound(TreeRangeSet.this.rangesByLowerBound), (TreeRangeSet.SyntheticClass_1)null);
      }

      public void add(Range<C> var1) {
         TreeRangeSet.this.remove(var1);
      }

      public void remove(Range<C> var1) {
         TreeRangeSet.this.add(var1);
      }

      public boolean contains(C var1) {
         return !TreeRangeSet.this.contains(var1);
      }

      public RangeSet<C> complement() {
         return TreeRangeSet.this;
      }
   }

   private static final class ComplementRangesByLowerBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
      private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
      private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
      private final Range<Cut<C>> complementLowerBoundWindow;

      ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> var1) {
         this(var1, Range.all());
      }

      private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> var1, Range<Cut<C>> var2) {
         this.positiveRangesByLowerBound = var1;
         this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound(var1);
         this.complementLowerBoundWindow = var2;
      }

      private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> var1) {
         if(!this.complementLowerBoundWindow.isConnected(var1)) {
            return ImmutableSortedMap.of();
         } else {
            var1 = var1.intersection(this.complementLowerBoundWindow);
            return new TreeRangeSet.ComplementRangesByLowerBound(this.positiveRangesByLowerBound, var1);
         }
      }

      public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> var1, boolean var2, Cut<C> var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator<? super Cut<C>> comparator() {
         return Ordering.natural();
      }

      Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
         Collection var1;
         if(this.complementLowerBoundWindow.hasLowerBound()) {
            var1 = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
         } else {
            var1 = this.positiveRangesByUpperBound.values();
         }

         final PeekingIterator var2 = Iterators.peekingIterator(var1.iterator());
         final Cut var3;
         if(this.complementLowerBoundWindow.contains(Cut.belowAll()) && (!var2.hasNext() || ((Range)var2.peek()).lowerBound != Cut.belowAll())) {
            var3 = Cut.belowAll();
         } else {
            if(!var2.hasNext()) {
               return Iterators.emptyIterator();
            }

            var3 = ((Range)var2.next()).upperBound;
         }

         return new AbstractIterator() {
            Cut<C> nextComplementRangeLowerBound = var3;

            protected Entry<Cut<C>, Range<C>> computeNext() {
               if(!ComplementRangesByLowerBound.this.complementLowerBoundWindow.upperBound.isLessThan(this.nextComplementRangeLowerBound) && this.nextComplementRangeLowerBound != Cut.aboveAll()) {
                  Range var1;
                  if(var2.hasNext()) {
                     Range var2x = (Range)var2.next();
                     var1 = Range.create(this.nextComplementRangeLowerBound, var2x.lowerBound);
                     this.nextComplementRangeLowerBound = var2x.upperBound;
                  } else {
                     var1 = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
                     this.nextComplementRangeLowerBound = Cut.aboveAll();
                  }

                  return Maps.immutableEntry(var1.lowerBound, var1);
               } else {
                  return (Entry)this.endOfData();
               }
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
         Cut var2 = this.complementLowerBoundWindow.hasUpperBound()?(Cut)this.complementLowerBoundWindow.upperEndpoint():Cut.aboveAll();
         boolean var3 = this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED;
         final PeekingIterator var4 = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(var2, var3).descendingMap().values().iterator());
         Cut var5;
         if(var4.hasNext()) {
            var5 = ((Range)var4.peek()).upperBound == Cut.aboveAll()?((Range)var4.next()).lowerBound:(Cut)this.positiveRangesByLowerBound.higherKey(((Range)var4.peek()).upperBound);
         } else {
            if(!this.complementLowerBoundWindow.contains(Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll())) {
               return Iterators.emptyIterator();
            }

            var5 = (Cut)this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
         }

         final Cut var6 = (Cut)Objects.firstNonNull(var5, Cut.aboveAll());
         return new AbstractIterator() {
            Cut<C> nextComplementRangeUpperBound = var6;

            protected Entry<Cut<C>, Range<C>> computeNext() {
               if(this.nextComplementRangeUpperBound == Cut.belowAll()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1;
                  if(var4.hasNext()) {
                     var1 = (Range)var4.next();
                     Range var2 = Range.create(var1.upperBound, this.nextComplementRangeUpperBound);
                     this.nextComplementRangeUpperBound = var1.lowerBound;
                     if(ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(var2.lowerBound)) {
                        return Maps.immutableEntry(var2.lowerBound, var2);
                     }
                  } else if(ComplementRangesByLowerBound.this.complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
                     var1 = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
                     this.nextComplementRangeUpperBound = Cut.belowAll();
                     return Maps.immutableEntry(Cut.belowAll(), var1);
                  }

                  return (Entry)this.endOfData();
               }
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      public int size() {
         return Iterators.size(this.entryIterator());
      }

      @Nullable
      public Range<C> get(Object var1) {
         if(var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               Entry var3 = this.tailMap(var2, true).firstEntry();
               if(var3 != null && ((Cut)var3.getKey()).equals(var2)) {
                  return (Range)var3.getValue();
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      public boolean containsKey(Object var1) {
         return this.get(var1) != null;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }
   }

   @VisibleForTesting
   static final class RangesByUpperBound<C extends Comparable<?>> extends AbstractNavigableMap<Cut<C>, Range<C>> {
      private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
      private final Range<Cut<C>> upperBoundWindow;

      RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> var1) {
         this.rangesByLowerBound = var1;
         this.upperBoundWindow = Range.all();
      }

      private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> var1, Range<Cut<C>> var2) {
         this.rangesByLowerBound = var1;
         this.upperBoundWindow = var2;
      }

      private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> var1) {
         return (NavigableMap)(var1.isConnected(this.upperBoundWindow)?new TreeRangeSet.RangesByUpperBound(this.rangesByLowerBound, var1.intersection(this.upperBoundWindow)):ImmutableSortedMap.of());
      }

      public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> var1, boolean var2, Cut<C> var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator<? super Cut<C>> comparator() {
         return Ordering.natural();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      public Range<C> get(@Nullable Object var1) {
         if(var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               if(!this.upperBoundWindow.contains(var2)) {
                  return null;
               }

               Entry var3 = this.rangesByLowerBound.lowerEntry(var2);
               if(var3 != null && ((Range)var3.getValue()).upperBound.equals(var2)) {
                  return (Range)var3.getValue();
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
         final Iterator var1;
         if(!this.upperBoundWindow.hasLowerBound()) {
            var1 = this.rangesByLowerBound.values().iterator();
         } else {
            Entry var2 = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
            if(var2 == null) {
               var1 = this.rangesByLowerBound.values().iterator();
            } else if(this.upperBoundWindow.lowerBound.isLessThan(((Range)var2.getValue()).upperBound)) {
               var1 = this.rangesByLowerBound.tailMap(var2.getKey(), true).values().iterator();
            } else {
               var1 = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
            }
         }

         return new AbstractIterator() {
            protected Entry<Cut<C>, Range<C>> computeNext() {
               if(!var1.hasNext()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1x = (Range)var1.next();
                  return RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(var1x.upperBound)?(Entry)this.endOfData():Maps.immutableEntry(var1x.upperBound, var1x);
               }
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      Iterator<Entry<Cut<C>, Range<C>>> descendingEntryIterator() {
         Collection var1;
         if(this.upperBoundWindow.hasUpperBound()) {
            var1 = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
         } else {
            var1 = this.rangesByLowerBound.descendingMap().values();
         }

         final PeekingIterator var2 = Iterators.peekingIterator(var1.iterator());
         if(var2.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range)var2.peek()).upperBound)) {
            var2.next();
         }

         return new AbstractIterator() {
            protected Entry<Cut<C>, Range<C>> computeNext() {
               if(!var2.hasNext()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1 = (Range)var2.next();
                  return RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(var1.upperBound)?Maps.immutableEntry(var1.upperBound, var1):(Entry)this.endOfData();
               }
            }

            // $FF: synthetic method
            // $FF: bridge method
            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      public int size() {
         return this.upperBoundWindow.equals(Range.all())?this.rangesByLowerBound.size():Iterators.size(this.entryIterator());
      }

      public boolean isEmpty() {
         return this.upperBoundWindow.equals(Range.all())?this.rangesByLowerBound.isEmpty():!this.entryIterator().hasNext();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object get(Object var1) {
         return this.get(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }
   }

   final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>> {
      AsRanges() {
      }

      protected Collection<Range<C>> delegate() {
         return TreeRangeSet.this.rangesByLowerBound.values();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object delegate() {
         return this.delegate();
      }
   }
}
