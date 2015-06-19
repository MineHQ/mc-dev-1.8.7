package io.netty.channel;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface ChannelFuture extends Future<Void> {
   Channel channel();

   ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> var1);

   ChannelFuture addListeners(GenericFutureListener... var1);

   ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

   ChannelFuture removeListeners(GenericFutureListener... var1);

   ChannelFuture sync() throws InterruptedException;

   ChannelFuture syncUninterruptibly();

   ChannelFuture await() throws InterruptedException;

   ChannelFuture awaitUninterruptibly();
}
