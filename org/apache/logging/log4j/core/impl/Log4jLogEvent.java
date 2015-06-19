package org.apache.logging.log4j.core.impl;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.TimestampMessage;

public class Log4jLogEvent implements LogEvent {
   private static final long serialVersionUID = -1351367343806656055L;
   private static final String NOT_AVAIL = "?";
   private final String fqcnOfLogger;
   private final Marker marker;
   private final Level level;
   private final String name;
   private final Message message;
   private final long timestamp;
   private final ThrowableProxy throwable;
   private final Map<String, String> mdc;
   private final ThreadContext.ContextStack ndc;
   private String threadName;
   private StackTraceElement location;
   private boolean includeLocation;
   private boolean endOfBatch;

   public Log4jLogEvent(long var1) {
      this("", (Marker)null, "", (Level)null, (Message)null, (ThrowableProxy)((ThrowableProxy)null), (Map)null, (ThreadContext.ContextStack)null, (String)null, (StackTraceElement)null, var1);
   }

   public Log4jLogEvent(String var1, Marker var2, String var3, Level var4, Message var5, Throwable var6) {
      this(var1, var2, var3, var4, var5, (List)null, var6);
   }

   public Log4jLogEvent(String var1, Marker var2, String var3, Level var4, Message var5, List<Property> var6, Throwable var7) {
      this(var1, var2, var3, var4, var5, (Throwable)var7, createMap(var6), ThreadContext.getDepth() == 0?null:ThreadContext.cloneStack(), (String)null, (StackTraceElement)null, System.currentTimeMillis());
   }

   public Log4jLogEvent(String var1, Marker var2, String var3, Level var4, Message var5, Throwable var6, Map<String, String> var7, ThreadContext.ContextStack var8, String var9, StackTraceElement var10, long var11) {
      this(var1, var2, var3, var4, var5, var6 == null?null:new ThrowableProxy(var6), var7, var8, var9, var10, var11);
   }

