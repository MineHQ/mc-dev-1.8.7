package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockTNT;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.World;
import net.minecraft.server.WorldProviderTheEnd;

public class BlockFire extends Block {
   public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 15);
   public static final BlockStateBoolean FLIP = BlockStateBoolean.of("flip");
   public static final BlockStateBoolean ALT = BlockStateBoolean.of("alt");
   public static final BlockStateBoolean NORTH = BlockStateBoolean.of("north");
   public static final BlockStateBoolean EAST = BlockStateBoolean.of("east");
   public static final BlockStateBoolean SOUTH = BlockStateBoolean.of("south");
   public static final BlockStateBoolean WEST = BlockStateBoolean.of("west");
   public static final BlockStateInteger UPPER = BlockStateInteger.of("upper", 0, 2);
   private final Map<Block, Integer> flameChances = Maps.newIdentityHashMap();
   private final Map<Block, Integer> U = Maps.newIdentityHashMap();

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      int var4 = var3.getX();
      int var5 = var3.getY();
      int var6 = var3.getZ();
      if(!World.a(var2, var3.down()) && !Blocks.FIRE.e(var2, var3.down())) {
         boolean var7 = (var4 + var5 + var6 & 1) == 1;
         boolean var8 = (var4 / 2 + var5 / 2 + var6 / 2 & 1) == 1;
         int var9 = 0;
         if(this.e(var2, var3.up())) {
            var9 = var7?1:2;
         }

         return var1.set(NORTH, Boolean.valueOf(this.e(var2, var3.north()))).set(EAST, Boolean.valueOf(this.e(var2, var3.east()))).set(SOUTH, Boolean.valueOf(this.e(var2, var3.south()))).set(WEST, Boolean.valueOf(this.e(var2, var3.west()))).set(UPPER, Integer.valueOf(var9)).set(FLIP, Boolean.valueOf(var8)).set(ALT, Boolean.valueOf(var7));
      } else {
         return this.getBlockData();
      }
   }

   protected BlockFire() {
      super(Material.FIRE);
      this.j(this.blockStateList.getBlockData().set(AGE, Integer.valueOf(0)).set(FLIP, Boolean.valueOf(false)).set(ALT, Boolean.valueOf(false)).set(NORTH, Boolean.valueOf(false)).set(EAST, Boolean.valueOf(false)).set(SOUTH, Boolean.valueOf(false)).set(WEST, Boolean.valueOf(false)).set(UPPER, Integer.valueOf(0)));
      this.a(true);
   }

   public static void l() {
      Blocks.FIRE.a(Blocks.PLANKS, 5, 20);
      Blocks.FIRE.a(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
      Blocks.FIRE.a(Blocks.WOODEN_SLAB, 5, 20);
      Blocks.FIRE.a(Blocks.FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.SPRUCE_FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.BIRCH_FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.JUNGLE_FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.ACACIA_FENCE_GATE, 5, 20);
      Blocks.FIRE.a(Blocks.FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.SPRUCE_FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.BIRCH_FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.JUNGLE_FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.DARK_OAK_FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.ACACIA_FENCE, 5, 20);
      Blocks.FIRE.a(Blocks.OAK_STAIRS, 5, 20);
      Blocks.FIRE.a(Blocks.BIRCH_STAIRS, 5, 20);
      Blocks.FIRE.a(Blocks.SPRUCE_STAIRS, 5, 20);
      Blocks.FIRE.a(Blocks.JUNGLE_STAIRS, 5, 20);
      Blocks.FIRE.a(Blocks.LOG, 5, 5);
      Blocks.FIRE.a(Blocks.LOG2, 5, 5);
      Blocks.FIRE.a(Blocks.LEAVES, 30, 60);
      Blocks.FIRE.a(Blocks.LEAVES2, 30, 60);
      Blocks.FIRE.a(Blocks.BOOKSHELF, 30, 20);
      Blocks.FIRE.a(Blocks.TNT, 15, 100);
      Blocks.FIRE.a(Blocks.TALLGRASS, 60, 100);
      Blocks.FIRE.a(Blocks.DOUBLE_PLANT, 60, 100);
      Blocks.FIRE.a(Blocks.YELLOW_FLOWER, 60, 100);
      Blocks.FIRE.a(Blocks.RED_FLOWER, 60, 100);
      Blocks.FIRE.a(Blocks.DEADBUSH, 60, 100);
      Blocks.FIRE.a(Blocks.WOOL, 30, 60);
      Blocks.FIRE.a(Blocks.VINE, 15, 100);
      Blocks.FIRE.a(Blocks.COAL_BLOCK, 5, 5);
      Blocks.FIRE.a(Blocks.HAY_BLOCK, 60, 20);
      Blocks.FIRE.a(Blocks.CARPET, 60, 20);
   }

   public void a(Block var1, int var2, int var3) {
      this.flameChances.put(var1, Integer.valueOf(var2));
      this.U.put(var1, Integer.valueOf(var3));
   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      return null;
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public int a(Random var1) {
      return 0;
   }

   public int a(World var1) {
      return 30;
   }

   public void b(World var1, BlockPosition var2, IBlockData var3, Random var4) {
      if(var1.getGameRules().getBoolean("doFireTick")) {
         if(!this.canPlace(var1, var2)) {
            var1.setAir(var2);
         }

         Block var5 = var1.getType(var2.down()).getBlock();
         boolean var6 = var5 == Blocks.NETHERRACK;
         if(var1.worldProvider instanceof WorldProviderTheEnd && var5 == Blocks.BEDROCK) {
            var6 = true;
         }

         if(!var6 && var1.S() && this.e(var1, var2)) {
            var1.setAir(var2);
         } else {
            int var7 = ((Integer)var3.get(AGE)).intValue();
            if(var7 < 15) {
               var3 = var3.set(AGE, Integer.valueOf(var7 + var4.nextInt(3) / 2));
               var1.setTypeAndData(var2, var3, 4);
            }

            var1.a((BlockPosition)var2, (Block)this, this.a(var1) + var4.nextInt(10));
            if(!var6) {
               if(!this.f(var1, var2)) {
                  if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down()) || var7 > 3) {
                     var1.setAir(var2);
                  }

                  return;
               }

               if(!this.e((IBlockAccess)var1, var2.down()) && var7 == 15 && var4.nextInt(4) == 0) {
                  var1.setAir(var2);
                  return;
               }
            }

            boolean var8 = var1.D(var2);
            byte var9 = 0;
            if(var8) {
               var9 = -50;
            }

            this.a(var1, var2.east(), 300 + var9, var4, var7);
            this.a(var1, var2.west(), 300 + var9, var4, var7);
            this.a(var1, var2.down(), 250 + var9, var4, var7);
            this.a(var1, var2.up(), 250 + var9, var4, var7);
            this.a(var1, var2.north(), 300 + var9, var4, var7);
            this.a(var1, var2.south(), 300 + var9, var4, var7);

            for(int var10 = -1; var10 <= 1; ++var10) {
               for(int var11 = -1; var11 <= 1; ++var11) {
                  for(int var12 = -1; var12 <= 4; ++var12) {
                     if(var10 != 0 || var12 != 0 || var11 != 0) {
                        int var13 = 100;
                        if(var12 > 1) {
                           var13 += (var12 - 1) * 100;
                        }

                        BlockPosition var14 = var2.a(var10, var12, var11);
                        int var15 = this.m(var1, var14);
                        if(var15 > 0) {
                           int var16 = (var15 + 40 + var1.getDifficulty().a() * 7) / (var7 + 30);
                           if(var8) {
                              var16 /= 2;
                           }

                           if(var16 > 0 && var4.nextInt(var13) <= var16 && (!var1.S() || !this.e(var1, var14))) {
                              int var17 = var7 + var4.nextInt(5) / 4;
                              if(var17 > 15) {
                                 var17 = 15;
                              }

                              var1.setTypeAndData(var14, var3.set(AGE, Integer.valueOf(var17)), 3);
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   protected boolean e(World var1, BlockPosition var2) {
      return var1.isRainingAt(var2) || var1.isRainingAt(var2.west()) || var1.isRainingAt(var2.east()) || var1.isRainingAt(var2.north()) || var1.isRainingAt(var2.south());
   }

   public boolean N() {
      return false;
   }

   private int c(Block var1) {
      Integer var2 = (Integer)this.U.get(var1);
      return var2 == null?0:var2.intValue();
   }

   private int d(Block var1) {
      Integer var2 = (Integer)this.flameChances.get(var1);
      return var2 == null?0:var2.intValue();
   }

   private void a(World var1, BlockPosition var2, int var3, Random var4, int var5) {
      int var6 = this.c(var1.getType(var2).getBlock());
      if(var4.nextInt(var3) < var6) {
         IBlockData var7 = var1.getType(var2);
         if(var4.nextInt(var5 + 10) < 5 && !var1.isRainingAt(var2)) {
            int var8 = var5 + var4.nextInt(5) / 4;
            if(var8 > 15) {
               var8 = 15;
            }

            var1.setTypeAndData(var2, this.getBlockData().set(AGE, Integer.valueOf(var8)), 3);
         } else {
            var1.setAir(var2);
         }

         if(var7.getBlock() == Blocks.TNT) {
            Blocks.TNT.postBreak(var1, var2, var7.set(BlockTNT.EXPLODE, Boolean.valueOf(true)));
         }
      }

   }

   private boolean f(World var1, BlockPosition var2) {
      EnumDirection[] var3 = EnumDirection.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumDirection var6 = var3[var5];
         if(this.e((IBlockAccess)var1, var2.shift(var6))) {
            return true;
         }
      }

      return false;
   }

   private int m(World var1, BlockPosition var2) {
      if(!var1.isEmpty(var2)) {
         return 0;
      } else {
         int var3 = 0;
         EnumDirection[] var4 = EnumDirection.values();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            EnumDirection var7 = var4[var6];
            var3 = Math.max(this.d(var1.getType(var2.shift(var7)).getBlock()), var3);
         }

         return var3;
      }
   }

   public boolean A() {
      return false;
   }

   public boolean e(IBlockAccess var1, BlockPosition var2) {
      return this.d(var1.getType(var2).getBlock()) > 0;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return World.a((IBlockAccess)var1, (BlockPosition)var2.down()) || this.f(var1, var2);
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down()) && !this.f(var1, var2)) {
         var1.setAir(var2);
      }

   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      if(var1.worldProvider.getDimension() > 0 || !Blocks.PORTAL.e(var1, var2)) {
         if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down()) && !this.f(var1, var2)) {
            var1.setAir(var2);
         } else {
            var1.a((BlockPosition)var2, (Block)this, this.a(var1) + var1.random.nextInt(10));
         }
      }
   }

   public MaterialMapColor g(IBlockData var1) {
      return MaterialMapColor.f;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(AGE, Integer.valueOf(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(AGE)).intValue();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{AGE, NORTH, EAST, SOUTH, WEST, UPPER, FLIP, ALT});
   }
}
