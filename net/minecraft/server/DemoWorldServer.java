package net.minecraft.server;

import net.minecraft.server.IDataManager;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public class DemoWorldServer extends WorldServer {
   private static final long I = (long)"North Carolina".hashCode();
   public static final WorldSettings a;

   public DemoWorldServer(MinecraftServer var1, IDataManager var2, WorldData var3, int var4, MethodProfiler var5) {
      super(var1, var2, var3, var4, var5);
      this.worldData.a(a);
   }

   static {
      a = (new WorldSettings(I, WorldSettings.EnumGamemode.SURVIVAL, true, false, WorldType.NORMAL)).a();
   }
}