   public static Log4jLogEvent createEvent(String var0, Marker var1, String var2, Level var3, Message var4, ThrowableProxy var5, Map<String, String> var6, ThreadContext.ContextStack var7, String var8, StackTraceElement var9, long var10) {
      return new Log4jLogEvent(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   private Log4jLogEvent(String var1, Marker var2, String var3, Level var4, Message var5, ThrowableProxy var6, Map<String, String> var7, ThreadContext.ContextStack var8, String var9, StackTraceElement var10, long var11) {
      this.threadName = null;
      this.endOfBatch = false;
      this.name = var1;
      this.marker = var2;
      this.fqcnOfLogger = var3;
      this.level = var4;
      this.message = var5;
      this.throwable = var6;
      this.mdc = var7;
      this.ndc = var8;
      this.timestamp = var5 instanceof TimestampMessage?((TimestampMessage)var5).getTimestamp():var11;
      this.threadName = var9;
      this.location = var10;
      if(var5 != null && var5 instanceof LoggerNameAwareMessage) {
         ((LoggerNameAwareMessage)var5).setLoggerName(this.name);
      }

   }

   private static Map<String, String> createMap(List<Property> var0) {
      Map var1 = ThreadContext.getImmutableContext();
      if(var1 != null || var0 != null && var0.size() != 0) {
         if(var0 != null && var0.size() != 0) {
            HashMap var2 = new HashMap(var1);
            Iterator var3 = var0.iterator();

            while(var3.hasNext()) {
               Property var4 = (Property)var3.next();
               if(!var2.containsKey(var4.getName())) {
                  var2.put(var4.getName(), var4.getValue());
               }
            }

            return Collections.unmodifiableMap(var2);
         } else {
            return var1;
         }
      } else {
         return null;
      }
   }

   public Level getLevel() {
      return this.level;
   }

   public String getLoggerName() {
      return this.name;
   }

   public Message getMessage() {
      return this.message;
   }

   public String getThreadName() {
      if(this.threadName == null) {
         this.threadName = Thread.currentThread().getName();
      }

      return this.threadName;
   }

   public long getMillis() {
      return this.timestamp;
   }

   public Throwable getThrown() {
      return this.throwable == null?null:this.throwable.getThrowable();
   }

   public ThrowableProxy getThrownProxy() {
      return this.throwable;
   }

   public Marker getMarker() {
      return this.marker;
   }

   public String getFQCN() {
      return this.fqcnOfLogger;
   }

   public Map<String, String> getContextMap() {
      return this.mdc == null?ThreadContext.EMPTY_MAP:this.mdc;
   }

   public ThreadContext.ContextStack getContextStack() {
      return (ThreadContext.ContextStack)(this.ndc == null?ThreadContext.EMPTY_STACK:this.ndc);
   }

   public StackTraceElement getSource() {
      if(this.location != null) {
         return this.location;
      } else if(this.fqcnOfLogger != null && this.includeLocation) {
         this.location = calcLocation(this.fqcnOfLogger);
         return this.location;
      } else {
         return null;
      }
   }

   public static StackTraceElement calcLocation(String var0) {
      if(var0 == null) {
         return null;
      } else {
         StackTraceElement[] var1 = Thread.currentThread().getStackTrace();
         boolean var2 = false;
         StackTraceElement[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            StackTraceElement var6 = var3[var5];
            String var7 = var6.getClassName();
            if(var2) {
               if(!var0.equals(var7)) {
                  return var6;
               }
            } else if(var0.equals(var7)) {
               var2 = true;
            } else if("?".equals(var7)) {
               break;
            }
         }

         return null;
      }
   }

   public boolean isIncludeLocation() {
      return this.includeLocation;
   }

   public void setIncludeLocation(boolean var1) {
      this.includeLocation = var1;
   }

   public boolean isEndOfBatch() {
      return this.endOfBatch;
   }

   public void setEndOfBatch(boolean var1) {
      this.endOfBatch = var1;
   }

   protected Object writeReplace() {
      return new Log4jLogEvent.LogEventProxy(this, this.includeLocation);
   }

   public static Serializable serialize(Log4jLogEvent var0, boolean var1) {
      return new Log4jLogEvent.LogEventProxy(var0, var1);
   }

   public static Log4jLogEvent deserialize(Serializable var0) {
      if(var0 == null) {
         throw new NullPointerException("Event cannot be null");
      } else if(var0 instanceof Log4jLogEvent.LogEventProxy) {
         Log4jLogEvent.LogEventProxy var1 = (Log4jLogEvent.LogEventProxy)var0;
         Log4jLogEvent var2 = new Log4jLogEvent(var1.name, var1.marker, var1.fqcnOfLogger, var1.level, var1.message, var1.throwable, var1.mdc, var1.ndc, var1.threadName, var1.location, var1.timestamp);
         var2.setEndOfBatch(var1.isEndOfBatch);
         var2.setIncludeLocation(var1.isLocationRequired);
         return var2;
      } else {
         throw new IllegalArgumentException("Event is not a serialized LogEvent: " + var0.toString());
      }
   }

   private void readObject(ObjectInputStream var1) throws InvalidObjectException {
      throw new InvalidObjectException("Proxy required");
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      String var2 = this.name.isEmpty()?"root":this.name;
      var1.append("Logger=").append(var2);
      var1.append(" Level=").append(this.level.name());
      var1.append(" Message=").append(this.message.getFormattedMessage());
      return var1.toString();
   }

   // $FF: synthetic method
   Log4jLogEvent(String var1, Marker var2, String var3, Level var4, Message var5, ThrowableProxy var6, Map var7, ThreadContext.ContextStack var8, String var9, StackTraceElement var10, long var11, Log4jLogEvent.SyntheticClass_1 var13) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class LogEventProxy implements Serializable {
      private static final long serialVersionUID = -7139032940312647146L;
      private final String fqcnOfLogger;
      private final Marker marker;
      private final Level level;
      private final String name;
      private final Message message;
      private final long timestamp;
      private final ThrowableProxy throwable;
      private final Map<String, String> mdc;
      private final ThreadContext.ContextStack ndc;
      private final String threadName;
      private final StackTraceElement location;
      private final boolean isLocationRequired;
      private final boolean isEndOfBatch;

      public LogEventProxy(Log4jLogEvent var1, boolean var2) {
         this.fqcnOfLogger = var1.fqcnOfLogger;
         this.marker = var1.marker;
         this.level = var1.level;
         this.name = var1.name;
         this.message = var1.message;
         this.timestamp = var1.timestamp;
         this.throwable = var1.throwable;
         this.mdc = var1.mdc;
         this.ndc = var1.ndc;
         this.location = var2?var1.getSource():null;
         this.threadName = var1.getThreadName();
         this.isLocationRequired = var2;
         this.isEndOfBatch = var1.endOfBatch;
      }

      protected Object readResolve() {
         Log4jLogEvent var1 = new Log4jLogEvent(this.name, this.marker, this.fqcnOfLogger, this.level, this.message, this.throwable, this.mdc, this.ndc, this.threadName, this.location, this.timestamp);
         var1.setEndOfBatch(this.isEndOfBatch);
         var1.setIncludeLocation(this.isLocationRequired);
         return var1;
      }
   }
}
