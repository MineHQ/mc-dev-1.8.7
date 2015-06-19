package net.minecraft.server;

import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PacketStatusInListener;
import net.minecraft.server.PacketStatusInPing;
import net.minecraft.server.PacketStatusInStart;
import net.minecraft.server.PacketStatusOutPong;
import net.minecraft.server.PacketStatusOutServerInfo;

public class PacketStatusListener implements PacketStatusInListener {
   private final MinecraftServer minecraftServer;
   private final NetworkManager networkManager;

   public PacketStatusListener(MinecraftServer var1, NetworkManager var2) {
      this.minecraftServer = var1;
      this.networkManager = var2;
   }

   public void a(IChatBaseComponent var1) {
   }

   public void a(PacketStatusInStart var1) {
      this.networkManager.handle(new PacketStatusOutServerInfo(this.minecraftServer.aG()));
   }

   public void a(PacketStatusInPing var1) {
      this.networkManager.handle(new PacketStatusOutPong(var1.a()));
   }
}
