package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandWhitelist extends CommandAbstract {
   public CommandWhitelist() {
   }

   public String getCommand() {
      return "whitelist";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.whitelist.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 1) {
         throw new ExceptionUsage("commands.whitelist.usage", new Object[0]);
      } else {
         MinecraftServer var3 = MinecraftServer.getServer();
         if(var2[0].equals("on")) {
            var3.getPlayerList().setHasWhitelist(true);
            a(var1, this, "commands.whitelist.enabled", new Object[0]);
         } else if(var2[0].equals("off")) {
            var3.getPlayerList().setHasWhitelist(false);
            a(var1, this, "commands.whitelist.disabled", new Object[0]);
         } else if(var2[0].equals("list")) {
            var1.sendMessage(new ChatMessage("commands.whitelist.list", new Object[]{Integer.valueOf(var3.getPlayerList().getWhitelisted().length), Integer.valueOf(var3.getPlayerList().getSeenPlayers().length)}));
            String[] var4 = var3.getPlayerList().getWhitelisted();
            var1.sendMessage(new ChatComponentText(a(var4)));
         } else {
            GameProfile var5;
            if(var2[0].equals("add")) {
               if(var2.length < 2) {
                  throw new ExceptionUsage("commands.whitelist.add.usage", new Object[0]);
               }

               var5 = var3.getUserCache().getProfile(var2[1]);
               if(var5 == null) {
                  throw new CommandException("commands.whitelist.add.failed", new Object[]{var2[1]});
               }

               var3.getPlayerList().addWhitelist(var5);
               a(var1, this, "commands.whitelist.add.success", new Object[]{var2[1]});
            } else if(var2[0].equals("remove")) {
               if(var2.length < 2) {
                  throw new ExceptionUsage("commands.whitelist.remove.usage", new Object[0]);
               }

               var5 = var3.getPlayerList().getWhitelist().a(var2[1]);
               if(var5 == null) {
                  throw new CommandException("commands.whitelist.remove.failed", new Object[]{var2[1]});
               }

               var3.getPlayerList().removeWhitelist(var5);
               a(var1, this, "commands.whitelist.remove.success", new Object[]{var2[1]});
            } else if(var2[0].equals("reload")) {
               var3.getPlayerList().reloadWhitelist();
               a(var1, this, "commands.whitelist.reloaded", new Object[0]);
            }
         }

      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      if(var2.length == 1) {
         return a(var2, new String[]{"on", "off", "list", "add", "remove", "reload"});
      } else {
         if(var2.length == 2) {
            if(var2[0].equals("remove")) {
               return a(var2, MinecraftServer.getServer().getPlayerList().getWhitelisted());
            }

            if(var2[0].equals("add")) {
               return a(var2, MinecraftServer.getServer().getUserCache().a());
            }
         }

         return null;
      }
   }
}
