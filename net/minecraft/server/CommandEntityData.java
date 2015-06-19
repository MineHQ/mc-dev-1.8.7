package net.minecraft.server;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTTagCompound;

public class CommandEntityData extends CommandAbstract {
   public CommandEntityData() {
   }

   public String getCommand() {
      return "entitydata";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.entitydata.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 2) {
         throw new ExceptionUsage("commands.entitydata.usage", new Object[0]);
      } else {
         Entity var3 = b(var1, var2[0]);
         if(var3 instanceof EntityHuman) {
            throw new CommandException("commands.entitydata.noPlayers", new Object[]{var3.getScoreboardDisplayName()});
         } else {
            NBTTagCompound var4 = new NBTTagCompound();
            var3.e(var4);
            NBTTagCompound var5 = (NBTTagCompound)var4.clone();

            NBTTagCompound var6;
            try {
               var6 = MojangsonParser.parse(a(var1, var2, 1).c());
            } catch (MojangsonParseException var8) {
               throw new CommandException("commands.entitydata.tagError", new Object[]{var8.getMessage()});
            }

            var6.remove("UUIDMost");
            var6.remove("UUIDLeast");
            var4.a(var6);
            if(var4.equals(var5)) {
               throw new CommandException("commands.entitydata.failed", new Object[]{var4.toString()});
            } else {
               var3.f(var4);
               a(var1, this, "commands.entitydata.success", new Object[]{var4.toString()});
            }
         }
      }
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
