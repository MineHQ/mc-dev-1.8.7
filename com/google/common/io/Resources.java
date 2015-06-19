package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

@Beta
public final class Resources {
   private Resources() {
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<InputStream> newInputStreamSupplier(URL var0) {
      return ByteStreams.asInputSupplier(asByteSource(var0));
   }

   public static ByteSource asByteSource(URL var0) {
      return new Resources.UrlByteSource(var0, null);
   }

   /** @deprecated */
   @Deprecated
   public static InputSupplier<InputStreamReader> newReaderSupplier(URL var0, Charset var1) {
      return CharStreams.asInputSupplier(asCharSource(var0, var1));
   }

   public static CharSource asCharSource(URL var0, Charset var1) {
      return asByteSource(var0).asCharSource(var1);
   }

   public static byte[] toByteArray(URL var0) throws IOException {
      return asByteSource(var0).read();
   }

   public static String toString(URL var0, Charset var1) throws IOException {
      return asCharSource(var0, var1).read();
   }

   public static <T> T readLines(URL var0, Charset var1, LineProcessor<T> var2) throws IOException {
      return CharStreams.readLines(newReaderSupplier(var0, var1), var2);
   }

   public static List<String> readLines(URL var0, Charset var1) throws IOException {
      return (List)readLines(var0, var1, new LineProcessor() {
         final List<String> result = Lists.newArrayList();

         public boolean processLine(String var1) {
            this.result.add(var1);
            return true;
         }

         public List<String> getResult() {
            return this.result;
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object getResult() {
            return this.getResult();
         }
      });
   }

   public static void copy(URL var0, OutputStream var1) throws IOException {
      asByteSource(var0).copyTo(var1);
   }

   public static URL getResource(String var0) {
      ClassLoader var1 = (ClassLoader)Objects.firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
      URL var2 = var1.getResource(var0);
      Preconditions.checkArgument(var2 != null, "resource %s not found.", new Object[]{var0});
      return var2;
   }

   public static URL getResource(Class<?> var0, String var1) {
      URL var2 = var0.getResource(var1);
      Preconditions.checkArgument(var2 != null, "resource %s relative to %s not found.", new Object[]{var1, var0.getName()});
      return var2;
   }

   private static final class UrlByteSource extends ByteSource {
      private final URL url;

      private UrlByteSource(URL var1) {
         this.url = (URL)Preconditions.checkNotNull(var1);
      }

      public InputStream openStream() throws IOException {
         return this.url.openStream();
      }

      public String toString() {
         return "Resources.asByteSource(" + this.url + ")";
      }

      // $FF: synthetic method
      UrlByteSource(URL var1, Object var2) {
         this(var1);
      }
   }
}
