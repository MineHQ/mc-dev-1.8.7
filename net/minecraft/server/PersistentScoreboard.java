package net.minecraft.server;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.ScoreboardTeam;
import net.minecraft.server.ScoreboardTeamBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistentScoreboard extends PersistentBase {
   private static final Logger b = LogManager.getLogger();
   private Scoreboard c;
   private NBTTagCompound d;

   public PersistentScoreboard() {
      this("scoreboard");
   }

   public PersistentScoreboard(String var1) {
      super(var1);
   }

   public void a(Scoreboard var1) {
      this.c = var1;
      if(this.d != null) {
         this.a(this.d);
      }

   }

   public void a(NBTTagCompound var1) {
      if(this.c == null) {
         this.d = var1;
      } else {
         this.b(var1.getList("Objectives", 10));
         this.c(var1.getList("PlayerScores", 10));
         if(var1.hasKeyOfType("DisplaySlots", 10)) {
            this.c(var1.getCompound("DisplaySlots"));
         }

         if(var1.hasKeyOfType("Teams", 9)) {
            this.a(var1.getList("Teams", 10));
         }

      }
   }

   protected void a(NBTTagList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         NBTTagCompound var3 = var1.get(var2);
         String var4 = var3.getString("Name");
         if(var4.length() > 16) {
            var4 = var4.substring(0, 16);
         }

         ScoreboardTeam var5 = this.c.createTeam(var4);
         String var6 = var3.getString("DisplayName");
         if(var6.length() > 32) {
            var6 = var6.substring(0, 32);
         }

         var5.setDisplayName(var6);
         if(var3.hasKeyOfType("TeamColor", 8)) {
            var5.a(EnumChatFormat.b(var3.getString("TeamColor")));
         }

         var5.setPrefix(var3.getString("Prefix"));
         var5.setSuffix(var3.getString("Suffix"));
         if(var3.hasKeyOfType("AllowFriendlyFire", 99)) {
            var5.setAllowFriendlyFire(var3.getBoolean("AllowFriendlyFire"));
         }

         if(var3.hasKeyOfType("SeeFriendlyInvisibles", 99)) {
            var5.setCanSeeFriendlyInvisibles(var3.getBoolean("SeeFriendlyInvisibles"));
         }

         ScoreboardTeamBase.EnumNameTagVisibility var7;
         if(var3.hasKeyOfType("NameTagVisibility", 8)) {
            var7 = ScoreboardTeamBase.EnumNameTagVisibility.a(var3.getString("NameTagVisibility"));
            if(var7 != null) {
               var5.setNameTagVisibility(var7);
            }
         }

         if(var3.hasKeyOfType("DeathMessageVisibility", 8)) {
            var7 = ScoreboardTeamBase.EnumNameTagVisibility.a(var3.getString("DeathMessageVisibility"));
            if(var7 != null) {
               var5.b(var7);
            }
         }

         this.a(var5, var3.getList("Players", 8));
      }

   }

   protected void a(ScoreboardTeam var1, NBTTagList var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         this.c.addPlayerToTeam(var2.getString(var3), var1.getName());
      }

   }

   protected void c(NBTTagCompound var1) {
      for(int var2 = 0; var2 < 19; ++var2) {
         if(var1.hasKeyOfType("slot_" + var2, 8)) {
            String var3 = var1.getString("slot_" + var2);
            ScoreboardObjective var4 = this.c.getObjective(var3);
            this.c.setDisplaySlot(var2, var4);
         }
      }

   }

   protected void b(NBTTagList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         NBTTagCompound var3 = var1.get(var2);
         IScoreboardCriteria var4 = (IScoreboardCriteria)IScoreboardCriteria.criteria.get(var3.getString("CriteriaName"));
         if(var4 != null) {
            String var5 = var3.getString("Name");
            if(var5.length() > 16) {
               var5 = var5.substring(0, 16);
            }

            ScoreboardObjective var6 = this.c.registerObjective(var5, var4);
            var6.setDisplayName(var3.getString("DisplayName"));
            var6.a(IScoreboardCriteria.EnumScoreboardHealthDisplay.a(var3.getString("RenderType")));
         }
      }

   }

   protected void c(NBTTagList var1) {
      for(int var2 = 0; var2 < var1.size(); ++var2) {
         NBTTagCompound var3 = var1.get(var2);
         ScoreboardObjective var4 = this.c.getObjective(var3.getString("Objective"));
         String var5 = var3.getString("Name");
         if(var5.length() > 40) {
            var5 = var5.substring(0, 40);
         }

         ScoreboardScore var6 = this.c.getPlayerScoreForObjective(var5, var4);
         var6.setScore(var3.getInt("Score"));
         if(var3.hasKey("Locked")) {
            var6.a(var3.getBoolean("Locked"));
         }
      }

   }

   public void b(NBTTagCompound var1) {
      if(this.c == null) {
         b.warn("Tried to save scoreboard without having a scoreboard...");
      } else {
         var1.set("Objectives", this.b());
         var1.set("PlayerScores", this.e());
         var1.set("Teams", this.a());
         this.d(var1);
      }
   }

   protected NBTTagList a() {
      NBTTagList var1 = new NBTTagList();
      Collection var2 = this.c.getTeams();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         ScoreboardTeam var4 = (ScoreboardTeam)var3.next();
         NBTTagCompound var5 = new NBTTagCompound();
         var5.setString("Name", var4.getName());
         var5.setString("DisplayName", var4.getDisplayName());
         if(var4.l().b() >= 0) {
            var5.setString("TeamColor", var4.l().e());
         }

         var5.setString("Prefix", var4.getPrefix());
         var5.setString("Suffix", var4.getSuffix());
         var5.setBoolean("AllowFriendlyFire", var4.allowFriendlyFire());
         var5.setBoolean("SeeFriendlyInvisibles", var4.canSeeFriendlyInvisibles());
         var5.setString("NameTagVisibility", var4.getNameTagVisibility().e);
         var5.setString("DeathMessageVisibility", var4.j().e);
         NBTTagList var6 = new NBTTagList();
         Iterator var7 = var4.getPlayerNameSet().iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            var6.add(new NBTTagString(var8));
         }

         var5.set("Players", var6);
         var1.add(var5);
      }

      return var1;
   }

   protected void d(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      boolean var3 = false;

      for(int var4 = 0; var4 < 19; ++var4) {
         ScoreboardObjective var5 = this.c.getObjectiveForSlot(var4);
         if(var5 != null) {
            var2.setString("slot_" + var4, var5.getName());
            var3 = true;
         }
      }

      if(var3) {
         var1.set("DisplaySlots", var2);
      }

   }

   protected NBTTagList b() {
      NBTTagList var1 = new NBTTagList();
      Collection var2 = this.c.getObjectives();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         ScoreboardObjective var4 = (ScoreboardObjective)var3.next();
         if(var4.getCriteria() != null) {
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getName());
            var5.setString("CriteriaName", var4.getCriteria().getName());
            var5.setString("DisplayName", var4.getDisplayName());
            var5.setString("RenderType", var4.e().a());
            var1.add(var5);
         }
      }

      return var1;
   }

   protected NBTTagList e() {
      NBTTagList var1 = new NBTTagList();
      Collection var2 = this.c.getScores();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         ScoreboardScore var4 = (ScoreboardScore)var3.next();
         if(var4.getObjective() != null) {
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setString("Name", var4.getPlayerName());
            var5.setString("Objective", var4.getObjective().getName());
            var5.setInt("Score", var4.getScore());
            var5.setBoolean("Locked", var4.g());
            var1.add(var5);
         }
      }

      return var1;
   }
}
