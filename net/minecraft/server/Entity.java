package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCobbleWall;
import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockFenceGate;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.DamageSource;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.EnchantmentProtection;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.Explosion;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.ReportedException;
import net.minecraft.server.ShapeDetector;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;

public abstract class Entity implements ICommandListener {
   private static final AxisAlignedBB a = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
   private static int entityCount;
   private int id;
   public double j;
   public boolean k;
   public Entity passenger;
   public Entity vehicle;
   public boolean attachedToPlayer;
   public World world;
   public double lastX;
   public double lastY;
   public double lastZ;
   public double locX;
   public double locY;
   public double locZ;
   public double motX;
   public double motY;
   public double motZ;
   public float yaw;
   public float pitch;
   public float lastYaw;
   public float lastPitch;
   private AxisAlignedBB boundingBox;
   public boolean onGround;
   public boolean positionChanged;
   public boolean E;
   public boolean F;
   public boolean velocityChanged;
   protected boolean H;
   private boolean g;
   public boolean dead;
   public float width;
   public float length;
   public float L;
   public float M;
   public float N;
   public float fallDistance;
   private int h;
   public double P;
   public double Q;
   public double R;
   public float S;
   public boolean noclip;
   public float U;
   protected Random random;
   public int ticksLived;
   public int maxFireTicks;
   public int fireTicks;
   protected boolean inWater;
   public int noDamageTicks;
   protected boolean justCreated;
   protected boolean fireProof;
   protected DataWatcher datawatcher;
   private double ar;
   private double as;
   public boolean ad;
   public int ae;
   public int af;
   public int ag;
   public boolean ah;
   public boolean ai;
   public int portalCooldown;
   protected boolean ak;
   protected int al;
   public int dimension;
   protected BlockPosition an;
   protected Vec3D ao;
   protected EnumDirection ap;
   private boolean invulnerable;
   protected UUID uniqueID;
   private final CommandObjectiveExecutor au;

   public int getId() {
      return this.id;
   }

   public void d(int var1) {
      this.id = var1;
   }

   public void G() {
      this.die();
   }

   public Entity(World var1) {
      this.id = entityCount++;
      this.j = 1.0D;
      this.boundingBox = a;
      this.width = 0.6F;
      this.length = 1.8F;
      this.h = 1;
      this.random = new Random();
      this.maxFireTicks = 1;
      this.justCreated = true;
      this.uniqueID = MathHelper.a(this.random);
      this.au = new CommandObjectiveExecutor();
      this.world = var1;
      this.setPosition(0.0D, 0.0D, 0.0D);
      if(var1 != null) {
         this.dimension = var1.worldProvider.getDimension();
      }

      this.datawatcher = new DataWatcher(this);
      this.datawatcher.a(0, Byte.valueOf((byte)0));
      this.datawatcher.a(1, Short.valueOf((short)300));
      this.datawatcher.a(3, Byte.valueOf((byte)0));
      this.datawatcher.a(2, "");
      this.datawatcher.a(4, Byte.valueOf((byte)0));
      this.h();
   }

   protected abstract void h();

   public DataWatcher getDataWatcher() {
      return this.datawatcher;
   }

   public boolean equals(Object var1) {
      return var1 instanceof Entity?((Entity)var1).id == this.id:false;
   }

   public int hashCode() {
      return this.id;
   }

   public void die() {
      this.dead = true;
   }

   public void setSize(float var1, float var2) {
      if(var1 != this.width || var2 != this.length) {
         float var3 = this.width;
         this.width = var1;
         this.length = var2;
         this.a(new AxisAlignedBB(this.getBoundingBox().a, this.getBoundingBox().b, this.getBoundingBox().c, this.getBoundingBox().a + (double)this.width, this.getBoundingBox().b + (double)this.length, this.getBoundingBox().c + (double)this.width));
         if(this.width > var3 && !this.justCreated && !this.world.isClientSide) {
            this.move((double)(var3 - this.width), 0.0D, (double)(var3 - this.width));
         }
      }

   }

   protected void setYawPitch(float var1, float var2) {
      this.yaw = var1 % 360.0F;
      this.pitch = var2 % 360.0F;
   }

   public void setPosition(double var1, double var3, double var5) {
      this.locX = var1;
      this.locY = var3;
      this.locZ = var5;
      float var7 = this.width / 2.0F;
      float var8 = this.length;
      this.a(new AxisAlignedBB(var1 - (double)var7, var3, var5 - (double)var7, var1 + (double)var7, var3 + (double)var8, var5 + (double)var7));
   }

   public void t_() {
      this.K();
   }

   public void K() {
      this.world.methodProfiler.a("entityBaseTick");
      if(this.vehicle != null && this.vehicle.dead) {
         this.vehicle = null;
      }

      this.L = this.M;
      this.lastX = this.locX;
      this.lastY = this.locY;
      this.lastZ = this.locZ;
      this.lastPitch = this.pitch;
      this.lastYaw = this.yaw;
      if(!this.world.isClientSide && this.world instanceof WorldServer) {
         this.world.methodProfiler.a("portal");
         MinecraftServer var1 = ((WorldServer)this.world).getMinecraftServer();
         int var2 = this.L();
         if(this.ak) {
            if(var1.getAllowNether()) {
               if(this.vehicle == null && this.al++ >= var2) {
                  this.al = var2;
                  this.portalCooldown = this.aq();
                  byte var3;
                  if(this.world.worldProvider.getDimension() == -1) {
                     var3 = 0;
                  } else {
                     var3 = -1;
                  }

                  this.c(var3);
               }

               this.ak = false;
            }
         } else {
            if(this.al > 0) {
               this.al -= 4;
            }

            if(this.al < 0) {
               this.al = 0;
            }
         }

         if(this.portalCooldown > 0) {
            --this.portalCooldown;
         }

         this.world.methodProfiler.b();
      }

      this.Y();
      this.W();
      if(this.world.isClientSide) {
         this.fireTicks = 0;
      } else if(this.fireTicks > 0) {
         if(this.fireProof) {
            this.fireTicks -= 4;
            if(this.fireTicks < 0) {
               this.fireTicks = 0;
            }
         } else {
            if(this.fireTicks % 20 == 0) {
               this.damageEntity(DamageSource.BURN, 1.0F);
            }

            --this.fireTicks;
         }
      }

      if(this.ab()) {
         this.burnFromLava();
         this.fallDistance *= 0.5F;
      }

      if(this.locY < -64.0D) {
         this.O();
      }

      if(!this.world.isClientSide) {
         this.b(0, this.fireTicks > 0);
      }

      this.justCreated = false;
      this.world.methodProfiler.b();
   }

