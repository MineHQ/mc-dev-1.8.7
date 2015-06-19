package net.minecraft.server;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.World;

public interface IComplex {
   World a();

   boolean a(EntityComplexPart var1, DamageSource var2, float var3);
}
