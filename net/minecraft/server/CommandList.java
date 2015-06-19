package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandList extends CommandAbstract {
   public CommandList() {
   }

   public String getCommand() {
      return "list";
   }

   public int a() {
      return 0;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.players.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      int var3 = MinecraftServer.getServer().I();
      var1.sendMessage(new ChatMessage("commands.players.list", new Object[]{Integer.valueOf(var3), Integer.valueOf(MinecraftServer.getServer().J())}));
      var1.sendMessage(new ChatComponentText(MinecraftServer.getServer().getPlayerList().b(var2.length > 0 && "uuids".equalsIgnoreCase(var2[0]))));
      var1.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, var3);
   }
}
