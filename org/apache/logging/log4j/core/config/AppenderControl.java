package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.filter.Filterable;

public class AppenderControl extends AbstractFilterable {
   private final ThreadLocal<AppenderControl> recursive = new ThreadLocal();
   private final Appender appender;
   private final Level level;
   private final int intLevel;

   public AppenderControl(Appender var1, Level var2, Filter var3) {
      super(var3);
      this.appender = var1;
      this.level = var2;
      this.intLevel = var2 == null?Level.ALL.intLevel():var2.intLevel();
      this.startFilter();
   }

   public Appender getAppender() {
      return this.appender;
   }

   public void callAppender(LogEvent var1) {
      if(this.getFilter() != null) {
         Filter.Result var2 = this.getFilter().filter(var1);
         if(var2 == Filter.Result.DENY) {
            return;
         }
      }

      if(this.level == null || this.intLevel >= var1.getLevel().intLevel()) {
         if(this.recursive.get() != null) {
            this.appender.getHandler().error("Recursive call to appender " + this.appender.getName());
         } else {
            try {
               this.recursive.set(this);
               if(!this.appender.isStarted()) {
                  this.appender.getHandler().error("Attempted to append to non-started appender " + this.appender.getName());
                  if(!this.appender.ignoreExceptions()) {
                     throw new AppenderLoggingException("Attempted to append to non-started appender " + this.appender.getName());
                  }
               }

               if(this.appender instanceof Filterable && ((Filterable)this.appender).isFiltered(var1)) {
                  return;
               }

               try {
                  this.appender.append(var1);
               } catch (RuntimeException var7) {
                  this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), var7);
                  if(!this.appender.ignoreExceptions()) {
                     throw var7;
                  }
               } catch (Exception var8) {
                  this.appender.getHandler().error("An exception occurred processing Appender " + this.appender.getName(), var8);
                  if(!this.appender.ignoreExceptions()) {
                     throw new AppenderLoggingException(var8);
                  }
               }
            } finally {
               this.recursive.set((Object)null);
            }

         }
      }
   }
}
