package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;

public class RollingRandomAccessFileManager extends RollingFileManager {
   static final int DEFAULT_BUFFER_SIZE = 262144;
   private static final RollingRandomAccessFileManager.RollingRandomAccessFileManagerFactory FACTORY = new RollingRandomAccessFileManager.RollingRandomAccessFileManagerFactory();
   private final boolean isImmediateFlush;
   private RandomAccessFile randomAccessFile;
   private final ByteBuffer buffer;
   private final ThreadLocal<Boolean> isEndOfBatch = new ThreadLocal();

   public RollingRandomAccessFileManager(RandomAccessFile var1, String var2, String var3, OutputStream var4, boolean var5, boolean var6, long var7, long var9, TriggeringPolicy var11, RolloverStrategy var12, String var13, Layout<? extends Serializable> var14) {
      super(var2, var3, var4, var5, var7, var9, var11, var12, var13, var14);
      this.isImmediateFlush = var6;
      this.randomAccessFile = var1;
      this.isEndOfBatch.set(Boolean.FALSE);
      this.buffer = ByteBuffer.allocate(262144);
   }

   public static RollingRandomAccessFileManager getRollingRandomAccessFileManager(String var0, String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout<? extends Serializable> var7) {
      return (RollingRandomAccessFileManager)getManager(var0, new RollingRandomAccessFileManager.FactoryData(var1, var2, var3, var4, var5, var6, var7), FACTORY);
   }

   public Boolean isEndOfBatch() {
      return (Boolean)this.isEndOfBatch.get();
   }

   public void setEndOfBatch(boolean var1) {
      this.isEndOfBatch.set(Boolean.valueOf(var1));
   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      super.write(var1, var2, var3);
      boolean var4 = false;

      do {
         if(var3 > this.buffer.remaining()) {
            this.flush();
         }

         int var5 = Math.min(var3, this.buffer.remaining());
         this.buffer.put(var1, var2, var5);
         var2 += var5;
         var3 -= var5;
      } while(var3 > 0);

      if(this.isImmediateFlush || this.isEndOfBatch.get() == Boolean.TRUE) {
         this.flush();
      }

   }

   protected void createFileAfterRollover() throws IOException {
      this.randomAccessFile = new RandomAccessFile(this.getFileName(), "rw");
      if(this.isAppend()) {
         this.randomAccessFile.seek(this.randomAccessFile.length());
      }

   }

   public synchronized void flush() {
      this.buffer.flip();

      try {
         this.randomAccessFile.write(this.buffer.array(), 0, this.buffer.limit());
      } catch (IOException var3) {
         String var2 = "Error writing to RandomAccessFile " + this.getName();
         throw new AppenderLoggingException(var2, var3);
      }

      this.buffer.clear();
   }

   public synchronized void close() {
      this.flush();

      try {
         this.randomAccessFile.close();
      } catch (IOException var2) {
         LOGGER.error("Unable to close RandomAccessFile " + this.getName() + ". " + var2);
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class FactoryData {
      private final String pattern;
      private final boolean append;
      private final boolean immediateFlush;
      private final TriggeringPolicy policy;
      private final RolloverStrategy strategy;
      private final String advertiseURI;
      private final Layout<? extends Serializable> layout;

      public FactoryData(String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout<? extends Serializable> var7) {
         this.pattern = var1;
         this.append = var2;
         this.immediateFlush = var3;
         this.policy = var4;
         this.strategy = var5;
         this.advertiseURI = var6;
         this.layout = var7;
      }
   }

   static class DummyOutputStream extends OutputStream {
      DummyOutputStream() {
      }

      public void write(int var1) throws IOException {
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
      }
   }

   private static class RollingRandomAccessFileManagerFactory implements ManagerFactory<RollingRandomAccessFileManager, RollingRandomAccessFileManager.FactoryData> {
      private RollingRandomAccessFileManagerFactory() {
      }

      public RollingRandomAccessFileManager createManager(String var1, RollingRandomAccessFileManager.FactoryData var2) {
         File var3 = new File(var1);
         File var4 = var3.getParentFile();
         if(null != var4 && !var4.exists()) {
            var4.mkdirs();
         }

         if(!var2.append) {
            var3.delete();
         }

         long var5 = var2.append?var3.length():0L;
         long var7 = var3.exists()?var3.lastModified():System.currentTimeMillis();
         RandomAccessFile var9 = null;

         try {
            var9 = new RandomAccessFile(var1, "rw");
            if(var2.append) {
               long var10 = var9.length();
               RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} seek to {}", new Object[]{var1, Long.valueOf(var10)});
               var9.seek(var10);
            } else {
               RollingRandomAccessFileManager.LOGGER.trace("RandomAccessFile {} set length to 0", new Object[]{var1});
               var9.setLength(0L);
            }

            return new RollingRandomAccessFileManager(var9, var1, var2.pattern, new RollingRandomAccessFileManager.DummyOutputStream(), var2.append, var2.immediateFlush, var5, var7, var2.policy, var2.strategy, var2.advertiseURI, var2.layout);
         } catch (IOException var13) {
            RollingRandomAccessFileManager.LOGGER.error("Cannot access RandomAccessFile {}) " + var13);
            if(var9 != null) {
               try {
                  var9.close();
               } catch (IOException var12) {
                  RollingRandomAccessFileManager.LOGGER.error("Cannot close RandomAccessFile {}", new Object[]{var1, var12});
               }
            }

            return null;
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (RollingRandomAccessFileManager.FactoryData)var2);
      }

      // $FF: synthetic method
      RollingRandomAccessFileManagerFactory(RollingRandomAccessFileManager.SyntheticClass_1 var1) {
         this();
      }
   }
}
