package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import net.minecraft.server.BlockState;

public class BlockStateBoolean extends BlockState<Boolean> {
   private final ImmutableSet<Boolean> a = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

   protected BlockStateBoolean(String var1) {
      super(var1, Boolean.class);
   }

   public Collection<Boolean> c() {
      return this.a;
   }

   public static BlockStateBoolean of(String var0) {
      return new BlockStateBoolean(var0);
   }

   public String a(Boolean var1) {
      return var1.toString();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public String a(Comparable var1) {
      return this.a((Boolean)var1);
   }
}
