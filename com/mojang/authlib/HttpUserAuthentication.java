package com.mojang.authlib;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.BaseUserAuthentication;
import com.mojang.authlib.HttpAuthenticationService;

public abstract class HttpUserAuthentication extends BaseUserAuthentication {
   protected HttpUserAuthentication(HttpAuthenticationService var1) {
      super(var1);
   }

   public HttpAuthenticationService getAuthenticationService() {
      return (HttpAuthenticationService)super.getAuthenticationService();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public AuthenticationService getAuthenticationService() {
      return this.getAuthenticationService();
   }
}
