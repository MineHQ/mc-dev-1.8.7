package org.apache.logging.log4j.core.appender.db.nosql;

import java.io.Closeable;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public interface NoSQLConnection<W, T extends NoSQLObject<W>> extends Closeable {
   T createObject();

   T[] createList(int var1);

   void insertObject(NoSQLObject<W> var1);

   void close();

   boolean isClosed();
}
