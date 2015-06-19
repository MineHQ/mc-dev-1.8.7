package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldData;

public class CommandToggleDownfall extends CommandAbstract {
   public CommandToggleDownfall() {
   }

   public String getCommand() {
      return "toggledownfall";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.downfall.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      this.d();
      a(var1, this, "commands.downfall.success", new Object[0]);
   }

   protected void d() {
      WorldData var1 = MinecraftServer.getServer().worldServer[0].getWorldData();
      var1.setStorm(!var1.hasStorm());
   }
}
