package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.XMLConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(
   name = "XMLConfigurationFactory",
   category = "ConfigurationFactory"
)
@Order(5)
public class XMLConfigurationFactory extends ConfigurationFactory {
   public static final String[] SUFFIXES = new String[]{".xml", "*"};

   public XMLConfigurationFactory() {
   }

   public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource var1) {
      return new XMLConfiguration(var1);
   }

   public String[] getSupportedTypes() {
      return SUFFIXES;
   }
}
