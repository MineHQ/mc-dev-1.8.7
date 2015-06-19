package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDataAbstract;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IteratorUtils;
import net.minecraft.server.MapGeneratorUtils;

public class BlockStateList {
   private static final Joiner a = Joiner.on(", ");
   private static final Function<IBlockState, String> b = new Function() {
      public String a(IBlockState var1) {
         return var1 == null?"<NULL>":var1.a();
      }

      // $FF: synthetic method
      public Object apply(Object var1) {
         return this.a((IBlockState)var1);
      }
   };
   private final Block c;
   private final ImmutableList<IBlockState> d;
   private final ImmutableList<IBlockData> e;

   public BlockStateList(Block var1, IBlockState... var2) {
      this.c = var1;
      Arrays.sort(var2, new Comparator() {
         public int a(IBlockState var1, IBlockState var2) {
            return var1.a().compareTo(var2.a());
         }

         // $FF: synthetic method
         public int compare(Object var1, Object var2) {
            return this.a((IBlockState)var1, (IBlockState)var2);
         }
      });
      this.d = ImmutableList.copyOf((Object[])var2);
      LinkedHashMap var3 = Maps.newLinkedHashMap();
      ArrayList var4 = Lists.newArrayList();
      Iterable var5 = IteratorUtils.a(this.e());
      Iterator var6 = var5.iterator();

      while(var6.hasNext()) {
         List var7 = (List)var6.next();
         Map var8 = MapGeneratorUtils.b(this.d, var7);
         BlockStateList.BlockData var9 = new BlockStateList.BlockData(var1, ImmutableMap.copyOf(var8), null);
         var3.put(var8, var9);
         var4.add(var9);
      }

      var6 = var4.iterator();

      while(var6.hasNext()) {
         BlockStateList.BlockData var10 = (BlockStateList.BlockData)var6.next();
         var10.a(var3);
      }

      this.e = ImmutableList.copyOf((Collection)var4);
   }

   public ImmutableList<IBlockData> a() {
      return this.e;
   }

   private List<Iterable<Comparable>> e() {
      ArrayList var1 = Lists.newArrayList();

      for(int var2 = 0; var2 < this.d.size(); ++var2) {
         var1.add(((IBlockState)this.d.get(var2)).c());
      }

      return var1;
   }

   public IBlockData getBlockData() {
      return (IBlockData)this.e.get(0);
   }

   public Block getBlock() {
      return this.c;
   }

   public Collection<IBlockState> d() {
      return this.d;
   }

   public String toString() {
      return Objects.toStringHelper((Object)this).add("block", Block.REGISTRY.c(this.c)).add("properties", Iterables.transform(this.d, b)).toString();
   }

   static class BlockData extends BlockDataAbstract {
      private final Block a;
      private final ImmutableMap<IBlockState, Comparable> b;
      private ImmutableTable<IBlockState, Comparable, IBlockData> c;

      private BlockData(Block var1, ImmutableMap<IBlockState, Comparable> var2) {
         this.a = var1;
         this.b = var2;
      }

      public Collection<IBlockState> a() {
         return Collections.unmodifiableCollection(this.b.keySet());
      }

      public <T extends Comparable<T>> T get(IBlockState<T> var1) {
         if(!this.b.containsKey(var1)) {
            throw new IllegalArgumentException("Cannot get property " + var1 + " as it does not exist in " + this.a.P());
         } else {
            return (Comparable)var1.b().cast(this.b.get(var1));
         }
      }

      public <T extends Comparable<T>, V extends T> IBlockData set(IBlockState<T> var1, V var2) {
         if(!this.b.containsKey(var1)) {
            throw new IllegalArgumentException("Cannot set property " + var1 + " as it does not exist in " + this.a.P());
         } else if(!var1.c().contains(var2)) {
            throw new IllegalArgumentException("Cannot set property " + var1 + " to " + var2 + " on block " + Block.REGISTRY.c(this.a) + ", it is not an allowed value");
         } else {
            return (IBlockData)(this.b.get(var1) == var2?this:(IBlockData)this.c.get(var1, var2));
         }
      }

      public ImmutableMap<IBlockState, Comparable> b() {
         return this.b;
      }

      public Block getBlock() {
         return this.a;
      }

      public boolean equals(Object var1) {
         return this == var1;
      }

      public int hashCode() {
         return this.b.hashCode();
      }

      public void a(Map<Map<IBlockState, Comparable>, BlockStateList.BlockData> var1) {
         if(this.c != null) {
            throw new IllegalStateException();
         } else {
            HashBasedTable var2 = HashBasedTable.create();
            Iterator var3 = this.b.keySet().iterator();

            while(var3.hasNext()) {
               IBlockState var4 = (IBlockState)var3.next();
               Iterator var5 = var4.c().iterator();

               while(var5.hasNext()) {
                  Comparable var6 = (Comparable)var5.next();
                  if(var6 != this.b.get(var4)) {
                     var2.put(var4, var6, var1.get(this.b(var4, var6)));
                  }
               }
            }

            this.c = ImmutableTable.copyOf(var2);
         }
      }

      private Map<IBlockState, Comparable> b(IBlockState var1, Comparable var2) {
         HashMap var3 = Maps.newHashMap(this.b);
         var3.put(var1, var2);
         return var3;
      }

      // $FF: synthetic method
      BlockData(Block var1, ImmutableMap var2, Object var3) {
         this(var1, var2);
      }
   }
}
