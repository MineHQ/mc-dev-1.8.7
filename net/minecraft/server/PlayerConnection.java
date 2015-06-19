package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.server.AchievementList;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatComponentText;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.CommandBlockListenerAbstract;
import net.minecraft.server.Container;
import net.minecraft.server.ContainerAnvil;
import net.minecraft.server.ContainerBeacon;
import net.minecraft.server.ContainerMerchant;
import net.minecraft.server.CrashReport;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityExperienceOrb;
import net.minecraft.server.EntityHorse;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityMinecartCommandBlock;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumChatFormat;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.GameProfileBanEntry;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.IInventory;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.IntHashMap;
import net.minecraft.server.ItemBookAndQuill;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemWrittenBook;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketDataSerializer;
import net.minecraft.server.PacketListener;
import net.minecraft.server.PacketListenerPlayIn;
import net.minecraft.server.PacketPlayInAbilities;
import net.minecraft.server.PacketPlayInArmAnimation;
import net.minecraft.server.PacketPlayInBlockDig;
import net.minecraft.server.PacketPlayInBlockPlace;
import net.minecraft.server.PacketPlayInChat;
import net.minecraft.server.PacketPlayInClientCommand;
import net.minecraft.server.PacketPlayInCloseWindow;
import net.minecraft.server.PacketPlayInCustomPayload;
import net.minecraft.server.PacketPlayInEnchantItem;
import net.minecraft.server.PacketPlayInEntityAction;
import net.minecraft.server.PacketPlayInFlying;
import net.minecraft.server.PacketPlayInHeldItemSlot;
import net.minecraft.server.PacketPlayInKeepAlive;
import net.minecraft.server.PacketPlayInResourcePackStatus;
import net.minecraft.server.PacketPlayInSetCreativeSlot;
import net.minecraft.server.PacketPlayInSettings;
import net.minecraft.server.PacketPlayInSpectate;
import net.minecraft.server.PacketPlayInSteerVehicle;
import net.minecraft.server.PacketPlayInTabComplete;
import net.minecraft.server.PacketPlayInTransaction;
import net.minecraft.server.PacketPlayInUpdateSign;
import net.minecraft.server.PacketPlayInUseEntity;
import net.minecraft.server.PacketPlayInWindowClick;
import net.minecraft.server.PacketPlayOutBlockChange;
import net.minecraft.server.PacketPlayOutChat;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import net.minecraft.server.PacketPlayOutKeepAlive;
import net.minecraft.server.PacketPlayOutKickDisconnect;
import net.minecraft.server.PacketPlayOutPosition;
import net.minecraft.server.PacketPlayOutRespawn;
import net.minecraft.server.PacketPlayOutSetSlot;
import net.minecraft.server.PacketPlayOutTabComplete;
import net.minecraft.server.PacketPlayOutTransaction;
import net.minecraft.server.PlayerConnectionUtils;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.ReportedException;
import net.minecraft.server.SharedConstants;
import net.minecraft.server.Slot;
import net.minecraft.server.Statistic;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityCommand;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerConnection implements PacketListenerPlayIn, IUpdatePlayerListBox {
   private static final Logger c = LogManager.getLogger();
   public final NetworkManager networkManager;
   private final MinecraftServer minecraftServer;
   public EntityPlayer player;
   private int e;
   private int f;
   private int g;
   private boolean h;
   private int i;
   private long j;
   private long k;
   private int chatThrottle;
   private int m;
   private IntHashMap<Short> n = new IntHashMap();
   private double o;
   private double p;
   private double q;
   private boolean checkMovement = true;

   public PlayerConnection(MinecraftServer var1, NetworkManager var2, EntityPlayer var3) {
      this.minecraftServer = var1;
      this.networkManager = var2;
      var2.a((PacketListener)this);
      this.player = var3;
      var3.playerConnection = this;
   }

   public void c() {
      this.h = false;
      ++this.e;
      this.minecraftServer.methodProfiler.a("keepAlive");
      if((long)this.e - this.k > 40L) {
         this.k = (long)this.e;
         this.j = this.d();
         this.i = (int)this.j;
         this.sendPacket(new PacketPlayOutKeepAlive(this.i));
      }

      this.minecraftServer.methodProfiler.b();
      if(this.chatThrottle > 0) {
         --this.chatThrottle;
      }

      if(this.m > 0) {
         --this.m;
      }

      if(this.player.D() > 0L && this.minecraftServer.getIdleTimeout() > 0 && MinecraftServer.az() - this.player.D() > (long)(this.minecraftServer.getIdleTimeout() * 1000 * 60)) {
         this.disconnect("You have been idle for too long!");
      }

   }

   public NetworkManager a() {
      return this.networkManager;
   }

   public void disconnect(String var1) {
      final ChatComponentText var2 = new ChatComponentText(var1);
      this.networkManager.a(new PacketPlayOutKickDisconnect(var2), new GenericFutureListener() {
         public void operationComplete(Future<? super Void> var1) throws Exception {
            PlayerConnection.this.networkManager.close(var2);
         }
      }, new GenericFutureListener[0]);
      this.networkManager.k();
      Futures.getUnchecked(this.minecraftServer.postToMainThread(new Runnable() {
         public void run() {
            PlayerConnection.this.networkManager.l();
         }
      }));
   }

   public void a(PacketPlayInSteerVehicle var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.a(var1.a(), var1.b(), var1.c(), var1.d());
   }

   private boolean b(PacketPlayInFlying var1) {
      return !Doubles.isFinite(var1.a()) || !Doubles.isFinite(var1.b()) || !Doubles.isFinite(var1.c()) || !Floats.isFinite(var1.e()) || !Floats.isFinite(var1.d());
   }

   public void a(PacketPlayInFlying var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      if(this.b(var1)) {
         this.disconnect("Invalid move packet received");
      } else {
         WorldServer var2 = this.minecraftServer.getWorldServer(this.player.dimension);
         this.h = true;
         if(!this.player.viewingCredits) {
            double var3 = this.player.locX;
            double var5 = this.player.locY;
            double var7 = this.player.locZ;
            double var9 = 0.0D;
            double var11 = var1.a() - this.o;
            double var13 = var1.b() - this.p;
            double var15 = var1.c() - this.q;
            if(var1.g()) {
               var9 = var11 * var11 + var13 * var13 + var15 * var15;
               if(!this.checkMovement && var9 < 0.25D) {
                  this.checkMovement = true;
               }
            }

            if(this.checkMovement) {
               this.f = this.e;
               double var19;
               double var21;
               double var23;
               if(this.player.vehicle != null) {
                  float var44 = this.player.yaw;
                  float var18 = this.player.pitch;
                  this.player.vehicle.al();
                  var19 = this.player.locX;
                  var21 = this.player.locY;
                  var23 = this.player.locZ;
                  if(var1.h()) {
                     var44 = var1.d();
                     var18 = var1.e();
                  }

                  this.player.onGround = var1.f();
                  this.player.l();
                  this.player.setLocation(var19, var21, var23, var44, var18);
                  if(this.player.vehicle != null) {
                     this.player.vehicle.al();
                  }

                  this.minecraftServer.getPlayerList().d(this.player);
                  if(this.player.vehicle != null) {
                     if(var9 > 4.0D) {
                        Entity var45 = this.player.vehicle;
                        this.player.playerConnection.sendPacket(new PacketPlayOutEntityTeleport(var45));
                        this.a(this.player.locX, this.player.locY, this.player.locZ, this.player.yaw, this.player.pitch);
                     }

                     this.player.vehicle.ai = true;
                  }

                  if(this.checkMovement) {
                     this.o = this.player.locX;
                     this.p = this.player.locY;
                     this.q = this.player.locZ;
                  }

                  var2.g(this.player);
                  return;
               }

               if(this.player.isSleeping()) {
                  this.player.l();
                  this.player.setLocation(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
                  var2.g(this.player);
                  return;
               }

               double var17 = this.player.locY;
               this.o = this.player.locX;
               this.p = this.player.locY;
               this.q = this.player.locZ;
               var19 = this.player.locX;
               var21 = this.player.locY;
               var23 = this.player.locZ;
               float var25 = this.player.yaw;
               float var26 = this.player.pitch;
               if(var1.g() && var1.b() == -999.0D) {
                  var1.a(false);
               }

               if(var1.g()) {
                  var19 = var1.a();
                  var21 = var1.b();
                  var23 = var1.c();
                  if(Math.abs(var1.a()) > 3.0E7D || Math.abs(var1.c()) > 3.0E7D) {
                     this.disconnect("Illegal position");
                     return;
                  }
               }

               if(var1.h()) {
                  var25 = var1.d();
                  var26 = var1.e();
               }

               this.player.l();
               this.player.setLocation(this.o, this.p, this.q, var25, var26);
               if(!this.checkMovement) {
                  return;
               }

               double var27 = var19 - this.player.locX;
               double var29 = var21 - this.player.locY;
               double var31 = var23 - this.player.locZ;
               double var33 = this.player.motX * this.player.motX + this.player.motY * this.player.motY + this.player.motZ * this.player.motZ;
               double var35 = var27 * var27 + var29 * var29 + var31 * var31;
               if(var35 - var33 > 100.0D && (!this.minecraftServer.T() || !this.minecraftServer.S().equals(this.player.getName()))) {
                  c.warn(this.player.getName() + " moved too quickly! " + var27 + "," + var29 + "," + var31 + " (" + var27 + ", " + var29 + ", " + var31 + ")");
                  this.a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
                  return;
               }

               float var37 = 0.0625F;
               boolean var38 = var2.getCubes(this.player, this.player.getBoundingBox().shrink((double)var37, (double)var37, (double)var37)).isEmpty();
               if(this.player.onGround && !var1.f() && var29 > 0.0D) {
                  this.player.bF();
               }

               this.player.move(var27, var29, var31);
               this.player.onGround = var1.f();
               double var39 = var29;
               var27 = var19 - this.player.locX;
               var29 = var21 - this.player.locY;
               if(var29 > -0.5D || var29 < 0.5D) {
                  var29 = 0.0D;
               }

               var31 = var23 - this.player.locZ;
               var35 = var27 * var27 + var29 * var29 + var31 * var31;
               boolean var41 = false;
               if(var35 > 0.0625D && !this.player.isSleeping() && !this.player.playerInteractManager.isCreative()) {
                  var41 = true;
                  c.warn(this.player.getName() + " moved wrongly!");
               }

               this.player.setLocation(var19, var21, var23, var25, var26);
               this.player.checkMovement(this.player.locX - var3, this.player.locY - var5, this.player.locZ - var7);
               if(!this.player.noclip) {
                  boolean var42 = var2.getCubes(this.player, this.player.getBoundingBox().shrink((double)var37, (double)var37, (double)var37)).isEmpty();
                  if(var38 && (var41 || !var42) && !this.player.isSleeping()) {
                     this.a(this.o, this.p, this.q, var25, var26);
                     return;
                  }
               }

               AxisAlignedBB var43 = this.player.getBoundingBox().grow((double)var37, (double)var37, (double)var37).a(0.0D, -0.55D, 0.0D);
               if(!this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly && !var2.c(var43)) {
                  if(var39 >= -0.03125D) {
                     ++this.g;
                     if(this.g > 80) {
                        c.warn(this.player.getName() + " was kicked for floating too long!");
                        this.disconnect("Flying is not enabled on this server");
                        return;
                     }
                  }
               } else {
                  this.g = 0;
               }

               this.player.onGround = var1.f();
               this.minecraftServer.getPlayerList().d(this.player);
               this.player.a(this.player.locY - var17, var1.f());
            } else if(this.e - this.f > 20) {
               this.a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
            }

         }
      }
   }

   public void a(double var1, double var3, double var5, float var7, float var8) {
      this.a(var1, var3, var5, var7, var8, Collections.emptySet());
   }

   public void a(double var1, double var3, double var5, float var7, float var8, Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> var9) {
      this.checkMovement = false;
      this.o = var1;
      this.p = var3;
      this.q = var5;
      if(var9.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X)) {
         this.o += this.player.locX;
      }

      if(var9.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y)) {
         this.p += this.player.locY;
      }

      if(var9.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z)) {
         this.q += this.player.locZ;
      }

      float var10 = var7;
      float var11 = var8;
      if(var9.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT)) {
         var10 = var7 + this.player.yaw;
      }

      if(var9.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT)) {
         var11 = var8 + this.player.pitch;
      }

      this.player.setLocation(this.o, this.p, this.q, var10, var11);
      this.player.playerConnection.sendPacket(new PacketPlayOutPosition(var1, var3, var5, var7, var8, var9));
   }

   public void a(PacketPlayInBlockDig var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      WorldServer var2 = this.minecraftServer.getWorldServer(this.player.dimension);
      BlockPosition var3 = var1.a();
      this.player.resetIdleTimer();
      switch(PlayerConnection.SyntheticClass_1.a[var1.c().ordinal()]) {
      case 1:
         if(!this.player.isSpectator()) {
            this.player.a(false);
         }

         return;
      case 2:
         if(!this.player.isSpectator()) {
            this.player.a(true);
         }

         return;
      case 3:
         this.player.bU();
         return;
      case 4:
      case 5:
      case 6:
         double var4 = this.player.locX - ((double)var3.getX() + 0.5D);
         double var6 = this.player.locY - ((double)var3.getY() + 0.5D) + 1.5D;
         double var8 = this.player.locZ - ((double)var3.getZ() + 0.5D);
         double var10 = var4 * var4 + var6 * var6 + var8 * var8;
         if(var10 > 36.0D) {
            return;
         } else if(var3.getY() >= this.minecraftServer.getMaxBuildHeight()) {
            return;
         } else {
            if(var1.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
               if(!this.minecraftServer.a(var2, var3, this.player) && var2.getWorldBorder().a(var3)) {
                  this.player.playerInteractManager.a(var3, var1.b());
               } else {
                  this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(var2, var3));
               }
            } else {
               if(var1.c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                  this.player.playerInteractManager.a(var3);
               } else if(var1.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                  this.player.playerInteractManager.e();
               }

               if(var2.getType(var3).getBlock().getMaterial() != Material.AIR) {
                  this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(var2, var3));
               }
            }

            return;
         }
      default:
         throw new IllegalArgumentException("Invalid player action");
      }
   }

   public void a(PacketPlayInBlockPlace var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      WorldServer var2 = this.minecraftServer.getWorldServer(this.player.dimension);
      ItemStack var3 = this.player.inventory.getItemInHand();
      boolean var4 = false;
      BlockPosition var5 = var1.a();
      EnumDirection var6 = EnumDirection.fromType1(var1.getFace());
      this.player.resetIdleTimer();
      if(var1.getFace() == 255) {
         if(var3 == null) {
            return;
         }

         this.player.playerInteractManager.useItem(this.player, var2, var3);
      } else if(var5.getY() >= this.minecraftServer.getMaxBuildHeight() - 1 && (var6 == EnumDirection.UP || var5.getY() >= this.minecraftServer.getMaxBuildHeight())) {
         ChatMessage var7 = new ChatMessage("build.tooHigh", new Object[]{Integer.valueOf(this.minecraftServer.getMaxBuildHeight())});
         var7.getChatModifier().setColor(EnumChatFormat.RED);
         this.player.playerConnection.sendPacket(new PacketPlayOutChat(var7));
         var4 = true;
      } else {
         if(this.checkMovement && this.player.e((double)var5.getX() + 0.5D, (double)var5.getY() + 0.5D, (double)var5.getZ() + 0.5D) < 64.0D && !this.minecraftServer.a(var2, var5, this.player) && var2.getWorldBorder().a(var5)) {
            this.player.playerInteractManager.interact(this.player, var2, var3, var5, var6, var1.d(), var1.e(), var1.f());
         }

         var4 = true;
      }

      if(var4) {
         this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(var2, var5));
         this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(var2, var5.shift(var6)));
      }

      var3 = this.player.inventory.getItemInHand();
      if(var3 != null && var3.count == 0) {
         this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
         var3 = null;
      }

      if(var3 == null || var3.l() == 0) {
         this.player.g = true;
         this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
         Slot var8 = this.player.activeContainer.getSlot(this.player.inventory, this.player.inventory.itemInHandIndex);
         this.player.activeContainer.b();
         this.player.g = false;
         if(!ItemStack.matches(this.player.inventory.getItemInHand(), var1.getItemStack())) {
            this.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, var8.rawSlotIndex, this.player.inventory.getItemInHand()));
         }
      }

   }

   public void a(PacketPlayInSpectate var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      if(this.player.isSpectator()) {
         Entity var2 = null;
         WorldServer[] var3 = this.minecraftServer.worldServer;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            WorldServer var6 = var3[var5];
            if(var6 != null) {
               var2 = var1.a(var6);
               if(var2 != null) {
                  break;
               }
            }
         }

         if(var2 != null) {
            this.player.setSpectatorTarget(this.player);
            this.player.mount((Entity)null);
            if(var2.world != this.player.world) {
               WorldServer var7 = this.player.u();
               WorldServer var8 = (WorldServer)var2.world;
               this.player.dimension = var2.dimension;
               this.sendPacket(new PacketPlayOutRespawn(this.player.dimension, var7.getDifficulty(), var7.getWorldData().getType(), this.player.playerInteractManager.getGameMode()));
               var7.removeEntity(this.player);
               this.player.dead = false;
               this.player.setPositionRotation(var2.locX, var2.locY, var2.locZ, var2.yaw, var2.pitch);
               if(this.player.isAlive()) {
                  var7.entityJoinedWorld(this.player, false);
                  var8.addEntity(this.player);
                  var8.entityJoinedWorld(this.player, false);
               }

               this.player.spawnIn(var8);
               this.minecraftServer.getPlayerList().a(this.player, var7);
               this.player.enderTeleportTo(var2.locX, var2.locY, var2.locZ);
               this.player.playerInteractManager.a(var8);
               this.minecraftServer.getPlayerList().b(this.player, var8);
               this.minecraftServer.getPlayerList().updateClient(this.player);
            } else {
               this.player.enderTeleportTo(var2.locX, var2.locY, var2.locZ);
            }
         }
      }

   }

   public void a(PacketPlayInResourcePackStatus var1) {
   }

   public void a(IChatBaseComponent var1) {
      c.info(this.player.getName() + " lost connection: " + var1);
      this.minecraftServer.aH();
      ChatMessage var2 = new ChatMessage("multiplayer.player.left", new Object[]{this.player.getScoreboardDisplayName()});
      var2.getChatModifier().setColor(EnumChatFormat.YELLOW);
      this.minecraftServer.getPlayerList().sendMessage(var2);
      this.player.q();
      this.minecraftServer.getPlayerList().disconnect(this.player);
      if(this.minecraftServer.T() && this.player.getName().equals(this.minecraftServer.S())) {
         c.info("Stopping singleplayer server as player logged out");
         this.minecraftServer.safeShutdown();
      }

   }

   public void sendPacket(final Packet var1) {
      if(var1 instanceof PacketPlayOutChat) {
         PacketPlayOutChat var2 = (PacketPlayOutChat)var1;
         EntityHuman.EnumChatVisibility var3 = this.player.getChatFlags();
         if(var3 == EntityHuman.EnumChatVisibility.HIDDEN) {
            return;
         }

         if(var3 == EntityHuman.EnumChatVisibility.SYSTEM && !var2.b()) {
            return;
         }
      }

      try {
         this.networkManager.handle(var1);
      } catch (Throwable var5) {
         CrashReport var6 = CrashReport.a(var5, "Sending packet");
         CrashReportSystemDetails var4 = var6.a("Packet being sent");
         var4.a("Packet class", new Callable() {
            public String a() throws Exception {
               return var1.getClass().getCanonicalName();
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
         throw new ReportedException(var6);
      }
   }

   public void a(PacketPlayInHeldItemSlot var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      if(var1.a() >= 0 && var1.a() < PlayerInventory.getHotbarSize()) {
         this.player.inventory.itemInHandIndex = var1.a();
         this.player.resetIdleTimer();
      } else {
         c.warn(this.player.getName() + " tried to set an invalid carried item");
      }
   }

   public void a(PacketPlayInChat var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      if(this.player.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN) {
         ChatMessage var4 = new ChatMessage("chat.cannotSend", new Object[0]);
         var4.getChatModifier().setColor(EnumChatFormat.RED);
         this.sendPacket(new PacketPlayOutChat(var4));
      } else {
         this.player.resetIdleTimer();
         String var2 = var1.a();
         var2 = StringUtils.normalizeSpace(var2);

         for(int var3 = 0; var3 < var2.length(); ++var3) {
            if(!SharedConstants.isAllowedChatCharacter(var2.charAt(var3))) {
               this.disconnect("Illegal characters in chat");
               return;
            }
         }

         if(var2.startsWith("/")) {
            this.handleCommand(var2);
         } else {
            ChatMessage var5 = new ChatMessage("chat.type.text", new Object[]{this.player.getScoreboardDisplayName(), var2});
            this.minecraftServer.getPlayerList().sendMessage(var5, false);
         }

         this.chatThrottle += 20;
         if(this.chatThrottle > 200 && !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) {
            this.disconnect("disconnect.spam");
         }

      }
   }

   private void handleCommand(String var1) {
      this.minecraftServer.getCommandHandler().a(this.player, var1);
   }

   public void a(PacketPlayInArmAnimation var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      this.player.bw();
   }

   public void a(PacketPlayInEntityAction var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      switch(PlayerConnection.SyntheticClass_1.b[var1.b().ordinal()]) {
      case 1:
         this.player.setSneaking(true);
         break;
      case 2:
         this.player.setSneaking(false);
         break;
      case 3:
         this.player.setSprinting(true);
         break;
      case 4:
         this.player.setSprinting(false);
         break;
      case 5:
         this.player.a(false, true, true);
         this.checkMovement = false;
         break;
      case 6:
         if(this.player.vehicle instanceof EntityHorse) {
            ((EntityHorse)this.player.vehicle).v(var1.c());
         }
         break;
      case 7:
         if(this.player.vehicle instanceof EntityHorse) {
            ((EntityHorse)this.player.vehicle).g(this.player);
         }
         break;
      default:
         throw new IllegalArgumentException("Invalid client command!");
      }

   }

   public void a(PacketPlayInUseEntity var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      WorldServer var2 = this.minecraftServer.getWorldServer(this.player.dimension);
      Entity var3 = var1.a((World)var2);
      this.player.resetIdleTimer();
      if(var3 != null) {
         boolean var4 = this.player.hasLineOfSight(var3);
         double var5 = 36.0D;
         if(!var4) {
            var5 = 9.0D;
         }

         if(this.player.h(var3) < var5) {
            if(var1.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
               this.player.u(var3);
            } else if(var1.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
               var3.a((EntityHuman)this.player, (Vec3D)var1.b());
            } else if(var1.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
               if(var3 instanceof EntityItem || var3 instanceof EntityExperienceOrb || var3 instanceof EntityArrow || var3 == this.player) {
                  this.disconnect("Attempting to attack an invalid entity");
                  this.minecraftServer.warning("Player " + this.player.getName() + " tried to attack an invalid entity");
                  return;
               }

               this.player.attack(var3);
            }
         }
      }

   }

   public void a(PacketPlayInClientCommand var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      PacketPlayInClientCommand.EnumClientCommand var2 = var1.a();
      switch(PlayerConnection.SyntheticClass_1.c[var2.ordinal()]) {
      case 1:
         if(this.player.viewingCredits) {
            this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, true);
         } else if(this.player.u().getWorldData().isHardcore()) {
            if(this.minecraftServer.T() && this.player.getName().equals(this.minecraftServer.S())) {
               this.player.playerConnection.disconnect("You have died. Game over, man, it\'s game over!");
               this.minecraftServer.aa();
            } else {
               GameProfileBanEntry var3 = new GameProfileBanEntry(this.player.getProfile(), (Date)null, "(You just lost the game)", (Date)null, "Death in Hardcore");
               this.minecraftServer.getPlayerList().getProfileBans().add(var3);
               this.player.playerConnection.disconnect("You have died. Game over, man, it\'s game over!");
            }
         } else {
            if(this.player.getHealth() > 0.0F) {
               return;
            }

            this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, false);
         }
         break;
      case 2:
         this.player.getStatisticManager().a(this.player);
         break;
      case 3:
         this.player.b((Statistic)AchievementList.f);
      }

   }

   public void a(PacketPlayInCloseWindow var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.p();
   }

   public void a(PacketPlayInWindowClick var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      if(this.player.activeContainer.windowId == var1.a() && this.player.activeContainer.c(this.player)) {
         if(this.player.isSpectator()) {
            ArrayList var2 = Lists.newArrayList();

            for(int var3 = 0; var3 < this.player.activeContainer.c.size(); ++var3) {
               var2.add(((Slot)this.player.activeContainer.c.get(var3)).getItem());
            }

            this.player.a(this.player.activeContainer, var2);
         } else {
            ItemStack var5 = this.player.activeContainer.clickItem(var1.b(), var1.c(), var1.f(), this.player);
            if(ItemStack.matches(var1.e(), var5)) {
               this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(var1.a(), var1.d(), true));
               this.player.g = true;
               this.player.activeContainer.b();
               this.player.broadcastCarriedItem();
               this.player.g = false;
            } else {
               this.n.a(this.player.activeContainer.windowId, Short.valueOf(var1.d()));
               this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(var1.a(), var1.d(), false));
               this.player.activeContainer.a(this.player, false);
               ArrayList var6 = Lists.newArrayList();

               for(int var4 = 0; var4 < this.player.activeContainer.c.size(); ++var4) {
                  var6.add(((Slot)this.player.activeContainer.c.get(var4)).getItem());
               }

               this.player.a(this.player.activeContainer, var6);
            }
         }
      }

   }

   public void a(PacketPlayInEnchantItem var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      if(this.player.activeContainer.windowId == var1.a() && this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
         this.player.activeContainer.a(this.player, var1.b());
         this.player.activeContainer.b();
      }

   }

   public void a(PacketPlayInSetCreativeSlot var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      if(this.player.playerInteractManager.isCreative()) {
         boolean var2 = var1.a() < 0;
         ItemStack var3 = var1.getItemStack();
         if(var3 != null && var3.hasTag() && var3.getTag().hasKeyOfType("BlockEntityTag", 10)) {
            NBTTagCompound var4 = var3.getTag().getCompound("BlockEntityTag");
            if(var4.hasKey("x") && var4.hasKey("y") && var4.hasKey("z")) {
               BlockPosition var5 = new BlockPosition(var4.getInt("x"), var4.getInt("y"), var4.getInt("z"));
               TileEntity var6 = this.player.world.getTileEntity(var5);
               if(var6 != null) {
                  NBTTagCompound var7 = new NBTTagCompound();
                  var6.b(var7);
                  var7.remove("x");
                  var7.remove("y");
                  var7.remove("z");
                  var3.a((String)"BlockEntityTag", (NBTBase)var7);
               }
            }
         }

         boolean var8 = var1.a() >= 1 && var1.a() < 36 + PlayerInventory.getHotbarSize();
         boolean var9 = var3 == null || var3.getItem() != null;
         boolean var10 = var3 == null || var3.getData() >= 0 && var3.count <= 64 && var3.count > 0;
         if(var8 && var9 && var10) {
            if(var3 == null) {
               this.player.defaultContainer.setItem(var1.a(), (ItemStack)null);
            } else {
               this.player.defaultContainer.setItem(var1.a(), var3);
            }

            this.player.defaultContainer.a(this.player, true);
         } else if(var2 && var9 && var10 && this.m < 200) {
            this.m += 20;
            EntityItem var11 = this.player.drop(var3, true);
            if(var11 != null) {
               var11.j();
            }
         }
      }

   }

   public void a(PacketPlayInTransaction var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      Short var2 = (Short)this.n.get(this.player.activeContainer.windowId);
      if(var2 != null && var1.b() == var2.shortValue() && this.player.activeContainer.windowId == var1.a() && !this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
         this.player.activeContainer.a(this.player, true);
      }

   }

   public void a(PacketPlayInUpdateSign var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.resetIdleTimer();
      WorldServer var2 = this.minecraftServer.getWorldServer(this.player.dimension);
      BlockPosition var3 = var1.a();
      if(var2.isLoaded(var3)) {
         TileEntity var4 = var2.getTileEntity(var3);
         if(!(var4 instanceof TileEntitySign)) {
            return;
         }

         TileEntitySign var5 = (TileEntitySign)var4;
         if(!var5.b() || var5.c() != this.player) {
            this.minecraftServer.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
            return;
         }

         IChatBaseComponent[] var6 = var1.b();

         for(int var7 = 0; var7 < var6.length; ++var7) {
            var5.lines[var7] = new ChatComponentText(EnumChatFormat.a(var6[var7].c()));
         }

         var5.update();
         var2.notify(var3);
      }

   }

   public void a(PacketPlayInKeepAlive var1) {
      if(var1.a() == this.i) {
         int var2 = (int)(this.d() - this.j);
         this.player.ping = (this.player.ping * 3 + var2) / 4;
      }

   }

   private long d() {
      return System.nanoTime() / 1000000L;
   }

   public void a(PacketPlayInAbilities var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.abilities.isFlying = var1.isFlying() && this.player.abilities.canFly;
   }

   public void a(PacketPlayInTabComplete var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      ArrayList var2 = Lists.newArrayList();
      Iterator var3 = this.minecraftServer.tabCompleteCommand(this.player, var1.a(), var1.b()).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(var4);
      }

      this.player.playerConnection.sendPacket(new PacketPlayOutTabComplete((String[])var2.toArray(new String[var2.size()])));
   }

   public void a(PacketPlayInSettings var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      this.player.a(var1);
   }

   public void a(PacketPlayInCustomPayload var1) {
      PlayerConnectionUtils.ensureMainThread(var1, this, this.player.u());
      PacketDataSerializer var2;
      ItemStack var3;
      ItemStack var4;
      if("MC|BEdit".equals(var1.a())) {
         var2 = new PacketDataSerializer(Unpooled.wrappedBuffer((ByteBuf)var1.b()));

         try {
            var3 = var2.i();
            if(var3 == null) {
               return;
            }

            if(!ItemBookAndQuill.b(var3.getTag())) {
               throw new IOException("Invalid book tag!");
            }

            var4 = this.player.inventory.getItemInHand();
            if(var4 == null) {
               return;
            }

            if(var3.getItem() == Items.WRITABLE_BOOK && var3.getItem() == var4.getItem()) {
               var4.a((String)"pages", (NBTBase)var3.getTag().getList("pages", 8));
            }
         } catch (Exception var38) {
            c.error((String)"Couldn\'t handle book info", (Throwable)var38);
         } finally {
            var2.release();
         }
      } else if("MC|BSign".equals(var1.a())) {
         var2 = new PacketDataSerializer(Unpooled.wrappedBuffer((ByteBuf)var1.b()));

         try {
            var3 = var2.i();
            if(var3 == null) {
               return;
            }

            if(!ItemWrittenBook.b(var3.getTag())) {
               throw new IOException("Invalid book tag!");
            }

            var4 = this.player.inventory.getItemInHand();
            if(var4 == null) {
               return;
            }

            if(var3.getItem() == Items.WRITTEN_BOOK && var4.getItem() == Items.WRITABLE_BOOK) {
               var4.a((String)"author", (NBTBase)(new NBTTagString(this.player.getName())));
               var4.a((String)"title", (NBTBase)(new NBTTagString(var3.getTag().getString("title"))));
               var4.a((String)"pages", (NBTBase)var3.getTag().getList("pages", 8));
               var4.setItem(Items.WRITTEN_BOOK);
            }
         } catch (Exception var36) {
            c.error((String)"Couldn\'t sign book", (Throwable)var36);
         } finally {
            var2.release();
         }
      } else if("MC|TrSel".equals(var1.a())) {
         try {
            int var40 = var1.b().readInt();
            Container var41 = this.player.activeContainer;
            if(var41 instanceof ContainerMerchant) {
               ((ContainerMerchant)var41).d(var40);
            }
         } catch (Exception var35) {
            c.error((String)"Couldn\'t select trade", (Throwable)var35);
         }
      } else if("MC|AdvCdm".equals(var1.a())) {
         if(!this.minecraftServer.getEnableCommandBlock()) {
            this.player.sendMessage(new ChatMessage("advMode.notEnabled", new Object[0]));
         } else if(this.player.a(2, "") && this.player.abilities.canInstantlyBuild) {
            var2 = var1.b();

            try {
               byte var43 = var2.readByte();
               CommandBlockListenerAbstract var46 = null;
               if(var43 == 0) {
                  TileEntity var5 = this.player.world.getTileEntity(new BlockPosition(var2.readInt(), var2.readInt(), var2.readInt()));
                  if(var5 instanceof TileEntityCommand) {
                     var46 = ((TileEntityCommand)var5).getCommandBlock();
                  }
               } else if(var43 == 1) {
                  Entity var47 = this.player.world.a(var2.readInt());
                  if(var47 instanceof EntityMinecartCommandBlock) {
                     var46 = ((EntityMinecartCommandBlock)var47).getCommandBlock();
                  }
               }

               String var48 = var2.c(var2.readableBytes());
               boolean var6 = var2.readBoolean();
               if(var46 != null) {
                  var46.setCommand(var48);
                  var46.a(var6);
                  if(!var6) {
                     var46.b((IChatBaseComponent)null);
                  }

                  var46.h();
                  this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[]{var48}));
               }
            } catch (Exception var33) {
               c.error((String)"Couldn\'t set command block", (Throwable)var33);
            } finally {
               var2.release();
            }
         } else {
            this.player.sendMessage(new ChatMessage("advMode.notAllowed", new Object[0]));
         }
      } else if("MC|Beacon".equals(var1.a())) {
         if(this.player.activeContainer instanceof ContainerBeacon) {
            try {
               var2 = var1.b();
               int var44 = var2.readInt();
               int var49 = var2.readInt();
               ContainerBeacon var50 = (ContainerBeacon)this.player.activeContainer;
               Slot var51 = var50.getSlot(0);
               if(var51.hasItem()) {
                  var51.a(1);
                  IInventory var7 = var50.e();
                  var7.b(1, var44);
                  var7.b(2, var49);
                  var7.update();
               }
            } catch (Exception var32) {
               c.error((String)"Couldn\'t set beacon", (Throwable)var32);
            }
         }
      } else if("MC|ItemName".equals(var1.a()) && this.player.activeContainer instanceof ContainerAnvil) {
         ContainerAnvil var42 = (ContainerAnvil)this.player.activeContainer;
         if(var1.b() != null && var1.b().readableBytes() >= 1) {
            String var45 = SharedConstants.a(var1.b().c(32767));
            if(var45.length() <= 30) {
               var42.a(var45);
            }
         } else {
            var42.a("");
         }
      }

   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
      // $FF: synthetic field
      static final int[] a;
      // $FF: synthetic field
      static final int[] b;
      // $FF: synthetic field
      static final int[] c = new int[PacketPlayInClientCommand.EnumClientCommand.values().length];

      static {
         try {
            c[PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN.ordinal()] = 1;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            c[PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS.ordinal()] = 2;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            c[PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT.ordinal()] = 3;
         } catch (NoSuchFieldError var14) {
            ;
         }

         b = new int[PacketPlayInEntityAction.EnumPlayerAction.values().length];

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING.ordinal()] = 1;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING.ordinal()] = 2;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING.ordinal()] = 3;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING.ordinal()] = 4;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SLEEPING.ordinal()] = 5;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.RIDING_JUMP.ordinal()] = 6;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            b[PacketPlayInEntityAction.EnumPlayerAction.OPEN_INVENTORY.ordinal()] = 7;
         } catch (NoSuchFieldError var7) {
            ;
         }

         a = new int[PacketPlayInBlockDig.EnumPlayerDigType.values().length];

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ALL_ITEMS.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            a[PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
