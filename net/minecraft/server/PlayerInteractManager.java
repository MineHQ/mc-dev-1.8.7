package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.BlockChest;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IInventory;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ItemSword;
import net.minecraft.server.Material;
import net.minecraft.server.PacketPlayOutBlockChange;
import net.minecraft.server.PacketPlayOutPlayerInfo;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import net.minecraft.server.WorldSettings;

public class PlayerInteractManager {
   public World world;
   public EntityPlayer player;
   private WorldSettings.EnumGamemode gamemode;
   private boolean d;
   private int lastDigTick;
   private BlockPosition f;
   private int currentTick;
   private boolean h;
   private BlockPosition i;
   private int j;
   private int k;

   public PlayerInteractManager(World var1) {
      this.gamemode = WorldSettings.EnumGamemode.NOT_SET;
      this.f = BlockPosition.ZERO;
      this.i = BlockPosition.ZERO;
      this.k = -1;
      this.world = var1;
   }

   public void setGameMode(WorldSettings.EnumGamemode var1) {
      this.gamemode = var1;
      var1.a(this.player.abilities);
      this.player.updateAbilities();
      this.player.server.getPlayerList().sendAll(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_GAME_MODE, new EntityPlayer[]{this.player}));
   }

   public WorldSettings.EnumGamemode getGameMode() {
      return this.gamemode;
   }

   public boolean c() {
      return this.gamemode.e();
   }

   public boolean isCreative() {
      return this.gamemode.d();
   }

   public void b(WorldSettings.EnumGamemode var1) {
      if(this.gamemode == WorldSettings.EnumGamemode.NOT_SET) {
         this.gamemode = var1;
      }

      this.setGameMode(this.gamemode);
   }

   public void a() {
      ++this.currentTick;
      float var3;
      int var4;
      if(this.h) {
         int var1 = this.currentTick - this.j;
         Block var2 = this.world.getType(this.i).getBlock();
         if(var2.getMaterial() == Material.AIR) {
            this.h = false;
         } else {
            var3 = var2.getDamage(this.player, this.player.world, this.i) * (float)(var1 + 1);
            var4 = (int)(var3 * 10.0F);
            if(var4 != this.k) {
               this.world.c(this.player.getId(), this.i, var4);
               this.k = var4;
            }

            if(var3 >= 1.0F) {
               this.h = false;
               this.breakBlock(this.i);
            }
         }
      } else if(this.d) {
         Block var5 = this.world.getType(this.f).getBlock();
         if(var5.getMaterial() == Material.AIR) {
            this.world.c(this.player.getId(), this.f, -1);
            this.k = -1;
            this.d = false;
         } else {
            int var6 = this.currentTick - this.lastDigTick;
            var3 = var5.getDamage(this.player, this.player.world, this.i) * (float)(var6 + 1);
            var4 = (int)(var3 * 10.0F);
            if(var4 != this.k) {
               this.world.c(this.player.getId(), this.f, var4);
               this.k = var4;
            }
         }
      }

   }

   public void a(BlockPosition var1, EnumDirection var2) {
      if(this.isCreative()) {
         if(!this.world.douseFire((EntityHuman)null, var1, var2)) {
            this.breakBlock(var1);
         }

      } else {
         Block var3 = this.world.getType(var1).getBlock();
         if(this.gamemode.c()) {
            if(this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
               return;
            }

            if(!this.player.cn()) {
               ItemStack var4 = this.player.bZ();
               if(var4 == null) {
                  return;
               }

               if(!var4.c(var3)) {
                  return;
               }
            }
         }

         this.world.douseFire((EntityHuman)null, var1, var2);
         this.lastDigTick = this.currentTick;
         float var6 = 1.0F;
         if(var3.getMaterial() != Material.AIR) {
            var3.attack(this.world, var1, this.player);
            var6 = var3.getDamage(this.player, this.player.world, var1);
         }

         if(var3.getMaterial() != Material.AIR && var6 >= 1.0F) {
            this.breakBlock(var1);
         } else {
            this.d = true;
            this.f = var1;
            int var5 = (int)(var6 * 10.0F);
            this.world.c(this.player.getId(), var1, var5);
            this.k = var5;
         }

      }
   }

   public void a(BlockPosition var1) {
      if(var1.equals(this.f)) {
         int var2 = this.currentTick - this.lastDigTick;
         Block var3 = this.world.getType(var1).getBlock();
         if(var3.getMaterial() != Material.AIR) {
            float var4 = var3.getDamage(this.player, this.player.world, var1) * (float)(var2 + 1);
            if(var4 >= 0.7F) {
               this.d = false;
               this.world.c(this.player.getId(), var1, -1);
               this.breakBlock(var1);
            } else if(!this.h) {
               this.d = false;
               this.h = true;
               this.i = var1;
               this.j = this.lastDigTick;
            }
         }
      }

   }

   public void e() {
      this.d = false;
      this.world.c(this.player.getId(), this.f, -1);
   }

   private boolean c(BlockPosition var1) {
      IBlockData var2 = this.world.getType(var1);
      var2.getBlock().a((World)this.world, var1, (IBlockData)var2, (EntityHuman)this.player);
      boolean var3 = this.world.setAir(var1);
      if(var3) {
         var2.getBlock().postBreak(this.world, var1, var2);
      }

      return var3;
   }

   public boolean breakBlock(BlockPosition var1) {
      if(this.gamemode.d() && this.player.bA() != null && this.player.bA().getItem() instanceof ItemSword) {
         return false;
      } else {
         IBlockData var2 = this.world.getType(var1);
         TileEntity var3 = this.world.getTileEntity(var1);
         if(this.gamemode.c()) {
            if(this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
               return false;
            }

            if(!this.player.cn()) {
               ItemStack var4 = this.player.bZ();
               if(var4 == null) {
                  return false;
               }

               if(!var4.c(var2.getBlock())) {
                  return false;
               }
            }
         }

         this.world.a(this.player, 2001, var1, Block.getCombinedId(var2));
         boolean var7 = this.c(var1);
         if(this.isCreative()) {
            this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(this.world, var1));
         } else {
            ItemStack var5 = this.player.bZ();
            boolean var6 = this.player.b((Block)var2.getBlock());
            if(var5 != null) {
               var5.a(this.world, var2.getBlock(), var1, this.player);
               if(var5.count == 0) {
                  this.player.ca();
               }
            }

            if(var7 && var6) {
               var2.getBlock().a(this.world, this.player, var1, var2, var3);
            }
         }

         return var7;
      }
   }

   public boolean useItem(EntityHuman var1, World var2, ItemStack var3) {
      if(this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
         return false;
      } else {
         int var4 = var3.count;
         int var5 = var3.getData();
         ItemStack var6 = var3.a(var2, var1);
         if(var6 != var3 || var6 != null && (var6.count != var4 || var6.l() > 0 || var6.getData() != var5)) {
            var1.inventory.items[var1.inventory.itemInHandIndex] = var6;
            if(this.isCreative()) {
               var6.count = var4;
               if(var6.e()) {
                  var6.setData(var5);
               }
            }

            if(var6.count == 0) {
               var1.inventory.items[var1.inventory.itemInHandIndex] = null;
            }

            if(!var1.bS()) {
               ((EntityPlayer)var1).updateInventory(var1.defaultContainer);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean interact(EntityHuman var1, World var2, ItemStack var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(this.gamemode == WorldSettings.EnumGamemode.SPECTATOR) {
         TileEntity var13 = var2.getTileEntity(var4);
         if(var13 instanceof ITileInventory) {
            Block var14 = var2.getType(var4).getBlock();
            ITileInventory var15 = (ITileInventory)var13;
            if(var15 instanceof TileEntityChest && var14 instanceof BlockChest) {
               var15 = ((BlockChest)var14).f(var2, var4);
            }

            if(var15 != null) {
               var1.openContainer(var15);
               return true;
            }
         } else if(var13 instanceof IInventory) {
            var1.openContainer((IInventory)var13);
            return true;
         }

         return false;
      } else {
         if(!var1.isSneaking() || var1.bA() == null) {
            IBlockData var9 = var2.getType(var4);
            if(var9.getBlock().interact(var2, var4, var9, var1, var5, var6, var7, var8)) {
               return true;
            }
         }

         if(var3 == null) {
            return false;
         } else if(this.isCreative()) {
            int var12 = var3.getData();
            int var10 = var3.count;
            boolean var11 = var3.placeItem(var1, var2, var4, var5, var6, var7, var8);
            var3.setData(var12);
            var3.count = var10;
            return var11;
         } else {
            return var3.placeItem(var1, var2, var4, var5, var6, var7, var8);
         }
      }
   }

   public void a(WorldServer var1) {
      this.world = var1;
   }
}
