package com.sun.jna.platform.dnd;

import com.sun.jna.platform.dnd.GhostedDragImage;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.JTextComponent;

public abstract class DragHandler implements DragSourceListener, DragSourceMotionListener, DragGestureListener {
   public static final Dimension MAX_GHOST_SIZE = new Dimension(250, 250);
   public static final float DEFAULT_GHOST_ALPHA = 0.5F;
   public static final int UNKNOWN_MODIFIERS = -1;
   public static final Transferable UNKNOWN_TRANSFERABLE = null;
   protected static final int MOVE = 2;
   protected static final int COPY = 1;
   protected static final int LINK = 1073741824;
   protected static final int NONE = 0;
   static final int MOVE_MASK = 64;
   static final boolean OSX = System.getProperty("os.name").toLowerCase().indexOf("mac") != -1;
   static final int COPY_MASK;
   static final int LINK_MASK;
   static final int KEY_MASK = 9152;
   private static int modifiers;
   private static Transferable transferable;
   private int supportedActions;
   private boolean fixCursor = true;
   private Component dragSource;
   private GhostedDragImage ghost;
   private Point imageOffset;
   private Dimension maxGhostSize;
   private float ghostAlpha;
   private String lastAction;
   private boolean moved;

   static int getModifiers() {
      return modifiers;
   }

   public static Transferable getTransferable(DropTargetEvent var0) {
      if(var0 instanceof DropTargetDragEvent) {
         try {
            return (Transferable)var0.getClass().getMethod("getTransferable", (Class[])null).invoke(var0, (Object[])null);
         } catch (Exception var2) {
            ;
         }
      } else if(var0 instanceof DropTargetDropEvent) {
         return ((DropTargetDropEvent)var0).getTransferable();
      }

      return transferable;
   }

   protected DragHandler(Component var1, int var2) {
      this.maxGhostSize = MAX_GHOST_SIZE;
      this.ghostAlpha = 0.5F;
      this.dragSource = var1;
      this.supportedActions = var2;

      try {
         String var3 = System.getProperty("DragHandler.alpha");
         if(var3 != null) {
            try {
               this.ghostAlpha = Float.parseFloat(var3);
            } catch (NumberFormatException var8) {
               ;
            }
         }

         String var4 = System.getProperty("DragHandler.maxDragImageSize");
         if(var4 != null) {
            String[] var5 = var4.split("x");
            if(var5.length == 2) {
               try {
                  this.maxGhostSize = new Dimension(Integer.parseInt(var5[0]), Integer.parseInt(var5[1]));
               } catch (NumberFormatException var7) {
                  ;
               }
            }
         }
      } catch (SecurityException var9) {
         ;
      }

      this.disableSwingDragSupport(var1);
      DragSource var10 = DragSource.getDefaultDragSource();
      var10.createDefaultDragGestureRecognizer(var1, this.supportedActions, this);
   }

   private void disableSwingDragSupport(Component var1) {
      if(var1 instanceof JTree) {
         ((JTree)var1).setDragEnabled(false);
      } else if(var1 instanceof JList) {
         ((JList)var1).setDragEnabled(false);
      } else if(var1 instanceof JTable) {
         ((JTable)var1).setDragEnabled(false);
      } else if(var1 instanceof JTextComponent) {
         ((JTextComponent)var1).setDragEnabled(false);
      } else if(var1 instanceof JColorChooser) {
         ((JColorChooser)var1).setDragEnabled(false);
      } else if(var1 instanceof JFileChooser) {
         ((JFileChooser)var1).setDragEnabled(false);
      }

   }

   protected boolean canDrag(DragGestureEvent var1) {
      int var2 = var1.getTriggerEvent().getModifiersEx() & 9152;
      return var2 == 64?(this.supportedActions & 2) != 0:(var2 == COPY_MASK?(this.supportedActions & 1) != 0:(var2 == LINK_MASK?(this.supportedActions & 1073741824) != 0:true));
   }

   protected void setModifiers(int var1) {
      modifiers = var1;
   }

   protected abstract Transferable getTransferable(DragGestureEvent var1);

   protected Icon getDragIcon(DragGestureEvent var1, Point var2) {
      return null;
   }

   protected void dragStarted(DragGestureEvent var1) {
   }

   public void dragGestureRecognized(DragGestureEvent var1) {
      if((var1.getDragAction() & this.supportedActions) != 0 && this.canDrag(var1)) {
         this.setModifiers(var1.getTriggerEvent().getModifiersEx() & 9152);
         Transferable var2 = this.getTransferable(var1);
         if(var2 == null) {
            return;
         }

         try {
            Point var3 = new Point(0, 0);
            Icon var4 = this.getDragIcon(var1, var3);
            Point var5 = var1.getDragOrigin();
            this.imageOffset = new Point(var3.x - var5.x, var3.y - var5.y);
            Icon var6 = this.scaleDragIcon(var4, this.imageOffset);
            Object var7 = null;
            if(var6 != null && DragSource.isDragImageSupported()) {
               GraphicsConfiguration var11 = var1.getComponent().getGraphicsConfiguration();
               var1.startDrag((Cursor)var7, this.createDragImage(var11, var6), this.imageOffset, var2, this);
            } else {
               if(var6 != null) {
                  Point var8 = this.dragSource.getLocationOnScreen();
                  var8.translate(var5.x, var5.y);
                  Point var9 = new Point(-this.imageOffset.x, -this.imageOffset.y);
                  this.ghost = new GhostedDragImage(this.dragSource, var6, this.getImageLocation(var8), var9);
                  this.ghost.setAlpha(this.ghostAlpha);
               }

               var1.startDrag((Cursor)var7, var2, this);
            }

            this.dragStarted(var1);
            this.moved = false;
            var1.getDragSource().addDragSourceMotionListener(this);
            transferable = var2;
         } catch (InvalidDnDOperationException var10) {
            if(this.ghost != null) {
               this.ghost.dispose();
               this.ghost = null;
            }
         }
      }

   }

