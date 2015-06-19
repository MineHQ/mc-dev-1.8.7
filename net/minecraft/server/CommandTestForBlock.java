package net.minecraft.server;

import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.ExceptionInvalidNumber;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class CommandTestForBlock extends CommandAbstract {
   public CommandTestForBlock() {
   }

   public String getCommand() {
      return "testforblock";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.testforblock.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 4) {
         throw new ExceptionUsage("commands.testforblock.usage", new Object[0]);
      } else {
         var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
         BlockPosition var3 = a(var1, var2, 0, false);
         Block var4 = Block.getByName(var2[3]);
         if(var4 == null) {
            throw new ExceptionInvalidNumber("commands.setblock.notFound", new Object[]{var2[3]});
         } else {
            int var5 = -1;
            if(var2.length >= 5) {
               var5 = a(var2[4], -1, 15);
            }

            World var6 = var1.getWorld();
            if(!var6.isLoaded(var3)) {
               throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
            } else {
               NBTTagCompound var7 = new NBTTagCompound();
               boolean var8 = false;
               if(var2.length >= 6 && var4.isTileEntity()) {
                  String var9 = a(var1, var2, 5).c();

                  try {
                     var7 = MojangsonParser.parse(var9);
                     var8 = true;
                  } catch (MojangsonParseException var13) {
                     throw new CommandException("commands.setblock.tagError", new Object[]{var13.getMessage()});
                  }
               }

               IBlockData var14 = var6.getType(var3);
               Block var10 = var14.getBlock();
               if(var10 != var4) {
                  throw new CommandException("commands.testforblock.failed.tile", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ()), var10.getName(), var4.getName()});
               } else {
                  if(var5 > -1) {
                     int var11 = var14.getBlock().toLegacyData(var14);
                     if(var11 != var5) {
                        throw new CommandException("commands.testforblock.failed.data", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ()), Integer.valueOf(var11), Integer.valueOf(var5)});
                     }
                  }

                  if(var8) {
                     TileEntity var15 = var6.getTileEntity(var3);
                     if(var15 == null) {
                        throw new CommandException("commands.testforblock.failed.tileEntity", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ())});
                     }

                     NBTTagCompound var12 = new NBTTagCompound();
                     var15.b(var12);
                     if(!GameProfileSerializer.a(var7, var12, true)) {
                        throw new CommandException("commands.testforblock.failed.nbt", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ())});
                     }
                  }

                  var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 1);
                  a(var1, this, "commands.testforblock.success", new Object[]{Integer.valueOf(var3.getX()), Integer.valueOf(var3.getY()), Integer.valueOf(var3.getZ())});
               }
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length > 0 && var2.length <= 3?a(var2, 0, var3):(var2.length == 4?a(var2, Block.REGISTRY.keySet()):null);
   }
}
