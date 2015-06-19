package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.BlockLongGrass;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateInteger;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockWood;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityFlowerPot;
import net.minecraft.server.World;

public class BlockFlowerPot extends BlockContainer {
   public static final BlockStateInteger LEGACY_DATA = BlockStateInteger.of("legacy_data", 0, 15);
   public static final BlockStateEnum<BlockFlowerPot.EnumFlowerPotContents> CONTENTS = BlockStateEnum.of("contents", BlockFlowerPot.EnumFlowerPotContents.class);

   public BlockFlowerPot() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(CONTENTS, BlockFlowerPot.EnumFlowerPotContents.EMPTY).set(LEGACY_DATA, Integer.valueOf(0)));
      this.j();
   }

   public String getName() {
      return LocaleI18n.get("item.flowerPot.name");
   }

   public void j() {
      float var1 = 0.375F;
      float var2 = var1 / 2.0F;
      this.a(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, var1, 0.5F + var2);
   }

   public boolean c() {
      return false;
   }

   public int b() {
      return 3;
   }

   public boolean d() {
      return false;
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      ItemStack var9 = var4.inventory.getItemInHand();
      if(var9 != null && var9.getItem() instanceof ItemBlock) {
         TileEntityFlowerPot var10 = this.f(var1, var2);
         if(var10 == null) {
            return false;
         } else if(var10.b() != null) {
            return false;
         } else {
            Block var11 = Block.asBlock(var9.getItem());
            if(!this.a(var11, var9.getData())) {
               return false;
            } else {
               var10.a(var9.getItem(), var9.getData());
               var10.update();
               var1.notify(var2);
               var4.b(StatisticList.T);
               if(!var4.abilities.canInstantlyBuild && --var9.count <= 0) {
                  var4.inventory.setItem(var4.inventory.itemInHandIndex, (ItemStack)null);
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private boolean a(Block var1, int var2) {
      return var1 != Blocks.YELLOW_FLOWER && var1 != Blocks.RED_FLOWER && var1 != Blocks.CACTUS && var1 != Blocks.BROWN_MUSHROOM && var1 != Blocks.RED_MUSHROOM && var1 != Blocks.SAPLING && var1 != Blocks.DEADBUSH?var1 == Blocks.TALLGRASS && var2 == BlockLongGrass.EnumTallGrassType.FERN.a():true;
   }

   public int getDropData(World var1, BlockPosition var2) {
      TileEntityFlowerPot var3 = this.f(var1, var2);
      return var3 != null && var3.b() != null?var3.c():0;
   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return super.canPlace(var1, var2) && World.a((IBlockAccess)var1, (BlockPosition)var2.down());
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      if(!World.a((IBlockAccess)var1, (BlockPosition)var2.down())) {
         this.b(var1, var2, var3, 0);
         var1.setAir(var2);
      }

   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      TileEntityFlowerPot var4 = this.f(var1, var2);
      if(var4 != null && var4.b() != null) {
         a(var1, var2, new ItemStack(var4.b(), 1, var4.c()));
      }

      super.remove(var1, var2, var3);
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      super.a(var1, var2, var3, var4);
      if(var4.abilities.canInstantlyBuild) {
         TileEntityFlowerPot var5 = this.f(var1, var2);
         if(var5 != null) {
            var5.a((Item)null, 0);
         }
      }

   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.FLOWER_POT;
   }

   private TileEntityFlowerPot f(World var1, BlockPosition var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      return var3 instanceof TileEntityFlowerPot?(TileEntityFlowerPot)var3:null;
   }

   public TileEntity a(World var1, int var2) {
      Object var3 = null;
      int var4 = 0;
      switch(var2) {
      case 1:
         var3 = Blocks.RED_FLOWER;
         var4 = BlockFlowers.EnumFlowerVarient.POPPY.b();
         break;
      case 2:
         var3 = Blocks.YELLOW_FLOWER;
         break;
      case 3:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.OAK.a();
         break;
      case 4:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.SPRUCE.a();
         break;
      case 5:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.BIRCH.a();
         break;
      case 6:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.JUNGLE.a();
         break;
      case 7:
         var3 = Blocks.RED_MUSHROOM;
         break;
      case 8:
         var3 = Blocks.BROWN_MUSHROOM;
         break;
      case 9:
         var3 = Blocks.CACTUS;
         break;
      case 10:
         var3 = Blocks.DEADBUSH;
         break;
      case 11:
         var3 = Blocks.TALLGRASS;
         var4 = BlockLongGrass.EnumTallGrassType.FERN.a();
         break;
      case 12:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.ACACIA.a();
         break;
      case 13:
         var3 = Blocks.SAPLING;
         var4 = BlockWood.EnumLogVariant.DARK_OAK.a();
      }

      return new TileEntityFlowerPot(Item.getItemOf((Block)var3), var4);
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{CONTENTS, LEGACY_DATA});
   }

   public int toLegacyData(IBlockData var1) {
      return ((Integer)var1.get(LEGACY_DATA)).intValue();
   }

   public IBlockData updateState(IBlockData var1, IBlockAccess var2, BlockPosition var3) {
      BlockFlowerPot.EnumFlowerPotContents var4 = BlockFlowerPot.EnumFlowerPotContents.EMPTY;
      TileEntity var5 = var2.getTileEntity(var3);
      if(var5 instanceof TileEntityFlowerPot) {
         TileEntityFlowerPot var6 = (TileEntityFlowerPot)var5;
         Item var7 = var6.b();
         if(var7 instanceof ItemBlock) {
            int var8 = var6.c();
            Block var9 = Block.asBlock(var7);
            if(var9 == Blocks.SAPLING) {
               switch(BlockFlowerPot.SyntheticClass_1.a[BlockWood.EnumLogVariant.a(var8).ordinal()]) {
               case 1:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.OAK_SAPLING;
                  break;
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.SPRUCE_SAPLING;
                  break;
               case 3:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.BIRCH_SAPLING;
                  break;
               case 4:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.JUNGLE_SAPLING;
                  break;
               case 5:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.ACACIA_SAPLING;
                  break;
               case 6:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.DARK_OAK_SAPLING;
                  break;
               default:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.EMPTY;
               }
            } else if(var9 == Blocks.TALLGRASS) {
               switch(var8) {
               case 0:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.DEAD_BUSH;
                  break;
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.FERN;
                  break;
               default:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.EMPTY;
               }
            } else if(var9 == Blocks.YELLOW_FLOWER) {
               var4 = BlockFlowerPot.EnumFlowerPotContents.DANDELION;
            } else if(var9 == Blocks.RED_FLOWER) {
               switch(BlockFlowerPot.SyntheticClass_1.b[BlockFlowers.EnumFlowerVarient.a(BlockFlowers.EnumFlowerType.RED, var8).ordinal()]) {
               case 1:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.POPPY;
                  break;
               case 2:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.BLUE_ORCHID;
                  break;
               case 3:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.ALLIUM;
                  break;
               case 4:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.HOUSTONIA;
                  break;
               case 5:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.RED_TULIP;
                  break;
               case 6:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.ORANGE_TULIP;
                  break;
               case 7:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.WHITE_TULIP;
                  break;
               case 8:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.PINK_TULIP;
                  break;
               case 9:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.OXEYE_DAISY;
                  break;
               default:
                  var4 = BlockFlowerPot.EnumFlowerPotContents.EMPTY;
               }
            } else if(var9 == Blocks.RED_MUSHROOM) {
               var4 = BlockFlowerPot.EnumFlowerPotContents.MUSHROOM_RED;
            } else if(var9 == Blocks.BROWN_MUSHROOM) {
               var4 = BlockFlowerPot.EnumFlowerPotContents.MUSHROOM_BROWN;
            } else if(var9 == Blocks.DEADBUSH) {
               var4 = BlockFlowerPot.EnumFlowerPotContents.DEAD_BUSH;
            } else if(var9 == Blocks.CACTUS) {
               var4 = BlockFlowerPot.EnumFlowerPotContents.CACTUS;
            }
         }
      }

      return var1.set(CONTENTS, var4);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b = new int[BlockFlowers.EnumFlowerVarient.values().length];

      static {
         try {
            b[BlockFlowers.EnumFlowerVarient.POPPY.ordinal()] = 1;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.BLUE_ORCHID.ordinal()] = 2;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.ALLIUM.ordinal()] = 3;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.HOUSTONIA.ordinal()] = 4;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.RED_TULIP.ordinal()] = 5;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.ORANGE_TULIP.ordinal()] = 6;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.WHITE_TULIP.ordinal()] = 7;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.PINK_TULIP.ordinal()] = 8;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[BlockFlowers.EnumFlowerVarient.OXEYE_DAISY.ordinal()] = 9;
         } catch (NoSuchFieldError var7) {
            ;
         }

         a = new int[BlockWood.EnumLogVariant.values().length];

         try {
            a[BlockWood.EnumLogVariant.OAK.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.SPRUCE.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.BIRCH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.JUNGLE.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.ACACIA.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[BlockWood.EnumLogVariant.DARK_OAK.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumFlowerPotContents implements INamable {
      EMPTY("empty"),
      POPPY("rose"),
      BLUE_ORCHID("blue_orchid"),
      ALLIUM("allium"),
      HOUSTONIA("houstonia"),
      RED_TULIP("red_tulip"),
      ORANGE_TULIP("orange_tulip"),
      WHITE_TULIP("white_tulip"),
      PINK_TULIP("pink_tulip"),
      OXEYE_DAISY("oxeye_daisy"),
      DANDELION("dandelion"),
      OAK_SAPLING("oak_sapling"),
      SPRUCE_SAPLING("spruce_sapling"),
      BIRCH_SAPLING("birch_sapling"),
      JUNGLE_SAPLING("jungle_sapling"),
      ACACIA_SAPLING("acacia_sapling"),
      DARK_OAK_SAPLING("dark_oak_sapling"),
      MUSHROOM_RED("mushroom_red"),
      MUSHROOM_BROWN("mushroom_brown"),
      DEAD_BUSH("dead_bush"),
      FERN("fern"),
      CACTUS("cactus");

      private final String w;

      private EnumFlowerPotContents(String var3) {
         this.w = var3;
      }

      public String toString() {
         return this.w;
      }

      public String getName() {
         return this.w;
      }
   }
}
