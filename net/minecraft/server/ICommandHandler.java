package net.minecraft.server;

import java.util.List;
import java.util.Map;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ICommand;
import net.minecraft.server.ICommandListener;

public interface ICommandHandler {
   int a(ICommandListener var1, String var2);

   List<String> a(ICommandListener var1, String var2, BlockPosition var3);

   List<ICommand> a(ICommandListener var1);

   Map<String, ICommand> getCommands();
}
