package com.google.common.escape;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(
   emulated = true
)
final class Platform {
   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal() {
      protected char[] initialValue() {
         return new char[1024];
      }

      // $FF: synthetic method
      // $FF: bridge method
      protected Object initialValue() {
         return this.initialValue();
      }
   };

   private Platform() {
   }

   static char[] charBufferFromThreadLocal() {
      return (char[])DEST_TL.get();
   }
}
