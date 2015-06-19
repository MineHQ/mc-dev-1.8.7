package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandPardon extends CommandAbstract {
   public CommandPardon() {
   }

   public String getCommand() {
      return "pardon";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.unban.usage";
   }

   public boolean canUse(ICommandListener var1) {
      return MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled() && super.canUse(var1);
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer var3 = MinecraftServer.getServer();
         GameProfile var4 = var3.getPlayerList().getProfileBans().a(var2[0]);
         if(var4 == null) {
            throw new CommandException("commands.unban.failed", new Object[]{var2[0]});
         } else {
            var3.getPlayerList().getProfileBans().remove(var4);
            a(var1, this, "commands.unban.success", new Object[]{var2[0]});
         }
      } else {
         throw new ExceptionUsage("commands.unban.usage", new Object[0]);
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayerList().getProfileBans().getEntries()):null;
   }
}
