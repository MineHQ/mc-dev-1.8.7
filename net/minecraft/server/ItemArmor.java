package net.minecraft.server;

import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockDispenser;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.DispenseBehaviorItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IDispenseBehavior;
import net.minecraft.server.IEntitySelector;
import net.minecraft.server.ISourceBlock;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.World;

public class ItemArmor extends Item {
   private static final int[] k = new int[]{11, 16, 15, 13};
   public static final String[] a = new String[]{"minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots"};
   private static final IDispenseBehavior l = new DispenseBehaviorItem() {
      protected ItemStack b(ISourceBlock var1, ItemStack var2) {
         BlockPosition var3 = var1.getBlockPosition().shift(BlockDispenser.b(var1.f()));
         int var4 = var3.getX();
         int var5 = var3.getY();
         int var6 = var3.getZ();
         AxisAlignedBB var7 = new AxisAlignedBB((double)var4, (double)var5, (double)var6, (double)(var4 + 1), (double)(var5 + 1), (double)(var6 + 1));
         List var8 = var1.i().a(EntityLiving.class, var7, Predicates.and(IEntitySelector.d, new IEntitySelector.EntitySelectorEquipable(var2)));
         if(var8.size() > 0) {
            EntityLiving var9 = (EntityLiving)var8.get(0);
            int var10 = var9 instanceof EntityHuman?1:0;
            int var11 = EntityInsentient.c(var2);
            ItemStack var12 = var2.cloneItemStack();
            var12.count = 1;
            var9.setEquipment(var11 - var10, var12);
            if(var9 instanceof EntityInsentient) {
               ((EntityInsentient)var9).a(var11, 2.0F);
            }

            --var2.count;
            return var2;
         } else {
            return super.b(var1, var2);
         }
      }
   };
   public final int b;
   public final int c;
   public final int d;
   private final ItemArmor.EnumArmorMaterial m;

   public ItemArmor(ItemArmor.EnumArmorMaterial var1, int var2, int var3) {
      this.m = var1;
      this.b = var3;
      this.d = var2;
      this.c = var1.b(var3);
      this.setMaxDurability(var1.a(var3));
      this.maxStackSize = 1;
      this.a(CreativeModeTab.j);
      BlockDispenser.N.a(this, l);
   }

   public int b() {
      return this.m.a();
   }

   public ItemArmor.EnumArmorMaterial x_() {
      return this.m;
   }

   public boolean d_(ItemStack var1) {
      return this.m != ItemArmor.EnumArmorMaterial.LEATHER?false:(!var1.hasTag()?false:(!var1.getTag().hasKeyOfType("display", 10)?false:var1.getTag().getCompound("display").hasKeyOfType("color", 3)));
   }

   public int b(ItemStack var1) {
      if(this.m != ItemArmor.EnumArmorMaterial.LEATHER) {
         return -1;
      } else {
         NBTTagCompound var2 = var1.getTag();
         if(var2 != null) {
            NBTTagCompound var3 = var2.getCompound("display");
            if(var3 != null && var3.hasKeyOfType("color", 3)) {
               return var3.getInt("color");
            }
         }

         return 10511680;
      }
   }

   public void c(ItemStack var1) {
      if(this.m == ItemArmor.EnumArmorMaterial.LEATHER) {
         NBTTagCompound var2 = var1.getTag();
         if(var2 != null) {
            NBTTagCompound var3 = var2.getCompound("display");
            if(var3.hasKey("color")) {
               var3.remove("color");
            }

         }
      }
   }

   public void b(ItemStack var1, int var2) {
      if(this.m != ItemArmor.EnumArmorMaterial.LEATHER) {
         throw new UnsupportedOperationException("Can\'t dye non-leather!");
      } else {
         NBTTagCompound var3 = var1.getTag();
         if(var3 == null) {
            var3 = new NBTTagCompound();
            var1.setTag(var3);
         }

         NBTTagCompound var4 = var3.getCompound("display");
         if(!var3.hasKeyOfType("display", 10)) {
            var3.set("display", var4);
         }

         var4.setInt("color", var2);
      }
   }

   public boolean a(ItemStack var1, ItemStack var2) {
      return this.m.b() == var2.getItem()?true:super.a(var1, var2);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      int var4 = EntityInsentient.c(var1) - 1;
      ItemStack var5 = var3.q(var4);
      if(var5 == null) {
         var3.setEquipment(var4, var1.cloneItemStack());
         var1.count = 0;
      }

      return var1;
   }

   public static enum EnumArmorMaterial {
      LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15),
      CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12),
      IRON("iron", 15, new int[]{2, 6, 5, 2}, 9),
      GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
      DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);

      private final String f;
      private final int g;
      private final int[] h;
      private final int i;

      private EnumArmorMaterial(String var3, int var4, int[] var5, int var6) {
         this.f = var3;
         this.g = var4;
         this.h = var5;
         this.i = var6;
      }

      public int a(int var1) {
         return ItemArmor.k[var1] * this.g;
      }

      public int b(int var1) {
         return this.h[var1];
      }

      public int a() {
         return this.i;
      }

      public Item b() {
         return this == LEATHER?Items.LEATHER:(this == CHAIN?Items.IRON_INGOT:(this == GOLD?Items.GOLD_INGOT:(this == IRON?Items.IRON_INGOT:(this == DIAMOND?Items.DIAMOND:null))));
      }
   }
}
