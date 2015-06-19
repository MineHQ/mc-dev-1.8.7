package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.comparator.AbstractFileComparator;
import org.apache.commons.io.comparator.ReverseComparator;

public class DefaultFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();
   public static final Comparator<File> DEFAULT_REVERSE;

   public DefaultFileComparator() {
   }

   public int compare(File var1, File var2) {
      return var1.compareTo(var2);
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
      DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);
   }
}
