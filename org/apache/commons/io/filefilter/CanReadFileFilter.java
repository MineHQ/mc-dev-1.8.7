package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;

public class CanReadFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter CAN_READ = new CanReadFileFilter();
   public static final IOFileFilter CANNOT_READ;
   public static final IOFileFilter READ_ONLY;

   protected CanReadFileFilter() {
   }

   public boolean accept(File var1) {
      return var1.canRead();
   }

   static {
      CANNOT_READ = new NotFileFilter(CAN_READ);
      READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);
   }
}
