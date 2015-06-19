package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class TileEntityCommand extends TileEntity {
   private final CommandBlockListenerAbstract a = new CommandBlockListenerAbstract() {
      public BlockPosition getChunkCoordinates() {
         return TileEntityCommand.this.position;
      }

      public Vec3D d() {
         return new Vec3D((double)TileEntityCommand.this.position.getX() + 0.5D, (double)TileEntityCommand.this.position.getY() + 0.5D, (double)TileEntityCommand.this.position.getZ() + 0.5D);
      }

      public World getWorld() {
         return TileEntityCommand.this.getWorld();
      }

      public void setCommand(String var1) {
         super.setCommand(var1);
         TileEntityCommand.this.update();
      }

      public void h() {
         TileEntityCommand.this.getWorld().notify(TileEntityCommand.this.position);
      }

      public Entity f() {
         return null;
      }
   };

   public TileEntityCommand() {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      this.a.a(var1);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.a.b(var1);
   }

   public Packet getUpdatePacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.b(var1);
      return new PacketPlayOutTileEntityData(this.position, 2, var1);
   }

   public boolean F() {
      return true;
   }

   public CommandBlockListenerAbstract getCommandBlock() {
      return this.a;
   }

   public CommandObjectiveExecutor c() {
      return this.a.n();
   }
}
