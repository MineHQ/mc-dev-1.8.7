package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.IBlockData;
import net.minecraft.server.NibbleArray;

public class ChunkSection {
   private int yPos;
   private int nonEmptyBlockCount;
   private int tickingBlockCount;
   private char[] blockIds;
   private NibbleArray emittedLight;
   private NibbleArray skyLight;

   public ChunkSection(int var1, boolean var2) {
      this.yPos = var1;
      this.blockIds = new char[4096];
      this.emittedLight = new NibbleArray();
      if(var2) {
         this.skyLight = new NibbleArray();
      }

   }

   public IBlockData getType(int var1, int var2, int var3) {
      IBlockData var4 = (IBlockData)Block.d.a(this.blockIds[var2 << 8 | var3 << 4 | var1]);
      return var4 != null?var4:Blocks.AIR.getBlockData();
   }

   public void setType(int var1, int var2, int var3, IBlockData var4) {
      IBlockData var5 = this.getType(var1, var2, var3);
      Block var6 = var5.getBlock();
      Block var7 = var4.getBlock();
      if(var6 != Blocks.AIR) {
         --this.nonEmptyBlockCount;
         if(var6.isTicking()) {
            --this.tickingBlockCount;
         }
      }

      if(var7 != Blocks.AIR) {
         ++this.nonEmptyBlockCount;
         if(var7.isTicking()) {
            ++this.tickingBlockCount;
         }
      }

      this.blockIds[var2 << 8 | var3 << 4 | var1] = (char)Block.d.b(var4);
   }

   public Block b(int var1, int var2, int var3) {
      return this.getType(var1, var2, var3).getBlock();
   }

   public int c(int var1, int var2, int var3) {
      IBlockData var4 = this.getType(var1, var2, var3);
      return var4.getBlock().toLegacyData(var4);
   }

   public boolean a() {
      return this.nonEmptyBlockCount == 0;
   }

   public boolean shouldTick() {
      return this.tickingBlockCount > 0;
   }

   public int getYPosition() {
      return this.yPos;
   }

   public void a(int var1, int var2, int var3, int var4) {
      this.skyLight.a(var1, var2, var3, var4);
   }

   public int d(int var1, int var2, int var3) {
      return this.skyLight.a(var1, var2, var3);
   }

   public void b(int var1, int var2, int var3, int var4) {
      this.emittedLight.a(var1, var2, var3, var4);
   }

   public int e(int var1, int var2, int var3) {
      return this.emittedLight.a(var1, var2, var3);
   }

   public void recalcBlockCounts() {
      this.nonEmptyBlockCount = 0;
      this.tickingBlockCount = 0;

      for(int var1 = 0; var1 < 16; ++var1) {
         for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
               Block var4 = this.b(var1, var2, var3);
               if(var4 != Blocks.AIR) {
                  ++this.nonEmptyBlockCount;
                  if(var4.isTicking()) {
                     ++this.tickingBlockCount;
                  }
               }
            }
         }
      }

   }

   public char[] getIdArray() {
      return this.blockIds;
   }

   public void a(char[] var1) {
      this.blockIds = var1;
   }

   public NibbleArray getEmittedLightArray() {
      return this.emittedLight;
   }

   public NibbleArray getSkyLightArray() {
      return this.skyLight;
   }

   public void a(NibbleArray var1) {
      this.emittedLight = var1;
   }

   public void b(NibbleArray var1) {
      this.skyLight = var1;
   }
}
