package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.GameRules;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PacketPlayOutEntityStatus;

public class CommandGamerule extends CommandAbstract {
   public CommandGamerule() {
   }

   public String getCommand() {
      return "gamerule";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.gamerule.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      GameRules var3 = this.d();
      String var4 = var2.length > 0?var2[0]:"";
      String var5 = var2.length > 1?a(var2, 1):"";
      switch(var2.length) {
      case 0:
         var1.sendMessage(new ChatComponentText(a(var3.getGameRules())));
         break;
      case 1:
         if(!var3.contains(var4)) {
            throw new CommandException("commands.gamerule.norule", new Object[]{var4});
         }

         String var6 = var3.get(var4);
         var1.sendMessage((new ChatComponentText(var4)).a(" = ").a(var6));
         var1.a(CommandObjectiveExecutor.EnumCommandResult.QUERY_RESULT, var3.c(var4));
         break;
      default:
         if(var3.a(var4, GameRules.EnumGameRuleType.BOOLEAN_VALUE) && !"true".equals(var5) && !"false".equals(var5)) {
            throw new CommandException("commands.generic.boolean.invalid", new Object[]{var5});
         }

         var3.set(var4, var5);
         a(var3, var4);
         a(var1, this, "commands.gamerule.success", new Object[0]);
      }

   }

   public static void a(GameRules var0, String var1) {
      if("reducedDebugInfo".equals(var1)) {
         int var2 = var0.getBoolean(var1)?22:23;
         Iterator var3 = MinecraftServer.getServer().getPlayerList().v().iterator();

         while(var3.hasNext()) {
            EntityPlayer var4 = (EntityPlayer)var3.next();
            var4.playerConnection.sendPacket(new PacketPlayOutEntityStatus(var4, (byte)var2));
         }
      }

   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      if(var2.length == 1) {
         return a(var2, this.d().getGameRules());
      } else {
         if(var2.length == 2) {
            GameRules var4 = this.d();
            if(var4.a(var2[0], GameRules.EnumGameRuleType.BOOLEAN_VALUE)) {
               return a(var2, new String[]{"true", "false"});
            }
         }

         return null;
      }
   }

   private GameRules d() {
      return MinecraftServer.getServer().getWorldServer(0).getGameRules();
   }
}
