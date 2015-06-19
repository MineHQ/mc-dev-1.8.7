package net.minecraft.server;

import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityWeather;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityLightning extends EntityWeather {
   private int lifeTicks;
   public long a;
   private int c;

   public EntityLightning(World var1, double var2, double var4, double var6) {
      super(var1);
      this.setPositionRotation(var2, var4, var6, 0.0F, 0.0F);
      this.lifeTicks = 2;
      this.a = this.random.nextLong();
      this.c = this.random.nextInt(3) + 1;
      BlockPosition var8 = new BlockPosition(this);
      if(!var1.isClientSide && var1.getGameRules().getBoolean("doFireTick") && (var1.getDifficulty() == EnumDifficulty.NORMAL || var1.getDifficulty() == EnumDifficulty.HARD) && var1.areChunksLoaded(var8, 10)) {
         if(var1.getType(var8).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(var1, var8)) {
            var1.setTypeUpdate(var8, Blocks.FIRE.getBlockData());
         }

         for(int var9 = 0; var9 < 4; ++var9) {
            BlockPosition var10 = var8.a(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
            if(var1.getType(var10).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(var1, var10)) {
               var1.setTypeUpdate(var10, Blocks.FIRE.getBlockData());
            }
         }
      }

   }

   public void t_() {
      super.t_();
      if(this.lifeTicks == 2) {
         this.world.makeSound(this.locX, this.locY, this.locZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
         this.world.makeSound(this.locX, this.locY, this.locZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
      }

      --this.lifeTicks;
      if(this.lifeTicks < 0) {
         if(this.c == 0) {
            this.die();
         } else if(this.lifeTicks < -this.random.nextInt(10)) {
            --this.c;
            this.lifeTicks = 1;
            this.a = this.random.nextLong();
            BlockPosition var1 = new BlockPosition(this);
            if(!this.world.isClientSide && this.world.getGameRules().getBoolean("doFireTick") && this.world.areChunksLoaded(var1, 10) && this.world.getType(var1).getBlock().getMaterial() == Material.AIR && Blocks.FIRE.canPlace(this.world, var1)) {
               this.world.setTypeUpdate(var1, Blocks.FIRE.getBlockData());
            }
         }
      }

      if(this.lifeTicks >= 0) {
         if(this.world.isClientSide) {
            this.world.d(2);
         } else {
            double var6 = 3.0D;
            List var3 = this.world.getEntities(this, new AxisAlignedBB(this.locX - var6, this.locY - var6, this.locZ - var6, this.locX + var6, this.locY + 6.0D + var6, this.locZ + var6));

            for(int var4 = 0; var4 < var3.size(); ++var4) {
               Entity var5 = (Entity)var3.get(var4);
               var5.onLightningStrike(this);
            }
         }
      }

   }

   protected void h() {
   }

   protected void a(NBTTagCompound var1) {
   }

   protected void b(NBTTagCompound var1) {
   }
}
