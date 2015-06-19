package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
   LEGACY("legacy"),
   MOJANG("mojang");

   private static final Map<String, UserType> BY_NAME;
   private final String name;

   private UserType(String var3) {
      this.name = var3;
   }

   public static UserType byName(String var0) {
      return (UserType)BY_NAME.get(var0.toLowerCase());
   }

   public String getName() {
      return this.name;
   }

   static {
      BY_NAME = new HashMap();
      UserType[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         UserType var3 = var0[var2];
         BY_NAME.put(var3.name, var3);
      }

   }
}
