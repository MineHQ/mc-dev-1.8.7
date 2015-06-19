package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.UtilColor;

public final class GameProfileSerializer {
   public static GameProfile deserialize(NBTTagCompound var0) {
      String var1 = null;
      String var2 = null;
      if(var0.hasKeyOfType("Name", 8)) {
         var1 = var0.getString("Name");
      }

      if(var0.hasKeyOfType("Id", 8)) {
         var2 = var0.getString("Id");
      }

      if(UtilColor.b(var1) && UtilColor.b(var2)) {
         return null;
      } else {
         UUID var3;
         try {
            var3 = UUID.fromString(var2);
         } catch (Throwable var12) {
            var3 = null;
         }

         GameProfile var4 = new GameProfile(var3, var1);
         if(var0.hasKeyOfType("Properties", 10)) {
            NBTTagCompound var5 = var0.getCompound("Properties");
            Iterator var6 = var5.c().iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               NBTTagList var8 = var5.getList(var7, 10);

               for(int var9 = 0; var9 < var8.size(); ++var9) {
                  NBTTagCompound var10 = var8.get(var9);
                  String var11 = var10.getString("Value");
                  if(var10.hasKeyOfType("Signature", 8)) {
                     var4.getProperties().put(var7, new Property(var7, var11, var10.getString("Signature")));
                  } else {
                     var4.getProperties().put(var7, new Property(var7, var11));
                  }
               }
            }
         }

         return var4;
      }
   }

   public static NBTTagCompound serialize(NBTTagCompound var0, GameProfile var1) {
      if(!UtilColor.b(var1.getName())) {
         var0.setString("Name", var1.getName());
      }

      if(var1.getId() != null) {
         var0.setString("Id", var1.getId().toString());
      }

      if(!var1.getProperties().isEmpty()) {
         NBTTagCompound var2 = new NBTTagCompound();
         Iterator var3 = var1.getProperties().keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            NBTTagList var5 = new NBTTagList();

            NBTTagCompound var8;
            for(Iterator var6 = var1.getProperties().get(var4).iterator(); var6.hasNext(); var5.add(var8)) {
               Property var7 = (Property)var6.next();
               var8 = new NBTTagCompound();
               var8.setString("Value", var7.getValue());
               if(var7.hasSignature()) {
                  var8.setString("Signature", var7.getSignature());
               }
            }

            var2.set(var4, var5);
         }

         var0.set("Properties", var2);
      }

      return var0;
   }

   public static boolean a(NBTBase var0, NBTBase var1, boolean var2) {
      if(var0 == var1) {
         return true;
      } else if(var0 == null) {
         return true;
      } else if(var1 == null) {
         return false;
      } else if(!var0.getClass().equals(var1.getClass())) {
         return false;
      } else if(var0 instanceof NBTTagCompound) {
         NBTTagCompound var9 = (NBTTagCompound)var0;
         NBTTagCompound var10 = (NBTTagCompound)var1;
         Iterator var11 = var9.c().iterator();

         String var12;
         NBTBase var13;
         do {
            if(!var11.hasNext()) {
               return true;
            }

            var12 = (String)var11.next();
            var13 = var9.get(var12);
         } while(a(var13, var10.get(var12), var2));

         return false;
      } else if(var0 instanceof NBTTagList && var2) {
         NBTTagList var3 = (NBTTagList)var0;
         NBTTagList var4 = (NBTTagList)var1;
         if(var3.size() == 0) {
            return var4.size() == 0;
         } else {
            for(int var5 = 0; var5 < var3.size(); ++var5) {
               NBTBase var6 = var3.g(var5);
               boolean var7 = false;

               for(int var8 = 0; var8 < var4.size(); ++var8) {
                  if(a(var6, var4.g(var8), var2)) {
                     var7 = true;
                     break;
                  }
               }

               if(!var7) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return var0.equals(var1);
      }
   }
}
