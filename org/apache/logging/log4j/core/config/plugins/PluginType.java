package org.apache.logging.log4j.core.config.plugins;

import java.io.Serializable;

public class PluginType<T> implements Serializable {
   private static final long serialVersionUID = 4743255148794846612L;
   private final Class<T> pluginClass;
   private final String elementName;
   private final boolean printObject;
   private final boolean deferChildren;

   public PluginType(Class<T> var1, String var2, boolean var3, boolean var4) {
      this.pluginClass = var1;
      this.elementName = var2;
      this.printObject = var3;
      this.deferChildren = var4;
   }

   public Class<T> getPluginClass() {
      return this.pluginClass;
   }

   public String getElementName() {
      return this.elementName;
   }

   public boolean isObjectPrintable() {
      return this.printObject;
   }

   public boolean isDeferChildren() {
      return this.deferChildren;
   }
}
