package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.ScoreboardTeam;

public class Scoreboard {
   private final Map<String, ScoreboardObjective> objectivesByName = Maps.newHashMap();
   private final Map<IScoreboardCriteria, List<ScoreboardObjective>> objectivesByCriteria = Maps.newHashMap();
   private final Map<String, Map<ScoreboardObjective, ScoreboardScore>> playerScores = Maps.newHashMap();
   private final ScoreboardObjective[] displaySlots = new ScoreboardObjective[19];
   private final Map<String, ScoreboardTeam> teamsByName = Maps.newHashMap();
   private final Map<String, ScoreboardTeam> teamsByPlayer = Maps.newHashMap();
   private static String[] g = null;

   public Scoreboard() {
   }

   public ScoreboardObjective getObjective(String var1) {
      return (ScoreboardObjective)this.objectivesByName.get(var1);
   }

   public ScoreboardObjective registerObjective(String var1, IScoreboardCriteria var2) {
      if(var1.length() > 16) {
         throw new IllegalArgumentException("The objective name \'" + var1 + "\' is too long!");
      } else {
         ScoreboardObjective var3 = this.getObjective(var1);
         if(var3 != null) {
            throw new IllegalArgumentException("An objective with the name \'" + var1 + "\' already exists!");
         } else {
            var3 = new ScoreboardObjective(this, var1, var2);
            Object var4 = (List)this.objectivesByCriteria.get(var2);
            if(var4 == null) {
               var4 = Lists.newArrayList();
               this.objectivesByCriteria.put(var2, var4);
            }

            ((List)var4).add(var3);
            this.objectivesByName.put(var1, var3);
            this.handleObjectiveAdded(var3);
            return var3;
         }
      }
   }

   public Collection<ScoreboardObjective> getObjectivesForCriteria(IScoreboardCriteria var1) {
      Collection var2 = (Collection)this.objectivesByCriteria.get(var1);
      return var2 == null?Lists.newArrayList():Lists.newArrayList((Iterable)var2);
   }

   public boolean b(String var1, ScoreboardObjective var2) {
      Map var3 = (Map)this.playerScores.get(var1);
      if(var3 == null) {
         return false;
      } else {
         ScoreboardScore var4 = (ScoreboardScore)var3.get(var2);
         return var4 != null;
      }
   }

   public ScoreboardScore getPlayerScoreForObjective(String var1, ScoreboardObjective var2) {
      if(var1.length() > 40) {
         throw new IllegalArgumentException("The player name \'" + var1 + "\' is too long!");
      } else {
         Object var3 = (Map)this.playerScores.get(var1);
         if(var3 == null) {
            var3 = Maps.newHashMap();
            this.playerScores.put(var1, var3);
         }

         ScoreboardScore var4 = (ScoreboardScore)((Map)var3).get(var2);
         if(var4 == null) {
            var4 = new ScoreboardScore(this, var2, var1);
            ((Map)var3).put(var2, var4);
         }

         return var4;
      }
   }

   public Collection<ScoreboardScore> getScoresForObjective(ScoreboardObjective var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = this.playerScores.values().iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         ScoreboardScore var5 = (ScoreboardScore)var4.get(var1);
         if(var5 != null) {
            var2.add(var5);
         }
      }

