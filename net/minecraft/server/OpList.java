package net.minecraft.server;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;
import net.minecraft.server.JsonList;
import net.minecraft.server.JsonListEntry;
import net.minecraft.server.OpListEntry;

public class OpList extends JsonList<GameProfile, OpListEntry> {
   public OpList(File var1) {
      super(var1);
   }

   protected JsonListEntry<GameProfile> a(JsonObject var1) {
      return new OpListEntry(var1);
   }

   public String[] getEntries() {
      String[] var1 = new String[this.e().size()];
      int var2 = 0;

      OpListEntry var4;
      for(Iterator var3 = this.e().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.getKey()).getName()) {
         var4 = (OpListEntry)var3.next();
      }

      return var1;
   }

   public boolean b(GameProfile var1) {
      OpListEntry var2 = (OpListEntry)this.get(var1);
      return var2 != null?var2.b():false;
   }

   protected String c(GameProfile var1) {
      return var1.getId().toString();
   }

   public GameProfile a(String var1) {
      Iterator var2 = this.e().values().iterator();

      OpListEntry var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (OpListEntry)var2.next();
      } while(!var1.equalsIgnoreCase(((GameProfile)var3.getKey()).getName()));

      return (GameProfile)var3.getKey();
   }

   // $FF: synthetic method
   protected String a(Object var1) {
      return this.c((GameProfile)var1);
   }
}
