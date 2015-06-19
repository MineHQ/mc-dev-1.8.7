package com.mojang.authlib.exceptions;

import com.mojang.authlib.exceptions.AuthenticationException;

public class InvalidCredentialsException extends AuthenticationException {
   public InvalidCredentialsException() {
   }

   public InvalidCredentialsException(String var1) {
      super(var1);
   }

   public InvalidCredentialsException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public InvalidCredentialsException(Throwable var1) {
      super(var1);
   }
}
