package com.sun.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import java.util.Iterator;

public abstract class Union extends Structure {
   private Structure.StructField activeField;
   Structure.StructField biggestField;

   protected Union() {
   }

   protected Union(Pointer var1) {
      super(var1);
   }

   protected Union(Pointer var1, int var2) {
      super(var1, var2);
   }

   protected Union(TypeMapper var1) {
      super(var1);
   }

   protected Union(Pointer var1, int var2, TypeMapper var3) {
      super(var1, var2, var3);
   }

   public void setType(Class var1) {
      this.ensureAllocated();
      Iterator var2 = this.fields().values().iterator();

      Structure.StructField var3;
      do {
         if(!var2.hasNext()) {
            throw new IllegalArgumentException("No field of type " + var1 + " in " + this);
         }

         var3 = (Structure.StructField)var2.next();
      } while(var3.type != var1);

      this.activeField = var3;
   }

   public void setType(String var1) {
      this.ensureAllocated();
      Structure.StructField var2 = (Structure.StructField)this.fields().get(var1);
      if(var2 != null) {
         this.activeField = var2;
      } else {
         throw new IllegalArgumentException("No field named " + var1 + " in " + this);
      }
   }

   public Object readField(String var1) {
      this.ensureAllocated();
      this.setType(var1);
      return super.readField(var1);
   }

   public void writeField(String var1) {
      this.ensureAllocated();
      this.setType(var1);
      super.writeField(var1);
   }

   public void writeField(String var1, Object var2) {
      this.ensureAllocated();
      this.setType(var1);
      super.writeField(var1, var2);
   }

   public Object getTypedValue(Class var1) {
      this.ensureAllocated();
      Iterator var2 = this.fields().values().iterator();

      Structure.StructField var3;
      do {
         if(!var2.hasNext()) {
            throw new IllegalArgumentException("No field of type " + var1 + " in " + this);
         }

         var3 = (Structure.StructField)var2.next();
      } while(var3.type != var1);

      this.activeField = var3;
      this.read();
      return this.getField(this.activeField);
   }

   public Object setTypedValue(Object var1) {
      Structure.StructField var2 = this.findField(var1.getClass());
      if(var2 != null) {
         this.activeField = var2;
         this.setField(var2, var1);
         return this;
      } else {
         throw new IllegalArgumentException("No field of type " + var1.getClass() + " in " + this);
      }
   }

   private Structure.StructField findField(Class var1) {
      this.ensureAllocated();
      Iterator var2 = this.fields().values().iterator();

      Structure.StructField var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (Structure.StructField)var2.next();
      } while(!var3.type.isAssignableFrom(var1));

      return var3;
   }

   void writeField(Structure.StructField var1) {
      if(var1 == this.activeField) {
         super.writeField(var1);
      }

   }

   Object readField(Structure.StructField var1) {
      return var1 != this.activeField && (Structure.class.isAssignableFrom(var1.type) || String.class.isAssignableFrom(var1.type) || WString.class.isAssignableFrom(var1.type))?null:super.readField(var1);
   }

   int calculateSize(boolean var1, boolean var2) {
      int var3 = super.calculateSize(var1, var2);
      if(var3 != -1) {
         int var4 = 0;
         Iterator var5 = this.fields().values().iterator();

         while(true) {
            Structure.StructField var6;
            do {
               if(!var5.hasNext()) {
                  var3 = this.calculateAlignedSize(var4);
                  if(var3 > 0 && this instanceof Structure.ByValue && !var2) {
                     this.getTypeInfo();
                  }

                  return var3;
               }

               var6 = (Structure.StructField)var5.next();
               var6.offset = 0;
            } while(var6.size <= var4 && (var6.size != var4 || !Structure.class.isAssignableFrom(var6.type)));

            var4 = var6.size;
            this.biggestField = var6;
         }
      } else {
         return var3;
      }
   }

   protected int getNativeAlignment(Class var1, Object var2, boolean var3) {
      return super.getNativeAlignment(var1, var2, true);
   }

   Pointer getTypeInfo() {
      return this.biggestField == null?null:super.getTypeInfo();
   }
}
