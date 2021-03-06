package org.apache.logging.log4j.core.appender;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.layout.RFC5424Layout;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.util.EnglishEnums;

@Plugin(
   name = "Syslog",
   category = "Core",
   elementType = "appender",
   printObject = true
)
public class SyslogAppender extends SocketAppender {
   protected static final String RFC5424 = "RFC5424";

   protected SyslogAppender(String var1, Layout<? extends Serializable> var2, Filter var3, boolean var4, boolean var5, AbstractSocketManager var6, Advertiser var7) {
      super(var1, var2, var3, var6, var4, var5, var7);
   }

   @PluginFactory
   public static SyslogAppender createAppender(@PluginAttribute("host") String var0, @PluginAttribute("port") String var1, @PluginAttribute("protocol") String var2, @PluginAttribute("reconnectionDelay") String var3, @PluginAttribute("immediateFail") String var4, @PluginAttribute("name") String var5, @PluginAttribute("immediateFlush") String var6, @PluginAttribute("ignoreExceptions") String var7, @PluginAttribute("facility") String var8, @PluginAttribute("id") String var9, @PluginAttribute("enterpriseNumber") String var10, @PluginAttribute("includeMDC") String var11, @PluginAttribute("mdcId") String var12, @PluginAttribute("mdcPrefix") String var13, @PluginAttribute("eventPrefix") String var14, @PluginAttribute("newLine") String var15, @PluginAttribute("newLineEscape") String var16, @PluginAttribute("appName") String var17, @PluginAttribute("messageId") String var18, @PluginAttribute("mdcExcludes") String var19, @PluginAttribute("mdcIncludes") String var20, @PluginAttribute("mdcRequired") String var21, @PluginAttribute("format") String var22, @PluginElement("Filters") Filter var23, @PluginConfiguration Configuration var24, @PluginAttribute("charset") String var25, @PluginAttribute("exceptionPattern") String var26, @PluginElement("LoggerFields") LoggerFields[] var27, @PluginAttribute("advertise") String var28) {
      boolean var29 = Booleans.parseBoolean(var6, true);
      boolean var30 = Booleans.parseBoolean(var7, true);
      int var31 = AbstractAppender.parseInt(var3, 0);
      boolean var32 = Booleans.parseBoolean(var4, true);
      int var33 = AbstractAppender.parseInt(var1, 0);
      boolean var34 = Boolean.parseBoolean(var28);
      Object var35 = "RFC5424".equalsIgnoreCase(var22)?RFC5424Layout.createLayout(var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20, var21, var26, "false", var27, var24):SyslogLayout.createLayout(var8, var15, var16, var25);
      if(var5 == null) {
         LOGGER.error("No name provided for SyslogAppender");
         return null;
      } else {
         Protocol var36 = (Protocol)EnglishEnums.valueOf(Protocol.class, var2);
         AbstractSocketManager var37 = createSocketManager(var36, var0, var33, var31, var32, (Layout)var35);
         return var37 == null?null:new SyslogAppender(var5, (Layout)var35, var23, var30, var29, var37, var34?var24.getAdvertiser():null);
      }
   }
}
