package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AchievementSet;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockCobbleWall;
import net.minecraft.server.BlockFence;
import net.minecraft.server.BlockFenceGate;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.ChestLock;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerChest;
import net.minecraft.server.ContainerHorse;
import net.minecraft.server.ContainerMerchant;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EnumAnimation;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.IMerchant;
import net.minecraft.server.IScoreboardCriteria;
import net.minecraft.server.ITileEntityContainer;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.InventoryMerchant;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWorldMapBase;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MerchantRecipeList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MobEffect;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.OpListEntry;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketPlayInSettings;
import net.minecraft.server.PacketPlayOutAbilities;
import net.minecraft.server.PacketPlayOutAnimation;
import net.minecraft.server.PacketPlayOutAttachEntity;
import net.minecraft.server.PacketPlayOutBed;
import net.minecraft.server.PacketPlayOutCamera;
import net.minecraft.server.PacketPlayOutChat;
import net.minecraft.server.PacketPlayOutCloseWindow;
import net.minecraft.server.PacketPlayOutCombatEvent;
import net.minecraft.server.PacketPlayOutCustomPayload;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutEntityEffect;
import net.minecraft.server.PacketPlayOutEntityStatus;
import net.minecraft.server.PacketPlayOutExperience;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PacketPlayOutMapChunkBulk;
import net.minecraft.server.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.PacketPlayOutOpenSignEditor;
import net.minecraft.server.PacketPlayOutOpenWindow;
import net.minecraft.server.PacketPlayOutRemoveEntityEffect;
import net.minecraft.server.PacketPlayOutResourcePackSend;
import net.minecraft.server.PacketPlayOutSetSlot;
import net.minecraft.server.PacketPlayOutUpdateHealth;
import net.minecraft.server.PacketPlayOutWindowData;
import net.minecraft.server.PacketPlayOutWindowItems;
import net.minecraft.server.PlayerConnection;
import net.minecraft.server.PlayerInteractManager;
import net.minecraft.server.ReportedException;
import net.minecraft.server.ScoreboardObjective;
import net.minecraft.server.ScoreboardScore;
import net.minecraft.server.ScoreboardTeamBase;
import net.minecraft.server.ServerStatisticManager;
import net.minecraft.server.SlotResult;
import net.minecraft.server.Statistic;
import net.minecraft.server.StatisticList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayer extends EntityHuman implements ICrafting {
   private static final Logger bH = LogManager.getLogger();
   private String locale = "en_US";
   public PlayerConnection playerConnection;
   public final MinecraftServer server;
   public final PlayerInteractManager playerInteractManager;
   public double d;
   public double e;
   public final List<ChunkCoordIntPair> chunkCoordIntPairQueue = Lists.newLinkedList();
   public final List<Integer> removeQueue = Lists.newLinkedList();
   private final ServerStatisticManager bK;
   private float bL = Float.MIN_VALUE;
   private float bM = -1.0E8F;
   private int bN = -99999999;
   private boolean bO = true;
   public int lastSentExp = -99999999;
   public int invulnerableTicks = 60;
   private EntityHuman.EnumChatVisibility bR;
   private boolean bS = true;
   private long bT = System.currentTimeMillis();
   private Entity bU = null;
   private int containerCounter;
   public boolean g;
   public int ping;
   public boolean viewingCredits;

   public EntityPlayer(MinecraftServer var1, WorldServer var2, GameProfile var3, PlayerInteractManager var4) {
      super(var2, var3);
      var4.player = this;
      this.playerInteractManager = var4;
      BlockPosition var5 = var2.getSpawn();
      if(!var2.worldProvider.o() && var2.getWorldData().getGameType() != WorldSettings.EnumGamemode.ADVENTURE) {
         int var6 = Math.max(5, var1.getSpawnProtection() - 6);
         int var7 = MathHelper.floor(var2.getWorldBorder().b((double)var5.getX(), (double)var5.getZ()));
         if(var7 < var6) {
            var6 = var7;
         }

         if(var7 <= 1) {
            var6 = 1;
         }

         var5 = var2.r(var5.a(this.random.nextInt(var6 * 2) - var6, 0, this.random.nextInt(var6 * 2) - var6));
      }

      this.server = var1;
      this.bK = var1.getPlayerList().a((EntityHuman)this);
      this.S = 0.0F;
      this.setPositionRotation(var5, 0.0F, 0.0F);

      while(!var2.getCubes(this, this.getBoundingBox()).isEmpty() && this.locY < 255.0D) {
         this.setPosition(this.locX, this.locY + 1.0D, this.locZ);
      }

   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      if(var1.hasKeyOfType("playerGameType", 99)) {
         if(MinecraftServer.getServer().getForceGamemode()) {
            this.playerInteractManager.setGameMode(MinecraftServer.getServer().getGamemode());
         } else {
            this.playerInteractManager.setGameMode(WorldSettings.EnumGamemode.getById(var1.getInt("playerGameType")));
         }
      }

   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("playerGameType", this.playerInteractManager.getGameMode().getId());
   }

   public void levelDown(int var1) {
      super.levelDown(var1);
      this.lastSentExp = -1;
   }

   public void b(int var1) {
      super.b(var1);
      this.lastSentExp = -1;
   }

   public void syncInventory() {
      this.activeContainer.addSlotListener(this);
   }

   public void enterCombat() {
      super.enterCombat();
      this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.bs(), PacketPlayOutCombatEvent.EnumCombatEventType.ENTER_COMBAT));
   }

   public void exitCombat() {
      super.exitCombat();
      this.playerConnection.sendPacket(new PacketPlayOutCombatEvent(this.bs(), PacketPlayOutCombatEvent.EnumCombatEventType.END_COMBAT));
   }

   public void t_() {
      this.playerInteractManager.a();
      --this.invulnerableTicks;
      if(this.noDamageTicks > 0) {
         --this.noDamageTicks;
      }

      this.activeContainer.b();
      if(!this.world.isClientSide && !this.activeContainer.a((EntityHuman)this)) {
         this.closeInventory();
         this.activeContainer = this.defaultContainer;
      }

      while(!this.removeQueue.isEmpty()) {
         int var1 = Math.min(this.removeQueue.size(), Integer.MAX_VALUE);
         int[] var2 = new int[var1];
         Iterator var3 = this.removeQueue.iterator();
         int var4 = 0;

         while(var3.hasNext() && var4 < var1) {
            var2[var4++] = ((Integer)var3.next()).intValue();
            var3.remove();
         }

         this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(var2));
      }

      if(!this.chunkCoordIntPairQueue.isEmpty()) {
         ArrayList var6 = Lists.newArrayList();
         Iterator var8 = this.chunkCoordIntPairQueue.iterator();
         ArrayList var9 = Lists.newArrayList();

         Chunk var5;
         while(var8.hasNext() && var6.size() < 10) {
            ChunkCoordIntPair var10 = (ChunkCoordIntPair)var8.next();
            if(var10 != null) {
               if(this.world.isLoaded(new BlockPosition(var10.x << 4, 0, var10.z << 4))) {
                  var5 = this.world.getChunkAt(var10.x, var10.z);
                  if(var5.isReady()) {
                     var6.add(var5);
                     var9.addAll(((WorldServer)this.world).getTileEntities(var10.x * 16, 0, var10.z * 16, var10.x * 16 + 16, 256, var10.z * 16 + 16));
                     var8.remove();
                  }
               }
            } else {
               var8.remove();
            }
         }

         if(!var6.isEmpty()) {
            if(var6.size() == 1) {
               this.playerConnection.sendPacket(new PacketPlayOutMapChunk((Chunk)var6.get(0), true, '\uffff'));
            } else {
               this.playerConnection.sendPacket(new PacketPlayOutMapChunkBulk(var6));
            }

            Iterator var11 = var9.iterator();

            while(var11.hasNext()) {
               TileEntity var12 = (TileEntity)var11.next();
               this.a(var12);
            }

            var11 = var6.iterator();

            while(var11.hasNext()) {
               var5 = (Chunk)var11.next();
               this.u().getTracker().a(this, var5);
            }
         }
      }

      Entity var7 = this.C();
      if(var7 != this) {
         if(!var7.isAlive()) {
            this.setSpectatorTarget(this);
         } else {
            this.setLocation(var7.locX, var7.locY, var7.locZ, var7.yaw, var7.pitch);
            this.server.getPlayerList().d(this);
            if(this.isSneaking()) {
               this.setSpectatorTarget(this);
            }
         }
      }

   }

   public void l() {
      try {
         super.t_();

         for(int var1 = 0; var1 < this.inventory.getSize(); ++var1) {
            ItemStack var6 = this.inventory.getItem(var1);
            if(var6 != null && var6.getItem().f()) {
               Packet var8 = ((ItemWorldMapBase)var6.getItem()).c(var6, this.world, this);
               if(var8 != null) {
                  this.playerConnection.sendPacket(var8);
               }
            }
         }

         if(this.getHealth() != this.bM || this.bN != this.foodData.getFoodLevel() || this.foodData.getSaturationLevel() == 0.0F != this.bO) {
            this.playerConnection.sendPacket(new PacketPlayOutUpdateHealth(this.getHealth(), this.foodData.getFoodLevel(), this.foodData.getSaturationLevel()));
            this.bM = this.getHealth();
            this.bN = this.foodData.getFoodLevel();
            this.bO = this.foodData.getSaturationLevel() == 0.0F;
         }

         if(this.getHealth() + this.getAbsorptionHearts() != this.bL) {
            this.bL = this.getHealth() + this.getAbsorptionHearts();
            Collection var5 = this.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.g);
            Iterator var7 = var5.iterator();

            while(var7.hasNext()) {
               ScoreboardObjective var9 = (ScoreboardObjective)var7.next();
               this.getScoreboard().getPlayerScoreForObjective(this.getName(), var9).updateForList(Arrays.asList(new EntityHuman[]{this}));
            }
         }

         if(this.expTotal != this.lastSentExp) {
            this.lastSentExp = this.expTotal;
            this.playerConnection.sendPacket(new PacketPlayOutExperience(this.exp, this.expTotal, this.expLevel));
         }

         if(this.ticksLived % 20 * 5 == 0 && !this.getStatisticManager().hasAchievement(AchievementList.L)) {
            this.i_();
         }

      } catch (Throwable var4) {
         CrashReport var2 = CrashReport.a(var4, "Ticking player");
         CrashReportSystemDetails var3 = var2.a("Player being ticked");
         this.appendEntityCrashDetails(var3);
         throw new ReportedException(var2);
      }
   }

   protected void i_() {
      BiomeBase var1 = this.world.getBiome(new BlockPosition(MathHelper.floor(this.locX), 0, MathHelper.floor(this.locZ)));
      String var2 = var1.ah;
      AchievementSet var3 = (AchievementSet)this.getStatisticManager().b(AchievementList.L);
      if(var3 == null) {
         var3 = (AchievementSet)this.getStatisticManager().a(AchievementList.L, new AchievementSet());
      }

      var3.add(var2);
      if(this.getStatisticManager().b(AchievementList.L) && var3.size() >= BiomeBase.n.size()) {
         HashSet var4 = Sets.newHashSet((Iterable)BiomeBase.n);
         Iterator var5 = var3.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            Iterator var7 = var4.iterator();

            while(var7.hasNext()) {
               BiomeBase var8 = (BiomeBase)var7.next();
               if(var8.ah.equals(var6)) {
                  var7.remove();
               }
            }

            if(var4.isEmpty()) {
               break;
            }
         }

         if(var4.isEmpty()) {
            this.b((Statistic)AchievementList.L);
         }
      }

   }

   public void die(DamageSource var1) {
      if(this.world.getGameRules().getBoolean("showDeathMessages")) {
         ScoreboardTeamBase var2 = this.getScoreboardTeam();
         if(var2 != null && var2.j() != ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS) {
            if(var2.j() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS) {
               this.server.getPlayerList().a((EntityHuman)this, (IChatBaseComponent)this.bs().b());
            } else if(var2.j() == ScoreboardTeamBase.EnumNameTagVisibility.HIDE_FOR_OWN_TEAM) {
               this.server.getPlayerList().b((EntityHuman)this, (IChatBaseComponent)this.bs().b());
            }
         } else {
            this.server.getPlayerList().sendMessage(this.bs().b());
         }
      }

      if(!this.world.getGameRules().getBoolean("keepInventory")) {
         this.inventory.n();
      }

      Collection var6 = this.world.getScoreboard().getObjectivesForCriteria(IScoreboardCriteria.d);
      Iterator var3 = var6.iterator();

      while(var3.hasNext()) {
         ScoreboardObjective var4 = (ScoreboardObjective)var3.next();
         ScoreboardScore var5 = this.getScoreboard().getPlayerScoreForObjective(this.getName(), var4);
         var5.incrementScore();
      }

      EntityLiving var7 = this.bt();
      if(var7 != null) {
         EntityTypes.MonsterEggInfo var8 = (EntityTypes.MonsterEggInfo)EntityTypes.eggInfo.get(Integer.valueOf(EntityTypes.a(var7)));
         if(var8 != null) {
            this.b((Statistic)var8.e);
         }

         var7.b(this, this.aW);
      }

      this.b((Statistic)StatisticList.y);
      this.a(StatisticList.h);
      this.bs().g();
   }

   public boolean damageEntity(DamageSource var1, float var2) {
      if(this.isInvulnerable(var1)) {
         return false;
      } else {
         boolean var3 = this.server.ae() && this.cr() && "fall".equals(var1.translationIndex);
         if(!var3 && this.invulnerableTicks > 0 && var1 != DamageSource.OUT_OF_WORLD) {
            return false;
         } else {
            if(var1 instanceof EntityDamageSource) {
               Entity var4 = var1.getEntity();
               if(var4 instanceof EntityHuman && !this.a((EntityHuman)var4)) {
                  return false;
               }

               if(var4 instanceof EntityArrow) {
                  EntityArrow var5 = (EntityArrow)var4;
                  if(var5.shooter instanceof EntityHuman && !this.a((EntityHuman)var5.shooter)) {
                     return false;
                  }
               }
            }

            return super.damageEntity(var1, var2);
         }
      }
   }

   public boolean a(EntityHuman var1) {
      return !this.cr()?false:super.a(var1);
   }

   private boolean cr() {
      return this.server.getPVP();
   }

   public void c(int var1) {
      if(this.dimension == 1 && var1 == 1) {
         this.b((Statistic)AchievementList.D);
         this.world.kill(this);
         this.viewingCredits = true;
         this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 0.0F));
      } else {
         if(this.dimension == 0 && var1 == 1) {
            this.b((Statistic)AchievementList.C);
            BlockPosition var2 = this.server.getWorldServer(var1).getDimensionSpawn();
            if(var2 != null) {
               this.playerConnection.a((double)var2.getX(), (double)var2.getY(), (double)var2.getZ(), 0.0F, 0.0F);
            }

            var1 = 1;
         } else {
            this.b((Statistic)AchievementList.y);
         }

         this.server.getPlayerList().changeDimension(this, var1);
         this.lastSentExp = -1;
         this.bM = -1.0F;
         this.bN = -1;
      }

   }

   public boolean a(EntityPlayer var1) {
      return var1.isSpectator()?this.C() == this:(this.isSpectator()?false:super.a((EntityPlayer)var1));
   }

   private void a(TileEntity var1) {
      if(var1 != null) {
         Packet var2 = var1.getUpdatePacket();
         if(var2 != null) {
            this.playerConnection.sendPacket(var2);
         }
      }

   }

   public void receive(Entity var1, int var2) {
      super.receive(var1, var2);
      this.activeContainer.b();
   }

   public EntityHuman.EnumBedResult a(BlockPosition var1) {
      EntityHuman.EnumBedResult var2 = super.a(var1);
      if(var2 == EntityHuman.EnumBedResult.OK) {
         PacketPlayOutBed var3 = new PacketPlayOutBed(this, var1);
         this.u().getTracker().a((Entity)this, (Packet)var3);
         this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
         this.playerConnection.sendPacket(var3);
      }

      return var2;
   }

   public void a(boolean var1, boolean var2, boolean var3) {
      if(this.isSleeping()) {
         this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 2));
      }

      super.a(var1, var2, var3);
      if(this.playerConnection != null) {
         this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      }

   }

   public void mount(Entity var1) {
      Entity var2 = this.vehicle;
      super.mount(var1);
      if(var1 != var2) {
         this.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this, this.vehicle));
         this.playerConnection.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      }

   }

   protected void a(double var1, boolean var3, Block var4, BlockPosition var5) {
   }

   public void a(double var1, boolean var3) {
      int var4 = MathHelper.floor(this.locX);
      int var5 = MathHelper.floor(this.locY - 0.20000000298023224D);
      int var6 = MathHelper.floor(this.locZ);
      BlockPosition var7 = new BlockPosition(var4, var5, var6);
      Block var8 = this.world.getType(var7).getBlock();
      if(var8.getMaterial() == Material.AIR) {
         Block var9 = this.world.getType(var7.down()).getBlock();
         if(var9 instanceof BlockFence || var9 instanceof BlockCobbleWall || var9 instanceof BlockFenceGate) {
            var7 = var7.down();
            var8 = this.world.getType(var7).getBlock();
         }
      }

      super.a(var1, var3, var8, var7);
   }

   public void openSign(TileEntitySign var1) {
      var1.a((EntityHuman)this);
      this.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(var1.getPosition()));
   }

   private void nextContainerCounter() {
      this.containerCounter = this.containerCounter % 100 + 1;
   }

   public void openTileEntity(ITileEntityContainer var1) {
      this.nextContainerCounter();
      this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, var1.getContainerName(), var1.getScoreboardDisplayName()));
      this.activeContainer = var1.createContainer(this.inventory, this);
      this.activeContainer.windowId = this.containerCounter;
      this.activeContainer.addSlotListener(this);
   }

   public void openContainer(IInventory var1) {
      if(this.activeContainer != this.defaultContainer) {
         this.closeInventory();
      }

      if(var1 instanceof ITileInventory) {
         ITileInventory var2 = (ITileInventory)var1;
         if(var2.r_() && !this.a((ChestLock)var2.i()) && !this.isSpectator()) {
            this.playerConnection.sendPacket(new PacketPlayOutChat(new ChatMessage("container.isLocked", new Object[]{var1.getScoreboardDisplayName()}), (byte)2));
            this.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect("random.door_close", this.locX, this.locY, this.locZ, 1.0F, 1.0F));
            return;
         }
      }

      this.nextContainerCounter();
      if(var1 instanceof ITileEntityContainer) {
         this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, ((ITileEntityContainer)var1).getContainerName(), var1.getScoreboardDisplayName(), var1.getSize()));
         this.activeContainer = ((ITileEntityContainer)var1).createContainer(this.inventory, this);
      } else {
         this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:container", var1.getScoreboardDisplayName(), var1.getSize()));
         this.activeContainer = new ContainerChest(this.inventory, var1, this);
      }

      this.activeContainer.windowId = this.containerCounter;
      this.activeContainer.addSlotListener(this);
   }

   public void openTrade(IMerchant var1) {
      this.nextContainerCounter();
      this.activeContainer = new ContainerMerchant(this.inventory, var1, this.world);
      this.activeContainer.windowId = this.containerCounter;
      this.activeContainer.addSlotListener(this);
      InventoryMerchant var2 = ((ContainerMerchant)this.activeContainer).e();
      IChatBaseComponent var3 = var1.getScoreboardDisplayName();
      this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "minecraft:villager", var3, var2.getSize()));
      MerchantRecipeList var4 = var1.getOffers(this);
      if(var4 != null) {
         PacketDataSerializer var5 = new PacketDataSerializer(Unpooled.buffer());
         var5.writeInt(this.containerCounter);
         var4.a(var5);
         this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", var5));
      }

   }

   public void openHorseInventory(EntityHorse var1, IInventory var2) {
      if(this.activeContainer != this.defaultContainer) {
         this.closeInventory();
      }

      this.nextContainerCounter();
      this.playerConnection.sendPacket(new PacketPlayOutOpenWindow(this.containerCounter, "EntityHorse", var2.getScoreboardDisplayName(), var2.getSize(), var1.getId()));
      this.activeContainer = new ContainerHorse(this.inventory, var2, var1, this);
      this.activeContainer.windowId = this.containerCounter;
      this.activeContainer.addSlotListener(this);
   }

   public void openBook(ItemStack var1) {
      Item var2 = var1.getItem();
      if(var2 == Items.WRITTEN_BOOK) {
         this.playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(Unpooled.buffer())));
      }

   }

   public void a(Container var1, int var2, ItemStack var3) {
      if(!(var1.getSlot(var2) instanceof SlotResult)) {
         if(!this.g) {
            this.playerConnection.sendPacket(new PacketPlayOutSetSlot(var1.windowId, var2, var3));
         }
      }
   }

   public void updateInventory(Container var1) {
      this.a(var1, var1.a());
   }

   public void a(Container var1, List<ItemStack> var2) {
      this.playerConnection.sendPacket(new PacketPlayOutWindowItems(var1.windowId, var2));
      this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
   }

   public void setContainerData(Container var1, int var2, int var3) {
      this.playerConnection.sendPacket(new PacketPlayOutWindowData(var1.windowId, var2, var3));
   }

   public void setContainerData(Container var1, IInventory var2) {
      for(int var3 = 0; var3 < var2.g(); ++var3) {
         this.playerConnection.sendPacket(new PacketPlayOutWindowData(var1.windowId, var3, var2.getProperty(var3)));
      }

   }

   public void closeInventory() {
      this.playerConnection.sendPacket(new PacketPlayOutCloseWindow(this.activeContainer.windowId));
      this.p();
   }

   public void broadcastCarriedItem() {
      if(!this.g) {
         this.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.inventory.getCarried()));
      }
   }

   public void p() {
      this.activeContainer.b((EntityHuman)this);
      this.activeContainer = this.defaultContainer;
   }

   public void a(float var1, float var2, boolean var3, boolean var4) {
      if(this.vehicle != null) {
         if(var1 >= -1.0F && var1 <= 1.0F) {
            this.aZ = var1;
         }

         if(var2 >= -1.0F && var2 <= 1.0F) {
            this.ba = var2;
         }

         this.aY = var3;
         this.setSneaking(var4);
      }

   }

   public void a(Statistic var1, int var2) {
      if(var1 != null) {
         this.bK.b(this, var1, var2);
         Iterator var3 = this.getScoreboard().getObjectivesForCriteria(var1.k()).iterator();

         while(var3.hasNext()) {
            ScoreboardObjective var4 = (ScoreboardObjective)var3.next();
            this.getScoreboard().getPlayerScoreForObjective(this.getName(), var4).addScore(var2);
         }

         if(this.bK.e()) {
            this.bK.a(this);
         }

      }
   }

   public void a(Statistic var1) {
      if(var1 != null) {
         this.bK.setStatistic(this, var1, 0);
         Iterator var2 = this.getScoreboard().getObjectivesForCriteria(var1.k()).iterator();

         while(var2.hasNext()) {
            ScoreboardObjective var3 = (ScoreboardObjective)var2.next();
            this.getScoreboard().getPlayerScoreForObjective(this.getName(), var3).setScore(0);
         }

         if(this.bK.e()) {
            this.bK.a(this);
         }

      }
   }

   public void q() {
      if(this.passenger != null) {
         this.passenger.mount(this);
      }

      if(this.sleeping) {
         this.a(true, false, false);
      }

   }

   public void triggerHealthUpdate() {
      this.bM = -1.0E8F;
   }

   public void b(IChatBaseComponent var1) {
      this.playerConnection.sendPacket(new PacketPlayOutChat(var1));
   }

   protected void s() {
      this.playerConnection.sendPacket(new PacketPlayOutEntityStatus(this, (byte)9));
      super.s();
   }

   public void a(ItemStack var1, int var2) {
      super.a(var1, var2);
      if(var1 != null && var1.getItem() != null && var1.getItem().e(var1) == EnumAnimation.EAT) {
         this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(this, 3));
      }

   }

   public void copyTo(EntityHuman var1, boolean var2) {
      super.copyTo(var1, var2);
      this.lastSentExp = -1;
      this.bM = -1.0F;
      this.bN = -1;
      this.removeQueue.addAll(((EntityPlayer)var1).removeQueue);
   }

   protected void a(MobEffect var1) {
      super.a((MobEffect)var1);
      this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), var1));
   }

   protected void a(MobEffect var1, boolean var2) {
      super.a(var1, var2);
      this.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.getId(), var1));
   }

   protected void b(MobEffect var1) {
      super.b((MobEffect)var1);
      this.playerConnection.sendPacket(new PacketPlayOutRemoveEntityEffect(this.getId(), var1));
   }

   public void enderTeleportTo(double var1, double var3, double var5) {
      this.playerConnection.a(var1, var3, var5, this.yaw, this.pitch);
   }

   public void b(Entity var1) {
      this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(var1, 4));
   }

   public void c(Entity var1) {
      this.u().getTracker().sendPacketToEntity(this, new PacketPlayOutAnimation(var1, 5));
   }

   public void updateAbilities() {
      if(this.playerConnection != null) {
         this.playerConnection.sendPacket(new PacketPlayOutAbilities(this.abilities));
         this.B();
      }
   }

   public WorldServer u() {
      return (WorldServer)this.world;
   }

   public void a(WorldSettings.EnumGamemode var1) {
      this.playerInteractManager.setGameMode(var1);
      this.playerConnection.sendPacket(new PacketPlayOutGameStateChange(3, (float)var1.getId()));
      if(var1 == WorldSettings.EnumGamemode.SPECTATOR) {
         this.mount((Entity)null);
      } else {
         this.setSpectatorTarget(this);
      }

      this.updateAbilities();
      this.bP();
   }

   public boolean isSpectator() {
      return this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR;
   }

   public void sendMessage(IChatBaseComponent var1) {
      this.playerConnection.sendPacket(new PacketPlayOutChat(var1));
   }

   public boolean a(int var1, String var2) {
      if("seed".equals(var2) && !this.server.ae()) {
         return true;
      } else if(!"tell".equals(var2) && !"help".equals(var2) && !"me".equals(var2) && !"trigger".equals(var2)) {
         if(this.server.getPlayerList().isOp(this.getProfile())) {
            OpListEntry var3 = (OpListEntry)this.server.getPlayerList().getOPs().get(this.getProfile());
            return var3 != null?var3.a() >= var1:this.server.p() >= var1;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public String w() {
      String var1 = this.playerConnection.networkManager.getSocketAddress().toString();
      var1 = var1.substring(var1.indexOf("/") + 1);
      var1 = var1.substring(0, var1.indexOf(":"));
      return var1;
   }

   public void a(PacketPlayInSettings var1) {
      this.locale = var1.a();
      this.bR = var1.c();
      this.bS = var1.d();
      this.getDataWatcher().watch(10, Byte.valueOf((byte)var1.e()));
   }

   public EntityHuman.EnumChatVisibility getChatFlags() {
      return this.bR;
   }

   public void setResourcePack(String var1, String var2) {
      this.playerConnection.sendPacket(new PacketPlayOutResourcePackSend(var1, var2));
   }

   public BlockPosition getChunkCoordinates() {
      return new BlockPosition(this.locX, this.locY + 0.5D, this.locZ);
   }

   public void resetIdleTimer() {
      this.bT = MinecraftServer.az();
   }

   public ServerStatisticManager getStatisticManager() {
      return this.bK;
   }

   public void d(Entity var1) {
      if(var1 instanceof EntityHuman) {
         this.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]{var1.getId()}));
      } else {
         this.removeQueue.add(Integer.valueOf(var1.getId()));
      }

   }

   protected void B() {
      if(this.isSpectator()) {
         this.bj();
         this.setInvisible(true);
      } else {
         super.B();
      }

      this.u().getTracker().a(this);
   }

   public Entity C() {
      return (Entity)(this.bU == null?this:this.bU);
   }

   public void setSpectatorTarget(Entity var1) {
      Entity var2 = this.C();
      this.bU = (Entity)(var1 == null?this:var1);
      if(var2 != this.bU) {
         this.playerConnection.sendPacket(new PacketPlayOutCamera(this.bU));
         this.enderTeleportTo(this.bU.locX, this.bU.locY, this.bU.locZ);
      }

   }

   public void attack(Entity var1) {
      if(this.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.SPECTATOR) {
         this.setSpectatorTarget(var1);
      } else {
         super.attack(var1);
      }

   }

   public long D() {
      return this.bT;
   }

   public IChatBaseComponent getPlayerListName() {
      return null;
   }
}
