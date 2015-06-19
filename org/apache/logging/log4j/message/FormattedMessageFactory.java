package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.Message;

public class FormattedMessageFactory extends AbstractMessageFactory {
   public FormattedMessageFactory() {
   }

   public Message newMessage(String var1, Object... var2) {
      return new FormattedMessage(var1, var2);
   }
}
