package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.AbstractSocketManager;

public class TCPSocketManager extends AbstractSocketManager {
   public static final int DEFAULT_RECONNECTION_DELAY = 30000;
   private static final int DEFAULT_PORT = 4560;
   private static final TCPSocketManager.TCPSocketManagerFactory FACTORY = new TCPSocketManager.TCPSocketManagerFactory();
   private final int reconnectionDelay;
   private TCPSocketManager.Reconnector connector = null;
   private Socket socket;
   private final boolean retry;
   private final boolean immediateFail;

   public TCPSocketManager(String var1, OutputStream var2, Socket var3, InetAddress var4, String var5, int var6, int var7, boolean var8, Layout<? extends Serializable> var9) {
      super(var1, var2, var4, var5, var6, var9);
      this.reconnectionDelay = var7;
      this.socket = var3;
      this.immediateFail = var8;
      this.retry = var7 > 0;
      if(var3 == null) {
         this.connector = new TCPSocketManager.Reconnector(this);
         this.connector.setDaemon(true);
         this.connector.setPriority(1);
         this.connector.start();
      }

   }

   public static TCPSocketManager getSocketManager(String var0, int var1, int var2, boolean var3, Layout<? extends Serializable> var4) {
      if(Strings.isEmpty(var0)) {
         throw new IllegalArgumentException("A host name is required");
      } else {
         if(var1 <= 0) {
            var1 = 4560;
         }

         if(var2 == 0) {
            var2 = 30000;
         }

         return (TCPSocketManager)getManager("TCP:" + var0 + ":" + var1, new TCPSocketManager.FactoryData(var0, var1, var2, var3, var4), FACTORY);
      }
   }

   protected void write(byte[] var1, int var2, int var3) {
      if(this.socket == null) {
         if(this.connector != null && !this.immediateFail) {
            this.connector.latch();
         }

         if(this.socket == null) {
            String var4 = "Error writing to " + this.getName() + " socket not available";
            throw new AppenderLoggingException(var4);
         }
      }

      synchronized(this) {
         try {
            this.getOutputStream().write(var1, var2, var3);
         } catch (IOException var8) {
            if(this.retry && this.connector == null) {
               this.connector = new TCPSocketManager.Reconnector(this);
               this.connector.setDaemon(true);
               this.connector.setPriority(1);
               this.connector.start();
            }

            String var6 = "Error writing to " + this.getName();
            throw new AppenderLoggingException(var6, var8);
         }

      }
   }

   protected synchronized void close() {
      super.close();
      if(this.connector != null) {
         this.connector.shutdown();
         this.connector.interrupt();
         this.connector = null;
      }

   }

   public Map<String, String> getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("protocol", "tcp");
      var1.put("direction", "out");
      return var1;
   }

   protected Socket createSocket(InetAddress var1, int var2) throws IOException {
      return this.createSocket(var1.getHostName(), var2);
   }

   protected Socket createSocket(String var1, int var2) throws IOException {
      return new Socket(var1, var2);
   }

   protected static class TCPSocketManagerFactory implements ManagerFactory<TCPSocketManager, TCPSocketManager.FactoryData> {
      protected TCPSocketManagerFactory() {
      }

      public TCPSocketManager createManager(String var1, TCPSocketManager.FactoryData var2) {
         InetAddress var3;
         try {
            var3 = InetAddress.getByName(var2.host);
         } catch (UnknownHostException var6) {
            TCPSocketManager.LOGGER.error((String)("Could not find address of " + var2.host), (Throwable)var6);
            return null;
         }

         try {
            Socket var5 = new Socket(var2.host, var2.port);
            OutputStream var8 = var5.getOutputStream();
            return new TCPSocketManager(var1, var8, var5, var3, var2.host, var2.port, var2.delay, var2.immediateFail, var2.layout);
         } catch (IOException var7) {
            TCPSocketManager.LOGGER.error("TCPSocketManager (" + var1 + ") " + var7);
            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            return var2.delay == 0?null:new TCPSocketManager(var1, var4, (Socket)null, var3, var2.host, var2.port, var2.delay, var2.immediateFail, var2.layout);
         }
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (TCPSocketManager.FactoryData)var2);
      }
   }

   private static class FactoryData {
      private final String host;
      private final int port;
      private final int delay;
      private final boolean immediateFail;
      private final Layout<? extends Serializable> layout;

      public FactoryData(String var1, int var2, int var3, boolean var4, Layout<? extends Serializable> var5) {
         this.host = var1;
         this.port = var2;
         this.delay = var3;
         this.immediateFail = var4;
         this.layout = var5;
      }
   }

   private class Reconnector extends Thread {
      private final CountDownLatch latch = new CountDownLatch(1);
      private boolean shutdown = false;
      private final Object owner;

      public Reconnector(OutputStreamManager var2) {
         this.owner = var2;
      }

      public void latch() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
            ;
         }

      }

      public void shutdown() {
         this.shutdown = true;
      }

      public void run() {
         while(!this.shutdown) {
            try {
               sleep((long)TCPSocketManager.this.reconnectionDelay);
               Socket var1 = TCPSocketManager.this.createSocket(TCPSocketManager.this.address, TCPSocketManager.this.port);
               OutputStream var2 = var1.getOutputStream();
               Object var3 = this.owner;
               synchronized(this.owner) {
                  try {
                     TCPSocketManager.this.getOutputStream().close();
                  } catch (IOException var13) {
                     ;
                  }

                  TCPSocketManager.this.setOutputStream(var2);
                  TCPSocketManager.this.socket = var1;
                  TCPSocketManager.this.connector = null;
                  this.shutdown = true;
               }

               TCPSocketManager.LOGGER.debug("Connection to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " reestablished.");
            } catch (InterruptedException var15) {
               TCPSocketManager.LOGGER.debug("Reconnection interrupted.");
            } catch (ConnectException var16) {
               TCPSocketManager.LOGGER.debug(TCPSocketManager.this.host + ":" + TCPSocketManager.this.port + " refused connection");
            } catch (IOException var17) {
               TCPSocketManager.LOGGER.debug("Unable to reconnect to " + TCPSocketManager.this.host + ":" + TCPSocketManager.this.port);
            } finally {
               this.latch.countDown();
            }
         }

      }
   }
}
