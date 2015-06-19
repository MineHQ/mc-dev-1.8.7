package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.comparator.AbstractFileComparator;

class ReverseComparator extends AbstractFileComparator implements Serializable {
   private final Comparator<File> delegate;

   public ReverseComparator(Comparator<File> var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Delegate comparator is missing");
      } else {
         this.delegate = var1;
      }
   }

   public int compare(File var1, File var2) {
      return this.delegate.compare(var2, var1);
   }

   public String toString() {
      return super.toString() + "[" + this.delegate.toString() + "]";
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compare(Object var1, Object var2) {
      return this.compare((File)var1, (File)var2);
   }
}
