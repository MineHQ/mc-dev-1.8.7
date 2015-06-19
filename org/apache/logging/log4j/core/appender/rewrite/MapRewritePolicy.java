package org.apache.logging.log4j.core.appender.rewrite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "MapRewritePolicy",
   category = "Core",
   elementType = "rewritePolicy",
   printObject = true
)
public final class MapRewritePolicy implements RewritePolicy {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   private final Map<String, String> map;
   private final MapRewritePolicy.Mode mode;

   private MapRewritePolicy(Map<String, String> var1, MapRewritePolicy.Mode var2) {
      this.map = var1;
      this.mode = var2;
   }

   public LogEvent rewrite(LogEvent var1) {
      Message var2 = var1.getMessage();
      if(var2 != null && var2 instanceof MapMessage) {
         HashMap var3 = new HashMap(((MapMessage)var2).getData());
         switch(MapRewritePolicy.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode[this.mode.ordinal()]) {
         case 1:
            var3.putAll(this.map);
            break;
         default:
            Iterator var4 = this.map.entrySet().iterator();

            while(var4.hasNext()) {
               Entry var5 = (Entry)var4.next();
               if(var3.containsKey(var5.getKey())) {
                  var3.put(var5.getKey(), var5.getValue());
               }
            }
         }

         MapMessage var6 = ((MapMessage)var2).newInstance(var3);
         if(var1 instanceof Log4jLogEvent) {
            Log4jLogEvent var7 = (Log4jLogEvent)var1;
            return Log4jLogEvent.createEvent(var7.getLoggerName(), var7.getMarker(), var7.getFQCN(), var7.getLevel(), var6, var7.getThrownProxy(), var7.getContextMap(), var7.getContextStack(), var7.getThreadName(), var7.getSource(), var7.getMillis());
         } else {
            return new Log4jLogEvent(var1.getLoggerName(), var1.getMarker(), var1.getFQCN(), var1.getLevel(), var6, var1.getThrown(), var1.getContextMap(), var1.getContextStack(), var1.getThreadName(), var1.getSource(), var1.getMillis());
         }
      } else {
         return var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("mode=").append(this.mode);
      var1.append(" {");
      boolean var2 = true;

      for(Iterator var3 = this.map.entrySet().iterator(); var3.hasNext(); var2 = false) {
         Entry var4 = (Entry)var3.next();
         if(!var2) {
            var1.append(", ");
         }

         var1.append((String)var4.getKey()).append("=").append((String)var4.getValue());
      }

      var1.append("}");
      return var1.toString();
   }

   @PluginFactory
   public static MapRewritePolicy createPolicy(@PluginAttribute("mode") String var0, @PluginElement("KeyValuePair") KeyValuePair[] var1) {
      MapRewritePolicy.Mode var2;
      if(var0 == null) {
         var2 = MapRewritePolicy.Mode.Add;
      } else {
         var2 = MapRewritePolicy.Mode.valueOf(var0);
         if(var2 == null) {
            LOGGER.error("Undefined mode " + var0);
            return null;
         }
      }

      if(var1 != null && var1.length != 0) {
         HashMap var3 = new HashMap();
         KeyValuePair[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            KeyValuePair var7 = var4[var6];
            String var8 = var7.getKey();
            if(var8 == null) {
               LOGGER.error("A null key is not valid in MapRewritePolicy");
            } else {
               String var9 = var7.getValue();
               if(var9 == null) {
                  LOGGER.error("A null value for key " + var8 + " is not allowed in MapRewritePolicy");
               } else {
                  var3.put(var7.getKey(), var7.getValue());
               }
            }
         }

         if(var3.size() == 0) {
            LOGGER.error("MapRewritePolicy is not configured with any valid key value pairs");
            return null;
         } else {
            return new MapRewritePolicy(var3, var2);
         }
      } else {
         LOGGER.error("keys and values must be specified for the MapRewritePolicy");
         return null;
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode = new int[MapRewritePolicy.Mode.values().length];

      static {
         try {
            $SwitchMap$org$apache$logging$log4j$core$appender$rewrite$MapRewritePolicy$Mode[MapRewritePolicy.Mode.Add.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Mode {
      Add,
      Update;

      private Mode() {
      }
   }
}
