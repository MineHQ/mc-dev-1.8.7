package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFlushPromiseNotifier;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressivePromise;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;

public class DefaultChannelProgressivePromise extends DefaultProgressivePromise<Void> implements ChannelProgressivePromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
   private final Channel channel;
   private long checkpoint;

   public DefaultChannelProgressivePromise(Channel var1) {
      this.channel = var1;
   }

   public DefaultChannelProgressivePromise(Channel var1, EventExecutor var2) {
      super(var2);
      this.channel = var1;
   }

   protected EventExecutor executor() {
      EventExecutor var1 = super.executor();
      return (EventExecutor)(var1 == null?this.channel().eventLoop():var1);
   }

   public Channel channel() {
      return this.channel;
   }

   public ChannelProgressivePromise setSuccess() {
      return this.setSuccess((Void)null);
   }

   public ChannelProgressivePromise setSuccess(Void var1) {
      super.setSuccess(var1);
      return this;
   }

   public boolean trySuccess() {
      return this.trySuccess((Object)null);
   }

   public ChannelProgressivePromise setFailure(Throwable var1) {
      super.setFailure(var1);
      return this;
   }

   public ChannelProgressivePromise setProgress(long var1, long var3) {
      super.setProgress(var1, var3);
      return this;
   }

   public ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> var1) {
      super.addListener(var1);
      return this;
   }

   public ChannelProgressivePromise addListeners(GenericFutureListener... var1) {
      super.addListeners(var1);
      return this;
   }

   public ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1) {
      super.removeListener(var1);
      return this;
   }

   public ChannelProgressivePromise removeListeners(GenericFutureListener... var1) {
      super.removeListeners(var1);
      return this;
   }

   public ChannelProgressivePromise sync() throws InterruptedException {
      super.sync();
      return this;
   }

   public ChannelProgressivePromise syncUninterruptibly() {
      super.syncUninterruptibly();
      return this;
   }

   public ChannelProgressivePromise await() throws InterruptedException {
      super.await();
      return this;
   }

   public ChannelProgressivePromise awaitUninterruptibly() {
      super.awaitUninterruptibly();
      return this;
   }

   public long flushCheckpoint() {
      return this.checkpoint;
   }

   public void flushCheckpoint(long var1) {
      this.checkpoint = var1;
   }

   public ChannelProgressivePromise promise() {
      return this;
   }

   protected void checkDeadLock() {
      if(this.channel().isRegistered()) {
         super.checkDeadLock();
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise setFailure(Throwable var1) {
      return this.setFailure(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise setSuccess(Object var1) {
      return this.setSuccess((Void)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressivePromise setProgress(long var1, long var3) {
      return this.setProgress(var1, var3);
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
   public Promise setSuccess(Object var1) {
      return this.setSuccess((Void)var1);
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
   public ProgressiveFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ProgressiveFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture awaitUninterruptibly() {
      return this.awaitUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture await() throws InterruptedException {
      return this.await();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture syncUninterruptibly() {
      return this.syncUninterruptibly();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture sync() throws InterruptedException {
      return this.sync();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture removeListeners(GenericFutureListener[] var1) {
      return this.removeListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture removeListener(GenericFutureListener var1) {
      return this.removeListener(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture addListeners(GenericFutureListener[] var1) {
      return this.addListeners(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ChannelProgressiveFuture addListener(GenericFutureListener var1) {
      return this.addListener(var1);
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
   public ChannelPromise promise() {
      return this.promise();
   }
}
