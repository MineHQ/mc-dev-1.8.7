package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Throwables;
import org.apache.logging.log4j.core.helpers.Transform;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MultiformatMessage;

@Plugin(
   name = "JSONLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public class JSONLayout extends AbstractStringLayout {
   private static final int DEFAULT_SIZE = 256;
   private static final String DEFAULT_EOL = "\r\n";
   private static final String COMPACT_EOL = "";
   private static final String DEFAULT_INDENT = "  ";
   private static final String COMPACT_INDENT = "";
   private static final String[] FORMATS = new String[]{"json"};
   private final boolean locationInfo;
   private final boolean properties;
   private final boolean complete;
   private final String eol;
   private final String indent1;
   private final String indent2;
   private final String indent3;
   private final String indent4;
   private volatile boolean firstLayoutDone;

   protected JSONLayout(boolean var1, boolean var2, boolean var3, boolean var4, Charset var5) {
      super(var5);
      this.locationInfo = var1;
      this.properties = var2;
      this.complete = var3;
      this.eol = var4?"":"\r\n";
      this.indent1 = var4?"":"  ";
      this.indent2 = this.indent1 + this.indent1;
      this.indent3 = this.indent2 + this.indent1;
      this.indent4 = this.indent3 + this.indent1;
   }

   public String toSerializable(LogEvent var1) {
      StringBuilder var2 = new StringBuilder(256);
      boolean var3 = this.firstLayoutDone;
      if(!this.firstLayoutDone) {
         synchronized(this) {
            var3 = this.firstLayoutDone;
            if(!var3) {
               this.firstLayoutDone = true;
            } else {
               var2.append(',');
               var2.append(this.eol);
            }
         }
      } else {
         var2.append(',');
         var2.append(this.eol);
      }

      var2.append(this.indent1);
      var2.append('{');
      var2.append(this.eol);
      var2.append(this.indent2);
      var2.append("\"logger\":\"");
      String var4 = var1.getLoggerName();
      if(var4.isEmpty()) {
         var4 = "root";
      }

      var2.append(Transform.escapeJsonControlCharacters(var4));
      var2.append("\",");
      var2.append(this.eol);
      var2.append(this.indent2);
      var2.append("\"timestamp\":\"");
      var2.append(var1.getMillis());
      var2.append("\",");
      var2.append(this.eol);
      var2.append(this.indent2);
      var2.append("\"level\":\"");
      var2.append(Transform.escapeJsonControlCharacters(String.valueOf(var1.getLevel())));
      var2.append("\",");
      var2.append(this.eol);
      var2.append(this.indent2);
      var2.append("\"thread\":\"");
      var2.append(Transform.escapeJsonControlCharacters(var1.getThreadName()));
      var2.append("\",");
      var2.append(this.eol);
      Message var5 = var1.getMessage();
      if(var5 != null) {
         boolean var6 = false;
         if(var5 instanceof MultiformatMessage) {
            String[] var7 = ((MultiformatMessage)var5).getFormats();
            String[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               if(var11.equalsIgnoreCase("JSON")) {
                  var6 = true;
                  break;
               }
            }
         }

         var2.append(this.indent2);
         var2.append("\"message\":\"");
         if(var6) {
            var2.append(((MultiformatMessage)var5).getFormattedMessage(FORMATS));
         } else {
            Transform.appendEscapingCDATA(var2, var1.getMessage().getFormattedMessage());
         }

         var2.append('\"');
      }

      if(var1.getContextStack().getDepth() > 0) {
         var2.append(",");
         var2.append(this.eol);
         var2.append("\"ndc\":");
         Transform.appendEscapingCDATA(var2, var1.getContextStack().toString());
         var2.append("\"");
      }

      Throwable var13 = var1.getThrown();
      if(var13 != null) {
         var2.append(",");
         var2.append(this.eol);
         var2.append(this.indent2);
         var2.append("\"throwable\":\"");
         List var14 = Throwables.toStringList(var13);
         Iterator var17 = var14.iterator();

         while(var17.hasNext()) {
            String var19 = (String)var17.next();
            var2.append(Transform.escapeJsonControlCharacters(var19));
            var2.append("\\\\n");
         }

         var2.append("\"");
      }

      if(this.locationInfo) {
         StackTraceElement var15 = var1.getSource();
         var2.append(",");
         var2.append(this.eol);
         var2.append(this.indent2);
         var2.append("\"LocationInfo\":{");
         var2.append(this.eol);
         var2.append(this.indent3);
         var2.append("\"class\":\"");
         var2.append(Transform.escapeJsonControlCharacters(var15.getClassName()));
         var2.append("\",");
         var2.append(this.eol);
         var2.append(this.indent3);
         var2.append("\"method\":\"");
         var2.append(Transform.escapeJsonControlCharacters(var15.getMethodName()));
         var2.append("\",");
         var2.append(this.eol);
         var2.append(this.indent3);
         var2.append("\"file\":\"");
         var2.append(Transform.escapeJsonControlCharacters(var15.getFileName()));
         var2.append("\",");
         var2.append(this.eol);
         var2.append(this.indent3);
         var2.append("\"line\":\"");
         var2.append(var15.getLineNumber());
         var2.append("\"");
         var2.append(this.eol);
         var2.append(this.indent2);
         var2.append("}");
      }

      if(this.properties && var1.getContextMap().size() > 0) {
         var2.append(",");
         var2.append(this.eol);
         var2.append(this.indent2);
         var2.append("\"Properties\":[");
         var2.append(this.eol);
         Set var16 = var1.getContextMap().entrySet();
         int var18 = 1;

         for(Iterator var20 = var16.iterator(); var20.hasNext(); ++var18) {
            Entry var21 = (Entry)var20.next();
            var2.append(this.indent3);
            var2.append('{');
            var2.append(this.eol);
            var2.append(this.indent4);
            var2.append("\"name\":\"");
            var2.append(Transform.escapeJsonControlCharacters((String)var21.getKey()));
            var2.append("\",");
            var2.append(this.eol);
            var2.append(this.indent4);
            var2.append("\"value\":\"");
            var2.append(Transform.escapeJsonControlCharacters(String.valueOf(var21.getValue())));
            var2.append("\"");
            var2.append(this.eol);
            var2.append(this.indent3);
            var2.append("}");
            if(var18 < var16.size()) {
               var2.append(",");
            }

            var2.append(this.eol);
         }

         var2.append(this.indent2);
         var2.append("]");
      }

      var2.append(this.eol);
      var2.append(this.indent1);
      var2.append("}");
      return var2.toString();
   }

   public byte[] getHeader() {
      if(!this.complete) {
         return null;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append('[');
         var1.append(this.eol);
         return var1.toString().getBytes(this.getCharset());
      }
   }

   public byte[] getFooter() {
      return !this.complete?null:(this.eol + "]" + this.eol).getBytes(this.getCharset());
   }

   public Map<String, String> getContentFormat() {
      HashMap var1 = new HashMap();
      var1.put("version", "2.0");
      return var1;
   }

   public String getContentType() {
      return "application/json; charset=" + this.getCharset();
   }

   @PluginFactory
   public static JSONLayout createLayout(@PluginAttribute("locationInfo") String var0, @PluginAttribute("properties") String var1, @PluginAttribute("complete") String var2, @PluginAttribute("compact") String var3, @PluginAttribute("charset") String var4) {
      Charset var5 = Charsets.getSupportedCharset(var4, Charsets.UTF_8);
      boolean var6 = Boolean.parseBoolean(var0);
      boolean var7 = Boolean.parseBoolean(var1);
      boolean var8 = Boolean.parseBoolean(var2);
      boolean var9 = Boolean.parseBoolean(var3);
      return new JSONLayout(var6, var7, var8, var9, var5);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }
}
