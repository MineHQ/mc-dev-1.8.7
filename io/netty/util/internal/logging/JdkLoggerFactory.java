package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.JdkLogger;
import java.util.logging.Logger;

public class JdkLoggerFactory extends InternalLoggerFactory {
   public JdkLoggerFactory() {
   }

   public InternalLogger newInstance(String var1) {
      return new JdkLogger(Logger.getLogger(var1));
   }
}
