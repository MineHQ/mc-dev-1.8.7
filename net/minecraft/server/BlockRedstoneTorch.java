package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockTorch;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.World;

public class BlockRedstoneTorch extends BlockTorch {
   private static Map<World, List<BlockRedstoneTorch.RedstoneUpdateInfo>> b = Maps.newHashMap();
   private final boolean isOn;

   private boolean a(World var1, BlockPosition var2, boolean var3) {
      if(!b.containsKey(var1)) {
         b.put(var1, Lists.newArrayList());
      }

      List var4 = (List)b.get(var1);
      if(var3) {
         var4.add(new BlockRedstoneTorch.RedstoneUpdateInfo(var2, var1.getTime()));
      }

      int var5 = 0;

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         BlockRedstoneTorch.RedstoneUpdateInfo var7 = (BlockRedstoneTorch.RedstoneUpdateInfo)var4.get(var6);
         if(var7.a.equals(var2)) {
            ++var5;
            if(var5 >= 8) {
               return true;
            }
         }
      }

      return false;
   }

   protected BlockRedstoneTorch(boolean var1) {
      this.isOn = var1;
      this.a(true);
      this.a((CreativeModeTab)null);
   }

   public int a(World var1) {
      return 2;
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(this.isOn) {
         EnumDirection[] var4 = EnumDirection.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumDirection var7 = var4[var6];
            var1.applyPhysics(var2.shift(var7), this);
         }
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      if(this.isOn) {
         EnumDirection[] var4 = EnumDirection.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumDirection var7 = var4[var6];
            var1.applyPhysics(var2.shift(var7), this);
         }
      }

   }

   public int a(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return this.isOn && var3.get(FACING) != var4?15:0;
   }

   private boolean g(World var1, BlockPosition var2, IBlockData var3) {
      EnumDirection var4 = ((EnumDirection)var3.get(FACING)).opposite();
      return var1.isBlockFacePowered(var2.shift(var4), var4);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Random var4) {
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      boolean var5 = this.g(var1, var2, var3);
      List var6 = (List)b.get(var1);

      while(var6 != null && !var6.isEmpty() && var1.getTime() - ((BlockRedstoneTorch.RedstoneUpdateInfo)var6.get(0)).b > 60L) {
         var6.remove(0);
      }

      if(this.isOn) {
         if(var5) {
            var1.setTypeAndData(var2, Blocks.UNLIT_REDSTONE_TORCH.getBlockData().set(FACING, var3.get(FACING)), 3);
            if(this.a(var1, var2, true)) {
               var1.makeSound((double)((float)var2.getX() + 0.5F), (double)((float)var2.getY() + 0.5F), (double)((float)var2.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (var1.random.nextFloat() - var1.random.nextFloat()) * 0.8F);

               for(int var7 = 0; var7 < 5; ++var7) {
                  double var8 = (double)var2.getX() + var4.nextDouble() * 0.6D + 0.2D;
                  double var10 = (double)var2.getY() + var4.nextDouble() * 0.6D + 0.2D;
                  double var12 = (double)var2.getZ() + var4.nextDouble() * 0.6D + 0.2D;
                  var1.addParticle(EnumParticle.SMOKE_NORMAL, var8, var10, var12, 0.0D, 0.0D, 0.0D, new int[0]);
               }

               var1.a((BlockPosition)var2, (Block)var1.getType(var2).getBlock(), 160);
            }
         }
      } else if(!var5 && !this.a(var1, var2, false)) {
         var1.setTypeAndData(var2, Blocks.REDSTONE_TORCH.getBlockData().set(FACING, var3.get(FACING)), 3);
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!this.e(var1, var2, var3)) {
         if(this.isOn == this.g(var1, var2, var3)) {
            var1.a((BlockPosition)var2, (Block)this, this.a(var1));
         }

      }
   }

   public int b(IBlockAccess var1, BlockPosition var2, IBlockData var3, EnumDirection var4) {
      return var4 == EnumDirection.DOWN?this.a(var1, var2, var3, var4):0;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.REDSTONE_TORCH);
   }

   public boolean isPowerSource() {
      return true;
   }

   public boolean b(Block var1) {
      return var1 == Blocks.UNLIT_REDSTONE_TORCH || var1 == Blocks.REDSTONE_TORCH;
   }

   static class RedstoneUpdateInfo {
      BlockPosition a;
      long b;

      public RedstoneUpdateInfo(BlockPosition var1, long var2) {
         this.a = var1;
         this.b = var2;
      }
   }
}
