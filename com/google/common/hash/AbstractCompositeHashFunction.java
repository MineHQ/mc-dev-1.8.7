package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.AbstractStreamingHashFunction;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.PrimitiveSink;
import java.nio.charset.Charset;

abstract class AbstractCompositeHashFunction extends AbstractStreamingHashFunction {
   final HashFunction[] functions;
   private static final long serialVersionUID = 0L;

   AbstractCompositeHashFunction(HashFunction... var1) {
      HashFunction[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         HashFunction var5 = var2[var4];
         Preconditions.checkNotNull(var5);
      }

      this.functions = var1;
   }

   abstract HashCode makeHash(Hasher[] var1);

   public Hasher newHasher() {
      final Hasher[] var1 = new Hasher[this.functions.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = this.functions[var2].newHasher();
      }

      return new Hasher() {
         public Hasher putByte(byte var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putByte(var1x);
            }

            return this;
         }

         public Hasher putBytes(byte[] var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putBytes(var1x);
            }

            return this;
         }

         public Hasher putBytes(byte[] var1x, int var2, int var3) {
            Hasher[] var4 = var1;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Hasher var7 = var4[var6];
               var7.putBytes(var1x, var2, var3);
            }

            return this;
         }

         public Hasher putShort(short var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putShort(var1x);
            }

            return this;
         }

         public Hasher putInt(int var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putInt(var1x);
            }

            return this;
         }

         public Hasher putLong(long var1x) {
            Hasher[] var3 = var1;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Hasher var6 = var3[var5];
               var6.putLong(var1x);
            }

            return this;
         }

         public Hasher putFloat(float var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putFloat(var1x);
            }

            return this;
         }

         public Hasher putDouble(double var1x) {
            Hasher[] var3 = var1;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Hasher var6 = var3[var5];
               var6.putDouble(var1x);
            }

            return this;
         }

         public Hasher putBoolean(boolean var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putBoolean(var1x);
            }

            return this;
         }

         public Hasher putChar(char var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putChar(var1x);
            }

            return this;
         }

         public Hasher putUnencodedChars(CharSequence var1x) {
            Hasher[] var2 = var1;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Hasher var5 = var2[var4];
               var5.putUnencodedChars(var1x);
            }

            return this;
         }

         public Hasher putString(CharSequence var1x, Charset var2) {
            Hasher[] var3 = var1;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Hasher var6 = var3[var5];
               var6.putString(var1x, var2);
            }

            return this;
         }

         public <T> Hasher putObject(T var1x, Funnel<? super T> var2) {
            Hasher[] var3 = var1;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Hasher var6 = var3[var5];
               var6.putObject(var1x, var2);
            }

            return this;
         }

         public HashCode hash() {
            return AbstractCompositeHashFunction.this.makeHash(var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putString(CharSequence var1x, Charset var2) {
            return this.putString(var1x, var2);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putUnencodedChars(CharSequence var1x) {
            return this.putUnencodedChars(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putChar(char var1x) {
            return this.putChar(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putBoolean(boolean var1x) {
            return this.putBoolean(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putDouble(double var1x) {
            return this.putDouble(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putFloat(float var1x) {
            return this.putFloat(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putLong(long var1x) {
            return this.putLong(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putInt(int var1x) {
            return this.putInt(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putShort(short var1x) {
            return this.putShort(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putBytes(byte[] var1x, int var2, int var3) {
            return this.putBytes(var1x, var2, var3);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putBytes(byte[] var1x) {
            return this.putBytes(var1x);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public PrimitiveSink putByte(byte var1x) {
            return this.putByte(var1x);
         }
      };
   }
}
