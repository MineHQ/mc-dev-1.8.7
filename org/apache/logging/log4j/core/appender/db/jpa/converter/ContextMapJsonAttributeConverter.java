package org.apache.logging.log4j.core.appender.db.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
   autoApply = false
)
public class ContextMapJsonAttributeConverter implements AttributeConverter<Map<String, String>, String> {
   static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   public ContextMapJsonAttributeConverter() {
   }

   public String convertToDatabaseColumn(Map<String, String> var1) {
      if(var1 == null) {
         return null;
      } else {
         try {
            return OBJECT_MAPPER.writeValueAsString(var1);
         } catch (IOException var3) {
            throw new PersistenceException("Failed to convert map to JSON string.", var3);
         }
      }
   }

   public Map<String, String> convertToEntityAttribute(String var1) {
      if(Strings.isEmpty(var1)) {
         return null;
      } else {
         try {
            return (Map)OBJECT_MAPPER.readValue(var1, new TypeReference() {
            });
         } catch (IOException var3) {
            throw new PersistenceException("Failed to convert JSON string to map.", var3);
         }
      }
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
