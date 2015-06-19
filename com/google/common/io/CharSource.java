package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSequenceReader;
import com.google.common.io.CharSink;
import com.google.common.io.CharStreams;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import com.google.common.io.MultiReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class CharSource implements InputSupplier<Reader> {
   protected CharSource() {
   }

   public abstract Reader openStream() throws IOException;

   /** @deprecated */
   @Deprecated
   public final Reader getInput() throws IOException {
      return this.openStream();
   }

   public BufferedReader openBufferedStream() throws IOException {
      Reader var1 = this.openStream();
      return var1 instanceof BufferedReader?(BufferedReader)var1:new BufferedReader(var1);
   }

   public long copyTo(Appendable var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      long var4;
      try {
         Reader var3 = (Reader)var2.register(this.openStream());
         var4 = CharStreams.copy((Readable)var3, (Appendable)var1);
      } catch (Throwable var9) {
         throw var2.rethrow(var9);
      } finally {
         var2.close();
      }

      return var4;
   }

   public long copyTo(CharSink var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      long var5;
      try {
         Reader var3 = (Reader)var2.register(this.openStream());
         Writer var4 = (Writer)var2.register(var1.openStream());
         var5 = CharStreams.copy((Readable)var3, (Appendable)var4);
      } catch (Throwable var10) {
         throw var2.rethrow(var10);
      } finally {
         var2.close();
      }

      return var5;
   }

   public String read() throws IOException {
      Closer var1 = Closer.create();

      String var3;
      try {
         Reader var2 = (Reader)var1.register(this.openStream());
         var3 = CharStreams.toString((Readable)var2);
      } catch (Throwable var7) {
         throw var1.rethrow(var7);
      } finally {
         var1.close();
      }

      return var3;
   }

   @Nullable
   public String readFirstLine() throws IOException {
      Closer var1 = Closer.create();

      String var3;
      try {
         BufferedReader var2 = (BufferedReader)var1.register(this.openBufferedStream());
         var3 = var2.readLine();
      } catch (Throwable var7) {
         throw var1.rethrow(var7);
      } finally {
         var1.close();
      }

      return var3;
   }

   public ImmutableList<String> readLines() throws IOException {
      Closer var1 = Closer.create();

      try {
         BufferedReader var2 = (BufferedReader)var1.register(this.openBufferedStream());
         ArrayList var3 = Lists.newArrayList();

         String var4;
         while((var4 = var2.readLine()) != null) {
            var3.add(var4);
         }

         ImmutableList var5 = ImmutableList.copyOf((Collection)var3);
         return var5;
      } catch (Throwable var9) {
         throw var1.rethrow(var9);
      } finally {
         var1.close();
      }
   }

   @Beta
   public <T> T readLines(LineProcessor<T> var1) throws IOException {
      Preconditions.checkNotNull(var1);
      Closer var2 = Closer.create();

      Object var4;
      try {
         Reader var3 = (Reader)var2.register(this.openStream());
         var4 = CharStreams.readLines((Readable)var3, var1);
      } catch (Throwable var8) {
         throw var2.rethrow(var8);
      } finally {
         var2.close();
      }

      return var4;
   }

   public boolean isEmpty() throws IOException {
      Closer var1 = Closer.create();

      boolean var3;
      try {
         Reader var2 = (Reader)var1.register(this.openStream());
         var3 = var2.read() == -1;
      } catch (Throwable var7) {
         throw var1.rethrow(var7);
      } finally {
         var1.close();
      }

      return var3;
   }

   public static CharSource concat(Iterable<? extends CharSource> var0) {
      return new CharSource.ConcatenatedCharSource(var0);
   }

   public static CharSource concat(Iterator<? extends CharSource> var0) {
      return concat((Iterable)ImmutableList.copyOf(var0));
   }

   public static CharSource concat(CharSource... var0) {
      return concat((Iterable)ImmutableList.copyOf((Object[])var0));
   }

   public static CharSource wrap(CharSequence var0) {
      return new CharSource.CharSequenceCharSource(var0);
   }

   public static CharSource empty() {
      return CharSource.EmptyCharSource.INSTANCE;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object getInput() throws IOException {
      return this.getInput();
   }

   private static final class ConcatenatedCharSource extends CharSource {
      private final Iterable<? extends CharSource> sources;

      ConcatenatedCharSource(Iterable<? extends CharSource> var1) {
         this.sources = (Iterable)Preconditions.checkNotNull(var1);
      }

      public Reader openStream() throws IOException {
         return new MultiReader(this.sources.iterator());
      }

      public boolean isEmpty() throws IOException {
         Iterator var1 = this.sources.iterator();

         CharSource var2;
         do {
            if(!var1.hasNext()) {
               return true;
            }

            var2 = (CharSource)var1.next();
         } while(var2.isEmpty());

         return false;
      }

      public String toString() {
         return "CharSource.concat(" + this.sources + ")";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object getInput() throws IOException {
         return super.getInput();
      }
   }

   private static final class EmptyCharSource extends CharSource.CharSequenceCharSource {
      private static final CharSource.EmptyCharSource INSTANCE = new CharSource.EmptyCharSource();

      private EmptyCharSource() {
         super("");
      }

      public String toString() {
         return "CharSource.empty()";
      }
   }

   private static class CharSequenceCharSource extends CharSource {
      private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
      private final CharSequence seq;

      protected CharSequenceCharSource(CharSequence var1) {
         this.seq = (CharSequence)Preconditions.checkNotNull(var1);
      }

      public Reader openStream() {
         return new CharSequenceReader(this.seq);
      }

      public String read() {
         return this.seq.toString();
      }

      public boolean isEmpty() {
         return this.seq.length() == 0;
      }

      private Iterable<String> lines() {
         return new Iterable() {
            public Iterator<String> iterator() {
               return new AbstractIterator() {
                  Iterator<String> lines;

                  {
                     this.lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                  }

                  protected String computeNext() {
                     if(this.lines.hasNext()) {
                        String var1 = (String)this.lines.next();
                        if(this.lines.hasNext() || !var1.isEmpty()) {
                           return var1;
                        }
                     }

                     return (String)this.endOfData();
                  }

                  // $FF: synthetic method
                  // $FF: bridge method
                  protected Object computeNext() {
                     return this.computeNext();
                  }
               };
            }
         };
      }

      public String readFirstLine() {
         Iterator var1 = this.lines().iterator();
         return var1.hasNext()?(String)var1.next():null;
      }

      public ImmutableList<String> readLines() {
         return ImmutableList.copyOf(this.lines());
      }

      public <T> T readLines(LineProcessor<T> var1) throws IOException {
         Iterator var2 = this.lines().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if(!var1.processLine(var3)) {
               break;
            }
         }

         return var1.getResult();
      }

      public String toString() {
         return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object getInput() throws IOException {
         return super.getInput();
      }
   }
}
