package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHanging;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class EntityPainting extends EntityHanging {
   public EntityPainting.EnumArt art;

   public EntityPainting(World var1) {
      super(var1);
   }

   public EntityPainting(World var1, BlockPosition var2, EnumDirection var3) {
      super(var1, var2);
      ArrayList var4 = Lists.newArrayList();
      EntityPainting.EnumArt[] var5 = EntityPainting.EnumArt.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EntityPainting.EnumArt var8 = var5[var7];
         this.art = var8;
         this.setDirection(var3);
         if(this.survives()) {
            var4.add(var8);
         }
      }

      if(!var4.isEmpty()) {
         this.art = (EntityPainting.EnumArt)var4.get(this.random.nextInt(var4.size()));
      }

      this.setDirection(var3);
   }

   public void b(NBTTagCompound var1) {
      var1.setString("Motive", this.art.B);
      super.b(var1);
   }

   public void a(NBTTagCompound var1) {
      String var2 = var1.getString("Motive");
      EntityPainting.EnumArt[] var3 = EntityPainting.EnumArt.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntityPainting.EnumArt var6 = var3[var5];
         if(var6.B.equals(var2)) {
            this.art = var6;
         }
      }

      if(this.art == null) {
         this.art = EntityPainting.EnumArt.KEBAB;
      }

      super.a(var1);
   }

   public int l() {
      return this.art.C;
   }

   public int m() {
      return this.art.D;
   }

   public void b(Entity var1) {
      if(this.world.getGameRules().getBoolean("doEntityDrops")) {
         if(var1 instanceof EntityHuman) {
            EntityHuman var2 = (EntityHuman)var1;
            if(var2.abilities.canInstantlyBuild) {
               return;
            }
         }

         this.a(new ItemStack(Items.PAINTING), 0.0F);
      }
   }

   public void setPositionRotation(double var1, double var3, double var5, float var7, float var8) {
      BlockPosition var9 = this.blockPosition.a(var1 - this.locX, var3 - this.locY, var5 - this.locZ);
      this.setPosition((double)var9.getX(), (double)var9.getY(), (double)var9.getZ());
   }

   public static enum EnumArt {
      KEBAB("Kebab", 16, 16, 0, 0),
      AZTEC("Aztec", 16, 16, 16, 0),
      ALBAN("Alban", 16, 16, 32, 0),
      AZTEC_2("Aztec2", 16, 16, 48, 0),
      BOMB("Bomb", 16, 16, 64, 0),
      PLANT("Plant", 16, 16, 80, 0),
      WASTELAND("Wasteland", 16, 16, 96, 0),
      POOL("Pool", 32, 16, 0, 32),
      COURBET("Courbet", 32, 16, 32, 32),
      SEA("Sea", 32, 16, 64, 32),
      SUNSET("Sunset", 32, 16, 96, 32),
      CREEBET("Creebet", 32, 16, 128, 32),
      WANDERER("Wanderer", 16, 32, 0, 64),
      GRAHAM("Graham", 16, 32, 16, 64),
      MATCH("Match", 32, 32, 0, 128),
      BUST("Bust", 32, 32, 32, 128),
      STAGE("Stage", 32, 32, 64, 128),
      VOID("Void", 32, 32, 96, 128),
      SKULL_AND_ROSES("SkullAndRoses", 32, 32, 128, 128),
      WITHER("Wither", 32, 32, 160, 128),
      FIGHTERS("Fighters", 64, 32, 0, 96),
      POINTER("Pointer", 64, 64, 0, 192),
      PIGSCENE("Pigscene", 64, 64, 64, 192),
      BURNING_SKULL("BurningSkull", 64, 64, 128, 192),
      SKELETON("Skeleton", 64, 48, 192, 64),
      DONKEY_KONG("DonkeyKong", 64, 48, 192, 112);

      public static final int A;
      public final String B;
      public final int C;
      public final int D;
      public final int E;
      public final int F;

      private EnumArt(String var3, int var4, int var5, int var6, int var7) {
         this.B = var3;
         this.C = var4;
         this.D = var5;
         this.E = var6;
         this.F = var7;
      }

      static {
         A = "SkullAndRoses".length();
      }
   }
}
