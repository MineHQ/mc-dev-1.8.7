package net.minecraft.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatClickable;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.ExceptionInvalidNumber;
import net.minecraft.server.ExceptionUnknownCommand;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommand;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;

public class CommandHelp extends CommandAbstract {
   public CommandHelp() {
   }

   public String getCommand() {
      return "help";
   }

   public int a() {
      return 0;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.help.usage";
   }

   public List<String> b() {
      return Arrays.asList(new String[]{"?"});
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      List var3 = this.d(var1);
      boolean var4 = true;
      int var5 = (var3.size() - 1) / 7;
      boolean var6 = false;

      int var13;
      try {
         var13 = var2.length == 0?0:a(var2[0], 1, var5 + 1) - 1;
      } catch (ExceptionInvalidNumber var12) {
         Map var8 = this.d();
         ICommand var9 = (ICommand)var8.get(var2[0]);
         if(var9 != null) {
            throw new ExceptionUsage(var9.getUsage(var1), new Object[0]);
         }

         if(MathHelper.a(var2[0], -1) != -1) {
            throw var12;
         }

         throw new ExceptionUnknownCommand();
      }

      int var7 = Math.min((var13 + 1) * 7, var3.size());
      ChatMessage var14 = new ChatMessage("commands.help.header", new Object[]{Integer.valueOf(var13 + 1), Integer.valueOf(var5 + 1)});
      var14.getChatModifier().setColor(EnumChatFormat.DARK_GREEN);
      var1.sendMessage(var14);

      for(int var15 = var13 * 7; var15 < var7; ++var15) {
         ICommand var10 = (ICommand)var3.get(var15);
         ChatMessage var11 = new ChatMessage(var10.getUsage(var1), new Object[0]);
         var11.getChatModifier().setChatClickable(new ChatClickable(ChatClickable.EnumClickAction.SUGGEST_COMMAND, "/" + var10.getCommand() + " "));
         var1.sendMessage(var11);
      }

      if(var13 == 0 && var1 instanceof EntityHuman) {
         ChatMessage var16 = new ChatMessage("commands.help.footer", new Object[0]);
         var16.getChatModifier().setColor(EnumChatFormat.GREEN);
         var1.sendMessage(var16);
      }

   }

   protected List<ICommand> d(ICommandListener var1) {
      List var2 = MinecraftServer.getServer().getCommandHandler().a(var1);
      Collections.sort(var2);
      return var2;
   }

   protected Map<String, ICommand> d() {
      return MinecraftServer.getServer().getCommandHandler().getCommands();
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      if(var2.length == 1) {
         Set var4 = this.d().keySet();
         return a(var2, (String[])var4.toArray(new String[var4.size()]));
      } else {
         return null;
      }
   }
}
