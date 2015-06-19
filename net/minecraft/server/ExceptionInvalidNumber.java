package net.minecraft.server;

import net.minecraft.server.CommandException;

public class ExceptionInvalidNumber extends CommandException {
   public ExceptionInvalidNumber() {
      this("commands.generic.num.invalid", new Object[0]);
   }

   public ExceptionInvalidNumber(String var1, Object... var2) {
      super(var1, var2);
   }
}
