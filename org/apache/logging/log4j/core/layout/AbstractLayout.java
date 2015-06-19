package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLayout<T extends Serializable> implements Layout<T> {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   protected byte[] header;
   protected byte[] footer;

   public AbstractLayout() {
   }

   public byte[] getHeader() {
      return this.header;
   }

   public void setHeader(byte[] var1) {
      this.header = var1;
   }

   public byte[] getFooter() {
      return this.footer;
   }

   public void setFooter(byte[] var1) {
      this.footer = var1;
   }
}
