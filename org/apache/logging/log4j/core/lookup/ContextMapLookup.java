package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(
   name = "ctx",
   category = "Lookup"
)
public class ContextMapLookup implements StrLookup {
   public ContextMapLookup() {
   }

   public String lookup(String var1) {
      return ThreadContext.get(var1);
   }

   public String lookup(LogEvent var1, String var2) {
      return (String)var1.getContextMap().get(var2);
   }
}
