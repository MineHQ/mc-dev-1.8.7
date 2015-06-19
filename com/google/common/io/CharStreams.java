package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.io.AppendableWriter;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import com.google.common.io.LineReader;
import com.google.common.io.OutputSupplier;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Beta
public final class CharStreams {
   private static final int BUF_SIZE = 2048;

   private CharStreams() {
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<StringReader> newReaderSupplier(String var0) {
      return asInputSupplier(CharSource.wrap(var0));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> var0, Charset var1) {
      return asInputSupplier(ByteStreams.asByteSource(var0).asCharSource(var1));
   }

   /** @deprecated */
   @Deprecated
   public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> var0, Charset var1) {
      return asOutputSupplier(ByteStreams.asByteSink(var0).asCharSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static <W extends Appendable & Closeable> void write(CharSequence var0, OutputSupplier<W> var1) throws IOException {
      asCharSink(var1).write(var0);
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable, W extends Appendable & Closeable> long copy(InputSupplier<R> var0, OutputSupplier<W> var1) throws IOException {
      return asCharSource(var0).copyTo(asCharSink(var1));
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable> long copy(InputSupplier<R> var0, Appendable var1) throws IOException {
      return asCharSource(var0).copyTo(var1);
   }

   public static long copy(Readable var0, Appendable var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      CharBuffer var2 = CharBuffer.allocate(2048);
      long var3 = 0L;

      while(var0.read(var2) != -1) {
         var2.flip();
         var1.append(var2);
         var3 += (long)var2.remaining();
         var2.clear();
      }

      return var3;
   }

   public static String toString(Readable var0) throws IOException {
      return toStringBuilder(var0).toString();
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable> String toString(InputSupplier<R> var0) throws IOException {
      return asCharSource(var0).read();
   }

   private static StringBuilder toStringBuilder(Readable var0) throws IOException {
      StringBuilder var1 = new StringBuilder();
      copy((Readable)var0, (Appendable)var1);
      return var1;
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable> String readFirstLine(InputSupplier<R> var0) throws IOException {
      return asCharSource(var0).readFirstLine();
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable> List<String> readLines(InputSupplier<R> var0) throws IOException {
      Closer var1 = Closer.create();

      List var3;
      try {
         Readable var2 = (Readable)var1.register((Closeable)var0.getInput());
         var3 = readLines(var2);
      } catch (Throwable var7) {
         throw var1.rethrow(var7);
      } finally {
         var1.close();
      }

      return var3;
   }

   public static List<String> readLines(Readable var0) throws IOException {
      ArrayList var1 = new ArrayList();
      LineReader var2 = new LineReader(var0);

      String var3;
      while((var3 = var2.readLine()) != null) {
         var1.add(var3);
      }

      return var1;
   }

   public static <T> T readLines(Readable var0, LineProcessor<T> var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      LineReader var2 = new LineReader(var0);

      String var3;
      while((var3 = var2.readLine()) != null && var1.processLine(var3)) {
         ;
      }

      return var1.getResult();
   }

   /** @deprecated */
   @Deprecated
   public static <R extends Readable & Closeable, T> T readLines(InputSupplier<R> var0, LineProcessor<T> var1) throws IOException {
      Preconditions.checkNotNull(var0);
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      Object var4;
      try {
         Readable var3 = (Readable)var2.register((Closeable)var0.getInput());
         var4 = readLines(var3, var1);
      } catch (Throwable var8) {
         throw var2.rethrow(var8);
      } finally {
         var2.close();
      }

      return var4;
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<Reader> join(Iterable<? extends InputSupplier<? extends Reader>> var0) {
      Preconditions.checkNotNull(var0);
      Iterable var1 = Iterables.transform(var0, new Function() {
         public CharSource apply(InputSupplier<? extends Reader> var1) {
            return CharStreams.asCharSource(var1);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object apply(Object var1) {
            return this.apply((InputSupplier)var1);
         }
      });
      return asInputSupplier(CharSource.concat(var1));
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<Reader> join(InputSupplier... var0) {
      return join((Iterable)Arrays.asList(var0));
   }

   public static void skipFully(Reader var0, long var1) throws IOException {
      Preconditions.checkNotNull(var0);

      while(var1 > 0L) {
         long var3 = var0.skip(var1);
         if(var3 == 0L) {
            if(var0.read() == -1) {
               throw new EOFException();
            }

            --var1;
         } else {
            var1 -= var3;
         }
      }

   }

   public static Writer nullWriter() {
      return CharStreams.NullWriter.INSTANCE;
   }

   public static Writer asWriter(Appendable var0) {
      return (Writer)(var0 instanceof Writer?(Writer)var0:new AppendableWriter(var0));
   }

   static Reader asReader(final Readable var0) {
      Preconditions.checkNotNull(var0);
      return var0 instanceof Reader?(Reader)var0:new Reader() {
         public int read(char[] var1, int var2, int var3) throws IOException {
            return this.read(CharBuffer.wrap(var1, var2, var3));
         }

         public int read(CharBuffer var1) throws IOException {
            return var0.read(var1);
         }

         public void close() throws IOException {
            if(var0 instanceof Closeable) {
               ((Closeable)var0).close();
            }

         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static CharSource asCharSource(final InputSupplier<? extends Readable> var0) {
      Preconditions.checkNotNull(var0);
      return new CharSource() {
         public Reader openStream() throws IOException {
            return CharStreams.asReader((Readable)var0.getInput());
         }

         public String toString() {
            return "CharStreams.asCharSource(" + var0 + ")";
         }
      };
   }

   /** @deprecated */
   @Deprecated
   public static CharSink asCharSink(final OutputSupplier<? extends Appendable> var0) {
      Preconditions.checkNotNull(var0);
      return new CharSink() {
         public Writer openStream() throws IOException {
            return CharStreams.asWriter((Appendable)var0.getOutput());
         }

         public String toString() {
            return "CharStreams.asCharSink(" + var0 + ")";
         }
      };
   }

   static <R extends Reader> InputSupplier<R> asInputSupplier(CharSource var0) {
      return (InputSupplier)Preconditions.checkNotNull(var0);
   }

   static <W extends Writer> OutputSupplier<W> asOutputSupplier(CharSink var0) {
      return (OutputSupplier)Preconditions.checkNotNull(var0);
   }

   private static final class NullWriter extends Writer {
      private static final CharStreams.NullWriter INSTANCE = new CharStreams.NullWriter();

      private NullWriter() {
      }

      public void write(int var1) {
      }

      public void write(char[] var1) {
         Preconditions.checkNotNull(var1);
      }

      public void write(char[] var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length);
      }

      public void write(String var1) {
         Preconditions.checkNotNull(var1);
      }

      public void write(String var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var2 + var3, var1.length());
      }

      public Writer append(CharSequence var1) {
         Preconditions.checkNotNull(var1);
         return this;
      }

      public Writer append(CharSequence var1, int var2, int var3) {
         Preconditions.checkPositionIndexes(var2, var3, var1.length());
         return this;
      }

      public Writer append(char var1) {
         return this;
      }

      public void flush() {
      }

      public void close() {
      }

      public String toString() {
         return "CharStreams.nullWriter()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Appendable append(char var1) throws IOException {
         return this.append(var1);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
         return this.append(var1, var2, var3);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Appendable append(CharSequence var1) throws IOException {
         return this.append(var1);
      }
   }
}
