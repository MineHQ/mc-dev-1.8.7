package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ExceptionEntityNotFound;
import net.minecraft.server.ExceptionPlayerNotFound;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerSelector;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.World;

public class CommandSpreadPlayers extends CommandAbstract {
   public CommandSpreadPlayers() {
   }

   public String getCommand() {
      return "spreadplayers";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.spreadplayers.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 6) {
         throw new ExceptionUsage("commands.spreadplayers.usage", new Object[0]);
      } else {
         byte var3 = 0;
         BlockPosition var4 = var1.getChunkCoordinates();
         double var10000 = (double)var4.getX();
         int var17 = var3 + 1;
         double var5 = b(var10000, var2[var3], true);
         double var7 = b((double)var4.getZ(), var2[var17++], true);
         double var9 = a(var2[var17++], 0.0D);
         double var11 = a(var2[var17++], var9 + 1.0D);
         boolean var13 = d(var2[var17++]);
         ArrayList var14 = Lists.newArrayList();

         while(var17 < var2.length) {
            String var15 = var2[var17++];
            if(PlayerSelector.isPattern(var15)) {
               List var16 = PlayerSelector.getPlayers(var1, var15, Entity.class);
               if(var16.size() == 0) {
                  throw new ExceptionEntityNotFound();
               }

               var14.addAll(var16);
            } else {
               EntityPlayer var18 = MinecraftServer.getServer().getPlayerList().getPlayer(var15);
               if(var18 == null) {
                  throw new ExceptionPlayerNotFound();
               }

               var14.add(var18);
            }
         }

         var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_ENTITIES, var14.size());
         if(var14.isEmpty()) {
            throw new ExceptionEntityNotFound();
         } else {
            var1.sendMessage(new ChatMessage("commands.spreadplayers.spreading." + (var13?"teams":"players"), new Object[]{Integer.valueOf(var14.size()), Double.valueOf(var11), Double.valueOf(var5), Double.valueOf(var7), Double.valueOf(var9)}));
            this.a(var1, var14, new CommandSpreadPlayers.Location2D(var5, var7), var9, var11, ((Entity)var14.get(0)).world, var13);
         }
      }
   }

   private void a(ICommandListener var1, List<Entity> var2, CommandSpreadPlayers.Location2D var3, double var4, double var6, World var8, boolean var9) throws CommandException {
      Random var10 = new Random();
      double var11 = var3.a - var6;
      double var13 = var3.b - var6;
      double var15 = var3.a + var6;
      double var17 = var3.b + var6;
      CommandSpreadPlayers.Location2D[] var19 = this.a(var10, var9?this.b(var2):var2.size(), var11, var13, var15, var17);
      int var20 = this.a(var3, var4, var8, var10, var11, var13, var15, var17, var19, var9);
      double var21 = this.a(var2, var8, var19, var9);
      a(var1, this, "commands.spreadplayers.success." + (var9?"teams":"players"), new Object[]{Integer.valueOf(var19.length), Double.valueOf(var3.a), Double.valueOf(var3.b)});
      if(var19.length > 1) {
         var1.sendMessage(new ChatMessage("commands.spreadplayers.info." + (var9?"teams":"players"), new Object[]{String.format("%.2f", new Object[]{Double.valueOf(var21)}), Integer.valueOf(var20)}));
      }

   }

   private int b(List<Entity> var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         if(var4 instanceof EntityHuman) {
            var2.add(((EntityHuman)var4).getScoreboardTeam());
         } else {
            var2.add((Object)null);
         }
      }

      return var2.size();
   }

   private int a(CommandSpreadPlayers.Location2D var1, double var2, World var4, Random var5, double var6, double var8, double var10, double var12, CommandSpreadPlayers.Location2D[] var14, boolean var15) throws CommandException {
      boolean var16 = true;
      double var18 = 3.4028234663852886E38D;

      int var17;
      for(var17 = 0; var17 < 10000 && var16; ++var17) {
         var16 = false;
         var18 = 3.4028234663852886E38D;

         int var22;
         CommandSpreadPlayers.Location2D var23;
         for(int var20 = 0; var20 < var14.length; ++var20) {
            CommandSpreadPlayers.Location2D var21 = var14[var20];
            var22 = 0;
            var23 = new CommandSpreadPlayers.Location2D();

            for(int var24 = 0; var24 < var14.length; ++var24) {
               if(var20 != var24) {
                  CommandSpreadPlayers.Location2D var25 = var14[var24];
                  double var26 = var21.a(var25);
                  var18 = Math.min(var26, var18);
                  if(var26 < var2) {
                     ++var22;
                     var23.a += var25.a - var21.a;
                     var23.b += var25.b - var21.b;
                  }
               }
            }

            if(var22 > 0) {
               var23.a /= (double)var22;
               var23.b /= (double)var22;
               double var30 = (double)var23.b();
               if(var30 > 0.0D) {
                  var23.a();
                  var21.b(var23);
               } else {
                  var21.a(var5, var6, var8, var10, var12);
               }

               var16 = true;
            }

            if(var21.a(var6, var8, var10, var12)) {
               var16 = true;
            }
         }

         if(!var16) {
            CommandSpreadPlayers.Location2D[] var28 = var14;
            int var29 = var14.length;

            for(var22 = 0; var22 < var29; ++var22) {
               var23 = var28[var22];
               if(!var23.b(var4)) {
                  var23.a(var5, var6, var8, var10, var12);
                  var16 = true;
               }
            }
         }
      }

      if(var17 >= 10000) {
         throw new CommandException("commands.spreadplayers.failure." + (var15?"teams":"players"), new Object[]{Integer.valueOf(var14.length), Double.valueOf(var1.a), Double.valueOf(var1.b), String.format("%.2f", new Object[]{Double.valueOf(var18)})});
      } else {
         return var17;
      }
   }

   private double a(List<Entity> var1, World var2, CommandSpreadPlayers.Location2D[] var3, boolean var4) {
      double var5 = 0.0D;
      int var7 = 0;
      HashMap var8 = Maps.newHashMap();

      for(int var9 = 0; var9 < var1.size(); ++var9) {
         Entity var10 = (Entity)var1.get(var9);
         CommandSpreadPlayers.Location2D var11;
         if(var4) {
            ScoreboardTeamBase var12 = var10 instanceof EntityHuman?((EntityHuman)var10).getScoreboardTeam():null;
            if(!var8.containsKey(var12)) {
               var8.put(var12, var3[var7++]);
            }

            var11 = (CommandSpreadPlayers.Location2D)var8.get(var12);
         } else {
            var11 = var3[var7++];
         }

         var10.enderTeleportTo((double)((float)MathHelper.floor(var11.a) + 0.5F), (double)var11.a(var2), (double)MathHelper.floor(var11.b) + 0.5D);
         double var17 = Double.MAX_VALUE;

         for(int var14 = 0; var14 < var3.length; ++var14) {
            if(var11 != var3[var14]) {
               double var15 = var11.a(var3[var14]);
               var17 = Math.min(var15, var17);
            }
         }

         var5 += var17;
      }

      var5 /= (double)var1.size();
      return var5;
   }

   private CommandSpreadPlayers.Location2D[] a(Random var1, int var2, double var3, double var5, double var7, double var9) {
      CommandSpreadPlayers.Location2D[] var11 = new CommandSpreadPlayers.Location2D[var2];

      for(int var12 = 0; var12 < var11.length; ++var12) {
         CommandSpreadPlayers.Location2D var13 = new CommandSpreadPlayers.Location2D();
         var13.a(var1, var3, var5, var7, var9);
         var11[var12] = var13;
      }

      return var11;
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length >= 1 && var2.length <= 2?b(var2, 0, var3):null;
   }

   static class Location2D {
      double a;
      double b;

      Location2D() {
      }

      Location2D(double var1, double var3) {
         this.a = var1;
         this.b = var3;
      }

      double a(CommandSpreadPlayers.Location2D var1) {
         double var2 = this.a - var1.a;
         double var4 = this.b - var1.b;
         return Math.sqrt(var2 * var2 + var4 * var4);
      }

      void a() {
         double var1 = (double)this.b();
         this.a /= var1;
         this.b /= var1;
      }

      float b() {
         return MathHelper.sqrt(this.a * this.a + this.b * this.b);
      }

      public void b(CommandSpreadPlayers.Location2D var1) {
         this.a -= var1.a;
         this.b -= var1.b;
      }

      public boolean a(double var1, double var3, double var5, double var7) {
         boolean var9 = false;
         if(this.a < var1) {
            this.a = var1;
            var9 = true;
         } else if(this.a > var5) {
            this.a = var5;
            var9 = true;
         }

         if(this.b < var3) {
            this.b = var3;
            var9 = true;
         } else if(this.b > var7) {
            this.b = var7;
            var9 = true;
         }

         return var9;
      }

      public int a(World var1) {
         BlockPosition var2 = new BlockPosition(this.a, 256.0D, this.b);

         do {
            if(var2.getY() <= 0) {
               return 257;
            }

            var2 = var2.down();
         } while(var1.getType(var2).getBlock().getMaterial() == Material.AIR);

         return var2.getY() + 1;
      }

      public boolean b(World var1) {
         BlockPosition var2 = new BlockPosition(this.a, 256.0D, this.b);

         Material var3;
         do {
            if(var2.getY() <= 0) {
               return false;
            }

            var2 = var2.down();
            var3 = var1.getType(var2).getBlock().getMaterial();
         } while(var3 == Material.AIR);

         return !var3.isLiquid() && var3 != Material.FIRE;
      }

      public void a(Random var1, double var2, double var4, double var6, double var8) {
         this.a = MathHelper.a(var1, var2, var6);
         this.b = MathHelper.a(var1, var4, var8);
      }
   }
}
