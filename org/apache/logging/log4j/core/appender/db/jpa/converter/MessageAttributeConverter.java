package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;

@Converter(
   autoApply = false
)
public class MessageAttributeConverter implements AttributeConverter<Message, String> {
   private static final StatusLogger LOGGER = StatusLogger.getLogger();

   public MessageAttributeConverter() {
   }

   public String convertToDatabaseColumn(Message var1) {
      return var1 == null?null:var1.getFormattedMessage();
   }

   public Message convertToEntityAttribute(String var1) {
      return Strings.isEmpty(var1)?null:LOGGER.getMessageFactory().newMessage(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Message)var1);
   }
}
