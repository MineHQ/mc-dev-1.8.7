package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;

public abstract class ZlibDecoder extends ByteToMessageDecoder {
   public ZlibDecoder() {
   }

   public abstract boolean isClosed();
}
