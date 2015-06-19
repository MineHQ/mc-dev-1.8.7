package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockWeb extends Block {
   public BlockWeb() {
      super(Material.WEB);
      this.a(CreativeModeTab.c);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      var4.aA();
   }

   public boolean c() {
      return false;
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean d() {
      return false;
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.STRING;
   }

   protected boolean I() {
      return true;
   }
}
