package net.minecraft.server;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.ShapeDetector;
import net.minecraft.server.ShapeDetectorBlock;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ShapeDetectorBuilder {
   private static final Joiner a = Joiner.on(",");
   private final List<String[]> b = Lists.newArrayList();
   private final Map<Character, Predicate<ShapeDetectorBlock>> c = Maps.newHashMap();
   private int d;
   private int e;

   private ShapeDetectorBuilder() {
      this.c.put(Character.valueOf(' '), Predicates.alwaysTrue());
   }

   public ShapeDetectorBuilder a(String... var1) {
      if(!ArrayUtils.isEmpty((Object[])var1) && !StringUtils.isEmpty(var1[0])) {
         if(this.b.isEmpty()) {
            this.d = var1.length;
            this.e = var1[0].length();
         }

         if(var1.length != this.d) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.d + ", but was given one with a height of " + var1.length + ")");
         } else {
            String[] var2 = var1;
            int var3 = var1.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String var5 = var2[var4];
               if(var5.length() != this.e) {
                  throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.e + ", found one with " + var5.length() + ")");
               }

               char[] var6 = var5.toCharArray();
               int var7 = var6.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  char var9 = var6[var8];
                  if(!this.c.containsKey(Character.valueOf(var9))) {
                     this.c.put(Character.valueOf(var9), (Object)null);
                  }
               }
            }

            this.b.add(var1);
            return this;
         }
      } else {
         throw new IllegalArgumentException("Empty pattern for aisle");
      }
   }

   public static ShapeDetectorBuilder a() {
      return new ShapeDetectorBuilder();
   }

   public ShapeDetectorBuilder a(char var1, Predicate<ShapeDetectorBlock> var2) {
      this.c.put(Character.valueOf(var1), var2);
      return this;
   }

   public ShapeDetector b() {
      return new ShapeDetector(this.c());
   }

   private Predicate<ShapeDetectorBlock>[][][] c() {
      this.d();
      Predicate[][][] var1 = (Predicate[][][])((Predicate[][][])Array.newInstance(Predicate.class, new int[]{this.b.size(), this.d, this.e}));

      for(int var2 = 0; var2 < this.b.size(); ++var2) {
         for(int var3 = 0; var3 < this.d; ++var3) {
            for(int var4 = 0; var4 < this.e; ++var4) {
               var1[var2][var3][var4] = (Predicate)this.c.get(Character.valueOf(((String[])this.b.get(var2))[var3].charAt(var4)));
            }
         }
      }

      return var1;
   }

   private void d() {
      ArrayList var1 = Lists.newArrayList();
      Iterator var2 = this.c.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if(var3.getValue() == null) {
            var1.add(var3.getKey());
         }
      }

      if(!var1.isEmpty()) {
         throw new IllegalStateException("Predicates for character(s) " + a.join((Iterable)var1) + " are missing");
      }
   }
}
