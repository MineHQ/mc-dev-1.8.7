package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class CommandBlockData extends CommandAbstract {
   public CommandBlockData() {
   }

   public String getCommand() {
      return "blockdata";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.blockdata.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 4) {
         throw new ExceptionUsage("commands.blockdata.usage", new Object[0]);
      } else {
         var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
         BlockPosition var3 = a(var1, var2, 0, false);
         World var4 = var1.getWorld();
         if(!var4.isLoaded(var3)) {
            throw new CommandException("commands.blockdata.outOfWorld", new Object[0]);
         } else {
            TileEntity var5 = var4.getTileEntity(var3);
            if(var5 == null) {
               throw new CommandException("commands.blockdata.notValid", new Object[0]);
            } else {
               NBTTagCompound var6 = new NBTTagCompound();
               var5.b(var6);
               NBTTagCompound var7 = (NBTTagCompound)var6.clone();

               NBTTagCompound var8;
               try {
                  var8 = MojangsonParser.parse(a(var1, var2, 3).c());
               } catch (MojangsonParseException var10) {
                  throw new CommandException("commands.blockdata.tagError", new Object[]{var10.getMessage()});
               }

               var6.a(var8);
               var6.setInt("x", var3.getX());
               var6.setInt("y", var3.getY());
               var6.setInt("z", var3.getZ());
               if(var6.equals(var7)) {
                  throw new CommandException("commands.blockdata.failed", new Object[]{var6.toString()});
               } else {
                  var5.a(var6);
                  var5.update();
                  var4.notify(var3);
                  var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 1);
                  a(var1, this, "commands.blockdata.success", new Object[]{var6.toString()});
               }
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length > 0 && var2.length <= 3?a(var2, 0, var3):null;
   }
}
