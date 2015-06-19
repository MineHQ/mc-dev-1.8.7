package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.mutable.Mutable;

public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {
   private static final long serialVersionUID = 62986528375L;
   private long value;

   public MutableLong() {
   }

   public MutableLong(long var1) {
      this.value = var1;
   }

   public MutableLong(Number var1) {
      this.value = var1.longValue();
   }

   public MutableLong(String var1) throws NumberFormatException {
      this.value = Long.parseLong(var1);
   }

   public Long getValue() {
      return Long.valueOf(this.value);
   }

   public void setValue(long var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.longValue();
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(long var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.longValue();
   }

   public void subtract(long var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.longValue();
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public Long toLong() {
      return Long.valueOf(this.longValue());
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableLong?this.value == ((MutableLong)var1).longValue():false;
   }

   public int hashCode() {
      return (int)(this.value ^ this.value >>> 32);
   }

   public int compareTo(MutableLong var1) {
      long var2 = var1.value;
      return this.value < var2?-1:(this.value == var2?0:1);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compareTo(Object var1) {
      return this.compareTo((MutableLong)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object getValue() {
      return this.getValue();
   }
}
