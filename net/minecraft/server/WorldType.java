package net.minecraft.server;

public class WorldType {
   public static final WorldType[] types = new WorldType[16];
   public static final WorldType NORMAL = (new WorldType(0, "default", 1)).i();
   public static final WorldType FLAT = new WorldType(1, "flat");
   public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
   public static final WorldType AMPLIFIED = (new WorldType(3, "amplified")).j();
   public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
   public static final WorldType DEBUG_ALL_BLOCK_STATES = new WorldType(5, "debug_all_block_states");
   public static final WorldType NORMAL_1_1 = (new WorldType(8, "default_1_1", 0)).a(false);
   private final int i;
   private final String name;
   private final int version;
   private boolean l;
   private boolean m;
   private boolean n;

   private WorldType(int var1, String var2) {
      this(var1, var2, 0);
   }

   private WorldType(int var1, String var2, int var3) {
      this.name = var2;
      this.version = var3;
      this.l = true;
      this.i = var1;
      types[var1] = this;
   }

   public String name() {
      return this.name;
   }

   public int getVersion() {
      return this.version;
   }

   public WorldType a(int var1) {
      return this == NORMAL && var1 == 0?NORMAL_1_1:this;
   }

   private WorldType a(boolean var1) {
      this.l = var1;
      return this;
   }

   private WorldType i() {
      this.m = true;
      return this;
   }

   public boolean f() {
      return this.m;
   }

   public static WorldType getType(String var0) {
      for(int var1 = 0; var1 < types.length; ++var1) {
         if(types[var1] != null && types[var1].name.equalsIgnoreCase(var0)) {
            return types[var1];
         }
      }

      return null;
   }

   public int g() {
      return this.i;
   }

   private WorldType j() {
      this.n = true;
      return this;
   }
}
