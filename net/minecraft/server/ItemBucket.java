package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public class ItemBucket extends Item {
   private Block a;

   public ItemBucket(Block var1) {
      this.maxStackSize = 1;
      this.a = var1;
      this.a(CreativeModeTab.f);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      boolean var4 = this.a == Blocks.AIR;
      MovingObjectPosition var5 = this.a(var2, var3, var4);
      if(var5 == null) {
         return var1;
      } else {
         if(var5.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
            BlockPosition var6 = var5.a();
            if(!var2.a(var3, var6)) {
               return var1;
            }

            if(var4) {
               if(!var3.a(var6.shift(var5.direction), var5.direction, var1)) {
                  return var1;
               }

               IBlockData var7 = var2.getType(var6);
               Material var8 = var7.getBlock().getMaterial();
               if(var8 == Material.WATER && ((Integer)var7.get(BlockFluids.LEVEL)).intValue() == 0) {
                  var2.setAir(var6);
                  var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                  return this.a(var1, var3, Items.WATER_BUCKET);
               }

               if(var8 == Material.LAVA && ((Integer)var7.get(BlockFluids.LEVEL)).intValue() == 0) {
                  var2.setAir(var6);
                  var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                  return this.a(var1, var3, Items.LAVA_BUCKET);
               }
            } else {
               if(this.a == Blocks.AIR) {
                  return new ItemStack(Items.BUCKET);
               }

               BlockPosition var9 = var6.shift(var5.direction);
               if(!var3.a(var9, var5.direction, var1)) {
                  return var1;
               }

               if(this.a(var2, var9) && !var3.abilities.canInstantlyBuild) {
                  var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                  return new ItemStack(Items.BUCKET);
               }
            }
         }

         return var1;
      }
   }

   private ItemStack a(ItemStack var1, EntityHuman var2, Item var3) {
      if(var2.abilities.canInstantlyBuild) {
         return var1;
      } else if(--var1.count <= 0) {
         return new ItemStack(var3);
      } else {
         if(!var2.inventory.pickup(new ItemStack(var3))) {
            var2.drop(new ItemStack(var3, 1, 0), false);
         }

         return var1;
      }
   }

   public boolean a(World var1, BlockPosition var2) {
      if(this.a == Blocks.AIR) {
         return false;
      } else {
         Material var3 = var1.getType(var2).getBlock().getMaterial();
         boolean var4 = !var3.isBuildable();
         if(!var1.isEmpty(var2) && !var4) {
            return false;
         } else {
            if(var1.worldProvider.n() && this.a == Blocks.FLOWING_WATER) {
               int var5 = var2.getX();
               int var6 = var2.getY();
               int var7 = var2.getZ();
               var1.makeSound((double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), (double)((float)var7 + 0.5F), "random.fizz", 0.5F, 2.6F + (var1.random.nextFloat() - var1.random.nextFloat()) * 0.8F);

               for(int var8 = 0; var8 < 8; ++var8) {
                  var1.addParticle(EnumParticle.SMOKE_LARGE, (double)var5 + Math.random(), (double)var6 + Math.random(), (double)var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
               }
            } else {
               if(!var1.isClientSide && var4 && !var3.isLiquid()) {
                  var1.setAir(var2, true);
               }

               var1.setTypeAndData(var2, this.a.getBlockData(), 3);
            }

            return true;
         }
      }
   }
}
