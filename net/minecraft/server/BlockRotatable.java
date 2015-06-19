package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateEnum;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public abstract class BlockRotatable extends Block {
   public static final BlockStateEnum<EnumDirection.EnumAxis> AXIS = BlockStateEnum.of("axis", EnumDirection.EnumAxis.class);

   protected BlockRotatable(Material var1) {
      super(var1, var1.r());
   }

   protected BlockRotatable(Material var1, MaterialMapColor var2) {
      super(var1, var2);
   }
}
