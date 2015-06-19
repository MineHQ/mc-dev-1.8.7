package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.SimpleMessage;

public abstract class AbstractMessageFactory implements MessageFactory {
   public AbstractMessageFactory() {
   }

   public Message newMessage(Object var1) {
      return new ObjectMessage(var1);
   }

   public Message newMessage(String var1) {
      return new SimpleMessage(var1);
   }

   public abstract Message newMessage(String var1, Object... var2);
}
