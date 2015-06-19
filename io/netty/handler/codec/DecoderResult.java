package io.netty.handler.codec;

import io.netty.util.Signal;

public class DecoderResult {
   protected static final Signal SIGNAL_UNFINISHED = Signal.valueOf(DecoderResult.class.getName() + ".UNFINISHED");
   protected static final Signal SIGNAL_SUCCESS = Signal.valueOf(DecoderResult.class.getName() + ".SUCCESS");
   public static final DecoderResult UNFINISHED;
   public static final DecoderResult SUCCESS;
   private final Throwable cause;

   public static DecoderResult failure(Throwable var0) {
      if(var0 == null) {
         throw new NullPointerException("cause");
      } else {
         return new DecoderResult(var0);
      }
   }

   protected DecoderResult(Throwable var1) {
      if(var1 == null) {
         throw new NullPointerException("cause");
      } else {
         this.cause = var1;
      }
   }

   public boolean isFinished() {
      return this.cause != SIGNAL_UNFINISHED;
   }

   public boolean isSuccess() {
      return this.cause == SIGNAL_SUCCESS;
   }

   public boolean isFailure() {
      return this.cause != SIGNAL_SUCCESS && this.cause != SIGNAL_UNFINISHED;
   }

   public Throwable cause() {
      return this.isFailure()?this.cause:null;
   }

   public String toString() {
      if(this.isFinished()) {
         if(this.isSuccess()) {
            return "success";
         } else {
            String var1 = this.cause().toString();
            StringBuilder var2 = new StringBuilder(var1.length() + 17);
            var2.append("failure(");
            var2.append(var1);
            var2.append(')');
            return var2.toString();
         }
      } else {
         return "unfinished";
      }
   }

   static {
      UNFINISHED = new DecoderResult(SIGNAL_UNFINISHED);
      SUCCESS = new DecoderResult(SIGNAL_SUCCESS);
   }
}
