package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

final class HttpHeaderEntity implements CharSequence {
   private final String name;
   private final int hash;
   private final byte[] bytes;
   private final int separatorLen;

   public HttpHeaderEntity(String var1) {
      this(var1, (byte[])null);
   }

   public HttpHeaderEntity(String var1, byte[] var2) {
      this.name = var1;
      this.hash = HttpHeaders.hash(var1);
      byte[] var3 = var1.getBytes(CharsetUtil.US_ASCII);
      if(var2 == null) {
         this.bytes = var3;
         this.separatorLen = 0;
      } else {
         this.separatorLen = var2.length;
         this.bytes = new byte[var3.length + var2.length];
         System.arraycopy(var3, 0, this.bytes, 0, var3.length);
         System.arraycopy(var2, 0, this.bytes, var3.length, var2.length);
      }

   }

   int hash() {
      return this.hash;
   }

   public int length() {
      return this.bytes.length - this.separatorLen;
   }

   public char charAt(int var1) {
      if(this.bytes.length - this.separatorLen <= var1) {
         throw new IndexOutOfBoundsException();
      } else {
         return (char)this.bytes[var1];
      }
   }

   public CharSequence subSequence(int var1, int var2) {
      return new HttpHeaderEntity(this.name.substring(var1, var2));
   }

   public String toString() {
      return this.name;
   }

   boolean encode(ByteBuf var1) {
      var1.writeBytes(this.bytes);
      return this.separatorLen > 0;
   }
}
