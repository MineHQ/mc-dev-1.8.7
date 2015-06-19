package net.minecraft.server;

import net.minecraft.server.IJsonStatistic;

public class StatisticWrapper {
   private int a;
   private IJsonStatistic b;

   public StatisticWrapper() {
   }

   public int a() {
      return this.a;
   }

   public void a(int var1) {
      this.a = var1;
   }

   public <T extends IJsonStatistic> T b() {
      return this.b;
   }

   public void a(IJsonStatistic var1) {
      this.b = var1;
   }
}
