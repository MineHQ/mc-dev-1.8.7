package io.netty.buffer;

public interface ByteBufProcessor {
   ByteBufProcessor FIND_NUL = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 != 0;
      }
   };
   ByteBufProcessor FIND_NON_NUL = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 == 0;
      }
   };
   ByteBufProcessor FIND_CR = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 != 13;
      }
   };
   ByteBufProcessor FIND_NON_CR = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 == 13;
      }
   };
   ByteBufProcessor FIND_LF = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 != 10;
      }
   };
   ByteBufProcessor FIND_NON_LF = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 == 10;
      }
   };
   ByteBufProcessor FIND_CRLF = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 != 13 && var1 != 10;
      }
   };
   ByteBufProcessor FIND_NON_CRLF = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 == 13 || var1 == 10;
      }
   };
   ByteBufProcessor FIND_LINEAR_WHITESPACE = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 != 32 && var1 != 9;
      }
   };
   ByteBufProcessor FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor() {
      public boolean process(byte var1) throws Exception {
         return var1 == 32 || var1 == 9;
      }
   };

   boolean process(byte var1) throws Exception;
}
