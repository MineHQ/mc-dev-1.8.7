package net.minecraft.server;

import java.io.File;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.IPlayerFileData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldProvider;

public interface IDataManager {
   WorldData getWorldData();

   void checkSession() throws ExceptionWorldConflict;

   IChunkLoader createChunkLoader(WorldProvider var1);

   void saveWorldData(WorldData var1, NBTTagCompound var2);

   void saveWorldData(WorldData var1);

   IPlayerFileData getPlayerFileData();

   void a();

   File getDirectory();

   File getDataFile(String var1);

   String g();
}
