package net.minecraft.server;

import com.google.common.base.Function;
import net.minecraft.server.Block;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;

public class ItemMultiTexture extends ItemBlock {
   protected final Block b;
   protected final Function<ItemStack, String> c;

   public ItemMultiTexture(Block var1, Block var2, Function<ItemStack, String> var3) {
      super(var1);
      this.b = var2;
      this.c = var3;
      this.setMaxDurability(0);
      this.a(true);
   }

   public ItemMultiTexture(Block var1, Block var2, final String[] var3) {
      this(var1, var2, new Function() {
         public String a(ItemStack var1) {
            int var2 = var1.getData();
            if(var2 < 0 || var2 >= var3.length) {
               var2 = 0;
            }

            return var3[var2];
         }

         // $FF: synthetic method
         public Object apply(Object var1) {
            return this.a((ItemStack)var1);
         }
      });
   }

   public int filterData(int var1) {
      return var1;
   }

   public String e_(ItemStack var1) {
      return super.getName() + "." + (String)this.c.apply(var1);
   }
}
