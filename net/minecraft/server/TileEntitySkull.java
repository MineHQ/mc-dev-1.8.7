package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.TileEntity;
import net.minecraft.server.UtilColor;

public class TileEntitySkull extends TileEntity {
   private int a;
   private int rotation;
   private GameProfile g = null;

   public TileEntitySkull() {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setByte("SkullType", (byte)(this.a & 255));
      var1.setByte("Rot", (byte)(this.rotation & 255));
      if(this.g != null) {
         NBTTagCompound var2 = new NBTTagCompound();
         GameProfileSerializer.serialize(var2, this.g);
         var1.set("Owner", var2);
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = var1.getByte("SkullType");
      this.rotation = var1.getByte("Rot");
      if(this.a == 3) {
         if(var1.hasKeyOfType("Owner", 10)) {
            this.g = GameProfileSerializer.deserialize(var1.getCompound("Owner"));
         } else if(var1.hasKeyOfType("ExtraType", 8)) {
            String var2 = var1.getString("ExtraType");
            if(!UtilColor.b(var2)) {
               this.g = new GameProfile((UUID)null, var2);
               this.e();
            }
         }
      }

   }

   public GameProfile getGameProfile() {
      return this.g;
   }

   public Packet getUpdatePacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.b(var1);
      return new PacketPlayOutTileEntityData(this.position, 4, var1);
   }

   public void setSkullType(int var1) {
      this.a = var1;
      this.g = null;
   }

   public void setGameProfile(GameProfile var1) {
      this.a = 3;
      this.g = var1;
      this.e();
   }

   private void e() {
      this.g = b(this.g);
      this.update();
   }

   public static GameProfile b(GameProfile var0) {
      if(var0 != null && !UtilColor.b(var0.getName())) {
         if(var0.isComplete() && var0.getProperties().containsKey("textures")) {
            return var0;
         } else if(MinecraftServer.getServer() == null) {
            return var0;
         } else {
            GameProfile var1 = MinecraftServer.getServer().getUserCache().getProfile(var0.getName());
            if(var1 == null) {
               return var0;
            } else {
               Property var2 = (Property)Iterables.getFirst(var1.getProperties().get("textures"), (Object)null);
               if(var2 == null) {
                  var1 = MinecraftServer.getServer().aD().fillProfileProperties(var1, true);
               }

               return var1;
            }
         }
      } else {
         return var0;
      }
   }

   public int getSkullType() {
      return this.a;
   }

   public void setRotation(int var1) {
      this.rotation = var1;
   }
}
