package io.netty.handler.ssl;

import io.netty.handler.ssl.JettyNpnSslSession;
import java.nio.ByteBuffer;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import org.eclipse.jetty.npn.NextProtoNego;
import org.eclipse.jetty.npn.NextProtoNego.ClientProvider;
import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;

final class JettyNpnSslEngine extends SSLEngine {
   private static boolean available;
   private final SSLEngine engine;
   private final JettyNpnSslSession session;

   static boolean isAvailable() {
      updateAvailability();
      return available;
   }

   private static void updateAvailability() {
      if(!available) {
         try {
            ClassLoader var0 = ClassLoader.getSystemClassLoader().getParent();
            if(var0 == null) {
               var0 = ClassLoader.getSystemClassLoader();
            }

            Class.forName("sun.security.ssl.NextProtoNegoExtension", true, var0);
            available = true;
         } catch (Exception var1) {
            ;
         }

      }
   }

   JettyNpnSslEngine(SSLEngine var1, final List<String> var2, boolean var3) {
      assert !var2.isEmpty();

      this.engine = var1;
      this.session = new JettyNpnSslSession(var1);
      if(var3) {
         NextProtoNego.put(var1, new ServerProvider() {
            public void unsupported() {
               JettyNpnSslEngine.this.getSession().setApplicationProtocol((String)var2.get(var2.size() - 1));
            }

            public List<String> protocols() {
               return var2;
            }

            public void protocolSelected(String var1) {
               JettyNpnSslEngine.this.getSession().setApplicationProtocol(var1);
            }
         });
      } else {
         final String[] var4 = (String[])var2.toArray(new String[var2.size()]);
         final String var5 = var4[var4.length - 1];
         NextProtoNego.put(var1, new ClientProvider() {
            public boolean supports() {
               return true;
            }

            public void unsupported() {
               JettyNpnSslEngine.this.session.setApplicationProtocol((String)null);
            }

            public String selectProtocol(List<String> var1) {
               String[] var2 = var4;
               int var3 = var2.length;

               for(int var4x = 0; var4x < var3; ++var4x) {
                  String var5x = var2[var4x];
                  if(var1.contains(var5x)) {
                     return var5x;
                  }
               }

               return var5;
            }
         });
      }

   }

   public JettyNpnSslSession getSession() {
      return this.session;
   }

   public void closeInbound() throws SSLException {
      NextProtoNego.remove(this.engine);
      this.engine.closeInbound();
   }

   public void closeOutbound() {
      NextProtoNego.remove(this.engine);
      this.engine.closeOutbound();
   }

   public String getPeerHost() {
      return this.engine.getPeerHost();
   }

   public int getPeerPort() {
      return this.engine.getPeerPort();
   }

   public SSLEngineResult wrap(ByteBuffer var1, ByteBuffer var2) throws SSLException {
      return this.engine.wrap(var1, var2);
   }

   public SSLEngineResult wrap(ByteBuffer[] var1, ByteBuffer var2) throws SSLException {
      return this.engine.wrap(var1, var2);
   }

   public SSLEngineResult wrap(ByteBuffer[] var1, int var2, int var3, ByteBuffer var4) throws SSLException {
      return this.engine.wrap(var1, var2, var3, var4);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer var2) throws SSLException {
      return this.engine.unwrap(var1, var2);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2) throws SSLException {
      return this.engine.unwrap(var1, var2);
   }

   public SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2, int var3, int var4) throws SSLException {
      return this.engine.unwrap(var1, var2, var3, var4);
   }

   public Runnable getDelegatedTask() {
      return this.engine.getDelegatedTask();
   }

   public boolean isInboundDone() {
      return this.engine.isInboundDone();
   }

   public boolean isOutboundDone() {
      return this.engine.isOutboundDone();
   }

   public String[] getSupportedCipherSuites() {
      return this.engine.getSupportedCipherSuites();
   }

   public String[] getEnabledCipherSuites() {
      return this.engine.getEnabledCipherSuites();
   }

   public void setEnabledCipherSuites(String[] var1) {
      this.engine.setEnabledCipherSuites(var1);
   }

   public String[] getSupportedProtocols() {
      return this.engine.getSupportedProtocols();
   }

   public String[] getEnabledProtocols() {
      return this.engine.getEnabledProtocols();
   }

   public void setEnabledProtocols(String[] var1) {
      this.engine.setEnabledProtocols(var1);
   }

   public SSLSession getHandshakeSession() {
      return this.engine.getHandshakeSession();
   }

   public void beginHandshake() throws SSLException {
      this.engine.beginHandshake();
   }

   public HandshakeStatus getHandshakeStatus() {
      return this.engine.getHandshakeStatus();
   }

   public void setUseClientMode(boolean var1) {
      this.engine.setUseClientMode(var1);
   }

   public boolean getUseClientMode() {
      return this.engine.getUseClientMode();
   }

   public void setNeedClientAuth(boolean var1) {
      this.engine.setNeedClientAuth(var1);
   }

   public boolean getNeedClientAuth() {
      return this.engine.getNeedClientAuth();
   }

   public void setWantClientAuth(boolean var1) {
      this.engine.setWantClientAuth(var1);
   }

   public boolean getWantClientAuth() {
      return this.engine.getWantClientAuth();
   }

   public void setEnableSessionCreation(boolean var1) {
      this.engine.setEnableSessionCreation(var1);
   }

   public boolean getEnableSessionCreation() {
      return this.engine.getEnableSessionCreation();
   }

   public SSLParameters getSSLParameters() {
      return this.engine.getSSLParameters();
   }

   public void setSSLParameters(SSLParameters var1) {
      this.engine.setSSLParameters(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SSLSession getSession() {
      return this.getSession();
   }
}
