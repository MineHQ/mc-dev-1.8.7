package net.minecraft.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.Random;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatHoverable;
import net.minecraft.server.Enchantment;
import net.minecraft.server.EnchantmentDurability;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumItemRarity;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBow;
import net.minecraft.server.MinecraftKey;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.StatisticList;
import net.minecraft.server.World;

public final class ItemStack {
   public static final DecimalFormat a = new DecimalFormat("#.###");
   public int count;
   public int c;
   private Item item;
   private NBTTagCompound tag;
   private int damage;
   private EntityItemFrame g;
   private Block h;
   private boolean i;
   private Block j;
   private boolean k;

   public ItemStack(Block var1) {
      this((Block)var1, 1);
   }

   public ItemStack(Block var1, int var2) {
      this((Block)var1, var2, 0);
   }

   public ItemStack(Block var1, int var2, int var3) {
      this(Item.getItemOf(var1), var2, var3);
   }

   public ItemStack(Item var1) {
      this((Item)var1, 1);
   }

   public ItemStack(Item var1, int var2) {
      this((Item)var1, var2, 0);
   }

   public ItemStack(Item var1, int var2, int var3) {
      this.h = null;
      this.i = false;
      this.j = null;
      this.k = false;
      this.item = var1;
      this.count = var2;
      this.damage = var3;
      if(this.damage < 0) {
         this.damage = 0;
      }

   }

   public static ItemStack createStack(NBTTagCompound var0) {
      ItemStack var1 = new ItemStack();
      var1.c(var0);
      return var1.getItem() != null?var1:null;
   }

   private ItemStack() {
      this.h = null;
      this.i = false;
      this.j = null;
      this.k = false;
   }

   public ItemStack a(int var1) {
      ItemStack var2 = new ItemStack(this.item, var1, this.damage);
      if(this.tag != null) {
         var2.tag = (NBTTagCompound)this.tag.clone();
      }

      this.count -= var1;
      return var2;
   }

   public Item getItem() {
      return this.item;
   }

   public boolean placeItem(EntityHuman var1, World var2, BlockPosition var3, EnumDirection var4, float var5, float var6, float var7) {
      boolean var8 = this.getItem().interactWith(this, var1, var2, var3, var4, var5, var6, var7);
      if(var8) {
         var1.b(StatisticList.USE_ITEM_COUNT[Item.getId(this.item)]);
      }

      return var8;
   }

   public float a(Block var1) {
      return this.getItem().getDestroySpeed(this, var1);
   }

   public ItemStack a(World var1, EntityHuman var2) {
      return this.getItem().a(this, var1, var2);
   }

   public ItemStack b(World var1, EntityHuman var2) {
      return this.getItem().b(this, var1, var2);
   }

   public NBTTagCompound save(NBTTagCompound var1) {
      MinecraftKey var2 = (MinecraftKey)Item.REGISTRY.c(this.item);
      var1.setString("id", var2 == null?"minecraft:air":var2.toString());
      var1.setByte("Count", (byte)this.count);
      var1.setShort("Damage", (short)this.damage);
      if(this.tag != null) {
         var1.set("tag", this.tag);
      }

      return var1;
   }

   public void c(NBTTagCompound var1) {
      if(var1.hasKeyOfType("id", 8)) {
         this.item = Item.d(var1.getString("id"));
      } else {
         this.item = Item.getById(var1.getShort("id"));
      }

      this.count = var1.getByte("Count");
      this.damage = var1.getShort("Damage");
      if(this.damage < 0) {
         this.damage = 0;
      }

      if(var1.hasKeyOfType("tag", 10)) {
         this.tag = var1.getCompound("tag");
         if(this.item != null) {
            this.item.a(this.tag);
         }
      }

   }

   public int getMaxStackSize() {
      return this.getItem().getMaxStackSize();
   }

   public boolean isStackable() {
      return this.getMaxStackSize() > 1 && (!this.e() || !this.g());
   }

   public boolean e() {
      return this.item == null?false:(this.item.getMaxDurability() <= 0?false:!this.hasTag() || !this.getTag().getBoolean("Unbreakable"));
   }

   public boolean usesData() {
      return this.item.k();
   }

   public boolean g() {
      return this.e() && this.damage > 0;
   }

   public int h() {
      return this.damage;
   }

   public int getData() {
      return this.damage;
   }

   public void setData(int var1) {
      this.damage = var1;
      if(this.damage < 0) {
         this.damage = 0;
      }

   }

   public int j() {
      return this.item.getMaxDurability();
   }

   public boolean isDamaged(int var1, Random var2) {
      if(!this.e()) {
         return false;
      } else {
         if(var1 > 0) {
            int var3 = EnchantmentManager.getEnchantmentLevel(Enchantment.DURABILITY.id, this);
            int var4 = 0;

            for(int var5 = 0; var3 > 0 && var5 < var1; ++var5) {
               if(EnchantmentDurability.a(this, var3, var2)) {
                  ++var4;
               }
            }

            var1 -= var4;
            if(var1 <= 0) {
               return false;
            }
         }

         this.damage += var1;
         return this.damage > this.j();
      }
   }

