package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.mutable.Mutable;

public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
   private static final long serialVersionUID = -2135791679L;
   private short value;

   public MutableShort() {
   }

   public MutableShort(short var1) {
      this.value = var1;
   }

   public MutableShort(Number var1) {
      this.value = var1.shortValue();
   }

   public MutableShort(String var1) throws NumberFormatException {
      this.value = Short.parseShort(var1);
   }

   public Short getValue() {
      return Short.valueOf(this.value);
   }

   public void setValue(short var1) {
      this.value = var1;
   }

   public void setValue(Number var1) {
      this.value = var1.shortValue();
   }

   public void increment() {
      ++this.value;
   }

   public void decrement() {
      --this.value;
   }

   public void add(short var1) {
      this.value += var1;
   }

   public void add(Number var1) {
      this.value += var1.shortValue();
   }

   public void subtract(short var1) {
      this.value -= var1;
   }

   public void subtract(Number var1) {
      this.value -= var1.shortValue();
   }

   public short shortValue() {
      return this.value;
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return (long)this.value;
   }

   public float floatValue() {
      return (float)this.value;
   }

   public double doubleValue() {
      return (double)this.value;
   }

   public Short toShort() {
      return Short.valueOf(this.shortValue());
   }

   public boolean equals(Object var1) {
      return var1 instanceof MutableShort?this.value == ((MutableShort)var1).shortValue():false;
   }

   public int hashCode() {
      return this.value;
   }

   public int compareTo(MutableShort var1) {
      short var2 = var1.value;
      return this.value < var2?-1:(this.value == var2?0:1);
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compareTo(Object var1) {
      return this.compareTo((MutableShort)var1);
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
