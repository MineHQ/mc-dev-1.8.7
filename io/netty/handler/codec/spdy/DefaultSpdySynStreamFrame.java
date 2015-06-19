package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdySynStreamFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdySynStreamFrame extends DefaultSpdyHeadersFrame implements SpdySynStreamFrame {
   private int associatedStreamId;
   private byte priority;
   private boolean unidirectional;

   public DefaultSpdySynStreamFrame(int var1, int var2, byte var3) {
      super(var1);
      this.setAssociatedStreamId(var2);
      this.setPriority(var3);
   }

   public SpdySynStreamFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdySynStreamFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public SpdySynStreamFrame setInvalid() {
      super.setInvalid();
      return this;
   }

   public int associatedStreamId() {
      return this.associatedStreamId;
   }

   public SpdySynStreamFrame setAssociatedStreamId(int var1) {
      if(var1 < 0) {
         throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + var1);
      } else {
         this.associatedStreamId = var1;
         return this;
      }
   }

   public byte priority() {
      return this.priority;
   }

   public SpdySynStreamFrame setPriority(byte var1) {
      if(var1 >= 0 && var1 <= 7) {
         this.priority = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + var1);
      }
   }

   public boolean isUnidirectional() {
      return this.unidirectional;
   }

   public SpdySynStreamFrame setUnidirectional(boolean var1) {
      this.unidirectional = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(last: ");
      var1.append(this.isLast());
      var1.append("; unidirectional: ");
      var1.append(this.isUnidirectional());
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.streamId());
      var1.append(StringUtil.NEWLINE);
      if(this.associatedStreamId != 0) {
         var1.append("--> Associated-To-Stream-ID = ");
         var1.append(this.associatedStreamId());
         var1.append(StringUtil.NEWLINE);
      }

      var1.append("--> Priority = ");
      var1.append(this.priority());
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Headers:");
      var1.append(StringUtil.NEWLINE);
      this.appendHeaders(var1);
      var1.setLength(var1.length() - StringUtil.NEWLINE.length());
      return var1.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SpdyHeadersFrame setInvalid() {
      return this.setInvalid();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SpdyHeadersFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SpdyHeadersFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SpdyStreamFrame setLast(boolean var1) {
      return this.setLast(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public SpdyStreamFrame setStreamId(int var1) {
      return this.setStreamId(var1);
   }
}
