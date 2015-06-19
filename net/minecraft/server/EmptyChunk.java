package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Chunk;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EmptyChunk extends Chunk {
   public EmptyChunk(World var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public boolean a(int var1, int var2) {
      return var1 == this.locX && var2 == this.locZ;
   }

   public int b(int var1, int var2) {
      return 0;
   }

   public void initLighting() {
   }

   public Block getType(BlockPosition var1) {
      return Blocks.AIR;
   }

   public int b(BlockPosition var1) {
      return 255;
   }

   public int c(BlockPosition var1) {
      return 0;
   }

   public int getBrightness(EnumSkyBlock var1, BlockPosition var2) {
      return var1.c;
   }

   public void a(EnumSkyBlock var1, BlockPosition var2, int var3) {
   }

   public int a(BlockPosition var1, int var2) {
      return 0;
   }

   public void a(Entity var1) {
   }

   public void b(Entity var1) {
   }

   public void a(Entity var1, int var2) {
   }

   public boolean d(BlockPosition var1) {
      return false;
   }

   public TileEntity a(BlockPosition var1, Chunk.EnumTileEntityState var2) {
      return null;
   }

   public void a(TileEntity var1) {
   }

   public void a(BlockPosition var1, TileEntity var2) {
   }

   public void e(BlockPosition var1) {
   }

   public void addEntities() {
   }

   public void removeEntities() {
   }

   public void e() {
   }

   public void a(Entity var1, AxisAlignedBB var2, List<Entity> var3, Predicate<? super Entity> var4) {
   }

   public <T extends Entity> void a(Class<? extends T> var1, AxisAlignedBB var2, List<T> var3, Predicate<? super T> var4) {
   }

   public boolean a(boolean var1) {
      return false;
   }

   public Random a(long var1) {
      return new Random(this.getWorld().getSeed() + (long)(this.locX * this.locX * 4987142) + (long)(this.locX * 5947611) + (long)(this.locZ * this.locZ) * 4392871L + (long)(this.locZ * 389711) ^ var1);
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean c(int var1, int var2) {
      return true;
   }
}
