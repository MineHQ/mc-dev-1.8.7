package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.Level;

public enum Severity {
   EMERG(0),
   ALERT(1),
   CRITICAL(2),
   ERROR(3),
   WARNING(4),
   NOTICE(5),
   INFO(6),
   DEBUG(7);

   private final int code;

   private Severity(int var3) {
      this.code = var3;
   }

   public int getCode() {
      return this.code;
   }

   public boolean isEqual(String var1) {
      return this.name().equalsIgnoreCase(var1);
   }

   public static Severity getSeverity(Level var0) {
      switch(Severity.SyntheticClass_1.$SwitchMap$org$apache$logging$log4j$Level[var0.ordinal()]) {
      case 1:
         return DEBUG;
      case 2:
         return DEBUG;
      case 3:
         return DEBUG;
      case 4:
         return INFO;
      case 5:
         return WARNING;
      case 6:
         return ERROR;
      case 7:
         return ALERT;
      case 8:
         return EMERG;
      default:
         return DEBUG;
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$org$apache$logging$log4j$Level = new int[Level.values().length];

      static {
         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.ALL.ordinal()] = 1;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.TRACE.ordinal()] = 2;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.DEBUG.ordinal()] = 3;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.INFO.ordinal()] = 4;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.WARN.ordinal()] = 5;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.ERROR.ordinal()] = 6;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.FATAL.ordinal()] = 7;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$org$apache$logging$log4j$Level[Level.OFF.ordinal()] = 8;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
