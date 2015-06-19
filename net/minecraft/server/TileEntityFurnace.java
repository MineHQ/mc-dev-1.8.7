package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockFurnace;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerFurnace;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.IWorldInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemHoe;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemSword;
import net.minecraft.server.ItemTool;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.RecipesFurnace;
import net.minecraft.server.SlotFurnaceFuel;
import net.minecraft.server.TileEntityContainer;

public class TileEntityFurnace extends TileEntityContainer implements IUpdatePlayerListBox, IWorldInventory {
   private static final int[] a = new int[]{0};
   private static final int[] f = new int[]{2, 1};
   private static final int[] g = new int[]{1};
   private ItemStack[] items = new ItemStack[3];
   public int burnTime;
   private int ticksForCurrentFuel;
   public int cookTime;
   private int cookTimeTotal;
   private String m;

   public TileEntityFurnace() {
   }

   public int getSize() {
      return this.items.length;
   }

   public ItemStack getItem(int var1) {
      return this.items[var1];
   }

   public ItemStack splitStack(int var1, int var2) {
      if(this.items[var1] != null) {
         ItemStack var3;
         if(this.items[var1].count <= var2) {
            var3 = this.items[var1];
            this.items[var1] = null;
            return var3;
         } else {
            var3 = this.items[var1].a(var2);
            if(this.items[var1].count == 0) {
               this.items[var1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack splitWithoutUpdate(int var1) {
      if(this.items[var1] != null) {
         ItemStack var2 = this.items[var1];
         this.items[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemStack var2) {
      boolean var3 = var2 != null && var2.doMaterialsMatch(this.items[var1]) && ItemStack.equals(var2, this.items[var1]);
      this.items[var1] = var2;
      if(var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

      if(var1 == 0 && !var3) {
         this.cookTimeTotal = this.a(var2);
         this.cookTime = 0;
         this.update();
      }

   }

   public String getName() {
      return this.hasCustomName()?this.m:"container.furnace";
   }

   public boolean hasCustomName() {
      return this.m != null && this.m.length() > 0;
   }

   public void a(String var1) {
      this.m = var1;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      NBTTagList var2 = var1.getList("Items", 10);
      this.items = new ItemStack[this.getSize()];

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         byte var5 = var4.getByte("Slot");
         if(var5 >= 0 && var5 < this.items.length) {
            this.items[var5] = ItemStack.createStack(var4);
         }
      }

      this.burnTime = var1.getShort("BurnTime");
      this.cookTime = var1.getShort("CookTime");
      this.cookTimeTotal = var1.getShort("CookTimeTotal");
      this.ticksForCurrentFuel = fuelTime(this.items[1]);
      if(var1.hasKeyOfType("CustomName", 8)) {
         this.m = var1.getString("CustomName");
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setShort("BurnTime", (short)this.burnTime);
      var1.setShort("CookTime", (short)this.cookTime);
      var1.setShort("CookTimeTotal", (short)this.cookTimeTotal);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.items.length; ++var3) {
         if(this.items[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.items[var3].save(var4);
            var2.add(var4);
         }
      }

      var1.set("Items", var2);
      if(this.hasCustomName()) {
         var1.setString("CustomName", this.m);
      }

   }

   public int getMaxStackSize() {
      return 64;
   }

   public boolean isBurning() {
      return this.burnTime > 0;
   }

   public void c() {
      boolean var1 = this.isBurning();
      boolean var2 = false;
      if(this.isBurning()) {
         --this.burnTime;
      }

      if(!this.world.isClientSide) {
         if(!this.isBurning() && (this.items[1] == null || this.items[0] == null)) {
            if(!this.isBurning() && this.cookTime > 0) {
               this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }
         } else {
            if(!this.isBurning() && this.canBurn()) {
               this.ticksForCurrentFuel = this.burnTime = fuelTime(this.items[1]);
               if(this.isBurning()) {
                  var2 = true;
                  if(this.items[1] != null) {
                     --this.items[1].count;
                     if(this.items[1].count == 0) {
                        Item var3 = this.items[1].getItem().q();
                        this.items[1] = var3 != null?new ItemStack(var3):null;
                     }
                  }
               }
            }

            if(this.isBurning() && this.canBurn()) {
               ++this.cookTime;
               if(this.cookTime == this.cookTimeTotal) {
                  this.cookTime = 0;
                  this.cookTimeTotal = this.a(this.items[0]);
                  this.burn();
                  var2 = true;
               }
            } else {
               this.cookTime = 0;
            }
         }

         if(var1 != this.isBurning()) {
            var2 = true;
            BlockFurnace.a(this.isBurning(), this.world, this.position);
         }
      }

      if(var2) {
         this.update();
      }

   }

   public int a(ItemStack var1) {
      return 200;
   }

   private boolean canBurn() {
      if(this.items[0] == null) {
         return false;
      } else {
         ItemStack var1 = RecipesFurnace.getInstance().getResult(this.items[0]);
         return var1 == null?false:(this.items[2] == null?true:(!this.items[2].doMaterialsMatch(var1)?false:(this.items[2].count < this.getMaxStackSize() && this.items[2].count < this.items[2].getMaxStackSize()?true:this.items[2].count < var1.getMaxStackSize())));
      }
   }

   public void burn() {
      if(this.canBurn()) {
         ItemStack var1 = RecipesFurnace.getInstance().getResult(this.items[0]);
         if(this.items[2] == null) {
            this.items[2] = var1.cloneItemStack();
         } else if(this.items[2].getItem() == var1.getItem()) {
            ++this.items[2].count;
         }

         if(this.items[0].getItem() == Item.getItemOf(Blocks.SPONGE) && this.items[0].getData() == 1 && this.items[1] != null && this.items[1].getItem() == Items.BUCKET) {
            this.items[1] = new ItemStack(Items.WATER_BUCKET);
         }

         --this.items[0].count;
         if(this.items[0].count <= 0) {
            this.items[0] = null;
         }

      }
   }

   public static int fuelTime(ItemStack var0) {
      if(var0 == null) {
         return 0;
      } else {
         Item var1 = var0.getItem();
         if(var1 instanceof ItemBlock && Block.asBlock(var1) != Blocks.AIR) {
            Block var2 = Block.asBlock(var1);
            if(var2 == Blocks.WOODEN_SLAB) {
               return 150;
            }

            if(var2.getMaterial() == Material.WOOD) {
               return 300;
            }

            if(var2 == Blocks.COAL_BLOCK) {
               return 16000;
            }
         }

         return var1 instanceof ItemTool && ((ItemTool)var1).h().equals("WOOD")?200:(var1 instanceof ItemSword && ((ItemSword)var1).h().equals("WOOD")?200:(var1 instanceof ItemHoe && ((ItemHoe)var1).g().equals("WOOD")?200:(var1 == Items.STICK?100:(var1 == Items.COAL?1600:(var1 == Items.LAVA_BUCKET?20000:(var1 == Item.getItemOf(Blocks.SAPLING)?100:(var1 == Items.BLAZE_ROD?2400:0)))))));
      }
   }

   public static boolean isFuel(ItemStack var0) {
      return fuelTime(var0) > 0;
   }

   public boolean a(EntityHuman var1) {
      return this.world.getTileEntity(this.position) != this?false:var1.e((double)this.position.getX() + 0.5D, (double)this.position.getY() + 0.5D, (double)this.position.getZ() + 0.5D) <= 64.0D;
   }

   public void startOpen(EntityHuman var1) {
   }

   public void closeContainer(EntityHuman var1) {
   }

   public boolean b(int var1, ItemStack var2) {
      return var1 == 2?false:(var1 != 1?true:isFuel(var2) || SlotFurnaceFuel.c_(var2));
   }

   public int[] getSlotsForFace(EnumDirection var1) {
      return var1 == EnumDirection.DOWN?f:(var1 == EnumDirection.UP?a:g);
   }

   public boolean canPlaceItemThroughFace(int var1, ItemStack var2, EnumDirection var3) {
      return this.b(var1, var2);
   }

   public boolean canTakeItemThroughFace(int var1, ItemStack var2, EnumDirection var3) {
      if(var3 == EnumDirection.DOWN && var1 == 1) {
         Item var4 = var2.getItem();
         if(var4 != Items.WATER_BUCKET && var4 != Items.BUCKET) {
            return false;
         }
      }

      return true;
   }

   public String getContainerName() {
      return "minecraft:furnace";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerFurnace(var1, this);
   }

   public int getProperty(int var1) {
      switch(var1) {
      case 0:
         return this.burnTime;
      case 1:
         return this.ticksForCurrentFuel;
      case 2:
         return this.cookTime;
      case 3:
         return this.cookTimeTotal;
      default:
         return 0;
      }
   }

   public void b(int var1, int var2) {
      switch(var1) {
      case 0:
         this.burnTime = var2;
         break;
      case 1:
         this.ticksForCurrentFuel = var2;
         break;
      case 2:
         this.cookTime = var2;
         break;
      case 3:
         this.cookTimeTotal = var2;
      }

   }

   public int g() {
      return 4;
   }

   public void l() {
      for(int var1 = 0; var1 < this.items.length; ++var1) {
         this.items[var1] = null;
      }

   }
}
