package net.minecraft.server;

import net.minecraft.server.CancelledPacketHandleException;
import net.minecraft.server.IAsyncTaskHandler;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketListener;

public class PlayerConnectionUtils {
   public static <T extends PacketListener> void ensureMainThread(final Packet<T> var0, final T var1, IAsyncTaskHandler var2) throws CancelledPacketHandleException {
      if(!var2.isMainThread()) {
         var2.postToMainThread(new Runnable() {
            public void run() {
               var0.a(var1);
            }
         });
         throw CancelledPacketHandleException.INSTANCE;
      }
   }
}
