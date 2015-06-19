package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFormatMessage;

public class MessageFormatMessageFactory extends AbstractMessageFactory {
   public MessageFormatMessageFactory() {
   }

   public Message newMessage(String var1, Object... var2) {
      return new MessageFormatMessage(var1, var2);
   }
}
