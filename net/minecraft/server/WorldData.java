package net.minecraft.server;

import java.util.concurrent.Callable;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.GameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public class WorldData {
   public static final EnumDifficulty a;
   private long b;
   private WorldType c;
   private String d;
   private int e;
   private int f;
   private int g;
   private long h;
   private long i;
   private long j;
   private long k;
   private NBTTagCompound l;
   private int m;
   private String n;
   private int o;
   private int p;
   private boolean q;
   private int r;
   private boolean s;
   private int t;
   private WorldSettings.EnumGamemode u;
   private boolean v;
   private boolean w;
   private boolean x;
   private boolean y;
   private EnumDifficulty z;
   private boolean A;
   private double B;
   private double C;
   private double D;
   private long E;
   private double F;
   private double G;
   private double H;
   private int I;
   private int J;
   private GameRules K;

   protected WorldData() {
      this.c = WorldType.NORMAL;
      this.d = "";
      this.B = 0.0D;
      this.C = 0.0D;
      this.D = 6.0E7D;
      this.E = 0L;
      this.F = 0.0D;
      this.G = 5.0D;
      this.H = 0.2D;
      this.I = 5;
      this.J = 15;
      this.K = new GameRules();
   }

   public WorldData(NBTTagCompound var1) {
      this.c = WorldType.NORMAL;
      this.d = "";
      this.B = 0.0D;
      this.C = 0.0D;
      this.D = 6.0E7D;
      this.E = 0L;
      this.F = 0.0D;
      this.G = 5.0D;
      this.H = 0.2D;
      this.I = 5;
      this.J = 15;
      this.K = new GameRules();
      this.b = var1.getLong("RandomSeed");
      if(var1.hasKeyOfType("generatorName", 8)) {
         String var2 = var1.getString("generatorName");
         this.c = WorldType.getType(var2);
         if(this.c == null) {
            this.c = WorldType.NORMAL;
         } else if(this.c.f()) {
            int var3 = 0;
            if(var1.hasKeyOfType("generatorVersion", 99)) {
               var3 = var1.getInt("generatorVersion");
            }

            this.c = this.c.a(var3);
         }

         if(var1.hasKeyOfType("generatorOptions", 8)) {
            this.d = var1.getString("generatorOptions");
         }
      }

      this.u = WorldSettings.EnumGamemode.getById(var1.getInt("GameType"));
      if(var1.hasKeyOfType("MapFeatures", 99)) {
         this.v = var1.getBoolean("MapFeatures");
      } else {
         this.v = true;
      }

      this.e = var1.getInt("SpawnX");
      this.f = var1.getInt("SpawnY");
      this.g = var1.getInt("SpawnZ");
      this.h = var1.getLong("Time");
      if(var1.hasKeyOfType("DayTime", 99)) {
         this.i = var1.getLong("DayTime");
      } else {
         this.i = this.h;
      }

      this.j = var1.getLong("LastPlayed");
      this.k = var1.getLong("SizeOnDisk");
      this.n = var1.getString("LevelName");
      this.o = var1.getInt("version");
      this.p = var1.getInt("clearWeatherTime");
      this.r = var1.getInt("rainTime");
      this.q = var1.getBoolean("raining");
      this.t = var1.getInt("thunderTime");
      this.s = var1.getBoolean("thundering");
      this.w = var1.getBoolean("hardcore");
      if(var1.hasKeyOfType("initialized", 99)) {
         this.y = var1.getBoolean("initialized");
      } else {
         this.y = true;
      }

      if(var1.hasKeyOfType("allowCommands", 99)) {
         this.x = var1.getBoolean("allowCommands");
      } else {
         this.x = this.u == WorldSettings.EnumGamemode.CREATIVE;
      }

      if(var1.hasKeyOfType("Player", 10)) {
         this.l = var1.getCompound("Player");
         this.m = this.l.getInt("Dimension");
      }

      if(var1.hasKeyOfType("GameRules", 10)) {
         this.K.a(var1.getCompound("GameRules"));
      }

      if(var1.hasKeyOfType("Difficulty", 99)) {
         this.z = EnumDifficulty.getById(var1.getByte("Difficulty"));
      }

      if(var1.hasKeyOfType("DifficultyLocked", 1)) {
         this.A = var1.getBoolean("DifficultyLocked");
      }

      if(var1.hasKeyOfType("BorderCenterX", 99)) {
         this.B = var1.getDouble("BorderCenterX");
      }

      if(var1.hasKeyOfType("BorderCenterZ", 99)) {
         this.C = var1.getDouble("BorderCenterZ");
      }

      if(var1.hasKeyOfType("BorderSize", 99)) {
         this.D = var1.getDouble("BorderSize");
      }

      if(var1.hasKeyOfType("BorderSizeLerpTime", 99)) {
         this.E = var1.getLong("BorderSizeLerpTime");
      }

      if(var1.hasKeyOfType("BorderSizeLerpTarget", 99)) {
         this.F = var1.getDouble("BorderSizeLerpTarget");
      }

      if(var1.hasKeyOfType("BorderSafeZone", 99)) {
         this.G = var1.getDouble("BorderSafeZone");
      }

      if(var1.hasKeyOfType("BorderDamagePerBlock", 99)) {
         this.H = var1.getDouble("BorderDamagePerBlock");
      }

      if(var1.hasKeyOfType("BorderWarningBlocks", 99)) {
         this.I = var1.getInt("BorderWarningBlocks");
      }

      if(var1.hasKeyOfType("BorderWarningTime", 99)) {
         this.J = var1.getInt("BorderWarningTime");
      }

   }

   public WorldData(WorldSettings var1, String var2) {
      this.c = WorldType.NORMAL;
      this.d = "";
      this.B = 0.0D;
      this.C = 0.0D;
      this.D = 6.0E7D;
      this.E = 0L;
      this.F = 0.0D;
      this.G = 5.0D;
      this.H = 0.2D;
      this.I = 5;
      this.J = 15;
      this.K = new GameRules();
      this.a(var1);
      this.n = var2;
      this.z = a;
      this.y = false;
   }

   public void a(WorldSettings var1) {
      this.b = var1.d();
      this.u = var1.e();
      this.v = var1.g();
      this.w = var1.f();
      this.c = var1.h();
      this.d = var1.j();
      this.x = var1.i();
   }

   public WorldData(WorldData var1) {
      this.c = WorldType.NORMAL;
      this.d = "";
      this.B = 0.0D;
      this.C = 0.0D;
      this.D = 6.0E7D;
      this.E = 0L;
      this.F = 0.0D;
      this.G = 5.0D;
      this.H = 0.2D;
      this.I = 5;
      this.J = 15;
      this.K = new GameRules();
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
      this.u = var1.u;
      this.v = var1.v;
      this.e = var1.e;
      this.f = var1.f;
      this.g = var1.g;
      this.h = var1.h;
      this.i = var1.i;
      this.j = var1.j;
      this.k = var1.k;
      this.l = var1.l;
      this.m = var1.m;
      this.n = var1.n;
      this.o = var1.o;
      this.r = var1.r;
      this.q = var1.q;
      this.t = var1.t;
      this.s = var1.s;
      this.w = var1.w;
      this.x = var1.x;
      this.y = var1.y;
      this.K = var1.K;
      this.z = var1.z;
      this.A = var1.A;
      this.B = var1.B;
      this.C = var1.C;
      this.D = var1.D;
      this.E = var1.E;
      this.F = var1.F;
      this.G = var1.G;
      this.H = var1.H;
      this.J = var1.J;
      this.I = var1.I;
   }

   public NBTTagCompound a() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.a(var1, this.l);
      return var1;
   }

   public NBTTagCompound a(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      this.a(var2, var1);
      return var2;
   }

   private void a(NBTTagCompound var1, NBTTagCompound var2) {
      var1.setLong("RandomSeed", this.b);
      var1.setString("generatorName", this.c.name());
      var1.setInt("generatorVersion", this.c.getVersion());
      var1.setString("generatorOptions", this.d);
      var1.setInt("GameType", this.u.getId());
      var1.setBoolean("MapFeatures", this.v);
      var1.setInt("SpawnX", this.e);
      var1.setInt("SpawnY", this.f);
      var1.setInt("SpawnZ", this.g);
      var1.setLong("Time", this.h);
      var1.setLong("DayTime", this.i);
      var1.setLong("SizeOnDisk", this.k);
      var1.setLong("LastPlayed", MinecraftServer.az());
      var1.setString("LevelName", this.n);
      var1.setInt("version", this.o);
      var1.setInt("clearWeatherTime", this.p);
      var1.setInt("rainTime", this.r);
      var1.setBoolean("raining", this.q);
      var1.setInt("thunderTime", this.t);
      var1.setBoolean("thundering", this.s);
      var1.setBoolean("hardcore", this.w);
      var1.setBoolean("allowCommands", this.x);
      var1.setBoolean("initialized", this.y);
      var1.setDouble("BorderCenterX", this.B);
      var1.setDouble("BorderCenterZ", this.C);
      var1.setDouble("BorderSize", this.D);
      var1.setLong("BorderSizeLerpTime", this.E);
      var1.setDouble("BorderSafeZone", this.G);
      var1.setDouble("BorderDamagePerBlock", this.H);
      var1.setDouble("BorderSizeLerpTarget", this.F);
      var1.setDouble("BorderWarningBlocks", (double)this.I);
      var1.setDouble("BorderWarningTime", (double)this.J);
      if(this.z != null) {
         var1.setByte("Difficulty", (byte)this.z.a());
      }

      var1.setBoolean("DifficultyLocked", this.A);
      var1.set("GameRules", this.K.a());
      if(var2 != null) {
         var1.set("Player", var2);
      }

   }

   public long getSeed() {
      return this.b;
   }

   public int c() {
      return this.e;
   }

   public int d() {
      return this.f;
   }

   public int e() {
      return this.g;
   }

   public long getTime() {
      return this.h;
   }

   public long getDayTime() {
      return this.i;
   }

   public NBTTagCompound i() {
      return this.l;
   }

   public void setTime(long var1) {
      this.h = var1;
   }

   public void setDayTime(long var1) {
      this.i = var1;
   }

   public void setSpawn(BlockPosition var1) {
      this.e = var1.getX();
      this.f = var1.getY();
      this.g = var1.getZ();
   }

   public String getName() {
      return this.n;
   }

   public void a(String var1) {
      this.n = var1;
   }

   public int l() {
      return this.o;
   }

   public void e(int var1) {
      this.o = var1;
   }

   public int A() {
      return this.p;
   }

   public void i(int var1) {
      this.p = var1;
   }

   public boolean isThundering() {
      return this.s;
   }

   public void setThundering(boolean var1) {
      this.s = var1;
   }

   public int getThunderDuration() {
      return this.t;
   }

   public void setThunderDuration(int var1) {
      this.t = var1;
   }

   public boolean hasStorm() {
      return this.q;
   }

   public void setStorm(boolean var1) {
      this.q = var1;
   }

   public int getWeatherDuration() {
      return this.r;
   }

   public void setWeatherDuration(int var1) {
      this.r = var1;
   }

   public WorldSettings.EnumGamemode getGameType() {
      return this.u;
   }

   public boolean shouldGenerateMapFeatures() {
      return this.v;
   }

   public void f(boolean var1) {
      this.v = var1;
   }

   public void setGameType(WorldSettings.EnumGamemode var1) {
      this.u = var1;
   }

   public boolean isHardcore() {
      return this.w;
   }

   public void g(boolean var1) {
      this.w = var1;
   }

   public WorldType getType() {
      return this.c;
   }

   public void a(WorldType var1) {
      this.c = var1;
   }

   public String getGeneratorOptions() {
      return this.d;
   }

   public boolean v() {
      return this.x;
   }

   public void c(boolean var1) {
      this.x = var1;
   }

   public boolean w() {
      return this.y;
   }

   public void d(boolean var1) {
      this.y = var1;
   }

   public GameRules x() {
      return this.K;
   }

   public double C() {
      return this.B;
   }

   public double D() {
      return this.C;
   }

   public double E() {
      return this.D;
   }

   public void a(double var1) {
      this.D = var1;
   }

   public long F() {
      return this.E;
   }

   public void e(long var1) {
      this.E = var1;
   }

   public double G() {
      return this.F;
   }

   public void b(double var1) {
      this.F = var1;
   }

   public void c(double var1) {
      this.C = var1;
   }

   public void d(double var1) {
      this.B = var1;
   }

   public double H() {
      return this.G;
   }

   public void e(double var1) {
      this.G = var1;
   }

   public double I() {
      return this.H;
   }

   public void f(double var1) {
      this.H = var1;
   }

   public int J() {
      return this.I;
   }

   public int K() {
      return this.J;
   }

   public void j(int var1) {
      this.I = var1;
   }

   public void k(int var1) {
      this.J = var1;
   }

   public EnumDifficulty getDifficulty() {
      return this.z;
   }

   public void setDifficulty(EnumDifficulty var1) {
      this.z = var1;
   }

   public boolean isDifficultyLocked() {
      return this.A;
   }

   public void e(boolean var1) {
      this.A = var1;
   }

   public void a(CrashReportSystemDetails var1) {
      var1.a("Level seed", new Callable() {
         public String a() throws Exception {
            return String.valueOf(WorldData.this.getSeed());
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level generator", new Callable() {
         public String a() throws Exception {
            return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[]{Integer.valueOf(WorldData.this.c.g()), WorldData.this.c.name(), Integer.valueOf(WorldData.this.c.getVersion()), Boolean.valueOf(WorldData.this.v)});
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level generator options", new Callable() {
         public String a() throws Exception {
            return WorldData.this.d;
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level spawn location", new Callable() {
         public String a() throws Exception {
            return CrashReportSystemDetails.a((double)WorldData.this.e, (double)WorldData.this.f, (double)WorldData.this.g);
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level time", new Callable() {
         public String a() throws Exception {
            return String.format("%d game time, %d day time", new Object[]{Long.valueOf(WorldData.this.h), Long.valueOf(WorldData.this.i)});
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level dimension", new Callable() {
         public String a() throws Exception {
            return String.valueOf(WorldData.this.m);
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level storage version", new Callable() {
         public String a() throws Exception {
            String var1 = "Unknown?";

            try {
               switch(WorldData.this.o) {
               case 19132:
                  var1 = "McRegion";
                  break;
               case 19133:
                  var1 = "Anvil";
               }
            } catch (Throwable var3) {
               ;
            }

            return String.format("0x%05X - %s", new Object[]{Integer.valueOf(WorldData.this.o), var1});
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level weather", new Callable() {
         public String a() throws Exception {
            return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[]{Integer.valueOf(WorldData.this.r), Boolean.valueOf(WorldData.this.q), Integer.valueOf(WorldData.this.t), Boolean.valueOf(WorldData.this.s)});
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.a("Level game mode", new Callable() {
         public String a() throws Exception {
            return String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[]{WorldData.this.u.b(), Integer.valueOf(WorldData.this.u.getId()), Boolean.valueOf(WorldData.this.w), Boolean.valueOf(WorldData.this.x)});
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
   }

   static {
      a = EnumDifficulty.NORMAL;
   }
}
