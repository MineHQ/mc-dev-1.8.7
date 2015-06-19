package net.minecraft.server;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PacketPlayOutPosition;

public class CommandTp extends CommandAbstract {
   public CommandTp() {
   }

   public String getCommand() {
      return "tp";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.tp.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 1) {
         throw new ExceptionUsage("commands.tp.usage", new Object[0]);
      } else {
         byte var3 = 0;
         Object var4;
         if(var2.length != 2 && var2.length != 4 && var2.length != 6) {
            var4 = b(var1);
         } else {
            var4 = b(var1, var2[0]);
            var3 = 1;
         }

         if(var2.length != 1 && var2.length != 2) {
            if(var2.length < var3 + 3) {
               throw new ExceptionUsage("commands.tp.usage", new Object[0]);
            } else if(((Entity)var4).world != null) {
               int var14 = var3 + 1;
               CommandAbstract.CommandNumber var6 = a(((Entity)var4).locX, var2[var3], true);
               CommandAbstract.CommandNumber var7 = a(((Entity)var4).locY, var2[var14++], 0, 0, false);
               CommandAbstract.CommandNumber var8 = a(((Entity)var4).locZ, var2[var14++], true);
               CommandAbstract.CommandNumber var9 = a((double)((Entity)var4).yaw, var2.length > var14?var2[var14++]:"~", false);
               CommandAbstract.CommandNumber var10 = a((double)((Entity)var4).pitch, var2.length > var14?var2[var14]:"~", false);
               float var12;
               if(var4 instanceof EntityPlayer) {
                  EnumSet var11 = EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class);
                  if(var6.c()) {
                     var11.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X);
                  }

                  if(var7.c()) {
                     var11.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y);
                  }

                  if(var8.c()) {
                     var11.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z);
                  }

                  if(var10.c()) {
                     var11.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT);
                  }

                  if(var9.c()) {
                     var11.add(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT);
                  }

                  var12 = (float)var9.b();
                  if(!var9.c()) {
                     var12 = MathHelper.g(var12);
                  }

                  float var13 = (float)var10.b();
                  if(!var10.c()) {
                     var13 = MathHelper.g(var13);
                  }

                  if(var13 > 90.0F || var13 < -90.0F) {
                     var13 = MathHelper.g(180.0F - var13);
                     var12 = MathHelper.g(var12 + 180.0F);
                  }

                  ((Entity)var4).mount((Entity)null);
                  ((EntityPlayer)var4).playerConnection.a(var6.b(), var7.b(), var8.b(), var12, var13, var11);
                  ((Entity)var4).f(var12);
               } else {
                  float var15 = (float)MathHelper.g(var9.a());
                  var12 = (float)MathHelper.g(var10.a());
                  if(var12 > 90.0F || var12 < -90.0F) {
                     var12 = MathHelper.g(180.0F - var12);
                     var15 = MathHelper.g(var15 + 180.0F);
                  }

                  ((Entity)var4).setPositionRotation(var6.a(), var7.a(), var8.a(), var15, var12);
                  ((Entity)var4).f(var15);
               }

               a(var1, this, "commands.tp.success.coordinates", new Object[]{((Entity)var4).getName(), Double.valueOf(var6.a()), Double.valueOf(var7.a()), Double.valueOf(var8.a())});
            }
         } else {
            Entity var5 = b(var1, var2[var2.length - 1]);
            if(var5.world != ((Entity)var4).world) {
               throw new CommandException("commands.tp.notSameDimension", new Object[0]);
            } else {
               ((Entity)var4).mount((Entity)null);
               if(var4 instanceof EntityPlayer) {
                  ((EntityPlayer)var4).playerConnection.a(var5.locX, var5.locY, var5.locZ, var5.yaw, var5.pitch);
               } else {
                  ((Entity)var4).setPositionRotation(var5.locX, var5.locY, var5.locZ, var5.yaw, var5.pitch);
               }

               a(var1, this, "commands.tp.success", new Object[]{((Entity)var4).getName(), var5.getName()});
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length != 1 && var2.length != 2?null:a(var2, MinecraftServer.getServer().getPlayers());
   }

   public boolean isListStart(String[] var1, int var2) {
      return var2 == 0;
   }
}
