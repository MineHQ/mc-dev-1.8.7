package net.minecraft.server;

import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MobSpawnerAbstract;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.World;

public class ItemMonsterEgg extends Item {
   public ItemMonsterEgg() {
      this.a(true);
      this.a(CreativeModeTab.f);
   }

   public String a(ItemStack var1) {
      String var2 = ("" + LocaleI18n.get(this.getName() + ".name")).trim();
      String var3 = EntityTypes.b(var1.getData());
      if(var3 != null) {
         var2 = var2 + " " + LocaleI18n.get("entity." + var3 + ".name");
      }

      return var2;
   }

   public boolean interactWith(ItemStack var1, EntityHuman var2, World var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var3.isClientSide) {
         return true;
      } else if(!var2.a(var4.shift(var5), var5, var1)) {
         return false;
      } else {
         IBlockData var9 = var3.getType(var4);
         if(var9.getBlock() == Blocks.MOB_SPAWNER) {
            TileEntity var10 = var3.getTileEntity(var4);
            if(var10 instanceof TileEntityMobSpawner) {
               MobSpawnerAbstract var11 = ((TileEntityMobSpawner)var10).getSpawner();
               var11.setMobName(EntityTypes.b(var1.getData()));
               var10.update();
               var3.notify(var4);
               if(!var2.abilities.canInstantlyBuild) {
                  --var1.count;
               }

               return true;
            }
         }

         var4 = var4.shift(var5);
         double var13 = 0.0D;
         if(var5 == EnumDirection.UP && var9 instanceof BlockFence) {
            var13 = 0.5D;
         }

         Entity var12 = a(var3, var1.getData(), (double)var4.getX() + 0.5D, (double)var4.getY() + var13, (double)var4.getZ() + 0.5D);
         if(var12 != null) {
            if(var12 instanceof EntityLiving && var1.hasName()) {
               var12.setCustomName(var1.getName());
            }

            if(!var2.abilities.canInstantlyBuild) {
               --var1.count;
            }
         }

         return true;
      }
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(var2.isClientSide) {
         return var1;
      } else {
         MovingObjectPosition var4 = this.a(var2, var3, true);
         if(var4 == null) {
            return var1;
         } else {
            if(var4.type == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
               BlockPosition var5 = var4.a();
               if(!var2.a(var3, var5)) {
                  return var1;
               }

               if(!var3.a(var5, var4.direction, var1)) {
                  return var1;
               }

               if(var2.getType(var5).getBlock() instanceof BlockFluids) {
                  Entity var6 = a(var2, var1.getData(), (double)var5.getX() + 0.5D, (double)var5.getY() + 0.5D, (double)var5.getZ() + 0.5D);
                  if(var6 != null) {
                     if(var6 instanceof EntityLiving && var1.hasName()) {
                        ((EntityInsentient)var6).setCustomName(var1.getName());
                     }

                     if(!var3.abilities.canInstantlyBuild) {
                        --var1.count;
                     }

                     var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
                  }
               }
            }

            return var1;
         }
      }
   }

   public static Entity a(World var0, int var1, double var2, double var4, double var6) {
      if(!EntityTypes.eggInfo.containsKey(Integer.valueOf(var1))) {
         return null;
      } else {
         Entity var8 = null;

         for(int var9 = 0; var9 < 1; ++var9) {
            var8 = EntityTypes.a(var1, var0);
            if(var8 instanceof EntityLiving) {
               EntityInsentient var10 = (EntityInsentient)var8;
               var8.setPositionRotation(var2, var4, var6, MathHelper.g(var0.random.nextFloat() * 360.0F), 0.0F);
               var10.aK = var10.yaw;
               var10.aI = var10.yaw;
               var10.prepare(var0.E(new BlockPosition(var10)), (GroupDataEntity)null);
               var0.addEntity(var8);
               var10.x();
            }
         }

         return var8;
      }
   }
}
