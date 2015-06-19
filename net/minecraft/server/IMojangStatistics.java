package net.minecraft.server;

import net.minecraft.server.MojangStatisticsGenerator;

public interface IMojangStatistics {
   void a(MojangStatisticsGenerator var1);

   void b(MojangStatisticsGenerator var1);

   boolean getSnooperEnabled();
}
