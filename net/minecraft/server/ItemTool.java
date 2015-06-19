package net.minecraft.server;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.server.AttributeModifier;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class ItemTool extends Item {
   private Set<Block> c;
   protected float a = 4.0F;
   private float d;
   protected Item.EnumToolMaterial b;

   protected ItemTool(float var1, Item.EnumToolMaterial var2, Set<Block> var3) {
      this.b = var2;
      this.c = var3;
      this.maxStackSize = 1;
      this.setMaxDurability(var2.a());
      this.a = var2.b();
      this.d = var1 + var2.c();
      this.a(CreativeModeTab.i);
   }

   public float getDestroySpeed(ItemStack var1, Block var2) {
      return this.c.contains(var2)?this.a:1.0F;
   }

   public boolean a(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      var1.damage(2, var3);
      return true;
   }

   public boolean a(ItemStack var1, World var2, Block var3, BlockPosition var4, EntityLiving var5) {
      if((double)var3.g(var2, var4) != 0.0D) {
         var1.damage(1, var5);
      }

      return true;
   }

   public Item.EnumToolMaterial g() {
      return this.b;
   }

   public int b() {
      return this.b.e();
   }

   public String h() {
      return this.b.toString();
   }

   public boolean a(ItemStack var1, ItemStack var2) {
      return this.b.f() == var2.getItem()?true:super.a(var1, var2);
   }

   public Multimap<String, AttributeModifier> i() {
      Multimap var1 = super.i();
      var1.put(GenericAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(f, "Tool modifier", (double)this.d, 0));
      return var1;
   }
}
