package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.ScoreboardObjective;

public class PacketPlayOutScoreboardObjective implements Packet<PacketListenerPlayOut> {
   private String a;
   private String b;
   private IScoreboardCriteria.EnumScoreboardHealthDisplay c;
   private int d;

   public PacketPlayOutScoreboardObjective() {
   }

   public PacketPlayOutScoreboardObjective(ScoreboardObjective var1, int var2) {
      this.a = var1.getName();
      this.b = var1.getDisplayName();
      this.c = var1.getCriteria().c();
      this.d = var2;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.c(16);
      this.d = var1.readByte();
      if(this.d == 0 || this.d == 2) {
         this.b = var1.c(32);
         this.c = IScoreboardCriteria.EnumScoreboardHealthDisplay.a(var1.c(16));
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a(this.a);
      var1.writeByte(this.d);
      if(this.d == 0 || this.d == 2) {
         var1.a(this.b);
         var1.a(this.c.a());
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
}
