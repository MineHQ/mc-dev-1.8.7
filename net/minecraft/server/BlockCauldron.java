package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;
import net.minecraft.server.ItemBanner;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MathHelper;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntityBanner;
import net.minecraft.server.World;

public class BlockCauldron extends Block {
   public static final BlockStateInteger LEVEL = BlockStateInteger.of("level", 0, 3);

   public BlockCauldron() {
      super(Material.ORE, MaterialMapColor.m);
      this.j(this.blockStateList.getBlockData().set(LEVEL, Integer.valueOf(0)));
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, AxisAlignedBB var4, List<AxisAlignedBB> var5, Entity var6) {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      float var7 = 0.125F;
      this.a(0.0F, 0.0F, 0.0F, var7, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var7);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(1.0F - var7, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.a(0.0F, 0.0F, 1.0F - var7, 1.0F, 1.0F, 1.0F);
      super.a(var1, var2, var3, var4, var5, var6);
      this.j();
   }

   public void j() {
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, Entity var4) {
      int var5 = ((Integer)var3.get(LEVEL)).intValue();
      float var6 = (float)var2.getY() + (6.0F + (float)(3 * var5)) / 16.0F;
      if(!var1.isClientSide && var4.isBurning() && var5 > 0 && var4.getBoundingBox().b <= (double)var6) {
         var4.extinguish();
         this.a(var1, var2, var3, var5 - 1);
      }

   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         ItemStack var9 = var4.inventory.getItemInHand();
         if(var9 == null) {
            return true;
         } else {
            int var10 = ((Integer)var3.get(LEVEL)).intValue();
            Item var11 = var9.getItem();
            if(var11 == Items.WATER_BUCKET) {
               if(var10 < 3) {
                  if(!var4.abilities.canInstantlyBuild) {
                     var4.inventory.setItem(var4.inventory.itemInHandIndex, new ItemStack(Items.BUCKET));
                  }

                  var4.b(StatisticList.I);
                  this.a(var1, var2, var3, 3);
               }

               return true;
            } else {
               ItemStack var13;
               if(var11 == Items.GLASS_BOTTLE) {
                  if(var10 > 0) {
                     if(!var4.abilities.canInstantlyBuild) {
                        var13 = new ItemStack(Items.POTION, 1, 0);
                        if(!var4.inventory.pickup(var13)) {
                           var1.addEntity(new EntityItem(var1, (double)var2.getX() + 0.5D, (double)var2.getY() + 1.5D, (double)var2.getZ() + 0.5D, var13));
                        } else if(var4 instanceof EntityPlayer) {
                           ((EntityPlayer)var4).updateInventory(var4.defaultContainer);
                        }

                        var4.b(StatisticList.J);
                        --var9.count;
                        if(var9.count <= 0) {
                           var4.inventory.setItem(var4.inventory.itemInHandIndex, (ItemStack)null);
                        }
                     }

                     this.a(var1, var2, var3, var10 - 1);
                  }

                  return true;
               } else {
                  if(var10 > 0 && var11 instanceof ItemArmor) {
                     ItemArmor var12 = (ItemArmor)var11;
                     if(var12.x_() == ItemArmor.EnumArmorMaterial.LEATHER && var12.d_(var9)) {
                        var12.c(var9);
                        this.a(var1, var2, var3, var10 - 1);
                        var4.b(StatisticList.K);
                        return true;
                     }
                  }

                  if(var10 > 0 && var11 instanceof ItemBanner && TileEntityBanner.c(var9) > 0) {
                     var13 = var9.cloneItemStack();
                     var13.count = 1;
                     TileEntityBanner.e(var13);
                     if(var9.count <= 1 && !var4.abilities.canInstantlyBuild) {
                        var4.inventory.setItem(var4.inventory.itemInHandIndex, var13);
                     } else {
                        if(!var4.inventory.pickup(var13)) {
                           var1.addEntity(new EntityItem(var1, (double)var2.getX() + 0.5D, (double)var2.getY() + 1.5D, (double)var2.getZ() + 0.5D, var13));
                        } else if(var4 instanceof EntityPlayer) {
                           ((EntityPlayer)var4).updateInventory(var4.defaultContainer);
                        }

                        var4.b(StatisticList.L);
                        if(!var4.abilities.canInstantlyBuild) {
                           --var9.count;
                        }
                     }

                     if(!var4.abilities.canInstantlyBuild) {
                        this.a(var1, var2, var3, var10 - 1);
                     }

                     return true;
                  } else {
                     return false;
                  }
               }
            }
         }
      }
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, int var4) {
      var1.setTypeAndData(var2, var3.set(LEVEL, Integer.valueOf(MathHelper.clamp(var4, 0, 3))), 2);
      var1.updateAdjacentComparators(var2, this);
   }

   public void k(World var1, BlockPosition var2) {
      if(var1.random.nextInt(20) == 1) {
         IBlockData var3 = var1.getType(var2);
         if(((Integer)var3.get(LEVEL)).intValue() < 3) {
            var1.setTypeAndData(var2, var3.a(LEVEL), 2);
         }

      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.CAULDRON;
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      return ((Integer)var1.getType(var2).get(LEVEL)).intValue();
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(LEVEL, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(LEVEL)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{LEVEL});
   }
}