   public int L() {
      return 0;
   }

   protected void burnFromLava() {
      if(!this.fireProof) {
         this.damageEntity(DamageSource.LAVA, 4.0F);
         this.setOnFire(15);
      }
   }

   public void setOnFire(int var1) {
      int var2 = var1 * 20;
      var2 = EnchantmentProtection.a(this, var2);
      if(this.fireTicks < var2) {
         this.fireTicks = var2;
      }

   }

   public void extinguish() {
      this.fireTicks = 0;
   }

   protected void O() {
      this.die();
   }

   public boolean c(double var1, double var3, double var5) {
      AxisAlignedBB var7 = this.getBoundingBox().c(var1, var3, var5);
      return this.b(var7);
   }

   private boolean b(AxisAlignedBB var1) {
      return this.world.getCubes(this, var1).isEmpty() && !this.world.containsLiquid(var1);
   }

   public void move(double var1, double var3, double var5) {
      if(this.noclip) {
         this.a(this.getBoundingBox().c(var1, var3, var5));
         this.recalcPosition();
      } else {
         this.world.methodProfiler.a("move");
         double var7 = this.locX;
         double var9 = this.locY;
         double var11 = this.locZ;
         if(this.H) {
            this.H = false;
            var1 *= 0.25D;
            var3 *= 0.05000000074505806D;
            var5 *= 0.25D;
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
         }

         double var13 = var1;
         double var15 = var3;
         double var17 = var5;
         boolean var19 = this.onGround && this.isSneaking() && this instanceof EntityHuman;
         if(var19) {
            double var20;
            for(var20 = 0.05D; var1 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(var1, -1.0D, 0.0D)).isEmpty(); var13 = var1) {
               if(var1 < var20 && var1 >= -var20) {
                  var1 = 0.0D;
               } else if(var1 > 0.0D) {
                  var1 -= var20;
               } else {
                  var1 += var20;
               }
            }

            for(; var5 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(0.0D, -1.0D, var5)).isEmpty(); var17 = var5) {
               if(var5 < var20 && var5 >= -var20) {
                  var5 = 0.0D;
               } else if(var5 > 0.0D) {
                  var5 -= var20;
               } else {
                  var5 += var20;
               }
            }

            for(; var1 != 0.0D && var5 != 0.0D && this.world.getCubes(this, this.getBoundingBox().c(var1, -1.0D, var5)).isEmpty(); var17 = var5) {
               if(var1 < var20 && var1 >= -var20) {
                  var1 = 0.0D;
               } else if(var1 > 0.0D) {
                  var1 -= var20;
               } else {
                  var1 += var20;
               }

               var13 = var1;
               if(var5 < var20 && var5 >= -var20) {
                  var5 = 0.0D;
               } else if(var5 > 0.0D) {
                  var5 -= var20;
               } else {
                  var5 += var20;
               }
            }
         }

         List var53 = this.world.getCubes(this, this.getBoundingBox().a(var1, var3, var5));
         AxisAlignedBB var21 = this.getBoundingBox();

         AxisAlignedBB var23;
         for(Iterator var22 = var53.iterator(); var22.hasNext(); var3 = var23.b(this.getBoundingBox(), var3)) {
            var23 = (AxisAlignedBB)var22.next();
         }

         this.a(this.getBoundingBox().c(0.0D, var3, 0.0D));
         boolean var54 = this.onGround || var15 != var3 && var15 < 0.0D;

         AxisAlignedBB var24;
         Iterator var55;
         for(var55 = var53.iterator(); var55.hasNext(); var1 = var24.a(this.getBoundingBox(), var1)) {
            var24 = (AxisAlignedBB)var55.next();
         }

         this.a(this.getBoundingBox().c(var1, 0.0D, 0.0D));

         for(var55 = var53.iterator(); var55.hasNext(); var5 = var24.c(this.getBoundingBox(), var5)) {
            var24 = (AxisAlignedBB)var55.next();
         }

         this.a(this.getBoundingBox().c(0.0D, 0.0D, var5));
         if(this.S > 0.0F && var54 && (var13 != var1 || var17 != var5)) {
            double var56 = var1;
            double var25 = var3;
            double var27 = var5;
            AxisAlignedBB var29 = this.getBoundingBox();
            this.a(var21);
            var3 = (double)this.S;
            List var30 = this.world.getCubes(this, this.getBoundingBox().a(var13, var3, var17));
            AxisAlignedBB var31 = this.getBoundingBox();
            AxisAlignedBB var32 = var31.a(var13, 0.0D, var17);
            double var33 = var3;

            AxisAlignedBB var36;
            for(Iterator var35 = var30.iterator(); var35.hasNext(); var33 = var36.b(var32, var33)) {
               var36 = (AxisAlignedBB)var35.next();
            }

            var31 = var31.c(0.0D, var33, 0.0D);
            double var67 = var13;

            AxisAlignedBB var38;
            for(Iterator var37 = var30.iterator(); var37.hasNext(); var67 = var38.a(var31, var67)) {
               var38 = (AxisAlignedBB)var37.next();
            }

            var31 = var31.c(var67, 0.0D, 0.0D);
            double var68 = var17;

            AxisAlignedBB var40;
            for(Iterator var39 = var30.iterator(); var39.hasNext(); var68 = var40.c(var31, var68)) {
               var40 = (AxisAlignedBB)var39.next();
            }

            var31 = var31.c(0.0D, 0.0D, var68);
            AxisAlignedBB var69 = this.getBoundingBox();
            double var70 = var3;

            AxisAlignedBB var43;
            for(Iterator var42 = var30.iterator(); var42.hasNext(); var70 = var43.b(var69, var70)) {
               var43 = (AxisAlignedBB)var42.next();
            }

            var69 = var69.c(0.0D, var70, 0.0D);
            double var71 = var13;

            AxisAlignedBB var45;
            for(Iterator var44 = var30.iterator(); var44.hasNext(); var71 = var45.a(var69, var71)) {
               var45 = (AxisAlignedBB)var44.next();
            }

            var69 = var69.c(var71, 0.0D, 0.0D);
            double var72 = var17;

            AxisAlignedBB var47;
            for(Iterator var46 = var30.iterator(); var46.hasNext(); var72 = var47.c(var69, var72)) {
               var47 = (AxisAlignedBB)var46.next();
            }

            var69 = var69.c(0.0D, 0.0D, var72);
            double var73 = var67 * var67 + var68 * var68;
            double var48 = var71 * var71 + var72 * var72;
            if(var73 > var48) {
               var1 = var67;
               var5 = var68;
               var3 = -var33;
               this.a(var31);
            } else {
               var1 = var71;
               var5 = var72;
               var3 = -var70;
               this.a(var69);
            }

            AxisAlignedBB var51;
            for(Iterator var50 = var30.iterator(); var50.hasNext(); var3 = var51.b(this.getBoundingBox(), var3)) {
               var51 = (AxisAlignedBB)var50.next();
            }

            this.a(this.getBoundingBox().c(0.0D, var3, 0.0D));
            if(var56 * var56 + var27 * var27 >= var1 * var1 + var5 * var5) {
               var1 = var56;
               var3 = var25;
               var5 = var27;
               this.a(var29);
            }
         }

