package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class NumericEntityUnescaper extends CharSequenceTranslator {
   private final EnumSet<NumericEntityUnescaper.OPTION> options;

   public NumericEntityUnescaper(NumericEntityUnescaper.OPTION... var1) {
      if(var1.length > 0) {
         this.options = EnumSet.copyOf(Arrays.asList(var1));
      } else {
         this.options = EnumSet.copyOf(Arrays.asList(new NumericEntityUnescaper.OPTION[]{NumericEntityUnescaper.OPTION.semiColonRequired}));
      }

   }

   public boolean isSet(NumericEntityUnescaper.OPTION var1) {
      return this.options == null?false:this.options.contains(var1);
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var4 = var1.length();
      if(var1.charAt(var2) == 38 && var2 < var4 - 2 && var1.charAt(var2 + 1) == 35) {
         int var5 = var2 + 2;
         boolean var6 = false;
         char var7 = var1.charAt(var5);
         if(var7 == 120 || var7 == 88) {
            ++var5;
            var6 = true;
            if(var5 == var4) {
               return 0;
            }
         }

         int var8;
         for(var8 = var5; var8 < var4 && (var1.charAt(var8) >= 48 && var1.charAt(var8) <= 57 || var1.charAt(var8) >= 97 && var1.charAt(var8) <= 102 || var1.charAt(var8) >= 65 && var1.charAt(var8) <= 70); ++var8) {
            ;
         }

         boolean var9 = var8 != var4 && var1.charAt(var8) == 59;
         if(!var9) {
            if(this.isSet(NumericEntityUnescaper.OPTION.semiColonRequired)) {
               return 0;
            }

            if(this.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon)) {
               throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
            }
         }

         int var10;
         try {
            if(var6) {
               var10 = Integer.parseInt(var1.subSequence(var5, var8).toString(), 16);
            } else {
               var10 = Integer.parseInt(var1.subSequence(var5, var8).toString(), 10);
            }
         } catch (NumberFormatException var12) {
            return 0;
         }

         if(var10 > '\uffff') {
            char[] var11 = Character.toChars(var10);
            var3.write(var11[0]);
            var3.write(var11[1]);
         } else {
            var3.write(var10);
         }

         return 2 + var8 - var5 + (var6?1:0) + (var9?1:0);
      } else {
         return 0;
      }
   }

   public static enum OPTION {
      semiColonRequired,
      semiColonOptional,
      errorIfNoSemiColon;

      private OPTION() {
      }
   }
}
