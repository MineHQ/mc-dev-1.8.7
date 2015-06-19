package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;

public class NextTickListEntry implements Comparable<NextTickListEntry> {
   private static long d;
   private final Block e;
   public final BlockPosition a;
   public long b;
   public int c;
   private long f;

   public NextTickListEntry(BlockPosition var1, Block var2) {
      this.f = (long)(d++);
      this.a = var1;
      this.e = var2;
   }

   public boolean equals(Object var1) {
      if(!(var1 instanceof NextTickListEntry)) {
         return false;
      } else {
         NextTickListEntry var2 = (NextTickListEntry)var1;
         return this.a.equals(var2.a) && Block.a(this.e, var2.e);
      }
   }

   public int hashCode() {
      return this.a.hashCode();
   }

   public NextTickListEntry a(long var1) {
      this.b = var1;
      return this;
   }

   public void a(int var1) {
      this.c = var1;
   }

   public int a(NextTickListEntry var1) {
      return this.b < var1.b?-1:(this.b > var1.b?1:(this.c != var1.c?this.c - var1.c:(this.f < var1.f?-1:(this.f > var1.f?1:0))));
   }

   public String toString() {
      return Block.getId(this.e) + ": " + this.a + ", " + this.b + ", " + this.c + ", " + this.f;
   }

   public Block a() {
      return this.e;
   }

   // $FF: synthetic method
   public int compareTo(Object var1) {
      return this.a((NextTickListEntry)var1);
   }
}
