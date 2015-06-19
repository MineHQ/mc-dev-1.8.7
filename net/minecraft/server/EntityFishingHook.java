package net.minecraft.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.ItemFish;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.PossibleFishingResult;
import net.minecraft.server.StatisticList;
import net.minecraft.server.Vec3D;
import net.minecraft.server.WeightedRandom;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public class EntityFishingHook extends Entity {
   private static final List<PossibleFishingResult> d;
   private static final List<PossibleFishingResult> e;
   private static final List<PossibleFishingResult> f;
   private int g = -1;
   private int h = -1;
   private int i = -1;
   private Block ar;
   private boolean as;
   public int a;
   public EntityHuman owner;
   private int at;
   private int au;
   private int av;
   private int aw;
   private int ax;
   private float ay;
   public Entity hooked;
   private int az;
   private double aA;
   private double aB;
   private double aC;
   private double aD;
   private double aE;

   public static List<PossibleFishingResult> j() {
      return f;
   }

   public EntityFishingHook(World var1) {
      super(var1);
      this.setSize(0.25F, 0.25F);
      this.ah = true;
   }

   public EntityFishingHook(World var1, EntityHuman var2) {
      super(var1);
      this.ah = true;
      this.owner = var2;
      this.owner.hookedFish = this;
      this.setSize(0.25F, 0.25F);
      this.setPositionRotation(var2.locX, var2.locY + (double)var2.getHeadHeight(), var2.locZ, var2.yaw, var2.pitch);
      this.locX -= (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.locY -= 0.10000000149011612D;
      this.locZ -= (double)(MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(this.locX, this.locY, this.locZ);
      float var3 = 0.4F;
      this.motX = (double)(-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var3);
      this.motZ = (double)(MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * var3);
      this.motY = (double)(-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * var3);
      this.c(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
   }

   protected void h() {
   }

   public void c(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var3 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var5 += this.random.nextGaussian() * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      this.motX = var1;
      this.motY = var3;
      this.motZ = var5;
      float var10 = MathHelper.sqrt(var1 * var1 + var5 * var5);
      this.lastYaw = this.yaw = (float)(MathHelper.b(var1, var5) * 180.0D / 3.1415927410125732D);
      this.lastPitch = this.pitch = (float)(MathHelper.b(var3, (double)var10) * 180.0D / 3.1415927410125732D);
      this.at = 0;
   }

   public void t_() {
      super.t_();
      if(this.az > 0) {
         double var29 = this.locX + (this.aA - this.locX) / (double)this.az;
         double var30 = this.locY + (this.aB - this.locY) / (double)this.az;
         double var31 = this.locZ + (this.aC - this.locZ) / (double)this.az;
         double var7 = MathHelper.g(this.aD - (double)this.yaw);
         this.yaw = (float)((double)this.yaw + var7 / (double)this.az);
         this.pitch = (float)((double)this.pitch + (this.aE - (double)this.pitch) / (double)this.az);
         --this.az;
         this.setPosition(var29, var30, var31);
         this.setYawPitch(this.yaw, this.pitch);
      } else {
         if(!this.world.isClientSide) {
            ItemStack var1 = this.owner.bZ();
            if(this.owner.dead || !this.owner.isAlive() || var1 == null || var1.getItem() != Items.FISHING_ROD || this.h(this.owner) > 1024.0D) {
               this.die();
               this.owner.hookedFish = null;
               return;
            }

            if(this.hooked != null) {
               if(!this.hooked.dead) {
                  this.locX = this.hooked.locX;
                  double var10002 = (double)this.hooked.length;
                  this.locY = this.hooked.getBoundingBox().b + var10002 * 0.8D;
                  this.locZ = this.hooked.locZ;
                  return;
               }

               this.hooked = null;
            }
         }

         if(this.a > 0) {
            --this.a;
         }

         if(this.as) {
            if(this.world.getType(new BlockPosition(this.g, this.h, this.i)).getBlock() == this.ar) {
               ++this.at;
               if(this.at == 1200) {
                  this.die();
               }

               return;
            }

            this.as = false;
            this.motX *= (double)(this.random.nextFloat() * 0.2F);
            this.motY *= (double)(this.random.nextFloat() * 0.2F);
            this.motZ *= (double)(this.random.nextFloat() * 0.2F);
            this.at = 0;
            this.au = 0;
         } else {
            ++this.au;
         }

         Vec3D var28 = new Vec3D(this.locX, this.locY, this.locZ);
         Vec3D var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         MovingObjectPosition var3 = this.world.rayTrace(var28, var2);
         var28 = new Vec3D(this.locX, this.locY, this.locZ);
         var2 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         if(var3 != null) {
            var2 = new Vec3D(var3.pos.a, var3.pos.b, var3.pos.c);
         }

         Entity var4 = null;
         List var5 = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
         double var6 = 0.0D;

         double var13;
         for(int var8 = 0; var8 < var5.size(); ++var8) {
            Entity var9 = (Entity)var5.get(var8);
            if(var9.ad() && (var9 != this.owner || this.au >= 5)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.getBoundingBox().grow((double)var10, (double)var10, (double)var10);
               MovingObjectPosition var12 = var11.a(var28, var2);
               if(var12 != null) {
                  var13 = var28.distanceSquared(var12.pos);
                  if(var13 < var6 || var6 == 0.0D) {
                     var4 = var9;
                     var6 = var13;
                  }
               }
            }
         }

         if(var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }

         if(var3 != null) {
            if(var3.entity != null) {
               if(var3.entity.damageEntity(DamageSource.projectile(this, this.owner), 0.0F)) {
                  this.hooked = var3.entity;
               }
            } else {
               this.as = true;
            }
         }

         if(!this.as) {
            this.move(this.motX, this.motY, this.motZ);
            float var32 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
            this.yaw = (float)(MathHelper.b(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

            for(this.pitch = (float)(MathHelper.b(this.motY, (double)var32) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
               ;
            }

            while(this.pitch - this.lastPitch >= 180.0F) {
               this.lastPitch += 360.0F;
            }

            while(this.yaw - this.lastYaw < -180.0F) {
               this.lastYaw -= 360.0F;
            }

            while(this.yaw - this.lastYaw >= 180.0F) {
               this.lastYaw += 360.0F;
            }

            this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
            this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
            float var33 = 0.92F;
            if(this.onGround || this.positionChanged) {
               var33 = 0.5F;
            }

            byte var34 = 5;
            double var35 = 0.0D;

            double var19;
            for(int var36 = 0; var36 < var34; ++var36) {
               AxisAlignedBB var14 = this.getBoundingBox();
               double var15 = var14.e - var14.b;
               double var17 = var14.b + var15 * (double)var36 / (double)var34;
               var19 = var14.b + var15 * (double)(var36 + 1) / (double)var34;
               AxisAlignedBB var21 = new AxisAlignedBB(var14.a, var17, var14.c, var14.d, var19, var14.f);
               if(this.world.b(var21, Material.WATER)) {
                  var35 += 1.0D / (double)var34;
               }
            }

            if(!this.world.isClientSide && var35 > 0.0D) {
               WorldServer var37 = (WorldServer)this.world;
               int var38 = 1;
               BlockPosition var39 = (new BlockPosition(this)).up();
               if(this.random.nextFloat() < 0.25F && this.world.isRainingAt(var39)) {
                  var38 = 2;
               }

               if(this.random.nextFloat() < 0.5F && !this.world.i(var39)) {
                  --var38;
               }

               if(this.av > 0) {
                  --this.av;
                  if(this.av <= 0) {
                     this.aw = 0;
                     this.ax = 0;
                  }
               } else {
                  float var16;
                  float var18;
                  double var23;
                  Block var25;
                  float var40;
                  double var41;
                  if(this.ax > 0) {
                     this.ax -= var38;
                     if(this.ax <= 0) {
                        this.motY -= 0.20000000298023224D;
                        this.makeSound("random.splash", 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                        var16 = (float)MathHelper.floor(this.getBoundingBox().b);
                        var37.a(EnumParticle.WATER_BUBBLE, this.locX, (double)(var16 + 1.0F), this.locZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D, new int[0]);
                        var37.a(EnumParticle.WATER_WAKE, this.locX, (double)(var16 + 1.0F), this.locZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D, new int[0]);
                        this.av = MathHelper.nextInt(this.random, 10, 30);
                     } else {
                        this.ay = (float)((double)this.ay + this.random.nextGaussian() * 4.0D);
                        var16 = this.ay * 0.017453292F;
                        var40 = MathHelper.sin(var16);
                        var18 = MathHelper.cos(var16);
                        var19 = this.locX + (double)(var40 * (float)this.ax * 0.1F);
                        var41 = (double)((float)MathHelper.floor(this.getBoundingBox().b) + 1.0F);
                        var23 = this.locZ + (double)(var18 * (float)this.ax * 0.1F);
                        var25 = var37.getType(new BlockPosition((int)var19, (int)var41 - 1, (int)var23)).getBlock();
                        if(var25 == Blocks.WATER || var25 == Blocks.FLOWING_WATER) {
                           if(this.random.nextFloat() < 0.15F) {
                              var37.a(EnumParticle.WATER_BUBBLE, var19, var41 - 0.10000000149011612D, var23, 1, (double)var40, 0.1D, (double)var18, 0.0D, new int[0]);
                           }

                           float var26 = var40 * 0.04F;
                           float var27 = var18 * 0.04F;
                           var37.a(EnumParticle.WATER_WAKE, var19, var41, var23, 0, (double)var27, 0.01D, (double)(-var26), 1.0D, new int[0]);
                           var37.a(EnumParticle.WATER_WAKE, var19, var41, var23, 0, (double)(-var27), 0.01D, (double)var26, 1.0D, new int[0]);
                        }
                     }
                  } else if(this.aw > 0) {
                     this.aw -= var38;
                     var16 = 0.15F;
                     if(this.aw < 20) {
                        var16 = (float)((double)var16 + (double)(20 - this.aw) * 0.05D);
                     } else if(this.aw < 40) {
                        var16 = (float)((double)var16 + (double)(40 - this.aw) * 0.02D);
                     } else if(this.aw < 60) {
                        var16 = (float)((double)var16 + (double)(60 - this.aw) * 0.01D);
                     }

                     if(this.random.nextFloat() < var16) {
                        var40 = MathHelper.a(this.random, 0.0F, 360.0F) * 0.017453292F;
                        var18 = MathHelper.a(this.random, 25.0F, 60.0F);
                        var19 = this.locX + (double)(MathHelper.sin(var40) * var18 * 0.1F);
                        var41 = (double)((float)MathHelper.floor(this.getBoundingBox().b) + 1.0F);
                        var23 = this.locZ + (double)(MathHelper.cos(var40) * var18 * 0.1F);
                        var25 = var37.getType(new BlockPosition((int)var19, (int)var41 - 1, (int)var23)).getBlock();
                        if(var25 == Blocks.WATER || var25 == Blocks.FLOWING_WATER) {
                           var37.a(EnumParticle.WATER_SPLASH, var19, var41, var23, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
                        }
                     }

                     if(this.aw <= 0) {
                        this.ay = MathHelper.a(this.random, 0.0F, 360.0F);
                        this.ax = MathHelper.nextInt(this.random, 20, 80);
                     }
                  } else {
                     this.aw = MathHelper.nextInt(this.random, 100, 900);
                     this.aw -= EnchantmentManager.h(this.owner) * 20 * 5;
                  }
               }

               if(this.av > 0) {
                  this.motY -= (double)(this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat()) * 0.2D;
               }
            }

            var13 = var35 * 2.0D - 1.0D;
            this.motY += 0.03999999910593033D * var13;
            if(var35 > 0.0D) {
               var33 = (float)((double)var33 * 0.9D);
               this.motY *= 0.8D;
            }

            this.motX *= (double)var33;
            this.motY *= (double)var33;
            this.motZ *= (double)var33;
            this.setPosition(this.locX, this.locY, this.locZ);
         }
      }
   }

   public void b(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.g);
      var1.setShort("yTile", (short)this.h);
      var1.setShort("zTile", (short)this.i);
      MinecraftKey var2 = (MinecraftKey)Block.REGISTRY.c(this.ar);
      var1.setString("inTile", var2 == null?"":var2.toString());
      var1.setByte("shake", (byte)this.a);
      var1.setByte("inGround", (byte)(this.as?1:0));
   }

   public void a(NBTTagCompound var1) {
      this.g = var1.getShort("xTile");
      this.h = var1.getShort("yTile");
      this.i = var1.getShort("zTile");
      if(var1.hasKeyOfType("inTile", 8)) {
         this.ar = Block.getByName(var1.getString("inTile"));
      } else {
         this.ar = Block.getById(var1.getByte("inTile") & 255);
      }

      this.a = var1.getByte("shake") & 255;
      this.as = var1.getByte("inGround") == 1;
   }

   public int l() {
      if(this.world.isClientSide) {
         return 0;
      } else {
         byte var1 = 0;
         if(this.hooked != null) {
            double var2 = this.owner.locX - this.locX;
            double var4 = this.owner.locY - this.locY;
            double var6 = this.owner.locZ - this.locZ;
            double var8 = (double)MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1D;
            this.hooked.motX += var2 * var10;
            this.hooked.motY += var4 * var10 + (double)MathHelper.sqrt(var8) * 0.08D;
            this.hooked.motZ += var6 * var10;
            var1 = 3;
         } else if(this.av > 0) {
            EntityItem var13 = new EntityItem(this.world, this.locX, this.locY, this.locZ, this.m());
            double var3 = this.owner.locX - this.locX;
            double var5 = this.owner.locY - this.locY;
            double var7 = this.owner.locZ - this.locZ;
            double var9 = (double)MathHelper.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1D;
            var13.motX = var3 * var11;
            var13.motY = var5 * var11 + (double)MathHelper.sqrt(var9) * 0.08D;
            var13.motZ = var7 * var11;
            this.world.addEntity(var13);
            this.owner.world.addEntity(new EntityExperienceOrb(this.owner.world, this.owner.locX, this.owner.locY + 0.5D, this.owner.locZ + 0.5D, this.random.nextInt(6) + 1));
            var1 = 1;
         }

         if(this.as) {
            var1 = 2;
         }

         this.die();
         this.owner.hookedFish = null;
         return var1;
      }
   }

   private ItemStack m() {
      float var1 = this.world.random.nextFloat();
      int var2 = EnchantmentManager.g(this.owner);
      int var3 = EnchantmentManager.h(this.owner);
      float var4 = 0.1F - (float)var2 * 0.025F - (float)var3 * 0.01F;
      float var5 = 0.05F + (float)var2 * 0.01F - (float)var3 * 0.01F;
      var4 = MathHelper.a(var4, 0.0F, 1.0F);
      var5 = MathHelper.a(var5, 0.0F, 1.0F);
      if(var1 < var4) {
         this.owner.b(StatisticList.D);
         return ((PossibleFishingResult)WeightedRandom.a(this.random, d)).a(this.random);
      } else {
         var1 -= var4;
         if(var1 < var5) {
            this.owner.b(StatisticList.E);
            return ((PossibleFishingResult)WeightedRandom.a(this.random, e)).a(this.random);
         } else {
            float var10000 = var1 - var5;
            this.owner.b(StatisticList.C);
            return ((PossibleFishingResult)WeightedRandom.a(this.random, f)).a(this.random);
         }
      }
   }

   public void die() {
      super.die();
      if(this.owner != null) {
         this.owner.hookedFish = null;
      }

   }

   static {
      d = Arrays.asList(new PossibleFishingResult[]{(new PossibleFishingResult(new ItemStack(Items.LEATHER_BOOTS), 10)).a(0.9F), new PossibleFishingResult(new ItemStack(Items.LEATHER), 10), new PossibleFishingResult(new ItemStack(Items.BONE), 10), new PossibleFishingResult(new ItemStack(Items.POTION), 10), new PossibleFishingResult(new ItemStack(Items.STRING), 5), (new PossibleFishingResult(new ItemStack(Items.FISHING_ROD), 2)).a(0.9F), new PossibleFishingResult(new ItemStack(Items.BOWL), 10), new PossibleFishingResult(new ItemStack(Items.STICK), 5), new PossibleFishingResult(new ItemStack(Items.DYE, 10, EnumColor.BLACK.getInvColorIndex()), 1), new PossibleFishingResult(new ItemStack(Blocks.TRIPWIRE_HOOK), 10), new PossibleFishingResult(new ItemStack(Items.ROTTEN_FLESH), 10)});
      e = Arrays.asList(new PossibleFishingResult[]{new PossibleFishingResult(new ItemStack(Blocks.WATERLILY), 1), new PossibleFishingResult(new ItemStack(Items.NAME_TAG), 1), new PossibleFishingResult(new ItemStack(Items.SADDLE), 1), (new PossibleFishingResult(new ItemStack(Items.BOW), 1)).a(0.25F).a(), (new PossibleFishingResult(new ItemStack(Items.FISHING_ROD), 1)).a(0.25F).a(), (new PossibleFishingResult(new ItemStack(Items.BOOK), 1)).a()});
      f = Arrays.asList(new PossibleFishingResult[]{new PossibleFishingResult(new ItemStack(Items.FISH, 1, ItemFish.EnumFish.COD.a()), 60), new PossibleFishingResult(new ItemStack(Items.FISH, 1, ItemFish.EnumFish.SALMON.a()), 25), new PossibleFishingResult(new ItemStack(Items.FISH, 1, ItemFish.EnumFish.CLOWNFISH.a()), 2), new PossibleFishingResult(new ItemStack(Items.FISH, 1, ItemFish.EnumFish.PUFFERFISH.a()), 13)});
   }
}
