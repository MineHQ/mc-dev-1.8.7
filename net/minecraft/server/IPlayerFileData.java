package net.minecraft.server;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.NBTTagCompound;

public interface IPlayerFileData {
   void save(EntityHuman var1);

   NBTTagCompound load(EntityHuman var1);

   String[] getSeenPlayers();
}
