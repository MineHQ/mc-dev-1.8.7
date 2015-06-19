package io.netty.channel.oio;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.ThreadPerChannelEventLoop;
import java.net.ConnectException;
import java.net.SocketAddress;

public abstract class AbstractOioChannel extends AbstractChannel {
   protected static final int SO_TIMEOUT = 1000;
   private volatile boolean readPending;
   private final Runnable readTask = new Runnable() {
      public void run() {
         if(AbstractOioChannel.this.isReadPending() || AbstractOioChannel.this.config().isAutoRead()) {
            AbstractOioChannel.this.setReadPending(false);
            AbstractOioChannel.this.doRead();
         }
      }
   };

   protected AbstractOioChannel(Channel var1) {
      super(var1);
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new AbstractOioChannel.DefaultOioUnsafe(null);
   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof ThreadPerChannelEventLoop;
   }

   protected abstract void doConnect(SocketAddress var1, SocketAddress var2) throws Exception;

   protected void doBeginRead() throws Exception {
      if(!this.isReadPending()) {
         this.setReadPending(true);
         this.eventLoop().execute(this.readTask);
      }
   }

   protected abstract void doRead();

   protected boolean isReadPending() {
      return this.readPending;
   }

   protected void setReadPending(boolean var1) {
      this.readPending = var1;
   }

   private final class DefaultOioUnsafe extends AbstractChannel.AbstractUnsafe {
      private DefaultOioUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         if(var3.setUncancellable() && this.ensureOpen(var3)) {
            try {
               boolean var7 = AbstractOioChannel.this.isActive();
               AbstractOioChannel.this.doConnect(var1, var2);
               this.safeSetSuccess(var3);
               if(!var7 && AbstractOioChannel.this.isActive()) {
                  AbstractOioChannel.this.pipeline().fireChannelActive();
               }
            } catch (Throwable var6) {
               Object var4 = var6;
               if(var6 instanceof ConnectException) {
                  ConnectException var5 = new ConnectException(var6.getMessage() + ": " + var1);
                  var5.setStackTrace(var6.getStackTrace());
                  var4 = var5;
               }

               this.safeSetFailure(var3, (Throwable)var4);
               this.closeIfClosed();
            }

         }
      }

      // $FF: synthetic method
      DefaultOioUnsafe(Object var2) {
         this();
      }
   }
}
