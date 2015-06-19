package com.sun.jna.platform;

import com.sun.jna.platform.win32.W32FileMonitor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class FileMonitor {
   public static final int FILE_CREATED = 1;
   public static final int FILE_DELETED = 2;
   public static final int FILE_MODIFIED = 4;
   public static final int FILE_ACCESSED = 8;
   public static final int FILE_NAME_CHANGED_OLD = 16;
   public static final int FILE_NAME_CHANGED_NEW = 32;
   public static final int FILE_RENAMED = 48;
   public static final int FILE_SIZE_CHANGED = 64;
   public static final int FILE_ATTRIBUTES_CHANGED = 128;
   public static final int FILE_SECURITY_CHANGED = 256;
   public static final int FILE_ANY = 511;
   private final Map<File, Integer> watched = new HashMap();
   private List<FileMonitor.FileListener> listeners = new ArrayList();

   public FileMonitor() {
   }

   protected abstract void watch(File var1, int var2, boolean var3) throws IOException;

   protected abstract void unwatch(File var1);

   public abstract void dispose();

   public void addWatch(File var1) throws IOException {
      this.addWatch(var1, 511);
   }

   public void addWatch(File var1, int var2) throws IOException {
      this.addWatch(var1, var2, var1.isDirectory());
   }

   public void addWatch(File var1, int var2, boolean var3) throws IOException {
      this.watched.put(var1, new Integer(var2));
      this.watch(var1, var2, var3);
   }

   public void removeWatch(File var1) {
      if(this.watched.remove(var1) != null) {
         this.unwatch(var1);
      }

   }

   protected void notify(FileMonitor.FileEvent var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         FileMonitor.FileListener var3 = (FileMonitor.FileListener)var2.next();
         var3.fileChanged(var1);
      }

   }

   public synchronized void addFileListener(FileMonitor.FileListener var1) {
      ArrayList var2 = new ArrayList(this.listeners);
      var2.add(var1);
      this.listeners = var2;
   }

   public synchronized void removeFileListener(FileMonitor.FileListener var1) {
      ArrayList var2 = new ArrayList(this.listeners);
      var2.remove(var1);
      this.listeners = var2;
   }

   protected void finalize() {
      Iterator var1 = this.watched.keySet().iterator();

      while(var1.hasNext()) {
         File var2 = (File)var1.next();
         this.removeWatch(var2);
      }

      this.dispose();
   }

   public static FileMonitor getInstance() {
      return FileMonitor.Holder.INSTANCE;
   }

   private static class Holder {
      public static final FileMonitor INSTANCE;

      private Holder() {
      }

      static {
         String var0 = System.getProperty("os.name");
         if(var0.startsWith("Windows")) {
            INSTANCE = new W32FileMonitor();
         } else {
            throw new Error("FileMonitor not implemented for " + var0);
         }
      }
   }

   public class FileEvent extends EventObject {
      private final File file;
      private final int type;

      public FileEvent(File var2, int var3) {
         super(FileMonitor.this);
         this.file = var2;
         this.type = var3;
      }

      public File getFile() {
         return this.file;
      }

      public int getType() {
         return this.type;
      }

      public String toString() {
         return "FileEvent: " + this.file + ":" + this.type;
      }
   }

   public interface FileListener {
      void fileChanged(FileMonitor.FileEvent var1);
   }
}
