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
import net.minecraft.server.PacketPlayOutTitle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandTitle extends CommandAbstract {
   private static final Logger a = LogManager.getLogger();

   public CommandTitle() {
   }

   public String getCommand() {
      return "title";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.title.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 2) {
         throw new ExceptionUsage("commands.title.usage", new Object[0]);
      } else {
         if(var2.length < 3) {
            if("title".equals(var2[1]) || "subtitle".equals(var2[1])) {
               throw new ExceptionUsage("commands.title.usage.title", new Object[0]);
            }

            if("times".equals(var2[1])) {
               throw new ExceptionUsage("commands.title.usage.times", new Object[0]);
            }
         }

         EntityPlayer var3 = a(var1, var2[0]);
         PacketPlayOutTitle.EnumTitleAction var4 = PacketPlayOutTitle.EnumTitleAction.a(var2[1]);
         if(var4 != PacketPlayOutTitle.EnumTitleAction.CLEAR && var4 != PacketPlayOutTitle.EnumTitleAction.RESET) {
            if(var4 == PacketPlayOutTitle.EnumTitleAction.TIMES) {
               if(var2.length != 5) {
                  throw new ExceptionUsage("commands.title.usage", new Object[0]);
               } else {
                  int var11 = a(var2[2]);
                  int var12 = a(var2[3]);
                  int var13 = a(var2[4]);
                  PacketPlayOutTitle var14 = new PacketPlayOutTitle(var11, var12, var13);
                  var3.playerConnection.sendPacket(var14);
                  a(var1, this, "commands.title.success", new Object[0]);
               }
            } else if(var2.length < 3) {
               throw new ExceptionUsage("commands.title.usage", new Object[0]);
            } else {
               String var10 = a(var2, 2);

               IChatBaseComponent var6;
               try {
                  var6 = IChatBaseComponent.ChatSerializer.a(var10);
               } catch (JsonParseException var9) {
                  Throwable var8 = ExceptionUtils.getRootCause(var9);
                  throw new ExceptionInvalidSyntax("commands.tellraw.jsonException", new Object[]{var8 == null?"":var8.getMessage()});
               }

               PacketPlayOutTitle var7 = new PacketPlayOutTitle(var4, ChatComponentUtils.filterForDisplay(var1, var6, var3));
               var3.playerConnection.sendPacket(var7);
               a(var1, this, "commands.title.success", new Object[0]);
            }
         } else if(var2.length != 2) {
            throw new ExceptionUsage("commands.title.usage", new Object[0]);
         } else {
            PacketPlayOutTitle var5 = new PacketPlayOutTitle(var4, (IChatBaseComponent)null);
            var3.playerConnection.sendPacket(var5);
            a(var1, this, "commands.title.success", new Object[0]);
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, MinecraftServer.getServer().getPlayers()):(var2.length == 2?a(var2, PacketPlayOutTitle.EnumTitleAction.a()):null);
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
