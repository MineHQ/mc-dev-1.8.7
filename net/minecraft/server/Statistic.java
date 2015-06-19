package net.minecraft.server;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.Counter;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IJsonStatistic;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.ScoreboardStatisticCriteria;
import net.minecraft.server.StatisticList;

public class Statistic {
   public final String name;
   private final IChatBaseComponent a;
   public boolean f;
   private final Counter b;
   private final IScoreboardCriteria c;
   private Class<? extends IJsonStatistic> d;
   private static NumberFormat k;
   public static Counter g;
   private static DecimalFormat l;
   public static Counter h;
   public static Counter i;
   public static Counter j;

   public Statistic(String var1, IChatBaseComponent var2, Counter var3) {
      this.name = var1;
      this.a = var2;
      this.b = var3;
      this.c = new ScoreboardStatisticCriteria(this);
      IScoreboardCriteria.criteria.put(this.c.getName(), this.c);
   }

   public Statistic(String var1, IChatBaseComponent var2) {
      this(var1, var2, g);
   }

   public Statistic i() {
      this.f = true;
      return this;
   }

   public Statistic h() {
      if(StatisticList.a.containsKey(this.name)) {
         throw new RuntimeException("Duplicate stat id: \"" + ((Statistic)StatisticList.a.get(this.name)).a + "\" and \"" + this.a + "\" at id " + this.name);
      } else {
         StatisticList.stats.add(this);
         StatisticList.a.put(this.name, this);
         return this;
      }
   }

   public boolean d() {
      return false;
   }

   public IChatBaseComponent e() {
      IChatBaseComponent var1 = this.a.f();
      var1.getChatModifier().setColor(EnumChatFormat.GRAY);
      var1.getChatModifier().setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ACHIEVEMENT, new ChatComponentText(this.name)));
      return var1;
   }

   public IChatBaseComponent j() {
      IChatBaseComponent var1 = this.e();
      IChatBaseComponent var2 = (new ChatComponentText("[")).addSibling(var1).a("]");
      var2.setChatModifier(var1.getChatModifier());
      return var2;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(var1 != null && this.getClass() == var1.getClass()) {
         Statistic var2 = (Statistic)var1;
         return this.name.equals(var2.name);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return "Stat{id=" + this.name + ", nameId=" + this.a + ", awardLocallyOnly=" + this.f + ", formatter=" + this.b + ", objectiveCriteria=" + this.c + '}';
   }

   public IScoreboardCriteria k() {
      return this.c;
   }

   public Class<? extends IJsonStatistic> l() {
      return this.d;
   }

   public Statistic b(Class<? extends IJsonStatistic> var1) {
      this.d = var1;
      return this;
   }

   static {
      k = NumberFormat.getIntegerInstance(Locale.US);
      g = new Counter() {
      };
      l = new DecimalFormat("########0.00");
      h = new Counter() {
      };
      i = new Counter() {
      };
      j = new Counter() {
      };
   }
}
