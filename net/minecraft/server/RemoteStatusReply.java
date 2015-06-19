package net.minecraft.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemoteStatusReply {
   private ByteArrayOutputStream buffer;
   private DataOutputStream stream;

   public RemoteStatusReply(int var1) {
      this.buffer = new ByteArrayOutputStream(var1);
      this.stream = new DataOutputStream(this.buffer);
   }

   public void a(byte[] var1) throws IOException {
      this.stream.write(var1, 0, var1.length);
   }

   public void a(String var1) throws IOException {
      this.stream.writeBytes(var1);
      this.stream.write(0);
   }

   public void a(int var1) throws IOException {
      this.stream.write(var1);
   }

   public void a(short var1) throws IOException {
      this.stream.writeShort(Short.reverseBytes(var1));
   }

   public byte[] a() {
      return this.buffer.toByteArray();
   }

   public void b() {
      this.buffer.reset();
   }
}
