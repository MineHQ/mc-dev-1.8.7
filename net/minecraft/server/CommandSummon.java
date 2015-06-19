package net.minecraft.server;

import java.util.List;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.CommandAbstract;
import net.minecraft.server.CommandException;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.EntityLightning;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.ExceptionUsage;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.MojangsonParseException;
import net.minecraft.server.MojangsonParser;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Vec3D;
import net.minecraft.server.World;

public class CommandSummon extends CommandAbstract {
   public CommandSummon() {
   }

   public String getCommand() {
      return "summon";
   }

   public int a() {
      return 2;
   }

   public String getUsage(ICommandListener var1) {
      return "commands.summon.usage";
   }

   public void execute(ICommandListener var1, String[] var2) throws CommandException {
      if(var2.length < 1) {
         throw new ExceptionUsage("commands.summon.usage", new Object[0]);
      } else {
         String var3 = var2[0];
         BlockPosition var4 = var1.getChunkCoordinates();
         Vec3D var5 = var1.d();
         double var6 = var5.a;
         double var8 = var5.b;
         double var10 = var5.c;
         if(var2.length >= 4) {
            var6 = b(var6, var2[1], true);
            var8 = b(var8, var2[2], false);
            var10 = b(var10, var2[3], true);
            var4 = new BlockPosition(var6, var8, var10);
         }

         World var12 = var1.getWorld();
         if(!var12.isLoaded(var4)) {
            throw new CommandException("commands.summon.outOfWorld", new Object[0]);
         } else if("LightningBolt".equals(var3)) {
            var12.strikeLightning(new EntityLightning(var12, var6, var8, var10));
            a(var1, this, "commands.summon.success", new Object[0]);
         } else {
            NBTTagCompound var13 = new NBTTagCompound();
            boolean var14 = false;
            if(var2.length >= 5) {
               IChatBaseComponent var15 = a(var1, var2, 4);

               try {
                  var13 = MojangsonParser.parse(var15.c());
                  var14 = true;
               } catch (MojangsonParseException var20) {
                  throw new CommandException("commands.summon.tagError", new Object[]{var20.getMessage()});
               }
            }

            var13.setString("id", var3);

            Entity var21;
            try {
               var21 = EntityTypes.a(var13, var12);
            } catch (RuntimeException var19) {
               throw new CommandException("commands.summon.failed", new Object[0]);
            }

            if(var21 == null) {
               throw new CommandException("commands.summon.failed", new Object[0]);
            } else {
               var21.setPositionRotation(var6, var8, var10, var21.yaw, var21.pitch);
               if(!var14 && var21 instanceof EntityInsentient) {
                  ((EntityInsentient)var21).prepare(var12.E(new BlockPosition(var21)), (GroupDataEntity)null);
               }

               var12.addEntity(var21);
               Entity var16 = var21;

               for(NBTTagCompound var17 = var13; var16 != null && var17.hasKeyOfType("Riding", 10); var17 = var17.getCompound("Riding")) {
                  Entity var18 = EntityTypes.a(var17.getCompound("Riding"), var12);
                  if(var18 != null) {
                     var18.setPositionRotation(var6, var8, var10, var18.yaw, var18.pitch);
                     var12.addEntity(var18);
                     var16.mount(var18);
                  }

                  var16 = var18;
               }

               a(var1, this, "commands.summon.success", new Object[0]);
            }
         }
      }
   }

   public List<String> tabComplete(ICommandListener var1, String[] var2, BlockPosition var3) {
      return var2.length == 1?a(var2, EntityTypes.b()):(var2.length > 1 && var2.length <= 4?a(var2, 1, var3):null);
   }
}
