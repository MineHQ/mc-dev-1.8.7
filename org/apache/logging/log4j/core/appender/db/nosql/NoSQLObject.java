package org.apache.logging.log4j.core.appender.db.nosql;

public interface NoSQLObject<W> {
   void set(String var1, Object var2);

   void set(String var1, NoSQLObject<W> var2);

   void set(String var1, Object[] var2);

   void set(String var1, NoSQLObject<W>[] var2);

   W unwrap();
}