   public void damage(int var1, EntityLiving var2) {
      if(!(var2 instanceof EntityHuman) || !((EntityHuman)var2).abilities.canInstantlyBuild) {
         if(this.e()) {
            if(this.isDamaged(var1, var2.bc())) {
               var2.b(this);
               --this.count;
               if(var2 instanceof EntityHuman) {
                  EntityHuman var3 = (EntityHuman)var2;
                  var3.b(StatisticList.BREAK_ITEM_COUNT[Item.getId(this.item)]);
                  if(this.count == 0 && this.getItem() instanceof ItemBow) {
                     var3.ca();
                  }
               }

               if(this.count < 0) {
                  this.count = 0;
               }

               this.damage = 0;
            }

         }
      }
   }

   public void a(EntityLiving var1, EntityHuman var2) {
      boolean var3 = this.item.a(this, (EntityLiving)var1, (EntityLiving)var2);
      if(var3) {
         var2.b(StatisticList.USE_ITEM_COUNT[Item.getId(this.item)]);
      }

   }

   public void a(World var1, Block var2, BlockPosition var3, EntityHuman var4) {
      boolean var5 = this.item.a(this, var1, var2, var3, var4);
      if(var5) {
         var4.b(StatisticList.USE_ITEM_COUNT[Item.getId(this.item)]);
      }

   }

   public boolean b(Block var1) {
      return this.item.canDestroySpecialBlock(var1);
   }

   public boolean a(EntityHuman var1, EntityLiving var2) {
      return this.item.a(this, var1, var2);
   }

   public ItemStack cloneItemStack() {
      ItemStack var1 = new ItemStack(this.item, this.count, this.damage);
      if(this.tag != null) {
         var1.tag = (NBTTagCompound)this.tag.clone();
      }

      return var1;
   }

   public static boolean equals(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null?true:(var0 != null && var1 != null?(var0.tag == null && var1.tag != null?false:var0.tag == null || var0.tag.equals(var1.tag)):false);
   }

   public static boolean matches(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null?true:(var0 != null && var1 != null?var0.d(var1):false);
   }

   private boolean d(ItemStack var1) {
      return this.count != var1.count?false:(this.item != var1.item?false:(this.damage != var1.damage?false:(this.tag == null && var1.tag != null?false:this.tag == null || this.tag.equals(var1.tag))));
   }

   public static boolean c(ItemStack var0, ItemStack var1) {
      return var0 == null && var1 == null?true:(var0 != null && var1 != null?var0.doMaterialsMatch(var1):false);
   }

   public boolean doMaterialsMatch(ItemStack var1) {
      return var1 != null && this.item == var1.item && this.damage == var1.damage;
   }

   public String a() {
      return this.item.e_(this);
   }

   public static ItemStack b(ItemStack var0) {
      return var0 == null?null:var0.cloneItemStack();
   }

   public String toString() {
      return this.count + "x" + this.item.getName() + "@" + this.damage;
   }

   public void a(World var1, Entity var2, int var3, boolean var4) {
      if(this.c > 0) {
         --this.c;
      }

      this.item.a(this, var1, var2, var3, var4);
   }

   public void a(World var1, EntityHuman var2, int var3) {
      var2.a(StatisticList.CRAFT_BLOCK_COUNT[Item.getId(this.item)], var3);
      this.item.d(this, var1, var2);
   }

   public int l() {
      return this.getItem().d(this);
   }

   public EnumAnimation m() {
      return this.getItem().e(this);
   }

   public void b(World var1, EntityHuman var2, int var3) {
      this.getItem().a(this, var1, var2, var3);
   }

   public boolean hasTag() {
      return this.tag != null;
   }

   public NBTTagCompound getTag() {
      return this.tag;
   }

