package com.sun.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.Structure;
import java.lang.reflect.Field;

public class StructureReadContext extends FromNativeContext {
   private Structure structure;
   private Field field;

   StructureReadContext(Structure var1, Field var2) {
      super(var2.getType());
      this.structure = var1;
      this.field = var2;
   }

   public Structure getStructure() {
      return this.structure;
   }

   public Field getField() {
      return this.field;
   }
}
