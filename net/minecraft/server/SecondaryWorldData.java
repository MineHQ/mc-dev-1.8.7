package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.GameRules;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldSettings;
import net.minecraft.server.WorldType;

public class SecondaryWorldData extends WorldData {
   private final WorldData b;

   public SecondaryWorldData(WorldData var1) {
      this.b = var1;
   }

   public NBTTagCompound a() {
      return this.b.a();
   }

   public NBTTagCompound a(NBTTagCompound var1) {
      return this.b.a(var1);
   }

   public long getSeed() {
      return this.b.getSeed();
   }

   public int c() {
      return this.b.c();
   }

   public int d() {
      return this.b.d();
   }

   public int e() {
      return this.b.e();
   }

   public long getTime() {
      return this.b.getTime();
   }

   public long getDayTime() {
      return this.b.getDayTime();
   }

   public NBTTagCompound i() {
      return this.b.i();
   }

   public String getName() {
      return this.b.getName();
   }

   public int l() {
      return this.b.l();
   }

   public boolean isThundering() {
      return this.b.isThundering();
   }

   public int getThunderDuration() {
      return this.b.getThunderDuration();
   }

   public boolean hasStorm() {
      return this.b.hasStorm();
   }

   public int getWeatherDuration() {
      return this.b.getWeatherDuration();
   }

   public WorldSettings.EnumGamemode getGameType() {
      return this.b.getGameType();
   }

   public void setTime(long var1) {
   }

   public void setDayTime(long var1) {
   }

   public void setSpawn(BlockPosition var1) {
   }

   public void a(String var1) {
   }

   public void e(int var1) {
   }

   public void setThundering(boolean var1) {
   }

   public void setThunderDuration(int var1) {
   }

   public void setStorm(boolean var1) {
   }

   public void setWeatherDuration(int var1) {
   }

   public boolean shouldGenerateMapFeatures() {
      return this.b.shouldGenerateMapFeatures();
   }

   public boolean isHardcore() {
      return this.b.isHardcore();
   }

   public WorldType getType() {
      return this.b.getType();
   }

   public void a(WorldType var1) {
   }

   public boolean v() {
      return this.b.v();
   }

   public void c(boolean var1) {
   }

   public boolean w() {
      return this.b.w();
   }

   public void d(boolean var1) {
   }

   public GameRules x() {
      return this.b.x();
   }

   public EnumDifficulty getDifficulty() {
      return this.b.getDifficulty();
   }

   public void setDifficulty(EnumDifficulty var1) {
   }

   public boolean isDifficultyLocked() {
      return this.b.isDifficultyLocked();
   }

   public void e(boolean var1) {
   }
}
