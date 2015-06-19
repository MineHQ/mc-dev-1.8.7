package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandKill extends CommandAbstract {
   public CommandKill() {
   }

   public String getCommand() {
      return "kill";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.kill.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length == 0) {
         EntityPlayer var4 = b(var1);
         var4.G();
         a(var1, this, "commands.kill.successful", new Object[]{var4.getScoreboardDisplayName()});
      } else {
         Entity var3 = b(var1, var2[0]);
         var3.G();
         a(var1, this, "commands.kill.successful", new Object[]{var3.getScoreboardDisplayName()});
      }
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayers()):null;
   }
}
