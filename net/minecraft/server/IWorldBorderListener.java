package net.minecraft.server;

import net.minecraft.server.WorldBorder;

public interface IWorldBorderListener {
   void a(WorldBorder var1, double var2);

   void a(WorldBorder var1, double var2, double var4, long var6);

   void a(WorldBorder var1, double var2, double var4);

   void a(WorldBorder var1, int var2);

   void b(WorldBorder var1, int var2);

   void b(WorldBorder var1, double var2);

   void c(WorldBorder var1, double var2);
}
