package net.minecraft.server;

import net.minecraft.server.CommandException;

public class ExceptionEntityNotFound extends CommandException {
   public ExceptionEntityNotFound() {
      this("commands.generic.entity.notFound", new Object[0]);
   }

   public ExceptionEntityNotFound(String var1, Object... var2) {
      super(var1, var2);
   }
}
