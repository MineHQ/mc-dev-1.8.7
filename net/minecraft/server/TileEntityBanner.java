package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockFlowers;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumColor;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.TileEntity;

public class TileEntityBanner extends TileEntity {
   public int color;
   public NBTTagList patterns;
   private boolean g;
   private List<TileEntityBanner.EnumBannerPatternType> h;
   private List<EnumColor> i;
   private String j;

   public TileEntityBanner() {
   }

   public void a(ItemStack var1) {
      this.patterns = null;
      if(var1.hasTag() && var1.getTag().hasKeyOfType("BlockEntityTag", 10)) {
         NBTTagCompound var2 = var1.getTag().getCompound("BlockEntityTag");
         if(var2.hasKey("Patterns")) {
            this.patterns = (NBTTagList)var2.getList("Patterns", 10).clone();
         }

         if(var2.hasKeyOfType("Base", 99)) {
            this.color = var2.getInt("Base");
         } else {
            this.color = var1.getData() & 15;
         }
      } else {
         this.color = var1.getData() & 15;
      }

      this.h = null;
      this.i = null;
      this.j = "";
      this.g = true;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      a(var1, this.color, this.patterns);
   }

   public static void a(NBTTagCompound var0, int var1, NBTTagList var2) {
      var0.setInt("Base", var1);
      if(var2 != null) {
         var0.set("Patterns", var2);
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.color = var1.getInt("Base");
      this.patterns = var1.getList("Patterns", 10);
      this.h = null;
      this.i = null;
      this.j = null;
      this.g = true;
   }

   public Packet getUpdatePacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.b(var1);
      return new PacketPlayOutTileEntityData(this.position, 6, var1);
   }

   public int b() {
      return this.color;
   }

   public static int b(ItemStack var0) {
      NBTTagCompound var1 = var0.a("BlockEntityTag", false);
      return var1 != null && var1.hasKey("Base")?var1.getInt("Base"):var0.getData();
   }

   public static int c(ItemStack var0) {
      NBTTagCompound var1 = var0.a("BlockEntityTag", false);
      return var1 != null && var1.hasKey("Patterns")?var1.getList("Patterns", 10).size():0;
   }

   public NBTTagList d() {
      return this.patterns;
   }

   public static void e(ItemStack var0) {
      NBTTagCompound var1 = var0.a("BlockEntityTag", false);
      if(var1 != null && var1.hasKeyOfType("Patterns", 9)) {
         NBTTagList var2 = var1.getList("Patterns", 10);
         if(var2.size() > 0) {
            var2.a(var2.size() - 1);
            if(var2.isEmpty()) {
               var0.getTag().remove("BlockEntityTag");
               if(var0.getTag().isEmpty()) {
                  var0.setTag((NBTTagCompound)null);
               }
            }

         }
      }
   }

   public static enum EnumBannerPatternType {
      BASE("base", "b"),
      SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "),
      SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"),
      SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "),
      SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "),
      STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"),
      STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "),
      STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "),
      STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"),
      STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "),
      STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "),
      STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"),
      STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "),
      STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "),
      CROSS("cross", "cr", "# #", " # ", "# #"),
      STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "),
      TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"),
      TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "),
      TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "),
      TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "),
      DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "),
      DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"),
      DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "),
      DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "),
      CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "),
      RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "),
      HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "),
      HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "),
      HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"),
      HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"),
      BORDER("border", "bo", "###", "# #", "###"),
      CURLY_BORDER,
      CREEPER,
      GRADIENT,
      GRADIENT_UP,
      BRICKS,
      SKULL,
      FLOWER,
      MOJANG;

      private String N;
      private String O;
      private String[] P;
      private ItemStack Q;

      private EnumBannerPatternType(String var3, String var4) {
         this.P = new String[3];
         this.N = var3;
         this.O = var4;
      }

      private EnumBannerPatternType(String var3, String var4, ItemStack var5) {
         this(var3, var4);
         this.Q = var5;
      }

      private EnumBannerPatternType(String var3, String var4, String var5, String var6, String var7) {
         this(var3, var4);
         this.P[0] = var5;
         this.P[1] = var6;
         this.P[2] = var7;
      }

      public String b() {
         return this.O;
      }

      public String[] c() {
         return this.P;
      }

      public boolean d() {
         return this.Q != null || this.P[0] != null;
      }

      public boolean e() {
         return this.Q != null;
      }

      public ItemStack f() {
         return this.Q;
      }

      static {
         CURLY_BORDER = new TileEntityBanner.EnumBannerPatternType("CURLY_BORDER", 31, "curly_border", "cbo", new ItemStack(Blocks.VINE));
         CREEPER = new TileEntityBanner.EnumBannerPatternType("CREEPER", 32, "creeper", "cre", new ItemStack(Items.SKULL, 1, 4));
         GRADIENT = new TileEntityBanner.EnumBannerPatternType("GRADIENT", 33, "gradient", "gra", "# #", " # ", " # ");
         GRADIENT_UP = new TileEntityBanner.EnumBannerPatternType("GRADIENT_UP", 34, "gradient_up", "gru", " # ", " # ", "# #");
         BRICKS = new TileEntityBanner.EnumBannerPatternType("BRICKS", 35, "bricks", "bri", new ItemStack(Blocks.BRICK_BLOCK));
         SKULL = new TileEntityBanner.EnumBannerPatternType("SKULL", 36, "skull", "sku", new ItemStack(Items.SKULL, 1, 1));
         FLOWER = new TileEntityBanner.EnumBannerPatternType("FLOWER", 37, "flower", "flo", new ItemStack(Blocks.RED_FLOWER, 1, BlockFlowers.EnumFlowerVarient.OXEYE_DAISY.b()));
         MOJANG = new TileEntityBanner.EnumBannerPatternType("MOJANG", 38, "mojang", "moj", new ItemStack(Items.GOLDEN_APPLE, 1, 1));
      }
   }
}
