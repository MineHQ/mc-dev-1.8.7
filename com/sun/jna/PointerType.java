package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.NativeMapped;
import com.sun.jna.Pointer;

public abstract class PointerType implements NativeMapped {
   private Pointer pointer;

   protected PointerType() {
      this.pointer = Pointer.NULL;
   }

   protected PointerType(Pointer var1) {
      this.pointer = var1;
   }

   public Class nativeType() {
      return Pointer.class;
   }

   public Object toNative() {
      return this.getPointer();
   }

   public Pointer getPointer() {
      return this.pointer;
   }

   public void setPointer(Pointer var1) {
      this.pointer = var1;
   }

   public Object fromNative(Object var1, FromNativeContext var2) {
      if(var1 == null) {
         return null;
      } else {
         try {
            PointerType var3 = (PointerType)this.getClass().newInstance();
            var3.pointer = (Pointer)var1;
            return var3;
         } catch (InstantiationException var4) {
            throw new IllegalArgumentException("Can\'t instantiate " + this.getClass());
         } catch (IllegalAccessException var5) {
            throw new IllegalArgumentException("Not allowed to instantiate " + this.getClass());
         }
      }
   }

   public int hashCode() {
      return this.pointer != null?this.pointer.hashCode():0;
   }

   public boolean equals(Object var1) {
      if(var1 == this) {
         return true;
      } else if(var1 instanceof PointerType) {
         Pointer var2 = ((PointerType)var1).getPointer();
         return this.pointer == null?var2 == null:this.pointer.equals(var2);
      } else {
         return false;
      }
   }

   public String toString() {
      return this.pointer == null?"NULL":this.pointer.toString();
   }
}