         this.world.methodProfiler.b();
         this.world.methodProfiler.a("rest");
         this.recalcPosition();
         this.positionChanged = var13 != var1 || var17 != var5;
         this.E = var15 != var3;
         this.onGround = this.E && var15 < 0.0D;
         this.F = this.positionChanged || this.E;
         int var57 = MathHelper.floor(this.locX);
         int var58 = MathHelper.floor(this.locY - 0.20000000298023224D);
         int var59 = MathHelper.floor(this.locZ);
         BlockPosition var26 = new BlockPosition(var57, var58, var59);
         Block var60 = this.world.getType(var26).getBlock();
         if(var60.getMaterial() == Material.AIR) {
            Block var28 = this.world.getType(var26.down()).getBlock();
            if(var28 instanceof BlockFence || var28 instanceof BlockCobbleWall || var28 instanceof BlockFenceGate) {
               var60 = var28;
               var26 = var26.down();
            }
         }

         this.a(var3, this.onGround, var60, var26);
         if(var13 != var1) {
            this.motX = 0.0D;
         }

         if(var17 != var5) {
            this.motZ = 0.0D;
         }

         if(var15 != var3) {
            var60.a(this.world, this);
         }

         if(this.s_() && !var19 && this.vehicle == null) {
            double var61 = this.locX - var7;
            double var64 = this.locY - var9;
            double var66 = this.locZ - var11;
            if(var60 != Blocks.LADDER) {
               var64 = 0.0D;
            }

            if(var60 != null && this.onGround) {
               var60.a(this.world, var26, this);
            }

            this.M = (float)((double)this.M + (double)MathHelper.sqrt(var61 * var61 + var66 * var66) * 0.6D);
            this.N = (float)((double)this.N + (double)MathHelper.sqrt(var61 * var61 + var64 * var64 + var66 * var66) * 0.6D);
            if(this.N > (float)this.h && var60.getMaterial() != Material.AIR) {
               this.h = (int)this.N + 1;
               if(this.V()) {
                  float var34 = MathHelper.sqrt(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.35F;
                  if(var34 > 1.0F) {
                     var34 = 1.0F;
                  }

                  this.makeSound(this.P(), var34, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
               }

               this.a(var26, var60);
            }
         }

         try {
            this.checkBlockCollisions();
         } catch (Throwable var52) {
            CrashReport var63 = CrashReport.a(var52, "Checking entity block collision");
            CrashReportSystemDetails var65 = var63.a("Entity being checked for collision");
            this.appendEntityCrashDetails(var65);
            throw new ReportedException(var63);
         }

         boolean var62 = this.U();
         if(this.world.e(this.getBoundingBox().shrink(0.001D, 0.001D, 0.001D))) {
            this.burn(1);
            if(!var62) {
               ++this.fireTicks;
               if(this.fireTicks == 0) {
                  this.setOnFire(8);
               }
            }
         } else if(this.fireTicks <= 0) {
            this.fireTicks = -this.maxFireTicks;
         }

         if(var62 && this.fireTicks > 0) {
            this.makeSound("random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
            this.fireTicks = -this.maxFireTicks;
         }

         this.world.methodProfiler.b();
      }
   }

   private void recalcPosition() {
      this.locX = (this.getBoundingBox().a + this.getBoundingBox().d) / 2.0D;
      this.locY = this.getBoundingBox().b;
      this.locZ = (this.getBoundingBox().c + this.getBoundingBox().f) / 2.0D;
   }

   protected String P() {
      return "game.neutral.swim";
   }

   protected void checkBlockCollisions() {
      BlockPosition var1 = new BlockPosition(this.getBoundingBox().a + 0.001D, this.getBoundingBox().b + 0.001D, this.getBoundingBox().c + 0.001D);
      BlockPosition var2 = new BlockPosition(this.getBoundingBox().d - 0.001D, this.getBoundingBox().e - 0.001D, this.getBoundingBox().f - 0.001D);
      if(this.world.areChunksLoadedBetween(var1, var2)) {
         for(int var3 = var1.getX(); var3 <= var2.getX(); ++var3) {
            for(int var4 = var1.getY(); var4 <= var2.getY(); ++var4) {
               for(int var5 = var1.getZ(); var5 <= var2.getZ(); ++var5) {
                  BlockPosition var6 = new BlockPosition(var3, var4, var5);
                  IBlockData var7 = this.world.getType(var6);

                  try {
                     var7.getBlock().a(this.world, var6, var7, this);
                  } catch (Throwable var11) {
                     CrashReport var9 = CrashReport.a(var11, "Colliding entity with block");
                     CrashReportSystemDetails var10 = var9.a("Block being collided with");
                     CrashReportSystemDetails.a(var10, var6, var7);
                     throw new ReportedException(var9);
                  }
               }
            }
         }
      }

   }

   protected void a(BlockPosition var1, Block var2) {
      Block.StepSound var3 = var2.stepSound;
      if(this.world.getType(var1.up()).getBlock() == Blocks.SNOW_LAYER) {
         var3 = Blocks.SNOW_LAYER.stepSound;
         this.makeSound(var3.getStepSound(), var3.getVolume1() * 0.15F, var3.getVolume2());
      } else if(!var2.getMaterial().isLiquid()) {
         this.makeSound(var3.getStepSound(), var3.getVolume1() * 0.15F, var3.getVolume2());
      }

   }

   public void makeSound(String var1, float var2, float var3) {
      if(!this.R()) {
         this.world.makeSound(this, var1, var2, var3);
      }

   }

   public boolean R() {
      return this.datawatcher.getByte(4) == 1;
   }

   public void b(boolean var1) {
      this.datawatcher.watch(4, Byte.valueOf((byte)(var1?1:0)));
   }

   protected boolean s_() {
      return true;
   }

   protected void a(double var1, boolean var3, Block var4, BlockPosition var5) {
      if(var3) {
         if(this.fallDistance > 0.0F) {
            if(var4 != null) {
               var4.a(this.world, var5, this, this.fallDistance);
            } else {
               this.e(this.fallDistance, 1.0F);
            }

            this.fallDistance = 0.0F;
         }
      } else if(var1 < 0.0D) {
         this.fallDistance = (float)((double)this.fallDistance - var1);
      }

   }

   public AxisAlignedBB S() {
      return null;
   }

   protected void burn(int var1) {
      if(!this.fireProof) {
         this.damageEntity(DamageSource.FIRE, (float)var1);
      }

   }

   public final boolean isFireProof() {
      return this.fireProof;
   }

   public void e(float var1, float var2) {
      if(this.passenger != null) {
         this.passenger.e(var1, var2);
      }

   }

   public boolean U() {
      return this.inWater || this.world.isRainingAt(new BlockPosition(this.locX, this.locY, this.locZ)) || this.world.isRainingAt(new BlockPosition(this.locX, this.locY + (double)this.length, this.locZ));
   }

   public boolean V() {
      return this.inWater;
   }

   public boolean W() {
      if(this.world.a(this.getBoundingBox().grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D, 0.001D, 0.001D), Material.WATER, this)) {
         if(!this.inWater && !this.justCreated) {
            this.X();
         }

         this.fallDistance = 0.0F;
         this.inWater = true;
         this.fireTicks = 0;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   protected void X() {
      float var1 = MathHelper.sqrt(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.2F;
      if(var1 > 1.0F) {
         var1 = 1.0F;
      }

      this.makeSound(this.aa(), var1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
      float var2 = (float)MathHelper.floor(this.getBoundingBox().b);

      int var3;
      float var4;
      float var5;
      for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
         var4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
         var5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
         this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX + (double)var4, (double)(var2 + 1.0F), this.locZ + (double)var5, this.motX, this.motY - (double)(this.random.nextFloat() * 0.2F), this.motZ, new int[0]);
      }

      for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
         var4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
         var5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
         this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + (double)var4, (double)(var2 + 1.0F), this.locZ + (double)var5, this.motX, this.motY, this.motZ, new int[0]);
      }

   }

