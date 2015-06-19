package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLogger;
import org.apache.log4j.Logger;

public class Log4JLoggerFactory extends InternalLoggerFactory {
   public Log4JLoggerFactory() {
   }

   public InternalLogger newInstance(String var1) {
      return new Log4JLogger(Logger.getLogger(var1));
   }
}
