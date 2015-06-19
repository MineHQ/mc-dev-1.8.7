package io.netty.util;

import io.netty.util.ResourceLeak;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ResourceLeakDetector<T> {
   private static final String PROP_LEVEL = "io.netty.leakDetectionLevel";
   private static final ResourceLeakDetector.Level DEFAULT_LEVEL;
   private static ResourceLeakDetector.Level level;
   private static final InternalLogger logger;
   private static final int DEFAULT_SAMPLING_INTERVAL = 113;
   private final ResourceLeakDetector<T>.DefaultResourceLeak head;
   private final ResourceLeakDetector<T>.DefaultResourceLeak tail;
   private final ReferenceQueue<Object> refQueue;
   private final ConcurrentMap<String, Boolean> reportedLeaks;
   private final String resourceType;
   private final int samplingInterval;
   private final long maxActive;
   private long active;
   private final AtomicBoolean loggedTooManyActive;
   private long leakCheckCnt;
   private static final String[] STACK_TRACE_ELEMENT_EXCLUSIONS;

   /** @deprecated */
   @Deprecated
   public static void setEnabled(boolean var0) {
      setLevel(var0?ResourceLeakDetector.Level.SIMPLE:ResourceLeakDetector.Level.DISABLED);
   }

   public static boolean isEnabled() {
      return getLevel().ordinal() > ResourceLeakDetector.Level.DISABLED.ordinal();
   }

   public static void setLevel(ResourceLeakDetector.Level var0) {
      if(var0 == null) {
         throw new NullPointerException("level");
      } else {
         level = var0;
      }
   }

   public static ResourceLeakDetector.Level getLevel() {
      return level;
   }

   public ResourceLeakDetector(Class<?> var1) {
      this(StringUtil.simpleClassName(var1));
   }

   public ResourceLeakDetector(String var1) {
      this((String)var1, 113, Long.MAX_VALUE);
   }

   public ResourceLeakDetector(Class<?> var1, int var2, long var3) {
      this(StringUtil.simpleClassName(var1), var2, var3);
   }

   public ResourceLeakDetector(String var1, int var2, long var3) {
      this.head = new ResourceLeakDetector.DefaultResourceLeak((Object)null);
      this.tail = new ResourceLeakDetector.DefaultResourceLeak((Object)null);
      this.refQueue = new ReferenceQueue();
      this.reportedLeaks = PlatformDependent.newConcurrentHashMap();
      this.loggedTooManyActive = new AtomicBoolean();
      if(var1 == null) {
         throw new NullPointerException("resourceType");
      } else if(var2 <= 0) {
         throw new IllegalArgumentException("samplingInterval: " + var2 + " (expected: 1+)");
      } else if(var3 <= 0L) {
         throw new IllegalArgumentException("maxActive: " + var3 + " (expected: 1+)");
      } else {
         this.resourceType = var1;
         this.samplingInterval = var2;
         this.maxActive = var3;
         this.head.next = this.tail;
         this.tail.prev = this.head;
      }
   }

   public ResourceLeak open(T var1) {
      ResourceLeakDetector.Level var2 = level;
      if(var2 == ResourceLeakDetector.Level.DISABLED) {
         return null;
      } else if(var2.ordinal() < ResourceLeakDetector.Level.PARANOID.ordinal()) {
         if(this.leakCheckCnt++ % (long)this.samplingInterval == 0L) {
            this.reportLeak(var2);
            return new ResourceLeakDetector.DefaultResourceLeak(var1);
         } else {
            return null;
         }
      } else {
         this.reportLeak(var2);
         return new ResourceLeakDetector.DefaultResourceLeak(var1);
      }
   }

   private void reportLeak(ResourceLeakDetector.Level var1) {
      if(logger.isErrorEnabled()) {
         int var5 = var1 == ResourceLeakDetector.Level.PARANOID?1:this.samplingInterval;
         if(this.active * (long)var5 > this.maxActive && this.loggedTooManyActive.compareAndSet(false, true)) {
            logger.error("LEAK: You are creating too many " + this.resourceType + " instances.  " + this.resourceType + " is a shared resource that must be reused across the JVM," + "so that only a few instances are created.");
         }

         while(true) {
            ResourceLeakDetector.DefaultResourceLeak var3 = (ResourceLeakDetector.DefaultResourceLeak)this.refQueue.poll();
            if(var3 == null) {
               return;
            }

            var3.clear();
            if(var3.close()) {
               String var4 = var3.toString();
               if(this.reportedLeaks.putIfAbsent(var4, Boolean.TRUE) == null) {
                  if(var4.isEmpty()) {
                     logger.error("LEAK: {}.release() was not called before it\'s garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option \'-D{}={}\' or call {}.setLevel()", new Object[]{this.resourceType, "io.netty.leakDetectionLevel", ResourceLeakDetector.Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName((Object)this)});
                  } else {
                     logger.error("LEAK: {}.release() was not called before it\'s garbage-collected.{}", this.resourceType, var4);
                  }
               }
            }
         }
      } else {
         while(true) {
            ResourceLeakDetector.DefaultResourceLeak var2 = (ResourceLeakDetector.DefaultResourceLeak)this.refQueue.poll();
            if(var2 == null) {
               return;
            }

            var2.close();
         }
      }
   }

   static String newRecord(int var0) {
      StringBuilder var1 = new StringBuilder(4096);
      StackTraceElement[] var2 = (new Throwable()).getStackTrace();
      StackTraceElement[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         StackTraceElement var6 = var3[var5];
         if(var0 > 0) {
            --var0;
         } else {
            String var7 = var6.toString();
            boolean var8 = false;
            String[] var9 = STACK_TRACE_ELEMENT_EXCLUSIONS;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               String var12 = var9[var11];
               if(var7.startsWith(var12)) {
                  var8 = true;
                  break;
               }
            }

            if(!var8) {
               var1.append('\t');
               var1.append(var7);
               var1.append(StringUtil.NEWLINE);
            }
         }
      }

      return var1.toString();
   }

   // $FF: synthetic method
   static long access$408(ResourceLeakDetector var0) {
      return (long)(var0.active++);
   }

   // $FF: synthetic method
   static long access$410(ResourceLeakDetector var0) {
      return (long)(var0.active--);
   }

   static {
      DEFAULT_LEVEL = ResourceLeakDetector.Level.SIMPLE;
      logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
      boolean var0;
      if(SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
         var0 = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
         logger.debug("-Dio.netty.noResourceLeakDetection: {}", (Object)Boolean.valueOf(var0));
         logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use \'-D{}={}\' instead.", "io.netty.leakDetectionLevel", DEFAULT_LEVEL.name().toLowerCase());
      } else {
         var0 = false;
      }

      ResourceLeakDetector.Level var1 = var0?ResourceLeakDetector.Level.DISABLED:DEFAULT_LEVEL;
      String var2 = SystemPropertyUtil.get("io.netty.leakDetectionLevel", var1.name()).trim().toUpperCase();
      ResourceLeakDetector.Level var3 = DEFAULT_LEVEL;
      Iterator var4 = EnumSet.allOf(ResourceLeakDetector.Level.class).iterator();

      while(true) {
         ResourceLeakDetector.Level var5;
         do {
            if(!var4.hasNext()) {
               level = var3;
               if(logger.isDebugEnabled()) {
                  logger.debug("-D{}: {}", "io.netty.leakDetectionLevel", var3.name().toLowerCase());
               }

               STACK_TRACE_ELEMENT_EXCLUSIONS = new String[]{"io.netty.buffer.AbstractByteBufAllocator.toLeakAwareBuffer("};
               return;
            }

            var5 = (ResourceLeakDetector.Level)var4.next();
         } while(!var2.equals(var5.name()) && !var2.equals(String.valueOf(var5.ordinal())));

         var3 = var5;
      }
   }

   private final class DefaultResourceLeak extends PhantomReference<Object> implements ResourceLeak {
      private static final int MAX_RECORDS = 4;
      private final String creationRecord;
      private final Deque<String> lastRecords = new ArrayDeque();
      private final AtomicBoolean freed;
      private ResourceLeakDetector<T>.DefaultResourceLeak prev;
      private ResourceLeakDetector<T>.DefaultResourceLeak next;

      DefaultResourceLeak(Object var2) {
         super(var2, var2 != null?ResourceLeakDetector.this.refQueue:null);
         if(var2 != null) {
            ResourceLeakDetector.Level var3 = ResourceLeakDetector.level;
            if(var3.ordinal() >= ResourceLeakDetector.Level.ADVANCED.ordinal()) {
               this.creationRecord = ResourceLeakDetector.newRecord(3);
            } else {
               this.creationRecord = null;
            }

            synchronized(ResourceLeakDetector.this.head) {
               this.prev = ResourceLeakDetector.this.head;
               this.next = ResourceLeakDetector.this.head.next;
               ResourceLeakDetector.this.head.next.prev = this;
               ResourceLeakDetector.this.head.next = this;
               ResourceLeakDetector.access$408(ResourceLeakDetector.this);
            }

            this.freed = new AtomicBoolean();
         } else {
            this.creationRecord = null;
            this.freed = new AtomicBoolean(true);
         }

      }

      public void record() {
         if(this.creationRecord != null) {
            String var1 = ResourceLeakDetector.newRecord(2);
            Deque var2 = this.lastRecords;
            synchronized(this.lastRecords) {
               int var3 = this.lastRecords.size();
               if(var3 == 0 || !((String)this.lastRecords.getLast()).equals(var1)) {
                  this.lastRecords.add(var1);
               }

               if(var3 > 4) {
                  this.lastRecords.removeFirst();
               }
            }
         }

      }

      public boolean close() {
         if(this.freed.compareAndSet(false, true)) {
            synchronized(ResourceLeakDetector.this.head) {
               ResourceLeakDetector.access$410(ResourceLeakDetector.this);
               this.prev.next = this.next;
               this.next.prev = this.prev;
               this.prev = null;
               this.next = null;
               return true;
            }
         } else {
            return false;
         }
      }

      public String toString() {
         if(this.creationRecord == null) {
            return "";
         } else {
            Deque var2 = this.lastRecords;
            Object[] var1;
            synchronized(this.lastRecords) {
               var1 = this.lastRecords.toArray();
            }

            StringBuilder var5 = new StringBuilder(16384);
            var5.append(StringUtil.NEWLINE);
            var5.append("Recent access records: ");
            var5.append(var1.length);
            var5.append(StringUtil.NEWLINE);
            if(var1.length > 0) {
               for(int var3 = var1.length - 1; var3 >= 0; --var3) {
                  var5.append('#');
                  var5.append(var3 + 1);
                  var5.append(':');
                  var5.append(StringUtil.NEWLINE);
                  var5.append(var1[var3]);
               }
            }

            var5.append("Created at:");
            var5.append(StringUtil.NEWLINE);
            var5.append(this.creationRecord);
            var5.setLength(var5.length() - StringUtil.NEWLINE.length());
            return var5.toString();
         }
      }
   }

   public static enum Level {
      DISABLED,
      SIMPLE,
      ADVANCED,
      PARANOID;

      private Level() {
      }
   }
}
