package org.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.ConcurrentInitializer;

public class ConstantInitializer<T> implements ConcurrentInitializer<T> {
   private static final String FMT_TO_STRING = "ConstantInitializer@%d [ object = %s ]";
   private final T object;

   public ConstantInitializer(T var1) {
      this.object = var1;
   }

   public final T getObject() {
      return this.object;
   }

   public T get() throws ConcurrentException {
      return this.getObject();
   }

   public int hashCode() {
      return this.getObject() != null?this.getObject().hashCode():0;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(!(var1 instanceof ConstantInitializer)) {
         return false;
      } else {
         ConstantInitializer var2 = (ConstantInitializer)var1;
         return ObjectUtils.equals(this.getObject(), var2.getObject());
      }
   }

   public String toString() {
      return String.format("ConstantInitializer@%d [ object = %s ]", new Object[]{Integer.valueOf(System.identityHashCode(this)), String.valueOf(this.getObject())});
   }
}
