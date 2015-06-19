package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerWorkbench;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ITileEntityContainer;
import net.minecraft.server.Material;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class BlockWorkbench extends Block {
   protected BlockWorkbench() {
      super(Material.WOOD);
      this.a(CreativeModeTab.c);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         var4.openTileEntity(new BlockWorkbench.TileEntityContainerWorkbench(var1, var2));
         var4.b(StatisticList.Z);
         return true;
      }
   }

   public static class TileEntityContainerWorkbench implements ITileEntityContainer {
      private final World a;
      private final BlockPosition b;

      public TileEntityContainerWorkbench(World var1, BlockPosition var2) {
         this.a = var1;
         this.b = var2;
      }

      public String getName() {
         return null;
      }

      public boolean hasCustomName() {
         return false;
      }

      public IChatBaseComponent getScoreboardDisplayName() {
         return new ChatMessage(Blocks.CRAFTING_TABLE.a() + ".name", new Object[0]);
      }

      public Container createContainer(PlayerInventory var1, EntityHuman var2) {
         return new ContainerWorkbench(var1, this.a, this.b);
      }

      public String getContainerName() {
         return "minecraft:crafting_table";
      }
   }
}
