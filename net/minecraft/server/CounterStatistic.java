package net.minecraft.server;

import net.minecraft.server.Counter;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;

public class CounterStatistic extends Statistic {
   public CounterStatistic(String var1, IChatBaseComponent var2, Counter var3) {
      super(var1, var2, var3);
   }

   public CounterStatistic(String var1, IChatBaseComponent var2) {
      super(var1, var2);
   }

   public Statistic h() {
      super.h();
      StatisticList.c.add(this);
      return this;
   }
}
