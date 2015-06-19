package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutTitle implements Packet<PacketListenerPlayOut> {
   private PacketPlayOutTitle.EnumTitleAction a;
   private IChatBaseComponent b;
   private int c;
   private int d;
   private int e;

   public PacketPlayOutTitle() {
   }

   public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction var1, IChatBaseComponent var2) {
      this(var1, var2, -1, -1, -1);
   }

   public PacketPlayOutTitle(int var1, int var2, int var3) {
      this(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent)null, var1, var2, var3);
   }

   public PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction var1, IChatBaseComponent var2, int var3, int var4, int var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = (PacketPlayOutTitle.EnumTitleAction)var1.a(PacketPlayOutTitle.EnumTitleAction.class);
      if(this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE) {
         this.b = var1.d();
      }

      if(this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
         this.c = var1.readInt();
         this.d = var1.readInt();
         this.e = var1.readInt();
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.a((Enum)this.a);
      if(this.a == PacketPlayOutTitle.EnumTitleAction.TITLE || this.a == PacketPlayOutTitle.EnumTitleAction.SUBTITLE) {
         var1.a(this.b);
      }

      if(this.a == PacketPlayOutTitle.EnumTitleAction.TIMES) {
         var1.writeInt(this.c);
         var1.writeInt(this.d);
         var1.writeInt(this.e);
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

   public static enum EnumTitleAction {
      TITLE,
      SUBTITLE,
      TIMES,
      CLEAR,
      RESET;

      private EnumTitleAction() {
      }

      public static PacketPlayOutTitle.EnumTitleAction a(String var0) {
         PacketPlayOutTitle.EnumTitleAction[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PacketPlayOutTitle.EnumTitleAction var4 = var1[var3];
            if(var4.name().equalsIgnoreCase(var0)) {
               return var4;
            }
         }

         return TITLE;
      }

      public static String[] a() {
         String[] var0 = new String[values().length];
         int var1 = 0;
         PacketPlayOutTitle.EnumTitleAction[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            PacketPlayOutTitle.EnumTitleAction var5 = var2[var4];
            var0[var1++] = var5.name().toLowerCase();
         }

         return var0;
      }
   }
}
