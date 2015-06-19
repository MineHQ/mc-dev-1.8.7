package net.minecraft.server;

import java.util.Vector;
import javax.swing.JList;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.IUpdatePlayerListBox;
import net.minecraft.server.MinecraftServer;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {
   private MinecraftServer a;
   private int b;

   public PlayerListBox(MinecraftServer var1) {
      this.a = var1;
      var1.a((IUpdatePlayerListBox)this);
   }

   public void c() {
      if(this.b++ % 20 == 0) {
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < this.a.getPlayerList().v().size(); ++var2) {
            var1.add(((EntityPlayer)this.a.getPlayerList().v().get(var2)).getName());
         }

         this.setListData(var1);
      }

   }
}
