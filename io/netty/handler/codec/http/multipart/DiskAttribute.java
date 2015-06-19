package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.AbstractDiskHttpData;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.ReferenceCounted;
import java.io.IOException;

public class DiskAttribute extends AbstractDiskHttpData implements Attribute {
   public static String baseDirectory;
   public static boolean deleteOnExitTemporaryFile = true;
   public static final String prefix = "Attr_";
   public static final String postfix = ".att";

   public DiskAttribute(String var1) {
      super(var1, HttpConstants.DEFAULT_CHARSET, 0L);
   }

   public DiskAttribute(String var1, String var2) throws IOException {
      super(var1, HttpConstants.DEFAULT_CHARSET, 0L);
      this.setValue(var2);
   }

   public InterfaceHttpData.HttpDataType getHttpDataType() {
      return InterfaceHttpData.HttpDataType.Attribute;
   }

   public String getValue() throws IOException {
      byte[] var1 = this.get();
      return new String(var1, this.charset.name());
   }

   public void setValue(String var1) throws IOException {
      if(var1 == null) {
         throw new NullPointerException("value");
      } else {
         byte[] var2 = var1.getBytes(this.charset.name());
         ByteBuf var3 = Unpooled.wrappedBuffer(var2);
         if(this.definedSize > 0L) {
            this.definedSize = (long)var3.readableBytes();
         }

         this.setContent(var3);
      }
   }

   public void addContent(ByteBuf var1, boolean var2) throws IOException {
      int var3 = var1.readableBytes();
      if(this.definedSize > 0L && this.definedSize < this.size + (long)var3) {
         this.definedSize = this.size + (long)var3;
      }

      super.addContent(var1, var2);
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public boolean equals(Object var1) {
      if(!(var1 instanceof Attribute)) {
         return false;
      } else {
         Attribute var2 = (Attribute)var1;
         return this.getName().equalsIgnoreCase(var2.getName());
      }
   }

   public int compareTo(InterfaceHttpData var1) {
      if(!(var1 instanceof Attribute)) {
         throw new ClassCastException("Cannot compare " + this.getHttpDataType() + " with " + var1.getHttpDataType());
      } else {
         return this.compareTo((Attribute)var1);
      }
   }

   public int compareTo(Attribute var1) {
      return this.getName().compareToIgnoreCase(var1.getName());
   }

   public String toString() {
      try {
         return this.getName() + '=' + this.getValue();
      } catch (IOException var2) {
         return this.getName() + "=IoException";
      }
   }

   protected boolean deleteOnExit() {
      return deleteOnExitTemporaryFile;
   }

   protected String getBaseDirectory() {
      return baseDirectory;
   }

   protected String getDiskFilename() {
      return this.getName() + ".att";
   }

   protected String getPostfix() {
      return ".att";
   }

   protected String getPrefix() {
      return "Attr_";
   }

   public Attribute copy() {
      DiskAttribute var1 = new DiskAttribute(this.getName());
      var1.setCharset(this.getCharset());
      ByteBuf var2 = this.content();
      if(var2 != null) {
         try {
            var1.setContent(var2.copy());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public Attribute duplicate() {
      DiskAttribute var1 = new DiskAttribute(this.getName());
      var1.setCharset(this.getCharset());
      ByteBuf var2 = this.content();
      if(var2 != null) {
         try {
            var1.setContent(var2.duplicate());
         } catch (IOException var4) {
            throw new ChannelException(var4);
         }
      }

      return var1;
   }

   public Attribute retain(int var1) {
      super.retain(var1);
      return this;
   }

   public Attribute retain() {
      super.retain();
      return this;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public HttpData retain(int var1) {
      return this.retain(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public HttpData retain() {
      return this.retain();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public HttpData duplicate() {
      return this.duplicate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public HttpData copy() {
      return this.copy();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public int compareTo(Object var1) {
      return this.compareTo((InterfaceHttpData)var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ReferenceCounted retain(int var1) {
      return this.retain(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ReferenceCounted retain() {
      return this.retain();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ByteBufHolder retain(int var1) {
      return this.retain(var1);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ByteBufHolder retain() {
      return this.retain();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ByteBufHolder duplicate() {
      return this.duplicate();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public ByteBufHolder copy() {
      return this.copy();
   }
}
