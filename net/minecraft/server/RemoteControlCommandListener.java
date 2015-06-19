package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class RemoteControlCommandListener implements ICommandListener {
   private static final RemoteControlCommandListener instance = new RemoteControlCommandListener();
   private StringBuffer b = new StringBuffer();

   public RemoteControlCommandListener() {
   }

   public static RemoteControlCommandListener getInstance() {
      return instance;
   }

   public void i() {
      this.b.setLength(0);
   }

   public String j() {
      return this.b.toString();
   }

   public String getName() {
      return "Rcon";
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return new ChatComponentText(this.getName());
   }

   public void sendMessage(IChatBaseComponent var1) {
      this.b.append(var1.c());
   }

   public boolean a(int var1, String var2) {
      return true;
   }

   public BlockPosition getChunkCoordinates() {
      return new BlockPosition(0, 0, 0);
   }

   public Vec3D d() {
      return new Vec3D(0.0D, 0.0D, 0.0D);
   }

   public World getWorld() {
      return MinecraftServer.getServer().getWorld();
   }

   public Entity f() {
      return null;
   }

   public boolean getSendCommandFeedback() {
      return true;
   }

   public void a(CommandObjectiveExecutor.EnumCommandResult var1, int var2) {
   }
}