      Collections.sort(var2, ScoreboardScore.a);
      return var2;
   }

   public Collection<ScoreboardObjective> getObjectives() {
      return this.objectivesByName.values();
   }

   public Collection<String> getPlayers() {
      return this.playerScores.keySet();
   }

   public void resetPlayerScores(String var1, ScoreboardObjective var2) {
      Map var3;
      if(var2 == null) {
         var3 = (Map)this.playerScores.remove(var1);
         if(var3 != null) {
            this.handlePlayerRemoved(var1);
         }
      } else {
         var3 = (Map)this.playerScores.get(var1);
         if(var3 != null) {
            ScoreboardScore var4 = (ScoreboardScore)var3.remove(var2);
            if(var3.size() < 1) {
               Map var5 = (Map)this.playerScores.remove(var1);
               if(var5 != null) {
                  this.handlePlayerRemoved(var1);
               }
            } else if(var4 != null) {
               this.a(var1, var2);
            }
         }
      }

   }

   public Collection<ScoreboardScore> getScores() {
      Collection var1 = this.playerScores.values();
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         var2.addAll(var4.values());
      }

      return var2;
   }

   public Map<ScoreboardObjective, ScoreboardScore> getPlayerObjectives(String var1) {
      Object var2 = (Map)this.playerScores.get(var1);
      if(var2 == null) {
         var2 = Maps.newHashMap();
      }

      return (Map)var2;
   }

   public void unregisterObjective(ScoreboardObjective var1) {
      this.objectivesByName.remove(var1.getName());

      for(int var2 = 0; var2 < 19; ++var2) {
         if(this.getObjectiveForSlot(var2) == var1) {
            this.setDisplaySlot(var2, (ScoreboardObjective)null);
         }
      }

      List var5 = (List)this.objectivesByCriteria.get(var1.getCriteria());
      if(var5 != null) {
         var5.remove(var1);
      }

      Iterator var3 = this.playerScores.values().iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         var4.remove(var1);
      }

      this.handleObjectiveRemoved(var1);
   }

   public void setDisplaySlot(int var1, ScoreboardObjective var2) {
      this.displaySlots[var1] = var2;
   }

   public ScoreboardObjective getObjectiveForSlot(int var1) {
      return this.displaySlots[var1];
   }

   public ScoreboardTeam getTeam(String var1) {
      return (ScoreboardTeam)this.teamsByName.get(var1);
   }

   public ScoreboardTeam createTeam(String var1) {
      if(var1.length() > 16) {
         throw new IllegalArgumentException("The team name \'" + var1 + "\' is too long!");
      } else {
         ScoreboardTeam var2 = this.getTeam(var1);
         if(var2 != null) {
            throw new IllegalArgumentException("A team with the name \'" + var1 + "\' already exists!");
         } else {
            var2 = new ScoreboardTeam(this, var1);
            this.teamsByName.put(var1, var2);
            this.handleTeamAdded(var2);
            return var2;
         }
      }
   }

   public void removeTeam(ScoreboardTeam var1) {
      this.teamsByName.remove(var1.getName());
      Iterator var2 = var1.getPlayerNameSet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.teamsByPlayer.remove(var3);
      }

      this.handleTeamRemoved(var1);
   }

   public boolean addPlayerToTeam(String var1, String var2) {
      if(var1.length() > 40) {
         throw new IllegalArgumentException("The player name \'" + var1 + "\' is too long!");
      } else if(!this.teamsByName.containsKey(var2)) {
         return false;
      } else {
         ScoreboardTeam var3 = this.getTeam(var2);
         if(this.getPlayerTeam(var1) != null) {
            this.removePlayerFromTeam(var1);
         }

         this.teamsByPlayer.put(var1, var3);
         var3.getPlayerNameSet().add(var1);
         return true;
      }
   }

   public boolean removePlayerFromTeam(String var1) {
      ScoreboardTeam var2 = this.getPlayerTeam(var1);
      if(var2 != null) {
         this.removePlayerFromTeam(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public void removePlayerFromTeam(String var1, ScoreboardTeam var2) {
      if(this.getPlayerTeam(var1) != var2) {
         throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + var2.getName() + "\'.");
      } else {
         this.teamsByPlayer.remove(var1);
         var2.getPlayerNameSet().remove(var1);
      }
   }

   public Collection<String> getTeamNames() {
      return this.teamsByName.keySet();
   }

   public Collection<ScoreboardTeam> getTeams() {
      return this.teamsByName.values();
   }

   public ScoreboardTeam getPlayerTeam(String var1) {
      return (ScoreboardTeam)this.teamsByPlayer.get(var1);
   }

   public void handleObjectiveAdded(ScoreboardObjective var1) {
   }

   public void handleObjectiveChanged(ScoreboardObjective var1) {
   }

   public void handleObjectiveRemoved(ScoreboardObjective var1) {
   }

   public void handleScoreChanged(ScoreboardScore var1) {
   }

   public void handlePlayerRemoved(String var1) {
   }

   public void a(String var1, ScoreboardObjective var2) {
   }

   public void handleTeamAdded(ScoreboardTeam var1) {
   }

   public void handleTeamChanged(ScoreboardTeam var1) {
   }

   public void handleTeamRemoved(ScoreboardTeam var1) {
   }

   public static String getSlotName(int var0) {
      switch(var0) {
      case 0:
         return "list";
      case 1:
         return "sidebar";
      case 2:
         return "belowName";
      default:
         if(var0 >= 3 && var0 <= 18) {
            EnumChatFormat var1 = EnumChatFormat.a(var0 - 3);
            if(var1 != null && var1 != EnumChatFormat.RESET) {
               return "sidebar.team." + var1.e();
            }
         }

         return null;
      }
   }

   public static int getSlotForName(String var0) {
      if(var0.equalsIgnoreCase("list")) {
         return 0;
      } else if(var0.equalsIgnoreCase("sidebar")) {
         return 1;
      } else if(var0.equalsIgnoreCase("belowName")) {
         return 2;
      } else {
         if(var0.startsWith("sidebar.team.")) {
            String var1 = var0.substring("sidebar.team.".length());
            EnumChatFormat var2 = EnumChatFormat.b(var1);
            if(var2 != null && var2.b() >= 0) {
               return var2.b() + 3;
            }
         }

         return -1;
      }
   }

   public static String[] h() {
      if(g == null) {
         g = new String[19];

         for(int var0 = 0; var0 < 19; ++var0) {
            g[var0] = getSlotName(var0);
         }
      }

      return g;
   }

   public void a(Entity var1) {
      if(var1 != null && !(var1 instanceof EntityHuman) && !var1.isAlive()) {
         String var2 = var1.getUniqueID().toString();
         this.resetPlayerScores(var2, (ScoreboardObjective)null);
         this.removePlayerFromTeam(var2);
      }
   }
}
