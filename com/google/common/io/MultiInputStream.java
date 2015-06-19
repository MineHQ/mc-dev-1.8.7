package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

final class MultiInputStream extends InputStream {
   private Iterator<? extends ByteSource> it;
   private InputStream in;

   public MultiInputStream(Iterator<? extends ByteSource> var1) throws IOException {
      this.it = (Iterator)Preconditions.checkNotNull(var1);
      this.advance();
   }

   public void close() throws IOException {
      if(this.in != null) {
         try {
            this.in.close();
         } finally {
            this.in = null;
         }
      }

   }

   private void advance() throws IOException {
      this.close();
      if(this.it.hasNext()) {
         this.in = ((ByteSource)this.it.next()).openStream();
      }

   }

   public int available() throws IOException {
      return this.in == null?0:this.in.available();
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      if(this.in == null) {
         return -1;
      } else {
         int var1 = this.in.read();
         if(var1 == -1) {
            this.advance();
            return this.read();
         } else {
            return var1;
         }
      }
   }

   public int read(@Nullable byte[] var1, int var2, int var3) throws IOException {
      if(this.in == null) {
         return -1;
      } else {
         int var4 = this.in.read(var1, var2, var3);
         if(var4 == -1) {
            this.advance();
            return this.read(var1, var2, var3);
         } else {
            return var4;
         }
      }
   }

   public long skip(long var1) throws IOException {
      if(this.in != null && var1 > 0L) {
         long var3 = this.in.skip(var1);
         return var3 != 0L?var3:(this.read() == -1?0L:1L + this.in.skip(var1 - 1L));
      } else {
         return 0L;
      }
   }
}
