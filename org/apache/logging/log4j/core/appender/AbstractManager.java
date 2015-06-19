package org.apache.logging.log4j.core.appender;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractManager {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private static final Map<String, AbstractManager> MAP = new HashMap();
   private static final Lock LOCK = new ReentrantLock();
   protected int count;
   private final String name;

   protected AbstractManager(String var1) {
      this.name = var1;
      LOGGER.debug("Starting {} {}", new Object[]{this.getClass().getSimpleName(), var1});
   }

   public static <M extends AbstractManager, T> M getManager(String var0, ManagerFactory<M, T> var1, T var2) {
      LOCK.lock();

      AbstractManager var4;
      try {
         AbstractManager var3 = (AbstractManager)MAP.get(var0);
         if(var3 == null) {
            var3 = (AbstractManager)var1.createManager(var0, var2);
            if(var3 == null) {
               throw new IllegalStateException("Unable to create a manager");
            }

            MAP.put(var0, var3);
         }

         ++var3.count;
         var4 = var3;
      } finally {
         LOCK.unlock();
      }

      return var4;
   }

   public static boolean hasManager(String var0) {
      LOCK.lock();

      boolean var1;
      try {
         var1 = MAP.containsKey(var0);
      } finally {
         LOCK.unlock();
      }

      return var1;
   }

   protected void releaseSub() {
   }

   protected int getCount() {
      return this.count;
   }

   public void release() {
      LOCK.lock();

      try {
         --this.count;
         if(this.count <= 0) {
            MAP.remove(this.name);
            LOGGER.debug("Shutting down {} {}", new Object[]{this.getClass().getSimpleName(), this.getName()});
            this.releaseSub();
         }
      } finally {
         LOCK.unlock();
      }

   }

   public String getName() {
      return this.name;
   }

   public Map<String, String> getContentFormat() {
      return new HashMap();
   }
}
