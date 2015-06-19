package net.minecraft.server;

import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IScoreboardCriteria;

public class ScoreboardCriteriaInteger implements IScoreboardCriteria {
   private final String j;

   public ScoreboardCriteriaInteger(String var1, EnumChatFormat var2) {
      this.j = var1 + var2.e();
      IScoreboardCriteria.criteria.put(this.j, this);
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
