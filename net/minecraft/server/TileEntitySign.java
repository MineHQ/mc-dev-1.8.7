package net.minecraft.server;

import com.google.gson.JsonParseException;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatClickable;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatComponentUtils;
import net.minecraft.server.ChatModifier;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutUpdateSign;
import net.minecraft.server.TileEntity;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class TileEntitySign extends TileEntity {
   public final IChatBaseComponent[] lines = new IChatBaseComponent[]{new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText(""), new ChatComponentText("")};
   public int f = -1;
   public boolean isEditable = true;
   private EntityHuman h;
   private final CommandObjectiveExecutor i = new CommandObjectiveExecutor();

   public TileEntitySign() {
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         String var3 = IChatBaseComponent.ChatSerializer.a(this.lines[var2]);
         var1.setString("Text" + (var2 + 1), var3);
      }

      this.i.b(var1);
   }

   public void a(NBTTagCompound var1) {
      this.isEditable = false;
      super.a(var1);
      ICommandListener var2 = new ICommandListener() {
         public String getName() {
            return "Sign";
         }

         public IChatBaseComponent getScoreboardDisplayName() {
            return new ChatComponentText(this.getName());
         }

         public void sendMessage(IChatBaseComponent var1) {
         }

         public boolean a(int var1, String var2) {
            return true;
         }

         public BlockPosition getChunkCoordinates() {
            return TileEntitySign.this.position;
         }

         public Vec3D d() {
            return new Vec3D((double)TileEntitySign.this.position.getX() + 0.5D, (double)TileEntitySign.this.position.getY() + 0.5D, (double)TileEntitySign.this.position.getZ() + 0.5D);
         }

         public World getWorld() {
            return TileEntitySign.this.world;
         }

         public Entity f() {
            return null;
         }

         public boolean getSendCommandFeedback() {
            return false;
         }

         public void a(CommandObjectiveExecutor.EnumCommandResult var1, int var2) {
         }
      };

      for(int var3 = 0; var3 < 4; ++var3) {
         String var4 = var1.getString("Text" + (var3 + 1));

         try {
            IChatBaseComponent var5 = IChatBaseComponent.ChatSerializer.a(var4);

            try {
               this.lines[var3] = ChatComponentUtils.filterForDisplay(var2, var5, (Entity)null);
            } catch (CommandException var7) {
               this.lines[var3] = var5;
            }
         } catch (JsonParseException var8) {
            this.lines[var3] = new ChatComponentText(var4);
         }
      }

      this.i.a(var1);
   }

   public Packet getUpdatePacket() {
      IChatBaseComponent[] var1 = new IChatBaseComponent[4];
      System.arraycopy(this.lines, 0, var1, 0, 4);
      return new PacketPlayOutUpdateSign(this.world, this.position, var1);
   }

   public boolean F() {
      return true;
   }

   public boolean b() {
      return this.isEditable;
   }

   public void a(EntityHuman var1) {
      this.h = var1;
   }

   public EntityHuman c() {
      return this.h;
   }

   public boolean b(final EntityHuman var1) {
      ICommandListener var2 = new ICommandListener() {
         public String getName() {
            return var1.getName();
         }

         public IChatBaseComponent getScoreboardDisplayName() {
            return var1.getScoreboardDisplayName();
         }

         public void sendMessage(IChatBaseComponent var1x) {
         }

         public boolean a(int var1x, String var2) {
            return var1x <= 2;
         }

         public BlockPosition getChunkCoordinates() {
            return TileEntitySign.this.position;
         }

         public Vec3D d() {
            return new Vec3D((double)TileEntitySign.this.position.getX() + 0.5D, (double)TileEntitySign.this.position.getY() + 0.5D, (double)TileEntitySign.this.position.getZ() + 0.5D);
         }

         public World getWorld() {
            return var1.getWorld();
         }

         public Entity f() {
            return var1;
         }

         public boolean getSendCommandFeedback() {
            return false;
         }

         public void a(CommandObjectiveExecutor.EnumCommandResult var1x, int var2) {
            TileEntitySign.this.i.a(this, var1x, var2);
         }
      };

      for(int var3 = 0; var3 < this.lines.length; ++var3) {
         ChatModifier var4 = this.lines[var3] == null?null:this.lines[var3].getChatModifier();
         if(var4 != null && var4.h() != null) {
            ChatClickable var5 = var4.h();
            if(var5.a() == ChatClickable.EnumClickAction.RUN_COMMAND) {
               MinecraftServer.getServer().getCommandHandler().a(var2, var5.b());
            }
         }
      }

      return true;
   }

   public CommandObjectiveExecutor d() {
      return this.i;
   }
}
