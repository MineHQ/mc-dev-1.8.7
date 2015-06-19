package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.LongHashMap;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutBlockChange;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PacketPlayOutMultiBlockChange;
import net.minecraft.server.TileEntity;
import net.minecraft.server.WorldProvider;
import net.minecraft.server.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerChunkMap {
   private static final Logger a = LogManager.getLogger();
   private final WorldServer world;
   private final List<EntityPlayer> managedPlayers = Lists.newArrayList();
   private final LongHashMap<PlayerChunkMap.PlayerChunk> d = new LongHashMap();
   private final List<PlayerChunkMap.PlayerChunk> e = Lists.newArrayList();
   private final List<PlayerChunkMap.PlayerChunk> f = Lists.newArrayList();
   private int g;
   private long h;
   private final int[][] i = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

   public PlayerChunkMap(WorldServer var1) {
      this.world = var1;
      this.a(var1.getMinecraftServer().getPlayerList().s());
   }

   public WorldServer a() {
      return this.world;
   }

   public void flush() {
      long var1 = this.world.getTime();
      int var3;
      PlayerChunkMap.PlayerChunk var4;
      if(var1 - this.h > 8000L) {
         this.h = var1;

         for(var3 = 0; var3 < this.f.size(); ++var3) {
            var4 = (PlayerChunkMap.PlayerChunk)this.f.get(var3);
            var4.b();
            var4.a();
         }
      } else {
         for(var3 = 0; var3 < this.e.size(); ++var3) {
            var4 = (PlayerChunkMap.PlayerChunk)this.e.get(var3);
            var4.b();
         }
      }

      this.e.clear();
      if(this.managedPlayers.isEmpty()) {
         WorldProvider var5 = this.world.worldProvider;
         if(!var5.e()) {
            this.world.chunkProviderServer.b();
         }
      }

   }

   public boolean a(int var1, int var2) {
      long var3 = (long)var1 + 2147483647L | (long)var2 + 2147483647L << 32;
      return this.d.getEntry(var3) != null;
   }

   private PlayerChunkMap.PlayerChunk a(int var1, int var2, boolean var3) {
      long var4 = (long)var1 + 2147483647L | (long)var2 + 2147483647L << 32;
      PlayerChunkMap.PlayerChunk var6 = (PlayerChunkMap.PlayerChunk)this.d.getEntry(var4);
      if(var6 == null && var3) {
         var6 = new PlayerChunkMap.PlayerChunk(var1, var2);
         this.d.put(var4, var6);
         this.f.add(var6);
      }

      return var6;
   }

   public void flagDirty(BlockPosition var1) {
      int var2 = var1.getX() >> 4;
      int var3 = var1.getZ() >> 4;
      PlayerChunkMap.PlayerChunk var4 = this.a(var2, var3, false);
      if(var4 != null) {
         var4.a(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
      }

   }

   public void addPlayer(EntityPlayer var1) {
      int var2 = (int)var1.locX >> 4;
      int var3 = (int)var1.locZ >> 4;
      var1.d = var1.locX;
      var1.e = var1.locZ;

      for(int var4 = var2 - this.g; var4 <= var2 + this.g; ++var4) {
         for(int var5 = var3 - this.g; var5 <= var3 + this.g; ++var5) {
            this.a(var4, var5, true).a(var1);
         }
      }

      this.managedPlayers.add(var1);
      this.b(var1);
   }

   public void b(EntityPlayer var1) {
      ArrayList var2 = Lists.newArrayList((Iterable)var1.chunkCoordIntPairQueue);
      int var3 = 0;
      int var4 = this.g;
      int var5 = (int)var1.locX >> 4;
      int var6 = (int)var1.locZ >> 4;
      int var7 = 0;
      int var8 = 0;
      ChunkCoordIntPair var9 = this.a(var5, var6, true).location;
      var1.chunkCoordIntPairQueue.clear();
      if(var2.contains(var9)) {
         var1.chunkCoordIntPairQueue.add(var9);
      }

      int var10;
      for(var10 = 1; var10 <= var4 * 2; ++var10) {
         for(int var11 = 0; var11 < 2; ++var11) {
            int[] var12 = this.i[var3++ % 4];

            for(int var13 = 0; var13 < var10; ++var13) {
               var7 += var12[0];
               var8 += var12[1];
               var9 = this.a(var5 + var7, var6 + var8, true).location;
               if(var2.contains(var9)) {
                  var1.chunkCoordIntPairQueue.add(var9);
               }
            }
         }
      }

      var3 %= 4;

      for(var10 = 0; var10 < var4 * 2; ++var10) {
         var7 += this.i[var3][0];
         var8 += this.i[var3][1];
         var9 = this.a(var5 + var7, var6 + var8, true).location;
         if(var2.contains(var9)) {
            var1.chunkCoordIntPairQueue.add(var9);
         }
      }

   }

   public void removePlayer(EntityPlayer var1) {
      int var2 = (int)var1.d >> 4;
      int var3 = (int)var1.e >> 4;

      for(int var4 = var2 - this.g; var4 <= var2 + this.g; ++var4) {
         for(int var5 = var3 - this.g; var5 <= var3 + this.g; ++var5) {
            PlayerChunkMap.PlayerChunk var6 = this.a(var4, var5, false);
            if(var6 != null) {
               var6.b(var1);
            }
         }
      }

      this.managedPlayers.remove(var1);
   }

   private boolean a(int var1, int var2, int var3, int var4, int var5) {
      int var6 = var1 - var3;
      int var7 = var2 - var4;
      return var6 >= -var5 && var6 <= var5?var7 >= -var5 && var7 <= var5:false;
   }

   public void movePlayer(EntityPlayer var1) {
      int var2 = (int)var1.locX >> 4;
      int var3 = (int)var1.locZ >> 4;
      double var4 = var1.d - var1.locX;
      double var6 = var1.e - var1.locZ;
      double var8 = var4 * var4 + var6 * var6;
      if(var8 >= 64.0D) {
         int var10 = (int)var1.d >> 4;
         int var11 = (int)var1.e >> 4;
         int var12 = this.g;
         int var13 = var2 - var10;
         int var14 = var3 - var11;
         if(var13 != 0 || var14 != 0) {
            for(int var15 = var2 - var12; var15 <= var2 + var12; ++var15) {
               for(int var16 = var3 - var12; var16 <= var3 + var12; ++var16) {
                  if(!this.a(var15, var16, var10, var11, var12)) {
                     this.a(var15, var16, true).a(var1);
                  }

                  if(!this.a(var15 - var13, var16 - var14, var2, var3, var12)) {
                     PlayerChunkMap.PlayerChunk var17 = this.a(var15 - var13, var16 - var14, false);
                     if(var17 != null) {
                        var17.b(var1);
                     }
                  }
               }
            }

            this.b(var1);
            var1.d = var1.locX;
            var1.e = var1.locZ;
         }
      }
   }

   public boolean a(EntityPlayer var1, int var2, int var3) {
      PlayerChunkMap.PlayerChunk var4 = this.a(var2, var3, false);
      return var4 != null && var4.b.contains(var1) && !var1.chunkCoordIntPairQueue.contains(var4.location);
   }

   public void a(int var1) {
      var1 = MathHelper.clamp(var1, 3, 32);
      if(var1 != this.g) {
         int var2 = var1 - this.g;
         ArrayList var3 = Lists.newArrayList((Iterable)this.managedPlayers);
         Iterator var4 = var3.iterator();

         while(true) {
            while(var4.hasNext()) {
               EntityPlayer var5 = (EntityPlayer)var4.next();
               int var6 = (int)var5.locX >> 4;
               int var7 = (int)var5.locZ >> 4;
               int var8;
               int var9;
               if(var2 > 0) {
                  for(var8 = var6 - var1; var8 <= var6 + var1; ++var8) {
                     for(var9 = var7 - var1; var9 <= var7 + var1; ++var9) {
                        PlayerChunkMap.PlayerChunk var10 = this.a(var8, var9, true);
                        if(!var10.b.contains(var5)) {
                           var10.a(var5);
                        }
                     }
                  }
               } else {
                  for(var8 = var6 - this.g; var8 <= var6 + this.g; ++var8) {
                     for(var9 = var7 - this.g; var9 <= var7 + this.g; ++var9) {
                        if(!this.a(var8, var9, var6, var7, var1)) {
                           this.a(var8, var9, true).b(var5);
                        }
                     }
                  }
               }
            }

            this.g = var1;
            return;
         }
      }
   }

   public static int getFurthestViewableBlock(int var0) {
      return var0 * 16 - 16;
   }

   class PlayerChunk {
      private final List<EntityPlayer> b = Lists.newArrayList();
      private final ChunkCoordIntPair location;
      private short[] dirtyBlocks = new short[64];
      private int dirtyCount;
      private int f;
      private long g;

      public PlayerChunk(int var2, int var3) {
         this.location = new ChunkCoordIntPair(var2, var3);
         PlayerChunkMap.this.a().chunkProviderServer.getChunkAt(var2, var3);
      }

      public void a(EntityPlayer var1) {
         if(this.b.contains(var1)) {
            PlayerChunkMap.a.debug("Failed to add player. {} already is in chunk {}, {}", new Object[]{var1, Integer.valueOf(this.location.x), Integer.valueOf(this.location.z)});
         } else {
            if(this.b.isEmpty()) {
               this.g = PlayerChunkMap.this.world.getTime();
            }

            this.b.add(var1);
            var1.chunkCoordIntPairQueue.add(this.location);
         }
      }

      public void b(EntityPlayer var1) {
         if(this.b.contains(var1)) {
            Chunk var2 = PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z);
            if(var2.isReady()) {
               var1.playerConnection.sendPacket(new PacketPlayOutMapChunk(var2, true, 0));
            }

            this.b.remove(var1);
            var1.chunkCoordIntPairQueue.remove(this.location);
            if(this.b.isEmpty()) {
               long var3 = (long)this.location.x + 2147483647L | (long)this.location.z + 2147483647L << 32;
               this.a(var2);
               PlayerChunkMap.this.d.remove(var3);
               PlayerChunkMap.this.f.remove(this);
               if(this.dirtyCount > 0) {
                  PlayerChunkMap.this.e.remove(this);
               }

               PlayerChunkMap.this.a().chunkProviderServer.queueUnload(this.location.x, this.location.z);
            }

         }
      }

      public void a() {
         this.a(PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z));
      }

      private void a(Chunk var1) {
         var1.c(var1.w() + PlayerChunkMap.this.world.getTime() - this.g);
         this.g = PlayerChunkMap.this.world.getTime();
      }

      public void a(int var1, int var2, int var3) {
         if(this.dirtyCount == 0) {
            PlayerChunkMap.this.e.add(this);
         }

         this.f |= 1 << (var2 >> 4);
         if(this.dirtyCount < 64) {
            short var4 = (short)(var1 << 12 | var3 << 8 | var2);

            for(int var5 = 0; var5 < this.dirtyCount; ++var5) {
               if(this.dirtyBlocks[var5] == var4) {
                  return;
               }
            }

            this.dirtyBlocks[this.dirtyCount++] = var4;
         }

      }

      public void a(Packet var1) {
         for(int var2 = 0; var2 < this.b.size(); ++var2) {
            EntityPlayer var3 = (EntityPlayer)this.b.get(var2);
            if(!var3.chunkCoordIntPairQueue.contains(this.location)) {
               var3.playerConnection.sendPacket(var1);
            }
         }

      }

      public void b() {
         if(this.dirtyCount != 0) {
            int var1;
            int var2;
            int var3;
            if(this.dirtyCount == 1) {
               var1 = (this.dirtyBlocks[0] >> 12 & 15) + this.location.x * 16;
               var2 = this.dirtyBlocks[0] & 255;
               var3 = (this.dirtyBlocks[0] >> 8 & 15) + this.location.z * 16;
               BlockPosition var4 = new BlockPosition(var1, var2, var3);
               this.a((Packet)(new PacketPlayOutBlockChange(PlayerChunkMap.this.world, var4)));
               if(PlayerChunkMap.this.world.getType(var4).getBlock().isTileEntity()) {
                  this.a(PlayerChunkMap.this.world.getTileEntity(var4));
               }
            } else {
               int var7;
               if(this.dirtyCount == 64) {
                  var1 = this.location.x * 16;
                  var2 = this.location.z * 16;
                  this.a((Packet)(new PacketPlayOutMapChunk(PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z), false, this.f)));

                  for(var3 = 0; var3 < 16; ++var3) {
                     if((this.f & 1 << var3) != 0) {
                        var7 = var3 << 4;
                        List var5 = PlayerChunkMap.this.world.getTileEntities(var1, var7, var2, var1 + 16, var7 + 16, var2 + 16);

                        for(int var6 = 0; var6 < var5.size(); ++var6) {
                           this.a((TileEntity)var5.get(var6));
                        }
                     }
                  }
               } else {
                  this.a((Packet)(new PacketPlayOutMultiBlockChange(this.dirtyCount, this.dirtyBlocks, PlayerChunkMap.this.world.getChunkAt(this.location.x, this.location.z))));

                  for(var1 = 0; var1 < this.dirtyCount; ++var1) {
                     var2 = (this.dirtyBlocks[var1] >> 12 & 15) + this.location.x * 16;
                     var3 = this.dirtyBlocks[var1] & 255;
                     var7 = (this.dirtyBlocks[var1] >> 8 & 15) + this.location.z * 16;
                     BlockPosition var8 = new BlockPosition(var2, var3, var7);
                     if(PlayerChunkMap.this.world.getType(var8).getBlock().isTileEntity()) {
                        this.a(PlayerChunkMap.this.world.getTileEntity(var8));
                     }
                  }
               }
            }

            this.dirtyCount = 0;
            this.f = 0;
         }
      }

      private void a(TileEntity var1) {
         if(var1 != null) {
            Packet var2 = var1.getUpdatePacket();
            if(var2 != null) {
               this.a(var2);
            }
         }

      }
   }
}
