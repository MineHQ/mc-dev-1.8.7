package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.server.Achievement;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IJsonStatistic;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticWrapper;

public class StatisticManager {
   protected final Map<Statistic, StatisticWrapper> a = Maps.newConcurrentMap();

   public StatisticManager() {
   }

   public boolean hasAchievement(Achievement var1) {
      return this.getStatisticValue(var1) > 0;
   }

   public boolean b(Achievement var1) {
      return var1.c == null || this.hasAchievement(var1.c);
   }

   public void b(EntityHuman var1, Statistic var2, int var3) {
      if(!var2.d() || this.b((Achievement)var2)) {
         this.setStatistic(var1, var2, this.getStatisticValue(var2) + var3);
      }
   }

   public void setStatistic(EntityHuman var1, Statistic var2, int var3) {
      StatisticWrapper var4 = (StatisticWrapper)this.a.get(var2);
      if(var4 == null) {
         var4 = new StatisticWrapper();
         this.a.put(var2, var4);
      }

      var4.a(var3);
   }

   public int getStatisticValue(Statistic var1) {
      StatisticWrapper var2 = (StatisticWrapper)this.a.get(var1);
      return var2 == null?0:var2.a();
   }

   public <T extends IJsonStatistic> T b(Statistic var1) {
      StatisticWrapper var2 = (StatisticWrapper)this.a.get(var1);
      return var2 != null?var2.b():null;
   }

   public <T extends IJsonStatistic> T a(Statistic var1, T var2) {
      StatisticWrapper var3 = (StatisticWrapper)this.a.get(var1);
      if(var3 == null) {
         var3 = new StatisticWrapper();
         this.a.put(var1, var3);
      }

      var3.a(var2);
      return var2;
   }
}
