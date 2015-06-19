package net.minecraft.server;

import net.minecraft.server.RegistrySimple;

public class RegistryDefault<K, V> extends RegistrySimple<K, V> {
   private final V a;

   public RegistryDefault(V var1) {
      this.a = var1;
   }

   public V get(K var1) {
      Object var2 = super.get(var1);
      return var2 == null?this.a:var2;
   }
}
