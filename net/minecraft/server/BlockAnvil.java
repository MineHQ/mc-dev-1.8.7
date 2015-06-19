package net.minecraft.server;

import net.minecraft.server.BlockFalling;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerAnvil;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ITileEntityContainer;
import net.minecraft.server.Material;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.World;

public class BlockAnvil extends BlockFalling {
   public static final BlockStateDirection FACING;
   public static final BlockStateInteger DAMAGE;

   protected BlockAnvil() {
      super(Material.HEAVY);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(DAMAGE, Integer.valueOf(0)));
      this.e(0);
      this.a(CreativeModeTab.c);
   }

   public boolean d() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      EnumDirection var9 = var8.getDirection().e();
      return super.getPlacedState(var1, var2, var3, var4, var5, var6, var7, var8).set(FACING, var9).set(DAMAGE, Integer.valueOf(var7 >> 2));
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(!var1.isClientSide) {
         var4.openTileEntity(new BlockAnvil.TileEntityContainerAnvil(var1, var2));
      }

      return true;
   }

   public int getDropData(IBlockData var1) {
      return ((Integer)var1.get(DAMAGE)).intValue();
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      EnumDirection var3 = (EnumDirection)var1.getType(var2).get(FACING);
      if(var3.k() == EnumDirection.EnumAxis.X) {
         this.a(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
      } else {
         this.a(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
      }

   }

   protected void a(EntityFallingBlock var1) {
      var1.a(true);
   }

   public void a_(World var1, BlockPosition var2) {
      var1.triggerEffect(1022, var2, 0);
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, EnumDirection.fromType2(var1 & 3)).set(DAMAGE, Integer.valueOf((var1 & 15) >> 2));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).b();
      var3 |= ((Integer)var1.get(DAMAGE)).intValue() << 2;
      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, DAMAGE});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      DAMAGE = BlockStateInteger.of("damage", 0, 2);
   }

   public static class TileEntityContainerAnvil implements ITileEntityContainer {
      private final World a;
      private final BlockPosition b;

      public TileEntityContainerAnvil(World var1, BlockPosition var2) {
         this.a = var1;
         this.b = var2;
      }

      public String getName() {
         return "anvil";
      }

      public boolean hasCustomName() {
         return false;
      }

      public IChatBaseComponent getScoreboardDisplayName() {
         return new ChatMessage(Blocks.ANVIL.a() + ".name", new Object[0]);
      }

      public Container createContainer(PlayerInventory var1, EntityHuman var2) {
         return new ContainerAnvil(var1, this.a, this.b, var2);
      }

      public String getContainerName() {
         return "minecraft:anvil";
      }
   }
}
