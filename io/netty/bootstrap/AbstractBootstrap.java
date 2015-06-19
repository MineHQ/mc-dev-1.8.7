package io.netty.bootstrap;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> implements Cloneable {
   private volatile EventLoopGroup group;
   private volatile ChannelFactory<? extends C> channelFactory;
   private volatile SocketAddress localAddress;
   private final Map<ChannelOption<?>, Object> options = new LinkedHashMap();
   private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap();
   private volatile ChannelHandler handler;

   AbstractBootstrap() {
   }

   AbstractBootstrap(AbstractBootstrap<B, C> var1) {
      this.group = var1.group;
      this.channelFactory = var1.channelFactory;
      this.handler = var1.handler;
      this.localAddress = var1.localAddress;
      Map var2 = var1.options;
      synchronized(var1.options) {
         this.options.putAll(var1.options);
      }

      var2 = var1.attrs;
      synchronized(var1.attrs) {
         this.attrs.putAll(var1.attrs);
      }
   }

   public B group(EventLoopGroup var1) {
      if(var1 == null) {
         throw new NullPointerException("group");
      } else if(this.group != null) {
         throw new IllegalStateException("group set already");
      } else {
         this.group = var1;
         return this;
      }
   }

   public B channel(Class<? extends C> var1) {
      if(var1 == null) {
         throw new NullPointerException("channelClass");
      } else {
         return this.channelFactory(new AbstractBootstrap.BootstrapChannelFactory(var1));
      }
   }

   public B channelFactory(ChannelFactory<? extends C> var1) {
      if(var1 == null) {
         throw new NullPointerException("channelFactory");
      } else if(this.channelFactory != null) {
         throw new IllegalStateException("channelFactory set already");
      } else {
         this.channelFactory = var1;
         return this;
      }
   }

   public B localAddress(SocketAddress var1) {
      this.localAddress = var1;
      return this;
   }

   public B localAddress(int var1) {
      return this.localAddress(new InetSocketAddress(var1));
   }

   public B localAddress(String var1, int var2) {
      return this.localAddress(new InetSocketAddress(var1, var2));
   }

   public B localAddress(InetAddress var1, int var2) {
      return this.localAddress(new InetSocketAddress(var1, var2));
   }

   public <T> B option(ChannelOption<T> var1, T var2) {
      if(var1 == null) {
         throw new NullPointerException("option");
      } else {
         Map var3;
         if(var2 == null) {
            var3 = this.options;
            synchronized(this.options) {
               this.options.remove(var1);
            }
         } else {
            var3 = this.options;
            synchronized(this.options) {
               this.options.put(var1, var2);
            }
         }

         return this;
      }
   }

   public <T> B attr(AttributeKey<T> var1, T var2) {
      if(var1 == null) {
         throw new NullPointerException("key");
      } else {
         Map var3;
         if(var2 == null) {
            var3 = this.attrs;
            synchronized(this.attrs) {
               this.attrs.remove(var1);
            }
         } else {
            var3 = this.attrs;
            synchronized(this.attrs) {
               this.attrs.put(var1, var2);
            }
         }

         return this;
      }
   }

   public B validate() {
      if(this.group == null) {
         throw new IllegalStateException("group not set");
      } else if(this.channelFactory == null) {
         throw new IllegalStateException("channel or channelFactory not set");
      } else {
         return this;
      }
   }

   public abstract B clone();

   public ChannelFuture register() {
      this.validate();
      return this.initAndRegister();
   }

   public ChannelFuture bind() {
      this.validate();
      SocketAddress var1 = this.localAddress;
      if(var1 == null) {
         throw new IllegalStateException("localAddress not set");
      } else {
         return this.doBind(var1);
      }
   }

   public ChannelFuture bind(int var1) {
      return this.bind(new InetSocketAddress(var1));
   }

   public ChannelFuture bind(String var1, int var2) {
      return this.bind(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture bind(InetAddress var1, int var2) {
      return this.bind(new InetSocketAddress(var1, var2));
   }

   public ChannelFuture bind(SocketAddress var1) {
      this.validate();
      if(var1 == null) {
         throw new NullPointerException("localAddress");
      } else {
         return this.doBind(var1);
      }
   }

   private ChannelFuture doBind(final SocketAddress var1) {
      final ChannelFuture var2 = this.initAndRegister();
      final Channel var3 = var2.channel();
      if(var2.cause() != null) {
         return var2;
      } else {
         final Object var4;
         if(var2.isDone()) {
            var4 = var3.newPromise();
            doBind0(var2, var3, var1, (ChannelPromise)var4);
         } else {
            var4 = new AbstractBootstrap.PendingRegistrationPromise(var3, null);
            var2.addListener(new ChannelFutureListener() {
               public void operationComplete(ChannelFuture var1x) throws Exception {
                  AbstractBootstrap.doBind0(var2, var3, var1, (ChannelPromise)var4);
               }

               // $FF: synthetic method
               // $FF: bridge method
               public void operationComplete(Future var1x) throws Exception {
                  this.operationComplete((ChannelFuture)var1x);
               }
            });
         }

         return (ChannelFuture)var4;
      }
   }

   final ChannelFuture initAndRegister() {
      Channel var1 = this.channelFactory().newChannel();

      try {
         this.init(var1);
      } catch (Throwable var3) {
         var1.unsafe().closeForcibly();
         return (new DefaultChannelPromise(var1, GlobalEventExecutor.INSTANCE)).setFailure(var3);
      }

      ChannelFuture var2 = this.group().register(var1);
      if(var2.cause() != null) {
         if(var1.isRegistered()) {
            var1.close();
         } else {
            var1.unsafe().closeForcibly();
         }
      }

      return var2;
   }

   abstract void init(Channel var1) throws Exception;

   private static void doBind0(final ChannelFuture var0, final Channel var1, final SocketAddress var2, final ChannelPromise var3) {
      var1.eventLoop().execute(new Runnable() {
         public void run() {
            if(var0.isSuccess()) {
               var1.bind(var2, var3).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
               var3.setFailure(var0.cause());
            }

         }
      });
   }

   public B handler(ChannelHandler var1) {
      if(var1 == null) {
         throw new NullPointerException("handler");
      } else {
         this.handler = var1;
         return this;
      }
   }

   final SocketAddress localAddress() {
      return this.localAddress;
   }

   final ChannelFactory<? extends C> channelFactory() {
      return this.channelFactory;
   }

   final ChannelHandler handler() {
      return this.handler;
   }

   public final EventLoopGroup group() {
      return this.group;
   }

   final Map<ChannelOption<?>, Object> options() {
      return this.options;
   }

   final Map<AttributeKey<?>, Object> attrs() {
      return this.attrs;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(StringUtil.simpleClassName((Object)this));
      var1.append('(');
      if(this.group != null) {
         var1.append("group: ");
         var1.append(StringUtil.simpleClassName((Object)this.group));
         var1.append(", ");
      }

      if(this.channelFactory != null) {
         var1.append("channelFactory: ");
         var1.append(this.channelFactory);
         var1.append(", ");
      }

      if(this.localAddress != null) {
         var1.append("localAddress: ");
         var1.append(this.localAddress);
         var1.append(", ");
      }

      Map var2 = this.options;
      synchronized(this.options) {
         if(!this.options.isEmpty()) {
            var1.append("options: ");
            var1.append(this.options);
            var1.append(", ");
         }
      }

      var2 = this.attrs;
      synchronized(this.attrs) {
         if(!this.attrs.isEmpty()) {
            var1.append("attrs: ");
            var1.append(this.attrs);
            var1.append(", ");
         }
      }

      if(this.handler != null) {
         var1.append("handler: ");
         var1.append(this.handler);
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
   public Object clone() throws CloneNotSupportedException {
      return this.clone();
   }

   private static final class PendingRegistrationPromise extends DefaultChannelPromise {
      private PendingRegistrationPromise(Channel var1) {
         super(var1);
      }

      protected EventExecutor executor() {
         return (EventExecutor)(this.channel().isRegistered()?super.executor():GlobalEventExecutor.INSTANCE);
      }

      // $FF: synthetic method
      PendingRegistrationPromise(Channel var1, Object var2) {
         this(var1);
      }
   }

   private static final class BootstrapChannelFactory<T extends Channel> implements ChannelFactory<T> {
      private final Class<? extends T> clazz;

      BootstrapChannelFactory(Class<? extends T> var1) {
         this.clazz = var1;
      }

      public T newChannel() {
         try {
            return (Channel)this.clazz.newInstance();
         } catch (Throwable var2) {
            throw new ChannelException("Unable to create Channel from class " + this.clazz, var2);
         }
      }

      public String toString() {
         return StringUtil.simpleClassName(this.clazz) + ".class";
      }
   }
}
