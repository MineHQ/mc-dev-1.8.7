package org.apache.commons.io;

import java.nio.charset.Charset;

public class Charsets {
   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
   public static final Charset US_ASCII = Charset.forName("US-ASCII");
   public static final Charset UTF_16 = Charset.forName("UTF-16");
   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
   public static final Charset UTF_8 = Charset.forName("UTF-8");

   public Charsets() {
   }

   public static Charset toCharset(Charset var0) {
      return var0 == null?Charset.defaultCharset():var0;
   }

   public static Charset toCharset(String var0) {
      return var0 == null?Charset.defaultCharset():Charset.forName(var0);
   }
}
