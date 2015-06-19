package net.minecraft.server;

import net.minecraft.server.CommandException;

public class ExceptionUnknownCommand extends CommandException {
   public ExceptionUnknownCommand() {
      this("commands.generic.notFound", new Object[0]);
   }

   public ExceptionUnknownCommand(String var1, Object... var2) {
      super(var1, var2);
   }
}
