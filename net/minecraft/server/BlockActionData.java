package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;

public class BlockActionData {
   private BlockPosition a;
   private Block b;
   private int c;
   private int d;

   public BlockActionData(BlockPosition var1, Block var2, int var3, int var4) {
      this.a = var1;
      this.c = var3;
      this.d = var4;
      this.b = var2;
   }

   public BlockPosition a() {
      return this.a;
   }

   public int b() {
      return this.c;
   }

   public int c() {
      return this.d;
   }

   public Block d() {
      return this.b;
   }

   public boolean equals(Object var1) {
      if(!(var1 instanceof BlockActionData)) {
         return false;
      } else {
         BlockActionData var2 = (BlockActionData)var1;
         return this.a.equals(var2.a) && this.c == var2.c && this.d == var2.d && this.b == var2.b;
      }
   }

   public String toString() {
      return "TE(" + this.a + ")," + this.c + "," + this.d + "," + this.b;
   }
}
