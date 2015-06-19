package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDoor;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityIronGolem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.UserCache;
import net.minecraft.server.Vec3D;
import net.minecraft.server.VillageDoor;
import net.minecraft.server.World;

public class Village {
   private World a;
   private final List<VillageDoor> b = Lists.newArrayList();
   private BlockPosition c;
   private BlockPosition d;
   private int e;
   private int f;
   private int g;
   private int h;
   private int i;
   private TreeMap<String, Integer> j;
   private List<Village.Aggressor> k;
   private int l;

   public Village() {
      this.c = BlockPosition.ZERO;
      this.d = BlockPosition.ZERO;
      this.j = new TreeMap();
      this.k = Lists.newArrayList();
   }

   public Village(World var1) {
      this.c = BlockPosition.ZERO;
      this.d = BlockPosition.ZERO;
      this.j = new TreeMap();
      this.k = Lists.newArrayList();
      this.a = var1;
   }

   public void a(World var1) {
      this.a = var1;
   }

   public void a(int var1) {
      this.g = var1;
      this.m();
      this.l();
      if(var1 % 20 == 0) {
         this.k();
      }

      if(var1 % 30 == 0) {
         this.j();
      }

      int var2 = this.h / 10;
      if(this.l < var2 && this.b.size() > 20 && this.a.random.nextInt(7000) == 0) {
         Vec3D var3 = this.a(this.d, 2, 4, 2);
         if(var3 != null) {
            EntityIronGolem var4 = new EntityIronGolem(this.a);
            var4.setPosition(var3.a, var3.b, var3.c);
            this.a.addEntity(var4);
            ++this.l;
         }
      }

   }

   private Vec3D a(BlockPosition var1, int var2, int var3, int var4) {
      for(int var5 = 0; var5 < 10; ++var5) {
         BlockPosition var6 = var1.a(this.a.random.nextInt(16) - 8, this.a.random.nextInt(6) - 3, this.a.random.nextInt(16) - 8);
         if(this.a(var6) && this.a(new BlockPosition(var2, var3, var4), var6)) {
            return new Vec3D((double)var6.getX(), (double)var6.getY(), (double)var6.getZ());
         }
      }

      return null;
   }

