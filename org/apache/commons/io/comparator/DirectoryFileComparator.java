package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.comparator.AbstractFileComparator;
import org.apache.commons.io.comparator.ReverseComparator;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator<File> DIRECTORY_COMPARATOR = new DirectoryFileComparator();
   public static final Comparator<File> DIRECTORY_REVERSE;

   public DirectoryFileComparator() {
   }

   public int compare(File var1, File var2) {
      return this.getType(var1) - this.getType(var2);
   }

   private int getType(File var1) {
      return var1.isDirectory()?1:2;
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
      DIRECTORY_REVERSE = new ReverseComparator(DIRECTORY_COMPARATOR);
   }
}
