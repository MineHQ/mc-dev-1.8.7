package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.PacketPlayOutScoreboardObjective;
import net.minecraft.server.PacketPlayOutScoreboardScore;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import net.minecraft.server.PersistentScoreboard;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.ScoreboardTeam;

public class ScoreboardServer extends Scoreboard {
   private final MinecraftServer a;
   private final Set<ScoreboardObjective> b = Sets.newHashSet();
   private PersistentScoreboard c;

   public ScoreboardServer(MinecraftServer var1) {
      this.a = var1;
   }

   public void handleScoreChanged(ScoreboardScore var1) {
      super.handleScoreChanged(var1);
      if(this.b.contains(var1.getObjective())) {
         this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardScore(var1));
      }

      this.b();
   }

   public void handlePlayerRemoved(String var1) {
      super.handlePlayerRemoved(var1);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardScore(var1));
      this.b();
   }

   public void a(String var1, ScoreboardObjective var2) {
      super.a(var1, var2);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardScore(var1, var2));
      this.b();
   }

   public void setDisplaySlot(int var1, ScoreboardObjective var2) {
      ScoreboardObjective var3 = this.getObjectiveForSlot(var1);
      super.setDisplaySlot(var1, var2);
      if(var3 != var2 && var3 != null) {
         if(this.h(var3) > 0) {
            this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardDisplayObjective(var1, var2));
         } else {
            this.g(var3);
         }
      }

      if(var2 != null) {
         if(this.b.contains(var2)) {
            this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardDisplayObjective(var1, var2));
         } else {
            this.e(var2);
         }
      }

      this.b();
   }

   public boolean addPlayerToTeam(String var1, String var2) {
      if(super.addPlayerToTeam(var1, var2)) {
         ScoreboardTeam var3 = this.getTeam(var2);
         this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(var3, Arrays.asList(new String[]{var1}), 3));
         this.b();
         return true;
      } else {
         return false;
      }
   }

   public void removePlayerFromTeam(String var1, ScoreboardTeam var2) {
      super.removePlayerFromTeam(var1, var2);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(var2, Arrays.asList(new String[]{var1}), 4));
      this.b();
   }

   public void handleObjectiveAdded(ScoreboardObjective var1) {
      super.handleObjectiveAdded(var1);
      this.b();
   }

   public void handleObjectiveChanged(ScoreboardObjective var1) {
      super.handleObjectiveChanged(var1);
      if(this.b.contains(var1)) {
         this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardObjective(var1, 2));
      }

      this.b();
   }

   public void handleObjectiveRemoved(ScoreboardObjective var1) {
      super.handleObjectiveRemoved(var1);
      if(this.b.contains(var1)) {
         this.g(var1);
      }

      this.b();
   }

   public void handleTeamAdded(ScoreboardTeam var1) {
      super.handleTeamAdded(var1);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(var1, 0));
      this.b();
   }

   public void handleTeamChanged(ScoreboardTeam var1) {
      super.handleTeamChanged(var1);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(var1, 2));
      this.b();
   }

   public void handleTeamRemoved(ScoreboardTeam var1) {
      super.handleTeamRemoved(var1);
      this.a.getPlayerList().sendAll(new PacketPlayOutScoreboardTeam(var1, 1));
      this.b();
   }

   public void a(PersistentScoreboard var1) {
      this.c = var1;
   }

   protected void b() {
      if(this.c != null) {
         this.c.c();
      }

   }

   public List<Packet> getScoreboardScorePacketsForObjective(ScoreboardObjective var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new PacketPlayOutScoreboardObjective(var1, 0));

      for(int var3 = 0; var3 < 19; ++var3) {
         if(this.getObjectiveForSlot(var3) == var1) {
            var2.add(new PacketPlayOutScoreboardDisplayObjective(var3, var1));
         }
      }

      Iterator var5 = this.getScoresForObjective(var1).iterator();

      while(var5.hasNext()) {
         ScoreboardScore var4 = (ScoreboardScore)var5.next();
         var2.add(new PacketPlayOutScoreboardScore(var4));
      }

      return var2;
   }

   public void e(ScoreboardObjective var1) {
      List var2 = this.getScoreboardScorePacketsForObjective(var1);
      Iterator var3 = this.a.getPlayerList().v().iterator();

      while(var3.hasNext()) {
         EntityPlayer var4 = (EntityPlayer)var3.next();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            Packet var6 = (Packet)var5.next();
            var4.playerConnection.sendPacket(var6);
         }
      }

      this.b.add(var1);
   }

   public List<Packet> f(ScoreboardObjective var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new PacketPlayOutScoreboardObjective(var1, 1));

      for(int var3 = 0; var3 < 19; ++var3) {
         if(this.getObjectiveForSlot(var3) == var1) {
            var2.add(new PacketPlayOutScoreboardDisplayObjective(var3, var1));
         }
      }

      return var2;
   }

   public void g(ScoreboardObjective var1) {
      List var2 = this.f(var1);
      Iterator var3 = this.a.getPlayerList().v().iterator();

      while(var3.hasNext()) {
         EntityPlayer var4 = (EntityPlayer)var3.next();
         Iterator var5 = var2.iterator();

         while(var5.hasNext()) {
            Packet var6 = (Packet)var5.next();
            var4.playerConnection.sendPacket(var6);
         }
      }

      this.b.remove(var1);
   }

   public int h(ScoreboardObjective var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < 19; ++var3) {
         if(this.getObjectiveForSlot(var3) == var1) {
            ++var2;
         }
      }

      return var2;
   }
}
