package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDiodeAbstract;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.World;

public class BlockRepeater extends BlockDiodeAbstract {
   public static final BlockStateBoolean LOCKED = BlockStateBoolean.of("locked");
   public static final BlockStateInteger DELAY = BlockStateInteger.of("delay", 1, 4);

   protected BlockRepeater(boolean var1) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(DELAY, Integer.valueOf(1)).set(LOCKED, Boolean.valueOf(false)));
   }

   public String getName() {
      return LocaleI18n.get("item.diode.name");
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      return var1.set(LOCKED, Boolean.valueOf(this.b(var2, var3, var1)));
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var4.abilities.mayBuild) {
         return false;
      } else {
         var1.setTypeAndData(var2, var3.a(DELAY), 3);
         return true;
      }
   }

   protected int d(IBlockData var1) {
      return ((Integer)var1.get(DELAY)).intValue() * 2;
   }

   protected IBlockData e(IBlockData var1) {
      Integer var2 = (Integer)var1.get(DELAY);
      Boolean var3 = (Boolean)var1.get(LOCKED);
      EnumDirection var4 = (EnumDirection)var1.get(FACING);
      return Blocks.POWERED_REPEATER.getBlockData().set(FACING, var4).set(DELAY, var2).set(LOCKED, var3);
   }

   protected IBlockData k(IBlockData var1) {
      Integer var2 = (Integer)var1.get(DELAY);
      Boolean var3 = (Boolean)var1.get(LOCKED);
      EnumDirection var4 = (EnumDirection)var1.get(FACING);
      return Blocks.UNPOWERED_REPEATER.getBlockData().set(FACING, var4).set(DELAY, var2).set(LOCKED, var3);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.REPEATER;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2, IBlockData var3) {
      return this.c(var1, var2, var3) > 0;
   }

   protected boolean c(Block var1) {
      return d(var1);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      super.remove(var1, var2, var3);
      this.h(var1, var2, var3);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, EnumDirection.fromType2(var1)).set(LOCKED, Boolean.valueOf(false)).set(DELAY, Integer.valueOf(1 + (var1 >> 2)));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).b();
      var3 |= ((Integer)var1.get(DELAY)).intValue() - 1 << 2;
      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, DELAY, LOCKED});
   }
}
