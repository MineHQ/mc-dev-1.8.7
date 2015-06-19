package net.minecraft.server;

import java.io.File;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.FileIOThread;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.RegionFileCache;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldNBTStorage;
import net.minecraft.server.WorldProvider;
import net.minecraft.server.WorldProviderHell;
import net.minecraft.server.WorldProviderTheEnd;

public class ServerNBTManager extends WorldNBTStorage {
   public ServerNBTManager(File var1, String var2, boolean var3) {
      super(var1, var2, var3);
   }

   public IChunkLoader createChunkLoader(WorldProvider var1) {
      File var2 = this.getDirectory();
      File var3;
      if(var1 instanceof WorldProviderHell) {
         var3 = new File(var2, "DIM-1");
         var3.mkdirs();
         return new ChunkRegionLoader(var3);
      } else if(var1 instanceof WorldProviderTheEnd) {
         var3 = new File(var2, "DIM1");
         var3.mkdirs();
         return new ChunkRegionLoader(var3);
      } else {
         return new ChunkRegionLoader(var2);
      }
   }

   public void saveWorldData(WorldData var1, NBTTagCompound var2) {
      var1.e(19133);
      super.saveWorldData(var1, var2);
   }

   public void a() {
      try {
         FileIOThread.a().b();
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      RegionFileCache.a();
   }
}
