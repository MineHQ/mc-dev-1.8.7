package com.sun.jna;

import com.sun.jna.FromNativeConverter;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultTypeMapper implements TypeMapper {
   private List toNativeConverters = new ArrayList();
   private List fromNativeConverters = new ArrayList();

   public DefaultTypeMapper() {
   }

   private Class getAltClass(Class var1) {
      return var1 == Boolean.class?Boolean.TYPE:(var1 == Boolean.TYPE?Boolean.class:(var1 == Byte.class?Byte.TYPE:(var1 == Byte.TYPE?Byte.class:(var1 == Character.class?Character.TYPE:(var1 == Character.TYPE?Character.class:(var1 == Short.class?Short.TYPE:(var1 == Short.TYPE?Short.class:(var1 == Integer.class?Integer.TYPE:(var1 == Integer.TYPE?Integer.class:(var1 == Long.class?Long.TYPE:(var1 == Long.TYPE?Long.class:(var1 == Float.class?Float.TYPE:(var1 == Float.TYPE?Float.class:(var1 == Double.class?Double.TYPE:(var1 == Double.TYPE?Double.class:null)))))))))))))));
   }

   public void addToNativeConverter(Class var1, ToNativeConverter var2) {
      this.toNativeConverters.add(new DefaultTypeMapper.Entry(var1, var2));
      Class var3 = this.getAltClass(var1);
      if(var3 != null) {
         this.toNativeConverters.add(new DefaultTypeMapper.Entry(var3, var2));
      }

   }

   public void addFromNativeConverter(Class var1, FromNativeConverter var2) {
      this.fromNativeConverters.add(new DefaultTypeMapper.Entry(var1, var2));
      Class var3 = this.getAltClass(var1);
      if(var3 != null) {
         this.fromNativeConverters.add(new DefaultTypeMapper.Entry(var3, var2));
      }

   }

   protected void addTypeConverter(Class var1, TypeConverter var2) {
      this.addFromNativeConverter(var1, var2);
      this.addToNativeConverter(var1, var2);
   }

   private Object lookupConverter(Class var1, List var2) {
      Iterator var3 = var2.iterator();

      DefaultTypeMapper.Entry var4;
      do {
         if(!var3.hasNext()) {
            return null;
         }

         var4 = (DefaultTypeMapper.Entry)var3.next();
      } while(!var4.type.isAssignableFrom(var1));

      return var4.converter;
   }

   public FromNativeConverter getFromNativeConverter(Class var1) {
      return (FromNativeConverter)this.lookupConverter(var1, this.fromNativeConverters);
   }

   public ToNativeConverter getToNativeConverter(Class var1) {
      return (ToNativeConverter)this.lookupConverter(var1, this.toNativeConverters);
   }

   private static class Entry {
      public Class type;
      public Object converter;

      public Entry(Class var1, Object var2) {
         this.type = var1;
         this.converter = var2;
      }
   }
}
