package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.MathHelper;
import net.minecraft.server.ScoreboardBaseCriteria;

public class ScoreboardHealthCriteria extends ScoreboardBaseCriteria {
   public ScoreboardHealthCriteria(String var1) {
      super(var1);
   }

   public int getScoreModifier(List<EntityHuman> var1) {
      float var2 = 0.0F;

      EntityHuman var4;
      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 += var4.getHealth() + var4.getAbsorptionHearts()) {
         var4 = (EntityHuman)var3.next();
      }

      if(var1.size() > 0) {
         var2 /= (float)var1.size();
      }

      return MathHelper.f(var2);
   }

   public boolean isReadOnly() {
      return true;
   }

   public IScoreboardCriteria.EnumScoreboardHealthDisplay c() {
      return IScoreboardCriteria.EnumScoreboardHealthDisplay.HEARTS;
   }
}
