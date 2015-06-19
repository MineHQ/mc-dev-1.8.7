package org.apache.logging.log4j.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class StatusLogger extends AbstractLogger {
   public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
   private static final String NOT_AVAIL = "?";
   private static final PropertiesUtil PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
   private static final int MAX_ENTRIES;
   private static final String DEFAULT_STATUS_LEVEL;
   private static final StatusLogger STATUS_LOGGER;
   private final SimpleLogger logger;
   private final CopyOnWriteArrayList<StatusListener> listeners = new CopyOnWriteArrayList();
   private final ReentrantReadWriteLock listenersLock = new ReentrantReadWriteLock();
   private final Queue<StatusData> messages;
   private final ReentrantLock msgLock;
   private int listenersLevel;

   private StatusLogger() {
      this.messages = new StatusLogger.BoundedQueue(MAX_ENTRIES);
      this.msgLock = new ReentrantLock();
      this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", (MessageFactory)null, PROPS, System.err);
      this.listenersLevel = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
   }

   public static StatusLogger getLogger() {
      return STATUS_LOGGER;
   }

   public Level getLevel() {
      return this.logger.getLevel();
   }

   public void setLevel(Level var1) {
      this.logger.setLevel(var1);
   }

   public void registerListener(StatusListener var1) {
      this.listenersLock.writeLock().lock();

      try {
         this.listeners.add(var1);
         Level var2 = var1.getStatusLevel();
         if(this.listenersLevel < var2.intLevel()) {
            this.listenersLevel = var2.intLevel();
         }
      } finally {
         this.listenersLock.writeLock().unlock();
      }

   }

   public void removeListener(StatusListener var1) {
      this.listenersLock.writeLock().lock();

      try {
         this.listeners.remove(var1);
         int var2 = Level.toLevel(DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
         Iterator var3 = this.listeners.iterator();

         while(var3.hasNext()) {
            StatusListener var4 = (StatusListener)var3.next();
            int var5 = var4.getStatusLevel().intLevel();
            if(var2 < var5) {
               var2 = var5;
            }
         }

         this.listenersLevel = var2;
      } finally {
         this.listenersLock.writeLock().unlock();
      }
   }

   public Iterator<StatusListener> getListeners() {
      return this.listeners.iterator();
   }

   public void reset() {
      this.listeners.clear();
      this.clear();
   }

   public List<StatusData> getStatusData() {
      this.msgLock.lock();

      ArrayList var1;
      try {
         var1 = new ArrayList(this.messages);
      } finally {
         this.msgLock.unlock();
      }

      return var1;
   }

   public void clear() {
      this.msgLock.lock();

      try {
         this.messages.clear();
      } finally {
         this.msgLock.unlock();
      }

   }

   public void log(Marker var1, String var2, Level var3, Message var4, Throwable var5) {
      StackTraceElement var6 = null;
      if(var2 != null) {
         var6 = this.getStackTraceElement(var2, Thread.currentThread().getStackTrace());
      }

      StatusData var7 = new StatusData(var6, var3, var4, var5);
      this.msgLock.lock();

      try {
         this.messages.add(var7);
      } finally {
         this.msgLock.unlock();
      }

      if(this.listeners.size() > 0) {
         Iterator var8 = this.listeners.iterator();

         while(var8.hasNext()) {
            StatusListener var9 = (StatusListener)var8.next();
            if(var7.getLevel().isAtLeastAsSpecificAs(var9.getStatusLevel())) {
               var9.log(var7);
            }
         }
      } else {
         this.logger.log(var1, var2, var3, var4, var5);
      }

   }

   private StackTraceElement getStackTraceElement(String var1, StackTraceElement[] var2) {
      if(var1 == null) {
         return null;
      } else {
         boolean var3 = false;
         StackTraceElement[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            StackTraceElement var7 = var4[var6];
            if(var3) {
               return var7;
            }

            String var8 = var7.getClassName();
            if(var1.equals(var8)) {
               var3 = true;
            } else if("?".equals(var8)) {
               break;
            }
         }

         return null;
      }
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, String var3, Object... var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   protected boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4) {
      return this.isEnabled(var1, var2);
   }

   public boolean isEnabled(Level var1, Marker var2) {
      if(this.listeners.size() > 0) {
         return this.listenersLevel >= var1.intLevel();
      } else {
         switch(StatusLogger.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[var1.ordinal()]) {
         case 1:
            return this.logger.isFatalEnabled(var2);
         case 2:
            return this.logger.isTraceEnabled(var2);
         case 3:
            return this.logger.isDebugEnabled(var2);
         case 4:
            return this.logger.isInfoEnabled(var2);
         case 5:
            return this.logger.isWarnEnabled(var2);
         case 6:
            return this.logger.isErrorEnabled(var2);
         default:
            return false;
         }
      }
   }

   static {
      MAX_ENTRIES = PROPS.getIntegerProperty("log4j2.status.entries", 200);
      DEFAULT_STATUS_LEVEL = PROPS.getStringProperty("log4j2.StatusLogger.level");
      STATUS_LOGGER = new StatusLogger();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$org$apache$logging$log4j$Level = new int[Level.values().length];

      static {
         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.FATAL.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.TRACE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.DEBUG.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.INFO.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.WARN.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.ERROR.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private class BoundedQueue<E> extends ConcurrentLinkedQueue<E> {
      private static final long serialVersionUID = -3945953719763255337L;
      private final int size;

      public BoundedQueue(int var2) {
         this.size = var2;
      }

      public boolean add(E var1) {
         while(StatusLogger.this.messages.size() > this.size) {
            StatusLogger.this.messages.poll();
         }

         return super.add(var1);
      }
   }
}
