package net.minecraft.server;

import net.minecraft.server.RegistryMaterials;
import org.apache.commons.lang3.Validate;

public class RegistryBlocks<K, V> extends RegistryMaterials<K, V> {
   private final K d;
   private V e;

   public RegistryBlocks(K var1) {
      this.d = var1;
   }

   public void a(int var1, K var2, V var3) {
      if(this.d.equals(var2)) {
         this.e = var3;
      }

      super.a(var1, var2, var3);
   }

   public void a() {
      Validate.notNull(this.d);
   }

   public V get(K var1) {
      Object var2 = super.get(var1);
      return var2 == null?this.e:var2;
   }

   public V a(int var1) {
      Object var2 = super.a(var1);
      return var2 == null?this.e:var2;
   }
}
