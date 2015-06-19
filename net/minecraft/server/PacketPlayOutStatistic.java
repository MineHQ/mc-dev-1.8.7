package net.minecraft.server;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;

public class PacketPlayOutStatistic implements Packet<PacketListenerPlayOut> {
   private Map<Statistic, Integer> a;

   public PacketPlayOutStatistic() {
   }

   public PacketPlayOutStatistic(Map<Statistic, Integer> var1) {
      this.a = var1;
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   public void a(PacketDataSerializer var1) throws IOException {
      int var2 = var1.e();
      this.a = Maps.newHashMap();

      for(int var3 = 0; var3 < var2; ++var3) {
         Statistic var4 = StatisticList.getStatistic(var1.c(32767));
         int var5 = var1.e();
         if(var4 != null) {
            this.a.put(var4, Integer.valueOf(var5));
         }
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a.size());
      Iterator var2 = this.a.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.a(((Statistic)var3.getKey()).name);
         var1.b(((Integer)var3.getValue()).intValue());
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }
}
