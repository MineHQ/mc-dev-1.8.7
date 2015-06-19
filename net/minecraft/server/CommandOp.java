package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandOp extends CommandAbstract {
   public CommandOp() {
   }

   public String getCommand() {
      return "op";
   }

   public int a() {
      return 3;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.op.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer var3 = MinecraftServer.getServer();
         GameProfile var4 = var3.getUserCache().getProfile(var2[0]);
         if(var4 == null) {
            throw new CommandException("commands.op.failed", new Object[]{var2[0]});
         } else {
            var3.getPlayerList().addOp(var4);
            a(var1, this, "commands.op.success", new Object[]{var2[0]});
         }
      } else {
         throw new ExceptionUsage("commands.op.usage", new Object[0]);
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      if(var2.length == 1) {
         String var4 = var2[var2.length - 1];
         ArrayList var5 = Lists.newArrayList();
         GameProfile[] var6 = MinecraftServer.getServer().L();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            GameProfile var9 = var6[var8];
            if(!MinecraftServer.getServer().getPlayerList().isOp(var9) && a(var4, var9.getName())) {
               var5.add(var9.getName());
            }
         }

         return var5;
      } else {
         return null;
      }
   }
}
