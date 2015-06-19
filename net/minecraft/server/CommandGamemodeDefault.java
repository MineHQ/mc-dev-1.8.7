package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandGamemode;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldSettings;

public class CommandGamemodeDefault extends CommandGamemode {
   public CommandGamemodeDefault() {
   }

   public String getCommand() {
      return "defaultgamemode";
   }

   public String getUsage(ICommandListener var1) {
      return "commands.defaultgamemode.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length <= 0) {
         throw new ExceptionUsage("commands.defaultgamemode.usage", new Object[0]);
      } else {
         WorldSettings.EnumGamemode var3 = this.h(var1, var2[0]);
         this.a(var3);
         a(var1, this, "commands.defaultgamemode.success", new Object[]{new ChatMessage("gameMode." + var3.b(), new Object[0])});
      }
   }

   protected void a(WorldSettings.EnumGamemode var1) {
      MinecraftServer var2 = MinecraftServer.getServer();
      var2.setGamemode(var1);
      EntityPlayer var4;
      if(var2.getForceGamemode()) {
         for(Iterator var3 = MinecraftServer.getServer().getPlayerList().v().iterator(); var3.hasNext(); var4.fallDistance = 0.0F) {
            var4 = (EntityPlayer)var3.next();
            var4.a(var1);
         }
      }

   }
}
