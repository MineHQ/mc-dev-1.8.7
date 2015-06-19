package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.CombatTracker;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutCombatEvent implements Packet<PacketListenerPlayOut> {
   public PacketPlayOutCombatEvent.EnumCombatEventType a;
   public int b;
   public int c;
   public int d;
   public String e;

   public PacketPlayOutCombatEvent() {
   }

   public PacketPlayOutCombatEvent(CombatTracker var1, PacketPlayOutCombatEvent.EnumCombatEventType var2) {
      this.a = var2;
      EntityLiving var3 = var1.c();
      switch(PacketPlayOutCombatEvent.SyntheticClass_1.a[var2.ordinal()]) {
      case 1:
         this.d = var1.f();
         this.c = var3 == null?-1:var3.getId();
         break;
      case 2:
         this.b = var1.h().getId();
         this.c = var3 == null?-1:var3.getId();
         this.e = var1.b().c();
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = (PacketPlayOutCombatEvent.EnumCombatEventType)var1.a(PacketPlayOutCombatEvent.EnumCombatEventType.class);
      if(this.a == PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT) {
         this.d = var1.e();
         this.c = var1.readInt();
      } else if(this.a == PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED) {
         this.b = var1.e();
         this.c = var1.readInt();
         this.e = var1.c(32767);
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.a);
      if(this.a == PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT) {
         var1.b(this.d);
         var1.writeInt(this.c);
      } else if(this.a == PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED) {
         var1.b(this.b);
         var1.writeInt(this.c);
         var1.a(this.e);
      }

   }

   public void a(PacketListenerPlayOut var1) {
      var1.a(this);
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void a(PacketListener var1) {
      this.a((PacketListenerPlayOut)var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[PacketPlayOutCombatEvent.EnumCombatEventType.values().length];

      static {
         try {
            a[PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumCombatEventType {
      ENTER_COMBAT,
      END_COMBAT,
      ENTITY_DIED;

      private EnumCombatEventType() {
      }
   }
}
