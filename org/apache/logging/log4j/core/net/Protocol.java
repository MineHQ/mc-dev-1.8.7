package org.apache.logging.log4j.core.net;

public enum Protocol {
   TCP,
   UDP;

   private Protocol() {
   }

   public boolean isEqual(String var1) {
      return this.name().equalsIgnoreCase(var1);
   }
}
