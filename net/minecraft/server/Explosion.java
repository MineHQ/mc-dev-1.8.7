package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentProtection;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class Explosion {
   private final boolean a;
   private final boolean b;
   private final Random c = new Random();
   private final World world;
   private final double posX;
   private final double posY;
   private final double posZ;
   public final Entity source;
   private final float size;
   private final List<BlockPosition> blocks = Lists.newArrayList();
   private final Map<EntityHuman, Vec3D> k = Maps.newHashMap();

   public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9, boolean var10, boolean var11) {
      this.world = var1;
      this.source = var2;
      this.size = var9;
      this.posX = var3;
      this.posY = var5;
      this.posZ = var7;
      this.a = var10;
      this.b = var11;
   }

   public void a() {
      HashSet var1 = Sets.newHashSet();
      boolean var2 = true;

      int var4;
      int var5;
      for(int var3 = 0; var3 < 16; ++var3) {
         for(var4 = 0; var4 < 16; ++var4) {
            for(var5 = 0; var5 < 16; ++var5) {
               if(var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15) {
                  double var6 = (double)((float)var3 / 15.0F * 2.0F - 1.0F);
                  double var8 = (double)((float)var4 / 15.0F * 2.0F - 1.0F);
                  double var10 = (double)((float)var5 / 15.0F * 2.0F - 1.0F);
                  double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                  var6 /= var12;
                  var8 /= var12;
                  var10 /= var12;
                  float var14 = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);
                  double var15 = this.posX;
                  double var17 = this.posY;
                  double var19 = this.posZ;

                  for(float var21 = 0.3F; var14 > 0.0F; var14 -= 0.22500001F) {
                     BlockPosition var22 = new BlockPosition(var15, var17, var19);
                     IBlockData var23 = this.world.getType(var22);
                     if(var23.getBlock().getMaterial() != Material.AIR) {
                        float var24 = this.source != null?this.source.a(this, this.world, var22, var23):var23.getBlock().a((Entity)null);
                        var14 -= (var24 + 0.3F) * 0.3F;
                     }

                     if(var14 > 0.0F && (this.source == null || this.source.a(this, this.world, var22, var23, var14))) {
                        var1.add(var22);
                     }

                     var15 += var6 * 0.30000001192092896D;
                     var17 += var8 * 0.30000001192092896D;
                     var19 += var10 * 0.30000001192092896D;
                  }
               }
            }
         }
      }

      this.blocks.addAll(var1);
      float var30 = this.size * 2.0F;
      var4 = MathHelper.floor(this.posX - (double)var30 - 1.0D);
      var5 = MathHelper.floor(this.posX + (double)var30 + 1.0D);
      int var31 = MathHelper.floor(this.posY - (double)var30 - 1.0D);
      int var7 = MathHelper.floor(this.posY + (double)var30 + 1.0D);
      int var32 = MathHelper.floor(this.posZ - (double)var30 - 1.0D);
      int var9 = MathHelper.floor(this.posZ + (double)var30 + 1.0D);
      List var33 = this.world.getEntities(this.source, new AxisAlignedBB((double)var4, (double)var31, (double)var32, (double)var5, (double)var7, (double)var9));
      Vec3D var11 = new Vec3D(this.posX, this.posY, this.posZ);

      for(int var34 = 0; var34 < var33.size(); ++var34) {
         Entity var13 = (Entity)var33.get(var34);
         if(!var13.aW()) {
            double var35 = var13.f(this.posX, this.posY, this.posZ) / (double)var30;
            if(var35 <= 1.0D) {
               double var16 = var13.locX - this.posX;
               double var18 = var13.locY + (double)var13.getHeadHeight() - this.posY;
               double var20 = var13.locZ - this.posZ;
               double var36 = (double)MathHelper.sqrt(var16 * var16 + var18 * var18 + var20 * var20);
               if(var36 != 0.0D) {
                  var16 /= var36;
                  var18 /= var36;
                  var20 /= var36;
                  double var37 = (double)this.world.a(var11, var13.getBoundingBox());
                  double var26 = (1.0D - var35) * var37;
                  var13.damageEntity(DamageSource.explosion(this), (float)((int)((var26 * var26 + var26) / 2.0D * 8.0D * (double)var30 + 1.0D)));
                  double var28 = EnchantmentProtection.a(var13, var26);
                  var13.motX += var16 * var28;
                  var13.motY += var18 * var28;
                  var13.motZ += var20 * var28;
                  if(var13 instanceof EntityHuman && !((EntityHuman)var13).abilities.isInvulnerable) {
                     this.k.put((EntityHuman)var13, new Vec3D(var16 * var26, var18 * var26, var20 * var26));
                  }
               }
            }
         }
      }

   }

   public void a(boolean var1) {
      this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
      if(this.size >= 2.0F && this.b) {
         this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D, new int[0]);
      } else {
         this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D, new int[0]);
      }

      Iterator var2;
      BlockPosition var3;
      if(this.b) {
         var2 = this.blocks.iterator();

         while(var2.hasNext()) {
            var3 = (BlockPosition)var2.next();
            Block var4 = this.world.getType(var3).getBlock();
            if(var1) {
               double var5 = (double)((float)var3.getX() + this.world.random.nextFloat());
               double var7 = (double)((float)var3.getY() + this.world.random.nextFloat());
               double var9 = (double)((float)var3.getZ() + this.world.random.nextFloat());
               double var11 = var5 - this.posX;
               double var13 = var7 - this.posY;
               double var15 = var9 - this.posZ;
               double var17 = (double)MathHelper.sqrt(var11 * var11 + var13 * var13 + var15 * var15);
               var11 /= var17;
               var13 /= var17;
               var15 /= var17;
               double var19 = 0.5D / (var17 / (double)this.size + 0.1D);
               var19 *= (double)(this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F);
               var11 *= var19;
               var13 *= var19;
               var15 *= var19;
               this.world.addParticle(EnumParticle.EXPLOSION_NORMAL, (var5 + this.posX * 1.0D) / 2.0D, (var7 + this.posY * 1.0D) / 2.0D, (var9 + this.posZ * 1.0D) / 2.0D, var11, var13, var15, new int[0]);
               this.world.addParticle(EnumParticle.SMOKE_NORMAL, var5, var7, var9, var11, var13, var15, new int[0]);
            }

            if(var4.getMaterial() != Material.AIR) {
               if(var4.a(this)) {
                  var4.dropNaturally(this.world, var3, this.world.getType(var3), 1.0F / this.size, 0);
               }

               this.world.setTypeAndData(var3, Blocks.AIR.getBlockData(), 3);
               var4.wasExploded(this.world, var3, this);
            }
         }
      }

      if(this.a) {
         var2 = this.blocks.iterator();

         while(var2.hasNext()) {
            var3 = (BlockPosition)var2.next();
            if(this.world.getType(var3).getBlock().getMaterial() == Material.AIR && this.world.getType(var3.down()).getBlock().o() && this.c.nextInt(3) == 0) {
               this.world.setTypeUpdate(var3, Blocks.FIRE.getBlockData());
            }
         }
      }

   }

   public Map<EntityHuman, Vec3D> b() {
      return this.k;
   }

   public EntityLiving c() {
      return this.source == null?null:(this.source instanceof EntityTNTPrimed?((EntityTNTPrimed)this.source).getSource():(this.source instanceof EntityLiving?(EntityLiving)this.source:null));
   }

   public void clearBlocks() {
      this.blocks.clear();
   }

   public List<BlockPosition> getBlocks() {
      return this.blocks;
   }
}
