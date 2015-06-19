package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class TileEntityNote extends TileEntity {
   public byte note;
   public boolean f;

   public TileEntityNote() {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setByte("note", this.note);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.note = var1.getByte("note");
      this.note = (byte)MathHelper.clamp(this.note, 0, 24);
   }

   public void b() {
      this.note = (byte)((this.note + 1) % 25);
      this.update();
   }

   public void play(World var1, BlockPosition var2) {
      if(var1.getType(var2.up()).getBlock().getMaterial() == Material.AIR) {
         Material var3 = var1.getType(var2.down()).getBlock().getMaterial();
         byte var4 = 0;
         if(var3 == Material.STONE) {
            var4 = 1;
         }

         if(var3 == Material.SAND) {
            var4 = 2;
         }

         if(var3 == Material.SHATTERABLE) {
            var4 = 3;
         }

         if(var3 == Material.WOOD) {
            var4 = 4;
         }

         var1.playBlockAction(var2, Blocks.NOTEBLOCK, var4, this.note);
      }
   }
}
