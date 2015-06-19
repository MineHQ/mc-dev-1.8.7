package net.minecraft.server;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.server.Block;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStatePredicate;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.IBlockData;
import net.minecraft.server.PathfinderGoal;
import net.minecraft.server.World;

public class PathfinderGoalEatTile extends PathfinderGoal {
   private static final Predicate<IBlockData> b;
   private EntityInsentient c;
   private World d;
   int a;

   public PathfinderGoalEatTile(EntityInsentient var1) {
      this.c = var1;
      this.d = var1.world;
      this.a(7);
   }

   public boolean a() {
      if(this.c.bc().nextInt(this.c.isBaby()?50:1000) != 0) {
         return false;
      } else {
         BlockPosition var1 = new BlockPosition(this.c.locX, this.c.locY, this.c.locZ);
         return b.apply(this.d.getType(var1))?true:this.d.getType(var1.down()).getBlock() == Blocks.GRASS;
      }
   }

   public void c() {
      this.a = 40;
      this.d.broadcastEntityEffect(this.c, (byte)10);
      this.c.getNavigation().n();
   }

   public void d() {
      this.a = 0;
   }

   public boolean b() {
      return this.a > 0;
   }

   public int f() {
      return this.a;
   }

   public void e() {
      this.a = Math.max(0, this.a - 1);
      if(this.a == 4) {
         BlockPosition var1 = new BlockPosition(this.c.locX, this.c.locY, this.c.locZ);
         if(b.apply(this.d.getType(var1))) {
            if(this.d.getGameRules().getBoolean("mobGriefing")) {
               this.d.setAir(var1, false);
            }

            this.c.v();
         } else {
            BlockPosition var2 = var1.down();
            if(this.d.getType(var2).getBlock() == Blocks.GRASS) {
               if(this.d.getGameRules().getBoolean("mobGriefing")) {
                  this.d.triggerEffect(2001, var2, Block.getId(Blocks.GRASS));
                  this.d.setTypeAndData(var2, Blocks.DIRT.getBlockData(), 2);
               }

               this.c.v();
            }
         }

      }
   }

   static {
      b = BlockStatePredicate.a((Block)Blocks.TALLGRASS).a(BlockLongGrass.TYPE, Predicates.equalTo(BlockLongGrass.EnumTallGrassType.GRASS));
   }
}
