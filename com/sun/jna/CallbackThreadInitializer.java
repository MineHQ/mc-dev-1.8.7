package com.sun.jna;

import com.sun.jna.Callback;

public class CallbackThreadInitializer {
   private boolean daemon;
   private boolean detach;
   private String name;
   private ThreadGroup group;

   public CallbackThreadInitializer() {
      this(true);
   }

   public CallbackThreadInitializer(boolean var1) {
      this(var1, false);
   }

   public CallbackThreadInitializer(boolean var1, boolean var2) {
      this(var1, var2, (String)null);
   }

   public CallbackThreadInitializer(boolean var1, boolean var2, String var3) {
      this(var1, var2, var3, (ThreadGroup)null);
   }

   public CallbackThreadInitializer(boolean var1, boolean var2, String var3, ThreadGroup var4) {
      this.daemon = var1;
      this.detach = var2;
      this.name = var3;
      this.group = var4;
   }

   public String getName(Callback var1) {
      return this.name;
   }

   public ThreadGroup getThreadGroup(Callback var1) {
      return this.group;
   }

   public boolean isDaemon(Callback var1) {
      return this.daemon;
   }

   public boolean detach(Callback var1) {
      return this.detach;
   }
}
