package net.minecraft.server;

import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class MaterialGas extends Material {
   public MaterialGas(MaterialMapColor var1) {
      super(var1);
      this.i();
   }

   public boolean isBuildable() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }

   public boolean isSolid() {
      return false;
   }
}
