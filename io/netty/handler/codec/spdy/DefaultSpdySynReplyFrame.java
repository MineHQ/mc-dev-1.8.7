package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyHeadersFrame;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdySynReplyFrame;
import io.netty.util.internal.StringUtil;

public class DefaultSpdySynReplyFrame extends DefaultSpdyHeadersFrame implements SpdySynReplyFrame {
   public DefaultSpdySynReplyFrame(int var1) {
      super(var1);
   }

   public SpdySynReplyFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdySynReplyFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public SpdySynReplyFrame setInvalid() {
      super.setInvalid();
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append("(last: ");
      var1.append(this.isLast());
      var1.append(')');
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.streamId());
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
