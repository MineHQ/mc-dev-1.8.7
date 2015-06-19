package net.minecraft.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.server.EnumProtocolDirection;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketHandshakingInSetProtocol;
import net.minecraft.server.PacketLoginInEncryptionBegin;
import net.minecraft.server.PacketLoginInStart;
import net.minecraft.server.PacketLoginOutDisconnect;
import net.minecraft.server.PacketLoginOutEncryptionBegin;
import net.minecraft.server.PacketLoginOutSetCompression;
import net.minecraft.server.PacketLoginOutSuccess;
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
import net.minecraft.server.PacketPlayOutAbilities;
import net.minecraft.server.PacketPlayOutAnimation;
import net.minecraft.server.PacketPlayOutAttachEntity;
import net.minecraft.server.PacketPlayOutBed;
import net.minecraft.server.PacketPlayOutBlockAction;
import net.minecraft.server.PacketPlayOutBlockBreakAnimation;
import net.minecraft.server.PacketPlayOutBlockChange;
import net.minecraft.server.PacketPlayOutCamera;
import net.minecraft.server.PacketPlayOutChat;
import net.minecraft.server.PacketPlayOutCloseWindow;
import net.minecraft.server.PacketPlayOutCollect;
import net.minecraft.server.PacketPlayOutCombatEvent;
import net.minecraft.server.PacketPlayOutCustomPayload;
import net.minecraft.server.PacketPlayOutEntity;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutEntityEffect;
import net.minecraft.server.PacketPlayOutEntityEquipment;
import net.minecraft.server.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.PacketPlayOutEntityMetadata;
import net.minecraft.server.PacketPlayOutEntityStatus;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import net.minecraft.server.PacketPlayOutEntityVelocity;
import net.minecraft.server.PacketPlayOutExperience;
import net.minecraft.server.PacketPlayOutExplosion;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.PacketPlayOutHeldItemSlot;
import net.minecraft.server.PacketPlayOutKeepAlive;
import net.minecraft.server.PacketPlayOutKickDisconnect;
import net.minecraft.server.PacketPlayOutLogin;
import net.minecraft.server.PacketPlayOutMap;
import net.minecraft.server.PacketPlayOutMapChunk;
import net.minecraft.server.PacketPlayOutMapChunkBulk;
import net.minecraft.server.PacketPlayOutMultiBlockChange;
import net.minecraft.server.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.PacketPlayOutOpenSignEditor;
import net.minecraft.server.PacketPlayOutOpenWindow;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import net.minecraft.server.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.PacketPlayOutPosition;
import net.minecraft.server.PacketPlayOutRemoveEntityEffect;
import net.minecraft.server.PacketPlayOutResourcePackSend;
import net.minecraft.server.PacketPlayOutRespawn;
import net.minecraft.server.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.PacketPlayOutScoreboardObjective;
import net.minecraft.server.PacketPlayOutScoreboardScore;
import net.minecraft.server.PacketPlayOutScoreboardTeam;
import net.minecraft.server.PacketPlayOutServerDifficulty;
import net.minecraft.server.PacketPlayOutSetCompression;
import net.minecraft.server.PacketPlayOutSetSlot;
import net.minecraft.server.PacketPlayOutSpawnEntity;
import net.minecraft.server.PacketPlayOutSpawnEntityExperienceOrb;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.PacketPlayOutSpawnEntityPainting;
import net.minecraft.server.PacketPlayOutSpawnEntityWeather;
import net.minecraft.server.PacketPlayOutSpawnPosition;
import net.minecraft.server.PacketPlayOutStatistic;
import net.minecraft.server.PacketPlayOutTabComplete;
import net.minecraft.server.PacketPlayOutTileEntityData;
import net.minecraft.server.PacketPlayOutTitle;
import net.minecraft.server.PacketPlayOutTransaction;
import net.minecraft.server.PacketPlayOutUpdateAttributes;
import net.minecraft.server.PacketPlayOutUpdateEntityNBT;
import net.minecraft.server.PacketPlayOutUpdateHealth;
import net.minecraft.server.PacketPlayOutUpdateSign;
import net.minecraft.server.PacketPlayOutUpdateTime;
import net.minecraft.server.PacketPlayOutWindowData;
import net.minecraft.server.PacketPlayOutWindowItems;
import net.minecraft.server.PacketPlayOutWorldBorder;
import net.minecraft.server.PacketPlayOutWorldEvent;
import net.minecraft.server.PacketPlayOutWorldParticles;
import net.minecraft.server.PacketStatusInPing;
import net.minecraft.server.PacketStatusInStart;
import net.minecraft.server.PacketStatusOutPong;
import net.minecraft.server.PacketStatusOutServerInfo;
import org.apache.logging.log4j.LogManager;

