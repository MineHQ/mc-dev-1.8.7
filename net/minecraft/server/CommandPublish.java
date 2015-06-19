package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldSettings;

public class CommandPublish extends CommandAbstract {
   public CommandPublish() {
   }

   public String getCommand() {
      return "publish";
   }

   public String getUsage(ICommandListener var1) {
      return "commands.publish.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      String var3 = MinecraftServer.getServer().a(WorldSettings.EnumGamemode.SURVIVAL, false);
      if(var3 != null) {
         a(var1, this, "commands.publish.started", new Object[]{var3});
      } else {
         a(var1, this, "commands.publish.failed", new Object[0]);
      }

   }
}
