package net.minecraft.server;

public class MapIcon {
   private byte type;
   private byte x;
   private byte y;
   private byte rotation;

   public MapIcon(byte var1, byte var2, byte var3, byte var4) {
      this.type = var1;
      this.x = var2;
      this.y = var3;
      this.rotation = var4;
   }

   public MapIcon(MapIcon var1) {
      this.type = var1.type;
      this.x = var1.x;
      this.y = var1.y;
      this.rotation = var1.rotation;
   }

   public byte getType() {
      return this.type;
   }

   public byte getX() {
      return this.x;
   }

   public byte getY() {
      return this.y;
   }

   public byte getRotation() {
      return this.rotation;
   }

   public boolean equals(Object var1) {
      if(this == var1) {
         return true;
      } else if(!(var1 instanceof MapIcon)) {
         return false;
      } else {
         MapIcon var2 = (MapIcon)var1;
         return this.type != var2.type?false:(this.rotation != var2.rotation?false:(this.x != var2.x?false:this.y == var2.y));
      }
   }

   public int hashCode() {
      byte var1 = this.type;
      int var2 = 31 * var1 + this.x;
      var2 = 31 * var2 + this.y;
      var2 = 31 * var2 + this.rotation;
      return var2;
   }
}
