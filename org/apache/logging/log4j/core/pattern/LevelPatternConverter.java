package org.apache.logging.log4j.core.pattern;

import java.util.EnumMap;
import java.util.Locale;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(
   name = "LevelPatternConverter",
   category = "Converter"
)
@ConverterKeys({"p", "level"})
public final class LevelPatternConverter extends LogEventPatternConverter {
   private static final String OPTION_LENGTH = "length";
   private static final String OPTION_LOWER = "lowerCase";
   private static final LevelPatternConverter INSTANCE = new LevelPatternConverter((EnumMap)null);
   private final EnumMap<Level, String> levelMap;

   private LevelPatternConverter(EnumMap<Level, String> var1) {
      super("Level", "level");
      this.levelMap = var1;
   }

   public static LevelPatternConverter newInstance(String[] var0) {
      if(var0 != null && var0.length != 0) {
         EnumMap var1 = new EnumMap(Level.class);
         int var2 = Integer.MAX_VALUE;
         boolean var3 = false;
         String[] var4 = var0[0].split(",");
         String[] var5 = var4;
         int var6 = var4.length;

         int var7;
         for(var7 = 0; var7 < var6; ++var7) {
            String var8 = var5[var7];
            String[] var9 = var8.split("=");
            if(var9 != null && var9.length == 2) {
               String var10 = var9[0].trim();
               String var11 = var9[1].trim();
               if("length".equalsIgnoreCase(var10)) {
                  var2 = Integer.parseInt(var11);
               } else if("lowerCase".equalsIgnoreCase(var10)) {
                  var3 = Boolean.parseBoolean(var11);
               } else {
                  Level var12 = Level.toLevel(var10, (Level)null);
                  if(var12 == null) {
                     LOGGER.error("Invalid Level {}", new Object[]{var10});
                  } else {
                     var1.put(var12, var11);
                  }
               }
            } else {
               LOGGER.error("Invalid option {}", new Object[]{var8});
            }
         }

         if(var1.size() == 0 && var2 == Integer.MAX_VALUE && !var3) {
            return INSTANCE;
         } else {
            Level[] var13 = Level.values();
            var6 = var13.length;

            for(var7 = 0; var7 < var6; ++var7) {
               Level var14 = var13[var7];
               if(!var1.containsKey(var14)) {
                  String var15 = left(var14, var2);
                  var1.put(var14, var3?var15.toLowerCase(Locale.US):var15);
               }
            }

            return new LevelPatternConverter(var1);
         }
      } else {
         return INSTANCE;
      }
   }

   private static String left(Level var0, int var1) {
      String var2 = var0.toString();
      return var1 >= var2.length()?var2:var2.substring(0, var1);
   }

   public void format(LogEvent var1, StringBuilder var2) {
      var2.append(this.levelMap == null?var1.getLevel().toString():(String)this.levelMap.get(var1.getLevel()));
   }

   public String getStyleClass(Object var1) {
      if(var1 instanceof LogEvent) {
         Level var2 = ((LogEvent)var1).getLevel();
         switch(LevelPatternConverter.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[var2.ordinal()]) {
         case 1:
            return "level trace";
         case 2:
            return "level debug";
         case 3:
            return "level info";
         case 4:
            return "level warn";
         case 5:
            return "level error";
         case 6:
            return "level fatal";
         default:
            return "level " + ((LogEvent)var1).getLevel().toString();
         }
      } else {
         return "level";
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$org$apache$logging$log4j$Level = new int[Level.values().length];

      static {
         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.TRACE.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.DEBUG.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.INFO.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.WARN.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.ERROR.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.FATAL.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
