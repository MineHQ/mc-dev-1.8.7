package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMappedConverter implements TypeConverter {
   private static final Map converters = new WeakHashMap();
   private final Class type;
   private final Class nativeType;
   private final NativeMapped instance;

   public static NativeMappedConverter getInstance(Class var0) {
      Map var1 = converters;
      synchronized(converters) {
         Reference var2 = (Reference)converters.get(var0);
         NativeMappedConverter var3 = var2 != null?(NativeMappedConverter)var2.get():null;
         if(var3 == null) {
            var3 = new NativeMappedConverter(var0);
            converters.put(var0, new SoftReference(var3));
         }

         return var3;
      }
   }

   public NativeMappedConverter(Class var1) {
      if(!NativeMapped.class.isAssignableFrom(var1)) {
         throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
      } else {
         this.type = var1;
         this.instance = this.defaultValue();
         this.nativeType = this.instance.nativeType();
      }
   }

   public NativeMapped defaultValue() {
      String var2;
      try {
         return (NativeMapped)this.type.newInstance();
      } catch (InstantiationException var3) {
         var2 = "Can\'t create an instance of " + this.type + ", requires a no-arg constructor: " + var3;
         throw new IllegalArgumentException(var2);
      } catch (IllegalAccessException var4) {
         var2 = "Not allowed to create an instance of " + this.type + ", requires a public, no-arg constructor: " + var4;
         throw new IllegalArgumentException(var2);
      }
   }

   public Object fromNative(Object var1, FromNativeContext var2) {
      return this.instance.fromNative(var1, var2);
   }

   public Class nativeType() {
      return this.nativeType;
   }

   public Object toNative(Object var1, ToNativeContext var2) {
      if(var1 == null) {
         if(Pointer.class.isAssignableFrom(this.nativeType)) {
            return null;
         }

         var1 = this.defaultValue();
      }

      return ((NativeMapped)var1).toNative();
   }
}
