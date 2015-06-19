package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class BlockDoor extends Block {
   public static final BlockStateDirection FACING;
   public static final BlockStateBoolean OPEN;
   public static final BlockStateEnum<BlockDoor.EnumDoorHinge> HINGE;
   public static final BlockStateBoolean POWERED;
   public static final BlockStateEnum<BlockDoor.EnumDoorHalf> HALF;

   protected BlockDoor(Material var1) {
      super(var1);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(OPEN, Boolean.valueOf(false)).set(HINGE, BlockDoor.EnumDoorHinge.LEFT).set(POWERED, Boolean.valueOf(false)).set(HALF, BlockDoor.EnumDoorHalf.LOWER));
   }

   public String getName() {
      return LocaleI18n.get((this.a() + ".name").replaceAll("tile", "item"));
   }

   public boolean c() {
      return false;
   }

   public boolean b(IBlockAccess var1, BlockPosition var2) {
      return g(e(var1, var2));
   }

   public boolean d() {
      return false;
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3);
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      this.k(e(var1, var2));
   }

   private void k(int var1) {
      float var2 = 0.1875F;
      this.a(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
      EnumDirection var3 = f(var1);
      boolean var4 = g(var1);
      boolean var5 = j(var1);
      if(var4) {
         if(var3 == EnumDirection.EAST) {
            if(!var5) {
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            } else {
               this.a(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }
         } else if(var3 == EnumDirection.SOUTH) {
            if(!var5) {
               this.a(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
               this.a(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
         } else if(var3 == EnumDirection.WEST) {
            if(!var5) {
               this.a(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            } else {
               this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }
         } else if(var3 == EnumDirection.NORTH) {
            if(!var5) {
               this.a(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            } else {
               this.a(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
         }
      } else if(var3 == EnumDirection.EAST) {
         this.a(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
      } else if(var3 == EnumDirection.SOUTH) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
      } else if(var3 == EnumDirection.WEST) {
         this.a(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else if(var3 == EnumDirection.NORTH) {
         this.a(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
      }

   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(this.material == Material.ORE) {
         return true;
      } else {
         BlockPosition var9 = var3.get(HALF) == BlockDoor.EnumDoorHalf.LOWER?var2:var2.down();
         IBlockData var10 = var2.equals(var9)?var3:var1.getType(var9);
         if(var10.getBlock() != this) {
            return false;
         } else {
            var3 = var10.a(OPEN);
            var1.setTypeAndData(var9, var3, 2);
            var1.b(var9, var2);
            var1.a(var4, ((Boolean)var3.get(OPEN)).booleanValue()?1003:1006, var2, 0);
            return true;
         }
      }
   }

   public void setDoor(World var1, BlockPosition var2, boolean var3) {
      IBlockData var4 = var1.getType(var2);
      if(var4.getBlock() == this) {
         BlockPosition var5 = var4.get(HALF) == BlockDoor.EnumDoorHalf.LOWER?var2:var2.down();
         IBlockData var6 = var2 == var5?var4:var1.getType(var5);
         if(var6.getBlock() == this && ((Boolean)var6.get(OPEN)).booleanValue() != var3) {
            var1.setTypeAndData(var5, var6.set(OPEN, Boolean.valueOf(var3)), 2);
            var1.b(var5, var2);
            var1.a((EntityHuman)null, var3?1003:1006, var2, 0);
         }

      }
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(var3.get(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
         BlockPosition var5 = var2.down();
         IBlockData var6 = var1.getType(var5);
         if(var6.getBlock() != this) {
            var1.setAir(var2);
         } else if(var4 != this) {
            this.doPhysics(var1, var5, var6, var4);
         }
      } else {
         boolean var9 = false;
         BlockPosition var10 = var2.up();
         IBlockData var7 = var1.getType(var10);
         if(var7.getBlock() != this) {
            var1.setAir(var2);
            var9 = true;
         }

         if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down())) {
            var1.setAir(var2);
            var9 = true;
            if(var7.getBlock() == this) {
               var1.setAir(var10);
            }
         }

         if(var9) {
            if(!var1.isClientSide) {
               this.b(var1, var2, var3, 0);
            }
         } else {
            boolean var8 = var1.isBlockIndirectlyPowered(var2) || var1.isBlockIndirectlyPowered(var10);
            if((var8 || var4.isPowerSource()) && var4 != this && var8 != ((Boolean)var7.get(POWERED)).booleanValue()) {
               var1.setTypeAndData(var10, var7.set(POWERED, Boolean.valueOf(var8)), 2);
               if(var8 != ((Boolean)var3.get(OPEN)).booleanValue()) {
                  var1.setTypeAndData(var2, var3.set(OPEN, Boolean.valueOf(var8)), 2);
                  var1.b(var2, var2);
                  var1.a((EntityHuman)null, var8?1003:1006, var2, 0);
               }
            }
         }
      }

   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return var1.get(HALF) == BlockDoor.EnumDoorHalf.UPPER?null:this.l();
   }

   public MovingObjectPosition a(World var1, BlockPosition var2, Vec3D var3, Vec3D var4) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3, var4);
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return var2.getY() >= 255?false:World.a((IBlockAccess)var1, (BlockPosition)var2.down()) && super.canPlace(var1, var2) && super.canPlace(var1, var2.up());
   }

   public int k() {
      return 1;
   }

   public static int e(IBlockAccess var0, BlockPosition var1) {
      IBlockData var2 = var0.getType(var1);
      int var3 = var2.getBlock().toLegacyData(var2);
      boolean var4 = i(var3);
      IBlockData var5 = var0.getType(var1.down());
      int var6 = var5.getBlock().toLegacyData(var5);
      int var7 = var4?var6:var3;
      IBlockData var8 = var0.getType(var1.up());
      int var9 = var8.getBlock().toLegacyData(var8);
      int var10 = var4?var3:var9;
      boolean var11 = (var10 & 1) != 0;
      boolean var12 = (var10 & 2) != 0;
      return b(var7) | (var4?8:0) | (var11?16:0) | (var12?32:0);
   }

   private Item l() {
      return this == Blocks.IRON_DOOR?Items.IRON_DOOR:(this == Blocks.SPRUCE_DOOR?Items.SPRUCE_DOOR:(this == Blocks.BIRCH_DOOR?Items.BIRCH_DOOR:(this == Blocks.JUNGLE_DOOR?Items.JUNGLE_DOOR:(this == Blocks.ACACIA_DOOR?Items.ACACIA_DOOR:(this == Blocks.DARK_OAK_DOOR?Items.DARK_OAK_DOOR:Items.WOODEN_DOOR)))));
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      BlockPosition var5 = var2.down();
      if(var4.abilities.canInstantlyBuild && var3.get(HALF) == BlockDoor.EnumDoorHalf.UPPER && var1.getType(var5).getBlock() == this) {
         var1.setAir(var5);
      }

   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      IBlockData var4;
      if(var1.get(HALF) == BlockDoor.EnumDoorHalf.LOWER) {
         var4 = var2.getType(var3.up());
         if(var4.getBlock() == this) {
            var1 = var1.set(HINGE, var4.get(HINGE)).set(POWERED, var4.get(POWERED));
         }
      } else {
         var4 = var2.getType(var3.down());
         if(var4.getBlock() == this) {
            var1 = var1.set(FACING, var4.get(FACING)).set(OPEN, var4.get(OPEN));
         }
      }

      return var1;
   }

   public IBlockData fromLegacyData(int var1) {
      return (var1 & 8) > 0?this.getBlockData().set(HALF, BlockDoor.EnumDoorHalf.UPPER).set(HINGE, (var1 & 1) > 0?BlockDoor.EnumDoorHinge.RIGHT:BlockDoor.EnumDoorHinge.LEFT).set(POWERED, Boolean.valueOf((var1 & 2) > 0)):this.getBlockData().set(HALF, BlockDoor.EnumDoorHalf.LOWER).set(FACING, EnumDirection.fromType2(var1 & 3).f()).set(OPEN, Boolean.valueOf((var1 & 4) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3;
      if(var1.get(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
         var3 = var2 | 8;
         if(var1.get(HINGE) == BlockDoor.EnumDoorHinge.RIGHT) {
            var3 |= 1;
         }

         if(((Boolean)var1.get(POWERED)).booleanValue()) {
            var3 |= 2;
         }
      } else {
         var3 = var2 | ((EnumDirection)var1.get(FACING)).e().b();
         if(((Boolean)var1.get(OPEN)).booleanValue()) {
            var3 |= 4;
         }
      }

      return var3;
   }

   protected static int b(int var0) {
      return var0 & 7;
   }

   public static boolean f(IBlockAccess var0, BlockPosition var1) {
      return g(e(var0, var1));
   }

   public static EnumDirection h(IBlockAccess var0, BlockPosition var1) {
      return f(e(var0, var1));
   }

   public static EnumDirection f(int var0) {
      return EnumDirection.fromType2(var0 & 3).f();
   }

   protected static boolean g(int var0) {
      return (var0 & 4) != 0;
   }

   protected static boolean i(int var0) {
      return (var0 & 8) != 0;
   }

   protected static boolean j(int var0) {
      return (var0 & 16) != 0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{HALF, FACING, OPEN, HINGE, POWERED});
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
      OPEN = BlockStateBoolean.of("open");
      HINGE = BlockStateEnum.of("hinge", BlockDoor.EnumDoorHinge.class);
      POWERED = BlockStateBoolean.of("powered");
      HALF = BlockStateEnum.of("half", BlockDoor.EnumDoorHalf.class);
   }

   public static enum EnumDoorHinge implements INamable {
      LEFT,
      RIGHT;

      private EnumDoorHinge() {
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this == LEFT?"left":"right";
      }
   }

   public static enum EnumDoorHalf implements INamable {
      UPPER,
      LOWER;

      private EnumDoorHalf() {
      }

      public String toString() {
         return this.getName();
      }

      public String getName() {
         return this == UPPER?"upper":"lower";
      }
   }
}
