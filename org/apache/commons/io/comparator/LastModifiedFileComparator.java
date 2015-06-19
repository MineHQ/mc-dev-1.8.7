package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.comparator.AbstractFileComparator;
import org.apache.commons.io.comparator.ReverseComparator;

public class LastModifiedFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator<File> LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();
   public static final Comparator<File> LASTMODIFIED_REVERSE;

   public LastModifiedFileComparator() {
   }

   public int compare(File var1, File var2) {
      long var3 = var1.lastModified() - var2.lastModified();
      return var3 < 0L?-1:(var3 > 0L?1:0);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String toString() {
      return super.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public List sort(List var1) {
      return super.sort(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public File[] sort(File[] var1) {
      return super.sort(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compare(Object var1, Object var2) {
      return this.compare((File)var1, (File)var2);
   }

   static {
      LASTMODIFIED_REVERSE = new ReverseComparator(LASTMODIFIED_COMPARATOR);
   }
}
