package net.minecraft.server;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChatMessage;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PacketPlayOutGameStateChange;
import net.minecraft.server.PlayerInteractManager;
import net.minecraft.server.World;

public class DemoPlayerInteractManager extends PlayerInteractManager {
   private boolean c;
   private boolean d;
   private int e;
   private int f;

   public DemoPlayerInteractManager(World var1) {
      super(var1);
   }

   public void a() {
      super.a();
      ++this.f;
      long var1 = this.world.getTime();
      long var3 = var1 / 24000L + 1L;
      if(!this.c && this.f > 20) {
         this.c = true;
         this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 0.0F));
      }

      this.d = var1 > 120500L;
      if(this.d) {
         ++this.e;
      }

      if(var1 % 24000L == 500L) {
         if(var3 <= 6L) {
            this.player.sendMessage(new ChatMessage("demo.day." + var3, new Object[0]));
         }
      } else if(var3 == 1L) {
         if(var1 == 100L) {
            this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 101.0F));
         } else if(var1 == 175L) {
            this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 102.0F));
         } else if(var1 == 250L) {
            this.player.playerConnection.sendPacket(new PacketPlayOutGameStateChange(5, 103.0F));
         }
      } else if(var3 == 5L && var1 % 24000L == 22000L) {
         this.player.sendMessage(new ChatMessage("demo.day.warning", new Object[0]));
      }

   }

   private void f() {
      if(this.e > 100) {
         this.player.sendMessage(new ChatMessage("demo.reminder", new Object[0]));
         this.e = 0;
      }

   }

   public void a(BlockPosition var1, EnumDirection var2) {
      if(this.d) {
         this.f();
      } else {
         super.a(var1, var2);
      }
   }

   public void a(BlockPosition var1) {
      if(!this.d) {
         super.a(var1);
      }
   }

   public boolean breakBlock(BlockPosition var1) {
      return this.d?false:super.breakBlock(var1);
   }

   public boolean useItem(EntityHuman var1, World var2, ItemStack var3) {
      if(this.d) {
         this.f();
         return false;
      } else {
         return super.useItem(var1, var2, var3);
      }
   }

   public boolean interact(EntityHuman var1, World var2, ItemStack var3, BlockPosition var4, EnumDirection var5, float var6, float var7, float var8) {
      if(this.d) {
         this.f();
         return false;
      } else {
         return super.interact(var1, var2, var3, var4, var5, var6, var7, var8);
      }
   }
}
