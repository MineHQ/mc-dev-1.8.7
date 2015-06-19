package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import java.io.PrintStream;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDispenser;
import net.minecraft.server.BlockFire;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockPumpkin;
import net.minecraft.server.BlockSkull;
import net.minecraft.server.BlockTNT;
import net.minecraft.server.Blocks;
import net.minecraft.server.DispenseBehaviorItem;
import net.minecraft.server.DispenseBehaviorProjectile;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityBoat;
import net.minecraft.server.EntityEgg;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityPotion;
import net.minecraft.server.EntitySmallFireball;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EntityThrownExpBottle;
import net.minecraft.server.EnumColor;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IDispenseBehavior;
import net.minecraft.server.IPosition;
import net.minecraft.server.IProjectile;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBucket;
import net.minecraft.server.ItemDye;
import net.minecraft.server.ItemMonsterEgg;
import net.minecraft.server.ItemPotion;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.RedirectStream;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntitySkull;
import net.minecraft.server.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DispenserRegistry {
   private static final PrintStream a;
   private static boolean b;
   private static final Logger c;

   public static boolean a() {
      return b;
   }

   static void b() {
      BlockDispenser.N.a(Items.ARROW, new DispenseBehaviorProjectile() {
         protected IProjectile a(World var1, IPosition var2) {
            EntityArrow var3 = new EntityArrow(var1, var2.getX(), var2.getY(), var2.getZ());
            var3.fromPlayer = 1;
            return var3;
         }
      });
      BlockDispenser.N.a(Items.EGG, new DispenseBehaviorProjectile() {
         protected IProjectile a(World var1, IPosition var2) {
            return new EntityEgg(var1, var2.getX(), var2.getY(), var2.getZ());
         }
      });
      BlockDispenser.N.a(Items.SNOWBALL, new DispenseBehaviorProjectile() {
         protected IProjectile a(World var1, IPosition var2) {
            return new EntitySnowball(var1, var2.getX(), var2.getY(), var2.getZ());
         }
      });
      BlockDispenser.N.a(Items.EXPERIENCE_BOTTLE, new DispenseBehaviorProjectile() {
         protected IProjectile a(World var1, IPosition var2) {
            return new EntityThrownExpBottle(var1, var2.getX(), var2.getY(), var2.getZ());
         }

         protected float a() {
            return super.a() * 0.5F;
         }

         protected float b() {
            return super.b() * 1.25F;
         }
      });
      BlockDispenser.N.a(Items.POTION, new IDispenseBehavior() {
         private final DispenseBehaviorItem b = new DispenseBehaviorItem();

         public ItemStack a(ISourceBlock var1, final ItemStack var2) {
            return ItemPotion.f(var2.getData())?(new DispenseBehaviorProjectile() {
               protected IProjectile a(World var1, IPosition var2x) {
                  return new EntityPotion(var1, var2x.getX(), var2x.getY(), var2x.getZ(), var2.cloneItemStack());
               }

               protected float a() {
                  return super.a() * 0.5F;
               }

               protected float b() {
                  return super.b() * 1.25F;
               }
            }).a(var1, var2):this.b.a(var1, var2);
         }
      });
      BlockDispenser.N.a(Items.SPAWN_EGG, new DispenseBehaviorItem() {
         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            EnumDirection var3 = BlockDispenser.b(var1.f());
            double var4 = var1.getX() + (double)var3.getAdjacentX();
            double var6 = (double)((float)var1.getBlockPosition().getY() + 0.2F);
            double var8 = var1.getZ() + (double)var3.getAdjacentZ();
            Entity var10 = ItemMonsterEgg.a(var1.i(), var2.getData(), var4, var6, var8);
            if(var10 instanceof EntityLiving && var2.hasName()) {
               ((EntityInsentient)var10).setCustomName(var2.getName());
            }

            var2.a(1);
            return var2;
         }
      });
      BlockDispenser.N.a(Items.FIREWORKS, new DispenseBehaviorItem() {
         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            EnumDirection var3 = BlockDispenser.b(var1.f());
            double var4 = var1.getX() + (double)var3.getAdjacentX();
            double var6 = (double)((float)var1.getBlockPosition().getY() + 0.2F);
            double var8 = var1.getZ() + (double)var3.getAdjacentZ();
            EntityFireworks var10 = new EntityFireworks(var1.i(), var4, var6, var8, var2);
            var1.i().addEntity(var10);
            var2.a(1);
            return var2;
         }

         protected void a(ISourceBlock var1) {
            var1.i().triggerEffect(1002, var1.getBlockPosition(), 0);
         }
      });
      BlockDispenser.N.a(Items.FIRE_CHARGE, new DispenseBehaviorItem() {
         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            EnumDirection var3 = BlockDispenser.b(var1.f());
            IPosition var4 = BlockDispenser.a(var1);
            double var5 = var4.getX() + (double)((float)var3.getAdjacentX() * 0.3F);
            double var7 = var4.getY() + (double)((float)var3.getAdjacentY() * 0.3F);
            double var9 = var4.getZ() + (double)((float)var3.getAdjacentZ() * 0.3F);
            World var11 = var1.i();
            Random var12 = var11.random;
            double var13 = var12.nextGaussian() * 0.05D + (double)var3.getAdjacentX();
            double var15 = var12.nextGaussian() * 0.05D + (double)var3.getAdjacentY();
            double var17 = var12.nextGaussian() * 0.05D + (double)var3.getAdjacentZ();
            var11.addEntity(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
            var2.a(1);
            return var2;
         }

         protected void a(ISourceBlock var1) {
            var1.i().triggerEffect(1009, var1.getBlockPosition(), 0);
         }
      });
      BlockDispenser.N.a(Items.BOAT, new DispenseBehaviorItem() {
         private final DispenseBehaviorItem b = new DispenseBehaviorItem();

         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            EnumDirection var3 = BlockDispenser.b(var1.f());
            World var4 = var1.i();
            double var5 = var1.getX() + (double)((float)var3.getAdjacentX() * 1.125F);
            double var7 = var1.getY() + (double)((float)var3.getAdjacentY() * 1.125F);
            double var9 = var1.getZ() + (double)((float)var3.getAdjacentZ() * 1.125F);
            BlockPosition var11 = var1.getBlockPosition().shift(var3);
            Material var12 = var4.getType(var11).getBlock().getMaterial();
            double var13;
            if(Material.WATER.equals(var12)) {
               var13 = 1.0D;
            } else {
               if(!Material.AIR.equals(var12) || !Material.WATER.equals(var4.getType(var11.down()).getBlock().getMaterial())) {
                  return this.b.a(var1, var2);
               }

               var13 = 0.0D;
            }

            EntityBoat var15 = new EntityBoat(var4, var5, var7 + var13, var9);
            var4.addEntity(var15);
            var2.a(1);
            return var2;
         }

         protected void a(ISourceBlock var1) {
            var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
         }
      });
      DispenseBehaviorItem var0 = new DispenseBehaviorItem() {
         private final DispenseBehaviorItem b = new DispenseBehaviorItem();

         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            ItemBucket var3 = (ItemBucket)var2.getItem();
            BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
            if(var3.a(var1.i(), var4)) {
               var2.setItem(Items.BUCKET);
               var2.count = 1;
               return var2;
            } else {
               return this.b.a(var1, var2);
            }
         }
      };
      BlockDispenser.N.a(Items.LAVA_BUCKET, var0);
      BlockDispenser.N.a(Items.WATER_BUCKET, var0);
      BlockDispenser.N.a(Items.BUCKET, new DispenseBehaviorItem() {
         private final DispenseBehaviorItem b = new DispenseBehaviorItem();

         public ItemStack b(ISourceBlock var1, ItemStack var2) {
            World var3 = var1.i();
            BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
            IBlockData var5 = var3.getType(var4);
            Block var6 = var5.getBlock();
            Material var7 = var6.getMaterial();
            Item var8;
            if(Material.WATER.equals(var7) && var6 instanceof BlockFluids && ((Integer)var5.get(BlockFluids.LEVEL)).intValue() == 0) {
               var8 = Items.WATER_BUCKET;
            } else {
               if(!Material.LAVA.equals(var7) || !(var6 instanceof BlockFluids) || ((Integer)var5.get(BlockFluids.LEVEL)).intValue() != 0) {
                  return super.b(var1, var2);
               }

               var8 = Items.LAVA_BUCKET;
            }

            var3.setAir(var4);
            if(--var2.count == 0) {
               var2.setItem(var8);
               var2.count = 1;
            } else if(((TileEntityDispenser)var1.getTileEntity()).addItem(new ItemStack(var8)) < 0) {
               this.b.a(var1, new ItemStack(var8));
            }

            return var2;
         }
      });
      BlockDispenser.N.a(Items.FLINT_AND_STEEL, new DispenseBehaviorItem() {
         private boolean b = true;

         protected ItemStack b(ISourceBlock var1, ItemStack var2) {
            World var3 = var1.i();
            BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
            if(var3.isEmpty(var4)) {
               var3.setTypeUpdate(var4, Blocks.FIRE.getBlockData());
               if(var2.isDamaged(1, var3.random)) {
                  var2.count = 0;
               }
            } else if(var3.getType(var4).getBlock() == Blocks.TNT) {
               Blocks.TNT.postBreak(var3, var4, Blocks.TNT.getBlockData().set(BlockTNT.EXPLODE, Boolean.valueOf(true)));
               var3.setAir(var4);
            } else {
               this.b = false;
            }

            return var2;
         }

         protected void a(ISourceBlock var1) {
            if(this.b) {
               var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
            } else {
               var1.i().triggerEffect(1001, var1.getBlockPosition(), 0);
            }

         }
      });
      BlockDispenser.N.a(Items.DYE, new DispenseBehaviorItem() {
         private boolean b = true;

         protected ItemStack b(ISourceBlock var1, ItemStack var2) {
            if(EnumColor.WHITE == EnumColor.fromInvColorIndex(var2.getData())) {
               World var3 = var1.i();
               BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
               if(ItemDye.a(var2, var3, var4)) {
                  if(!var3.isClientSide) {
                     var3.triggerEffect(2005, var4, 0);
                  }
               } else {
                  this.b = false;
               }

               return var2;
            } else {
               return super.b(var1, var2);
            }
         }

         protected void a(ISourceBlock var1) {
            if(this.b) {
               var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
            } else {
               var1.i().triggerEffect(1001, var1.getBlockPosition(), 0);
            }

         }
      });
      BlockDispenser.N.a(Item.getItemOf(Blocks.TNT), new DispenseBehaviorItem() {
         protected ItemStack b(ISourceBlock var1, ItemStack var2) {
            World var3 = var1.i();
            BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
            EntityTNTPrimed var5 = new EntityTNTPrimed(var3, (double)var4.getX() + 0.5D, (double)var4.getY(), (double)var4.getZ() + 0.5D, (EntityLiving)null);
            var3.addEntity(var5);
            var3.makeSound(var5, "game.tnt.primed", 1.0F, 1.0F);
            --var2.count;
            return var2;
         }
      });
      BlockDispenser.N.a(Items.SKULL, new DispenseBehaviorItem() {
         private boolean b = true;

         protected ItemStack b(ISourceBlock var1, ItemStack var2) {
            World var3 = var1.i();
            EnumDirection var4 = BlockDispenser.b(var1.f());
            BlockPosition var5 = var1.getBlockPosition().shift(var4);
            BlockSkull var6 = Blocks.SKULL;
            if(var3.isEmpty(var5) && var6.b(var3, var5, var2)) {
               if(!var3.isClientSide) {
                  var3.setTypeAndData(var5, var6.getBlockData().set(BlockSkull.FACING, EnumDirection.UP), 3);
                  TileEntity var7 = var3.getTileEntity(var5);
                  if(var7 instanceof TileEntitySkull) {
                     if(var2.getData() == 3) {
                        GameProfile var8 = null;
                        if(var2.hasTag()) {
                           NBTTagCompound var9 = var2.getTag();
                           if(var9.hasKeyOfType("SkullOwner", 10)) {
                              var8 = GameProfileSerializer.deserialize(var9.getCompound("SkullOwner"));
                           } else if(var9.hasKeyOfType("SkullOwner", 8)) {
                              var8 = new GameProfile((UUID)null, var9.getString("SkullOwner"));
                           }
                        }

                        ((TileEntitySkull)var7).setGameProfile(var8);
                     } else {
                        ((TileEntitySkull)var7).setSkullType(var2.getData());
                     }

                     ((TileEntitySkull)var7).setRotation(var4.opposite().b() * 4);
                     Blocks.SKULL.a(var3, var5, (TileEntitySkull)var7);
                  }

                  --var2.count;
               }
            } else {
               this.b = false;
            }

            return var2;
         }

         protected void a(ISourceBlock var1) {
            if(this.b) {
               var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
            } else {
               var1.i().triggerEffect(1001, var1.getBlockPosition(), 0);
            }

         }
      });
      BlockDispenser.N.a(Item.getItemOf(Blocks.PUMPKIN), new DispenseBehaviorItem() {
         private boolean b = true;

         protected ItemStack b(ISourceBlock var1, ItemStack var2) {
            World var3 = var1.i();
            BlockPosition var4 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
            BlockPumpkin var5 = (BlockPumpkin)Blocks.PUMPKIN;
            if(var3.isEmpty(var4) && var5.e(var3, var4)) {
               if(!var3.isClientSide) {
                  var3.setTypeAndData(var4, var5.getBlockData(), 3);
               }

               --var2.count;
            } else {
               this.b = false;
            }

            return var2;
         }

         protected void a(ISourceBlock var1) {
            if(this.b) {
               var1.i().triggerEffect(1000, var1.getBlockPosition(), 0);
            } else {
               var1.i().triggerEffect(1001, var1.getBlockPosition(), 0);
            }

         }
      });
   }

   public static void c() {
      if(!b) {
         b = true;
         if(c.isDebugEnabled()) {
            d();
         }

         Block.S();
         BlockFire.l();
         Item.t();
         StatisticList.a();
         b();
      }
   }

   private static void d() {
      System.setErr(new RedirectStream("STDERR", System.err));
      System.setOut(new RedirectStream("STDOUT", a));
   }

   static {
      a = System.out;
      b = false;
      c = LogManager.getLogger();
   }
}
