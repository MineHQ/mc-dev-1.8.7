package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.ExceptionInvalidNumber;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;

public class CommandEffect extends CommandAbstract {
   public CommandEffect() {
   }

   public String getCommand() {
      return "effect";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.effect.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 2) {
         throw new ExceptionUsage("commands.effect.usage", new Object[0]);
      } else {
         EntityLiving var3 = (EntityLiving)a(var1, var2[0], EntityLiving.class);
         if(var2[1].equals("clear")) {
            if(var3.getEffects().isEmpty()) {
               throw new CommandException("commands.effect.failure.notActive.all", new Object[]{var3.getName()});
            } else {
               var3.removeAllEffects();
               a(var1, this, "commands.effect.success.removed.all", new Object[]{var3.getName()});
            }
         } else {
            int var4;
            try {
               var4 = a(var2[1], 1);
            } catch (ExceptionInvalidNumber var11) {
               MobEffectList var6 = MobEffectList.b(var2[1]);
               if(var6 == null) {
                  throw var11;
               }

               var4 = var6.id;
            }

            int var5 = 600;
            int var12 = 30;
            int var7 = 0;
            if(var4 >= 0 && var4 < MobEffectList.byId.length && MobEffectList.byId[var4] != null) {
               MobEffectList var8 = MobEffectList.byId[var4];
               if(var2.length >= 3) {
                  var12 = a(var2[2], 0, 1000000);
                  if(var8.isInstant()) {
                     var5 = var12;
                  } else {
                     var5 = var12 * 20;
                  }
               } else if(var8.isInstant()) {
                  var5 = 1;
               }

               if(var2.length >= 4) {
                  var7 = a(var2[3], 0, 255);
               }

               boolean var9 = true;
               if(var2.length >= 5 && "true".equalsIgnoreCase(var2[4])) {
                  var9 = false;
               }

               if(var12 > 0) {
                  MobEffect var10 = new MobEffect(var4, var5, var7, false, var9);
                  var3.addEffect(var10);
                  a(var1, this, "commands.effect.success", new Object[]{new ChatMessage(var10.g(), new Object[0]), Integer.valueOf(var4), Integer.valueOf(var7), var3.getName(), Integer.valueOf(var12)});
               } else if(var3.hasEffect(var4)) {
                  var3.removeEffect(var4);
                  a(var1, this, "commands.effect.success.removed", new Object[]{new ChatMessage(var8.a(), new Object[0]), var3.getName()});
               } else {
                  throw new CommandException("commands.effect.failure.notActive", new Object[]{new ChatMessage(var8.a(), new Object[0]), var3.getName()});
               }
            } else {
               throw new ExceptionInvalidNumber("commands.effect.notFound", new Object[]{Integer.valueOf(var4)});
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, this.d()):(var2.length == 2?a(var2, MobEffectList.c()):(var2.length == 5?a(var2, new String[]{"true", "false"}):null));
   }

   protected String[] d() {
      return MinecraftServer.getServer().getPlayers();
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
