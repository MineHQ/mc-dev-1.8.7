package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.IRegistry;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegistrySimple<K, V> implements IRegistry<K, V> {
   private static final Logger a = LogManager.getLogger();
   protected final Map<K, V> c = this.b();

   public RegistrySimple() {
   }

   protected Map<K, V> b() {
      return Maps.newHashMap();
   }

   public V get(K var1) {
      return this.c.get(var1);
   }

   public void a(K var1, V var2) {
      Validate.notNull(var1);
      Validate.notNull(var2);
      if(this.c.containsKey(var1)) {
         a.debug("Adding duplicate key \'" + var1 + "\' to registry");
      }

      this.c.put(var1, var2);
   }

   public Set<K> keySet() {
      return Collections.unmodifiableSet(this.c.keySet());
   }

   public boolean d(K var1) {
      return this.c.containsKey(var1);
   }

   public Iterator<V> iterator() {
      return this.c.values().iterator();
   }
}
