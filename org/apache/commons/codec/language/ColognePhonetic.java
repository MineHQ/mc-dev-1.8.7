package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
   private static final char[] AEIJOUY = new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
   private static final char[] SCZ = new char[]{'S', 'C', 'Z'};
   private static final char[] WFPV = new char[]{'W', 'F', 'P', 'V'};
   private static final char[] GKQ = new char[]{'G', 'K', 'Q'};
   private static final char[] CKQ = new char[]{'C', 'K', 'Q'};
   private static final char[] AHKLOQRUX = new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
   private static final char[] SZ = new char[]{'S', 'Z'};
   private static final char[] AHOUKQX = new char[]{'A', 'H', 'O', 'U', 'K', 'Q', 'X'};
   private static final char[] TDX = new char[]{'T', 'D', 'X'};
   private static final char[][] PREPROCESS_MAP = new char[][]{{'\u00c4', 'A'}, {'\u00dc', 'U'}, {'\u00d6', 'O'}, {'\u00df', 'S'}};

   public ColognePhonetic() {
   }

   private static boolean arrayContains(char[] var0, char var1) {
      char[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char var5 = var2[var4];
         if(var5 == var1) {
            return true;
         }
      }

      return false;
   }

   public String colognePhonetic(String var1) {
      if(var1 == null) {
         return null;
      } else {
         var1 = this.preprocess(var1);
         ColognePhonetic.CologneOutputBuffer var2 = new ColognePhonetic.CologneOutputBuffer(var1.length() * 2);
         ColognePhonetic.CologneInputBuffer var3 = new ColognePhonetic.CologneInputBuffer(var1.toCharArray());
         char var5 = 45;
         char var6 = 47;
         int var9 = var3.length();

         while(true) {
            char var7;
            char var8;
            while(true) {
               if(var9 <= 0) {
                  return var2.toString();
               }

               var8 = var3.removeNext();
               char var4;
               if((var9 = var3.length()) > 0) {
                  var4 = var3.getNextChar();
               } else {
                  var4 = 45;
               }

               if(arrayContains(AEIJOUY, var8)) {
                  var7 = 48;
                  break;
               }

               if(var8 != 72 && var8 >= 65 && var8 <= 90) {
                  if(var8 != 66 && (var8 != 80 || var4 == 72)) {
                     if((var8 == 68 || var8 == 84) && !arrayContains(SCZ, var4)) {
                        var7 = 50;
                        break;
                     }

                     if(arrayContains(WFPV, var8)) {
                        var7 = 51;
                        break;
                     }

                     if(arrayContains(GKQ, var8)) {
                        var7 = 52;
                        break;
                     }

                     if(var8 == 88 && !arrayContains(CKQ, var5)) {
                        var7 = 52;
                        var3.addLeft('S');
                        ++var9;
                        break;
                     }

                     if(var8 != 83 && var8 != 90) {
                        if(var8 == 67) {
                           if(var6 == 47) {
                              if(arrayContains(AHKLOQRUX, var4)) {
                                 var7 = 52;
                              } else {
                                 var7 = 56;
                              }
                              break;
                           }

                           if(!arrayContains(SZ, var5) && arrayContains(AHOUKQX, var4)) {
                              var7 = 52;
                              break;
                           }

                           var7 = 56;
                           break;
                        }

                        if(arrayContains(TDX, var8)) {
                           var7 = 56;
                           break;
                        }

                        if(var8 == 82) {
                           var7 = 55;
                           break;
                        }

                        if(var8 == 76) {
                           var7 = 53;
                           break;
                        }

                        if(var8 != 77 && var8 != 78) {
                           var7 = var8;
                           break;
                        }

                        var7 = 54;
                        break;
                     }

                     var7 = 56;
                     break;
                  }

                  var7 = 49;
                  break;
               }

               if(var6 != 47) {
                  var7 = 45;
                  break;
               }
            }

            if(var7 != 45 && (var6 != var7 && (var7 != 48 || var6 == 47) || var7 < 48 || var7 > 56)) {
               var2.addRight(var7);
            }

            var5 = var8;
            var6 = var7;
         }
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if(!(var1 instanceof String)) {
         throw new EncoderException("This method\'s parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + var1.getClass().getName() + ".");
      } else {
         return this.encode((String)var1);
      }
   }

   public String encode(String var1) {
      return this.colognePhonetic(var1);
   }

   public boolean isEncodeEqual(String var1, String var2) {
      return this.colognePhonetic(var1).equals(this.colognePhonetic(var2));
   }

   private String preprocess(String var1) {
      var1 = var1.toUpperCase(Locale.GERMAN);
      char[] var2 = var1.toCharArray();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if(var2[var3] > 90) {
            char[][] var4 = PREPROCESS_MAP;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               char[] var7 = var4[var6];
               if(var2[var3] == var7[0]) {
                  var2[var3] = var7[1];
                  break;
               }
            }
         }
      }

      return new String(var2);
   }

   private class CologneInputBuffer extends ColognePhonetic.CologneBuffer {
      public CologneInputBuffer(char[] var2) {
         super();
      }

      public void addLeft(char var1) {
         ++this.length;
         this.data[this.getNextPos()] = var1;
      }

      protected char[] copyData(int var1, int var2) {
         char[] var3 = new char[var2];
         System.arraycopy(this.data, this.data.length - this.length + var1, var3, 0, var2);
         return var3;
      }

      public char getNextChar() {
         return this.data[this.getNextPos()];
      }

      protected int getNextPos() {
         return this.data.length - this.length;
      }

      public char removeNext() {
         char var1 = this.getNextChar();
         --this.length;
         return var1;
      }
   }

   private class CologneOutputBuffer extends ColognePhonetic.CologneBuffer {
      public CologneOutputBuffer(int var2) {
         super();
      }

      public void addRight(char var1) {
         this.data[this.length] = var1;
         ++this.length;
      }

      protected char[] copyData(int var1, int var2) {
         char[] var3 = new char[var2];
         System.arraycopy(this.data, var1, var3, 0, var2);
         return var3;
      }
   }

   private abstract class CologneBuffer {
      protected final char[] data;
      protected int length = 0;

      public CologneBuffer(char[] var2) {
         this.data = var2;
         this.length = var2.length;
      }

      public CologneBuffer(int var2) {
         this.data = new char[var2];
         this.length = 0;
      }

      protected abstract char[] copyData(int var1, int var2);

      public int length() {
         return this.length;
      }

      public String toString() {
         return new String(this.copyData(0, this.length));
      }
   }
}
