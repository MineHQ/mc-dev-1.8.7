package net.minecraft.server;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.IAttribute;

public interface AttributeInstance {
   IAttribute getAttribute();

   double b();

   void setValue(double var1);

   Collection<AttributeModifier> a(int var1);

   Collection<AttributeModifier> c();

   boolean a(AttributeModifier var1);

   AttributeModifier a(UUID var1);

   void b(AttributeModifier var1);

   void c(AttributeModifier var1);

   double getValue();
}
