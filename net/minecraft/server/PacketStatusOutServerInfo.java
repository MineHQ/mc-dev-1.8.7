package net.minecraft.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import net.minecraft.server.ChatModifier;
import net.minecraft.server.ChatTypeAdapterFactory;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketStatusOutListener;
import net.minecraft.server.ServerPing;

public class PacketStatusOutServerInfo implements Packet<PacketStatusOutListener> {
   private static final Gson a = (new GsonBuilder()).registerTypeAdapter(ServerPing.ServerData.class, new ServerPing.ServerData.ServerData$Serializer()).registerTypeAdapter(ServerPing.ServerPingPlayerSample.class, new ServerPing.ServerPingPlayerSample.ServerPingPlayerSample$Serializer()).registerTypeAdapter(ServerPing.class, new ServerPing.Serializer()).registerTypeHierarchyAdapter(IChatBaseComponent.class, new IChatBaseComponent.ChatSerializer()).registerTypeHierarchyAdapter(ChatModifier.class, new ChatModifier.ChatModifierSerializer()).registerTypeAdapterFactory(new ChatTypeAdapterFactory()).create();
   private ServerPing b;

   public PacketStatusOutServerInfo() {
   }

   public PacketStatusOutServerInfo(ServerPing var1) {
      this.b = var1;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.b = (ServerPing)a.fromJson(var1.c(32767), ServerPing.class);
   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(a.toJson((Object)this.b));
   }

   public void a(PacketStatusOutListener var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketStatusOutListener)var1);
   }
}
