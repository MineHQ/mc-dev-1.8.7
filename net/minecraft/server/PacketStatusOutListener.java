package net.minecraft.server;

import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketStatusOutPong;
import net.minecraft.server.PacketStatusOutServerInfo;

public interface PacketStatusOutListener extends PacketListener {
   void a(PacketStatusOutServerInfo var1);

   void a(PacketStatusOutPong var1);
}
