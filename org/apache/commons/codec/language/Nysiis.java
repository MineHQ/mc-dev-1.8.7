package org.apache.commons.codec.language;

import java.util.regex.Pattern;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

public class Nysiis implements StringEncoder {
   private static final char[] CHARS_A = new char[]{'A'};
   private static final char[] CHARS_AF = new char[]{'A', 'F'};
   private static final char[] CHARS_C = new char[]{'C'};
   private static final char[] CHARS_FF = new char[]{'F', 'F'};
   private static final char[] CHARS_G = new char[]{'G'};
   private static final char[] CHARS_N = new char[]{'N'};
   private static final char[] CHARS_NN = new char[]{'N', 'N'};
   private static final char[] CHARS_S = new char[]{'S'};
   private static final char[] CHARS_SSS = new char[]{'S', 'S', 'S'};
   private static final Pattern PAT_MAC = Pattern.compile("^MAC");
   private static final Pattern PAT_KN = Pattern.compile("^KN");
   private static final Pattern PAT_K = Pattern.compile("^K");
   private static final Pattern PAT_PH_PF = Pattern.compile("^(PH|PF)");
   private static final Pattern PAT_SCH = Pattern.compile("^SCH");
   private static final Pattern PAT_EE_IE = Pattern.compile("(EE|IE)$");
   private static final Pattern PAT_DT_ETC = Pattern.compile("(DT|RT|RD|NT|ND)$");
   private static final char SPACE = ' ';
   private static final int TRUE_LENGTH = 6;
   private final boolean strict;

   private static boolean isVowel(char var0) {
      return var0 == 65 || var0 == 69 || var0 == 73 || var0 == 79 || var0 == 85;
   }

   private static char[] transcodeRemaining(char var0, char var1, char var2, char var3) {
      return var1 == 69 && var2 == 86?CHARS_AF:(isVowel(var1)?CHARS_A:(var1 == 81?CHARS_G:(var1 == 90?CHARS_S:(var1 == 77?CHARS_N:(var1 == 75?(var2 == 78?CHARS_NN:CHARS_C):(var1 == 83 && var2 == 67 && var3 == 72?CHARS_SSS:(var1 == 80 && var2 == 72?CHARS_FF:(var1 == 72 && (!isVowel(var0) || !isVowel(var2))?new char[]{var0}:(var1 == 87 && isVowel(var0)?new char[]{var0}:new char[]{var1})))))))));
   }

   public Nysiis() {
      this(true);
   }

   public Nysiis(boolean var1) {
      this.strict = var1;
   }

   public Object encode(Object var1) throws EncoderException {
      if(!(var1 instanceof String)) {
         throw new EncoderException("Parameter supplied to Nysiis encode is not of type java.lang.String");
      } else {
         return this.nysiis((String)var1);
      }
   }

   public String encode(String var1) {
      return this.nysiis(var1);
   }

   public boolean isStrict() {
      return this.strict;
   }

   public String nysiis(String var1) {
      if(var1 == null) {
         return null;
      } else {
         var1 = SoundexUtils.clean(var1);
         if(var1.length() == 0) {
            return var1;
         } else {
            var1 = PAT_MAC.matcher(var1).replaceFirst("MCC");
            var1 = PAT_KN.matcher(var1).replaceFirst("NN");
            var1 = PAT_K.matcher(var1).replaceFirst("C");
            var1 = PAT_PH_PF.matcher(var1).replaceFirst("FF");
            var1 = PAT_SCH.matcher(var1).replaceFirst("SSS");
            var1 = PAT_EE_IE.matcher(var1).replaceFirst("Y");
            var1 = PAT_DT_ETC.matcher(var1).replaceFirst("D");
            StringBuilder var2 = new StringBuilder(var1.length());
            var2.append(var1.charAt(0));
            char[] var3 = var1.toCharArray();
            int var4 = var3.length;

            char var6;
            for(int var5 = 1; var5 < var4; ++var5) {
               var6 = var5 < var4 - 1?var3[var5 + 1]:32;
               char var7 = var5 < var4 - 2?var3[var5 + 2]:32;
               char[] var8 = transcodeRemaining(var3[var5 - 1], var3[var5], var6, var7);
               System.arraycopy(var8, 0, var3, var5, var8.length);
               if(var3[var5] != var3[var5 - 1]) {
                  var2.append(var3[var5]);
               }
            }

            if(var2.length() > 1) {
               char var9 = var2.charAt(var2.length() - 1);
               if(var9 == 83) {
                  var2.deleteCharAt(var2.length() - 1);
                  var9 = var2.charAt(var2.length() - 1);
               }

               if(var2.length() > 2) {
                  var6 = var2.charAt(var2.length() - 2);
                  if(var6 == 65 && var9 == 89) {
                     var2.deleteCharAt(var2.length() - 2);
                  }
               }

               if(var9 == 65) {
                  var2.deleteCharAt(var2.length() - 1);
               }
            }

            String var10 = var2.toString();
            return this.isStrict()?var10.substring(0, Math.min(6, var10.length())):var10;
         }
      }
   }
}
