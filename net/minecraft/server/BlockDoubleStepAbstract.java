package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.INamable;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public abstract class BlockDoubleStepAbstract extends BlockStepAbstract {
   public static final BlockStateBoolean SEAMLESS = BlockStateBoolean.of("seamless");
   public static final BlockStateEnum<BlockDoubleStepAbstract.EnumStoneSlabVariant> VARIANT = BlockStateEnum.of("variant", BlockDoubleStepAbstract.EnumStoneSlabVariant.class);

   public BlockDoubleStepAbstract() {
      super(Material.STONE);
      IBlockData var1 = this.blockStateList.getBlockData();
      if(this.l()) {
         var1 = var1.set(SEAMLESS, Boolean.valueOf(false));
      } else {
         var1 = var1.set(HALF, BlockStepAbstract.EnumSlabHalf.BOTTOM);
      }

      this.j(var1.set(VARIANT, BlockDoubleStepAbstract.EnumStoneSlabVariant.STONE));
      this.a(CreativeModeTab.b);
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Item.getItemOf(Blocks.STONE_SLAB);
   }

   public String b(int var1) {
      return super.a() + "." + BlockDoubleStepAbstract.EnumStoneSlabVariant.a(var1).d();
   }

   public IBlockState<?> n() {
      return VARIANT;
   }

   public Object a(ItemStack var1) {
      return BlockDoubleStepAbstract.EnumStoneSlabVariant.a(var1.getData() & 7);
   }

   public IBlockData fromLegacyData(int var1) {
      IBlockData var2 = this.getBlockData().set(VARIANT, BlockDoubleStepAbstract.EnumStoneSlabVariant.a(var1 & 7));
      if(this.l()) {
         var2 = var2.set(SEAMLESS, Boolean.valueOf((var1 & 8) != 0));
      } else {
         var2 = var2.set(HALF, (var1 & 8) == 0?BlockStepAbstract.EnumSlabHalf.BOTTOM:BlockStepAbstract.EnumSlabHalf.TOP);
      }

      return var2;
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((BlockDoubleStepAbstract.EnumStoneSlabVariant)var1.get(VARIANT)).a();
      if(this.l()) {
         if(((Boolean)var1.get(SEAMLESS)).booleanValue()) {
            var3 |= 8;
         }
      } else if(var1.get(HALF) == BlockStepAbstract.EnumSlabHalf.TOP) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return this.l()?new BlockStateList(this, new IBlockState[]{SEAMLESS, VARIANT}):new BlockStateList(this, new IBlockState[]{HALF, VARIANT});
   }

   public int getDropData(IBlockData var1) {
      return ((BlockDoubleStepAbstract.EnumStoneSlabVariant)var1.get(VARIANT)).a();
   }

   public MaterialMapColor g(IBlockData var1) {
      return ((BlockDoubleStepAbstract.EnumStoneSlabVariant)var1.get(VARIANT)).c();
   }

   public static enum EnumStoneSlabVariant implements INamable {
      STONE,
      SAND,
      WOOD,
      COBBLESTONE,
      BRICK,
      SMOOTHBRICK,
      NETHERBRICK,
      QUARTZ;

      private static final BlockDoubleStepAbstract.EnumStoneSlabVariant[] i;
      private final int j;
      private final MaterialMapColor k;
      private final String l;
      private final String m;

      private EnumStoneSlabVariant(int var3, MaterialMapColor var4, String var5) {
         this(var3, var4, var5, var5);
      }

      private EnumStoneSlabVariant(int var3, MaterialMapColor var4, String var5, String var6) {
         this.j = var3;
         this.k = var4;
         this.l = var5;
         this.m = var6;
      }

      public int a() {
         return this.j;
      }

      public MaterialMapColor c() {
         return this.k;
      }

      public String toString() {
         return this.l;
      }

      public static BlockDoubleStepAbstract.EnumStoneSlabVariant a(int var0) {
         if(var0 < 0 || var0 >= i.length) {
            var0 = 0;
         }

         return i[var0];
      }

      public String getName() {
         return this.l;
      }

      public String d() {
         return this.m;
      }

      static {
         STONE = new BlockDoubleStepAbstract.EnumStoneSlabVariant("STONE", 0, 0, MaterialMapColor.m, "stone");
         SAND = new BlockDoubleStepAbstract.EnumStoneSlabVariant("SAND", 1, 1, MaterialMapColor.d, "sandstone", "sand");
         WOOD = new BlockDoubleStepAbstract.EnumStoneSlabVariant("WOOD", 2, 2, MaterialMapColor.o, "wood_old", "wood");
         COBBLESTONE = new BlockDoubleStepAbstract.EnumStoneSlabVariant("COBBLESTONE", 3, 3, MaterialMapColor.m, "cobblestone", "cobble");
         BRICK = new BlockDoubleStepAbstract.EnumStoneSlabVariant("BRICK", 4, 4, MaterialMapColor.D, "brick");
         SMOOTHBRICK = new BlockDoubleStepAbstract.EnumStoneSlabVariant("SMOOTHBRICK", 5, 5, MaterialMapColor.m, "stone_brick", "smoothStoneBrick");
         NETHERBRICK = new BlockDoubleStepAbstract.EnumStoneSlabVariant("NETHERBRICK", 6, 6, MaterialMapColor.K, "nether_brick", "netherBrick");
         QUARTZ = new BlockDoubleStepAbstract.EnumStoneSlabVariant("QUARTZ", 7, 7, MaterialMapColor.p, "quartz");
         i = new BlockDoubleStepAbstract.EnumStoneSlabVariant[values().length];
         BlockDoubleStepAbstract.EnumStoneSlabVariant[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            BlockDoubleStepAbstract.EnumStoneSlabVariant var3 = var0[var2];
            i[var3.a()] = var3;
         }

      }
   }
}
