package net.minecraft.server;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.ChestLock;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ITileEntityContainer;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;

public abstract class TileEntityContainer extends TileEntity implements ITileEntityContainer, ITileInventory {
   private ChestLock a;

   public TileEntityContainer() {
      this.a = ChestLock.a;
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a = ChestLock.b(var1);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      if(this.a != null) {
         this.a.a(var1);
      }

   }

   public boolean r_() {
      return this.a != null && !this.a.a();
   }

   public ChestLock i() {
      return this.a;
   }

   public void a(ChestLock var1) {
      this.a = var1;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return (IChatBaseComponent)(this.hasCustomName()?new ChatComponentText(this.getName()):new ChatMessage(this.getName(), new Object[0]));
   }
}
