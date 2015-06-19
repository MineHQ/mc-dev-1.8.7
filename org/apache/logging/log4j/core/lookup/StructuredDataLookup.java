package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.message.StructuredDataMessage;

@Plugin(
   name = "sd",
   category = "Lookup"
)
public class StructuredDataLookup implements StrLookup {
   public StructuredDataLookup() {
   }

   public String lookup(String var1) {
      return null;
   }

   public String lookup(LogEvent var1, String var2) {
      if(var1 != null && var1.getMessage() instanceof StructuredDataMessage) {
         StructuredDataMessage var3 = (StructuredDataMessage)var1.getMessage();
         return var2.equalsIgnoreCase("id")?var3.getId().getName():(var2.equalsIgnoreCase("type")?var3.getType():var3.get(var2));
      } else {
         return null;
      }
   }
}
