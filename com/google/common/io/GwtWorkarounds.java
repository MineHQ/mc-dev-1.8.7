package com.google.common.io;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

@GwtCompatible(
   emulated = true
)
final class GwtWorkarounds {
   private GwtWorkarounds() {
   }

   @GwtIncompatible("Reader")
   static GwtWorkarounds.CharInput asCharInput(final Reader var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharInput() {
         public int read() throws IOException {
            return var0.read();
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   static GwtWorkarounds.CharInput asCharInput(final CharSequence var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharInput() {
         int index = 0;

         public int read() {
            return this.index < var0.length()?var0.charAt(this.index++):-1;
         }

         public void close() {
            this.index = var0.length();
         }
      };
   }

   @GwtIncompatible("InputStream")
   static InputStream asInputStream(final GwtWorkarounds.ByteInput var0) {
      Preconditions.checkNotNull(var0);
      return new InputStream() {
         public int read() throws IOException {
            return var0.read();
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            Preconditions.checkNotNull(var1);
            Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
            if(var3 == 0) {
               return 0;
            } else {
               int var4 = this.read();
               if(var4 == -1) {
                  return -1;
               } else {
                  var1[var2] = (byte)var4;

                  for(int var5 = 1; var5 < var3; ++var5) {
                     int var6 = this.read();
                     if(var6 == -1) {
                        return var5;
                     }

                     var1[var2 + var5] = (byte)var6;
                  }

                  return var3;
               }
            }
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   @GwtIncompatible("OutputStream")
   static OutputStream asOutputStream(final GwtWorkarounds.ByteOutput var0) {
      Preconditions.checkNotNull(var0);
      return new OutputStream() {
         public void write(int var1) throws IOException {
            var0.write((byte)var1);
         }

         public void flush() throws IOException {
            var0.flush();
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   @GwtIncompatible("Writer")
   static GwtWorkarounds.CharOutput asCharOutput(final Writer var0) {
      Preconditions.checkNotNull(var0);
      return new GwtWorkarounds.CharOutput() {
         public void write(char var1) throws IOException {
            var0.append(var1);
         }

         public void flush() throws IOException {
            var0.flush();
         }

         public void close() throws IOException {
            var0.close();
         }
      };
   }

   static GwtWorkarounds.CharOutput stringBuilderOutput(int var0) {
      final StringBuilder var1 = new StringBuilder(var0);
      return new GwtWorkarounds.CharOutput() {
         public void write(char var1x) {
            var1.append(var1x);
         }

         public void flush() {
         }

         public void close() {
         }

         public String toString() {
            return var1.toString();
         }
      };
   }

   interface CharOutput {
      void write(char var1) throws IOException;

      void flush() throws IOException;

      void close() throws IOException;
   }

   interface ByteOutput {
      void write(byte var1) throws IOException;

      void flush() throws IOException;

      void close() throws IOException;
   }

   interface ByteInput {
      int read() throws IOException;

      void close() throws IOException;
   }

   interface CharInput {
      int read() throws IOException;

      void close() throws IOException;
   }
}
