package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;

public class CommandSpawnpoint extends CommandAbstract {
   public CommandSpawnpoint() {
   }

   public String getCommand() {
      return "spawnpoint";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.spawnpoint.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length > 1 && var2.length < 4) {
         throw new ExceptionUsage("commands.spawnpoint.usage", new Object[0]);
      } else {
         EntityPlayer var3 = var2.length > 0?a(var1, var2[0]):b(var1);
         BlockPosition var4 = var2.length > 3?a(var1, var2, 1, true):var3.getChunkCoordinates();
         if(var3.world != null) {
            var3.setRespawnPosition(var4, true);
            a(var1, this, "commands.spawnpoint.success", new Object[]{var3.getName(), Integer.valueOf(var4.getX()), Integer.valueOf(var4.getY()), Integer.valueOf(var4.getZ())});
         }

      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayers()):(var2.length > 1 && var2.length <= 4?a(var2, 1, var3):null);
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
