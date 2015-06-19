package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.ItemStack;
import net.minecraft.server.MapIcon;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutMap;
import net.minecraft.server.PersistentBase;
import net.minecraft.server.World;

public class WorldMap extends PersistentBase {
   public int centerX;
   public int centerZ;
   public byte map;
   public byte scale;
   public byte[] colors = new byte[16384];
   public List<WorldMap.WorldMapHumanTracker> g = Lists.newArrayList();
   private Map<EntityHuman, WorldMap.WorldMapHumanTracker> i = Maps.newHashMap();
   public Map<String, MapIcon> decorations = Maps.newLinkedHashMap();

   public WorldMap(String var1) {
      super(var1);
   }

   public void a(double var1, double var3, int var5) {
      int var6 = 128 * (1 << var5);
      int var7 = MathHelper.floor((var1 + 64.0D) / (double)var6);
      int var8 = MathHelper.floor((var3 + 64.0D) / (double)var6);
      this.centerX = var7 * var6 + var6 / 2 - 64;
      this.centerZ = var8 * var6 + var6 / 2 - 64;
   }

   public void a(NBTTagCompound var1) {
      this.map = var1.getByte("dimension");
      this.centerX = var1.getInt("xCenter");
      this.centerZ = var1.getInt("zCenter");
      this.scale = var1.getByte("scale");
      this.scale = (byte)MathHelper.clamp(this.scale, 0, 4);
      short var2 = var1.getShort("width");
      short var3 = var1.getShort("height");
      if(var2 == 128 && var3 == 128) {
         this.colors = var1.getByteArray("colors");
      } else {
         byte[] var4 = var1.getByteArray("colors");
         this.colors = new byte[16384];
         int var5 = (128 - var2) / 2;
         int var6 = (128 - var3) / 2;

         for(int var7 = 0; var7 < var3; ++var7) {
            int var8 = var7 + var6;
            if(var8 >= 0 || var8 < 128) {
               for(int var9 = 0; var9 < var2; ++var9) {
                  int var10 = var9 + var5;
                  if(var10 >= 0 || var10 < 128) {
                     this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                  }
               }
            }
         }
      }

   }

   public void b(NBTTagCompound var1) {
      var1.setByte("dimension", this.map);
      var1.setInt("xCenter", this.centerX);
      var1.setInt("zCenter", this.centerZ);
      var1.setByte("scale", this.scale);
      var1.setShort("width", (short)128);
      var1.setShort("height", (short)128);
      var1.setByteArray("colors", this.colors);
   }

   public void a(EntityHuman var1, ItemStack var2) {
      if(!this.i.containsKey(var1)) {
         WorldMap.WorldMapHumanTracker var3 = new WorldMap.WorldMapHumanTracker(var1);
         this.i.put(var1, var3);
         this.g.add(var3);
      }

      if(!var1.inventory.c(var2)) {
         this.decorations.remove(var1.getName());
      }

      for(int var6 = 0; var6 < this.g.size(); ++var6) {
         WorldMap.WorldMapHumanTracker var4 = (WorldMap.WorldMapHumanTracker)this.g.get(var6);
         if(var4.trackee.dead || !var4.trackee.inventory.c(var2) && !var2.y()) {
            this.i.remove(var4.trackee);
            this.g.remove(var4);
         } else if(!var2.y() && var4.trackee.dimension == this.map) {
            this.a(0, var4.trackee.world, var4.trackee.getName(), var4.trackee.locX, var4.trackee.locZ, (double)var4.trackee.yaw);
         }
      }

      if(var2.y()) {
         EntityItemFrame var7 = var2.z();
         BlockPosition var8 = var7.getBlockPosition();
         this.a(1, var1.world, "frame-" + var7.getId(), (double)var8.getX(), (double)var8.getZ(), (double)(var7.direction.b() * 90));
      }

      if(var2.hasTag() && var2.getTag().hasKeyOfType("Decorations", 9)) {
         NBTTagList var9 = var2.getTag().getList("Decorations", 10);

         for(int var10 = 0; var10 < var9.size(); ++var10) {
            NBTTagCompound var5 = var9.get(var10);
            if(!this.decorations.containsKey(var5.getString("id"))) {
               this.a(var5.getByte("type"), var1.world, var5.getString("id"), var5.getDouble("x"), var5.getDouble("z"), var5.getDouble("rot"));
            }
         }
      }

   }

