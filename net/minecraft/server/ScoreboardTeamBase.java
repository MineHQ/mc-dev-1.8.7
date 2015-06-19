package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public abstract class ScoreboardTeamBase {
   public ScoreboardTeamBase() {
   }

   public boolean isAlly(ScoreboardTeamBase var1) {
      return var1 == null?false:this == var1;
   }

   public abstract String getName();

   public abstract String getFormattedName(String var1);

   public abstract boolean allowFriendlyFire();

   public abstract Collection<String> getPlayerNameSet();

   public abstract ScoreboardTeamBase.EnumNameTagVisibility j();

   public static enum EnumNameTagVisibility {
      ALWAYS("always", 0),
      NEVER("never", 1),
      HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2),
      HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);

      private static Map<String, ScoreboardTeamBase.EnumNameTagVisibility> g;
      public final String e;
      public final int f;

      public static String[] a() {
         return (String[])g.keySet().toArray(new String[g.size()]);
      }

      public static ScoreboardTeamBase.EnumNameTagVisibility a(String var0) {
         return (ScoreboardTeamBase.EnumNameTagVisibility)g.get(var0);
      }

      private EnumNameTagVisibility(String var3, int var4) {
         this.e = var3;
         this.f = var4;
      }

      static {
         g = Maps.newHashMap();
         ScoreboardTeamBase.EnumNameTagVisibility[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ScoreboardTeamBase.EnumNameTagVisibility var3 = var0[var2];
            g.put(var3.e, var3);
         }

      }
   }
}
