package io.netty.handler.codec.spdy;

import io.netty.handler.codec.spdy.DefaultSpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.util.internal.StringUtil;

public class DefaultSpdyRstStreamFrame extends DefaultSpdyStreamFrame implements SpdyRstStreamFrame {
   private SpdyStreamStatus status;

   public DefaultSpdyRstStreamFrame(int var1, int var2) {
      this(var1, SpdyStreamStatus.valueOf(var2));
   }

   public DefaultSpdyRstStreamFrame(int var1, SpdyStreamStatus var2) {
      super(var1);
      this.setStatus(var2);
   }

   public SpdyRstStreamFrame setStreamId(int var1) {
      super.setStreamId(var1);
      return this;
   }

   public SpdyRstStreamFrame setLast(boolean var1) {
      super.setLast(var1);
      return this;
   }

   public SpdyStreamStatus status() {
      return this.status;
   }

   public SpdyRstStreamFrame setStatus(SpdyStreamStatus var1) {
      this.status = var1;
      return this;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Stream-ID = ");
      var1.append(this.streamId());
      var1.append(StringUtil.NEWLINE);
      var1.append("--> Status: ");
      var1.append(this.status());
      return var1.toString();
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
