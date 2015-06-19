package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

public final class ParameterizedMessageFactory extends AbstractMessageFactory {
   public static final ParameterizedMessageFactory INSTANCE = new ParameterizedMessageFactory();

   public ParameterizedMessageFactory() {
   }

   public Message newMessage(String var1, Object... var2) {
      return new ParameterizedMessage(var1, var2);
   }
}
