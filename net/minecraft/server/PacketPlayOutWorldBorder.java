package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;
import net.minecraft.server.WorldBorder;

public class PacketPlayOutWorldBorder implements Packet<PacketListenerPlayOut> {
   private PacketPlayOutWorldBorder.EnumWorldBorderAction a;
   private int b;
   private double c;
   private double d;
   private double e;
   private double f;
   private long g;
   private int h;
   private int i;

   public PacketPlayOutWorldBorder() {
   }

   public PacketPlayOutWorldBorder(WorldBorder var1, PacketPlayOutWorldBorder.EnumWorldBorderAction var2) {
      this.a = var2;
      this.c = var1.getCenterX();
      this.d = var1.getCenterZ();
      this.f = var1.getSize();
      this.e = var1.j();
      this.g = var1.i();
      this.b = var1.l();
      this.i = var1.getWarningDistance();
      this.h = var1.getWarningTime();
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = (PacketPlayOutWorldBorder.EnumWorldBorderAction)var1.a(PacketPlayOutWorldBorder.EnumWorldBorderAction.class);
      switch(PacketPlayOutWorldBorder.SyntheticClass_1.a[this.a.ordinal()]) {
      case 1:
         this.e = var1.readDouble();
         break;
      case 2:
         this.f = var1.readDouble();
         this.e = var1.readDouble();
         this.g = var1.f();
         break;
      case 3:
         this.c = var1.readDouble();
         this.d = var1.readDouble();
         break;
      case 4:
         this.i = var1.e();
         break;
      case 5:
         this.h = var1.e();
         break;
      case 6:
         this.c = var1.readDouble();
         this.d = var1.readDouble();
         this.f = var1.readDouble();
         this.e = var1.readDouble();
         this.g = var1.f();
         this.b = var1.e();
         this.i = var1.e();
         this.h = var1.e();
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.a);
      switch(PacketPlayOutWorldBorder.SyntheticClass_1.a[this.a.ordinal()]) {
      case 1:
         var1.writeDouble(this.e);
         break;
      case 2:
         var1.writeDouble(this.f);
         var1.writeDouble(this.e);
         var1.b(this.g);
         break;
      case 3:
         var1.writeDouble(this.c);
         var1.writeDouble(this.d);
         break;
      case 4:
         var1.b(this.i);
         break;
      case 5:
         var1.b(this.h);
         break;
      case 6:
         var1.writeDouble(this.c);
         var1.writeDouble(this.d);
         var1.writeDouble(this.f);
         var1.writeDouble(this.e);
         var1.b(this.g);
         var1.b(this.b);
         var1.b(this.i);
         var1.b(this.h);
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
      static final int[] a = new int[PacketPlayOutWorldBorder.EnumWorldBorderAction.values().length];

      static {
         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumWorldBorderAction {
      SET_SIZE,
      LERP_SIZE,
      SET_CENTER,
      INITIALIZE,
      SET_WARNING_TIME,
      SET_WARNING_BLOCKS;

      private EnumWorldBorderAction() {
      }
   }
}
