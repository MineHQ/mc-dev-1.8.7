package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Chunk;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.HttpUtilities;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityBeacon;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public class BlockBeacon extends BlockContainer {
   public BlockBeacon() {
      super(Material.SHATTERABLE, MaterialMapColor.G);
      this.c(3.0F);
      this.a(CreativeModeTab.f);
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityBeacon();
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityBeacon) {
            var4.openContainer((TileEntityBeacon)var9);
            var4.b(StatisticList.N);
         }

         return true;
      }
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public int b() {
      return 3;
   }

   public void postPlace(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4, ItemStack var5) {
      super.postPlace(var1, var2, var3, var4, var5);
      if(var5.hasName()) {
         TileEntity var6 = var1.getTileEntity(var2);
         if(var6 instanceof TileEntityBeacon) {
            ((TileEntityBeacon)var6).a(var5.getName());
         }
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      TileEntity var5 = var1.getTileEntity(var2);
      if(var5 instanceof TileEntityBeacon) {
         ((TileEntityBeacon)var5).m();
         var1.playBlockAction(var2, this, 1, 0);
      }

   }

   public static void f(final World var0, final BlockPosition var1) {
      HttpUtilities.a.submit(new Runnable() {
         public void run() {
            Chunk var1x = var0.getChunkAtWorldCoords(var1);

            for(int var2 = var1.getY() - 1; var2 >= 0; --var2) {
               final BlockPosition var3 = new BlockPosition(var1.getX(), var2, var1.getZ());
               if(!var1x.d(var3)) {
                  break;
               }

               IBlockData var4 = var0.getType(var3);
               if(var4.getBlock() == Blocks.BEACON) {
                  ((WorldServer)var0).postToMainThread(new Runnable() {
                     public void run() {
                        TileEntity var1x = var0.getTileEntity(var3);
                        if(var1x instanceof TileEntityBeacon) {
                           ((TileEntityBeacon)var1x).m();
                           var0.playBlockAction(var3, Blocks.BEACON, 1, 0);
                        }

                     }
                  });
               }
            }

         }
      });
   }
}
