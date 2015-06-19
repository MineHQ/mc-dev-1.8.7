package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.GameProfileBanEntry;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandBan extends CommandAbstract {
   public CommandBan() {
   }

   public String getCommand() {
      return "ban";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.ban.usage";
   }

   public boolean canUse(ICommandListener var1) {
      return MinecraftServer.getServer().getPlayerList().getProfileBans().isEnabled() && super.canUse(var1);
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length >= 1 && var2[0].length() > 0) {
         MinecraftServer var3 = MinecraftServer.getServer();
         GameProfile var4 = var3.getUserCache().getProfile(var2[0]);
         if(var4 == null) {
            throw new CommandException("commands.ban.failed", new Object[]{var2[0]});
         } else {
            String var5 = null;
            if(var2.length >= 2) {
               var5 = a(var1, var2, 1).c();
            }

            GameProfileBanEntry var6 = new GameProfileBanEntry(var4, (Date)null, var1.getName(), (Date)null, var5);
            var3.getPlayerList().getProfileBans().add(var6);
            EntityPlayer var7 = var3.getPlayerList().getPlayer(var2[0]);
            if(var7 != null) {
               var7.playerConnection.disconnect("You are banned from this server.");
            }

            a(var1, this, "commands.ban.success", new Object[]{var2[0]});
         }
      } else {
         throw new ExceptionUsage("commands.ban.usage", new Object[0]);
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length >= 1?a(var2, MinecraftServer.getServer().getPlayers()):null;
   }
}
