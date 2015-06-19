package net.minecraft.server;

import java.io.IOException;
import net.minecraft.server.Chunk;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.World;

public interface IChunkLoader {
   Chunk a(World var1, int var2, int var3) throws IOException;

   void a(World var1, Chunk var2) throws IOException, ExceptionWorldConflict;

   void b(World var1, Chunk var2) throws IOException;

   void a();

   void b();
}
