package net.minecraft.server;

import net.minecraft.server.BlockContainer;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockStateBoolean;
import net.minecraft.server.BlockStateList;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IBlockState;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.Material;
import net.minecraft.server.MaterialMapColor;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class BlockJukeBox extends BlockContainer {
   public static final BlockStateBoolean HAS_RECORD = BlockStateBoolean.of("has_record");

   protected BlockJukeBox() {
      super(Material.WOOD, MaterialMapColor.l);
      this.j(this.blockStateList.getBlockData().set(HAS_RECORD, Boolean.valueOf(false)));
      this.a(CreativeModeTab.c);
   }

   public boolean interact(World var1, BlockPosition var2, IBlockData var3, EntityHuman var4, EnumDirection var5, float var6, float var7, float var8) {
      if(((Boolean)var3.get(HAS_RECORD)).booleanValue()) {
         this.dropRecord(var1, var2, var3);
         var3 = var3.set(HAS_RECORD, Boolean.valueOf(false));
         var1.setTypeAndData(var2, var3, 2);
         return true;
      } else {
         return false;
      }
   }

   public void a(World var1, BlockPosition var2, IBlockData var3, ItemStack var4) {
      if(!var1.isClientSide) {
         TileEntity var5 = var1.getTileEntity(var2);
         if(var5 instanceof BlockJukeBox.TileEntityRecordPlayer) {
            ((BlockJukeBox.TileEntityRecordPlayer)var5).setRecord(new ItemStack(var4.getItem(), 1, var4.getData()));
            var1.setTypeAndData(var2, var3.set(HAS_RECORD, Boolean.valueOf(true)), 2);
         }
      }
   }

   public void dropRecord(World var1, BlockPosition var2, IBlockData var3) {
      if(!var1.isClientSide) {
         TileEntity var4 = var1.getTileEntity(var2);
         if(var4 instanceof BlockJukeBox.TileEntityRecordPlayer) {
            BlockJukeBox.TileEntityRecordPlayer var5 = (BlockJukeBox.TileEntityRecordPlayer)var4;
            ItemStack var6 = var5.getRecord();
            if(var6 != null) {
               var1.triggerEffect(1005, var2, 0);
               var1.a((BlockPosition)var2, (String)null);
               var5.setRecord((ItemStack)null);
               float var7 = 0.7F;
               double var8 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
               double var10 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.2D + 0.6D;
               double var12 = (double)(var1.random.nextFloat() * var7) + (double)(1.0F - var7) * 0.5D;
               ItemStack var14 = var6.cloneItemStack();
               EntityItem var15 = new EntityItem(var1, (double)var2.getX() + var8, (double)var2.getY() + var10, (double)var2.getZ() + var12, var14);
               var15.p();
               var1.addEntity(var15);
            }
         }
      }
   }

   public void remove(World var1, BlockPosition var2, IBlockData var3) {
      this.dropRecord(var1, var2, var3);
      super.remove(var1, var2, var3);
   }

   public void dropNaturally(World var1, BlockPosition var2, IBlockData var3, float var4, int var5) {
      if(!var1.isClientSide) {
         super.dropNaturally(var1, var2, var3, var4, 0);
      }
   }

   public TileEntity a(World var1, int var2) {
      return new BlockJukeBox.TileEntityRecordPlayer();
   }

   public boolean isComplexRedstone() {
      return true;
   }

   public int l(World var1, BlockPosition var2) {
      TileEntity var3 = var1.getTileEntity(var2);
      if(var3 instanceof BlockJukeBox.TileEntityRecordPlayer) {
         ItemStack var4 = ((BlockJukeBox.TileEntityRecordPlayer)var3).getRecord();
         if(var4 != null) {
            return Item.getId(var4.getItem()) + 1 - Item.getId(Items.RECORD_13);
         }
      }

      return 0;
   }

   public int b() {
      return 3;
   }

   public IBlockData fromLegacyData(int var1) {
      return this.getBlockData().set(HAS_RECORD, Boolean.valueOf(var1 > 0));
   }

   public int toLegacyData(IBlockData var1) {
      return ((Boolean)var1.get(HAS_RECORD)).booleanValue()?1:0;
   }

   protected BlockStateList getStateList() {
      return new BlockStateList(this, new IBlockState[]{HAS_RECORD});
   }

   public static class TileEntityRecordPlayer extends TileEntity {
      private ItemStack record;

      public TileEntityRecordPlayer() {
      }

      public void a(NBTTagCompound var1) {
         super.a(var1);
         if(var1.hasKeyOfType("RecordItem", 10)) {
            this.setRecord(ItemStack.createStack(var1.getCompound("RecordItem")));
         } else if(var1.getInt("Record") > 0) {
            this.setRecord(new ItemStack(Item.getById(var1.getInt("Record")), 1, 0));
         }

      }

      public void b(NBTTagCompound var1) {
         super.b(var1);
         if(this.getRecord() != null) {
            var1.set("RecordItem", this.getRecord().save(new NBTTagCompound()));
         }

      }

      public ItemStack getRecord() {
         return this.record;
      }

      public void setRecord(ItemStack var1) {
         this.record = var1;
         this.update();
      }
   }
}
