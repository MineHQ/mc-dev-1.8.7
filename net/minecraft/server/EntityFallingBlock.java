package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.Block;
import net.minecraft.server.BlockAnvil;
import net.minecraft.server.BlockFalling;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IContainer;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class EntityFallingBlock extends Entity {
   private IBlockData block;
   public int ticksLived;
   public boolean dropItem = true;
   private boolean e;
   private boolean hurtEntities;
   private int fallHurtMax = 40;
   private float fallHurtAmount = 2.0F;
   public NBTTagCompound tileEntityData;

   public EntityFallingBlock(World var1) {
      super(var1);
   }

   public EntityFallingBlock(World var1, double var2, double var4, double var6, IBlockData var8) {
      super(var1);
      this.block = var8;
      this.k = true;
      this.setSize(0.98F, 0.98F);
      this.setPosition(var2, var4, var6);
      this.motX = 0.0D;
      this.motY = 0.0D;
      this.motZ = 0.0D;
      this.lastX = var2;
      this.lastY = var4;
      this.lastZ = var6;
   }

   protected boolean s_() {
      return false;
   }

   protected void h() {
   }

   public boolean ad() {
      return !this.dead;
   }

   public void t_() {
      Block var1 = this.block.getBlock();
      if(var1.getMaterial() == Material.AIR) {
         this.die();
      } else {
         this.lastX = this.locX;
         this.lastY = this.locY;
         this.lastZ = this.locZ;
         BlockPosition var2;
         if(this.ticksLived++ == 0) {
            var2 = new BlockPosition(this);
            if(this.world.getType(var2).getBlock() == var1) {
               this.world.setAir(var2);
            } else if(!this.world.isClientSide) {
               this.die();
               return;
            }
         }

         this.motY -= 0.03999999910593033D;
         this.move(this.motX, this.motY, this.motZ);
         this.motX *= 0.9800000190734863D;
         this.motY *= 0.9800000190734863D;
         this.motZ *= 0.9800000190734863D;
         if(!this.world.isClientSide) {
            var2 = new BlockPosition(this);
            if(this.onGround) {
               this.motX *= 0.699999988079071D;
               this.motZ *= 0.699999988079071D;
               this.motY *= -0.5D;
               if(this.world.getType(var2).getBlock() != Blocks.PISTON_EXTENSION) {
                  this.die();
                  if(!this.e) {
                     if(this.world.a(var1, var2, true, EnumDirection.UP, (Entity)null, (ItemStack)null) && !BlockFalling.canFall(this.world, var2.down()) && this.world.setTypeAndData(var2, this.block, 3)) {
                        if(var1 instanceof BlockFalling) {
                           ((BlockFalling)var1).a_(this.world, var2);
                        }

                        if(this.tileEntityData != null && var1 instanceof IContainer) {
                           TileEntity var3 = this.world.getTileEntity(var2);
                           if(var3 != null) {
                              NBTTagCompound var4 = new NBTTagCompound();
                              var3.b(var4);
                              Iterator var5 = this.tileEntityData.c().iterator();

                              while(var5.hasNext()) {
                                 String var6 = (String)var5.next();
                                 NBTBase var7 = this.tileEntityData.get(var6);
                                 if(!var6.equals("x") && !var6.equals("y") && !var6.equals("z")) {
                                    var4.set(var6, var7.clone());
                                 }
                              }

                              var3.a(var4);
                              var3.update();
                           }
                        }
                     } else if(this.dropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                        this.a(new ItemStack(var1, 1, var1.getDropData(this.block)), 0.0F);
                     }
                  }
               }
            } else if(this.ticksLived > 100 && !this.world.isClientSide && (var2.getY() < 1 || var2.getY() > 256) || this.ticksLived > 600) {
               if(this.dropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                  this.a(new ItemStack(var1, 1, var1.getDropData(this.block)), 0.0F);
               }

               this.die();
            }
         }

      }
   }

   public void e(float var1, float var2) {
      Block var3 = this.block.getBlock();
      if(this.hurtEntities) {
         int var4 = MathHelper.f(var1 - 1.0F);
         if(var4 > 0) {
            ArrayList var5 = Lists.newArrayList((Iterable)this.world.getEntities(this, this.getBoundingBox()));
            boolean var6 = var3 == Blocks.ANVIL;
            DamageSource var7 = var6?DamageSource.ANVIL:DamageSource.FALLING_BLOCK;
            Iterator var8 = var5.iterator();

            while(var8.hasNext()) {
               Entity var9 = (Entity)var8.next();
               var9.damageEntity(var7, (float)Math.min(MathHelper.d((float)var4 * this.fallHurtAmount), this.fallHurtMax));
            }

            if(var6 && (double)this.random.nextFloat() < 0.05000000074505806D + (double)var4 * 0.05D) {
               int var10 = ((Integer)this.block.get(BlockAnvil.DAMAGE)).intValue();
               ++var10;
               if(var10 > 2) {
                  this.e = true;
               } else {
                  this.block = this.block.set(BlockAnvil.DAMAGE, Integer.valueOf(var10));
               }
            }
         }
      }

   }

   protected void b(NBTTagCompound var1) {
      Block var2 = this.block != null?this.block.getBlock():Blocks.AIR;
      MinecraftKey var3 = (MinecraftKey)Block.REGISTRY.c(var2);
      var1.setString("Block", var3 == null?"":var3.toString());
      var1.setByte("Data", (byte)var2.toLegacyData(this.block));
      var1.setByte("Time", (byte)this.ticksLived);
      var1.setBoolean("DropItem", this.dropItem);
      var1.setBoolean("HurtEntities", this.hurtEntities);
      var1.setFloat("FallHurtAmount", this.fallHurtAmount);
      var1.setInt("FallHurtMax", this.fallHurtMax);
      if(this.tileEntityData != null) {
         var1.set("TileEntityData", this.tileEntityData);
      }

   }

   protected void a(NBTTagCompound var1) {
      int var2 = var1.getByte("Data") & 255;
      if(var1.hasKeyOfType("Block", 8)) {
         this.block = Block.getByName(var1.getString("Block")).fromLegacyData(var2);
      } else if(var1.hasKeyOfType("TileID", 99)) {
         this.block = Block.getById(var1.getInt("TileID")).fromLegacyData(var2);
      } else {
         this.block = Block.getById(var1.getByte("Tile") & 255).fromLegacyData(var2);
      }

      this.ticksLived = var1.getByte("Time") & 255;
      Block var3 = this.block.getBlock();
      if(var1.hasKeyOfType("HurtEntities", 99)) {
         this.hurtEntities = var1.getBoolean("HurtEntities");
         this.fallHurtAmount = var1.getFloat("FallHurtAmount");
         this.fallHurtMax = var1.getInt("FallHurtMax");
      } else if(var3 == Blocks.ANVIL) {
         this.hurtEntities = true;
      }

      if(var1.hasKeyOfType("DropItem", 99)) {
         this.dropItem = var1.getBoolean("DropItem");
      }

      if(var1.hasKeyOfType("TileEntityData", 10)) {
         this.tileEntityData = var1.getCompound("TileEntityData");
      }

      if(var3 == null || var3.getMaterial() == Material.AIR) {
         this.block = Blocks.SAND.getBlockData();
      }

   }

   public void a(boolean var1) {
      this.hurtEntities = var1;
   }

   public void appendEntityCrashDetails(CrashReportSystemDetails var1) {
      super.appendEntityCrashDetails(var1);
      if(this.block != null) {
         Block var2 = this.block.getBlock();
         var1.a((String)"Immitating block ID", (Object)Integer.valueOf(Block.getId(var2)));
         var1.a((String)"Immitating block data", (Object)Integer.valueOf(var2.toLegacyData(this.block)));
      }

   }

   public IBlockData getBlock() {
      return this.block;
   }
}
