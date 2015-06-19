package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandIdleTimeout extends CommandAbstract {
   public CommandIdleTimeout() {
   }

   public String getCommand() {
      return "setidletimeout";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.setidletimeout.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length != 1) {
         throw new ExceptionUsage("commands.setidletimeout.usage", new Object[0]);
      } else {
         int var3 = a(var2[0], 0);
         MinecraftServer.getServer().setIdleTimeout(var3);
         a(var1, this, "commands.setidletimeout.success", new Object[]{Integer.valueOf(var3)});
      }
   }
}
