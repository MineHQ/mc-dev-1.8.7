package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.DatagramOutputStream;

public class DatagramSocketManager extends AbstractSocketManager {
   private static final DatagramSocketManager.DatagramSocketManagerFactory FACTORY = new DatagramSocketManager.DatagramSocketManagerFactory();

   protected DatagramSocketManager(String var1, OutputStream var2, InetAddress var3, String var4, int var5, Layout<? extends Serializable> var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public static DatagramSocketManager getSocketManager(String var0, int var1, Layout<? extends Serializable> var2) {
      if(Strings.isEmpty(var0)) {
         throw new IllegalArgumentException("A host name is required");
      } else if(var1 <= 0) {
         throw new IllegalArgumentException("A port value is required");
      } else {
         return (DatagramSocketManager)getManager("UDP:" + var0 + ":" + var1, new DatagramSocketManager.FactoryData(var0, var1, var2), FACTORY);
      }
   }

   public Map<String, String> getContentFormat() {
      HashMap var1 = new HashMap(super.getContentFormat());
      var1.put("protocol", "udp");
      var1.put("direction", "out");
      return var1;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class DatagramSocketManagerFactory implements ManagerFactory<DatagramSocketManager, DatagramSocketManager.FactoryData> {
      private DatagramSocketManagerFactory() {
      }

      public DatagramSocketManager createManager(String var1, DatagramSocketManager.FactoryData var2) {
         DatagramOutputStream var4 = new DatagramOutputStream(var2.host, var2.port, var2.layout.getHeader(), var2.layout.getFooter());

         InetAddress var3;
         try {
            var3 = InetAddress.getByName(var2.host);
         } catch (UnknownHostException var6) {
            DatagramSocketManager.LOGGER.error((String)("Could not find address of " + var2.host), (Throwable)var6);
            return null;
         }

         return new DatagramSocketManager(var1, var4, var3, var2.host, var2.port, var2.layout);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (DatagramSocketManager.FactoryData)var2);
      }

      // $FF: synthetic method
      DatagramSocketManagerFactory(DatagramSocketManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class FactoryData {
      private final String host;
      private final int port;
      private final Layout<? extends Serializable> layout;

      public FactoryData(String var1, int var2, Layout<? extends Serializable> var3) {
         this.host = var1;
         this.port = var2;
         this.layout = var3;
      }
   }
}
