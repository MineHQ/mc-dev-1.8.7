package net.minecraft.server;

import net.minecraft.server.ICommand;
import net.minecraft.server.ICommandListener;

public interface ICommandDispatcher {
   void a(ICommandListener var1, ICommand var2, int var3, String var4, Object... var5);
}