   private void a(int var1, World var2, String var3, double var4, double var6, double var8) {
      int var10 = 1 << this.scale;
      float var11 = (float)(var4 - (double)this.centerX) / (float)var10;
      float var12 = (float)(var6 - (double)this.centerZ) / (float)var10;
      byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5D));
      byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5D));
      byte var16 = 63;
      byte var15;
      if(var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16) {
         var8 += var8 < 0.0D?-8.0D:8.0D;
         var15 = (byte)((int)(var8 * 16.0D / 360.0D));
         if(this.map < 0) {
            int var17 = (int)(var2.getWorldData().getDayTime() / 10L);
            var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
         }
      } else {
         if(Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F) {
            this.decorations.remove(var3);
            return;
         }

         var1 = 6;
         var15 = 0;
         if(var11 <= (float)(-var16)) {
            var13 = (byte)((int)((double)(var16 * 2) + 2.5D));
         }

         if(var12 <= (float)(-var16)) {
            var14 = (byte)((int)((double)(var16 * 2) + 2.5D));
         }

         if(var11 >= (float)var16) {
            var13 = (byte)(var16 * 2 + 1);
         }

         if(var12 >= (float)var16) {
            var14 = (byte)(var16 * 2 + 1);
         }
      }

      this.decorations.put(var3, new MapIcon((byte)var1, var13, var14, var15));
   }

   public Packet a(ItemStack var1, World var2, EntityHuman var3) {
      WorldMap.WorldMapHumanTracker var4 = (WorldMap.WorldMapHumanTracker)this.i.get(var3);
      return var4 == null?null:var4.a(var1);
   }

   public void flagDirty(int var1, int var2) {
      super.c();
      Iterator var3 = this.g.iterator();

      while(var3.hasNext()) {
         WorldMap.WorldMapHumanTracker var4 = (WorldMap.WorldMapHumanTracker)var3.next();
         var4.a(var1, var2);
      }

   }

   public WorldMap.WorldMapHumanTracker a(EntityHuman var1) {
      WorldMap.WorldMapHumanTracker var2 = (WorldMap.WorldMapHumanTracker)this.i.get(var1);
      if(var2 == null) {
         var2 = new WorldMap.WorldMapHumanTracker(var1);
         this.i.put(var1, var2);
         this.g.add(var2);
      }

      return var2;
   }

   public class WorldMapHumanTracker {
      public final EntityHuman trackee;
      private boolean d = true;
      private int e = 0;
      private int f = 0;
      private int g = 127;
      private int h = 127;
      private int i;
      public int b;

      public WorldMapHumanTracker(EntityHuman var2) {
         this.trackee = var2;
      }

      public Packet a(ItemStack var1) {
         if(this.d) {
            this.d = false;
            return new PacketPlayOutMap(var1.getData(), WorldMap.this.scale, WorldMap.this.decorations.values(), WorldMap.this.colors, this.e, this.f, this.g + 1 - this.e, this.h + 1 - this.f);
         } else {
            return this.i++ % 5 == 0?new PacketPlayOutMap(var1.getData(), WorldMap.this.scale, WorldMap.this.decorations.values(), WorldMap.this.colors, 0, 0, 0, 0):null;
         }
      }

      public void a(int var1, int var2) {
         if(this.d) {
            this.e = Math.min(this.e, var1);
            this.f = Math.min(this.f, var2);
            this.g = Math.max(this.g, var1);
            this.h = Math.max(this.h, var2);
         } else {
            this.d = true;
            this.e = var1;
            this.f = var2;
            this.g = var1;
            this.h = var2;
         }

      }
   }
}
