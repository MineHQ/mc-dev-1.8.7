package org.apache.logging.log4j.core.appender.rolling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.PatternProcessor;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public class RollingFileManager extends FileManager {
   private static RollingFileManager.RollingFileManagerFactory factory = new RollingFileManager.RollingFileManagerFactory();
   private long size;
   private long initialTime;
   private final PatternProcessor patternProcessor;
   private final Semaphore semaphore = new Semaphore(1);
   private final TriggeringPolicy policy;
   private final RolloverStrategy strategy;

   protected RollingFileManager(String var1, String var2, OutputStream var3, boolean var4, long var5, long var7, TriggeringPolicy var9, RolloverStrategy var10, String var11, Layout<? extends Serializable> var12) {
      super(var1, var3, var4, false, var11, var12);
      this.size = var5;
      this.initialTime = var7;
      this.policy = var9;
      this.strategy = var10;
      this.patternProcessor = new PatternProcessor(var2);
      var9.initialize(this);
   }

   public static RollingFileManager getFileManager(String var0, String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout<? extends Serializable> var7) {
      return (RollingFileManager)getManager(var0, new RollingFileManager.FactoryData(var1, var2, var3, var4, var5, var6, var7), factory);
   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      this.size += (long)var3;
      super.write(var1, var2, var3);
   }

   public long getFileSize() {
      return this.size;
   }

   public long getFileTime() {
      return this.initialTime;
   }

   public synchronized void checkRollover(LogEvent var1) {
      if(this.policy.isTriggeringEvent(var1) && this.rollover(this.strategy)) {
         try {
            this.size = 0L;
            this.initialTime = System.currentTimeMillis();
            this.createFileAfterRollover();
         } catch (IOException var3) {
            LOGGER.error("FileManager (" + this.getFileName() + ") " + var3);
         }
      }

   }

   protected void createFileAfterRollover() throws IOException {
      FileOutputStream var1 = new FileOutputStream(this.getFileName(), this.isAppend());
      this.setOutputStream(var1);
   }

   public PatternProcessor getPatternProcessor() {
      return this.patternProcessor;
   }

   private boolean rollover(RolloverStrategy var1) {
      try {
         this.semaphore.acquire();
      } catch (InterruptedException var11) {
         LOGGER.error((String)"Thread interrupted while attempting to check rollover", (Throwable)var11);
         return false;
      }

      boolean var2 = false;
      Thread var3 = null;

      boolean var5;
      try {
         RolloverDescription var4 = var1.rollover(this);
         if(var4 != null) {
            this.close();
            if(var4.getSynchronous() != null) {
               try {
                  var2 = var4.getSynchronous().execute();
               } catch (Exception var10) {
                  LOGGER.error((String)"Error in synchronous task", (Throwable)var10);
               }
            }

            if(var2 && var4.getAsynchronous() != null) {
               var3 = new Thread(new RollingFileManager.AsyncAction(var4.getAsynchronous(), this));
               var3.start();
            }

            var5 = true;
            return var5;
         }

         var5 = false;
      } finally {
         if(var3 == null) {
            this.semaphore.release();
         }

      }

      return var5;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class RollingFileManagerFactory implements ManagerFactory<RollingFileManager, RollingFileManager.FactoryData> {
      private RollingFileManagerFactory() {
      }

      public RollingFileManager createManager(String var1, RollingFileManager.FactoryData var2) {
         File var3 = new File(var1);
         File var4 = var3.getParentFile();
         if(null != var4 && !var4.exists()) {
            var4.mkdirs();
         }

         try {
            var3.createNewFile();
         } catch (IOException var12) {
            RollingFileManager.LOGGER.error((String)("Unable to create file " + var1), (Throwable)var12);
            return null;
         }

         long var5 = var2.append?var3.length():0L;
         long var7 = var3.lastModified();

         try {
            Object var9 = new FileOutputStream(var1, var2.append);
            if(var2.bufferedIO) {
               var9 = new BufferedOutputStream((OutputStream)var9);
            }

            return new RollingFileManager(var1, var2.pattern, (OutputStream)var9, var2.append, var5, var7, var2.policy, var2.strategy, var2.advertiseURI, var2.layout);
         } catch (FileNotFoundException var11) {
            RollingFileManager.LOGGER.error("FileManager (" + var1 + ") " + var11);
            return null;
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (RollingFileManager.FactoryData)var2);
      }

      // $FF: synthetic method
      RollingFileManagerFactory(RollingFileManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class FactoryData {
      private final String pattern;
      private final boolean append;
      private final boolean bufferedIO;
      private final TriggeringPolicy policy;
      private final RolloverStrategy strategy;
      private final String advertiseURI;
      private final Layout<? extends Serializable> layout;

      public FactoryData(String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout<? extends Serializable> var7) {
         this.pattern = var1;
         this.append = var2;
         this.bufferedIO = var3;
         this.policy = var4;
         this.strategy = var5;
         this.advertiseURI = var6;
         this.layout = var7;
      }
   }

   private static class AsyncAction extends AbstractAction {
      private final Action action;
      private final RollingFileManager manager;

      public AsyncAction(Action var1, RollingFileManager var2) {
         this.action = var1;
         this.manager = var2;
      }

      public boolean execute() throws IOException {
         boolean var1;
         try {
            var1 = this.action.execute();
         } finally {
            this.manager.semaphore.release();
         }

         return var1;
      }

      public void close() {
         this.action.close();
      }

      public boolean isComplete() {
         return this.action.isComplete();
      }
   }
}
