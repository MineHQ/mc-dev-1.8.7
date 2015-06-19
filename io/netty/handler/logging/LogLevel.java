package io.netty.handler.logging;

import io.netty.util.internal.logging.InternalLogLevel;

public enum LogLevel {
   TRACE,
   DEBUG,
   INFO,
   WARN,
   ERROR;

   private final InternalLogLevel internalLevel;

   private LogLevel(InternalLogLevel var3) {
      this.internalLevel = var3;
   }

   InternalLogLevel toInternalLevel() {
      return this.internalLevel;
   }

   static {
      TRACE = new LogLevel("TRACE", 0, InternalLogLevel.TRACE);
      DEBUG = new LogLevel("DEBUG", 1, InternalLogLevel.DEBUG);
      INFO = new LogLevel("INFO", 2, InternalLogLevel.INFO);
      WARN = new LogLevel("WARN", 3, InternalLogLevel.WARN);
      ERROR = new LogLevel("ERROR", 4, InternalLogLevel.ERROR);
   }
}
