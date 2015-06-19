package io.netty.handler.codec.spdy;

public class SpdyStreamStatus implements Comparable<SpdyStreamStatus> {
   public static final SpdyStreamStatus PROTOCOL_ERROR = new SpdyStreamStatus(1, "PROTOCOL_ERROR");
   public static final SpdyStreamStatus INVALID_STREAM = new SpdyStreamStatus(2, "INVALID_STREAM");
   public static final SpdyStreamStatus REFUSED_STREAM = new SpdyStreamStatus(3, "REFUSED_STREAM");
   public static final SpdyStreamStatus UNSUPPORTED_VERSION = new SpdyStreamStatus(4, "UNSUPPORTED_VERSION");
   public static final SpdyStreamStatus CANCEL = new SpdyStreamStatus(5, "CANCEL");
   public static final SpdyStreamStatus INTERNAL_ERROR = new SpdyStreamStatus(6, "INTERNAL_ERROR");
   public static final SpdyStreamStatus FLOW_CONTROL_ERROR = new SpdyStreamStatus(7, "FLOW_CONTROL_ERROR");
   public static final SpdyStreamStatus STREAM_IN_USE = new SpdyStreamStatus(8, "STREAM_IN_USE");
   public static final SpdyStreamStatus STREAM_ALREADY_CLOSED = new SpdyStreamStatus(9, "STREAM_ALREADY_CLOSED");
   public static final SpdyStreamStatus INVALID_CREDENTIALS = new SpdyStreamStatus(10, "INVALID_CREDENTIALS");
   public static final SpdyStreamStatus FRAME_TOO_LARGE = new SpdyStreamStatus(11, "FRAME_TOO_LARGE");
   private final int code;
   private final String statusPhrase;

   public static SpdyStreamStatus valueOf(int var0) {
      if(var0 == 0) {
         throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
      } else {
         switch(var0) {
         case 1:
            return PROTOCOL_ERROR;
         case 2:
            return INVALID_STREAM;
         case 3:
            return REFUSED_STREAM;
         case 4:
            return UNSUPPORTED_VERSION;
         case 5:
            return CANCEL;
         case 6:
            return INTERNAL_ERROR;
         case 7:
            return FLOW_CONTROL_ERROR;
         case 8:
            return STREAM_IN_USE;
         case 9:
            return STREAM_ALREADY_CLOSED;
         case 10:
            return INVALID_CREDENTIALS;
         case 11:
            return FRAME_TOO_LARGE;
         default:
            return new SpdyStreamStatus(var0, "UNKNOWN (" + var0 + ')');
         }
      }
   }

   public SpdyStreamStatus(int var1, String var2) {
      if(var1 == 0) {
         throw new IllegalArgumentException("0 is not a valid status code for a RST_STREAM");
      } else if(var2 == null) {
         throw new NullPointerException("statusPhrase");
      } else {
         this.code = var1;
         this.statusPhrase = var2;
      }
   }

   public int code() {
      return this.code;
   }

   public String statusPhrase() {
      return this.statusPhrase;
   }

   public int hashCode() {
      return this.code();
   }

   public boolean equals(Object var1) {
      return !(var1 instanceof SpdyStreamStatus)?false:this.code() == ((SpdyStreamStatus)var1).code();
   }

   public String toString() {
      return this.statusPhrase();
   }

   public int compareTo(SpdyStreamStatus var1) {
      return this.code() - var1.code();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compareTo(Object var1) {
      return this.compareTo((SpdyStreamStatus)var1);
   }
}
