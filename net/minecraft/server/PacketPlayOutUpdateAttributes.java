package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayOut;

public class PacketPlayOutUpdateAttributes implements Packet<PacketListenerPlayOut> {
   private int a;
   private final List<PacketPlayOutUpdateAttributes.AttributeSnapshot> b = Lists.newArrayList();

   public PacketPlayOutUpdateAttributes() {
   }

   public PacketPlayOutUpdateAttributes(int var1, Collection<AttributeInstance> var2) {
      this.a = var1;
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         AttributeInstance var4 = (AttributeInstance)var3.next();
         this.b.add(new PacketPlayOutUpdateAttributes.AttributeSnapshot(var4.getAttribute().getName(), var4.b(), var4.c()));
      }

   }

   public void a(PacketDataSerializer var1) throws IOException {
      this.a = var1.e();
      int var2 = var1.readInt();

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1.c(64);
         double var5 = var1.readDouble();
         ArrayList var7 = Lists.newArrayList();
         int var8 = var1.e();

         for(int var9 = 0; var9 < var8; ++var9) {
            UUID var10 = var1.g();
            var7.add(new AttributeModifier(var10, "Unknown synced attribute modifier", var1.readDouble(), var1.readByte()));
         }

         this.b.add(new PacketPlayOutUpdateAttributes.AttributeSnapshot(var4, var5, var7));
      }

   }

   public void b(PacketDataSerializer var1) throws IOException {
      var1.b(this.a);
      var1.writeInt(this.b.size());
      Iterator var2 = this.b.iterator();

      while(var2.hasNext()) {
         PacketPlayOutUpdateAttributes.AttributeSnapshot var3 = (PacketPlayOutUpdateAttributes.AttributeSnapshot)var2.next();
         var1.a(var3.a());
         var1.writeDouble(var3.b());
         var1.b(var3.c().size());
         Iterator var4 = var3.c().iterator();

         while(var4.hasNext()) {
            AttributeModifier var5 = (AttributeModifier)var4.next();
            var1.a(var5.a());
            var1.writeDouble(var5.d());
            var1.writeByte(var5.c());
         }
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

   public class AttributeSnapshot {
      private final String b;
      private final double c;
      private final Collection<AttributeModifier> d;

      public AttributeSnapshot(String var1, double var2, Collection<AttributeModifier> var3) {
         this.b = var2;
         this.c = var3;
         this.d = var5;
      }

      public String a() {
         return this.b;
      }

      public double b() {
         return this.c;
      }

      public Collection<AttributeModifier> c() {
         return this.d;
      }
   }
}
