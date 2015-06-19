package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FailedChannelFuture;
import io.netty.channel.ThreadPerChannelEventLoop;
import io.netty.util.concurrent.AbstractEventExecutorGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ReadOnlyIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPerChannelEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup {
   private final Object[] childArgs;
   private final int maxChannels;
   final ThreadFactory threadFactory;
   final Set<ThreadPerChannelEventLoop> activeChildren;
   final Queue<ThreadPerChannelEventLoop> idleChildren;
   private final ChannelException tooManyChannels;
   private volatile boolean shuttingDown;
   private final Promise<?> terminationFuture;
   private final FutureListener<Object> childTerminationListener;

   protected ThreadPerChannelEventLoopGroup() {
      this(0);
   }

   protected ThreadPerChannelEventLoopGroup(int var1) {
      this(var1, Executors.defaultThreadFactory(), new Object[0]);
   }

   protected ThreadPerChannelEventLoopGroup(int var1, ThreadFactory var2, Object... var3) {
      this.activeChildren = Collections.newSetFromMap(PlatformDependent.newConcurrentHashMap());
      this.idleChildren = new ConcurrentLinkedQueue();
      this.terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
      this.childTerminationListener = new FutureListener() {
         public void operationComplete(Future<Object> var1) throws Exception {
            if(ThreadPerChannelEventLoopGroup.this.isTerminated()) {
               ThreadPerChannelEventLoopGroup.this.terminationFuture.trySuccess((Object)null);
            }

         }
      };
      if(var1 < 0) {
         throw new IllegalArgumentException(String.format("maxChannels: %d (expected: >= 0)", new Object[]{Integer.valueOf(var1)}));
      } else if(var2 == null) {
         throw new NullPointerException("threadFactory");
      } else {
         if(var3 == null) {
            this.childArgs = EmptyArrays.EMPTY_OBJECTS;
         } else {
            this.childArgs = (Object[])var3.clone();
         }

         this.maxChannels = var1;
         this.threadFactory = var2;
         this.tooManyChannels = new ChannelException("too many channels (max: " + var1 + ')');
         this.tooManyChannels.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      }
   }

   protected ThreadPerChannelEventLoop newChild(Object... var1) throws Exception {
      return new ThreadPerChannelEventLoop(this);
   }

   public Iterator<EventExecutor> iterator() {
      return new ReadOnlyIterator(this.activeChildren.iterator());
   }

   public EventLoop next() {
      throw new UnsupportedOperationException();
   }

   public Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5) {
      this.shuttingDown = true;
      Iterator var6 = this.activeChildren.iterator();

      EventLoop var7;
      while(var6.hasNext()) {
         var7 = (EventLoop)var6.next();
         var7.shutdownGracefully(var1, var3, var5);
      }

      var6 = this.idleChildren.iterator();

      while(var6.hasNext()) {
         var7 = (EventLoop)var6.next();
         var7.shutdownGracefully(var1, var3, var5);
      }

      if(this.isTerminated()) {
         this.terminationFuture.trySuccess((Object)null);
      }

      return this.terminationFuture();
   }

   public Future<?> terminationFuture() {
      return this.terminationFuture;
   }

   /** @deprecated */
   @Deprecated
   public void shutdown() {
      this.shuttingDown = true;
      Iterator var1 = this.activeChildren.iterator();

      EventLoop var2;
      while(var1.hasNext()) {
         var2 = (EventLoop)var1.next();
         var2.shutdown();
      }

      var1 = this.idleChildren.iterator();

      while(var1.hasNext()) {
         var2 = (EventLoop)var1.next();
         var2.shutdown();
      }

      if(this.isTerminated()) {
         this.terminationFuture.trySuccess((Object)null);
      }

   }

   public boolean isShuttingDown() {
      Iterator var1 = this.activeChildren.iterator();

      EventLoop var2;
      do {
         if(!var1.hasNext()) {
            var1 = this.idleChildren.iterator();

            do {
               if(!var1.hasNext()) {
                  return true;
               }

               var2 = (EventLoop)var1.next();
            } while(var2.isShuttingDown());

            return false;
         }

         var2 = (EventLoop)var1.next();
      } while(var2.isShuttingDown());

      return false;
   }

   public boolean isShutdown() {
      Iterator var1 = this.activeChildren.iterator();

      EventLoop var2;
      do {
         if(!var1.hasNext()) {
            var1 = this.idleChildren.iterator();

            do {
               if(!var1.hasNext()) {
                  return true;
               }

               var2 = (EventLoop)var1.next();
            } while(var2.isShutdown());

            return false;
         }

         var2 = (EventLoop)var1.next();
      } while(var2.isShutdown());

      return false;
   }

   public boolean isTerminated() {
      Iterator var1 = this.activeChildren.iterator();

      EventLoop var2;
      do {
         if(!var1.hasNext()) {
            var1 = this.idleChildren.iterator();

            do {
               if(!var1.hasNext()) {
                  return true;
               }

               var2 = (EventLoop)var1.next();
            } while(var2.isTerminated());

            return false;
         }

         var2 = (EventLoop)var1.next();
      } while(var2.isTerminated());

      return false;
   }

   public boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException {
      long var4 = System.nanoTime() + var3.toNanos(var1);
      Iterator var6 = this.activeChildren.iterator();

      EventLoop var7;
      long var8;
      while(var6.hasNext()) {
         var7 = (EventLoop)var6.next();

         while(true) {
            var8 = var4 - System.nanoTime();
            if(var8 <= 0L) {
               return this.isTerminated();
            }

            if(var7.awaitTermination(var8, TimeUnit.NANOSECONDS)) {
               break;
            }
         }
      }

      var6 = this.idleChildren.iterator();

      while(var6.hasNext()) {
         var7 = (EventLoop)var6.next();

         while(true) {
            var8 = var4 - System.nanoTime();
            if(var8 <= 0L) {
               return this.isTerminated();
            }

            if(var7.awaitTermination(var8, TimeUnit.NANOSECONDS)) {
               break;
            }
         }
      }

      return this.isTerminated();
   }

   public ChannelFuture register(Channel var1) {
      if(var1 == null) {
         throw new NullPointerException("channel");
      } else {
         try {
            EventLoop var2 = this.nextChild();
            return var2.register(var1, new DefaultChannelPromise(var1, var2));
         } catch (Throwable var3) {
            return new FailedChannelFuture(var1, GlobalEventExecutor.INSTANCE, var3);
         }
      }
   }

   public ChannelFuture register(Channel var1, ChannelPromise var2) {
      if(var1 == null) {
         throw new NullPointerException("channel");
      } else {
         try {
            return this.nextChild().register(var1, var2);
         } catch (Throwable var4) {
            var2.setFailure(var4);
            return var2;
         }
      }
   }

   private EventLoop nextChild() throws Exception {
      if(this.shuttingDown) {
         throw new RejectedExecutionException("shutting down");
      } else {
         ThreadPerChannelEventLoop var1 = (ThreadPerChannelEventLoop)this.idleChildren.poll();
         if(var1 == null) {
            if(this.maxChannels > 0 && this.activeChildren.size() >= this.maxChannels) {
               throw this.tooManyChannels;
            }

            var1 = this.newChild(this.childArgs);
            var1.terminationFuture().addListener(this.childTerminationListener);
         }

         this.activeChildren.add(var1);
         return var1;
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   public EventExecutor next() {
      return this.next();
   }
}
