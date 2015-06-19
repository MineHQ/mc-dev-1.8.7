package net.minecraft.server;

import net.minecraft.server.BlockChest;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.EntityMinecartContainer;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.Item;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.World;

public class EntityMinecartChest extends EntityMinecartContainer {
   public EntityMinecartChest(World var1) {
      super(var1);
   }

   public EntityMinecartChest(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   public void a(DamageSource var1) {
      super.a(var1);
      if(this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.a(Item.getItemOf(Blocks.CHEST), 1, 0.0F);
      }

   }

   public int getSize() {
      return 27;
   }

   public EntityMinecartAbstract.EnumMinecartType s() {
      return EntityMinecartAbstract.EnumMinecartType.CHEST;
   }

   public IBlockData u() {
      return Blocks.CHEST.getBlockData().set(BlockChest.FACING, EnumDirection.NORTH);
   }

   public int w() {
      return 8;
   }

   public String getContainerName() {
      return "minecraft:chest";
   }

   public Container createContainer(PlayerInventory var1, EntityHuman var2) {
      return new ContainerChest(var1, this, var2);
   }
}
