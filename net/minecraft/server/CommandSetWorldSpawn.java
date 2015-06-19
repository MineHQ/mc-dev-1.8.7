package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PacketPlayOutSpawnPosition;

public class CommandSetWorldSpawn extends CommandAbstract {
   public CommandSetWorldSpawn() {
   }

   public String getCommand() {
      return "setworldspawn";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.setworldspawn.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      BlockPosition var3;
      if(var2.length == 0) {
         var3 = b(var1).getChunkCoordinates();
      } else {
         if(var2.length != 3 || var1.getWorld() == null) {
            throw new ExceptionUsage("commands.setworldspawn.usage", new Object[0]);
         }

         var3 = a(var1, var2, 0, true);
      }

      var1.getWorld().B(var3);
      MinecraftServer.getServer().getPlayerList().sendAll(new PacketPlayOutSpawnPosition(var3));
      a(var1, this, "commands.setworldspawn.success", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ())});
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length > 0 && var2.length <= 3?a(var2, 0, var3):null;
   }
}
