package net.minecraft.server;

import net.minecraft.server.Entity;

public interface EntityOwnable {
   String getOwnerUUID();

   Entity getOwner();
}
