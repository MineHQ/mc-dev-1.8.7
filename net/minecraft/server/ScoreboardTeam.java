package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.Scoreboard;
import net.minecraft.server.ScoreboardTeamBase;

public class ScoreboardTeam extends ScoreboardTeamBase {
   private final Scoreboard a;
   private final String b;
   private final Set<String> c = Sets.newHashSet();
   private String d;
   private String e = "";
   private String f = "";
   private boolean g = true;
   private boolean h = true;
   private ScoreboardTeamBase.EnumNameTagVisibility i;
   private ScoreboardTeamBase.EnumNameTagVisibility j;
   private EnumChatFormat k;

   public ScoreboardTeam(Scoreboard var1, String var2) {
      this.i = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
      this.j = ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS;
      this.k = EnumChatFormat.RESET;
      this.a = var1;
      this.b = var2;
      this.d = var2;
   }

   public String getName() {
      return this.b;
   }

   public String getDisplayName() {
      return this.d;
   }

   public void setDisplayName(String var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Name cannot be null");
      } else {
         this.d = var1;
         this.a.handleTeamChanged(this);
      }
   }

   public Collection<String> getPlayerNameSet() {
      return this.c;
   }

   public String getPrefix() {
      return this.e;
   }

   public void setPrefix(String var1) {
      if(var1 == null) {
         throw new IllegalArgumentException("Prefix cannot be null");
      } else {
         this.e = var1;
         this.a.handleTeamChanged(this);
      }
   }

   public String getSuffix() {
      return this.f;
   }

   public void setSuffix(String var1) {
      this.f = var1;
      this.a.handleTeamChanged(this);
   }

   public String getFormattedName(String var1) {
      return this.getPrefix() + var1 + this.getSuffix();
   }

   public static String getPlayerDisplayName(ScoreboardTeamBase var0, String var1) {
      return var0 == null?var1:var0.getFormattedName(var1);
   }

   public boolean allowFriendlyFire() {
      return this.g;
   }

   public void setAllowFriendlyFire(boolean var1) {
      this.g = var1;
      this.a.handleTeamChanged(this);
   }

   public boolean canSeeFriendlyInvisibles() {
      return this.h;
   }

   public void setCanSeeFriendlyInvisibles(boolean var1) {
      this.h = var1;
      this.a.handleTeamChanged(this);
   }

   public ScoreboardTeamBase.EnumNameTagVisibility getNameTagVisibility() {
      return this.i;
   }

   public ScoreboardTeamBase.EnumNameTagVisibility j() {
      return this.j;
   }

   public void setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility var1) {
      this.i = var1;
      this.a.handleTeamChanged(this);
   }

   public void b(ScoreboardTeamBase.EnumNameTagVisibility var1) {
      this.j = var1;
      this.a.handleTeamChanged(this);
   }

   public int packOptionData() {
      int var1 = 0;
      if(this.allowFriendlyFire()) {
         var1 |= 1;
      }

      if(this.canSeeFriendlyInvisibles()) {
         var1 |= 2;
      }

      return var1;
   }

   public void a(EnumChatFormat var1) {
      this.k = var1;
   }

   public EnumChatFormat l() {
      return this.k;
   }
}
