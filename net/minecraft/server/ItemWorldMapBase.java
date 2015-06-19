package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet;
import net.minecraft.server.World;

public class ItemWorldMapBase extends Item {
   protected ItemWorldMapBase() {
   }

   public boolean f() {
      return true;
   }

   public Packet c(ItemStack var1, World var2, EntityHuman var3) {
      return null;
   }
}
