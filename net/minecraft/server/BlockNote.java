package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Material;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityNote;
import net.minecraft.server.World;

public class BlockNote extends BlockContainer {
   private static final List<String> a = Lists.newArrayList((Object[])(new String[]{"harp", "bd", "snare", "hat", "bassattack"}));

   public BlockNote() {
      super(Material.WOOD);
      this.a(CreativeModeTab.d);
   }

   public void doPhysics(World var1, BlockPosition var2, IBlockData var3, Block var4) {
      boolean var5 = var1.isBlockIndirectlyPowered(var2);
      TileEntity var6 = var1.getTileEntity(var2);
      if(var6 instanceof TileEntityNote) {
         TileEntityNote var7 = (TileEntityNote)var6;
         if(var7.f != var5) {
            if(var5) {
               var7.play(var1, var2);
            }

            var7.f = var5;
         }
      }

   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(var1.isClientSide) {
         return true;
      } else {
         TileEntity var9 = var1.getTileEntity(var2);
         if(var9 instanceof TileEntityNote) {
            TileEntityNote var10 = (TileEntityNote)var9;
            var10.b();
            var10.play(var1, var2);
            var4.b(StatisticList.S);
         }

         return true;
      }
   }

   public void attack(World var1, BlockPosition var2, EntityHuman var3) {
      if(!var1.isClientSide) {
         TileEntity var4 = var1.getTileEntity(var2);
         if(var4 instanceof TileEntityNote) {
            ((TileEntityNote)var4).play(var1, var2);
            var3.b(StatisticList.R);
         }

      }
   }

   public TileEntity a(World var1, int var2) {
      return new TileEntityNote();
   }

   private String b(int var1) {
      if(var1 < 0 || var1 >= a.size()) {
         var1 = 0;
      }

      return (String)a.get(var1);
   }

   public boolean a(World var1, BlockPosition var2, IBlockData var3, int var4, int var5) {
      float var6 = (float)Math.pow(2.0D, (double)(var5 - 12) / 12.0D);
      var1.makeSound((double)var2.getX() + 0.5D, (double)var2.getY() + 0.5D, (double)var2.getZ() + 0.5D, "note." + this.b(var4), 3.0F, var6);
      var1.addParticle(EnumParticle.NOTE, (double)var2.getX() + 0.5D, (double)var2.getY() + 1.2D, (double)var2.getZ() + 0.5D, (double)var5 / 24.0D, 0.0D, 0.0D, new int[0]);
      return true;
   }

   public int b() {
      return 3;
   }
}
