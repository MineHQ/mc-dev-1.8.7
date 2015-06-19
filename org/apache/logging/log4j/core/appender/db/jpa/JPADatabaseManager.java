package org.apache.logging.log4j.core.appender.db.jpa;

import java.lang.reflect.Constructor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.jpa.AbstractLogEventWrapperEntity;

public final class JPADatabaseManager extends AbstractDatabaseManager {
   private static final JPADatabaseManager.JPADatabaseManagerFactory FACTORY = new JPADatabaseManager.JPADatabaseManagerFactory();
   private final String entityClassName;
   private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
   private final String persistenceUnitName;
   private EntityManagerFactory entityManagerFactory;

   private JPADatabaseManager(String var1, int var2, Class<? extends AbstractLogEventWrapperEntity> var3, Constructor<? extends AbstractLogEventWrapperEntity> var4, String var5) {
      super(var1, var2);
      this.entityClassName = var3.getName();
      this.entityConstructor = var4;
      this.persistenceUnitName = var5;
   }

   protected void connectInternal() {
      this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
   }

   protected void disconnectInternal() {
      if(this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
         this.entityManagerFactory.close();
      }

   }

   protected void writeInternal(LogEvent var1) {
      if(this.isConnected() && this.entityManagerFactory != null) {
         AbstractLogEventWrapperEntity var2;
         try {
            var2 = (AbstractLogEventWrapperEntity)this.entityConstructor.newInstance(new Object[]{var1});
         } catch (Exception var10) {
            throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", var10);
         }

         EntityManager var3 = null;
         EntityTransaction var4 = null;

         try {
            var3 = this.entityManagerFactory.createEntityManager();
            var4 = var3.getTransaction();
            var4.begin();
            var3.persist(var2);
            var4.commit();
         } catch (Exception var11) {
            if(var4 != null && var4.isActive()) {
               var4.rollback();
            }

            throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + var11.getMessage(), var11);
         } finally {
            if(var3 != null && var3.isOpen()) {
               var3.close();
            }

         }

      } else {
         throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
      }
   }

   public static JPADatabaseManager getJPADatabaseManager(String var0, int var1, Class<? extends AbstractLogEventWrapperEntity> var2, Constructor<? extends AbstractLogEventWrapperEntity> var3, String var4) {
      return (JPADatabaseManager)AbstractDatabaseManager.getManager(var0, new JPADatabaseManager.FactoryData(var1, var2, var3, var4), FACTORY);
   }

   // $FF: synthetic method
   JPADatabaseManager(String var1, int var2, Class var3, Constructor var4, String var5, JPADatabaseManager.SyntheticClass_1 var6) {
      this(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class JPADatabaseManagerFactory implements ManagerFactory<JPADatabaseManager, JPADatabaseManager.FactoryData> {
      private JPADatabaseManagerFactory() {
      }

      public JPADatabaseManager createManager(String var1, JPADatabaseManager.FactoryData var2) {
         return new JPADatabaseManager(var1, var2.getBufferSize(), var2.entityClass, var2.entityConstructor, var2.persistenceUnitName);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JPADatabaseManager.FactoryData)var2);
      }

      // $FF: synthetic method
      JPADatabaseManagerFactory(JPADatabaseManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
      private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
      private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
      private final String persistenceUnitName;

      protected FactoryData(int var1, Class<? extends AbstractLogEventWrapperEntity> var2, Constructor<? extends AbstractLogEventWrapperEntity> var3, String var4) {
         super(var1);
         this.entityClass = var2;
         this.entityConstructor = var3;
         this.persistenceUnitName = var4;
      }
   }
}
