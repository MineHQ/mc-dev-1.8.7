package net.minecraft.server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.Processor;

public class SystemUtils {
   private static String a;

   public static String a() {
      return a == null?"<unknown>":a;
   }

   public static <V> V a(FutureTask<V> var0, Logger var1) {
      try {
         var0.run();
         return var0.get();
      } catch (ExecutionException var3) {
         var1.fatal((String)"Error executing task", (Throwable)var3);
      } catch (InterruptedException var4) {
         var1.fatal((String)"Error executing task", (Throwable)var4);
      }

      return null;
   }

   static {
      try {
         Processor[] var0 = (new SystemInfo()).getHardware().getProcessors();
         a = String.format("%dx %s", new Object[]{Integer.valueOf(var0.length), var0[0]}).replaceAll("\\s+", " ");
      } catch (Exception var1) {
         ;
      }

   }
}
