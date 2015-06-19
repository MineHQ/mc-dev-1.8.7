package net.minecraft.server;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.WorldSettings;

public class PacketPlayOutPlayerInfo implements Packet<PacketListenerPlayOut> {
   private PacketPlayOutPlayerInfo.EnumPlayerInfoAction a;
   private final List<PacketPlayOutPlayerInfo.PlayerInfoData> b = Lists.newArrayList();

   public PacketPlayOutPlayerInfo() {
   }

   public PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction var1, EntityPlayer... var2) {
      this.a = var1;
      EntityPlayer[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntityPlayer var6 = var3[var5];
         this.b.add(new PacketPlayOutPlayerInfo.PlayerInfoData(var6.getProfile(), var6.ping, var6.playerInteractManager.getGameMode(), var6.getPlayerListName()));
      }

   }

   public PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction var1, Iterable<EntityPlayer> var2) {
      this.a = var1;
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         EntityPlayer var4 = (EntityPlayer)var3.next();
         this.b.add(new PacketPlayOutPlayerInfo.PlayerInfoData(var4.getProfile(), var4.ping, var4.playerInteractManager.getGameMode(), var4.getPlayerListName()));
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = (PacketPlayOutPlayerInfo.EnumPlayerInfoAction)var1.a(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class);
      int var2 = var1.e();

      for(int var3 = 0; var3 < var2; ++var3) {
         GameProfile var4 = null;
         int var5 = 0;
         WorldSettings.EnumGamemode var6 = null;
         IChatBaseComponent var7 = null;
         switch(PacketPlayOutPlayerInfo.SyntheticClass_1.a[this.a.ordinal()]) {
         case 1:
            var4 = new GameProfile(var1.g(), var1.c(16));
            int var8 = var1.e();
            int var9 = 0;

            for(; var9 < var8; ++var9) {
               String var10 = var1.c(32767);
               String var11 = var1.c(32767);
               if(var1.readBoolean()) {
                  var4.getProperties().put(var10, new Property(var10, var11, var1.c(32767)));
               } else {
                  var4.getProperties().put(var10, new Property(var10, var11));
               }
            }

            var6 = WorldSettings.EnumGamemode.getById(var1.e());
            var5 = var1.e();
            if(var1.readBoolean()) {
               var7 = var1.d();
            }
            break;
         case 2:
            var4 = new GameProfile(var1.g(), (String)null);
            var6 = WorldSettings.EnumGamemode.getById(var1.e());
            break;
         case 3:
            var4 = new GameProfile(var1.g(), (String)null);
            var5 = var1.e();
            break;
         case 4:
            var4 = new GameProfile(var1.g(), (String)null);
            if(var1.readBoolean()) {
               var7 = var1.d();
            }
            break;
         case 5:
            var4 = new GameProfile(var1.g(), (String)null);
         }

         this.b.add(new PacketPlayOutPlayerInfo.PlayerInfoData(var4, var5, var6, var7));
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.a);
      var1.b(this.b.size());
      Iterator var2 = this.b.iterator();

      while(true) {
         while(var2.hasNext()) {
            PacketPlayOutPlayerInfo.PlayerInfoData var3 = (PacketPlayOutPlayerInfo.PlayerInfoData)var2.next();
            switch(PacketPlayOutPlayerInfo.SyntheticClass_1.a[this.a.ordinal()]) {
            case 1:
               var1.a(var3.a().getId());
               var1.a(var3.a().getName());
               var1.b(var3.a().getProperties().size());
               Iterator var4 = var3.a().getProperties().values().iterator();

               while(var4.hasNext()) {
                  Property var5 = (Property)var4.next();
                  var1.a(var5.getName());
                  var1.a(var5.getValue());
                  if(var5.hasSignature()) {
                     var1.writeBoolean(true);
                     var1.a(var5.getSignature());
                  } else {
                     var1.writeBoolean(false);
                  }
               }

               var1.b(var3.c().getId());
               var1.b(var3.b());
               if(var3.d() == null) {
                  var1.writeBoolean(false);
               } else {
                  var1.writeBoolean(true);
                  var1.a(var3.d());
               }
               break;
            case 2:
               var1.a(var3.a().getId());
               var1.b(var3.c().getId());
               break;
            case 3:
               var1.a(var3.a().getId());
               var1.b(var3.b());
               break;
            case 4:
               var1.a(var3.a().getId());
               if(var3.d() == null) {
                  var1.writeBoolean(false);
               } else {
                  var1.writeBoolean(true);
                  var1.a(var3.d());
               }
               break;
            case 5:
               var1.a(var3.a().getId());
            }
         }

         return;
      }
   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   public String toString() {
      return Objects.toStringHelper((Object)this).add("action", this.a).add("entries", this.b).toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.values().length];

      static {
         try {
            a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public class PlayerInfoData {
      private final int b;
      private final WorldSettings.EnumGamemode c;
      private final GameProfile d;
      private final IChatBaseComponent e;

      public PlayerInfoData(GameProfile var2, int var3, WorldSettings.EnumGamemode var4, IChatBaseComponent var5) {
         this.d = var2;
         this.b = var3;
         this.c = var4;
         this.e = var5;
      }

      public GameProfile a() {
         return this.d;
      }

      public int b() {
         return this.b;
      }

      public WorldSettings.EnumGamemode c() {
         return this.c;
      }

      public IChatBaseComponent d() {
         return this.e;
      }

      public String toString() {
         return Objects.toStringHelper((Object)this).add("latency", this.b).add("gameMode", this.c).add("profile", this.d).add("displayName", this.e == null?null:IChatBaseComponent.ChatSerializer.a(this.e)).toString();
      }
   }

   public static enum EnumPlayerInfoAction {
      ADD_PLAYER,
      UPDATE_GAME_MODE,
      UPDATE_LATENCY,
      UPDATE_DISPLAY_NAME,
      REMOVE_PLAYER;

      private EnumPlayerInfoAction() {
      }
   }
}
