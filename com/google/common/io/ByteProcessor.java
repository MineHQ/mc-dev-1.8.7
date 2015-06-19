package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.IOException;

@Beta
public interface ByteProcessor<T> {
   boolean processBytes(byte[] var1, int var2, int var3) throws IOException;

   T getResult();
}
