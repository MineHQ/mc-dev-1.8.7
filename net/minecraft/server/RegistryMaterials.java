package net.minecraft.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.Registry;
import net.minecraft.server.RegistryID;
import net.minecraft.server.RegistrySimple;

public class RegistryMaterials<K, V> extends RegistrySimple<K, V> implements Registry<V> {
   protected final RegistryID<V> a = new RegistryID();
   protected final Map<V, K> b;

   public RegistryMaterials() {
      this.b = ((BiMap)this.c).inverse();
   }

   public void a(int var1, K var2, V var3) {
      this.a.a(var3, var1);
      this.a(var2, var3);
   }

   protected Map<K, V> b() {
      return HashBiMap.create();
   }

   public V get(K var1) {
      return super.get(var1);
   }

   public K c(V var1) {
      return this.b.get(var1);
   }

   public boolean d(K var1) {
      return super.d(var1);
   }

   public int b(V var1) {
      return this.a.b(var1);
   }

   public V a(int var1) {
      return this.a.a(var1);
   }

   public Iterator<V> iterator() {
      return this.a.iterator();
   }
}
