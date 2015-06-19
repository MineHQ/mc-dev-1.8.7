package io.netty.channel;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ServerChannel;
import java.net.SocketAddress;

public abstract class AbstractServerChannel extends AbstractChannel implements ServerChannel {
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);

   protected AbstractServerChannel() {
      super((Channel)null);
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public SocketAddress remoteAddress() {
      return null;
   }

   protected SocketAddress remoteAddress0() {
      return null;
   }

   protected void doDisconnect() throws Exception {
      throw new UnsupportedOperationException();
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new AbstractServerChannel.DefaultServerUnsafe();
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      throw new UnsupportedOperationException();
   }

   protected final Object filterOutboundMessage(Object var1) {
      throw new UnsupportedOperationException();
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class DefaultServerUnsafe extends AbstractChannel.AbstractUnsafe {
      private DefaultServerUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         this.safeSetFailure(var3, new UnsupportedOperationException());
      }

      // $FF: synthetic method
      DefaultServerUnsafe(AbstractServerChannel.SyntheticClass_1 var2) {
         this();
      }
   }
}
