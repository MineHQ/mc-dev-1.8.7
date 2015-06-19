package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.net.ssl.StoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.StoreConfigurationException;

@Plugin(
   name = "trustStore",
   category = "Core",
   printObject = true
)
public class TrustStoreConfiguration extends StoreConfiguration {
   private KeyStore trustStore = null;
   private String trustStoreType = "JKS";

   public TrustStoreConfiguration(String var1, String var2) {
      super(var1, var2);
   }

   protected void load() throws StoreConfigurationException {
      KeyStore var1 = null;
      FileInputStream var2 = null;
      LOGGER.debug("Loading truststore from file with params(location={})", new Object[]{this.getLocation()});

      try {
         if(this.getLocation() == null) {
            throw new IOException("The location is null");
         }

         var1 = KeyStore.getInstance(this.trustStoreType);
         var2 = new FileInputStream(this.getLocation());
         var1.load(var2, this.getPasswordAsCharArray());
      } catch (CertificateException var15) {
         LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[]{this.trustStoreType});
         throw new StoreConfigurationException(var15);
      } catch (NoSuchAlgorithmException var16) {
         LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
         throw new StoreConfigurationException(var16);
      } catch (KeyStoreException var17) {
         LOGGER.error(var17);
         throw new StoreConfigurationException(var17);
      } catch (FileNotFoundException var18) {
         LOGGER.error("The keystore file({}) is not found", new Object[]{this.getLocation()});
         throw new StoreConfigurationException(var18);
      } catch (IOException var19) {
         LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", new Object[]{var19.getMessage()});
         throw new StoreConfigurationException(var19);
      } finally {
         try {
            if(var2 != null) {
               var2.close();
            }
         } catch (Exception var14) {
            LOGGER.warn("Error closing {}", new Object[]{this.getLocation(), var14});
         }

      }

      this.trustStore = var1;
      LOGGER.debug("Truststore successfully loaded with params(location={})", new Object[]{this.getLocation()});
   }

   public KeyStore getTrustStore() throws StoreConfigurationException {
      if(this.trustStore == null) {
         this.load();
      }

      return this.trustStore;
   }

   @PluginFactory
   public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute("location") String var0, @PluginAttribute("password") String var1) {
      return new TrustStoreConfiguration(var0, var1);
   }
}
