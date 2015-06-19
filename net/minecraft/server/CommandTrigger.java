package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;

public class CommandTrigger extends CommandAbstract {
   public CommandTrigger() {
   }

   public String getCommand() {
      return "trigger";
   }

   public int a() {
      return 0;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.trigger.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 3) {
         throw new ExceptionUsage("commands.trigger.usage", new Object[0]);
      } else {
         EntityPlayer var3;
         if(var1 instanceof EntityPlayer) {
            var3 = (EntityPlayer)var1;
         } else {
            Entity var4 = var1.f();
            if(!(var4 instanceof EntityPlayer)) {
               throw new CommandException("commands.trigger.invalidPlayer", new Object[0]);
            }

            var3 = (EntityPlayer)var4;
         }

         Scoreboard var8 = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
         ScoreboardObjective var5 = var8.getObjective(var2[0]);
         if(var5 != null && var5.getCriteria() == IScoreboardCriteria.c) {
            int var6 = a(var2[2]);
            if(!var8.b(var3.getName(), var5)) {
               throw new CommandException("commands.trigger.invalidObjective", new Object[]{var2[0]});
            } else {
               ScoreboardScore var7 = var8.getPlayerScoreForObjective(var3.getName(), var5);
               if(var7.g()) {
                  throw new CommandException("commands.trigger.disabled", new Object[]{var2[0]});
               } else {
                  if("set".equals(var2[1])) {
                     var7.setScore(var6);
                  } else {
                     if(!"add".equals(var2[1])) {
                        throw new CommandException("commands.trigger.invalidMode", new Object[]{var2[1]});
                     }

                     var7.addScore(var6);
                  }

                  var7.a(true);
                  if(var3.playerInteractManager.isCreative()) {
                     a(var1, this, "commands.trigger.success", new Object[]{var2[0], var2[1], var2[2]});
                  }

               }
            }
         } else {
            throw new CommandException("commands.trigger.invalidObjective", new Object[]{var2[0]});
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      if(var2.length == 1) {
         Scoreboard var4 = MinecraftServer.getServer().getWorldServer(0).getScoreboard();
         ArrayList var5 = Lists.newArrayList();
         Iterator var6 = var4.getObjectives().iterator();

         while(var6.hasNext()) {
            ScoreboardObjective var7 = (ScoreboardObjective)var6.next();
            if(var7.getCriteria() == IScoreboardCriteria.c) {
               var5.add(var7.getName());
            }
         }

         return a(var2, (String[])var5.toArray(new String[var5.size()]));
      } else {
         return var2.length == 2?a(var2, new String[]{"add", "set"}):null;
      }
   }
}
