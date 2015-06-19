package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(
   name = "env",
   category = "Lookup"
)
public class EnvironmentLookup implements StrLookup {
   public EnvironmentLookup() {
   }

   public String lookup(String var1) {
      return System.getenv(var1);
   }

   public String lookup(LogEvent var1, String var2) {
      return System.getenv(var2);
   }
}
