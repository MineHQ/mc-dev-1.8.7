package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.NBTBase;
import net.minecraft.server.NBTReadLimiter;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagEnd;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagIntArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase {
   private static final Logger b = LogManager.getLogger();
   private List<NBTBase> list = Lists.newArrayList();
   private byte type = 0;

   public NBTTagList() {
   }

   void write(DataOutput var1) throws IOException {
      if(!this.list.isEmpty()) {
         this.type = ((NBTBase)this.list.get(0)).getTypeId();
      } else {
         this.type = 0;
      }

      var1.writeByte(this.type);
      var1.writeInt(this.list.size());

      for(int var2 = 0; var2 < this.list.size(); ++var2) {
         ((NBTBase)this.list.get(var2)).write(var1);
      }

   }

   void load(DataInput var1, int var2, NBTReadLimiter var3) throws IOException {
      var3.a(296L);
      if(var2 > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.type = var1.readByte();
         int var4 = var1.readInt();
         if(this.type == 0 && var4 > 0) {
            throw new RuntimeException("Missing type on ListTag");
         } else {
            var3.a(32L * (long)var4);
            this.list = Lists.newArrayListWithCapacity(var4);

            for(int var5 = 0; var5 < var4; ++var5) {
               NBTBase var6 = NBTBase.createTag(this.type);
               var6.load(var1, var2 + 1, var3);
               this.list.add(var6);
            }

         }
      }
   }

   public byte getTypeId() {
      return (byte)9;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("[");

      for(int var2 = 0; var2 < this.list.size(); ++var2) {
         if(var2 != 0) {
            var1.append(',');
         }

         var1.append(var2).append(':').append(this.list.get(var2));
      }

      return var1.append(']').toString();
   }

   public void add(NBTBase var1) {
      if(var1.getTypeId() == 0) {
         b.warn("Invalid TagEnd added to ListTag");
      } else {
         if(this.type == 0) {
            this.type = var1.getTypeId();
         } else if(this.type != var1.getTypeId()) {
            b.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.list.add(var1);
      }
   }

   public void a(int var1, NBTBase var2) {
      if(var2.getTypeId() == 0) {
         b.warn("Invalid TagEnd added to ListTag");
      } else if(var1 >= 0 && var1 < this.list.size()) {
         if(this.type == 0) {
            this.type = var2.getTypeId();
         } else if(this.type != var2.getTypeId()) {
            b.warn("Adding mismatching tag types to tag list");
            return;
         }

         this.list.set(var1, var2);
      } else {
         b.warn("index out of bounds to set tag in tag list");
      }
   }

   public NBTBase a(int var1) {
      return (NBTBase)this.list.remove(var1);
   }

   public boolean isEmpty() {
      return this.list.isEmpty();
   }

   public NBTTagCompound get(int var1) {
      if(var1 >= 0 && var1 < this.list.size()) {
         NBTBase var2 = (NBTBase)this.list.get(var1);
         return var2.getTypeId() == 10?(NBTTagCompound)var2:new NBTTagCompound();
      } else {
         return new NBTTagCompound();
      }
   }

   public int[] c(int var1) {
      if(var1 >= 0 && var1 < this.list.size()) {
         NBTBase var2 = (NBTBase)this.list.get(var1);
         return var2.getTypeId() == 11?((NBTTagIntArray)var2).c():new int[0];
      } else {
         return new int[0];
      }
   }

   public double d(int var1) {
      if(var1 >= 0 && var1 < this.list.size()) {
         NBTBase var2 = (NBTBase)this.list.get(var1);
         return var2.getTypeId() == 6?((NBTTagDouble)var2).g():0.0D;
      } else {
         return 0.0D;
      }
   }

   public float e(int var1) {
      if(var1 >= 0 && var1 < this.list.size()) {
         NBTBase var2 = (NBTBase)this.list.get(var1);
         return var2.getTypeId() == 5?((NBTTagFloat)var2).h():0.0F;
      } else {
         return 0.0F;
      }
   }

   public String getString(int var1) {
      if(var1 >= 0 && var1 < this.list.size()) {
         NBTBase var2 = (NBTBase)this.list.get(var1);
         return var2.getTypeId() == 8?var2.a_():var2.toString();
      } else {
         return "";
      }
   }

   public NBTBase g(int var1) {
      return (NBTBase)(var1 >= 0 && var1 < this.list.size()?(NBTBase)this.list.get(var1):new NBTTagEnd());
   }

   public int size() {
      return this.list.size();
   }

   public NBTBase clone() {
      NBTTagList var1 = new NBTTagList();
      var1.type = this.type;
      Iterator var2 = this.list.iterator();

      while(var2.hasNext()) {
         NBTBase var3 = (NBTBase)var2.next();
         NBTBase var4 = var3.clone();
         var1.list.add(var4);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      if(super.equals(var1)) {
         NBTTagList var2 = (NBTTagList)var1;
         if(this.type == var2.type) {
            return this.list.equals(var2.list);
         }
      }

      return false;
   }

   public int hashCode() {
      return super.hashCode() ^ this.list.hashCode();
   }

   public int f() {
      return this.type;
   }
}
