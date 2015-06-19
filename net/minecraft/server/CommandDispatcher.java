package net.minecraft.server;

import java.util.Iterator;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandAchievement;
import net.minecraft.server.CommandBan;
import net.minecraft.server.CommandBanIp;
import net.minecraft.server.CommandBanList;
import net.minecraft.server.CommandBlockData;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.CommandClear;
import net.minecraft.server.CommandClone;
import net.minecraft.server.CommandDebug;
import net.minecraft.server.CommandDeop;
import net.minecraft.server.CommandDifficulty;
import net.minecraft.server.CommandEffect;
import net.minecraft.server.CommandEnchant;
import net.minecraft.server.CommandEntityData;
import net.minecraft.server.CommandExecute;
import net.minecraft.server.CommandFill;
import net.minecraft.server.CommandGamemode;
import net.minecraft.server.CommandGamemodeDefault;
import net.minecraft.server.CommandGamerule;
import net.minecraft.server.CommandGive;
import net.minecraft.server.CommandHandler;
import net.minecraft.server.CommandHelp;
import net.minecraft.server.CommandIdleTimeout;
import net.minecraft.server.CommandKick;
import net.minecraft.server.CommandKill;
import net.minecraft.server.CommandList;
import net.minecraft.server.CommandMe;
import net.minecraft.server.CommandOp;
import net.minecraft.server.CommandPardon;
import net.minecraft.server.CommandPardonIP;
import net.minecraft.server.CommandParticle;
import net.minecraft.server.CommandPlaySound;
import net.minecraft.server.CommandPublish;
import net.minecraft.server.CommandReplaceItem;
import net.minecraft.server.CommandSaveAll;
import net.minecraft.server.CommandSaveOff;
import net.minecraft.server.CommandSaveOn;
import net.minecraft.server.CommandSay;
import net.minecraft.server.CommandScoreboard;
import net.minecraft.server.CommandSeed;
import net.minecraft.server.CommandSetBlock;
import net.minecraft.server.CommandSetWorldSpawn;
import net.minecraft.server.CommandSpawnpoint;
import net.minecraft.server.CommandSpreadPlayers;
import net.minecraft.server.CommandStats;
import net.minecraft.server.CommandStop;
import net.minecraft.server.CommandSummon;
import net.minecraft.server.CommandTell;
import net.minecraft.server.CommandTellRaw;
import net.minecraft.server.CommandTestFor;
import net.minecraft.server.CommandTestForBlock;
import net.minecraft.server.CommandTestForBlocks;
import net.minecraft.server.CommandTime;
import net.minecraft.server.CommandTitle;
import net.minecraft.server.CommandToggleDownfall;
import net.minecraft.server.CommandTp;
import net.minecraft.server.CommandTrigger;
import net.minecraft.server.CommandWeather;
import net.minecraft.server.CommandWhitelist;
import net.minecraft.server.CommandWorldBorder;
import net.minecraft.server.CommandXp;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.ICommand;
import net.minecraft.server.ICommandDispatcher;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RemoteControlCommandListener;

public class CommandDispatcher extends CommandHandler implements ICommandDispatcher {
   public CommandDispatcher() {
      this.a(new CommandTime());
      this.a(new CommandGamemode());
      this.a(new CommandDifficulty());
      this.a(new CommandGamemodeDefault());
      this.a(new CommandKill());
      this.a(new CommandToggleDownfall());
      this.a(new CommandWeather());
      this.a(new CommandXp());
      this.a(new CommandTp());
      this.a(new CommandGive());
      this.a(new CommandReplaceItem());
      this.a(new CommandStats());
      this.a(new CommandEffect());
      this.a(new CommandEnchant());
      this.a(new CommandParticle());
      this.a(new CommandMe());
      this.a(new CommandSeed());
      this.a(new CommandHelp());
      this.a(new CommandDebug());
      this.a(new CommandTell());
      this.a(new CommandSay());
      this.a(new CommandSpawnpoint());
      this.a(new CommandSetWorldSpawn());
      this.a(new CommandGamerule());
      this.a(new CommandClear());
      this.a(new CommandTestFor());
      this.a(new CommandSpreadPlayers());
      this.a(new CommandPlaySound());
      this.a(new CommandScoreboard());
      this.a(new CommandExecute());
      this.a(new CommandTrigger());
      this.a(new CommandAchievement());
      this.a(new CommandSummon());
      this.a(new CommandSetBlock());
      this.a(new CommandFill());
      this.a(new CommandClone());
      this.a(new CommandTestForBlocks());
      this.a(new CommandBlockData());
      this.a(new CommandTestForBlock());
      this.a(new CommandTellRaw());
      this.a(new CommandWorldBorder());
      this.a(new CommandTitle());
      this.a(new CommandEntityData());
      if(MinecraftServer.getServer().ae()) {
         this.a(new CommandOp());
         this.a(new CommandDeop());
         this.a(new CommandStop());
         this.a(new CommandSaveAll());
         this.a(new CommandSaveOff());
         this.a(new CommandSaveOn());
         this.a(new CommandBanIp());
         this.a(new CommandPardonIP());
         this.a(new CommandBan());
         this.a(new CommandBanList());
         this.a(new CommandPardon());
         this.a(new CommandKick());
         this.a(new CommandList());
         this.a(new CommandWhitelist());
         this.a(new CommandIdleTimeout());
      } else {
         this.a(new CommandPublish());
      }

      CommandAbstract.a((ICommandDispatcher)this);
   }

   public void a(ICommandListener var1, ICommand var2, int var3, String var4, Object... var5) {
      boolean var6 = true;
      MinecraftServer var7 = MinecraftServer.getServer();
      if(!var1.getSendCommandFeedback()) {
         var6 = false;
      }

      ChatMessage var8 = new ChatMessage("chat.type.admin", new Object[]{var1.getName(), new ChatMessage(var4, var5)});
      var8.getChatModifier().setColor(EnumChatFormat.GRAY);
      var8.getChatModifier().setItalic(Boolean.valueOf(true));
      if(var6) {
         Iterator var9 = var7.getPlayerList().v().iterator();

         label85:
         while(true) {
            EntityHuman var10;
            boolean var11;
            boolean var12;
            do {
               do {
                  do {
                     do {
                        if(!var9.hasNext()) {
                           break label85;
                        }

                        var10 = (EntityHuman)var9.next();
                     } while(var10 == var1);
                  } while(!var7.getPlayerList().isOp(var10.getProfile()));
               } while(!var2.canUse(var1));

               var11 = var1 instanceof MinecraftServer && MinecraftServer.getServer().r();
               var12 = var1 instanceof RemoteControlCommandListener && MinecraftServer.getServer().q();
            } while(!var11 && !var12 && (var1 instanceof RemoteControlCommandListener || var1 instanceof MinecraftServer));

            var10.sendMessage(var8);
         }
      }

      if(var1 != var7 && var7.worldServer[0].getGameRules().getBoolean("logAdminCommands")) {
         var7.sendMessage(var8);
      }

      boolean var13 = var7.worldServer[0].getGameRules().getBoolean("sendCommandFeedback");
      if(var1 instanceof CommandBlockListenerAbstract) {
         var13 = ((CommandBlockListenerAbstract)var1).m();
      }

      if((var3 & 1) != 1 && var13 || var1 instanceof MinecraftServer) {
         var1.sendMessage(new ChatMessage(var4, var5));
      }

   }
}
