package net.minecraft.server;

import net.minecraft.server.EnchantmentSlotType;

public abstract class CreativeModeTab {
   public static final CreativeModeTab[] a = new CreativeModeTab[12];
   public static final CreativeModeTab b = new CreativeModeTab(0, "buildingBlocks") {
   };
   public static final CreativeModeTab c = new CreativeModeTab(1, "decorations") {
   };
   public static final CreativeModeTab d = new CreativeModeTab(2, "redstone") {
   };
   public static final CreativeModeTab e = new CreativeModeTab(3, "transportation") {
   };
   public static final CreativeModeTab f;
   public static final CreativeModeTab g;
   public static final CreativeModeTab h;
   public static final CreativeModeTab i;
   public static final CreativeModeTab j;
   public static final CreativeModeTab k;
   public static final CreativeModeTab l;
   public static final CreativeModeTab m;
   private final int n;
   private final String o;
   private String p = "items.png";
   private boolean q = true;
   private boolean r = true;
   private EnchantmentSlotType[] s;

   public CreativeModeTab(int var1, String var2) {
      this.n = var1;
      this.o = var2;
      a[var1] = this;
   }

   public CreativeModeTab a(String var1) {
      this.p = var1;
      return this;
   }

   public CreativeModeTab i() {
      this.r = false;
      return this;
   }

   public CreativeModeTab k() {
      this.q = false;
      return this;
   }

   public CreativeModeTab a(EnchantmentSlotType... var1) {
      this.s = var1;
      return this;
   }

   static {
      f = (new CreativeModeTab(4, "misc") {
      }).a(new EnchantmentSlotType[]{EnchantmentSlotType.ALL});
      g = (new CreativeModeTab(5, "search") {
      }).a("item_search.png");
      h = new CreativeModeTab(6, "food") {
      };
      i = (new CreativeModeTab(7, "tools") {
      }).a(new EnchantmentSlotType[]{EnchantmentSlotType.DIGGER, EnchantmentSlotType.FISHING_ROD, EnchantmentSlotType.BREAKABLE});
      j = (new CreativeModeTab(8, "combat") {
      }).a(new EnchantmentSlotType[]{EnchantmentSlotType.ARMOR, EnchantmentSlotType.ARMOR_FEET, EnchantmentSlotType.ARMOR_HEAD, EnchantmentSlotType.ARMOR_LEGS, EnchantmentSlotType.ARMOR_TORSO, EnchantmentSlotType.BOW, EnchantmentSlotType.WEAPON});
      k = new CreativeModeTab(9, "brewing") {
      };
      l = new CreativeModeTab(10, "materials") {
      };
      m = (new CreativeModeTab(11, "inventory") {
      }).a("inventory.png").k().i();
   }
}
