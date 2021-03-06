package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.Entity;
import net.minecraft.server.ExceptionEntityNotFound;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class CommandObjectiveExecutor {
   private static final int a = CommandObjectiveExecutor.EnumCommandResult.values().length;
   private static final String[] b;
   private String[] c;
   private String[] d;

   public CommandObjectiveExecutor() {
      this.c = b;
      this.d = b;
   }

   public void a(final ICommandListener var1, CommandObjectiveExecutor.EnumCommandResult var2, int var3) {
      String var4 = this.c[var2.a()];
      if(var4 != null) {
         ICommandListener var5 = new ICommandListener() {
            public String getName() {
               return var1.getName();
            }

            public IChatBaseComponent getScoreboardDisplayName() {
               return var1.getScoreboardDisplayName();
            }

            public void sendMessage(IChatBaseComponent var1x) {
               var1.sendMessage(var1x);
            }

            public boolean a(int var1x, String var2) {
               return true;
            }

            public BlockPosition getChunkCoordinates() {
               return var1.getChunkCoordinates();
            }

            public Vec3D d() {
               return var1.d();
            }

            public World getWorld() {
               return var1.getWorld();
            }

            public Entity f() {
               return var1.f();
            }

            public boolean getSendCommandFeedback() {
               return var1.getSendCommandFeedback();
            }

            public void a(CommandObjectiveExecutor.EnumCommandResult var1x, int var2) {
               var1.a(var1x, var2);
            }
         };

         String var6;
         try {
            var6 = CommandAbstract.e(var5, var4);
         } catch (ExceptionEntityNotFound var11) {
            return;
         }

         String var7 = this.d[var2.a()];
         if(var7 != null) {
            Scoreboard var8 = var1.getWorld().getScoreboard();
            ScoreboardObjective var9 = var8.getObjective(var7);
            if(var9 != null) {
               if(var8.b(var6, var9)) {
                  ScoreboardScore var10 = var8.getPlayerScoreForObjective(var6, var9);
                  var10.setScore(var3);
               }
            }
         }
      }
   }

   public void a(NBTTagCompound var1) {
      if(var1.hasKeyOfType("CommandStats", 10)) {
         NBTTagCompound var2 = var1.getCompound("CommandStats");
         CommandObjectiveExecutor.EnumCommandResult[] var3 = CommandObjectiveExecutor.EnumCommandResult.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CommandObjectiveExecutor.EnumCommandResult var6 = var3[var5];
            String var7 = var6.b() + "Name";
            String var8 = var6.b() + "Objective";
            if(var2.hasKeyOfType(var7, 8) && var2.hasKeyOfType(var8, 8)) {
               String var9 = var2.getString(var7);
               String var10 = var2.getString(var8);
               a(this, var6, var9, var10);
            }
         }

      }
   }

   public void b(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      CommandObjectiveExecutor.EnumCommandResult[] var3 = CommandObjectiveExecutor.EnumCommandResult.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CommandObjectiveExecutor.EnumCommandResult var6 = var3[var5];
         String var7 = this.c[var6.a()];
         String var8 = this.d[var6.a()];
         if(var7 != null && var8 != null) {
            var2.setString(var6.b() + "Name", var7);
            var2.setString(var6.b() + "Objective", var8);
         }
      }

      if(!var2.isEmpty()) {
         var1.set("CommandStats", var2);
      }

   }

   public static void a(CommandObjectiveExecutor var0, CommandObjectiveExecutor.EnumCommandResult var1, String var2, String var3) {
      if(var2 != null && var2.length() != 0 && var3 != null && var3.length() != 0) {
         if(var0.c == b || var0.d == b) {
            var0.c = new String[a];
            var0.d = new String[a];
         }

         var0.c[var1.a()] = var2;
         var0.d[var1.a()] = var3;
      } else {
         a(var0, var1);
      }
   }

   private static void a(CommandObjectiveExecutor var0, CommandObjectiveExecutor.EnumCommandResult var1) {
      if(var0.c != b && var0.d != b) {
         var0.c[var1.a()] = null;
         var0.d[var1.a()] = null;
         boolean var2 = true;
         CommandObjectiveExecutor.EnumCommandResult[] var3 = CommandObjectiveExecutor.EnumCommandResult.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CommandObjectiveExecutor.EnumCommandResult var6 = var3[var5];
            if(var0.c[var6.a()] != null && var0.d[var6.a()] != null) {
               var2 = false;
               break;
            }
         }

         if(var2) {
            var0.c = b;
            var0.d = b;
         }

      }
   }

   public void a(CommandObjectiveExecutor var1) {
      CommandObjectiveExecutor.EnumCommandResult[] var2 = CommandObjectiveExecutor.EnumCommandResult.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CommandObjectiveExecutor.EnumCommandResult var5 = var2[var4];
         a(this, var5, var1.c[var5.a()], var1.d[var5.a()]);
      }

   }

   static {
      b = new String[a];
   }

   public static enum EnumCommandResult {
      SUCCESS_COUNT(0, "SuccessCount"),
      AFFECTED_BLOCKS(1, "AffectedBlocks"),
      AFFECTED_ENTITIES(2, "AffectedEntities"),
      AFFECTED_ITEMS(3, "AffectedItems"),
      QUERY_RESULT(4, "QueryResult");

      final int f;
      final String g;

      private EnumCommandResult(int var3, String var4) {
         this.f = var3;
         this.g = var4;
      }

      public int a() {
         return this.f;
      }

      public String b() {
         return this.g;
      }

      public static String[] c() {
         String[] var0 = new String[values().length];
         int var1 = 0;
         CommandObjectiveExecutor.EnumCommandResult[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            CommandObjectiveExecutor.EnumCommandResult var5 = var2[var4];
            var0[var1++] = var5.b();
         }

         return var0;
      }

      public static CommandObjectiveExecutor.EnumCommandResult a(String var0) {
         CommandObjectiveExecutor.EnumCommandResult[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CommandObjectiveExecutor.EnumCommandResult var4 = var1[var3];
            if(var4.b().equals(var0)) {
               return var4;
            }
         }

         return null;
      }
   }
}
