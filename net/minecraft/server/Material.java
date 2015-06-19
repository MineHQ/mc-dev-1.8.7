package net.minecraft.server;

import net.minecraft.server.MaterialDecoration;
import net.minecraft.server.MaterialGas;
import net.minecraft.server.MaterialLiquid;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.MaterialPortal;

public class Material {
   public static final Material AIR;
   public static final Material GRASS;
   public static final Material EARTH;
   public static final Material WOOD;
   public static final Material STONE;
   public static final Material ORE;
   public static final Material HEAVY;
   public static final Material WATER;
   public static final Material LAVA;
   public static final Material LEAVES;
   public static final Material PLANT;
   public static final Material REPLACEABLE_PLANT;
   public static final Material SPONGE;
   public static final Material CLOTH;
   public static final Material FIRE;
   public static final Material SAND;
   public static final Material ORIENTABLE;
   public static final Material WOOL;
   public static final Material SHATTERABLE;
   public static final Material BUILDABLE_GLASS;
   public static final Material TNT;
   public static final Material CORAL;
   public static final Material ICE;
   public static final Material SNOW_LAYER;
   public static final Material PACKED_ICE;
   public static final Material SNOW_BLOCK;
   public static final Material CACTUS;
   public static final Material CLAY;
   public static final Material PUMPKIN;
   public static final Material DRAGON_EGG;
   public static final Material PORTAL;
   public static final Material CAKE;
   public static final Material WEB;
   public static final Material PISTON;
   public static final Material BANNER;
   private boolean canBurn;
   private boolean K;
   private boolean L;
   private final MaterialMapColor M;
   private boolean N = true;
   private int O;
   private boolean P;

   public Material(MaterialMapColor var1) {
      this.M = var1;
   }

   public boolean isLiquid() {
      return false;
   }

   public boolean isBuildable() {
      return true;
   }

   public boolean blocksLight() {
      return true;
   }

   public boolean isSolid() {
      return true;
   }

   private Material s() {
      this.L = true;
      return this;
   }

   protected Material f() {
      this.N = false;
      return this;
   }

   protected Material g() {
      this.canBurn = true;
      return this;
   }

   public boolean isBurnable() {
      return this.canBurn;
   }

   public Material i() {
      this.K = true;
      return this;
   }

   public boolean isReplaceable() {
      return this.K;
   }

   public boolean k() {
      return this.L?false:this.isSolid();
   }

   public boolean isAlwaysDestroyable() {
      return this.N;
   }

   public int getPushReaction() {
      return this.O;
   }

   protected Material n() {
      this.O = 1;
      return this;
   }

   protected Material o() {
      this.O = 2;
      return this;
   }

   protected Material p() {
      this.P = true;
      return this;
   }

   public MaterialMapColor r() {
      return this.M;
   }

   static {
      AIR = new MaterialGas(MaterialMapColor.b);
      GRASS = new Material(MaterialMapColor.c);
      EARTH = new Material(MaterialMapColor.l);
      WOOD = (new Material(MaterialMapColor.o)).g();
      STONE = (new Material(MaterialMapColor.m)).f();
      ORE = (new Material(MaterialMapColor.h)).f();
      HEAVY = (new Material(MaterialMapColor.h)).f().o();
      WATER = (new MaterialLiquid(MaterialMapColor.n)).n();
      LAVA = (new MaterialLiquid(MaterialMapColor.f)).n();
      LEAVES = (new Material(MaterialMapColor.i)).g().s().n();
      PLANT = (new MaterialDecoration(MaterialMapColor.i)).n();
      REPLACEABLE_PLANT = (new MaterialDecoration(MaterialMapColor.i)).g().n().i();
      SPONGE = new Material(MaterialMapColor.t);
      CLOTH = (new Material(MaterialMapColor.e)).g();
      FIRE = (new MaterialGas(MaterialMapColor.b)).n();
      SAND = new Material(MaterialMapColor.d);
      ORIENTABLE = (new MaterialDecoration(MaterialMapColor.b)).n();
      WOOL = (new MaterialDecoration(MaterialMapColor.e)).g();
      SHATTERABLE = (new Material(MaterialMapColor.b)).s().p();
      BUILDABLE_GLASS = (new Material(MaterialMapColor.b)).p();
      TNT = (new Material(MaterialMapColor.f)).g().s();
      CORAL = (new Material(MaterialMapColor.i)).n();
      ICE = (new Material(MaterialMapColor.g)).s().p();
      SNOW_LAYER = (new Material(MaterialMapColor.g)).p();
      PACKED_ICE = (new MaterialDecoration(MaterialMapColor.j)).i().s().f().n();
      SNOW_BLOCK = (new Material(MaterialMapColor.j)).f();
      CACTUS = (new Material(MaterialMapColor.i)).s().n();
      CLAY = new Material(MaterialMapColor.k);
      PUMPKIN = (new Material(MaterialMapColor.i)).n();
      DRAGON_EGG = (new Material(MaterialMapColor.i)).n();
      PORTAL = (new MaterialPortal(MaterialMapColor.b)).o();
      CAKE = (new Material(MaterialMapColor.b)).n();
      WEB = (new Material(MaterialMapColor.e) {
         public boolean isSolid() {
            return false;
         }
      }).f().n();
      PISTON = (new Material(MaterialMapColor.m)).o();
      BANNER = (new Material(MaterialMapColor.b)).f().o();
   }
}
