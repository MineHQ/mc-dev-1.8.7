package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeMapBase;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.IAttribute;

public class AttributeModifiable implements AttributeInstance {
   private final AttributeMapBase a;
   private final IAttribute b;
   private final Map<Integer, Set<AttributeModifier>> c = Maps.newHashMap();
   private final Map<String, Set<AttributeModifier>> d = Maps.newHashMap();
   private final Map<UUID, AttributeModifier> e = Maps.newHashMap();
   private double f;
   private boolean g = true;
   private double h;

   public AttributeModifiable(AttributeMapBase var1, IAttribute var2) {
      this.a = var1;
      this.b = var2;
      this.f = var2.b();

      for(int var3 = 0; var3 < 3; ++var3) {
         this.c.put(Integer.valueOf(var3), Sets.newHashSet());
      }

   }

   public IAttribute getAttribute() {
      return this.b;
   }

   public double b() {
      return this.f;
   }

   public void setValue(double var1) {
      if(var1 != this.b()) {
         this.f = var1;
         this.f();
      }
   }

   public Collection<AttributeModifier> a(int var1) {
      return (Collection)this.c.get(Integer.valueOf(var1));
   }

   public Collection<AttributeModifier> c() {
      HashSet var1 = Sets.newHashSet();

      for(int var2 = 0; var2 < 3; ++var2) {
         var1.addAll(this.a(var2));
      }

      return var1;
   }

   public AttributeModifier a(UUID var1) {
      return (AttributeModifier)this.e.get(var1);
   }

   public boolean a(AttributeModifier var1) {
      return this.e.get(var1.a()) != null;
   }

   public void b(AttributeModifier var1) {
      if(this.a(var1.a()) != null) {
         throw new IllegalArgumentException("Modifier is already applied on this attribute!");
      } else {
         Object var2 = (Set)this.d.get(var1.b());
         if(var2 == null) {
            var2 = Sets.newHashSet();
            this.d.put(var1.b(), var2);
         }

         ((Set)this.c.get(Integer.valueOf(var1.c()))).add(var1);
         ((Set)var2).add(var1);
         this.e.put(var1.a(), var1);
         this.f();
      }
   }

   protected void f() {
      this.g = true;
      this.a.a((AttributeInstance)this);
   }

   public void c(AttributeModifier var1) {
      for(int var2 = 0; var2 < 3; ++var2) {
         Set var3 = (Set)this.c.get(Integer.valueOf(var2));
         var3.remove(var1);
      }

      Set var4 = (Set)this.d.get(var1.b());
      if(var4 != null) {
         var4.remove(var1);
         if(var4.isEmpty()) {
            this.d.remove(var1.b());
         }
      }

      this.e.remove(var1.a());
      this.f();
   }

   public double getValue() {
      if(this.g) {
         this.h = this.g();
         this.g = false;
      }

      return this.h;
   }

   private double g() {
      double var1 = this.b();

      AttributeModifier var4;
      for(Iterator var3 = this.b(0).iterator(); var3.hasNext(); var1 += var4.d()) {
         var4 = (AttributeModifier)var3.next();
      }

      double var7 = var1;

      Iterator var5;
      AttributeModifier var6;
      for(var5 = this.b(1).iterator(); var5.hasNext(); var7 += var1 * var6.d()) {
         var6 = (AttributeModifier)var5.next();
      }

      for(var5 = this.b(2).iterator(); var5.hasNext(); var7 *= 1.0D + var6.d()) {
         var6 = (AttributeModifier)var5.next();
      }

      return this.b.a(var7);
   }

   private Collection<AttributeModifier> b(int var1) {
      HashSet var2 = Sets.newHashSet((Iterable)this.a(var1));

      for(IAttribute var3 = this.b.d(); var3 != null; var3 = var3.d()) {
         AttributeInstance var4 = this.a.a(var3);
         if(var4 != null) {
            var2.addAll(var4.a(var1));
         }
      }

      return var2;
   }
}
