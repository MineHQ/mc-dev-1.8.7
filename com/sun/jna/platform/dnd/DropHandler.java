package com.sun.jna.platform.dnd;

import com.sun.jna.platform.dnd.DragHandler;
import com.sun.jna.platform.dnd.DropTargetPainter;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class DropHandler implements DropTargetListener {
   private int acceptedActions;
   private List<DataFlavor> acceptedFlavors;
   private DropTarget dropTarget;
   private boolean active;
   private DropTargetPainter painter;
   private String lastAction;

   public DropHandler(Component var1, int var2) {
      this(var1, var2, new DataFlavor[0]);
   }

   public DropHandler(Component var1, int var2, DataFlavor[] var3) {
      this(var1, var2, var3, (DropTargetPainter)null);
   }

   public DropHandler(Component var1, int var2, DataFlavor[] var3, DropTargetPainter var4) {
      this.active = true;
      this.acceptedActions = var2;
      this.acceptedFlavors = Arrays.asList(var3);
      this.painter = var4;
      this.dropTarget = new DropTarget(var1, var2, this, this.active);
   }

   protected DropTarget getDropTarget() {
      return this.dropTarget;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean var1) {
      this.active = var1;
      if(this.dropTarget != null) {
         this.dropTarget.setActive(var1);
      }

   }

   protected int getDropActionsForFlavors(DataFlavor[] var1) {
      return this.acceptedActions;
   }

   protected int getDropAction(DropTargetEvent var1) {
      int var2 = 0;
      int var3 = 0;
      Point var4 = null;
      DataFlavor[] var5 = new DataFlavor[0];
      if(var1 instanceof DropTargetDragEvent) {
         DropTargetDragEvent var6 = (DropTargetDragEvent)var1;
         var2 = var6.getDropAction();
         var3 = var6.getSourceActions();
         var5 = var6.getCurrentDataFlavors();
         var4 = var6.getLocation();
      } else if(var1 instanceof DropTargetDropEvent) {
         DropTargetDropEvent var7 = (DropTargetDropEvent)var1;
         var2 = var7.getDropAction();
         var3 = var7.getSourceActions();
         var5 = var7.getCurrentDataFlavors();
         var4 = var7.getLocation();
      }

      if(this.isSupported(var5)) {
         int var8 = this.getDropActionsForFlavors(var5);
         var2 = this.getDropAction(var1, var2, var3, var8);
         if(var2 != 0 && this.canDrop(var1, var2, var4)) {
            return var2;
         }
      }

      return 0;
   }

   protected int getDropAction(DropTargetEvent var1, int var2, int var3, int var4) {
      boolean var5 = this.modifiersActive(var2);
      int var6;
      if((var2 & var4) == 0 && !var5) {
         var6 = var4 & var3;
         var2 = var6;
      } else if(var5) {
         var6 = var2 & var4 & var3;
         if(var6 != var2) {
            var2 = var6;
         }
      }

      return var2;
   }

   protected boolean modifiersActive(int var1) {
      int var2 = DragHandler.getModifiers();
      return var2 == -1?var1 == 1073741824 || var1 == 1:var2 != 0;
   }

   private void describe(String var1, DropTargetEvent var2) {
   }

   protected int acceptOrReject(DropTargetDragEvent var1) {
      int var2 = this.getDropAction(var1);
      if(var2 != 0) {
         var1.acceptDrag(var2);
      } else {
         var1.rejectDrag();
      }

      return var2;
   }

   public void dragEnter(DropTargetDragEvent var1) {
      this.describe("enter(tgt)", var1);
      int var2 = this.acceptOrReject(var1);
      this.paintDropTarget(var1, var2, var1.getLocation());
   }

   public void dragOver(DropTargetDragEvent var1) {
      this.describe("over(tgt)", var1);
      int var2 = this.acceptOrReject(var1);
      this.paintDropTarget(var1, var2, var1.getLocation());
   }

   public void dragExit(DropTargetEvent var1) {
      this.describe("exit(tgt)", var1);
      this.paintDropTarget(var1, 0, (Point)null);
   }

   public void dropActionChanged(DropTargetDragEvent var1) {
      this.describe("change(tgt)", var1);
      int var2 = this.acceptOrReject(var1);
      this.paintDropTarget(var1, var2, var1.getLocation());
   }

   public void drop(DropTargetDropEvent var1) {
      this.describe("drop(tgt)", var1);
      int var2 = this.getDropAction(var1);
      if(var2 != 0) {
         var1.acceptDrop(var2);

         try {
            this.drop(var1, var2);
            var1.dropComplete(true);
         } catch (Exception var4) {
            var1.dropComplete(false);
         }
      } else {
         var1.rejectDrop();
      }

      this.paintDropTarget(var1, 0, var1.getLocation());
   }

   protected boolean isSupported(DataFlavor[] var1) {
      HashSet var2 = new HashSet(Arrays.asList(var1));
      var2.retainAll(this.acceptedFlavors);
      return !var2.isEmpty();
   }

   protected void paintDropTarget(DropTargetEvent var1, int var2, Point var3) {
      if(this.painter != null) {
         this.painter.paintDropTarget(var1, var2, var3);
      }

   }

   protected boolean canDrop(DropTargetEvent var1, int var2, Point var3) {
      return true;
   }

   protected abstract void drop(DropTargetDropEvent var1, int var2) throws UnsupportedFlavorException, IOException;
}
