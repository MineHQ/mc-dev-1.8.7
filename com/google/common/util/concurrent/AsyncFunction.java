package com.google.common.util.concurrent;

import com.google.common.util.concurrent.ListenableFuture;

public interface AsyncFunction<I, O> {
   ListenableFuture<O> apply(I var1) throws Exception;
}
