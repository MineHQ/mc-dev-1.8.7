package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.CommandObjectiveExecutor;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.IBlockData;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.IInventory;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NextTickListEntry;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

public class CommandClone extends CommandAbstract {
   public CommandClone() {
   }

   public String getCommand() {
      return "clone";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.clone.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 9) {
         throw new ExceptionUsage("commands.clone.usage", new Object[0]);
      } else {
         var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, 0);
         BlockPosition var3 = a(var1, var2, 0, false);
         BlockPosition var4 = a(var1, var2, 3, false);
         BlockPosition var5 = a(var1, var2, 6, false);
         StructureBoundingBox var6 = new StructureBoundingBox(var3, var4);
         StructureBoundingBox var7 = new StructureBoundingBox(var5, var5.a(var6.b()));
         int var8 = var6.c() * var6.d() * var6.e();
         if(var8 > '\u8000') {
            throw new CommandException("commands.clone.tooManyBlocks", new Object[]{Integer.valueOf(var8), Integer.valueOf('\u8000')});
         } else {
            boolean var9 = false;
            Block var10 = null;
            int var11 = -1;
            if((var2.length < 11 || !var2[10].equals("force") && !var2[10].equals("move")) && var6.a(var7)) {
               throw new CommandException("commands.clone.noOverlap", new Object[0]);
            } else {
               if(var2.length >= 11 && var2[10].equals("move")) {
                  var9 = true;
               }

               if(var6.b >= 0 && var6.e < 256 && var7.b >= 0 && var7.e < 256) {
                  World var12 = var1.getWorld();
                  if(var12.a(var6) && var12.a(var7)) {
                     boolean var13 = false;
                     if(var2.length >= 10) {
                        if(var2[9].equals("masked")) {
                           var13 = true;
                        } else if(var2[9].equals("filtered")) {
                           if(var2.length < 12) {
                              throw new ExceptionUsage("commands.clone.usage", new Object[0]);
                           }

                           var10 = g(var1, var2[11]);
                           if(var2.length >= 13) {
                              var11 = a(var2[12], 0, 15);
                           }
                        }
                     }

                     ArrayList var14 = Lists.newArrayList();
                     ArrayList var15 = Lists.newArrayList();
                     ArrayList var16 = Lists.newArrayList();
                     LinkedList var17 = Lists.newLinkedList();
                     BlockPosition var18 = new BlockPosition(var7.a - var6.a, var7.b - var6.b, var7.c - var6.c);

                     for(int var19 = var6.c; var19 <= var6.f; ++var19) {
                        for(int var20 = var6.b; var20 <= var6.e; ++var20) {
                           for(int var21 = var6.a; var21 <= var6.d; ++var21) {
                              BlockPosition var22 = new BlockPosition(var21, var20, var19);
                              BlockPosition var23 = var22.a(var18);
                              IBlockData var24 = var12.getType(var22);
                              if((!var13 || var24.getBlock() != Blocks.AIR) && (var10 == null || var24.getBlock() == var10 && (var11 < 0 || var24.getBlock().toLegacyData(var24) == var11))) {
                                 TileEntity var25 = var12.getTileEntity(var22);
                                 if(var25 != null) {
                                    NBTTagCompound var26 = new NBTTagCompound();
                                    var25.b(var26);
                                    var15.add(new CommandClone.CommandCloneStoredTileEntity(var23, var24, var26));
                                    var17.addLast(var22);
                                 } else if(!var24.getBlock().o() && !var24.getBlock().d()) {
                                    var16.add(new CommandClone.CommandCloneStoredTileEntity(var23, var24, (NBTTagCompound)null));
                                    var17.addFirst(var22);
                                 } else {
                                    var14.add(new CommandClone.CommandCloneStoredTileEntity(var23, var24, (NBTTagCompound)null));
                                    var17.addLast(var22);
                                 }
                              }
                           }
                        }
                     }

                     if(var9) {
                        Iterator var27;
                        BlockPosition var29;
                        for(var27 = var17.iterator(); var27.hasNext(); var12.setTypeAndData(var29, Blocks.BARRIER.getBlockData(), 2)) {
                           var29 = (BlockPosition)var27.next();
                           TileEntity var31 = var12.getTileEntity(var29);
                           if(var31 instanceof IInventory) {
                              ((IInventory)var31).l();
                           }
                        }

                        var27 = var17.iterator();

                        while(var27.hasNext()) {
                           var29 = (BlockPosition)var27.next();
                           var12.setTypeAndData(var29, Blocks.AIR.getBlockData(), 3);
                        }
                     }

                     ArrayList var28 = Lists.newArrayList();
                     var28.addAll(var14);
                     var28.addAll(var15);
                     var28.addAll(var16);
                     List var30 = Lists.reverse(var28);

                     Iterator var32;
                     CommandClone.CommandCloneStoredTileEntity var33;
                     TileEntity var34;
                     for(var32 = var30.iterator(); var32.hasNext(); var12.setTypeAndData(var33.a, Blocks.BARRIER.getBlockData(), 2)) {
                        var33 = (CommandClone.CommandCloneStoredTileEntity)var32.next();
                        var34 = var12.getTileEntity(var33.a);
                        if(var34 instanceof IInventory) {
                           ((IInventory)var34).l();
                        }
                     }

                     var8 = 0;
                     var32 = var28.iterator();

                     while(var32.hasNext()) {
                        var33 = (CommandClone.CommandCloneStoredTileEntity)var32.next();
                        if(var12.setTypeAndData(var33.a, var33.b, 2)) {
                           ++var8;
                        }
                     }

                     for(var32 = var15.iterator(); var32.hasNext(); var12.setTypeAndData(var33.a, var33.b, 2)) {
                        var33 = (CommandClone.CommandCloneStoredTileEntity)var32.next();
                        var34 = var12.getTileEntity(var33.a);
                        if(var33.c != null && var34 != null) {
                           var33.c.setInt("x", var33.a.getX());
                           var33.c.setInt("y", var33.a.getY());
                           var33.c.setInt("z", var33.a.getZ());
                           var34.a(var33.c);
                           var34.update();
                        }
                     }

                     var32 = var30.iterator();

                     while(var32.hasNext()) {
                        var33 = (CommandClone.CommandCloneStoredTileEntity)var32.next();
                        var12.update(var33.a, var33.b.getBlock());
                     }

                     List var35 = var12.a(var6, false);
                     if(var35 != null) {
                        Iterator var36 = var35.iterator();

                        while(var36.hasNext()) {
                           NextTickListEntry var37 = (NextTickListEntry)var36.next();
                           if(var6.b((BaseBlockPosition)var37.a)) {
                              BlockPosition var38 = var37.a.a(var18);
                              var12.b(var38, var37.a(), (int)(var37.b - var12.getWorldData().getTime()), var37.c);
                           }
                        }
                     }

                     if(var8 <= 0) {
                        throw new CommandException("commands.clone.failed", new Object[0]);
                     } else {
                        var1.a(CommandObjectiveExecutor.EnumCommandResult.AFFECTED_BLOCKS, var8);
                        a(var1, this, "commands.clone.success", new Object[]{Integer.valueOf(var8)});
                     }
                  } else {
                     throw new CommandException("commands.clone.outOfWorld", new Object[0]);
                  }
               } else {
                  throw new CommandException("commands.clone.outOfWorld", new Object[0]);
               }
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length > 0 && var2.length <= 3?a(var2, 0, var3):(var2.length > 3 && var2.length <= 6?a(var2, 3, var3):(var2.length > 6 && var2.length <= 9?a(var2, 6, var3):(var2.length == 10?a(var2, new String[]{"replace", "masked", "filtered"}):(var2.length == 11?a(var2, new String[]{"normal", "force", "move"}):(var2.length == 12 && "filtered".equals(var2[9])?a(var2, Block.REGISTRY.keySet()):null)))));
   }

   static class CommandCloneStoredTileEntity {
      public final BlockPosition a;
      public final IBlockData b;
      public final NBTTagCompound c;

      public CommandCloneStoredTileEntity(BlockPosition var1, IBlockData var2, NBTTagCompound var3) {
         this.a = var1;
         this.b = var2;
         this.c = var3;
      }
   }
}
