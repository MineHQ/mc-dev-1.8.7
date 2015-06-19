package io.netty.handler.codec.http;

import io.netty.util.internal.InternalThreadLocalMap;

final class CookieEncoderUtil {
   static StringBuilder stringBuilder() {
      return InternalThreadLocalMap.get().stringBuilder();
   }

   static String stripTrailingSeparator(StringBuilder var0) {
      if(var0.length() > 0) {
         var0.setLength(var0.length() - 2);
      }

      return var0.toString();
   }

   static void add(StringBuilder var0, String var1, String var2) {
      if(var2 == null) {
         addQuoted(var0, var1, "");
      } else {
         int var3 = 0;

         while(var3 < var2.length()) {
            char var4 = var2.charAt(var3);
            switch(var4) {
            case '\t':
            case ' ':
            case '\"':
            case '(':
            case ')':
            case ',':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case '[':
            case '\\':
            case ']':
            case '{':
            case '}':
               addQuoted(var0, var1, var2);
               return;
            default:
               ++var3;
            }
         }

         addUnquoted(var0, var1, var2);
      }
   }

   static void addUnquoted(StringBuilder var0, String var1, String var2) {
      var0.append(var1);
      var0.append('=');
      var0.append(var2);
      var0.append(';');
      var0.append(' ');
   }

   static void addQuoted(StringBuilder var0, String var1, String var2) {
      if(var2 == null) {
         var2 = "";
      }

      var0.append(var1);
      var0.append('=');
      var0.append('\"');
      var0.append(var2.replace("\\", "\\\\").replace("\"", "\\\""));
      var0.append('\"');
      var0.append(';');
      var0.append(' ');
   }

   static void add(StringBuilder var0, String var1, long var2) {
      var0.append(var1);
      var0.append('=');
      var0.append(var2);
      var0.append(';');
      var0.append(' ');
   }

   private CookieEncoderUtil() {
   }
}
