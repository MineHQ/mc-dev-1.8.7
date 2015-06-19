package net.minecraft.server;

import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandBanIp;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionInvalidSyntax;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandPardonIP extends CommandAbstract {
   public CommandPardonIP() {
   }

   public String getCommand() {
      return "pardon-ip";
   }

   public int a() {
      return 3;
   }

   public boolean canUse(ICommandListener var1) {
      return MinecraftServer.getServer().getPlayerList().getIPBans().isEnabled() && super.canUse(var1);
   }

   public String getUsage(ICommandListener var1) {
      return "commands.unbanip.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length == 1 && var2[0].length() > 1) {
         Matcher var3 = CommandBanIp.a.matcher(var2[0]);
         if(var3.matches()) {
            MinecraftServer.getServer().getPlayerList().getIPBans().remove(var2[0]);
            a(var1, this, "commands.unbanip.success", new Object[]{var2[0]});
         } else {
            throw new ExceptionInvalidSyntax("commands.unbanip.invalid", new Object[0]);
         }
      } else {
         throw new ExceptionUsage("commands.unbanip.usage", new Object[0]);
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayerList().getIPBans().getEntries()):null;
   }
}
