package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.JdkSslClientContext;
import io.netty.handler.ssl.JdkSslServerContext;
import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.OpenSslServerContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;
import java.io.File;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;

public abstract class SslContext {
   public static SslProvider defaultServerProvider() {
      return OpenSsl.isAvailable()?SslProvider.OPENSSL:SslProvider.JDK;
   }

   public static SslProvider defaultClientProvider() {
      return SslProvider.JDK;
   }

   public static SslContext newServerContext(File var0, File var1) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, (String)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(File var0, File var1, String var2) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, var2, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(File var0, File var1, String var2, Iterable<String> var3, Iterable<String> var4, long var5, long var7) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, var2, var3, var4, var5, var7);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2) throws SSLException {
      return newServerContext(var0, var1, var2, (String)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2, String var3) throws SSLException {
      return newServerContext(var0, var1, var2, var3, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2, String var3, Iterable<String> var4, Iterable<String> var5, long var6, long var8) throws SSLException {
      if(var0 == null) {
         var0 = OpenSsl.isAvailable()?SslProvider.OPENSSL:SslProvider.JDK;
      }

      switch(SslContext.SyntheticClass_1.$SwitchMap$io$netty$handler$ssl$SslProvider[var0.ordinal()]) {
      case 1:
         return new JdkSslServerContext(var1, var2, var3, var4, var5, var6, var8);
      case 2:
         return new OpenSslServerContext(var1, var2, var3, var4, var5, var6, var8);
      default:
         throw new Error(var0.toString());
      }
   }

   public static SslContext newClientContext() throws SSLException {
      return newClientContext((SslProvider)null, (File)null, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0) throws SSLException {
      return newClientContext((SslProvider)null, var0, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(TrustManagerFactory var0) throws SSLException {
      return newClientContext((SslProvider)null, (File)null, var0, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0, TrustManagerFactory var1) throws SSLException {
      return newClientContext((SslProvider)null, var0, var1, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0, TrustManagerFactory var1, Iterable<String> var2, Iterable<String> var3, long var4, long var6) throws SSLException {
      return newClientContext((SslProvider)null, var0, var1, var2, var3, var4, var6);
   }

   public static SslContext newClientContext(SslProvider var0) throws SSLException {
      return newClientContext(var0, (File)null, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1) throws SSLException {
      return newClientContext(var0, var1, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, TrustManagerFactory var1) throws SSLException {
      return newClientContext(var0, (File)null, var1, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1, TrustManagerFactory var2) throws SSLException {
      return newClientContext(var0, var1, var2, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1, TrustManagerFactory var2, Iterable<String> var3, Iterable<String> var4, long var5, long var7) throws SSLException {
      if(var0 != null && var0 != SslProvider.JDK) {
         throw new SSLException("client context unsupported for: " + var0);
      } else {
         return new JdkSslClientContext(var1, var2, var3, var4, var5, var7);
      }
   }

   SslContext() {
   }

   public final boolean isServer() {
      return !this.isClient();
   }

   public abstract boolean isClient();

   public abstract List<String> cipherSuites();

   public abstract long sessionCacheSize();

   public abstract long sessionTimeout();

   public abstract List<String> nextProtocols();

   public abstract SSLEngine newEngine(ByteBufAllocator var1);

   public abstract SSLEngine newEngine(ByteBufAllocator var1, String var2, int var3);

   public final SslHandler newHandler(ByteBufAllocator var1) {
      return newHandler(this.newEngine(var1));
   }

   public final SslHandler newHandler(ByteBufAllocator var1, String var2, int var3) {
      return newHandler(this.newEngine(var1, var2, var3));
   }

   private static SslHandler newHandler(SSLEngine var0) {
      return new SslHandler(var0);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] $SwitchMap$io$netty$handler$ssl$SslProvider = new int[SslProvider.values().length];

      static {
         try {
            $SwitchMap$io$netty$handler$ssl$SslProvider[SslProvider.JDK.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$io$netty$handler$ssl$SslProvider[SslProvider.OPENSSL.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
