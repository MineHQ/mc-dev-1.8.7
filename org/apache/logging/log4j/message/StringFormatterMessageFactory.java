package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StringFormattedMessage;

public final class StringFormatterMessageFactory extends AbstractMessageFactory {
   public static final StringFormatterMessageFactory INSTANCE = new StringFormatterMessageFactory();

   public StringFormatterMessageFactory() {
   }

   public Message newMessage(String var1, Object... var2) {
      return new StringFormattedMessage(var1, var2);
   }
}
