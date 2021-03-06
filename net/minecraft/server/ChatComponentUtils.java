package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.server.ChatComponentScore;
import net.minecraft.server.ChatComponentSelector;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.ChatModifier;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.ExceptionEntityNotFound;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.PlayerSelector;

public class ChatComponentUtils {
   public static IChatBaseComponent filterForDisplay(ICommandListener var0, IChatBaseComponent var1, Entity var2) throws CommandException {
      Object var3 = null;
      if(var1 instanceof ChatComponentScore) {
         ChatComponentScore var4 = (ChatComponentScore)var1;
         String var5 = var4.g();
         if(PlayerSelector.isPattern(var5)) {
            List var6 = PlayerSelector.getPlayers(var0, var5, Entity.class);
            if(var6.size() != 1) {
               throw new ExceptionEntityNotFound();
            }

            var5 = ((Entity)var6.get(0)).getName();
         }

         var3 = var2 != null && var5.equals("*")?new ChatComponentScore(var2.getName(), var4.h()):new ChatComponentScore(var5, var4.h());
         ((ChatComponentScore)var3).b(var4.getText());
      } else if(var1 instanceof ChatComponentSelector) {
         String var7 = ((ChatComponentSelector)var1).g();
         var3 = PlayerSelector.getPlayerNames(var0, var7);
         if(var3 == null) {
            var3 = new ChatComponentText("");
         }
      } else if(var1 instanceof ChatComponentText) {
         var3 = new ChatComponentText(((ChatComponentText)var1).g());
      } else {
         if(!(var1 instanceof ChatMessage)) {
            return var1;
         }

         Object[] var8 = ((ChatMessage)var1).j();

         for(int var10 = 0; var10 < var8.length; ++var10) {
            Object var11 = var8[var10];
            if(var11 instanceof IChatBaseComponent) {
               var8[var10] = filterForDisplay(var0, (IChatBaseComponent)var11, var2);
            }
         }

         var3 = new ChatMessage(((ChatMessage)var1).i(), var8);
      }

      ChatModifier var9 = var1.getChatModifier();
      if(var9 != null) {
         ((IChatBaseComponent)var3).setChatModifier(var9.clone());
      }

      Iterator var13 = var1.a().iterator();

      while(var13.hasNext()) {
         IChatBaseComponent var12 = (IChatBaseComponent)var13.next();
         ((IChatBaseComponent)var3).addSibling(filterForDisplay(var0, var12, var2));
      }

      return (IChatBaseComponent)var3;
   }
}
