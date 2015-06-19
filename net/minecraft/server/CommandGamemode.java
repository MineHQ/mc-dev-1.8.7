package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionInvalidNumber;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldSettings;

public class CommandGamemode extends CommandAbstract {
   public CommandGamemode() {
   }

   public String getCommand() {
      return "gamemode";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.gamemode.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length <= 0) {
         throw new ExceptionUsage("commands.gamemode.usage", new Object[0]);
      } else {
         WorldSettings.EnumGamemode var3 = this.h(var1, var2[0]);
         EntityPlayer var4 = var2.length >= 2?a(var1, var2[1]):b(var1);
         var4.a(var3);
         var4.fallDistance = 0.0F;
         if(var1.getWorld().getGameRules().getBoolean("sendCommandFeedback")) {
            var4.sendMessage(new ChatMessage("gameMode.changed", new Object[0]));
         }

         ChatMessage var5 = new ChatMessage("gameMode." + var3.b(), new Object[0]);
         if(var4 != var1) {
            a(var1, this, 1, "commands.gamemode.success.other", new Object[]{var4.getName(), var5});
         } else {
            a(var1, this, 1, "commands.gamemode.success.self", new Object[]{var5});
         }

      }
   }

   protected WorldSettings.EnumGamemode h(ICommandListener var1, String var2) throws ExceptionInvalidNumber {
      return !var2.equalsIgnoreCase(WorldSettings.EnumGamemode.SURVIVAL.b()) && !var2.equalsIgnoreCase("s")?(!var2.equalsIgnoreCase(WorldSettings.EnumGamemode.CREATIVE.b()) && !var2.equalsIgnoreCase("c")?(!var2.equalsIgnoreCase(WorldSettings.EnumGamemode.ADVENTURE.b()) && !var2.equalsIgnoreCase("a")?(!var2.equalsIgnoreCase(WorldSettings.EnumGamemode.SPECTATOR.b()) && !var2.equalsIgnoreCase("sp")?WorldSettings.a(a(var2, 0, WorldSettings.EnumGamemode.values().length - 2)):WorldSettings.EnumGamemode.SPECTATOR):WorldSettings.EnumGamemode.ADVENTURE):WorldSettings.EnumGamemode.CREATIVE):WorldSettings.EnumGamemode.SURVIVAL;
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, new String[]{"survival", "creative", "adventure", "spectator"}):(var2.length == 2?a(var2, this.d()):null);
   }

   protected String[] d() {
      return MinecraftServer.getServer().getPlayers();
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 1;
   }
}
