package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;

public class PacketPlayOutScoreboardScore implements Packet<PacketListenerPlayOut> {
   private String a = "";
   private String b = "";
   private int c;
   private PacketPlayOutScoreboardScore.EnumScoreboardAction d;

   public PacketPlayOutScoreboardScore() {
   }

   public PacketPlayOutScoreboardScore(ScoreboardScore var1) {
      this.a = var1.getPlayerName();
      this.b = var1.getObjective().getName();
      this.c = var1.getScore();
      this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE;
   }

   public PacketPlayOutScoreboardScore(String var1) {
      this.a = var1;
      this.b = "";
      this.c = 0;
      this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE;
   }

   public PacketPlayOutScoreboardScore(String var1, ScoreboardObjective var2) {
      this.a = var1;
      this.b = var2.getName();
      this.c = 0;
      this.d = PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(40);
      this.d = (PacketPlayOutScoreboardScore.EnumScoreboardAction)var1.a(PacketPlayOutScoreboardScore.EnumScoreboardAction.class);
      this.b = var1.c(16);
      if(this.d != PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE) {
         this.c = var1.e();
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.a((Enum)this.d);
      var1.a(this.b);
      if(this.d != PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE) {
         var1.b(this.c);
      }

   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }

   public static enum EnumScoreboardAction {
      CHANGE,
      REMOVE;

      private EnumScoreboardAction() {
      }
   }
}
