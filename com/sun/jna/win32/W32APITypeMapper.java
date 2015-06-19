package com.sun.jna.win32;

import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FromNativeContext;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;

public class W32APITypeMapper extends DefaultTypeMapper {
   public static final TypeMapper UNICODE = new W32APITypeMapper(true);
   public static final TypeMapper ASCII = new W32APITypeMapper(false);

   protected W32APITypeMapper(boolean var1) {
      TypeConverter var2;
      if(var1) {
         var2 = new TypeConverter() {
            public Object toNative(Object var1, ToNativeContext var2) {
               return var1 == null?null:(var1 instanceof String[]?new StringArray((String[])((String[])var1), true):new WString(var1.toString()));
            }

            public Object fromNative(Object var1, FromNativeContext var2) {
               return var1 == null?null:var1.toString();
            }

            public Class nativeType() {
               return WString.class;
            }
         };
         this.addTypeConverter(String.class, var2);
         this.addToNativeConverter(String[].class, var2);
      }

      var2 = new TypeConverter() {
         public Object toNative(Object var1, ToNativeContext var2) {
            return new Integer(Boolean.TRUE.equals(var1)?1:0);
         }

         public Object fromNative(Object var1, FromNativeContext var2) {
            return ((Integer)var1).intValue() != 0?Boolean.TRUE:Boolean.FALSE;
         }

         public Class nativeType() {
            return Integer.class;
         }
      };
      this.addTypeConverter(Boolean.class, var2);
   }
}
