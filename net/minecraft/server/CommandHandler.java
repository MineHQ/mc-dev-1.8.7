package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommand;
import net.minecraft.server.ICommandHandler;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.PlayerSelector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ICommandHandler {
   private static final Logger a = LogManager.getLogger();
   private final Map<String, ICommand> b = Maps.newHashMap();
   private final Set<ICommand> c = Sets.newHashSet();

   public CommandHandler() {
   }

   public int a(ICommandListener var1, String var2) {
      var2 = var2.trim();
      if(var2.startsWith("/")) {
         var2 = var2.substring(1);
      }

      String[] var3 = var2.split(" ");
      String var4 = var3[0];
      var3 = a(var3);
      ICommand var5 = (ICommand)this.b.get(var4);
      int var6 = this.a(var5, var3);
      int var7 = 0;
      ChatMessage var8;
      if(var5 == null) {
         var8 = new ChatMessage("commands.generic.notFound", new Object[0]);
         var8.getChatModifier().setColor(EnumChatFormat.RED);
         var1.sendMessage(var8);
      } else if(var5.canUse(var1)) {
         if(var6 > -1) {
            List var12 = PlayerSelector.getPlayers(var1, var3[var6], Entity.class);
            String var9 = var3[var6];
            var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, var12.size());
            Iterator var10 = var12.iterator();

            while(var10.hasNext()) {
               Entity var11 = (Entity)var10.next();
               var3[var6] = var11.getUniqueID().toString();
               if(this.a(var1, var3, var5, var2)) {
                  ++var7;
               }
            }

            var3[var6] = var9;
         } else {
            var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, 1);
            if(this.a(var1, var3, var5, var2)) {
               ++var7;
            }
         }
      } else {
         var8 = new ChatMessage("commands.generic.permission", new Object[0]);
         var8.getChatModifier().setColor(EnumChatFormat.RED);
         var1.sendMessage(var8);
      }

      var1.a(CommandObjectiveExecutor.EnumCommandResult.SUCCESS_COUNT, var7);
      return var7;
   }

   protected boolean a(ICommandListener var1, String[] var2, ICommand var3, String var4) {
      ChatMessage var6;
      try {
         var3.execute(var1, var2);
         return true;
      } catch (ExceptionUsage var7) {
         var6 = new ChatMessage("commands.generic.usage", new Object[]{new ChatMessage(var7.getMessage(), var7.getArgs())});
         var6.getChatModifier().setColor(EnumChatFormat.RED);
         var1.sendMessage(var6);
      } catch (CommandException var8) {
         var6 = new ChatMessage(var8.getMessage(), var8.getArgs());
         var6.getChatModifier().setColor(EnumChatFormat.RED);
         var1.sendMessage(var6);
      } catch (Throwable var9) {
         var6 = new ChatMessage("commands.generic.exception", new Object[0]);
         var6.getChatModifier().setColor(EnumChatFormat.RED);
         var1.sendMessage(var6);
         a.warn("Couldn\'t process command: \'" + var4 + "\'");
      }

      return false;
   }

   public ICommand a(ICommand var1) {
      this.b.put(var1.getCommand(), var1);
      this.c.add(var1);
      Iterator var2 = var1.b().iterator();

      while(true) {
         String var3;
         ICommand var4;
         do {
            if(!var2.hasNext()) {
               return var1;
            }

            var3 = (String)var2.next();
            var4 = (ICommand)this.b.get(var3);
         } while(var4 != null && var4.getCommand().equals(var3));

         this.b.put(var3, var1);
      }
   }

   private static String[] a(String[] var0) {
      String[] var1 = new String[var0.length - 1];
      System.arraycopy(var0, 1, var1, 0, var0.length - 1);
      return var1;
   }

   public List<String> a(ICommandListener var1, String var2, BlockPosition var3) {
      String[] var4 = var2.split(" ", -1);
      String var5 = var4[0];
      if(var4.length == 1) {
         ArrayList var9 = Lists.newArrayList();
         Iterator var7 = this.b.entrySet().iterator();

         while(var7.hasNext()) {
            Entry var8 = (Entry)var7.next();
            if(CommandAbstract.a(var5, (String)var8.getKey()) && ((ICommand)var8.getValue()).canUse(var1)) {
               var9.add(var8.getKey());
            }
         }

         return var9;
      } else {
         if(var4.length > 1) {
            ICommand var6 = (ICommand)this.b.get(var5);
            if(var6 != null && var6.canUse(var1)) {
               return var6.tabComplete(var1, a(var4), var3);
            }
         }

         return null;
      }
   }

   public List<ICommand> a(ICommandListener var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = this.c.iterator();

      while(var3.hasNext()) {
         ICommand var4 = (ICommand)var3.next();
         if(var4.canUse(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public Map<String, ICommand> getCommands() {
      return this.b;
   }

   private int a(ICommand var1, String[] var2) {
      if(var1 == null) {
         return -1;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if(var1.isListStart(var2, var3) && PlayerSelector.isList(var2[var3])) {
               return var3;
            }
         }

         return -1;
      }
   }
}
