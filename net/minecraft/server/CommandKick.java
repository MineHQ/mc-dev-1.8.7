package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionPlayerNotFound;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandKick extends CommandAbstract {
   public CommandKick() {
   }

   public String getCommand() {
      return "kick";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.kick.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length > 0 && var2[0].length() > 1) {
         EntityPlayer var3 = MinecraftServer.getServer().getPlayerList().getPlayer(var2[0]);
         String var4 = "Kicked by an operator.";
         boolean var5 = false;
         if(var3 == null) {
            throw new ExceptionPlayerNotFound();
         } else {
            if(var2.length >= 2) {
               var4 = a(var1, var2, 1).c();
               var5 = true;
            }

            var3.playerConnection.disconnect(var4);
            if(var5) {
               a(var1, this, "commands.kick.success.reason", new Object[]{var3.getName(), var4});
            } else {
               a(var1, this, "commands.kick.success", new Object[]{var3.getName()});
            }

         }
      } else {
         throw new ExceptionUsage("commands.kick.usage", new Object[0]);
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length >= 1?a(var2, MinecraftServer.getServer().getPlayers()):null;
   }
}
