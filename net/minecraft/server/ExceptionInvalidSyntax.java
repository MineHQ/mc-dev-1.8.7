package net.minecraft.server;

import net.minecraft.server.CommandException;

public class ExceptionInvalidSyntax extends CommandException {
   public ExceptionInvalidSyntax() {
      this("commands.generic.snytax", new Object[0]);
   }

   public ExceptionInvalidSyntax(String var1, Object... var2) {
      super(var1, var2);
   }
}
