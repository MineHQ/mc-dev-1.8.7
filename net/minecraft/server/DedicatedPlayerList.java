package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import net.minecraft.server.DedicatedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DedicatedPlayerList extends PlayerList {
   private static final Logger f = LogManager.getLogger();

   public DedicatedPlayerList(DedicatedServer var1) {
      super(var1);
      this.a(var1.a("view-distance", 10));
      this.maxPlayers = var1.a("max-players", 20);
      this.setHasWhitelist(var1.a("white-list", false));
      if(!var1.T()) {
         this.getProfileBans().a(true);
         this.getIPBans().a(true);
      }

      this.z();
      this.x();
      this.y();
      this.w();
      this.A();
      this.C();
      this.B();
      if(!this.getWhitelist().c().exists()) {
         this.D();
      }

   }

   public void setHasWhitelist(boolean var1) {
      super.setHasWhitelist(var1);
      this.getServer().a("white-list", (Object)Boolean.valueOf(var1));
      this.getServer().a();
   }

   public void addOp(GameProfile var1) {
      super.addOp(var1);
      this.B();
   }

   public void removeOp(GameProfile var1) {
      super.removeOp(var1);
      this.B();
   }

   public void removeWhitelist(GameProfile var1) {
      super.removeWhitelist(var1);
      this.D();
   }

   public void addWhitelist(GameProfile var1) {
      super.addWhitelist(var1);
      this.D();
   }

   public void reloadWhitelist() {
      this.C();
   }

   private void w() {
      try {
         this.getIPBans().save();
      } catch (IOException var2) {
         f.warn((String)"Failed to save ip banlist: ", (Throwable)var2);
      }

   }

   private void x() {
      try {
         this.getProfileBans().save();
      } catch (IOException var2) {
         f.warn((String)"Failed to save user banlist: ", (Throwable)var2);
      }

   }

   private void y() {
      try {
         this.getIPBans().load();
      } catch (IOException var2) {
         f.warn((String)"Failed to load ip banlist: ", (Throwable)var2);
      }

   }

   private void z() {
      try {
         this.getProfileBans().load();
      } catch (IOException var2) {
         f.warn((String)"Failed to load user banlist: ", (Throwable)var2);
      }

   }

   private void A() {
      try {
         this.getOPs().load();
      } catch (Exception var2) {
         f.warn((String)"Failed to load operators list: ", (Throwable)var2);
      }

   }

   private void B() {
      try {
         this.getOPs().save();
      } catch (Exception var2) {
         f.warn((String)"Failed to save operators list: ", (Throwable)var2);
      }

   }

   private void C() {
      try {
         this.getWhitelist().load();
      } catch (Exception var2) {
         f.warn((String)"Failed to load white-list: ", (Throwable)var2);
      }

   }

   private void D() {
      try {
         this.getWhitelist().save();
      } catch (Exception var2) {
         f.warn((String)"Failed to save white-list: ", (Throwable)var2);
      }

   }

   public boolean isWhitelisted(GameProfile var1) {
      return !this.getHasWhitelist() || this.isOp(var1) || this.getWhitelist().isWhitelisted(var1);
   }

   public DedicatedServer getServer() {
      return (DedicatedServer)super.getServer();
   }

   public boolean f(GameProfile var1) {
      return this.getOPs().b(var1);
   }

   // $FF: synthetic method
   public MinecraftServer getServer() {
      return this.getServer();
   }
}
