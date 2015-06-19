package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.Blocks;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public class TileEntityPiston extends TileEntity implements IUpdatePlayerListBox {
   private IBlockData a;
   private EnumDirection f;
   private boolean g;
   private boolean h;
   private float i;
   private float j;
   private List<Entity> k = Lists.newArrayList();

   public TileEntityPiston() {
   }

   public TileEntityPiston(IBlockData var1, EnumDirection var2, boolean var3, boolean var4) {
      this.a = var1;
      this.f = var2;
      this.g = var3;
      this.h = var4;
   }

   public IBlockData b() {
      return this.a;
   }

   public int u() {
      return 0;
   }

   public boolean d() {
      return this.g;
   }

   public EnumDirection e() {
      return this.f;
   }

   public float a(float var1) {
      if(var1 > 1.0F) {
         var1 = 1.0F;
      }

      return this.j + (this.i - this.j) * var1;
   }

   private void a(float var1, float var2) {
      if(this.g) {
         var1 = 1.0F - var1;
      } else {
         --var1;
      }

      AxisAlignedBB var3 = Blocks.PISTON_EXTENSION.a(this.world, this.position, this.a, var1, this.f);
      if(var3 != null) {
         List var4 = this.world.getEntities((Entity)null, var3);
         if(!var4.isEmpty()) {
            this.k.addAll(var4);
            Iterator var5 = this.k.iterator();

            while(true) {
               while(var5.hasNext()) {
                  Entity var6 = (Entity)var5.next();
                  if(this.a.getBlock() == Blocks.SLIME && this.g) {
                     switch(TileEntityPiston.SyntheticClass_1.a[this.f.k().ordinal()]) {
                     case 1:
                        var6.motX = (double)this.f.getAdjacentX();
                        break;
                     case 2:
                        var6.motY = (double)this.f.getAdjacentY();
                        break;
                     case 3:
                        var6.motZ = (double)this.f.getAdjacentZ();
                     }
                  } else {
                     var6.move((double)(var2 * (float)this.f.getAdjacentX()), (double)(var2 * (float)this.f.getAdjacentY()), (double)(var2 * (float)this.f.getAdjacentZ()));
                  }
               }

               this.k.clear();
               break;
            }
         }
      }

   }

   public void h() {
      if(this.j < 1.0F && this.world != null) {
         this.j = this.i = 1.0F;
         this.world.t(this.position);
         this.y();
         if(this.world.getType(this.position).getBlock() == Blocks.PISTON_EXTENSION) {
            this.world.setTypeAndData(this.position, this.a, 3);
            this.world.d(this.position, this.a.getBlock());
         }
      }

   }

   public void c() {
      this.j = this.i;
      if(this.j >= 1.0F) {
         this.a(1.0F, 0.25F);
         this.world.t(this.position);
         this.y();
         if(this.world.getType(this.position).getBlock() == Blocks.PISTON_EXTENSION) {
            this.world.setTypeAndData(this.position, this.a, 3);
            this.world.d(this.position, this.a.getBlock());
         }

      } else {
         this.i += 0.5F;
         if(this.i >= 1.0F) {
            this.i = 1.0F;
         }

         if(this.g) {
            this.a(this.i, this.i - this.j + 0.0625F);
         }

      }
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = Block.getById(var1.getInt("blockId")).fromLegacyData(var1.getInt("blockData"));
      this.f = EnumDirection.fromType1(var1.getInt("facing"));
      this.j = this.i = var1.getFloat("progress");
      this.g = var1.getBoolean("extending");
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("blockId", Block.getId(this.a.getBlock()));
      var1.setInt("blockData", this.a.getBlock().toLegacyData(this.a));
      var1.setInt("facing", this.f.a());
      var1.setFloat("progress", this.j);
      var1.setBoolean("extending", this.g);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a = new int[EnumDirection.EnumAxis.values().length];

      static {
         try {
            a[EnumDirection.EnumAxis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[EnumDirection.EnumAxis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
