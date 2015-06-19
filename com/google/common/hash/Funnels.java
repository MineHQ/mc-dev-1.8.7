package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
public final class Funnels {
   private Funnels() {
   }

   public static Funnel<byte[]> byteArrayFunnel() {
      return Funnels.ByteArrayFunnel.INSTANCE;
   }

   public static Funnel<CharSequence> unencodedCharsFunnel() {
      return Funnels.UnencodedCharsFunnel.INSTANCE;
   }

   public static Funnel<CharSequence> stringFunnel(Charset var0) {
      return new Funnels.StringCharsetFunnel(var0);
   }

   public static Funnel<Integer> integerFunnel() {
      return Funnels.IntegerFunnel.INSTANCE;
   }

   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> var0) {
      return new Funnels.SequentialFunnel(var0);
   }

   public static Funnel<Long> longFunnel() {
      return Funnels.LongFunnel.INSTANCE;
   }

   public static OutputStream asOutputStream(PrimitiveSink var0) {
      return new Funnels.SinkAsStream(var0);
   }

   private static class SinkAsStream extends OutputStream {
      final PrimitiveSink sink;

      SinkAsStream(PrimitiveSink var1) {
         this.sink = (PrimitiveSink)Preconditions.checkNotNull(var1);
      }

      public void write(int var1) {
         this.sink.putByte((byte)var1);
      }

      public void write(byte[] var1) {
         this.sink.putBytes(var1);
      }

      public void write(byte[] var1, int var2, int var3) {
         this.sink.putBytes(var1, var2, var3);
      }

      public String toString() {
         return "Funnels.asOutputStream(" + this.sink + ")";
      }
   }

   private static enum LongFunnel implements Funnel<Long> {
      INSTANCE;

      private LongFunnel() {
      }

      public void funnel(Long var1, PrimitiveSink var2) {
         var2.putLong(var1.longValue());
      }

      public String toString() {
         return "Funnels.longFunnel()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((Long)var1, var2);
      }
   }

   private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
      private final Funnel<E> elementFunnel;

      SequentialFunnel(Funnel<E> var1) {
         this.elementFunnel = (Funnel)Preconditions.checkNotNull(var1);
      }

      public void funnel(Iterable<? extends E> var1, PrimitiveSink var2) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            this.elementFunnel.funnel(var4, var2);
         }

      }

      public String toString() {
         return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Funnels.SequentialFunnel) {
            Funnels.SequentialFunnel var2 = (Funnels.SequentialFunnel)var1;
            return this.elementFunnel.equals(var2.elementFunnel);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Funnels.SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((Iterable)var1, var2);
      }
   }

   private static enum IntegerFunnel implements Funnel<Integer> {
      INSTANCE;

      private IntegerFunnel() {
      }

      public void funnel(Integer var1, PrimitiveSink var2) {
         var2.putInt(var1.intValue());
      }

      public String toString() {
         return "Funnels.integerFunnel()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((Integer)var1, var2);
      }
   }

   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
      private final Charset charset;

      StringCharsetFunnel(Charset var1) {
         this.charset = (Charset)Preconditions.checkNotNull(var1);
      }

      public void funnel(CharSequence var1, PrimitiveSink var2) {
         var2.putString(var1, this.charset);
      }

      public String toString() {
         return "Funnels.stringFunnel(" + this.charset.name() + ")";
      }

      public boolean equals(@Nullable Object var1) {
         if(var1 instanceof Funnels.StringCharsetFunnel) {
            Funnels.StringCharsetFunnel var2 = (Funnels.StringCharsetFunnel)var1;
            return this.charset.equals(var2.charset);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Funnels.StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
      }

      Object writeReplace() {
         return new Funnels.StringCharsetFunnel.StringCharsetFunnel$SerializedForm(this.charset);
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((CharSequence)var1, var2);
      }

      private static class StringCharsetFunnel$SerializedForm implements Serializable {
         private final String charsetCanonicalName;
         private static final long serialVersionUID = 0L;

         StringCharsetFunnel$SerializedForm(Charset var1) {
            this.charsetCanonicalName = var1.name();
         }

         private Object readResolve() {
            return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
         }
      }
   }

   private static enum UnencodedCharsFunnel implements Funnel<CharSequence> {
      INSTANCE;

      private UnencodedCharsFunnel() {
      }

      public void funnel(CharSequence var1, PrimitiveSink var2) {
         var2.putUnencodedChars(var1);
      }

      public String toString() {
         return "Funnels.unencodedCharsFunnel()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((CharSequence)var1, var2);
      }
   }

   private static enum ByteArrayFunnel implements Funnel<byte[]> {
      INSTANCE;

      private ByteArrayFunnel() {
      }

      public void funnel(byte[] var1, PrimitiveSink var2) {
         var2.putBytes(var1);
      }

      public String toString() {
         return "Funnels.byteArrayFunnel()";
      }

      // $FF: synthetic method
      // $FF: bridge method
      public void funnel(Object var1, PrimitiveSink var2) {
         this.funnel((byte[])var1, var2);
      }
   }
}
