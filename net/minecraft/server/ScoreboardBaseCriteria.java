package net.minecraft.server;

import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IScoreboardCriteria;

public class ScoreboardBaseCriteria implements IScoreboardCriteria {
   private final String j;

   public ScoreboardBaseCriteria(String var1) {
      this.j = var1;
      IScoreboardCriteria.criteria.put(var1, this);
   }

   public String getName() {
      return this.j;
   }

   public int getScoreModifier(List<EntityHuman> var1) {
      return 0;
   }

   public boolean isReadOnly() {
      return false;
   }

   public IScoreboardCriteria.EnumScoreboardHealthDisplay c() {
      return IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER;
   }
}
