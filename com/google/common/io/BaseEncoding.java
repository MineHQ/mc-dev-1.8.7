package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.GwtWorkarounds;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class BaseEncoding {
   private static final BaseEncoding BASE64 = new BaseEncoding.StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
   private static final BaseEncoding BASE64_URL = new BaseEncoding.StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));
   private static final BaseEncoding BASE32 = new BaseEncoding.StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
   private static final BaseEncoding BASE32_HEX = new BaseEncoding.StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
   private static final BaseEncoding BASE16 = new BaseEncoding.StandardBaseEncoding("base16()", "0123456789ABCDEF", (Character)null);

   BaseEncoding() {
   }

   public String encode(byte[] var1) {
      return this.encode((byte[])Preconditions.checkNotNull(var1), 0, var1.length);
   }

   public final String encode(byte[] var1, int var2, int var3) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      GwtWorkarounds.CharOutput var4 = GwtWorkarounds.stringBuilderOutput(this.maxEncodedSize(var3));
      GwtWorkarounds.ByteOutput var5 = this.encodingStream(var4);

      try {
         for(int var6 = 0; var6 < var3; ++var6) {
            var5.write(var1[var2 + var6]);
         }

         var5.close();
         return var4.toString();
      } catch (IOException var7) {
         throw new AssertionError("impossible");
      }
   }

   @GwtIncompatible("Writer,OutputStream")
   public final OutputStream encodingStream(Writer var1) {
      return GwtWorkarounds.asOutputStream(this.encodingStream(GwtWorkarounds.asCharOutput(var1)));
   }

   @GwtIncompatible("ByteSink,CharSink")
   public final ByteSink encodingSink(final CharSink var1) {
      Preconditions.checkNotNull(var1);
      return new ByteSink() {
         public OutputStream openStream() throws IOException {
            return BaseEncoding.this.encodingStream(var1.openStream());
         }
      };
   }

   private static byte[] extract(byte[] var0, int var1) {
      if(var1 == var0.length) {
         return var0;
      } else {
         byte[] var2 = new byte[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
         return var2;
      }
   }

   public final byte[] decode(CharSequence var1) {
      try {
         return this.decodeChecked(var1);
      } catch (BaseEncoding.DecodingException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   final byte[] decodeChecked(CharSequence var1) throws BaseEncoding.DecodingException {
      String var8 = this.padding().trimTrailingFrom(var1);
      GwtWorkarounds.ByteInput var2 = this.decodingStream(GwtWorkarounds.asCharInput((CharSequence)var8));
      byte[] var3 = new byte[this.maxDecodedSize(var8.length())];
      int var4 = 0;

      try {
         for(int var5 = var2.read(); var5 != -1; var5 = var2.read()) {
            var3[var4++] = (byte)var5;
         }
      } catch (BaseEncoding.DecodingException var6) {
         throw var6;
      } catch (IOException var7) {
         throw new AssertionError(var7);
      }

      return extract(var3, var4);
   }

   @GwtIncompatible("Reader,InputStream")
   public final InputStream decodingStream(Reader var1) {
      return GwtWorkarounds.asInputStream(this.decodingStream(GwtWorkarounds.asCharInput(var1)));
   }

   @GwtIncompatible("ByteSource,CharSource")
   public final ByteSource decodingSource(final CharSource var1) {
      Preconditions.checkNotNull(var1);
      return new ByteSource() {
         public InputStream openStream() throws IOException {
            return BaseEncoding.this.decodingStream(var1.openStream());
         }
      };
   }

   abstract int maxEncodedSize(int var1);

   abstract GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput var1);

   abstract int maxDecodedSize(int var1);

   abstract GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput var1);

   abstract CharMatcher padding();

   @CheckReturnValue
   public abstract BaseEncoding omitPadding();

   @CheckReturnValue
   public abstract BaseEncoding withPadChar(char var1);

   @CheckReturnValue
   public abstract BaseEncoding withSeparator(String var1, int var2);

   @CheckReturnValue
   public abstract BaseEncoding upperCase();

   @CheckReturnValue
   public abstract BaseEncoding lowerCase();

   public static BaseEncoding base64() {
      return BASE64;
   }

   public static BaseEncoding base64Url() {
      return BASE64_URL;
   }

   public static BaseEncoding base32() {
      return BASE32;
   }

   public static BaseEncoding base32Hex() {
      return BASE32_HEX;
   }

   public static BaseEncoding base16() {
      return BASE16;
   }

   static GwtWorkarounds.CharInput ignoringInput(final GwtWorkarounds.CharInput var0, final CharMatcher var1) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      return new GwtWorkarounds.CharInput() {
         public int read() throws IOException {
            int var1x;
            do {
               var1x = var0.read();
            } while(var1x != -1 && var1.matches((char)var1x));

            return var1x;
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   static GwtWorkarounds.CharOutput separatingOutput(final GwtWorkarounds.CharOutput var0, final String var1, final int var2) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(var2 > 0);
      return new GwtWorkarounds.CharOutput() {
         int charsUntilSeparator = var2;

         public void write(char var1x) throws IOException {
            if(this.charsUntilSeparator == 0) {
               for(int var2x = 0; var2x < var1.length(); ++var2x) {
                  var0.write(var1.charAt(var2x));
               }

               this.charsUntilSeparator = var2;
            }

            var0.write(var1x);
            --this.charsUntilSeparator;
         }

         public void flush() throws IOException {
            var0.flush();
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   static final class SeparatedBaseEncoding extends BaseEncoding {
      private final BaseEncoding delegate;
      private final String separator;
      private final int afterEveryChars;
      private final CharMatcher separatorChars;

      SeparatedBaseEncoding(BaseEncoding var1, String var2, int var3) {
         this.delegate = (BaseEncoding)Preconditions.checkNotNull(var1);
         this.separator = (String)Preconditions.checkNotNull(var2);
         this.afterEveryChars = var3;
         Preconditions.checkArgument(var3 > 0, "Cannot add a separator after every %s chars", new Object[]{Integer.valueOf(var3)});
         this.separatorChars = CharMatcher.anyOf(var2).precomputed();
      }

      CharMatcher padding() {
         return this.delegate.padding();
      }

      int maxEncodedSize(int var1) {
         int var2 = this.delegate.maxEncodedSize(var1);
         return var2 + this.separator.length() * IntMath.divide(Math.max(0, var2 - 1), this.afterEveryChars, RoundingMode.FLOOR);
      }

      GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput var1) {
         return this.delegate.encodingStream(separatingOutput(var1, this.separator, this.afterEveryChars));
      }

      int maxDecodedSize(int var1) {
         return this.delegate.maxDecodedSize(var1);
      }

      GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput var1) {
         return this.delegate.decodingStream(ignoringInput(var1, this.separatorChars));
      }

      public BaseEncoding omitPadding() {
         return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding withPadChar(char var1) {
         return this.delegate.withPadChar(var1).withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding withSeparator(String var1, int var2) {
         throw new UnsupportedOperationException("Already have a separator");
      }

      public BaseEncoding upperCase() {
         return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
      }

      public BaseEncoding lowerCase() {
         return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
      }

      public String toString() {
         return this.delegate.toString() + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
      }
   }

   static final class StandardBaseEncoding extends BaseEncoding {
      private final BaseEncoding.Alphabet alphabet;
      @Nullable
      private final Character paddingChar;
      private transient BaseEncoding upperCase;
      private transient BaseEncoding lowerCase;

      StandardBaseEncoding(String var1, String var2, @Nullable Character var3) {
         this(new BaseEncoding.Alphabet(var1, var2.toCharArray()), var3);
      }

      StandardBaseEncoding(BaseEncoding.Alphabet var1, @Nullable Character var2) {
         this.alphabet = (BaseEncoding.Alphabet)Preconditions.checkNotNull(var1);
         Preconditions.checkArgument(var2 == null || !var1.matches(var2.charValue()), "Padding character %s was already in alphabet", new Object[]{var2});
         this.paddingChar = var2;
      }

      CharMatcher padding() {
         return this.paddingChar == null?CharMatcher.NONE:CharMatcher.is(this.paddingChar.charValue());
      }

      int maxEncodedSize(int var1) {
         return this.alphabet.charsPerChunk * IntMath.divide(var1, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
      }

      GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput var1) {
         Preconditions.checkNotNull(var1);
         return new GwtWorkarounds.ByteOutput() {
            int bitBuffer = 0;
            int bitBufferLength = 0;
            int writtenChars = 0;

            public void write(byte var1x) throws IOException {
               this.bitBuffer <<= 8;
               this.bitBuffer |= var1x & 255;

               for(this.bitBufferLength += 8; this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar; this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                  int var2 = this.bitBuffer >> this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar & StandardBaseEncoding.this.alphabet.mask;
                  var1.write(StandardBaseEncoding.this.alphabet.encode(var2));
                  ++this.writtenChars;
               }

            }

            public void flush() throws IOException {
               var1.flush();
            }

            public void close() throws IOException {
               if(this.bitBufferLength > 0) {
                  int var1x = this.bitBuffer << StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & StandardBaseEncoding.this.alphabet.mask;
                  var1.write(StandardBaseEncoding.this.alphabet.encode(var1x));
                  ++this.writtenChars;
                  if(StandardBaseEncoding.this.paddingChar != null) {
                     while(this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                        var1.write(StandardBaseEncoding.this.paddingChar.charValue());
                        ++this.writtenChars;
                     }
                  }
               }

               var1.close();
            }
         };
      }

      int maxDecodedSize(int var1) {
         return (int)(((long)this.alphabet.bitsPerChar * (long)var1 + 7L) / 8L);
      }

      GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput var1) {
         Preconditions.checkNotNull(var1);
         return new GwtWorkarounds.ByteInput() {
            int bitBuffer = 0;
            int bitBufferLength = 0;
            int readChars = 0;
            boolean hitPadding = false;
            final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();

            public int read() throws IOException {
               while(true) {
                  int var1x = var1.read();
                  if(var1x == -1) {
                     if(!this.hitPadding && !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                        throw new BaseEncoding.DecodingException("Invalid input length " + this.readChars);
                     }

                     return -1;
                  }

                  ++this.readChars;
                  char var2 = (char)var1x;
                  if(!this.paddingMatcher.matches(var2)) {
                     if(this.hitPadding) {
                        throw new BaseEncoding.DecodingException("Expected padding character but found \'" + var2 + "\' at index " + this.readChars);
                     }

                     this.bitBuffer <<= StandardBaseEncoding.this.alphabet.bitsPerChar;
                     this.bitBuffer |= StandardBaseEncoding.this.alphabet.decode(var2);
                     this.bitBufferLength += StandardBaseEncoding.this.alphabet.bitsPerChar;
                     if(this.bitBufferLength >= 8) {
                        this.bitBufferLength -= 8;
                        return this.bitBuffer >> this.bitBufferLength & 255;
                     }
                  } else {
                     if(!this.hitPadding && (this.readChars == 1 || !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                        throw new BaseEncoding.DecodingException("Padding cannot start at index " + this.readChars);
                     }

                     this.hitPadding = true;
                  }
               }
            }

            public void close() throws IOException {
               var1.close();
            }
         };
      }

      public BaseEncoding omitPadding() {
         return this.paddingChar == null?this:new BaseEncoding.StandardBaseEncoding(this.alphabet, (Character)null);
      }

      public BaseEncoding withPadChar(char var1) {
         return 8 % this.alphabet.bitsPerChar != 0 && (this.paddingChar == null || this.paddingChar.charValue() != var1)?new BaseEncoding.StandardBaseEncoding(this.alphabet, Character.valueOf(var1)):this;
      }

      public BaseEncoding withSeparator(String var1, int var2) {
         Preconditions.checkNotNull(var1);
         Preconditions.checkArgument(this.padding().or(this.alphabet).matchesNoneOf(var1), "Separator cannot contain alphabet or padding characters");
         return new BaseEncoding.SeparatedBaseEncoding(this, var1, var2);
      }

      public BaseEncoding upperCase() {
         BaseEncoding var1 = this.upperCase;
         if(var1 == null) {
            BaseEncoding.Alphabet var2 = this.alphabet.upperCase();
            var1 = this.upperCase = var2 == this.alphabet?this:new BaseEncoding.StandardBaseEncoding(var2, this.paddingChar);
         }

         return var1;
      }

      public BaseEncoding lowerCase() {
         BaseEncoding var1 = this.lowerCase;
         if(var1 == null) {
            BaseEncoding.Alphabet var2 = this.alphabet.lowerCase();
            var1 = this.lowerCase = var2 == this.alphabet?this:new BaseEncoding.StandardBaseEncoding(var2, this.paddingChar);
         }

         return var1;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("BaseEncoding.");
         var1.append(this.alphabet.toString());
         if(8 % this.alphabet.bitsPerChar != 0) {
            if(this.paddingChar == null) {
               var1.append(".omitPadding()");
            } else {
               var1.append(".withPadChar(").append(this.paddingChar).append(')');
            }
         }

         return var1.toString();
      }
   }

   private static final class Alphabet extends CharMatcher {
      private final String name;
      private final char[] chars;
      final int mask;
      final int bitsPerChar;
      final int charsPerChunk;
      final int bytesPerChunk;
      private final byte[] decodabet;
      private final boolean[] validPadding;

      Alphabet(String var1, char[] var2) {
         this.name = (String)Preconditions.checkNotNull(var1);
         this.chars = (char[])Preconditions.checkNotNull(var2);

         try {
            this.bitsPerChar = IntMath.log2(var2.length, RoundingMode.UNNECESSARY);
         } catch (ArithmeticException var7) {
            throw new IllegalArgumentException("Illegal alphabet length " + var2.length, var7);
         }

         int var3 = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
         this.charsPerChunk = 8 / var3;
         this.bytesPerChunk = this.bitsPerChar / var3;
         this.mask = var2.length - 1;
         byte[] var4 = new byte[128];
         Arrays.fill(var4, (byte)-1);

         for(int var5 = 0; var5 < var2.length; ++var5) {
            char var6 = var2[var5];
            Preconditions.checkArgument(CharMatcher.ASCII.matches(var6), "Non-ASCII character: %s", new Object[]{Character.valueOf(var6)});
            Preconditions.checkArgument(var4[var6] == -1, "Duplicate character: %s", new Object[]{Character.valueOf(var6)});
            var4[var6] = (byte)var5;
         }

         this.decodabet = var4;
         boolean[] var8 = new boolean[this.charsPerChunk];

         for(int var9 = 0; var9 < this.bytesPerChunk; ++var9) {
            var8[IntMath.divide(var9 * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
         }

         this.validPadding = var8;
      }

      char encode(int var1) {
         return this.chars[var1];
      }

      boolean isValidPaddingStartPosition(int var1) {
         return this.validPadding[var1 % this.charsPerChunk];
      }

      int decode(char var1) throws IOException {
         if(var1 <= 127 && this.decodabet[var1] != -1) {
            return this.decodabet[var1];
         } else {
            throw new BaseEncoding.DecodingException("Unrecognized character: " + var1);
         }
      }

      private boolean hasLowerCase() {
         char[] var1 = this.chars;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            char var4 = var1[var3];
            if(Ascii.isLowerCase(var4)) {
               return true;
            }
         }

         return false;
      }

      private boolean hasUpperCase() {
         char[] var1 = this.chars;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            char var4 = var1[var3];
            if(Ascii.isUpperCase(var4)) {
               return true;
            }
         }

         return false;
      }

      BaseEncoding.Alphabet upperCase() {
         if(!this.hasLowerCase()) {
            return this;
         } else {
            Preconditions.checkState(!this.hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] var1 = new char[this.chars.length];

            for(int var2 = 0; var2 < this.chars.length; ++var2) {
               var1[var2] = Ascii.toUpperCase(this.chars[var2]);
            }

            return new BaseEncoding.Alphabet(this.name + ".upperCase()", var1);
         }
      }

      BaseEncoding.Alphabet lowerCase() {
         if(!this.hasUpperCase()) {
            return this;
         } else {
            Preconditions.checkState(!this.hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] var1 = new char[this.chars.length];

            for(int var2 = 0; var2 < this.chars.length; ++var2) {
               var1[var2] = Ascii.toLowerCase(this.chars[var2]);
            }

            return new BaseEncoding.Alphabet(this.name + ".lowerCase()", var1);
         }
      }

      public boolean matches(char var1) {
         return CharMatcher.ASCII.matches(var1) && this.decodabet[var1] != -1;
      }

      public String toString() {
         return this.name;
      }
   }

   public static final class DecodingException extends IOException {
      DecodingException(String var1) {
         super(var1);
      }

      DecodingException(Throwable var1) {
         super(var1);
      }
   }
}