   protected Icon scaleDragIcon(Icon var1, Point var2) {
      return var1;
   }

   protected Image createDragImage(GraphicsConfiguration var1, Icon var2) {
      int var3 = var2.getIconWidth();
      int var4 = var2.getIconHeight();
      BufferedImage var5 = var1.createCompatibleImage(var3, var4, 3);
      Graphics2D var6 = (Graphics2D)var5.getGraphics();
      var6.setComposite(AlphaComposite.Clear);
      var6.fillRect(0, 0, var3, var4);
      var6.setComposite(AlphaComposite.getInstance(2, this.ghostAlpha));
      var2.paintIcon(this.dragSource, var6, 0, 0);
      var6.dispose();
      return var5;
   }

   private int reduce(int var1) {
      return (var1 & 2) != 0 && var1 != 2?2:((var1 & 1) != 0 && var1 != 1?1:var1);
   }

   protected Cursor getCursorForAction(int var1) {
      switch(var1) {
      case 1:
         return DragSource.DefaultCopyDrop;
      case 2:
         return DragSource.DefaultMoveDrop;
      case 1073741824:
         return DragSource.DefaultLinkDrop;
      default:
         return DragSource.DefaultMoveNoDrop;
      }
   }

   protected int getAcceptableDropAction(int var1) {
      return this.reduce(this.supportedActions & var1);
   }

   protected int getDropAction(DragSourceEvent var1) {
      if(var1 instanceof DragSourceDragEvent) {
         DragSourceDragEvent var2 = (DragSourceDragEvent)var1;
         return var2.getDropAction();
      } else {
         return var1 instanceof DragSourceDropEvent?((DragSourceDropEvent)var1).getDropAction():0;
      }
   }

   protected int adjustDropAction(DragSourceEvent var1) {
      int var2 = this.getDropAction(var1);
      if(var1 instanceof DragSourceDragEvent) {
         DragSourceDragEvent var3 = (DragSourceDragEvent)var1;
         if(var2 == 0) {
            int var4 = var3.getGestureModifiersEx() & 9152;
            if(var4 == 0) {
               var2 = this.getAcceptableDropAction(var3.getTargetActions());
            }
         }
      }

      return var2;
   }

   protected void updateCursor(DragSourceEvent var1) {
      if(this.fixCursor) {
         Cursor var2 = this.getCursorForAction(this.adjustDropAction(var1));
         var1.getDragSourceContext().setCursor(var2);
      }
   }

   static String actionString(int var0) {
      switch(var0) {
      case 1:
         return "COPY";
      case 2:
         return "MOVE";
      case 3:
         return "MOVE|COPY";
      case 1073741824:
         return "LINK";
      case 1073741825:
         return "COPY|LINK";
      case 1073741826:
         return "MOVE|LINK";
      case 1073741827:
         return "MOVE|COPY|LINK";
      default:
         return "NONE";
      }
   }

   private void describe(String var1, DragSourceEvent var2) {
   }

   public void dragDropEnd(DragSourceDropEvent var1) {
      this.describe("end", var1);
      this.setModifiers(-1);
      transferable = UNKNOWN_TRANSFERABLE;
      if(this.ghost != null) {
         if(var1.getDropSuccess()) {
            this.ghost.dispose();
         } else {
            this.ghost.returnToOrigin();
         }

         this.ghost = null;
      }

      DragSource var2 = var1.getDragSourceContext().getDragSource();
      var2.removeDragSourceMotionListener(this);
      this.moved = false;
   }

   private Point getImageLocation(Point var1) {
      var1.translate(this.imageOffset.x, this.imageOffset.y);
      return var1;
   }

   public void dragEnter(DragSourceDragEvent var1) {
      this.describe("enter", var1);
      if(this.ghost != null) {
         this.ghost.move(this.getImageLocation(var1.getLocation()));
      }

      this.updateCursor(var1);
   }

   public void dragMouseMoved(DragSourceDragEvent var1) {
      this.describe("move", var1);
      if(this.ghost != null) {
         this.ghost.move(this.getImageLocation(var1.getLocation()));
      }

      if(this.moved) {
         this.updateCursor(var1);
      }

      this.moved = true;
   }

   public void dragOver(DragSourceDragEvent var1) {
      this.describe("over", var1);
      if(this.ghost != null) {
         this.ghost.move(this.getImageLocation(var1.getLocation()));
      }

      this.updateCursor(var1);
   }

   public void dragExit(DragSourceEvent var1) {
      this.describe("exit", var1);
   }

   public void dropActionChanged(DragSourceDragEvent var1) {
      this.describe("change", var1);
      this.setModifiers(var1.getGestureModifiersEx() & 9152);
      if(this.ghost != null) {
         this.ghost.move(this.getImageLocation(var1.getLocation()));
      }

      this.updateCursor(var1);
   }

   static {
      COPY_MASK = OSX?512:128;
      LINK_MASK = OSX?768:192;
      modifiers = -1;
      transferable = UNKNOWN_TRANSFERABLE;
   }
}
