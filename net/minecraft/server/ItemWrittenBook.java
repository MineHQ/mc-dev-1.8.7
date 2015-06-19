package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatComponentUtils;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBookAndQuill;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.PacketPlayOutSetSlot;
import net.minecraft.server.Slot;
import net.minecraft.server.StatisticList;
import net.minecraft.server.UtilColor;
import net.minecraft.server.World;

public class ItemWrittenBook extends Item {
   public ItemWrittenBook() {
      this.c(1);
   }

   public static boolean b(NBTTagCompound var0) {
      if(!ItemBookAndQuill.b(var0)) {
         return false;
      } else if(!var0.hasKeyOfType("title", 8)) {
         return false;
      } else {
         String var1 = var0.getString("title");
         return var1 != null && var1.length() <= 32?var0.hasKeyOfType("author", 8):false;
      }
   }

   public static int h(ItemStack var0) {
      return var0.getTag().getInt("generation");
   }

   public String a(ItemStack var1) {
      if(var1.hasTag()) {
         NBTTagCompound var2 = var1.getTag();
         String var3 = var2.getString("title");
         if(!UtilColor.b(var3)) {
            return var3;
         }
      }

      return super.a(var1);
   }

   public ItemStack a(ItemStack var1, World var2, EntityHuman var3) {
      if(!var2.isClientSide) {
         this.a(var1, var3);
      }

      var3.openBook(var1);
      var3.b(StatisticList.USE_ITEM_COUNT[Item.getId(this)]);
      return var1;
   }

   private void a(ItemStack var1, EntityHuman var2) {
      if(var1 != null && var1.getTag() != null) {
         NBTTagCompound var3 = var1.getTag();
         if(!var3.getBoolean("resolved")) {
            var3.setBoolean("resolved", true);
            if(b(var3)) {
               NBTTagList var4 = var3.getList("pages", 8);

               for(int var5 = 0; var5 < var4.size(); ++var5) {
                  String var6 = var4.getString(var5);

                  Object var7;
                  try {
                     IChatBaseComponent var11 = IChatBaseComponent.ChatSerializer.a(var6);
                     var7 = ChatComponentUtils.filterForDisplay(var2, var11, var2);
                  } catch (Exception var9) {
                     var7 = new ChatComponentText(var6);
                  }

                  var4.a(var5, new NBTTagString(IChatBaseComponent.ChatSerializer.a((IChatBaseComponent)var7)));
               }

               var3.set("pages", var4);
               if(var2 instanceof EntityPlayer && var2.bZ() == var1) {
                  Slot var10 = var2.activeContainer.getSlot(var2.inventory, var2.inventory.itemInHandIndex);
                  ((EntityPlayer)var2).playerConnection.sendPacket(new PacketPlayOutSetSlot(0, var10.rawSlotIndex, var1));
               }

            }
         }
      }
   }
}
