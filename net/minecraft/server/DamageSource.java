package net.minecraft.server;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EntityDamageSourceIndirect;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.Explosion;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.LocaleI18n;

public class DamageSource {
   public static DamageSource FIRE = (new DamageSource("inFire")).setExplosion();
   public static DamageSource LIGHTNING = new DamageSource("lightningBolt");
   public static DamageSource BURN = (new DamageSource("onFire")).setIgnoreArmor().setExplosion();
   public static DamageSource LAVA = (new DamageSource("lava")).setExplosion();
   public static DamageSource STUCK = (new DamageSource("inWall")).setIgnoreArmor();
   public static DamageSource DROWN = (new DamageSource("drown")).setIgnoreArmor();
   public static DamageSource STARVE = (new DamageSource("starve")).setIgnoreArmor().m();
   public static DamageSource CACTUS = new DamageSource("cactus");
   public static DamageSource FALL = (new DamageSource("fall")).setIgnoreArmor();
   public static DamageSource OUT_OF_WORLD = (new DamageSource("outOfWorld")).setIgnoreArmor().l();
   public static DamageSource GENERIC = (new DamageSource("generic")).setIgnoreArmor();
   public static DamageSource MAGIC = (new DamageSource("magic")).setIgnoreArmor().setMagic();
   public static DamageSource WITHER = (new DamageSource("wither")).setIgnoreArmor();
   public static DamageSource ANVIL = new DamageSource("anvil");
   public static DamageSource FALLING_BLOCK = new DamageSource("fallingBlock");
   private boolean q;
   private boolean r;
   private boolean s;
   private float t = 0.3F;
   private boolean u;
   private boolean v;
   private boolean w;
   private boolean x;
   private boolean y;
   public String translationIndex;

   public static DamageSource mobAttack(EntityLiving var0) {
      return new EntityDamageSource("mob", var0);
   }

   public static DamageSource playerAttack(EntityHuman var0) {
      return new EntityDamageSource("player", var0);
   }

   public static DamageSource arrow(EntityArrow var0, Entity var1) {
      return (new EntityDamageSourceIndirect("arrow", var0, var1)).b();
   }

   public static DamageSource fireball(EntityFireball var0, Entity var1) {
      return var1 == null?(new EntityDamageSourceIndirect("onFire", var0, var0)).setExplosion().b():(new EntityDamageSourceIndirect("fireball", var0, var1)).setExplosion().b();
   }

   public static DamageSource projectile(Entity var0, Entity var1) {
      return (new EntityDamageSourceIndirect("thrown", var0, var1)).b();
   }

   public static DamageSource b(Entity var0, Entity var1) {
      return (new EntityDamageSourceIndirect("indirectMagic", var0, var1)).setIgnoreArmor().setMagic();
   }

   public static DamageSource a(Entity var0) {
      return (new EntityDamageSource("thorns", var0)).v().setMagic();
   }

   public static DamageSource explosion(Explosion var0) {
      return var0 != null && var0.c() != null?(new EntityDamageSource("explosion.player", var0.c())).q().d():(new DamageSource("explosion")).q().d();
   }

   public boolean a() {
      return this.v;
   }

   public DamageSource b() {
      this.v = true;
      return this;
   }

   public boolean isExplosion() {
      return this.y;
   }

   public DamageSource d() {
      this.y = true;
      return this;
   }

   public boolean ignoresArmor() {
      return this.q;
   }

   public float getExhaustionCost() {
      return this.t;
   }

   public boolean ignoresInvulnerability() {
      return this.r;
   }

   public boolean isStarvation() {
      return this.s;
   }

   protected DamageSource(String var1) {
      this.translationIndex = var1;
   }

   public Entity i() {
      return this.getEntity();
   }

   public Entity getEntity() {
      return null;
   }

   protected DamageSource setIgnoreArmor() {
      this.q = true;
      this.t = 0.0F;
      return this;
   }

   protected DamageSource l() {
      this.r = true;
      return this;
   }

   protected DamageSource m() {
      this.s = true;
      this.t = 0.0F;
      return this;
   }

   protected DamageSource setExplosion() {
      this.u = true;
      return this;
   }

   public IChatBaseComponent getLocalizedDeathMessage(EntityLiving var1) {
      EntityLiving var2 = var1.bt();
      String var3 = "death.attack." + this.translationIndex;
      String var4 = var3 + ".player";
      return var2 != null && LocaleI18n.c(var4)?new ChatMessage(var4, new Object[]{var1.getScoreboardDisplayName(), var2.getScoreboardDisplayName()}):new ChatMessage(var3, new Object[]{var1.getScoreboardDisplayName()});
   }

   public boolean o() {
      return this.u;
   }

   public String p() {
      return this.translationIndex;
   }

   public DamageSource q() {
      this.w = true;
      return this;
   }

   public boolean r() {
      return this.w;
   }

   public boolean isMagic() {
      return this.x;
   }

   public DamageSource setMagic() {
      this.x = true;
      return this;
   }

   public boolean u() {
      Entity var1 = this.getEntity();
      return var1 instanceof EntityHuman && ((EntityHuman)var1).abilities.canInstantlyBuild;
   }
}
