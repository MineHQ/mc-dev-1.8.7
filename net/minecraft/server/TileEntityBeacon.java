package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStainedGlass;
import net.minecraft.server.BlockStainedGlassPane;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerBeacon;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EnumColor;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IInventory;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Statistic;
import net.minecraft.server.TileEntityContainer;

public class TileEntityBeacon extends TileEntityContainer implements IUpdatePlayerListBox, IInventory {
   public static final MobEffectList[][] a;
   private final List<TileEntityBeacon.BeaconColorTracker> f = Lists.newArrayList();
   private boolean i;
   private int j = -1;
   private int k;
   private int l;
   private ItemStack inventorySlot;
   private String n;

   public TileEntityBeacon() {
   }

   public void c() {
      if(this.world.getTime() % 80L == 0L) {
         this.m();
      }

   }

   public void m() {
      this.B();
      this.A();
   }

   private void A() {
      if(this.i && this.j > 0 && !this.world.isClientSide && this.k > 0) {
         double var1 = (double)(this.j * 10 + 10);
         byte var3 = 0;
         if(this.j >= 4 && this.k == this.l) {
            var3 = 1;
         }

         int var4 = this.position.getX();
         int var5 = this.position.getY();
         int var6 = this.position.getZ();
         AxisAlignedBB var7 = (new AxisAlignedBB((double)var4, (double)var5, (double)var6, (double)(var4 + 1), (double)(var5 + 1), (double)(var6 + 1))).grow(var1, var1, var1).a(0.0D, (double)this.world.getHeight(), 0.0D);
         List var8 = this.world.a(EntityHuman.class, var7);
         Iterator var9 = var8.iterator();

         EntityHuman var10;
         while(var9.hasNext()) {
            var10 = (EntityHuman)var9.next();
            var10.addEffect(new MobEffect(this.k, 180, var3, true, true));
         }

         if(this.j >= 4 && this.k != this.l && this.l > 0) {
            var9 = var8.iterator();

            while(var9.hasNext()) {
               var10 = (EntityHuman)var9.next();
               var10.addEffect(new MobEffect(this.l, 180, 0, true, true));
            }
         }
      }

   }

   private void B() {
      int var1 = this.j;
      int var2 = this.position.getX();
      int var3 = this.position.getY();
      int var4 = this.position.getZ();
      this.j = 0;
      this.f.clear();
      this.i = true;
      TileEntityBeacon.BeaconColorTracker var5 = new TileEntityBeacon.BeaconColorTracker(EntitySheep.a(EnumColor.WHITE));
      this.f.add(var5);
      boolean var6 = true;
      BlockPosition.MutableBlockPosition var7 = new BlockPosition.MutableBlockPosition();

      int var8;
      for(var8 = var3 + 1; var8 < 256; ++var8) {
         IBlockData var9 = this.world.getType(var7.c(var2, var8, var4));
         float[] var10;
         if(var9.getBlock() == Blocks.STAINED_GLASS) {
            var10 = EntitySheep.a((EnumColor)var9.get(BlockStainedGlass.COLOR));
         } else {
            if(var9.getBlock() != Blocks.STAINED_GLASS_PANE) {
               if(var9.getBlock().p() >= 15 && var9.getBlock() != Blocks.BEDROCK) {
                  this.i = false;
                  this.f.clear();
                  break;
               }

               var5.a();
               continue;
            }

            var10 = EntitySheep.a((EnumColor)var9.get(BlockStainedGlassPane.COLOR));
         }

         if(!var6) {
            var10 = new float[]{(var5.b()[0] + var10[0]) / 2.0F, (var5.b()[1] + var10[1]) / 2.0F, (var5.b()[2] + var10[2]) / 2.0F};
         }

         if(Arrays.equals(var10, var5.b())) {
            var5.a();
         } else {
            var5 = new TileEntityBeacon.BeaconColorTracker(var10);
            this.f.add(var5);
         }

         var6 = false;
      }

      if(this.i) {
         for(var8 = 1; var8 <= 4; this.j = var8++) {
            int var15 = var3 - var8;
            if(var15 < 0) {
               break;
            }

            boolean var17 = true;

            for(int var11 = var2 - var8; var11 <= var2 + var8 && var17; ++var11) {
               for(int var12 = var4 - var8; var12 <= var4 + var8; ++var12) {
                  Block var13 = this.world.getType(new BlockPosition(var11, var15, var12)).getBlock();
                  if(var13 != Blocks.EMERALD_BLOCK && var13 != Blocks.GOLD_BLOCK && var13 != Blocks.DIAMOND_BLOCK && var13 != Blocks.IRON_BLOCK) {
                     var17 = false;
                     break;
                  }
               }
            }

            if(!var17) {
               break;
            }
         }

         if(this.j == 0) {
            this.i = false;
         }
      }

      if(!this.world.isClientSide && this.j == 4 && var1 < this.j) {
         Iterator var14 = this.world.a(EntityHuman.class, (new AxisAlignedBB((double)var2, (double)var3, (double)var4, (double)var2, (double)(var3 - 4), (double)var4)).grow(10.0D, 5.0D, 10.0D)).iterator();

         while(var14.hasNext()) {
            EntityHuman var16 = (EntityHuman)var14.next();
            var16.b((Statistic)AchievementList.K);
         }
      }

   }

