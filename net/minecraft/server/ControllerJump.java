package net.minecraft.server;

import net.minecraft.server.EntityInsentient;

public class ControllerJump {
   private EntityInsentient b;
   protected boolean a;

   public ControllerJump(EntityInsentient var1) {
      this.b = var1;
   }

   public void a() {
      this.a = true;
   }

   public void b() {
      this.b.i(this.a);
      this.a = false;
   }
}
