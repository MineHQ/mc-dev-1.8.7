package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;

public class CommandGive extends CommandAbstract {
   public CommandGive() {
   }

   public String getCommand() {
      return "give";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.give.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 2) {
         throw new ExceptionUsage("commands.give.usage", new Object[0]);
      } else {
         EntityPlayer var3 = a(var1, var2[0]);
         Item var4 = f(var1, var2[1]);
         int var5 = var2.length >= 3?a(var2[2], 1, 64):1;
         int var6 = var2.length >= 4?a(var2[3]):0;
         ItemStack var7 = new ItemStack(var4, var5, var6);
         if(var2.length >= 5) {
            String var8 = a(var1, var2, 4).c();

            try {
               var7.setTag(MojangsonParser.parse(var8));
            } catch (MojangsonParseException var10) {
               throw new CommandException("commands.give.tagError", new Object[]{var10.getMessage()});
            }
         }

         boolean var11 = var3.inventory.pickup(var7);
         if(var11) {
            var3.world.makeSound(var3, "random.pop", 0.2F, ((var3.bc().nextFloat() - var3.bc().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var3.defaultContainer.b();
         }

         EntityItem var9;
         if(var11 && var7.count <= 0) {
            var7.count = 1;
            var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, var5);
            var9 = var3.drop(var7, false);
            if(var9 != null) {
               var9.v();
            }
         } else {
            var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ITEMS, var5 - var7.count);
            var9 = var3.drop(var7, false);
            if(var9 != null) {
               var9.q();
               var9.b(var3.getName());
            }
         }

         a(var1, this, "commands.give.success", new Object[]{var7.C(), Integer.valueOf(var5), var3.getName()});
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, this.d()):(var2.length == 2?a(var2, Item.REGISTRY.keySet()):null);
   }

   protected String[] d() {
      return MinecraftServer.getServer().getPlayers();
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
