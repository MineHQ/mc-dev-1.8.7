package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class CommandTime extends CommandAbstract {
   public CommandTime() {
   }

   public String getCommand() {
      return "time";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.time.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length > 1) {
         int var3;
         if(var2[0].equals("set")) {
            if(var2[1].equals("day")) {
               var3 = 1000;
            } else if(var2[1].equals("night")) {
               var3 = 13000;
            } else {
               var3 = a(var2[1], 0);
            }

            this.a(var1, var3);
            a(var1, this, "commands.time.set", new Object[]{Integer.valueOf(var3)});
            return;
         }

         if(var2[0].equals("add")) {
            var3 = a(var2[1], 0);
            this.b(var1, var3);
            a(var1, this, "commands.time.added", new Object[]{Integer.valueOf(var3)});
            return;
         }

         if(var2[0].equals("query")) {
            if(var2[1].equals("daytime")) {
               var3 = (int)(var1.getWorld().getDayTime() % 2147483647L);
               var1.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, var3);
               a(var1, this, "commands.time.query", new Object[]{Integer.valueOf(var3)});
               return;
            }

            if(var2[1].equals("gametime")) {
               var3 = (int)(var1.getWorld().getTime() % 2147483647L);
               var1.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, var3);
               a(var1, this, "commands.time.query", new Object[]{Integer.valueOf(var3)});
               return;
            }
         }
      }

      throw new ExceptionUsage("commands.time.usage", new Object[0]);
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, new String[]{"set", "add", "query"}):(var2.length == 2 && var2[0].equals("set")?a(var2, new String[]{"day", "night"}):(var2.length == 2 && var2[0].equals("query")?a(var2, new String[]{"daytime", "gametime"}):null));
   }

   protected void a(ICommandListener var1, int var2) {
      for(int var3 = 0; var3 < MinecraftServer.getServer().worldServer.length; ++var3) {
         MinecraftServer.getServer().worldServer[var3].setDayTime((long)var2);
      }

   }

   protected void b(ICommandListener var1, int var2) {
      for(int var3 = 0; var3 < MinecraftServer.getServer().worldServer.length; ++var3) {
         WorldServer var4 = MinecraftServer.getServer().worldServer[var3];
         var4.setDayTime(var4.getDayTime() + (long)var2);
      }

   }
}
