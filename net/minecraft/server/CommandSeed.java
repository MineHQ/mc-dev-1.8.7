package net.minecraft.server;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public class CommandSeed extends CommandAbstract {
   public CommandSeed() {
   }

   public boolean canUse(ICommandListener var1) {
      return MinecraftServer.getServer().T() || super.canUse(var1);
   }

   public String getCommand() {
      return "seed";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.seed.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      Object var3 = var1 instanceof EntityHuman?((EntityHuman)var1).world:MinecraftServer.getServer().getWorldServer(0);
      var1.sendMessage(new ChatMessage("commands.seed.success", new Object[]{Long.valueOf(((World)var3).getSeed())}));
   }
}
