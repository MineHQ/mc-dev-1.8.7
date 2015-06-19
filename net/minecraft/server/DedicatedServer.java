package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Proxy;
import java.security.KeyPair;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CrashReport;
import net.minecraft.server.DedicatedPlayerList;
import net.minecraft.server.EULA;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IMinecraftServer;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftEncryption;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MojangStatisticsGenerator;
import net.minecraft.server.NameReferencingFileConverter;
import net.minecraft.server.PlayerList;
import net.minecraft.server.PropertyManager;
import net.minecraft.server.RemoteControlCommandListener;
import net.minecraft.server.RemoteControlListener;
import net.minecraft.server.RemoteStatusListener;
import net.minecraft.server.ServerCommand;
import net.minecraft.server.ServerGUI;
import net.minecraft.server.ThreadWatchdog;
import net.minecraft.server.World;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DedicatedServer extends MinecraftServer implements IMinecraftServer {
   private static final Logger LOGGER = LogManager.getLogger();
   private final List<ServerCommand> l = Collections.synchronizedList(Lists.newArrayList());
   private RemoteStatusListener m;
   private RemoteControlListener n;
   public PropertyManager propertyManager;
   private EULA p;
   private boolean generateStructures;
   private WorldSettings.EnumGamemode r;
   private boolean s;

   public DedicatedServer(File var1) {
      super(var1, Proxy.NO_PROXY, a);
      Thread var10001 = new Thread("Server Infinisleeper") {
         {
            this.setDaemon(true);
            this.start();
         }

         public void run() {
            while(true) {
               try {
                  Thread.sleep(2147483647L);
               } catch (InterruptedException var2) {
                  ;
               }
            }
         }
      };
   }

   protected boolean init() throws IOException {
      Thread var1 = new Thread("Server console handler") {
         public void run() {
            BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));

            String var2;
            try {
               while(!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning() && (var2 = var1.readLine()) != null) {
                  DedicatedServer.this.issueCommand(var2, DedicatedServer.this);
               }
            } catch (IOException var4) {
               DedicatedServer.LOGGER.error((String)"Exception handling console input", (Throwable)var4);
            }

         }
      };
      var1.setDaemon(true);
      var1.start();
      LOGGER.info("Starting minecraft server version 1.8.7");
      if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
         LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
      }

      LOGGER.info("Loading properties");
      this.propertyManager = new PropertyManager(new File("server.properties"));
      this.p = new EULA(new File("eula.txt"));
      if(!this.p.a()) {
         LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
         this.p.b();
         return false;
      } else {
         if(this.T()) {
            this.c("127.0.0.1");
         } else {
            this.setOnlineMode(this.propertyManager.getBoolean("online-mode", true));
            this.c(this.propertyManager.getString("server-ip", ""));
         }

         this.setSpawnAnimals(this.propertyManager.getBoolean("spawn-animals", true));
         this.setSpawnNPCs(this.propertyManager.getBoolean("spawn-npcs", true));
         this.setPVP(this.propertyManager.getBoolean("pvp", true));
         this.setAllowFlight(this.propertyManager.getBoolean("allow-flight", false));
         this.setResourcePack(this.propertyManager.getString("resource-pack", ""), this.propertyManager.getString("resource-pack-hash", ""));
         this.setMotd(this.propertyManager.getString("motd", "A Minecraft Server"));
         this.setForceGamemode(this.propertyManager.getBoolean("force-gamemode", false));
         this.setIdleTimeout(this.propertyManager.getInt("player-idle-timeout", 0));
         if(this.propertyManager.getInt("difficulty", 1) < 0) {
            this.propertyManager.setProperty("difficulty", Integer.valueOf(0));
         } else if(this.propertyManager.getInt("difficulty", 1) > 3) {
            this.propertyManager.setProperty("difficulty", Integer.valueOf(3));
         }

         this.generateStructures = this.propertyManager.getBoolean("generate-structures", true);
         int var2 = this.propertyManager.getInt("gamemode", WorldSettings.EnumGamemode.SURVIVAL.getId());
         this.r = WorldSettings.a(var2);
         LOGGER.info("Default game type: " + this.r);
         InetAddress var3 = null;
         if(this.getServerIp().length() > 0) {
            var3 = InetAddress.getByName(this.getServerIp());
         }

         if(this.R() < 0) {
            this.setPort(this.propertyManager.getInt("server-port", 25565));
         }

         LOGGER.info("Generating keypair");
         this.a((KeyPair)MinecraftEncryption.b());
         LOGGER.info("Starting Minecraft server on " + (this.getServerIp().length() == 0?"*":this.getServerIp()) + ":" + this.R());

         try {
            this.aq().a(var3, this.R());
         } catch (IOException var17) {
            LOGGER.warn("**** FAILED TO BIND TO PORT!");
            LOGGER.warn("The exception was: {}", new Object[]{var17.toString()});
            LOGGER.warn("Perhaps a server is already running on that port?");
            return false;
         }

         if(!this.getOnlineMode()) {
            LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
            LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
         }

         if(this.aR()) {
            this.getUserCache().c();
         }

         if(!NameReferencingFileConverter.a(this.propertyManager)) {
            return false;
         } else {
            this.a((PlayerList)(new DedicatedPlayerList(this)));
            long var4 = System.nanoTime();
            if(this.U() == null) {
               this.setWorld(this.propertyManager.getString("level-name", "world"));
            }

            String var6 = this.propertyManager.getString("level-seed", "");
            String var7 = this.propertyManager.getString("level-type", "DEFAULT");
            String var8 = this.propertyManager.getString("generator-settings", "");
            long var9 = (new Random()).nextLong();
            if(var6.length() > 0) {
               try {
                  long var11 = Long.parseLong(var6);
                  if(var11 != 0L) {
                     var9 = var11;
                  }
               } catch (NumberFormatException var16) {
                  var9 = (long)var6.hashCode();
               }
            }

            WorldType var18 = WorldType.getType(var7);
            if(var18 == null) {
               var18 = WorldType.NORMAL;
            }

            this.aB();
            this.getEnableCommandBlock();
            this.p();
            this.getSnooperEnabled();
            this.aK();
            this.c(this.propertyManager.getInt("max-build-height", 256));
            this.c((this.getMaxBuildHeight() + 8) / 16 * 16);
            this.c(MathHelper.clamp(this.getMaxBuildHeight(), 64, 256));
            this.propertyManager.setProperty("max-build-height", Integer.valueOf(this.getMaxBuildHeight()));
            LOGGER.info("Preparing level \"" + this.U() + "\"");
            this.a(this.U(), this.U(), var9, var18, var8);
            long var12 = System.nanoTime() - var4;
            String var14 = String.format("%.3fs", new Object[]{Double.valueOf((double)var12 / 1.0E9D)});
            LOGGER.info("Done (" + var14 + ")! For help, type \"help\" or \"?\"");
            if(this.propertyManager.getBoolean("enable-query", false)) {
               LOGGER.info("Starting GS4 status listener");
               this.m = new RemoteStatusListener(this);
               this.m.a();
            }

            if(this.propertyManager.getBoolean("enable-rcon", false)) {
               LOGGER.info("Starting remote control listener");
               this.n = new RemoteControlListener(this);
               this.n.a();
            }

            if(this.aS() > 0L) {
               Thread var15 = new Thread(new ThreadWatchdog(this));
               var15.setName("Server Watchdog");
               var15.setDaemon(true);
               var15.start();
            }

            return true;
         }
      }
   }

   public void setGamemode(WorldSettings.EnumGamemode var1) {
      super.setGamemode(var1);
      this.r = var1;
   }

   public boolean getGenerateStructures() {
      return this.generateStructures;
   }

   public WorldSettings.EnumGamemode getGamemode() {
      return this.r;
   }

   public EnumDifficulty getDifficulty() {
      return EnumDifficulty.getById(this.propertyManager.getInt("difficulty", 1));
   }

   public boolean isHardcore() {
      return this.propertyManager.getBoolean("hardcore", false);
   }

   protected void a(CrashReport var1) {
   }

   public CrashReport b(CrashReport var1) {
      var1 = super.b(var1);
      var1.g().a("Is Modded", new Callable() {
         public String a() throws Exception {
            String var1 = DedicatedServer.this.getServerModName();
            return !var1.equals("vanilla")?"Definitely; Server brand changed to \'" + var1 + "\'":"Unknown (can\'t tell)";
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      var1.g().a("Type", new Callable() {
         public String a() throws Exception {
            return "Dedicated Server (map_server.txt)";
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      return var1;
   }

   protected void z() {
      System.exit(0);
   }

   protected void B() {
      super.B();
      this.aO();
   }

   public boolean getAllowNether() {
      return this.propertyManager.getBoolean("allow-nether", true);
   }

   public boolean getSpawnMonsters() {
      return this.propertyManager.getBoolean("spawn-monsters", true);
   }

   public void a(MojangStatisticsGenerator var1) {
      var1.a("whitelist_enabled", Boolean.valueOf(this.aP().getHasWhitelist()));
      var1.a("whitelist_count", Integer.valueOf(this.aP().getWhitelisted().length));
      super.a(var1);
   }

   public boolean getSnooperEnabled() {
      return this.propertyManager.getBoolean("snooper-enabled", true);
   }

   public void issueCommand(String var1, ICommandListener var2) {
      this.l.add(new ServerCommand(var1, var2));
   }

   public void aO() {
      while(!this.l.isEmpty()) {
         ServerCommand var1 = (ServerCommand)this.l.remove(0);
         this.getCommandHandler().a(var1.source, var1.command);
      }

   }

   public boolean ae() {
      return true;
   }

   public boolean ai() {
      return this.propertyManager.getBoolean("use-native-transport", true);
   }

   public DedicatedPlayerList aP() {
      return (DedicatedPlayerList)super.getPlayerList();
   }

   public int a(String var1, int var2) {
      return this.propertyManager.getInt(var1, var2);
   }

   public String a(String var1, String var2) {
      return this.propertyManager.getString(var1, var2);
   }

   public boolean a(String var1, boolean var2) {
      return this.propertyManager.getBoolean(var1, var2);
   }

   public void a(String var1, Object var2) {
      this.propertyManager.setProperty(var1, var2);
   }

   public void a() {
      this.propertyManager.savePropertiesFile();
   }

   public String b() {
      File var1 = this.propertyManager.c();
      return var1 != null?var1.getAbsolutePath():"No settings file";
   }

   public void aQ() {
      ServerGUI.a(this);
      this.s = true;
   }

   public boolean as() {
      return this.s;
   }

   public String a(WorldSettings.EnumGamemode var1, boolean var2) {
      return "";
   }

   public boolean getEnableCommandBlock() {
      return this.propertyManager.getBoolean("enable-command-block", false);
   }

   public int getSpawnProtection() {
      return this.propertyManager.getInt("spawn-protection", super.getSpawnProtection());
   }

   public boolean a(World var1, BlockPosition var2, EntityHuman var3) {
      if(var1.worldProvider.getDimension() != 0) {
         return false;
      } else if(this.aP().getOPs().isEmpty()) {
         return false;
      } else if(this.aP().isOp(var3.getProfile())) {
         return false;
      } else if(this.getSpawnProtection() <= 0) {
         return false;
      } else {
         BlockPosition var4 = var1.getSpawn();
         int var5 = MathHelper.a(var2.getX() - var4.getX());
         int var6 = MathHelper.a(var2.getZ() - var4.getZ());
         int var7 = Math.max(var5, var6);
         return var7 <= this.getSpawnProtection();
      }
   }

   public int p() {
      return this.propertyManager.getInt("op-permission-level", 4);
   }

   public void setIdleTimeout(int var1) {
      super.setIdleTimeout(var1);
      this.propertyManager.setProperty("player-idle-timeout", Integer.valueOf(var1));
      this.a();
   }

   public boolean q() {
      return this.propertyManager.getBoolean("broadcast-rcon-to-ops", true);
   }

   public boolean r() {
      return this.propertyManager.getBoolean("broadcast-console-to-ops", true);
   }

   public boolean aB() {
      return this.propertyManager.getBoolean("announce-player-achievements", true);
   }

   public int aI() {
      int var1 = this.propertyManager.getInt("max-world-size", super.aI());
      if(var1 < 1) {
         var1 = 1;
      } else if(var1 > super.aI()) {
         var1 = super.aI();
      }

      return var1;
   }

   public int aK() {
      return this.propertyManager.getInt("network-compression-threshold", super.aK());
   }

   protected boolean aR() {
      boolean var2 = false;

      int var1;
      for(var1 = 0; !var2 && var1 <= 2; ++var1) {
         if(var1 > 0) {
            LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
            this.aU();
         }

         var2 = NameReferencingFileConverter.a((MinecraftServer)this);
      }

      boolean var3 = false;

      for(var1 = 0; !var3 && var1 <= 2; ++var1) {
         if(var1 > 0) {
            LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
            this.aU();
         }

         var3 = NameReferencingFileConverter.b((MinecraftServer)this);
      }

      boolean var4 = false;

      for(var1 = 0; !var4 && var1 <= 2; ++var1) {
         if(var1 > 0) {
            LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
            this.aU();
         }

         var4 = NameReferencingFileConverter.c((MinecraftServer)this);
      }

      boolean var5 = false;

      for(var1 = 0; !var5 && var1 <= 2; ++var1) {
         if(var1 > 0) {
            LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
            this.aU();
         }

         var5 = NameReferencingFileConverter.d((MinecraftServer)this);
      }

      boolean var6 = false;

      for(var1 = 0; !var6 && var1 <= 2; ++var1) {
         if(var1 > 0) {
            LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
            this.aU();
         }

         var6 = NameReferencingFileConverter.a(this, this.propertyManager);
      }

      return var2 || var3 || var4 || var5 || var6;
   }

   private void aU() {
      try {
         Thread.sleep(5000L);
      } catch (InterruptedException var2) {
         ;
      }
   }

   public long aS() {
      return this.propertyManager.getLong("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
   }

   public String getPlugins() {
      return "";
   }

   public String executeRemoteCommand(String var1) {
      RemoteControlCommandListener.getInstance().i();
      this.b.a(RemoteControlCommandListener.getInstance(), var1);
      return RemoteControlCommandListener.getInstance().j();
   }

   // $FF: synthetic method
   public PlayerList getPlayerList() {
      return this.aP();
   }
}
