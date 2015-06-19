package net.minecraft.server;

import java.util.Comparator;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;

public class ScoreboardScore {
   public static final Comparator<ScoreboardScore> a = new Comparator() {
      public int a(ScoreboardScore var1, ScoreboardScore var2) {
         return var1.getScore() > var2.getScore()?1:(var1.getScore() < var2.getScore()?-1:var2.getPlayerName().compareToIgnoreCase(var1.getPlayerName()));
      }

      // $FF: synthetic method
      public int compare(Object var1, Object var2) {
         return this.a((ScoreboardScore)var1, (ScoreboardScore)var2);
      }
   };
   private final Scoreboard b;
   private final ScoreboardObjective c;
   private final String playerName;
   private int score;
   private boolean f;
   private boolean g;

   public ScoreboardScore(Scoreboard var1, ScoreboardObjective var2, String var3) {
      this.b = var1;
      this.c = var2;
      this.playerName = var3;
      this.g = true;
   }

   public void addScore(int var1) {
      if(this.c.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScore(this.getScore() + var1);
      }
   }

   public void removeScore(int var1) {
      if(this.c.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.setScore(this.getScore() - var1);
      }
   }

   public void incrementScore() {
      if(this.c.getCriteria().isReadOnly()) {
         throw new IllegalStateException("Cannot modify read-only score");
      } else {
         this.addScore(1);
      }
   }

   public int getScore() {
      return this.score;
   }

   public void setScore(int var1) {
      int var2 = this.score;
      this.score = var1;
      if(var2 != var1 || this.g) {
         this.g = false;
         this.f().handleScoreChanged(this);
      }

   }

   public ScoreboardObjective getObjective() {
      return this.c;
   }

   public String getPlayerName() {
      return this.playerName;
   }

   public Scoreboard f() {
      return this.b;
   }

   public boolean g() {
      return this.f;
   }

   public void a(boolean var1) {
      this.f = var1;
   }

   public void updateForList(List<EntityHuman> var1) {
      this.setScore(this.c.getCriteria().getScoreModifier(var1));
   }
}
