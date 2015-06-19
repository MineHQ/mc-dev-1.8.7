package com.sun.jna.platform;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.RasterRangesUtils;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.peer.ComponentPeer;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class WindowUtils {
   private static final String TRANSPARENT_OLD_BG = "transparent-old-bg";
   private static final String TRANSPARENT_OLD_OPAQUE = "transparent-old-opaque";
   private static final String TRANSPARENT_ALPHA = "transparent-alpha";
   public static final Shape MASK_NONE = null;

   public WindowUtils() {
   }

   private static WindowUtils.NativeWindowUtils getInstance() {
      return WindowUtils.Holder.INSTANCE;
   }

   public static void setWindowMask(Window var0, Shape var1) {
      getInstance().setWindowMask(var0, (Shape)var1);
   }

   public static void setComponentMask(Component var0, Shape var1) {
      getInstance().setWindowMask(var0, var1);
   }

   public static void setWindowMask(Window var0, Icon var1) {
      getInstance().setWindowMask(var0, (Icon)var1);
   }

   public static boolean isWindowAlphaSupported() {
      return getInstance().isWindowAlphaSupported();
   }

   public static GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
      return getInstance().getAlphaCompatibleGraphicsConfiguration();
   }

   public static void setWindowAlpha(Window var0, float var1) {
      getInstance().setWindowAlpha(var0, Math.max(0.0F, Math.min(var1, 1.0F)));
   }

   public static void setWindowTransparent(Window var0, boolean var1) {
      getInstance().setWindowTransparent(var0, var1);
   }

   // $FF: synthetic class
   static class SyntheticClass_1 {
   }

   private static class X11WindowUtils extends WindowUtils.NativeWindowUtils {
      private boolean didCheck;
      private long[] alphaVisualIDs;
      private static final long OPAQUE = 4294967295L;
      private static final String OPACITY = "_NET_WM_WINDOW_OPACITY";

      private X11WindowUtils() {
         this.alphaVisualIDs = new long[0];
      }

      private static X11.Pixmap createBitmap(X11.Display var0, X11.Window var1, Raster var2) {
         X11 var3 = X11.INSTANCE;
         Rectangle var4 = var2.getBounds();
         int var5 = var4.x + var4.width;
         int var6 = var4.y + var4.height;
         X11.Pixmap var7 = var3.XCreatePixmap(var0, var1, var5, var6, 1);
         X11.GC var8 = var3.XCreateGC(var0, var7, new NativeLong(0L), (X11.XGCValues)null);
         if(var8 == null) {
            return null;
         } else {
            var3.XSetForeground(var0, var8, new NativeLong(0L));
            var3.XFillRectangle(var0, var7, var8, 0, 0, var5, var6);
            final ArrayList var9 = new ArrayList();

            try {
               RasterRangesUtils.outputOccupiedRanges(var2, new RasterRangesUtils.RangesOutput() {
                  public boolean outputRange(int var1, int var2, int var3, int var4) {
                     var9.add(new Rectangle(var1, var2, var3, var4));
                     return true;
                  }
               });
               X11.XRectangle[] var10 = (X11.XRectangle[])((X11.XRectangle[])(new X11.XRectangle()).toArray(var9.size()));

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  Rectangle var12 = (Rectangle)var9.get(var11);
                  var10[var11].x = (short)var12.x;
                  var10[var11].y = (short)var12.y;
                  var10[var11].width = (short)var12.width;
                  var10[var11].height = (short)var12.height;
                  Pointer var13 = var10[var11].getPointer();
                  var13.setShort(0L, (short)var12.x);
                  var13.setShort(2L, (short)var12.y);
                  var13.setShort(4L, (short)var12.width);
                  var13.setShort(6L, (short)var12.height);
                  var10[var11].setAutoSynch(false);
               }

               boolean var17 = true;
               var3.XSetForeground(var0, var8, new NativeLong(1L));
               var3.XFillRectangles(var0, var7, var8, var10, var10.length);
               return var7;
            } finally {
               var3.XFreeGC(var0, var8);
            }
         }
      }

      public boolean isWindowAlphaSupported() {
         return this.getAlphaVisualIDs().length > 0;
      }

      private static long getVisualID(GraphicsConfiguration var0) {
         try {
            Object var1 = var0.getClass().getMethod("getVisual", (Class[])null).invoke(var0, (Object[])null);
            return ((Number)var1).longValue();
         } catch (Exception var2) {
            var2.printStackTrace();
            return -1L;
         }
      }

      public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
         if(this.isWindowAlphaSupported()) {
            GraphicsEnvironment var1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] var2 = var1.getScreenDevices();

            for(int var3 = 0; var3 < var2.length; ++var3) {
               GraphicsConfiguration[] var4 = var2[var3].getConfigurations();

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  long var6 = getVisualID(var4[var5]);
                  long[] var8 = this.getAlphaVisualIDs();

                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     if(var6 == var8[var9]) {
                        return var4[var5];
                     }
                  }
               }
            }
         }

         return super.getAlphaCompatibleGraphicsConfiguration();
      }

      private synchronized long[] getAlphaVisualIDs() {
         if(this.didCheck) {
            return this.alphaVisualIDs;
         } else {
            this.didCheck = true;
            X11 var1 = X11.INSTANCE;
            X11.Display var2 = var1.XOpenDisplay((String)null);
            if(var2 == null) {
               return this.alphaVisualIDs;
            } else {
               X11.XVisualInfo var3 = null;

               try {
                  int var4 = var1.XDefaultScreen(var2);
                  X11.XVisualInfo var5 = new X11.XVisualInfo();
                  var5.screen = var4;
                  var5.depth = 32;
                  var5.c_class = 4;
                  NativeLong var6 = new NativeLong(14L);
                  IntByReference var7 = new IntByReference();
                  var3 = var1.XGetVisualInfo(var2, var6, var5, var7);
                  if(var3 != null) {
                     ArrayList var8 = new ArrayList();
                     X11.XVisualInfo[] var9 = (X11.XVisualInfo[])((X11.XVisualInfo[])var3.toArray(var7.getValue()));

                     int var10;
                     for(var10 = 0; var10 < var9.length; ++var10) {
                        X11.Xrender.Xrender$XRenderPictFormat var11 = X11.Xrender.INSTANCE.XRenderFindVisualFormat(var2, var9[var10].visual);
                        if(var11.type == 1 && var11.direct.alphaMask != 0) {
                           var8.add(var9[var10].visualid);
                        }
                     }

                     this.alphaVisualIDs = new long[var8.size()];

                     for(var10 = 0; var10 < this.alphaVisualIDs.length; ++var10) {
                        this.alphaVisualIDs[var10] = ((Number)var8.get(var10)).longValue();
                     }

                     long[] var15 = this.alphaVisualIDs;
                     return var15;
                  }
               } finally {
                  if(var3 != null) {
                     var1.XFree(var3.getPointer());
                  }

                  var1.XCloseDisplay(var2);
               }

               return this.alphaVisualIDs;
            }
         }
      }

      private static X11.Window getContentWindow(Window var0, X11.Display var1, X11.Window var2, Point var3) {
         if(var0 instanceof Frame && !((Frame)var0).isUndecorated() || var0 instanceof Dialog && !((Dialog)var0).isUndecorated()) {
            X11 var4 = X11.INSTANCE;
            X11.WindowByReference var5 = new X11.WindowByReference();
            X11.WindowByReference var6 = new X11.WindowByReference();
            PointerByReference var7 = new PointerByReference();
            IntByReference var8 = new IntByReference();
            var4.XQueryTree(var1, var2, var5, var6, var7, var8);
            Pointer var9 = var7.getValue();
            int[] var10 = var9.getIntArray(0L, var8.getValue());
            int var12 = var10.length;
            byte var13 = 0;
            if(var13 < var12) {
               int var14 = var10[var13];
               X11.Window var15 = new X11.Window((long)var14);
               X11.XWindowAttributes var16 = new X11.XWindowAttributes();
               var4.XGetWindowAttributes(var1, var15, var16);
               var3.x = -var16.x;
               var3.y = -var16.y;
               var2 = var15;
            }

            if(var9 != null) {
               var4.XFree(var9);
            }
         }

         return var2;
      }

      private static X11.Window getDrawable(Component var0) {
         int var1 = (int)Native.getComponentID(var0);
         return var1 == 0?null:new X11.Window((long)var1);
      }

      public void setWindowAlpha(final Window var1, final float var2) {
         if(!this.isWindowAlphaSupported()) {
            throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
         } else {
            Runnable var3 = new Runnable() {
               public void run() {
                  X11 var1x = X11.INSTANCE;
                  X11.Display var2x = var1x.XOpenDisplay((String)null);
                  if(var2x != null) {
                     try {
                        X11.Window var3 = WindowUtils.X11WindowUtils.getDrawable(var1);
                        if(var2 == 1.0F) {
                           var1x.XDeleteProperty(var2x, var3, var1x.XInternAtom(var2x, "_NET_WM_WINDOW_OPACITY", false));
                        } else {
                           int var4 = (int)((long)(var2 * 4.2949673E9F) & -1L);
                           IntByReference var5 = new IntByReference(var4);
                           var1x.XChangeProperty(var2x, var3, var1x.XInternAtom(var2x, "_NET_WM_WINDOW_OPACITY", false), X11.XA_CARDINAL, 32, 0, var5.getPointer(), 1);
                        }
                     } finally {
                        var1x.XCloseDisplay(var2x);
                     }

                  }
               }
            };
            this.whenDisplayable(var1, var3);
         }
      }

      public void setWindowTransparent(final Window var1, final boolean var2) {
         if(!(var1 instanceof RootPaneContainer)) {
            throw new IllegalArgumentException("Window must be a RootPaneContainer");
         } else if(!this.isWindowAlphaSupported()) {
            throw new UnsupportedOperationException("This X11 display does not provide a 32-bit visual");
         } else if(!var1.getGraphicsConfiguration().equals(this.getAlphaCompatibleGraphicsConfiguration())) {
            throw new IllegalArgumentException("Window GraphicsConfiguration \'" + var1.getGraphicsConfiguration() + "\' does not support transparency");
         } else {
            boolean var3 = var1.getBackground() != null && var1.getBackground().getAlpha() == 0;
            if(var2 != var3) {
               this.whenDisplayable(var1, new Runnable() {
                  public void run() {
                     JRootPane var1x = ((RootPaneContainer)var1).getRootPane();
                     JLayeredPane var2x = var1x.getLayeredPane();
                     Container var3 = var1x.getContentPane();
                     if(var3 instanceof WindowUtils.X11WindowUtils.X11WindowUtils$X11TransparentContentPane) {
                        ((WindowUtils.X11WindowUtils.X11WindowUtils$X11TransparentContentPane)var3).setTransparent(var2);
                     } else if(var2) {
                        WindowUtils.X11WindowUtils.X11WindowUtils$X11TransparentContentPane var4 = X11WindowUtils.this.new X11WindowUtils$X11TransparentContentPane(var3);
                        var1x.setContentPane(var4);
                        var2x.add(new WindowUtils.RepaintTrigger(var4), JLayeredPane.DRAG_LAYER);
                     }

                     X11WindowUtils.this.setLayersTransparent(var1, var2);
                     X11WindowUtils.this.setForceHeavyweightPopups(var1, var2);
                     X11WindowUtils.this.setDoubleBuffered(var1, !var2);
                  }
               });
            }
         }
      }

      private void setWindowShape(final Window var1, final WindowUtils.X11WindowUtils.X11WindowUtils$PixmapSource var2) {
         Runnable var3 = new Runnable() {
            public void run() {
               X11 var1x = X11.INSTANCE;
               X11.Display var2x = var1x.XOpenDisplay((String)null);
               if(var2x != null) {
                  X11.Pixmap var3 = null;

                  try {
                     X11.Window var4 = WindowUtils.X11WindowUtils.getDrawable(var1);
                     var3 = var2.getPixmap(var2x, var4);
                     X11.Xext var5 = X11.Xext.INSTANCE;
                     var5.XShapeCombineMask(var2x, var4, 0, 0, 0, var3 == null?X11.Pixmap.None:var3, 0);
                  } finally {
                     if(var3 != null) {
                        var1x.XFreePixmap(var2x, var3);
                     }

                     var1x.XCloseDisplay(var2x);
                  }

                  X11WindowUtils.this.setForceHeavyweightPopups(X11WindowUtils.this.getWindow(var1), var3 != null);
               }
            }
         };
         this.whenDisplayable(var1, var3);
      }

      protected void setMask(Component var1, final Raster var2) {
         this.setWindowShape(this.getWindow(var1), new WindowUtils.X11WindowUtils.X11WindowUtils$PixmapSource() {
            public X11.Pixmap getPixmap(X11.Display var1, X11.Window var2x) {
               return var2 != null?WindowUtils.X11WindowUtils.createBitmap(var1, var2x, var2):null;
            }
         });
      }

      // $FF: synthetic method
      X11WindowUtils(WindowUtils.SyntheticClass_1 var1) {
         this();
      }

      private interface X11WindowUtils$PixmapSource {
         X11.Pixmap getPixmap(X11.Display var1, X11.Window var2);
      }

      private class X11WindowUtils$X11TransparentContentPane extends WindowUtils.NativeWindowUtils.NativeWindowUtils$TransparentContentPane {
         private static final long serialVersionUID = 1L;
         private Memory buffer;
         private int[] pixels;
         private final int[] pixel = new int[4];

         public X11WindowUtils$X11TransparentContentPane(Container var2) {
            super();
         }

         protected void paintDirect(BufferedImage var1, Rectangle var2) {
            Window var3 = SwingUtilities.getWindowAncestor(this);
            X11 var4 = X11.INSTANCE;
            X11.Display var5 = var4.XOpenDisplay((String)null);
            X11.Window var6 = WindowUtils.X11WindowUtils.getDrawable(var3);
            Point var7 = new Point();
            var6 = WindowUtils.X11WindowUtils.getContentWindow(var3, var5, var6, var7);
            X11.GC var8 = var4.XCreateGC(var5, var6, new NativeLong(0L), (X11.XGCValues)null);
            Raster var9 = var1.getData();
            int var10 = var2.width;
            int var11 = var2.height;
            if(this.buffer == null || this.buffer.size() != (long)(var10 * var11 * 4)) {
               this.buffer = new Memory((long)(var10 * var11 * 4));
               this.pixels = new int[var10 * var11];
            }

            for(int var12 = 0; var12 < var11; ++var12) {
               for(int var13 = 0; var13 < var10; ++var13) {
                  var9.getPixel(var13, var12, this.pixel);
                  int var14 = this.pixel[3] & 255;
                  int var15 = this.pixel[2] & 255;
                  int var16 = this.pixel[1] & 255;
                  int var17 = this.pixel[0] & 255;
                  this.pixels[var12 * var10 + var13] = var14 << 24 | var17 << 16 | var16 << 8 | var15;
               }
            }

            X11.XWindowAttributes var18 = new X11.XWindowAttributes();
            var4.XGetWindowAttributes(var5, var6, var18);
            X11.XImage var19 = var4.XCreateImage(var5, var18.visual, 32, 2, 0, this.buffer, var10, var11, 32, var10 * 4);
            this.buffer.write(0L, (int[])this.pixels, 0, this.pixels.length);
            var7.x += var2.x;
            var7.y += var2.y;
            var4.XPutImage(var5, var6, var8, var19, 0, 0, var7.x, var7.y, var10, var11);
            var4.XFree(var19.getPointer());
            var4.XFreeGC(var5, var8);
            var4.XCloseDisplay(var5);
         }
      }
   }

   private static class MacWindowUtils extends WindowUtils.NativeWindowUtils {
      private static final String WDRAG = "apple.awt.draggableWindowBackground";

      private MacWindowUtils() {
      }

      public boolean isWindowAlphaSupported() {
         return true;
      }

      private WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane installMaskingPane(Window var1) {
         WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane var2;
         if(var1 instanceof RootPaneContainer) {
            RootPaneContainer var3 = (RootPaneContainer)var1;
            Container var4 = var3.getContentPane();
            if(var4 instanceof WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane) {
               var2 = (WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane)var4;
            } else {
               var2 = new WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane(var4);
               var3.setContentPane(var2);
            }
         } else {
            Component var5 = var1.getComponentCount() > 0?var1.getComponent(0):null;
            if(var5 instanceof WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane) {
               var2 = (WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane)var5;
            } else {
               var2 = new WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane(var5);
               var1.add(var2);
            }
         }

         return var2;
      }

      public void setWindowTransparent(Window var1, boolean var2) {
         boolean var3 = var1.getBackground() != null && var1.getBackground().getAlpha() == 0;
         if(var2 != var3) {
            this.setBackgroundTransparent(var1, var2, "setWindowTransparent");
         }

      }

      private void fixWindowDragging(Window var1, String var2) {
         if(var1 instanceof RootPaneContainer) {
            JRootPane var3 = ((RootPaneContainer)var1).getRootPane();
            Boolean var4 = (Boolean)var3.getClientProperty("apple.awt.draggableWindowBackground");
            if(var4 == null) {
               var3.putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
               if(var1.isDisplayable()) {
                  System.err.println(var2 + "(): To avoid content dragging, " + var2 + "() must be called before the window is realized, or " + "apple.awt.draggableWindowBackground" + " must be set to Boolean.FALSE before the window is realized.  If you really want content dragging, set " + "apple.awt.draggableWindowBackground" + " on the window\'s root pane to Boolean.TRUE before calling " + var2 + "() to hide this message.");
               }
            }
         }

      }

      public void setWindowAlpha(final Window var1, final float var2) {
         if(var1 instanceof RootPaneContainer) {
            JRootPane var3 = ((RootPaneContainer)var1).getRootPane();
            var3.putClientProperty("Window.alpha", new Float(var2));
            this.fixWindowDragging(var1, "setWindowAlpha");
         }

         this.whenDisplayable(var1, new Runnable() {
            public void run() {
               ComponentPeer var1x = var1.getPeer();

               try {
                  var1x.getClass().getMethod("setAlpha", new Class[]{Float.TYPE}).invoke(var1x, new Object[]{new Float(var2)});
               } catch (Exception var3) {
                  ;
               }

            }
         });
      }

      protected void setWindowMask(Component var1, Raster var2) {
         if(var2 != null) {
            this.setWindowMask(var1, this.toShape(var2));
         } else {
            this.setWindowMask(var1, (Shape)(new Rectangle(0, 0, var1.getWidth(), var1.getHeight())));
         }

      }

      public void setWindowMask(Component var1, Shape var2) {
         if(var1 instanceof Window) {
            Window var3 = (Window)var1;
            WindowUtils.MacWindowUtils.MacWindowUtils$OSXMaskingContentPane var4 = this.installMaskingPane(var3);
            var4.setMask(var2);
            this.setBackgroundTransparent(var3, var2 != WindowUtils.MASK_NONE, "setWindowMask");
         }

      }

      private void setBackgroundTransparent(Window var1, boolean var2, String var3) {
         JRootPane var4 = var1 instanceof RootPaneContainer?((RootPaneContainer)var1).getRootPane():null;
         if(var2) {
            if(var4 != null) {
               var4.putClientProperty("transparent-old-bg", var1.getBackground());
            }

            var1.setBackground(new Color(0, 0, 0, 0));
         } else if(var4 != null) {
            Color var5 = (Color)var4.getClientProperty("transparent-old-bg");
            if(var5 != null) {
               var5 = new Color(var5.getRed(), var5.getGreen(), var5.getBlue(), var5.getAlpha());
            }

            var1.setBackground(var5);
            var4.putClientProperty("transparent-old-bg", (Object)null);
         } else {
            var1.setBackground((Color)null);
         }

         this.fixWindowDragging(var1, var3);
      }

      // $FF: synthetic method
      MacWindowUtils(WindowUtils.SyntheticClass_1 var1) {
         this();
      }

      private static class MacWindowUtils$OSXMaskingContentPane extends JPanel {
         private static final long serialVersionUID = 1L;
         private Shape shape;

         public MacWindowUtils$OSXMaskingContentPane(Component var1) {
            super(new BorderLayout());
            if(var1 != null) {
               this.add(var1, "Center");
            }

         }

         public void setMask(Shape var1) {
            this.shape = var1;
            this.repaint();
         }

         public void paint(Graphics var1) {
            Graphics2D var2 = (Graphics2D)var1.create();
            var2.setComposite(AlphaComposite.Clear);
            var2.fillRect(0, 0, this.getWidth(), this.getHeight());
            var2.dispose();
            if(this.shape != null) {
               var2 = (Graphics2D)var1.create();
               var2.setClip(this.shape);
               super.paint(var2);
               var2.dispose();
            } else {
               super.paint(var1);
            }

         }
      }
   }

   private static class W32WindowUtils extends WindowUtils.NativeWindowUtils {
      private W32WindowUtils() {
      }

      private WinDef.HWND getHWnd(Component var1) {
         WinDef.HWND var2 = new WinDef.HWND();
         var2.setPointer(Native.getComponentPointer(var1));
         return var2;
      }

      public boolean isWindowAlphaSupported() {
         return Boolean.getBoolean("sun.java2d.noddraw");
      }

      private boolean usingUpdateLayeredWindow(Window var1) {
         if(var1 instanceof RootPaneContainer) {
            JRootPane var2 = ((RootPaneContainer)var1).getRootPane();
            return var2.getClientProperty("transparent-old-bg") != null;
         } else {
            return false;
         }
      }

      private void storeAlpha(Window var1, byte var2) {
         if(var1 instanceof RootPaneContainer) {
            JRootPane var3 = ((RootPaneContainer)var1).getRootPane();
            Byte var4 = var2 == -1?null:new Byte(var2);
            var3.putClientProperty("transparent-alpha", var4);
         }

      }

      private byte getAlpha(Window var1) {
         if(var1 instanceof RootPaneContainer) {
            JRootPane var2 = ((RootPaneContainer)var1).getRootPane();
            Byte var3 = (Byte)var2.getClientProperty("transparent-alpha");
            if(var3 != null) {
               return var3.byteValue();
            }
         }

         return (byte)-1;
      }

      public void setWindowAlpha(final Window var1, final float var2) {
         if(!this.isWindowAlphaSupported()) {
            throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
         } else {
            this.whenDisplayable(var1, new Runnable() {
               public void run() {
                  WinDef.HWND var1x = W32WindowUtils.this.getHWnd(var1);
                  User32 var2x = User32.INSTANCE;
                  int var3 = var2x.GetWindowLong(var1x, -20);
                  byte var4 = (byte)((int)(255.0F * var2) & 255);
                  if(W32WindowUtils.this.usingUpdateLayeredWindow(var1)) {
                     WinUser.BLENDFUNCTION var5 = new WinUser.BLENDFUNCTION();
                     var5.SourceConstantAlpha = var4;
                     var5.AlphaFormat = 1;
                     var2x.UpdateLayeredWindow(var1x, (WinDef.HDC)null, (WinUser.POINT)null, (WinUser.SIZE)null, (WinDef.HDC)null, (WinUser.POINT)null, 0, var5, 2);
                  } else if(var2 == 1.0F) {
                     var3 &= -524289;
                     var2x.SetWindowLong(var1x, -20, var3);
                  } else {
                     var3 |= 524288;
                     var2x.SetWindowLong(var1x, -20, var3);
                     var2x.SetLayeredWindowAttributes(var1x, 0, var4, 2);
                  }

                  W32WindowUtils.this.setForceHeavyweightPopups(var1, var2 != 1.0F);
                  W32WindowUtils.this.storeAlpha(var1, var4);
               }
            });
         }
      }

      public void setWindowTransparent(final Window var1, final boolean var2) {
         if(!(var1 instanceof RootPaneContainer)) {
            throw new IllegalArgumentException("Window must be a RootPaneContainer");
         } else if(!this.isWindowAlphaSupported()) {
            throw new UnsupportedOperationException("Set sun.java2d.noddraw=true to enable transparent windows");
         } else {
            boolean var3 = var1.getBackground() != null && var1.getBackground().getAlpha() == 0;
            if(var2 != var3) {
               this.whenDisplayable(var1, new Runnable() {
                  public void run() {
                     User32 var1x = User32.INSTANCE;
                     WinDef.HWND var2x = W32WindowUtils.this.getHWnd(var1);
                     int var3 = var1x.GetWindowLong(var2x, -20);
                     JRootPane var4 = ((RootPaneContainer)var1).getRootPane();
                     JLayeredPane var5 = var4.getLayeredPane();
                     Container var6 = var4.getContentPane();
                     if(var6 instanceof WindowUtils.W32WindowUtils.W32WindowUtils$W32TransparentContentPane) {
                        ((WindowUtils.W32WindowUtils.W32WindowUtils$W32TransparentContentPane)var6).setTransparent(var2);
                     } else if(var2) {
                        WindowUtils.W32WindowUtils.W32WindowUtils$W32TransparentContentPane var7 = W32WindowUtils.this.new W32WindowUtils$W32TransparentContentPane(var6);
                        var4.setContentPane(var7);
                        var5.add(new WindowUtils.RepaintTrigger(var7), JLayeredPane.DRAG_LAYER);
                     }

                     if(var2 && !W32WindowUtils.this.usingUpdateLayeredWindow(var1)) {
                        var3 |= 524288;
                        var1x.SetWindowLong(var2x, -20, var3);
                     } else if(!var2 && W32WindowUtils.this.usingUpdateLayeredWindow(var1)) {
                        var3 &= -524289;
                        var1x.SetWindowLong(var2x, -20, var3);
                     }

                     W32WindowUtils.this.setLayersTransparent(var1, var2);
                     W32WindowUtils.this.setForceHeavyweightPopups(var1, var2);
                     W32WindowUtils.this.setDoubleBuffered(var1, !var2);
                  }
               });
            }
         }
      }

      public void setWindowMask(Component var1, Shape var2) {
         if(var2 instanceof Area && ((Area)var2).isPolygonal()) {
            this.setMask(var1, (Area)var2);
         } else {
            super.setWindowMask(var1, var2);
         }

      }

      private void setWindowRegion(final Component var1, final WinDef.HRGN var2) {
         this.whenDisplayable(var1, new Runnable() {
            public void run() {
               GDI32 var1x = GDI32.INSTANCE;
               User32 var2x = User32.INSTANCE;
               WinDef.HWND var3 = W32WindowUtils.this.getHWnd(var1);

               try {
                  var2x.SetWindowRgn(var3, var2, true);
                  W32WindowUtils.this.setForceHeavyweightPopups(W32WindowUtils.this.getWindow(var1), var2 != null);
               } finally {
                  var1x.DeleteObject(var2);
               }

            }
         });
      }

      private void setMask(Component var1, Area var2) {
         GDI32 var3 = GDI32.INSTANCE;
         PathIterator var4 = var2.getPathIterator((AffineTransform)null);
         int var5 = var4.getWindingRule() == 1?2:1;
         float[] var6 = new float[6];
         ArrayList var7 = new ArrayList();
         int var8 = 0;

         ArrayList var9;
         for(var9 = new ArrayList(); !var4.isDone(); var4.next()) {
            int var10 = var4.currentSegment(var6);
            if(var10 == 0) {
               var8 = 1;
               var7.add(new WinUser.POINT((int)var6[0], (int)var6[1]));
            } else if(var10 == 1) {
               ++var8;
               var7.add(new WinUser.POINT((int)var6[0], (int)var6[1]));
            } else {
               if(var10 != 4) {
                  throw new RuntimeException("Area is not polygonal: " + var2);
               }

               var9.add(new Integer(var8));
            }
         }

         WinUser.POINT[] var14 = (WinUser.POINT[])((WinUser.POINT[])(new WinUser.POINT()).toArray(var7.size()));
         WinUser.POINT[] var11 = (WinUser.POINT[])var7.toArray(new WinUser.POINT[var7.size()]);

         for(int var12 = 0; var12 < var14.length; ++var12) {
            var14[var12].x = var11[var12].x;
            var14[var12].y = var11[var12].y;
         }

         int[] var15 = new int[var9.size()];

         for(int var13 = 0; var13 < var15.length; ++var13) {
            var15[var13] = ((Integer)var9.get(var13)).intValue();
         }

         WinDef.HRGN var16 = var3.CreatePolyPolygonRgn(var14, var15, var15.length, var5);
         this.setWindowRegion(var1, var16);
      }

      protected void setMask(Component var1, Raster var2) {
         GDI32 var3 = GDI32.INSTANCE;
         final WinDef.HRGN var4 = var2 != null?var3.CreateRectRgn(0, 0, 0, 0):null;
         if(var4 != null) {
            final WinDef.HRGN var5 = var3.CreateRectRgn(0, 0, 0, 0);

            try {
               RasterRangesUtils.outputOccupiedRanges(var2, new RasterRangesUtils.RangesOutput() {
                  public boolean outputRange(int var1, int var2, int var3, int var4x) {
                     GDI32 var5x = GDI32.INSTANCE;
                     var5x.SetRectRgn(var5, var1, var2, var1 + var3, var2 + var4x);
                     return var5x.CombineRgn(var4, var4, var5, 2) != 0;
                  }
               });
            } finally {
               var3.DeleteObject(var5);
            }
         }

         this.setWindowRegion(var1, var4);
      }

      // $FF: synthetic method
      W32WindowUtils(WindowUtils.SyntheticClass_1 var1) {
         this();
      }

      private class W32WindowUtils$W32TransparentContentPane extends WindowUtils.NativeWindowUtils.NativeWindowUtils$TransparentContentPane {
         private static final long serialVersionUID = 1L;
         private WinDef.HDC memDC;
         private WinDef.HBITMAP hBitmap;
         private Pointer pbits;
         private Dimension bitmapSize;

         public W32WindowUtils$W32TransparentContentPane(Container var2) {
            super();
         }

         private void disposeBackingStore() {
            GDI32 var1 = GDI32.INSTANCE;
            if(this.hBitmap != null) {
               var1.DeleteObject(this.hBitmap);
               this.hBitmap = null;
            }

            if(this.memDC != null) {
               var1.DeleteDC(this.memDC);
               this.memDC = null;
            }

         }

         public void removeNotify() {
            super.removeNotify();
            this.disposeBackingStore();
         }

         public void setTransparent(boolean var1) {
            super.setTransparent(var1);
            if(!var1) {
               this.disposeBackingStore();
            }

         }

         protected void paintDirect(BufferedImage var1, Rectangle var2) {
            Window var3 = SwingUtilities.getWindowAncestor(this);
            GDI32 var4 = GDI32.INSTANCE;
            User32 var5 = User32.INSTANCE;
            int var6 = var2.x;
            int var7 = var2.y;
            Point var8 = SwingUtilities.convertPoint(this, var6, var7, var3);
            int var9 = var2.width;
            int var10 = var2.height;
            int var11 = var3.getWidth();
            int var12 = var3.getHeight();
            WinDef.HDC var13 = var5.GetDC((WinDef.HWND)null);
            WinNT.HANDLE var14 = null;

            try {
               if(this.memDC == null) {
                  this.memDC = var4.CreateCompatibleDC(var13);
               }

               if(this.hBitmap == null || !var3.getSize().equals(this.bitmapSize)) {
                  if(this.hBitmap != null) {
                     var4.DeleteObject(this.hBitmap);
                     this.hBitmap = null;
                  }

                  WinGDI.BITMAPINFO var15 = new WinGDI.BITMAPINFO();
                  var15.bmiHeader.biWidth = var11;
                  var15.bmiHeader.biHeight = var12;
                  var15.bmiHeader.biPlanes = 1;
                  var15.bmiHeader.biBitCount = 32;
                  var15.bmiHeader.biCompression = 0;
                  var15.bmiHeader.biSizeImage = var11 * var12 * 4;
                  PointerByReference var16 = new PointerByReference();
                  this.hBitmap = var4.CreateDIBSection(this.memDC, var15, 0, var16, (Pointer)null, 0);
                  this.pbits = var16.getValue();
                  this.bitmapSize = new Dimension(var11, var12);
               }

               var14 = var4.SelectObject(this.memDC, this.hBitmap);
               Raster var32 = var1.getData();
               int[] var33 = new int[4];
               int[] var17 = new int[var9];

               for(int var18 = 0; var18 < var10; ++var18) {
                  int var19;
                  for(var19 = 0; var19 < var9; ++var19) {
                     var32.getPixel(var19, var18, var33);
                     int var20 = (var33[3] & 255) << 24;
                     int var21 = var33[2] & 255;
                     int var22 = (var33[1] & 255) << 8;
                     int var23 = (var33[0] & 255) << 16;
                     var17[var19] = var20 | var21 | var22 | var23;
                  }

                  var19 = var12 - (var8.y + var18) - 1;
                  this.pbits.write((long)((var19 * var11 + var8.x) * 4), (int[])var17, 0, var17.length);
               }

               WinUser.SIZE var34 = new WinUser.SIZE();
               var34.cx = var3.getWidth();
               var34.cy = var3.getHeight();
               WinUser.POINT var35 = new WinUser.POINT();
               var35.x = var3.getX();
               var35.y = var3.getY();
               WinUser.POINT var36 = new WinUser.POINT();
               WinUser.BLENDFUNCTION var37 = new WinUser.BLENDFUNCTION();
               WinDef.HWND var38 = W32WindowUtils.this.getHWnd(var3);
               ByteByReference var39 = new ByteByReference();
               IntByReference var24 = new IntByReference();
               byte var25 = W32WindowUtils.this.getAlpha(var3);

               try {
                  if(var5.GetLayeredWindowAttributes(var38, (IntByReference)null, var39, var24) && (var24.getValue() & 2) != 0) {
                     var25 = var39.getValue();
                  }
               } catch (UnsatisfiedLinkError var30) {
                  ;
               }

               var37.SourceConstantAlpha = var25;
               var37.AlphaFormat = 1;
               var5.UpdateLayeredWindow(var38, var13, var35, var34, this.memDC, var36, 0, var37, 2);
            } finally {
               var5.ReleaseDC((WinDef.HWND)null, var13);
               if(this.memDC != null && var14 != null) {
                  var4.SelectObject(this.memDC, var14);
               }

            }
         }
      }
   }

   private static class Holder {
      public static boolean requiresVisible;
      public static final WindowUtils.NativeWindowUtils INSTANCE;

      private Holder() {
      }

      static {
         if(Platform.isWindows()) {
            INSTANCE = new WindowUtils.W32WindowUtils();
         } else if(Platform.isMac()) {
            INSTANCE = new WindowUtils.MacWindowUtils();
         } else {
            if(!Platform.isX11()) {
               String var0 = System.getProperty("os.name");
               throw new UnsupportedOperationException("No support for " + var0);
            }

            INSTANCE = new WindowUtils.X11WindowUtils();
            requiresVisible = System.getProperty("java.version").matches("^1\\.4\\..*");
         }

      }
   }

   public abstract static class NativeWindowUtils {
      public NativeWindowUtils() {
      }

      protected Window getWindow(Component var1) {
         return var1 instanceof Window?(Window)var1:SwingUtilities.getWindowAncestor(var1);
      }

      protected void whenDisplayable(Component var1, final Runnable var2) {
         if(!var1.isDisplayable() || WindowUtils.Holder.requiresVisible && !var1.isVisible()) {
            if(WindowUtils.Holder.requiresVisible) {
               this.getWindow(var1).addWindowListener(new WindowAdapter() {
                  public void windowOpened(WindowEvent var1) {
                     var1.getWindow().removeWindowListener(this);
                     var2.run();
                  }

                  public void windowClosed(WindowEvent var1) {
                     var1.getWindow().removeWindowListener(this);
                  }
               });
            } else {
               var1.addHierarchyListener(new HierarchyListener() {
                  public void hierarchyChanged(HierarchyEvent var1) {
                     if((var1.getChangeFlags() & 2L) != 0L && var1.getComponent().isDisplayable()) {
                        var1.getComponent().removeHierarchyListener(this);
                        var2.run();
                     }

                  }
               });
            }
         } else {
            var2.run();
         }

      }

      protected Raster toRaster(Shape var1) {
         WritableRaster var2 = null;
         if(var1 != WindowUtils.MASK_NONE) {
            Rectangle var3 = var1.getBounds();
            if(var3.width > 0 && var3.height > 0) {
               BufferedImage var4 = new BufferedImage(var3.x + var3.width, var3.y + var3.height, 12);
               Graphics2D var5 = var4.createGraphics();
               var5.setColor(Color.black);
               var5.fillRect(0, 0, var3.x + var3.width, var3.y + var3.height);
               var5.setColor(Color.white);
               var5.fill(var1);
               var2 = var4.getRaster();
            }
         }

         return var2;
      }

      protected Raster toRaster(Component var1, Icon var2) {
         WritableRaster var3 = null;
         if(var2 != null) {
            Rectangle var4 = new Rectangle(0, 0, var2.getIconWidth(), var2.getIconHeight());
            BufferedImage var5 = new BufferedImage(var4.width, var4.height, 2);
            Graphics2D var6 = var5.createGraphics();
            var6.setComposite(AlphaComposite.Clear);
            var6.fillRect(0, 0, var4.width, var4.height);
            var6.setComposite(AlphaComposite.SrcOver);
            var2.paintIcon(var1, var6, 0, 0);
            var3 = var5.getAlphaRaster();
         }

         return var3;
      }

      protected Shape toShape(Raster var1) {
         final Area var2 = new Area(new Rectangle(0, 0, 0, 0));
         RasterRangesUtils.outputOccupiedRanges(var1, new RasterRangesUtils.RangesOutput() {
            public boolean outputRange(int var1, int var2x, int var3, int var4) {
               var2.add(new Area(new Rectangle(var1, var2x, var3, var4)));
               return true;
            }
         });
         return var2;
      }

      public void setWindowAlpha(Window var1, float var2) {
      }

      public boolean isWindowAlphaSupported() {
         return false;
      }

      public GraphicsConfiguration getAlphaCompatibleGraphicsConfiguration() {
         GraphicsEnvironment var1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
         GraphicsDevice var2 = var1.getDefaultScreenDevice();
         return var2.getDefaultConfiguration();
      }

      public void setWindowTransparent(Window var1, boolean var2) {
      }

      protected void setDoubleBuffered(Component var1, boolean var2) {
         if(var1 instanceof JComponent) {
            ((JComponent)var1).setDoubleBuffered(var2);
         }

         if(var1 instanceof JRootPane && var2) {
            ((JRootPane)var1).setDoubleBuffered(true);
         } else if(var1 instanceof Container) {
            Component[] var3 = ((Container)var1).getComponents();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.setDoubleBuffered(var3[var4], var2);
            }
         }

      }

      protected void setLayersTransparent(Window var1, boolean var2) {
         Color var3 = var2?new Color(0, 0, 0, 0):null;
         if(var1 instanceof RootPaneContainer) {
            RootPaneContainer var4 = (RootPaneContainer)var1;
            JRootPane var5 = var4.getRootPane();
            JLayeredPane var6 = var5.getLayeredPane();
            Container var7 = var5.getContentPane();
            JComponent var8 = var7 instanceof JComponent?(JComponent)var7:null;
            if(var2) {
               var6.putClientProperty("transparent-old-opaque", Boolean.valueOf(var6.isOpaque()));
               var6.setOpaque(false);
               var5.putClientProperty("transparent-old-opaque", Boolean.valueOf(var5.isOpaque()));
               var5.setOpaque(false);
               if(var8 != null) {
                  var8.putClientProperty("transparent-old-opaque", Boolean.valueOf(var8.isOpaque()));
                  var8.setOpaque(false);
               }

               var5.putClientProperty("transparent-old-bg", var5.getParent().getBackground());
            } else {
               var6.setOpaque(Boolean.TRUE.equals(var6.getClientProperty("transparent-old-opaque")));
               var6.putClientProperty("transparent-old-opaque", (Object)null);
               var5.setOpaque(Boolean.TRUE.equals(var5.getClientProperty("transparent-old-opaque")));
               var5.putClientProperty("transparent-old-opaque", (Object)null);
               if(var8 != null) {
                  var8.setOpaque(Boolean.TRUE.equals(var8.getClientProperty("transparent-old-opaque")));
                  var8.putClientProperty("transparent-old-opaque", (Object)null);
               }

               var3 = (Color)var5.getClientProperty("transparent-old-bg");
               var5.putClientProperty("transparent-old-bg", (Object)null);
            }
         }

         var1.setBackground(var3);
      }

      protected void setMask(Component var1, Raster var2) {
         throw new UnsupportedOperationException("Window masking is not available");
      }

      protected void setWindowMask(Component var1, Raster var2) {
         if(var1.isLightweight()) {
            throw new IllegalArgumentException("Component must be heavyweight: " + var1);
         } else {
            this.setMask(var1, var2);
         }
      }

      public void setWindowMask(Component var1, Shape var2) {
         this.setWindowMask(var1, this.toRaster(var2));
      }

      public void setWindowMask(Component var1, Icon var2) {
         this.setWindowMask(var1, this.toRaster(var1, var2));
      }

      protected void setForceHeavyweightPopups(Window var1, boolean var2) {
         if(!(var1 instanceof WindowUtils.HeavyweightForcer)) {
            Window[] var3 = var1.getOwnedWindows();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               if(var3[var4] instanceof WindowUtils.HeavyweightForcer) {
                  if(var2) {
                     return;
                  }

                  var3[var4].dispose();
               }
            }

            Boolean var5 = Boolean.valueOf(System.getProperty("jna.force_hw_popups", "true"));
            if(var2 && var5.booleanValue()) {
               new WindowUtils.HeavyweightForcer(var1);
            }
         }

      }

      protected abstract class NativeWindowUtils$TransparentContentPane extends JPanel implements AWTEventListener {
         private static final long serialVersionUID = 1L;
         private boolean transparent;

         public NativeWindowUtils$TransparentContentPane(Container var2) {
            super(new BorderLayout());
            this.add(var2, "Center");
            this.setTransparent(true);
            if(var2 instanceof JPanel) {
               ((JComponent)var2).setOpaque(false);
            }

         }

         public void addNotify() {
            super.addNotify();
            Toolkit.getDefaultToolkit().addAWTEventListener(this, 2L);
         }

         public void removeNotify() {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            super.removeNotify();
         }

         public void setTransparent(boolean var1) {
            this.transparent = var1;
            this.setOpaque(!var1);
            this.setDoubleBuffered(!var1);
            this.repaint();
         }

         public void eventDispatched(AWTEvent var1) {
            if(var1.getID() == 300 && SwingUtilities.isDescendingFrom(((ContainerEvent)var1).getChild(), this)) {
               Component var2 = ((ContainerEvent)var1).getChild();
               NativeWindowUtils.this.setDoubleBuffered(var2, false);
            }

         }

         public void paint(Graphics var1) {
            if(this.transparent) {
               Rectangle var2 = var1.getClipBounds();
               int var3 = var2.width;
               int var4 = var2.height;
               if(this.getWidth() > 0 && this.getHeight() > 0) {
                  BufferedImage var5 = new BufferedImage(var3, var4, 3);
                  Graphics2D var6 = var5.createGraphics();
                  var6.setComposite(AlphaComposite.Clear);
                  var6.fillRect(0, 0, var3, var4);
                  var6.dispose();
                  var6 = var5.createGraphics();
                  var6.translate(-var2.x, -var2.y);
                  super.paint(var6);
                  var6.dispose();
                  this.paintDirect(var5, var2);
               }
            } else {
               super.paint(var1);
            }

         }

         protected abstract void paintDirect(BufferedImage var1, Rectangle var2);
      }
   }

   protected static class RepaintTrigger extends JComponent {
      private static final long serialVersionUID = 1L;
      private final WindowUtils.RepaintTrigger.RepaintTrigger$Listener listener = this.createListener();
      private final JComponent content;
      private Rectangle dirty;

      public RepaintTrigger(JComponent var1) {
         this.content = var1;
      }

      public void addNotify() {
         super.addNotify();
         Window var1 = SwingUtilities.getWindowAncestor(this);
         this.setSize(this.getParent().getSize());
         var1.addComponentListener(this.listener);
         var1.addWindowListener(this.listener);
         Toolkit.getDefaultToolkit().addAWTEventListener(this.listener, 48L);
      }

      public void removeNotify() {
         Toolkit.getDefaultToolkit().removeAWTEventListener(this.listener);
         Window var1 = SwingUtilities.getWindowAncestor(this);
         var1.removeComponentListener(this.listener);
         var1.removeWindowListener(this.listener);
         super.removeNotify();
      }

      protected void paintComponent(Graphics var1) {
         Rectangle var2 = var1.getClipBounds();
         if(this.dirty != null && this.dirty.contains(var2)) {
            this.dirty = null;
         } else {
            if(this.dirty == null) {
               this.dirty = var2;
            } else {
               this.dirty = this.dirty.union(var2);
            }

            this.content.repaint(this.dirty);
         }

      }

      protected WindowUtils.RepaintTrigger.RepaintTrigger$Listener createListener() {
         return new WindowUtils.RepaintTrigger.RepaintTrigger$Listener();
      }

      protected class RepaintTrigger$Listener extends WindowAdapter implements ComponentListener, HierarchyListener, AWTEventListener {
         protected RepaintTrigger$Listener() {
         }

         public void windowOpened(WindowEvent var1) {
            RepaintTrigger.this.repaint();
         }

         public void componentHidden(ComponentEvent var1) {
         }

         public void componentMoved(ComponentEvent var1) {
         }

         public void componentResized(ComponentEvent var1) {
            RepaintTrigger.this.setSize(RepaintTrigger.this.getParent().getSize());
            RepaintTrigger.this.repaint();
         }

         public void componentShown(ComponentEvent var1) {
            RepaintTrigger.this.repaint();
         }

         public void hierarchyChanged(HierarchyEvent var1) {
            RepaintTrigger.this.repaint();
         }

         public void eventDispatched(AWTEvent var1) {
            if(var1 instanceof MouseEvent) {
               Component var2 = ((MouseEvent)var1).getComponent();
               if(var2 != null && SwingUtilities.isDescendingFrom(var2, RepaintTrigger.this.content)) {
                  MouseEvent var3 = SwingUtilities.convertMouseEvent(var2, (MouseEvent)var1, RepaintTrigger.this.content);
                  Component var4 = SwingUtilities.getDeepestComponentAt(RepaintTrigger.this.content, var3.getX(), var3.getY());
                  if(var4 != null) {
                     RepaintTrigger.this.setCursor(var4.getCursor());
                  }
               }
            }

         }
      }
   }

   private static class HeavyweightForcer extends Window {
      private static final long serialVersionUID = 1L;
      private final boolean packed;

      public HeavyweightForcer(Window var1) {
         super(var1);
         this.pack();
         this.packed = true;
      }

      public boolean isVisible() {
         return this.packed;
      }

      public Rectangle getBounds() {
         return this.getOwner().getBounds();
      }
   }
}
