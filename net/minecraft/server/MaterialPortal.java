package net.minecraft.server;

import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public class MaterialPortal extends Material {
   public MaterialPortal(MaterialMapColor var1) {
      super(var1);
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