   public void Y() {
      if(this.isSprinting() && !this.V()) {
         this.Z();
      }

   }

   protected void Z() {
      int var1 = MathHelper.floor(this.locX);
      int var2 = MathHelper.floor(this.locY - 0.20000000298023224D);
      int var3 = MathHelper.floor(this.locZ);
      BlockPosition var4 = new BlockPosition(var1, var2, var3);
      IBlockData var5 = this.world.getType(var4);
      Block var6 = var5.getBlock();
      if(var6.b() != -1) {
         this.world.addParticle(EnumParticle.BLOCK_CRACK, this.locX + ((double)this.random.nextFloat() - 0.5D) * (double)this.width, this.getBoundingBox().b + 0.1D, this.locZ + ((double)this.random.nextFloat() - 0.5D) * (double)this.width, -this.motX * 4.0D, 1.5D, -this.motZ * 4.0D, new int[]{Block.getCombinedId(var5)});
      }

   }

   protected String aa() {
      return "game.neutral.swim.splash";
   }

   public boolean a(Material var1) {
      double var2 = this.locY + (double)this.getHeadHeight();
      BlockPosition var4 = new BlockPosition(this.locX, var2, this.locZ);
      IBlockData var5 = this.world.getType(var4);
      Block var6 = var5.getBlock();
      if(var6.getMaterial() == var1) {
         float var7 = BlockFluids.b(var5.getBlock().toLegacyData(var5)) - 0.11111111F;
         float var8 = (float)(var4.getY() + 1) - var7;
         boolean var9 = var2 < (double)var8;
         return !var9 && this instanceof EntityHuman?false:var9;
      } else {
         return false;
      }
   }

