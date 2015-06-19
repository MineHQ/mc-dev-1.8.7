package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;

public class PacketPlayInUpdateSign implements Packet<PacketListenerPlayIn> {
   private BlockPosition a;
   private IChatBaseComponent[] b;

   public PacketPlayInUpdateSign() {
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c();
      this.b = new IChatBaseComponent[4];

      for(int var2 = 0; var2 < 4; ++var2) {
         String var3 = var1.c(384);
         IChatBaseComponent var4 = IChatBaseComponent.ChatSerializer.a(var3);
         this.b[var2] = var4;
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);

      for(int var2 = 0; var2 < 4; ++var2) {
         IChatBaseComponent var3 = this.b[var2];
         String var4 = IChatBaseComponent.ChatSerializer.a(var3);
         var1.a(var4);
      }

   }

   public void a(PacketListenerPlayIn var1) {
      var1.a(this);
   }

   public BlockPosition a() {
      return this.a;
   }

   public IChatBaseComponent[] b() {
      return this.b;
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayIn)var1);
   }
}
