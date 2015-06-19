package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.Block;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;

public class BlockStatePredicate implements Predicate<IBlockData> {
   private final BlockStateList a;
   private final Map<IBlockState, Predicate> b = Maps.newHashMap();

   private BlockStatePredicate(BlockStateList var1) {
      this.a = var1;
   }

   public static BlockStatePredicate a(Block var0) {
      return new BlockStatePredicate(var0.P());
   }

   public boolean a(IBlockData var1) {
      if(var1 != null && var1.getBlock().equals(this.a.getBlock())) {
         Iterator var2 = this.b.entrySet().iterator();

         Entry var3;
         Comparable var4;
         do {
            if(!var2.hasNext()) {
               return true;
            }

            var3 = (Entry)var2.next();
            var4 = var1.get((IBlockState)var3.getKey());
         } while(((Predicate)var3.getValue()).apply(var4));

         return false;
      } else {
         return false;
      }
   }

   public <V extends Comparable<V>> BlockStatePredicate a(IBlockState<V> var1, Predicate<? extends V> var2) {
      if(!this.a.d().contains(var1)) {
         throw new IllegalArgumentException(this.a + " cannot support property " + var1);
      } else {
         this.b.put(var1, var2);
         return this;
      }
   }

   // $FF: synthetic method
   public boolean apply(Object var1) {
      return this.a((IBlockData)var1);
   }
}
