package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;

public interface Packet<T extends PacketListener> {
   void a(PacketDataSerializer var1) throws IOException;

   void b(PacketDataSerializer var1) throws IOException;

   void a(T var1);
}
