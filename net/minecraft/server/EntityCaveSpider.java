package net.minecraft.server;

import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySpider;
import net.minecraft.server.EnumDifficulty;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.MobEffect;
import net.minecraft.server.MobEffectList;
import net.minecraft.server.World;

public class EntityCaveSpider extends EntitySpider {
   public EntityCaveSpider(World var1) {
      super(var1);
      this.setSize(0.7F, 0.5F);
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(12.0D);
   }

   public boolean r(Entity var1) {
      if(super.r(var1)) {
         if(var1 instanceof EntityLiving) {
            byte var2 = 0;
            if(this.world.getDifficulty() == EnumDifficulty.NORMAL) {
               var2 = 7;
            } else if(this.world.getDifficulty() == EnumDifficulty.HARD) {
               var2 = 15;
            }

            if(var2 > 0) {
               ((EntityLiving)var1).addEffect(new MobEffect(MobEffectList.POISON.id, var2 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      return var2;
   }

   public float getHeadHeight() {
      return 0.45F;
   }
}
