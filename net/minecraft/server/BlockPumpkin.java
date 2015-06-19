package net.minecraft.server;

import com.google.common.base.Predicate;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStatePredicate;
import net.minecraft.server.Blocks;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.ShapeDetector;
import net.minecraft.server.ShapeDetectorBlock;
import net.minecraft.server.ShapeDetectorBuilder;
import net.minecraft.server.World;

public class BlockPumpkin extends BlockDirectional {
   private ShapeDetector snowGolemPart;
   private ShapeDetector snowGolem;
   private ShapeDetector ironGolemPart;
   private ShapeDetector ironGolem;
   private static final Predicate<IBlockData> Q = new Predicate() {
      public boolean a(IBlockData var1) {
         return var1 != null && (var1.getBlock() == Blocks.PUMPKIN || var1.getBlock() == Blocks.LIT_PUMPKIN);
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((IBlockData)var1);
      }
   };

   protected BlockPumpkin() {
      super(Material.PUMPKIN, MaterialMapColor.q);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH));
      this.a(true);
      this.a(CreativeModeTab.b);
   }

   public void onPlace(World var1, BlockPosition var2, IBlockData var3) {
      super.onPlace(var1, var2, var3);
      this.f(var1, var2);
   }

   public boolean e(World var1, BlockPosition var2) {
      return this.getDetectorSnowGolemPart().a(var1, var2) != null || this.getDetectorIronGolemPart().a(var1, var2) != null;
   }

   private void f(World var1, BlockPosition var2) {
      ShapeDetector.ShapeDetectorCollection var3;
      int var4;
      int var6;
      if((var3 = this.getDetectorSnowGolem().a(var1, var2)) != null) {
         for(var4 = 0; var4 < this.getDetectorSnowGolem().b(); ++var4) {
            ShapeDetectorBlock var5 = var3.a(0, var4, 0);
            var1.setTypeAndData(var5.d(), Blocks.AIR.getBlockData(), 2);
         }

         EntitySnowman var9 = new EntitySnowman(var1);
         BlockPosition var10 = var3.a(0, 2, 0).d();
         var9.setPositionRotation((double)var10.getX() + 0.5D, (double)var10.getY() + 0.05D, (double)var10.getZ() + 0.5D, 0.0F, 0.0F);
         var1.addEntity(var9);

         for(var6 = 0; var6 < 120; ++var6) {
            var1.addParticle(EnumParticle.SNOW_SHOVEL, (double)var10.getX() + var1.random.nextDouble(), (double)var10.getY() + var1.random.nextDouble() * 2.5D, (double)var10.getZ() + var1.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
         }

         for(var6 = 0; var6 < this.getDetectorSnowGolem().b(); ++var6) {
            ShapeDetectorBlock var7 = var3.a(0, var6, 0);
            var1.update(var7.d(), Blocks.AIR);
         }
      } else if((var3 = this.getDetectorIronGolem().a(var1, var2)) != null) {
         for(var4 = 0; var4 < this.getDetectorIronGolem().c(); ++var4) {
            for(int var12 = 0; var12 < this.getDetectorIronGolem().b(); ++var12) {
               var1.setTypeAndData(var3.a(var4, var12, 0).d(), Blocks.AIR.getBlockData(), 2);
            }
         }

         BlockPosition var11 = var3.a(1, 2, 0).d();
         EntityIronGolem var13 = new EntityIronGolem(var1);
         var13.setPlayerCreated(true);
         var13.setPositionRotation((double)var11.getX() + 0.5D, (double)var11.getY() + 0.05D, (double)var11.getZ() + 0.5D, 0.0F, 0.0F);
         var1.addEntity(var13);

         for(var6 = 0; var6 < 120; ++var6) {
            var1.addParticle(EnumParticle.SNOWBALL, (double)var11.getX() + var1.random.nextDouble(), (double)var11.getY() + var1.random.nextDouble() * 3.9D, (double)var11.getZ() + var1.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
         }

         for(var6 = 0; var6 < this.getDetectorIronGolem().c(); ++var6) {
            for(int var14 = 0; var14 < this.getDetectorIronGolem().b(); ++var14) {
               ShapeDetectorBlock var8 = var3.a(var6, var14, 0);
               var1.update(var8.d(), Blocks.AIR);
            }
         }
      }

   }

   public boolean canPlace(World var1, BlockPosition var2) {
      return var1.getType(var2).getBlock().material.isReplaceable() && World.a((IBlockAccess)var1, (BlockPosition)var2.down());
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection().opposite());
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, EnumDirection.fromType2(var1));
   }

   public int toLegacyData(IBlockData var1) {
      return ((EnumDirection)var1.get(FACING)).b();
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING});
   }

   protected ShapeDetector getDetectorSnowGolemPart() {
      if(this.snowGolemPart == null) {
         this.snowGolemPart = ShapeDetectorBuilder.a().a(new String[]{" ", "#", "#"}).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SNOW))).b();
      }

      return this.snowGolemPart;
   }

   protected ShapeDetector getDetectorSnowGolem() {
      if(this.snowGolem == null) {
         this.snowGolem = ShapeDetectorBuilder.a().a(new String[]{"^", "#", "#"}).a('^', ShapeDetectorBlock.a(Q)).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SNOW))).b();
      }

      return this.snowGolem;
   }

   protected ShapeDetector getDetectorIronGolemPart() {
      if(this.ironGolemPart == null) {
         this.ironGolemPart = ShapeDetectorBuilder.a().a(new String[]{"~ ~", "###", "~#~"}).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.IRON_BLOCK))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
      }

      return this.ironGolemPart;
   }

   protected ShapeDetector getDetectorIronGolem() {
      if(this.ironGolem == null) {
         this.ironGolem = ShapeDetectorBuilder.a().a(new String[]{"~^~", "###", "~#~"}).a('^', ShapeDetectorBlock.a(Q)).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.IRON_BLOCK))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
      }

      return this.ironGolem;
   }
}
