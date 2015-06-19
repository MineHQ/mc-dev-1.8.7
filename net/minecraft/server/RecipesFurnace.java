package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.Block;
import net.minecraft.server.BlockSmoothBrick;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumColor;
import net.minecraft.server.Item;
import net.minecraft.server.ItemFish;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;

public class RecipesFurnace {
   private static final RecipesFurnace a = new RecipesFurnace();
   public Map<ItemStack, ItemStack> recipes = Maps.newHashMap();
   private Map<ItemStack, Float> c = Maps.newHashMap();

   public static RecipesFurnace getInstance() {
      return a;
   }

   private RecipesFurnace() {
      this.registerRecipe(Blocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
      this.registerRecipe(Blocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
      this.registerRecipe(Blocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
      this.registerRecipe(Blocks.SAND, new ItemStack(Blocks.GLASS), 0.1F);
      this.a(Items.PORKCHOP, new ItemStack(Items.COOKED_PORKCHOP), 0.35F);
      this.a(Items.BEEF, new ItemStack(Items.COOKED_BEEF), 0.35F);
      this.a(Items.CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 0.35F);
      this.a(Items.RABBIT, new ItemStack(Items.COOKED_RABBIT), 0.35F);
      this.a(Items.MUTTON, new ItemStack(Items.COOKED_MUTTON), 0.35F);
      this.registerRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE), 0.1F);
      this.a(new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.b), new ItemStack(Blocks.STONEBRICK, 1, BlockSmoothBrick.O), 0.1F);
      this.a(Items.CLAY_BALL, new ItemStack(Items.BRICK), 0.3F);
      this.registerRecipe(Blocks.CLAY, new ItemStack(Blocks.HARDENED_CLAY), 0.35F);
      this.registerRecipe(Blocks.CACTUS, new ItemStack(Items.DYE, 1, EnumColor.GREEN.getInvColorIndex()), 0.2F);
      this.registerRecipe(Blocks.LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
      this.registerRecipe(Blocks.LOG2, new ItemStack(Items.COAL, 1, 1), 0.15F);
      this.registerRecipe(Blocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0F);
      this.a(Items.POTATO, new ItemStack(Items.BAKED_POTATO), 0.35F);
      this.registerRecipe(Blocks.NETHERRACK, new ItemStack(Items.NETHERBRICK), 0.1F);
      this.a(new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Blocks.SPONGE, 1, 0), 0.15F);
      ItemFish.EnumFish[] var1 = ItemFish.EnumFish.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ItemFish.EnumFish var4 = var1[var3];
         if(var4.g()) {
            this.a(new ItemStack(Items.FISH, 1, var4.a()), new ItemStack(Items.COOKED_FISH, 1, var4.a()), 0.35F);
         }
      }

      this.registerRecipe(Blocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
      this.registerRecipe(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
      this.registerRecipe(Blocks.LAPIS_ORE, new ItemStack(Items.DYE, 1, EnumColor.BLUE.getInvColorIndex()), 0.2F);
      this.registerRecipe(Blocks.QUARTZ_ORE, new ItemStack(Items.QUARTZ), 0.2F);
   }

   public void registerRecipe(Block var1, ItemStack var2, float var3) {
      this.a(Item.getItemOf(var1), var2, var3);
   }

   public void a(Item var1, ItemStack var2, float var3) {
      this.a(new ItemStack(var1, 1, 32767), var2, var3);
   }

   public void a(ItemStack var1, ItemStack var2, float var3) {
      this.recipes.put(var1, var2);
      this.c.put(var2, Float.valueOf(var3));
   }

   public ItemStack getResult(ItemStack var1) {
      Iterator var2 = this.recipes.entrySet().iterator();

      Entry var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (Entry)var2.next();
      } while(!this.a(var1, (ItemStack)var3.getKey()));

      return (ItemStack)var3.getValue();
   }

   private boolean a(ItemStack var1, ItemStack var2) {
      return var2.getItem() == var1.getItem() && (var2.getData() == 32767 || var2.getData() == var1.getData());
   }

   public Map<ItemStack, ItemStack> getRecipes() {
      return this.recipes;
   }

   public float b(ItemStack var1) {
      Iterator var2 = this.c.entrySet().iterator();

      Entry var3;
      do {
         if(!var2.hasNext()) {
            return 0.0F;
         }

         var3 = (Entry)var2.next();
      } while(!this.a(var1, (ItemStack)var3.getKey()));

      return ((Float)var3.getValue()).floatValue();
   }
}
