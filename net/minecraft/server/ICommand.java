package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandException;
import net.minecraft.server.ICommandListener;

public interface ICommand extends Comparable<ICommand> {
   String getCommand();

   String getUsage(ICommandListener var1);

   List<String> b();

   void execute(ICommandListener var1, String[] var2) throws CommandException;

   boolean canUse(ICommandListener var1);

   List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3);

   boolean isListStart(String[] var1, int var2);
}
