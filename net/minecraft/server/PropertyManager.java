package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyManager {
   private static final Logger a = LogManager.getLogger();
   public final Properties properties = new Properties();
   private final File file;

   public PropertyManager(File var1) {
      this.file = var1;
      if(var1.exists()) {
         FileInputStream var2 = null;

         try {
            var2 = new FileInputStream(var1);
            this.properties.load(var2);
         } catch (Exception var12) {
            a.warn((String)("Failed to load " + var1), (Throwable)var12);
            this.a();
         } finally {
            if(var2 != null) {
               try {
                  var2.close();
               } catch (IOException var11) {
                  ;
               }
            }

         }
      } else {
         a.warn(var1 + " does not exist");
         this.a();
      }

   }

   public void a() {
      a.info("Generating new properties file");
      this.savePropertiesFile();
   }

   public void savePropertiesFile() {
      FileOutputStream var1 = null;

      try {
         var1 = new FileOutputStream(this.file);
         this.properties.store(var1, "Minecraft server properties");
      } catch (Exception var11) {
         a.warn((String)("Failed to save " + this.file), (Throwable)var11);
         this.a();
      } finally {
         if(var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
               ;
            }
         }

      }

   }

   public File c() {
      return this.file;
   }

   public String getString(String var1, String var2) {
      if(!this.properties.containsKey(var1)) {
         this.properties.setProperty(var1, var2);
         this.savePropertiesFile();
         this.savePropertiesFile();
      }

      return this.properties.getProperty(var1, var2);
   }

   public int getInt(String var1, int var2) {
      try {
         return Integer.parseInt(this.getString(var1, "" + var2));
      } catch (Exception var4) {
         this.properties.setProperty(var1, "" + var2);
         this.savePropertiesFile();
         return var2;
      }
   }

   public long getLong(String var1, long var2) {
      try {
         return Long.parseLong(this.getString(var1, "" + var2));
      } catch (Exception var5) {
         this.properties.setProperty(var1, "" + var2);
         this.savePropertiesFile();
         return var2;
      }
   }

   public boolean getBoolean(String var1, boolean var2) {
      try {
         return Boolean.parseBoolean(this.getString(var1, "" + var2));
      } catch (Exception var4) {
         this.properties.setProperty(var1, "" + var2);
         this.savePropertiesFile();
         return var2;
      }
   }

   public void setProperty(String var1, Object var2) {
      this.properties.setProperty(var1, "" + var2);
   }
}
