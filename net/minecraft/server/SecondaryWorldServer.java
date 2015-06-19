package net.minecraft.server;

import net.minecraft.server.IDataManager;
import net.minecraft.server.IWorldBorderListener;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PersistentVillage;
import net.minecraft.server.SecondaryWorldData;
import net.minecraft.server.World;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldServer;

public class SecondaryWorldServer extends WorldServer {
   private WorldServer a;

   public SecondaryWorldServer(MinecraftServer var1, IDataManager var2, int var3, WorldServer var4, MethodProfiler var5) {
      super(var1, var2, new SecondaryWorldData(var4.getWorldData()), var3, var5);
      this.a = var4;
      var4.getWorldBorder().a(new IWorldBorderListener() {
         public void a(WorldBorder var1, double var2) {
            SecondaryWorldServer.this.getWorldBorder().setSize(var2);
         }

         public void a(WorldBorder var1, double var2, double var4, long var6) {
            SecondaryWorldServer.this.getWorldBorder().transitionSizeBetween(var2, var4, var6);
         }

         public void a(WorldBorder var1, double var2, double var4) {
            SecondaryWorldServer.this.getWorldBorder().setCenter(var2, var4);
         }

         public void a(WorldBorder var1, int var2) {
            SecondaryWorldServer.this.getWorldBorder().setWarningTime(var2);
         }

         public void b(WorldBorder var1, int var2) {
            SecondaryWorldServer.this.getWorldBorder().setWarningDistance(var2);
         }

         public void b(WorldBorder var1, double var2) {
            SecondaryWorldServer.this.getWorldBorder().setDamageAmount(var2);
         }

         public void c(WorldBorder var1, double var2) {
            SecondaryWorldServer.this.getWorldBorder().setDamageBuffer(var2);
         }
      });
   }

   protected void a() {
   }

   public World b() {
      this.worldMaps = this.a.T();
      this.scoreboard = this.a.getScoreboard();
      String var1 = PersistentVillage.a(this.worldProvider);
      PersistentVillage var2 = (PersistentVillage)this.worldMaps.get(PersistentVillage.class, var1);
      if(var2 == null) {
         this.villages = new PersistentVillage(this);
         this.worldMaps.a(var1, this.villages);
      } else {
         this.villages = var2;
         this.villages.a((World)this);
      }

      return this;
   }
}
