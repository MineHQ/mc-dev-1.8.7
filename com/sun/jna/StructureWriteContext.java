package com.sun.jna;

import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import java.lang.reflect.Field;

public class StructureWriteContext extends ToNativeContext {
   private Structure struct;
   private Field field;

   StructureWriteContext(Structure var1, Field var2) {
      this.struct = var1;
      this.field = var2;
   }

   public Structure getStructure() {
      return this.struct;
   }

   public Field getField() {
      return this.field;
   }
}