   private boolean a(BlockPosition var1, BlockPosition var2) {
      if(!World.a((IBlockAccess)this.a, (BlockPosition)var2.down())) {
         return false;
      } else {
         int var3 = var2.getX() - var1.getX() / 2;
         int var4 = var2.getZ() - var1.getZ() / 2;

         for(int var5 = var3; var5 < var3 + var1.getX(); ++var5) {
            for(int var6 = var2.getY(); var6 < var2.getY() + var1.getY(); ++var6) {
               for(int var7 = var4; var7 < var4 + var1.getZ(); ++var7) {
                  if(this.a.getType(new BlockPosition(var5, var6, var7)).getBlock().isOccluding()) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   private void j() {
      List var1 = this.a.a(EntityIronGolem.class, new AxisAlignedBB((double)(this.d.getX() - this.e), (double)(this.d.getY() - 4), (double)(this.d.getZ() - this.e), (double)(this.d.getX() + this.e), (double)(this.d.getY() + 4), (double)(this.d.getZ() + this.e)));
      this.l = var1.size();
   }

   private void k() {
      List var1 = this.a.a(EntityVillager.class, new AxisAlignedBB((double)(this.d.getX() - this.e), (double)(this.d.getY() - 4), (double)(this.d.getZ() - this.e), (double)(this.d.getX() + this.e), (double)(this.d.getY() + 4), (double)(this.d.getZ() + this.e)));
      this.h = var1.size();
      if(this.h == 0) {
         this.j.clear();
      }

   }

   public BlockPosition a() {
      return this.d;
   }

   public int b() {
      return this.e;
   }

   public int c() {
      return this.b.size();
   }

   public int d() {
      return this.g - this.f;
   }

   public int e() {
      return this.h;
   }

   public boolean a(BlockPosition var1) {
      return this.d.i(var1) < (double)(this.e * this.e);
   }

   public List<VillageDoor> f() {
      return this.b;
   }

   public VillageDoor b(BlockPosition var1) {
      VillageDoor var2 = null;
      int var3 = Integer.MAX_VALUE;
      Iterator var4 = this.b.iterator();

      while(var4.hasNext()) {
         VillageDoor var5 = (VillageDoor)var4.next();
         int var6 = var5.a(var1);
         if(var6 < var3) {
            var2 = var5;
            var3 = var6;
         }
      }

      return var2;
   }

   public VillageDoor c(BlockPosition var1) {
      VillageDoor var2 = null;
      int var3 = Integer.MAX_VALUE;
      Iterator var4 = this.b.iterator();

      while(var4.hasNext()) {
         VillageDoor var5 = (VillageDoor)var4.next();
         int var6 = var5.a(var1);
         if(var6 > 256) {
            var6 *= 1000;
         } else {
            var6 = var5.c();
         }

         if(var6 < var3) {
            var2 = var5;
            var3 = var6;
         }
      }

      return var2;
   }

   public VillageDoor e(BlockPosition var1) {
      if(this.d.i(var1) > (double)(this.e * this.e)) {
         return null;
      } else {
         Iterator var2 = this.b.iterator();

         VillageDoor var3;
         do {
            if(!var2.hasNext()) {
               return null;
            }

            var3 = (VillageDoor)var2.next();
         } while(var3.d().getX() != var1.getX() || var3.d().getZ() != var1.getZ() || Math.abs(var3.d().getY() - var1.getY()) > 1);

         return var3;
      }
   }

   public void a(VillageDoor var1) {
      this.b.add(var1);
      this.c = this.c.a(var1.d());
      this.n();
      this.f = var1.h();
   }

   public boolean g() {
      return this.b.isEmpty();
   }

   public void a(EntityLiving var1) {
      Iterator var2 = this.k.iterator();

      Village.Aggressor var3;
      do {
         if(!var2.hasNext()) {
            this.k.add(new Village.Aggressor(var1, this.g));
            return;
         }

         var3 = (Village.Aggressor)var2.next();
      } while(var3.a != var1);

      var3.b = this.g;
   }

   public EntityLiving b(EntityLiving var1) {
      double var2 = Double.MAX_VALUE;
      Village.Aggressor var4 = null;

      for(int var5 = 0; var5 < this.k.size(); ++var5) {
         Village.Aggressor var6 = (Village.Aggressor)this.k.get(var5);
         double var7 = var6.a.h(var1);
         if(var7 <= var2) {
            var4 = var6;
            var2 = var7;
         }
      }

      return var4 != null?var4.a:null;
   }

   public EntityHuman c(EntityLiving var1) {
      double var2 = Double.MAX_VALUE;
      EntityHuman var4 = null;
      Iterator var5 = this.j.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if(this.d(var6)) {
            EntityHuman var7 = this.a.a(var6);
            if(var7 != null) {
               double var8 = var7.h(var1);
               if(var8 <= var2) {
                  var4 = var7;
                  var2 = var8;
               }
            }
         }
      }

      return var4;
   }

   private void l() {
      Iterator var1 = this.k.iterator();

      while(true) {
         Village.Aggressor var2;
         do {
            if(!var1.hasNext()) {
               return;
            }

            var2 = (Village.Aggressor)var1.next();
         } while(var2.a.isAlive() && Math.abs(this.g - var2.b) <= 300);

         var1.remove();
      }
   }

   private void m() {
      boolean var1 = false;
      boolean var2 = this.a.random.nextInt(50) == 0;
      Iterator var3 = this.b.iterator();

      while(true) {
         VillageDoor var4;
         do {
            if(!var3.hasNext()) {
               if(var1) {
                  this.n();
               }

               return;
            }

            var4 = (VillageDoor)var3.next();
            if(var2) {
               var4.a();
            }
         } while(this.f(var4.d()) && Math.abs(this.g - var4.h()) <= 1200);

         this.c = this.c.b(var4.d());
         var1 = true;
         var4.a(true);
         var3.remove();
      }
   }

   private boolean f(BlockPosition var1) {
      Block var2 = this.a.getType(var1).getBlock();
      return var2 instanceof BlockDoor?var2.getMaterial() == Material.WOOD:false;
   }

   private void n() {
      int var1 = this.b.size();
      if(var1 == 0) {
         this.d = new BlockPosition(0, 0, 0);
         this.e = 0;
      } else {
         this.d = new BlockPosition(this.c.getX() / var1, this.c.getY() / var1, this.c.getZ() / var1);
         int var2 = 0;

         VillageDoor var4;
         for(Iterator var3 = this.b.iterator(); var3.hasNext(); var2 = Math.max(var4.a(this.d), var2)) {
            var4 = (VillageDoor)var3.next();
         }

         this.e = Math.max(32, (int)Math.sqrt((double)var2) + 1);
      }
   }

   public int a(String var1) {
      Integer var2 = (Integer)this.j.get(var1);
      return var2 != null?var2.intValue():0;
   }

   public int a(String var1, int var2) {
      int var3 = this.a(var1);
      int var4 = MathHelper.clamp(var3 + var2, -30, 10);
      this.j.put(var1, Integer.valueOf(var4));
      return var4;
   }

   public boolean d(String var1) {
      return this.a(var1) <= -15;
   }

   public void a(NBTTagCompound var1) {
      this.h = var1.getInt("PopSize");
      this.e = var1.getInt("Radius");
      this.l = var1.getInt("Golems");
      this.f = var1.getInt("Stable");
      this.g = var1.getInt("Tick");
      this.i = var1.getInt("MTick");
      this.d = new BlockPosition(var1.getInt("CX"), var1.getInt("CY"), var1.getInt("CZ"));
      this.c = new BlockPosition(var1.getInt("ACX"), var1.getInt("ACY"), var1.getInt("ACZ"));
      NBTTagList var2 = var1.getList("Doors", 10);

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         NBTTagCompound var4 = var2.get(var3);
         VillageDoor var5 = new VillageDoor(new BlockPosition(var4.getInt("X"), var4.getInt("Y"), var4.getInt("Z")), var4.getInt("IDX"), var4.getInt("IDZ"), var4.getInt("TS"));
         this.b.add(var5);
      }

      NBTTagList var8 = var1.getList("Players", 10);

      for(int var9 = 0; var9 < var8.size(); ++var9) {
         NBTTagCompound var10 = var8.get(var9);
         if(var10.hasKey("UUID")) {
            UserCache var6 = MinecraftServer.getServer().getUserCache();
            GameProfile var7 = var6.a(UUID.fromString(var10.getString("UUID")));
            if(var7 != null) {
               this.j.put(var7.getName(), Integer.valueOf(var10.getInt("S")));
            }
         } else {
            this.j.put(var10.getString("Name"), Integer.valueOf(var10.getInt("S")));
         }
      }

   }

   public void b(NBTTagCompound var1) {
      var1.setInt("PopSize", this.h);
      var1.setInt("Radius", this.e);
      var1.setInt("Golems", this.l);
      var1.setInt("Stable", this.f);
      var1.setInt("Tick", this.g);
      var1.setInt("MTick", this.i);
      var1.setInt("CX", this.d.getX());
      var1.setInt("CY", this.d.getY());
      var1.setInt("CZ", this.d.getZ());
      var1.setInt("ACX", this.c.getX());
      var1.setInt("ACY", this.c.getY());
      var1.setInt("ACZ", this.c.getZ());
      NBTTagList var2 = new NBTTagList();
      Iterator var3 = this.b.iterator();

      while(var3.hasNext()) {
         VillageDoor var4 = (VillageDoor)var3.next();
         NBTTagCompound var5 = new NBTTagCompound();
         var5.setInt("X", var4.d().getX());
         var5.setInt("Y", var4.d().getY());
         var5.setInt("Z", var4.d().getZ());
         var5.setInt("IDX", var4.f());
         var5.setInt("IDZ", var4.g());
         var5.setInt("TS", var4.h());
         var2.add(var5);
      }

      var1.set("Doors", var2);
      NBTTagList var9 = new NBTTagList();
      Iterator var10 = this.j.keySet().iterator();

      while(var10.hasNext()) {
         String var11 = (String)var10.next();
         NBTTagCompound var6 = new NBTTagCompound();
         UserCache var7 = MinecraftServer.getServer().getUserCache();
         GameProfile var8 = var7.getProfile(var11);
         if(var8 != null) {
            var6.setString("UUID", var8.getId().toString());
            var6.setInt("S", ((Integer)this.j.get(var11)).intValue());
            var9.add(var6);
         }
      }

      var1.set("Players", var9);
   }

   public void h() {
      this.i = this.g;
   }

   public boolean i() {
      return this.i == 0 || this.g - this.i >= 3600;
   }

   public void b(int var1) {
      Iterator var2 = this.j.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.a(var3, var1);
      }

   }

   class Aggressor {
      public EntityLiving a;
      public int b;

      Aggressor(EntityLiving var2, int var3) {
         this.a = var2;
         this.b = var3;
      }
   }
}
