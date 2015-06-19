package net.minecraft.server;

import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginInEncryptionBegin;
import net.minecraft.server.PacketLoginInStart;

public interface PacketLoginInListener extends PacketListener {
   void a(PacketLoginInStart var1);

   void a(PacketLoginInEncryptionBegin var1);
}
