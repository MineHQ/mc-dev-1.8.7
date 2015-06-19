package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandDispatcher;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.Convertable;
import net.minecraft.server.CrashReport;
import net.minecraft.server.DedicatedServer;
import net.minecraft.server.DemoWorldServer;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.IAsyncTaskHandler;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandHandler;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IDataManager;
import net.minecraft.server.IMojangStatistics;
import net.minecraft.server.IProgressUpdate;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MethodProfiler;
import net.minecraft.server.MojangStatisticsGenerator;
import net.minecraft.server.PacketPlayOutUpdateTime;
import net.minecraft.server.PlayerList;
import net.minecraft.server.ReportedException;
import net.minecraft.server.SecondaryWorldServer;
import net.minecraft.server.ServerConnection;
import net.minecraft.server.ServerPing;
import net.minecraft.server.SystemUtils;
import net.minecraft.server.UserCache;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldLoaderServer;
import net.minecraft.server.WorldManager;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer implements Runnable, ICommandListener, IAsyncTaskHandler, IMojangStatistics {
   public static final Logger LOGGER = LogManager.getLogger();
   public static final File a = new File("usercache.json");
   private static MinecraftServer l;
   public Convertable convertable;
   private final MojangStatisticsGenerator n = new MojangStatisticsGenerator("server", this, az());
   public File universe;
   private final List<IUpdatePlayerListBox> p = Lists.newArrayList();
   protected final ICommandHandler b;
   public final MethodProfiler methodProfiler = new MethodProfiler();
   private final ServerConnection q;
   private final ServerPing r = new ServerPing();
   private final Random s = new Random();
   private String serverIp;
   private int u = -1;
   public WorldServer[] worldServer;
   private PlayerList v;
   private boolean isRunning = true;
   private boolean isStopped;
   private int ticks;
   protected final Proxy e;
   public String f;
   public int g;
   private boolean onlineMode;
   private boolean spawnAnimals;
   private boolean spawnNPCs;
   private boolean pvpMode;
   private boolean allowFlight;
   private String motd;
   private int F;
   private int G = 0;
   public final long[] h = new long[100];
   public long[][] i;
   private KeyPair H;
   private String I;
   private String J;
   private boolean demoMode;
   private boolean M;
   private boolean N;
   private String O = "";
   private String P = "";
   private boolean Q;
   private long R;
   private String S;
   private boolean T;
   private boolean U;
   private final YggdrasilAuthenticationService V;
   private final MinecraftSessionService W;
   private long X = 0L;
   private final GameProfileRepository Y;
   private final UserCache Z;
   protected final Queue<FutureTask<?>> j = Queues.newArrayDeque();
   private Thread serverThread;
   private long ab = az();

   public MinecraftServer(File var1, Proxy var2, File var3) {
      this.e = var2;
      l = this;
      this.universe = var1;
      this.q = new ServerConnection(this);
      this.Z = new UserCache(this, var3);
      this.b = this.h();
      this.convertable = new WorldLoaderServer(var1);
      this.V = new YggdrasilAuthenticationService(var2, UUID.randomUUID().toString());
      this.W = this.V.createMinecraftSessionService();
      this.Y = this.V.createProfileRepository();
   }

   protected CommandDispatcher h() {
      return new CommandDispatcher();
   }

   protected abstract boolean init() throws IOException;

   protected void a(String var1) {
      if(this.getConvertable().isConvertable(var1)) {
         LOGGER.info("Converting map!");
         this.b("menu.convertingLevel");
         this.getConvertable().convert(var1, new IProgressUpdate() {
            private long b = System.currentTimeMillis();

            public void a(String var1) {
            }

            public void a(int var1) {
               if(System.currentTimeMillis() - this.b >= 1000L) {
                  this.b = System.currentTimeMillis();
                  MinecraftServer.LOGGER.info("Converting... " + var1 + "%");
               }

            }

            public void c(String var1) {
            }
         });
      }

   }

   protected synchronized void b(String var1) {
      this.S = var1;
   }

   protected void a(String var1, String var2, long var3, WorldType var5, String var6) {
      this.a(var1);
      this.b("menu.loadingLevel");
      this.worldServer = new WorldServer[3];
      this.i = new long[this.worldServer.length][100];
      IDataManager var7 = this.convertable.a(var1, true);
      this.a(this.U(), var7);
      WorldData var9 = var7.getWorldData();
      WorldSettings var8;
      if(var9 == null) {
         if(this.X()) {
            var8 = DemoWorldServer.a;
         } else {
            var8 = new WorldSettings(var3, this.getGamemode(), this.getGenerateStructures(), this.isHardcore(), var5);
            var8.setGeneratorSettings(var6);
            if(this.M) {
               var8.a();
            }
         }

         var9 = new WorldData(var8, var2);
      } else {
         var9.a(var2);
         var8 = new WorldSettings(var9);
      }

      for(int var10 = 0; var10 < this.worldServer.length; ++var10) {
         byte var11 = 0;
         if(var10 == 1) {
            var11 = -1;
         }

         if(var10 == 2) {
            var11 = 1;
         }

         if(var10 == 0) {
            if(this.X()) {
               this.worldServer[var10] = (WorldServer)(new DemoWorldServer(this, var7, var9, var11, this.methodProfiler)).b();
            } else {
               this.worldServer[var10] = (WorldServer)(new WorldServer(this, var7, var9, var11, this.methodProfiler)).b();
            }

            this.worldServer[var10].a(var8);
         } else {
            this.worldServer[var10] = (WorldServer)(new SecondaryWorldServer(this, var7, var11, this.worldServer[0], this.methodProfiler)).b();
         }

         this.worldServer[var10].addIWorldAccess(new WorldManager(this, this.worldServer[var10]));
         if(!this.T()) {
            this.worldServer[var10].getWorldData().setGameType(this.getGamemode());
         }
      }

      this.v.setPlayerFileData(this.worldServer);
      this.a(this.getDifficulty());
      this.k();
   }

   protected void k() {
      boolean var1 = true;
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = true;
      int var5 = 0;
      this.b("menu.generatingTerrain");
      byte var6 = 0;
      LOGGER.info("Preparing start region for level " + var6);
      WorldServer var7 = this.worldServer[var6];
      BlockPosition var8 = var7.getSpawn();
      long var9 = az();

      for(int var11 = -192; var11 <= 192 && this.isRunning(); var11 += 16) {
         for(int var12 = -192; var12 <= 192 && this.isRunning(); var12 += 16) {
            long var13 = az();
            if(var13 - var9 > 1000L) {
               this.a_("Preparing spawn area", var5 * 100 / 625);
               var9 = var13;
            }

            ++var5;
            var7.chunkProviderServer.getChunkAt(var8.getX() + var11 >> 4, var8.getZ() + var12 >> 4);
         }
      }

      this.s();
   }

   protected void a(String var1, IDataManager var2) {
      File var3 = new File(var2.getDirectory(), "resources.zip");
      if(var3.isFile()) {
         this.setResourcePack("level://" + var1 + "/" + var3.getName(), "");
      }

   }

   public abstract boolean getGenerateStructures();

   public abstract WorldSettings.EnumGamemode getGamemode();

   public abstract EnumDifficulty getDifficulty();

   public abstract boolean isHardcore();

   public abstract int p();

   public abstract boolean q();

   public abstract boolean r();

   protected void a_(String var1, int var2) {
      this.f = var1;
      this.g = var2;
      LOGGER.info(var1 + ": " + var2 + "%");
   }

   protected void s() {
      this.f = null;
      this.g = 0;
   }

   protected void saveChunks(boolean var1) {
      if(!this.N) {
         WorldServer[] var2 = this.worldServer;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldServer var5 = var2[var4];
            if(var5 != null) {
               if(!var1) {
                  LOGGER.info("Saving chunks for level \'" + var5.getWorldData().getName() + "\'/" + var5.worldProvider.getName());
               }

               try {
                  var5.save(true, (IProgressUpdate)null);
               } catch (ExceptionWorldConflict var7) {
                  LOGGER.warn(var7.getMessage());
               }
            }
         }

      }
   }

   protected void stop() {
      if(!this.N) {
         LOGGER.info("Stopping server");
         if(this.aq() != null) {
            this.aq().b();
         }

         if(this.v != null) {
            LOGGER.info("Saving players");
            this.v.savePlayers();
            this.v.u();
         }

         if(this.worldServer != null) {
            LOGGER.info("Saving worlds");
            this.saveChunks(false);

            for(int var1 = 0; var1 < this.worldServer.length; ++var1) {
               WorldServer var2 = this.worldServer[var1];
               var2.saveLevel();
            }
         }

         if(this.n.d()) {
            this.n.e();
         }

      }
   }

   public String getServerIp() {
      return this.serverIp;
   }

   public void c(String var1) {
      this.serverIp = var1;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public void safeShutdown() {
      this.isRunning = false;
   }

   public void run() {
      try {
         if(this.init()) {
            this.ab = az();
            long var1 = 0L;
            this.r.setMOTD(new ChatComponentText(this.motd));
            this.r.setServerInfo(new ServerPing.ServerData("1.8.7", 47));
            this.a(this.r);

            while(this.isRunning) {
               long var48 = az();
               long var5 = var48 - this.ab;
               if(var5 > 2000L && this.ab - this.R >= 15000L) {
                  LOGGER.warn("Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{Long.valueOf(var5), Long.valueOf(var5 / 50L)});
                  var5 = 2000L;
                  this.R = this.ab;
               }

               if(var5 < 0L) {
                  LOGGER.warn("Time ran backwards! Did the system time change?");
                  var5 = 0L;
               }

               var1 += var5;
               this.ab = var48;
               if(this.worldServer[0].everyoneDeeplySleeping()) {
                  this.A();
                  var1 = 0L;
               } else {
                  while(var1 > 50L) {
                     var1 -= 50L;
                     this.A();
                  }
               }

               Thread.sleep(Math.max(1L, 50L - var1));
               this.Q = true;
            }
         } else {
            this.a((CrashReport)null);
         }
      } catch (Throwable var46) {
         LOGGER.error("Encountered an unexpected exception", var46);
         CrashReport var2 = null;
         if(var46 instanceof ReportedException) {
            var2 = this.b(((ReportedException)var46).a());
         } else {
            var2 = this.b(new CrashReport("Exception in server tick loop", var46));
         }

         File var3 = new File(new File(this.y(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");
         if(var2.a(var3)) {
            LOGGER.error("This crash report has been saved to: " + var3.getAbsolutePath());
         } else {
            LOGGER.error("We were unable to save this crash report to disk.");
         }

         this.a(var2);
      } finally {
         try {
            this.isStopped = true;
            this.stop();
         } catch (Throwable var44) {
            LOGGER.error("Exception stopping the server", var44);
         } finally {
            this.z();
         }

      }

   }

   private void a(ServerPing var1) {
      File var2 = this.d("server-icon.png");
      if(var2.isFile()) {
         ByteBuf var3 = Unpooled.buffer();

         try {
            BufferedImage var4 = ImageIO.read(var2);
            Validate.validState(var4.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
            Validate.validState(var4.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
            ImageIO.write(var4, "PNG", new ByteBufOutputStream(var3));
            ByteBuf var5 = Base64.encode(var3);
            var1.setFavicon("data:image/png;base64," + var5.toString(Charsets.UTF_8));
         } catch (Exception var9) {
            LOGGER.error((String)"Couldn\'t load server icon", (Throwable)var9);
         } finally {
            var3.release();
         }
      }

   }

   public File y() {
      return new File(".");
   }

   protected void a(CrashReport var1) {
   }

   protected void z() {
   }

   protected void A() {
      long var1 = System.nanoTime();
      ++this.ticks;
      if(this.T) {
         this.T = false;
         this.methodProfiler.a = true;
         this.methodProfiler.a();
      }

      this.methodProfiler.a("root");
      this.B();
      if(var1 - this.X >= 5000000000L) {
         this.X = var1;
         this.r.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.J(), this.I()));
         GameProfile[] var3 = new GameProfile[Math.min(this.I(), 12)];
         int var4 = MathHelper.nextInt(this.s, 0, this.I() - var3.length);

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var3[var5] = ((EntityPlayer)this.v.v().get(var4 + var5)).getProfile();
         }

         Collections.shuffle(Arrays.asList(var3));
         this.r.b().a(var3);
      }

      if(this.ticks % 900 == 0) {
         this.methodProfiler.a("save");
         this.v.savePlayers();
         this.saveChunks(true);
         this.methodProfiler.b();
      }

      this.methodProfiler.a("tallying");
      this.h[this.ticks % 100] = System.nanoTime() - var1;
      this.methodProfiler.b();
      this.methodProfiler.a("snooper");
      if(!this.n.d() && this.ticks > 100) {
         this.n.a();
      }

      if(this.ticks % 6000 == 0) {
         this.n.b();
      }

      this.methodProfiler.b();
      this.methodProfiler.b();
   }

   public void B() {
      this.methodProfiler.a("jobs");
      Queue var1 = this.j;
      synchronized(this.j) {
         while(!this.j.isEmpty()) {
            SystemUtils.a((FutureTask)this.j.poll(), LOGGER);
         }
      }

      this.methodProfiler.c("levels");

      int var10;
      for(var10 = 0; var10 < this.worldServer.length; ++var10) {
         long var2 = System.nanoTime();
         if(var10 == 0 || this.getAllowNether()) {
            WorldServer var4 = this.worldServer[var10];
            this.methodProfiler.a(var4.getWorldData().getName());
            if(this.ticks % 20 == 0) {
               this.methodProfiler.a("timeSync");
               this.v.a(new PacketPlayOutUpdateTime(var4.getTime(), var4.getDayTime(), var4.getGameRules().getBoolean("doDaylightCycle")), var4.worldProvider.getDimension());
               this.methodProfiler.b();
            }

            this.methodProfiler.a("tick");

            CrashReport var6;
            try {
               var4.doTick();
            } catch (Throwable var8) {
               var6 = CrashReport.a(var8, "Exception ticking world");
               var4.a((CrashReport)var6);
               throw new ReportedException(var6);
            }

            try {
               var4.tickEntities();
            } catch (Throwable var7) {
               var6 = CrashReport.a(var7, "Exception ticking world entities");
               var4.a((CrashReport)var6);
               throw new ReportedException(var6);
            }

            this.methodProfiler.b();
            this.methodProfiler.a("tracker");
            var4.getTracker().updatePlayers();
            this.methodProfiler.b();
            this.methodProfiler.b();
         }

         this.i[var10][this.ticks % 100] = System.nanoTime() - var2;
      }

      this.methodProfiler.c("connection");
      this.aq().c();
      this.methodProfiler.c("players");
      this.v.tick();
      this.methodProfiler.c("tickables");

      for(var10 = 0; var10 < this.p.size(); ++var10) {
         ((IUpdatePlayerListBox)this.p.get(var10)).c();
      }

      this.methodProfiler.b();
   }

   public boolean getAllowNether() {
      return true;
   }

   public void a(IUpdatePlayerListBox var1) {
      this.p.add(var1);
   }

   public static void main(String[] var0) {
      DispenserRegistry.c();

      try {
         boolean var1 = true;
         String var2 = null;
         String var3 = ".";
         String var4 = null;
         boolean var5 = false;
         boolean var6 = false;
         int var7 = -1;

         for(int var8 = 0; var8 < var0.length; ++var8) {
            String var9 = var0[var8];
            String var10 = var8 == var0.length - 1?null:var0[var8 + 1];
            boolean var11 = false;
            if(!var9.equals("nogui") && !var9.equals("--nogui")) {
               if(var9.equals("--port") && var10 != null) {
                  var11 = true;

                  try {
                     var7 = Integer.parseInt(var10);
                  } catch (NumberFormatException var13) {
                     ;
                  }
               } else if(var9.equals("--singleplayer") && var10 != null) {
                  var11 = true;
                  var2 = var10;
               } else if(var9.equals("--universe") && var10 != null) {
                  var11 = true;
                  var3 = var10;
               } else if(var9.equals("--world") && var10 != null) {
                  var11 = true;
                  var4 = var10;
               } else if(var9.equals("--demo")) {
                  var5 = true;
               } else if(var9.equals("--bonusChest")) {
                  var6 = true;
               }
            } else {
               var1 = false;
            }

            if(var11) {
               ++var8;
            }
         }

         final DedicatedServer var15 = new DedicatedServer(new File(var3));
         if(var2 != null) {
            var15.i(var2);
         }

         if(var4 != null) {
            var15.setWorld(var4);
         }

         if(var7 >= 0) {
            var15.setPort(var7);
         }

         if(var5) {
            var15.b(true);
         }

         if(var6) {
            var15.c(true);
         }

         if(var1 && !GraphicsEnvironment.isHeadless()) {
            var15.aQ();
         }

         var15.D();
         Runtime.getRuntime().addShutdownHook(new Thread("Server Shutdown Thread") {
            public void run() {
               var15.stop();
            }
         });
      } catch (Exception var14) {
         LOGGER.fatal((String)"Failed to start the minecraft server", (Throwable)var14);
      }

   }

   public void D() {
      this.serverThread = new Thread(this, "Server thread");
      this.serverThread.start();
   }

   public File d(String var1) {
      return new File(this.y(), var1);
   }

   public void info(String var1) {
      LOGGER.info(var1);
   }

   public void warning(String var1) {
      LOGGER.warn(var1);
   }

   public WorldServer getWorldServer(int var1) {
      return var1 == -1?this.worldServer[1]:(var1 == 1?this.worldServer[2]:this.worldServer[0]);
   }

   public String E() {
      return this.serverIp;
   }

   public int F() {
      return this.u;
   }

   public String G() {
      return this.motd;
   }

   public String getVersion() {
      return "1.8.7";
   }

   public int I() {
      return this.v.getPlayerCount();
   }

   public int J() {
      return this.v.getMaxPlayers();
   }

   public String[] getPlayers() {
      return this.v.f();
   }

   public GameProfile[] L() {
      return this.v.g();
   }

   public boolean isDebugging() {
      return false;
   }

   public void g(String var1) {
      LOGGER.error(var1);
   }

   public void h(String var1) {
      if(this.isDebugging()) {
         LOGGER.info(var1);
      }

   }

   public String getServerModName() {
      return "vanilla";
   }

   public CrashReport b(CrashReport var1) {
      var1.g().a("Profiler Position", new Callable() {
         public String a() throws Exception {
            return MinecraftServer.this.methodProfiler.a?MinecraftServer.this.methodProfiler.c():"N/A (disabled)";
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      if(this.v != null) {
         var1.g().a("Player Count", new Callable() {
            public String a() {
               return MinecraftServer.this.v.getPlayerCount() + " / " + MinecraftServer.this.v.getMaxPlayers() + "; " + MinecraftServer.this.v.v();
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
      }

      return var1;
   }

   public List<String> tabCompleteCommand(ICommandListener var1, String var2, BlockPosition var3) {
      ArrayList var4 = Lists.newArrayList();
      if(var2.startsWith("/")) {
         var2 = var2.substring(1);
         boolean var11 = !var2.contains(" ");
         List var12 = this.b.a(var1, var2, var3);
         if(var12 != null) {
            Iterator var13 = var12.iterator();

            while(var13.hasNext()) {
               String var14 = (String)var13.next();
               if(var11) {
                  var4.add("/" + var14);
               } else {
                  var4.add(var14);
               }
            }
         }

         return var4;
      } else {
         String[] var5 = var2.split(" ", -1);
         String var6 = var5[var5.length - 1];
         String[] var7 = this.v.f();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            if(CommandAbstract.a(var6, var10)) {
               var4.add(var10);
            }
         }

         return var4;
      }
   }

   public static MinecraftServer getServer() {
      return l;
   }

   public boolean O() {
      return this.universe != null;
   }

   public String getName() {
      return "Server";
   }

   public void sendMessage(IChatBaseComponent var1) {
      LOGGER.info(var1.c());
   }

   public boolean a(int var1, String var2) {
      return true;
   }

   public ICommandHandler getCommandHandler() {
      return this.b;
   }

   public KeyPair Q() {
      return this.H;
   }

   public int R() {
      return this.u;
   }

   public void setPort(int var1) {
      this.u = var1;
   }

   public String S() {
      return this.I;
   }

   public void i(String var1) {
      this.I = var1;
   }

   public boolean T() {
      return this.I != null;
   }

   public String U() {
      return this.J;
   }

   public void setWorld(String var1) {
      this.J = var1;
   }

   public void a(KeyPair var1) {
      this.H = var1;
   }

   public void a(EnumDifficulty var1) {
      for(int var2 = 0; var2 < this.worldServer.length; ++var2) {
         WorldServer var3 = this.worldServer[var2];
         if(var3 != null) {
            if(var3.getWorldData().isHardcore()) {
               var3.getWorldData().setDifficulty(EnumDifficulty.HARD);
               var3.setSpawnFlags(true, true);
            } else if(this.T()) {
               var3.getWorldData().setDifficulty(var1);
               var3.setSpawnFlags(var3.getDifficulty() != EnumDifficulty.PEACEFUL, true);
            } else {
               var3.getWorldData().setDifficulty(var1);
               var3.setSpawnFlags(this.getSpawnMonsters(), this.spawnAnimals);
            }
         }
      }

   }

   protected boolean getSpawnMonsters() {
      return true;
   }

   public boolean X() {
      return this.demoMode;
   }

   public void b(boolean var1) {
      this.demoMode = var1;
   }

   public void c(boolean var1) {
      this.M = var1;
   }

   public Convertable getConvertable() {
      return this.convertable;
   }

   public void aa() {
      this.N = true;
      this.getConvertable().d();

      for(int var1 = 0; var1 < this.worldServer.length; ++var1) {
         WorldServer var2 = this.worldServer[var1];
         if(var2 != null) {
            var2.saveLevel();
         }
      }

      this.getConvertable().e(this.worldServer[0].getDataManager().g());
      this.safeShutdown();
   }

   public String getResourcePack() {
      return this.O;
   }

   public String getResourcePackHash() {
      return this.P;
   }

   public void setResourcePack(String var1, String var2) {
      this.O = var1;
      this.P = var2;
   }

   public void a(MojangStatisticsGenerator var1) {
      var1.a("whitelist_enabled", Boolean.valueOf(false));
      var1.a("whitelist_count", Integer.valueOf(0));
      if(this.v != null) {
         var1.a("players_current", Integer.valueOf(this.I()));
         var1.a("players_max", Integer.valueOf(this.J()));
         var1.a("players_seen", Integer.valueOf(this.v.getSeenPlayers().length));
      }

      var1.a("uses_auth", Boolean.valueOf(this.onlineMode));
      var1.a("gui_state", this.as()?"enabled":"disabled");
      var1.a("run_time", Long.valueOf((az() - var1.g()) / 60L * 1000L));
      var1.a("avg_tick_ms", Integer.valueOf((int)(MathHelper.a(this.h) * 1.0E-6D)));
      int var2 = 0;
      if(this.worldServer != null) {
         for(int var3 = 0; var3 < this.worldServer.length; ++var3) {
            if(this.worldServer[var3] != null) {
               WorldServer var4 = this.worldServer[var3];
               WorldData var5 = var4.getWorldData();
               var1.a("world[" + var2 + "][dimension]", Integer.valueOf(var4.worldProvider.getDimension()));
               var1.a("world[" + var2 + "][mode]", var5.getGameType());
               var1.a("world[" + var2 + "][difficulty]", var4.getDifficulty());
               var1.a("world[" + var2 + "][hardcore]", Boolean.valueOf(var5.isHardcore()));
               var1.a("world[" + var2 + "][generator_name]", var5.getType().name());
               var1.a("world[" + var2 + "][generator_version]", Integer.valueOf(var5.getType().getVersion()));
               var1.a("world[" + var2 + "][height]", Integer.valueOf(this.F));
               var1.a("world[" + var2 + "][chunks_loaded]", Integer.valueOf(var4.N().getLoadedChunks()));
               ++var2;
            }
         }
      }

      var1.a("worlds", Integer.valueOf(var2));
   }

   public void b(MojangStatisticsGenerator var1) {
      var1.b("singleplayer", Boolean.valueOf(this.T()));
      var1.b("server_brand", this.getServerModName());
      var1.b("gui_supported", GraphicsEnvironment.isHeadless()?"headless":"supported");
      var1.b("dedicated", Boolean.valueOf(this.ae()));
   }

   public boolean getSnooperEnabled() {
      return true;
   }

   public abstract boolean ae();

   public boolean getOnlineMode() {
      return this.onlineMode;
   }

   public void setOnlineMode(boolean var1) {
      this.onlineMode = var1;
   }

   public boolean getSpawnAnimals() {
      return this.spawnAnimals;
   }

   public void setSpawnAnimals(boolean var1) {
      this.spawnAnimals = var1;
   }

   public boolean getSpawnNPCs() {
      return this.spawnNPCs;
   }

   public abstract boolean ai();

   public void setSpawnNPCs(boolean var1) {
      this.spawnNPCs = var1;
   }

   public boolean getPVP() {
      return this.pvpMode;
   }

   public void setPVP(boolean var1) {
      this.pvpMode = var1;
   }

   public boolean getAllowFlight() {
      return this.allowFlight;
   }

   public void setAllowFlight(boolean var1) {
      this.allowFlight = var1;
   }

   public abstract boolean getEnableCommandBlock();

   public String getMotd() {
      return this.motd;
   }

   public void setMotd(String var1) {
      this.motd = var1;
   }

   public int getMaxBuildHeight() {
      return this.F;
   }

   public void c(int var1) {
      this.F = var1;
   }

   public boolean isStopped() {
      return this.isStopped;
   }

   public PlayerList getPlayerList() {
      return this.v;
   }

   public void a(PlayerList var1) {
      this.v = var1;
   }

   public void setGamemode(WorldSettings.EnumGamemode var1) {
      for(int var2 = 0; var2 < this.worldServer.length; ++var2) {
         getServer().worldServer[var2].getWorldData().setGameType(var1);
      }

   }

   public ServerConnection aq() {
      return this.q;
   }

   public boolean as() {
      return false;
   }

   public abstract String a(WorldSettings.EnumGamemode var1, boolean var2);

   public int at() {
      return this.ticks;
   }

   public void au() {
      this.T = true;
   }

   public BlockPosition getChunkCoordinates() {
      return BlockPosition.ZERO;
   }

   public Vec3D d() {
      return new Vec3D(0.0D, 0.0D, 0.0D);
   }

   public World getWorld() {
      return this.worldServer[0];
   }

   public Entity f() {
      return null;
   }

   public int getSpawnProtection() {
      return 16;
   }

   public boolean a(World var1, BlockPosition var2, EntityHuman var3) {
      return false;
   }

   public void setForceGamemode(boolean var1) {
      this.U = var1;
   }

   public boolean getForceGamemode() {
      return this.U;
   }

   public Proxy ay() {
      return this.e;
   }

   public static long az() {
      return System.currentTimeMillis();
   }

   public int getIdleTimeout() {
      return this.G;
   }

   public void setIdleTimeout(int var1) {
      this.G = var1;
   }

   public IChatBaseComponent getScoreboardDisplayName() {
      return new ChatComponentText(this.getName());
   }

   public boolean aB() {
      return true;
   }

   public MinecraftSessionService aD() {
      return this.W;
   }

   public GameProfileRepository getGameProfileRepository() {
      return this.Y;
   }

   public UserCache getUserCache() {
      return this.Z;
   }

   public ServerPing aG() {
      return this.r;
   }

   public void aH() {
      this.X = 0L;
   }

   public Entity a(UUID var1) {
      WorldServer[] var2 = this.worldServer;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorldServer var5 = var2[var4];
         if(var5 != null) {
            Entity var6 = var5.getEntity(var1);
            if(var6 != null) {
               return var6;
            }
         }
      }

      return null;
   }

   public boolean getSendCommandFeedback() {
      return getServer().worldServer[0].getGameRules().getBoolean("sendCommandFeedback");
   }

   public void a(CommandObjectiveExecutor.EnumCommandResult var1, int var2) {
   }

   public int aI() {
      return 29999984;
   }

   public <V> ListenableFuture<V> a(Callable<V> var1) {
      Validate.notNull(var1);
      if(!this.isMainThread() && !this.isStopped()) {
         ListenableFutureTask var2 = ListenableFutureTask.create(var1);
         Queue var3 = this.j;
         synchronized(this.j) {
            this.j.add(var2);
            return var2;
         }
      } else {
         try {
            return Futures.immediateFuture(var1.call());
         } catch (Exception var6) {
            return Futures.immediateFailedCheckedFuture(var6);
         }
      }
   }

   public ListenableFuture<Object> postToMainThread(Runnable var1) {
      Validate.notNull(var1);
      return this.a(Executors.callable(var1));
   }

   public boolean isMainThread() {
      return Thread.currentThread() == this.serverThread;
   }

   public int aK() {
      return 256;
   }

   public long aL() {
      return this.ab;
   }

   public Thread aM() {
      return this.serverThread;
   }
}
