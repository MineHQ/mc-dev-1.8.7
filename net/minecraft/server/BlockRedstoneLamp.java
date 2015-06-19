package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockRedstoneLamp extends Block {
   private final boolean a;

   public BlockRedstoneLamp(boolean var1) {
      super(Material.BUILDABLE_GLASS);
      this.a = var1;
      if(var1) {
         this.a(1.0F);
      }

   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         if(this.a && !var1.isBlockIndirectlyPowered(var2)) {
            var1.setTypeAndData(var2, Blocks.REDSTONE_LAMP.getBlockData(), 2);
         } else if(!this.a && var1.isBlockIndirectlyPowered(var2)) {
            var1.setTypeAndData(var2, Blocks.LIT_REDSTONE_LAMP.getBlockData(), 2);
         }

      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!var1.isClientSide) {
         if(this.a && !var1.isBlockIndirectlyPowered(var2)) {
            var1.a((BlockPosition)var2, (Block)this, 4);
         } else if(!this.a && var1.isBlockIndirectlyPowered(var2)) {
            var1.setTypeAndData(var2, Blocks.LIT_REDSTONE_LAMP.getBlockData(), 2);
         }

      }
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(!var1.isClientSide) {
         if(this.a && !var1.isBlockIndirectlyPowered(var2)) {
            var1.setTypeAndData(var2, Blocks.REDSTONE_LAMP.getBlockData(), 2);
         }

      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.REDSTONE_LAMP);
   }

   protected ItemStack i(IBlockData var1) {
      return new ItemStack(Blocks.REDSTONE_LAMP);
   }
}
