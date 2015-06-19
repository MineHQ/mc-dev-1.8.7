package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLogger;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

public class Slf4JLoggerFactory extends InternalLoggerFactory {
   public Slf4JLoggerFactory() {
   }

   Slf4JLoggerFactory(boolean var1) {
      assert var1;

      final StringBuffer var2 = new StringBuffer();
      PrintStream var3 = System.err;

      try {
         System.setErr(new PrintStream(new OutputStream() {
            public void write(int var1) {
               var2.append((char)var1);
            }
         }, true, "US-ASCII"));
      } catch (UnsupportedEncodingException var8) {
         throw new Error(var8);
      }

      try {
         if(LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new NoClassDefFoundError(var2.toString());
         }

         var3.print(var2);
         var3.flush();
      } finally {
         System.setErr(var3);
      }

   }

   public InternalLogger newInstance(String var1) {
      return new Slf4JLogger(LoggerFactory.getLogger(var1));
   }
}
