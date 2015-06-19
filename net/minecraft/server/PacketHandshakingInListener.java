package net.minecraft.server;

import net.minecraft.server.PacketHandshakingInSetProtocol;
import net.minecraft.server.PacketListener;

public interface PacketHandshakingInListener extends PacketListener {
   void a(PacketHandshakingInSetProtocol var1);
}
