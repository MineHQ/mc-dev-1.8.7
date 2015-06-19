package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityMinecartAbstract;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class EntityMinecartCommandBlock extends EntityMinecartAbstract {
   private final CommandBlockListenerAbstract a = new CommandBlockListenerAbstract() {
      public void h() {
         EntityMinecartCommandBlock.this.getDataWatcher().watch(23, this.getCommand());
         EntityMinecartCommandBlock.this.getDataWatcher().watch(24, IChatBaseComponent.ChatSerializer.a(this.k()));
      }

      public BlockPosition getChunkCoordinates() {
         return new BlockPosition(EntityMinecartCommandBlock.this.locX, EntityMinecartCommandBlock.this.locY + 0.5D, EntityMinecartCommandBlock.this.locZ);
      }

      public Vec3D d() {
         return new Vec3D(EntityMinecartCommandBlock.this.locX, EntityMinecartCommandBlock.this.locY, EntityMinecartCommandBlock.this.locZ);
      }

      public World getWorld() {
         return EntityMinecartCommandBlock.this.world;
      }

      public Entity f() {
         return EntityMinecartCommandBlock.this;
      }
   };
   private int b = 0;

   public EntityMinecartCommandBlock(World var1) {
      super(var1);
   }

   public EntityMinecartCommandBlock(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   protected void h() {
      super.h();
      this.getDataWatcher().a(23, "");
      this.getDataWatcher().a(24, "");
   }

   protected void a(NBTTagCompound var1) {
      super.a(var1);
      this.a.b(var1);
      this.getDataWatcher().watch(23, this.getCommandBlock().getCommand());
      this.getDataWatcher().watch(24, IChatBaseComponent.ChatSerializer.a(this.getCommandBlock().k()));
   }

   protected void b(NBTTagCompound var1) {
      super.b(var1);
      this.a.a(var1);
   }

   public EntityMinecartAbstract.EnumMinecartType s() {
      return EntityMinecartAbstract.EnumMinecartType.COMMAND_BLOCK;
   }

   public IBlockData u() {
      return Blocks.COMMAND_BLOCK.getBlockData();
   }

   public CommandBlockListenerAbstract getCommandBlock() {
      return this.a;
   }

   public void a(int var1, int var2, int var3, boolean var4) {
      if(var4 && this.ticksLived - this.b >= 4) {
         this.getCommandBlock().a(this.world);
         this.b = this.ticksLived;
      }

   }

   public boolean e(EntityHuman var1) {
      this.a.a(var1);
      return false;
   }

   public void i(int var1) {
      super.i(var1);
      if(var1 == 24) {
         try {
            this.a.b(IChatBaseComponent.ChatSerializer.a(this.getDataWatcher().getString(24)));
         } catch (Throwable var3) {
            ;
         }
      } else if(var1 == 23) {
         this.a.setCommand(this.getDataWatcher().getString(23));
      }

   }
}
