package org.apache.logging.log4j.util;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.Provider;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class ProviderUtil {
   private static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
   private static final String API_VERSION = "Log4jAPIVersion";
   private static final String[] COMPATIBLE_API_VERSIONS = new String[]{"2.0.0"};
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final List<Provider> PROVIDERS = new ArrayList();

   private ProviderUtil() {
   }

   public static Iterator<Provider> getProviders() {
      return PROVIDERS.iterator();
   }

   public static boolean hasProviders() {
      return PROVIDERS.size() > 0;
   }

   public static ClassLoader findClassLoader() {
      ClassLoader var0;
      if(System.getSecurityManager() == null) {
         var0 = Thread.currentThread().getContextClassLoader();
      } else {
         var0 = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
            public ClassLoader run() {
               return Thread.currentThread().getContextClassLoader();
            }

            // $FF: synthetic method
            // $FF: bridge method
            public Object run() {
               return this.run();
            }
         });
      }

      if(var0 == null) {
         var0 = ProviderUtil.class.getClassLoader();
      }

      return var0;
   }

   private static boolean validVersion(String var0) {
      String[] var1 = COMPATIBLE_API_VERSIONS;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         if(var0.startsWith(var4)) {
            return true;
         }
      }

      return false;
   }

   static {
      ClassLoader var0 = findClassLoader();
      Enumeration var1 = null;

      try {
         var1 = var0.getResources("META-INF/log4j-provider.properties");
      } catch (IOException var6) {
         LOGGER.fatal((String)"Unable to locate META-INF/log4j-provider.properties", (Throwable)var6);
      }

      if(var1 != null) {
         while(var1.hasMoreElements()) {
            URL var2 = (URL)var1.nextElement();

            try {
               Properties var3 = PropertiesUtil.loadClose(var2.openStream(), var2);
               if(validVersion(var3.getProperty("Log4jAPIVersion"))) {
                  PROVIDERS.add(new Provider(var3, var2));
               }
            } catch (IOException var5) {
               LOGGER.error((String)("Unable to open " + var2.toString()), (Throwable)var5);
            }
         }
      }

   }
}