   public Packet getUpdatePacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.b(var1);
      return new PacketPlayOutTileEntityData(this.position, 3, var1);
   }

   private int h(int var1) {
      if(var1 >= 0 && var1 < MobEffectList.byId.length && MobEffectList.byId[var1] != null) {
         MobEffectList var2 = MobEffectList.byId[var1];
         return var2 != MobEffectList.FASTER_MOVEMENT && var2 != MobEffectList.FASTER_DIG && var2 != MobEffectList.RESISTANCE && var2 != MobEffectList.JUMP && var2 != MobEffectList.INCREASE_DAMAGE && var2 != MobEffectList.REGENERATION?0:var1;
      } else {
         return 0;
      }
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.k = this.h(var1.getInt("Primary"));
      this.l = this.h(var1.getInt("Secondary"));
      this.j = var1.getInt("Levels");
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("Primary", this.k);
      var1.setInt("Secondary", this.l);
      var1.setInt("Levels", this.j);
   }

   public int getSize() {
      return 1;
   }

   public ItemStack getItem(int var1) {
      return var1 == 0?this.inventorySlot:null;
   }

   public ItemStack splitStack(int var1, int var2) {
      if(var1 == 0 && this.inventorySlot != null) {
         if(var2 >= this.inventorySlot.count) {
            ItemStack var3 = this.inventorySlot;
            this.inventorySlot = null;
            return var3;
         } else {
            this.inventorySlot.count -= var2;
            return new ItemStack(this.inventorySlot.getItem(), var2, this.inventorySlot.getData());
         }
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(var1 == 0 && this.inventorySlot != null) {
         ItemStack var2 = this.inventorySlot;
         this.inventorySlot = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      if(var1 == 0) {
         this.inventorySlot = var2;
      }

   }

   public String getName() {
      return this.hasCustomName()?this.n:"container.beacon";
   }

   public boolean hasCustomName() {
      return this.n != null && this.n.length() > 0;
   }

   public void a(String var1) {
      this.n = var1;
   }

   public int getMaxStackSize() {
      return 1;
   }

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.position) != this?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return var2.getItem() == Items.EMERALD || var2.getItem() == Items.DIAMOND || var2.getItem() == Items.GOLD_INGOT || var2.getItem() == Items.IRON_INGOT;
   }

   public String getContainerName() {
      return "minecraft:beacon";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerBeacon(var1, this);
   }

   public int getProperty(int var1) {
      switch(var1) {
      case 0:
         return this.j;
      case 1:
         return this.k;
      case 2:
         return this.l;
      default:
         return 0;
      }
   }

   public void b(int var1, int var2) {
      switch(var1) {
      case 0:
         this.j = var2;
         break;
      case 1:
         this.k = this.h(var2);
         break;
      case 2:
         this.l = this.h(var2);
      }

   }

   public int g() {
      return 3;
   }

   public void l() {
      this.inventorySlot = null;
   }

   public boolean c(int var1, int var2) {
      if(var1 == 1) {
         this.m();
         return true;
      } else {
         return super.c(var1, var2);
      }
   }

   static {
      a = new MobEffectList[][]{{MobEffectList.FASTER_MOVEMENT, MobEffectList.FASTER_DIG}, {MobEffectList.RESISTANCE, MobEffectList.JUMP}, {MobEffectList.INCREASE_DAMAGE}, {MobEffectList.REGENERATION}};
   }

   public static class BeaconColorTracker {
      private final float[] a;
      private int b;

      public BeaconColorTracker(float[] var1) {
         this.a = var1;
         this.b = 1;
      }

      protected void a() {
         ++this.b;
      }

      public float[] b() {
         return this.a;
      }
   }
}
