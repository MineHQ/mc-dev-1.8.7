package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import java.util.EventListener;

public interface GenericFutureListener<F extends Future<?>> extends EventListener {
   void operationComplete(F var1) throws Exception;
}
