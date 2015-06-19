package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;

public class EntitySenses {
   EntityInsentient a;
   List<Entity> b = Lists.newArrayList();
   List<Entity> c = Lists.newArrayList();

   public EntitySenses(EntityInsentient var1) {
      this.a = var1;
   }

   public void a() {
      this.b.clear();
      this.c.clear();
   }

   public boolean a(Entity var1) {
      if(this.b.contains(var1)) {
         return true;
      } else if(this.c.contains(var1)) {
         return false;
      } else {
         this.a.world.methodProfiler.a("canSee");
         boolean var2 = this.a.hasLineOfSight(var1);
         this.a.world.methodProfiler.b();
         if(var2) {
            this.b.add(var1);
         } else {
            this.c.add(var1);
         }

         return var2;
      }
   }
}
