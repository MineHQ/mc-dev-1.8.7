package org.apache.logging.log4j.core.appender.db.jdbc;

import java.io.Closeable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.layout.PatternLayout;

public final class JDBCDatabaseManager extends AbstractDatabaseManager {
   private static final JDBCDatabaseManager.JDBCDatabaseManagerFactory FACTORY = new JDBCDatabaseManager.JDBCDatabaseManagerFactory();
   private final List<JDBCDatabaseManager.Column> columns;
   private final ConnectionSource connectionSource;
   private final String sqlStatement;
   private Connection connection;
   private PreparedStatement statement;

   private JDBCDatabaseManager(String var1, int var2, ConnectionSource var3, String var4, List<JDBCDatabaseManager.Column> var5) {
      super(var1, var2);
      this.connectionSource = var3;
      this.sqlStatement = var4;
      this.columns = var5;
   }

   protected void connectInternal() throws SQLException {
      this.connection = this.connectionSource.getConnection();
      this.statement = this.connection.prepareStatement(this.sqlStatement);
   }

   protected void disconnectInternal() throws SQLException {
      try {
         Closer.close((Statement)this.statement);
      } finally {
         Closer.close(this.connection);
      }

   }

   protected void writeInternal(LogEvent var1) {
      StringReader var2 = null;

      try {
         if(!this.isConnected() || this.connection == null || this.connection.isClosed()) {
            throw new AppenderLoggingException("Cannot write logging event; JDBC manager not connected to the database.");
         } else {
            int var3 = 1;
            Iterator var4 = this.columns.iterator();

            while(var4.hasNext()) {
               JDBCDatabaseManager.Column var5 = (JDBCDatabaseManager.Column)var4.next();
               if(var5.isEventTimestamp) {
                  this.statement.setTimestamp(var3++, new Timestamp(var1.getMillis()));
               } else if(var5.isClob) {
                  var2 = new StringReader(var5.layout.toSerializable(var1));
                  if(var5.isUnicode) {
                     this.statement.setNClob(var3++, var2);
                  } else {
                     this.statement.setClob(var3++, var2);
                  }
               } else if(var5.isUnicode) {
                  this.statement.setNString(var3++, var5.layout.toSerializable(var1));
               } else {
                  this.statement.setString(var3++, var5.layout.toSerializable(var1));
               }
            }

            if(this.statement.executeUpdate() == 0) {
               throw new AppenderLoggingException("No records inserted in database table for log event in JDBC manager.");
            }
         }
      } catch (SQLException var9) {
         throw new AppenderLoggingException("Failed to insert record for log event in JDBC manager: " + var9.getMessage(), var9);
      } finally {
         Closer.closeSilent((Closeable)var2);
      }
   }

   public static JDBCDatabaseManager getJDBCDatabaseManager(String var0, int var1, ConnectionSource var2, String var3, ColumnConfig[] var4) {
      return (JDBCDatabaseManager)AbstractDatabaseManager.getManager(var0, new JDBCDatabaseManager.FactoryData(var1, var2, var3, var4), FACTORY);
   }

   // $FF: synthetic method
   JDBCDatabaseManager(String var1, int var2, ConnectionSource var3, String var4, List var5, JDBCDatabaseManager.SyntheticClass_1 var6) {
      this(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static final class Column {
      private final PatternLayout layout;
      private final boolean isEventTimestamp;
      private final boolean isUnicode;
      private final boolean isClob;

      private Column(PatternLayout var1, boolean var2, boolean var3, boolean var4) {
         this.layout = var1;
         this.isEventTimestamp = var2;
         this.isUnicode = var3;
         this.isClob = var4;
      }

      // $FF: synthetic method
      Column(PatternLayout var1, boolean var2, boolean var3, boolean var4, JDBCDatabaseManager.SyntheticClass_1 var5) {
         this(var1, var2, var3, var4);
      }
   }

   private static final class JDBCDatabaseManagerFactory implements ManagerFactory<JDBCDatabaseManager, JDBCDatabaseManager.FactoryData> {
      private JDBCDatabaseManagerFactory() {
      }

      public JDBCDatabaseManager createManager(String var1, JDBCDatabaseManager.FactoryData var2) {
         StringBuilder var3 = new StringBuilder();
         StringBuilder var4 = new StringBuilder();
         ArrayList var5 = new ArrayList();
         int var6 = 0;
         ColumnConfig[] var7 = var2.columnConfigs;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            ColumnConfig var10 = var7[var9];
            if(var6++ > 0) {
               var3.append(',');
               var4.append(',');
            }

            var3.append(var10.getColumnName());
            if(var10.getLiteralValue() != null) {
               var4.append(var10.getLiteralValue());
            } else {
               var5.add(new JDBCDatabaseManager.Column(var10.getLayout(), var10.isEventTimestamp(), var10.isUnicode(), var10.isClob()));
               var4.append('?');
            }
         }

         String var11 = "INSERT INTO " + var2.tableName + " (" + var3 + ") VALUES (" + var4 + ")";
         return new JDBCDatabaseManager(var1, var2.getBufferSize(), var2.connectionSource, var11, var5);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JDBCDatabaseManager.FactoryData)var2);
      }

      // $FF: synthetic method
      JDBCDatabaseManagerFactory(JDBCDatabaseManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
      private final ColumnConfig[] columnConfigs;
      private final ConnectionSource connectionSource;
      private final String tableName;

      protected FactoryData(int var1, ConnectionSource var2, String var3, ColumnConfig[] var4) {
         super(var1);
         this.connectionSource = var2;
         this.tableName = var3;
         this.columnConfigs = var4;
      }
   }
}
