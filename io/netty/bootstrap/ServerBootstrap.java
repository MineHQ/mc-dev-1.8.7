package io.netty.bootstrap;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public final class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, ServerChannel> {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
   private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap();
   private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap();
   private volatile EventLoopGroup childGroup;
   private volatile ChannelHandler childHandler;

   public ServerBootstrap() {
   }

   private ServerBootstrap(ServerBootstrap var1) {
      super(var1);
      this.childGroup = var1.childGroup;
      this.childHandler = var1.childHandler;
      Map var2 = var1.childOptions;
      synchronized(var1.childOptions) {
         this.childOptions.putAll(var1.childOptions);
      }

      var2 = var1.childAttrs;
      synchronized(var1.childAttrs) {
         this.childAttrs.putAll(var1.childAttrs);
      }
   }

   public ServerBootstrap group(EventLoopGroup var1) {
      return this.group(var1, var1);
   }

   public ServerBootstrap group(EventLoopGroup var1, EventLoopGroup var2) {
      super.group(var1);
      if(var2 == null) {
         throw new NullPointerException("childGroup");
      } else if(this.childGroup != null) {
         throw new IllegalStateException("childGroup set already");
      } else {
         this.childGroup = var2;
         return this;
      }
   }

   public <T> ServerBootstrap childOption(ChannelOption<T> var1, T var2) {
      if(var1 == null) {
         throw new NullPointerException("childOption");
      } else {
         Map var3;
         if(var2 == null) {
            var3 = this.childOptions;
            synchronized(this.childOptions) {
               this.childOptions.remove(var1);
            }
         } else {
            var3 = this.childOptions;
            synchronized(this.childOptions) {
               this.childOptions.put(var1, var2);
            }
         }

         return this;
      }
   }

   public <T> ServerBootstrap childAttr(AttributeKey<T> var1, T var2) {
      if(var1 == null) {
         throw new NullPointerException("childKey");
      } else {
         if(var2 == null) {
            this.childAttrs.remove(var1);
         } else {
            this.childAttrs.put(var1, var2);
         }

         return this;
      }
   }

   public ServerBootstrap childHandler(ChannelHandler var1) {
      if(var1 == null) {
         throw new NullPointerException("childHandler");
      } else {
         this.childHandler = var1;
         return this;
      }
   }

   public EventLoopGroup childGroup() {
      return this.childGroup;
   }

   void init(Channel var1) throws Exception {
      Map var2 = this.options();
      synchronized(var2) {
         var1.config().setOptions(var2);
      }

      Map var3 = this.attrs();
      synchronized(var3) {
         Iterator var5 = var3.entrySet().iterator();

         while(true) {
            if(!var5.hasNext()) {
               break;
            }

            Entry var6 = (Entry)var5.next();
            AttributeKey var7 = (AttributeKey)var6.getKey();
            var1.attr(var7).set(var6.getValue());
         }
      }

      ChannelPipeline var4 = var1.pipeline();
      if(this.handler() != null) {
         var4.addLast(new ChannelHandler[]{this.handler()});
      }

      final EventLoopGroup var16 = this.childGroup;
      final ChannelHandler var17 = this.childHandler;
      Map var9 = this.childOptions;
      final Entry[] var18;
      synchronized(this.childOptions) {
         var18 = (Entry[])this.childOptions.entrySet().toArray(newOptionArray(this.childOptions.size()));
      }

      var9 = this.childAttrs;
      final Entry[] var8;
      synchronized(this.childAttrs) {
         var8 = (Entry[])this.childAttrs.entrySet().toArray(newAttrArray(this.childAttrs.size()));
      }

      var4.addLast(new ChannelHandler[]{new ChannelInitializer() {
         public void initChannel(Channel var1) throws Exception {
            var1.pipeline().addLast(new ChannelHandler[]{new ServerBootstrap.ServerBootstrapAcceptor(var16, var17, var18, var8)});
         }
      }});
   }

   public ServerBootstrap validate() {
      super.validate();
      if(this.childHandler == null) {
         throw new IllegalStateException("childHandler not set");
      } else {
         if(this.childGroup == null) {
            logger.warn("childGroup is not set. Using parentGroup instead.");
            this.childGroup = this.group();
         }

         return this;
      }
   }

   private static Entry<ChannelOption<?>, Object>[] newOptionArray(int var0) {
      return new Entry[var0];
   }

   private static Entry<AttributeKey<?>, Object>[] newAttrArray(int var0) {
      return new Entry[var0];
   }

   public ServerBootstrap clone() {
      return new ServerBootstrap(this);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(super.toString());
      var1.setLength(var1.length() - 1);
      var1.append(", ");
      if(this.childGroup != null) {
         var1.append("childGroup: ");
         var1.append(StringUtil.simpleClassName((Object)this.childGroup));
         var1.append(", ");
      }

      Map var2 = this.childOptions;
      synchronized(this.childOptions) {
         if(!this.childOptions.isEmpty()) {
            var1.append("childOptions: ");
            var1.append(this.childOptions);
            var1.append(", ");
         }
      }

      var2 = this.childAttrs;
      synchronized(this.childAttrs) {
         if(!this.childAttrs.isEmpty()) {
            var1.append("childAttrs: ");
            var1.append(this.childAttrs);
            var1.append(", ");
         }
      }

      if(this.childHandler != null) {
         var1.append("childHandler: ");
         var1.append(this.childHandler);
         var1.append(", ");
      }

      if(var1.charAt(var1.length() - 1) == 40) {
         var1.append(')');
      } else {
         var1.setCharAt(var1.length() - 2, ')');
         var1.setLength(var1.length() - 1);
      }

      return var1.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public AbstractBootstrap clone() {
      return this.clone();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public AbstractBootstrap validate() {
      return this.validate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public AbstractBootstrap group(EventLoopGroup var1) {
      return this.group(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   private static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter {
      private final EventLoopGroup childGroup;
      private final ChannelHandler childHandler;
      private final Entry<ChannelOption<?>, Object>[] childOptions;
      private final Entry<AttributeKey<?>, Object>[] childAttrs;

      ServerBootstrapAcceptor(EventLoopGroup var1, ChannelHandler var2, Entry<ChannelOption<?>, Object>[] var3, Entry<AttributeKey<?>, Object>[] var4) {
         this.childGroup = var1;
         this.childHandler = var2;
         this.childOptions = var3;
         this.childAttrs = var4;
      }

      public void channelRead(ChannelHandlerContext var1, Object var2) {
         final Channel var3 = (Channel)var2;
         var3.pipeline().addLast(new ChannelHandler[]{this.childHandler});
         Entry[] var4 = this.childOptions;
         int var5 = var4.length;

         int var6;
         Entry var7;
         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];

            try {
               if(!var3.config().setOption((ChannelOption)var7.getKey(), var7.getValue())) {
                  ServerBootstrap.logger.warn("Unknown channel option: " + var7);
               }
            } catch (Throwable var10) {
               ServerBootstrap.logger.warn("Failed to set a channel option: " + var3, var10);
            }
         }

         var4 = this.childAttrs;
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.attr((AttributeKey)var7.getKey()).set(var7.getValue());
         }

         try {
            this.childGroup.register(var3).addListener(new ChannelFutureListener() {
               public void operationComplete(ChannelFuture var1) throws Exception {
                  if(!var1.isSuccess()) {
                     ServerBootstrap.ServerBootstrapAcceptor.forceClose(var3, var1.cause());
                  }

               }

               // $FF: synthetic method
               // $FF: bridge method
               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
         } catch (Throwable var9) {
            forceClose(var3, var9);
         }

      }

      private static void forceClose(Channel var0, Throwable var1) {
         var0.unsafe().closeForcibly();
         ServerBootstrap.logger.warn("Failed to register an accepted channel: " + var0, var1);
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
         final ChannelConfig var3 = var1.channel().config();
         if(var3.isAutoRead()) {
            var3.setAutoRead(false);
            var1.channel().eventLoop().schedule(new Runnable() {
               public void run() {
                  var3.setAutoRead(true);
               }
            }, 1L, TimeUnit.SECONDS);
         }

         var1.fireExceptionCaught(var2);
      }
   }
}
