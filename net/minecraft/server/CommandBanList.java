package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandBanList extends CommandAbstract {
   public CommandBanList() {
   }

   public String getCommand() {
      return "banlist";
   }

   public int a() {
      return 3;
   }

   public boolean canUse(ICommandListener var1) {
      return (MinecraftServer.getServer().getPlayerList().getIPBans().isEnabled() || MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled()) && super.canUse(var1);
   }

   public String getUsage(ICommandListener var1) {
      return "commands.banlist.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length >= 1 && var2[0].equalsIgnoreCase("ips")) {
         var1.sendMessage(new ChatMessage("commands.banlist.ips", new Object[]{Integer.valueOf(MinecraftServer.getServer().getPlayerList().getIPBans().getEntries().length)}));
         var1.sendMessage(new ChatComponentText(a(MinecraftServer.getServer().getPlayerList().getIPBans().getEntries())));
      } else {
         var1.sendMessage(new ChatMessage("commands.banlist.players", new Object[]{Integer.valueOf(MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries().length)}));
         var1.sendMessage(new ChatComponentText(a(MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries())));
      }

   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, new String[]{"players", "ips"}):null;
   }
}
