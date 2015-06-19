package net.minecraft.server;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockReed extends Block {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);

   protected BlockReed() {
      super(Material.PLANT);
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)));
      float var1 = 0.375F;
      this.a(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 1.0F, 0.5F + var1);
      this.a(true);
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var1.getType(var2.down()).getBlock() == Blocks.REEDS || this.e(var1, var2, var3)) {
         if(var1.isEmpty(var2.up())) {
            int var5;
            for(var5 = 1; var1.getType(var2.down(var5)).getBlock() == this; ++var5) {
               ;
            }

            if(var5 < 3) {
               int var6 = ((Integer)var3.get(AGE)).intValue();
               if(var6 == 15) {
                  var1.setTypeUpdate(var2.up(), this.getBlockData());
                  var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(0)), 4);
               } else {
                  var1.setTypeAndData(var2, var3.set(AGE, Integer.valueOf(var6 + 1)), 4);
               }
            }
         }

      }
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      Block var3 = var1.getType(var2.down()).getBlock();
      if(var3 == this) {
         return true;
      } else if(var3 != Blocks.GRASS && var3 != Blocks.DIRT && var3 != Blocks.SAND) {
         return false;
      } else {
         Iterator var4 = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

         EnumDirection var5;
         do {
            if(!var4.hasNext()) {
               return false;
            }

            var5 = (EnumDirection)var4.next();
         } while(var1.getType(var2.shift(var5).down()).getBlock().getMaterial() != Material.WATER);

         return true;
      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      this.e(var1, var2, var3);
   }

   protected final boolean e(World var1, BlockPosition var2, IBlockData var3) {
      if(this.e(var1, var2)) {
         return true;
      } else {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
         return false;
      }
   }

   public boolean e(World var1, BlockPosition var2) {
      return this.canPlace(var1, var2);
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.REEDS;
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(AGE, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(AGE)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{AGE});
   }
}
