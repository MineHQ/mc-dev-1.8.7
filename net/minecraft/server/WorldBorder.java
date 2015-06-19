package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.Entity;
import net.minecraft.server.EnumWorldBorderState;
import net.minecraft.server.IWorldBorderListener;

public class WorldBorder {
   private final List<IWorldBorderListener> a = Lists.newArrayList();
   private double b = 0.0D;
   private double c = 0.0D;
   private double d = 6.0E7D;
   private double e;
   private long f;
   private long g;
   private int h;
   private double i;
   private double j;
   private int k;
   private int l;

   public WorldBorder() {
      this.e = this.d;
      this.h = 29999984;
      this.i = 0.2D;
      this.j = 5.0D;
      this.k = 15;
      this.l = 5;
   }

   public boolean a(BlockPosition var1) {
      return (double)(var1.getX() + 1) > this.b() && (double)var1.getX() < this.d() && (double)(var1.getZ() + 1) > this.c() && (double)var1.getZ() < this.e();
   }

   public boolean isInBounds(ChunkCoordIntPair var1) {
      return (double)var1.e() > this.b() && (double)var1.c() < this.d() && (double)var1.f() > this.c() && (double)var1.d() < this.e();
   }

   public boolean a(AxisAlignedBB var1) {
      return var1.d > this.b() && var1.a < this.d() && var1.f > this.c() && var1.c < this.e();
   }

   public double a(Entity var1) {
      return this.b(var1.locX, var1.locZ);
   }

   public double b(double var1, double var3) {
      double var5 = var3 - this.c();
      double var7 = this.e() - var3;
      double var9 = var1 - this.b();
      double var11 = this.d() - var1;
      double var13 = Math.min(var9, var11);
      var13 = Math.min(var13, var5);
      return Math.min(var13, var7);
   }

   public EnumWorldBorderState getState() {
      return this.e < this.d?EnumWorldBorderState.SHRINKING:(this.e > this.d?EnumWorldBorderState.GROWING:EnumWorldBorderState.STATIONARY);
   }

   public double b() {
      double var1 = this.getCenterX() - this.getSize() / 2.0D;
      if(var1 < (double)(-this.h)) {
         var1 = (double)(-this.h);
      }

      return var1;
   }

   public double c() {
      double var1 = this.getCenterZ() - this.getSize() / 2.0D;
      if(var1 < (double)(-this.h)) {
         var1 = (double)(-this.h);
      }

      return var1;
   }

   public double d() {
      double var1 = this.getCenterX() + this.getSize() / 2.0D;
      if(var1 > (double)this.h) {
         var1 = (double)this.h;
      }

      return var1;
   }

   public double e() {
      double var1 = this.getCenterZ() + this.getSize() / 2.0D;
      if(var1 > (double)this.h) {
         var1 = (double)this.h;
      }

      return var1;
   }

   public double getCenterX() {
      return this.b;
   }

   public double getCenterZ() {
      return this.c;
   }

   public void setCenter(double var1, double var3) {
      this.b = var1;
      this.c = var3;
      Iterator var5 = this.k().iterator();

      while(var5.hasNext()) {
         IWorldBorderListener var6 = (IWorldBorderListener)var5.next();
         var6.a(this, var1, var3);
      }

   }

   public double getSize() {
      if(this.getState() != EnumWorldBorderState.STATIONARY) {
         double var1 = (double)((float)(System.currentTimeMillis() - this.g) / (float)(this.f - this.g));
         if(var1 < 1.0D) {
            return this.d + (this.e - this.d) * var1;
         }

         this.setSize(this.e);
      }

      return this.d;
   }

   public long i() {
      return this.getState() != EnumWorldBorderState.STATIONARY?this.f - System.currentTimeMillis():0L;
   }

   public double j() {
      return this.e;
   }

   public void setSize(double var1) {
      this.d = var1;
      this.e = var1;
      this.f = System.currentTimeMillis();
      this.g = this.f;
      Iterator var3 = this.k().iterator();

      while(var3.hasNext()) {
         IWorldBorderListener var4 = (IWorldBorderListener)var3.next();
         var4.a(this, var1);
      }

   }

   public void transitionSizeBetween(double var1, double var3, long var5) {
      this.d = var1;
      this.e = var3;
      this.g = System.currentTimeMillis();
      this.f = this.g + var5;
      Iterator var7 = this.k().iterator();

      while(var7.hasNext()) {
         IWorldBorderListener var8 = (IWorldBorderListener)var7.next();
         var8.a(this, var1, var3, var5);
      }

   }

   protected List<IWorldBorderListener> k() {
      return Lists.newArrayList((Iterable)this.a);
   }

   public void a(IWorldBorderListener var1) {
      this.a.add(var1);
   }

   public void a(int var1) {
      this.h = var1;
   }

   public int l() {
      return this.h;
   }

   public double getDamageBuffer() {
      return this.j;
   }

   public void setDamageBuffer(double var1) {
      this.j = var1;
      Iterator var3 = this.k().iterator();

      while(var3.hasNext()) {
         IWorldBorderListener var4 = (IWorldBorderListener)var3.next();
         var4.c(this, var1);
      }

   }

   public double getDamageAmount() {
      return this.i;
   }

   public void setDamageAmount(double var1) {
      this.i = var1;
      Iterator var3 = this.k().iterator();

      while(var3.hasNext()) {
         IWorldBorderListener var4 = (IWorldBorderListener)var3.next();
         var4.b(this, var1);
      }

   }

   public int getWarningTime() {
      return this.k;
   }

   public void setWarningTime(int var1) {
      this.k = var1;
      Iterator var2 = this.k().iterator();

      while(var2.hasNext()) {
         IWorldBorderListener var3 = (IWorldBorderListener)var2.next();
         var3.a(this, var1);
      }

   }

   public int getWarningDistance() {
      return this.l;
   }

   public void setWarningDistance(int var1) {
      this.l = var1;
      Iterator var2 = this.k().iterator();

      while(var2.hasNext()) {
         IWorldBorderListener var3 = (IWorldBorderListener)var2.next();
         var3.b(this, var1);
      }

   }
}
