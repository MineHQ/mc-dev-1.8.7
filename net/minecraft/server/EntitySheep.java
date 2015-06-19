package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;
import net.minecraft.server.Container;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.EntityAgeable;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EnumColor;
import net.minecraft.server.GenericAttributes;
import net.minecraft.server.GroupDataEntity;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Items;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathfinderGoalBreed;
import net.minecraft.server.PathfinderGoalEatTile;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalFollowParent;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalPanic;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalTempt;
import net.minecraft.server.World;

public class EntitySheep extends EntityAnimal {
   private final InventoryCrafting bm = new InventoryCrafting(new Container() {
      public boolean a(EntityHuman var1) {
         return false;
      }
   }, 2, 1);
   private static final Map<EnumColor, float[]> bo = Maps.newEnumMap(EnumColor.class);
   private int bp;
   private PathfinderGoalEatTile bq = new PathfinderGoalEatTile(this);

   public static float[] a(EnumColor var0) {
      return (float[])bo.get(var0);
   }

   public EntitySheep(World var1) {
      super(var1);
      this.setSize(0.9F, 1.3F);
      ((Navigation)this.getNavigation()).a(true);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
      this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
      this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.1D, Items.WHEAT, false));
      this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
      this.goalSelector.a(5, this.bq);
      this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
      this.bm.setItem(0, new ItemStack(Items.DYE, 1, 0));
      this.bm.setItem(1, new ItemStack(Items.DYE, 1, 0));
   }

   protected void E() {
      this.bp = this.bq.f();
      super.E();
   }

   public void m() {
      if(this.world.isClientSide) {
         this.bp = Math.max(0, this.bp - 1);
      }

      super.m();
   }

   protected void initAttributes() {
      super.initAttributes();
      this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
      this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.23000000417232513D);
   }

   protected void h() {
      super.h();
      this.datawatcher.a(16, new Byte((byte)0));
   }

   protected void dropDeathLoot(boolean var1, int var2) {
      if(!this.isSheared()) {
         this.a(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, this.getColor().getColorIndex()), 0.0F);
      }

      int var3 = this.random.nextInt(2) + 1 + this.random.nextInt(1 + var2);

      for(int var4 = 0; var4 < var3; ++var4) {
         if(this.isBurning()) {
            this.a(Items.COOKED_MUTTON, 1);
         } else {
            this.a(Items.MUTTON, 1);
         }
      }

   }

   protected Item getLoot() {
      return Item.getItemOf(Blocks.WOOL);
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && var2.getItem() == Items.SHEARS && !this.isSheared() && !this.isBaby()) {
         if(!this.world.isClientSide) {
            this.setSheared(true);
            int var3 = 1 + this.random.nextInt(3);

            for(int var4 = 0; var4 < var3; ++var4) {
               EntityItem var5 = this.a(new ItemStack(Item.getItemOf(Blocks.WOOL), 1, this.getColor().getColorIndex()), 1.0F);
               var5.motY += (double)(this.random.nextFloat() * 0.05F);
               var5.motX += (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
               var5.motZ += (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
            }
         }

         var2.damage(1, var1);
         this.makeSound("mob.sheep.shear", 1.0F, 1.0F);
      }

      return super.a(var1);
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setBoolean("Sheared", this.isSheared());
      var1.setByte("Color", (byte)this.getColor().getColorIndex());
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.setSheared(var1.getBoolean("Sheared"));
      this.setColor(EnumColor.fromColorIndex(var1.getByte("Color")));
   }

   protected String z() {
      return "mob.sheep.say";
   }

   protected String bo() {
      return "mob.sheep.say";
   }

   protected String bp() {
      return "mob.sheep.say";
   }

   protected void a(BlockPosition var1, Block var2) {
      this.makeSound("mob.sheep.step", 0.15F, 1.0F);
   }

   public EnumColor getColor() {
      return EnumColor.fromColorIndex(this.datawatcher.getByte(16) & 15);
   }

   public void setColor(EnumColor var1) {
      byte var2 = this.datawatcher.getByte(16);
      this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & 240 | var1.getColorIndex() & 15)));
   }

   public boolean isSheared() {
      return (this.datawatcher.getByte(16) & 16) != 0;
   }

   public void setSheared(boolean var1) {
      byte var2 = this.datawatcher.getByte(16);
      if(var1) {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 | 16)));
      } else {
         this.datawatcher.watch(16, Byte.valueOf((byte)(var2 & -17)));
      }

   }

   public static EnumColor a(Random var0) {
      int var1 = var0.nextInt(100);
      return var1 < 5?EnumColor.BLACK:(var1 < 10?EnumColor.GRAY:(var1 < 15?EnumColor.SILVER:(var1 < 18?EnumColor.BROWN:(var0.nextInt(500) == 0?EnumColor.PINK:EnumColor.WHITE))));
   }

   public EntitySheep b(EntityAgeable var1) {
      EntitySheep var2 = (EntitySheep)var1;
      EntitySheep var3 = new EntitySheep(this.world);
      var3.setColor(this.a((EntityAnimal)this, (EntityAnimal)var2));
      return var3;
   }

   public void v() {
      this.setSheared(false);
      if(this.isBaby()) {
         this.setAge(60);
      }

   }

   public GroupDataEntity prepare(DifficultyDamageScaler var1, GroupDataEntity var2) {
      var2 = super.prepare(var1, var2);
      this.setColor(a(this.world.random));
      return var2;
   }

   private EnumColor a(EntityAnimal var1, EntityAnimal var2) {
      int var3 = ((EntitySheep)var1).getColor().getInvColorIndex();
      int var4 = ((EntitySheep)var2).getColor().getInvColorIndex();
      this.bm.getItem(0).setData(var3);
      this.bm.getItem(1).setData(var4);
      ItemStack var5 = CraftingManager.getInstance().craft(this.bm, ((EntitySheep)var1).world);
      int var6;
      if(var5 != null && var5.getItem() == Items.DYE) {
         var6 = var5.getData();
      } else {
         var6 = this.world.random.nextBoolean()?var3:var4;
      }

      return EnumColor.fromInvColorIndex(var6);
   }

   public float getHeadHeight() {
      return 0.95F * this.length;
   }

   // $FF: synthetic method
   public EntityAgeable createChild(EntityAgeable var1) {
      return this.b(var1);
   }

   static {
      bo.put(EnumColor.WHITE, new float[]{1.0F, 1.0F, 1.0F});
      bo.put(EnumColor.ORANGE, new float[]{0.85F, 0.5F, 0.2F});
      bo.put(EnumColor.MAGENTA, new float[]{0.7F, 0.3F, 0.85F});
      bo.put(EnumColor.LIGHT_BLUE, new float[]{0.4F, 0.6F, 0.85F});
      bo.put(EnumColor.YELLOW, new float[]{0.9F, 0.9F, 0.2F});
      bo.put(EnumColor.LIME, new float[]{0.5F, 0.8F, 0.1F});
      bo.put(EnumColor.PINK, new float[]{0.95F, 0.5F, 0.65F});
      bo.put(EnumColor.GRAY, new float[]{0.3F, 0.3F, 0.3F});
      bo.put(EnumColor.SILVER, new float[]{0.6F, 0.6F, 0.6F});
      bo.put(EnumColor.CYAN, new float[]{0.3F, 0.5F, 0.6F});
      bo.put(EnumColor.PURPLE, new float[]{0.5F, 0.25F, 0.7F});
      bo.put(EnumColor.BLUE, new float[]{0.2F, 0.3F, 0.7F});
      bo.put(EnumColor.BROWN, new float[]{0.4F, 0.3F, 0.2F});
      bo.put(EnumColor.GREEN, new float[]{0.4F, 0.5F, 0.2F});
      bo.put(EnumColor.RED, new float[]{0.6F, 0.2F, 0.2F});
      bo.put(EnumColor.BLACK, new float[]{0.1F, 0.1F, 0.1F});
   }
}
