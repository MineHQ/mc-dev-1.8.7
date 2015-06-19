package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(
   autoApply = false
)
public class ContextMapAttributeConverter implements AttributeConverter<Map<String, String>, String> {
   public ContextMapAttributeConverter() {
   }

   public String convertToDatabaseColumn(Map<String, String> var1) {
      return var1 == null?null:var1.toString();
   }

   public Map<String, String> convertToEntityAttribute(String var1) {
      throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Map)var1);
   }
}
