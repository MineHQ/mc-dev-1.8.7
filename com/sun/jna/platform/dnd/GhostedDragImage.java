package com.sun.jna.platform.dnd;

import com.sun.jna.platform.WindowUtils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GhostedDragImage {
   private static final float DEFAULT_ALPHA = 0.5F;
   private Window dragImage;
   private Point origin;
   private static final int SLIDE_INTERVAL = 33;

   public GhostedDragImage(Component var1, final Icon var2, Point var3, final Point var4) {
      Window var5 = var1 instanceof Window?(Window)var1:SwingUtilities.getWindowAncestor(var1);
      final GraphicsConfiguration var6 = var5.getGraphicsConfiguration();
      this.dragImage = new Window(JOptionPane.getRootFrame(), var6) {
         private static final long serialVersionUID = 1L;

         public void paint(Graphics var1) {
            var2.paintIcon(this, var1, 0, 0);
         }

         public Dimension getPreferredSize() {
            return new Dimension(var2.getIconWidth(), var2.getIconHeight());
         }

         public Dimension getMinimumSize() {
            return this.getPreferredSize();
         }

         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      };
      this.dragImage.setFocusableWindowState(false);
      this.dragImage.setName("###overrideRedirect###");
      Icon var7 = new Icon() {
         public int getIconHeight() {
            return var2.getIconHeight();
         }

         public int getIconWidth() {
            return var2.getIconWidth();
         }

         public void paintIcon(Component var1, Graphics var2x, int var3, int var4x) {
            var2x = var2x.create();
            Area var5 = new Area(new Rectangle(var3, var4x, this.getIconWidth(), this.getIconHeight()));
            var5.subtract(new Area(new Rectangle(var3 + var4.x - 1, var4x + var4.y - 1, 3, 3)));
            var2x.setClip(var5);
            var2.paintIcon(var1, var2x, var3, var4x);
            var2x.dispose();
         }
      };
      this.dragImage.pack();
      WindowUtils.setWindowMask(this.dragImage, var7);
      WindowUtils.setWindowAlpha(this.dragImage, 0.5F);
      this.move(var3);
      this.dragImage.setVisible(true);
   }

   public void setAlpha(float var1) {
      WindowUtils.setWindowAlpha(this.dragImage, var1);
   }

   public void dispose() {
      this.dragImage.dispose();
      this.dragImage = null;
   }

   public void move(Point var1) {
      if(this.origin == null) {
         this.origin = var1;
      }

      this.dragImage.setLocation(var1.x, var1.y);
   }

   public void returnToOrigin() {
      final Timer var1 = new Timer(33, (ActionListener)null);
      var1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1x) {
            Point var2 = GhostedDragImage.this.dragImage.getLocationOnScreen();
            Point var3 = new Point(GhostedDragImage.this.origin);
            int var4 = (var3.x - var2.x) / 2;
            int var5 = (var3.y - var2.y) / 2;
            if(var4 == 0 && var5 == 0) {
               var1.stop();
               GhostedDragImage.this.dispose();
            } else {
               var2.translate(var4, var5);
               GhostedDragImage.this.move(var2);
            }

         }
      });
      var1.setInitialDelay(0);
      var1.start();
   }
}
