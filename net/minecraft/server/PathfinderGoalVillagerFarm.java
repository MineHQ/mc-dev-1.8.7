package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.IBlockData;
import net.minecraft.server.InventorySubcontainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.PathfinderGoalGotoTarget;
import net.minecraft.server.World;

public class PathfinderGoalVillagerFarm extends PathfinderGoalGotoTarget {
   private final EntityVillager c;
   private boolean d;
   private boolean e;
   private int f;

   public PathfinderGoalVillagerFarm(EntityVillager var1, double var2) {
      super(var1, var2, 16);
      this.c = var1;
   }

   public boolean a() {
      if(this.a <= 0) {
         if(!this.c.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
         }

         this.f = -1;
         this.d = this.c.cu();
         this.e = this.c.ct();
      }

      return super.a();
   }

   public boolean b() {
      return this.f >= 0 && super.b();
   }

   public void c() {
      super.c();
   }

   public void d() {
      super.d();
   }

   public void e() {
      super.e();
      this.c.getControllerLook().a((double)this.b.getX() + 0.5D, (double)(this.b.getY() + 1), (double)this.b.getZ() + 0.5D, 10.0F, (float)this.c.bQ());
      if(this.f()) {
         World var1 = this.c.world;
         BlockPosition var2 = this.b.up();
         IBlockData var3 = var1.getType(var2);
         Block var4 = var3.getBlock();
         if(this.f == 0 && var4 instanceof BlockCrops && ((Integer)var3.get(BlockCrops.AGE)).intValue() == 7) {
            var1.setAir(var2, true);
         } else if(this.f == 1 && var4 == Blocks.AIR) {
            InventorySubcontainer var5 = this.c.cq();

            for(int var6 = 0; var6 < var5.getSize(); ++var6) {
               ItemStack var7 = var5.getItem(var6);
               boolean var8 = false;
               if(var7 != null) {
                  if(var7.getItem() == Items.WHEAT_SEEDS) {
                     var1.setTypeAndData(var2, Blocks.WHEAT.getBlockData(), 3);
                     var8 = true;
                  } else if(var7.getItem() == Items.POTATO) {
                     var1.setTypeAndData(var2, Blocks.POTATOES.getBlockData(), 3);
                     var8 = true;
                  } else if(var7.getItem() == Items.CARROT) {
                     var1.setTypeAndData(var2, Blocks.CARROTS.getBlockData(), 3);
                     var8 = true;
                  }
               }

               if(var8) {
                  --var7.count;
                  if(var7.count <= 0) {
                     var5.setItem(var6, (ItemStack)null);
                  }
                  break;
               }
            }
         }

         this.f = -1;
         this.a = 10;
      }

   }

   protected boolean a(World var1, BlockPosition var2) {
      Block var3 = var1.getType(var2).getBlock();
      if(var3 == Blocks.FARMLAND) {
         var2 = var2.up();
         IBlockData var4 = var1.getType(var2);
         var3 = var4.getBlock();
         if(var3 instanceof BlockCrops && ((Integer)var4.get(BlockCrops.AGE)).intValue() == 7 && this.e && (this.f == 0 || this.f < 0)) {
            this.f = 0;
            return true;
         }

         if(var3 == Blocks.AIR && this.d && (this.f == 1 || this.f < 0)) {
            this.f = 1;
            return true;
         }
      }

      return false;
   }
}
