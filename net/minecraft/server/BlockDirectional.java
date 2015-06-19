package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockStateDirection;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;

public abstract class BlockDirectional extends Block {
   public static final BlockStateDirection FACING;

   protected BlockDirectional(Material var1) {
      super(var1);
   }

   protected BlockDirectional(Material var1, MaterialMapColor var2) {
      super(var1, var2);
   }

   static {
      FACING = BlockStateDirection.of("facing", EnumDirection.EnumDirectionLimit.HORIZONTAL);
   }
}
