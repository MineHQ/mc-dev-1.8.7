package org.apache.logging.log4j.core.layout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractLayout;

@Plugin(
   name = "SerializedLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public final class SerializedLayout extends AbstractLayout<LogEvent> {
   private static byte[] header;

   private SerializedLayout() {
   }

   public byte[] toByteArray(LogEvent var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();

      try {
         SerializedLayout.PrivateObjectOutputStream var3 = new SerializedLayout.PrivateObjectOutputStream(var2);

         try {
            var3.writeObject(var1);
            var3.reset();
         } finally {
            var3.close();
         }
      } catch (IOException var8) {
         LOGGER.error((String)"Serialization of LogEvent failed.", (Throwable)var8);
      }

      return var2.toByteArray();
   }

   public LogEvent toSerializable(LogEvent var1) {
      return var1;
   }

   @PluginFactory
   public static SerializedLayout createLayout() {
      return new SerializedLayout();
   }

   public byte[] getHeader() {
      return header;
   }

   public Map<String, String> getContentFormat() {
      return new HashMap();
   }

   public String getContentType() {
      return "application/octet-stream";
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }

   static {
      ByteArrayOutputStream var0 = new ByteArrayOutputStream();

      try {
         ObjectOutputStream var1 = new ObjectOutputStream(var0);
         var1.close();
         header = var0.toByteArray();
      } catch (Exception var2) {
         LOGGER.error((String)"Unable to generate Object stream header", (Throwable)var2);
      }

   }

   private class PrivateObjectOutputStream extends ObjectOutputStream {
      public PrivateObjectOutputStream(OutputStream var2) throws IOException {
         super(var2);
      }

      protected void writeStreamHeader() {
      }
   }
}
