package org.apache.logging.log4j.core.net;

import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.net.AbstractJMSManager;

public class JMSTopicManager extends AbstractJMSManager {
   private static final JMSTopicManager.JMSTopicManagerFactory FACTORY = new JMSTopicManager.JMSTopicManagerFactory();
   private JMSTopicManager.TopicInfo info;
   private final String factoryBindingName;
   private final String topicBindingName;
   private final String userName;
   private final String password;
   private final Context context;

   protected JMSTopicManager(String var1, Context var2, String var3, String var4, String var5, String var6, JMSTopicManager.TopicInfo var7) {
      super(var1);
      this.context = var2;
      this.factoryBindingName = var3;
      this.topicBindingName = var4;
      this.userName = var5;
      this.password = var6;
      this.info = var7;
   }

   public static JMSTopicManager getJMSTopicManager(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
      if(var5 == null) {
         LOGGER.error("No factory name provided for JMSTopicManager");
         return null;
      } else if(var6 == null) {
         LOGGER.error("No topic name provided for JMSTopicManager");
         return null;
      } else {
         String var9 = "JMSTopic:" + var5 + '.' + var6;
         return (JMSTopicManager)getManager(var9, FACTORY, new JMSTopicManager.FactoryData(var0, var1, var2, var3, var4, var5, var6, var7, var8));
      }
   }

   public void send(Serializable var1) throws Exception {
      if(this.info == null) {
         this.info = connect(this.context, this.factoryBindingName, this.topicBindingName, this.userName, this.password, false);
      }

      try {
         super.send(var1, this.info.session, this.info.publisher);
      } catch (Exception var3) {
         this.cleanup(true);
         throw var3;
      }
   }

   public void releaseSub() {
      if(this.info != null) {
         this.cleanup(false);
      }

   }

   private void cleanup(boolean var1) {
      try {
         this.info.session.close();
      } catch (Exception var4) {
         if(!var1) {
            LOGGER.error((String)("Error closing session for " + this.getName()), (Throwable)var4);
         }
      }

      try {
         this.info.conn.close();
      } catch (Exception var3) {
         if(!var1) {
            LOGGER.error((String)("Error closing connection for " + this.getName()), (Throwable)var3);
         }
      }

      this.info = null;
   }

   private static JMSTopicManager.TopicInfo connect(Context var0, String var1, String var2, String var3, String var4, boolean var5) throws Exception {
      try {
         TopicConnectionFactory var6 = (TopicConnectionFactory)lookup(var0, var1);
         TopicConnection var7;
         if(var3 != null) {
            var7 = var6.createTopicConnection(var3, var4);
         } else {
            var7 = var6.createTopicConnection();
         }

         TopicSession var8 = var7.createTopicSession(false, 1);
         Topic var9 = (Topic)lookup(var0, var2);
         TopicPublisher var10 = var8.createPublisher(var9);
         var7.start();
         return new JMSTopicManager.TopicInfo(var7, var8, var10);
      } catch (NamingException var11) {
         LOGGER.warn((String)("Unable to locate connection factory " + var1), (Throwable)var11);
         if(!var5) {
            throw var11;
         }
      } catch (JMSException var12) {
         LOGGER.warn((String)("Unable to create connection to queue " + var2), (Throwable)var12);
         if(!var5) {
            throw var12;
         }
      }

      return null;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class JMSTopicManagerFactory implements ManagerFactory<JMSTopicManager, JMSTopicManager.FactoryData> {
      private JMSTopicManagerFactory() {
      }

      public JMSTopicManager createManager(String var1, JMSTopicManager.FactoryData var2) {
         try {
            Context var3 = AbstractJMSManager.createContext(var2.factoryName, var2.providerURL, var2.urlPkgPrefixes, var2.securityPrincipalName, var2.securityCredentials);
            JMSTopicManager.TopicInfo var4 = JMSTopicManager.connect(var3, var2.factoryBindingName, var2.topicBindingName, var2.userName, var2.password, true);
            return new JMSTopicManager(var1, var3, var2.factoryBindingName, var2.topicBindingName, var2.userName, var2.password, var4);
         } catch (NamingException var5) {
            JMSTopicManager.LOGGER.error((String)"Unable to locate resource", (Throwable)var5);
         } catch (Exception var6) {
            JMSTopicManager.LOGGER.error((String)"Unable to connect", (Throwable)var6);
         }

         return null;
      }

      // $FF: synthetic method
      // $FF: bridge method
      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (JMSTopicManager.FactoryData)var2);
      }

      // $FF: synthetic method
      JMSTopicManagerFactory(JMSTopicManager.SyntheticClass_1 var1) {
         this();
      }
   }

   private static class TopicInfo {
      private final TopicConnection conn;
      private final TopicSession session;
      private final TopicPublisher publisher;

      public TopicInfo(TopicConnection var1, TopicSession var2, TopicPublisher var3) {
         this.conn = var1;
         this.session = var2;
         this.publisher = var3;
      }
   }

   private static class FactoryData {
      private final String factoryName;
      private final String providerURL;
      private final String urlPkgPrefixes;
      private final String securityPrincipalName;
      private final String securityCredentials;
      private final String factoryBindingName;
      private final String topicBindingName;
      private final String userName;
      private final String password;

      public FactoryData(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9) {
         this.factoryName = var1;
         this.providerURL = var2;
         this.urlPkgPrefixes = var3;
         this.securityPrincipalName = var4;
         this.securityCredentials = var5;
         this.factoryBindingName = var6;
         this.topicBindingName = var7;
         this.userName = var8;
         this.password = var9;
      }
   }
}
