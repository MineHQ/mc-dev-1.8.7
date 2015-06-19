package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;

public class TLSSocketManager extends TCPSocketManager {
   public static final int DEFAULT_PORT = 6514;
   private static final TLSSocketManager.TLSSocketManagerFactory FACTORY = new TLSSocketManager.TLSSocketManagerFactory();
   private SSLConfiguration sslConfig;

   public TLSSocketManager(String var1, OutputStream var2, Socket var3, SSLConfiguration var4, InetAddress var5, String var6, int var7, int var8, boolean var9, Layout var10) {
      super(var1, var2, var3, var5, var6, var7, var8, var9, var10);
      this.sslConfig = var4;
   }

   public static TLSSocketManager getSocketManager(SSLConfiguration var0, String var1, int var2, int var3, boolean var4, Layout var5) {
      if(Strings.isEmpty(var1)) {
         throw new IllegalArgumentException("A host name is required");
      } else {
         if(var2 <= 0) {
            var2 = 6514;
         }

         if(var3 == 0) {
            var3 = 30000;
         }

         return (TLSSocketManager)getManager("TLS:" + var1 + ":" + var2, new TLSSocketManager.TLSFactoryData(var0, var1, var2, var3, var4, var5), FACTORY);
      }
   }

   protected Socket createSocket(String var1, int var2) throws IOException {
      SSLSocketFactory var3 = createSSLSocketFactory(this.sslConfig);
      return var3.createSocket(var1, var2);
   }

   private static SSLSocketFactory createSSLSocketFactory(SSLConfiguration var0) {
      SSLSocketFactory var1;
      if(var0 != null) {
         var1 = var0.getSSLSocketFactory();
      } else {
         var1 = (SSLSocketFactory)SSLSocketFactory.getDefault();
      }

      return var1;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class TLSSocketManagerFactory implements ManagerFactory<TLSSocketManager, TLSSocketManager.TLSFactoryData> {
      private TLSSocketManagerFactory() {
      }

      public TLSSocketManager createManager(String var1, TLSSocketManager.TLSFactoryData var2) {
         InetAddress var3 = null;
         Object var4 = null;
         Socket var5 = null;

         try {
            var3 = this.resolveAddress(var2.host);
            var5 = this.createSocket(var2);
            var4 = var5.getOutputStream();
            this.checkDelay(var2.delay, (OutputStream)var4);
         } catch (IOException var7) {
            TLSSocketManager.LOGGER.error("TLSSocketManager (" + var1 + ") " + var7);
            var4 = new ByteArrayOutputStream();
         } catch (TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException var8) {
            return null;
         }

         return this.createManager(var1, (OutputStream)var4, var5, var2.sslConfig, var3, var2.host, var2.port, var2.delay, var2.immediateFail, var2.layout);
      }

      private InetAddress resolveAddress(String var1) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException {
         try {
            InetAddress var2 = InetAddress.getByName(var1);
            return var2;
         } catch (UnknownHostException var4) {
            TLSSocketManager.LOGGER.error((String)("Could not find address of " + var1), (Throwable)var4);
            throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException();
         }
      }

      private void checkDelay(int var1, OutputStream var2) throws TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException {
         if(var1 == 0 && var2 == null) {
            throw new TLSSocketManager.TLSSocketManagerFactory.TLSSocketManagerFactory$TLSSocketManagerFactoryException();
         }
      }

      private Socket createSocket(TLSSocketManager.TLSFactoryData var1) throws IOException {
         SSLSocketFactory var2 = TLSSocketManager.createSSLSocketFactory(var1.sslConfig);
         SSLSocket var3 = (SSLSocket)var2.createSocket(var1.host, var1.port);
         return var3;
      }

      private TLSSocketManager createManager(String var1, OutputStream var2, Socket var3, SSLConfiguration var4, InetAddress var5, String var6, int var7, int var8, boolean var9, Layout var10) {
         return new TLSSocketManager(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (TLSSocketManager.TLSFactoryData)var2);
      }

      // $FF: synthetic method
      TLSSocketManagerFactory(TLSSocketManager.SyntheticClass_1 var1) {
         this();
      }

      private class TLSSocketManagerFactory$TLSSocketManagerFactoryException extends Exception {
         private TLSSocketManagerFactory$TLSSocketManagerFactoryException() {
         }

         // $FF: synthetic method
         TLSSocketManagerFactory$TLSSocketManagerFactoryException(TLSSocketManager.SyntheticClass_1 var2) {
            this();
         }
      }
   }

   private static class TLSFactoryData {
      protected SSLConfiguration sslConfig;
      private final String host;
      private final int port;
      private final int delay;
      private final boolean immediateFail;
      private final Layout layout;

      public TLSFactoryData(SSLConfiguration var1, String var2, int var3, int var4, boolean var5, Layout var6) {
         this.host = var2;
         this.port = var3;
         this.delay = var4;
         this.immediateFail = var5;
         this.layout = var6;
         this.sslConfig = var1;
      }
   }
}
