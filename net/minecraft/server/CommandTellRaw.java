package net.minecraft.server;

import com.google.gson.JsonParseException;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentUtils;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionInvalidSyntax;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class CommandTellRaw extends CommandAbstract {
   public CommandTellRaw() {
   }

   public String getCommand() {
      return "tellraw";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.tellraw.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 2) {
         throw new ExceptionUsage("commands.tellraw.usage", new Object[0]);
      } else {
         EntityPlayer var3 = a(var1, var2[0]);
         String var4 = a(var2, 1);

         try {
            IChatBaseComponent var5 = IChatBaseComponent.ChatSerializer.a(var4);
            var3.sendMessage(ChatComponentUtils.filterForDisplay(var1, var5, var3));
         } catch (JsonParseException var7) {
            Throwable var6 = ExceptionUtils.getRootCause(var7);
            throw new ExceptionInvalidSyntax("commands.tellraw.jsonException", new Object[]{var6 == null?"":var6.getMessage()});
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayers()):null;
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
