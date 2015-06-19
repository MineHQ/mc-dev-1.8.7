package net.minecraft.server;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.BlockStatePredicate;
import net.minecraft.server.Blocks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWither;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.LocaleI18n;
import net.minecraft.server.Material;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.ShapeDetector;
import net.minecraft.server.ShapeDetectorBlock;
import net.minecraft.server.ShapeDetectorBuilder;
import net.minecraft.server.Statistic;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntitySkull;
import net.minecraft.server.World;

public class BlockSkull extends BlockContainer {
   public static final BlockStateDirection FACING = BlockStateDirection.of("facing");
   public static final BlockStateBoolean NODROP = BlockStateBoolean.of("nodrop");
   private static final Predicate<ShapeDetectorBlock> N = new Predicate() {
      public boolean a(ShapeDetectorBlock var1) {
         return var1.a() != null && var1.a().getBlock() == Blocks.SKULL && var1.b() instanceof TileEntitySkull && ((TileEntitySkull)var1.b()).getSkullType() == 1;
      }

      // $FF: synthetic method
      public boolean apply(Object var1) {
         return this.a((ShapeDetectorBlock)var1);
      }
   };
   private ShapeDetector O;
   private ShapeDetector P;

   protected BlockSkull() {
      super(Material.ORIENTABLE);
      this.j(this.blockStateList.getBlockData().set(FACING, EnumDirection.NORTH).set(NODROP, Boolean.valueOf(false)));
      this.a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
   }

   public String getName() {
      return LocaleI18n.get("tile.skull.skeleton.name");
   }

   public boolean c() {
      return false;
   }

   public boolean d() {
      return false;
   }