   public NBTTagCompound a(String var1, boolean var2) {
      if(this.tag != null && this.tag.hasKeyOfType(var1, 10)) {
         return this.tag.getCompound(var1);
      } else if(var2) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.a((String)var1, (NBTBase)var3);
         return var3;
      } else {
         return null;
      }
   }

   public NBTTagList getEnchantments() {
      return this.tag == null?null:this.tag.getList("ench", 10);
   }

   public void setTag(NBTTagCompound var1) {
      this.tag = var1;
   }

   public String getName() {
      String var1 = this.getItem().a(this);
      if(this.tag != null && this.tag.hasKeyOfType("display", 10)) {
         NBTTagCompound var2 = this.tag.getCompound("display");
         if(var2.hasKeyOfType("Name", 8)) {
            var1 = var2.getString("Name");
         }
      }

      return var1;
   }

   public ItemStack c(String var1) {
      if(this.tag == null) {
         this.tag = new NBTTagCompound();
      }

      if(!this.tag.hasKeyOfType("display", 10)) {
         this.tag.set("display", new NBTTagCompound());
      }

      this.tag.getCompound("display").setString("Name", var1);
      return this;
   }

   public void r() {
      if(this.tag != null) {
         if(this.tag.hasKeyOfType("display", 10)) {
            NBTTagCompound var1 = this.tag.getCompound("display");
            var1.remove("Name");
            if(var1.isEmpty()) {
               this.tag.remove("display");
               if(this.tag.isEmpty()) {
                  this.setTag((NBTTagCompound)null);
               }
            }

         }
      }
   }

   public boolean hasName() {
      return this.tag == null?false:(!this.tag.hasKeyOfType("display", 10)?false:this.tag.getCompound("display").hasKeyOfType("Name", 8));
   }

   public EnumItemRarity u() {
      return this.getItem().g(this);
   }

   public boolean v() {
      return !this.getItem().f_(this)?false:!this.hasEnchantments();
   }

   public void addEnchantment(Enchantment var1, int var2) {
      if(this.tag == null) {
         this.setTag(new NBTTagCompound());
      }

      if(!this.tag.hasKeyOfType("ench", 9)) {
         this.tag.set("ench", new NBTTagList());
      }

      NBTTagList var3 = this.tag.getList("ench", 10);
      NBTTagCompound var4 = new NBTTagCompound();
      var4.setShort("id", (short)var1.id);
      var4.setShort("lvl", (short)((byte)var2));
      var3.add(var4);
   }

   public boolean hasEnchantments() {
      return this.tag != null && this.tag.hasKeyOfType("ench", 9);
   }

   public void a(String var1, NBTBase var2) {
      if(this.tag == null) {
         this.setTag(new NBTTagCompound());
      }

      this.tag.set(var1, var2);
   }

   public boolean x() {
      return this.getItem().s();
   }

   public boolean y() {
      return this.g != null;
   }

   public void a(EntityItemFrame var1) {
      this.g = var1;
   }

   public EntityItemFrame z() {
      return this.g;
   }

   public int getRepairCost() {
      return this.hasTag() && this.tag.hasKeyOfType("RepairCost", 3)?this.tag.getInt("RepairCost"):0;
   }

   public void setRepairCost(int var1) {
      if(!this.hasTag()) {
         this.tag = new NBTTagCompound();
      }

      this.tag.setInt("RepairCost", var1);
   }

   public Multimap<String, AttributeModifier> B() {
      Object var1;
      if(this.hasTag() && this.tag.hasKeyOfType("AttributeModifiers", 9)) {
         var1 = HashMultimap.create();
         NBTTagList var2 = this.tag.getList("AttributeModifiers", 10);

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            NBTTagCompound var4 = var2.get(var3);
            AttributeModifier var5 = GenericAttributes.a(var4);
            if(var5 != null && var5.a().getLeastSignificantBits() != 0L && var5.a().getMostSignificantBits() != 0L) {
               ((Multimap)var1).put(var4.getString("AttributeName"), var5);
            }
         }
      } else {
         var1 = this.getItem().i();
      }

      return (Multimap)var1;
   }

   public void setItem(Item var1) {
      this.item = var1;
   }

   public IChatBaseComponent C() {
      ChatComponentText var1 = new ChatComponentText(this.getName());
      if(this.hasName()) {
         var1.getChatModifier().setItalic(Boolean.valueOf(true));
      }

      IChatBaseComponent var2 = (new ChatComponentText("[")).addSibling(var1).a("]");
      if(this.item != null) {
         NBTTagCompound var3 = new NBTTagCompound();
         this.save(var3);
         var2.getChatModifier().setChatHoverable(new ChatHoverable(ChatHoverable.EnumHoverAction.SHOW_ITEM, new ChatComponentText(var3.toString())));
         var2.getChatModifier().setColor(this.u().e);
      }

      return var2;
   }

   public boolean c(Block var1) {
      if(var1 == this.h) {
         return this.i;
      } else {
         this.h = var1;
         if(this.hasTag() && this.tag.hasKeyOfType("CanDestroy", 9)) {
            NBTTagList var2 = this.tag.getList("CanDestroy", 8);

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               Block var4 = Block.getByName(var2.getString(var3));
               if(var4 == var1) {
                  this.i = true;
                  return true;
               }
            }
         }

         this.i = false;
         return false;
      }
   }

   public boolean d(Block var1) {
      if(var1 == this.j) {
         return this.k;
      } else {
         this.j = var1;
         if(this.hasTag() && this.tag.hasKeyOfType("CanPlaceOn", 9)) {
            NBTTagList var2 = this.tag.getList("CanPlaceOn", 8);

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               Block var4 = Block.getByName(var2.getString(var3));
               if(var4 == var1) {
                  this.k = true;
                  return true;
               }
            }
         }

         this.k = false;
         return false;
      }
   }
}
