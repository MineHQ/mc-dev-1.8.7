package net.minecraft.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.AttributeInstance;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.IAttribute;
import net.minecraft.server.InsensitiveStringMap;

public abstract class AttributeMapBase {
   protected final Map<IAttribute, AttributeInstance> a = Maps.newHashMap();
   protected final Map<String, AttributeInstance> b = new InsensitiveStringMap();
   protected final Multimap<IAttribute, IAttribute> c = HashMultimap.create();

   public AttributeMapBase() {
   }

   public AttributeInstance a(IAttribute var1) {
      return (AttributeInstance)this.a.get(var1);
   }

   public AttributeInstance a(String var1) {
      return (AttributeInstance)this.b.get(var1);
   }

   public AttributeInstance b(IAttribute var1) {
      if(this.b.containsKey(var1.getName())) {
         throw new IllegalArgumentException("Attribute is already registered!");
      } else {
         AttributeInstance var2 = this.c(var1);
         this.b.put(var1.getName(), var2);
         this.a.put(var1, var2);

         for(IAttribute var3 = var1.d(); var3 != null; var3 = var3.d()) {
            this.c.put(var3, var1);
         }

         return var2;
      }
   }

   protected abstract AttributeInstance c(IAttribute var1);

   public Collection<AttributeInstance> a() {
      return this.b.values();
   }

   public void a(AttributeInstance var1) {
   }

   public void a(Multimap<String, AttributeModifier> var1) {
      Iterator var2 = var1.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         AttributeInstance var4 = this.a((String)var3.getKey());
         if(var4 != null) {
            var4.c((AttributeModifier)var3.getValue());
         }
      }

   }

   public void b(Multimap<String, AttributeModifier> var1) {
      Iterator var2 = var1.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         AttributeInstance var4 = this.a((String)var3.getKey());
         if(var4 != null) {
            var4.c((AttributeModifier)var3.getValue());
            var4.b((AttributeModifier)var3.getValue());
         }
      }

   }
}
