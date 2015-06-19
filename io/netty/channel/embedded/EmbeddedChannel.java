package io.netty.channel.embedded;

import io.netty.channel.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.EventLoop;
import io.netty.channel.embedded.EmbeddedEventLoop;
import io.netty.channel.embedded.EmbeddedSocketAddress;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.RecyclableArrayList;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayDeque;
import java.util.Queue;

public class EmbeddedChannel extends AbstractChannel {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(EmbeddedChannel.class);
   private static final ChannelMetadata METADATA = new ChannelMetadata(false);
   private final EmbeddedEventLoop loop = new EmbeddedEventLoop();
   private final ChannelConfig config = new DefaultChannelConfig(this);
   private final SocketAddress localAddress = new EmbeddedSocketAddress();
   private final SocketAddress remoteAddress = new EmbeddedSocketAddress();
   private final Queue<Object> inboundMessages = new ArrayDeque();
   private final Queue<Object> outboundMessages = new ArrayDeque();
   private Throwable lastException;
   private int state;

   public EmbeddedChannel(ChannelHandler... var1) {
      super((Channel)null);
      if(var1 == null) {
         throw new NullPointerException("handlers");
      } else {
         int var2 = 0;
         ChannelPipeline var3 = this.pipeline();
         ChannelHandler[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ChannelHandler var7 = var4[var6];
            if(var7 == null) {
               break;
            }

            ++var2;
            var3.addLast(new ChannelHandler[]{var7});
         }

         if(var2 == 0) {
            throw new IllegalArgumentException("handlers is empty.");
         } else {
            this.loop.register(this);
            var3.addLast(new ChannelHandler[]{new EmbeddedChannel.LastInboundHandler()});
         }
      }
   }

   public ChannelMetadata metadata() {
      return METADATA;
   }

   public ChannelConfig config() {
      return this.config;
   }

   public boolean isOpen() {
      return this.state < 2;
   }

   public boolean isActive() {
      return this.state == 1;
   }

   public Queue<Object> inboundMessages() {
      return this.inboundMessages;
   }

   /** @deprecated */
   @Deprecated
   public Queue<Object> lastInboundBuffer() {
      return this.inboundMessages();
   }

   public Queue<Object> outboundMessages() {
      return this.outboundMessages;
   }

   /** @deprecated */
   @Deprecated
   public Queue<Object> lastOutboundBuffer() {
      return this.outboundMessages();
   }

   public Object readInbound() {
      return this.inboundMessages.poll();
   }

   public Object readOutbound() {
      return this.outboundMessages.poll();
   }

   public boolean writeInbound(Object... var1) {
      this.ensureOpen();
      if(var1.length == 0) {
         return !this.inboundMessages.isEmpty();
      } else {
         ChannelPipeline var2 = this.pipeline();
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            var2.fireChannelRead(var6);
         }

         var2.fireChannelReadComplete();
         this.runPendingTasks();
         this.checkException();
         return !this.inboundMessages.isEmpty();
      }
   }

   public boolean writeOutbound(Object... var1) {
      this.ensureOpen();
      if(var1.length == 0) {
         return !this.outboundMessages.isEmpty();
      } else {
         RecyclableArrayList var2 = RecyclableArrayList.newInstance(var1.length);

         try {
            Object[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Object var6 = var3[var5];
               if(var6 == null) {
                  break;
               }

               var2.add(this.write(var6));
            }

            this.flush();
            int var10 = var2.size();

            for(var4 = 0; var4 < var10; ++var4) {
               ChannelFuture var12 = (ChannelFuture)var2.get(var4);

               assert var12.isDone();

               if(var12.cause() != null) {
                  this.recordException(var12.cause());
               }
            }

            this.runPendingTasks();
            this.checkException();
            boolean var11 = !this.outboundMessages.isEmpty();
            return var11;
         } finally {
            var2.recycle();
         }
      }
   }

   public boolean finish() {
      this.close();
      this.runPendingTasks();
      this.checkException();
      return !this.inboundMessages.isEmpty() || !this.outboundMessages.isEmpty();
   }

   public void runPendingTasks() {
      try {
         this.loop.runTasks();
      } catch (Exception var2) {
         this.recordException(var2);
      }

   }

   private void recordException(Throwable var1) {
      if(this.lastException == null) {
         this.lastException = var1;
      } else {
         logger.warn("More than one exception was raised. Will report only the first one and log others.", var1);
      }

   }

   public void checkException() {
      Throwable var1 = this.lastException;
      if(var1 != null) {
         this.lastException = null;
         PlatformDependent.throwException(var1);
      }
   }

   protected final void ensureOpen() {
      if(!this.isOpen()) {
         this.recordException(new ClosedChannelException());
         this.checkException();
      }

   }

   protected boolean isCompatible(EventLoop var1) {
      return var1 instanceof EmbeddedEventLoop;
   }

   protected SocketAddress localAddress0() {
      return this.isActive()?this.localAddress:null;
   }

   protected SocketAddress remoteAddress0() {
      return this.isActive()?this.remoteAddress:null;
   }

   protected void doRegister() throws Exception {
      this.state = 1;
   }

   protected void doBind(SocketAddress var1) throws Exception {
   }

   protected void doDisconnect() throws Exception {
      this.doClose();
   }

   protected void doClose() throws Exception {
      this.state = 2;
   }

   protected void doBeginRead() throws Exception {
   }

   protected AbstractChannel.AbstractUnsafe newUnsafe() {
      return new EmbeddedChannel.DefaultUnsafe();
   }

   protected void doWrite(ChannelOutboundBuffer var1) throws Exception {
      while(true) {
         Object var2 = var1.current();
         if(var2 == null) {
            return;
         }

         ReferenceCountUtil.retain(var2);
         this.outboundMessages.add(var2);
         var1.remove();
      }
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private final class LastInboundHandler extends ChannelInboundHandlerAdapter {
      private LastInboundHandler() {
      }

      public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
         EmbeddedChannel.this.inboundMessages.add(var2);
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         EmbeddedChannel.this.recordException(var2);
      }

      // $FF: synthetic method
      LastInboundHandler(EmbeddedChannel.SyntheticClass_1 var2) {
         this();
      }
   }

   private class DefaultUnsafe extends AbstractChannel.AbstractUnsafe {
      private DefaultUnsafe() {
         super();
      }

      public void connect(SocketAddress var1, SocketAddress var2, ChannelPromise var3) {
         this.safeSetSuccess(var3);
      }

      // $FF: synthetic method
      DefaultUnsafe(EmbeddedChannel.SyntheticClass_1 var2) {
         this();
      }
   }
}
