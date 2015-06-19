package org.apache.logging.log4j.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.CyclicBuffer;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.helpers.NetUtils;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.MimeMessageBuilder;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SMTPManager extends AbstractManager {
   private static final SMTPManager.SMTPManagerFactory FACTORY = new SMTPManager.SMTPManagerFactory();
   private final Session session;
   private final CyclicBuffer<LogEvent> buffer;
   private volatile MimeMessage message;
   private final SMTPManager.FactoryData data;

   protected SMTPManager(String var1, Session var2, MimeMessage var3, SMTPManager.FactoryData var4) {
      super(var1);
      this.session = var2;
      this.message = var3;
      this.data = var4;
      this.buffer = new CyclicBuffer(LogEvent.class, var4.numElements);
   }

   public void add(LogEvent var1) {
      this.buffer.add(var1);
   }

   public static SMTPManager getSMTPManager(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, int var8, String var9, String var10, boolean var11, String var12, int var13) {
      if(Strings.isEmpty(var6)) {
         var6 = "smtp";
      }

      StringBuilder var14 = new StringBuilder();
      if(var0 != null) {
         var14.append(var0);
      }

      var14.append(":");
      if(var1 != null) {
         var14.append(var1);
      }

      var14.append(":");
      if(var2 != null) {
         var14.append(var2);
      }

      var14.append(":");
      if(var3 != null) {
         var14.append(var3);
      }

      var14.append(":");
      if(var4 != null) {
         var14.append(var4);
      }

      var14.append(":");
      if(var5 != null) {
         var14.append(var5);
      }

      var14.append(":");
      var14.append(var6).append(":").append(var7).append(":").append("port").append(":");
      if(var9 != null) {
         var14.append(var9);
      }

      var14.append(":");
      if(var10 != null) {
         var14.append(var10);
      }

      var14.append(var11?":debug:":"::");
      var14.append(var12);
      String var15 = "SMTP:" + NameUtil.md5(var14.toString());
      return (SMTPManager)getManager(var15, FACTORY, new SMTPManager.FactoryData(var0, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var13));
   }

   public void sendEvents(Layout<?> var1, LogEvent var2) {
      if(this.message == null) {
         this.connect();
      }

      try {
         LogEvent[] var3 = (LogEvent[])this.buffer.removeAll();
         byte[] var4 = this.formatContentToBytes(var3, var2, var1);
         String var5 = var1.getContentType();
         String var6 = this.getEncoding(var4, var5);
         byte[] var7 = this.encodeContentToBytes(var4, var6);
         InternetHeaders var8 = this.getHeaders(var5, var6);
         MimeMultipart var9 = this.getMimeMultipart(var7, var8);
         this.sendMultipartMessage(this.message, var9);
      } catch (MessagingException var10) {
         LOGGER.error((String)"Error occurred while sending e-mail notification.", (Throwable)var10);
         throw new LoggingException("Error occurred while sending email", var10);
      } catch (IOException var11) {
         LOGGER.error((String)"Error occurred while sending e-mail notification.", (Throwable)var11);
         throw new LoggingException("Error occurred while sending email", var11);
      } catch (RuntimeException var12) {
         LOGGER.error((String)"Error occurred while sending e-mail notification.", (Throwable)var12);
         throw new LoggingException("Error occurred while sending email", var12);
      }
   }

   protected byte[] formatContentToBytes(LogEvent[] var1, LogEvent var2, Layout<?> var3) throws IOException {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      this.writeContent(var1, var2, var3, var4);
      return var4.toByteArray();
   }

   private void writeContent(LogEvent[] var1, LogEvent var2, Layout<?> var3, ByteArrayOutputStream var4) throws IOException {
      this.writeHeader(var3, var4);
      this.writeBuffer(var1, var2, var3, var4);
      this.writeFooter(var3, var4);
   }

   protected void writeHeader(Layout<?> var1, OutputStream var2) throws IOException {
      byte[] var3 = var1.getHeader();
      if(var3 != null) {
         var2.write(var3);
      }

   }

   protected void writeBuffer(LogEvent[] var1, LogEvent var2, Layout<?> var3, OutputStream var4) throws IOException {
      LogEvent[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         LogEvent var8 = var5[var7];
         byte[] var9 = var3.toByteArray(var8);
         var4.write(var9);
      }

      byte[] var10 = var3.toByteArray(var2);
      var4.write(var10);
   }

   protected void writeFooter(Layout<?> var1, OutputStream var2) throws IOException {
      byte[] var3 = var1.getFooter();
      if(var3 != null) {
         var2.write(var3);
      }

   }

   protected String getEncoding(byte[] var1, String var2) {
      ByteArrayDataSource var3 = new ByteArrayDataSource(var1, var2);
      return MimeUtility.getEncoding(var3);
   }

   protected byte[] encodeContentToBytes(byte[] var1, String var2) throws MessagingException, IOException {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      this.encodeContent(var1, var2, var3);
      return var3.toByteArray();
   }

   protected void encodeContent(byte[] var1, String var2, ByteArrayOutputStream var3) throws MessagingException, IOException {
      OutputStream var4 = MimeUtility.encode(var3, var2);
      var4.write(var1);
      var4.close();
   }

   protected InternetHeaders getHeaders(String var1, String var2) {
      InternetHeaders var3 = new InternetHeaders();
      var3.setHeader("Content-Type", var1 + "; charset=UTF-8");
      var3.setHeader("Content-Transfer-Encoding", var2);
      return var3;
   }

   protected MimeMultipart getMimeMultipart(byte[] var1, InternetHeaders var2) throws MessagingException {
      MimeMultipart var3 = new MimeMultipart();
      MimeBodyPart var4 = new MimeBodyPart(var2, var1);
      var3.addBodyPart(var4);
      return var3;
   }

   protected void sendMultipartMessage(MimeMessage var1, MimeMultipart var2) throws MessagingException {
      synchronized(var1) {
         var1.setContent(var2);
         var1.setSentDate(new Date());
         Transport.send(var1);
      }
   }

   private synchronized void connect() {
      if(this.message == null) {
         try {
            this.message = (new MimeMessageBuilder(this.session)).setFrom(this.data.from).setReplyTo(this.data.replyto).setRecipients(RecipientType.TO, this.data.to).setRecipients(RecipientType.CC, this.data.cc).setRecipients(RecipientType.BCC, this.data.bcc).setSubject(this.data.subject).getMimeMessage();
         } catch (MessagingException var2) {
            LOGGER.error((String)"Could not set SMTPAppender message options.", (Throwable)var2);
            this.message = null;
         }

      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class SMTPManagerFactory implements ManagerFactory<SMTPManager, SMTPManager.FactoryData> {
      private SMTPManagerFactory() {
      }

      public SMTPManager createManager(String var1, SMTPManager.FactoryData var2) {
         String var3 = "mail." + var2.protocol;
         Properties var4 = PropertiesUtil.getSystemProperties();
         var4.put("mail.transport.protocol", var2.protocol);
         if(var4.getProperty("mail.host") == null) {
            var4.put("mail.host", NetUtils.getLocalHostname());
         }

         if(null != var2.host) {
            var4.put(var3 + ".host", var2.host);
         }

         if(var2.port > 0) {
            var4.put(var3 + ".port", String.valueOf(var2.port));
         }

         Authenticator var5 = this.buildAuthenticator(var2.username, var2.password);
         if(null != var5) {
            var4.put(var3 + ".auth", "true");
         }

         Session var6 = Session.getInstance(var4, var5);
         var6.setProtocolForAddress("rfc822", var2.protocol);
         var6.setDebug(var2.isDebug);

         MimeMessage var7;
         try {
            var7 = (new MimeMessageBuilder(var6)).setFrom(var2.from).setReplyTo(var2.replyto).setRecipients(RecipientType.TO, var2.to).setRecipients(RecipientType.CC, var2.cc).setRecipients(RecipientType.BCC, var2.bcc).setSubject(var2.subject).getMimeMessage();
         } catch (MessagingException var9) {
            SMTPManager.LOGGER.error((String)"Could not set SMTPAppender message options.", (Throwable)var9);
            var7 = null;
         }

         return new SMTPManager(var1, var6, var7, var2);
      }

      private Authenticator buildAuthenticator(final String var1, final String var2) {
         return null != var2 && null != var1?new Authenticator() {
            private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication(var1, var2);

            protected PasswordAuthentication getPasswordAuthentication() {
               return this.passwordAuthentication;
            }
         }:null;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (SMTPManager.FactoryData)var2);
      }

      // $FF: synthetic method
      SMTPManagerFactory(SMTPManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class FactoryData {
      private final String to;
      private final String cc;
      private final String bcc;
      private final String from;
      private final String replyto;
      private final String subject;
      private final String protocol;
      private final String host;
      private final int port;
      private final String username;
      private final String password;
      private final boolean isDebug;
      private final int numElements;

      public FactoryData(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9, String var10, String var11, boolean var12, int var13) {
         this.to = var1;
         this.cc = var2;
         this.bcc = var3;
         this.from = var4;
         this.replyto = var5;
         this.subject = var6;
         this.protocol = var7;
         this.host = var8;
         this.port = var9;
         this.username = var10;
         this.password = var11;
         this.isDebug = var12;
         this.numElements = var13;
      }
   }
}
