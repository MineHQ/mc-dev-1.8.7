package com.mojang.authlib.minecraft;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.minecraft.MinecraftSessionService;

public abstract class BaseMinecraftSessionService implements MinecraftSessionService {
   private final AuthenticationService authenticationService;

   protected BaseMinecraftSessionService(AuthenticationService var1) {
      this.authenticationService = var1;
   }

   public AuthenticationService getAuthenticationService() {
      return this.authenticationService;
   }
}