public enum EnumProtocol {
   HANDSHAKING(-1) {
      {
         this.a(EnumProtocolDirection.SERVERBOUND, PacketHandshakingInSetProtocol.class);
      }
   },
   PLAY(0) {
      {
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutKeepAlive.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutLogin.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutChat.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutUpdateTime.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityEquipment.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnPosition.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutUpdateHealth.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutRespawn.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutPosition.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutHeldItemSlot.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutBed.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutAnimation.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutNamedEntitySpawn.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutCollect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnEntity.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnEntityLiving.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnEntityPainting.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnEntityExperienceOrb.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityVelocity.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityDestroy.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntity.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntity.PacketPlayOutRelEntityMove.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntity.PacketPlayOutEntityLook.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityTeleport.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityHeadRotation.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityStatus.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutAttachEntity.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityMetadata.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutEntityEffect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutRemoveEntityEffect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutExperience.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutUpdateAttributes.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutMapChunk.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutMultiBlockChange.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutBlockChange.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutBlockAction.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutBlockBreakAnimation.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutMapChunkBulk.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutExplosion.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutWorldEvent.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutNamedSoundEffect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutWorldParticles.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutGameStateChange.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSpawnEntityWeather.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutOpenWindow.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutCloseWindow.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSetSlot.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutWindowItems.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutWindowData.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutTransaction.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutUpdateSign.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutMap.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutTileEntityData.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutOpenSignEditor.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutStatistic.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutPlayerInfo.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutAbilities.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutTabComplete.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutScoreboardObjective.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutScoreboardScore.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutScoreboardDisplayObjective.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutScoreboardTeam.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutCustomPayload.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutKickDisconnect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutServerDifficulty.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutCombatEvent.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutCamera.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutWorldBorder.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutTitle.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutSetCompression.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutPlayerListHeaderFooter.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutResourcePackSend.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketPlayOutUpdateEntityNBT.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInKeepAlive.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInChat.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInUseEntity.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInFlying.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInFlying.PacketPlayInPosition.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInFlying.PacketPlayInLook.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInFlying.PacketPlayInPositionLook.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInBlockDig.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInBlockPlace.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInHeldItemSlot.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInArmAnimation.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInEntityAction.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInSteerVehicle.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInCloseWindow.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInWindowClick.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInTransaction.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInSetCreativeSlot.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInEnchantItem.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInUpdateSign.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInAbilities.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInTabComplete.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInSettings.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInClientCommand.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInCustomPayload.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInSpectate.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketPlayInResourcePackStatus.class);
      }
   },
   STATUS(1) {
      {
         this.a(EnumProtocolDirection.SERVERBOUND, PacketStatusInStart.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketStatusOutServerInfo.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketStatusInPing.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketStatusOutPong.class);
      }
   },
   LOGIN(2) {
      {
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketLoginOutDisconnect.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketLoginOutEncryptionBegin.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketLoginOutSuccess.class);
         this.a(EnumProtocolDirection.CLIENTBOUND, PacketLoginOutSetCompression.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketLoginInStart.class);
         this.a(EnumProtocolDirection.SERVERBOUND, PacketLoginInEncryptionBegin.class);
      }
   };

   private static int e;
   private static int f;
   private static final EnumProtocol[] g;
   private static final Map<Class<? extends Packet>, EnumProtocol> h;
   private final int i;
   private final Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet>>> j;

   private EnumProtocol(int var3) {
      this.j = Maps.newEnumMap(EnumProtocolDirection.class);
      this.i = var3;
   }

   protected EnumProtocol a(EnumProtocolDirection var1, Class<? extends Packet> var2) {
      Object var3 = (BiMap)this.j.get(var1);
      if(var3 == null) {
         var3 = HashBiMap.create();
         this.j.put(var1, var3);
      }

      if(((BiMap)var3).containsValue(var2)) {
         String var4 = var1 + " packet " + var2 + " is already known to ID " + ((BiMap)var3).inverse().get(var2);
         LogManager.getLogger().fatal(var4);
         throw new IllegalArgumentException(var4);
      } else {
         ((BiMap)var3).put(Integer.valueOf(((BiMap)var3).size()), var2);
         return this;
      }
   }

   public Integer a(EnumProtocolDirection var1, Packet var2) {
      return (Integer)((BiMap)this.j.get(var1)).inverse().get(var2.getClass());
   }

   public Packet a(EnumProtocolDirection var1, int var2) throws IllegalAccessException, InstantiationException {
      Class var3 = (Class)((BiMap)this.j.get(var1)).get(Integer.valueOf(var2));
      return var3 == null?null:(Packet)var3.newInstance();
   }

   public int a() {
      return this.i;
   }

   public static EnumProtocol a(int var0) {
      return var0 >= e && var0 <= f?g[var0 - e]:null;
   }

   public static EnumProtocol a(Packet var0) {
      return (EnumProtocol)h.get(var0.getClass());
   }

   // $FF: synthetic method
   EnumProtocol(int var3, Object var4) {
      this(var3);
   }

   static {
      e = -1;
      f = 2;
      g = new EnumProtocol[f - e + 1];
      h = Maps.newHashMap();
      EnumProtocol[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumProtocol var3 = var0[var2];
         int var4 = var3.a();
         if(var4 < e || var4 > f) {
            throw new Error("Invalid protocol ID " + Integer.toString(var4));
         }

         g[var4 - e] = var3;
         Iterator var5 = var3.j.keySet().iterator();

         while(var5.hasNext()) {
            EnumProtocolDirection var6 = (EnumProtocolDirection)var5.next();

            Class var8;
            for(Iterator var7 = ((BiMap)var3.j.get(var6)).values().iterator(); var7.hasNext(); h.put(var8, var3)) {
               var8 = (Class)var7.next();
               if(h.containsKey(var8) && h.get(var8) != var3) {
                  throw new Error("Packet " + var8 + " is already assigned to protocol " + h.get(var8) + " - can\'t reassign to " + var3);
               }

               try {
                  var8.newInstance();
               } catch (Throwable var10) {
                  throw new Error("Packet " + var8 + " fails instantiation checks! " + var8);
               }
            }
         }
      }

   }
}
