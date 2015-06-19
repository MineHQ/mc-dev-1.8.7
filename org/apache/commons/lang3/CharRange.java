package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class CharRange implements Iterable<Character>, Serializable {
   private static final long serialVersionUID = 8270183163158333422L;
   private final char start;
   private final char end;
   private final boolean negated;
   private transient String iToString;

   private CharRange(char var1, char var2, boolean var3) {
      if(var1 > var2) {
         char var4 = var1;
         var1 = var2;
         var2 = var4;
      }

      this.start = var1;
      this.end = var2;
      this.negated = var3;
   }

   public static CharRange is(char var0) {
      return new CharRange(var0, var0, false);
   }

   public static CharRange isNot(char var0) {
      return new CharRange(var0, var0, true);
   }

   public static CharRange isIn(char var0, char var1) {
      return new CharRange(var0, var1, false);
   }

   public static CharRange isNotIn(char var0, char var1) {
      return new CharRange(var0, var1, true);
   }

   public char getStart() {
      return this.start;
   }

   public char getEnd() {
      return this.end;
   }

   public boolean isNegated() {
      return this.negated;
   }

   public boolean contains(char var1) {
      return (var1 >= this.start && var1 <= this.end) != this.negated;
   }

   public boolean contains(CharRange var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("The Range must not be null");
      } else {
         return this.negated?(var1.negated?this.start >= var1.start && this.end <= var1.end:var1.end < this.start || var1.start > this.end):(var1.negated?this.start == 0 && this.end == '\uffff':this.start <= var1.start && this.end >= var1.end);
      }
   }

   public boolean equals(Object var1) {
      if(var1 == this) {
         return true;
      } else if(!(var1 instanceof CharRange)) {
         return false;
      } else {
         CharRange var2 = (CharRange)var1;
         return this.start == var2.start && this.end == var2.end && this.negated == var2.negated;
      }
   }

   public int hashCode() {
      return 83 + this.start + 7 * this.end + (this.negated?1:0);
   }

   public String toString() {
      if(this.iToString == null) {
         StringBuilder var1 = new StringBuilder(4);
         if(this.isNegated()) {
            var1.append('^');
         }

         var1.append(this.start);
         if(this.start != this.end) {
            var1.append('-');
            var1.append(this.end);
         }

         this.iToString = var1.toString();
      }

      return this.iToString;
   }

   public Iterator<Character> iterator() {
      return new CharRange.CharacterIterator(this);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class CharacterIterator implements Iterator<Character> {
      private char current;
      private final CharRange range;
      private boolean hasNext;

      private CharacterIterator(CharRange var1) {
         this.range = var1;
         this.hasNext = true;
         if(this.range.negated) {
            if(this.range.start == 0) {
               if(this.range.end == '\uffff') {
                  this.hasNext = false;
               } else {
                  this.current = (char)(this.range.end + 1);
               }
            } else {
               this.current = 0;
            }
         } else {
            this.current = this.range.start;
         }

      }

      private void prepareNext() {
         if(this.range.negated) {
            if(this.current == '\uffff') {
               this.hasNext = false;
            } else if(this.current + 1 == this.range.start) {
               if(this.range.end == '\uffff') {
                  this.hasNext = false;
               } else {
                  this.current = (char)(this.range.end + 1);
               }
            } else {
               ++this.current;
            }
         } else if(this.current < this.range.end) {
            ++this.current;
         } else {
            this.hasNext = false;
         }

      }

      public boolean hasNext() {
         return this.hasNext;
      }

      public Character next() {
         if(!this.hasNext) {
            throw new NoSuchElementException();
         } else {
            char var1 = this.current;
            this.prepareNext();
            return Character.valueOf(var1);
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object next() {
         return this.next();
      }

      // $FF: synthetic method
      CharacterIterator(CharRange var1, CharRange.SyntheticClass_1 var2) {
         this(var1);
      }
   }
}
