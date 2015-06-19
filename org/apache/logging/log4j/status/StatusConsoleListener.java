package org.apache.logging.log4j.status;

import java.io.PrintStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.util.PropertiesUtil;

public class StatusConsoleListener implements StatusListener {
   private static final String STATUS_LEVEL = "org.apache.logging.log4j.StatusLevel";
   private Level level;
   private String[] filters;
   private final PrintStream stream;

   public StatusConsoleListener() {
      this.level = Level.FATAL;
      this.filters = null;
      String var1 = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.StatusLevel");
      if(var1 != null) {
         this.level = Level.toLevel(var1, Level.FATAL);
      }

      this.stream = System.out;
   }

   public StatusConsoleListener(Level var1) {
      this.level = Level.FATAL;
      this.filters = null;
      this.level = var1;
      this.stream = System.out;
   }

   public StatusConsoleListener(Level var1, PrintStream var2) {
      this.level = Level.FATAL;
      this.filters = null;
      this.level = var1;
      this.stream = var2;
   }

   public void setLevel(Level var1) {
      this.level = var1;
   }

   public Level getStatusLevel() {
      return this.level;
   }

   public void log(StatusData var1) {
      if(!this.filtered(var1)) {
         this.stream.println(var1.getFormattedStatus());
      }

   }

   public void setFilters(String... var1) {
      this.filters = var1;
   }

   private boolean filtered(StatusData var1) {
      if(this.filters == null) {
         return false;
      } else {
         String var2 = var1.getStackTraceElement().getClassName();
         String[] var3 = this.filters;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if(var2.startsWith(var6)) {
               return true;
            }
         }

         return false;
      }
   }
}