   public void updateShape(IBlockAccess var1, BlockPosition var2) {
      switch(BlockSkull.SyntheticClass_1.a[((EnumDirection)var1.getType(var2).get(FACING)).ordinal()]) {
      case 1:
      default:
         this.a(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
         break;
      case 2:
         this.a(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
         break;
      case 3:
         this.a(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
         break;
      case 4:
         this.a(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
         break;
      case 5:
         this.a(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
      }

   }

   public AxisAlignedBB a(World var1, BlockPosition var2, IBlockData var3) {
      this.updateShape(var1, var2);
      return super.a(var1, var2, var3);
   }

   public IBlockData getPlacedState(World var1, BlockPosition var2, EnumDirection var3, float var4, float var5, float var6, int var7, EntityLiving var8) {
      return this.getBlockData().set(FACING, var8.getDirection()).set(NODROP, Boolean.valueOf(false));
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntitySkull();
   }

   public int getDropData(World var1, BlockPosition var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      return var3 instanceof TileEntitySkull?((TileEntitySkull)var3).getSkullType():super.getDropData(var1, var2);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4) {
      if(var4.abilities.canInstantlyBuild) {
         var3 = var3.set(NODROP, Boolean.valueOf(true));
         var1.setTypeAndData(var2, var3, 4);
      }

      super.a(var1, var2, var3, var4);
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         if(!((Boolean)var3.get(NODROP)).booleanValue()) {
            TileEntity var4 = var1.getTileEntity(var2);
            if(var4 instanceof TileEntitySkull) {
               TileEntitySkull var5 = (TileEntitySkull)var4;
               ItemStack var6 = new ItemStack(Items.SKULL, 1, this.getDropData(var1, var2));
               if(var5.getSkullType() == 3 && var5.getGameProfile() != null) {
                  var6.setTag(new NBTTagCompound());
                  NBTTagCompound var7 = new NBTTagCompound();
                  GameProfileSerializer.serialize(var7, var5.getGameProfile());
                  var6.getTag().set("SkullOwner", var7);
               }

               a(var1, var2, (ItemStack)var6);
            }
         }

         super.remove(var1, var2, var3);
      }
   }

   public Item getDropType(IBlockData var1, Random var2, int var3) {
      return Items.SKULL;
   }

   public boolean b(World var1, BlockPosition var2, ItemStack var3) {
      return var3.getData() == 1 && var2.getY() >= 2 && var1.getDifficulty() != EnumDifficulty.PEACEFUL && !var1.isClientSide?this.l().a(var1, var2) != null:false;
   }

   public void a(World var1, BlockPosition var2, TileEntitySkull var3) {
      if(var3.getSkullType() == 1 && var2.getY() >= 2 && var1.getDifficulty() != EnumDifficulty.PEACEFUL && !var1.isClientSide) {
         ShapeDetector var4 = this.n();
         ShapeDetector.ShapeDetectorCollection var5 = var4.a(var1, var2);
         if(var5 != null) {
            int var6;
            for(var6 = 0; var6 < 3; ++var6) {
               ShapeDetectorBlock var7 = var5.a(var6, 0, 0);
               var1.setTypeAndData(var7.d(), var7.a().set(NODROP, Boolean.valueOf(true)), 2);
            }

            for(var6 = 0; var6 < var4.c(); ++var6) {
               for(int var13 = 0; var13 < var4.b(); ++var13) {
                  ShapeDetectorBlock var8 = var5.a(var6, var13, 0);
                  var1.setTypeAndData(var8.d(), Blocks.AIR.getBlockData(), 2);
               }
            }

            BlockPosition var12 = var5.a(1, 0, 0).d();
            EntityWither var14 = new EntityWither(var1);
            BlockPosition var15 = var5.a(1, 2, 0).d();
            var14.setPositionRotation((double)var15.getX() + 0.5D, (double)var15.getY() + 0.55D, (double)var15.getZ() + 0.5D, var5.b().k() == EnumDirection.EnumAxis.X?0.0F:90.0F, 0.0F);
            var14.aI = var5.b().k() == EnumDirection.EnumAxis.X?0.0F:90.0F;
            var14.n();
            Iterator var9 = var1.a(EntityHuman.class, var14.getBoundingBox().grow(50.0D, 50.0D, 50.0D)).iterator();

            while(var9.hasNext()) {
               EntityHuman var10 = (EntityHuman)var9.next();
               var10.b((Statistic)AchievementList.I);
            }

            var1.addEntity(var14);

            int var16;
            for(var16 = 0; var16 < 120; ++var16) {
               var1.addParticle(EnumParticle.SNOWBALL, (double)var12.getX() + var1.random.nextDouble(), (double)(var12.getY() - 2) + var1.random.nextDouble() * 3.9D, (double)var12.getZ() + var1.random.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for(var16 = 0; var16 < var4.c(); ++var16) {
               for(int var17 = 0; var17 < var4.b(); ++var17) {
                  ShapeDetectorBlock var11 = var5.a(var16, var17, 0);
                  var1.update(var11.d(), Blocks.AIR);
               }
            }

         }
      }
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(FACING, EnumDirection.fromType1(var1 & 7)).set(NODROP, Boolean.valueOf((var1 & 8) > 0));
   }

   public int toLegacyData(IBlockData var1) {
      byte var2 = 0;
      int var3 = var2 | ((EnumDirection)var1.get(FACING)).a();
      if(((Boolean)var1.get(NODROP)).booleanValue()) {
         var3 |= 8;
      }

      return var3;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{FACING, NODROP});
   }

   protected ShapeDetector l() {
      if(this.O == null) {
         this.O = ShapeDetectorBuilder.a().a(new String[]{"   ", "###", "~#~"}).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SOUL_SAND))).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
      }

      return this.O;
   }

   protected ShapeDetector n() {
      if(this.P == null) {
         this.P = ShapeDetectorBuilder.a().a(new String[]{"^^^", "###", "~#~"}).a('#', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.SOUL_SAND))).a('^', N).a('~', ShapeDetectorBlock.a(BlockStatePredicate.a(Blocks.AIR))).b();
      }

      return this.P;
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.values().length];

      static {
         try {
            a[EnumDirection.UP.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[EnumDirection.NORTH.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[EnumDirection.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EAST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
