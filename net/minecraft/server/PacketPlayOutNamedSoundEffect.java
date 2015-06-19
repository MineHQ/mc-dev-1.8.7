package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import org.apache.commons.lang3.Validate;

public class PacketPlayOutNamedSoundEffect implements Packet<PacketListenerPlayOut> {
   private String a;
   private int b;
   private int c = Integer.MAX_VALUE;
   private int d;
   private float e;
   private int f;

   public PacketPlayOutNamedSoundEffect() {
   }

   public PacketPlayOutNamedSoundEffect(String var1, double var2, double var4, double var6, float var8, float var9) {
      Validate.notNull(var1, "name", new Object[0]);
      this.a = var1;
      this.b = (int)(var2 * 8.0D);
      this.c = (int)(var4 * 8.0D);
      this.d = (int)(var6 * 8.0D);
      this.e = var8;
      this.f = (int)(var9 * 63.0F);
      var9 = MathHelper.a(var9, 0.0F, 255.0F);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(256);
      this.b = var1.readInt();
      this.c = var1.readInt();
      this.d = var1.readInt();
      this.e = var1.readFloat();
      this.f = var1.readUnsignedByte();
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.writeInt(this.b);
      var1.writeInt(this.c);
      var1.writeInt(this.d);
      var1.writeFloat(this.e);
      var1.writeByte(this.f);
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
