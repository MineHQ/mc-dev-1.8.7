package net.minecraft.server;

import net.minecraft.server.ScoreboardBaseCriteria;
import net.minecraft.server.Statistic;

public class ScoreboardStatisticCriteria extends ScoreboardBaseCriteria {
   private final Statistic j;

   public ScoreboardStatisticCriteria(Statistic var1) {
      super(var1.name);
      this.j = var1;
   }
}
