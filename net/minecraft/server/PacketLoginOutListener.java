package net.minecraft.server;

import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketLoginOutDisconnect;
import net.minecraft.server.PacketLoginOutEncryptionBegin;
import net.minecraft.server.PacketLoginOutSetCompression;
import net.minecraft.server.PacketLoginOutSuccess;

public interface PacketLoginOutListener extends PacketListener {
   void a(PacketLoginOutEncryptionBegin var1);

   void a(PacketLoginOutSuccess var1);

   void a(PacketLoginOutDisconnect var1);

   void a(PacketLoginOutSetCompression var1);
}
