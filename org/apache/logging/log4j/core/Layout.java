package org.apache.logging.log4j.core;

import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;

public interface Layout<T extends Serializable> {
   byte[] getFooter();

   byte[] getHeader();

   byte[] toByteArray(LogEvent var1);

   T toSerializable(LogEvent var1);

   String getContentType();

   Map<String, String> getContentFormat();
}
