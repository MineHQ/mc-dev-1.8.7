package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class CommandSaveOff extends CommandAbstract {
   public CommandSaveOff() {
   }

   public String getCommand() {
      return "save-off";
   }

   public String getUsage(ICommandListener var1) {
      return "commands.save-off.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      MinecraftServer var3 = MinecraftServer.getServer();
      boolean var4 = false;

      for(int var5 = 0; var5 < var3.worldServer.length; ++var5) {
         if(var3.worldServer[var5] != null) {
            WorldServer var6 = var3.worldServer[var5];
            if(!var6.savingDisabled) {
               var6.savingDisabled = true;
               var4 = true;
            }
         }
      }

      if(var4) {
         a(var1, this, "commands.save.disabled", new Object[0]);
      } else {
         throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
      }
   }
}
