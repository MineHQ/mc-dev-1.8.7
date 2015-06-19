package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.server.Block;
import net.minecraft.server.BlockJukeBox;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CrashReportSystemDetails;
import net.minecraft.server.IBlockData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Packet;
import net.minecraft.server.TileEntityBanner;
import net.minecraft.server.TileEntityBeacon;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityCommand;
import net.minecraft.server.TileEntityComparator;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityDropper;
import net.minecraft.server.TileEntityEnchantTable;
import net.minecraft.server.TileEntityEnderChest;
import net.minecraft.server.TileEntityEnderPortal;
import net.minecraft.server.TileEntityFlowerPot;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.TileEntityHopper;
import net.minecraft.server.TileEntityLightDetector;
import net.minecraft.server.TileEntityMobSpawner;
import net.minecraft.server.TileEntityNote;
import net.minecraft.server.TileEntityPiston;
import net.minecraft.server.TileEntitySign;
import net.minecraft.server.TileEntitySkull;
import net.minecraft.server.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
   private static final Logger a = LogManager.getLogger();
   private static Map<String, Class<? extends TileEntity>> f = Maps.newHashMap();
   private static Map<Class<? extends TileEntity>, String> g = Maps.newHashMap();
   protected World world;
   protected BlockPosition position;
   protected boolean d;
   private int h;
   protected Block e;

   public TileEntity() {
      this.position = BlockPosition.ZERO;
      this.h = -1;
   }

   private static void a(Class<? extends TileEntity> var0, String var1) {
      if(f.containsKey(var1)) {
         throw new IllegalArgumentException("Duplicate id: " + var1);
      } else {
         f.put(var1, var0);
         g.put(var0, var1);
      }
   }

   public World getWorld() {
      return this.world;
   }

   public void a(World var1) {
      this.world = var1;
   }

   public boolean t() {
      return this.world != null;
   }

   public void a(NBTTagCompound var1) {
      this.position = new BlockPosition(var1.getInt("x"), var1.getInt("y"), var1.getInt("z"));
   }

   public void b(NBTTagCompound var1) {
      String var2 = (String)g.get(this.getClass());
      if(var2 == null) {
         throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
      } else {
         var1.setString("id", var2);
         var1.setInt("x", this.position.getX());
         var1.setInt("y", this.position.getY());
         var1.setInt("z", this.position.getZ());
      }
   }

   public static TileEntity c(NBTTagCompound var0) {
      TileEntity var1 = null;

      try {
         Class var2 = (Class)f.get(var0.getString("id"));
         if(var2 != null) {
            var1 = (TileEntity)var2.newInstance();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      if(var1 != null) {
         var1.a(var0);
      } else {
         a.warn("Skipping BlockEntity with id " + var0.getString("id"));
      }

      return var1;
   }

   public int u() {
      if(this.h == -1) {
         IBlockData var1 = this.world.getType(this.position);
         this.h = var1.getBlock().toLegacyData(var1);
      }

      return this.h;
   }

   public void update() {
      if(this.world != null) {
         IBlockData var1 = this.world.getType(this.position);
         this.h = var1.getBlock().toLegacyData(var1);
         this.world.b(this.position, this);
         if(this.w() != Blocks.AIR) {
            this.world.updateAdjacentComparators(this.position, this.w());
         }
      }

   }

   public BlockPosition getPosition() {
      return this.position;
   }

   public Block w() {
      if(this.e == null) {
         this.e = this.world.getType(this.position).getBlock();
      }

      return this.e;
   }

   public Packet getUpdatePacket() {
      return null;
   }

   public boolean x() {
      return this.d;
   }

   public void y() {
      this.d = true;
   }

   public void D() {
      this.d = false;
   }

   public boolean c(int var1, int var2) {
      return false;
   }

   public void E() {
      this.e = null;
      this.h = -1;
   }

   public void a(CrashReportSystemDetails var1) {
      var1.a("Name", new Callable() {
         public String a() throws Exception {
            return (String)TileEntity.g.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
         }

         // $FF: synthetic method
         public Object call() throws Exception {
            return this.a();
         }
      });
      if(this.world != null) {
         CrashReportSystemDetails.a(var1, this.position, this.w(), this.u());
         var1.a("Actual block type", new Callable() {
            public String a() throws Exception {
               int var1 = Block.getId(TileEntity.this.world.getType(TileEntity.this.position).getBlock());

               try {
                  return String.format("ID #%d (%s // %s)", new Object[]{Integer.valueOf(var1), Block.getById(var1).a(), Block.getById(var1).getClass().getCanonicalName()});
               } catch (Throwable var3) {
                  return "ID #" + var1;
               }
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
         var1.a("Actual block data value", new Callable() {
            public String a() throws Exception {
               IBlockData var1 = TileEntity.this.world.getType(TileEntity.this.position);
               int var2 = var1.getBlock().toLegacyData(var1);
               if(var2 < 0) {
                  return "Unknown? (Got " + var2 + ")";
               } else {
                  String var3 = String.format("%4s", new Object[]{Integer.toBinaryString(var2)}).replace(" ", "0");
                  return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[]{Integer.valueOf(var2), var3});
               }
            }

            // $FF: synthetic method
            public Object call() throws Exception {
               return this.a();
            }
         });
      }
   }

   public void a(BlockPosition var1) {
      this.position = var1;
   }

   public boolean F() {
      return false;
   }

   static {
      a(TileEntityFurnace.class, "Furnace");
      a(TileEntityChest.class, "Chest");
      a(TileEntityEnderChest.class, "EnderChest");
      a(BlockJukeBox.TileEntityRecordPlayer.class, "RecordPlayer");
      a(TileEntityDispenser.class, "Trap");
      a(TileEntityDropper.class, "Dropper");
      a(TileEntitySign.class, "Sign");
      a(TileEntityMobSpawner.class, "MobSpawner");
      a(TileEntityNote.class, "Music");
      a(TileEntityPiston.class, "Piston");
      a(TileEntityBrewingStand.class, "Cauldron");
      a(TileEntityEnchantTable.class, "EnchantTable");
      a(TileEntityEnderPortal.class, "Airportal");
      a(TileEntityCommand.class, "Control");
      a(TileEntityBeacon.class, "Beacon");
      a(TileEntitySkull.class, "Skull");
      a(TileEntityLightDetector.class, "DLDetector");
      a(TileEntityHopper.class, "Hopper");
      a(TileEntityComparator.class, "Comparator");
      a(TileEntityFlowerPot.class, "FlowerPot");
      a(TileEntityBanner.class, "Banner");
   }
}
