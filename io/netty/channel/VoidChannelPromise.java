package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.TimeUnit;

final class VoidChannelPromise extends AbstractFuture<Void> implements ChannelPromise {
   private final Channel channel;
   private final boolean fireException;

   VoidChannelPromise(Channel var1, boolean var2) {
      if(var1 == null) {
         throw new NullPointerException("channel");
      } else {
         this.channel = var1;
         this.fireException = var2;
      }
   }

   public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> var1) {
      fail();
      return this;
   }

   public VoidChannelPromise addListeners(GenericFutureListener... var1) {
      fail();
      return this;
   }

   public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1) {
      return this;
   }

   public VoidChannelPromise removeListeners(GenericFutureListener... var1) {
      return this;
   }

   public VoidChannelPromise await() throws InterruptedException {
      if(Thread.interrupted()) {
         throw new InterruptedException();
      } else {
         return this;
      }
   }

   public boolean await(long var1, TimeUnit var3) {
      fail();
      return false;
   }

   public boolean await(long var1) {
      fail();
      return false;
   }

   public VoidChannelPromise awaitUninterruptibly() {
      fail();
      return this;
   }

   public boolean awaitUninterruptibly(long var1, TimeUnit var3) {
      fail();
      return false;
   }

   public boolean awaitUninterruptibly(long var1) {
      fail();
      return false;
   }

   public Channel channel() {
      return this.channel;
   }

   public boolean isDone() {
      return false;
   }

   public boolean isSuccess() {
      return false;
   }

   public boolean setUncancellable() {
      return true;
   }

   public boolean isCancellable() {
      return false;
   }

   public boolean isCancelled() {
      return false;
   }

   public Throwable cause() {
      return null;
   }

   public VoidChannelPromise sync() {
      fail();
      return this;
   }

   public VoidChannelPromise syncUninterruptibly() {
      fail();
      return this;
   }

   public VoidChannelPromise setFailure(Throwable var1) {
      this.fireException(var1);
      return this;
   }

   public VoidChannelPromise setSuccess() {
      return this;
   }

   public boolean tryFailure(Throwable var1) {
      this.fireException(var1);
      return false;
   }

   public boolean cancel(boolean var1) {
      return false;
   }

   public boolean trySuccess() {
      return false;
   }

   private static void fail() {
      throw new IllegalStateException("void future");
   }

   public VoidChannelPromise setSuccess(Void var1) {
      return this;
   }

   public boolean trySuccess(Void var1) {
      return false;
   }

   public Void getNow() {
      return null;
   }

   private void fireException(Throwable var1) {
      if(this.fireException && this.channel.isRegistered()) {
         this.channel.pipeline().fireExceptionCaught(var1);
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object getNow() {
      return this.getNow();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Future addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise setSuccess() {
      return this.setSuccess();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelPromise setSuccess(Void var1) {
      return this.setSuccess(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public boolean trySuccess(Object var1) {
      return this.trySuccess((Void)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Promise setSuccess(Object var1) {
      return this.setSuccess((Void)var1);
   }
}
