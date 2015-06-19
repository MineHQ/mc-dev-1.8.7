package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum BoundType {
   OPEN {
      BoundType flip() {
         return CLOSED;
      }
   },
   CLOSED {
      BoundType flip() {
         return OPEN;
      }
   };

   private BoundType() {
   }

   static BoundType forBoolean(boolean var0) {
      return var0?CLOSED:OPEN;
   }

   abstract BoundType flip();

   // $FF: synthetic method
   BoundType(Object var3) {
      this();
   }
}
