package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.EnumProtocol;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.LoginListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketHandshakingInListener;
import net.minecraft.server.PacketHandshakingInSetProtocol;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginOutDisconnect;
import net.minecraft.server.PacketStatusListener;

public class HandshakeListener implements PacketHandshakingInListener {
   private final MinecraftServer a;
   private final NetworkManager b;

   public HandshakeListener(MinecraftServer var1, NetworkManager var2) {
      this.a = var1;
      this.b = var2;
   }

   public void a(PacketHandshakingInSetProtocol var1) {
      switch(HandshakeListener.SyntheticClass_1.a[var1.a().ordinal()]) {
      case 1:
         this.b.a(EnumProtocol.LOGIN);
         ChatComponentText var2;
         if(var1.b() > 47) {
            var2 = new ChatComponentText("Outdated server! I\'m still on 1.8.7");
            this.b.handle(new PacketLoginOutDisconnect(var2));
            this.b.close(var2);
         } else if(var1.b() < 47) {
            var2 = new ChatComponentText("Outdated client! Please use 1.8.7");
            this.b.handle(new PacketLoginOutDisconnect(var2));
            this.b.close(var2);
         } else {
            this.b.a((PacketListener)(new LoginListener(this.a, this.b)));
         }
         break;
      case 2:
         this.b.a(EnumProtocol.STATUS);
         this.b.a((PacketListener)(new PacketStatusListener(this.a, this.b)));
         break;
      default:
         throw new UnsupportedOperationException("Invalid intention " + var1.a());
      }

   }

   public void a(IChatBaseComponent var1) {
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumProtocol.values().length];

      static {
         try {
            a[EnumProtocol.LOGIN.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumProtocol.STATUS.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