   public boolean ab() {
      return this.world.a(this.getBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
   }

   public void a(float var1, float var2, float var3) {
      float var4 = var1 * var1 + var2 * var2;
      if(var4 >= 1.0E-4F) {
         var4 = MathHelper.c(var4);
         if(var4 < 1.0F) {
            var4 = 1.0F;
         }

         var4 = var3 / var4;
         var1 *= var4;
         var2 *= var4;
         float var5 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F);
         float var6 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F);
         this.motX += (double)(var1 * var6 - var2 * var5);
         this.motZ += (double)(var2 * var6 + var1 * var5);
      }
   }

   public float c(float var1) {
      BlockPosition var2 = new BlockPosition(this.locX, this.locY + (double)this.getHeadHeight(), this.locZ);
      return this.world.isLoaded(var2)?this.world.o(var2):0.0F;
   }

   public void spawnIn(World var1) {
      this.world = var1;
   }

   public void setLocation(double var1, double var3, double var5, float var7, float var8) {
      this.lastX = this.locX = var1;
      this.lastY = this.locY = var3;
      this.lastZ = this.locZ = var5;
      this.lastYaw = this.yaw = var7;
      this.lastPitch = this.pitch = var8;
      double var9 = (double)(this.lastYaw - var7);
      if(var9 < -180.0D) {
         this.lastYaw += 360.0F;
      }

      if(var9 >= 180.0D) {
         this.lastYaw -= 360.0F;
      }

      this.setPosition(this.locX, this.locY, this.locZ);
      this.setYawPitch(var7, var8);
   }

   public void setPositionRotation(BlockPosition var1, float var2, float var3) {
      this.setPositionRotation((double)var1.getX() + 0.5D, (double)var1.getY(), (double)var1.getZ() + 0.5D, var2, var3);
   }

   public void setPositionRotation(double var1, double var3, double var5, float var7, float var8) {
      this.P = this.lastX = this.locX = var1;
      this.Q = this.lastY = this.locY = var3;
      this.R = this.lastZ = this.locZ = var5;
      this.yaw = var7;
      this.pitch = var8;
      this.setPosition(this.locX, this.locY, this.locZ);
   }

   public float g(Entity var1) {
      float var2 = (float)(this.locX - var1.locX);
      float var3 = (float)(this.locY - var1.locY);
      float var4 = (float)(this.locZ - var1.locZ);
      return MathHelper.c(var2 * var2 + var3 * var3 + var4 * var4);
   }

   public double e(double var1, double var3, double var5) {
      double var7 = this.locX - var1;
      double var9 = this.locY - var3;
      double var11 = this.locZ - var5;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public double b(BlockPosition var1) {
      return var1.c(this.locX, this.locY, this.locZ);
   }

   public double c(BlockPosition var1) {
      return var1.d(this.locX, this.locY, this.locZ);
   }

   public double f(double var1, double var3, double var5) {
      double var7 = this.locX - var1;
      double var9 = this.locY - var3;
      double var11 = this.locZ - var5;
      return (double)MathHelper.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
   }

   public double h(Entity var1) {
      double var2 = this.locX - var1.locX;
      double var4 = this.locY - var1.locY;
      double var6 = this.locZ - var1.locZ;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public void d(EntityHuman var1) {
   }

   public void collide(Entity var1) {
      if(var1.passenger != this && var1.vehicle != this) {
         if(!var1.noclip && !this.noclip) {
            double var2 = var1.locX - this.locX;
            double var4 = var1.locZ - this.locZ;
            double var6 = MathHelper.a(var2, var4);
            if(var6 >= 0.009999999776482582D) {
               var6 = (double)MathHelper.sqrt(var6);
               var2 /= var6;
               var4 /= var6;
               double var8 = 1.0D / var6;
               if(var8 > 1.0D) {
                  var8 = 1.0D;
               }

               var2 *= var8;
               var4 *= var8;
               var2 *= 0.05000000074505806D;
               var4 *= 0.05000000074505806D;
               var2 *= (double)(1.0F - this.U);
               var4 *= (double)(1.0F - this.U);
               if(this.passenger == null) {
                  this.g(-var2, 0.0D, -var4);
               }

               if(var1.passenger == null) {
                  var1.g(var2, 0.0D, var4);
               }
            }

         }
      }
   }

   public void g(double var1, double var3, double var5) {
      this.motX += var1;
      this.motY += var3;
      this.motZ += var5;
      this.ai = true;
   }

   protected void ac() {
      this.velocityChanged = true;
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         this.ac();
         return false;
      }
   }

   public Vec3D d(float var1) {
      if(var1 == 1.0F) {
         return this.f(this.pitch, this.yaw);
      } else {
         float var2 = this.lastPitch + (this.pitch - this.lastPitch) * var1;
         float var3 = this.lastYaw + (this.yaw - this.lastYaw) * var1;
         return this.f(var2, var3);
      }
   }

   protected final Vec3D f(float var1, float var2) {
      float var3 = MathHelper.cos(-var2 * 0.017453292F - 3.1415927F);
      float var4 = MathHelper.sin(-var2 * 0.017453292F - 3.1415927F);
      float var5 = -MathHelper.cos(-var1 * 0.017453292F);
      float var6 = MathHelper.sin(-var1 * 0.017453292F);
      return new Vec3D((double)(var4 * var5), (double)var6, (double)(var3 * var5));
   }

   public boolean ad() {
      return false;
   }

   public boolean ae() {
      return false;
   }

   public void b(Entity var1, int var2) {
   }

   public boolean c(NBTTagCompound var1) {
      String var2 = this.ag();
      if(!this.dead && var2 != null) {
         var1.setString("id", var2);
         this.e(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean d(NBTTagCompound var1) {
      String var2 = this.ag();
      if(!this.dead && var2 != null && this.passenger == null) {
         var1.setString("id", var2);
         this.e(var1);
         return true;
      } else {
         return false;
      }
   }

   public void e(NBTTagCompound var1) {
      try {
         var1.set("Pos", this.a(new double[]{this.locX, this.locY, this.locZ}));
         var1.set("Motion", this.a(new double[]{this.motX, this.motY, this.motZ}));
         var1.set("Rotation", this.a(new float[]{this.yaw, this.pitch}));
         var1.setFloat("FallDistance", this.fallDistance);
         var1.setShort("Fire", (short)this.fireTicks);
         var1.setShort("Air", (short)this.getAirTicks());
         var1.setBoolean("OnGround", this.onGround);
         var1.setInt("Dimension", this.dimension);
         var1.setBoolean("Invulnerable", this.invulnerable);
         var1.setInt("PortalCooldown", this.portalCooldown);
         var1.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
         var1.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
         if(this.getCustomName() != null && this.getCustomName().length() > 0) {
            var1.setString("CustomName", this.getCustomName());
            var1.setBoolean("CustomNameVisible", this.getCustomNameVisible());
         }

         this.au.b(var1);
         if(this.R()) {
            var1.setBoolean("Silent", this.R());
         }

         this.b(var1);
         if(this.vehicle != null) {
            NBTTagCompound var2 = new NBTTagCompound();
            if(this.vehicle.c(var2)) {
               var1.set("Riding", var2);
            }
         }

      } catch (Throwable var5) {
         CrashReport var3 = CrashReport.a(var5, "Saving entity NBT");
         CrashReportSystemDetails var4 = var3.a("Entity being saved");
         this.appendEntityCrashDetails(var4);
         throw new ReportedException(var3);
      }
   }

   public void f(NBTTagCompound var1) {
      try {
         NBTTagList var2 = var1.getList("Pos", 6);
         NBTTagList var6 = var1.getList("Motion", 6);
         NBTTagList var7 = var1.getList("Rotation", 5);
         this.motX = var6.d(0);
         this.motY = var6.d(1);
         this.motZ = var6.d(2);
         if(Math.abs(this.motX) > 10.0D) {
            this.motX = 0.0D;
         }

         if(Math.abs(this.motY) > 10.0D) {
            this.motY = 0.0D;
         }

         if(Math.abs(this.motZ) > 10.0D) {
            this.motZ = 0.0D;
         }

         this.lastX = this.P = this.locX = var2.d(0);
         this.lastY = this.Q = this.locY = var2.d(1);
         this.lastZ = this.R = this.locZ = var2.d(2);
         this.lastYaw = this.yaw = var7.e(0);
         this.lastPitch = this.pitch = var7.e(1);
         this.f(this.yaw);
         this.g(this.yaw);
         this.fallDistance = var1.getFloat("FallDistance");
         this.fireTicks = var1.getShort("Fire");
         this.setAirTicks(var1.getShort("Air"));
         this.onGround = var1.getBoolean("OnGround");
         this.dimension = var1.getInt("Dimension");
         this.invulnerable = var1.getBoolean("Invulnerable");
         this.portalCooldown = var1.getInt("PortalCooldown");
         if(var1.hasKeyOfType("UUIDMost", 4) && var1.hasKeyOfType("UUIDLeast", 4)) {
            this.uniqueID = new UUID(var1.getLong("UUIDMost"), var1.getLong("UUIDLeast"));
         } else if(var1.hasKeyOfType("UUID", 8)) {
            this.uniqueID = UUID.fromString(var1.getString("UUID"));
         }

         this.setPosition(this.locX, this.locY, this.locZ);
         this.setYawPitch(this.yaw, this.pitch);
         if(var1.hasKeyOfType("CustomName", 8) && var1.getString("CustomName").length() > 0) {
            this.setCustomName(var1.getString("CustomName"));
         }

         this.setCustomNameVisible(var1.getBoolean("CustomNameVisible"));
         this.au.a(var1);
         this.b(var1.getBoolean("Silent"));
         this.a(var1);
         if(this.af()) {
            this.setPosition(this.locX, this.locY, this.locZ);
         }

      } catch (Throwable var5) {
         CrashReport var3 = CrashReport.a(var5, "Loading entity NBT");
         CrashReportSystemDetails var4 = var3.a("Entity being loaded");
         this.appendEntityCrashDetails(var4);
         throw new ReportedException(var3);
      }
   }

   protected boolean af() {
      return true;
   }

   protected final String ag() {
      return EntityTypes.b(this);
   }

   protected abstract void a(NBTTagCompound var1);

   protected abstract void b(NBTTagCompound var1);

   public void ah() {
   }

   protected NBTTagList a(double... var1) {
      NBTTagList var2 = new NBTTagList();
      double[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         double var6 = var3[var5];
         var2.add(new NBTTagDouble(var6));
      }

      return var2;
   }

   protected NBTTagList a(float... var1) {
      NBTTagList var2 = new NBTTagList();
      float[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         float var6 = var3[var5];
         var2.add(new NBTTagFloat(var6));
      }

      return var2;
   }

   public EntityItem a(Item var1, int var2) {
      return this.a(var1, var2, 0.0F);
   }

   public EntityItem a(Item var1, int var2, float var3) {
      return this.a(new ItemStack(var1, var2, 0), var3);
   }

   public EntityItem a(ItemStack var1, float var2) {
      if(var1.count != 0 && var1.getItem() != null) {
         EntityItem var3 = new EntityItem(this.world, this.locX, this.locY + (double)var2, this.locZ, var1);
         var3.p();
         this.world.addEntity(var3);
         return var3;
      } else {
         return null;
      }
   }

   public boolean isAlive() {
      return !this.dead;
   }

   public boolean inBlock() {
      if(this.noclip) {
         return false;
      } else {
         BlockPosition.MutableBlockPosition var1 = new BlockPosition.MutableBlockPosition(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

         for(int var2 = 0; var2 < 8; ++var2) {
            int var3 = MathHelper.floor(this.locY + (double)(((float)((var2 >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getHeadHeight());
            int var4 = MathHelper.floor(this.locX + (double)(((float)((var2 >> 1) % 2) - 0.5F) * this.width * 0.8F));
            int var5 = MathHelper.floor(this.locZ + (double)(((float)((var2 >> 2) % 2) - 0.5F) * this.width * 0.8F));
            if(var1.getX() != var4 || var1.getY() != var3 || var1.getZ() != var5) {
               var1.c(var4, var3, var5);
               if(this.world.getType(var1).getBlock().w()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean e(EntityHuman var1) {
      return false;
   }

   public AxisAlignedBB j(Entity var1) {
      return null;
   }

   public void ak() {
      if(this.vehicle.dead) {
         this.vehicle = null;
      } else {
         this.motX = 0.0D;
         this.motY = 0.0D;
         this.motZ = 0.0D;
         this.t_();
         if(this.vehicle != null) {
            this.vehicle.al();
            this.as += (double)(this.vehicle.yaw - this.vehicle.lastYaw);

            for(this.ar += (double)(this.vehicle.pitch - this.vehicle.lastPitch); this.as >= 180.0D; this.as -= 360.0D) {
               ;
            }

            while(this.as < -180.0D) {
               this.as += 360.0D;
            }

            while(this.ar >= 180.0D) {
               this.ar -= 360.0D;
            }

            while(this.ar < -180.0D) {
               this.ar += 360.0D;
            }

            double var1 = this.as * 0.5D;
            double var3 = this.ar * 0.5D;
            float var5 = 10.0F;
            if(var1 > (double)var5) {
               var1 = (double)var5;
            }

            if(var1 < (double)(-var5)) {
               var1 = (double)(-var5);
            }

            if(var3 > (double)var5) {
               var3 = (double)var5;
            }

            if(var3 < (double)(-var5)) {
               var3 = (double)(-var5);
            }

            this.as -= var1;
            this.ar -= var3;
         }
      }
   }

   public void al() {
      if(this.passenger != null) {
         this.passenger.setPosition(this.locX, this.locY + this.an() + this.passenger.am(), this.locZ);
      }
   }

   public double am() {
      return 0.0D;
   }

   public double an() {
      return (double)this.length * 0.75D;
   }

   public void mount(Entity var1) {
      this.ar = 0.0D;
      this.as = 0.0D;
      if(var1 == null) {
         if(this.vehicle != null) {
            this.setPositionRotation(this.vehicle.locX, this.vehicle.getBoundingBox().b + (double)this.vehicle.length, this.vehicle.locZ, this.yaw, this.pitch);
            this.vehicle.passenger = null;
         }

         this.vehicle = null;
      } else {
         if(this.vehicle != null) {
            this.vehicle.passenger = null;
         }

         if(var1 != null) {
            for(Entity var2 = var1.vehicle; var2 != null; var2 = var2.vehicle) {
               if(var2 == this) {
                  return;
               }
            }
         }

         this.vehicle = var1;
         var1.passenger = this;
      }
   }

   public float ao() {
      return 0.1F;
   }

   public Vec3D ap() {
      return null;
   }

   public void d(BlockPosition var1) {
      if(this.portalCooldown > 0) {
         this.portalCooldown = this.aq();
      } else {
         if(!this.world.isClientSide && !var1.equals(this.an)) {
            this.an = var1;
            ShapeDetector.ShapeDetectorCollection var2 = Blocks.PORTAL.f(this.world, var1);
            double var3 = var2.b().k() == EnumDirection.EnumAxis.X?(double)var2.a().getZ():(double)var2.a().getX();
            double var5 = var2.b().k() == EnumDirection.EnumAxis.X?this.locZ:this.locX;
            var5 = Math.abs(MathHelper.c(var5 - (double)(var2.b().e().c() == EnumDirection.EnumAxisDirection.NEGATIVE?1:0), var3, var3 - (double)var2.d()));
            double var7 = MathHelper.c(this.locY - 1.0D, (double)var2.a().getY(), (double)(var2.a().getY() - var2.e()));
            this.ao = new Vec3D(var5, var7, 0.0D);
            this.ap = var2.b();
         }

         this.ak = true;
      }
   }

   public int aq() {
      return 300;
   }

   public ItemStack[] getEquipment() {
      return null;
   }

   public void setEquipment(int var1, ItemStack var2) {
   }

   public boolean isBurning() {
      boolean var1 = this.world != null && this.world.isClientSide;
      return !this.fireProof && (this.fireTicks > 0 || var1 && this.g(0));
   }

   public boolean au() {
      return this.vehicle != null;
   }

   public boolean isSneaking() {
      return this.g(1);
   }

   public void setSneaking(boolean var1) {
      this.b(1, var1);
   }

   public boolean isSprinting() {
      return this.g(3);
   }

   public void setSprinting(boolean var1) {
      this.b(3, var1);
   }

   public boolean isInvisible() {
      return this.g(5);
   }

   public void setInvisible(boolean var1) {
      this.b(5, var1);
   }

   public void f(boolean var1) {
      this.b(4, var1);
   }

   protected boolean g(int var1) {
      return (this.datawatcher.getByte(0) & 1 << var1) != 0;
   }

   protected void b(int var1, boolean var2) {
      byte var3 = this.datawatcher.getByte(0);
      if(var2) {
         this.datawatcher.watch(0, Byte.valueOf((byte)(var3 | 1 << var1)));
      } else {
         this.datawatcher.watch(0, Byte.valueOf((byte)(var3 & ~(1 << var1))));
      }

   }

   public int getAirTicks() {
      return this.datawatcher.getShort(1);
   }

   public void setAirTicks(int var1) {
      this.datawatcher.watch(1, Short.valueOf((short)var1));
   }

   public void onLightningStrike(EntityLightning var1) {
      this.damageEntity(DamageSource.LIGHTNING, 5.0F);
      ++this.fireTicks;
      if(this.fireTicks == 0) {
         this.setOnFire(8);
      }

   }

   public void a(EntityLiving var1) {
   }

   protected boolean j(double var1, double var3, double var5) {
      BlockPosition var7 = new BlockPosition(var1, var3, var5);
      double var8 = var1 - (double)var7.getX();
      double var10 = var3 - (double)var7.getY();
      double var12 = var5 - (double)var7.getZ();
      List var14 = this.world.a(this.getBoundingBox());
      if(var14.isEmpty() && !this.world.u(var7)) {
         return false;
      } else {
         byte var15 = 3;
         double var16 = 9999.0D;
         if(!this.world.u(var7.west()) && var8 < var16) {
            var16 = var8;
            var15 = 0;
         }

         if(!this.world.u(var7.east()) && 1.0D - var8 < var16) {
            var16 = 1.0D - var8;
            var15 = 1;
         }

         if(!this.world.u(var7.up()) && 1.0D - var10 < var16) {
            var16 = 1.0D - var10;
            var15 = 3;
         }

         if(!this.world.u(var7.north()) && var12 < var16) {
            var16 = var12;
            var15 = 4;
         }

         if(!this.world.u(var7.south()) && 1.0D - var12 < var16) {
            var16 = 1.0D - var12;
            var15 = 5;
         }

         float var18 = this.random.nextFloat() * 0.2F + 0.1F;
         if(var15 == 0) {
            this.motX = (double)(-var18);
         }

         if(var15 == 1) {
            this.motX = (double)var18;
         }

         if(var15 == 3) {
            this.motY = (double)var18;
         }

         if(var15 == 4) {
            this.motZ = (double)(-var18);
         }

         if(var15 == 5) {
            this.motZ = (double)var18;
         }

         return true;
      }
   }

   public void aA() {
      this.H = true;
      this.fallDistance = 0.0F;
   }

   public String getName() {
      if(this.hasCustomName()) {
         return this.getCustomName();
      } else {
         String var1 = EntityTypes.b(this);
         if(var1 == null) {
            var1 = "generic";
         }

         return LocaleI18n.get("entity." + var1 + ".name");
      }
   }

   public Entity[] aB() {
      return null;
   }

   public boolean k(Entity var1) {
      return this == var1;
   }

   public float getHeadRotation() {
      return 0.0F;
   }

   public void f(float var1) {
   }

   public void g(float var1) {
   }

   public boolean aD() {
      return true;
   }

   public boolean l(Entity var1) {
      return false;
   }

   public String toString() {
      return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[]{this.getClass().getSimpleName(), this.getName(), Integer.valueOf(this.id), this.world == null?"~NULL~":this.world.getWorldData().getName(), Double.valueOf(this.locX), Double.valueOf(this.locY), Double.valueOf(this.locZ)});
   }

   public boolean isInvulnerable(DamageSource var1) {
      return this.invulnerable && var1 != DamageSource.OUT_OF_WORLD && !var1.u();
   }

   public void m(Entity var1) {
      this.setPositionRotation(var1.locX, var1.locY, var1.locZ, var1.yaw, var1.pitch);
   }

   public void n(Entity var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      var1.e(var2);
      this.f(var2);
      this.portalCooldown = var1.portalCooldown;
      this.an = var1.an;
      this.ao = var1.ao;
      this.ap = var1.ap;
   }

   public void c(int var1) {
      if(!this.world.isClientSide && !this.dead) {
         this.world.methodProfiler.a("changeDimension");
         MinecraftServer var2 = MinecraftServer.getServer();
         int var3 = this.dimension;
         WorldServer var4 = var2.getWorldServer(var3);
         WorldServer var5 = var2.getWorldServer(var1);
         this.dimension = var1;
         if(var3 == 1 && var1 == 1) {
            var5 = var2.getWorldServer(0);
            this.dimension = 0;
         }

         this.world.kill(this);
         this.dead = false;
         this.world.methodProfiler.a("reposition");
         var2.getPlayerList().changeWorld(this, var3, var4, var5);
         this.world.methodProfiler.c("reloading");
         Entity var6 = EntityTypes.createEntityByName(EntityTypes.b(this), var5);
         if(var6 != null) {
            var6.n(this);
            if(var3 == 1 && var1 == 1) {
               BlockPosition var7 = this.world.r(var5.getSpawn());
               var6.setPositionRotation(var7, var6.yaw, var6.pitch);
            }

            var5.addEntity(var6);
         }

         this.dead = true;
         this.world.methodProfiler.b();
         var4.j();
         var5.j();
         this.world.methodProfiler.b();
      }
   }

   public float a(Explosion var1, World var2, BlockPosition var3, IBlockData var4) {
      return var4.getBlock().a(this);
   }

   public boolean a(Explosion var1, World var2, BlockPosition var3, IBlockData var4, float var5) {
      return true;
   }

   public int aE() {
      return 3;
   }

   public Vec3D aG() {
      return this.ao;
   }

   public EnumDirection aH() {
      return this.ap;
   }

   public boolean aI() {
      return false;
   }

   public void appendEntityCrashDetails(CrashReportSystemDetails var1) {
      var1.a("Entity Type", new Callable() {
         public String a() throws Exception {
            return EntityTypes.b(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a((String)"Entity ID", (Object)Integer.valueOf(this.id));
      var1.a("Entity Name", new Callable() {
         public String a() throws Exception {
            return Entity.this.getName();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a((String)"Entity\'s Exact location", (Object)String.format("%.2f, %.2f, %.2f", new Object[]{Double.valueOf(this.locX), Double.valueOf(this.locY), Double.valueOf(this.locZ)}));
      var1.a((String)"Entity\'s Block location", (Object)CrashReportSystemDetails.a((double)MathHelper.floor(this.locX), (double)MathHelper.floor(this.locY), (double)MathHelper.floor(this.locZ)));
      var1.a((String)"Entity\'s Momentum", (Object)String.format("%.2f, %.2f, %.2f", new Object[]{Double.valueOf(this.motX), Double.valueOf(this.motY), Double.valueOf(this.motZ)}));
      var1.a("Entity\'s Rider", new Callable() {
         public String a() throws Exception {
            return Entity.this.passenger.toString();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Entity\'s Vehicle", new Callable() {
         public String a() throws Exception {
            return Entity.this.vehicle.toString();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
   }

   public UUID getUniqueID() {
      return this.uniqueID;
   }

   public boolean aL() {
      return true;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      ChatComponentText var1 = new ChatComponentText(this.getName());
      var1.getChatModifier().setChatHoverable(this.aQ());
      var1.getChatModifier().setInsertion(this.getUniqueID().toString());
      return var1;
   }

   public void setCustomName(String var1) {
      this.datawatcher.watch(2, var1);
   }

   public String getCustomName() {
      return this.datawatcher.getString(2);
   }

   public boolean hasCustomName() {
      return this.datawatcher.getString(2).length() > 0;
   }

   public void setCustomNameVisible(boolean var1) {
      this.datawatcher.watch(3, Byte.valueOf((byte)(var1?1:0)));
   }

   public boolean getCustomNameVisible() {
      return this.datawatcher.getByte(3) == 1;
   }

   public void enderTeleportTo(double var1, double var3, double var5) {
      this.setPositionRotation(var1, var3, var5, this.yaw, this.pitch);
   }

   public void i(int var1) {
   }

   public EnumDirection getDirection() {
      return EnumDirection.fromType2(MathHelper.floor((double)(this.yaw * 4.0F / 360.0F) + 0.5D) & 3);
   }

   protected ChatHoverable aQ() {
      NBTTagCompound var1 = new NBTTagCompound();
      String var2 = EntityTypes.b(this);
      var1.setString("id", this.getUniqueID().toString());
      if(var2 != null) {
         var1.setString("type", var2);
      }

      var1.setString("name", this.getName());
      return new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ENTITY, new ChatComponentText(var1.toString()));
   }

   public boolean a(EntityPlayer var1) {
      return true;
   }

   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }

   public void a(AxisAlignedBB var1) {
      this.boundingBox = var1;
   }

   public float getHeadHeight() {
      return this.length * 0.85F;
   }

   public boolean aT() {
      return this.g;
   }

   public void h(boolean var1) {
      this.g = var1;
   }

   public boolean d(int var1, ItemStack var2) {
      return false;
   }

   public void sendMessage(IChatBaseComponent var1) {
   }

   public boolean a(int var1, String var2) {
      return true;
   }

   public BlockPosition getChunkCoordinates() {
      return new BlockPosition(this.locX, this.locY + 0.5D, this.locZ);
   }

   public Vec3D d() {
      return new Vec3D(this.locX, this.locY, this.locZ);
   }

   public World getWorld() {
      return this.world;
   }

   public Entity f() {
      return this;
   }

   public boolean getSendCommandFeedback() {
      return false;
   }

   public void a(CommandObjectiveExecutor.EnumCommandResult var1, int var2) {
      this.au.a(this, var1, var2);
   }

   public CommandObjectiveExecutor aU() {
      return this.au;
   }

   public void o(Entity var1) {
      this.au.a(var1.aU());
   }

   public NBTTagCompound getNBTTag() {
      return null;
   }

   public boolean a(EntityHuman var1, Vec3D var2) {
      return false;
   }

   public boolean aW() {
      return false;
   }

   protected void a(EntityLiving var1, Entity var2) {
      if(var2 instanceof EntityLiving) {
         EnchantmentManager.a((EntityLiving)((EntityLiving)var2), (Entity)var1);
      }

      EnchantmentManager.b(var1, var2);
   }
}
