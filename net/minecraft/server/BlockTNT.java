package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.World;

public class BlockTNT extends Block {
   public static final BlockStateBoolean EXPLODE = BlockStateBoolean.of("explode");

   public BlockTNT() {
      super(Material.TNT);
      this.j(this.blockStateList.getBlockData().set(EXPLODE, Boolean.valueOf(false)));
      this.a(CreativeModeTab.d);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      super.onPlace(var1, var2, var3);
      if(var1.isBlockIndirectlyPowered(var2)) {
         this.postBreak(var1, var2, var3.set(EXPLODE, Boolean.valueOf(true)));
         var1.setAir(var2);
      }

   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(var1.isBlockIndirectlyPowered(var2)) {
         this.postBreak(var1, var2, var3.set(EXPLODE, Boolean.valueOf(true)));
         var1.setAir(var2);
      }

   }

   public void wasExploded(World var1, BlockPosition var2, Explosion var3) {
      if(!var1.isClientSide) {
         EntityTNTPrimed var4 = new EntityTNTPrimed(var1, (double)((float)var2.getX() + 0.5F), (double)var2.getY(), (double)((float)var2.getZ() + 0.5F), var3.c());
         var4.fuseTicks = var1.random.nextInt(var4.fuseTicks / 4) + var4.fuseTicks / 8;
         var1.addEntity(var4);
      }
   }

   public void postBreak(World var1, BlockPosition var2, IBlockData var3) {
      this.a(var1, var2, var3, (EntityLiving)null);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityLiving var4) {
      if(!var1.isClientSide) {
         if(((Boolean)var3.get(EXPLODE)).booleanValue()) {
            EntityTNTPrimed var5 = new EntityTNTPrimed(var1, (double)((float)var2.getX() + 0.5F), (double)var2.getY(), (double)((float)var2.getZ() + 0.5F), var4);
            var1.addEntity(var5);
            var1.makeSound(var5, "game.tnt.primed", 1.0F, 1.0F);
         }

      }
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var4.bZ() != null) {
         Item var9 = var4.bZ().getItem();
         if(var9 == Items.FLINT_AND_STEEL || var9 == Items.FIRE_CHARGE) {
            this.a(var1, var2, var3.set(EXPLODE, Boolean.valueOf(true)), (EntityLiving)var4);
            var1.setAir(var2);
            if(var9 == Items.FLINT_AND_STEEL) {
               var4.bZ().damage(1, var4);
            } else if(!var4.abilities.canInstantlyBuild) {
               --var4.bZ().count;
            }

            return true;
         }
      }

      return super.interact(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      if(!var1.isClientSide && var4 instanceof EntityArrow) {
         EntityArrow var5 = (EntityArrow)var4;
         if(var5.isBurning()) {
            this.a(var1, var2, var1.getType(var2).set(EXPLODE, Boolean.valueOf(true)), var5.shooter instanceof EntityLiving?(EntityLiving)var5.shooter:null);
            var1.setAir(var2);
         }
      }

   }

   public boolean a(Explosion var1) {
      return false;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(EXPLODE, Boolean.valueOf((var1 & 1) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Boolean)var1.get(EXPLODE)).booleanValue()?1:0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{EXPLODE});
   }
}
