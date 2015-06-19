package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.DemoPlayerInteractManager;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.GameProfileBanEntry;
import net.minecraft.server.GameProfileBanList;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IPlayerFileData;
import net.minecraft.server.IWorldBorderListener;
import net.minecraft.server.IpBanEntry;
import net.minecraft.server.IpBanList;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.OpList;
import net.minecraft.server.OpListEntry;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketPlayOutAbilities;
import net.minecraft.server.PacketPlayOutChat;
import net.minecraft.server.PacketPlayOutCustomPayload;
import net.minecraft.server.PacketPlayOutEntityEffect;
import net.minecraft.server.PacketPlayOutExperience;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.PacketPlayOutHeldItemSlot;
import net.minecraft.server.PacketPlayOutLogin;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import net.minecraft.server.PacketPlayOutRespawn;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import net.minecraft.server.PacketPlayOutServerDifficulty;
import net.minecraft.server.PacketPlayOutSpawnPosition;
import net.minecraft.server.PacketPlayOutUpdateTime;
import net.minecraft.server.PacketPlayOutWorldBorder;
import net.minecraft.server.PlayerChunkMap;
import net.minecraft.server.PlayerConnection;
import net.minecraft.server.PlayerInteractManager;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.ScoreboardTeam;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.ServerStatisticManager;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;
import net.minecraft.server.UserCache;
import net.minecraft.server.WhiteList;
import net.minecraft.server.WhiteListEntry;
import net.minecraft.server.World;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PlayerList {
   public static final File a = new File("banned-players.json");
   public static final File b = new File("banned-ips.json");
   public static final File c = new File("ops.json");
   public static final File d = new File("whitelist.json");
   private static final Logger f = LogManager.getLogger();
   private static final SimpleDateFormat g = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
   private final MinecraftServer server;
   public final List<EntityPlayer> players = Lists.newArrayList();
   private final Map<UUID, EntityPlayer> j = Maps.newHashMap();
   private final GameProfileBanList k;
   private final IpBanList l;
   private final OpList operators;
   private final WhiteList whitelist;
   private final Map<UUID, ServerStatisticManager> o;
   public IPlayerFileData playerFileData;
   private boolean hasWhitelist;
   protected int maxPlayers;
   private int r;
   private WorldSettings.EnumGamemode s;
   private boolean t;
   private int u;

   public PlayerList(MinecraftServer var1) {
      this.k = new GameProfileBanList(a);
      this.l = new IpBanList(b);
      this.operators = new OpList(c);
      this.whitelist = new WhiteList(d);
      this.o = Maps.newHashMap();
      this.server = var1;
      this.k.a(false);
      this.l.a(false);
      this.maxPlayers = 8;
   }

   public void a(NetworkManager var1, EntityPlayer var2) {
      GameProfile var3 = var2.getProfile();
      UserCache var4 = this.server.getUserCache();
      GameProfile var5 = var4.a(var3.getId());
      String var6 = var5 == null?var3.getName():var5.getName();
      var4.a(var3);
      NBTTagCompound var7 = this.a(var2);
      var2.spawnIn(this.server.getWorldServer(var2.dimension));
      var2.playerInteractManager.a((WorldServer)var2.world);
      String var8 = "local";
      if(var1.getSocketAddress() != null) {
         var8 = var1.getSocketAddress().toString();
      }

      f.info(var2.getName() + "[" + var8 + "] logged in with entity id " + var2.getId() + " at (" + var2.locX + ", " + var2.locY + ", " + var2.locZ + ")");
      WorldServer var9 = this.server.getWorldServer(var2.dimension);
      WorldData var10 = var9.getWorldData();
      BlockPosition var11 = var9.getSpawn();
      this.a(var2, (EntityPlayer)null, var9);
      PlayerConnection var12 = new PlayerConnection(this.server, var1, var2);
      var12.sendPacket(new PacketPlayOutLogin(var2.getId(), var2.playerInteractManager.getGameMode(), var10.isHardcore(), var9.worldProvider.getDimension(), var9.getDifficulty(), this.getMaxPlayers(), var10.getType(), var9.getGameRules().getBoolean("reducedDebugInfo")));
      var12.sendPacket(new PacketPlayOutCustomPayload("MC|Brand", (new PacketDataSerializer(Unpooled.buffer())).a(this.getServer().getServerModName())));
      var12.sendPacket(new PacketPlayOutServerDifficulty(var10.getDifficulty(), var10.isDifficultyLocked()));
      var12.sendPacket(new PacketPlayOutSpawnPosition(var11));
      var12.sendPacket(new PacketPlayOutAbilities(var2.abilities));
      var12.sendPacket(new PacketPlayOutHeldItemSlot(var2.inventory.itemInHandIndex));
      var2.getStatisticManager().d();
      var2.getStatisticManager().updateStatistics(var2);
      this.sendScoreboard((ScoreboardServer)var9.getScoreboard(), var2);
      this.server.aH();
      ChatMessage var13;
      if(!var2.getName().equalsIgnoreCase(var6)) {
         var13 = new ChatMessage("multiplayer.player.joined.renamed", new Object[]{var2.getScoreboardDisplayName(), var6});
      } else {
         var13 = new ChatMessage("multiplayer.player.joined", new Object[]{var2.getScoreboardDisplayName()});
      }

      var13.getChatModifier().setColor(EnumChatFormat.YELLOW);
      this.sendMessage(var13);
      this.onPlayerJoin(var2);
      var12.a(var2.locX, var2.locY, var2.locZ, var2.yaw, var2.pitch);
      this.b(var2, var9);
      if(this.server.getResourcePack().length() > 0) {
         var2.setResourcePack(this.server.getResourcePack(), this.server.getResourcePackHash());
      }

      Iterator var14 = var2.getEffects().iterator();

      while(var14.hasNext()) {
         MobEffect var15 = (MobEffect)var14.next();
         var12.sendPacket(new PacketPlayOutEntityEffect(var2.getId(), var15));
      }

      var2.syncInventory();
      if(var7 != null && var7.hasKeyOfType("Riding", 10)) {
         Entity var16 = EntityTypes.a((NBTTagCompound)var7.getCompound("Riding"), (World)var9);
         if(var16 != null) {
            var16.attachedToPlayer = true;
            var9.addEntity(var16);
            var2.mount(var16);
            var16.attachedToPlayer = false;
         }
      }

   }

   public void sendScoreboard(ScoreboardServer var1, EntityPlayer var2) {
      HashSet var3 = Sets.newHashSet();
      Iterator var4 = var1.getTeams().iterator();

      while(var4.hasNext()) {
         ScoreboardTeam var5 = (ScoreboardTeam)var4.next();
         var2.playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(var5, 0));
      }

      for(int var9 = 0; var9 < 19; ++var9) {
         ScoreboardObjective var10 = var1.getObjectiveForSlot(var9);
         if(var10 != null && !var3.contains(var10)) {
            List var6 = var1.getScoreboardScorePacketsForObjective(var10);
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               Packet var8 = (Packet)var7.next();
               var2.playerConnection.sendPacket(var8);
            }

            var3.add(var10);
         }
      }

   }

   public void setPlayerFileData(WorldServer[] var1) {
      this.playerFileData = var1[0].getDataManager().getPlayerFileData();
      var1[0].getWorldBorder().a(new IWorldBorderListener() {
         public void a(WorldBorder var1, double var2) {
            PlayerList.this.sendAll(new PacketPlayOutWorldBorder(var1, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE));
         }

         public void a(WorldBorder var1, double var2, double var4, long var6) {
            PlayerList.this.sendAll(new PacketPlayOutWorldBorder(var1, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE));
         }

         public void a(WorldBorder var1, double var2, double var4) {
            PlayerList.this.sendAll(new PacketPlayOutWorldBorder(var1, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER));
         }

         public void a(WorldBorder var1, int var2) {
            PlayerList.this.sendAll(new PacketPlayOutWorldBorder(var1, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME));
         }

         public void b(WorldBorder var1, int var2) {
            PlayerList.this.sendAll(new PacketPlayOutWorldBorder(var1, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS));
         }

         public void b(WorldBorder var1, double var2) {
         }

         public void c(WorldBorder var1, double var2) {
         }
      });
   }

   public void a(EntityPlayer var1, WorldServer var2) {
      WorldServer var3 = var1.u();
      if(var2 != null) {
         var2.getPlayerChunkMap().removePlayer(var1);
      }

      var3.getPlayerChunkMap().addPlayer(var1);
      var3.chunkProviderServer.getChunkAt((int)var1.locX >> 4, (int)var1.locZ >> 4);
   }

   public int d() {
      return PlayerChunkMap.getFurthestViewableBlock(this.s());
   }

   public NBTTagCompound a(EntityPlayer var1) {
      NBTTagCompound var2 = this.server.worldServer[0].getWorldData().i();
      NBTTagCompound var3;
      if(var1.getName().equals(this.server.S()) && var2 != null) {
         var1.f(var2);
         var3 = var2;
         f.debug("loading single player");
      } else {
         var3 = this.playerFileData.load(var1);
      }

      return var3;
   }

   protected void savePlayerFile(EntityPlayer var1) {
      this.playerFileData.save(var1);
      ServerStatisticManager var2 = (ServerStatisticManager)this.o.get(var1.getUniqueID());
      if(var2 != null) {
         var2.b();
      }

   }

   public void onPlayerJoin(EntityPlayer var1) {
      this.players.add(var1);
      this.j.put(var1.getUniqueID(), var1);
      this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{var1}));
      WorldServer var2 = this.server.getWorldServer(var1.dimension);
      var2.addEntity(var1);
      this.a((EntityPlayer)var1, (WorldServer)null);

      for(int var3 = 0; var3 < this.players.size(); ++var3) {
         EntityPlayer var4 = (EntityPlayer)this.players.get(var3);
         var1.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, new EntityPlayer[]{var4}));
      }

   }

   public void d(EntityPlayer var1) {
      var1.u().getPlayerChunkMap().movePlayer(var1);
   }

   public void disconnect(EntityPlayer var1) {
      var1.b((Statistic)StatisticList.f);
      this.savePlayerFile(var1);
      WorldServer var2 = var1.u();
      if(var1.vehicle != null) {
         var2.removeEntity(var1.vehicle);
         f.debug("removing player mount");
      }

      var2.kill(var1);
      var2.getPlayerChunkMap().removePlayer(var1);
      this.players.remove(var1);
      UUID var3 = var1.getUniqueID();
      EntityPlayer var4 = (EntityPlayer)this.j.get(var3);
      if(var4 == var1) {
         this.j.remove(var3);
         this.o.remove(var3);
      }

      this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, new EntityPlayer[]{var1}));
   }

   public String attemptLogin(SocketAddress var1, GameProfile var2) {
      String var4;
      if(this.k.isBanned(var2)) {
         GameProfileBanEntry var5 = (GameProfileBanEntry)this.k.get(var2);
         var4 = "You are banned from this server!\nReason: " + var5.getReason();
         if(var5.getExpires() != null) {
            var4 = var4 + "\nYour ban will be removed on " + g.format(var5.getExpires());
         }

         return var4;
      } else if(!this.isWhitelisted(var2)) {
         return "You are not white-listed on this server!";
      } else if(this.l.isBanned(var1)) {
         IpBanEntry var3 = this.l.get(var1);
         var4 = "Your IP address is banned from this server!\nReason: " + var3.getReason();
         if(var3.getExpires() != null) {
            var4 = var4 + "\nYour ban will be removed on " + g.format(var3.getExpires());
         }

         return var4;
      } else {
         return this.players.size() >= this.maxPlayers && !this.f(var2)?"The server is full!":null;
      }
   }

   public EntityPlayer processLogin(GameProfile var1) {
      UUID var2 = EntityHuman.a(var1);
      ArrayList var3 = Lists.newArrayList();

      for(int var4 = 0; var4 < this.players.size(); ++var4) {
         EntityPlayer var5 = (EntityPlayer)this.players.get(var4);
         if(var5.getUniqueID().equals(var2)) {
            var3.add(var5);
         }
      }

      EntityPlayer var7 = (EntityPlayer)this.j.get(var1.getId());
      if(var7 != null && !var3.contains(var7)) {
         var3.add(var7);
      }

      Iterator var8 = var3.iterator();

      while(var8.hasNext()) {
         EntityPlayer var6 = (EntityPlayer)var8.next();
         var6.playerConnection.disconnect("You logged in from another location");
      }

      Object var9;
      if(this.server.X()) {
         var9 = new DemoPlayerInteractManager(this.server.getWorldServer(0));
      } else {
         var9 = new PlayerInteractManager(this.server.getWorldServer(0));
      }

      return new EntityPlayer(this.server, this.server.getWorldServer(0), var1, (PlayerInteractManager)var9);
   }

   public EntityPlayer moveToWorld(EntityPlayer var1, int var2, boolean var3) {
      var1.u().getTracker().untrackPlayer(var1);
      var1.u().getTracker().untrackEntity(var1);
      var1.u().getPlayerChunkMap().removePlayer(var1);
      this.players.remove(var1);
      this.server.getWorldServer(var1.dimension).removeEntity(var1);
      BlockPosition var4 = var1.getBed();
      boolean var5 = var1.isRespawnForced();
      var1.dimension = var2;
      Object var6;
      if(this.server.X()) {
         var6 = new DemoPlayerInteractManager(this.server.getWorldServer(var1.dimension));
      } else {
         var6 = new PlayerInteractManager(this.server.getWorldServer(var1.dimension));
      }

      EntityPlayer var7 = new EntityPlayer(this.server, this.server.getWorldServer(var1.dimension), var1.getProfile(), (PlayerInteractManager)var6);
      var7.playerConnection = var1.playerConnection;
      var7.copyTo(var1, var3);
      var7.d(var1.getId());
      var7.o(var1);
      WorldServer var8 = this.server.getWorldServer(var1.dimension);
      this.a(var7, var1, var8);
      BlockPosition var9;
      if(var4 != null) {
         var9 = EntityHuman.getBed(this.server.getWorldServer(var1.dimension), var4, var5);
         if(var9 != null) {
            var7.setPositionRotation((double)((float)var9.getX() + 0.5F), (double)((float)var9.getY() + 0.1F), (double)((float)var9.getZ() + 0.5F), 0.0F, 0.0F);
            var7.setRespawnPosition(var4, var5);
         } else {
            var7.playerConnection.sendPacket(new PacketPlayOutGameStateChange(0, 0.0F));
         }
      }

      var8.chunkProviderServer.getChunkAt((int)var7.locX >> 4, (int)var7.locZ >> 4);

      while(!var8.getCubes(var7, var7.getBoundingBox()).isEmpty() && var7.locY < 256.0D) {
         var7.setPosition(var7.locX, var7.locY + 1.0D, var7.locZ);
      }

      var7.playerConnection.sendPacket(new PacketPlayOutRespawn(var7.dimension, var7.world.getDifficulty(), var7.world.getWorldData().getType(), var7.playerInteractManager.getGameMode()));
      var9 = var8.getSpawn();
      var7.playerConnection.a(var7.locX, var7.locY, var7.locZ, var7.yaw, var7.pitch);
      var7.playerConnection.sendPacket(new PacketPlayOutSpawnPosition(var9));
      var7.playerConnection.sendPacket(new PacketPlayOutExperience(var7.exp, var7.expTotal, var7.expLevel));
      this.b(var7, var8);
      var8.getPlayerChunkMap().addPlayer(var7);
      var8.addEntity(var7);
      this.players.add(var7);
      this.j.put(var7.getUniqueID(), var7);
      var7.syncInventory();
      var7.setHealth(var7.getHealth());
      return var7;
   }

   public void changeDimension(EntityPlayer var1, int var2) {
      int var3 = var1.dimension;
      WorldServer var4 = this.server.getWorldServer(var1.dimension);
      var1.dimension = var2;
      WorldServer var5 = this.server.getWorldServer(var1.dimension);
      var1.playerConnection.sendPacket(new PacketPlayOutRespawn(var1.dimension, var1.world.getDifficulty(), var1.world.getWorldData().getType(), var1.playerInteractManager.getGameMode()));
      var4.removeEntity(var1);
      var1.dead = false;
      this.changeWorld(var1, var3, var4, var5);
      this.a(var1, var4);
      var1.playerConnection.a(var1.locX, var1.locY, var1.locZ, var1.yaw, var1.pitch);
      var1.playerInteractManager.a(var5);
      this.b(var1, var5);
      this.updateClient(var1);
      Iterator var6 = var1.getEffects().iterator();

      while(var6.hasNext()) {
         MobEffect var7 = (MobEffect)var6.next();
         var1.playerConnection.sendPacket(new PacketPlayOutEntityEffect(var1.getId(), var7));
      }

   }

   public void changeWorld(Entity var1, int var2, WorldServer var3, WorldServer var4) {
      double var5 = var1.locX;
      double var7 = var1.locZ;
      double var9 = 8.0D;
      float var11 = var1.yaw;
      var3.methodProfiler.a("moving");
      if(var1.dimension == -1) {
         var5 = MathHelper.a(var5 / var9, var4.getWorldBorder().b() + 16.0D, var4.getWorldBorder().d() - 16.0D);
         var7 = MathHelper.a(var7 / var9, var4.getWorldBorder().c() + 16.0D, var4.getWorldBorder().e() - 16.0D);
         var1.setPositionRotation(var5, var1.locY, var7, var1.yaw, var1.pitch);
         if(var1.isAlive()) {
            var3.entityJoinedWorld(var1, false);
         }
      } else if(var1.dimension == 0) {
         var5 = MathHelper.a(var5 * var9, var4.getWorldBorder().b() + 16.0D, var4.getWorldBorder().d() - 16.0D);
         var7 = MathHelper.a(var7 * var9, var4.getWorldBorder().c() + 16.0D, var4.getWorldBorder().e() - 16.0D);
         var1.setPositionRotation(var5, var1.locY, var7, var1.yaw, var1.pitch);
         if(var1.isAlive()) {
            var3.entityJoinedWorld(var1, false);
         }
      } else {
         BlockPosition var12;
         if(var2 == 1) {
            var12 = var4.getSpawn();
         } else {
            var12 = var4.getDimensionSpawn();
         }

         var5 = (double)var12.getX();
         var1.locY = (double)var12.getY();
         var7 = (double)var12.getZ();
         var1.setPositionRotation(var5, var1.locY, var7, 90.0F, 0.0F);
         if(var1.isAlive()) {
            var3.entityJoinedWorld(var1, false);
         }
      }

      var3.methodProfiler.b();
      if(var2 != 1) {
         var3.methodProfiler.a("placing");
         var5 = (double)MathHelper.clamp((int)var5, -29999872, 29999872);
         var7 = (double)MathHelper.clamp((int)var7, -29999872, 29999872);
         if(var1.isAlive()) {
            var1.setPositionRotation(var5, var1.locY, var7, var1.yaw, var1.pitch);
            var4.getTravelAgent().a(var1, var11);
            var4.addEntity(var1);
            var4.entityJoinedWorld(var1, false);
         }

         var3.methodProfiler.b();
      }

      var1.spawnIn(var4);
   }

   public void tick() {
      if(++this.u > 600) {
         this.sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, this.players));
         this.u = 0;
      }

   }

   public void sendAll(Packet var1) {
      for(int var2 = 0; var2 < this.players.size(); ++var2) {
         ((EntityPlayer)this.players.get(var2)).playerConnection.sendPacket(var1);
      }

   }

   public void a(Packet var1, int var2) {
      for(int var3 = 0; var3 < this.players.size(); ++var3) {
         EntityPlayer var4 = (EntityPlayer)this.players.get(var3);
         if(var4.dimension == var2) {
            var4.playerConnection.sendPacket(var1);
         }
      }

   }

   public void a(EntityHuman var1, IChatBaseComponent var2) {
      ScoreboardTeamBase var3 = var1.getScoreboardTeam();
      if(var3 != null) {
         Collection var4 = var3.getPlayerNameSet();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            EntityPlayer var7 = this.getPlayer(var6);
            if(var7 != null && var7 != var1) {
               var7.sendMessage(var2);
            }
         }

      }
   }

   public void b(EntityHuman var1, IChatBaseComponent var2) {
      ScoreboardTeamBase var3 = var1.getScoreboardTeam();
      if(var3 == null) {
         this.sendMessage(var2);
      } else {
         for(int var4 = 0; var4 < this.players.size(); ++var4) {
            EntityPlayer var5 = (EntityPlayer)this.players.get(var4);
            if(var5.getScoreboardTeam() != var3) {
               var5.sendMessage(var2);
            }
         }

      }
   }

   public String b(boolean var1) {
      String var2 = "";
      ArrayList var3 = Lists.newArrayList((Iterable)this.players);

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         if(var4 > 0) {
            var2 = var2 + ", ";
         }

         var2 = var2 + ((EntityPlayer)var3.get(var4)).getName();
         if(var1) {
            var2 = var2 + " (" + ((EntityPlayer)var3.get(var4)).getUniqueID().toString() + ")";
         }
      }

      return var2;
   }

   public String[] f() {
      String[] var1 = new String[this.players.size()];

      for(int var2 = 0; var2 < this.players.size(); ++var2) {
         var1[var2] = ((EntityPlayer)this.players.get(var2)).getName();
      }

      return var1;
   }

   public GameProfile[] g() {
      GameProfile[] var1 = new GameProfile[this.players.size()];

      for(int var2 = 0; var2 < this.players.size(); ++var2) {
         var1[var2] = ((EntityPlayer)this.players.get(var2)).getProfile();
      }

      return var1;
   }

   public GameProfileBanList getProfileBans() {
      return this.k;
   }

   public IpBanList getIPBans() {
      return this.l;
   }

   public void addOp(GameProfile var1) {
      this.operators.add(new OpListEntry(var1, this.server.p(), this.operators.b(var1)));
   }

   public void removeOp(GameProfile var1) {
      this.operators.remove(var1);
   }

   public boolean isWhitelisted(GameProfile var1) {
      return !this.hasWhitelist || this.operators.d(var1) || this.whitelist.d(var1);
   }

   public boolean isOp(GameProfile var1) {
      return this.operators.d(var1) || this.server.T() && this.server.worldServer[0].getWorldData().v() && this.server.S().equalsIgnoreCase(var1.getName()) || this.t;
   }

   public EntityPlayer getPlayer(String var1) {
      Iterator var2 = this.players.iterator();

      EntityPlayer var3;
      do {
         if(!var2.hasNext()) {
            return null;
         }

         var3 = (EntityPlayer)var2.next();
      } while(!var3.getName().equalsIgnoreCase(var1));

      return var3;
   }

   public void sendPacketNearby(double var1, double var3, double var5, double var7, int var9, Packet var10) {
      this.sendPacketNearby((EntityHuman)null, var1, var3, var5, var7, var9, var10);
   }

   public void sendPacketNearby(EntityHuman var1, double var2, double var4, double var6, double var8, int var10, Packet var11) {
      for(int var12 = 0; var12 < this.players.size(); ++var12) {
         EntityPlayer var13 = (EntityPlayer)this.players.get(var12);
         if(var13 != var1 && var13.dimension == var10) {
            double var14 = var2 - var13.locX;
            double var16 = var4 - var13.locY;
            double var18 = var6 - var13.locZ;
            if(var14 * var14 + var16 * var16 + var18 * var18 < var8 * var8) {
               var13.playerConnection.sendPacket(var11);
            }
         }
      }

   }

   public void savePlayers() {
      for(int var1 = 0; var1 < this.players.size(); ++var1) {
         this.savePlayerFile((EntityPlayer)this.players.get(var1));
      }

   }

   public void addWhitelist(GameProfile var1) {
      this.whitelist.add(new WhiteListEntry(var1));
   }

   public void removeWhitelist(GameProfile var1) {
      this.whitelist.remove(var1);
   }

   public WhiteList getWhitelist() {
      return this.whitelist;
   }

   public String[] getWhitelisted() {
      return this.whitelist.getEntries();
   }

   public OpList getOPs() {
      return this.operators;
   }

   public String[] n() {
      return this.operators.getEntries();
   }

   public void reloadWhitelist() {
   }

   public void b(EntityPlayer var1, WorldServer var2) {
      WorldBorder var3 = this.server.worldServer[0].getWorldBorder();
      var1.playerConnection.sendPacket(new PacketPlayOutWorldBorder(var3, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
      var1.playerConnection.sendPacket(new PacketPlayOutUpdateTime(var2.getTime(), var2.getDayTime(), var2.getGameRules().getBoolean("doDaylightCycle")));
      if(var2.S()) {
         var1.playerConnection.sendPacket(new PacketPlayOutGameStateChange(1, 0.0F));
         var1.playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, var2.j(1.0F)));
         var1.playerConnection.sendPacket(new PacketPlayOutGameStateChange(8, var2.h(1.0F)));
      }

   }

   public void updateClient(EntityPlayer var1) {
      var1.updateInventory(var1.defaultContainer);
      var1.triggerHealthUpdate();
      var1.playerConnection.sendPacket(new PacketPlayOutHeldItemSlot(var1.inventory.itemInHandIndex));
   }

   public int getPlayerCount() {
      return this.players.size();
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public String[] getSeenPlayers() {
      return this.server.worldServer[0].getDataManager().getPlayerFileData().getSeenPlayers();
   }

   public boolean getHasWhitelist() {
      return this.hasWhitelist;
   }

   public void setHasWhitelist(boolean var1) {
      this.hasWhitelist = var1;
   }

   public List<EntityPlayer> b(String var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = this.players.iterator();

      while(var3.hasNext()) {
         EntityPlayer var4 = (EntityPlayer)var3.next();
         if(var4.w().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public int s() {
      return this.r;
   }

   public MinecraftServer getServer() {
      return this.server;
   }

   public NBTTagCompound t() {
      return null;
   }

   private void a(EntityPlayer var1, EntityPlayer var2, World var3) {
      if(var2 != null) {
         var1.playerInteractManager.setGameMode(var2.playerInteractManager.getGameMode());
      } else if(this.s != null) {
         var1.playerInteractManager.setGameMode(this.s);
      }

      var1.playerInteractManager.b(var3.getWorldData().getGameType());
   }

   public void u() {
      for(int var1 = 0; var1 < this.players.size(); ++var1) {
         ((EntityPlayer)this.players.get(var1)).playerConnection.disconnect("Server closed");
      }

   }

   public void sendMessage(IChatBaseComponent var1, boolean var2) {
      this.server.sendMessage(var1);
      int var3 = var2?1:0;
      this.sendAll(new PacketPlayOutChat(var1, (byte)var3));
   }

   public void sendMessage(IChatBaseComponent var1) {
      this.sendMessage(var1, true);
   }

   public ServerStatisticManager a(EntityHuman var1) {
      UUID var2 = var1.getUniqueID();
      ServerStatisticManager var3 = var2 == null?null:(ServerStatisticManager)this.o.get(var2);
      if(var3 == null) {
         File var4 = new File(this.server.getWorldServer(0).getDataManager().getDirectory(), "stats");
         File var5 = new File(var4, var2.toString() + ".json");
         if(!var5.exists()) {
            File var6 = new File(var4, var1.getName() + ".json");
            if(var6.exists() && var6.isFile()) {
               var6.renameTo(var5);
            }
         }

         var3 = new ServerStatisticManager(this.server, var5);
         var3.a();
         this.o.put(var2, var3);
      }

      return var3;
   }

   public void a(int var1) {
      this.r = var1;
      if(this.server.worldServer != null) {
         WorldServer[] var2 = this.server.worldServer;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            WorldServer var5 = var2[var4];
            if(var5 != null) {
               var5.getPlayerChunkMap().a(var1);
            }
         }

      }
   }

   public List<EntityPlayer> v() {
      return this.players;
   }

   public EntityPlayer a(UUID var1) {
      return (EntityPlayer)this.j.get(var1);
   }

   public boolean f(GameProfile var1) {
      return false;
   }
}
