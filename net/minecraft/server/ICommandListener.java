package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Entity;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public interface ICommandListener {
   String getName();

   IChatBaseComponent getScoreboardDisplayName();

   void sendMessage(IChatBaseComponent var1);

   boolean a(int var1, String var2);

   BlockPosition getChunkCoordinates();

   Vec3D d();

   World getWorld();

   Entity f();

   boolean getSendCommandFeedback();

   void a(CommandObjectiveExecutor.EnumCommandResult var1, int var2);
}
