package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public abstract class AbstractDatabaseAppender<T extends AbstractDatabaseManager> extends AbstractAppender {
   private final ReadWriteLock lock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;
   private T manager;

   protected AbstractDatabaseAppender(String var1, Filter var2, boolean var3, T var4) {
      super(var1, var2, (Layout)null, var3);
      this.readLock = this.lock.readLock();
      this.writeLock = this.lock.writeLock();
      this.manager = var4;
   }

   public final Layout<LogEvent> getLayout() {
      return null;
   }

   public final T getManager() {
      return this.manager;
   }

   public final void start() {
      if(this.getManager() == null) {
         LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", new Object[]{this.getName()});
      }

      super.start();
      if(this.getManager() != null) {
         this.getManager().connect();
      }

   }

   public final void stop() {
      super.stop();
      if(this.getManager() != null) {
         this.getManager().release();
      }

   }

   public final void append(LogEvent var1) {
      this.readLock.lock();

      try {
         this.getManager().write(var1);
      } catch (LoggingException var7) {
         LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[]{this.getManager().getName(), this.getName(), var7});
         throw var7;
      } catch (Exception var8) {
         LOGGER.error("Unable to write to database [{}] for appender [{}].", new Object[]{this.getManager().getName(), this.getName(), var8});
         throw new AppenderLoggingException("Unable to write to database in appender: " + var8.getMessage(), var8);
      } finally {
         this.readLock.unlock();
      }

   }

   protected final void replaceManager(T var1) {
      this.writeLock.lock();

      try {
         AbstractDatabaseManager var2 = this.getManager();
         if(!var1.isConnected()) {
            var1.connect();
         }

         this.manager = var1;
         var2.release();
      } finally {
         this.writeLock.unlock();
      }

   }
}
