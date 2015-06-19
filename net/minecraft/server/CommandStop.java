package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandAbstract {
   public CommandStop() {
   }

   public String getCommand() {
      return "stop";
   }

   public String getUsage(ICommandListener var1) {
      return "commands.stop.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(MinecraftServer.getServer().worldServer != null) {
         a(var1, this, "commands.stop.start", new Object[0]);
      }

      MinecraftServer.getServer().safeShutdown();
   }
}
