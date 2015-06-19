package net.minecraft.server;

import net.minecraft.server.PacketListener;
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

public interface PacketListenerPlayIn extends PacketListener {
   void a(PacketPlayInArmAnimation var1);

   void a(PacketPlayInChat var1);

   void a(PacketPlayInTabComplete var1);

   void a(PacketPlayInClientCommand var1);

   void a(PacketPlayInSettings var1);

   void a(PacketPlayInTransaction var1);

   void a(PacketPlayInEnchantItem var1);

   void a(PacketPlayInWindowClick var1);

   void a(PacketPlayInCloseWindow var1);

   void a(PacketPlayInCustomPayload var1);

   void a(PacketPlayInUseEntity var1);

   void a(PacketPlayInKeepAlive var1);

   void a(PacketPlayInFlying var1);

   void a(PacketPlayInAbilities var1);

   void a(PacketPlayInBlockDig var1);

   void a(PacketPlayInEntityAction var1);

   void a(PacketPlayInSteerVehicle var1);

   void a(PacketPlayInHeldItemSlot var1);

   void a(PacketPlayInSetCreativeSlot var1);

   void a(PacketPlayInUpdateSign var1);

   void a(PacketPlayInBlockPlace var1);

   void a(PacketPlayInSpectate var1);

   void a(PacketPlayInResourcePackStatus var1);
}
