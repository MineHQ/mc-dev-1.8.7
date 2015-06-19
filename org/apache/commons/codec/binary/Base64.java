package org.apache.commons.codec.binary;

import java.math.BigInteger;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.StringUtils;

public class Base64 extends BaseNCodec {
   private static final int BITS_PER_ENCODED_BYTE = 6;
   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
   private static final int BYTES_PER_ENCODED_BLOCK = 4;
   static final byte[] CHUNK_SEPARATOR = new byte[]{(byte)13, (byte)10};
   private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)43, (byte)47};
   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{(byte)65, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71, (byte)72, (byte)73, (byte)74, (byte)75, (byte)76, (byte)77, (byte)78, (byte)79, (byte)80, (byte)81, (byte)82, (byte)83, (byte)84, (byte)85, (byte)86, (byte)87, (byte)88, (byte)89, (byte)90, (byte)97, (byte)98, (byte)99, (byte)100, (byte)101, (byte)102, (byte)103, (byte)104, (byte)105, (byte)106, (byte)107, (byte)108, (byte)109, (byte)110, (byte)111, (byte)112, (byte)113, (byte)114, (byte)115, (byte)116, (byte)117, (byte)118, (byte)119, (byte)120, (byte)121, (byte)122, (byte)48, (byte)49, (byte)50, (byte)51, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)45, (byte)95};
   private static final byte[] DECODE_TABLE = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)62, (byte)-1, (byte)62, (byte)-1, (byte)63, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)63, (byte)-1, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51};
   private static final int MASK_6BITS = 63;
   private final byte[] encodeTable;
   private final byte[] decodeTable;
   private final byte[] lineSeparator;
   private final int decodeSize;
   private final int encodeSize;

   public Base64() {
      this(0);
   }

   public Base64(boolean var1) {
      this(76, CHUNK_SEPARATOR, var1);
   }

   public Base64(int var1) {
      this(var1, CHUNK_SEPARATOR);
   }

   public Base64(int var1, byte[] var2) {
      this(var1, var2, false);
   }

   public Base64(int var1, byte[] var2, boolean var3) {
      super(3, 4, var1, var2 == null?0:var2.length);
      this.decodeTable = DECODE_TABLE;
      if(var2 != null) {
         if(this.containsAlphabetOrPad(var2)) {
            String var4 = StringUtils.newStringUtf8(var2);
            throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + var4 + "]");
         }

         if(var1 > 0) {
            this.encodeSize = 4 + var2.length;
            this.lineSeparator = new byte[var2.length];
            System.arraycopy(var2, 0, this.lineSeparator, 0, var2.length);
         } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
         }
      } else {
         this.encodeSize = 4;
         this.lineSeparator = null;
      }

      this.decodeSize = this.encodeSize - 1;
      this.encodeTable = var3?URL_SAFE_ENCODE_TABLE:STANDARD_ENCODE_TABLE;
   }

   public boolean isUrlSafe() {
      return this.encodeTable == URL_SAFE_ENCODE_TABLE;
   }

   void encode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if(!var4.eof) {
         if(var3 < 0) {
            var4.eof = true;
            if(0 == var4.modulus && this.lineLength == 0) {
               return;
            }

            byte[] var5 = this.ensureBufferSize(this.encodeSize, var4);
            int var6 = var4.pos;
            switch(var4.modulus) {
            case 0:
               break;
            case 1:
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 2 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea << 4 & 63];
               if(this.encodeTable == STANDARD_ENCODE_TABLE) {
                  var5[var4.pos++] = 61;
                  var5[var4.pos++] = 61;
               }
               break;
            case 2:
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 10 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 4 & 63];
               var5[var4.pos++] = this.encodeTable[var4.ibitWorkArea << 2 & 63];
               if(this.encodeTable == STANDARD_ENCODE_TABLE) {
                  var5[var4.pos++] = 61;
               }
               break;
            default:
               throw new IllegalStateException("Impossible modulus " + var4.modulus);
            }

            var4.currentLinePos += var4.pos - var6;
            if(this.lineLength > 0 && var4.currentLinePos > 0) {
               System.arraycopy(this.lineSeparator, 0, var5, var4.pos, this.lineSeparator.length);
               var4.pos += this.lineSeparator.length;
            }
         } else {
            for(int var8 = 0; var8 < var3; ++var8) {
               byte[] var9 = this.ensureBufferSize(this.encodeSize, var4);
               var4.modulus = (var4.modulus + 1) % 3;
               int var7 = var1[var2++];
               if(var7 < 0) {
                  var7 += 256;
               }

               var4.ibitWorkArea = (var4.ibitWorkArea << 8) + var7;
               if(0 == var4.modulus) {
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 18 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 12 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea >> 6 & 63];
                  var9[var4.pos++] = this.encodeTable[var4.ibitWorkArea & 63];
                  var4.currentLinePos += 4;
                  if(this.lineLength > 0 && this.lineLength <= var4.currentLinePos) {
                     System.arraycopy(this.lineSeparator, 0, var9, var4.pos, this.lineSeparator.length);
                     var4.pos += this.lineSeparator.length;
                     var4.currentLinePos = 0;
                  }
               }
            }
         }

      }
   }

   void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if(!var4.eof) {
         if(var3 < 0) {
            var4.eof = true;
         }

         for(int var5 = 0; var5 < var3; ++var5) {
            byte[] var6 = this.ensureBufferSize(this.decodeSize, var4);
            byte var7 = var1[var2++];
            if(var7 == 61) {
               var4.eof = true;
               break;
            }

            if(var7 >= 0 && var7 < DECODE_TABLE.length) {
               byte var8 = DECODE_TABLE[var7];
               if(var8 >= 0) {
                  var4.modulus = (var4.modulus + 1) % 4;
                  var4.ibitWorkArea = (var4.ibitWorkArea << 6) + var8;
                  if(var4.modulus == 0) {
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea >> 16 & 255);
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea >> 8 & 255);
                     var6[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
                  }
               }
            }
         }

         if(var4.eof && var4.modulus != 0) {
            byte[] var9 = this.ensureBufferSize(this.decodeSize, var4);
            switch(var4.modulus) {
            case 1:
               break;
            case 2:
               var4.ibitWorkArea >>= 4;
               var9[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
               break;
            case 3:
               var4.ibitWorkArea >>= 2;
               var9[var4.pos++] = (byte)(var4.ibitWorkArea >> 8 & 255);
               var9[var4.pos++] = (byte)(var4.ibitWorkArea & 255);
               break;
            default:
               throw new IllegalStateException("Impossible modulus " + var4.modulus);
            }
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public static boolean isArrayByteBase64(byte[] var0) {
      return isBase64(var0);
   }

   public static boolean isBase64(byte var0) {
      return var0 == 61 || var0 >= 0 && var0 < DECODE_TABLE.length && DECODE_TABLE[var0] != -1;
   }

   public static boolean isBase64(String var0) {
      return isBase64(StringUtils.getBytesUtf8(var0));
   }

   public static boolean isBase64(byte[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if(!isBase64(var0[var1]) && !isWhiteSpace(var0[var1])) {
            return false;
         }
      }

      return true;
   }

   public static byte[] encodeBase64(byte[] var0) {
      return encodeBase64(var0, false);
   }

   public static String encodeBase64String(byte[] var0) {
      return StringUtils.newStringUtf8(encodeBase64(var0, false));
   }

   public static byte[] encodeBase64URLSafe(byte[] var0) {
      return encodeBase64(var0, false, true);
   }

   public static String encodeBase64URLSafeString(byte[] var0) {
      return StringUtils.newStringUtf8(encodeBase64(var0, false, true));
   }

   public static byte[] encodeBase64Chunked(byte[] var0) {
      return encodeBase64(var0, true);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1) {
      return encodeBase64(var0, var1, false);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2) {
      return encodeBase64(var0, var1, var2, Integer.MAX_VALUE);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2, int var3) {
      if(var0 != null && var0.length != 0) {
         Base64 var4 = var1?new Base64(var2):new Base64(0, CHUNK_SEPARATOR, var2);
         long var5 = var4.getEncodedLength(var0);
         if(var5 > (long)var3) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + var5 + ") than the specified maximum size of " + var3);
         } else {
            return var4.encode(var0);
         }
      } else {
         return var0;
      }
   }

   public static byte[] decodeBase64(String var0) {
      return (new Base64()).decode(var0);
   }

   public static byte[] decodeBase64(byte[] var0) {
      return (new Base64()).decode(var0);
   }

   public static BigInteger decodeInteger(byte[] var0) {
      return new BigInteger(1, decodeBase64(var0));
   }

   public static byte[] encodeInteger(BigInteger var0) {
      if(var0 == null) {
         throw new NullPointerException("encodeInteger called with null parameter");
      } else {
         return encodeBase64(toIntegerBytes(var0), false);
      }
   }

   static byte[] toIntegerBytes(BigInteger var0) {
      int var1 = var0.bitLength();
      var1 = var1 + 7 >> 3 << 3;
      byte[] var2 = var0.toByteArray();
      if(var0.bitLength() % 8 != 0 && var0.bitLength() / 8 + 1 == var1 / 8) {
         return var2;
      } else {
         byte var3 = 0;
         int var4 = var2.length;
         if(var0.bitLength() % 8 == 0) {
            var3 = 1;
            --var4;
         }

         int var5 = var1 / 8 - var4;
         byte[] var6 = new byte[var1 / 8];
         System.arraycopy(var2, var3, var6, var5, var4);
         return var6;
      }
   }

   protected boolean isInAlphabet(byte var1) {
      return var1 >= 0 && var1 < this.decodeTable.length && this.decodeTable[var1] != -1;
   }
}
