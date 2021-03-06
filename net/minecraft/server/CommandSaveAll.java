package net.minecraft.server;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class CommandSaveAll extends CommandAbstract {
   public CommandSaveAll() {
   }

   public String getCommand() {
      return "save-all";
   }

   public String getUsage(ICommandListener var1) {
      return "commands.save.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      MinecraftServer var3 = MinecraftServer.getServer();
      var1.sendMessage(new ChatMessage("commands.save.start", new Object[0]));
      if(var3.getPlayerList() != null) {
         var3.getPlayerList().savePlayers();
      }

      try {
         int var4;
         WorldServer var5;
         boolean var6;
         for(var4 = 0; var4 < var3.worldServer.length; ++var4) {
            if(var3.worldServer[var4] != null) {
               var5 = var3.worldServer[var4];
               var6 = var5.savingDisabled;
               var5.savingDisabled = false;
               var5.save(true, (IProgressUpdate)null);
               var5.savingDisabled = var6;
            }
         }

         if(var2.length > 0 && "flush".equals(var2[0])) {
            var1.sendMessage(new ChatMessage("commands.save.flushStart", new Object[0]));

            for(var4 = 0; var4 < var3.worldServer.length; ++var4) {
               if(var3.worldServer[var4] != null) {
                  var5 = var3.worldServer[var4];
                  var6 = var5.savingDisabled;
                  var5.savingDisabled = false;
                  var5.flushSave();
                  var5.savingDisabled = var6;
               }
            }

            var1.sendMessage(new ChatMessage("commands.save.flushEnd", new Object[0]));
         }
      } catch (ExceptionWorldConflict var7) {
         a(var1, this, "commands.save.failed", new Object[]{var7.getMessage()});
         return;
      }

      a(var1, this, "commands.save.success", new Object[0]);
   }
}
